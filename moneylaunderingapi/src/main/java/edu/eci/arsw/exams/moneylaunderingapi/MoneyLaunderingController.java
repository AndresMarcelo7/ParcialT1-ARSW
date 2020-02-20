package edu.eci.arsw.exams.moneylaunderingapi;


import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import edu.eci.arsw.exams.moneylaunderingapi.service.MoneyLaunderingService;
import edu.eci.arsw.exams.moneylaunderingapi.service.SuspectAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = "/fraud-bank-accounts")
public class MoneyLaunderingController
{
    @Autowired
    MoneyLaunderingService moneyLaunderingService;


    @RequestMapping(method = GET)
    public ResponseEntity<?> offendingAccounts() {
        try {
            return new ResponseEntity<>(moneyLaunderingService.getSuspectAccounts(), HttpStatus.ACCEPTED);
        } catch (SuspectAccountException e) {
            return new ResponseEntity<>("ERROR 500", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value = "/{acId}", method = GET)
    public ResponseEntity<?> offendingAccountsById(@PathVariable("acId") String acId) {
        try {
            return new ResponseEntity<>(moneyLaunderingService.getAccountStatus(acId), HttpStatus.ACCEPTED);
        } catch (SuspectAccountException e) {
            return new ResponseEntity<>("ERROR 404", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = POST)
    @ResponseBody
    public ResponseEntity<?> registerAccount(@RequestBody SuspectAccount suspectAccount) {
        try {
            moneyLaunderingService.createAccount(suspectAccount);
            return new ResponseEntity<>("Nueva cuenta Registrada exitosamente",HttpStatus.ACCEPTED);
        } catch (SuspectAccountException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.ALREADY_REPORTED);
        }
    }

    @RequestMapping(value = "/{accountId}", method = PUT)
    @ResponseBody
    public ResponseEntity<?> updateAccount(@RequestBody SuspectAccount suspectAccount) {
        try {
            moneyLaunderingService.updateAccountStatus(suspectAccount);
            return new ResponseEntity<>("Actualizacion exitosa",HttpStatus.CREATED);
        } catch (SuspectAccountException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
