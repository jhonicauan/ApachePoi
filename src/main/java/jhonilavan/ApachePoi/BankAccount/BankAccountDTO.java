package jhonilavan.ApachePoi.BankAccount;

import lombok.Data;

@Data
public class BankAccountDTO {
    private String owner;
    private String account;
    private float money;
    private float debt;
}
