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
                        <li class="breadcrumb-item"><a href="#">上架单详情</a></li>
                    </ol>
                </div>
                <div class="card-box table-responsive">
                    <div class="title_text">基本信息</div>
                    <div class="info_box">
                        <div class="info_item">
                            <div class="info_text">
                                <span>上架单号：</span>
                                <span>
                                    ${detail.code}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>销售订单号：</span>
                                <span>
                                    ${detail.saleOrderCode}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>PO号：</span>
                                <span>
                                	${detail.poNo}
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>客户：</span>
                                <span>
                                    ${detail.customerName}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>事业部：</span>
                                <span>
                                    ${detail.buName}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>上架日期：</span>
                                <span>
                                    <fmt:formatDate value="${detail.shelfDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>单据状态：</span>
                                <span>
                                   ${detail.stateLabel}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>上架人：</span>
                                <span>
                                	${detail.operator}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>创建时间：</span>
                                <span>
                                    <fmt:formatDate value="${detail.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="title_text">上架明细</div>
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
	                                <th>上架量</th>
	                                <th>残损量</th>
	                                <th>少货量</th>
	                                <th>备注</th>
	                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${itemList}" var="item" varStatus="status">
                                <tr>
                                    <td>${status.index+1}</td>
                                    <td>${item.goodsNo}</td>
                                    <td>${item.barcode}</td>
                                    <td>${item.commbarcode}</td>
                                    <td>${item.goodsName}</td>
                                    <td class="shelfNum">${item.shelfNum}</td>
                                    <td class="damagedNum">${item.damagedNum}</td>
                                    <td class="lackNum">${item.lackNum}</td>
                                    <td>${item.remark}</td>

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
                                <div class="col-4">
                                </div>
                                <div class="col-3">
                                    <label>上架总数：</label>
                                    <span id="shelfTotal"></span>
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
           	$module.load.pageOrder('act=4010');
        });

    });

    $(function () {
        var total = 0;
        $(".shelfNum").each(function () {
            var val = $(this).text();
            total += parseFloat(val);
        })
        $(".damagedNum").each(function () {
            var val = $(this).text();
            total += parseFloat(val);
        })
        $(".lackNum").each(function () {
            var val = $(this).text();
            total += parseFloat(val);
        })
        $("#shelfTotal").text(total);
    })
</script>
