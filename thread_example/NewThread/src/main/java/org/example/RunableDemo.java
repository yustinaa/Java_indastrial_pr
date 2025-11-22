package org.example;

class NewThread implements Runnable {
    Thread t;
    NewThread() {
        t = new Thread( this,"New thread" );
        System.out.println( "Child thread: " + t );
        t.start();
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
public class RunableDemo{
    public static void main(String args[]) {
        new NewThread();
        try {
            for ( int i = 5; i > 0; i-- ) {
                System.out.println("Main thread: " + i * 100 );
                Thread.sleep(1000);
            }
        }
        catch( InterruptedException e ) {
            System.out.println("Main thread interruption!");
        }
        System.out.println("Main thread termination!");
    }
}