<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>

<head>
    <!-- App favicon -->
    <link rel="shortcut icon" href='<c:url value="/resources/assets/images/favicon.ico" />'>
    <!-- DataTables -->
    <link href='<c:url value="/resources/plugins/datatables/dataTables.bootstrap4.min.css" />' rel="stylesheet"
        type="text/css" />
    <link href='<c:url value="/resources/plugins/datatables/buttons.bootstrap4.min.css" />' rel="stylesheet"
        type="text/css" />
    <!-- Responsive datatable examples -->
    <link href='<c:url value="/resources/plugins/datatables/responsive.bootstrap4.min.css" />' rel="stylesheet"
        type="text/css" />
    <link href='<c:url value="/resources/plugins/datetimepicker/bootstrap-datetimepicker.min.css" />' rel="stylesheet"
        type="text/css" />
    <!-- App css -->
    <link href='<c:url value="/resources/assets/css/bootstrap.min.css" />' rel="stylesheet" type="text/css" />
    <link href='<c:url value="/resources/assets/css/icons.css" />' rel="stylesheet" type="text/css" />
    <link href='<c:url value="/resources/assets/css/metismenu.min.css" />' rel="stylesheet" type="text/css" />
    <link href='<c:url value="/resources/assets/css/style.css" />' rel="stylesheet" type="text/css" />
    <link href='<c:url value="/resources/assets/css/common.css" />' rel="stylesheet" type="text/css" />
    <!-- Custom box css -->
    <link href='<c:url value="/resources/plugins/custombox/css/custombox.min.css" />' rel="stylesheet">
    <!-- Sweet Alert css -->
    <link href='<c:url value="/resources/plugins/sweet-alert/sweetalert2.min.css" />' rel="stylesheet"
        type="text/css" />
    <!-- Plugins css   icon-->
    <link href='<c:url value="/resources/plugins/bootstrap-tagsinput/css/bootstrap-tagsinput.css" />'
        rel="stylesheet" />
    <link href='<c:url value="/resources/plugins/bootstrap-select/css/bootstrap-select.min.css" />' rel="stylesheet" />
    <link href='<c:url value="/resources/plugins/select2/css/select2.min.css" />' rel="stylesheet" type="text/css" />
    <link href='<c:url value="/resources/plugins/bootstrap-fileupload/bootstrap-fileupload.css" />' rel="stylesheet"
        type="text/css" />
    <link href='<c:url value="/resources/plugins/bootstrap-touchspin/css/jquery.bootstrap-touchspin.min.css" />'
        rel="stylesheet" />
    <link rel="stylesheet" href='<c:url value="/resources/plugins/switchery/switchery.min.css" />' rel="stylesheet" />
    <!-- jqueryui -->
    <link rel="stylesheet" href='<c:url value="/resources/plugins/jqueryui/jquery-ui.min.css" />' rel="stylesheet" />
    <!-- zTree  -->
    <link rel="stylesheet" href='<c:url value="/resources/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css" />'
        rel="stylesheet" />
    <!-- Private css -->
    <link href='<c:url value="/resources/css/typ.css" />' rel="stylesheet" type="text/css" />' />
    <!-- 自定义分页插件 css -->
    <link href='<c:url value="/resources/css/pagination.css" />' rel="stylesheet" type="text/css" />' />
    <!-- multi-select -->
    <link rel="stylesheet" href='<c:url value="/resources/plugins/multi-select/css/multi-select.css" />'
        rel="stylesheet" />
    <link rel="stylesheet" href='<c:url value="/resources/plugins/step/jquery.step.css"/>'>
