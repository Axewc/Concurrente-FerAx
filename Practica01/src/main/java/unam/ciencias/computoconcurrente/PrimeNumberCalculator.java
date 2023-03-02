package unam.ciencias.computoconcurrente;
import java.lang.Math;
public class PrimeNumberCalculator implements Runnable{

    int threads;
    static int numPrimo;
    public static boolean result;
    public static int longitudSubInter; //Dividimos el intervalo [2,N-1] en this.threads cantidad de sub interbalos, uno por cada hilo


    public PrimeNumberCalculator() {
        this.threads = 1;
    }

    public PrimeNumberCalculator(int threads) {
        this.threads = threads > 1 ? threads : 1;
    }
    

    public boolean isPrime(int n) throws InterruptedException{
        if (n < 2) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
    

    @Override
    public void run(){
        int inicio = 2 + (int) (Thread.currentThread().getId() * longitudSubInter);
        int fin = inicio + longitudSubInter;
        for (int i = inicio; i < fin; i++) {
            try {
                if (isPrime(i)) {
                    numPrimo = i;
                    result = true;
                    break;
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
    }

}