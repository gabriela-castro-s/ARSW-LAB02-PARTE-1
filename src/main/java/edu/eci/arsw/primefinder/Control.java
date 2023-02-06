/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.primefinder;

import java.util.Scanner;

/**
 * Clase coordinadora de los hilos
 */
public class Control extends Thread {
    
    private final static int NTHREADS = 3;
    private final static int MAXVALUE = 30000000;
    private final static int TMILISECONDS = 5000;

    private final int NDATA = MAXVALUE / NTHREADS;

    private PrimeFinderThread pft[];
    private Control control;

    private boolean over = true;

    /**
     * Constructor que crea los hilos a coordinar
     */
    private Control() {
        super();
        this.pft = new  PrimeFinderThread[NTHREADS];
        control = this;
        int i;
        for(i = 0;i < NTHREADS - 1; i++) {
            PrimeFinderThread elem = new PrimeFinderThread(i*NDATA, (i+1)*NDATA, this);
            pft[i] = elem;
        }
        pft[i] = new PrimeFinderThread(i*NDATA, MAXVALUE + 1, this);
    }

    /**
     * Inicializa un nuevo coordinador
     * @return coordinador de hilos
     */
    public static Control newControl() {
        return new Control();
    }

    /**
     * Encargado de dar las instrucciones a los hilos
     */
    @Override
    public void run() {
        for (int i = 0; i < NTHREADS; i++) {
            System.out.println("Inicio hilo");
            pft[i].start();
        }

        while (true) {
                try {
                    //Después de cierto tiempo, se para el hilo
                    sleep(TMILISECONDS);
                    //Debe cambiar el estado de todos los hilos
                    for (int i = 0; i < NTHREADS; i++) {
                        pft[i].parar();
                        //System.out.println("Detiene hilos");
                    }
                    //Indica el estado de los hilos y espera el enter para continuar la ejecución
                    System.out.println("SE HAN ENCONTRADO: " + primosEncontrados() + " NUMEROS DE PRIMOS, presionar ENTER ");
                    Scanner enter = new Scanner(System.in);
                    enter.nextLine();
                    //Después de escuchar el enter, cambia el estado de los hilos paraque continuen su ejecución
                    for (int i = 0; i < NTHREADS; i++) {
                        pft[i].correr();
                        System.out.println("Corre hilos");
                    }
                    //Cuando los estados cambian, se sincronizan los hilos y se les notifica que pueden continuar
                    synchronized (control) {
                        control.notifyAll();
                    }

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                //Revisa si ya se terminó la ejecución de los hilos
                if (over){
                    break;
                }

            }
        }

    /**
     * Calcula la cantidad de primos encontrados por todos los hilos
     * @return total de primos
     */
    private int primosEncontrados () {
        int total = 0;
        int i = 0;
        while(i<NTHREADS){
            if(pft[i].isWait()){
                total = total+ pft[i].getprimosEncontrados();
                i++;
            }
        }
            return total;
    }

    public void isOver(){
        over = true;
    }
}

