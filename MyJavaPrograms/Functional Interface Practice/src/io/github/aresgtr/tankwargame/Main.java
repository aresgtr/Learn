package io.github.aresgtr.tankwargame;

public class Main {

    public static void main(String[] args) {

        //  expands the empty function for functional interface
        TheFunction upperConcat = (s1, s2) -> ((String) s1).toUpperCase() + ((String) s2).toUpperCase();
        TheFunction print = (s1, s2) -> {
            System.out.println("This function takes " + s1 + " and " + s2 + " as inputs");
            return null;
        };

        TheFunction add = (a, b) -> (Integer) a + (Integer) b;
        TheFunction substract = (a, b) -> (Integer) a - (Integer) b;


        //  We cannot call the functional interface. We can only call the defined empty function. 
        System.out.println(doStuff(upperConcat, "Hello ", "World"));
        doStuff(print, "good", "bad");
        System.out.println(doStuff(add, 1, 2));
        System.out.println(doStuff(substract, 100, 50));
    }


    //  defines the empty function for main for easier call
    public final static Object doStuff(TheFunction tf, Object o1, Object o2) {
        return tf.doTheFunction(o1, o2);
    }
}


//  The interface of a function
@FunctionalInterface
interface TheFunction {
    public Object doTheFunction (Object o1, Object o2);
}
