package io.github.aresgtr.chapter2;

public class TaxCalculator {

    private final double salary;
    private final double bonus;

    public TaxCalculator(double salary, double bonus) {
        this.salary = salary;
        this.bonus = bonus;
    }

    public double getSalary() {
        return salary;
    }

    public double getBonus() {
        return bonus;
    }

    protected double calcTax() {

        return  0.0d;
    }

    public double calculate() {
        return this.calcTax();
    }
}
