<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>

<jsp:include page="/WEB-INF/views/common.jsp" />

<!-- Start content-->
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
										<li class="breadcrumb-item"><a href="javascript:dh_list();">采购订单列表</a></li>
										<li class="breadcrumb-item"><a href="#">新增融资订单</a></li>
									</ol>
								</div>
							</div>
							<!--  title   end  -->
							<!--  title   基本信息   start -->
							<div class="title_text">基本信息</div>
							<form id="infoForm">
								<div class="edit_box mt20">
									<div class="edit_item">
										<input type="hidden" value="${detail.purchaseOrders }"
											name="purchaseOrders" id="purchaseOrders" />
										<input type="hidden" value="${detail.code }"
											name="code" id="code" />
										<label class="edit_label"><i class="red">*</i> 供应商：</label>
										<select
											class="edit_input zpxApiInfo" name="supplierCode"
											id="supplierCode" required reqMsg="供应商不能为空">
											<option value="">请选择</option>
										</select>
										<input type="hidden" value=""
											name="supplierName" id="supplierName" />
									</div>
									<div class="edit_item">
										<label class="edit_label"><i class="red">*</i> 入库仓库：</label> 
										<select
											class="edit_input zpxApiInfo" name="depotCode" id="depotCode"
											required reqMsg="入库仓库不能为空">
											<option value="">请选择</option>
										</select> 
										<input type="hidden" value=""
											name="depotName" id="depotName" />
									</div>
									<div class="edit_item">
										<label class="edit_label"><i class="red">*</i> 采购币种：</label> <select
											class="edit_input" name="billCurrencCode"
											id="billCurrencCode" required reqMsg="采购币种不能为空">
											<option value="">请选择</option>
											<option value="HKD">港币</option>
											<option value="JPY">日元</option>
											<option value="USD">美元</option>
											<option value="AUD">澳元</option>
											<option value="EUR">欧元</option>
											<option value="CNY">人民币</option>
										</select>
									</div>
								</div>
								<div class="edit_box">
									<div class="edit_item">
										<label class="edit_label"> 业务类型：</label> <input type="text"
											class="edit_input" value="跨境现金代采" disabled="disabled" /> <input
											type="hidden" name="businessModel" value="DC001" />
									</div>
									<div class="edit_item">
										<label class="edit_label"> 资金方：</label> <input type="text"
											class="edit_input" value="卓普信" disabled="disabled" /> <input
											type="hidden" name="investorType" value="1" />
									</div>
									<div class="edit_item">
										<label class="edit_label"><i class="red">*</i> 计息币种：</label>
										<input type="text" class="edit_input" id="interestCurrencyLabel" value="" disabled="disabled" />
										 <input type="hidden" name="interestCurrency" id="interestCurrency"  />
									</div>
								</div>
								<div class="edit_box">
									<div class="edit_item">
										<label class="edit_label"><i class="red">*</i> 融资天数：</label> <input
											type="text" required reqMsg="融资天数不能为空"
											value="" class="edit_input"
											id="borrowingDays" name="borrowingDays" onkeyup="value=value.replace(/[^\\\d^]/g,'')">
									</div>
									<div class="edit_item">
										<label class="edit_label"><i class="red">*</i> 采购合同号：</label>
										<input
											type="text" class="edit_input" name="poNo" id="poNo"
											parsley-trigger="change" required reqMsg="采购合同号不能为空"
											value="${detail.poNo}">
									</div>
									<div class="edit_item">
										<label class="edit_label"><i class="red">*</i> 付款条款：</label> <select
											name="paymentTermId" class="edit_input zpxApiInfo"
											id="paymentTermId" required reqMsg="付款条款不能为空">
											<option value="">请选择</option>
										</select> <input type="hidden" value=""
											name="paymentTermName" id="paymentTermName" />
									</div>
								</div>
								<div class="edit_box">
									<div class="edit_item">
										<label class="edit_label"><i class="red">*</i> 提货地址：</label> 
										<input
											type="text" class="edit_input" name="warehouseAddress"
											id="warehouseAddress" required reqMsg="提货地址不能为空" value="">
									</div>
									<div class="edit_item">
										<label class="edit_label"> 贸易方式：</label> <select
											name="tradeMode" class="edit_input zpxApiInfo" id="tradeMode">
											<option value="">请选择</option>
										</select>
									</div>
									<div class="edit_item">
										<label class="edit_label">预计交货日期：</label>
										<input type="text"
											class="edit_input" name="expDeliveryDate"
											id="expDeliveryDate" style="font-size: 13px;"
											value="<fmt:formatDate value="${detail.expDeliveryDate }" pattern="yyyy-MM-dd"/>">
									</div>
								</div>
								<div class="edit_box">
									<div class="edit_item">
										<label class="edit_label"> 备注：</label> 
										<textarea type="text"
											class="edit_input" name="loanApplyRemark"
											id="loanApplyRemark">${detail.loanApplyRemark }</textarea>
									</div>
									<div class="edit_item">
									</div>
									<div class="edit_item"></div>
								</div>

								<div class="title_text">融资成本</div>
								<div class="edit_box mt20">
									<div class="edit_item">
										<label class="edit_label">采购总金额：</label> 
										<input type="text" class="edit_input" id="purchaseAmount" value="${detail.purchaseAmount }" disabled="disabled">
										<input type="hidden" id="purchaseAmountHidden" name="purchaseAmount" value="${detail.purchaseAmount }" >
									</div>
									<div class="edit_item">
										<label class="edit_label">采购折扣金额：</label>
										<input type="text" class="edit_input" id="purchaseDiscountAmount"
											name="purchaseDiscountAmount" value="${detail.purchaseDiscountAmount }"
											onkeyup="value=value.replace(/[^\\\d^\\\.]/g,''); amountVal(this, '6')">
									</div>
									<div class="edit_item">
										<label class="edit_label"><i class="red">*</i> 保证金比例：</label>
										<input type="text" class="edit_input" id="marginLevelRatio"
											name="marginLevelRatio" 
											value="<c:if test="${empty detail.marginLevelRatio}">0.2</c:if><c:if test="${not empty detail.marginLevelRatio}">${detail.marginLevelRatio }</c:if>" 
											required reqMsg="保证金比例不能为空"
											onkeyup="value=value.replace(/[^\\\d^\\\.]/g,'')">
									</div>
								</div>
								<div class="edit_box">
									<div class="edit_item">
										<label class="edit_label">应付保证金：</label> 
										<input type="text" class="edit_input" id="marginPayableAmount"
											value="${detail.marginPayableAmount }" disabled="disabled">
										<input type="hidden" class="edit_input" id="marginPayableAmountHidden"
											name="marginPayableAmount" value="${detail.marginPayableAmount }" >
									</div>
									<div class="edit_item">
									</div>
									<div class="edit_item"></div>
								</div>
							</form>
						</div>
						<!--  title   基本信息  start -->
						<!--   商品信息  start -->
						<div class="col-12 ml10">
	                        <div class="row">
		                         <div class="col-9">
		                             <div class="title_text">融资商品</div>
		                        </div>
		                        <div class="col-1 mt10">
		                          	<button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick="$popupGoods.orderGoods.init()">选择商品</button>
		                        </div>
		                         <div class="col-1 mt10">
		                          	<button type="button" class="btn btn-find waves-effect waves-light btn_default" id="delete-list-button">删 除</button>
		                        </div> 
	                        </div>
	                    </div>
						<div class="row col-12 mt20 table-responsive">
							<table id="item-datatable" class="table table-striped"
								cellspacing="0" width="100%">
								<thead>
									<tr>
										<th><input id="checkbox11" type="checkbox" name="all"></th>
										<th>&nbsp;&nbsp;&nbsp;商品货号&nbsp;&nbsp;&nbsp;</th>
										<th>商品名称</th>
										<th>条形码</th>
										<th>商品原产地</th>
										<th>商品保质天数</th>
										<th><i class="red">*</i>采购数量</th>
										<th>采购单价</th>
										<th><i class="red">*</i>采购金额</th>
										<th>规格型号</th>
									</tr>
								</thead>
								<tbody id="itemTable">
									<c:set var="amount" value="0"></c:set>
									<c:set var="num" value="0"></c:set>
									<c:forEach items="${detail.itemList}" var="item">
										<tr>
				        					<td><input type='checkbox' name='item-checkbox'><input type='hidden' name='goodsId' value='${item.goodsId}'></td>
				        					<td>${item.goodsNo}</td>
				        					<td>${item.goodsName}</td>
				        					<td>${item.barcode}</td>
				        					<td>${item.originCountry} </td>
				        					<td>${item.qualityGuaranteeDates}</td>
				        					<c:set var="num" value="${num + item.purchaseQuantity }"></c:set>
				        					<td><input type='text' class='purchaseQuantity' value='${ item.purchaseQuantity}' onblur='calcPurchasePrice(this)' required reqMsg='采购数量不能为空' onkeyup='value=value.replace(/[^\\\d^]/g, "")'></td>
				        					<td class='purchasePrice'><fmt:formatNumber value="${item.purchasePrice }" pattern="#.##" minFractionDigits="8" > </fmt:formatNumber></td>
				        					<c:set var="amount" value="${amount + item.purchaseAmount }"></c:set>
				        					<td><input type='text' class='purchaseAmount' value='${ item.purchaseAmount}' onblur='calcPurchasePrice(this)' required reqMsg='采购金额不能为空' onkeyup='value=value.replace(/[^\\\d^\\\.]/g, "") ; amountVal(this, "6")'></td>
				        					<td>${item.spec}</td>
        								</tr>
									</c:forEach>
									<tr class='total'>
    									<td style='text-align: center;' colspan='6'>合计：</td>
    									<td class='totalNum'>${num}</td>
    									<td></td>
    									<td class='totalAmount'>${amount}</td>
    									<td></td>
    								</tr>
								</tbody>
							</table>
						</div>
						<div class='col-12 ml10'>
                            <div class="title_text">附件列表</div>
                            <div class="info-box">
                                <button type="button" style="margin-left: 20px; margin-top: 10px;"
                                        class="btn btn-find waves-effect waves-light btn-sm btn_default"
                                        id="btn-file">上传附件
                                </button>
                                <form enctype="multipart/form-data" id="form-upload">
                                    <input type="file" name="file" id="file"
                                           class="btn-hidden file-import">
                                </form>
                                <div id="attachmentTable" ></div>
                            </div>
                        </div>
						<!--   商品信息  end -->
						<div class="form-row m-t-50">
							<div class="col-12">
								<div class="row">
									<div class="col-4"></div>
									<div class="col-4">
										<button type="button"
											class="btn btn-light waves-effect waves-light btn_default"
											id="cancel-buttons">取 消</button>
										<shiro:hasPermission name="financing_audit">
											<input type="button"
												class="btn btn-info waves-effect waves-light btn_default delayedBtn"
												id="audit-buttons" value="提交" />
										</shiro:hasPermission>
									</div>
									<div class="col-4"></div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- end row -->
		<!-- 弹窗开始 -->
		<jsp:include page="/WEB-INF/views/modals/8000111-V2.jsp" />
		<!-- 弹窗结束 -->
		<!-- end row -->
		<div class="popupbg" style="display: none">
		    <div class="popup_box">
		        <img src="/resources/assets/images/uploading.gif" alt="">
		        <p>正在提交中...</p>
		    </div>
		</div>
	</div>
	<!-- container -->
