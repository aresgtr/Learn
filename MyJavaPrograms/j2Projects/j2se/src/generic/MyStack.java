package generic;

import java.util.LinkedList;

public class MyStack<T> {

    LinkedList<T> values = new LinkedList<>();

    public void push(T t) {
        values.addLast(t);
    }

    public T pull() {
        return values.removeLast();
    }

    public T peek() {
        return values.getLast();
    }
}
