package jhonilavan.ApachePoi.BankAccount;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "bankaccount")
public class ModelBankAccount {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long idAccount;
    private String account;
    private String owner;
    private float money;
    private float debt;
}
