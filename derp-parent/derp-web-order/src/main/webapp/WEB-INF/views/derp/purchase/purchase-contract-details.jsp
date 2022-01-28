<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common.jsp" />
<!-- Start content -->

<style>

	.detail-span{
		vertical-align:top;
		word-break:normal; 
	    width:20%; 
	    display:inline-block; 
	    white-space:pre-wrap;
	    word-wrap : break-word ;
	    overflow: hidden ;
	}

</style>

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
                                            <li class="breadcrumb-item"><a href="#">采购合同详情</a></li>
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
                                            <span class="detail-span" style="text-align: left;">${detail.purchaseContractNo }</span>
                                            <input type="hidden" id="id" name="id" value="${detail.id }">
                                            <input type="hidden" id="orderId" name="orderId" value="${detail.orderId }">
                                            <span>采购订单号：</span>
                                            <span class="detail-span" style="text-align: left;">${detail.purchaseOrderNo }</span>
                                        </div>
                                        <div class="purchase_contract_head_left">
                                            <span>Purchase contract No.：</span>
                                            <span class="detail-span" style="text-align: left;">${detail.purchaseContractNo }</span>
                                            <span>Purchase order No.：</span>
                                            <span class="detail-span" style="text-align: left;">${detail.purchaseOrderNo }</span>
                                        </div>
                                        <div class="purchase_contract_head_left">
                                            <span>甲方：</span>
                                            <span class="detail-span" style="text-align: left;">${detail.buyerCnName }</span>
                                            <span>乙方：</span>
                                            <span class="detail-span" style="text-align: left;">${detail.sellerCnName }</span>
                                        </div>
                                        <div class="purchase_contract_head_left">
                                            <span>Buyer：</span>
                                            <span class="detail-span" style="text-align: left;">${detail.buyerEnName }</span>
                                            <span>Seller：</span>
                                            <span class="detail-span" style="text-align: left;">${detail.sellerEnName }</span>
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
	                                            <input type="checkbox" name="meansOfTransportation" value="CIF" onclick="linkageCheckBox(this)" disabled>CIF
	                                            <input type="checkbox" name="meansOfTransportation" value="CFR" onclick="linkageCheckBox(this)" disabled>CFR
	                                            <input type="checkbox" name="meansOfTransportation" value="FOB" onclick="linkageCheckBox(this)" disabled>FOB
	                                            <input type="checkbox" name="meansOfTransportation" value="DPA" onclick="linkageCheckBox(this)" disabled>DPA
	                                            <input type="checkbox" name="meansOfTransportation" value="EXW" onclick="linkageCheckBox(this)" disabled>EXW
	                                            <input type="checkbox" name="meansOfTransportation" value="BY SEA" onclick="linkageCheckBox(this)" disabled>海运
	                                            <input type="checkbox" name="meansOfTransportation" value="BY AIR" onclick="linkageCheckBox(this)" disabled>空运
	                                            <input type="checkbox" name="meansOfTransportation" value="BY LAND" onclick="linkageCheckBox(this)" disabled>陆运
                                            </div>
                                        </div>
                                        <div class="mt10">
                                        	<div class="purchase_contract_sub_content">
	                                            <span class="purchase_contract_inline" style="width: 18%;">Means of Transportation：</span>
	                                            <input type="checkbox"  value="CIF" onclick="linkageCheckBox(this)" disabled>CIF
	                                            <input type="checkbox" value="CFR" onclick="linkageCheckBox(this)" disabled>CFR
	                                            <input type="checkbox" value="FOB" onclick="linkageCheckBox(this)" disabled>FOB
	                                            <input type="checkbox" value="DPA" onclick="linkageCheckBox(this)" disabled>DPA
	                                            <input type="checkbox" value="EXW" onclick="linkageCheckBox(this)" disabled>EXW
	                                            <input type="checkbox" value="BY SEA" onclick="linkageCheckBox(this)" disabled>BY SEA
	                                            <input type="checkbox" value="BY AIR" onclick="linkageCheckBox(this)" disabled>BY AIR
	                                            <input type="checkbox" value="BY LAND" onclick="linkageCheckBox(this)" disabled>BY LAND
                                            </div>
                                        </div>
                                        <div class="mt20">
                                        	<span class="purchase_contract_sub_title">3、</span>
                                        	<div class="purchase_contract_sub_content meansOfTransportation1">
	                                            <span class="purchase_contract_inline purchase_contract_w20 ">始发地（港）：</span>
	                                            <span class="detail-span" style="text-align: left;">${detail.loadingCnPort }</span>
	                                            <span class="purchase_contract_inline purchase_contract_w20 ">目的地（港）：</span>
	                                            <span class="detail-span" style="text-align: left;">${detail.destinationdCn }</span>
                                            </div>
                                            <div class="purchase_contract_sub_content meansOfTransportation2" style="display:none ;">
	                                            <span class="purchase_contract_inline purchase_contract_w20 ">发货地址：</span>
	                                            <span class="detail-span" style="text-align: left; width:45% ;">${detail.deliveryAddress }</span>
                                            </div>
                                            <div class="purchase_contract_sub_content meansOfTransportation3" style="display:none ;">
	                                            <span class="purchase_contract_inline purchase_contract_w20 ">提货地址：</span>
	                                            <span class="detail-span" style="text-align: left; width:45% ;">${detail.pickingUpAddress }</span>
                                            </div>
                                        </div>
                                        <div class="mt10 ">
                                        	<span class="purchase_contract_sub_title"></span>
                                        	<div class="purchase_contract_sub_content meansOfTransportation1">
	                                            <span class="purchase_contract_inline purchase_contract_w20 ">Loading Port：</span>
	                                            <span class="detail-span" style="text-align: left;">${detail.loadingEnPort }</span>
	                                            <span class="purchase_contract_inline purchase_contract_w20 ">Destination：</span>
	                                            <span class="detail-span" style="text-align: left;">${detail.destinationdEn }</span>
                                            </div>
                                            <div class="purchase_contract_sub_content meansOfTransportation2" style="display:none ;">
	                                            <span class="purchase_contract_inline purchase_contract_w20 ">Delivery Address:</span>
	                                            <span class="detail-span" style="text-align: left; width:45% ;">${detail.deliveryAddressEn }</span>
                                            </div>
                                            <div class="purchase_contract_sub_content meansOfTransportation3" style="display:none ;">
	                                            <span class="purchase_contract_inline purchase_contract_w20 ">Picking up Address：</span>
	                                            <span class="detail-span" style="text-align: left; width:45% ;">${detail.pickingUpAddressEn }</span>
                                            </div>
                                        </div>
                                        <div class="mt20">
                                        	<span class="purchase_contract_sub_title">4、</span>
                                        	<div class="purchase_contract_sub_content">
                                        		<span class="purchase_contract_w15 purchase_contract_inline">交货日期：</span>
                                        		<span style="width:4%;" id="year"> </span> 年 
                                        		<span style="width:4%;" id="month"> </span> 月 
                                        		<span style="width:4%;" id="days"> </span> 日 
                                        		(若此交货日期与甲方与最终买方签署的框架采购合同不一致的,以此交货期为准)
                                        	</div>
                                        </div>
                                        <div class="mt10">
                                        	<span class="purchase_contract_sub_title"></span>
                                        	<div class="purchase_contract_sub_content">
                                        		<span class="purchase_contract_w15 purchase_contract_inline">Delivery date：</span>
                                        		<span style="width:10%;" id="deliveryEngDate"></span> (If this delivery date is in conflict with the delivery date stipulated in the Purchase Frame Contract signed by the Buyer and the ultimate buyer, this one will prevail.)
                                        	</div>
                                        </div>
                                        <div class="mt20">
                                        	<span class="purchase_contract_sub_title" style="width: 10%;">付款方式 ：</span>
                                            <input type="radio" name="paymentMethod" onclick="paymentMethodTextShow(this)"  value="1" disabled> 一次结清
                                            <input type="radio" name="paymentMethod" onclick="paymentMethodTextShow(this)" value="2" disabled> 分批付款
                                            <input type="radio" name="paymentMethod" onclick="paymentMethodTextShow(this)" value="3" disabled> 其他
                                        </div>
                                        <div class="mt20">
                                        	<span class="purchase_contract_sub_title">5、</span>
                                        	<div class="purchase_contract_sub_content paymentMethodText_1">
                                        		<span class="purchase_contract_w15 purchase_contract_inline">付款方式：</span>
                                        		双方确认本订单之日起 <span id="paymentMethodText_1"></span> 个工作日内甲方向乙方支付订单总金额100%货款
                                        	</div>
                                        	<div class="purchase_contract_sub_content paymentMethodText_2" style="display:none;">
                                        		<span class="purchase_contract_w15 purchase_contract_inline">付款方式：</span>
                                        		卖方确认本订单之日起  <span></span> 个工作日内，买方支付订单总金额的
                                        		<span></span> 货款：  <span></span> ，货物到仓后买方验收合格且双方确认理货数据无误后在
                                        		<span></span> 个工作日内支付剩余 
                                        		<span></span> 货款:   <span></span> 。
                                        	</div>
                                        	<div class="purchase_contract_sub_content paymentMethodText_3"  style="display:none;">
                                        		<span class="purchase_contract_w15 purchase_contract_inline">付款方式：</span>
                                        		<textarea style="vertical-align:top;width:60%;heigth:100px;" readonly></textarea>
                                        	</div>
                                        </div>
                                        <div class="mt10">
                                        	<span class="purchase_contract_sub_title"></span>
                                        	<div class="purchase_contract_sub_content paymentMethodText_1">
                                        		<span class="purchase_contract_w15 purchase_contract_inline">Terms of payment:</span>
                                        		TT 100% within <span id="paymentMethodText_1_en"></span> working days from the date of this order.
                                        	</div>
                                        	<div class="purchase_contract_sub_content paymentMethodText_2"  style="display:none;">
                                        		<span class="purchase_contract_w15 purchase_contract_inline">Terms of payment:</span>
                                        		TT <span></span> <span></span> within 
                                        		<span></span> working days   from the date of this order, TT
                                        		<span></span> <span></span> within
                                        		<span></span> working days subject to the satisfaction of the following conditions: (1) delivery of goods to the designated warehouse, (2) the quality of goods is satisfied by the buyer after inspection, and (3) both parties’ confirmation on the quantity of delivered goods.
                                        	</div>
                                        </div>
                                        <div class="mt20">
                                        	<span class="purchase_contract_sub_title">6、特别约定（本条与本订单约定的内容存在冲突的，以本条约定的为准）：</span>
                                        	<div class="purchase_contract_sub_content">
                                        		<span class="purchase_contract_w15 purchase_contract_inline">特别约定: </span>
                                        		【<span class="detail-span" style="text-align: left;">${detail.specialAgreement }</span>】
                                        	</div>
                                        </div>
                                        <div class="mt10">
                                        	<span class="purchase_contract_sub_title"></span>
                                        	<div class="purchase_contract_sub_content">
                                        		<span class="purchase_contract_w15 purchase_contract_inline">Special agreement: </span>
                                        		【<span class="detail-span" style="text-align: left;">${detail.specialAgreementEn }</span>】
                                        	</div>
                                        </div>
                                        <div class="mt20">
                                        	<span class="purchase_contract_sub_title">7、乙方（供货方）收款账户  Banking information of the seller：</span>
                                        	<div class="purchase_contract_content_left">
	                                            <span>账号：</span>
	                                            <span class="detail-span" style="text-align: left; width:45% ;">${detail.accountNo }</span>
	                                        </div>
	                                        <div class="purchase_contract_content_left">
	                                            <span>采购币种：</span>
	                                            <span class="detail-span" style="text-align: left; width:45% ;">${currencyLabel }</span>
	                                        </div>
	                                        <div class="purchase_contract_content_left">
	                                            <span>账户：</span>
	                                            <span class="detail-span" style="text-align: left; width:45% ;">${detail.beneficiaryName }</span>
	                                        </div>
	                                        <div class="purchase_contract_content_left">
	                                            <span>开户行：</span>
	                                            <span class="detail-span" style="text-align: left; width:45% ;">${detail.beneficiaryBankName }</span>
	                                        </div>
	                                        <div class="purchase_contract_content_left">
	                                            <span>地址：</span>
	                                            <span class="detail-span" style="text-align: left; width:45% ;">${detail.bankAddress }</span>
	                                        </div>
	                                        <div class="purchase_contract_content_left">
	                                            <span>Swift Code：</span>
	                                            <span class="detail-span" style="text-align: left; width:45% ;">${detail.swiftCode }</span>
	                                        </div>
                                        </div>
                                        <div class="mt20">
                                        	<div class="purchase_contract_content_left">
	                                            <span>Account No：</span>
	                                            <span class="detail-span" style="text-align: left; width:45% ;">${detail.accountNo }</span>
	                                        </div>
	                                        <div class="purchase_contract_content_left">
	                                            <span>Currency：</span>
	                                            <span class="detail-span" style="text-align: left; width:45% ;">${detail.currency }</span>
	                                        </div>
	                                        <div class="purchase_contract_content_left">
	                                            <span>Beneficiary Name：</span>
	                                            <span class="detail-span" style="text-align: left; width:45% ;">${detail.beneficiaryName }</span>
	                                        </div>
	                                        <div class="purchase_contract_content_left">
	                                            <span>Beneficiary Bank Name：</span>
	                                            <span class="detail-span" style="text-align: left; width:45% ;">${detail.beneficiaryBankName }</span>
	                                        </div>
	                                        <div class="purchase_contract_content_left">
	                                            <span>Bank Address：</span>
	                                            <span class="detail-span" style="text-align: left; width:45% ;">${detail.bankAddress }</span>
	                                        </div>
	                                        <div class="purchase_contract_content_left">
	                                            <span>Swift Code：</span>
	                                            <span class="detail-span" style="text-align: left; width:45% ;">${detail.swiftCode }</span>
	                                        </div>
                                        </div>
                                        <div class="mt60">
                                        	<div class="purchase_contract_head_left">
	                                            <span>甲方：</span>
	                                            <span class="detail-span" style="text-align: left;">${detail.buyerAuthorizedSignature }</span>  (盖章)
	                                        </div>
	                                        <div class="purchase_contract_head_left">
	                                            <span>Buyer：</span>
	                                            <span class="detail-span" style="text-align: left;">${detail.buyerAuthorizedSignatureEn }</span> (Common Seal)
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
	                                            <span class="detail-span" style="text-align: left;">${detail.sellerAuthorizedSignature }</span>  (盖章)
	                                        </div>
	                                        <div class="purchase_contract_head_left">
	                                            <span>Seller：</span>
	                                            <span class="detail-span" style="text-align: left;">${detail.sellerAuthorizedSignatureEn }</span>  (Common Seal)
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
                                    </form>
								<div class="form-row m-t-50">
			                          <div class="col-12">
			                              <div class="row">
			                                  <div class="col-6">
			                                      <input type="button" class="btn btn-info waves-effect waves-light fr btn_default" id="export-buttons" value="导出">
			                                  </div>
			                                  <div class="col-6">
			                                      <input type="button" class="btn btn-light waves-effect waves-light btn_default" id="cancel-buttons" value="取 消">
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

