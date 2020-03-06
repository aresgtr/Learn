package io.github.aresgtr;

public class Main {

    public static void main(String[] args) {

        BinaryTree bt = new BinaryTree().createBinaryTree();

        System.out.println(bt.containsNode(6));

        System.out.println(bt.containsNode(4));

        System.out.println(bt.containsNode(1));
    }
}
