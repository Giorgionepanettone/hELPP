package gz.helpp.dao;

import java.sql.SQLException;

public interface DAO<T>{
    public void create(T t) throws SQLException;
    public T read(String user) throws SQLException;
    public void update(T t) throws SQLException;
    public void delete(T t) throws SQLException;
}