</head>
<style>
    .topbar-left {
        background: #6ea9f3;

    }

    .topbar-left span {
        font-size: 24px;
    }

    .navbar-custom {
        background: #6ea9f3;
    }

    .side-menu {
        background: #FFFFFF !important;
        width: 200px;
    }

    #sidebar-menu>ul>li>a>span {
        /*color: #3488CA;*/
        font-size: 14px;
    }

    #sidebar-menu ul li a i {
        color: #3488CA;
    }

    .nav-second-level>li>a {
        color: #3488CA;
        font-size: 13px;
    }

    .nav-second-level>li>a:hover,
    .nav-second-level>li>a:focus {
        background: #F2F3F9;
        color: #5fbaff;
    }

    #sidebar-menu>ul>li>a:hover,
    #sidebar-menu>ul>li>a:focus,
    #sidebar-menu>ul>li>a:active,
    #sidebar-menu>ul>li>a.active {
        background: #F2F3F9;
        /*color: #fff;*/
    }

    .enlarged #wrapper .left.side-menu #sidebar-menu>ul>li :hover,
    .enlarged #wrapper .left.side-menu #sidebar-menu>ul>li :hover+ul {
        background: #F2F3F9;
    }

    .enlarged #wrapper .left.side-menu #sidebar-menu ul>li a:hover {
        background: #F2F3F9;
        color: #5fbaff !important;
    }

    #sidebar-menu>ul>.active>a span,
    #sidebar-menu>ul>.active>a i {
        color: #5fbaff;
    }

    .message {
        position: relative;
        
    }

    .message .message-badge {
        position: absolute;
        right: 0;
        top: 15px;
        background: #FF816A;
        border-radius: 50%;
        width: 18px;
        height: 18px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #FFFFFF;
        font-size: 12px;
    }

    .message-detail {
        position: absolute;
        top: 70px;
        right: 10%;
        width: 337px;
        background-color: #FFFFFF;
        display: none;
        border: 1px solid rgba(0, 0, 0, 0.15);
        border-radius: 0.5rem;
    }

    .message-detail .system-message {
        height: 40px;
        line-height: 40px;
        font-size: 14px;
        padding-left: 12px;
        border-bottom: 0.1px solid #ddd;
    }

    .message-list {
    	height: 220px !important;
        margin: 0;
    }

    .message-list li {
    	position:relative;
        padding: 0 8px 0 12px;
        width: 337px;
        height: 44px;
        line-height: 44px;
        display: flex;
        justify-content: space-between;
    }

    .message-list li:hover {
        background: #F5F6FF;
    }
    .message-list li:hover span{
    	color: #75A7D8;
    }
    /* .message-list li:hover .tip{
    	color:red;
    } */
	.message-list li .tip{
        top: 15px;
        background: #FF816A;
        border-radius: 50%;
        width: 9px;
        height: 9px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 12px;
	}
	.message-list li span{
		color: #626D76;
		display:block;
        float:left;
	}
	.message-list li .message-time{
		color:#BCBCE5;
		margin-left: 10px;
        font-size: 14px;
	}
    .loadmore {
        width: 320px;
        height: 30px;
        line-height: 30px;
        text-align: center;
        color: #75A7D8;
        font-size:14px;
    }
    .message-title{
        width: 200px;
        white-space:nowrap;
        text-overflow:ellipsis;
        overflow:hidden;
        margin-left: 5px;
    }
    .message-tip{
        width: 9px;
        margin-top: 6%;
        display:block;
        float:left;
    }
