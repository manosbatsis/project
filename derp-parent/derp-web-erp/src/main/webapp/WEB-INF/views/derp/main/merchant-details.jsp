<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<style>
	.span-br {
	    word-break:normal; 
	    width:auto; 
	    display:block; 
	    white-space:pre-wrap;
	    word-wrap : break-word ;
	    overflow: hidden ;
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
										<li class="breadcrumb-item"><a href="#">公司档案</a></li>
										<li class="breadcrumb-item "><a
											href="javascript:$load.a('/merchant/toPage.html');">公司列表</a></li>
										<li class="breadcrumb-item "><a
											href="javascript:$load.a('/merchant/toDetailsPage.html?id=${detail.id }');">公司信息详情</a></li>
									</ol>
								</div>
							</div>
							<!--  title   end  -->
							<!--  title   基本信息   start -->
							<div class="title_text">公司信息</div>
							<form id="add-form">
								<div class="info_box">
									<div class="info_item">
										<div class="col-4">
											<div class="info_text">
												<input type="hidden" value="${detail.id }" name="id" id="merchantId">
												<span>公司简称：</span> <span> ${detail.name } </span>
											</div>
										</div>
										<div class="info_text">
											<span>卓志编码：</span> <span> ${detail.topidealCode } </span>
										</div>
									</div>
									<div class="info_item">
										<div class="col-4">
											<div class="info_text">
												<span>公司全称：</span> <span> ${detail.fullName } </span>
											</div>
										</div>
										<div class="info_text">
										</div>
									</div>
									<div class="info_item">
										<div class="col-4">
											<div class="info_text">
												<span>商品备案价申报系数：</span> <span>${detail.priceDeclareRatio } </span>
											</div>
										</div>
										<div class="info_text">
											<span>备注：</span> <span> ${detail.remark } </span>
										</div>
									</div>
									<div class="info_item">
										<div class="col-4">
											<div class="info_text">
												<span>是否代理：</span> <span> ${ detail.isProxyLabel} </span>
											</div>
										</div>
										<div class="info_text">
											<span>授权码：</span> <span> ${detail.permission } </span>
											
										</div>
									</div>
									<div class="info_item">
										<div class="col-4">
											<div class="info_text">
												<span>联系电话：</span> <span> ${detail.tel } </span>
											</div>
										</div>
										<div class="info_text">
											<span>勾稽完结百分比(%)：</span> <span> <c:if
														test="${ detail.articulationPercent!=null}">${detail.articulationPercent*100 }</c:if>
												</span>
										</div>
									</div>
									<div class="info_item">
										<div class="col-4">
											<div class="info_text">
												<span>成本核算币种：</span>
												<span> ${detail.accountCurrencyLabel } </span>
											</div>
										</div>
										<div class="info_text">
											<span>核算单价：</span>
											<span style="width:56%;">${detail.accountPriceLabel } </span>
										</div>
									</div>
									<div class="info_item">
										<div class="col-4">
											<div class="info_text">
												<span>提醒财务付款邮箱：</span> 
												<span class="span-br">${detail.financePayEmail } </span>
											</div>
										</div>
										<div class="info_text">
											<span>英文名称：</span>
											<span> ${detail.englishName } </span>
										</div>
									</div>

									<div class="info_item">
										<div class="col-4">
											<div class="info_text">
												<span>接收清关资料邮箱：</span>
												<span class="span-br"> ${detail.email } </span>
											</div>
										</div>
										<div class="info_text">
											<span>盘点结果通知邮箱：</span>
											<span class="span-br" style="width:56%;">${detail.inventoryResultEmail } </span>
										</div>
									</div>

									<div class="info_item">
										<div class="col-4">
											<div class="info_text">
												<span>注册地址：</span>
												<span> ${detail.registeredAddress }
											</span>
											</div>
										</div>
										<div class="info_text">
											<span>注册地址(英文)：</span>
											<span> ${detail.englishRegisteredAddress }</span>
										</div>
										<%--<div class="info_text">
										</div>--%>
									</div>

									<div class="info_item">
										<div class="col-4">
											<div class="info_text">
												<span>注册地类型：</span>
												<span> ${detail.registeredTypeLabel} </span>
											</div>
										</div>
									</div>
								</div>

							</form>
							<div class="title_text">银行信息</div>
							<div class="info_item">
								<div class="col-4">
									<div class="info_text">
										<span>开户银行：</span>
										<span >${detail.depositBank } </span>
									</div>
								</div>
								<div class="info_text">
									<span>银行账户：</span> <span> ${detail.beneficiaryName } </span>
								</div>
							</div>
							<div class="info_item">
								<div class="col-4">
									<div class="info_text">
										<span>银行账号：</span>
										<span >${detail.bankAccount } </span>
									</div>
								</div>
								<div class="info_text">
									<span>Swift Code：</span> <span> ${detail.swiftCode } </span>
								</div>
							</div>
							<div class="info_item">
								<div class="col-4">
									<div class="info_text">
										<span>开户行地址：</span>
										<span >${detail.bankAddress } </span>
									</div>
								</div>
							</div>
							<!--  title   基本信息  start -->
							<!--   商品信息  start -->
							<div class="form-row mt20">
								<table id="table-depot-list" class="table table-striped">
									<thead>
										<tr>
											<th>仓库名称</th>
											<th>仓库编号</th>
											<th>合同编号</th>
											<th>进出接口指令</th>
											<th>统计存货跌价</th>
											<th>选品限制</th>
											<th>以入定出</th>
											<th>以出定入</th>
											<th>绑定事业部</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${depotMerchantRelList }" var="merchant">
											<tr>
												<td><input type="hidden" class="depot_id"
													name="depot_id" value="${merchant.depotId }">
													${merchant.depotName}</td>
												<td>${merchant.depotCode}</td>
												<td>${merchant.contractCode}</td>
												<td><c:choose>
														<c:when test="${merchant.isInOutInstruction eq '1' }">
				                              	是
				                          </c:when>
														<c:otherwise>
				                              	否
				                          </c:otherwise>
													</c:choose></td>
												<td><c:choose>
														<c:when test="${merchant.isInvertoryFallingPrice eq '1' }">
				                              	是
				                          </c:when>
														<c:otherwise>
				                              	否
				                          </c:otherwise>
													</c:choose></td>
												<td><c:choose>
														<c:when test="${merchant.productRestriction eq '1' }">
				                              	仅备案商品
				                          </c:when>
														<c:when test="${merchant.productRestriction eq '2' }">
				                              	仅外仓统一码
				                          </c:when>
														<c:when test="${merchant.productRestriction eq '3' }">
				                              	无限制
				                          </c:when>
													</c:choose></td>
												<td><c:choose>
														<c:when test="${merchant.isInDependOut eq '1' }">
				                              	是
				                          </c:when>
														<c:otherwise>
				                              	否
				                          </c:otherwise>
													</c:choose></td>
												<td><c:choose>
														<c:when test="${merchant.isOutDependIn eq '1' }">
				                              	是
				                          </c:when>
														<c:otherwise>
				                              	否
				                          </c:otherwise>
													</c:choose></td>
												<td>
												 </td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
							<div class="title_text">代理公司</div>
							<div class="form-row mt20">
								<table id="table-list" class="table table-striped">
									<thead>
										<tr>
											<td>公司简称</td>
											<td>公司编号</td>

										</tr>
									</thead>
									<tbody>
										<c:forEach items="${list }" var="merchant">
											<tr>
												<td>${merchant.name}</td>
												<td>${merchant.code}</td>

											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>

							<!--   商品信息  end -->
							<div class="form-row m-t-50">
								<div class="col-12">
									<div class="row">
										<div class="col-6">
											<button type="button"
												class="btn btn-find waves-effect waves-light btn_default"
												id="cancel-buttons">返回</button>
										</div>
										<div class="col-6"></div>
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
	
	initBusiness() ;

	//返回
	$("#cancel-buttons").click(function(){
		$load.a("/merchant/toPage.html");
	});
	
	function initBusiness(){
		var merchantId = $("#merchantId").val() ;
		$("#table-depot-list tbody").find("tr").each(function(index, item){
			var depotId = $(item).find("input[name='depot_id']").val() ;
			
			var data = {"depotId" : depotId , "merchantId" : merchantId}
			$custom.request.ajax("/merchantDepotBuRel/getBuNameByMerchantDepot.asyn",data,function(result) {
				var buName = result.data ; 
				
				if(buName){
					$(item).children("td:last-child").text(buName) ;
				}
			});
			
		}) ;
		
	}
	
</script>