package unam.ciencias.computoconcurrente;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppTest {

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS) // tiempo límite de 10 segundos
    public void testFivePhilosophers() throws InterruptedException {
        int numPhilosophers = 5;
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

        Thread[] threads = new Thread[numPhilosophers];
        for (int i = 0; i < numPhilosophers; i++) {
            threads[i] = new Thread(philosophers[i]);
            threads[i].start();
        }

        // Esperar a que los hilos terminen
        for (int i = 0; i < numPhilosophers; i++) {
            threads[i].join();
        }

        // Verificar que todos los filósofos comieron al menos una vez
        for (int i = 0; i < numPhilosophers; i++) {
            assertEquals(philosophers[i].getNumMeals(), 1);
        }
    }

    @Test
    public void testThreadCreationAndExecution() throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello from thread 1");
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello from thread 2");
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        assertTrue(true);
    }

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS) // tiempo límite de 10 segundos
    public void testFivePhilosophersMultipleMeals() throws InterruptedException {
        int numPhilosophers = 5;
        Semaphore waiter = new Semaphore(numPhilosophers - 1);

        // Cada filósofo comerá 3 veces
        int numMeals = 3;

        Filosofo[] philosophers = new Filosofo[numPhilosophers];

        for (int i = 0; i < numPhilosophers; i++) {
            Semaphore leftFork = new Semaphore(1);
            Semaphore rightFork = new Semaphore(1);

            if (i == numPhilosophers - 1) {
                philosophers[i] = new Filosofo(i, rightFork, leftFork, waiter) {
                    @Override
                    public void run() {
                        for (int i = 0; i < numMeals; i++) {
                            eat();
                        }
                    }
                };
            } else {
                philosophers[i] = new Filosofo(i, leftFork, rightFork, waiter) {
                    @Override
                    public void run() {
                        for (int i = 0; i < numMeals; i++) {
                            eat();
                        }
                    }
                };
            }
        }

        Thread[] threads = new Thread[numPhilosophers];
        for (int i = 0; i < numPhilosophers; i++) {
            threads[i] = new Thread(philosophers[i]);
            threads[i].start();
        }

        // Esperar a que los hilos terminen
        for (int i = 0; i < numPhilosophers; i++) {
            threads[i].join();
        }

        // Verificar que todos los filósofos comieron al menos numMeals veces
        for (int i = 0; i < numPhilosophers; i++) {
            assertEquals(philosophers[i].getNumMeals(), numMeals);
        }
    }

}
