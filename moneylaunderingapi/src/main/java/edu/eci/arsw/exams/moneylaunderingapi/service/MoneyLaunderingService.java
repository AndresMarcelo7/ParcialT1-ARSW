package edu.eci.arsw.exams.moneylaunderingapi.service;

import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;

import java.util.List;

public interface MoneyLaunderingService {
    void updateAccountStatus(SuspectAccount suspectAccount) throws SuspectAccountException;
    SuspectAccount getAccountStatus(String accountId) throws SuspectAccountException;
    List<SuspectAccount> getSuspectAccounts() throws SuspectAccountException;
    public void createAccount(SuspectAccount suspectAccount)throws SuspectAccountException;
}
