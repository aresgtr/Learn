package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) {
	// write your code here
//        new Thread(()-> {
//            System.out.println("Printing from the Runnable");
//            System.out.println("Line 2");
//        }).start();


        Employee john = new Employee("John Doe", 30);
        Employee tim = new Employee("Tim Buchalka", 21);
        Employee jack = new Employee("Jack Hill", 40);
        Employee snow = new Employee("Snow White", 22);

        List<Employee> employees = new ArrayList<>();
        employees.add(john);
        employees.add(tim);
        employees.add(jack);
        employees.add(snow);



        //  To sort the employees in order
//        Collections.sort(employees, new Comparator<Employee>() {
//            @Override
//            public int compare(Employee employee1, Employee employee2) {
//                return employee1.getName().compareTo(employee2.getName());
//            }
//        });


//        Collections.sort(employees, (Employee employee1, Employee employee2) ->
//                employee1.getName().compareTo(employee2.getName()));

        //  We can actually remove the datatypes
        Collections.sort(employees, (employee1, employee2) ->
                employee1.getName().compareTo(employee2.getName()));


        for (Employee employee : employees) {
            System.out.println(employee.getName());
        }

        System.out.println("==================");

//        String sillyString = doStringStuff(new UpperConcat() {
//            @Override
//            public String upperAndConcat(String s1, String s2) {
//                return s1.toUpperCase() + s2.toUpperCase();
//            }
//        },
//                employees.get(0).getName(), employees.get(1).getName());
//        System.out.println(sillyString);

        //  This is how Functional Interface works
        UpperConcat uc = (s1, s2) -> s1.toUpperCase() + s2.toUpperCase();
        String sillyString = doStringStuff(uc, employees.get(0).getName(), employees.get(1).getName());
        System.out.println(sillyString);

        System.out.println("===================");

        AnotherClass anotherClass = new AnotherClass();
        String s = anotherClass.doSomething();
        System.out.println(s);

        System.out.println();
        System.out.println("Improved by Lambda:");

        AnotherClassLambda anotherClassLambda = new AnotherClassLambda();
        s = anotherClassLambda.doSomething();
        System.out.println(s);

        System.out.println("====================");

        Unknown unknown = new Unknown();
        s = unknown.doSomething();
        System.out.println(s);

        System.out.println();
        System.out.println("Improved by Lambda");

        LambdaUnknow lambdaUnknow = new LambdaUnknow();
        s = lambdaUnknow.doSomething();
        System.out.println(s);
    }

    public final static String doStringStuff(UpperConcat uc, String s1, String s2) {
        return uc.upperAndConcat(s1, s2);
    }
}


@FunctionalInterface
interface UpperConcat {
    public String upperAndConcat(String s1, String s2);

    default String upperAndConcat3(String s1, String s2) {
        return "";
    }
}










class Employee {
    private String name;
    private int age;

    public Employee(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}



class AnotherClass {
    public String doSomething() {
        System.out.println("The AnotherClass class's name is: " + getClass().getSimpleName());
        return Main.doStringStuff(new UpperConcat() {
            @Override
            public String upperAndConcat(String s1, String s2) {
                System.out.println("The anonymous class's name is: " + getClass().getSimpleName());
                return s1.toUpperCase() + s2.toUpperCase();
            }
        }, "String1", "String2");
    }
}

//  Improved by Lambda
class AnotherClassLambda {
    public String doSomething() {
        UpperConcat uc = (s1, s2) -> {
            System.out.println("The lambda expression's class is: " + getClass().getSimpleName());
            String result = s1.toUpperCase() + s2.toUpperCase();
            return result;
        };

        System.out.println("The AnotherClass class's name is: " + getClass().getSimpleName());
        return  Main.doStringStuff(uc, "String1", "String2");
    }
}

//  I don't know what is this
class Unknown {
    public String doSomething() {
        final int i = 0;
        {
            UpperConcat uc = new UpperConcat() {
                @Override
                public String upperAndConcat(String s1, String s2) {
                    System.out.println("i (within anonymous class) = " + i);
                    return s1.toUpperCase() + s2.toUpperCase();
                }
            };
            System.out.println("The AnotherClass class's name is " + getClass().getSimpleName());

            System.out.println("i = " + i);
            return  Main.doStringStuff(uc, "String1", "String2");
        }
    }
}

class LambdaUnknow {

    public String doSomething() {
        int i = 0;

        UpperConcat uc = (s1, s2) -> {
            System.out.println("The lambda expression's class is: " +getClass().getSimpleName());
            String result = s1.toUpperCase() + s2.toUpperCase();
            return result;
        };

        System.out.println("The AnotherClass class's name is " + getClass().getSimpleName());
        return Main.doStringStuff(uc, "String1", "String2");
    }
}
