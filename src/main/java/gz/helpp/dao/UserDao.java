package gz.helpp.dao;

import gz.helpp.model.ModelSession;
import gz.helpp.model.ModelUser;
import gz.helpp.utils.Hash256;
import javafx.util.Pair;

import java.sql.*;
import java.util.HashMap;

public class UserDao implements DAO<ModelUser>{
    private final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/e-market", "root", System.getenv("DBPWD"));

    public UserDao() throws SQLException {//empty cause of private field assignment connection on instance creation
    }

    @Override
    public void create(ModelUser modelUser) throws SQLException {
        String query = "INSERT INTO users (Nickname, Password, isFinancialAdvisor, email, balance) values (?,?,?,?,?);";
        PreparedStatement preparedStmt = null;

        try{
            preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, modelUser.getUserName());
            preparedStmt.setString(2, Hash256.hash256(modelUser.getPassword()));
            preparedStmt.setInt(3, ModelUser.toNumeralString(modelUser.getIsFinancialAdvisor()));
            preparedStmt.setString(4, modelUser.getEmail());
            preparedStmt.setDouble(5, modelUser.getBalance());
            preparedStmt.execute();
        }
        catch (Exception e){
            //handle exception
        }
        finally{
            if(preparedStmt != null){
                preparedStmt.close();
            }
        }

    }

    @Override
    public ModelUser read(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE Nickname = ?;";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String nickName = null;
        double balance = 0;
        Boolean isFinancialAdvisor = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            resultSet = statement.executeQuery();
            resultSet.next();
            nickName = resultSet.getString("Nickname");
            isFinancialAdvisor = resultSet.getBoolean("isFinancialAdvisor");
            balance = resultSet.getDouble("balance");
            resultSet.close();
        }
        catch(Exception e){
            //handle exception
        }
        finally {
            if (statement != null) {
                statement.close();
            }
        }



        HashMap<String, Pair<Double, Double>> portfolio = new HashMap<>();

        PreparedStatement statement1 = null;
        try {
            String sql1 = "SELECT * FROM portfolio WHERE username = ?;";
            statement1 = connection.prepareStatement(sql1);
            statement1.setString(1, username);
            ResultSet resultSet1 = statement1.executeQuery();

            while (resultSet1.next()) {
                Pair<Double, Double> pair = new Pair<>(resultSet1.getDouble("quantity"), resultSet1.getDouble("value"));
                portfolio.put(resultSet1.getString("ticker"), pair);
            }
            resultSet1.close();
            return new ModelUser(nickName, isFinancialAdvisor, portfolio, balance);
        }
        catch(Exception e)
        {
            //handle exception
        }
        finally{
            if(statement1 != null)
            {
                statement1.close();
            }
        }
        return null;
    }



    @Override
    public void update(ModelUser modelUser) throws SQLException {
        String updateSql = "UPDATE users SET balance=? WHERE Nickname=?";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(updateSql);
            preparedStatement.setDouble(1, modelUser.getBalance());
            preparedStatement.setString(2, modelUser.getUserName());
            preparedStatement.executeUpdate();
        }
        catch(Exception e) {
            ModelSession.getLogger().error("Model file, update method sql error", e);
        }
        finally{
            if(preparedStatement != null){
                preparedStatement.close();
            }
        }

        String username = modelUser.getUserName();
        modelUser.getPortfolio().forEach(
                (key, value)
                        -> {
                    try {
                        String insertSQL = "INSERT INTO portfolio (username, ticker, quantity, value) values (?, ?, ?, ?) ON DUPLICATE KEY UPDATE username = values(username), ticker = values(ticker), quantity = values(quantity), value = values(value);";
                        PreparedStatement preparedStatement1 = connection.prepareStatement(insertSQL);
                        preparedStatement1.setString(1, username);
                        preparedStatement1.setString(2, key);
                        preparedStatement1.setDouble(3, value.getKey());
                        preparedStatement1.setDouble(4, value.getValue());
                        preparedStatement1.executeUpdate();

                    } catch (SQLException e) {
                        ModelSession.getLogger().error("Model file, update method error", e);
                    }
                });
    }

    @Override
    public void delete(ModelUser modelUser) throws SQLException {
        PreparedStatement deleteUser = null;
        try {
            deleteUser = connection.prepareStatement("DELETE from users WHERE userId = ?");
            deleteUser.setInt(1, modelUser.getUserId());
            deleteUser.executeUpdate();
        }
        catch(Exception e){
            //handle exception
        }
        finally{
            if(deleteUser != null) {
                deleteUser.close();
            }
        }

        PreparedStatement deletePortfolio = null;
        try{
            deletePortfolio = connection.prepareStatement("DELETE from portfolio WHERE userId = ?");
            deletePortfolio.setInt(1, modelUser.getUserId());
            deletePortfolio.executeUpdate();
        }
        catch(Exception e){
            //handle exception
        }
        finally{
            if(deletePortfolio != null)
            {
                deletePortfolio.close();
            }
        }
    }

    public boolean checkLogIn(String user, String password) throws SQLException {
        PreparedStatement statement = null;
        boolean result = false;
        try {
            statement = connection.prepareStatement("SELECT * FROM users WHERE Nickname = ?");
            statement.setString(1, user);
            ResultSet resultSet = statement.executeQuery();



            if (resultSet.next()) {
                String pwdHash256 = Hash256.hash256(password);
                String passwordDb = resultSet.getString("password");
                if (pwdHash256.equals(passwordDb)) {
                    result = true;
                }
            }
            resultSet.close();
        }
        catch(Exception e){
            //handle exception
        }
        finally{
            if(statement != null) {
                statement.close();
            }
        }
        return result;
    }
}