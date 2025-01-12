package jhonilavan.ApachePoi.BankAccount;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Dados para criar uma conta no banco")
public class BankAccountDTO {
    private String owner;
    private String account;
    private float money;
    private float debt;
}
