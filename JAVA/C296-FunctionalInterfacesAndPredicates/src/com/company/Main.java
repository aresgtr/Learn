package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Employee john = new Employee("John Doe", 30);
        Employee tim = new Employee("Tim Buchalka", 21);
        Employee jack = new Employee("Jack Hill", 40);
        Employee snow = new Employee("Snow White", 22);
        Employee red = new Employee("Red RidingHood", 35);
        Employee charming = new Employee("Prince Charming", 31);

        List<Employee> employees = new ArrayList<>();
        employees.add(john);
        employees.add(tim);
        employees.add(jack);
        employees.add(snow);
        employees.add(red);
        employees.add(charming);

        System.out.println("Employees over 30:");
        System.out.println("==================");
//        for (Employee employee : employees) {
//            if (employee.getAge() > 30) {
//                System.out.println(employee.getName());
//            }
//        }

        //  Lambda Expression
        employees.forEach(employee -> {
            if (employee.getAge() > 30) {
                System.out.println(employee.getName());
            }
        });

        System.out.println("\nEmployees 30 and younger:");
        System.out.println("===========================");
        employees.forEach(employee -> {
            if (employee.getAge() <= 30) {
                System.out.println(employee.getName());
            }
        });

        System.out.println("\n****************************************************\n");

        printEmployeesByAge(employees, "Employees over 30", employee -> employee.getAge() > 30);
        printEmployeesByAge(employees, "\nEmployees 30 and under", employee -> employee.getAge() <= 30);

        //  With anonymous class
        printEmployeesByAge(employees, "\nEmployees younger than 25", new Predicate<Employee>() {
            @Override
            public boolean test(Employee employee) {
                return employee.getAge() < 25;
            }
        });

        System.out.println("\n\n\n");



        //IntPredicate
        IntPredicate greatherThan15 = i -> i > 15;
        IntPredicate lessThan100 = i -> i < 100;


        System.out.println(greatherThan15.test(10));
        int a = 20;
        System.out.println(greatherThan15.test(a + 5));

        System.out.println(greatherThan15.and(lessThan100).test(50));

        System.out.println(greatherThan15.and(lessThan100).test(15));

        System.out.println("\n\n\n");


        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            System.out.println(random.nextInt(1000));
        }

        System.out.println("Now use lambda");
        Supplier<Integer> randomSupplier = () -> random.nextInt(1000);
        for (int i = 0; i < 10; i++) {
            System.out.println(randomSupplier.get());
        }
    }


    private static void printEmployeesByAge(List<Employee> employees,
                                            String ageText,
                                            Predicate<Employee> ageCondition) {
        System.out.println(ageText);
        System.out.println("==================");

        for (Employee employee : employees) {
            if (ageCondition.test(employee)) {
                System.out.println(employee.getName());
            }
        }
    }
}
