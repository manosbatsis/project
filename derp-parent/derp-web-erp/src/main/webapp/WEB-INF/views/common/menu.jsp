<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!-- ========== Left Sidebar Start ========== -->
<div class="left side-menu">
    <div class="slimscroll-menu" id="remove-scroll">

        <!--- Sidemenu -->
        <div id="sidebar-menu">
            <!-- Left Menu Start -->
            <ul class="metismenu" id="side-menu">
                <li class="menu-title">Navigation</li>
                <li >
                    <a href="index.html">
                        <i class="fi-air-play"></i><span> 首页 </span>
                    </a>
                </li>
               <%-- <shiro:hasPermission name="sale_retail_admin">
                    <li class="nav-second-level" aria-expanded="false">
                        <a href="javascript:void(0);" onclick="$module.load.pageReport('act=30001');">
                            <i class="dripicons-search"></i><span> 销售洞察 </span>
                        </a>
                    </li>
                </shiro:hasPermission>--%>
                <c:forEach items="${permissions}" var="permission" >
                    <c:if test="${permission.children != null and permission.children.size() == 0}">
                        <li class="nav-second-level" aria-expanded="false">
                            <a href="javascript:void(0);" onclick='$load.a("${permission.url})")'>
                                <i class="${permission.icon}"></i><span> ${permission.name} </span>
                            </a>
                        </li>
                    </c:if>
                    <c:if test="${permission.children != null and permission.children.size() > 0}">
                        <li>
                            <a href="javascript: void(0);"><i class="${permission.icon}"></i> <span> ${permission.name} </span> <span class="menu-arrow"></span></a>
                            <ul class="nav-second-level" aria-expanded="false">
                                <c:forEach items="${permission.children}" var="node">
                                    <c:choose>
                                        <c:when test="${empty node.serverAddr}">
                                            <li><a href="javascript:void(0);" path='<c:url value="${node.url}"/>'>${node.name}</a></li>
                                        </c:when>
                                        <c:otherwise>
                                            <li><a href="javascript:void(0);" path='${node.url}?url=${node.serverAddr}'>${node.name}</a></li>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </ul>
                        </li>
                    </c:if>
                </c:forEach>
                <!--<li>
                    <a href="javascript: void(0);"><i class="fi-mail"></i><span> 前端页面 </span> <span class="menu-arrow"></span></a>
                    <ul class="nav-second-level" aria-expanded="false">
                         <li><a href="javascript:void(0);" path="http://10.10.102.180:3101/page/001.html?id=1" > 批量导入111</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/001.html?id=1"/>'>批量导入</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/002.html?id=1"/>'>期初库存列表</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/003.html?id=1"/>'>期初库存新增</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/004.html?id=1"/>'>客户列表编辑</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/005.html?id=1"/>'>新增仓库</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/006.html?id=1"/>'>商品编辑</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/007.html?id=1"/>'>设置库存预警</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/008.html?id=1"/>'>设置库存预警</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/009.html?id=1"/>'>供应商价目详情</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/010.html?id=1"/>'>新增销售订单</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/011.html?id=1"/>'>模态框集合</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/012.html?id=1"/>'>新增采购订单1</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/013.html?id=1"/>'>新增采购订单2</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/014.html?id=1"/>'>采购入库详情</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/015.html?id=1"/>'>采购订单详情</a></li>
                         <li><a href="javascript:void(0);" path='<c:url value="/page/016.html?id=1"/>'>客户详情</a></li>
                         <li><a href="javascript:void(0);" path='<c:url value="/page/017.html?id=1"/>'>编辑供应商报价</a></li>
                         <li><a href="javascript:void(0);" path='<c:url value="/page/018.html?id=1"/>'>供应商询价池</a></li>
                         <li><a href="javascript:void(0);" path='<c:url value="/page/019.html?id=1"/>'>编辑询价池</a></li>
                         <li><a href="javascript:void(0);" path='<c:url value="/page/020.html?id=1"/>'>预申报单</a></li>
                         <li><a href="javascript:void(0);" path='<c:url value="/page/021.html?id=1"/>'>编辑入仓单</a></li>
                         <li><a href="javascript:void(0);" path='<c:url value="/page/022.html?id=1"/>'>预申报单详情</a></li>
                         <li><a href="javascript:void(0);" path='<c:url value="/page/023.html?id=1"/>'>编辑资质</a></li>
                         <li><a href="javascript:void(0);" path='<c:url value="/page/024.html?id=1"/>'>销售订单</a></li>
                         <li><a href="javascript:void(0);" path='<c:url value="/page/025.html?id=1"/>'>新增销售订单</a></li>
                         <li><a href="javascript:void(0);" path='<c:url value="/page/026.html?id=1"/>'>销售订单详情</a></li>
                         <li><a href="javascript:void(0);" path='<c:url value="/page/027.html?id=1"/>'>代销选单</a></li>
                         <li><a href="javascript:void(0);" path='<c:url value="/page/028.html?id=1"/>'>电商订单</a></li>
                         <li><a href="javascript:void(0);" path='<c:url value="/page/029.html?id=1"/>'>电商订单详情</a></li>
                         <li><a href="javascript:void(0);" path='<c:url value="/page/030.html?id=1"/>'>销售出库清单</a></li>
                         <li><a href="javascript:void(0);" path='<c:url value="/page/031.html?id=1"/>'>销售单详情</a></li>
                         <li><a href="javascript:void(0);" path='<c:url value="/page/032.html?id=1"/>'>调拨订单</a></li>
                         <li><a href="javascript:void(0);" path='<c:url value="/page/033.html?id=1"/>'>新增调拨订单</a></li>
                          <li><a href="javascript:void(0);" path='<c:url value="/page/034.html?id=1"/>'>调拨理货单</a></li>
                          <li><a href="javascript:void(0);" path='<c:url value="/page/035.html?id=1"/>'>调拨入库</a></li>
                          <li><a href="javascript:void(0);" path='<c:url value="/page/036.html?id=1"/>'>调拨出库</a></li>
                          <li><a href="javascript:void(0);" path='<c:url value="/page/037.html?id=1"/>'>新增/编辑配置信息</a></li>
                          <li><a href="javascript:void(0);" path='<c:url value="/page/038.html?id=1"/>'>配置信息详情</a></li>
                          <li><a href="javascript:void(0);" path='<c:url value="/page/039.html?id=1"/>'>新增/编辑接口</a></li>
                          <li><a href="javascript:void(0);" path='<c:url value="/page/040.html?id=1"/>'>接口详情</a></li>
                          <li><a href="javascript:void(0);" path='<c:url value="/page/041.html?id=1"/>'>调拨出库详情</a></li>
                          <li><a href="javascript:void(0);" path='<c:url value="/page/042.html?id=1"/>'>调拨理货单详情</a></li>
                          <li><a href="javascript:void(0);" path='<c:url value="/page/043.html?id=1"/>'>库存结算详情</a></li>
                          <li><a href="javascript:void(0);" path='<c:url value="/page/044.html?id=1"/>'>电商订单</a></li>
                          <li><a href="javascript:void(0);" path='<c:url value="/page/045.html?id=1"/>'>电商订单详情</a></li>
                          <li><a href="javascript:void(0);" path='<c:url value="/page/046.html?id=1"/>'>公司信息</a></li>
                          <li><a href="javascript:void(0);" path='<c:url value="/page/047.html?id=1"/>'>报文详情</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/048.html?id=1"/>'>供应商详情</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/049.html?id=1"/>'>编辑资质</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/050.html?id=1"/>'>新增库存期初</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/051.html?id=1"/>'>盘点指定列表</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/052.html?id=1"/>'>盘点单详情</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/053.html?id=1"/>'>新增/盘点指令</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/054.html?id=1"/>'>盘点结果</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/055.html?id=1"/>'>新增盘点结果单</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/056.html?id=1"/>'>盘点结果详情</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/057.html?id=1"/>'>类型调整列表</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/058.html?id=1"/>'>类型调整详情</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/059.html?id=1"/>'>库存调整列表</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/060.html?id=1"/>'>库存调整详情</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/061.html?id=1"/>'>爬虫数据列表</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/062.html?id=1"/>'>新增爬虫配置</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/063.html?id=1"/>'>批量导入</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/064.html?id=1"/>'>编辑清关资料</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/065.html?id=1"/>'>搜索下拉提示</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/066.html?id=1"/>'>品牌匹配</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/067.html?id=1"/>'>解除品牌匹配</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/068.html?id=1"/>'>进销存汇总表</a></li>
                        <li><a href="javascript:void(0);" path='<c:url value="/page/069.html?id=1"/>'>入库详情</a></li>
                    </ul>
                </li> -->
            </ul>

        </div>
        <!-- Sidebar -->
        <div class="clearfix"></div>
    </div>
    <!-- Sidebar -left -->
</div>
