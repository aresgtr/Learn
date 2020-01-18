package io.github.aresgtr;


/**
 * https://www.cnblogs.com/dong-liu/p/7476955.html
 */
public class Main {

    public static void main(String[] args) {
	// write your code here
        Student student = new Student();
        Teacher teacher = new Teacher(student);

        teacher.askQuestion("1 + 1 = ?");
    }
}
