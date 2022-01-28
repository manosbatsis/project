<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<jsp:include page="/WEB-INF/views/common.jsp" />

<!-- Start content -->
<div class="content">
	<div class="container-fluid mt80">
		<!-- Start row -->
		<div class="row">
			<div class="col-12">
				<div class="card-box table-responsive">
					<div class="col-12 row">
						<div>
							<ol class="breadcrumb">
								<li><i class="block"></i> 当前位置：</li>
								<li class="breadcrumb-item"><a href="javascript:dh_list();">采购SD单列表</a></li>
								<li class="breadcrumb-item"><a href="#">采购SD单详情</a></li>
							</ol>
						</div>
					</div>
					<div class="title_text">采购SD单详情</div>
					<div class="info_box">
						<div class="info_item">
							<div class="info_text">
								<span>SD单号：</span> <span>
								    ${detail.code}
								</span>
							</div>
							<div class="info_text">
								<span>事业部：</span> <span> ${detail.buName} </span>
							</div>
							
							<div class="info_text">
								<span>供应商：</span> <span> ${detail.supplierName} </span>
							</div>
						</div>
						<div class="info_item">
							<div class="info_text">
								<span>采购订单号：</span> 
								<span>
								    ${detail.purchaseCode}
								</span>
							</div>
							<div class="info_text">
								<span>采购币种：</span> <span> ${detail.currencyLabel} </span>
							</div>
							<div class="info_text">
								<span>入库时间：</span> <span> <fmt:formatDate
										value="${detail.inboundDate}" pattern="yyyy-MM-dd HH:mm:ss" />
								</span>
							</div>
						</div>
						<div class="info_item">
							<div class="info_text">
								<span>PO 号：</span> <span> ${detail.poNo} </span>
							</div>
							<div class="info_text">
								<span>本币币种：</span> <span> ${detail.tgtCurrencyLabel} </span>
							</div>
							<div class="info_text">
								<span>单据类型：</span> <span> ${detail.typeLabel} </span>
							</div>
						</div>
						<div class="info_item">
							<div class="info_text">
								<span>汇率：</span> <span> ${detail.rate} </span>
							</div>
							<div class="info_text">
								<span>创建人：</span> <span> ${detail.createName} </span>
							</div>
							<div class="info_text">
								<span>创建时间：</span> <span> <fmt:formatDate
										value="${detail.createDate}" pattern="yyyy-MM-dd HH:mm:ss" />
								</span>
							</div>
						</div>
						<div class="info_item">
							<div class="info_text">
								<span>仓库：</span> <span> ${detail.depotName} </span>
							</div>
							<div class="info_text">
							</div>
							<div class="info_text">
							</div>
						</div>
					</div>
				</div>
				<!-- end row -->
			</div>
			<!-- container -->
		</div>
		<!--  ================================表格 ===============================================   -->
		<div class="row mt20">
			<div class='col-12'>
				<div class="card-box">
					<div class="title_text">商品SD明细</div>
					<table id="datatable-buttons2" class="table table-striped"
						cellspacing="0" width="100%">
						<thead>
							<tr>
								<th style="white-space: nowrap;">商品货号</th>
								<th style="white-space: nowrap;">商品名称</th>
								<th style="white-space: nowrap;">采购数量</th>
								<th style="white-space: nowrap;">采购单价</th>
								<th style="white-space: nowrap;">采购金额</th>
								<th style="white-space: nowrap;">SD类型</th>
								<th style="white-space: nowrap;">SD单价</th>
								<th style="white-space: nowrap;">SD金额</th>
							</tr>
						</thead>
						<tbody>
							<c:set var="sdAmount" value="0"></c:set>
							<c:set var="amount" value="0"></c:set>
							<c:set var="num" value="0"></c:set>
							<c:forEach items="${sdItemList }" var="sdItem" >
								<tr>
									<td style="white-space: nowrap;">${sdItem.goodsNo }</td>
									<td style="white-space: nowrap;">${sdItem.goodsName }</td>
									<td style="white-space: nowrap;">${sdItem.num }<c:set var="num" value="${num +  sdItem.num}"></c:set></td>
									<td style="white-space: nowrap;"><fmt:formatNumber value="${sdItem.price }" pattern="#.##" minFractionDigits="8" ></fmt:formatNumber></td>
									<td style="white-space: nowrap;"><c:if test="${ not empty sdItem.amount}">${detail.currency} <fmt:formatNumber value="${sdItem.amount }" pattern="#.##" minFractionDigits="2" ></fmt:formatNumber><c:set var="amount" value="${amount +  sdItem.amount}"></c:set></c:if></td>
									<td style="white-space: nowrap;">${sdItem.sdTypeName }</td>
									<td style="white-space: nowrap;"><fmt:formatNumber value="${sdItem.sdPrice }" pattern="#.##" minFractionDigits="8" ></fmt:formatNumber></td>
									<td style="white-space: nowrap;">${detail.currency} <fmt:formatNumber value="${sdItem.sdAmount }" pattern="#.##" minFractionDigits="2" ></fmt:formatNumber><c:set var="sdAmount" value="${sdAmount +  sdItem.sdAmount}"></c:set></td>
								</tr>
							</c:forEach>
							<tr>
								<td>合计：</td>
								<td></td>
								<td><c:if test="${num > 0 }">${num }</c:if></td>
								<td></td>
								<td><c:if test="${amount > 0 }">${detail.currency} <fmt:formatNumber value="${amount }" pattern="#.##" minFractionDigits="2" ></fmt:formatNumber></c:if></td>
								<td></td>
								<td></td>
								<td><c:if test="${sdAmount > 0 }">${detail.currency} <fmt:formatNumber value="${sdAmount }" pattern="#.##" minFractionDigits="2" ></fmt:formatNumber></c:if></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<div class="row mt20">
			<div class='col-12'>
				<div class="card-box">
					<div class="form-row">
						<div class="col-12">
							<div class="row">
								<div class="col-5">
									<button type="button"
										class="btn btn-find waves-effect waves-light btn_default"
										id="cancel-buttons">返回</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!--  ================================表格  end ===============================================   -->
</div>
<!-- content -->
<script type="text/javascript">

 function dh_list(){
	 $module.load.pageOrder("act=3010");
 }
 
 //返回
 $("#cancel-buttons").click(function () {
     $module.load.pageOrder("act=3010");
 });
</script>
