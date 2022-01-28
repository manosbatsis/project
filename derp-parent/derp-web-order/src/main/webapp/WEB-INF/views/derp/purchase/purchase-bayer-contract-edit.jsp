<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common.jsp" />
<style type="text/css">
	.title_text_new{
	    line-height: 40px;
	    font-size: 24px;
	    font-weight: bold;
	    text-align: center;
	}
	.info_box { 
		padding: 10px;
	}
	
	table {
	    border-collapse: collapse;
	    border-spacing: 0;
	}
	 
	td,th {
	    padding: 0;
	}
	 
	.pure-table {
	    border-collapse: collapse;
	    border-spacing: 0;
	    empty-cells: show;
	    border: 2px solid #cbcbcb;
	    width: 95% ;
	    margin-top:25px ;
	    text-align: center;
	}
	 
	.pure-table caption {
	    color: #000;
	    font: italic 85%/1 arial,sans-serif;
	    padding: 1em 0;
	    text-align: center;
	}
	 
	.pure-table td,.pure-table th {
	    border-left: 2px solid #cbcbcb;
	    border-width: 0 0 0 1px;
	    font-size: inherit;
	    margin: 0;
	    overflow: visible;
	    padding: .5em 1em;
	}
	 
	.pure-table thead {
	    background-color: #e0e0e0;
	    color: #000;
	    text-align: left;
	    vertical-align: bottom;
	}
	 
	.pure-table td {
	    background-color: transparent;
	}
	 
	.pure-table-bordered td {
	    border-bottom: 2px solid #cbcbcb;
	}
	 
	.pure-table-bordered tbody>tr:last-child>td {
	    border-bottom-width: 0;
	}
