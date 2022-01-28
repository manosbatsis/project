<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common.jsp"/>
<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
        <div class="row">
            <div class="col-12">
                <div class="card-box">
                    <!--  title   start  -->
                    <div class="col-12 row">
                        <div>
                            <ol class="breadcrumb">
                                <li><i class="block"></i> 当前位置：</li>
                                <li class="breadcrumb-item"><a href="#">模版管理</a></li>
                                <li class="breadcrumb-item"><a
                                        href="#">导出模版管理</a></li>
                                <li class="breadcrumb-item"><a
                                        href="#">预览</a></li>
                            </ol>
                        </div>
                    </div>
                    <div id="table-div">
                        ${detail.contentBody}

                    </div>

                    <div class="form-row m-t-50">
                        <div class="col-12">
                            <div class="row">
                                <div class="col-6">
                                    <input type="button"
                                           class="btn btn-light waves-effect waves-light btn_default"
                                           id="cancel-buttons" value="返回">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- end row -->
            <!--======================Modal框===========================  -->
        </div>
        <!-- container -->
    </div>

</div>

<script type="text/javascript">
    //取消按钮
    $("#cancel-buttons").click(function(){
        $module.load.pageOrder("act=16001");
    });
</script>