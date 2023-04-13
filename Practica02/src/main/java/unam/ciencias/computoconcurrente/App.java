package unam.ciencias.computoconcurrente;

import java.util.concurrent.Semaphore;

public class App {

    public static void main(String[] a) throws InterruptedException {
        int numPhilosophers = Filosofo.DEFAULT_TABLE_SIZE;
        Semaphore waiter = new Semaphore(numPhilosophers - 1);

        Filosofo[] philosophers = new Filosofo[numPhilosophers];

        for (int i = 0; i < numPhilosophers; i++) {
            Semaphore leftFork = new Semaphore(1);
            Semaphore rightFork = new Semaphore(1);

            if (i == numPhilosophers - 1) {
                philosophers[i] = new Filosofo(i, rightFork, leftFork, waiter) {
                };
            } else {
                philosophers[i] = new Filosofo(i, leftFork, rightFork, waiter) {
                };
            }
        }

        for (int i = 0; i < numPhilosophers; i++) {
            Thread t = new Thread(philosophers[i]);
            t.start();
        }
    }
}