</style>
<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
        <form id="edit-form">
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
                                            <li class="breadcrumb-item"><a href="#">采购档案</a></li>
                                			<li class="breadcrumb-item"><a href="javascript:dh_list();">采购订单</a></li>
                                            <li class="breadcrumb-item"><a href="#">编辑采购合同</a></li>
                                        </ol>
                                    </div>
                                </div>
                                <!--  title   end  -->
                                    <div>
                                    <input type="hidden" id="orderId" value="${id }">
                                    <form id="edit-form">
                                    <input type="hidden" name="fileTempCode" value="Bayer">
                                    <input type="hidden" name="code" value="${detail.code }">
                                    	<table class="pure-table pure-table-bordered" >
                                    		<tr style="text-align:center;">
                                    			<td colspan="11" >
	                                    			${detail.buyerEnName }
	                                    			<br>
	                                    			${detail.buyerAddress }
                                    			</td>
                                    		</tr>
                                    		<tr>
                                    			<td colspan="11" style="text-align:center;font-weight: 750 ;font-size: 16px;">
                                    				Order Placement
                                    			</td>
                                    		</tr>
                                    		<tr>
                                    			<td colspan="2">
                                    				PO Number
                                    			</td>
                                    			<td style="width:150px;">
                                    				${detail.poNo }
                                    				<input type="hidden" name="detail.poNo" value="${detail.poNo }">
                                    			</td>
                                    			<td colspan="2">
                                    				Week Number
                                    			</td>
                                    			<td colspan="6">
                                    				<input type="text" name="detail.weekNumber" style="width: 500px;" value="${detail.weekNumber }">
                                    			</td>
                                    		</tr>
                                    		<tr>
                                    			<td colspan="2">
                                    				Buyer Name
                                    			</td>
                                    			<td style="width:150px;">
                                    				${detail.buyerEnName }
                                    				<input type="hidden" name="detail.buyerEnName" value="${detail.buyerEnName }">
                                    			</td>
                                    			<td colspan="2">
                                    				Seller Name
                                    			</td>
                                    			<td colspan="6">
                                    				${detail.sellerEnName }
                                    				<input type="hidden" name="detail.sellerEnName" value="${detail.sellerEnName }">
                                    			</td>
                                    		</tr>
                                    		<tr>
                                    			<td colspan="2">
                                    				Buyer Address
                                    			</td>
                                    			<td style="width:150px;">
                                    				${detail.buyerAddress }
                                    				<input type="hidden" name="detail.buyerAddress" value="${detail.buyerAddress }">
                                    			</td>
                                    			<td colspan="2">
                                    				Seller Address
                                    			</td>
                                    			<td colspan="6">
                                    				${detail.sellerAddress }
                                    				<input type="hidden" name="detail.sellerAddress" value="${detail.sellerAddress }">
                                    			</td>
                                    		</tr>
                                    		<tr>
                                    			<td colspan="11" style="text-align: right;padding-right: 50px;">
                                    				Value (${detail.currency})
                                    				<input type="hidden" name="detail.currency" value="${detail.currency }">
                                    			</td>
                                    		</tr>
                                    		<tr>
                                    			<td>NO</td>
                                    			<td>Brand</td>
                                    			<td>Product Name</td>
                                    			<td style="width:100px;">SKUs</td>
                                    			<td>Globan</td>
                                    			<td>条码</td>
                                    			<td>BatchNo.</td>
                                    			<td>Expired date</td>
                                    			<td style="width:100px;">合计总数</td>
                                    			<td>Purchase Price</td>
                                    			<td>Net Sales </td>
                                    		</tr>
                                    		
                                    		<c:forEach items="${goodsList }" var="item">
                                    			<tr>
	                                    			<td>
	                                    			    ${item.index }
	                                    			    <input type="hidden" name="goods.index" value="${item.index }">
	                                    			</td>
	                                    			<td>
	                                    			    <input type="text" name="goods.brand" value="${item.brand }">
	                                    			</td>
	                                    			<td>
	                                    			    <input type="text" name="goods.productName" value="${item.productName }">
	                                    			</td>
	                                    			<td>
	                                    			    <input type="text" name="goods.skus" value="${item.skus }" style="width:100px;">
	                                    			</td>
	                                    			<td><input type="text" name="goods.globan" value="${item.globan }" style="width:100px;"></td>
	                                    			<td>
	                                    			    ${item.barcode}
	                                    			    <input type="hidden" name="goods.barcode" value="${item.barcode }">
	                                    			</td>
	                                    			<td><input type="text" name="goods.batch" value="${item.batch }" style="width:100px;"></td>
	                                    			<td><input type="text" name="goods.expiredDate" value="${item.expiredDate }" style="width:100px;"></td>
	                                    			<td>
	                                    			    ${item.num}
	                                    			    <input type="hidden" name="goods.num" value="${item.num }">
	                                    			</td>
	                                    			<td>
	                                    			    <c:if test="${empty item.price }">${detail.currency}</c:if> ${item.price}
	                                    			    <input type="hidden" name="goods.price" value="<c:if test="${empty item.price }">${detail.currency}</c:if> ${item.price}">
	                                    			</td>
	                                    			<td>
	                                    			    <c:if test="${empty item.amount }">${detail.currency}</c:if> ${item.amount}
	                                    			    <input type="hidden" name="goods.amount" value="<c:if test="${empty item.amount }">${detail.currency}</c:if> ${item.amount}">
	                                    			</td>
	                                    		</tr>
                                    		</c:forEach>
                                    		
                                    		<tr>
                                    			<td><br></td>
                                    			<td></td>
                                    			<td></td>
                                    			<td></td>
                                    			<td></td>
                                    			<td></td>
                                    			<td></td>
                                    			<td></td>
                                    			<td></td>
                                    			<td></td>
                                    			<td></td>
                                    		</tr>
                                    		
                                    		<tr>
                                    			<td></td>
                                    			<td></td>
                                    			<td></td>
                                    			<td></td>
                                    			<td></td>
                                    			<td></td>
                                    			<td></td>
                                    			<td></td>
                                    			<td>
                                    			     ${detail.totalNum}
                                    			     <input type="hidden" name="detail.totalNum" value="${detail.totalNum}">
                                    			</td>
                                    			<td></td>
                                    			<td>
                                    			     ${detail.currency} ${detail.totalAccount}
                                    			     <input type="hidden" name="detail.totalAccount" value="${detail.totalAccount}">
                                    			</td>
                                    		</tr>
                                    		<tr>
                                    			<td><br></td>
                                    			<td colspan="10"></td>
                                    		</tr>
                                    		<tr>
                                    			<td rowspan="2" colspan="2">Signature</td>
                                    			<td colspan="2"></td>
                                    			<td colspan="7">Seller Signature</td>
                                    		</tr>
                                    		<tr>
                                    			<td colspan="2"><br></td>
                                    			<td colspan="7"></td>
                                    		</tr>  
                                    		<tr>
                                    			<td colspan="11" style="text-align: left;">Delivery address：<input type="text" name="detail.deliveryAddress" value="${detail.deliveryAddress }" style="width: 60%;"></td>
                                    		</tr>  
                                    	</table>
                                    	<div>
	                                    	<div class="mt20" id="statusDiv">
	                                    		<i class="red">*</i> <span>审核结果：</span> <label><input type="radio" name="status" value="003">通过</label> <label><input type="radio" name="status" value="001">驳回</label>
	                                    	</div>
	                                    </div>
                                    </form>
								<div class="form-row m-t-50">
			                          <div class="col-12">
			                              <div class="row">
			                                  <div class="col-4">
			                                  </div>
			                                  <div class="col-6">
			                                      <input type="button" class="btn btn-warning waves-effect waves-light btn_default" id="cancel-buttons" value="取 消">
			                                      <input type="button" class="btn btn-light waves-effect waves-light btn_default" id="last-buttons" value="上一步">
			                                      <input type="button" class="btn btn-info waves-effect waves-light btn_default" id="save-buttons" value="保 存">
			                                      <input type="button" class="btn btn-info waves-effect waves-light btn_default delayedBtn" id="audit-buttons" value="保存并审核">
			                                      <input type="button" class="btn btn-info waves-effect waves-light btn_default delayedBtn" id="submit-buttons" value="保存并提交">
			                                  </div>
			                                  <div class="col-4">
			                                  </div>
			                              </div>
			                          </div>
			                      </div>

                            </div>
                        </div>
                    </div>
                    <!--======================Modal框===========================  -->
                    <!-- end row -->
                </div>
            </div>
        </form>
    </div>
    <!-- container -->
