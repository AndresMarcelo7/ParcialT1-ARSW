package edu.eci.arsw.exams.moneylaunderingapi.service;

import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class MoneyLaunderingServiceStub implements MoneyLaunderingService {
    List<SuspectAccount> sospechosas;
    public MoneyLaunderingServiceStub(){
        //Load Stub Data
        sospechosas=new CopyOnWriteArrayList<>();
        SuspectAccount a1=new SuspectAccount("cuenta1",400);
        SuspectAccount a2=new SuspectAccount("cuenta2",401);
        SuspectAccount a3=new SuspectAccount("cuenta3",402);
        sospechosas.add(a1);
        sospechosas.add(a2);
        sospechosas.add(a3);
    }

    @Override
    public void updateAccountStatus(SuspectAccount suspectAccount) throws SuspectAccountException {
        try{
            SuspectAccount cuenta=getAccountStatus(suspectAccount.getAccountId());
            cuenta.updateAmount(suspectAccount.getAmountOfSmallTransactions());
        } catch (SuspectAccountException e) {
            throw e;
        }
    }

    @Override
    public SuspectAccount getAccountStatus(String accountId) throws SuspectAccountException {
        for (SuspectAccount cuenta:sospechosas) {
            if (cuenta.getAccountId().equals(accountId)) {
                return cuenta;
            }
        }
        throw new SuspectAccountException("No se encontro la Cuenta Sospechosa");

    }

    @Override
    public List<SuspectAccount> getSuspectAccounts()throws SuspectAccountException {
        return sospechosas;
    }

    @Override
    public void createAccount(SuspectAccount suspectAccount)throws SuspectAccountException {
        for (SuspectAccount cuenta :sospechosas) {
            if (cuenta.getAccountId().equals(suspectAccount.getAccountId())) {
                throw new SuspectAccountException("La cuenta ya se encuentra  registrada en el sistema, PUT para actualizar");
            }
        }
        sospechosas.add(suspectAccount);
    }
}
