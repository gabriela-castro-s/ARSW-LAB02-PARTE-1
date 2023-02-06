package edu.eci.arsw.primefinder;

import java.util.LinkedList;
import java.util.List;

/**
 * Hilo encargado de la busqueda de primos
 */
public class PrimeFinderThread extends Thread{

	
	int a,b;
	private List<Integer> primes;
    private Control control;
    boolean corre;

    /**
     * Constructor de hilo encargado de buscar primos en un rango determinado
     * @param a inicio de busqueda
     * @param b fin de busqueda
     * @param control coordinador de los hilos
     */
	public PrimeFinderThread(int a, int b, Control control) {
		super();
        corre = true;
        this.primes = new LinkedList<>();
		this.a = a;
		this.b = b;
        this.control = control;

	}

    /**
     * El método run se encarga de la ejecución del hilo
     */
        @Override
	public void run(){
            for (int i = a;i < b;i++){
                //Revisa el estado del hilo, si está pausado, se sincroniza el control y se
                //pide una espera para todos los hilos
                if (!corre){
                    try {
                        synchronized (control){
                            control.wait();

                        }
                    } catch (InterruptedException e){
                        throw new RuntimeException(e);
                    }
                }
                //De lo contrario, continua con la ejecución normal y revisa si su posición actual es primo
                if(isPrime(i)){
                    primes.add(i);
                    System.out.println(i);
                    //Revisa si ya termino de calcular los primos en su rango
                    if (i == b){
                        control.isOver();
                    }
                }
            }

        }

    /**
     * Revisa si un numero es primo
     * @param n numero a revisar
     * @return boolean resultante de la revisión
     */
    boolean isPrime(int n) {
	    boolean ans;
            if (n > 2) { 
                ans = n%2 != 0;
                for(int i = 3;ans && i*i <= n; i+=2 ) {
                    ans = n % i != 0;
                }
            } else {
                ans = n == 2;
            }
	    return ans;
	}

    /**
     * Cambia el estado de ejecución del hilo a falso
     */
    public void parar() {
        corre = false;
    }

    /**
     * Cambia el estado de ejecución del hilo a verdadero
     */
    public void correr() {
        corre = true;
    }

    /**
     * Revisa el estado actual del hilo
     * @return estado de ejecución del hilo
     */
    public boolean isWait() {
        return this.getState() == Thread.State.WAITING;
    }

    /**
     * Retorna la cantidad de primos encontrados
     * @return cantidad de números encontrados
     */
    public int getprimosEncontrados() {
        return primes.size();
    }
}
