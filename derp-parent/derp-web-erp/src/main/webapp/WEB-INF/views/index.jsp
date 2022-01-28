<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
    //jsp禁用缓存
    response.addHeader("Cache-Control", "no-store, must-revalidate");
    response.addHeader("Expires", "Thu, 01 Jan 1970 00:00:01 GMT");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>经分销</title>
    <link rel="shortcut icon" type="image/x-icon" href="/resources/images/favicon.ico">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <meta content="A fully featured admin theme which can be used to build CRM, CMS, etc." name="description"/>
    <meta content="Coderthemes" name="author"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <style>
        body {
            overflow-y: auto !important;
            padding-right: 0 !important;
        }

        body::-webkit-scrollbar {
            /*高宽分别对应横竖滚动条的尺寸*/
            width: 10px;
        }

        body::-webkit-scrollbar-thumb {
            border-radius: 10px;
            -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
            background: #D6D6D6;
                 
        }

             body::-webkit-scrollbar-track {
            -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
            border-radius: 10px;
            background: #F1F1F1;

                 
        }

        .content-page-new {
            margin-top: 70px;
        }

        iframe {
            width: 100%;
            height: calc(100vh - 100px);
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/views/common.jsp"/>
<!-- 下载文件隐藏域 -->
<div id="downloadDiv" style="display: none;"></div>
<!-- 加载中图片 -->
<img id="loadingDiv" src="/resources/assets/images/loading.gif" width="180" height="180"
     style="display: none;position:absolute; z-index:2; filter:alpha(opacity=60);opacity:0.3;top: 25%;left: 50%"
     alt="加载中...">

<!-- Begin page -->
<div id="wrapper">
    <!-- 头部信息  start-->
    <jsp:include page="/WEB-INF/views/common/head.jsp"/>
    <!-- 头部信息  end  -->

    <!-- 左侧菜单   start-->
    <jsp:include page="/WEB-INF/views/common/menu.jsp"/>
    <!-- 左侧菜单   end  -->
    <div class="content-page" id="rightContent">
    </div>
    <!-- 新增 兼容新系统 -->
    <div id="myrame-bx" class="content-page content-page-new">
        <iframe name="myiframe" id="myrame" src="" frameborder="0" scrolling="no">
            <p>你的浏览器不支持iframe标签</p>
        </iframe>
    </div>
    <!-- 新增 兼容新系统  end-->
</div>
<!-- 页脚  start-->
<jsp:include page="/WEB-INF/views/common/foot.jsp"/>
<!-- 页脚  end  -->

<!-- 定义一些变量 -->
<!-- 设置首次进入加载的页面   -->
<input type="hidden" value="${param.firstPage}" id="firstPage"/>
<!-- 应用名 -->
<input type="hidden" value="${pageContext.request.contextPath}" id="baseURL"/>
</body>
</html>
<!-- index js工具类 -->
<script src='<c:url value="/resources/js/derp/index.js" />' type="text/javascript"></script>
<jsp:include page="/WEB-INF/views/modals/0003.jsp"/>
<script>
    var noticePageNo = 1;
    $(function () {
        //获取系统名称
        $custom.request.ajax("/commonConfig/getSystemName.asyn", {}, function (result) {
        if (result.state == 200) {
            $('#systemName').text(result.data);
        }
    });


    // changeby dongpeng 20210721
    // 监听 message事件
    // 解决 iframe跨域通信
    // 功能 嵌入的前后端分离项目，iframe内登录失效之后，弹起外层登录失效
    // tips 前后端完全分离之后，可删除该段代码
    /*window.addEventListener('message',function(e){
        var value = e.data;
        if (value != null && value == 'login_fail'){
            $custom.alert.prompt("登陆失效,请重新登陆",function(){
                window.location.href='/logout';
            });
        }
    }, false);*/

    window.addEventListener('message', function (e) {
        const value = e.data;
        if(value.source === 'vue-devtools-proxy') return ;
        // 登录过期
        if (value != null && value == 'login_fail') {
            $custom.alert.prompt("登陆失效,请重新登陆", function () {
                window.location.href = '/logout';
            });
            return
        }
        const messageData = value && JSON.parse(value)
        // 新系统控制旧系统跳转页面
        if (messageData && messageData.path) {
            if ($('a[path="' + messageData.path + '"]').parents('li').find('a').eq(0).attr('aria-expanded') === 'false') {
                $('a[path="' + messageData.path + '"]').parents('li').find('a').eq(0).click()
            }
            $('a[path="' + messageData.path + '"]').click()
        }
    }, false);

    // $("body").addClass('enlarged');

    var num = $(".top_menu_con ul li").length;
    var width = 116;
    var k = num - 8;
    var page = 1;

    $(".top_menu_con ul").width(num * 116);
    $(".rightIcon").click(function () {
        if (k == page) {
            $(".top_menu_con ul").animate({"right": '0px'}, 116);
        } else {
            $(".top_menu_con ul").animate({"left": '-=' + width}, 116);
            page++;
        }
    });
    $(".leftIcon").click(function () {
        if (page == 1) {
            $(".top_menu_con ul").animate({"left": '0px'}, 116);
        } else {
            $(".top_menu_con ul").animate({"left": '+=' + width}, 116);
            page--;
        }
    });

    $(".enlarged #wrapper .left.side-menu #sidebar-menu ul > li a").hover(function () {
        $(this).parent().parent().prev().css("background", "#2f3643");
    });


    $('.message').click(function () { // 系统公告
        $('.message-detail').toggle()
    })
    $('.user').click(function () { // 用户信息
        $('.user-detail').toggle()
    })

    $(".slimscroll-message").slimScroll({
        /* width: '100%', //可滚动区域宽度 */
        height: '250px', //可滚动区域高度
        size: '5px', //滚动条宽度，即组件宽度
        color: '#333', //滚动条颜色
        position: 'right', //组件位置：left/right
        distance: '0px', //组件与侧边之间的距离
        start: 'top', //默认滚动位置：top/bottom
        opacity: .2, //滚动条透明度
        alwaysVisible: false, //是否 始终显示组件
        disableFadeOut: true, //是否 鼠标经过可滚动区域时显示组件，离开时隐藏组件
        railVisible: true, //是否 显示轨道
        railColor: '#333', //轨道颜色
        railOpacity: .2, //轨道透明度
        railDraggable: true, //是否 滚动条可拖动
        railClass: 'slimScrollRail', //轨道div类名
        barClass: 'slimScrollBar', //滚动条div类名
        wrapperClass: 'slimScrollDiv', //外包div类名
        allowPageScroll: true, //是否 使用滚轮到达顶端/底端时，滚动窗口
        wheelStep: 10, //滚轮滚动量
        touchScrollStep: 200, //滚动量当用户使用手势
        borderRadius: '7px', //滚动条圆角
        railBorderRadius: '7px' //轨道圆角
    })

    /**
     * 获取公告
     */
    getNotice(noticePageNo);

    $(".loadmore").click(function () {
        noticePageNo += 1;
        getNotice(noticePageNo);
    });

    $('#changeMer').click(function () {
        $m0003.init();
    })
    })

    $(document).on("click", function (e) {
        var e = e || window.event; //事件对象，兼容IE
        var target = e.target || e.srcElement; //源对象，兼容火狐和IE
        while (target) { //循环判断至根节点
            if ($(target).hasClass('message') || target.className == 'message-detail') {
                $(".user-detail").hide(); // 点击message时,隐藏user-detail下拉菜单
                return;
            } else if ($(target).hasClass('user')) {
                $(".message-detail").hide();
                return;
            }
            target = target.parentNode;
        }
        $(".message-detail").hide(); //点击的不是message和它的子元素，隐藏下拉菜单
        $(".user-detail").hide();
    });

    $('.nav-second-level').delegate('li', 'click', function () {
        $(".nav-second-level li a").removeClass('im_color');
        $(this).find('a').addClass('im_color');
        /*
        *	2019-7-23 清空详细页公共参数
        */
        $module.revertList.params = [];
        $module.revertList.isMainList = true;
    })

    function getNotice(pageNo) {
        var url = "/notice/getNoticeByUser.aysn";

        $custom.request.ajax(url, {"pageNo": pageNo}, function (data) {
            if (data.state == 200) {
                var list = data.data.noticeList;
                var unReadAmount = data.data.unReadAmount;
                var account = data.data.account;

                if (pageNo == 1) {
                    $(".message-list").empty();
                }

                if (account > 0) {
                    $(".noticeAccount").html('<font style="color:#FF816A;"> (' + account + ')</font>');
                }

                if (unReadAmount > 0) {
                    $(".message-badge").html(unReadAmount);
                    $(".message-badge").show();
                }

                if (list.length > 0) {

                    var html = "";

                    $(list).each(function (i, m) {

                        html += '<a href="javascript:void(0);" onclick="showNotice(' + m.id + ',this)"><li>';

                        html += '<span class="message-tip ';
                        if (m.readStatus == '0') {
                            if (i == 0) {
                                showNotice(m.id);
                            } else {
                                html += ' tip ';
                            }
                        }
                        html += '"></span>';

                        html += '<span class="message-title" title="' + m.title + '">' + m.title + '</span>';

                        var publishDate = "";

                        if (m.publishDate) {
                            publishDate = m.publishDate;
                            publishDate = publishDate.split(" ")[0];
                        }

                        html += '<span class="message-time">' + publishDate + '</span>';
                        html += '</a>';

                    });

                    $(".message-list").append(html);
                } else {
                    if (noticePageNo > 1) {
                        noticePageNo -= 1;
                    }
                }
            }
        });
    }

    function showNotice(id, org) {
        var url = "/notice/changeReadStatus.aysn";

        $custom.request.ajax(url, {"noticeId": id}, function (data) {
            if (data.state == 200) {
                var unReadAmount = data.data;

                if (unReadAmount > 0) {
                    $(".message-badge").html(unReadAmount);
                    $(".message-badge").show();
                } else {
                    $(".message-badge").hide()
                }

                $(org).find(".tip").removeClass("tip");
                $m3001.init(id, "homePage");
            }
        });
    }
</script>

