package gz.helpp;

import io.jsondb.JsonDBTemplate;
import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;
import javafx.util.Pair;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitstamp.BitstampExchange;
import org.knowm.xchange.bitstamp.service.BitstampMarketDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ModelUser{
    private int userId;
    private String password;
    private String nickName;
    private boolean isFinancialAdvisor;
    private double balance;
    private Map<String, Pair<Double, Double>> portfolio;
    private String email;
    private List<BalanceObserver> observers = new ArrayList<>();

    public ModelUser( String nickName, boolean isFinancialAdvisor, Map<String, Pair<Double, Double>> portfolio, double balance){
        this.nickName = nickName;
        this.isFinancialAdvisor = isFinancialAdvisor;
        this.portfolio = portfolio;
        this.balance = balance;
    }

    public void setNickName(String nickName) {

        this.nickName = nickName;
    }

    public void setIsFinancialAdvisor(boolean isFinancialAdvisor){

        this.isFinancialAdvisor = isFinancialAdvisor;
    }

    public void setEmail(String email){

        this.email = email;
    }

    public void setBalance(double newBalance){
        this.balance = newBalance;
        notifyObservers(newBalance);
    }

    public void addObserver(BalanceObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(BalanceObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(double newBalance) {
        for (BalanceObserver observer : observers) {
            try{
                observer.balanceChanged(newBalance);
            }
            catch(Exception e){
                removeObserver(observer);
            }
        }
    }
    public void setPassword(String password){

        this.password = password;
    }
    public void setPortfolio(Map<String, Pair<Double, Double>> portfolio){

        this.portfolio = portfolio;
    }

    public void setUserId(int id){
        this.userId = id;
    }
    public int getUserId(){

        return this.userId;
    }

    public String getEmail(){

        return this.email;
    }
    public String getNickName(){

        return this.nickName;
    }

    public String getPassword(){

        return this.password;
    }
    public boolean getIsFinancialAdvisor(){

        return this.isFinancialAdvisor;
    }

    public Map<String, Pair<Double, Double>> getPortfolio(){

        return this.portfolio;
    }

    public double getBalance(){

        return this.balance;
    }

    public static int toNumeralString(final Boolean input) {

        return Boolean.TRUE.equals(input) ? 1 : 0;
    }
}

interface BalanceObserver {
    void balanceChanged(double newBalance);
}

class CryptoList{
    private String[][] supportedCryptoList;

    public CryptoList(){
        supportedCryptoList = new String[][]{
                {"BTC", "Bitcoin"},
                {"ETH", "Ethereum"},
                {"LTC", "Litecoin"},
                {"XRP", "Ripple"},
                {"BCH", "Bitcoin Cash"},
                {"USDT", "Tether"},
                {"ADA", "Cardano"},
                {"MATIC", "Polygon"},
                {"DOGE", "Dogecoin"},
                {"SOL", "Solana"}
        };
    }

    public String[][] getCryptoList(){
        return this.supportedCryptoList;
    }
}
class ModelCrypto{
    private final String ticker;
    private final String name;
    private Double price;


    public ModelCrypto(String ticker, String name){
        this.ticker = ticker;
        this.name = name;
    }
    public String getTicker(){

        return this.ticker;
    }

    public String getName(){

        return this.name;
    }

    public double getPrice(){

        return this.price;
    }

    public void setPrice(double price){
        this.price = price;
    }
}


interface Subject{

        public void register(Observer obj);
        public void unregister(Observer obj);

        public void notifyObservers() throws IOException;
}


class MyThread extends Thread{
    @Override
    public void run(){
        CryptoUpdater cryptoUpdater = CryptoUpdater.getInstance();
        while(true){
            synchronized (cryptoUpdater.lock) {
                while (cryptoUpdater.observers.isEmpty()) {
                    try {
                        cryptoUpdater.lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        e.printStackTrace();
                        e.printStackTrace();
                    }
                }
            }
            Exchange bitstamp = ExchangeFactory.INSTANCE.createExchange(BitstampExchange.class.getName());
            BitstampMarketDataService bitstampMarketDataService = (BitstampMarketDataService) bitstamp.getMarketDataService();
            cryptoUpdater.setState(bitstampMarketDataService);
            try {
                cryptoUpdater.notifyObservers();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000); //change it depending on your needs
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }
}

//class that handles updating cryyyypto
class CryptoUpdater implements Subject{ //singleton
    public final List<Observer> observers = new ArrayList<>();
    private static CryptoUpdater instance;
    public final Object lock = new Object();

    private MyThread myThread;
    private BitstampMarketDataService bitstampMarketDataService;
    private CryptoUpdater(){
        myThread = new MyThread();
        myThread.start();
    }
    public static CryptoUpdater getInstance(){
        if(instance == null){
            instance = new CryptoUpdater();
        }
        return instance;
    }

    public void setState(BitstampMarketDataService bitstampMarketDataService){

        this.bitstampMarketDataService = bitstampMarketDataService;
    }

    public BitstampMarketDataService getState(){

        return this.bitstampMarketDataService;
    }
    public synchronized void register(Observer obj){
        synchronized (lock) {
            observers.add(obj);
            lock.notifyAll();
        }
    }

    public synchronized void unregister(Observer obj){
        synchronized (lock) {
            observers.remove(obj);
        }

    }

    public synchronized void notifyObservers() throws IOException {
        for(Observer observer : observers){
            observer.update();
        }
    }
}

interface Observer{
    //method to update the observer, used by subject
    public void update() throws IOException;
}

class ModelSession {
    private static final Logger logger = LoggerFactory.getLogger(ModelSession.class);

    private static ModelSession instance;
    private ModelUser modelUser;

    private ModelSession() {}

    public static ModelSession getInstance() {
        if (instance == null) {
            instance = new ModelSession();
        }
        return instance;
    }

    public static Logger getLogger(){
        return logger;
    }

    public void setModelUser(ModelUser modelUser){
        this.modelUser = modelUser;
    }

    public ModelUser getModelUser(){
        return this.modelUser;
    }

}


@Document(collection = "transactions", schemaVersion = "1.0")
class ModelTransaction{
    @Id
    private int transactionId;
    private String usernameAssociated;
    private String cryptoTicker;
    private double quantity;
    private double price; //expressed in euro
    enum Type{
        BUY,SELL,DEPOSIT,WITHDRAW
    }
    private Type type;
    private Date date;

    public int getTransactionId() {
        return this.transactionId;
    }

    public String getUsernameAssociated(){
        return this.usernameAssociated;
    }

    public String getCryptoTicker(){
        return this.cryptoTicker;
    }

    public double getQuantity() {
        return this.quantity;
    }

    public Date getDate() {
        return this.date;
    }

    public double getPrice() {
        return price;
    }

    public Type getType() {
        return type;
    }

    public void setCryptoTicker(String cryptoTicker) {
        this.cryptoTicker = cryptoTicker;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setUsernameAssociated(String usernameAssociated) {
        this.usernameAssociated = usernameAssociated;
    }

    public void setTransactionId(int transactionId){
        this.transactionId = transactionId;
    }
}


interface DAO<T>{
    public void create(T t) throws SQLException;
    public T read(String user) throws SQLException;
    public void update(T t) throws SQLException;
    public void delete(T t) throws SQLException;
}

class UserDao implements DAO<ModelUser>{
    private final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/e-market", "root", System.getenv("DBPWD"));

    UserDao() throws SQLException {
    }

    @Override
    public void create(ModelUser modelUser) throws SQLException {
        String query = "INSERT INTO users (Nickname, Password, isFinancialAdvisor, email, balance) values (?,?,?,?,?);";
        PreparedStatement preparedStmt = null;

        try{
            preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, modelUser.getNickName());
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
            preparedStatement.setString(2, modelUser.getNickName());
            preparedStatement.executeUpdate();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally{
            if(preparedStatement != null){
                preparedStatement.close();
            }
        }

        String username = modelUser.getNickName();
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
                        e.printStackTrace();
                        e.printStackTrace();
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


class TransactionDAO implements DAO<ModelTransaction>{

    private final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/e-market", "admin", System.getenv("DBPWD"));
    private final Statement statement = connection.createStatement();

    TransactionDAO() throws SQLException {
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
            e.printStackTrace();
            e.printStackTrace();
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
        ResultSet resultSet = statement.executeQuery("SELECT * FROM transactions WHERE usernameAssociated=" + ModelSession.getInstance().getModelUser().getNickName() + ");");

        ArrayList<ModelTransaction> modelTransactionArray = new ArrayList<>();

        while(resultSet.next()) {
            ModelTransaction modelTransaction = new ModelTransaction();
            modelTransaction.setUsernameAssociated(resultSet.getString("usernameAssociated"));
            modelTransaction.setCryptoTicker(resultSet.getString("cryptoTicker"));
            modelTransaction.setQuantity(resultSet.getInt("quantity"));
            modelTransaction.setPrice(resultSet.getInt("price"));
            modelTransaction.setType(ModelTransaction.Type.valueOf(resultSet.getString("type")));
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

class TransactionDaoFile implements DAO<ModelTransaction>{

    String dbFilesLocation = System.getenv("FileDbLocation");

    String baseScanPackage = "com.example.helpp";

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



class Hash256 {
    private Hash256(){}
    public static String hash256(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}

class UnsupportedFunctionRuntimeException extends RuntimeException{
    public UnsupportedFunctionRuntimeException(String message){
        super(message);
    }
}