</div> <!-- content -->
<script type="text/javascript">
	
	init() ;
	
	//返回
	$("#cancel-buttons").click(function(){
		$module.load.pageOrder("act=3001");
	});
	
	//保存按钮
	$("#export-buttons").click(function(){
		var url = serverAddr+"/purchase/exportPurchaseContractPdf.asyn?orderId=";
		
		var id = $("#orderId").val() ;
		
		window.open(url + id);
		
	});
	
	/**
	* 多选框联动
	*/
	function linkageCheckBox(org){
		var val = $(org).val() ;
		
		if($(org).is(':checked')){
			$("input[value='"+val+"']").prop('checked', 'checked') ;
			
			if("DPA" == val){
				$(".meansOfTransportation2").show() ;
				$(".meansOfTransportation1").hide() ;
				$(".meansOfTransportation3").hide() ;
			}else if("EXW" == val){
				$(".meansOfTransportation3").show() ;
				$(".meansOfTransportation1").hide() ;
				$(".meansOfTransportation2").hide() ;
			}else if("CIF" == val || "CFR" == val || "FOB" == val){
				$(".meansOfTransportation1").show() ;
				$(".meansOfTransportation1").hide() ;
				$(".meansOfTransportation3").hide() ;
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
					$(".meansOfTransportation2").hide() ;
					$(".meansOfTransportation3").hide() ;
				}
				
			}) ;
		}else{
			$("input[value='1']").prop('checked', 'checked') ;
		}
		
		if(date){
			date = 	date.split(" ")[0] ;		
			var deliveryEndDate = parseEngDate(date) ;
			
			$("#deliveryEngDate").text(deliveryEndDate) ;
			
			var dateArr = date.split("-") ;
			
			$("#year").text(dateArr[0]) ;
			$("#month").text(dateArr[1]) ;
			$("#days").text(dateArr[2]) ;
			
		}
		
		if(paymentMethod){
			
			$("input[name='paymentMethod'][value='"+paymentMethod+"']").prop('checked', 'checked') ;
			
			if('1' == paymentMethod){
				$(".paymentMethodText_1").show() ;
				$(".paymentMethodText_2").hide() ;
				$(".paymentMethodText_3").hide() ;
				
				if(paymentMethodText){
					$("#paymentMethodText_1").text(paymentMethodText) ;
				}
				
				if(paymentMethodTextEn){
					$("#paymentMethodText_1_en").text(paymentMethodTextEn) ;
				}
				
			}else if('2' == paymentMethod){
				$(".paymentMethodText_1").hide() ;
				$(".paymentMethodText_2").show() ;
				$(".paymentMethodText_3").hide() ;
				
				if(paymentMethodText){
					
					var arr = paymentMethodText.split(";") ;
					
					$(".paymentMethodText_2").eq(0).find("span").each(function(j , item){
						if(j > 0){
							$(item).text(arr[j - 1]) ;
						}
					}) ;
				}
				
				if(paymentMethodTextEn){
					
					var arr = paymentMethodTextEn.split(";") ;
					
					$(".paymentMethodText_2").eq(1).find("span").each(function(j , item){
						if(j > 0){
							$(item).text(arr[j - 1]) ;
						}
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
	
</script>
