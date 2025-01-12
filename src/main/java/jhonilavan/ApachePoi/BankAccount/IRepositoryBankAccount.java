package jhonilavan.ApachePoi.BankAccount;

import org.springframework.data.jpa.repository.JpaRepository;


public interface IRepositoryBankAccount extends JpaRepository<ModelBankAccount,Long>{
    ModelBankAccount findByAccount(String account);
}
