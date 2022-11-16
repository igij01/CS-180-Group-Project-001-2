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
                if (cc.getOtherUser(other.user).equals(this.user) && !cc.equals(c)) {
                    conversations.set(index, cc);
                    break;
                }
                index++;
            }
        }
    }
```