</style>
<!-- Top Bar Start -->
<div class="topbar">
    <!-- LOGO -->
    <div class="topbar-left">
        <a href="index.html" class="logo">
            <span>
                <div id="systemName"></div>
            </span>
            <i>
                <img src='<c:url value="/resources/assets/images/logo_sm.png" />' alt="" height="28">
            </i>
        </a>
    </div>

    <nav class="navbar-custom">
        <!-- ==============================================右上角导航栏  开始=========================================================-->
        <ul class="list-unstyled topbar-right-menu float-right mb-0">
            <li class="dropdown notification-list">

                <div class="nav-link dropdown-toggle waves-effect waves-light nav-user message" data-toggle="" href="#"
                    role="button" aria-haspopup="false" aria-expanded="false" id="message">
                    <img src='<c:url value="/resources/assets/images/users/message.png" />'
                        class="rounded-circle message-circle">
                    <div class="message-badge" style="display:none;">
                        
                    </div>

                </div>
                <div class="message-detail">
                    <div class="system-message">系统公告<span class="noticeAccount"></span></div>
                    <ul class="message-list clearfix slimscroll-message">
                    </ul>
                    <div class="loadmore"><a href="#">更多</a></div>
                </div>
                <a class="nav-link dropdown-toggle waves-effect waves-light nav-user user" data-toggle="" href="#"
                    role="button" aria-haspopup="false" aria-expanded="false" style="width:200px;">
                    <img src='<c:url value="/resources/assets/images/users/default.png" />' alt="user"
                        class="rounded-circle user-circle"> <span class="ml-1">
                        <shiro:principal property="name" /><i class="mdi mdi-chevron-down" style="position:absolute;right: 5px"></i> </span>
                </a>
                <div class="dropdown-menu dropdown-menu-right profile-dropdown user-detail" style="margin-left:30%;">
                    <!-- item-->
                    <div class="dropdown-item noti-title">
                        <h6 class="text-overflow m-0">Welcome !</h6>
                    </div>

                    <!-- item-->
                    <a href="javascript:void(0);" class="dropdown-item notify-item">
                        <i class="fi-head"></i> <span>个人信息</span>
                    </a>

                    <!-- item-->
                    <%--<a href="javascript:void(0);" class="dropdown-item notify-item" onclick='$m0001.init();'>
                        <i class="fi-cog"></i> <span>修改密码</span>
                    </a>--%>

                    <!-- item-->
                    <a href='<c:url value="/logout" />' class="dropdown-item notify-item">
                        <i class="fi-power"></i> <span>退出</span>
                    </a>
                </div>

            </li>
        </ul>
        <!-- ==============================================右上角导航栏  结束 =========================================================-->
        <!--  左侧菜单栏收缩  开始 -->
        <ul class="list-inline menu-left mb-0">
            <li class="float-left">
                <button class="button-menu-mobile open-left waves-light waves-effect">
                    <i class="dripicons-menu"></i>
                </button>
            </li>
            <c:if test="${tag!='1'}" >
                <div class="nav-link dropdown-toggle waves-effect waves-light" data-toggle=""
                     role="button" aria-haspopup="false" aria-expanded="false">
                    <span style="font-weight: bolder; font-size: 18px; color: white;" id="company-name" data-name="${merchantName}">公司：${merchantName}</span>
                    <a href="javascript:void(0);" id="changeMer" style="color: #eee;text-decoration:underline !important; font-size: 14px;" >切换</a></div>
            </c:if>
        </ul>
        <!--  左侧菜单栏收缩  结束  -->
        <%--<div class="top_menu">
            <a href="javascript:;" class="leftIcon"></a>
            <div class="top_menu_con">
                <ul>
                    <li>用户管理<i class="closeItem"></i></li>
                    <li>系统管理<i class="closeItem"></i></li>
                    <li>供应商商品价目表<i class="closeItem"></i></li>
                    <li>系统管理<i class="closeItem"></i></li>
                    <li>系统管理<i class="closeItem"></i></li>
                    <li>系统管理<i class="closeItem"></i></li>
                    <li>系统管理<i class="closeItem"></i></li>
                    <li>系统管理<i class="closeItem"></i></li>
                    <li>系统管理<i class="closeItem"></i></li>
                    <li>系统管理<i class="closeItem"></i></li>
                    <li>系统管理<i class="closeItem"></i></li>
                    <li>系统管理<i class="closeItem"></i></li>
                    <li>系统管理<i class="closeItem"></i></li>

                </ul>
            </div>
            <a href="javascript:;" class="rightIcon"></a>
        </div>--%>
    </nav>
</div>

<!--======================Modal框===========================  -->
<jsp:include page="/WEB-INF/views/modals/0001.jsp" />

<jsp:include page="/WEB-INF/views/modals/3001.jsp" />