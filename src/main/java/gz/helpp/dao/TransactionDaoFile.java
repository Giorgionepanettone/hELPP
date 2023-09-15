package gz.helpp.dao;

import gz.helpp.exceptions.UnsupportedFunctionRuntimeException;

import gz.helpp.model.ModelTransaction;
import io.jsondb.JsonDBTemplate;

import java.sql.SQLException;

public class TransactionDaoFile implements DAO<ModelTransaction>{

    String dbFilesLocation = System.getenv("FileDbLocation");

    String baseScanPackage = "gz.helpp.model";

    JsonDBTemplate jsonDBTemplate = new JsonDBTemplate(dbFilesLocation, baseScanPackage, null);


    @Override
    public void create(ModelTransaction modelTransaction) throws SQLException {
        if(!jsonDBTemplate.collectionExists(ModelTransaction.class)){
            jsonDBTemplate.createCollection(ModelTransaction.class);
        }
        jsonDBTemplate.insert(modelTransaction);
    }

    @Override
    public ModelTransaction read(String user) throws SQLException {
        return jsonDBTemplate.findById(user, ModelTransaction.class);
    }

    @Override
    public void update(ModelTransaction modelTransaction) throws SQLException {
        throw new UnsupportedFunctionRuntimeException("update function not supported in transaction model");
    }

    @Override
    public void delete(ModelTransaction modelTransaction) throws SQLException {
        throw new UnsupportedFunctionRuntimeException("delete function not supported in transaction model");
    }
}