package jhonilavan.ApachePoi.BankAccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/account")
public class ControllerBankAccount {

    @Autowired
    private ServiceBankAccount service;

    @Operation(summary = "Adicionar nova conta",description = "Adiciona uma nova conta no banco")
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

    @Operation(summary = "Gerar relatorio",description = "Pega as contas no banco de dados e defini como esta seu saldo em relação a divida gerando uma tabela excel")
    @GetMapping("/excel")
    public String excel(){
        return service.generateExcel();
    }
}
