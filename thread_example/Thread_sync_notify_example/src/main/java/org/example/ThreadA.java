package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class ThreadA {
    public static void main(String[] args) {
        ThreadB b = new ThreadB();
        b.start();
        synchronized (b) {
            try {
                System.out.println("Ждем пока поток b выполнится...");
                b.wait();
            } catch (InterruptedException e) {
            }
            System.out.println("Total равно: " + b.total);
        }
    }
}
class ThreadB extends Thread {
    int total = 0;
    public void run() {
        synchronized (this) {
            for (int i = 0; i < 100; i++) {
                total += i;
            }
            notify();
        }
    }
}