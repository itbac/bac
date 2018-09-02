<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>单点登录系统测试-1</title>
</head>
<body>
<h1 style="background-color: yellow">
    欢迎您!<%=request.getRemoteUser()%> 访问本系统CAS111111111111111,热烈欢迎.
</h1>
<hr size="2" color="blue">
<a href="http://192.168.66.67:9100/cas/logout?service=http://www.jd.com">单点退出功能</a>
<hr>
<a href="http://localhost:7002">去页面二</a>
</body>
</html>
