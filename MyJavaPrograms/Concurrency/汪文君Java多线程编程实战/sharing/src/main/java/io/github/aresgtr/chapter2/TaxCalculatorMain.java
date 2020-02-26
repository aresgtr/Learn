package io.github.aresgtr.chapter2;

public class TaxCalculatorMain {

    public static void main(String[] args) {
        TaxCalculator taxCalculator = new TaxCalculator(10000d, 2000d) {
            @Override
            protected double calcTax() {
                return getSalary() * 0.1 + getBonus() * 0.15;
            }
        };

        double tax = taxCalculator.calculate();
        System.out.println(tax);
    }
}
