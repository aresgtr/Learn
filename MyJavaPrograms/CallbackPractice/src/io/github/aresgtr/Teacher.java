package io.github.aresgtr;

public class Teacher implements Callback {

    private Student student;

    public Teacher(Student student) {
        this.student = student;
    }

    public void askQuestion(final String question) {
        System.out.println("Teacher ask a question: " + question);
        student.resolveQuestion(this, question);
        System.out.println("Teacher: do something else");
    }

    @Override
    public void tellAnswer(int answer) {
        System.out.println("Your answer is: " + answer);
    }
}
