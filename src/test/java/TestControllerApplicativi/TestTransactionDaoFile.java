package TestControllerApplicativi;

import gz.helpp.dao.TransactionDaoFile;
import gz.helpp.model.ModelTransaction;
import gz.helpp.model.ModelTransactionType;
import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import java.sql.Date;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;


class TestTransactionDaoFile{

    @Test
    void TestTransactionDaoFileCreate() throws SQLException, IOException {
        Random rand = new Random();
        int randomTransactionId = rand.nextInt(400000000);
        int randomQuantity = rand.nextInt(2000);
        int randomPrice = rand.nextInt(100000);
        int randomYear = rand.nextInt(2000);
        int randomMonth = rand.nextInt(13);
        int randomDay = rand.nextInt(29);

        ModelTransaction modelTransaction = new ModelTransaction();
        modelTransaction.setTransactionId(randomTransactionId);
        modelTransaction.setType(ModelTransactionType.Type.BUY);
        modelTransaction.setCryptoTicker("Test");
        modelTransaction.setQuantity(randomQuantity);
        modelTransaction.setUsernameAssociated("Test");
        Date date = new Date(randomYear, randomMonth,randomDay);
        modelTransaction.setDate(date);
        modelTransaction.setPrice(randomPrice);

        TransactionDaoFile transactionDaoFile = new TransactionDaoFile();

        transactionDaoFile.create(modelTransaction);

        FileReader fileReader = new FileReader(System.getenv("FileDbLOCATION") + "\\transactions.json");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String searchString =  "\"transactionId\":" + randomTransactionId + ",\"usernameAssociated\":\"Test\",\"cryptoTicker\":\"Test\",\"quantity\":" + randomQuantity + ".0" + ",\"price\":" + randomPrice + ".0" + ",\"type\":\"BUY\",\"date\":" + date.getTime() + "}";
        boolean found = false;
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (line.contains(searchString)) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

}