package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
class NewThread extends Thread {

    NewThread() {
        super( "New thread" );
        System.out.println( "Child thread: "
                + this );
        start();
    }
    public void run() {
        try {
            for( int i = 5; i > 0; i-- ) {
                System.out.println("Child thread: " + i );
                Thread.sleep(500);
            }
        }
        catch( InterruptedException e ) {
            System.out.println(
                    "Child thread interruption!");
        }
        System.out.println("Child thread termination!");
    }
}
public class ThreadDemo {
    public static void main(String args[]) {
        new NewThread();
        try {
            for (int i = 5; i > 0; i--) {
                System.out.println("Main thread: " + i * 100);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Main thread interruption!");
        }
        System.out.println("Main thread termination!");
    }
}