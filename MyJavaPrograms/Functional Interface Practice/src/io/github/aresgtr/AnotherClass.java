package io.github.aresgtr;

public class AnotherClass {
    public Object doStuff() {
        System.out.println("Class name is: " + getClass().getSimpleName());
        return Main.doStuff(new TheFunction() {
            @Override
            public Object doTheFunction(Object o1, Object o2) {
                System.out.println("The anonymous class name is: " + getClass().getSimpleName());
                return ((String) o1).toUpperCase() + ((String) o2).toUpperCase();
            }
        },
                "Hello ", "Qi");
    }


    public static void main(String[] args) {
        AnotherClass anotherClass = new AnotherClass();
        System.out.println(anotherClass.doStuff());

        System.out.println("========= Below is the output of AnotherClassLambda =========");

        AnotherClassLambda anotherClassLambda = new AnotherClassLambda();
        System.out.println(anotherClassLambda.doStuff());
    }
}

//  improve the class with lambda
class AnotherClassLambda {
    public Object doStuff() {
        TheFunction tf = (o1, o2) -> {
            System.out.println("Lambda class name is: " + getClass().getSimpleName());
            return ((String) o1).toUpperCase() + ((String) o2).toUpperCase();
        };

        System.out.println("Class name is: " + getClass().getSimpleName());
        return Main.doStuff(tf, "Hello ", "QI");
    }
}
