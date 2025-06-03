package dao;

import model.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserDao {
    public void insertUser(User user) throws SQLException;
    public User selectUser(int id) ;
    public List<User> selectAllUser()  ;
    public boolean deleteUser(int id) throws SQLException;
    public boolean updateUser(User user) throws SQLException;
    public List<User> selectUserByCountry(String country) throws SQLException;
    public List<User> sortUsersByName() throws SQLException;
}
