package jhonilavan.ApachePoi.BankAccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class ControllerBankAccount {

    @Autowired
    private ServiceBankAccount service;

    @PostMapping("/add")
    public ResponseEntity addAccount(@RequestBody BankAccountDTO bankAccountDTO){
        try{
            ModelBankAccount bankAccount = service.addAccount(bankAccountDTO);
            return ResponseEntity.accepted().body(bankAccount);
        }catch(RuntimeException e){
            String text = e.getLocalizedMessage();
            return ResponseEntity.badRequest().body(text);
        }
    }
}
