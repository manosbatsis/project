<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<jsp:include page="/WEB-INF/views/common.jsp" />
<!-- Start content -->
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
                       <li class="breadcrumb-item"><a href="javascript:dh_list();">采购订单列表</a></li>
                       <li class="breadcrumb-item"><a href="javascript:dh_add();">新增采购订单</a></li>
                    </ol>
                    </div>
                   </div>
                    <!--  title   end  -->               
                          <!--  title   基本信息   start -->
                    <div class="title_text">基本信息</div>
                <form id="add-form">
                    <div class="edit_box mt20">
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> 事业部：</label>
                            <select class="edit_input" name="buId" id="buId" submitReqired required reqMsg = "事业部不能为空">
                                <option value="">请选择</option>
                            </select>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> 付款主体：</label>
                                <select class="edit_input" name="paySubject" id="paySubject" submitReqired required reqMsg = "付款主体不能为空">
                                    <option value="">请选择</option>
                                    <c:choose>
                                        <c:when test="${detail.paySubject eq '1' }">
                                            <option value="1" selected>卓普信</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option value="1">卓普信</option>
                                        </c:otherwise>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${detail.paySubject eq '2' }">
                                            <option value="2" selected>${merchantName }</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option value="2">${merchantName }</option>
                                        </c:otherwise>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${detail.paySubject eq '3' }">
                                            <option value="3" selected>卓烨</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option value="3">卓烨</option>
                                        </c:otherwise>
                                    </c:choose>
                                </select>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> 业务模式：</label>
                            <select class="edit_input" name="businessModel" id="businessModel" submitReqired required reqMsg = "业务模式不能为空">
                                <option value="">请选择</option>
                            </select>
                        </div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> 入仓仓库：</label>
                            <select class="edit_input" name="depotId" id="depotId" submitReqired required reqMsg = "入仓仓库不能为空">
                                <option value="">请选择</option>
                            </select>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> 供应商：</label>
                            <select class="edit_input" name="supplierId" id="supplierId" submitReqired required reqMsg = "供应商不能为空">
                                <option value="">请选择</option>
                            </select>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red" id="poNoI" >*</i> PO号：</label>
                            <input type="text" submitReqired required reqMsg = "PO号不能为空" parsley-type="text" class="edit_input" name="poNo" id="poNo" value="${detail.poNo }">
                        </div>
                    </div>
                    <div class="edit_box">
                    	<div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> 采购币种：</label>
                            <input id="currency_hidden" type="hidden" value="${detail.currency }">
                            <select name="currency" class="edit_input" id="currency" submitReqired required reqMsg = "采购币种不能为空">
                            	<option value="">请选择</option>
                            </select>
                        </div>
                        <div class="edit_item" >
                        	<label class="edit_label"><i class="red">*</i> 归属日期：</label>
                        	<input type="text" class="edit_input" name="attributionDate" id="attributionDate" style="font-size:13px;" submitReqired required reqMsg = "归属日期不能为空" value="<fmt:formatDate value="${detail.attributionDate }" pattern="yyyy-MM-dd"/>">
                        </div>
                        <div class="edit_item">
                            <label class="edit_label">LBX单号：</label>
                            <input type="text"  parsley-type="text" class="edit_input" name="lbxNo" id="lbxNo" value="${detail.lbxNo }">
                        </div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item" >
                        	<label class="edit_label"> 入库关区：</label>
                            <select name="inCustomsId" class="edit_input" id="inCustomsId" >
                            	<option value="">请选择</option>
                            </select>
                        </div>
                        <div class="edit_item"  id="tallyingUnitDiv">
                        	<c:if test="${depotType eq 'c' }">
                        		<label class="edit_label"><i class="red">*</i> 海外仓理货单位：</label>
	                            <select name="tallyingUnit" class="edit_input" id="tallyingUnit" submitReqired required reqMsg = "海外仓理货单位不能为空">
	                            	<option value="">请选择</option>
	                            	<option value="00" <c:if test="${detail.tallyingUnit eq '00' }">selected</c:if>>托盘</option>
	                            	<option value="01" <c:if test="${detail.tallyingUnit eq '01' }">selected</c:if>>箱</option>
	                            	<option value="02" <c:if test="${detail.tallyingUnit eq '02' }">selected</c:if>>件</option>
	                            </select>
                        	</c:if>
                        </div>
                        <div class="edit_item">
                        </div>
                    </div>
                    <div class="title_text">物流信息</div>
                    <div class="edit_box mt20" >
                    	<div class="edit_item">
                        	<label class="edit_label"><i class="red" id="transportI">*</i> 运输方式：</label>
                        	<select name="transport" id="transport" class="edit_input" >
                            	<option value="">请选择</option>
                            </select>
                        </div>
                        <div class="edit_item">
                           	<c:choose>
                           		<c:when test="${detail.transport eq '2' }">
		                            <label class="edit_label"><i class="red" id="standardCaseTeuI">*</i> 标准箱TEU：</label>
                           		</c:when>
                           		<c:otherwise>
                           			<label class="edit_label"><i class="red" id="standardCaseTeuI" style="display:none ;">*</i> 标准箱TEU：</label>
                           		</c:otherwise>
                           	</c:choose>
                         	<input type="text" parsley-type="text" class="edit_input" name="standardCaseTeu" id="standardCaseTeu" value="${detail.standardCaseTeu }">
                        </div>
                        <div class="edit_item">
                           	<c:choose>
                           		<c:when test="${detail.transport eq '3' || detail.transport eq '4'}">
                           			<label class="edit_label"><i class="red" id="trainNumberI">*</i> 车次：</label>
                           		</c:when>
                           		<c:otherwise>
                           			<label class="edit_label"><i class="red" id="trainNumberI" style="display:none ;">*</i> 车次：</label>
                           		</c:otherwise>
                           	</c:choose>
                           	<input type="text" parsley-type="text" class="edit_input" name="trainNumber" id="trainNumber" value="${detail.trainNumber }">
                        </div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item">
                            <label class="edit_label">
                            	<c:choose>
                            		<c:when test="${contractNo_required eq '1' }">
                            			<i class="red" id="contractNoI">*</i> 
                            		</c:when>
                            		<c:otherwise>
                            			<i class="red" id="contractNoI" style="display:none;">*</i> 
                            		</c:otherwise>
                            	</c:choose>
                            	报关合同号：
                            </label>
                            <input type="text"  parsley-type="text" class="edit_input" name="contractNo" id="contractNo" value="${detail.contractNo }">
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red" id="loadPortI">*</i> 装货港：</label>
                            <input type="text" parsley-type="text" class="edit_input" name="loadPort" value="${detail.loadPort }">
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red" id="unloadPortI">*</i> 卸货港：</label>
                            <select class="edit_input" name="unloadPort" id="unloadPort" >
                        		<option value="">请选择</option>
                        	</select>
                        </div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item">
                            <label class="edit_label"><i class="red" id="carrierI">*</i> 承运商：</label>
                            <input type="text"  parsley-type="text" class="edit_input" name="carrier" id="carrier" value="${detail.carrier }">
                        </div>
                        <div class="edit_item">
                            <c:choose>
                           		<c:when test="${detail.transport eq '1' || detail.transport eq '2'}">
                           			<label class="edit_label"><i class="red" id="ladingBillI">*</i> 提单号：</label>
                           		</c:when>
                           		<c:otherwise>
                           			<label class="edit_label"><i class="red" id="ladingBillI" style="display:none ;">*</i> 提单号：</label>
                           		</c:otherwise>
                           	</c:choose>
                          	<input type="text" parsley-type="text" class="edit_input" name="ladingBill" id="ladingBill" value="${detail.ladingBill }">
                        </div>
                        <div class="edit_item">
                            <label class="edit_label">提单毛重KG：</label>
                            <input type="text"  parsley-type="text" value="${detail.grossWeight }" class="edit_input" name="grossWeight" id="grossWeight" onblur="calcItemGrossWeight();" onkeyup="amountVal(this, '5')">
                        </div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item">
                        	<label class="edit_label"><i class="red" id="torrNumI">*</i> 托数：</label>
                            <input type="text"  parsley-type="text" class="edit_input" name="torrNum" id="torrNum" value="${detail.torrNum }" onkeyup="amountVal(this, '0')" onblur="setPackType() ">
                        </div>
                        <div class="edit_item">
                        	<label class="edit_label">箱数：</label>
                            <input type="text"  parsley-type="text" class="edit_input" name="boxNum" id="boxNum" value="${detail.boxNum }" onkeyup="amountVal(this, '0')" onblur="setPackType() ">
                        </div>
                        <div class="edit_item">
                            <label class="edit_label">预计到港时间：</label>
                            <input type="text"  parsley-type="text" class="edit_input form_datetime date-input" name="arriveDate2" id="arriveDate2" value='<fmt:formatDate value="${detail.arriveDate }" pattern="yyyy-MM-dd"/>'>
                        </div>
                    </div>
                    <div class="edit_box">
                    	<div class="edit_item">
                        	<label class="edit_label">目的港名称：</label>
                            <input type="text"  parsley-type="text" class="edit_input" name="destinationPortName" id="destinationPortName" value="${detail.destinationPortName }">
                        </div>
                        <div class="edit_item">
                            <label class="edit_label">包装方式：</label>
                            <input type="text"  parsley-type="text" class="edit_input" name="packType" id="packType" value="${detail.packType }">
                        </div>
                        <div class="edit_item">
                        	<label class="edit_label">付款条约：</label>
                            <input type="text"  parsley-type="text" class="edit_input" name="payRules" id="payRules" value="${detail.payRules }">
                        </div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item">
                        	<label class="edit_label">二程提单号：</label>
                            <input type="text"  parsley-type="text" class="edit_input" name="blNo" id="blNo" value="${detail.blNo }">
                        </div>
                        <div class="edit_item">
                        	<label class="edit_label">进出口口岸：</label>
                            <input type="text"  parsley-type="text" class="edit_input" name="imExpPort" id="imExpPort" value="${detail.imExpPort }">
                        </div>
                        <div class="edit_item">
                        	<label class="edit_label">境外发货人：</label>
                            <input type="text"  parsley-type="text" class="edit_input" name="shipper" id="shipper" value="${detail.shipper }">
                        </div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item">
                            <label class="edit_label">唛头：</label>
                            <input type="text"  parsley-type="text" class="edit_input" name="mark" id="mark" value="${detail.mark }">
                        </div>
                        <div id="address" class="edit_item">
                        	<label class="edit_label"><i class="red" id="destinationNameI" style="display:none;">*</i>目的地名称：</label>
                            <input type="text"  parsley-type="text" class="edit_input" name="destinationName" id="destinationName" value="${detail.destinationName }">
                        </div>
                        <div class="edit_item">
                        	<label class="edit_label">发票号：</label>
                            <input type="text"  parsley-type="text" class="edit_input" name="invoiceNo" id="invoiceNo" value="${detail.invoiceNo }">
                        </div>
                    </div>
                    <div class="edit_box">
                    	<div class="edit_item">
                            <label class="edit_label">预计入仓时间：</label>
                            <input type="text"  parsley-type="text" class="edit_input form_datetime date-input" name="arriveDepotDate2" value='<fmt:formatDate value="${detail.arriveDepotDate }" pattern="yyyy-MM-dd"/>'>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label">预计付款时间：</label>
                            <input type="text"  parsley-type="text" class="edit_input form_datetime date-input" name="paymentDate2" value='<fmt:formatDate value="${detail.paymentDate }" pattern="yyyy-MM-dd"/>'>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label">装船时间：</label>
                            <input type="text"  parsley-type="text" class="edit_input form_datetime date-input" name="shipDate2" value='<fmt:formatDate value="${detail.shipDate }" pattern="yyyy-MM-dd"/>'>
                        </div>
                    </div>
                    <div class="edit_box">
                    	<div class="edit_item">
                            <label class="edit_label">收货地点：</label>
                            <input type="text"  parsley-type="text" class="edit_input" name="deliveryAddr" id="deliveryAddr" value="${detail.deliveryAddr }">
                        </div>
                        <div class="edit_item">
                        	<label class="edit_label">销售渠道：</label>
                            <input type="text"  parsley-type="text" class="edit_input" name="channel" id="channel" value="${detail.channel }">
                        </div>
                        <div class="edit_item">
                        </div>
                    </div>
                    <div class="edit_box">
                    	<div class="edit_item">
                            <label class="edit_label">备注：</label>
                            <input type="text"  parsley-type="text" class="edit_input" name="remark" value="${detail.remark }">
                        </div>
                        <div class="edit_item">
                        </div>
                        <div class="edit_item">
                        </div>
                    </div>
                 <!--  title   基本信息  start -->
                 </form>
                   <!--   商品信息  start -->
                    <div class="col-12 ml10">
                        <div class="row">
                         <div class="col-9">
                             <div class="title_text">商品信息</div>
                        </div>
                        <div class="col-1 mt10">
                        </div>
                        <div class="col-1 mt10">
                          <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick="chooseGoods();">选择商品</button>
                        </div>
                         <div class="col-1 mt10">
                          <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="delete-list-button">删 除</button>
                        </div> 
                        </div>
                    </div>
                    <div class="form-row mt20" style="padding: 0 10px;overflow-x: auto">
                   <table id="table-list" class="table table-striped">
                      <thead>
	                      <tr>
	                         <th><input id="checkbox11" type="checkbox" name="all"></th>
	                         <th style="min-width: 100px">子合同号</th>
	                         <th style="min-width: 220px">商品名称</th>
	                         <th style="min-width: 100px">商品条码</th>
	                         <th style="min-width: 100px">商品货号</th>
	                         <th style="min-width: 100px"><i class="red">*</i> 采购数量</th>
	                         <th style="min-width: 100px"><i class="red">*</i> 采购单价（不含税）</th>
	                         <th style="min-width: 100px"><i class="red">*</i> 采购总金额（不含税）</th>
	                         <th style="min-width: 100px">采购总金额（含税）</th>
	                         <th style="min-width: 100px">税率</th>
	                         <th style="min-width: 100px">税额</th>
	                         <th style="min-width: 100px">品牌类型</th>
	                         <th style="min-width: 80px"><i class="red">*</i> 毛重(KG)</th>
	                         <th style="min-width: 80px"><i class="red">*</i> 净重(KG)</th>
	                         <th style="min-width: 60px">箱号</th>
	                         <th style="min-width: 90px">生产批次号</th>
	                         <th style="min-width:100px">成分占比</th>
	                         <th style="min-width:100px">备注</th>
	                      </tr>
                      <thead>
                      <tbody id="itemTbody">
	                      <c:forEach items="${detail.itemList }"  var="item">
	                           <tr>
	                               <td><input type='checkbox' name='item-checkbox'><input type='hidden' name='goodsId' value='${item.goodsId }'><input type='hidden' name='itemId' value='${item.id }'></td>
	                               <td>
                                   	   <input type='text' name='contractNo' class="goods-contractNo" style='width:70px;text-align:center;' value='${item.contractNo }'>
	                               </td>
	                               <td>${item.goodsName }</td>
	                               <td>
                                    	<input type='hidden' name='barcode' value='${item.barcode }'>
	                                   ${item.barcode }
	                               </td>
	                               <td>${item.goodsNo }</td>
	                               <td><input type='text' name='num' style='width:70px;text-align:center;' class='goods-num' value='${item.num }'></td>
	                               <td><input type='text' name='price' class='goods-price' style='width:100px;text-align:center;' value='<fmt:formatNumber value="${item.price }" pattern="#.##" minFractionDigits="8" > </fmt:formatNumber>'></td>
	                               <td>
	                                   <input type='text' name='goods-amount' class='goods-amount' style='width:100px;text-align:center;' value='${item.amount }'>
	                               </td>
	                               <td>
	                               		<input type='hidden' name="taxPrice" class="taxPrice" value="${item.taxPrice }">
	                                   <input type='text' name='taxAmount' class='taxAmount' style='width:100px;text-align:center;' value='${item.taxAmount }'>
	                               </td>
	                               <td>
	                               	   <input type='hidden' class="taxRateHidden" value="${item.taxRate }">
	                                   <select name="taxRate" class="taxRate" onchange="taxRateChange(this) ;"></select>
	                               </td>
	                               <td>
	                                   <input type='text' name='tax' class='tax' style='width:100px;text-align:center;' value='${item.tax }' readonly>
	                               </td>
	                               <td>
	                               	   <input type='text' name='brandName' class='brandName' style='text-align:center;' value='${item.brandName }' onMouseMove="this.title=this.value">
	                               </td>
	                               <td>
	                               		<input type='hidden' name='grossWeights-hidden' class='goods-grossWeight-hidden' value='${item.grossWeight }'>
	                                   	<input type='text' name='grossWeights' class='goods-grossWeight' style='width:70px;text-align:center;' value='${item.grossWeightSum }' onkeyup='amountVal(this, "5")'>
	                               </td>
	                               <td>
	                               		<input type='hidden' name='netWeight-hidden' class='goods-netWeight-hidden' value='${item.netWeight }'>
	                               		<input type='text' name='netWeight' class='goods-netWeight' style='width:70px;text-align:center;' value='${item.netWeightSum }' onkeyup='amountVal(this, "5")'>
	                               </td>
	                               <td>
                                    	<input type='text' name='boxNo' class="goods-boxNo" style='width:70px;text-align:center;' value='${item.boxNo }'>
	                               <td>
	                                    <input type='text' name='batchNo' class="goods-batchNo" style='width:70px;text-align:center;' value='${item.batchNo }'>
	                               <td>
	                                   	<textarea name='constituentRatio' class='goods-constituentRatio' cols='30' rows='3' align='center'>${item.constituentRatio }</textarea>
	                               </td>
	                               <td>
	                               	   	<textarea name='goodsRemark' class='goods-remark' cols='30' rows='3' align='center'>${item.remark }</textarea>
	                               </td>
	                           </tr>
	                       </c:forEach>
                       </tbody>
                  </table>                              
                   </div>
                   <div class="form-row mt20">
                        <div class="col-2">
                            <span>总数量：</span>
                            <span id="totalNum"></span>
                        </div>
                        <div class="col-2">
                            <span>总金额（不含税）：</span>
                            <span id="totalAmount"></span>
                        </div>
                        <div class="col-2">
                            <span>税额：</span>
                            <span id="totalTax"></span>
                        </div>
                        <div class="col-2">
                            <span>总金额（含税）：</span>
                            <span id="totalTaxAmount"></span>
                        </div>
                        <div class="col-2">
                            <span>总毛重：</span>
                            <span id="totalGrossWright"></span>
                        </div>
                        <div class="col-2">
                            <span>总净重：</span>
                            <span id="totalNetWright"></span>
                        </div>
                   </div>
                 <!--   商品信息  end -->
                      <div class="form-row m-t-50">
                          <div class="col-12">
                              <div class="row">
                                  <div class="col-4">

                                  </div>
                                  <div class="col-4">
                                      <input type="button" class="btn btn-info waves-effect waves-light btn_default" id="save-temp-buttons" value="保 存"/>
                                      <input type="button" class="btn btn-light waves-effect waves-light btn_default" id="cancel-buttons" value="取 消" />
                                      <shiro:hasPermission name="purchase_savePurchaseOrder">
                                      		<input type="button" class="btn btn-info waves-effect waves-light btn_default" id="save-buttons" value="下一步"/>
                                      </shiro:hasPermission>
                                  </div>
                                  <div class="col-4">

                                  </div>
                              </div>
                          </div>
                      </div>
                </div>
              </div>
             </div>
			<!-- end row -->
			<!-- 弹窗开始 -->
			<jsp:include page="/WEB-INF/views/modals/4011-V2.jsp" />
			<jsp:include page="/WEB-INF/views/modals/16001.jsp" />
			<!-- 弹窗结束 -->
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->
<script type="text/javascript">

	var detailsBuId = "${detail.buId}" ;
	var orderType = "${type}" ;
	var depotType = "${depotType}" ;
	var businessModel = "${detail.businessModel}" ;
	
    $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0], detailsBuId );

    if("${detail.depotId}"){
        $module.depot.getSelectBeanByMerchantBuRel.call($("select[name='depotId']")[0], "${detail.depotId}", {"buId": detailsBuId, "type":"a,c,d"});
    }else{
        $module.depot.getSelectBeanByMerchantBuRel.call($("select[name='depotId']")[0], "", {"buId": detailsBuId, "type":"a,c,d"});
    }

	depotTypeGLogic(depotType) ;

	
	$(function(){
		
		$module.constant.getConstantListByNameURL.call($('select[name="transport"]')[0],"transportList",'${detail.transport}');
		$module.constant.getConstantListByNameURL.call($('select[name="unloadPort"]')[0],"portDestList",'${detail.unloadPort}');
		$module.constant.getConstantListByNameURL.call($('select[name="businessModel"]')[0],"purchaseOrder_businessModelList",'${detail.businessModel}');
		$module.depot.getSelectBeanByDepotCustomsRel.call($("select[name='inCustomsId']")[0],$("#depotId").val(),'${detail.inCustomsId }');
		$module.supplier.loadSelectData.call($("select[name='supplierId']")[0], '${detail.supplierId }');
		$module.currency.loadSelectData.call($("select[name='currency']")[0],"");
		$derpTimer.lessThanNowDateTimer("#attributionDate", "yyyy-MM-dd") ;
		$derpTimer.init(".date-input", "yyyy-MM-dd") ;

		initTaxRate() ;
		
		var currency = $("#currency_hidden").val() ;
		if(currency){
			$("#currency").val(currency) ;
		}
		
		//根据供应商选择带出对应的币种
		$("#supplierId").change(function(){
			
			var val = $(this).val() ; 
			
			if(val){
				$module.currency.loadSelectData.call($("select[name='currency']")[0],val);
			}else{
				$("#currency").val("") ;
			}
			
		}) ;

		$("#buId").change(function(){

		    var buId = $(this).val() ;

            $module.depot.getSelectBeanByMerchantBuRel.call($("select[name='depotId']")[0], null, {"buId": buId, "type":"a,c,d"});
        }) ;
		
		//当运输方式为海运、空运时, 提单号必填
		$("#transport").change(function(){
			
			var val = $(this).val() ; 
			
			$("#standardCaseTeuI").hide() ;
			//$("#standardCaseTeu").removeAttr("required") ;
			//$("#standardCaseTeu").removeAttr("reqMsg") ;
			
			$("#trainNumberI").hide() ;
			//$("#trainNumber").removeAttr("required") ;
			//$("#trainNumber").removeAttr("reqMsg") ;
			
			$("#ladingBillI").hide() ;
			//$("#ladingBill").removeAttr("required") ;
			//$("#ladingBill").removeAttr("reqMsg") ;
			
			if(val == '1' || val == '2'){
				$("#ladingBillI").show() ;
				//$("#ladingBill").attr("required", "") ;
				//$("#ladingBill").attr("reqMsg", "提单号不能为空") ;
				
				if(val == '2'){
					$("#standardCaseTeuI").show() ;
					//$("#standardCaseTeu").attr("required", "") ;
					//$("#standardCaseTeu").attr("reqMsg", "标准箱TEU不能为空") ;
				}
			}else{
				
				if(val == '3' || val == '4'){
					$("#trainNumberI").show() ;
					//$("#trainNumber").attr("required", "") ;
					//$("#trainNumber").attr("reqMsg", "车次不能为空") ;
				}
				
			}
			
		}) ;
		
		/**
		*当卸货港选其他时，目的地名称必填；
		**/
		$("#unloadPort").change(function(){
			var val = $(this).val() ; 
			
			$("#destinationNameI").hide() ;
			//$("#destinationName").removeAttr("required") ;
			//$("#destinationName").removeAttr("reqMsg") ;
			
			if("other" == val){
				$("#destinationNameI").show() ;
				//$("#destinationName").attr("required", "") ;
				//$("#destinationName").attr("reqMsg", "当卸货港选其他时，目的地名称必填") ;
			}
		});
		
		//弹框取消按钮
		$("#cancel-button").click(function(){
			$(".fade").removeClass("show");
		});
	
		//选择仓库，回写仓库地址, 根据仓库带出事业部
		$("#depotId").change(function(){
			var depot_id = $(this).val();
			
			if(depot_id){
		        $module.depot.getSelectBeanByDepotCustomsRel.call($("select[name='inCustomsId']")[0], depot_id);
			}else{
				$("#inCustomsId").val("") ;
			}
			
			$custom.request.ajax("/depot/getDepotDetails.asyn", {"id":depot_id}, function(result){
				$("#deliveryAddr").val(result.data.address);
				if(result.data.type == 'c'){
					$("#tallyingUnitDiv").html('<label class="edit_label" style="font-size: 12px"><i class="red">*</i> 海外仓理货单位：</label><select name="tallyingUnit" id="tallyingUnit" class="edit_input" required reqMsg = "海外仓理货单位不能为空"><option value="">请选择</option><option value="00">托盘</option><option value="01">箱</option><option value="02">件</option></select>');
				}else{
					$("#tallyingUnitDiv").html('');
				}
				
				if(result.data.type == 'a' && result.data.isTopBooks == '1'){
					$("#contractNoI").css("display","inline-block");
				}else{
					$("#contractNoI").css("display","none");
				}
				
				if(result.data.type == 'a'){
					$custom.request.ajax("/depot/checkDepotMerchantRel.asyn", {"depotId":depot_id}, function(result){
						if(result.data.isInOutInstruction == '1'){
							
							if("other" == $("#unloadPort").val()){
								$("#unloadPort").val("") ;
							}
							
							$("#destinationNameI").hide() ;
							//$("#destinationName").removeAttr("required") ;
							//$("#destinationName").removeAttr("reqMsg") ;
							
							$('#unloadPort option[value="other"]').hide() ;
						}else{
							$('#unloadPort option[value="other"]').show() ;
						}
					}); 
				}
				
				depotTypeGLogic(result.data.type) ;
				depotType = result.data.type ;
				
			});
			//清空商品列表
			$("#table-list tr:gt(0)").remove();
			$("#unNeedIds").val('');
			
			//根据入仓仓库改变目的地
			$custom.request.ajaxSync(serverAddr + "/purchase/getDepotInfo.asyn", {"depotId":depot_id}, function(result){
				if(result.data == "1"){
					$("#address").html('<label class="edit_label"><i class="red" id="destinationNameI">*</i> 目的地名称：</label>'+
							'	<select class="edit_input" name="destinationName" id="destinationName" required reqMsg = "目的地名称不能为空">'+
							'		<option value="">请选择</option>'+
							'	</select>');
					$module.depotSchedule.loadSelectByDepotId.call($("select[name='destinationName']")[0],depot_id);
				} else {
					$("#address").html('<label class="edit_label"><i class="red" id="destinationNameI">*</i>目的地名称：</label>'+
							'<input type="text"  parsley-type="text" class="edit_input" name="destinationName" id="destinationName">');
                
				}
			});
		});
		
		//保存并审核按钮
		$("#save-buttons").click(function(){
            $custom.alert.warning("确定提交该采购订单？",function(){
            	
            	//必填项校验
            	if(!sumbitVerificationRequired()){
            		return ;
            	}
                
                if($("#unNeedIds").val()==""){
                    $custom.alert.error("至少选择一个商品！");
                    return ;
                }
                if($("#boxNum").val() && isNaN($("#boxNum").val())){
                    $custom.alert.error("箱数请输入数值！");
                    return ;
                }
                if($("#torrNum").val() && isNaN($("#torrNum").val())){
                    $custom.alert.error("托数请输入数值！");
                    return ;
                }
                
                var is_overseas = 0;
                var is_dx = 0;
                var is_rookie = 0;
                var depot_id = $("#depotId").val();
                $custom.request.ajaxSync("/depot/getDepotDetails.asyn", {"id":depot_id}, function(result){
                    if(result.data.name.indexOf("菜鸟")!=-1){
                        is_rookie = 1;
                    }
                    if(result.data.type == 'a' && result.data.isTopBooks == '1'){
                        is_dx = 1;
                    }
                    if(result.data.type == 'c'){
                        is_overseas = 1;
                    }
                });
                //如果仓库为卓志保税仓且是代销仓，合同号必填
                /* if(is_dx == 1){
                    if("" == $("#contractNo").val()){
                        $custom.alert.error("仓库为卓志保税仓且是代销仓时，报关合同号必填！");
                        return ;
                    }
                } */

                //商品必填校验
                //数量
                var num_flag = 0;
                $(".goods-num").each(function(){
                    if(isNaN($(this).val()) || $(this).val()<=0 || $(this).val()==''){
                        num_flag = 1;
                    }
                });
                if(num_flag==1){
                    $custom.alert.error("数量要为大于0的数！");
                    return ;
                }
                //价格
                var price_flag = 0;
                $(".goods-price").each(function(){
                    if(isNaN($(this).val()) || $(this).val() < 0 || $(this).val()==''){
                        price_flag = 1;
                    }
                });
                if(price_flag==1){
                    $custom.alert.error("价格要为大于等于0的数！");
                    return ;
                }
                //品牌类型
                var brandName_flag = 0;
                $(".brandName").each(function(){
                    if($(this).val()==''){
                        brandName_flag = 1;
                    }
                });
                if(brandName_flag==1){
                    $custom.alert.error("品牌类型不能为空！");
                    return ;
                }
                //毛重
                var grossWeight_flag = 0;
                $(".goods-grossWeight").each(function(){
                    if(isNaN($(this).val()) || $(this).val()==''){
                        //grossWeight_flag = 1;
                        $(this).val(0) ;
                    }
                });
                /**毛重、净重非必填；*/
                /* if(grossWeight_flag==1){
                    $custom.alert.error("毛重至少为0！");
                    return ;
                } */
                //净重
                var netWeight_flag = 0;
                $(".goods-netWeight").each(function(){
                    if(isNaN($(this).val()) || $(this).val()==''){
                        //netWeight_flag = 1;
                        $(this).val(0) ;
                    }
                });
                /**毛重、净重非必填；*/
                /* if(netWeight_flag==1){
                    $custom.alert.error("净重至少为0！");
                    return ;
                } */
                var billWeight = $("#grossWeight").val();
                //获取所有毛重之和
                var obj_grossWeight = document.getElementsByName("grossWeights");
                var grossWeight = 0;
                for(var i = 0; i < obj_grossWeight.length; i++){
                    grossWeight += Number(obj_grossWeight[i].value);
                }
                if(billWeight == null || billWeight==""){
                    $("#grossWeight").val(grossWeight);
                }else{
                    if(parseFloat(billWeight).toFixed(5) != grossWeight.toFixed(5)){
                        $custom.alert.error("提单毛重要和商品毛重之和相等！");
                        return ;
                    }
                }
                //校验商品毛重<商品净重
                var check_grossWeight = 0;
                $("input[name='grossWeights']").each(function(){
                    if(Math.round($(this).val()*100)/100 < Math.round($(this).parent().next().find("input[name='netWeight']").val()*100)/100){
                        check_grossWeight = 1;
                    }
                });
                if(check_grossWeight==1){
                    $custom.alert.error("商品列表中毛重不能小于净重！");
                    return ;
                }

                var flag = 0;
                var contractNo = $("#contractNo").val();
                var poNo = $("#poNo").val();
                //校验合同号是否存在，存在给予提示
                $custom.request.ajaxSync(serverAddr + "/purchase/getPurchaseOrder.asyn", {"contractNo":contractNo,"poNo":poNo}, function(result){
                    if(result.data == ""){
                        flag = 1;
                    }else{
                        $custom.alert.warning(result.data,function(){
                            saveData(billWeight);
                        });
                    }
                });
                if(flag == 1){
                    saveData(billWeight);
                }
            });
            
		});
		
		function saveData(billWeight){
			var url = serverAddr + "/purchase/savePurchaseOrder.asyn";
			
			var attributionDate = $("#attributionDate").val() ;
			if(attributionDate){
				attributionDate += " 00:00:00" ;
				
				$("#attributionDate").val(attributionDate) ;
			}
			
			var form = $("#add-form").serializeArray();
			
			if($("#depotId").attr("disabled")) {
				form.push({name:"depotId",value:$("#depotId").val()}) ;
			}
			
			if($("#buId").attr("disabled")) {
				form.push({name:"buId",value:$("#buId").val()}) ;
			}
			
			var items = new Array() ;
			
		    $("#itemTbody").find("tr").each(function(index, tr){
                
                var contractNo = $(tr).find("input[name='contractNo']").val() ;
                var goodsId = $(tr).find("input[name='goodsId']").val() ;
                var num = $(tr).find("input[name='num']").val() ;
                var price = $(tr).find("input[name='price']").val() ;
                var amount = $(tr).find("input[name='goods-amount']").val() ;
                var boxNo = $(tr).find("input[name='boxNo']").val() ;
                var batchNo = $(tr).find("input[name='batchNo']").val() ;
                var constituentRatio = $(tr).find("textarea[name='constituentRatio']").val() ;
                var brandName = $(tr).find("input[name='brandName']").val() ;
                var grossWeight = $(tr).find("input[name='grossWeights-hidden']").val() ;
                var grossWeightSum = $(tr).find("input[name='grossWeights']").val() ;
                var netWeight = $(tr).find("input[name='netWeight-hidden']").val() ;
                var netWeightSum = $(tr).find("input[name='netWeight']").val() ;
                var barcode = $(tr).find("input[name='barcode']").val() ;
                var taxAmount = $(tr).find("input[name='taxAmount']").val() ;
                var taxPrice = $(tr).find("input[name='taxPrice']").val() ;
                var tax = $(tr).find("input[name='tax']").val() ;
                var remark = $(tr).find("textarea[name='goodsRemark']").val() ;
                
                var taxRate = "" ;
                if($(tr).find(".taxRate").val()){
	                taxRate = $(tr).find(".taxRate option:selected").text() ;
                }
                
                var item = {"contractNo": contractNo, "goodsId": goodsId, "num":num, "amount": amount,
                        "price":price, "boxNo":boxNo, "batchNo": batchNo, "constituentRatio":constituentRatio,
                        "brandName":brandName, "grossWeight":grossWeight, "grossWeightSum":grossWeightSum,
                        "netWeight":netWeight, "netWeightSum":netWeightSum, "barcode":barcode, 
                        "remark":remark, "taxAmount": taxAmount, "taxPrice":taxPrice, 
                        "taxRate":taxRate, "tax":tax} ;
                
                items.push(item) ;
                
            })  ;
			
			items = JSON.stringify(items);
			
			form.push({name:"items", value:items }) ;
			
			$custom.request.ajax(url, form, function(result){
				if(result.state == '200'){
					if(isNumber(result.data)){
						$custom.alert.success("保存成功, 请进行合同编辑");
						setTimeout(function () {
							editContract(result.data) ;
						}, 1000);
					}else{
						$custom.alert.error(result.data);
					}
				}else{
	            	if(result.expMessage != null){
						$custom.alert.error(result.expMessage);
					}else{
						$custom.alert.error(result.message);
					}

                    if(attributionDate &&
                        attributionDate.indexOf(" ")){

                        $("#attributionDate").val(attributionDate.split(" ")[0]) ;
                    }
	            }
			});
		}
		
		//监听采购总金额 (含税）的离开事件
	    $("#table-list").on("blur",'.taxAmount',function(){

	    	var that = $(this);
	    	//获取当前总价
	    	var taxAmount = that.val();
	    	//判断是否数字
	    	if(isNaN(taxAmount)){
	    		return ;
	    	}
	    	
	    	taxAmount = parseFloat(taxAmount) ;
	    	taxAmount = taxAmount.toFixed(2) ; 
	    	$(this).val(taxAmount) ; 
	    	
			
			
			//获取采购数量
	    	var num = that.parent().parent().find(".goods-num").val();
	    	
	    	if(parseInt(num) != 0){
	    		//设置价格
		    	var price = parseFloat(taxAmount)/parseFloat(num);
		    	that.parent().parent().find('.taxPrice').val(price.toFixed(8));
	    	}
	    	
	    	// 根据税率计算含税金额
	    	var taxRate = that.parent().parent().find(".taxRate") ;
	    	
	    	if($(taxRate).val()){
	    		
		    	taxRate = taxRate.find("option:selected").text() ;
	    		
	    		var sum = parseFloat(taxAmount)/ (1 + parseFloat(taxRate));
	    		sum = parseFloat(sum).toFixed(2) ;
	    		that.parent().parent().find(".goods-amount").val(sum);
	    		
	    		var tax = parseFloat(taxAmount) - parseFloat(sum) ;
	    		that.parent().parent().find(".tax").val(parseFloat(tax).toFixed(2)) ;
	    		
	    		var price = parseFloat(sum)/parseFloat(num);
		    	that.parent().parent().find(".goods-price").val(price.toFixed(8));
	    	}
	    		
	    	
	    	sumTotal() ;
	    });
		
		//监听总金额的离开事件
	    $("#table-list").on("blur",'.goods-amount',function(){
	    	var that = $(this);
	    	//获取当前总价
	    	var amount = that.val();
	    	//判断是否数字
	    	if(isNaN(amount)){
	    		return ;
	    	}
	    	
	    	amount = parseFloat(amount) ;
	    	amount = amount.toFixed(2) ; 
	    	$(this).val(amount) ; 
	    	
	    	//获取采购数量
	    	var num = that.parent().prev().prev().find("input").val();
	    	
	    	if(parseInt(num) != 0){
	    		//设置价格
		    	var price = parseFloat(amount)/parseFloat(num);
		    	that.parent().prev().find('input').val(price.toFixed(8));
	    	}
	    	
	    	// 根据税率计算含税金额
	    	var taxRate = that.parent().parent().find(".taxRate") ;
	    	
	    	if($(taxRate).val()){
		    	taxRate = taxRate.find("option:selected").text() ;
	    		
	    		var taxAmount = parseFloat(sum) * (1 + parseFloat(taxRate))  ;
	    		that.parent().parent().find(".taxAmount").val(parseFloat(taxAmount).toFixed(2)) ;
	    		
	    		var taxPrice = parseFloat(taxAmount) / parseInt(num)  ;
	    		that.parent().parent().find(".taxPrice").val(parseFloat(taxPrice).toFixed(8)) ;
	    		
	    		var tax = parseFloat(taxAmount) - parseFloat(sum) ;
	    		that.parent().parent().find(".tax").val(parseFloat(tax).toFixed(2)) ;
	    		
	    	}
	    	
	    	sumTotal() ;
	    });
		
		//监听采购价格的离开事件
	    $("#table-list").on("blur",'.goods-price',function(){
	    	var that = $(this);
	    	//获取当前价格
	    	var price = that.val();
	    	//判断是否数字
	    	if(!price || isNaN(price)){
	    		return ;
	    	}
	    	price = parseFloat(price) ;
	    	price = price.toFixed(8) ; 
	    	that.val(price) ; 
	    	//获取采购数量
	    	var num = that.parent().prev().find("input").val();
	    	//设置总金额
	    	var sum = price*num;
	    	that.parent().next().find('input').val(sum.toFixed(2));
	    	
	    	// 根据税率计算含税金额
	    	var taxRate = that.parent().parent().find(".taxRate") ;
	    	
	    	if($(taxRate).val()){
		    	taxRate = taxRate.find("option:selected").text() ;
	    		
	    		var taxAmount = parseFloat(sum) * (1 + parseFloat(taxRate))  ;
	    		that.parent().parent().find(".taxAmount").val(parseFloat(taxAmount).toFixed(2)) ;
	    		
	    		var taxPrice = parseFloat(taxAmount) / parseInt(num)  ;
	    		that.parent().parent().find(".taxPrice").val(parseFloat(taxPrice).toFixed(8)) ;
	    		
	    		var tax = parseFloat(taxAmount) - parseFloat(sum) ;
	    		that.parent().parent().find(".tax").val(parseFloat(tax).toFixed(2)) ;
	    		
	    	}
	    	
	    	sumTotal() ;
	    }); 
		
	    //监听数量的离开事件
	    $("#table-list").on("blur",'.goods-num',function(){
	    	
	    	var that = $(this); 
	    	//获取当前数量
	    	var num = that.val();
	    	//获取价格
	    	var price = that.parent().next().find("input").val();
	    	//设置总金额
	    	var sum = price*num;
	    	that.parent().next().next().find('input').val(sum.toFixed(2));
	    	//设置总毛重、总净重
	    	var grossWeight = that.parent().parent().find(".goods-grossWeight-hidden").val();
	    	that.parent().parent().find(".goods-grossWeight").val((grossWeight*num).toFixed(5));
	    	var netWeight = that.parent().parent().find(".goods-netWeight-hidden").val();
	    	that.parent().parent().find(".goods-netWeight").val((netWeight*num).toFixed(5));
	    	
	    	// 根据税率计算含税金额
	    	var taxRate = that.parent().parent().find(".taxRate") ;
	    	
	    	if($(taxRate).val()){
		    	taxRate = taxRate.find("option:selected").text() ;
	    		
	    		var taxAmount = parseFloat(sum) * (1 + parseFloat(taxRate))  ;
	    		that.parent().parent().find(".taxAmount").val(parseFloat(taxAmount).toFixed(2)) ;
	    		
	    		var taxPrice = parseFloat(taxAmount) / parseInt(num)  ;
	    		that.parent().parent().find(".taxPrice").val(parseFloat(taxPrice).toFixed(8)) ;
	    		
	    		var tax = parseFloat(taxAmount) - parseFloat(sum) ;
	    		that.parent().parent().find(".tax").val(parseFloat(tax).toFixed(2)) ;
	    		
	    	}
	    	
	    	sumTotal() ;
	    	
	    });
	    
	  //监听毛重的点击事件
		$("#table-list").on("blur",'.goods-grossWeight',function(){
			sumTotal() ;
		});
	  
		//监听净重的点击事件
		$("#table-list").on("blur",'.goods-netWeight',function(){
			sumTotal() ;
		});
		
		//删除选中行
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
	        
	        sumTotal() ;
	        
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
	    
	    //返回
	    $("#cancel-buttons").click(function(){
            var pageSource = '${pageSource}';
            if (pageSource == "1") {
                $load.a("/list/menu.asyn?act=100&r="+Math.random());
            } else {
                $module.load.pageOrder("act=3001");
            }

	    });
	    
        //保存按钮
        $("#save-temp-buttons").click(function(){
        	
            if($("#buId").val()==""){
                $custom.alert.error("事业部不能为空！");
                return ;
            }

            //数量
            var num_flag = 0;
            $(".goods-num").each(function(){
                if(isNaN($(this).val()) || $(this).val()<=0 || $(this).val()==''){
                    num_flag = 1;
                }
            });
            if(num_flag==1){
                $custom.alert.error("数量要为大于0的数！");
                return ;
            }
            var billWeight = $("#grossWeight").val();
            //获取所有毛重之和
            var obj_grossWeight = document.getElementsByName("grossWeights");
            var grossWeight = 0;
            for(var i = 0; i < obj_grossWeight.length; i++){
                grossWeight += Number(obj_grossWeight[i].value);
            }
            if(billWeight == null || billWeight==""){
                $("#grossWeight").val(grossWeight);
            }

            var attributionDate = $("#attributionDate").val() ;
			if(attributionDate){
				
				if(attributionDate.indexOf("00:00:00") < 0){
					attributionDate += " 00:00:00" ;
				}
				
				$("#attributionDate").val(attributionDate) ;
			}
            
            var url = serverAddr + "/purchase/saveOrUpdateTempOrder.asyn";
            var form = $("#add-form").serializeArray();
            
            if($("#depotId").attr("disabled")) {
				form.push({name:"depotId",value:$("#depotId").val()}) ;
			}
			
			if($("#buId").attr("disabled")) {
				form.push({name:"buId",value:$("#buId").val()}) ;
			}
            
		    var items = new Array() ;
            
            $("#itemTbody").find("tr").each(function(index, tr){
            	
                var contractNo = $(tr).find("input[name='contractNo']").val() ;
                var goodsId = $(tr).find("input[name='goodsId']").val() ;
                var num = $(tr).find("input[name='num']").val() ;
                var price = $(tr).find("input[name='price']").val() ;
                var amount = $(tr).find("input[name='goods-amount']").val() ;
                var boxNo = $(tr).find("input[name='boxNo']").val() ;
                var batchNo = $(tr).find("input[name='batchNo']").val() ;
                var constituentRatio = $(tr).find("textarea[name='constituentRatio']").val() ;
                var brandName = $(tr).find("input[name='brandName']").val() ;
                var grossWeight = $(tr).find("input[name='grossWeights-hidden']").val() ;
                var grossWeightSum = $(tr).find("input[name='grossWeights']").val() ;
                var netWeight = $(tr).find("input[name='netWeight-hidden']").val() ;
                var netWeightSum = $(tr).find("input[name='netWeight']").val() ;
                var barcode = $(tr).find("input[name='barcode']").val() ;
                var remark = $(tr).find("textarea[name='goodsRemark']").val() ;
                var taxAmount = $(tr).find("input[name='taxAmount']").val() ;
                var taxPrice = $(tr).find("input[name='taxPrice']").val() ;
                var tax = $(tr).find("input[name='tax']").val() ;
                
                var taxRate = "" ;
                if($(tr).find(".taxRate").val()){
	                taxRate = $(tr).find(".taxRate option:selected").text() ;
                }
                
                var item = {"contractNo": contractNo, "goodsId": goodsId, "num":num, "amount": amount,
                        "price":price, "boxNo":boxNo, "batchNo": batchNo, "constituentRatio":constituentRatio,
                        "brandName":brandName, "grossWeight":grossWeight, "grossWeightSum":grossWeightSum,
                        "netWeight":netWeight, "netWeightSum":netWeightSum, "barcode":barcode, 
                        "remark":remark, "taxAmount": taxAmount, "taxPrice":taxPrice, 
                        "taxRate":taxRate, "tax":tax} ;
                
                items.push(item) ;
                
            })  ;
            
            items = JSON.stringify(items);
            
            form.push({name:"items", value:items }) ;
            
            $custom.request.ajax(url, form, function(result){
                if(result.state == '200'){
                    $custom.alert.success("新增成功！");
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
    });
	function dh_list(){
		$module.load.pageOrder("act=3001");
	}
	
	function dh_add(){
		$module.load.pageOrder("act=30011");
	}
	
  	//编辑合同
    function editContract(id){
    	var customerId = $("#supplierId").val() ;
  		var url = $module.params.loadOrderPageURL+"act=30015&id="+id ;
  		$m16001.init(url, customerId) ;
    }
  	
  	/**判断是否数字*/
    function isNumber(val){
	   if(val === "" || val ==null){
	   		return false;
	   }
       if(!isNaN(val)){　　　　
    　　//对于空数组和只有一个数值成员的数组或全是数字组成的字符串，isNaN返回false，例如：'123'、[]、[2]、['123'],isNaN返回false,
       //所以如果不需要val包含这些特殊情况，则这个判断改写为if(!isNaN(val) && typeof val === 'number' )
    　　　 		return true; 
    　　	}else{ 
	    　　　　return false; 
	    } 

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
  	
  	function depotTypeGLogic(depotType){
  		/*if(depotType== 'd'){
			$("#transportI").hide() ;
			$("#torrNumI").hide() ;
			$("#loadPortI").hide() ;
			$("#unloadPortI").hide() ;
			$("#carrierI").hide() ;
		}else{
			$("#transportI").show() ;
			$("#torrNumI").show() ;
			$("#loadPortI").show() ;
			$("#unloadPortI").show() ;
			$("#carrierI").show() ;
		}*/
  		$("#transportI").hide() ;
		$("#torrNumI").hide() ;
		$("#loadPortI").hide() ;
		$("#unloadPortI").hide() ;
		$("#carrierI").hide() ;
  	}
  	
  	/* 表单必填项校验 例子：<input type="text" required reqMsg = "车次不能为空" parsley-type="text" class="edit_input" name="trainNumber" id="trainNumber">
	/ 	若表单元素 需必填项校验 只需添加属性 required 、 reqMsg， reqMsg为错误提示信息
	/ 	必填通过返回 true 否则返回false */ 
  	function sumbitVerificationRequired() {
		var reqrs = $('*[submitReqired]') ;
		//是否校验成功
		var flagLength = 0  ;
		var content = "" ;
		$(reqrs).each(function(index , reqr){
			
			$(reqr).css("border-color", "") ;
			
			var val = $(reqr).val();
			
			if(val){
				flagLength++ ;
			}else {
				content = $(reqr).attr("reqMsg") ;
				
				if(!content){
					content = "请检查必填项" ;
				}
				
				$(reqr).css("border-color", "red") ;
				
				var top = $(reqr).offset() ; 
				// 滑动
				$("html,body").animate({scrollTop:top.top - "150" + "px"}, 500);
				
				// 震动
				$(reqr).shake(8, 10, 1500); ;
				
				return false ;
			}
		});
		
		//检查必填项是否全部已输入，否则提示
		if(flagLength != $(reqrs).length){
			$custom.alert.error(content);
			
			return false ;
		}
		
		return true ;
	}
	
	/**根据净重小数位，动态判断小数位后取值*/
	function calcItemGrossWeight(){
		
		var grossWeight = $("#grossWeight").val() ;
		var grossWeightTotal = $("#grossWeight").val() ;
		
		$("#totalGrossWright").html(grossWeight) ;
		
		// 默认5 位
		var decimalPlace = 3 ;
		
		if(grossWeight.indexOf(".") > -1){
			decimalPlace = grossWeight.substr(grossWeight.indexOf(".") + 1).length ;
			
			decimalPlace = parseInt(decimalPlace) ;
			
			if(decimalPlace > 5){
				decimalPlace = 5 ;
			}
		}
		
		/**汇总净重*/
		var netWeightSum = 0;
		$("input[name='netWeight']").each(function(){
			
			if(!$(this).val()){
				$(this).val(0) ;
			}
			
			netWeightSum += parseFloat($(this).val());
		});

		if(netWeightSum!=0){
			$("input[name='netWeight']").each(function(index , item){
				
				var grossWeightTemp = (grossWeight * ($(this).val()/netWeightSum)).toFixed(decimalPlace) ;
				
				if(index != $("input[name='netWeight']").length - 1){
				    $(this).parent().parent().find("input[name='grossWeights']").val(grossWeightTemp);
				    grossWeightTotal -= parseFloat(grossWeightTemp) ;
				    grossWeightTotal = parseFloat(grossWeightTotal).toFixed(decimalPlace) ;
				}else{
					$(this).parent().parent().find("input[name='grossWeights']").val(grossWeightTotal) ;
				}
				
			});
		}
	}

	function setPackType() {

		var packType = "" ;
		
		var boxNum = $("#boxNum").val() ;
		
		if(boxNum){
			packType += boxNum + "箱" ;
		}
		
		var torrNum = $("#torrNum").val() ;
		
		if(torrNum){
			packType += torrNum + "托" ;
		}
		
		$("#packType").val(packType) ;
		
	}
	
	
</script>
