package jhonilavan.ApachePoi.BankAccount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface IRepositoryBankAccount extends JpaRepository<ModelBankAccount,Long>{
    ModelBankAccount findByAccount(String account);
}
