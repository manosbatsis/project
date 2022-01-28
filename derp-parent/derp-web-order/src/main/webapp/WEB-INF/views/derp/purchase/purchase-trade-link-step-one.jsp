<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<jsp:include page="/WEB-INF/views/common.jsp" />
<!-- Start content -->
<style>
    #step {
        overflow: hidden;
    }

    .ui-step-item-title {
        height: 42px;
    }

    .ui-step-wrap .ui-step .ui-step-item .ui-step-item-num {
        margin-top: 0;
    }
</style>
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
          <div class="row">
           <div class="col-12">
              <div class="card-box table-responsive" >
                                          <!--  title   start  -->
              <div class="col-12">
                <div>
                   <div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                       <li><i class="block"></i> 当前位置：</li>
                       <li class="breadcrumb-item"><a href="#">采购档案</a></li>
                       <li class="breadcrumb-item"><a href="#">采购订单列表</a></li>
                       <li class="breadcrumb-item"><a href="#">采购链路步骤一</a></li>
                    </ol>
                    </div>
                   </div>
                    <!--  title   end  -->     
                    <div id="step"></div>          
                          <!--  title   基本信息   start -->
                    <form id="add-form">
                    <div class="title_text">交易链路</div>
                    <div class="edit_box mt20" style="height: 45px;">
                    	<input type="hidden" name="id" id="id" value="${detail.id }" >
                    	<input type="hidden" name="tradeLinkId" id="tradeLinkId" value="${detail.tradeLinkId }" required reqMsg = "请选择交易链路">
                    	<input type="hidden" name="purchaseOrderId" id="purchaseOrderId" value="${detail.purchaseOrderId }">
                    	<input type="hidden" name="purchaseOrderCode" id="purchaseOrderCode" value="${detail.purchaseOrderCode }">
                    	<div class="edit_item">
                    		<i class="red">*</i> 选择交易链路：<input type="button" class="btn btn-info waves-effect waves-light btn_default" id="trade-select-button" value="请选择"/>
                    	</div>
                    	<div class="edit_item"></div>
                    	<div class="edit_item"></div>
                    </div>
                    <div class="edit_box divCustomer1"  style="display:none;">
                    	<div class="edit_item">
                    		<label class="edit_label"><i class="red">*</i> 起点公司：</label>
                    		<input type="text" readonly parsley-type="text" class="edit_input" id="startPointMerchantName" value="${detail.startPointMerchantName }">
                    	</div>
                    	<div class="edit_item">
                    		<label class="edit_label"><i class="red" >*</i> 事业部：</label>
                    		<input type="text" readonly parsley-type="text" class="edit_input" id="startPointBuName" value="${detail.startPointBuName }">
                    	</div>
                    	<div class="edit_item">
                    		<label class="edit_label" style="font-size: 13px;"><i class="red">*</i> 销售加价比例%：</label>
                    		<input type="text" readonly parsley-type="text" class="edit_input" id="startPointPremiumRate" value="${detail.startPointPremiumRate }">
                    	</div>
                    </div>
                    <div class="edit_box  divCustomer1" style="display:none;">
                    	<div class="edit_item">
                    		<label class="edit_label"><i class="red" id="startDepotI">*</i> 仓库：</label>
                    		<select class="edit_input depot" name="startPointDepotId" id="startPointDepotId" required reqMsg="起点公司仓库不能为空">
                                <option value="">请选择</option>
                            </select>
                    	</div>
                    	<div class="edit_item">
                    		<label class="edit_label"><i class="red" id="startPoNoI">*</i> PO号：</label>
                    		<input type="text" parsley-type="text" class="edit_input" id="startPointPoNo" name="startPointPoNo" value="${detail.startPointPoNo }" required reqMsg="起点PO 号不能为空">
                    	</div>
                    	<div class="edit_item">
                    	</div>
                    </div>
                    <div class="edit_box divCustomer2"  style="display:none;">
                    	<div class="edit_item">
                    		<label class="edit_label"><i class="red">*</i> 客户1：</label>
                    		<input type="text" readonly parsley-type="text" class="edit_input" id="oneCustomerName" value="${detail.oneCustomerName }">
                    	</div>
                    	<div class="edit_item">
                    		<label class="edit_label"><i class="red" id="oneBuNameI">*</i> 事业部：</label>
                    		<input type="text" readonly parsley-type="text" class="edit_input" id="oneBuName" value="${detail.oneBuName }">
                    	</div>
                    	<div class="edit_item">
                    		<label class="edit_label" style="font-size: 13px;"><i class="red">*</i> 销售加价比例%：</label>
                    		<input type="text" readonly parsley-type="text" class="edit_input" id="onePremiumRate" value="${detail.onePremiumRate }">
                    	</div>
                    </div>
                    <div class="edit_box divCustomer2" style="display:none;">
                    	<div class="edit_item">
                    		<label class="edit_label"><i class="red" id="depotIdI2">*</i> 仓库：</label>
                    		<select class="edit_input depot" name=oneDepotId id="oneDepotId" >
                                <option value="">请选择</option>
                            </select>
                    	</div>
                    	<div class="edit_item">
                    		<label class="edit_label"><i class="red" id="poNo2I" <c:if test="${ detail.oneType == '2'}">style="display:none;"</c:if>>*</i> PO号：</label>
                    		<input type="text" parsley-type="text" class="edit_input" id="onePoNo" name="onePoNo" value="${detail.onePoNo }" <c:if test="${ detail.oneType == '2'}">readonly</c:if>>
                    	</div>
                    	<div class="edit_item">
                    	</div>
                    </div>
                    <div class="edit_box divCustomer3" style="display:none;">
                    	<div class="edit_item">
                    		<label class="edit_label"><i class="red">*</i> 客户2：</label>
                    		<input type="text" readonly parsley-type="text" class="edit_input" id="twoCustomerName" value="${detail.twoCustomerName }">
                    	</div>
                    	<div class="edit_item">
                    		<label class="edit_label"><i class="red" id="twoBuNameI">*</i> 事业部：</label>
                    		<input type="text" readonly parsley-type="text" class="edit_input" id="twoBuName" value="${detail.twoBuName }">
                    	</div>
                    	<div class="edit_item">
                    		<label class="edit_label" style="font-size: 13px;"><i class="red">*</i> 销售加价比例%：</label>
                    		<input type="text" readonly parsley-type="text" class="edit_input" id="twoPremiumRate" value="${detail.twoPremiumRate }">
                    	</div>
                    </div>
                    <div class="edit_box divCustomer3"  style="display:none;">
                    	<div class="edit_item">
                    		<label class="edit_label"><i class="red" id="depotIdI3">*</i> 仓库：</label>
                    		<select class="edit_input depot" name="twoDepotId" id="twoDepotId" >
                                <option value="">请选择</option>
                            </select>
                    	</div>
                    	<div class="edit_item">
                    		<label class="edit_label"><i class="red" id="poNo3I" <c:if test="${ detail.twoType == '2'}">style="display:none;"</c:if>>*</i> PO号：</label>
                    		<input type="text" parsley-type="text" class="edit_input" id="twoPoNo" name="twoPoNo" value="${detail.twoPoNo }" <c:if test="${ detail.twoType == '2'}">readonly</c:if>>
                    	</div>
                    	<div class="edit_item">
                    	</div>
                    </div>
                    <div class="edit_box divCustomer4" style="display:none;">
                    	<div class="edit_item">
                    		<label class="edit_label"><i class="red">*</i> 客户3：</label>
                    		<input type="text" readonly parsley-type="text" class="edit_input" id="threeCustomerName" value="${detail.threeCustomerName }">
                    	</div>
                    	<div class="edit_item">
                    		<label class="edit_label"><i class="red" id="threeBuNameI">*</i> 事业部：</label>
                    		<input type="text" readonly parsley-type="text" class="edit_input" id="threeBuName" value="${detail.threeBuName }">
                    	</div>
                    	<div class="edit_item">
                    		<label class="edit_label" style="font-size: 13px;"><i class="red">*</i> 销售加价比例%：</label>
                    		<input type="text" readonly parsley-type="text" class="edit_input" id="threePremiumRate" value="${detail.threePremiumRate }">
                    	</div>
                    </div>
                    <div class="edit_box divCustomer4" style="display:none;">
                    	<div class="edit_item">
                    		<label class="edit_label"><i class="red" id="depotIdI4">*</i> 仓库：</label>
                    		<select class="edit_input depot" name="threeDepotId" id="threeDepotId" >
                                <option value="">请选择</option>
                            </select>
                    	</div>
                    	<div class="edit_item">
                    		<label class="edit_label"><i class="red" id="poNo4" <c:if test="${ detail.threeType == '2'}">style="display:none;"</c:if>>*</i> PO号：</label>
                    		<input type="text" parsley-type="text" class="edit_input" id="threePoNo" name="threePoNo" value="${detail.threePoNo }" <c:if test="${ detail.threeType == '2'}">readonly</c:if>>
                    	</div>
                    	<div class="edit_item">
                    	</div>
                    </div>
                    <div class="edit_box divCustomer5" style="display:none;">
                    	<div class="edit_item">
                    		<label class="edit_label"><i class="red">*</i> 客户4：</label>
                    		<input type="text" readonly parsley-type="text" class="edit_input" id="fourCustomerName" value="${detail.fourCustomerName }">
                    	</div>
                    	<div class="edit_item">
                    		<label class="edit_label"><i class="red" id="fourBuNameI">*</i> 事业部：</label>
                    		<input type="text" readonly parsley-type="text" class="edit_input" id="fourBuName" value="${detail.fourBuName }">
                    	</div>
                    	<div class="edit_item">
                    		<label class="edit_label" style="font-size: 13px;"><i class="red">*</i> 销售加价比例%：</label>
                    		<input type="text" readonly parsley-type="text" class="edit_input" id="fourPremiumRate" value="${detail.fourPremiumRate }">
                    	</div>
                    </div>
                    <div class="edit_box divCustomer5" style="display:none;">
                    	<div class="edit_item">
                    		<label class="edit_label"><i class="red" id="depotIdI4">*</i> 仓库：</label>
                    		<select class="edit_input depot" name="fourDepotId" id="fourDepotId" >
                                <option value="">请选择</option>
                            </select>
                    	</div>
                    	<div class="edit_item">
                    		<label class="edit_label"><i class="red" id="poNo5I" <c:if test="${ detail.fourType == '2'}">style="display:none;"</c:if>>*</i> PO号：</label>
                    		<input type="text" parsley-type="text" class="edit_input" id="fourPoNo" name="fourPoNo" value="${detail.fourPoNo }" <c:if test="${ detail.fourType == '2'}">readonly</c:if>>
                    	</div>
                    	<div class="edit_item">
                    	</div>
                    </div>
                	<div class="title_text">公共交易信息  (信息默认从当前采购单带出且部分信息不能修改，信息将应用在自动生成的采购、销售单据中)</div>
                    <div class="edit_box mt20">
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> 归属日期：</label>
                            <input type="text" class="edit_input" name="infoAuditDate" id="infoAuditDate" style="font-size:13px;" required reqMsg = "归属日期不能为空"  value='<fmt:formatDate value="${detail.infoAuditDate }" pattern="yyyy-MM-dd"/>'>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i>交易币种：</label> 
                            <input type="text" class="edit_input" readonly value="${detail.infoCurrencyLabel }">
                            <input type="hidden" name="infoCurrency" value="${detail.infoCurrency }">
                        </div>
                        <div class="edit_item">
                        	<label class="edit_label"><i class="red" >*</i>业务模式：</label> 
                            <select class="edit_input" name="infoBusinessModel" id="infoBusinessModel" required reqMsg = "业务模式不能为空" >
                        		<option value="">请选择</option>
                        		<option value="1">购销-整批结算</option>
                        		<option value="4">购销-实销实结</option>
                        		<option value="0">购销-预售订单</option>
                        		<option value="3">采销执行</option>
                        	</select>
                        </div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item">
                            <label class="edit_label"><i class="red" id="infoLoadPortI">*</i> 装货港：</label>
                            <input type="text" parsley-type="text" class="edit_input" name="infoLoadPort" id="infoLoadPort" required reqMsg = "装货港不能为空" value="${detail.infoLoadPort }">
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red" id="infoUnloadPortI">*</i> 卸货港：</label>
                            <select class="edit_input" name="infoUnloadPort" id="infoUnloadPort" required reqMsg = "卸货港不能为空" >
                        		<option value="">请选择</option>
                        	</select>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"> 装船时间：</label>
                            <input type="text"  parsley-type="text" class="edit_input form_datetime date-input" name="infoShipDate" value="<fmt:formatDate value="${detail.infoShipDate }" pattern="yyyy-MM-dd"/>" id="infoShipDate">
                        </div>
                    </div>
                    <div class="edit_box">
                    	<div class="edit_item">
                        	<label class="edit_label"><i class="red" id="infoTransportI">*</i> 运输方式：</label>
                        	<select name="infoTransport" id="infoTransport" class="edit_input" required reqMsg = "运输方式不能为空">
                            	<option value="">请选择</option>
                            </select>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red" id="infoCarrierI">*</i> 承运商：</label>
                            <input type="text"  parsley-type="text" class="edit_input" name="infoCarrier" id="infoCarrier" value="${detail.infoCarrier }" required reqMsg = "承运商不能为空">
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"> 车次：</label>
                            <input type="text" parsley-type="text" class="edit_input" name="infoTrainNumber" id="infoTrainNumber" value="${detail.infoTrainNumber }">
                        </div>
                    </div>
                    <div class="edit_box">
                    	<div class="edit_item">
                         	<label class="edit_label"> 标准箱TEU：</label>
                         	<input type="text" parsley-type="text" class="edit_input" name="infoStandardCaseTeu" id="infoStandardCaseTeu" value="${detail.infoStandardCaseTeu }">
                        </div>
                        <div class="edit_item">
                        	<label class="edit_label"><i class="red" id="infoTorrNumI">*</i> 托数：</label>
                            <input type="text"  parsley-type="text" class="edit_input" name="infoTorrNum" id="infoTorrNum" value="${detail.infoTorrNum }" required reqMsg = "托数不能为空">
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"> LBX单号：</label>
                            <input type="text"  parsley-type="text" class="edit_input" name="infoLbxNo" id="infoLbxNo" value="${detail.infoLbxNo }">
                        </div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item">
                          	<label class="edit_label"> 提单号：</label>
                          	<input type="text" parsley-type="text" class="edit_input" name="infoLadingBill" id="infoLadingBill" value="${detail.infoLadingBill }">
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"> 提单毛重KG：</label>
                            <input type="text"  parsley-type="text" class="edit_input" name="infoGrossWeight" id="infoGrossWeight" value="${detail.infoGrossWeight }">
                        </div>
                        <div class="edit_item">
                        </div>
                    </div>
                   <!--   商品信息  start -->
                    <div class="col-12 ml10">
                        <div class="row">
	                         <div class="col-9">
	                             <div class="title_text">公共PO合同信息  (以我司标准版合同做参照，默认从当前PO文件带出，信息将应用到自动生成的采购PO文件中)</div>
	                             <div class="mt20">
                                   	<span class="purchase_contract_sub_title"></span>
                                   	<div class="purchase_contract_sub_content">
                                        <span class="purchase_contract_inline" style="width: 18%;">运输方式：</span>
                                        <input type="checkbox" class="meansCheck1" name="conMeansOfTransportation" value="CIF" onclick="linkageCheckBox(this)">CIF
                                        <input type="checkbox" class="meansCheck1" name="conMeansOfTransportation" value="CFR" onclick="linkageCheckBox(this)">CFR
                                        <input type="checkbox" class="meansCheck1" name="conMeansOfTransportation" value="FOB" onclick="linkageCheckBox(this)">FOB
                                        <input type="checkbox" class="meansCheck1" name="conMeansOfTransportation" value="DPA" onclick="linkageCheckBox(this)">DPA
                                        <input type="checkbox" class="meansCheck1" name="conMeansOfTransportation" value="EXW" onclick="linkageCheckBox(this)">EXW
                                        <input type="checkbox" class="meansCheck2" name="conMeansOfTransportation" value="BY SEA" onclick="linkageCheckBox(this)">海运
                                        <input type="checkbox" class="meansCheck2" name="conMeansOfTransportation" value="BY AIR" onclick="linkageCheckBox(this)">空运
                                        <input type="checkbox" class="meansCheck2" name="conMeansOfTransportation" value="BY LAND" onclick="linkageCheckBox(this)">陆运
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
                                   	<span class="purchase_contract_sub_title"></span>
                                   	<div class="purchase_contract_sub_content meansOfTransportation1">
                                        <span class="purchase_contract_inline purchase_contract_w20 ">始发地（港）：</span>
                                        <input class=" purchase_contract_w20 " type="text" name="conLoadingCnPort" value="">
                                        <span class="purchase_contract_inline purchase_contract_w20 ">目的地（港）：</span>
                                        <input class=" purchase_contract_w20 " type="text"name="conDestinationdCn" value="">
                                       </div>
                                       <div class="purchase_contract_sub_content meansOfTransportation2" style="display:none ;">
                                        <span class="purchase_contract_inline purchase_contract_w20 ">发货地址：</span>
                                        <input style="width:60%;" type="text" name="conDeliveryAddress" value="">
                                       </div>
                                       <div class="purchase_contract_sub_content meansOfTransportation3" style="display:none ;">
                                        <span class="purchase_contract_inline purchase_contract_w20 ">提货地址：</span>
                                        <input style="width:60%;" type="text" name="conPickingUpAddress" value="">
                                       </div>
                                   </div>
                                   <div class="mt10 ">
                                   	<span class="purchase_contract_sub_title"></span>
                                   	<div class="purchase_contract_sub_content meansOfTransportation1">
                                        <span class="purchase_contract_inline purchase_contract_w20 ">Loading Port：</span>
                                        <input class=" purchase_contract_w20 " type="text" name="conLoadingEnPort" value="">
                                        <span class="purchase_contract_inline purchase_contract_w20 ">Destination：</span>
                                        <input class=" purchase_contract_w20 " type="text" name="conDestinationdEn" value="">
                                       </div>
                                       <div class="purchase_contract_sub_content meansOfTransportation2" style="display:none ;">
                                        <span class="purchase_contract_inline purchase_contract_w20 ">Delivery Address:</span>
                                        <input style="width:60%;" class=" purchase_contract_w20 " type="text" name="conDeliveryAddressEn" value="">
                                       </div>
                                       <div class="purchase_contract_sub_content meansOfTransportation3" style="display:none ;">
                                        <span class="purchase_contract_inline purchase_contract_w20 ">Picking up Address：</span>
                                        <input style="width:60%;" type="text" name="conPickingUpAddressEn" value="">
                                       </div>
                                   </div>
                                   <div class="mt20">
                                   	<span class="purchase_contract_sub_title"></span>
                                   	<div class="purchase_contract_sub_content">
                                   		<span class="purchase_contract_w15 purchase_contract_inline">交货日期：</span>
                                   		<input style="width:5%;"  type="text" id="year"> 年
                                   		<input style="width:3%;"  type="text" id="month"> 月
                                   		<input style="width:3%;"  type="text" id="days"> 日 
                                   		(若此交货日期与甲方与最终买方签署的框架采购合同不一致的,以此交货期为准)
                                   	</div>
                                   </div>
                                   <div class="mt10">
                                   	<span class="purchase_contract_sub_title"></span>
                                   	<div class="purchase_contract_sub_content">
                                   		<span class="purchase_contract_w15 purchase_contract_inline">Delivery date：</span>
                                   		<input style="width:12%;" id="deliveryEngDate"  type="text" value=""> (If this delivery date is in conflict with the delivery date stipulated in the Purchase Frame Contract signed by the Buyer and the ultimate buyer, this one will prevail.)
                                   	</div>
                                   </div>
                                   <div class="mt20">
                                   	<span class="purchase_contract_sub_title" style="width: 10%;">付款方式 ：</span>
                                       <input type="radio" name="conPaymentMethod" onclick="paymentMethodTextShow(this)"  value="1" checked> 一次结清
                                       <input type="radio" name="conPaymentMethod" onclick="paymentMethodTextShow(this)" value="2"> 分批付款
                                       <input type="radio" name="conPaymentMethod" onclick="paymentMethodTextShow(this)" value="3"> 其他
                                   </div>
                                   <div class="mt20">
                                   	<span class="purchase_contract_sub_title"></span>
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
                                   	<span class="purchase_contract_sub_title">特别约定（本条与本订单约定的内容存在冲突的，以本条约定的为准）：</span>
                                   	<div class="purchase_contract_sub_content">
                                   		<span class="purchase_contract_w15 purchase_contract_inline">特别约定: </span>
                                   		【<textarea name="conSpecialAgreement" style="vertical-align:top;width:60%;heigth:100px;">${detail.conSpecialAgreement }</textarea>】
                                   	</div>
                                   </div>
                                   <div class="mt10">
                                   	<span class="purchase_contract_sub_title"></span>
                                   	<div class="purchase_contract_sub_content">
                                   		<span class="purchase_contract_w15 purchase_contract_inline">Special agreement: </span>
                                   		【<textarea name="conSpecialAgreementEn" style="vertical-align:top;width:60%;heigth:100px;">${detail.conSpecialAgreementEn }</textarea>】
                                   	</div>
                                   </div>
	                        </div>
                        </div>
                    </div>
                    </form>
                    <div class="title_text">说明</div>
				    <div style="padding: inherit;">
				    	<p></p>
				        <p> 1、只有在加价比例配置模块配置好的客户才能参与选择</p>
                        <p> 2、非末级客户只能是内部公司，客户不能重复</p>
						<p> 3、系统将自动生成采购、销售订单 </p>
						<p> 4、该功能只适用购销-整批结算、购销实销实结、预算订单业务模式</p>
				    </div>
                    <div class="form-row m-t-50">
                        <div class="col-12">
                            <div class="row">
                                <div class="col-4">

                                </div>
                                <div class="col-4">
                                    <input type="button" class="btn btn-light waves-effect waves-light btn_default" id="cancel-buttons" value="取 消" />
                                 	<input type="button" class="btn btn-info waves-effect waves-light btn_default" id="save-buttons" value="下一步"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
              </div>
             </div>
                  <!-- end row -->
                  <!-- 弹窗开始 -->
                  <jsp:include page="/WEB-INF/views/modals/2015.jsp" />
                  <!-- 弹窗结束 -->
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->
<script type="text/javascript">

	var businessModel = "${detail.infoBusinessModel }" ;
	if(businessModel){
		$("#infoBusinessModel").val("${detail.infoBusinessModel }") ;
	}

	var depotType = "${depotType}" ;
	depotTypeGLogic(depotType) ;
	
	
	$(function(){
		
		/**根据页面宽度渲染进度条长度*/
		var width = $(".content").width() ;
		width -= 100 ;
		
		if(width % 3 != 0){
			width = Math.floor(width / 3) * 3 ;
		}
		$("#step").width(width) ;
		
		var $step = $("#step");
        $step.step({
            index: 0,
            time: 500,
            title: ["<font style=\"font-size:12px;\">确定交易链路<br>确定交易链路的参与公司及交易数据</font>",  "<font style=\"font-size:12px;\">预览交易信息<br>预览各公司的采购、销售单据，确认价格等信息</font>", "<font style=\"font-size:12px;\">生成单据<br>生成各公司的采购、销售单据</font>"]
        });
        
        
		
		$module.constant.getConstantListByNameURL.call($('select[name="infoTransport"]')[0],"transportList",'${detail.infoTransport}');
		$module.constant.getConstantListByNameURL.call($('select[name="infoUnloadPort"]')[0],"portDestList",'${detail.infoUnloadPort}');
		$derpTimer.lessThanNowDateTimer("#infoAuditDate", "yyyy-MM-dd") ;
		$derpTimer.init("#infoShipDate", "yyyy-MM-dd") ;
		
		initLink() ;
		contractInit() ;
		
		
		//弹框取消按钮
		$("#cancel-buttons").click(function(){
			$custom.alert.warning("界面数据将不保存，是否返回列表页？",function(){
				$module.load.pageOrder("act=3001");
			}) ;
		});
	
		//保存并审核按钮
		$("#save-buttons").click(function(){
			
			//必填项校验
        	if(!$checker.verificationRequired()){
        		return ;
        	}
			
        	saveStepOne() ;
		});
		
		//选择交易链路
		$("#trade-select-button").click(function(){
			$m2015.init() ;
		});
		
		$("#startPointDepotId").change(function(){
			var depotId = $(this).val();
			
			$custom.request.ajax("/depot/getDepotDetails.asyn", {"id":depotId}, function(result){
				
				depotTypeGLogic(result.data.type) ;
				
			});
		});
		
	}) ;
	
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
	
	function contractInit(){
		var meansOfTransportation = "${detail.conMeansOfTransportation}" ;
		var date = "${detail.conDeliveryDate}" ;
		var paymentMethod = "${detail.conPaymentMethod}" ;
		var paymentMethodText = "${detail.conPaymentMethodText}" ;
		var paymentMethodTextEn = "${detail.conPaymentMethodTextEn}" ;
		
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
	
	function saveStepOne(){
		
		var url = serverAddr+"/purchase/saveOrUpdateLinkStepOne.asyn";
		
		var infoAuditDate = $("#infoAuditDate").val() ;
		if(infoAuditDate && infoAuditDate.indexOf("00:00:00") < 0){
			infoAuditDate += " 00:00:00" ;
			$("#infoAuditDate").val(infoAuditDate) ;
		}
		
		var infoShipDate = $("#infoShipDate").val() ;
		if(infoShipDate && infoShipDate.indexOf("00:00:00") < 0){
			infoShipDate += " 00:00:00" ;
			$("#infoShipDate").val(infoShipDate) ;
		}
		
		var form = $("#add-form").serializeArray();
		
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
		
		var paymentMethod = $("input[name='conPaymentMethod']:checked").val() ;
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
			form.push({"name":"conPaymentMethodText","value":paymentMethodText}) ;
		}
		
		if(paymentMethodTextEn){
			form.push({"name":"conPaymentMethodTextEn","value":paymentMethodTextEn}) ;
		}
		
		$custom.request.ajax(url, form, function(result){
			if(result.state == 200){
				$load.a($module.params.loadOrderPageURL+"act=30018&id=" + result.data);
			}else{
				if(result.expMessage != null){
					$custom.alert.error(result.expMessage);
				}else{
					$custom.alert.error(result.message);
				}
			}
		});
	}
	
	function initLink(){
		
		var depotType = "a,b,c,d,e,f,g" ;
		
		if('3' == businessModel){
			depotType = 'd' ;
			
			var html = "<input type='hidden' name='infoBusinessModel' value='3'><input type='text' readonly parsley-type='text' class='edit_input' value='采销执行' />" ;
			
			$("#infoBusinessModel").after(html) ;
			$("#infoBusinessModel").remove() ;
		}
		
		if('${detail.startPointMerchantName}'){
        	$(".divCustomer1").show() ;
        	$module.depot.getSelectBeanByMerchantRel.call($("#startPointDepotId"), '${detail.startPointDepotId}', {"type": depotType, "merchantId": '${detal.startPointMerchantId}'});
        }
		
        var oneCustomerJson = null ;
        var oneCustomerId = null;
        if('${detail.oneCustomerName}'){
        	$(".divCustomer2").show() ;
        	if('${detail.oneType}' == '1'){
             	$("#oneDepotId").attr("required", "true") ;
             	$("#oneDepotId").attr("reqMsg", "客户1仓库不能为空") ;
             	
             	oneCustomerId = '${detail.oneCustomerId}' ;
             	var tempMerchantId = oneCustomerId;
             	if('${detail.oneIdValueType}' == '1'){
                 	oneCustomerJson = $module.customer.getCustomerById(oneCustomerId) ;
                 	tempMerchantId = oneCustomerJson.innerMerchantId ;
             	}
             	
             	$module.depot.getSelectBeanByMerchantRel.call($("#oneDepotId"), '${detail.oneDepotId}',{"type": depotType, "merchantId": tempMerchantId});
             }else{
             	$module.depot.getSelectBeanByMerchantRel.call($("#oneDepotId"), '${detail.oneDepotId}',{"type": "b", "merchantId": '${detail.startPointMerchantId}'});
             }
        }
        
        var twoCustomerJson = null ;
        var twoCustomerId = null;
        if('${detail.twoCustomerName}'){
        	$(".divCustomer3").show() ;
        	
        	 if('${detail.twoType}' == '1'){
              	$("#twoDepotId").attr("required", "true") ;
              	$("#twoDepotId").attr("reqMsg", "客户2仓库不能为空") ;
              	
              	twoCustomerId = '${detail.twoCustomerId}' ;
              	var tempMerchantId = twoCustomerId ;
              	if('${detail.twoIdValueType}' == '1'){
              		twoCustomerJson = $module.customer.getCustomerById(twoCustomerId) ;
	                tempMerchantId = twoCustomerJson.innerMerchantId ;
              	}
              	
             	$module.depot.getSelectBeanByMerchantRel.call($("#twoDepotId"), '${detail.twoDepotId}',{"type": depotType, "merchantId": tempMerchantId});
              }else{
             	 if(oneCustomerJson){
	                 	$module.depot.getSelectBeanByMerchantRel.call($("#twoDepotId"), '${detail.twoDepotId}',{"type": 'b', "merchantId": oneCustomerJson.innerMerchantId});
             	 }else{
             		$module.depot.getSelectBeanByMerchantRel.call($("#twoDepotId"), '${detail.twoDepotId}',{"type": 'b', "merchantId": oneCustomerId});
             	 }
              }
        }
        
        var threeCustomerJson = null ;
        var threeCustomerId = null;
        if('${detail.threeCustomerName}'){
        	$(".divCustomer4").show() ;
        	
        	if('${detail.threeType}' == '1'){
             	$("#threeDepotId").attr("required", "true") ;
             	$("#threeDepotId").attr("reqMsg", "客户3仓库不能为空") ;
             	
             	threeCustomerId = '${detail.threeCustomerId}' ;
             	var tempMerchantId = threeCustomerId ;
             	if('${detail.threeIdValueType}' == '1'){
             		threeCustomerJson = $module.customer.getCustomerById(threeCustomerId) ;
                 	tempMerchantId = threeCustomerJson.innerMerchantId ;
             	}
             	
           		$module.depot.getSelectBeanByMerchantRel.call($("#threeDepotId"), '${detail.threeDepotId}', {"type": depotType, "merchantId": tempMerchantId});
             }else{
            	 if(twoCustomerJson){
                 	$module.depot.getSelectBeanByMerchantRel.call($("#threeDepotId"), '${detail.threeDepotId}', {"type": 'b', "merchantId": twoCustomerJson.innerMerchantId});
            	 }else{
            		$module.depot.getSelectBeanByMerchantRel.call($("#threeDepotId"), '${detail.threeDepotId}', {"type": 'b', "merchantId": twoCustomerId});
            	 }
             }
        }
        
        var fourCustomerJson = null ;
        var fourCustomerId = null;
        if('${detail.fourCustomerName}'){
        	$(".divCustomer5").show() ;
        	
        	if('${detail.fourType}' == '1'){
             	$("#fourDepotId").attr("required", "true") ;
             	$("#fourDepotId").attr("reqMsg", "客户4仓库不能为空") ;
             	
             	fourCustomerId = '${detail.fourCustomerId}' ;
             	var tempMerchantId = fourCustomerId ;
             	if('${detail.fourIdValueType}' == '1'){
             		fourCustomerJson = $module.customer.getCustomerById(fourCustomerId) ;
                 	tempMerchantId = fourCustomerJson.innerMerchantId ;
             	}
             	
             	fourCustomerJson = $module.customer.getCustomerById(fourCustomerId)
           		$module.depot.getSelectBeanByMerchantRel.call($("#fourDepotId"), '${detail.fourDepotId}',{"type": depotType, "merchantId": tempMerchantId});
             }else{
            	 if(threeCustomerJson){
                 	$module.depot.getSelectBeanByMerchantRel.call($("#fourDepotId"), '${detail.fourDepotId}',{"type": 'b', "merchantId": threeCustomerJson.innerMerchantId});
            	 }else{
            		$module.depot.getSelectBeanByMerchantRel.call($("#fourDepotId"), '${detail.fourDepotId}',{"type": 'b', "merchantId": threeCustomerId});
            	 }
             }
        }
	}
	
	function depotTypeGLogic(depotType){
  		if(depotType== 'd'){
			$("#infoTransportI").hide() ;
			$("#infoTorrNumI").hide() ;
			$("#infoLoadPortI").hide() ;
			$("#infoUnloadPortI").hide() ;

			$("#infoTorrNum").removeAttr("required") ;
			$("#infoTransport").removeAttr("required") ;
			$("#infoLoadPort").removeAttr("required") ;
			$("#infoUnloadPort").removeAttr("required") ;
			$("#infoCarrier").removeAttr("required") ;
			
		}else{
			$("#infoTransportI").show() ;
			$("#infoTorrNumI").show() ;
			$("#infoLoadPortI").show() ;
			$("#infoUnloadPortI").show() ;
			$("#infoCarrierI").show() ;
			
			$("#infoTorrNum").attr("required", "") ;
			$("#infoTransport").attr("required", "") ;
			$("#infoLoadPort").attr("required", "") ;
			$("#infoUnloadPort").attr("required", "") ;
			$("#infoCarrier").attr("required", "") ;
		}
  	}
	
</script>
