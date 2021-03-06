package edu.eci.arsw.moneylaundering;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MoneyLaundering
{
    private TransactionAnalyzer transactionAnalyzer;
    private TransactionReader transactionReader;
    private int amountOfFilesTotal;
    private static AtomicInteger amountOfFilesProcessed;
    private MoneyLaunderingThread[] listaHilos;

    public MoneyLaundering()
    {
        transactionAnalyzer = new TransactionAnalyzer();
        transactionReader = new TransactionReader();
        amountOfFilesProcessed = new AtomicInteger();
    }
    public void processTransactionData()
    {
        amountOfFilesProcessed.set(0);
        List<File> transactionFiles = getTransactionFileList();
        amountOfFilesTotal = transactionFiles.size();
        int nHilos=5;
        int nArchivos = Math.floorDiv(amountOfFilesTotal,nHilos);
        int sobrante = amountOfFilesTotal%nHilos;
        listaHilos = new MoneyLaunderingThread[nHilos];

        int inicio=0;
        int fin=nHilos;
        for (int i=0;i<nHilos;i++){
            //Repartir el documento para cada hilo
            ArrayList<File> aux = new ArrayList<File>();
            for(int j=inicio;j<fin;j++){
                aux.add(transactionFiles.get(j));
            }
            //System.out.println(inicio + " " + fin);
            if (i==nHilos-2){
                inicio= amountOfFilesTotal-sobrante;
                fin=amountOfFilesTotal;
            }
            else{
                inicio=fin;
                fin+=nHilos;
            }
            listaHilos[i] = new MoneyLaunderingThread(aux,this,transactionAnalyzer,transactionReader);
            listaHilos[i].start();

            //System.out.printf(" \n Imprimiento arreglo de tamaño " + aux.size());
        }

        for (MoneyLaunderingThread hilo: listaHilos){
            try {
                hilo.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public List<String> getOffendingAccounts()
    {
        return transactionAnalyzer.listOffendingAccounts();
    }

    public MoneyLaunderingThread[] getListaHilos() {
        return listaHilos;
    }

    public void incrementFilesProcesed(){ amountOfFilesProcessed.incrementAndGet();}
    private List<File> getTransactionFileList()
    {
        List<File> csvFiles = new ArrayList<>();
        try (Stream<Path> csvFilePaths = Files.walk(Paths.get("src/main/resources/")).filter(path -> path.getFileName().toString().endsWith(".csv"))) {
            csvFiles = csvFilePaths.map(Path::toFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvFiles;
    }

    public static void main(String[] args)
    {
        System.out.println(getBanner());
        System.out.println(getHelp());
        MoneyLaundering moneyLaundering = new MoneyLaundering();
        Thread processingThread = new Thread(() -> moneyLaundering.processTransactionData());
        processingThread.start();
        while(true)
        {
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if(line.contains("exit"))
            {
                System.exit(0);
            }

            if (line.contains("")){
                String estado="";
                for (MoneyLaunderingThread hilo: moneyLaundering.getListaHilos()){
                    if (hilo.isPaused()) {
                        hilo.continuar();
                        estado="Activo";
                    }
                    else {
                        hilo.pausar();
                        estado="Pausado";
                    }

                }

                System.out.printf(estado + "!!!");


            }

            String message = "Processed %d out of %d files.\nFound %d suspect accounts:\n%s";
            List<String> offendingAccounts = moneyLaundering.getOffendingAccounts();
            String suspectAccounts = offendingAccounts.stream().reduce("", (s1, s2)-> s1 + "\n"+s2);
            message = String.format(message, moneyLaundering.amountOfFilesProcessed.get(), moneyLaundering.amountOfFilesTotal, offendingAccounts.size(), suspectAccounts);
            System.out.println(message);
        }
    }

    private static String getBanner()
    {
        String banner = "\n";
        try {
            banner = String.join("\n", Files.readAllLines(Paths.get("src/main/resources/banner.ascii")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return banner;
    }

    private static String getHelp()
    {
        String help = "Type 'exit' to exit the program. Press 'Enter' to get a status update\n";
        return help;
    }
}