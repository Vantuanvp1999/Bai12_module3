package controller;

import dao.IUserDao;
import dao.UserDao;
import model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "UserServlet", urlPatterns = "/users")
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private IUserDao userDao;

    public void init() {
        userDao = new UserDao();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) {
            action = "";
        }
        try {
            switch (action) {
                case "create":

                    insertUser(req, resp);

                    break;
                case "edit":
                    updateUser(req, resp);
                    break;
                case "search":
                    searchUser(req, resp);
                    break;
                case "sort":
                    sortUser(req, resp);
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertUser(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String country = req.getParameter("country");

        String add = req.getParameter("add");
        String edit = req.getParameter("edit");
        String delete = req.getParameter("delete");
        String view = req.getParameter("view");

        List<Integer> permission = new ArrayList<>();
        if (add != null) {
            permission.add(1);
        }
        if (edit != null) {
            permission.add(2);
        }
        if (delete != null) {
            permission.add(3);
        }
        if (view != null) {
            permission.add(4);
        }
        User newUser = new User(name, email, country);
        //userDao.insertUser(newUser);
        //userDao.insertUserStore(newUser);
        userDao.addUserTransaction(newUser, permission);
        RequestDispatcher dispatcher = req.getRequestDispatcher("user/create.jsp");
        try {
            dispatcher.forward(req, resp);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateUser(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String country = req.getParameter("country");
        User newUser = new User(id, name, email, country);
        //userDao.updateUser(newUser);
        userDao.updateUserUseProcedure(newUser);
        resp.sendRedirect(req.getContextPath() + "/users");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) {
            action = "";
        }
        try {
            switch (action) {
                case "create":
                    showNewForm(req, resp);
                    break;
                case "edit":
                    showEditForm(req, resp);
                    break;
                case "delete":
                    deleteUser(req, resp);
                    break;
                case "search":
                    searchUser(req, resp); // 👈 Thêm dòng này!
                    break;
                case "sort":
                    sortUser(req, resp);
                    break;
                case "test-without-tran":
                    testWithoutTran(req, resp);
                    break;
                case "test-use-tran":
                    testUseTran(req, resp);
                    break;
                default:
                    listUser(req, resp);
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void testUseTran(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        userDao.insertUpdateUseTransaction();
    }

    private void testWithoutTran(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        userDao.insertUpdateWithoutTransaction();
    }

    private void showNewForm(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("user/create.jsp");
        try {
            dispatcher.forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        //User existingUser = userDao.selectUser(id);
        User existingUser = userDao.getUserById(id);
        req.setAttribute("user", existingUser);
        RequestDispatcher dispatcher = req.getRequestDispatcher("user/edit.jsp");
        try {
            dispatcher.forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    private void deleteUser(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        //userDao.deleteUser(id);
            userDao.deleteUserUseProcedure(id);
        List<User> listUser = userDao.selectAllUser();
        req.setAttribute("listUser", listUser);
        RequestDispatcher dispatcher = req.getRequestDispatcher("user/list.jsp");
        try {
            dispatcher.forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    private void listUser(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        //List<User> listUser = userDao.selectAllUser();
        List<User> listUser = userDao.selectAllUserUseProcedure();
        req.setAttribute("listUser", listUser);
        RequestDispatcher dispatcher = req.getRequestDispatcher("user/list.jsp");
        try {
            dispatcher.forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    private void searchUser(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        String country = req.getParameter("country");
        List<User> users;
        if (country != null && !country.trim().isEmpty()) {
            users = userDao.selectUserByCountry(country);
            ; // Gọi service tìm theo country
        } else {
            users = userDao.selectAllUser(); // Nếu không nhập country thì lấy toàn bộ
        }

        req.setAttribute("listUser", users);
        RequestDispatcher dispatcher = req.getRequestDispatcher("user/search.jsp");
        try {
            dispatcher.forward(req, resp);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }

    private void sortUser(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        List<User> listUser = userDao.sortUsersByName();
        req.setAttribute("listUser", listUser);
        RequestDispatcher dispatcher = req.getRequestDispatcher("user/sort.jsp");
        try {
            dispatcher.forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }
}
