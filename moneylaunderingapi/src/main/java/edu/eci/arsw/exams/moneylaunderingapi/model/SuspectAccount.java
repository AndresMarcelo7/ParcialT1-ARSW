package edu.eci.arsw.exams.moneylaunderingapi.model;

public class SuspectAccount {
    public String accountId;
    public int amountOfSmallTransactions;
    
     public SuspectAccount(String accountId, int amountOfSmallTransactions) {
        this.accountId = accountId;
        this.amountOfSmallTransactions = amountOfSmallTransactions;
    }

    public void updateAmount(int amount){
        amountOfSmallTransactions+=amount;
    }

    public String getId() {
        return accountId;
    }

    public int getAmountOfSmallTransactions() {
        return amountOfSmallTransactions;
    }
}
