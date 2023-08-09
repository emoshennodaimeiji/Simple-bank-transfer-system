<%--
  Created by IntelliJ IDEA.
  User: 19713
  Date: 2023/8/7
  Time: 16:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <!--<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">-->
    <title>银行账户转账</title>
  </head>
  <body>
    <form action="/bank/transfer" method="post">
      转出账户：<input type="text" name="fromAct"><br>
      转入账户：<input type="text" name="toAct"><br>
      转账金额：<input type="text" name="money"><br>
      <input type="submit" value="确定">
    </form>
  </body>
</html>
