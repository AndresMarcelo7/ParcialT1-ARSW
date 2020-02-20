package edu.eci.arsw.moneylaundering;

import sun.awt.Mutex;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MoneyLaunderingThread extends Thread {
    private ArrayList<File> files;
    private TransactionAnalyzer ta;
    private TransactionReader tr;
    private MoneyLaundering entorno;
    private boolean paused;
    private Mutex lock;

    public MoneyLaunderingThread(ArrayList<File> files, MoneyLaundering ambiente, TransactionAnalyzer ta, TransactionReader tr){
        lock = new Mutex();
        this.files=files;
        this.ta =ta;
        this.tr=tr;
        this.entorno=ambiente;
        this.paused=false;
    }

    public void run(){
        //Contenido del metodo processTransactionalData de MoneyLaundring
        for(File transactionFile : files)
        {
            List<Transaction> transactions = tr.readTransactionsFromFile(transactionFile);
            for(Transaction transaction : transactions)
            {
                while(paused){
                    synchronized (lock){
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                ta.addTransaction(transaction);
            }
            entorno.incrementFilesProcesed();
        }

    }

    public void pausar(){
        paused=true;
    }

    public void continuar(){
        paused=false;
        synchronized (lock){
            lock.notify();
        }

    }

    public boolean isPaused() {
        return paused;
    }
}
