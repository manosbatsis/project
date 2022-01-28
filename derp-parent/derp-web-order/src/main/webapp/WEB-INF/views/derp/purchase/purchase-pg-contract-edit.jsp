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
	    text-align: left;
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
                                    <input type="hidden" name="fileTempCode" value="PG">
                                    <input type="hidden" name="code" value="${detail.code }">
                                    	<table class="pure-table pure-table-bordered" >
                                    		<tr>
                                    			<td>
	                                    			Sales Organisation
                                    			</td>
                                    			<td>
                                                    HK01
                                                </td>
                                                <td>
                                                    Sold to code
                                                </td>
                                                <td>
                                                    ${detail.soldToCode }
                                                    <input type="hidden"  name="detail.soldToCode" value="${detail.soldToCode }">
                                                </td>
                                                <td>
                                                    Internal order #
                                                </td>
                                                <td>
                                                </td>
                                    		</tr>
                                    		<tr>
                                                <td>
                                                    Distribution Channel
                                                </td>
                                                <td>
                                                    ${detail.distributionChannel }
                                                    <input type="hidden"  name="detail.distributionChannel" value="${detail.distributionChannel }">
                                                </td>
                                                <td>
                                                    Ship to code
                                                </td>
                                                <td>
                                                    ${detail.shipToCode }
                                                    <input type="hidden"  name="detail.shipToCode" value="${detail.shipToCode }">
                                                </td>
                                                <td>
                                                    GL account #
                                                </td>
                                                <td>
                                                </td>
                                            </tr>
                                    		<tr>
                                    			<td>
                                                    Division
                                                </td>
                                                <td>
                                                    01
                                                    <input type="hidden"  name="detail.division" value="01">
                                                </td>
                                                <td>
                                                    Requesting department
                                                </td>
                                                <td>
                                                </td>
                                                <td>
                                                    Cost center #
                                                </td>
                                                <td>
                                                </td>
                                    		</tr>
                                    		<tr>
                                                <td>
                                                    Order Type
                                                </td>
                                                <td>
                                                    Z001
                                                </td>
                                                <td>
                                                    Shipping Condition
                                                </td>
                                                <td>
                                                </td>
                                                <td>
                                                    Delivery Plant
                                                </td>
                                                <td>
                                                    ${detail.deliveryPlant}
                                                    <input type="hidden"  name="detail.deliveryPlant" value="${detail.deliveryPlant }">
                                                </td>
                                            </tr>
                                    		<tr>
                                                <td>
                                                    Order date
                                                </td>
                                                <td>
                                                    <input type="text"  parsley-type="text" class="edit_input form_datetime date-input" name="detail.orderDate" value="${detail.orderDate }">
                                                </td>
                                                <td>
                                                    Order Reason
                                                </td>
                                                <td>
                                                </td>
                                                <td>
                                                    PO Number
                                                </td>
                                                <td>
                                                    ${detail.poNo}
                                                    <input type="hidden"  name="detail.poNo" value="${detail.poNo }">
                                                </td>
                                            </tr>
                                    		<tr>
                                                <td>
                                                    Request delivery date
                                                </td>
                                                <td>
                                                    <input type="text"  parsley-type="text" class="edit_input form_datetime date-input" name="detail.requestDeliveryDate" value="${detail.requestDeliveryDate }">
                                                </td>
                                                <td>
                                                    SLOG bracket
                                                </td>
                                                <td>
                                                </td>
                                                <td>
                                                    Sold-to Name
                                                </td>
                                                <td>
                                                </td>
                                            </tr>
                                    		<tr>
                                                <td>
                                                    Remark
                                                </td>
                                                <td colspan="2">
                                                </td>
                                                <td colspan="2">
                                                    MAXIMUM 30 LETTERS
                                                </td>
                                                <td>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="6">
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="6">
                                                    Order list
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    Description
                                                </td>
                                                <td>
                                                </td>
                                                <td>
                                                    PG code
                                                </td>
                                                <td>
                                                    Barcode
                                                </td>
                                                <td>
                                                    Order Qty
                                                </td>
                                                <td>
                                                    UOM
                                                </td>
                                            </tr>
                                    		<c:forEach items="${goodsList }" var="item">
                                    			<tr>
	                                    			<td>
	                                    			    <input type="text" name="goods.description" value="${item.description }" style="width:200px;">
	                                    			</td>
	                                    			<td>
	                                    			</td>
	                                    			<td>
	                                    			    ${item.pgCode}
	                                    			    <input type="hidden" name="goods.pgCode" value="${item.pgCode }">
	                                    			</td>
	                                    			<td>
	                                    			</td>
	                                    			<td>
	                                    			    ${item.orderQty}
	                                    			    <input type="hidden" name="goods.orderQty" value="${item.orderQty }">
	                                    			</td>
	                                    			<td>
                                                        CS
                                                    </td>
	                                    		</tr>
                                    		</c:forEach>
                                    		
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

	// 采购订单状态
	var status = "" ;
	init() ;

	$derpTimer.init(".date-input", "dd/MM/yyyy") ;
	
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
