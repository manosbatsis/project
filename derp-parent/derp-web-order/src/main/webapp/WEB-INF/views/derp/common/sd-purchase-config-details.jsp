<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common.jsp"/>
<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
        <div class="row">
            <div class="col-12">
                <div class="card-box table-responsive">
                    <!--  title   start  -->
                    <div class="col-12">
                        <div>
                            <div class="col-12 row">
                                <div>
                                    <ol class="breadcrumb">
                                        <li><i class="block"></i> 当前位置：</li>
                                        <li class="breadcrumb-item"><a href="#">采购档案</a></li>
                                        <li class="breadcrumb-item"><a href="javascript:dh_list();">采购SD配置</a></li>
                                        <li class="breadcrumb-item"><a href="#">采购SD配置详情</a>
                                        </li>
                                    </ol>
                                </div>
                            </div>
                            <!--  title   end  -->
                            <!--  title   基本信息   start -->

                            <div class="title_text">基本信息</div>
                            <form id="add-form">
                                <div class="info_box">
                                    <div class="info_item">
                                        <div class="info_text">
                                            <span>公司：</span>
                                            <span>${details.merchantName }</span>
                                        </div>
                                        <div class="info_text">
                                            <span>事业部：</span>
                                            <span>
                                                ${details.buName }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>供应商：</span>
                                            <span>${details.supplierName }</span>
                                        </div>
                                    </div>
                                    <div class="info_item">
                                        <div class="info_text">
                                            <span>生效时间：</span>
                                            <span>
                                                <fmt:formatDate value="${details.effectiveTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>失效时间：</span>
                                            <span>
                                                <fmt:formatDate value="${details.invalidTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>状态：</span>
                                            <span>
                                                ${details.statusLabel }
                                            </span>
                                        </div>
                                    </div>
                                </div>
                                <!--  title   基本信息  start -->
                            </form>
                            <!--   商品信息  start -->
                            <div class="title_text">单比例配置</div>
                            <div class="form-row mt20">
                                <table class="table table-striped " cellspacing="0"
                                       width="100%">
                                    <thead>
                                        <tr>
                                            <th>SD类型</th>
                                            <th>简称</th>
                                            <th>比例</th>
                                        </tr>
                                    </thead>
                                    <tbody id="single-tbody">
                                       <c:forEach items="${itemList }" var="item">
                                           <c:if test="${ item.sdConfigSimpleType == '1'}">
                                               <tr>
                                                   <td>${item.sdConfigName }</td>
                                                   <td>${item.sdConfigSimpleName }</td>
                                                   <td>${item.proportion }</td>
                                               </tr>
                                           </c:if>
                                       </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <div class="title_text">多比例配置</div>
                            <div class="form-row mt20">
                                <table  class="table table-striped " cellspacing="0"
                                       width="100%">
                                    <thead>
                                        <tr>
                                            <th>SD类型</th>
											<th>简称</th>
											<th>标准品牌</th>
											<th>标准条码</th>
											<th>商品名称</th>
											<th>比例</th>
                                        </tr>
                                    </thead>
                                    <tbody id="multi-tbody">
                                       <c:forEach items="${itemList }" var="item">
                                           <c:if test="${ item.sdConfigSimpleType == '2'}">
                                               <tr>
                                                   <td>${item.sdConfigName }</td>
                                                   <td>${item.sdConfigSimpleName }</td>
                                                   <td>${item.brandParent }</td>
                                                   <td>${item.commbarcode }</td>
                                                   <td>${item.goodsName }</td>
                                                   <td>${item.proportion }</td>
                                               </tr>
                                           </c:if>
                                       </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <!--   商品信息  end -->
                            <div class="form-row">
                                <div class="col-3">
                                    <div class="form-row m-t-50">
                                        <div class="row">
                                            <div class="col-12">
                                                <button type="button" class="btn btn-find waves-effect waves-light btn_default"
                                                        id="cancel-buttons">返 回
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- end row -->
                <!-- 弹窗开始 -->
                <!-- 弹窗结束 -->
            </div>
        </div>
        <!-- container -->
    </div>

</div>
<!-- content -->
<script type="text/javascript">
    $(function () {

        function dh_list() {
            $module.load.pageOrder("act=20002");
        }

        //返回
        $("#cancel-buttons").click(function () {
        	$module.load.pageOrder("act=20002");
        });
        
    });
    
</script>
