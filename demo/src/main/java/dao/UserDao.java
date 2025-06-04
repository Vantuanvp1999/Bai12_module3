package dao;

import model.User;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
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
    private static final String ORDER_BY_USER_NAME="SELECT * FROM users ORDER BY name ASC ";
    private static final String SQL_INSERT = "INSERT INTO EMPLOYEE (NAME, SALARY, CREATED_DATE) VALUES (?,?,?)";
    private static final String SQL_UPDATE = "UPDATE EMPLOYEE SET SALARY=? WHERE NAME=?";
    private static final String SQL_TABLE_CREATE = "CREATE TABLE EMPLOYEE"
            + "("
            + " ID serial,"
            + " NAME varchar(100) NOT NULL,"
            + " SALARY numeric(15, 2) NOT NULL,"
            + " CREATED_DATE timestamp,"
            + " PRIMARY KEY (ID)"
            + ")";
    private static final String SQL_TABLE_DROP = "DROP TABLE IF EXISTS EMPLOYEE";

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
        preparedStatement.setString(1, country.trim());
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
    public List<User> sortUsersByName() throws SQLException {
        List<User> users = new ArrayList<>();
        Connection con = getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(ORDER_BY_USER_NAME);
        System.out.println(preparedStatement);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String email = rs.getString("email");
            String country = rs.getString("country");
            users.add(new User(id,name, email, country));
        }
        return users;
    }

    @Override
    public User getUserById(int id) {
        User user = null;
        String query= "{CALL get_user_by_id(?)}";

        try {
            Connection con = getConnection();
            CallableStatement callableStatement= con.prepareCall(query);
            callableStatement.setInt(1, id);
            ResultSet rs = callableStatement.executeQuery();
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
    public void insertUserStore(User user) throws SQLException {
        String query = "{CALL insert_user(?,?,?)}";
        try{
            Connection con = getConnection();
            CallableStatement callableStatement = con.prepareCall(query);
            callableStatement.setString(1, user.getName());
            callableStatement.setString(2, user.getEmail());
            callableStatement.setString(3, user.getCountry());
            System.out.println(callableStatement);
            callableStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    @Override
    public void addUserTransaction(User user, List<Integer> permission) {
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmtAssignment = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            con.setAutoCommit(false);
            pstmt = con.prepareStatement(INSERT_USERS_SQL, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getCountry());
            int rowsAffected = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            int userId = 0;
            if (rs.next()) {
                userId = rs.getInt(1);
            }
            if (rowsAffected == 1 ) {
                String sql = "INSERT INTO user_permision (permision_id, user_id) VALUES (?, ?)";
                pstmtAssignment = con.prepareStatement(sql);
                for(int permissionId : permission) {
                    pstmtAssignment.setInt(2,userId);
                    pstmtAssignment.setInt(1, permissionId);
                    pstmtAssignment.executeUpdate();
                }
                con.commit();
            }else{
                con.rollback();
            }
        } catch (SQLException e) {
            try {
                if(con!=null)
                    con.rollback();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            System.out.println(e.getMessage());

        }finally{
            try{
                if(rs!=null) rs.close();
                if(pstmt!=null) pstmt.close();
                if(pstmtAssignment != null) pstmtAssignment.close();
                if(con != null) con.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());;
            }
        }

    }

    @Override
    public void insertUpdateWithoutTransaction() {
    try(
        Connection con = getConnection();
        Statement statement = con.createStatement();
        PreparedStatement psInsert = con.prepareStatement(SQL_INSERT);
        PreparedStatement psUpdate = con.prepareStatement(SQL_UPDATE)){
        statement.execute(SQL_TABLE_DROP);
        statement.execute(SQL_TABLE_CREATE);

        psInsert.setString(1,"Quynh");
        psInsert.setBigDecimal(2, new BigDecimal(10));
        psInsert.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
        psInsert.executeUpdate();

        psInsert.setString(1,"Ngan");
        psInsert.setBigDecimal(2, new BigDecimal(20));
        psInsert.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
        psInsert.executeUpdate();

        psUpdate.setBigDecimal(2, new BigDecimal(999.99));
        psUpdate.setString(2,"Quynh");
        psUpdate.executeUpdate();
    } catch (SQLException e) {
       e.printStackTrace();
    }
    }

    @Override
    public void insertUpdateUseTransaction() {
        try(Connection conn =getConnection();
            Statement statement = conn.createStatement();
            PreparedStatement psInsert = conn.prepareStatement(SQL_INSERT);
            PreparedStatement psUpdate = conn.prepareStatement(SQL_UPDATE)
        ) { statement.executeUpdate(SQL_TABLE_DROP);
            statement.executeUpdate(SQL_TABLE_CREATE);

            conn.setAutoCommit(false);
            psInsert.setString(1,"Quynh");
            psInsert.setBigDecimal(2, new BigDecimal(10));
            psInsert.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            psInsert.executeUpdate();
            psInsert.setString(1,"Ngan");
            psInsert.setBigDecimal(2, new BigDecimal(20));
            psInsert.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            psInsert.executeUpdate();
            psUpdate.setBigDecimal(2, new BigDecimal(999.99));
            psUpdate.setString(2,"Quynh");
            psUpdate.executeUpdate();
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<User> selectAllUserUseProcedure() {
        List<User> users = new ArrayList<>();
        String query = "{CALL select_all_users()}";
        try {
            Connection con = getConnection();
            CallableStatement callableStatement = con.prepareCall(query);
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                users.add(new User(name, email, country));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public boolean deleteUserUseProcedure(int id) {
        boolean rowUpdated = false;
        String query = "{CALL delete_user_by_id(?)}";
        try {
            Connection con = getConnection();
            CallableStatement callableStatement= con.prepareCall(query);
            callableStatement.setInt(1,id);
            rowUpdated= callableStatement.executeUpdate() >0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowUpdated;
    }

    @Override
    public boolean updateUserUseProcedure(User user) throws SQLException {
        boolean rowUpdated = false;
        String query = "{CALL update_user_by_id(?,?,?,?)}";
        try{
            Connection con = getConnection();
            CallableStatement callableStatement = con.prepareCall(query);
            callableStatement.setString(1,user.getName());
            callableStatement.setString(2,user.getEmail());
            callableStatement.setString(3,user.getCountry());
            callableStatement.setInt(4,user.getId());
            rowUpdated = callableStatement.executeUpdate() >0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowUpdated;
    }

}
