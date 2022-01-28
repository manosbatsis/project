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
					<!--  title   start  -->
					<div class="col-12">
						<div>
							<div class="col-12 row">
								<div>
									<ol class="breadcrumb">
										<li><i class="block"></i> 当前位置：</li>
										<li class="breadcrumb-item"><a
											href="javascript:dh_list();">采购退货订单列表</a></li>
										<li class="breadcrumb-item"><a
											href="javascript:dh_details(${detail.id });">采购退货订单详情</a></li>
									</ol>
								</div>
							</div>
							<!--  title   end  -->
							<!--  title   基本信息   start -->

							<div class="title_text">基本信息</div>
								<div class="info_box">
									<div class="info_item">
										<div class="info_text">
											<span>采购退货订单号：</span> <span>${detail.code }</span>
										</div>
										<div class="info_text">
										</div>
										<div class="info_text">
										</div>
									</div>
									<div class="info_item">
										<div class="info_text">
											<span>供应商：</span> <span>${detail.supplierName }</span>
										</div>
										<div class="info_text">
											<span>公司：</span> <span> ${detail.merchantName } </span>
										</div>
										<div class="info_text">
											<span>出仓仓库：</span> <span>${detail.outDepotName }</span>
										</div>
									</div>
									<div class="info_item">
										<div class="info_text">
											<span>是否同关区：</span> <span>${detail.isSameAreaLabel }</span>
										</div>
										<div class="info_text">
											<span>入仓仓库：</span> <span> ${detail.inDepotName } </span>
										</div>
										<div class="info_text">
											<span>事业部：</span> <span> ${detail.buName } </span>
										</div>
									</div>
									<div class="info_item">
										<div class="info_text">
											<span>PO单号：</span> <span> ${detail.poNo } </span>
										</div>
										<div class="info_text">
											<span>采购币种：</span> <span> ${detail.currencyLabel } </span>
										</div>
										<div class="info_text">
											<span>合同号：</span> <span> ${detail.contractNo } </span>
										</div>
									</div>
									<div class="info_item">
										<div class="info_text">
											<span>理货单位：</span> <span> ${detail.tallyingUnitLabel }
											</span>
										</div>
										<div class="info_text">
											<span>目的地：</span> <span> ${detail.destinationNameLabel }
											</span>
										</div>
										<div class="info_text">
											<span>备注：</span> <span> ${detail.remark } </span>
										</div>
									</div>
									<div class="info_item">
										<div class="info_text">
											<span>目的地址：</span> <span> ${detail.destinationAddress }
											</span>
										</div>
										<div class="info_text">
										</div>
										<div class="info_text">
										</div>
									</div>
									<div class="info_item">
										<div class="info_text">
											<span>创建人：</span> <span> ${detail.createName }
											</span>
										</div>
										<div class="info_text">
											<span>创建时间：</span> <span> <fmt:formatDate value="${detail.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
											</span>
										</div>
										<div class="info_text">
										</div>
									</div>
									<div class="info_item">
										<div class="info_text">
											<span>审核人：</span> <span> ${detail.auditName }
											</span>
										</div>
										<div class="info_text">
											<span>审核时间：</span> <span> <fmt:formatDate value="${detail.auditDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
											</span>
										</div>
										<div class="info_text">
										</div>
									</div>
								</div>
								<div class="title_text">收件信息</div>
								<div class="info_box">
									<div class="info_item">
										<div class="info_text">
											<span>提货方式：</span> <span> ${detail.deliveryMethodLabel }
											</span>
										</div>
										<div class="info_text">
											<span>收货人姓名：</span> <span> ${detail.deliveryName }
											</span>
										</div>
										<div class="info_text">
										</div>
									</div>
								</div>
								<div class="info_box">
									<div class="info_item">
										<div class="info_text">
											<span>国      家：</span> <span> ${detail.country }
											</span>
										</div>
										<div class="info_text">
											<span>详细地址：</span> <span> ${detail.deliveryAddr }
											</span>
										</div>
										<div class="info_text">
										</div>
									</div>
								</div>
						</div>
						<!--   款项信息  end -->
						<!--   商品信息  start -->
						<div class="title_text">采购退货商品明细</div>
						<div class="form-row mt20">
							<table id="table-list"
								class="table table-striped table-responsive" cellspacing="0"
								width="100%">
								<thead>
									<tr>
										<th style="width: 10%;"> &nbsp;&nbsp;&nbsp;商品货号&nbsp;&nbsp;&nbsp;</th>
                                        <th style="width: 10%;">商品名称</th>
                                        <th style="width: 10%;">商品条形码</th>
                                        <th style="width: 10%;">退货商品数量</th>
                                        <th style="width: 10%;">退货单价</th>
                                        <th style="width: 10%;">申报单价</th>
                                        <th style="width: 10%;">退货总金额</th>
                                        <th style="width: 10%;">品牌类型</th>
                                        <th style="width: 10%;">&nbsp;&nbsp;&nbsp;箱&nbsp;号&nbsp;&nbsp;&nbsp;</th>
                                        <th style="width: 10%;">&nbsp;&nbsp;合&nbsp;同&nbsp;号&nbsp;&nbsp;&nbsp;</th>
                                        <th style="width: 10%;">&nbsp;&nbsp;&nbsp;备&nbsp;注&nbsp;&nbsp;&nbsp;</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${itemList }" var="item">
										<tr>
											<td>${item.goodsNo }</td>
											<td>${item.goodsName }</td>
											<td>${item.barcode }</td>
											<td>${item.returnNum }</td>
											<td>${item.returnPrice }</td>
											<td>${item.declarePrice }</td>
											<td>${item.returnAmount }</td>
											<td>${item.brandName }</td>
											<td>${item.boxNo }</td>
											<td>${item.contractNo }</td>
											<td>${item.remark }</td>
										</tr>
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
											<button type="button"
												class="btn btn-find waves-effect waves-light btn_default"
												id="cancel-buttons">返 回</button>
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
            $module.load.pageOrder("act=3008");
        }

        function dh_details(id) {
            //$load.a(pageUrl+"30013&id="+id);
            $module.load.pageOrder("act=30081&id=" + id);
        } 

        //返回
        $("#cancel-buttons").click(function () {
            $module.load.pageOrder("act=3008");
        });
    });
</script>
