<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="org.apache.commons.codec.binary.Base64" %>
<!DOCTYPE html>
<%
    String username = "";
    String password = "";
    String cookieName;
    Cookie[] cookies = request.getCookies();
    Base64 base64 = new Base64();
    if (cookies != null){
        for (Cookie cookie : cookies) {
            cookieName = cookie.getName(); //通过getName方法获得cookie的名称
            if (cookieName.equals("username")) {
                if (cookie.getValue() != null) {
                    username = username + new String(base64.decode(cookie.getValue()), "UTF-8"); //通过getValue方法获得cookie的值
                }
            } else if (cookieName.equals("password")) {
                if(cookie.getValue() != null) {
                    password = password + new String(base64.decode(cookie.getValue()), "UTF-8");
                }
            }
        }
    }
%>
<html>
<head>
    <meta charset="utf-8" />
    <title>经分销</title>
    <link rel="shortcut icon" type="image/x-icon" href="/resources/images/favicon.ico">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <meta content="A fully featured admin theme which can be used to build CRM, CMS, etc." name="description" />
    <meta content="Coderthemes" name="author" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <script src="/resources/assets/js/jquery.min.js" ></script>
    <script src="/resources/assets/js/popper.min.js" ></script>
    <script src="/resources/assets/js/bootstrap.min.js" ></script>
    <link href="/resources/assets/css/bootstrap.min.css"   rel="stylesheet" type="text/css" />
    <link href="/resources/assets/css/style.css"  rel="stylesheet" type="text/css" />
    <link href="/resources/assets/css/common.css"   rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href= "/resources/plugins/jqueryui/jquery-ui.min.css" rel="stylesheet" />

    <style>
        .tips{font-size: 12px;color: #f00;margin: 0 0 10px 0;}

        body,html{
            margin:0px;
            padding:0px;
            overflow-y: hidden;
        }
        .login-bg{
            background: url("/resources/images/login_bg.png") no-repeat;
            background-position: center center;
            background-attachment: fixed;
            background-size: cover;
            height:100vw;
            position: relative;
        }
        .login-info{
            width:19.5%;
            height:23.4%;
            position: absolute;
            top:28.7%;
            left:56.5%;
            margin-top:-16.9%;
        }
        .login-info .login-info-title{
            font-size: 26px;
            color: #333;
            letter-spacing: 3px;
        }
        .login-info .login-info-english{
            font-size: 12px;
            color: #c1c1c1;
            margin: 10px 0px 10px 2px;
        }
        .login-info-text{
            position:relative;
        }
        .login-info .login-info-text input{
            width: 100%;
            border: none;
            border-bottom:1px solid #eee;
            margin-top: 20px;
            padding: 10px 10px 10px 30px;
            color: #a9a9a9;
        }
        .login-info .login-info-text input:focus{
            outline:medium
        }
        .login-info .login-info-text .login-icon{
            width:20px;
            height:20px;
            position:absolute;
            top:25px;
            left:0px;
            overflow: hidden;
        }
        .login-info .login-icon img{
            width:100%;
        }
        .login-info .login-info-remember{
            font-size: 14px;
            color: #a9a9a9;
            margin-top:20px;
        }
        .login-info .login-info-btn button{
            height:45px;
            width:85%;
            border-radius:20px;
            background: #4f93fe;
            text-align: center;
            color: #fff;
            font-size: 18px;
            letter-spacing: 2px;
            margin-top:25px;
            border: none;
            cursor: pointer;
        }
        input:-webkit-autofill {
            -webkit-box-shadow: 0 0 0px 1000px white inset;
            -webkit-text-fill-color: #333;
        }

        button{
            outline:none
        }
    </style>
</head>


<body>

<div class="login-bg">
    <div class="login-info">
        <div class="login-info-title">经分销系统</div>
        <div class="login-info-english">DISTRIBUTION SYSTEM</div>
       <%--  <form id="loginForm" action="${pageContext.request.contextPath}/login/login.asyn" method="post"> --%>
            <div class="login-info-text">
                <input type="text" placeholder="请输入您的用户名" required=""
                       id="username" name="username" value="<%=username %>"/>
                <i class="login-icon">
                    <img src="/resources/images/login_info.png" alt="">
                </i>
            </div>
            <div class="login-info-text">
                <input type="password" placeholder="请输入登录密码" value="<%=password %>"
                       required="" name="password" id="password">
                <i class="login-icon">
                    <img src="/resources/images/login_pwd.png">
                </i>
            </div>
            <p class="tips" id="tips"></p>
            <div class="login-info-remember">
                <input type="checkbox" name="remember" checked="checked"/>记住账号和密码
            </div>
            <div class="login-info-btn">
               <button type="button" id="login-button">登录</button>
                <!-- <input type="button" value="登录" onclick="dologin()"/> -->
            </div>
      <!--   </form> -->
    </div>

</div>
<div style="position:fixed; bottom:5px;left: 45%;font-size: 12px;color: #F6F6F1;">
   粤ICP备19099077号-1 <a href="/resources/images/yy02.jpg" style="color: #F6F6F1;">营业执照</a>
</div>
<jsp:include page="/WEB-INF/views/modals/0002.jsp" />

<%--<script src='/resources/assets/js/jquery.min.js' ></script>--%>
<script type="text/javascript">
     var merchants = null;
     $("#login-button").click(function () {
         //禁用保存按钮
         $(this).attr('disabled',true);
         var username = $("input[name=username]").val();
         var password = $("input[name=password]").val();
         var remember = $("input[name=remember]").is(':checked');
         var url="/login/login.asyn";
         $.post(url,{username:username,password:password,remember:remember},function(result){
             if(result.state==200){
                 if (result.data && result.data.length > 1) {
                     //校验成功
                     merchants = result.data;
                     for (var i = 0; i<result.data.length; i++) {
                         var id = result.data[i].id;
                         var code = result.data[i].code;
                         var name = result.data[i].name;
                         $("#merchantDiv").append('<div class="col-6"><div class="row">' +
                             '<label style="height: 25px; line-height: 25px"><input type="radio" name="merchantId" ' +
                             'value="'+id+ '">' + code + '&nbsp;&nbsp;' + name +'</label></div></div>');
                     }
                     $("input[name=username]").val(username);
                     $("input[name=password]").val(password);
                     if (remember) {
                         $("input[name=remember]").attr("checked",true);
                     }
                     $m0002.init(username, password, remember);
                 } else {
                     window.location.href = "/index.html";
                 }
             }else{
                 //登录失败
                 $("#tips").html(result.message);
                 $(this).attr('disabled',false);
             }
         });
         setTimeout(function () {
             $("#login-button").attr('disabled',false);
         }, 2000);
     });

</script>
</body>

</html>
