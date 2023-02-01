/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.primefinder;

import java.util.Scanner;

/**
 *
 */
public class Control extends Thread {
    
    private final static int NTHREADS = 3;
    private final static int MAXVALUE = 30000000;
    private final static int TMILISECONDS = 5000;

    private final int NDATA = MAXVALUE / NTHREADS;

    private PrimeFinderThread pft[];
    private Control control;
    
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
    
    public static Control newControl() {
        return new Control();
    }

    @Override
    public void run() {
        for (int i = 0; i < NTHREADS; i++) {
            System.out.println("Inicio hilo");
            pft[i].start();
        }

        while (true) {
                try {
                    sleep(TMILISECONDS);

                    for (int i = 0; i < NTHREADS; i++) {
                        pft[i].parar();
                        System.out.println("Detiene hilos");
                    }
                    System.out.println("SE HAN ENCONTRADO: " + primosEncontrados() + " NUMEROS DE PRIMOS, presionar ENTER ");
                    Scanner enter = new Scanner(System.in);
                    enter.nextLine();

                    for (int i = 0; i < NTHREADS; i++) {

                        pft[i].correr();
                        System.out.println("Corre hilos");
                    }
                    synchronized (control) {
                        control.notifyAll();
                    }

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

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
    }