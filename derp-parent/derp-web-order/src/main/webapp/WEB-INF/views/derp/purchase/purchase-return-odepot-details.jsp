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
											href="javascript:dh_list();">采购退货出库订单列表</a></li>
										<li class="breadcrumb-item"><a
											href="javascript:dh_details(${detail.id });">采购退货出库订单详情</a></li>
									</ol>
								</div>
							</div>
							<!--  title   end  -->
							<!--  title   基本信息   start -->

							<div class="title_text">基本信息</div>
								<div class="info_box">
									<div class="info_item">
										<div class="info_text">
											<span>单据状态：</span> <span>${detail.statusLabel }</span>
										</div>
										<div class="info_text">
										</div>
										<div class="info_text">
										</div>
									</div>
								</div>
								<div class="info_box">
									<div class="info_item">
										<div class="info_text">
											<span>出库清单编号：</span> <span> ${detail.code } </span>
										</div>
										<div class="info_text">
											<span>供应商：</span> <span>${detail.supplierName }</span>
										</div>
										<div class="info_text">
											<span>物流企业名称：</span> <span>${detail.logisticsName }</span>
										</div>
									</div>
									<div class="info_item">
										<div class="info_text">
											<span>公司：</span> <span>${detail.merchantName }</span>
										</div>
										<div class="info_text">
											<span>采购退订单号：</span> <span> ${detail.purchaseReturnCode } </span>
										</div>
										<div class="info_text">
											<span>出库时间：</span> <span> ${detail.returnOutDate } </span>
										</div>
									</div>
									<div class="info_item">
										<div class="info_text">
											<span>PO单号：</span> <span> ${detail.poNo } </span>
										</div>
										<div class="info_text">
											<span>LBX号：</span> <span> ${detail.lbxNo } </span>
										</div>
										<div class="info_text">
											<span>提单号：</span> <span> ${detail.blNo } </span>
										</div>
									</div>
									<div class="info_item">
										<div class="info_text">
											<span>出库仓库：</span> <span> ${detail.outDepotName }
											</span>
										</div>
										<div class="info_text">
											<span>事业部：</span> <span> ${detail.buName }
											</span>
										</div>
										<div class="info_text">
											<span>运单号：</span> <span> ${detail.wayBillNo } </span>
										</div>
									</div>
									<div class="info_item">
										<div class="info_text">
											<span>币种：</span> <span> ${detail.currencyLabel }
											</span>
										</div>
										<div class="info_text">
											<span>进口模式：</span> <span> ${detail.importModeLabel }
											</span>
										</div>
										<div class="info_text">
											<span>理货单位：</span> <span> ${detail.tallyingUnitLabel }
											</span>
										</div>
									</div>
								</div>
						</div>
						<!--   款项信息  end -->
						<!--   商品信息  start -->
						<div class="title_text">出库商品明细</div>
						<div class="form-row mt20">
							<table id="table-list"
								class="table table-striped table-responsive" cellspacing="0"
								width="100%">
								<thead>
									<tr>
										<th style="width: 12.5%;"> &nbsp;&nbsp;&nbsp;商品货号&nbsp;&nbsp;&nbsp;</th>
                                        <th style="width: 12.5%;">商品名称</th>
                                        <th style="width: 12.5%;">商品条形码</th>
                                        <th style="width: 12.5%;">数量</th>
                                        <th style="width: 12.5%;">批次号</th>
                                        <th style="width: 12.5%;">生产日期</th>
                                        <th style="width: 12.5%;">失效日期</th>
                                        <th style="width: 12.5%;">&nbsp;&nbsp;&nbsp;备&nbsp;注&nbsp;&nbsp;&nbsp;</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${itemList }" var="item">
										<tr>
											<td>${item.goodsNo }</td>
											<td>${item.goodsName }</td>
											<td>${item.barcode }</td>
											<td>${item.num }</td>
											<td>${item.batchNo }</td>
											<td><fmt:formatDate value="${item.productionDate}" pattern="yyyy-MM-dd" /></td>
											<td><fmt:formatDate value="${item.overdueDate}" pattern="yyyy-MM-dd" /></td>
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
            $module.load.pageOrder("act=3009");
        }

        function dh_details(id) {
            //$load.a(pageUrl+"30013&id="+id);
            $module.load.pageOrder("act=30091&id=" + id);
        } 

        //返回
        $("#cancel-buttons").click(function () {
            $module.load.pageOrder("act=3009");
        });
    });
</script>
