<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common.jsp" />
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
                                    <form id="edit-form">
                                        <p class="purchase_contract_title">采购订单（C）</p>
                                        <p class="purchase_contract_title">Purchase Contract</p>
                                        <div class="purchase_contract_head_left">
                                            <span>采购合同编号：</span>
                                            <input type="text" name="purchaseContractNo" value="${detail.purchaseContractNo }">
                                            <input type="hidden" name="id" value="${detail.id }">
                                            <input type="hidden" id="orderId" name="orderId" value="${detail.orderId }">
                                            <span>采购订单号：</span>
                                            <input type="text" name="purchaseOrderNo" value="${detail.purchaseOrderNo }">
                                        </div>
                                        <div class="purchase_contract_head_left">
                                            <span>Purchase contract No.：</span>
                                            <input type="text" value="${detail.purchaseContractNo }">
                                            <span>Purchase order No.：</span>
                                            <input type="text" value="${detail.purchaseOrderNo }">
                                        </div>
                                        <div class="purchase_contract_head_left">
                                            <span>甲方：</span>
                                            <input type="text" name="buyerCnName" value="${detail.buyerCnName }">
                                            <span>乙方：</span>
                                            <input type="text" name="sellerCnName" value="${detail.sellerCnName }">
                                        </div>
                                        <div class="purchase_contract_head_left">
                                            <span>Buyer：</span>
                                            <input type="text" name="buyerEnName" value="${detail.buyerEnName }">
                                            <span>Seller：</span>
                                            <input type="text" name="sellerEnName" value="${detail.sellerEnName }">
                                        </div>
                                        <p class="purchase_contract_sub_title purchase_contract_mt15">1、货品明细 Goods details:</p>
                                        <div class="batch_import">
                                            <table>
                                                <thead>
                                                <tr>
                                                    <td>品牌<br>Brand</td>
                                                    <td>品名<br>Goods Description</td>
                                                    <td>条形码<br>Bar Code</td>
                                                    <td>规格<br>Specification</td>
                                                    <td>数量<br>Quantity</td>
                                                    <td>单价(${detail.currency })<br>Unit Value</td>
                                                    <td>总价(${detail.currency })<br>Total Value</td>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach items="${itemList }" var="item">
                                                	<tr>
	                                                    <td>${item.brandName }</td>
	                                                    <td>${item.goodsName }</td>
	                                                    <td>${item.barcode }</td>
	                                                    <td>${item.spec }</td>
	                                                    <td>${item.num }</td>
	                                                    <td><fmt:formatNumber value="${item.price }" pattern="#.##" minFractionDigits="8" > </fmt:formatNumber></td>
	                                                    <td>${item.amount }</td>
	                                                </tr>
                                                </c:forEach>
                                                <tr>
                                                	<td colspan="4">合计 Total</td>
                                                	<td>${totalMap.total }</td>
                                                	<td>/</td>
                                                	<td>${totalMap.totalAccount }</td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                        <div class="mt20">
                                        	<span class="purchase_contract_sub_title">2、</span>
                                        	<div class="purchase_contract_sub_content">
	                                            <span class="purchase_contract_inline" style="width: 18%;">运输方式：</span>
	                                            <input type="checkbox" class="meansCheck1" name="meansOfTransportation" value="CIF" onclick="linkageCheckBox(this)">CIF
	                                            <input type="checkbox" class="meansCheck1" name="meansOfTransportation" value="CFR" onclick="linkageCheckBox(this)">CFR
	                                            <input type="checkbox" class="meansCheck1" name="meansOfTransportation" value="FOB" onclick="linkageCheckBox(this)">FOB
	                                            <input type="checkbox" class="meansCheck1" name="meansOfTransportation" value="DPA" onclick="linkageCheckBox(this)">DPA
	                                            <input type="checkbox" class="meansCheck1" name="meansOfTransportation" value="EXW" onclick="linkageCheckBox(this)">EXW
	                                            <input type="checkbox" class="meansCheck2" name="meansOfTransportation" value="BY SEA" onclick="linkageCheckBox(this)">海运
	                                            <input type="checkbox" class="meansCheck2" name="meansOfTransportation" value="BY AIR" onclick="linkageCheckBox(this)">空运
	                                            <input type="checkbox" class="meansCheck2" name="meansOfTransportation" value="BY LAND" onclick="linkageCheckBox(this)">陆运
                                            </div>
                                        </div>
                                        <div class="mt10">
                                        	<div class="purchase_contract_sub_content">
	                                            <span class="purchase_contract_inline" style="width: 18%;">Means of Transportation：</span>
	                                            <input type="checkbox" class="meansCheck1" value="CIF" onclick="linkageCheckBox(this)">CIF
	                                            <input type="checkbox" class="meansCheck1" value="CFR" onclick="linkageCheckBox(this)">CFR
	                                            <input type="checkbox" class="meansCheck1" value="FOB" onclick="linkageCheckBox(this)">FOB
	                                            <input type="checkbox" class="meansCheck1" value="DPA" onclick="linkageCheckBox(this)">DPA
	                                            <input type="checkbox" class="meansCheck1" value="EXW" onclick="linkageCheckBox(this)">EXW
	                                            <input type="checkbox" class="meansCheck2" value="BY SEA" onclick="linkageCheckBox(this)">BY SEA
	                                            <input type="checkbox" class="meansCheck2" value="BY AIR" onclick="linkageCheckBox(this)">BY AIR
	                                            <input type="checkbox" class="meansCheck2" value="BY LAND" onclick="linkageCheckBox(this)">BY LAND
                                            </div>
                                        </div>
                                        <div>
                                        <div class="mt20">
                                        	<span class="purchase_contract_sub_title">3、</span>
                                        	<div class="purchase_contract_sub_content meansOfTransportation1">
	                                            <span class="purchase_contract_inline purchase_contract_w20 ">始发地（港）：</span>
	                                            <input class=" purchase_contract_w20 " type="text" name="loadingCnPort" value="${detail.loadingCnPort }">
	                                            <span class="purchase_contract_inline purchase_contract_w20 ">目的地（港）：</span>
	                                            <input class=" purchase_contract_w20 " type="text"name="destinationdCn" value="${detail.destinationdCn }">
                                            </div>
                                            <div class="purchase_contract_sub_content meansOfTransportation2" style="display:none ;">
	                                            <span class="purchase_contract_inline purchase_contract_w20 ">发货地址：</span>
	                                            <input style="width:60%;" type="text" name="deliveryAddress" value="${detail.deliveryAddress }">
                                            </div>
                                            <div class="purchase_contract_sub_content meansOfTransportation3" style="display:none ;">
	                                            <span class="purchase_contract_inline purchase_contract_w20 ">提货地址：</span>
	                                            <input style="width:60%;" type="text" name="pickingUpAddress" value="${detail.pickingUpAddress }">
                                            </div>
                                        </div>
                                        <div class="mt10 ">
                                        	<span class="purchase_contract_sub_title"></span>
                                        	<div class="purchase_contract_sub_content meansOfTransportation1">
	                                            <span class="purchase_contract_inline purchase_contract_w20 ">Loading Port：</span>
	                                            <input class=" purchase_contract_w20 " type="text" name="loadingEnPort" value="${detail.loadingEnPort }">
	                                            <span class="purchase_contract_inline purchase_contract_w20 ">Destination：</span>
	                                            <input class=" purchase_contract_w20 " type="text" name="destinationdEn" value="${detail.destinationdEn }">
                                            </div>
                                            <div class="purchase_contract_sub_content meansOfTransportation2" style="display:none ;">
	                                            <span class="purchase_contract_inline purchase_contract_w20 ">Delivery Address:</span>
	                                            <input style="width:60%;" class=" purchase_contract_w20 " type="text" name="deliveryAddressEn" value="${detail.deliveryAddressEn }">
                                            </div>
                                            <div class="purchase_contract_sub_content meansOfTransportation3" style="display:none ;">
	                                            <span class="purchase_contract_inline purchase_contract_w20 ">Picking up Address：</span>
	                                            <input style="width:60%;" type="text" name="pickingUpAddressEn" value="${detail.pickingUpAddressEn }">
                                            </div>
                                        </div>
                                        <div class="mt20">
                                        	<span class="purchase_contract_sub_title">4、</span>
                                        	<div class="purchase_contract_sub_content">
                                        		<span class="purchase_contract_w15 purchase_contract_inline">交货日期：</span>
                                        		<input style="width:4%;"  type="text" id="year"> 年
                                        		<input style="width:2%;"  type="text" id="month"> 月
                                        		<input style="width:2%;"  type="text" id="days"> 日 
                                        		(若此交货日期与甲方与最终买方签署的框架采购合同不一致的,以此交货期为准)
                                        	</div>
                                        </div>
                                        <div class="mt10">
                                        	<span class="purchase_contract_sub_title"></span>
                                        	<div class="purchase_contract_sub_content">
                                        		<span class="purchase_contract_w15 purchase_contract_inline">Delivery date：</span>
                                        		<input style="width:10%;" id="deliveryEngDate"  type="text" value=""> (If this delivery date is in conflict with the delivery date stipulated in the Purchase Frame Contract signed by the Buyer and the ultimate buyer, this one will prevail.)
                                        	</div>
                                        </div>
                                        <div class="mt20">
                                        	<span class="purchase_contract_sub_title" style="width: 10%;">付款方式 ：</span>
                                            <input type="radio" name="paymentMethod" onclick="paymentMethodTextShow(this)"  value="1" checked> 一次结清
                                            <input type="radio" name="paymentMethod" onclick="paymentMethodTextShow(this)" value="2"> 分批付款
                                            <input type="radio" name="paymentMethod" onclick="paymentMethodTextShow(this)" value="3"> 其他
                                        </div>
                                        <div class="mt20">
                                        	<span class="purchase_contract_sub_title">5、</span>
                                        	<div class="purchase_contract_sub_content paymentMethodText_1">
                                        		<span class="purchase_contract_w15 purchase_contract_inline">付款方式：</span>
                                        		双方确认本订单之日起 <input style="width:3%;"  type="text" value=""> 个工作日内甲方向乙方支付订单总金额100%货款
                                        	</div>
                                        	<div class="purchase_contract_sub_content paymentMethodText_2" style="display:none;">
                                        		<span class="purchase_contract_w15 purchase_contract_inline">付款方式：</span>
                                        		卖方确认本订单之日起 <input style="width:3%;"  type="text" value=""> 个工作日内，买方支付订单总金额的
                                        		<input style="width:3%;"  type="text" value=""> 货款：<input style="width:20%;"  type="text" value=""> ，货物到仓后买方验收合格且双方确认理货数据无误后在
                                        		<input style="width:3%;"  type="text" value=""> 个工作日内支付剩余 
                                        		<input style="width:3%;"  type="text" value=""> 货款:  <input style="width:20%;"  type="text" value=""> 。
                                        	</div>
                                        	<div class="purchase_contract_sub_content paymentMethodText_3"  style="display:none;">
                                        		<span class="purchase_contract_w15 purchase_contract_inline">付款方式：</span>
                                        		<textarea style="vertical-align:top;width:60%;heigth:100px;"></textarea>
                                        	</div>
                                        </div>
                                        <div class="mt10">
                                        	<span class="purchase_contract_sub_title"></span>
                                        	<div class="purchase_contract_sub_content paymentMethodText_1">
                                        		<span class="purchase_contract_w15 purchase_contract_inline">Terms of payment:</span>
                                        		TT 100% within <input style="width:3%;"  type="text" value=""> working days from the date of this order.
                                        	</div>
                                        	<div class="purchase_contract_sub_content paymentMethodText_2"  style="display:none;">
                                        		<span class="purchase_contract_w15 purchase_contract_inline">Terms of payment:</span>
                                        		TT <input style="width:3%;"  type="text" value=""> <input style="width: 20%;"  type="text" value=""> within 
                                        		<input style="width:3%;"  type="text" value=""> working days from the date of this order, TT
                                        		<input style="width:3%;"  type="text" value=""> <input style="width:20%;"  type="text" value=""> within
                                        		<input style="width:3%;"  type="text" value=""> working days subject to the satisfaction of the following conditions: (1) delivery of goods to the designated warehouse, (2) the quality of goods is satisfied by the buyer after inspection, and (3) both parties’ confirmation on the quantity of delivered goods.
                                        	</div>
                                        </div>
                                        <div class="mt20">
                                        	<span class="purchase_contract_sub_title">6、特别约定（本条与本订单约定的内容存在冲突的，以本条约定的为准）：</span>
                                        	<div class="purchase_contract_sub_content">
                                        		<span class="purchase_contract_w15 purchase_contract_inline">特别约定: </span>
                                        		【<textarea name="specialAgreement" style="vertical-align:top;width:60%;heigth:100px;">${detail.specialAgreement }</textarea>】
                                        	</div>
                                        </div>
                                        <div class="mt10">
                                        	<span class="purchase_contract_sub_title"></span>
                                        	<div class="purchase_contract_sub_content">
                                        		<span class="purchase_contract_w15 purchase_contract_inline">Special agreement: </span>
                                        		【<textarea name="specialAgreementEn" style="vertical-align:top;width:60%;heigth:100px;">${detail.specialAgreementEn }</textarea>】
                                        	</div>
                                        </div>
                                        <div class="mt20">
                                        	<span class="purchase_contract_sub_title">7、乙方（供货方）收款账户  Banking information of the seller：</span>
                                        	<div class="purchase_contract_content_left">
	                                            <span>账号：</span>
	                                            <input type="text" class="" value="${detail.accountNo }">
	                                        </div>
	                                        <div class="purchase_contract_content_left">
	                                            <span>采购币种：</span>
	                                            <input type="text" class="" value="${currencyLabel }">
	                                        </div>
	                                        <div class="purchase_contract_content_left">
	                                            <span>账户：</span>
	                                            <input type="text" class="" value="${detail.beneficiaryName }">
	                                        </div>
	                                        <div class="purchase_contract_content_left">
	                                            <span>开户行：</span>
	                                            <input type="text" class="" value="${detail.beneficiaryBankName }">
	                                        </div>
	                                        <div class="purchase_contract_content_left">
	                                            <span>地址：</span>
	                                            <input type="text" class=""  value="${detail.bankAddress }">
	                                        </div>
	                                        <div class="purchase_contract_content_left">
	                                            <span>Swift Code：</span>
	                                            <input type="text" class="" value="${detail.swiftCode }">
	                                        </div>
                                        </div>
                                        <div class="mt20">
                                        	<div class="purchase_contract_content_left">
	                                            <span>Account No：</span>
	                                            <input type="text" name="accountNo" value="${detail.accountNo }">
	                                        </div>
	                                        <div class="purchase_contract_content_left">
	                                            <span>Currency：</span>
	                                            <input type="text" name="currency" value="${detail.currency }">
	                                        </div>
	                                        <div class="purchase_contract_content_left">
	                                            <span>Beneficiary Name：</span>
	                                            <input type="text" name="beneficiaryName" value="${detail.beneficiaryName }">
	                                        </div>
	                                        <div class="purchase_contract_content_left">
	                                            <span>Beneficiary Bank Name：</span>
	                                            <input type="text" name="beneficiaryBankName" value="${detail.beneficiaryBankName }">
	                                        </div>
	                                        <div class="purchase_contract_content_left">
	                                            <span>Bank Address：</span>
	                                            <input type="text" name="bankAddress" value="${detail.bankAddress }">
	                                        </div>
	                                        <div class="purchase_contract_content_left">
	                                            <span>Swift Code：</span>
	                                            <input type="text" name="swiftCode"  value="${detail.swiftCode }">
	                                        </div>
                                        </div>
                                        <div class="mt60">
                                        	<div class="purchase_contract_head_left">
	                                            <span>甲方：</span>
	                                            <input type="text" name="buyerAuthorizedSignature" value="${detail.buyerAuthorizedSignature }">  (盖章)
	                                        </div>
	                                        <div class="purchase_contract_head_left">
	                                            <span>Buyer：</span>
	                                            <input type="text" name="buyerAuthorizedSignatureEn" value="${detail.buyerAuthorizedSignatureEn }">  (Common Seal)
	                                        </div>
	                                        <div class="purchase_contract_head_left">
	                                            <span>授权代表签字：</span>
	                                        </div>
	                                        <div class="purchase_contract_head_left">
	                                            <span>Authorized signature：</span>
	                                        </div>
                                        </div>
                                        <div class="mt60">
                                        	<div class="purchase_contract_head_left">
	                                            <span>乙方：</span>
	                                            <input type="text" name="sellerAuthorizedSignature" value="${detail.sellerAuthorizedSignature }">  (盖章)
	                                        </div>
	                                        <div class="purchase_contract_head_left">
	                                            <span>Seller：</span>
	                                            <input type="text" name="sellerAuthorizedSignatureEn" value="${detail.sellerAuthorizedSignatureEn }">  (Common Seal)
	                                        </div>
	                                        <div class="purchase_contract_head_left">
	                                            <span>授权代表签字：</span>
	                                        </div>
	                                        <div class="purchase_contract_head_left">
	                                            <span>Authorized signature：</span>
	                                        </div>
                                        </div>
                                        
                                        <div class="mt60">
                                        	<div class="purchase_contract_head_left">
	                                            <span>签订日期：</span>
	                                        </div>
	                                        <div class="purchase_contract_head_left">
	                                            <span>Date：</span>
	                                        </div>
                                        </div>
                                        
                                    </div>
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
		
		var url = serverAddr+"/purchase/modifyPurchaseContract.asyn";
		var form = $("#edit-form").serializeArray();
		
		var year = $("#year").val() ;
		var month = $("#month").val() ;
		var days = $("#days").val() ;
		if(year && month && days){
			
			var date = year + "-" + month + "-" + days ;
			
			if(isNaN(date)&&!isNaN(Date.parse(date))){
				form.push({"name":"deliveryDateStr","value":date}) ;
			}else{
				$custom.alert.error("交货日期非日期格式");
			}
			
		}
		
		var paymentMethod = $("input[name='paymentMethod']:checked").val() ;
		var paymentMethodText = "" ;
		var paymentMethodTextEn = "" ;
		if(paymentMethod == '1'){
			var input = $(".paymentMethodText_1").eq(0).find("input") ;
			var inputEn = $(".paymentMethodText_1").eq(1).find("input") ;
			
			if($(input).val()){
				paymentMethodText = $(input).val() ;
			}
			
			if($(inputEn).val()){
				paymentMethodTextEn = $(inputEn).val() ;
			}
			
		}else if(paymentMethod == '2'){
			var input = $(".paymentMethodText_2").eq(0).find("input") ;
			$(input).each(function(index, item){
				var text = "" ;
				if($(item).val()){
					text = $(item).val() ;
				}
				
				paymentMethodText += text + ";" ;
			}) ;
			
			var inputEn = $(".paymentMethodText_2").eq(1).find("input") ;
			$(inputEn).each(function(index, item){
				var text = "" ;
				if($(item).val()){
					text = $(item).val() ;
				}
				
				paymentMethodTextEn += text + ";" ;
			}) ;
			
		}else if(paymentMethod == '3'){
			var textarea = $(".paymentMethodText_3").eq(0).find("textarea") ;
			paymentMethodText += $(textarea).val() ;
		}
		
		if(paymentMethodText){
			form.push({"name":"paymentMethodText","value":paymentMethodText}) ;
		}
		
		if(paymentMethodTextEn){
			form.push({"name":"paymentMethodTextEn","value":paymentMethodTextEn}) ;
		}
		
		$custom.request.ajax(url, form, function(result){
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
		
		var url = serverAddr+"/purchase/modifyPurchaseContract.asyn";
		var form = $("#edit-form").serializeArray();
		
		var year = $("#year").val() ;
		var month = $("#month").val() ;
		var days = $("#days").val() ;
		if(year && month && days){
			
			var date = year + "-" + month + "-" + days ;
			
			if(isNaN(date)&&!isNaN(Date.parse(date))){
				form.push({"name":"deliveryDateStr","value":date}) ;
			}else{
				$custom.alert.error("交货日期非日期格式");
			}
			
		}
		
		var paymentMethod = $("input[name='paymentMethod']:checked").val() ;
		var paymentMethodText = "" ;
		var paymentMethodTextEn = "" ;
		if(paymentMethod == '1'){
			var input = $(".paymentMethodText_1").eq(0).find("input") ;
			var inputEn = $(".paymentMethodText_1").eq(1).find("input") ;
			
			if($(input).val()){
				paymentMethodText = $(input).val() ;
			}
			
			if($(inputEn).val()){
				paymentMethodTextEn = $(inputEn).val() ;
			}
			
		}else if(paymentMethod == '2'){
			var input = $(".paymentMethodText_2").eq(0).find("input") ;
			$(input).each(function(index, item){
				var text = "" ;
				if($(item).val()){
					text = $(item).val() ;
				}
				
				paymentMethodText += text + ";" ;
			}) ;
			
			var inputEn = $(".paymentMethodText_2").eq(1).find("input") ;
			$(inputEn).each(function(index, item){
				var text = "" ;
				if($(item).val()){
					text = $(item).val() ;
				}
				
				paymentMethodTextEn += text + ";" ;
			}) ;
			
		}else if(paymentMethod == '3'){
			var textarea = $(".paymentMethodText_3").eq(0).find("textarea") ;
			paymentMethodText += $(textarea).val() ;
		}
		
		if(paymentMethodText){
			form.push({"name":"paymentMethodText","value":paymentMethodText}) ;
		}
		
		if(paymentMethodTextEn){
			form.push({"name":"paymentMethodTextEn","value":paymentMethodTextEn}) ;
		}
		
		$custom.request.ajax(url, form, function(result){
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
	
	/**
	* 多选框联动
	*/
	function linkageCheckBox(org){
		var val = $(org).val() ;
		
		var cls = $(org).attr("class") ;
		
		if($(org).is(':checked')){
			$("." + cls).prop('checked', '') ;			
			$("input[value='"+val+"']").prop('checked', 'checked') ;
			
			if("DPA" == val){
				$(".meansOfTransportation2").show() ;
				$(".meansOfTransportation1").hide() ;
				$(".meansOfTransportation3").hide() ;
				
				$(".meansOfTransportation1").find("input").val("") ;
				$(".meansOfTransportation3").find("input").val("") ;
				
			}else if("EXW" == val){
				$(".meansOfTransportation3").show() ;
				$(".meansOfTransportation1").hide() ;
				$(".meansOfTransportation2").hide() ;
				
				$(".meansOfTransportation1").find("input").val("") ;
				$(".meansOfTransportation2").find("input").val("") ;
				
			}else if("CIF" == val || "CFR" == val || "FOB" == val){
				$(".meansOfTransportation1").show() ;
				$(".meansOfTransportation2").hide() ;
				$(".meansOfTransportation3").hide() ;
				
				$(".meansOfTransportation2").find("input").val("") ;
				$(".meansOfTransportation3").find("input").val("") ;
			}
			
		}else{
			$("input[value='"+val+"']").prop('checked', '') ;
		}
		
	}
	
	function paymentMethodTextShow(org){
		var val = $(org).val() ;
		
		if('1' == val){
			$(".paymentMethodText_1").show() ;
			$(".paymentMethodText_2").hide() ;
			$(".paymentMethodText_3").hide() ;
		}else if('2' == val){
			$(".paymentMethodText_1").hide() ;
			$(".paymentMethodText_2").show() ;
			$(".paymentMethodText_3").hide() ;
		}else if('3' == val){
			$(".paymentMethodText_1").hide() ;
			$(".paymentMethodText_2").hide() ;
			$(".paymentMethodText_3").show() ;
		}
	}
	
	function init(){
		var meansOfTransportation = "${detail.meansOfTransportation}" ;
		var date = "${detail.deliveryDate}" ;
		var paymentMethod = "${detail.paymentMethod}" ;
		var paymentMethodText = "${detail.paymentMethodText}" ;
		var paymentMethodTextEn = "${detail.paymentMethodTextEn}" ;
		
		if(meansOfTransportation){
			var means = meansOfTransportation.split(",") ;
			
			$(means).each(function(i, m){
				$("input[value='"+ m +"']").prop('checked', 'checked') ;
				
				if("DPA" == m){
					$(".meansOfTransportation2").show() ;
					$(".meansOfTransportation1").hide() ;
					$(".meansOfTransportation3").hide() ;
				}else if("EXW" == m){
					$(".meansOfTransportation3").show() ;
					$(".meansOfTransportation1").hide() ;
					$(".meansOfTransportation2").hide() ;
				}else if("CIF" == m || "CFR" == m || "FOB" == m){
					$(".meansOfTransportation1").show() ;
					$(".meansOfTransportation1").hide() ;
					$(".meansOfTransportation3").hide() ;
				}
				
			}) ;
		}
		
		if(date){
			date = 	date.split(" ")[0] ;
			var deliveryEndDate = parseEngDate(date) ;
			
			$("#deliveryEngDate").val(deliveryEndDate) ;
			
			var dateArr = date.split("-") ;
			
			$("#year").val(dateArr[0]) ;
			$("#month").val(dateArr[1]) ;
			$("#days").val(dateArr[2]) ;
			
		}
		
		if(paymentMethod){
			
			$("input[name='paymentMethod'][value='"+paymentMethod+"']").prop('checked', 'checked') ;
			
			if('1' == paymentMethod){
				$(".paymentMethodText_1").show() ;
				$(".paymentMethodText_2").hide() ;
				$(".paymentMethodText_3").hide() ;
				
				if(paymentMethodText){
					$(".paymentMethodText_1").eq(0).find("input").val(paymentMethodText) ;
				}
				
				if(paymentMethodTextEn){
					$(".paymentMethodText_1").eq(1).find("input").val(paymentMethodTextEn) ;
				}
				
			}else if('2' == paymentMethod){
				$(".paymentMethodText_1").hide() ;
				$(".paymentMethodText_2").show() ;
				$(".paymentMethodText_3").hide() ;
				
				if(paymentMethodText){
					
					var arr = paymentMethodText.split(";") ;
					
					$(".paymentMethodText_2").eq(0).find("input").each(function(j , item){
						$(item).val(arr[j]) ;
					}) ;
				}
				
				if(paymentMethodTextEn){
					
					var arr = paymentMethodTextEn.split(";") ;
					
					$(".paymentMethodText_2").eq(1).find("input").each(function(j , item){
						$(item).val(arr[j]) ;
					}) ;
				}
				
			}else if('3' == paymentMethod){
				$(".paymentMethodText_1").hide() ;
				$(".paymentMethodText_2").hide() ;
				$(".paymentMethodText_3").show() ;
				
				if(paymentMethodText){
					$(".paymentMethodText_3").find("textarea").text(paymentMethodText) ;
				}
			}
		}
		
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
	
	function parseEngDate(dateDtr){
		let date = new Date(dateDtr.replace(/-/g,'/')); //Wed Jan 02 2019 00:00:00 GMT+0800 (China Standard Time)

		let chinaDate = date.toDateString(); //"Tue, 01 Jan 2019 16:00:00 GMT"
		//注意：此处时间为中国时区，如果是全球项目，需要转成【协调世界时】（UTC）
		let globalDate = date.toUTCString(); //"Wed Jan 02 2019"

		//之后的处理是一样的
		let chinaDateArray = chinaDate.split(' '); //["Wed", "Jan", "02", "2019"]

		let displayDate = chinaDateArray[1] + ' ' + chinaDateArray[2] + ', '  + chinaDateArray[3];
		
		return displayDate ;
	}
	
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
	
	/**
	*	审核合同
	*/
	function auditContract(){
		
		var url = "" ;
		var tempStatus = $('input:radio[name="status"]:checked').val() ;
		
		if('001' == status){
			url = serverAddr+"/purchase/submitPurchaseContract.asyn";
		}else if('002' == status){
			
			if(!tempStatus){
				$custom.alert.error("请选择审核结果");
				
				return ;
			}
			
			url = serverAddr+"/purchase/auditPurchaseContract.asyn";
		}
		
		var form = $("#edit-form").serializeArray();
		
		var year = $("#year").val() ;
		var month = $("#month").val() ;
		var days = $("#days").val() ;
		if(year && month && days){
			
			var date = year + "-" + month + "-" + days ;
			
			if(isNaN(date)&&!isNaN(Date.parse(date))){
				form.push({"name":"deliveryDateStr","value":date}) ;
			}else{
				$custom.alert.error("交货日期非日期格式");
			}
			
		}
		
		var paymentMethod = $("input[name='paymentMethod']:checked").val() ;
		var paymentMethodText = "" ;
		var paymentMethodTextEn = "" ;
		if(paymentMethod == '1'){
			var input = $(".paymentMethodText_1").eq(0).find("input") ;
			var inputEn = $(".paymentMethodText_1").eq(1).find("input") ;
			
			if($(input).val()){
				paymentMethodText = $(input).val() ;
			}
			
			if($(inputEn).val()){
				paymentMethodTextEn = $(inputEn).val() ;
			}
			
		}else if(paymentMethod == '2'){
			var input = $(".paymentMethodText_2").eq(0).find("input") ;
			$(input).each(function(index, item){
				var text = "" ;
				if($(item).val()){
					text = $(item).val() ;
				}
				
				paymentMethodText += text + ";" ;
			}) ;
			
			var inputEn = $(".paymentMethodText_2").eq(1).find("input") ;
			$(inputEn).each(function(index, item){
				var text = "" ;
				if($(item).val()){
					text = $(item).val() ;
				}
				
				paymentMethodTextEn += text + ";" ;
			}) ;
			
		}else if(paymentMethod == '3'){
			var textarea = $(".paymentMethodText_3").eq(0).find("textarea") ;
			paymentMethodText += $(textarea).text() ;
		}
		
		if(paymentMethodText){
			form.push({"name":"paymentMethodText","value":paymentMethodText}) ;
		}
		
		if(paymentMethodTextEn){
			form.push({"name":"paymentMethodTextEn","value":paymentMethodTextEn}) ;
		}
		
		$custom.request.ajax(url, form, function(result){
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
