<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 6/3/2025
  Time: 11:37 AM
  To change this template use File | Settings | File Templates.
--%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<center>
  <h1>User Management</h1>
  <h2>
    <a href="${pageContext.request.contextPath}/users?action=create">Add New User</a>
  </h2>
</center>
<div align="center">
  <form action="users" method="get">
    <input type="hidden" name="action" value="sort">
    <button type="submit">Sort Users</button>
  </form>
  <form method="get" action="${pageContext.request.contextPath}/users">
    <input type="hidden" value="search" name="action">
    <input type="text" name="country" id="country" size="45">
    <input type="submit" value="search">
  </form>
  <table border="1" cellpadding="5">
    <caption><h2>List of Users</h2></caption>
    <tr>
      <th>ID</th>
      <th>Name</th>
      <th>Email</th>
      <th>Country</th>
      <th>Actions</th>
    </tr>
    <c:forEach var="user" items="${listUser}">
      <tr>
        <td><c:out value="${user.id}"/></td>
        <td><c:out value="${user.name}"/></td>
        <td><c:out value="${user.email}"/></td>
        <td><c:out value="${user.country}"/></td>
        <td>
          <a href="/users?action=edit&id=${user.id}">Edit</a>
          <a href="/users?action=delete&id=${user.id}">delete</a>
        </td>
      </tr>
    </c:forEach>
  </table>
</div>
</body>
</html>
