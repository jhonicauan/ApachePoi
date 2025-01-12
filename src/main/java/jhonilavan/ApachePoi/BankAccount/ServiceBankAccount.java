package jhonilavan.ApachePoi.BankAccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jhonilavan.ApachePoi.Error.InvalidUniqueValue;

@Service
public class ServiceBankAccount {

    @Autowired
    private IRepositoryBankAccount repositoryBankAccount;

    public ModelBankAccount addAccount(BankAccountDTO bankAccountDTO){
        String account = bankAccountDTO.getAccount();
        accountExists(account);
        ModelBankAccount newAccount = new ModelBankAccount();
        newAccount.setAccount(account);
        newAccount.setDebt(bankAccountDTO.getDebt());
        newAccount.setMoney(bankAccountDTO.getMoney());
        newAccount.setOwner(bankAccountDTO.getOwner());
        return repositoryBankAccount.save(newAccount);
    }

    public void accountExists(String account){
        ModelBankAccount bankAccount = repositoryBankAccount.findByAccount(account);
        if(bankAccount != null){
            throw new InvalidUniqueValue("Esta conta não esta dispónivel");
        }
    }
}
