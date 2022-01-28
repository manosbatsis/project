<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<html>
<body>
<h2>Hello World!</h2>


----------------------------------------<br>
   测试按钮权限:
    
     <shiro:hasPermission name="url_agents_add">
             <input type="button" value="新增">              
     </shiro:hasPermission>
</body>
</html>
