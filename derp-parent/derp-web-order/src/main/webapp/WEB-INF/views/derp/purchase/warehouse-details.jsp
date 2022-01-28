<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
								<li class="breadcrumb-item"><a href="#">采购档案</a></li>
								<li class="breadcrumb-item"><a href="javascript:dh_list();">采购入库单列表</a></li>
								<li class="breadcrumb-item"><a
									href="javascript:dh_details(${detail.id });">采购入库单详情</a></li>
							</ol>
						</div>
					</div>
					<div class="title_text">采购入库详情</div>
					<div class="info_box">
						<div class="info_item">
							<div class="info_text">
								<span>采购入库状态：</span> <span>
								    ${detail.stateLabel}
								</span>
							</div>
							<div class="info_text">
								<span>入库时间：</span> <span> <fmt:formatDate
										value="${detail.inboundDate}" pattern="yyyy-MM-dd HH:mm:ss" />
								</span>
							</div>
							<div class="info_text">
								<span>入库单号：</span> <span> ${detail.code} </span>
							</div>
						</div>
						<div class="info_item">
							<div class="info_text">
								<span>理货单号：</span> <span> ${detail.tallyingOrderCode} </span>
							</div>
							<div class="info_text">
								<span>仓库名称：</span> <span> ${detail.depotName} </span>
							</div>
							<div class="info_text">
								<span>事业部：</span> <span> ${detail.buName} </span>
							</div>
						</div>
						<div class="info_item">
							<div class="info_text">
								<span>企业订单号：</span> <span> ${detail.declareOrderCode} </span>
							</div>
							<div class="info_text">
								<span>确认时间：</span> <span> <fmt:formatDate
										value="${detail.confirmDate}" pattern="yyyy-MM-dd HH:mm:ss" />
								</span>
							</div>
							<div class="info_text">
								<span>理货时间：</span> <span> <fmt:formatDate
										value="${detail.tallyingDate}" pattern="yyyy-MM-dd HH:mm:ss" />
								</span>
							</div>
						</div>
						<div class="info_item">
							<div class="info_text">
								<span>勾稽状态：</span> <span> ${detail.correlationStatusLabel} </span>
							</div>
							<div class="info_text">
								<span>外部单号：</span> <span> ${detail.extraCode} </span>
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
					<ul class="nav nav-tabs">
						<li class="nav-item"><a href="#itemList" data-toggle="tab"
							class="nav-link active">商品列表</a></li>
						<li class="nav-item"><a href="#batchDefault"
							data-toggle="tab" class="nav-link">批次明细</a></li>
					</ul>
					<div class="tab-content">
						<div class="tab-pane fade show active table-responsive"
							id="itemList">
							<table id="datatable-buttons1" class="table table-striped"
								cellspacing="0" width="100%">
								<thead>
									<tr>
										<th style="white-space: nowrap;">序号</th>
										<th style="white-space: nowrap;">商品货号</th>
										<th style="max-width: 200px">商品名称</th>
										<th style="white-space: nowrap;">商品条形码</th>
										<th style="white-space: nowrap;">海外仓理货单位</th>
										<th style="white-space: nowrap;">应收数量</th>
										<th style="white-space: nowrap;">实收数量</th>
									</tr>
								</thead>
								<tbody>
									<c:set var="purchaseNum" value="0"></c:set>
									<c:set var="tallyingNum" value="0"></c:set>
									<c:forEach items="${detail.itemList }" var="item"
										varStatus="itemStatus">
										<tr>
											<td>${itemStatus.index+1 }</td>
											<td>${item.goodsNo }</td>
											<td>${item.goodsName }</td>
											<td>${item.barcode }</td>
											<td><c:choose>
													<c:when test="${item.tallyingUnit eq '00' }">托盘</c:when>
													<c:when test="${item.tallyingUnit eq '01' }">箱</c:when>
													<c:when test="${item.tallyingUnit eq '02' }">件</c:when>
													<c:otherwise></c:otherwise>
												</c:choose></td>
											<td>${item.purchaseNum }<c:set var="purchaseNum" value="${purchaseNum +  item.purchaseNum}"></c:set></td>
											<td>${item.tallyingNum }<c:set var="tallyingNum" value="${tallyingNum +  item.tallyingNum}"></c:set></td>
										</tr>
									</c:forEach>
									<tr>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td>合计：</td>
										<td>${purchaseNum }</td>
										<td>${tallyingNum }</td>
									</tr>
								</tbody>
							</table>
						</div>
						<div class="tab-pane fade table-responsive" id="batchDefault">
							<table id="datatable-buttons2" class="table table-striped"
								cellspacing="0" width="100%">
								<thead>
									<tr>
										<th style="white-space: nowrap;">序号</th>
										<th style="white-space: nowrap;">商品货号</th>
										<th style="white-space: nowrap;">商品名称</th>
										<th style="white-space: nowrap;">批次号</th>
										<th style="white-space: nowrap;">生产日期</th>
										<th style="white-space: nowrap;">效期至</th>
										<th style="white-space: nowrap;">海外仓理货单位</th>
										<th style="white-space: nowrap;">损货数量</th>
										<th style="white-space: nowrap;">过期数量</th>
										<th style="white-space: nowrap;">正常数量</th>
										<th style="white-space: nowrap;">毛重</th>
										<th style="white-space: nowrap;">净重</th>
										<th style="white-space: nowrap;">体积</th>
										<th style="white-space: nowrap;">长</th>
										<th style="white-space: nowrap;">宽</th>
										<th style="white-space: nowrap;">高</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${batchBean }" var="batch"
										varStatus="batchStatus">
										<tr>
											<td style="white-space: nowrap;">${batchStatus.index+1 }</td>
											<td style="white-space: nowrap;">${batch.goodsNo }</td>
											<td style="white-space: nowrap;">${batch.goodsName }</td>
											<td style="white-space: nowrap;">${batch.batchNo }</td>
											<td style="white-space: nowrap;">${batch.productionDateStr }</td>
											<td style="white-space: nowrap;">${batch.overdueDateStr }</td>
											<td style="white-space: nowrap;"><c:choose>
													<c:when test="${batch.tallyingUnit eq '00' }">托盘</c:when>
													<c:when test="${batch.tallyingUnit eq '01' }">箱</c:when>
													<c:when test="${batch.tallyingUnit eq '02' }">件</c:when>
													<c:otherwise></c:otherwise>
												</c:choose></td>
											<td style="white-space: nowrap;">${batch.wornNum }</td>
											<td style="white-space: nowrap;">${batch.expireNum }</td>
											<td style="white-space: nowrap;">${batch.normalNum }</td>
											<td style="white-space: nowrap;">${batch.grossWeight }</td>
											<td style="white-space: nowrap;">${batch.netWeight }</td>
											<td style="white-space: nowrap;">${batch.volume }</td>
											<td style="white-space: nowrap;">${batch.length }</td>
											<td style="white-space: nowrap;">${batch.width }</td>
											<td style="white-space: nowrap;">${batch.height }</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row mt20 order">
			<div class='col-12'>
				<div class="card-box">
					<div class="title_text">附件列表</div>
					<div id="attachmentTable" multiple ></div>
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
<input id="code" type="hidden" value="${detail.code}">
<!-- content -->
<script type="text/javascript">
	var code =  $("#code").val()   ;
	$("#attachmentTable").createAttachTables(code);

 function dh_list(){
	 $module.load.pageOrder("act=3004");
 }
 
 function dh_details(id){
	 $module.load.pageOrder("act=30041&id="+id);
 }
 
 //返回
 $("#cancel-buttons").click(function () {
     $module.load.pageOrder("act=3004");
 });
</script>
