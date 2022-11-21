package Experiment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestReflection {

    public static long INC = 0;

    public static final int COUNT = 100000000;

    private static class Dummy {
        public void doSomething(String s1, String s2, String s3) {
            INC++;
        }
    }

    public static void main(String[] args) throws NoSuchMethodException {

        Dummy obj = new Dummy();
        Method method = obj.getClass().getMethod("doSomething", String.class, String.class, String.class);

        String s1 = "string1";
        String s2 = "string2";
        String s3 = "string3";

        //warmup
        runReflection(obj, method, s1, s2, s3, COUNT / 10);
        runStatic(obj, s1, s2, s3, COUNT / 10);

        ///realtest

        long reftime = System.nanoTime();
        runReflection(obj, method, s1, s2, s3, COUNT);
        reftime = System.nanoTime() - reftime;

        long time = System.nanoTime();
        runStatic(obj, s1, s2, s3, COUNT);
        time = System.nanoTime() - time;

        System.out.println(reftime);
        System.out.println(time);

        //1000 *1000 *1000 nanoseconds in a second
        System.out.println(reftime / (1000 * 1000 * 1000));
        System.out.println(time / (1000 * 1000 * 1000));
        System.out.println((double) reftime / (double) time);

        System.out.println("percentage speed decrease from using reflection:" + (((double) reftime / (double) time) - 1) * 100);

    }

    private static void runReflection(Dummy obj, Method method, String s1, String s2, String s3, int count) {
        for (int i = 0; i < count; i++) {
            try {
                method.invoke(obj, s1, s2, s3);
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
    }

    private static void runStatic(Dummy obj, String s1, String s2, String s3, int count) {
        for (int i = 0; i < count; i++) {
            obj.doSomething(s1, s2, s3);
        }
    }

}