</div>
<jsp:include page="/WEB-INF/views/modals/2013.jsp" />
<jsp:include page="/WEB-INF/views/modals/2019.jsp" />
<jsp:include page="/WEB-INF/views/modals/4005.jsp" />
</div> <!-- content -->
<script type="text/javascript">
	
	//采购订单状态
	var status = "" ;
	init() ;
	
	//返回
	$("#cancel-buttons").click(function(){
		$module.load.pageOrder("act=3001");
	});
	
	//上一步
	$("#last-buttons").click(function(){
		var url = serverAddr+"/purchase/modifyJsonPurchaseContract.asyn";
		var json = getFormJson($("#edit-form")) ;
		json = JSON.stringify(json);
		
		$custom.request.ajaxJson(url, json, function(result){
			if(result.state == '200'){
				setTimeout(function () {
					var id = $("#orderId").val() ;
					$load.a($module.params.loadOrderPageURL+"act=30012&id="+id);
				}, 1000);
			}else{
				if(result.expMessage != null){
					$custom.alert.error(result.expMessage);
				}else{
					$custom.alert.error(result.message);
				}
			}
		}); 
	});
	
	//保存按钮
	$("#save-buttons").click(function(){
		
		var url = serverAddr+"/purchase/modifyJsonPurchaseContract.asyn";
		var json = getFormJson($("#edit-form")) ;
		json = JSON.stringify(json);
		
		$custom.request.ajaxJson(url, json, function(result){
			if(result.state == '200'){
				$custom.alert.success("编辑成功！");
				setTimeout(function () {
					$module.load.pageOrder("act=3001");
				}, 1000);
			}else{
				if(result.expMessage != null){
					$custom.alert.error(result.expMessage);
				}else{
					$custom.alert.error(result.message);
				}
			}
		}); 
	});
	
	//保存按钮
	$("#audit-buttons").click(function(){
		
		time(this);
		
		var queryAttributionDateUrl = serverAddr+"/purchase/getAttributionDate.asyn" ;
		var id = $("#orderId").val() ;
		
		$custom.request.ajax(queryAttributionDateUrl, {"id" : id}, function(result){
			if(result.state == '200'){
				var data = result.data ;
				
				if(data){
					data = data.split(" ")[0] ;
				}
				
				$m2013.init(data, saveAttrubutionDate) ;
				
			}else{
				if(result.expMessage != null){
					$custom.alert.error(result.expMessage);
				}else{
					$custom.alert.error(result.message);
				}
			}
		});
		
		
	});
	
	//提交按钮
	$("#submit-buttons").click(function(){
		
		time(this);
		
		auditContract() ;
	});
	
	function saveAttrubutionDate(){
		
		var url = serverAddr+"/purchase/updateAttributionDate.asyn";
		var orderId = $("#orderId").val() ;
		var attributionDate = $("#attributionDate").val() ;
		
		if(!attributionDate){
			$custom.alert.error("归属时间不能未空");
			return ; 
		}
		
		attributionDate += " 00:00:00" ;
		
		$custom.request.ajax(url, {"id": orderId, "attributionDate": attributionDate}, function(result){
			if(result.state == '200'){
				auditContract() ;
			}else{
				if(result.expMessage != null){
					$custom.alert.error(result.expMessage);
				}else{
					$custom.alert.error(result.message);
				}
			}
		});
	}
	
	function auditContract(){
		var url = "" ;
		var tempStatus = $('input:radio[name="status"]:checked').val() ;
		
		if('001' == status){
			url = serverAddr+"/purchase/submitJSONPurchaseContract.asyn";
		}else if('002' == status){
			
			
			if(!tempStatus){
				$custom.alert.error("请选择审核结果");
				
				return ;
			}
			
			url = serverAddr+"/purchase/auditJSONPurchaseContract.asyn";
		}
		
		var json = getFormJson($("#edit-form")) ;
        json = JSON.stringify(json);
		
		$custom.request.ajaxJson(url, json, function(result){
			if(result.state == '200'){
				
				if('001' == status
						|| (tempStatus && '001' == tempStatus)){
					
					$custom.alert.success("操作成功！");
					
					setTimeout(function () {
						$module.load.pageOrder("act=3001");
					}, 1000);
					
				}else if('002' == status){
					checkInnerMerchantSaleOrder(
							function(){
								checkTradeLink(function(){
									
									$custom.alert.success("操作成功！");
									
									if($m2019.params.tradeLinkId){
										var orderId = $("#orderId").val() ;
										setTimeout(function () {
											$load.a(pageUrl + "300181&id=" + orderId + "&tradeLinkId=" + $m2019.params.tradeLinkId);
										}, 1000);
									}else{
										setTimeout(function () {
											$module.load.pageOrder("act=3001");
										}, 1000);
									}
								}) ;
						
					}) ;
				}
				
			}else{
				if(result.expMessage != null){
					$custom.alert.error(result.expMessage);
				}else{
					$custom.alert.error(result.message);
				}
			}
		});
	}
	
	function getFormJson(form) {
        var o = {};
        var a = $(form).serializeArray();
        $.each(a, function () {
            if (o[this.name] !== undefined) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
            	
            	if(this.name.indexOf("goods.") > -1){
            		o[this.name] = new Array() ;
                    o[this.name].push(this.value || '');
            	}else{
	                o[this.name] = this.value || '';
            	}
            	
            }
        });
        return o;
    }
	
	/**
	*	检查是否要生成内部供应商对应销售订单
	*/
	function checkInnerMerchantSaleOrder(callback){
		
		var orderId = $("#orderId").val() ;
		
		var url = serverAddr+"/purchase/checkInnerMerchantSaleOrder.asyn" ;
		
		$custom.request.ajax(url, {"id": orderId}, function(result){
			if(result.state == '200'){
				var data = result.data ;
				
				if(data.flag){
					swal({
	    				title: data.merchantName + "公司下无对应的销售订单，是否生成销售订单？",
	    				type: 'warning',
	    				showCancelButton: true,
	    				confirmButtonColor: '#4fa7f3',
	    				cancelButtonColor: '#d57171',
	    			}).then(function(){ 
	    				//确定生成对应供应商销售订单
	    				saveInnerMerchantSaleOrder(data.merchantName, callback) ;
	    				
	    			}, function(dismiss){ 
	    			    if(dismiss == 'cancel'){ 
	    			    	callback() ;
	    			    } 
	    			}) ;
				}else{
					callback() ;
				}
				
			}else{
				if(result.expMessage != null){
					$custom.alert.error(result.expMessage);
				}else{
					$custom.alert.error(result.message);
				}
			}
		}) ;
	}
	
	/**保存内部公司销售订单*/
	function saveInnerMerchantSaleOrder(merchantName, callback){
		
		var orderId = $("#orderId").val() ;
		
		$m4005.init(orderId, merchantName, callback) ;
		
	}
	
	function init(){
		var orderId = $("#orderId").val() ;
		
		var url = serverAddr+"/purchase/getPurchaseOrderDetails.asyn" ;
		
		$custom.request.ajaxSync(url, {id: orderId}, function(result){
            if(result.state == '200'){
                
            	if(result.data){
            		
            		var data = result.data ;
            		
            		status = data.status ;
            		
            		if('001' == status){
            			$("#audit-buttons").remove() ;
            			$("#statusDiv").remove() ;
            		}else if('002' == status){
            			$("#submit-buttons").remove() ;
            		}else{
            			$("#audit-buttons").remove() ;
            			$("#submit-buttons").remove() ;
            		}
            	}
            	
            }else{
            	if(result.expMessage != null){
					$custom.alert.error(result.expMessage);
				}else{
					$custom.alert.error(result.message);
				}
            }
        });
	}
	
	//判断是否存在采购链路配置
	function checkTradeLink(callback){
		
		var url = serverAddr+"/purchase/checkTradeLink.asyn" ;
		var orderId = $("#orderId").val() ;
		
		$custom.request.ajax(url, {"id": orderId}, function(result){
			if(result.state == '200'){
				
				if(result.data.flag == true){
					$m2019.init(result.data.tradeLinkList, callback) ;
				}else{
					callback() ;
				}
				
			}else{
				if(result.expMessage != null){
					$custom.alert.error(result.expMessage);
				}else{
					$custom.alert.error(result.message);
				}
			}
		});
	}
	
</script>
