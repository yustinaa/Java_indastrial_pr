package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Reader extends Thread {
    Calculator c;
    public Reader(Calculator calc) {
        c = calc;
    }
    public void run() {
        synchronized (c) {
            try {
                System.out.println("Вычисление...");
                c.wait();
            } catch (InterruptedException e) {
            }
            System.out.println(
                    "Total равно: " + c.total);
        }
    }
    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        new Reader(calculator).start();
        new Reader(calculator).start();
        new Reader(calculator).start();
        calculator.start();
    }
}
class Calculator extends Thread {
    int total = 0;
    public void run() {
        synchronized (this) {
            for (int i = 0; i < 100; i++) {
                total += i;
            }
            notifyAll();
        }
    }
}