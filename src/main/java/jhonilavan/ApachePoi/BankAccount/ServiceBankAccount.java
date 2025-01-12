package jhonilavan.ApachePoi.BankAccount;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.TableStyleInfo;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyleInfo;
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

    public String generateExcel(){

    //Cria o projeto do excel
    XSSFWorkbook workbook = new XSSFWorkbook();

    //Criar a planilha atual
    XSSFSheet sheet = workbook.createSheet("Tabelas");

    //Cria o cabeçalho da minha tabela
    XSSFRow headerRow = sheet.createRow(0);
    headerRow.createCell(0).setCellValue("CONTA");
    headerRow.createCell(1).setCellValue("PROPRIETARIO");
    headerRow.createCell(2).setCellValue("DINHEIRO");
    headerRow.createCell(3).setCellValue("DIVIDA");
    headerRow.createCell(4).setCellValue("SALDO ATUAL");

    //Cria uma cor para ser usada
    XSSFColor customRed = new XSSFColor();
    customRed.setARGBHex("A84F4F");

    //Costumiza uma fonte
    XSSFFont negativeFont = workbook.createFont();
    negativeFont.setColor(IndexedColors.WHITE.getIndex());

    //Cria um estilo para ser usado em celulas
    XSSFCellStyle negativeCellStyle = workbook.createCellStyle();
    negativeCellStyle.setFont(negativeFont);
    negativeCellStyle.setFillBackgroundColor(customRed);
    negativeCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    //Pega os usuarios do banco
    List<ModelBankAccount> bankAccounts = repositoryBankAccount.findAll();

    //Conta a quantidade de linhas
    int numrow = 1;
    for(ModelBankAccount account:bankAccounts){
        XSSFRow row = sheet.createRow(numrow++);
        //Adicionar os valores do banco de dados a seus respectivos lugares
        row.createCell(0).setCellValue((String) account.getAccount());
        row.createCell(1).setCellValue((String) account.getOwner());
        row.createCell(2).setCellValue((Float) account.getMoney());
        row.createCell(3).setCellValue((Float) account.getDebt());
        //Adiciona uma formula para a celula
        row.createCell(4).setCellFormula("C"+numrow+"-D"+numrow);

        //Pega a celula para caso precisemos
        XSSFCell cell = row.getCell(4);
        //Conferi se o saldo é negativo caso sim use o estilo alternativo
        float newMoney = account.getMoney() - account.getDebt();
        if(newMoney < 0){
            cell.setCellStyle(negativeCellStyle);
        }
    }

    //Defini onde começa e termina a tabela
    String range = "A1:E"+numrow;

    //Cria a area de referencia
    AreaReference areaReference = new AreaReference(range,workbook.getSpreadsheetVersion());

    //Cria a tabela
    XSSFTable table = sheet.createTable(areaReference);

    //Seta o nome e estilo da tabela
    table.setName("Informações bancarias");
    table.setDisplayName("Informações bancarias");
    table.setArea(areaReference);
    table.setStyleName("TableStyleMedium2");

    //Pega a tabela em sua forma mais bruta
    CTTable cttable = table.getCTTable();

    //adiciona filtro aos campos
    cttable.addNewAutoFilter();

    //Modifica o estilos das linhas e colunas
    CTTableStyleInfo styleInfo = cttable.getTableStyleInfo();
    styleInfo.setShowColumnStripes(false);
    styleInfo.setShowRowStripes(true);

    //Ajusta o tamanho das colunas
    for(int i=0;i<5;i++){
        sheet.autoSizeColumn(i);

        sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 600);
    }

    //Pega o caminho da pasta que deve salvar
    String path = getPath();

    //Salva na pasta
   try (FileOutputStream fileOut = new FileOutputStream(new File(path,"Relatorio.xlsx"))){
        workbook.write(fileOut);
    } catch (IOException e) {
        e.printStackTrace();
    }
    return "Excel criado";
    }

    public void accountExists(String account){
        ModelBankAccount bankAccount = repositoryBankAccount.findByAccount(account);
        if(bankAccount != null){
            throw new InvalidUniqueValue("Esta conta não esta dispónivel");
        }
    }

    public String getPath(){
        return System.getProperty("user.dir")+"/src/main/java/jhonilavan/ApachePoi/Excel/ExcelRelatory";
    }
}
