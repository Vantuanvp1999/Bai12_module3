<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 6/3/2025
  Time: 11:31 AM
  To change this template use File | Settings | File Templates.
--%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>User Management</h1>
<h2><a href="users?action=users">List All Users</a></h2>
</center>
<div align="center">
    <form method="post">
        <table border="1" cellpadding="5">
            <caption><h2>Edit User</h2></caption>
            <c:if test="${user!=null}">
                <input type="hidden" name="id" value="<c:out value="${user.id}"/>"/>
            </c:if>
            <tr>
                <th>User Name:</th>
                <td>
                    <input type="text" name="name" id="name" value="<c:out value="${user.name}"/>" size="45"/>
                </td>
            </tr>
            <tr>
                <th>User Email:</th>
                <td>
                    <input type="text" name="email" value="<c:out value="${user.email}"/>" id="email" size="45"/>
                </td>
            </tr>
            <tr>
                <th>Country:</th>
                <td>
                    <input type="text" name="country" id="country" value="<c:out value="${user.country}"/>" size="45"/>
                </td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <input type="submit" value="save"/>
                </td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
