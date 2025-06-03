package dao;

import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao implements IUserDao {
    private String jdbcURL = "jdbc:mysql://localhost:3306/demo?useSSL=false";
    private String jdbcUsername = "root";
    private String jdbcPassword = "123456";

    private static final String INSERT_USERS_SQL = "INSERT INTO users" + "(name, email, country) VALUES "+
        "(?, ?, ?)";
    private static final String SELECT_USER_BY_ID="SELECT id, name, email, country FROM users WHERE id = ?";
    private static final String SELECT_ALL_USERS="SELECT * FROM users";
    private static final String DELETE_USER_SQL="DELETE FROM users WHERE id = ?";
    private static final String UPDATE_USERS_SQL="UPDATE users SET name = ?, email = ?, country = ? WHERE id = ?";
    private static final String SELECT_USER_BY_COUNTRY="SELECT id, name, email, country FROM users WHERE country = ?";
    private static final String ORDER_BY_USER_NAME="ORDER BY name DESC";

    public UserDao() {
    }
    protected Connection getConnection()  {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
           e.printStackTrace();
        }
        return con;
    }

    @Override
    public void insertUser(User user) {
        System.out.println(INSERT_USERS_SQL);
        try {
            Connection con = getConnection();

            PreparedStatement ps = con.prepareStatement(INSERT_USERS_SQL);
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getCountry());
            System.out.println(ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    @Override
    public User selectUser(int id) {
        User user = null;
        Connection con = getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(SELECT_USER_BY_ID);
            ps.setInt(1, id);
            System.out.println(ps);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                user = new User(id,name, email, country);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return user;
    }

    @Override
    public List<User> selectAllUser() {
        List<User> users = new ArrayList<User>();

        try {
            Connection con = getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(SELECT_ALL_USERS);
        System.out.println(preparedStatement);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String email = rs.getString("email");
            String country = rs.getString("country");
            users.add(new User(id,name, email, country));
        }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return users;
    }

    @Override
    public boolean deleteUser(int id) throws SQLException {
       boolean rowDeleted ;
       Connection con = getConnection();
       PreparedStatement preparedStatement = con.prepareStatement(DELETE_USER_SQL);
       preparedStatement.setInt(1, id);
       System.out.println(preparedStatement);
       rowDeleted = preparedStatement.executeUpdate() > 0;
       return rowDeleted;
    }

    @Override
    public boolean updateUser(User user) throws SQLException {
       boolean rowUpdated ;
       Connection con = getConnection();
       PreparedStatement preparedStatement = con.prepareStatement(UPDATE_USERS_SQL);
       preparedStatement.setString(1, user.getName());
       preparedStatement.setString(2, user.getEmail());
       preparedStatement.setString(3, user.getCountry());
       preparedStatement.setInt(4, user.getId());

       rowUpdated = preparedStatement.executeUpdate() > 0;
       return rowUpdated;
    }
    private void printSQLException(SQLException ex) {
        for(Throwable e :ex){
            if(e instanceof SQLException){
                e.printStackTrace(System.err);
                System.err.println("SQLState: "+((SQLException)e).getSQLState());
                System.err.println("Error Code: "+((SQLException)e).getErrorCode());
                System.err.println("Message: "+e.getMessage());
                Throwable t = ex.getCause();
                while(t != null){
                    System.err.println("Cause: "+t);
                    t = t.getCause();
                }
            }
        }
    }

    @Override
    public List<User> selectUserByCountry(String country) throws SQLException {
        List<User> users = new ArrayList<>();
        Connection con = getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(SELECT_USER_BY_COUNTRY);
        preparedStatement.setString(1, "%"+country+"%");
        System.out.println(preparedStatement);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String email = rs.getString("email");

            users.add(new User(id,name, email, country));
        }
        return users;
    }

    @Override
    public void sortUsersByName() throws SQLException {
        Connection con = getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(ORDER_BY_USER_NAME);
        System.out.println(preparedStatement);
        ResultSet rs = preparedStatement.executeQuery();
    }
}
