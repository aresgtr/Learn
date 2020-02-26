public class Dog extends Animal {

    public Dog() {
        name = getClass().getSimpleName();
        System.out.println("create " + name);
    }

    public Animal getAnimal() {
        return new Dog();
    }
}
