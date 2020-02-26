package io.github.aresgtr;

public class AbstractFactoryPatternDemo {
    public static void main(String[] args) {

        AbstractFactory shapeFactory = FactoryProducer.getFactory("SHAPE");

        Shape1 shape1 = shapeFactory.getShape("CIRCLE");
        shape1.draw();

        Shape1 shape2 = shapeFactory.getShape("RECTANGLE");
        shape2.draw();

        Shape1 shape3 = shapeFactory.getShape("SQUARE");
        shape3.draw();

        AbstractFactory colorFactory = FactoryProducer.getFactory("COLOR");

        Color color1 = colorFactory.getColor("RED");
        color1.fill();

        Color color2 = colorFactory.getColor("GREEN");
        color2.fill();

        Color color3 = colorFactory.getColor("BLUE");
        color3.fill();
    }
}

interface Shape1 {
    void draw();
}

class Rectangle1 implements Shape1 {
    @Override
    public void draw() {
        System.out.println("Inside Rectangle::draw() method.");
    }
}

class Square1 implements Shape1 {
    @Override
    public void draw() {
        System.out.println("Inside Square::draw() method.");
    }
}

class Circle1 implements Shape1 {
    @Override
    public void draw() {
        System.out.println("Inside Circle::draw() method.");
    }
}

interface Color {
    void fill();
}

class Red implements Color {
    @Override
    public void fill() {
        System.out.println("Inside Red::fill() method.");
    }
}

class Green implements Color {
    @Override
    public void fill() {
        System.out.println("Inside Green::fill() method.");
    }
}

class Blue implements Color {
    @Override
    public void fill() {
        System.out.println("Inside Blue::fill() method.");
    }
}

abstract class AbstractFactory {
    abstract Color getColor(String color);
    abstract Shape1 getShape(String shape);
}

class ShapeFactory1 extends AbstractFactory {
    @Override
    Shape1 getShape(String shape) {
        if (shape == null) {
            return null;
        }
        if (shape.equalsIgnoreCase("CIRCLE")) {
            return new Circle1();
        } else if (shape.equalsIgnoreCase("RECTANGLE")) {
            return new Rectangle1();
        } else if (shape.equalsIgnoreCase("SQUARE")) {
            return new Square1();
        }
        return null;
    }

    @Override
    Color getColor(String color) {
        return null;
    }
}

class ColorFactory extends AbstractFactory {
    @Override
    Shape1 getShape(String shape) {
        return null;
    }

    @Override
    Color getColor(String color) {
        if (color == null) {
            return null;
        }
        if (color.equalsIgnoreCase("RED")) {
            return new Red();
        } else if (color.equalsIgnoreCase("GREEN")) {
            return new Green();
        } else if (color.equalsIgnoreCase("BLUE")) {
            return new Blue();
        }
        return null;
    }
}

class FactoryProducer {
    static AbstractFactory getFactory(String choice) {
        if (choice.equalsIgnoreCase("SHAPE")) {
            return new ShapeFactory1();
        } else if (choice.equalsIgnoreCase("COLOR")) {
            return new ColorFactory();
        }
        return null;
    }
}