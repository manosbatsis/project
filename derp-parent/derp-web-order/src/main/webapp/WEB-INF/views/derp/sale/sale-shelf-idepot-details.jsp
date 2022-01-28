<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common.jsp"/>
<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
        <div class="row">
            <div class="col-12 ml10">
                <div class="col-12 row">
                    <ol class="breadcrumb">
                        <li><i class="block"></i> 当前位置：</li>
                        <li class="breadcrumb-item"><a href="#">上架入库单详情</a></li>
                    </ol>
                </div>
                <div class="card-box table-responsive">
                    <div class="title_text">上架入库详情</div>
                    <div class="info_box">
                        <div class="info_item">
                            <div class="info_text">
                                <span>上架入库单：</span>
                                <span>
                                    ${detail.code}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>入仓仓库：</span>
                                <span>
                                    ${detail.inDepotName}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>单据状态：</span>
                                <span>
                                	${detail.stateLabel}
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>销售订单号：</span>
                                <span>
                                    ${detail.saleOrderCode}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>出仓仓库：</span>
                                <span>
                                    ${detail.outDepotName}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>入库时间：</span>
                                <span>
                                    <fmt:formatDate value="${detail.shelfDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>销售出库单：</span>
                                <span>
                                   ${detail.saleOutCode}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>PO单号：</span>
                                <span>
                                	${detail.poNo}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>操作人：</span>
                                <span>
                                    ${detail.operator}
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
	                        <div class="info_text">
	                            <span>操作时间：</span>
	                            <span>
	                                <fmt:formatDate value="${detail.operatorDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
	                            </span>
	                        </div>
	                        <div class="info_text">
	                           <span>事业部：</span>
                                <span>
                                    ${detail.buName}
                                </span>
	                        </div>
	                        <div class="info_text">
	                        </div>
	                    </div>
                    </div>
                    <div class="title_text">商品信息</div>
                    <!--   商品信息  start -->
                    <div class="form-row mt20" style="padding-left: 15px;padding-right: 15px;">
                        <table id="table-list" class="table table-striped">
                            <thead>
	                            <tr>
	                                <th>序号</th>
	                                <th>商品货号</th>
	                                <th>商品条码</th>
	                                <th>标准条码</th>
	                                <th>商品名称</th>
	                                <th>好品数量</th>
	                                <th>坏品数量</th>
	                                <th>入库批次号</th>
	                                <th>生产日期</th>
	                                <th>失效日期</th>
	                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${itemList}" var="item" varStatus="status">
                                <tr>
                                    <td>${status.index+1}</td>
                                    <td>${item.inGoodsNo}</td>
                                    <td>${item.barcode}</td>
                                    <td>${item.commbarcode}</td>
                                    <td>${item.inGoodsName}</td>
                                    <td>${item.normalNum}</td>
                                    <td>${item.damagedNum}</td>
                                    <td>${item.batchNo }</td>
                                    <td>
                                    	<fmt:formatDate value="${item.productionDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </td>
                                    <td>
                                    	<fmt:formatDate value="${item.overdueDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                 <!--   商品上架明细  end -->
                    <div class="form-row m-t-50">
                        <div class="col-12">
                            <div class="row">
                                <div class="col-5">
                                    <button type="button" class="btn btn-find waves-effect waves-light"
                                            id="cancel-buttons">返回</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- end row -->
        </div>
        <!-- container -->
    </div>

</div>
<!-- content -->
<script type="text/javascript">
    $(document).ready(function () {
        //取消按钮
        $("#cancel-buttons").click(function(){
           	$module.load.pageOrder('act=40042');
           	// $module.load.pageOrder('act=4010');
        });

    });
</script>
