package gz.helpp.dao;

import gz.helpp.exceptions.UnsupportedFunctionRuntimeException;
import gz.helpp.model.ModelSession;
import gz.helpp.model.ModelTransaction;
import gz.helpp.model.ModelTransactionType;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO implements DAO<ModelTransaction>{

    private final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/e-market", "root", System.getenv("DBPWD"));
    public TransactionDAO() throws SQLException {//empty cause variables are initialized when instance is created
    }

    @Override
    public void create(ModelTransaction modelTransaction) throws SQLException {
        String query = "INSERT INTO transactions (usernameAssociated, cryptoTicker, date, quantity, price, type) VALUES (?,?,?,?,?,?)";
        PreparedStatement preparedStatement = null;

        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, modelTransaction.getUsernameAssociated());
            preparedStatement.setString(2, modelTransaction.getCryptoTicker());
            preparedStatement.setDate(3, modelTransaction.getDate());
            preparedStatement.setDouble(4, modelTransaction.getQuantity());
            preparedStatement.setDouble(5, modelTransaction.getPrice());
            preparedStatement.setString(6, modelTransaction.getType().toString());
            preparedStatement.executeUpdate();
        }
        catch(Exception e){
            ModelSession.getLogger().error("Model file, create method sql error", e);
        }
        finally{
            if(preparedStatement != null) preparedStatement.close();
        }

    }

    @Override
    public ModelTransaction read(String user){
        return null;
    }

    public List<ModelTransaction> read() throws SQLException {
        String query = "SELECT * FROM transactions WHERE usernameAssociated = ?";
        ResultSet resultSet = null;

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, ModelSession.getInstance().getModelUser().getUserName());
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            ModelSession.getLogger().error("sql error in read method in Model file", e);
        }
        finally{
            if(preparedStatement != null) preparedStatement.close();
        }

        ArrayList<ModelTransaction> modelTransactionArray = new ArrayList<>();
        if(resultSet == null) {
            ModelSession.getLogger().error("resultSet is null in TransactionDao.java, read method");
            throw new SQLException();
        }

        while(resultSet.next()) {
            ModelTransaction modelTransaction = new ModelTransaction();
            modelTransaction.setUsernameAssociated(resultSet.getString("usernameAssociated"));
            modelTransaction.setCryptoTicker(resultSet.getString("cryptoTicker"));
            modelTransaction.setQuantity(resultSet.getInt("quantity"));
            modelTransaction.setPrice(resultSet.getInt("price"));
            modelTransaction.setType(ModelTransactionType.Type.valueOf(resultSet.getString("type")));
            modelTransactionArray.add(modelTransaction);

        }
        resultSet.close();
        return modelTransactionArray;
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