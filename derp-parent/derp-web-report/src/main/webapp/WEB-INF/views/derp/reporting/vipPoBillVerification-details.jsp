<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="/WEB-INF/views/common.jsp" />
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
										<li class="breadcrumb-item"><a href="#">报表管理</a></li>
										<li class="breadcrumb-item"><a
											href="javascript:$module.load.pageReport('act=120010');">唯品PO账单核销报表</a></li>
										<li class="breadcrumb-item"><a href="#">唯品PO账单核销报表详情</a></li>
									</ol>
								</div>
							</div>
							<form id="form1">
								<input type="hidden" name='poNo' id='poNo' value='${detail.po}' />
								<input type="hidden" name='commbarcode' id='commbarcode'
									value='${detail.commbarcode}' />
							</form>
							<div class="title_text">基本信息</div>
							<div class="info_box">
								<div class="info_item">
									<div class="info_text">
										<span>公司：</span> <span>${detail.merchantName }</span>
									</div>
									<div class="info_text">
										<span>客户：</span> <span>${customerName }</span>
									</div>
									<div class="info_text">
										<span>PO单号：</span> <span>${detail.po }</span>
									</div>
									<div class="info_text">
										<span>PO时间：</span> <span>${detail.poDate }</span>
									</div>
								</div>
								<div class="info_item">
									<div class="info_text">
										<span>标准条码：</span> <span>${detail.commbarcode }</span>
									</div>
									<div class="info_text">
										<span>事业部：</span> <span>${detail.buName }</span>
									</div>
									<div class="info_text">
										<span>标准品牌：</span> <span>${detail.brandParent }</span>
									</div>
									<div class="info_text">
										<span>母品牌：</span> <span>${detail.superiorParentBrand }</span>
									</div>
								</div>
								<div class="info_item">
									<div class="info_text">
										<span>商品名称：</span> <span>${goodsName }</span>
									</div>
									<div class="info_text">
										<span>销售币种：</span> <span>${detail.currency }</span>
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
					<div class="title_text">商品上架明细</div>
					<div class="row col-12 table-responsive">
						<table class="table table-striped dataTable no-footer"
							cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>商品货号</th>
									<th>销售订单号</th>
									<th>销售数量</th>
									<th>销售单价</th>
									<th>上架数量</th>
									<th>残损数量</th>
									<th>少货数量</th>
									<th>上架时间</th>
								</tr>
							</thead>
							<tbody id="shelfBody">
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div class="col-12">
				<div class="card-box">
					<div class="title_text">销售出库明细</div>
					<div class="row col-12 table-responsive">
						<table class="table table-striped dataTable no-footer"
							cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>销售出库单号</th>
									<th>唯品账单号</th>
									<th>账单类型</th>
									<th>销售订单号</th>
									<th>商品货号</th>
									<th>出库数量</th>
									<th>出库时间</th>
								</tr>
							</thead>
							<tbody id = "saleOutBody">
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div class="col-12">
				<div class="card-box">
					<div class="title_text">销售退明细</div>
					<div class="row col-12 table-responsive">
						<table class="table table-striped dataTable no-footer"
							cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>销售退订单号</th>
									<th>销售退出库单号</th>
									<th>退货类型</th>
									<th>商品货号</th>
									<th>退货数量</th>
									<th>退货时间</th>
								</tr>
							</thead>
							<tbody id="returnOdepotBody">
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div class="col-12">
				<div class="card-box">
					<div class="title_text">国检抽样明细</div>
					<div class="row col-12 table-responsive">
						<table class="table table-striped dataTable no-footer"
							cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>库存调整单号</th>
									<th>商品货号</th>
									<th>调整类型</th>
									<th>调整数量</th>
									<th>归属月份</th>
									<th>调整时间</th>
								</tr>
							</thead>
							<tbody id="nationalInspectionBody">
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div class="col-12">
				<div class="card-box">
					<div class="title_text">唯品红冲明细</div>
					<div class="row col-12 table-responsive">
						<table class="table table-striped dataTable no-footer"
							cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>库存调整单号</th>
									<th>唯品账单号</th>
									<th>商品货号</th>
									<th>调整类型</th>
									<th>调整数量</th>
									<th>归属月份</th>
									<th>调整时间</th>
								</tr>
							</thead>
							<tbody id="hcBody">
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div class="col-12">
				<div class="card-box">
					<div class="title_text">唯品报废明细</div>
					<div class="row col-12 table-responsive">
						<table class="table table-striped dataTable no-footer"
							cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>库存调整单号</th>
									<th>唯品账单号</th>
									<th>商品货号</th>
									<th>调整类型</th>
									<th>调整数量</th>
									<th>归属月份</th>
									<th>调整时间</th>
								</tr>
							</thead>
							<tbody id="scrapBody">
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div class="col-12">
				<div class="card-box">
					<div class="title_text">唯品盘点明细</div>
					<div class="row col-12 table-responsive">
						<table class="table table-striped dataTable no-footer"
							cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>盘点单号</th>
									<th>唯品账单号</th>
									<th>商品货号</th>
									<th>调整类型</th>
									<th>调整数量</th>
									<th>接收时间</th>
								</tr>
							</thead>
							<tbody id="takeStockBody">
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div class="col-12">
                <div class="card-box">
                    <div class="title_text">唯品调拨明细</div>
                    <div class="row col-12 table-responsive">
                        <table class="table table-striped dataTable no-footer"
                            cellspacing="0" width="100%">
                            <thead>
                                <tr>
                                    <th>调拨单据号</th>
                                    <th>调拨出/入库单号</th>
                                    <th>调拨类型</th>
                                    <th>商品货号</th>
                                    <th>调拨数量</th>
                                    <th>调拨时间</th>
                                </tr>
                            </thead>
                            <tbody id="transferBody">
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
				$module.load.pageReport("act=12010");
			});
			
	        //获取销售上架明细
	        getDetails("shelf" , function(result){
	        	var html = "" ;
	        	var list = result.data ;
	        	
	        	$(list).each(function(index , item){
	        		
	        		
	        		html += '<tr>' + 
                    '<td>' + item.goodsNo +'</td>' + 
                    '<td>' + item.orderNo + '</td>' +
                    '<td>' + formatReturnObj(item.num) + '</td>' + 
                    '<td>' + formatReturnObj(item.salePrice) + '</td>' + 
                    '<td>' + formatReturnObj(item.shelfNum) + '</td>' +
                    '<td>' + formatReturnObj(item.damagedNum) + '</td>' + 
                    '<td>' + formatReturnObj(item.lackNum) + '</td>' + 
                    '<td>' + formatReturnObj(item.shelfDate) + '</td>' +
                    '</tr>' ;
	        	}) ;
	        	
	        	$("#shelfBody").html(html) ; 
	        }) ;
	        
	        //获取销售出库明细
            getDetails("saleOut" , function(result){
                var html = "" ;
                var list = result.data ;
                
                $(list).each(function(index , item){
                    
                    html += '<tr>' + 
                    '<td>' + item.saleOutOrder +'</td>' + 
                    '<td>' + item.vipBillCode + '</td>' +
                    '<td>' + formatReturnObj(item.billType) + '</td>' + 
                    '<td>' + formatReturnObj(item.saleOrder) + '</td>' +
                    '<td>' + formatReturnObj(item.goodsNo) + '</td>' + 
                    '<td>' + formatReturnObj(item.outDepotNum) + '</td>' + 
                    '<td>' + formatReturnObj(item.outDepotDate) + '</td>' +
                    '</tr>' ;
                }) ;
                
                $("#saleOutBody").html(html) ; 
            }) ;
	        
          //获取销售退出库明细
            getDetails("returnOdepot" , function(result){
                var html = "" ;
                var list = result.data ;
                
                $(list).each(function(index , item){
                    
                    
                    html += '<tr>' + 
                    '<td>' + item.saleReturnOrder +'</td>' + 
                    '<td>' + item.saleReturnOdepotOrder + '</td>' +
                    '<td>' + formatReturnObj(item.saleReturnOdepotType) + '</td>' + 
                    '<td>' + formatReturnObj(item.goodsNo) + '</td>' +
                    '<td>' + formatReturnObj(item.saleReturnOdepotNum) + '</td>' + 
                    '<td>' + formatReturnObj(item.saleReturnOdepotDate) + '</td>' + 
                    '</tr>' ;
                }) ;
                
                $("#returnOdepotBody").html(html) ; 
            }) ;
          
          //获取国检抽样明细
            getDetails("nationalInspection" , function(result){
                var html = "" ;
                var list = result.data ;
                
                $(list).each(function(index , item){
                    
                    
                    html += '<tr>' + 
                    '<td>' + item.adjustmentInventoryOrder +'</td>' + 
                    '<td>' + item.goodsNo + '</td>' +
                    '<td>' + formatReturnObj(item.adjustmentInventoryType) + '</td>' + 
                    '<td>' + formatReturnObj(item.adjustmentInventoryNum) + '</td>' +
                    '<td>' + formatReturnObj(item.adjustmentInventoryMonths) + '</td>' + 
                    '<td>' + formatReturnObj(item.adjustmentInventoryDate) + '</td>' + 
                    '</tr>' ;
                }) ;
                
                $("#nationalInspectionBody").html(html) ; 
            }) ;
          
          //获取唯品红冲明细
            getDetails("hc" , function(result){
                var html = "" ;
                var list = result.data ;
                
                $(list).each(function(index , item){
                    
                    
                    html += '<tr>' + 
                    '<td>' + item.adjustmentInventoryOrder +'</td>' + 
                    '<td>' + item.sourceCode + '</td>' +
                    '<td>' + formatReturnObj(item.goodsNo) + '</td>' + 
                    '<td>' + formatReturnObj(item.adjustmentInventoryType) + '</td>' +
                    '<td>' + formatReturnObj(item.adjustmentInventoryNum) + '</td>' + 
                    '<td>' + formatReturnObj(item.adjustmentInventoryMonths) + '</td>' + 
                    '<td>' + formatReturnObj(item.adjustmentInventoryDate) + '</td>' + 
                    '</tr>' ;
                }) ;
                
                $("#hcBody").html(html) ; 
            }) ;
          
          //获取唯品报废明细
            getDetails("scrap" , function(result){
                var html = "" ;
                var list = result.data ;
                
                $(list).each(function(index , item){
                    
                    
                    html += '<tr>' + 
                    '<td>' + item.adjustmentInventoryOrder +'</td>' + 
                    '<td>' + item.sourceCode + '</td>' +
                    '<td>' + formatReturnObj(item.goodsNo) + '</td>' + 
                    '<td>' + formatReturnObj(item.adjustmentInventoryType) + '</td>' +
                    '<td>' + formatReturnObj(item.adjustmentInventoryNum) + '</td>' + 
                    '<td>' + formatReturnObj(item.adjustmentInventoryMonths) + '</td>' + 
                    '<td>' + formatReturnObj(item.adjustmentInventoryDate) + '</td>' + 
                    '</tr>' ;
                }) ;
                
                $("#scrapBody").html(html) ; 
            }) ;
          
          //获取唯品盘点明细
            getDetails("takeStock" , function(result){
                var html = "" ;
                var list = result.data ;
                
                $(list).each(function(index , item){
                    
                    
                    html += '<tr>' + 
                    '<td>' + item.takesStockResultOrder +'</td>' + 
                    '<td>' + item.sourceCode + '</td>' +
                    '<td>' + formatReturnObj(item.goodsNo) + '</td>' + 
                    '<td>' + formatReturnObj(item.takesStockResultType) + '</td>' +
                    '<td>' + formatReturnObj(item.takesStockResultNum) + '</td>' + 
                    '<td>' + formatReturnObj(item.receiveTime) + '</td>' + 
                    '</tr>' ;
                }) ;
                
                $("#takeStockBody").html(html) ; 
            }) ;
          
          //获取唯品盘点明细
            getDetails("transfer" , function(result){
                var html = "" ;
                var list = result.data ;
                
                $(list).each(function(index , item){
                    
                    
                    html += '<tr>' + 
                    '<td>' + item.transferOrder +'</td>' + 
                    '<td>' + item.transferDepotOrder + '</td>' +
                    '<td>' + formatReturnObj(item.transferType) + '</td>' + 
                    '<td>' + formatReturnObj(item.goodsNo) + '</td>' +
                    '<td>' + formatReturnObj(item.transferNum) + '</td>' + 
                    '<td>' + formatReturnObj(item.transferTime) + '</td>' + 
                    '</tr>' ;
                }) ;
                
                $("#transferBody").html(html) ; 
            }) ;
		});
		
		function getDetails(type, succFunc){
			
			var poNo = '${detail.po }' ;
			var commbarcode = '${detail.commbarcode }' ;
			
			var url = serverAddr+"/vipPoBillVerification/getDetailsByType.asyn?r="+Math.random(); 
	         $custom.request.ajax(url,{"type": type , "poNo" : poNo, "commbarcode" : commbarcode},function(result){
	             if(result.state==200){
	            	 succFunc(result) ;
	              }else{
	                  $custom.alert.error(result.data.message);
	              }
	         });
		}
		
		function formatReturnObj(obj){
			if(obj){
				return obj ;
			}else{
				return "" ;
			}
		}
	</script>