</div>

</div>
<!-- content -->
<script type="text/javascript">

	var code = '${detail.code}' ;
	$("#attachmentTable").createAttachTables(code);
	
	var purchaseAmout = ${detail.purchaseAmount} ;
	
	$(function(){
	 
		$derpTimer.init("#expDeliveryDate", "yyyy-MM-dd") ;
		
		initZpxSelect() ;
	
	});
    
    //取消按钮
    $("#cancel-buttons").click(function(){
          $module.load.pageOrder('act=3001');
    });
    
    //保存按钮
    $("#audit-buttons").click(function(){
    	
    	$custom.alert.warning("确定提交该融资订单？",function(){
    	
    		$(".popupbg").show();
    		
			var url = serverAddr + "/purchase/submitSapience.asyn" ;
			
			if(!$checker.verificationRequired()){

				$(".popupbg").hide();

	    		return ;
			}
	    	
	    	var form = $("#infoForm").serializeArray();
	    	
	    	$(form).each(function(i ,m){
	    		if(m.name == 'expDeliveryDate'
	    				&& m.value){
	    			m.value += " 00:00:00";
	    		}
	    	}) ;
	    	
	    	var trs = $("#itemTable").find("tr") ;
	    	
	    	var items = [] ;
	    	
	    	$(trs).each(function(index, tr){
	    		
	    		if(index == trs.length - 1){
	    			return false ;
	    		}
	    		
	    		var goodsId = $(tr).find("input[name='goodsId']").val() ;
	    		var purchaseQuantity = $(tr).find(".purchaseQuantity").val() ;
	    		var purchaseAmount = $(tr).find(".purchaseAmount").val() ;
	    		var purchasePrice = $(tr).find(".purchasePrice").text() ;
	    		
	    		var json = {"goodsId": goodsId, "purchaseQuantity": purchaseQuantity,
	    				"purchaseAmount":purchaseAmount, "purchasePrice":purchasePrice} ;
	    		
	    		items.push(json) ;
	    		
	    	}) ;
	    	
	    	items = JSON.stringify(items);
	        
	        form.push({name:"items", value:items }) ;
	        
	        $custom.request.ajax(url, form, function(result){
	        	
	        	$(".popupbg").hide();
	        	
	            if(result.state == '200'){
	                $custom.alert.success("提交成功！附件生成中，请稍后查看【附件列表】");
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
    	}) ;
    }) ;
    
    //加载卓普信信息下拉框
    function initZpxSelect(){
    	
    	var url = serverAddr+"/purchase/getSapienceApiInfo.asyn"; ;
    	
    	var interestCurrency = '${detail.interestCurrency}' ;
    	
    	$custom.request.ajax(url, {}, function(data){
			if(data.state==200 && data.data){
				var json = JSON.parse(data.data) ;
				
				var result = json.result ;

				if(!result){
					$custom.alert.error("获取卓普信接口信息失败");
					
					return ;
				}
				
				//付款条款
				var paymentTermId = '${detail.paymentTermId}' ;
				var paymentTermHtml = "<option value=''>请选择</option>" ;
				$(result.paymentTermList).each(function(index, item){
					
					var selected = "" ;
					
					if(paymentTermId == item.id){
						selected = "selected" ;
					}
					
					paymentTermHtml += "<option value='"+ item.id +"' "+ selected +">" + item.name + "</option>" ;
				}) ;
				$("#paymentTermId").html(paymentTermHtml) ;
				
				//供应商
				var supplierCode = '${detail.supplierCode}' ;
				var supplierHtml = "<option value=''>请选择</option>" ;
				$(result.supplierList).each(function(index, item){
					
					var selected = "" ;
					
					if(supplierCode == item.code){
						selected = "selected" ;
					}
					
					supplierHtml += "<option value='"+ item.code +"' "+ selected +">" + item.name + "</option>" ;
				}) ;
				$("#supplierCode").html(supplierHtml) ;

				//贸易方式
				var tradeMode = '${detail.tradeMode}' ;
				var tradeModeHtml = "<option value=''>请选择</option>" ;
				$(result.tradeMode).each(function(index, item){
					
					var selected = "" ;
					
					if(tradeMode == item.name){
						selected = "selected" ;
					}
					
					tradeModeHtml += "<option value='"+ item.name +"' "+ selected +">" + item.name + "</option>" ;
				}) ;
				$("#tradeMode").html(tradeModeHtml) ;
				
				//入库仓库
				var depotCode = '${detail.depotCode}' ;
				var selectObj=$("#depotCode");
	            selectObj.html("<option value=''>请选择</option>") ;
	            
	            var width = "70%" ;
            	$(selectObj).removeClass("edit_input") ;
	            
	            $(selectObj).wrap('<div style="width:'+ width +'; display: inline-block;"></div>') ;
	            
	            $(selectObj).addClass("selectpicker").addClass("show-tick") ;
	            $(selectObj).attr("data-live-search", "true") ;
	            
	            $(result.warehouseList).each(function(o,i){
	            	
	            	var selected = "" ;
	            	
	            	if(depotCode == i.code){
	            		selected = "selected" ;
	            	}
	            	
	                selectObj.append("<option value='"+ i.code+"' "+ selected +">"+i.name+"</option>");
	            });
	            
	            $(selectObj).selectpicker({width: width});
				$(selectObj).selectpicker('refresh');
				
				$(".selectpicker").prev().prev().css({"height": "30px","margin-bottom": "5px","border": "1px solid #dadada","background": "white"}) ;
			    $(".selectpicker").prev().css({"z-index":"99"});
			    $(".selectpicker").prev().find(".dropdown-menu.inner").css({"height":"220px"}) ;
			  	//入库仓库
				
			}else{
				$custom.alert.error("获取卓普信接口信息失败");
			}
		});
    }
    
  	//点击上传文件
    $("#btn-file").click(function () {
        var input = '<input type="file" name="file" id="file" class="btn-hidden file-import">';
        $("#form-upload").empty();
        $("#form-upload").append(input);
        $("#file").click();
    });
    
    //上传文件到后端
    $("#form-upload").on("change", '.file-import', function () {
        var formData = new FormData($("#form-upload")[0]);

        $custom.request.ajaxUpload(serverAddr + "/attachment/uploadFiles.asyn?code=" + code, formData, function (result) {
            if (result.state == 200 && result.data && result.data.code == 200) {
                
                $attachTable.fn.reload();
                $custom.alert.success("上传成功");
            } else {
                $custom.alert.error("上传失败");
            }
        });
    });
    
    $("#delete-list-button").click(function() {
 		var goodsId = [];
        $("input[name='item-checkbox']:checked").each(function() { // 遍历选中的checkbox
            goodsId.push($(this).next().val());
            $(this).parents("tr").remove();
        });
        var unNeedIds = $("#unNeedIds").val();
        var temp = unNeedIds.split(",");
        var newUnNeedIds = delArrElement(goodsId,temp);
        $("#unNeedIds").val(newUnNeedIds);
        $("input[name='all']").prop("checked",false);
        
        calcPurchaseAmount() ;
        
    });
	
 	function delArrElement(goodsId,temp){
		for (var i = 0; i < goodsId.length; i++) {
        	for (var j = 0; j < temp.length; j++) {
				if(goodsId[i] == temp[j]){
					temp.splice(j,1);
					delArrElement(goodsId,temp);
				}
			}
		}
		return temp;
	}
    
    $("input[name='all']").click(function(){
		//判断当前点击的复选框处于什么状态$(this).is(":checked") 返回的是布尔类型
		if($(this).is(":checked")){
			$("input[name='item-checkbox']").prop("checked",true);
		}else{
			$("input[name='item-checkbox']").prop("checked",false);
		}
	});
    
    $("#purchaseDiscountAmount").blur(function(){
    	calcMarginPayableAmount() ;
    }) ;
    
    $(".zpxApiInfo").change(function(){

    	var val = $(this).val() ;
    	
    	if(val){
    		var text = $(this).find("option:selected").text() ;
    		
    		if($(this).attr("id") == 'depotCode'){
    			$(this).parent().parent().next().val(text) ;
    		}else{
	    		$(this).next().val(text) ;
    		}
    	}
    }) ;
    
    /**计算应付保证金*/
    function calcMarginPayableAmount(){
    	
    	$("#purchaseAmount").val(purchaseAmout) ;
    	$("#purchaseAmountHidden").val(purchaseAmout) ;
		
		var purchaseDiscountAmount = 0 ;
		
		if($("#purchaseDiscountAmount").val()){
			purchaseDiscountAmount = $("#purchaseDiscountAmount").val() ;
		}
		
		var marginLevelRatio = 0 ;
		
		if($("#marginLevelRatio").val()){
			marginLevelRatio = $("#marginLevelRatio").val() ;
		}
		
		var marginPayableAmount = (parseFloat(purchaseAmout) - parseFloat(purchaseDiscountAmount)) * parseFloat(marginLevelRatio) ;
		
		marginPayableAmount = parseFloat(marginPayableAmount).toFixed(6) ;
		// 计算应付保证金
		$("#marginPayableAmount").val(marginPayableAmount) ;
		$("#marginPayableAmountHidden").val(marginPayableAmount) ;
    }
    
    /**计算采购单价*/
    function calcPurchasePrice(obj){
    	var tr = $(obj).parent().parent() ;
    	
    	var purchaseTempAmount = $(tr).find(".purchaseAmount").val() ;
    	var purchaseTempQuantity = $(tr).find(".purchaseQuantity").val() ;
    	
    	if(!purchaseTempAmount
    			|| !purchaseTempQuantity){
    		return ;
    	}
    	
    	var purchasePrice = parseFloat(purchaseTempAmount) / parseFloat(purchaseTempQuantity) ;
    	purchasePrice = parseFloat(purchasePrice).toFixed(8) ;
    	
    	$(tr).find(".purchasePrice").html(purchasePrice) ;
    	
    	calcPurchaseAmount() ;
    }
    
    /**计算采购总价*/
    function calcPurchaseAmount(){
    	var tds = $("#itemTable").find(".purchaseAmount") ;
    	
    	purchaseAmout = 0.0 ;
    	purchaseNum = 0 ;
    	
    	$(tds).each(function(index, item){
    		
    		var tr = $(item).parent().parent() ;
    		
    		var amount = $(item).val()  ;
    		
    		if(amount){
    			purchaseAmout += parseFloat(amount) ;
    		}
    		
    		var purchaseQuantity = $(tr).find(".purchaseQuantity").val() ;
    		
    		if(purchaseQuantity){
    			purchaseNum += parseInt(purchaseQuantity) ;
    		}
    		
    	}) ;
    	
    	$(".totalNum").html(purchaseNum) ;
    	$(".totalAmount").html(purchaseAmout) ;

    	calcMarginPayableAmount() ;
    }
    
    /**限制输入框不能超过5位小数*/
  	function amountVal(org, length){
  	    var regStrs = [
  	                       ['[^\\d\\.]+$', ''], //禁止录入任何非数字和点
  	                       ['\\.(\\d?)\\.+', '.$1'], //禁止录入两个以上的点
  	                       ['^(\\d+\\.\\d{' + length + '}).+', '$1'] //禁止录入小数点后两位以上
  	                   ];
  	   for(i=0; i<regStrs.length; i++){
  	       var reg = new RegExp(regStrs[i][0]);
  	     org.value = org.value.replace(reg, regStrs[i][1]);
  	   }
  	}
    
    $("#billCurrencCode").change(function(){
    	var billCurrencCode = $(this).val() ;
    	
    	$("#interestCurrency").val(billCurrencCode) ;
    	var interestCurrencyLabel = $("#billCurrencCode").find("option:selected").text();
    	
    	$("#interestCurrencyLabel").val(interestCurrencyLabel) ;
    	
    }) ;
	 
</script>
