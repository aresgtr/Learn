package io.github.aresgtr;

public class Student {

    public void resolveQuestion(Callback callback, final String question) {

        System.out.println("Student receive the question: " + question);
        System.out.println("Student: I am busy");
        doSomething();
        callback.tellAnswer(2);
    }

    public void doSomething() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
