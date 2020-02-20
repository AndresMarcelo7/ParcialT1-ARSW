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
        SuspectAccount cuenta1=new SuspectAccount("cuenta1",400);
        SuspectAccount cuenta2=new SuspectAccount("cuenta2",401);
        SuspectAccount cuenta3=new SuspectAccount("cuenta3",500);
        SuspectAccount cuenta4=new SuspectAccount("cuenta4",501);
        SuspectAccount cuenta5=new SuspectAccount("cuenta5",600);
        sospechosas.add(cuenta1);
        sospechosas.add(cuenta2);
        sospechosas.add(cuenta3);
        sospechosas.add(cuenta4);
        sospechosas.add(cuenta5);
    }
    @Override
    public SuspectAccount getAccountStatus(String accountId) throws SuspectAccountException {
        for (SuspectAccount cuenta:sospechosas) {
            if (cuenta.getId().equals(accountId)) {
                return cuenta;
            }
        }
        throw new SuspectAccountException("No se encontro la Cuenta Sospechosa");
    }

    @Override
    public void updateAccountStatus(SuspectAccount suspectAccount) throws SuspectAccountException {
        try{
            SuspectAccount cuenta=getAccountStatus(suspectAccount.getId());
            cuenta.updateAmount(suspectAccount.getAmountOfSmallTransactions());
        } catch (SuspectAccountException e) {
            throw e;
        }
    }

    @Override
    public void createAccount(SuspectAccount suspectAccount)throws SuspectAccountException {
        for (SuspectAccount cuenta :sospechosas) {
            if (cuenta.getId().equals(suspectAccount.getId())) {
                throw new SuspectAccountException("La cuenta ya se encuentra  registrada en el sistema,  haga PUT para actualizar");
            }
        }
        sospechosas.add(suspectAccount);
    }

    @Override
    public List<SuspectAccount> getSuspectAccounts() {
        return sospechosas;
    }
}
