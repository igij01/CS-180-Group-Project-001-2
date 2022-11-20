# Update Log
__This update log is used to record the updates that happened to the project since project 4__

## 11/16/2022
### Removed linker - Yulin Lin
#### Description
The linker for linking conversations and messages are removed and replaced by serializing all public information arrays
into a single serialized file. 
#### Notes
* The reason that linker is not necessary when serializing to a single file is due to java serialization mechanisms:
  * below is a quote from [Discover the Secrets of Java Serialization](https://www.oracle.com/technical-resources/articles/java/serializationapi.html)
> Caching Objects in the Stream
> > First, consider the situation in which an object is written to a stream and then written again later. 
> > By default, an ObjectOutputStream will maintain a reference to an object written to it. 

  * below is another quote from [ObjectOutputStream Javadoc](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/ObjectOutputStream.html)
> 
> The default serialization mechanism for an object writes the class of the object, the class signature, and the values 
> of all non-transient and non-static fields. References to other objects (except in transient or static fields) cause 
> those objects to be written also. <b>Multiple references to a single object are encoded using a reference sharing 
> mechanism so that graphs of objects can be restored to the same shape as when the original was written.</b>
> 
  * If you want more control over serialization in terms of the replacing the object is going to read or write with
another object. You can override `writeReplace() ` and `readResolve()` methods in [Serializable interface](https://docs.oracle.com/en/java/javase/18/docs/api/java.base/java/io/Serializable.html)

#### Affected code
* `PublicInformation`
  * The init and serialize methods, including the debugged versions
  * Here's the original linker portion of the code
```java
for (FullSeller seller : listOfSellers) {
    seller.linker();
}
for (FullBuyer buyer : listOfBuyers) {
    buyer.linker();
}
for (Store store : listOfStores) {
    for (FullBuyer buyer : listOfBuyers) {
        int index = 0;
        for (Store buyerStore : buyer.getStoresMessaged()) {
            if (store != buyerStore && store.equals(buyerStore))
                buyer.getStoresMessaged().set(index, store);
        }
    }
    for (FullSeller seller : listOfSellers) {
        int index = 0;
        for (Store sellerStore : seller.getStores()) {
            if (store != sellerStore && store.equals(sellerStore))
                seller.getStores().set(index, store);
        }
    }
}
```

* `FullUser`
  * Removed linker method:
```java
    /**
     * links the conversations in both parties together so same point to the same memory location
     * <p>
     * This is used to fix the issues of both user copies of conversations instead of all point to one conversation
     * after serialization and deserialization cycle
     */
    protected void linker() {
        for (Conversation c : conversations) {
            FullUser other = PublicInformation.userTranslate(c.getOtherUser(this.user));
            assert other != null;
            int index = 0;
            for (Conversation cc : other.conversations) {
        if(cc.getOtherUser(other.user).equals(this.user)&&!cc.equals(c)){
        conversations.set(index,cc);
        break;
        }
        index++;
        }
        }
        }
```

## 11/18 - 11/20/2022

### Researched about different implementations of server client models - Yulin Lin

#### Description

There are multiple packages and ways to implement them to establishes a server-client connections.
However, one must know the pros and cons and how to implement them. Preferable the implementation also provide a way to
asynchronously write to a client to achieve real time update

#### Simple Server - Client model using `Java.net` Sockets and `Java.io` readers and writers

* Simple
* delegate each client with one or multiple threads which can also be neatly packed into a class that extends thread
* The Message Core and Public Information class must be synchronized or synchronize the thread that's trying to access
  or change them
* Blocked, not threadsafe
* It's slower than alternative option due to the usage of `java.io` package, but it allows a wider choice of
  reader/writer and data types
* In order to achieve real time update, another socket must be opened and the server must know that the 2 sockets are
  from the same client
* Not scalable
* Multithreading nightmare

#### Usage of `java.nio.channel` ServerSocketChannel and SocketChannel

* [great introduction of it](https://medium.com/coderscorner/tale-of-client-server-and-socket-a6ef54a74763)
* Some other great discussion about the implementation of this model
  * [How to read SocketChannel in one thread, and write from n threads?](https://stackoverflow.com/questions/46874437/how-to-read-socketchannel-in-one-thread-and-write-from-n-threads)
  * [A great explanation of non-blocking io that SocketChannel implement](https://stackoverflow.com/questions/31789847/parallel-writes-reads-in-a-single-nio-selector)
  * [Difference between NIO and IO packages](https://www.tutorialspoint.com/java_nio/java_nio_vs_io.htm)

##### The selected solution - ServerSocketChannel and SocketChannel coupled with `Java.util.concurrent.ArrayBlockingQueue` (can be switched to another concurrent structure)

* __Inspired
  by [this post](https://stackoverflow.com/questions/43928247/java-socketchannel-selector-combine-write-channel-with-blocking-queue)__
* The usage of Selector to control multiple channels from different clients allow such operation be done in a single
  thread
* However, since the there all channels would share the same data, other than an attachment of their own queue which
  contains the information to be written, you have to implement a way for the server to know which client it's talking
  to
* Non-blocking mode(NOT enabled by default) greatly speeds up the server by freeing them from waiting for the data to
  be processed, so it can process other request and send the data once they are ready
* Scalable since there's only a few threads(the main selection loop and a few thread used to notify the update to users)
  so it won't overwhelm the OS scheduler
* In order to achieve those functionalities, however, the only medium available are primitive type buffers, and
  ByteBuffer are used the most which can be confusing

#### Some other creative ways to achieve this functionality

* `java.nio.channel` AsynchronousServerSocketChannel
  * achieve true async read and write by the usage of `Future` objects
  * Would be most efficient
  * However, it will be even more complicated and would produce unreliable
    code if not careful since async allows read and write to return immediately
    even if only none was read/written(null), or some were read/written(very hard to catch)
  * It will be more suited for implementing an emergency/critical communication system
    for which delay are not tolerable
  * [A great post that briefly explains the different between async and normal socket channel](https://stackoverflow.com/questions/22177722/java-nio-non-blocking-channels-vs-asynchronouschannels)
* Datagram
  * An alternative to TCP protocol
  * datagram don't have definite point to point connection and forced order of package like TCP
  * Datagram do not specify the path of the packages and the ordering of the package(if they arrive at all)
    * Much like original IP protocol
  * However, datagram allow a message to be broadcast over the network and any listeners of the broadcast can
    get the most up-to-date information
  * A great use case of it is stock price quote server

* `java.util` Observer and Observable classes
  * implement observer and observable programming scheme
  * the observer will be notified when an observable object changes
  * However, those classes are _deprecated_ since Java 9