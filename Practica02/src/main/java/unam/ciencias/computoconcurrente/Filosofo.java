package unam.ciencias.computoconcurrente;

import java.util.concurrent.Semaphore;

public abstract class Filosofo implements Runnable {
    public static int DEFAULT_TABLE_SIZE = 5;

    protected int id;
    protected Semaphore leftFork;
    protected Semaphore rightFork;
    protected Semaphore waiter;

    public Filosofo(int id, Semaphore leftFork, Semaphore rightFork, Semaphore waiter) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.waiter = waiter;
    }

    private int numMeals = 0;

    public synchronized void eat() {
        numMeals++;
    }

    public int getNumMeals() {
        return numMeals;
    }

    public void run() {
        try {
            while (true) {
                // Pensar
                System.out.println("Filosofo " + id + " está pensando");

                // Esperar al camarero
                waiter.acquire();

                // Tomar el tenedor izquierdo
                leftFork.acquire();

                // Tomar el tenedor derecho
                rightFork.acquire();

                // Comer
                System.out.println("Filosofo " + id + " está comiendo");

                // Soltar el tenedor izquierdo
                leftFork.release();

                // Soltar el tenedor derecho
                rightFork.release();

                // Liberar al camarero
                waiter.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
