package Experiment;

import java.io.*;
import java.util.ArrayList;

public class TestExternalizable implements Externalizable {

    ArrayList<A> as;
    ArrayList<A> bs;

    static class A implements Serializable {
        @Serial
        private static final long serialVersionUID = 2L;

        String s;
        int i;

        A(String s, int i) {
            this.s = s;
            this.i = i;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this)
                return true;
            if (obj instanceof A) {
                A a = (A) obj;
                return this.s.equals(a.s) && a.i == this.i;
            }
            return false;
        }
    }

    public TestExternalizable() {
        as = new ArrayList<>();
        bs = new ArrayList<>();
    }

    void addA(String s, int i) {
        as.add(new A(s, i));
    }

    void addB(A a) {
        bs.add(a);
    }


    @Override
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeInt(as.size());
        for (A a : as) {
            objectOutput.writeObject(a);
        }
        objectOutput.writeInt(bs.size());
        for (A a : bs) {
            objectOutput.writeObject(a);
        }
    }

    @Override
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        as.clear();
        bs.clear();
        int length = objectInput.readInt();
        for (int i = 0; i < length; i++) {
            as.add((A) objectInput.readObject());
        }
        length = objectInput.readInt();
        for (int i = 0; i < length; i++) {
            bs.add((A) objectInput.readObject());
        }
    }

    public static void main(String[] args) {
        int i;
        char c;
        TestExternalizable test = new TestExternalizable();

        for (i = 0, c = '!'; i < 500; i++, c++) {
            test.addA(Character.toString(c), i);
        }

        for (i = 0, c = '!'; i < 500; i++, c++) {
            test.addB(new A(Character.toString(c), i));
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("src/Experiment/test"))) {
            out.writeObject(test);
        } catch (Exception e) {
            e.printStackTrace();
        }

        TestExternalizable b = null;

        try (ObjectInputStream out = new ObjectInputStream(new FileInputStream("src/Experiment/test"))) {
            b = (TestExternalizable) out.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(b.as.get(24) == b.bs.get(24));
        System.out.println(b.as.get(24).equals(b.bs.get(24)));

    }
}
