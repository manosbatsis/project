<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- App favicon -->
<link rel="shortcut icon" href='<c:url value="/resources/assets/images/favicon.ico"/>'>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt60">
        <!-- Start row -->
          <div class="row">
          <div class="card-box">
              <div onclick='$m0001.init();'>居中模态框演示</div>
          </div>
          <div class="card-box">
          	<div onclick='$m6011.init();'>选择商品模态框演示</div>
          </div>
                  <!--======================Modal框===========================  -->
        <jsp:include page="/WEB-INF/views/modals/0001.jsp" />
        <jsp:include page="/WEB-INF/views/modals/6011.jsp" />
                  <!-- end row -->
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->
<script type="text/javascript">

</script>
