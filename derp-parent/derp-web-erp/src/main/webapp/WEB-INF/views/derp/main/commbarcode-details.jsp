<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<style type="text/css">
.not-arrow {
	padding: 5px 10px;
	border: 1px solid #dcd8d8;
	-webkit-appearance: none;
	-moz-appearance: none;
	appearance: none; /*去掉下拉箭头*/
}

.not-arrow::-ms-expand {
	display: none;
}
</style>
<!-- Start content -->
<div class="content">
	<div class="container-fluid mt80">
		<!-- Start row -->
		<div class="row">
			<div class="col-12">
				<div class="card-box">
					<!--  title   start  -->
					<div class="col-12">
						<div>
							<div class="col-12 row">
								<div>
									<ol class="breadcrumb">
										<li><i class="block"></i> 当前位置：</li>
										<li class="breadcrumb-item"><a href="#">品牌管理</a></li>
										<li class="breadcrumb-item"><a href="$load.a('/list/menu.asyn?act=2001');">标准条码管理</a></li>
										<li class="breadcrumb-item"><a href="#">标准条码管理详情</a></li>
									</ol>
								</div>
							</div>
							<div class="title_text">基本信息</div>
							<div class="info_box">
								<div class="info_item">
									<div class="info_text">
										<span>标准条码：</span> <span>${detail.commbarcode }</span>
									</div>
									<div class="info_text">
										<span>标准品牌：</span> <span>${detail.commBrandParentName }</span>
									</div>
									<div class="info_text">
										<span>标准品类：</span> <span>${detail.commTypeName }</span>
									</div>
								</div>
								<div class="info_item">
									<div class="info_text">
										<span>标准品名：</span> <span>${detail.commGoodsName }</span>
									</div>
								</div>
							</div>

						</div>
					</div>
				</div>
				<!-- end row -->
			</div>
			<div class="col-12">
				<div class="card-box">
					<div class="title_text">商品明细</div>
					<div class="row col-12 table-responsive">
						<table class="table table-striped dataTable no-footer"
							cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>商品编码</th>
									<th>条形码</th>
									<th>商品货号</th>
									<th>商品名称</th>
									<th>外仓统一码</th>
									<th>品牌</th>
									<th>二级品类</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${itemList}" var="item" varStatus="status">
									<tr>
										<td>${item.goodsCode}</td>
										<td>${item.barcode}</td>
										<td>${item.goodsNo}</td>
										<td>${item.goodsName}</td>
										<td>
											<c:choose>
												<c:when test="${item.outDepotFlag == '0'}" >
													是
												</c:when>
												<c:otherwise>
													否
												</c:otherwise>
											</c:choose>
										</td>
										<td>${item.brandName}</td>
										<td>${item.typeName}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div class="col-12">
				<div class="card-box">
					<div class="form-row m-t-50">
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
		<!-- container -->
	</div>
	</div>

	<script type="text/javascript">

		$(function() {
			//取消按钮
			$("#cancel-buttons").click(function() {
				$module.revertList.isMainList = true;
				$load.a('/list/menu.asyn?act=2001');
			});
			
		});
	</script>