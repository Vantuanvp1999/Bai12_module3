<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 6/3/2025
  Time: 11:23 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<center>
<h1>User Management</h1>
<h2><a href="users?action=users">List All Users</a></h2>
</center>
<div align="center">
  <form method="post">
    <table border="1" cellpadding="5">
      <caption>Add New User</caption>
      <tr>
        <th>User Name:</th>
        <td>
          <input type="text" name="name" id="name" size="45"/>
        </td>
      </tr>
      <tr>
        <th>User Email:</th>
        <td>
          <input type="text" name="email" id="email" size="45"/>
        </td>
      </tr>
      <tr>
        <th>Country:</th>
        <td>
          <input type="text" name="country" id="country" size="45"/>
        </td>
      </tr>
      <tr>
        <th>Permission:</th>
        <td>
          <input type="checkbox" name="add" size="15"/> add|
          <input type="checkbox" name="edit" size="15"/> edit|
          <input type="checkbox" name="delete" size="15"/> delete|
          <input type="checkbox" name="view" size="15"/> view|

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
