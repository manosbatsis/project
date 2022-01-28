<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common.jsp" />
<style type="text/css">
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
        border: 1px solid #cbcbcb;
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
                                    <input type="hidden" name="fileTempCode" value="mead">
                                    <input type="hidden" name="code" value="${detail.code }">
                                    <input type="hidden" name="detail.currency" value="${detail.currency }">
                                    	<table class="pure-table pure-table-bordered">
                                    		<tr>
                                    			<td colspan="4">
                                    			</td>
                                    			<td colspan="4" style="border-bottom: 1px solid #000000;">
                                    			   <b>採購單Purchase Order</b>
                                                </td>
                                    		</tr>
                                    		<tr>
                                                <td colspan="8">
                                                    Company Name:<b>${detail.buyerEnName }</b>
                                                    <input type="hidden" name="detail.buyerEnName" value="${detail.buyerEnName }">
                                                </td>
                                            </tr>
                                    		<tr>
                                    			<td colspan="6">
                                                    Address: <input type="text" style="width:500px;" name="detail.buyerAddress" value="${detail.buyerAddress }">
                                                </td>
                                                <td colspan="2">
                                                	日期 : <input type="text"  parsley-type="text" class="form_datetime date-input" id="date" name="detail.date" value="${detail.date }">
                                                </td>
                                    		</tr>
                                    		<tr style="border-bottom: 2px solid #000000;">
                                                <td colspan="6">
                                                    <input type="text" name="detail.title" value="${detail.title }">
                                                </td>
                                                <td colspan="2">
                                                    PO No:${detail.poNo }
                                                    <input type="hidden" name="detail.poNo" value="${detail.poNo }">
                                                </td>
                                            </tr>
                                    		<tr>
                                                <td colspan="6">
                                                    Supplier:${detail.sellerEnName }
                                                    <input type="hidden" name="detail.sellerEnName" value="${detail.sellerEnName }">
                                                </td>
                                                <td colspan="2">
                                                                                                                聯絡人: <input type="text"  parsley-type="text" name="detail.linkman" value="${detail.linkman }">
                                                </td>
                                            </tr>
                                    		<tr>
                                                <td colspan="6">
                                                                                                                交貨地址: <input type="text" style="width:500px;"  parsley-type="text" name="detail.deliveryAddress" value="${detail.deliveryAddress }">
                                                </td>
                                                <td colspan="2">
                                                                                                                電話: <input type="text"  parsley-type="text" name="detail.tel" value="${detail.tel }">
                                                </td>
                                            </tr>
                                            <tr style="border-bottom: 2px solid #000000;">
                                                <td colspan="6">
                                                </td>
                                                <td colspan="2">
                                                                                                               手機: <input type="text"  parsley-type="text" name="detail.moblie" value="${detail.moblie }">
                                                </td>
                                            </tr>
                                    		<tr>
                                                <td colspan="5">
                                                                                                                送貨日期:<input type="text"  parsley-type="text" class="edit_input form_datetime date-input" id="deliveryDate" name="detail.deliveryDate" value="${detail.deliveryDate }">
                                                </td>
                                                <td colspan="3">
                                                                                                                送貨方式: <input type="text"  parsley-type="text" name="detail.deliveryMethod" value="<c:if test="${ empty detail.deliveryMethod }">By Truck</c:if><c:if test="${ not empty detail.deliveryMethod }">${detail.deliveryMethod }</c:if>">
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="8">
                                                </td>
                                            </tr>
                                            <tr>
                                                <td >
                                                    SAP CODE
                                                </td>
                                                <td >
                                                                                                                                                            產品名稱
                                                </td>
                                                <td >
                                                                                                                                                            板數
                                                </td>
                                                <td >
                                                                                                                                                            每板數量/罐
                                                </td>
                                                <td >
                                                                                                                                                            數量/罐
                                                </td>
                                                <td >
                                                    List price/${detail.currency }
                                                </td>
                                                <td >
                                                    <c:choose>
                                                   		<c:when test="${ not empty detail.codDiscount}"> 
                                                   			Net price <label id="discount">${detail.codDiscount }</label>% COD/${detail.currency }
                                                   		</c:when>
                                                   		<c:otherwise>
                                                   			Net price <label id="discount">1</label>% COD/${detail.currency }
                                                   		</c:otherwise>
                                                   	</c:choose>
                                                </td>
                                                <td >
                                                    Total
                                                </td>
                                            </tr>
                                    		<c:forEach items="${goodsList }" var="item">
                                    			<tr>
	                                    			<td >
	                                    			    <input style="width: 120px;" type="text" name="goods.sapCode" value="${item.sapCode }" >
	                                    			</td>
	                                    			<td >
	                                    			    ${item.goodsName}
                                                        <input type="hidden" name="goods.goodsName" value="${item.goodsName }">
	                                    			</td>
	                                    			<td >
	                                    			    <input style="width: 70px;" readonly type="text" class="platesNum" name="goods.platesNum" value="${item.platesNum }">
	                                    			</td>
	                                    			<td >
	                                    			    <input style="width: 70px;" type="text" class="preNum" name="goods.preNum" value="${item.preNum }">
	                                    			</td>
	                                    			<td >
	                                    			    ${item.num}
	                                    			    <input type="hidden" class="num" name="goods.num" value="${item.num }">
	                                    			</td>
	                                    			<td >
                                                        <input style="width: 70px;" type="text" name="goods.listPrice" value="${item.listPrice }">
                                                    </td>
                                                    <td >
                                                        ${item.price}
                                                        <input type="hidden" name="goods.price" value="${item.price }">
                                                    </td>
                                                    <td >
                                                        ${item.amount}
                                                        <input type="hidden" name="goods.amount" value="${item.amount }">
                                                    </td>
                                                    <input type="hidden" name="goods.barcode" value="${item.barcode }">
	                                    		</tr>
                                    		</c:forEach>
                                    		    <tr>
                                                    <td >
                                                        Total：
                                                    </td>
                                                    <td ></td>
                                                    <td >
                                                        <input style="width: 70px;" type="text" id="totalPlatesNum" name="detail.totalPlatesNum" value="${detail.totalPlatesNum }">
                                                    </td>
                                                    <td ></td>
                                                    <td >
                                                        ${detail.totalNum}
                                                        <input type="hidden" name="detail.totalNum" value="${detail.totalNum }">
                                                    </td>
                                                    <td ></td>
                                                    <td >Grand Total：</td>
                                                    <td >
                                                        <input type="text" name="detail.totalAccount" value="${detail.totalAccount }">
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td colspan="8"></td>
                                                </tr>
                                    		    <tr>
                                                    <td>備註: </td>
                                                    <td colspan="7">
                                                        <textarea rows="7" cols="5" name="detail.remark" style="width:500px;">${detail.remark }</textarea>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>公司蓋章: </td>
                                                    <td colspan="5"></td>
                                                    <td>COD Discount:</td>
                                                    <td>
                                                    	<c:choose>
                                                    		<c:when test="${ not empty detail.codDiscount}"> 
                                                    			<input style="width: 40px;" type="text" id="codDiscount" name="detail.codDiscount" value="${detail.codDiscount }">%
                                                    		</c:when>
                                                    		<c:otherwise>
                                                    			<input style="width: 40px;" type="text" id="codDiscount" name="detail.codDiscount" value="1">%
                                                    		</c:otherwise>
                                                    	</c:choose>
                                                    </td>
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

	// 采购订单状态
	var status = "" ;
	init() ;

	$derpTimer.init("#date", "yyyy/MM/dd") ;
	$derpTimer.init("#deliveryDate", "yyyyMMdd") ;
	
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
	
	$("#codDiscount").on('change', function(){
		var val = $(this).val() ;
		
		$("#discount").html(val) ;
	});
	
	$(".preNum").on('blur', function(){
		var preNum = $(this).val() ;
		
		if(!isRealNum(preNum)){
			$custom.alert.error("每板數量/罐 请输入数字");
			
			return ;
		} 
		
		var numOrg = $(this).parent().parent().find(".num").eq(0) ;
		var num = $(numOrg).val() ;
		
		num = num.replace(/,/g,"") ;
		
		num = parseFloat(num) ;
		preNum = parseFloat(preNum) ;
		
		var platesNum = num / preNum ;
		platesNum = platesNum.toFixed(2);
		
		var platesNumOrg = $(this).parent().parent().find(".platesNum").eq(0) ;
		$(platesNumOrg).val(platesNum) ;
		
		var totalPlatesNum = 0.00 ;
		
		$(".platesNum").each(function(i, m){
			var platesNum = $(m).val() ;
			
			if(!isRealNum(platesNum)){
				return true ;
			}
			
			platesNum = parseFloat(platesNum) ;
			
			totalPlatesNum += platesNum ;
		}); 
		
		totalPlatesNum = totalPlatesNum.toFixed(2) ;
		
		$("#totalPlatesNum").val(totalPlatesNum) ;
	});
	
	//返回
	$("#cancel-buttons").click(function(){
		$module.load.pageOrder("act=3001");
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
	*判断是否是数字
	*
	**/

	function isRealNum(val){
		
		if(!val){
			return false ;
		}
		
		if(!isNaN(val)){
			return true ;
		}else{
			return false ;
		}
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
