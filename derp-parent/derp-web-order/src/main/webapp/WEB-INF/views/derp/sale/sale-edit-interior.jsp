<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<jsp:include page="/WEB-INF/views/common.jsp" />

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
          <div class="row">
           <div class="col-12">
              <div class="card-box" >
                                          <!--  title   start  -->
              <div class="col-12">
                <div>
                   <div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                       <li><i class="block"></i> 当前位置：</li>
                       <li class="breadcrumb-item"><a href="#">编辑销售订单</a></li>
                    </ol>
                    </div>
                   </div>
                    <!--  title   end  -->               
                          <!--  title   基本信息   start -->
                    <div class="title_text">基本信息</div>
                    <div class="edit_box">
                    <div class="edit_item">
                        <label class="edit_label"><i class="red">*</i> 事业部：</label>
                           <select class="edit_input" name="buId" id="buId" parsley-trigger="change" required reqMsg="请选择事业部">
                               <option value="">请选择</option>
                           </select>
                        </div>                        
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i>出仓仓库：</label>
                            <select class="edit_input" name="outDepotId" id="outDepotId" required reqMsg="请选择出仓仓库">
                                <option value="">请选择</option>
                            </select>
                        </div>
                       <c:choose>
                            <c:when test="${outDepotType == 'd'}">
                                <div class="edit_item" >
                                    <label class="edit_label"><i class="red">*</i>销售类型：</label>
                                    <select class="edit_input" name="businessModel" id="businessModel"  required reqMsg="请选择销售类型">
                                        <option value="" >请选择</option>
                                        <option value="1" <c:if test="${detail.businessModel == '1'}"> selected = 'selected'</c:if>>购销-整批结算</option>
                                        <option value="4" <c:if test="${detail.businessModel == '4'}"> selected = 'selected'</c:if>>购销-实销实结</option>
                                        <option value="3" <c:if test="${detail.businessModel == '3'}"> selected = 'selected'</c:if>>采销执行</option>
                                    </select>
                                </div>
                            </c:when>
                            <c:otherwise>
	                            <div class="edit_item" >
	                                <label class="edit_label"><i class="red">*</i>销售类型：</label>
	                                <select class="edit_input" name="businessModel" id="businessModel" required reqMsg="请选择销售类型">
	                                    <option value="">请选择</option>
	                                    <option value="1" <c:if test="${detail.businessModel == '1'}"> selected = 'selected'</c:if>>购销-整批结算</option>
	                                    <option value="4" <c:if test="${detail.businessModel == '4'}"> selected = 'selected'</c:if>>购销-实销实结</option>
	                                    <option value="3" <c:if test="${detail.businessModel == '3'}"> selected = 'selected'</c:if> disabled="disabled">采销执行</option>
	                                </select>
	                            </div>
                            </c:otherwise>
                        </c:choose>
                        
                    </div>
                    <div class="edit_box">
                    <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> 客   户：</label>
                            <input type="hidden" value="${detail.ownSaleType}" name="ownSaleType" id="ownSaleType">
                            <input type="hidden" value="${detail.id}" name="orderId" id="orderId">
                             <input type="hidden" value="${detail.code}" name="orderCode" id="orderCode">
                            <select class="edit_input" name="customerId" id="customerId" required reqMsg="客户不能为空">
                                <option value="">请选择</option>
                            </select>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> 公   司：</label>
                            <input type="hidden" value="${detail.merchantId}" name="merchantId" id="merchantId"/>
                            <input type="text" class="edit_input" value="${detail.merchantName}" name = "merchantName" id="merchantName" disabled required reqMsg="公司不能为空">
                        </div>
                         
                        <div class="edit_item">
                            <label class="edit_label">
                                	<c:choose>
                            		<c:when test="${isSameAreaRequired == 0 }">
                            			<i class="red" style="display:none;" id="isSameArea_i">*</i>
                            		</c:when>
                            		<c:otherwise>
                            			<i class="red" id="isSameArea_i">*</i>
                            		</c:otherwise>
                            	</c:choose>
                            是否同关区：</label>
                            <select class="edit_input" name="isSameArea" id="isSameArea">
                                <option value="">请选择</option>
                                <option value="1" <c:if test="${detail.isSameArea == '1'}"> selected = 'selected'</c:if>>同关区</option>
                                <option value="0" <c:if test="${detail.isSameArea == '0'}"> selected = 'selected'</c:if>>跨关区</option>
                            </select>
                        </div>
                    </div>
                    <div class="edit_box">
                         <div class="edit_item">
                            <label class="edit_label"><i class="red" id="inDepotId_i">*</i>入仓仓库：</label>
                            <select class="edit_input" name="inDepotId" id="inDepotId">
                                <option value="">请选择</option>
                            </select>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label">
                            	<c:choose>
                            		<c:when test="${isPoRequired == 1 }">
                            			<i class="red"  id="poNo_i">*</i>
                            		</c:when>                            	
                            		<c:otherwise>
                            			<i class="red" style="display:none;" id="poNo_i">*</i>
                            		</c:otherwise>
                            	</c:choose>
                            	 PO单号：
                            </label>
                            <input type="text" class="edit_input" name="poNo" id="poNo" value="${detail.poNo}" placeholder="多PO输入时以&字符隔开">
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i>销售币种：</label>
                             <select class="edit_input" name="currency" id="currency" parsley-trigger="change" required reqMsg="请选择销售币种">
                                <option value="">请选择</option>
                            </select>
                            <input id="currency_hidden" type="hidden" value="${detail.currency }">
                        </div>                         
                    </div>
                    <div class="edit_box">
                   		<div class="edit_item">
                            <label class="edit_label">LBX单号：</label>
                            <input type="text" class="edit_input" name="lbxNo" id="lbxNo" value="${detail.lbxNo}">
                        </div>                        
                    	<div class="edit_item">
                            <label class="edit_label"><i class="red" id="payRules_i">*</i> 付款条约：</label>
                            <input type="text" class="edit_input"  name="payRules" id="payRules" value="${detail.payRules}" >
                        </div>
                        <div class="edit_item"></div>
                    </div>
                    <div class="title_text">物流信息</div>
                    <div class="edit_box">
                    	<div class="edit_item">
                            <label class="edit_label">
                            <c:choose>
                            		<c:when test="${isContractNoRequired == 0 }">
                            			<i class="red" style="display:none;" id="fContractNo_i">*</i>
                            		</c:when>
                            		<c:otherwise>
                            			<i class="red" id="fContractNo_i">*</i>
                            		</c:otherwise>
                            	</c:choose>合同号：</label>
                            <input type="text" class="edit_input" name="fContractNo" id="fContractNo" value="${detail.contractNo}">
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red"  id="transport_i">*</i> 运输方式：</label>
                            <select class="edit_input" name="transport" id="transport" value="${detail.transport}"  ></select>
                        </div>
                         <div class="edit_item">
                            <label class="edit_label">
                                <c:choose>
                                    <c:when test="${isTeuRequired == 1 }">
                                        <i class="red"  id="teu_i">*</i>
                                    </c:when>
                                    <c:otherwise>
                                        <i class="red"  id="teu_i" style="display: none">*</i>
                                    </c:otherwise>
                                </c:choose>
                                标准箱TEU：</label>
                            <input type="text" class="edit_input" name="teu" id="teu" value="${detail.teu}">
                        </div>
                       
                    </div>
                    <div class="edit_box"> 
                        <div class="edit_item">
                            <label class="edit_label">
                                <c:choose>
                                    <c:when test="${isTrainnoRequired == 1 }">
                                        <i class="red"  id="trainno_i">*</i>
                                    </c:when>
                                    <c:otherwise>
                                        <i class="red"  id="trainno_i" style="display: none">*</i>
                                    </c:otherwise>
                                </c:choose>
                                车次：</label>
                            <input type="text" class="edit_input" name="trainno" id="trainno" value="${detail.trainno}">
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red"  id="carrier_i">*</i> 承运商：</label>
                            <input type="text" class="edit_input" name="carrier" id="carrier"  value="${detail.carrier}" >
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red"  id="billWeight_i">*</i> 提单毛重：</label>
                            <div class="edit_input">
                                <input type="text" class="form-control"
                                       id="billWeight" name="billWeight" value="${detail.billWeight}" required reqMsg="提单毛重不能为空" onblur="calcItemGrossWeight();" onkeyup="numFormat(this,5)">
                            </div>
                        </div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item">
                            <label class="edit_label"><i class="red"  id="torusNumber_i">*</i> 托数：</label>
                            <input type="text" class="edit_input" name="torusNumber" id="torusNumber" value="${detail.torusNumber}">
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red"  id="boxNum_i">*</i> 箱数：</label>
                            <input type="text" class="edit_input"  name="boxNum" id="boxNum" value="${detail.boxNum}" >
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red"  id="torrPackType_i">*</i> 托盘材质：</label>
                            <select class="edit_input" name="torrPackType" id="torrPackType"></select>
                        </div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item">
                            <label class="edit_label"><i class="red"  id="pack_i">*</i> 包装：</label>
                            <input type="text" class="edit_input" name="pack" id="pack" value="${detail.pack}" >
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red"  id="invoiceNo_i">*</i> 发票单号：</label>
                            <input type="text" class="edit_input"  name="invoiceNo" id="invoiceNo" value="${detail.invoiceNo}" >
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red"  id="portDes_i">*</i>卸货港：</label>
                            <input type="text" class="edit_input"  name="portDes" id="portDes" value="${detail.portDes}" >                            
                        </div>                    
                    </div>
                    <input type="hidden" value="${detail.businessModel}" id="businessModelVal" >
                    <div class="edit_box">
                        <div class="edit_item">
                            <label class="edit_label"><i class="red"  id="outdepotAddr_i">*</i> 出库地点：</label>
                            <input type="text" class="edit_input"  name="outdepotAddr" id="outdepotAddr" value="${detail.outdepotAddr}" >
                        </div>
                         <div class="edit_item">
                            <label class="edit_label">出库关区：</label>
                            <select class="edit_input" name="outCustomsId" id="outCustomsId" >
                                <option value="">请选择</option>
                            </select>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label">入库关区：</label>
                            <select class="edit_input" name="inCustomsId" id="inCustomsId" >
                                <option value="">请选择</option>
                            </select>
                        </div>           
                    </div>
                    <div class="edit_box">
                    	<div class="edit_item">
                            <label class="edit_label">交货日期：</label>
                            <input type="text" class="edit_input form_datetime date-input" value="${detail.deliveryDate}" name="deliveryDate" id="deliveryDate" >
                        </div>
                         <div class="edit_item" style="flex: 2">
                            <label class="edit_label"  style="flex: 0.369">备 注：</label>
                            <textarea rows="" cols="" class="edit_input" name="remark" id="remark">${detail.remark}</textarea>
                        </div> 
                    </div> 
                    <div class="edit_box"> 
                       <div class="edit_item" id="tallyingUnitDiv" >
                            <c:if test="${not empty detail.tallyingUnit  or outDepotType=='c' }">
                                <label class="edit_label"><i class="red">*</i> 理货单位：</label>
                                <select name="tallyingUnit" id="tallyingUnit" class="edit_input">
                                    <option value="00" <c:if test="${detail.tallyingUnit == '00'}"> selected = 'selected'</c:if>>托盘</option>
                                    <option value="01" <c:if test="${detail.tallyingUnit == '01'}"> selected = 'selected'</c:if>>箱</option>
                                    <option value="02" <c:if test="${detail.tallyingUnit == '02'}"> selected = 'selected'</c:if>>件</option>
                                </select>
                            </c:if>
                        </div>                         
                         <div class="edit_item" id="destinationDiv" >
                            <c:if test="${not empty detail.destination or outDepotType=='c'}">
                                <label class="edit_label"><i class="red">*</i> 目的地：</label>
                                <select name="destination" id="destination" class="edit_input">
                                    <option value="">请选择</option>
                                    <option value="GZJC01"<c:if test="${detail.destination == 'GZJC01'}"> selected = 'selected'</c:if>>广州机场</option>
                                    <option value="HP01"<c:if test="${detail.destination == 'HP01'}"> selected = 'selected'</c:if>>黄埔状元谷</option>
                                    <option value="HK01"<c:if test="${detail.destination == 'HK01'}"> selected = 'selected'</c:if>>香港本地</option>
                                </select>
                            </c:if>
                        </div> 
                       
                    </div>
                    
                    <div class="title_text"> 收件信息</div>
                    <div id="receiverInfoDiv" >
                    	<div class="edit_box">      					                    	
                     		<div class="edit_item">                        		                        	                      	
	                        	<label class="edit_label"><i class="red" id="mailModeDiv" style="display:none">*</i> 提货方式：</label>
	                        	<select name="mailMode" id="mailMode" class="edit_input">
	                        		<option value="">请选择</option>
		                        	<option value="1"<c:if test="${detail.mailMode == '1'}"> selected = 'selected'</c:if>>邮寄</option>
		                        	<option value="2"<c:if test="${detail.mailMode == '2'}"> selected = 'selected'</c:if>>自提</option>		                        	<
	                        	</select>	                        	
                        	</div>
                        	<div class="edit_item">
                            	<label class="edit_label"><i class="red" id="receiverNameDiv" style="display:none">*</i>收件人姓名：</label>
                            	<input type="text" class="edit_input" name="receiverName" id="receiverName" value="${detail.receiverName}">
                        	</div>
                                               
                    	</div>
                     	<div class="edit_box" >
                    	 	<div class="edit_item">
                            	<label class="edit_label"><i class="red" id="countryDiv" style="display:none">*</i>国家：</label>
                            	<input type="text" class="edit_input" name="country" id="country" value="${detail.country}">
                         	</div>
                         	<div class="edit_item">
                            	<label class="edit_label"><i class="red" id="receiverAddressDiv" style="display:none">*</i>详细地址：</label>
                            	<input type="text" class="edit_input" name="receiverAddress" id="receiverAddress" value="${detail.receiverAddress}">
                         	</div>
                     	</div>
                   </div>                    	
                                                                                
                 <!--  title   基本信息  start -->
                   <!--   商品信息  start -->
                    <div class="col-12 ml10">
                        <div class="row">
                         <div class="col-9">
                             <div class="title_text">商品信息</div>
                        </div>
                        <div class="col-1 mt10">
                            <button type="button" class="btn btn-find waves-effect waves-light btn_default"  onclick='$m9015.init();'>商品导入</button>
                        </div>
                         <div class="col-1 mt10">
                          <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick="$erpPopup.orderGoods.init(2, null, null);">选择商品</button>
                        </div>
                         <div class="col-1 mt10">
                          <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="delete-list-button">删 除</button>
                        </div> 
                        </div>
                    </div>                       
                    <div class="form-row mt20 ml15 table-responsive">
                   <table id="table-list" class="table table-striped" style="width: 2000px;">
                   <thead>
                      <tr>
                         <th><input id="checkbox11" type="checkbox" name="all"></th>
                          <th>序号</th>
                         <th>商品编号</th>
                         <th>商品名称</th>
                         <th>商品货号</th>
                         <th>条码</th>
                         <th><i class="red">*</i>销售数量</th>
                         <th><i class="red">*</i>销售单价<br>(不含税)</th>
                         <th><i class="red" id="declarePriceI">*</i>申报单价(RMB)</th>
                         <th><i class="red">*</i>销售总金额<br>(不含税)</th>
                         <th><i class="red">*</i>销售总金额<br>(含税)</th>
                         <th><i class="red">*</i>税率</th>
                         <th>税额</th>
                         <th>品牌类型</th>
                         <th><i class="red">*</i>毛重</th>
                         <th><i class="red">*</i>净重</th>
                         <th>箱号</th>
                         <th>合同号</th>
                         <th>备注</th>
						 <th>托盘号</th>
                         <th>箱数</th>
                         <th>成分占比</th>
                      </tr>
                      </thead>
                      <tbody id="tbody">
                      <c:forEach items="${detail.itemList}" var="item" varStatus="status">
                    	<tr>
                    		<td><input type='checkbox' name='item-checkbox'>
                            <td><input type='text' name='seq' value="${item.seq == null ? status.index + 1 : item.seq}" style='width:70px;text-align:center;'/></td>
	                    	<input type='hidden' name='id' value='${item.id}'>
	                    	<input type='hidden' name='goodsId' value='${item.goodsId}'></td>
                            <td>${item.goodsCode}<input type="hidden" name="goodsCode" value="${item.goodsCode}"/></td>
                            <td>${item.goodsName}<input type="hidden" name="goodsName" value="${item.goodsName}"/></td>
                            <td>${item.goodsNo}<input type="hidden" name="goodsNo" value="${item.goodsNo}"/></td>
                            <td>${item.barcode}<input type="hidden" name="barcode" value="${item.barcode}"/></td>
                            <td><input type='text' name='num' style='width:70px;text-align:center;' class='goods-num' value="${item.num}"  onkeyup="value=value.replace(/[^\d]/g,'')"></td>
                            <td><input type='text' name='price' class='goods-price' style='width:100px;text-align:center;' value='<fmt:formatNumber value="${item.price}" pattern="#.########" minFractionDigits="8" />' ></td>
                            <td><input type='text' name='declarePrice' class='goods-declarePrice' style='width:100px;text-align:center;' value='<fmt:formatNumber value="${item.declarePrice}" pattern="#.#####" minFractionDigits="5" />'  ></td>
                            <td><input type='text' name='amount'  class='goods-amount'  style='width:100px;text-align:center;' value="<fmt:formatNumber value="${item.amount}" pattern="#.##" minFractionDigits="2" />" onkeyup="numFormat(this,2)"></td>
                            <td><input type='text' name='taxAmount'  class='goods-taxAmount'  style='width:100px;text-align:center;' value="<fmt:formatNumber value="${item.taxAmount}" pattern="#.##" minFractionDigits="2" />" onkeyup="numFormat(this,2)"></td>                             
                            <td>
                                <input type="hidden" name="goodsTaxRate" value="${item.taxRate}"/>
                                <select name="taxRate" class="edit_input" style="width: 80px;" onchange="calculateTaxAmount(this)"></select>
                            </td>	                             
                            <td><input type="text" name="tax" class="goods-tax" value="${item.tax}" onkeyup="numFormat(this,2)"/></td>
                            <td>${item.brandName}<input type="hidden" name="brandName" value="${item.brandName}"/></td>
                            <td>
                                <input type="text" class="form-control goods-rough" id="goods-rough" name="grossWeight" style='width:60px;text-align:center;'
                                       value="<fmt:formatNumber value="${item.grossWeightSum}" pattern="#.#####" minFractionDigits="5"/>"  onchange="calculateWeight5()"
                                       onkeyup="numFormat(this,5)" maxlength="11">
                                <input type="hidden" name="grossWeight-hidden" value="${item.grossWeight}">
                            </td>
                            <td>
                                <input type="text" class="form-control goods-suttle" onchange="suttleWeight5()" id="goods-suttle" name="netWeight" style='width:60px;text-align:center;'
                                       value="<fmt:formatNumber value="${item.netWeightSum}" pattern="#.#####" minFractionDigits="5"/>"
                                       onkeyup="numFormat(this,5)" maxlength="11">
                                <input type="hidden"  name="netWeight-hidden" value="${item.netWeight}">
                            </td>
                            <td><input type="text" name="boxNo" value="${item.boxNo}"/></td>
                            <td><input type="text" name="itemContractNo" value="${item.contractNo}"/></td>
                        	<td><input type="text" name="itemRemark" value="${item.remark}" style='width:70px;text-align:center;'/></td>
                        	<td><input type="text" name="torrNo" value="${item.torrNo}"/></td>
                            <td><input type="text" name="itemBoxNum" value="${item.boxNum}" onchange="calTotalBoxNum()"/></td>
                            <td><textarea name='component' class='goods-component' cols='30' rows='3' align='center'>${item.component }</textarea></td>
                          
                        </tr>
                      </c:forEach>
                      </tbody>
                  </table>
                   </div>
                    <div class="form-row m-t-20">
                        <div class="col-12">
                            <div class="row">
                               <div class="col-1">
                                </div>
                                <div class="col-9">
                                    <table>
                                        <tr>
                                            <td width="600px" align="center">总金额(不含税)：<span name="totalAmount" id="totalAmount"></span></td>
                                            <td width="100px"></td>
                                            <td width="300px" align="center">税额：<span name="totalTax" id="totalTax"></span></td>
                                            <td width="100px"></td>
                                            <td width="500px" align="center">总金额(含税)：<span name="totalTaxAmount" id="totalTaxAmount"></span></td>
                                            <td width="100px"></td>
                                            <td width="300px" align="center">总数量：<span name="totalNum" id="totalNum"></span></td>
                                            <td width="100px"></td>
                                            <td width="300px" align="center">总毛重：<span name="totalRough" id="totalRough"></span></td>
                                            <td width="100px"></td>
                                            <td width="300px" align="center">总净重：<span name="totalSuttle" id="totalSuttle"></span></td>
                                        </tr>
                                    </table>
                                </div>
                                <div class="col-2">
                                </div>
                            </div>
                        </div>
                    </div>
                 <!--   商品信息  end -->                 
                 <div class="edit_box" id="auditResultDiv" style="margin-top:50px">
						<div class="edit_item">
                   			<label class="edit_label">审核结果：</label>
							<label style="margin-right:150px"><input type="radio" name="auditResult" value="0" >通过</label>
							<label><input type="radio" name="auditResult" value="1" >驳回</label>
						</div>
						
					</div> 
                      <div class="form-row m-t-50">
                          <div class="col-12">
                              <div class="row">
                                  <div class="col-4">
                                  </div>
                                  <div class="col-4">
                                      <input type="button" class="btn btn-info waves-effect waves-light btn_default" id="save-temp-buttons" value="保 存"/>
                                       <shiro:hasPermission name="sale_submitSaleOrder">
                                      		<input type="button" class="btn btn-info waves-effect waves-light btn_default" id="submit-buttons" value="提交"/>
                                 	  </shiro:hasPermission>
                                      <shiro:hasPermission name="sale_addSaleOrder">
                                      		<input type="button" class="btn btn-info waves-effect waves-light btn_default" id="save-buttons" value="审核"/>
                                 	  </shiro:hasPermission>
                                      <button type="button" class="btn btn-light waves-effect waves-light btn_default" id="cancel-buttons">取 消</button>
                                  </div>
                                  <div class="col-4">
                                  </div>
                              </div>
                          </div>
                      </div>
                </div>
              </div>
             </div>
             </div>
                  <!-- end row -->
                  <!-- 弹窗开始 -->
                  <jsp:include page="/WEB-INF/views/modals/9011-V2.jsp" />
                  <jsp:include page="/WEB-INF/views/modals/9015.jsp" />
                  <jsp:include page="/WEB-INF/views/modals/9016.jsp" />
                  <!-- 弹窗结束 -->
                  <!-- end row -->
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->
<script type="text/javascript">

    var isInOutInstruction = false ;
    changeDeclareNecc($("#outDepotId").val()) ;
    var operatePage = '${operate}';//用于控制页面按钮显示
    var salePriceManage = ${salePriceManage};//是否启用销售价格管理
$(document).ready(function() {
	if(operatePage == "2"){//审核
		$("#submit-buttons").css("display","none");
		$("#save-buttons").css("display","inline-block");
		$("#auditResultDiv").css("display","block");
	}else{//提交
		$("#submit-buttons").css("display","inline-block");
		$("#save-buttons").css("display","none");
		$("#auditResultDiv").css("display","none");
	}
	
	if("${isSameAreaDisabled}"=="true"){
		$("#isSameArea").attr("disabled",true);
	}else if("${isSameAreaDisabled}"=="false"){
		$("#isSameArea").attr("disabled",false);
	}
	
	//税率下拉
    if("${detail.itemList}" != null && "${detail.itemList}" != ""){
   	 	var selectObj = $("#tbody").find("tr").find("select[name='taxRate']");
   	 	$(selectObj).each(function(){
   	 		$(this).empty();
   	 		var taxRate = $(this).prev('input[name="goodsTaxRate"]').val();
   		 	$(this).append(getTaxRateSelectHtml(taxRate));
   	 	});
    }
	if("${inDepotDisabled}"=="true"){
		$("#inDepotId").attr("disabled",true);
		$("#inDepotId_i").css("display","none");
		$("#fContractNo_i").css("display","none");
	}else if("${inDepotDisabled}"=="false"){
		$("#inDepotId").attr("disabled",false);
	}
	
	//当出库仓库类型为“中转仓”时，运输方式、承运商、托数、出库地点 箱数、托盘材质、包装、发票单号、卸货港非必填
	if("${outDepotType}" == "d"){
		$("#transport_i").css("display","none");
		$("#torusNumber_i").css("display","none");
		$("#carrier_i").css("display","none");
		$("#outdepotAddr_i").css("display","none");
		$("#boxNum_i").css("display","none");
 		$("#torrPackType_i").css("display","none");
 		$("#pack_i").css("display","none");
 		$("#invoiceNo_i").css("display","none");
 		$("#portDes_i").css("display","none");
	}else{
		$("#transport_i").css("display","inline-block");
		$("#torusNumber_i").css("display","inline-block");
		$("#carrier_i").css("display","inline-block");
		$("#outdepotAddr_i").css("display","inline-block");
		$("#boxNum_i").css("display","inline-block");
 		$("#torrPackType_i").css("display","inline-block");
 		$("#pack_i").css("display","inline-block");
 		$("#invoiceNo_i").css("display","inline-block");
 		$("#portDes_i").css("display","inline-block");
	}
	
	
	 var buId = "${buId}";
	 var inDepotId = "${inDepotId}";
	 var outDepotId = "${outDepotId}";
	 var businessModelVal=$("#businessModelVal").val();
	  
	 if($("#isSameArea").val()=="1"){
		$("#fContractNo_i").css("display","inline-block");
	 }else{
		$("#fContractNo_i").css("display","none");
	 }
	 inDepotChange(businessModelVal);
	 
	//加载事业部
     $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],buId,null,null,null);
     $module.constant.getConstantListByNameURL.call($('select[name="transport"]')[0],"transportList","${detail.transport}");//运输方式
     $module.constant.getConstantListByNameURL.call($('select[name="torrPackType"]')[0],"order_torrpacktypeList",'${detail.torrPackType}');
     $module.depot.getSelectBeanByMerchantBuRel.call($("select[name='outDepotId']")[0],"${detail.outDepotId}", {"buId": "${detail.buId}"});	
     var outCustomsId = "${detail.inDepotId}" == null ?'': "${detail.outCustomsId}";
     var inCustomsId = "${detail.outDepotId}" == null ?'': "${detail.inCustomsId}";
     $module.depot.getSelectBeanByDepotCustomsRel.call($("select[name='outCustomsId']")[0],"${detail.outDepotId}",outCustomsId);
     $module.depot.getSelectBeanByDepotCustomsRel.call($("select[name='inCustomsId']")[0],"${detail.inDepotId}",inCustomsId);
	// 当销售类型为采销执行时，出仓仓库可选仅为中转仓；
	if(businessModelVal == '3'){
		// 当销售类型为采销执行时，PO单号必填项
		$("#poNo_i").css("display","inline");		
	}else if(businessModelVal == '4'){	// 购销-实销实结
		$("#poNo_i").css("display","none");
	}else if(businessModelVal=='1'){	// 购销-整批结算
		$("#poNo_i").css("display","inline");
	}
	//保税仓，入库仓为备查库；海外仓，入库仓为备查库；中转仓，实销实结，入库仓为备查库
 	if("${outDepotType}" != null && ("${outDepotType}" == "a" || "${outDepotType}" == "c" )){ 		
 		$module.depot.getSelectBeanByMerchantRel.call($("select[name='inDepotId']")[0],"${detail.inDepotId}",{"type":"b"}); 			
 		
 	}else if("${outDepotType}" != null && "${outDepotType}" == "d" && businessModelVal == '4'){
 		//加载仓库的下拉数据
 		$module.depot.getSelectBeanByMerchantRel.call($("select[name='inDepotId']")[0],"${detail.inDepotId}",{"type":"b"});
 	}else if("${outDepotType}" != null && businessModelVal == '1'){//整批结算，入库仓为备查库
 		//加载仓库的下拉数据
 		$module.depot.getSelectBeanByMerchantRel.call($("select[name='inDepotId']")[0],"${detail.inDepotId}",{"type":"b"});
 	}else{
 		$module.depot.getSelectBeanByMerchantRel.call($("select[name='inDepotId']")[0],"${detail.inDepotId}",{});
 	}

	// 加载客户的下拉数据
	$module.customer.loadSelectData.call($('select[name="customerId"]')[0],'${detail.customerId}');
	//加载销售币种的下拉数据
	$module.currency.loadSelectData.call($("select[name='currency']")[0],"");
	//币种赋值
	var currency = $("#currency_hidden").val() ;
	if(currency){
		$("#currency").val(currency) ;
	}
	//根据客户选择带出对应的币种、销售类型
	$("#customerId").change(function(){
		var val = $(this).val() ; 
		if(val){
			$module.currency.loadSelectData.call($("select[name='currency']")[0],val);
		}else{
			$("#currency").val("") ;
		}
		salePriceManage = getSalePriceManage() ;
		var getRateurl = "/webapi/system/customer/getRateByCustomerAndMerchant.asyn" ;	
		var merchantId = $("#merchantId").val();
		var selectBusinessModel = $("#businessModel").val();
        //如果已经选中 销售类型 不带出客户配置的销售类型
        if(!selectBusinessModel){
        	$custom.request.ajaxSync(getRateurl, {"customerId": val,"merchantId": merchantId}, function(result){
    			if(result.code == '10000' && result.data){			
    				businessModel = result.data.businessModel ;	
    				$("#businessModel").val(businessModel);
    			}
    		});
        }
	}) ;
    //根据运输方式,校验必填项（标准箱TEU、车次）
    $("#transport").change(function(){
        var val = $(this).val() ;
        if(val=='2'){  //标准箱TEU:当运输方式为海运时必填
            $("#teu_i").show();
            $("#trainno_i").hide();
        }else if(val=='3' || val=='4'){  //车次:当运输方式为陆运、港到仓拖车时必填
            $("#trainno_i").show();
            $("#teu_i").hide();
        }else{
            $("#teu_i").hide();
            $("#trainno_i").hide();
        }
    });
	 	var count = 0;
		var flag = " ${empty itemCount}";  
		if(flag!="true"){  
			count = ${itemCount};
        }
    $(function(){
			//取消按钮
			$("#cancel-buttons").click(function(){
                var pageSource = '${pageSource}';
                if (pageSource == "1") {
                    $load.a("/list/menu.asyn?act=100&r="+Math.random());
                } else {
                    $module.load.pageOrder('act=4001');
                }
			});
		});
	// 当购销时，并且PO号填完，失焦时
	var orderId=$("#orderId").val();
	$("#poNo").blur(function(){
		var businessModel = $("#businessModel").val();
		var poNoVal = $("#poNo").val();
        // 若PO不为空
		if(poNoVal){
            var orderIdValue = $("#orderId").val();
            $custom.request.ajaxSync(serverAddr+"/sale/checkExistByPo.asyn", {"poNo":poNoVal,"orderId":orderIdValue}, function(data){
                if(data.state==200){
                    if(data.data.length>0){
                        $custom.alert.error("PO号:"+data.data+"已有存在销售订单信息");
                    }
                }
            });
            var results	= poNoVal.split("&");
            var result = results.sort();
            for(var i=0;i<result.length-1;i++){
                if(result[i]==result[i+1]){	// PO号重复了
                    $custom.alert.error("PO号重复了");
                }
            }
        }else{
            if(businessModel=="1" || businessModel=="3"){
                $custom.alert.error("销售类型为购销-整批结算或采销执行时，PO号不能为空");
            }
        }
	});
	//保存按钮
	$("#save-temp-buttons").click(function(){
		var url = serverAddr+"/sale/saveOrModifyTempOrder.asyn";
		var form = {
				
		};
		var json = {};
		var orderId = $("#orderId").val();
		var orderCode = $("#orderCode").val();
		var customerId = $("#customerId").val();

		var merchantId = $("#merchantId").val();
		var merchantName = $("#merchantName").val();

		var outDepotId = $("#outDepotId").val();

		
		// 出库仓库是海外仓 必填字段校验
		var destination=$("#destination").val();// 目的地海外仓必填
		var country=$("#country").val();
		var receiverName=$("#receiverName").val();
		var receiverAddress=$("#receiverAddress").val();	
		var mailMode=$("#mailMode").val();//邮寄方式 1.寄售 2.自提  海外仓必填
		var outDepotType = $("#outDepotId").find("option:selected").attr("type");
		
		
		var lbxNo = $("#lbxNo").val();
		var poNo = $("#poNo").val();
		var businessModel = $("#businessModel").val();

		// 当销售类型为购销-整批结算时，PO单号必填项
		if(poNo!=null && poNo.indexOf("&")!=-1){
            var results	= poNo.split("&");
            var result = results.sort();
            for(var i=0;i<result.length-1;i++){
                 if(result[i]==result[i+1]){	// PO号重复了
                      $custom.alert.error("新增失败，PO号重复了");
                      return;
                  }
                }
		}
        var referToOrder = $("#referToOrder").val();
		var inDepotId = $("#inDepotId").val();
		var isSameArea = $("#isSameArea").val();
		var deliveryDate = $("#deliveryDate").val();
		var fContractNo = $("#fContractNo").val();
		var tallyingUnit = $("#tallyingUnit").val();
		var remark = $("#remark").val();
		var currency1 = $("#currency").val();
		var buId = $("#buId").val();
        if(!buId){
            $custom.alert.error("事业部不能为空");
            return;
        }
        var payRules = $("#payRules").val();
        
        var outdepotAddr = $("#outdepotAddr").val();//出库地点
        var billWeight = $("#billWeight").val();//提单毛重
        var transport = $("#transport").val();//运输方式
        var teu = $("#teu").val();//标准箱TEU
        var trainno = $("#trainno").val();//车次
        var torusNumber = $("#torusNumber").val();//托数
        if(torusNumber && isNaN(torusNumber)){
            $custom.alert.error("托数请输入数值！");
            return ;
        }
        var carrier = $("#carrier").val();//承运商
        var boxNum = $("#boxNum").val();//箱数
        if(boxNum && isNaN(boxNum)){
            $custom.alert.error("箱数请输入数值！");
            return ;
        }
 		var torrPackType = $("#torrPackType").val();//托盘材质
 		var pack = $("#pack").val();//包装
 		var invoiceNo = $("#invoiceNo").val();//发票单据
 		var portDes = $("#portDes").val();//卸货港
 		var outCustomsId = $("#outCustomsId").val();//出仓关区
 		var inCustomsId = $("#inCustomsId").val();//入仓关区 		
 		var seq = $("#seq").val();  // 序号
		var itemList = [];
		var flag = true;
		$('#table-list tbody tr').each(function(i){// 遍历 tr
			var item ={};
			var id = $(this).find('input[name="id"]').val();

            var seq= $(this).find('input[name="seq"]').val();
            item['seq']=seq;

			item['id']=id;
			var goodsId = $(this).find('input[name="goodsId"]').val();
			item['goodsId']=goodsId;
			var goodsCode = $(this).find('input[name="goodsCode"]').val();
			item['goodsCode']=goodsCode;
			var goodsName = $(this).find('input[name="goodsName"]').val();
			item['goodsName']=goodsName;
			var goodsNo = $(this).find('input[name="goodsNo"]').val();
			item['goodsNo']=goodsNo;
			var unit = $(this).find('input[name="tallyingUnit"]').val();
			item['unit']=unit;
			var unitName = $(this).find('input[name="unitName"]').val();
			item['unitName']=unitName;
			var barcode = $(this).find('input[name="barcode"]').val();
			item['barcode']=barcode;
			var num = $(this).find('input[name="num"]').val();
			item['num']=num;
			var declarePrice = $(this).find('input[name="declarePrice"]').val();//申报单价
            item['declarePrice']=declarePrice;
			var amount = $(this).find('input[name="amount"]').val();
			item['amount']=amount;
			var brandName = $(this).find('input[name="brandName"]').val();
			item['brandName']=brandName;
			var boxNo = $(this).find('input[name="boxNo"]').val();
			item['boxNo']=boxNo;
			var itemContractNo = $(this).find('input[name="itemContractNo"]').val();
			item['contractNo']=itemContractNo;
			var itemRemark= $(this).find('input[name="itemRemark"]').val();
			item['remark']=itemRemark;
            var price = $(this).find('input[name="price"]').val();
            if(parseFloat(amount) == 0) {
                price = 0.0+"";
            }
            item['price']=price;

			if(!!tallyingUnit){
				item['tallyingUnit']=tallyingUnit;
			}
            var grossWeight = $(this).find('input[name="grossWeight-hidden"]').val();//毛重
            item['grossWeight'] = grossWeight;
            var grossWeightSum = $(this).find('input[name="grossWeight"]').val();//总毛重
            item['grossWeightSum'] = grossWeightSum;
            var netWeight = $(this).find('input[name="netWeight-hidden"]').val();//净重
            item['netWeight'] = netWeight;
            var netWeightSum = $(this).find('input[name="netWeight"]').val();//总净重
            item['netWeightSum'] = netWeightSum;
            var torrNo = $(this).find('input[name="torrNo"]').val();//托盘号
            item['torrNo'] = torrNo;
            var itemBoxNum = $(this).find('input[name="itemBoxNum"]').val();//箱数
            if(itemBoxNum && isNaN(itemBoxNum)){
                $custom.alert.error("商品箱数请输入数值！");
                return ;
            }
            item['boxNum'] = itemBoxNum;
            var component = $(this).find('textarea[name="component"]').val();//成分占比
            item['component'] = component;
            var taxAmount = $(this).find('input[name="taxAmount"]').val();//销售总金额（含税）
            item['taxAmount']=taxAmount;
			var taxRate = $(this).find('select[name="taxRate"]').find("option:selected").text();//税率
			item['taxRate']=taxRate;
			var tax = $(this).find('input[name="tax"]').val();//税额
			item['tax']=tax;
            
			if(!num || isNaN(num) ||num<=0){
				$custom.alert.error("商品数量必须是大于0的数");
	       		flag = false;
	    		return;
			}
			var reg = /^\d+$/;
			if (!reg.test(num)||num<0) {
	       		$custom.alert.error("商品数量只能输入正整数");
	    		return;
	       	}
			if(!price || price < 0){
				$custom.alert.error("商品单价不能为空或小于0");
	       		flag = false;
	    		return;
			}
	    	var reg1 = /^\d+(\.\d+)?$/;
	       	if (!reg1.test(price)) {
	       		$custom.alert.error("商品单价只能输入数字");
	       		flag = false;
	    		return;
	       	}
	       	if(price.length>15){
	       		$custom.alert.error("商品单价长度只能输入15位（包含小数点）");
	       		flag = false;
	    		return;
	       	}
            if(isInOutInstruction){
                if(!declarePrice || declarePrice==0){
                    $custom.alert.error("出仓仓库进出接口指令为是,申报单价必填");
                    flag = false ;
                    return;
                }
            }
            if(declarePrice){
                if (!reg1.test(declarePrice)) {
                    $custom.alert.error("申报单价只能输入数字");
                    flag = false;
                    return;
                }
            }
			if(!amount ||amount < 0 || isNaN(amount)){
				$custom.alert.error("总金额不能为空或小于0");
	       		flag = false;
	    		return;
			}else{
                var indexOf = amount.indexOf(".");
                // 如果是小数
                if (indexOf != -1) {
                    var count = (parseFloat(amount)+"").length - 1 - indexOf;
                    if (count > 2) {
                        $custom.alert.error("总金额只能为两位小数");
                        flag = false;
                        return;
                    }
                }
            }
	       	if(amount.length>21){
	       		$custom.alert.error("总金额长度只能输入21位（包含小数点）");
	       		flag = false;
	    		return;
	       	}
            if (parseFloat(grossWeightSum) < parseFloat(netWeightSum)) {
                $custom.alert.error("毛重不能小于净重，商品货号:" + goodsNo);
                flag = false;
                return;
            }
            if(!taxAmount || taxAmount < 0 || isNaN(taxAmount)){
				$custom.alert.error("销售金额（含税）不能为空或小于0");
	       		flag = false;
	    		return;
			}else{
                var indexOf = taxAmount.indexOf(".");
                // 如果是小数
                if (indexOf != -1) {
                    var count = (parseFloat(taxAmount)+"").length - 1 - indexOf;
                    if (count > 2) {
                        $custom.alert.error("销售金额（含税）只能为两位小数");
                        flag = false;
                        return;
                    }
                }
            }
            if(!taxRate || isNaN(taxRate)){
				$custom.alert.error("税率不能为空");
	       		flag = false;
	    		return;
			}
        	if(taxRate){
                if (!reg1.test(taxRate)) {
                    $custom.alert.error("税率只能输入数字");
                    flag = false;
                    return;
                }
            }
            if(!tax || tax < 0 || isNaN(tax)){
				$custom.alert.error("税额不能为空或小于0");
	       		flag = false;
	    		return;
			}
        	if(tax){
                if (!reg1.test(tax)) {
                    $custom.alert.error("税额只能输入数字");
                    flag = false;
                    return;
                }
            }
			itemList.push(item);
		});
		if(!flag){
			return;
		}
        json['outdepotAddr']=outdepotAddr;
        json['billWeight']=billWeight;
        json['transport']=transport;
        json['teu']=teu;
        json['trainno']=trainno;
        json['torusNumber']=torusNumber;
        json['carrier']=carrier;
        json['boxNum']=boxNum;
        json['torrPackType']=torrPackType;
        json['pack']=pack;
        json['invoiceNo']=invoiceNo;
        json['portDes']=portDes;
        json['referToOrder']=referToOrder;
		json['orderId']=orderId;
		json['orderCode']=orderCode;
		json['customerId']=customerId;
		json['merchantId']=merchantId;
		json['merchantName']=merchantName;
		json['outDepotId']=outDepotId;
		json['inDepotId']=inDepotId;
		json['isSameArea']=isSameArea;
		json['lbxNo']=lbxNo;
		json['poNo']=poNo;
		json['businessModel']=businessModel;
		json['deliveryDate']=deliveryDate;
		json['contractNo']=fContractNo;
		if(!!tallyingUnit){
			json['tallyingUnit']=tallyingUnit;
		}
		json['remark']=remark;
		json['currency']=currency1;
		json['buId']=buId;
		json['destination']=destination;
		json['country']=country;
		json['receiverName']=receiverName;
		json['receiverAddress']=receiverAddress;
		json['mailMode']=mailMode;
		json['destination']=destination;
		json['country']=country;
		json['receiverName']=receiverName;
		json['receiverAddress']=receiverAddress;
		json['mailMode']=mailMode;			
		json['itemList']=itemList;
        json['preSaleOrderRelCode']="";// 关联的预售单号默认为空
        json['operate']="0";//操作 0-保存 1-提交 2-审核
        json['orderType']="2";//非预售转销
        json['outCustomsId']=outCustomsId;
		json['inCustomsId']=inCustomsId;
		json['payRules']=payRules;//付款条约
		var jsonStr= JSON.stringify(json);
		form['json']=jsonStr;
        // if (flag2){
        //     $custom.alert.warning("存在排序序号为空的商品，是否按现有排序提交？",function () {
        //         $custom.request.ajax(url, form, function(result){
        //             if(result.state == '200'){
        //                 $custom.alert.success("编辑成功！");
        //                 setTimeout(function () {
        //                     $load.a(pageUrl+"4001");
        //                 }, 1000);
        //             }else{
        //                 if(!!result.expMessage){
        //                     $custom.alert.error(result.expMessage);
        //                 }else{
        //                     $custom.alert.error(result.message);
        //                 }
        //             }
        //         });
        //     })
        // }else {
            $custom.request.ajax(url, form, function(result){
                if(result.state == '200'){
                    $custom.alert.success("编辑成功！");
                    setTimeout(function () {
                        $load.a(pageUrl+"4001");
                    }, 1000);
                }else{
                    if(!!result.expMessage){
                        $custom.alert.error(result.expMessage);
                    }else{
                        $custom.alert.error(result.message);
                    }
                }
            });
        // }
	});
	//提交
	$("#submit-buttons").click(function(){
        var isContinueFlag=0;
        var businessModel = $("#businessModel").val();
        //必填项校验
        if(!$checker.verificationRequired()){
            return ;
        }
        var poNo = $("#poNo").val();
        if(poNo.indexOf("&")!=-1){
            var results	= poNo.split("&");
            var result = results.sort();
            for(var i=0;i<result.length-1;i++){
                if(result[i]==result[i+1]){	// PO号重复了
                    $custom.alert.error("提交失败，PO号重复了");
                    return;
                }
            }
        }

        if(poNo){
            var orderIdValue = $("#orderId").val();
            $custom.request.ajaxSync(serverAddr+"/sale/checkExistByPo.asyn", {"poNo":poNo,"orderId":orderIdValue}, function(data){
                if(data.state==200){
                    if(data.data.length>0){
                        $custom.alert.warning("PO号:"+data.data+"已有存在销售订单信息",function(){
                            saveData(poNo,businessModel,'1');
                        });
                    }else{
                        isContinueFlag = 1;
                    }
                }
            });
        }else{// 其他类型的po号可以为空，直接走审核
            isContinueFlag = 1;
        }
        if(isContinueFlag == 1){
            saveData(poNo,businessModel,'1');
        }
    });
    $("#save-buttons").click(function(){
        var isContinueFlag=0;
        var businessModel = $("#businessModel").val();
        //必填项校验
        if(!$checker.verificationRequired()){
            return ;
        }
        var poNo = $("#poNo").val();
        // 当销售类型为购销-整批结算时，PO单号必填项
        if(businessModel=="1") {
            if(!poNo){
                $custom.alert.error("审核失败，销售类型为购销-整批结算，PO单号不能为空");
                return;
            }
        }
        if(poNo.indexOf("&")!=-1){
            var results	= poNo.split("&");
            var result = results.sort();
            for(var i=0;i<result.length-1;i++){
                if(result[i]==result[i+1]){	// PO号重复了
                    $custom.alert.error("审核失败，PO号重复了");
                    return;
                }
            }
        }

        if(poNo){
            var orderIdValue = $("#orderId").val();
            $custom.request.ajaxSync(serverAddr+"/sale/checkExistByPo.asyn", {"poNo":poNo,"orderId":orderIdValue}, function(data){
                if(data.state==200){
                    if(data.data.length>0){
                        $custom.alert.warning("PO号:"+data.data+"已有存在销售订单信息",function(){
                            saveData(poNo,businessModel,'2');
                        });
                    }else{
                        isContinueFlag = 1;
                    }
                }
            });
        }else{// 其他类型的po号可以为空，直接走审核
            isContinueFlag = 1;
        }
        if(isContinueFlag == 1){
            saveData(poNo,businessModel,'2');
        }
    });
	
    //提交或审核按钮 operate:1-提交 2-审核
    function saveData(poNo,businessModel,operate){
        var url = "";
        var tags ="";
        if(operate == '2'){               
            url = serverAddr+"/sale/modifySaleOrder.asyn";//审核
            tags = "确定审核该销售订单？";
        }else if(operate == '1'){
	    	url = serverAddr+"/sale/submitSaleOrder.asyn";//提交
	    	tags = "确定提交该销售订单？";
   		}
        $custom.alert.warning(tags,function(){
			var form = {};
			var json = {};
			var orderId = $("#orderId").val();
			var orderCode = $("#orderCode").val();
			var customerId = $("#customerId").val();
			var merchantId = $("#merchantId").val();
			var merchantName = $("#merchantName").val();
			var outDepotId = $("#outDepotId").val();
			var a =$("#outDepotId").find("option:selected").text();
			// 出库仓库是海外仓 必填字段校验
			var destination = $("#destination").val();// 目的地海外仓必填
			var country = $("#country").val();
			var receiverName = $("#receiverName").val();
			var receiverAddress = $("#receiverAddress").val();
			var mailMode = $("#mailMode").val();//邮寄方式 1.寄售 2.自提  海外仓必填
			var outDepotType = $("#outDepotId").find("option:selected").attr("type");
			var lbxNo = $("#lbxNo").val();
			var referToOrder = $("#referToOrder").val();
			var inDepotId = $("#inDepotId").val();
			var isSameArea = $("#isSameArea").val();
			var deliveryDate = $("#deliveryDate").val();
			var fContractNo = $("#fContractNo").val();
			var tallyingUnit = $("#tallyingUnit").val();
			var remark = $("#remark").val();
			var currency1 = $("#currency").val();
			var buId = $("#buId").val();	// 事业部
			var payRules = $("#payRules").val();
	        if(!payRules){
	       	 $custom.alert.error("付款条约不能为空！");
	            return ;
	        }
			var outdepotAddr = $("#outdepotAddr").val();//出库地点
            var billWeight = $("#billWeight").val();//提单毛重
            var transport = $("#transport").val();//运输方式
            var teu = $("#teu").val();//标准箱TEU
            var trainno = $("#trainno").val();//车次
            var torusNumber = $("#torusNumber").val();//托数
            if(torusNumber && isNaN(torusNumber)){
                $custom.alert.error("托数请输入数值！");
                return ;
            }
            var carrier = $("#carrier").val();//承运商
            var boxNum = $("#boxNum").val();//箱数
            if(boxNum && isNaN(boxNum)){
                $custom.alert.error("箱数请输入数值！");
                return ;
            }
     		var torrPackType = $("#torrPackType").val();//托盘材质
     		var pack = $("#pack").val();//包装
     		var invoiceNo = $("#invoiceNo").val();//发票单据
     		var portDes = $("#portDes").val();//卸货港
     		var outCustomsId = $("#outCustomsId").val();//出仓关区
     		var inCustomsId = $("#inCustomsId").val();//入仓关区
     		
            var auditResult = $("input[name='auditResult']:checked").val() ;//审核结果 0-通过；1-驳回
			if(operate == '2' && auditResult == '0'){
			   $custom.request.ajaxSync("/depot/getDepotDetails.asyn", {"id":outDepotId}, function(result){
	                if(result.data.type=='c') {	// 当出仓仓库为海外仓时;
	                    if (!destination) {
	                        $custom.alert.error("出库仓时海外仓目的地不能为空");
	                        return;
	                    }
	                    if (!mailMode) {
	                        $custom.alert.error("出库仓时海外仓邮寄方式不能为空");
	                        return;
	                    }
	                    if (!receiverName) {
	                        $custom.alert.error("出库仓时海外仓收件人姓名不能为空");
	                        return;
	                    }
	                    if (!country) {
	                        $custom.alert.error("出库仓时海外仓国家不能为空");
	                        return;
	                    }
	                    if (!receiverAddress) {
	                        $custom.alert.error("出库仓时海外仓详细地址不能为空");
	                        return;
	                    }
	                }
	            });
	            // 合同号:当“是否同关区”为是时，为必填项；其他情况为非必填；
				if(isSameArea=="1"){
					if (!fContractNo) {
						$custom.alert.error("同关区时合同号不能为空");
						return;
					}
				}
				// 标准箱TEU,当运输方式为海运时必填
                if(transport=='2' && !teu){
                    $custom.alert.error("标准箱TEU不能为空");
                    return;
                }else if(transport=='3' || transport=='4'){  //当运输方式为陆运、港到仓拖车时必填
                    if(!trainno){
                        $custom.alert.error("车次不能为空");
                        return;
                    }
                }
			}
                
                
			var itemList = [];
			var flag = true;
			var flag2 = false;
			var checkInventoryGoods = [];
			var goodsArr = [];
			$('#table-list tbody tr').each(function(i){// 遍历 tr
				var item ={};
                var seq= $(this).find('input[name="seq"]').val();
                item['seq']=seq;
				var id = $(this).find('input[name="id"]').val();
				item['id']=id;
				var goodsId = $(this).find('input[name="goodsId"]').val();
				item['goodsId']=goodsId;
				var goodsCode = $(this).find('input[name="goodsCode"]').val();
				item['goodsCode']=goodsCode;
				var goodsName = $(this).find('input[name="goodsName"]').val();
				item['goodsName']=goodsName;
				var goodsNo = $(this).find('input[name="goodsNo"]').val();
				item['goodsNo']=goodsNo;
// 				var tallyingUnit = $("#tallyingUnit").val();
// 				item['tallyingUnit']=tallyingUnit;
				var unit = $(this).find('input[name="unit"]').val();
				item['unit']=unit;
				var unitName = $(this).find('input[name="unitName"]').val();
				item['unitName']=unitName;
				var barcode = $(this).find('input[name="barcode"]').val();
				item['barcode']=barcode;
				var num = $(this).find('input[name="num"]').val();
				item['num']=num;
				var declarePrice = $(this).find('input[name="declarePrice"]').val();//申报单价
                item['declarePrice']=declarePrice;
				var amount = $(this).find('input[name="amount"]').val();
				item['amount']=amount;
				var brandName = $(this).find('input[name="brandName"]').val();
				item['brandName']=brandName;
				var boxNo = $(this).find('input[name="boxNo"]').val();
				item['boxNo']=boxNo;
				var itemContractNo = $(this).find('input[name="itemContractNo"]').val();
				item['contractNo']=itemContractNo;
				var itemRemark= $(this).find('input[name="itemRemark"]').val();
				item['remark']=itemRemark;
                var price = $(this).find('input[name="price"]').val();
                if(parseFloat(amount) == 0) {
                    price = 0.0+"";
                }
                item['price']=price;
				if(!!tallyingUnit){
					item['tallyingUnit']=tallyingUnit;
				}
                var grossWeight = $(this).find('input[name="grossWeight-hidden"]').val();//毛重
                item['grossWeight'] = grossWeight;
                var grossWeightSum = $(this).find('input[name="grossWeight"]').val();//总毛重
                item['grossWeightSum'] = grossWeightSum;
                var netWeight = $(this).find('input[name="netWeight-hidden"]').val();//净重
                item['netWeight'] = netWeight;
                var netWeightSum = $(this).find('input[name="netWeight"]').val();//总净重
                item['netWeightSum'] = netWeightSum;
                var torrNo = $(this).find('input[name="torrNo"]').val();//托盘号
                item['torrNo'] = torrNo;
                var itemBoxNum = $(this).find('input[name="itemBoxNum"]').val();//箱数
                if(itemBoxNum && isNaN(itemBoxNum)){
                    $custom.alert.error("商品箱数请输入数值！");
                    return ;
                }
                item['boxNum'] = itemBoxNum;                
                var component = $(this).find('textarea[name="component"]').val();//成分占比
                item['component'] = component;
                var taxAmount = $(this).find('input[name="taxAmount"]').val();//销售总金额（含税）
                item['taxAmount']=taxAmount;
    			var taxRate = $(this).find('select[name="taxRate"]').find("option:selected").text();//税率
    			item['taxRate']=taxRate;
    			var tax = $(this).find('input[name="tax"]').val();//税额
    			item['tax']=tax;
                
				if(!num || isNaN(num) ||num<=0){
					$custom.alert.error("商品数量必须是大于0的数");
		       		flag = false;
		    		return;
				}
				var reg = /^\d+$/;
				if (!reg.test(num)||num<0) {
		       		$custom.alert.error("只能输入正整数");
		    		return;
		       	}
                if(isInOutInstruction){
                    if(!declarePrice || declarePrice==0){
                        $custom.alert.error("出仓仓库进出接口指令为是,申报单价必填");
                        flag = false ;
                        return;
                    }
                }
                var reg1 = /^\d+(\.\d+)?$/;
                if(declarePrice){
                    if (!reg1.test(declarePrice)) {
                        $custom.alert.error("申报单价只能输入数字");
                        flag = false;
                        return;
                    }
                }
                if(!price || price < 0){
                    $custom.alert.error("商品单价不能为空或小于0");
                    flag = false;
                    return;
                }
                if (!reg1.test(price)) {
                    $custom.alert.error("单价只能输入数字");
                    flag = false;
                    return;
                }

				if(!amount || amount < 0 || isNaN(amount)){
					$custom.alert.error("总金额不能为空或小于0");
		       		flag = false;
		    		return;
				}else{
                    var indexOf = amount.indexOf(".");
                    // 如果是小数
                    if (indexOf != -1) {
                        var count = (parseFloat(amount)+"").length - 1 - indexOf;
                        if (count > 2) {
                            $custom.alert.error("总金额只能为两位小数");
                            flag = false;
                            return;
                        }
                    }
                }
		       	if(amount.length>21){
		       		$custom.alert.error("总金额长度只能输入21位（包含小数点）");
		       		flag = false;
		    		return;
		       	}
                if (parseFloat(grossWeightSum) < parseFloat(netWeightSum)) {
                    $custom.alert.error("毛重不能小于净重，商品货号:" + goodsNo);
                    flag = false;
                    return;
                }
                if(!taxAmount || taxAmount < 0 || isNaN(taxAmount)){
     				$custom.alert.error("销售金额（含税）不能为空或小于0");
     	       		flag = false;
     	    		return;
     			}else{
                     var indexOf = taxAmount.indexOf(".");
                     // 如果是小数
                     if (indexOf != -1) {
                         var count = (parseFloat(taxAmount)+"").length - 1 - indexOf;
                         if (count > 2) {
                             $custom.alert.error("销售金额（含税）只能为两位小数");
                             flag = false;
                             return;
                         }
                     }
                 }
                 if(!taxRate || isNaN(taxRate)){
     				$custom.alert.error("税率不能为空");
     	       		flag = false;
     	    		return;
     			}
             	if(taxRate){
                     if (!reg1.test(taxRate)) {
                         $custom.alert.error("税率只能输入数字");
                         flag = false;
                         return;
                     }
                 }
                 if(!tax || tax < 0 || isNaN(tax)){
     				$custom.alert.error("税额不能为空或小于0");
     	       		flag = false;
     	    		return;
     			}
             	if(tax){
                     if (!reg1.test(tax)) {
                         $custom.alert.error("税额只能输入数字");
                         flag = false;
                         return;
                     }
                 }
		       	if(isExist(goodsArr, goodsId)) {
		       		var newArr = checkInventoryGoods.filter(function(p){
		       		  return p.goodsId === goodsId;
		       		});
		       		newArr[0].num = parseFloat(newArr[0].num) +parseFloat(num);
		       		checkInventoryGoods.push({"goodsId":newArr[0].goodsId, "goodsNo":newArr[0].goodsNo, "num": newArr[0].num });
		       	} else {
		       		goodsArr.push(goodsId);
		       	 	checkInventoryGoods.push({"goodsId":goodsId, "goodsNo":goodsNo, "num":num});
		       	}

                if (seq.trim().length == 0 || seq == null){
                    flag2 = true;
                }

				itemList.push(item);
			});
			if(!flag){
				return;
			}
			if(!itemList || itemList.length==0){
				$custom.alert.error("至少选择一个商品");
				return;
			}
           // 审核销售订单时，校验出库仓若为“中转仓”，则仅校验必填项，不校验库存可用量，不做库存冻结；
           // 查询仓库信息
           if( operate == '2' && auditResult == '0'){
	          var mergeJson = {} ;
	          $custom.request.ajaxSync("/depot/getDepotDetails.asyn", {"id":outDepotId}, function(result){
	             if(result.data.type!='d'){
	                 //合并相同项
	                 $.each(checkInventoryGoods, function (index, value) {
	                     var originalGoodsId = $module.inventoryLocationMapping.getOriginalGoodsId(value.goodsId, value.goodsNo) ;
	
	                     if (originalGoodsId) {
	                         if (!mergeJson[originalGoodsId]) {
	                             mergeJson[originalGoodsId] = value;
	                         } else {
	                        	 var num = mergeJson[originalGoodsId].num;
	                             value.num = parseFloat(num) + parseFloat(value.num);
	                             mergeJson[originalGoodsId] = value;
	                         }
	                     } else {
	                    	 mergeJson[value.goodsId] = value;
	                     }
	                 });
	
				  //查询可用量
		            $.each(mergeJson, function (index, value) {
		                var jsonDepot = $module.depot.getDepotById(outDepotId);
		                var tallyingUnitVal='';
		    	       	if(jsonDepot.type=="c"){
		    	       		if ('00'==tallyingUnit) {
		    	       			tallyingUnitVal='0';
		    				}else if ('01'==tallyingUnit) {
		    					tallyingUnitVal='1';
		    				}else if ('02'==tallyingUnit) {
		    					tallyingUnitVal='2';
		    				}
		    	       	}
		                var availableNum =$module.inventory.queryAvailability(outDepotId,index,jsonDepot.code,tallyingUnitVal,null,null,null);
		                var tempGoodsNo = $module.inventoryLocationMapping.getOriginalGoodsNo(index, value.goodsNo) ;
		
		                if(availableNum ==-1){
		                	flag = false;
		                    if(tempGoodsNo){
		                        $custom.alert.warningText("原货号：" + tempGoodsNo + " 商品货号："+value.goodsNo+",未查到库存可用量");
		                    }else{
		                        $custom.alert.warningText("未查到库存余量货号："+value.goodsNo+",仓库:"+jsonDepot.name);
		                    }
		                    return;
		                }else if(value.num>availableNum){
		                	flag = false;
		                    if(tempGoodsNo){
		                        $custom.alert.warningText("库存不足，"+"原货号：" + tempGoodsNo + " 商品货号："+value.goodsNo+",余量："+availableNum);
		                    }else{
		                        $custom.alert.warningText("库存不足,货号："+value.goodsNo+",仓库:"+jsonDepot.name+",余量："+availableNum);
		                    }
		                    return;
		                }
		            });
		         }
		    });        	   
        }
       	if(!flag){
			return false;
		}
        json['outdepotAddr']=outdepotAddr;
        json['billWeight']=billWeight;
        json['transport']=transport;
        json['teu']=teu;
        json['trainno']=trainno;
        json['torusNumber']=torusNumber;
        json['carrier']=carrier;
        json['boxNum']=boxNum;
        json['torrPackType']=torrPackType;
        json['pack']=pack;
        json['invoiceNo']=invoiceNo;
        json['portDes']=portDes;
       	json['referToOrder']=referToOrder;
		json['orderId']=orderId;
		json['orderCode']=orderCode;
		json['customerId']=customerId;
		json['merchantId']=merchantId;
		json['merchantName']=merchantName;
		json['outDepotId']=outDepotId;
		json['inDepotId']=inDepotId;
		json['isSameArea']=isSameArea;
		json['lbxNo']=lbxNo;
		json['poNo']=poNo;
		json['businessModel']=businessModel;
		json['deliveryDate']=deliveryDate;
		json['contractNo']=fContractNo;
		if(!!tallyingUnit){
			json['tallyingUnit']=tallyingUnit;
		}
		json['remark']=remark;
		json['currency']=currency1;
		json['buId']=buId;
		json['destination']=destination;
		json['country']=country;
		json['receiverName']=receiverName;
		json['receiverAddress']=receiverAddress;
		json['mailMode']=mailMode;
		json['destination']=destination;
		json['country']=country;
		json['receiverName']=receiverName;
		json['receiverAddress']=receiverAddress;
		json['mailMode']=mailMode;
        json['preSaleOrderRelCode']="";// 关联的预售单号默认为空
        json['orderType']="2";//非预售转销 
        json['auditResult']=auditResult; //审核结果 0-通过 1-驳回
        json['operate']=operate;//操作 1-提交 2-审核
        json['operate']=operate;
        json['outCustomsId']=outCustomsId;
		json['inCustomsId']=inCustomsId;
		json['itemList']=itemList;
		json['payRules']=payRules;//付款条约
		// json['seq'] = seq;
		var jsonStr= JSON.stringify(json);
		form['json']=jsonStr;

		if (flag2){
		    $custom.alert.warning("存在排序序号为空的商品，是否按现有排序提交？",function () {
                   // 已经校验库存余量，接下来进行保存并审核
                   $custom.request.ajax(url, form, function(data){
                       if(data.state==200){
                    	   if(operate == '2'){
		                       	var str = data.data;
		                        checkPurchase(str);                    		   
                    	   }else{
                    		   $custom.alert.success("提交成功！");
	   							setTimeout(function () {
	   								$load.a(pageUrl+"4001");
	   							});
                    	   }
                       }else{
                           if(!!data.expMessage){
                               $custom.alert.error(data.expMessage);
                           }else{
                               $custom.alert.error(data.message);
                           }
                       }
                   });
               })
           }else {
               // 已经校验库存余量，接下来进行保存并审核
               $custom.request.ajax(url, form, function(data){
                   if(data.state==200){
                	   if(operate == '2'){
	                       	var str = data.data;
	                        checkPurchase(str);                    		   
	               	   }else{
	               		   $custom.alert.success("提交成功！");
	  							setTimeout(function () {
	  								$load.a(pageUrl+"4001");
	  							});
	               	   }
                   }else{
                       if(!!data.expMessage){
                           $custom.alert.error(data.expMessage);
                       }else{
                           $custom.alert.error(data.message);
                       }
                   }
               });
           }
	 	});
}
		
		//删除选中行
	 	$("#delete-list-button").click(function() {
	 		var goodsId = [];
	        $("input[name='item-checkbox']:checked").each(function() { // 遍历选中的checkbox
	            n = $(this).parents("tr").index()+1;  // 获取checkbox所在行的顺序
	            goodsId.push($(this).next().val());
	            $("#table-list").find("tr:eq("+n+")").remove();
	        });
	        var unNeedIds = $("#unNeedIds").val();
	        var temp = unNeedIds.split(",");
	        var newUnNeedIds = delArrElement(goodsId,temp);
	        $("#unNeedIds").val(newUnNeedIds);
	        $("input[name='all']").prop("checked",false);
            goods_change();
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
	
		//监听采购数量的离开事件
 		$("#table-list").on("blur",'.goods-num',function(){
			var that = $(this);
	    	//获取采购数量
	    	var num = that.val();
	    	//判断是否数字
	    	if(isNaN(num) || !num){
	    		$(this).val(0);
	    		$custom.alert.error("销售数量请输入数字");
	    		return ;
	    	}
	    	var tr = that.parent().parent();
            //获取预售单价
            var price = tr.find("input[name='price']").val();
            //设置总金额
            var sum = 0;
            if(!!num && !!price){
                sum = Number((price*num).toFixed(2));
            }
            tr.find("input[name='amount']").val(sum);
            var grossWeight = that.parents("tr").children('td').eq(14).children('input[name="grossWeight-hidden"]').val();//毛重  10
            var netWeight = that.parents("tr").children('td').eq(15).children('input[name="netWeight-hidden"]').val();//净重  11
            if (!grossWeight) {
                grossWeight = 0;
            }
            if (!netWeight) {
                netWeight = 0;
            }
            var totalGrossWeight = parseFloat(grossWeight)*num;
            var totalNetWeight = parseFloat(netWeight)*num;
            that.parents("tr").children('td').eq(14).children('input[name="grossWeight"]').val(totalGrossWeight.toFixed(5));   //10
            that.parents("tr").children('td').eq(15).children('input[name="netWeight"]').val(totalNetWeight.toFixed(5));   // 11
            
         // 根据税率计算含税金额
 	    	var taxRate = tr.find("select[name='taxRate']").find("option:selected").text();
 			if(taxRate){	    		
 	    		var taxAmount = parseFloat(sum) * (1 + parseFloat(taxRate));
 	    		taxAmount = parseFloat(taxAmount).toFixed(2) ;
 	    		tr.find("input[name='taxAmount']").val(taxAmount);
 	    		
 	    		var tax = parseFloat(taxAmount) - parseFloat(sum) ;
 	    		tr.find("input[name='tax']").val(parseFloat(tax).toFixed(2)) ; 	    		
 	    	}
            calculateWeight5();
            goods_change();
    });
    //监听采购总金额的离开事件
	$("#table-list").on("blur",'.goods-amount',function(){
		var that = $(this);
		//获取总金额
    	var sum= that.val();
    	var tr = that.parent().parent();
    	//获取采购数量
    	var num = tr.find("input[name='num']").val();
    	//判断是否数字
    	if(isNaN(sum) || !sum){
    		$(this).val(0);
    		$custom.alert.error("总金额请输入数字");
    		return ;
    	}
    	sum = parseFloat(sum) ;
    	sum = sum.toFixed(2) ;
    	$(this).val(sum) ; 
    	//设置单价
    	var price = 0;
    	if(!!num && !!sum){
	    	price = Number(sum/num).toFixed(8);
    	}
    	tr.find("input[name='price']").val(price);
    	
    	// 根据税率计算含税金额
    	var taxRate = tr.find("select[name='taxRate']").find("option:selected").text();
 		if(taxRate){	    		
    		var taxAmount = parseFloat(sum)* (1 + parseFloat(taxRate));
    		taxAmount = parseFloat(taxAmount).toFixed(2) ;
    		tr.find("input[name='taxAmount']").val(taxAmount);
    		
    		var tax = parseFloat(taxAmount) - parseFloat(sum) ;
    		tr.find("input[name='tax']").val(parseFloat(tax).toFixed(2)) ;		
	    }
        goods_change();
    });
	$("#table-list").on("blur",'.goods-price',function(){
			var that = $(this);
			var tr = that.parent().parent();
			//获取当前价格
	    	var price= that.val();
	    	//判断是否数字
	    	if(isNaN(price) || !price){
	    		$(this).val(0);
	    		$custom.alert.error("销售单价请输入数字");
	    		return ;
	    	}
	    	var indexOf = price.indexOf(".");
	    	if (indexOf != -1) {
	            var count = (parseFloat(price)+"").length - 1 - indexOf;
	            if (count > 8) {
	            	$(this).val("");
	            	tr.find("input[name='amount']").val("");
	                $custom.alert.error("销售单价只能为8位小数");
	                return;
	            }
	        }
          
	    	//获取采购数量
	    	var num = tr.find("input[name='num']").val();
	    	//设置总金额
	    	var sum = 0;
	    	if(!!num && !!price){
		    	sum = Number((price*num).toFixed(2));
	    	}
	    	tr.find("input[name='amount']").val(sum);
	    	$(this).val(parseFloat(price).toFixed(8)) ; 
	    	
	    	// 根据税率计算含税金额
	    	var taxRate = tr.find("select[name='taxRate']").find("option:selected").text();
 			if(taxRate){    		
	    		var taxAmount = parseFloat(sum) * (1+parseFloat(taxRate));
	    		taxAmount = parseFloat(taxAmount).toFixed(2) ;
	    		tr.find("input[name='taxAmount']").val(taxAmount);
	    		
	    		var tax = parseFloat(taxAmount) - parseFloat(sum) ;
	    		tr.find("input[name='tax']").val(parseFloat(tax).toFixed(2)) ;    		
	    	}
            goods_change();
	    });

    $("#table-list").on("blur",'.goods-declarePrice',function(){
        var that = $(this);
        //获取当前申报单价
        var declarePrice= that.val();
        //判断是否数字
        if(isNaN(declarePrice) || !declarePrice){
        	$(this).val(0);
            $custom.alert.error("申报单价请输入数字");
            return ;
        }
        goods_change();
    });
    
  //监听总金额 (含税）的离开事件
    $("#table-list").on("blur",'.goods-taxAmount',function(){
    	var that = $(this);
    	//获取当前总价
    	var taxAmount = that.val();
    	//判断是否数字
    	if(isNaN(taxAmount) || !taxAmount){
    		$(this).val(0);
    		$custom.alert.error("销售金额（含税）请输入数字");
    		return ;
    	}
    	
    	taxAmount = parseFloat(taxAmount) ;
    	taxAmount = taxAmount.toFixed(2) ; 
    	$(this).val(taxAmount) ; 		
		
		var tr = that.parent().parent();
		//获取销售数量
    	var num = tr.find("input[name='num']").val();	
    	
    	// 根据税率计算含税金额
    	var taxRate = tr.find("select[name='taxRate']").find("option:selected").text();
 		if(taxRate){    		
    		var sum = parseFloat(taxAmount)/ (1+parseFloat(taxRate));
    		sum = parseFloat(sum).toFixed(2) ;
    		tr.find("input[name='amount']").val(sum);
    		
    		var tax = parseFloat(taxAmount) - parseFloat(sum) ;
    		tr.find("input[name='tax']").val(parseFloat(tax).toFixed(2)) ;
    		
    		var price = parseFloat(sum)/parseFloat(num);
	    	tr.find("input[name='price']").val(price.toFixed(8));
    	}
    	goods_change();
    });
	 	
	$(".date-input").datetimepicker({
        language:  'zh-CN',
        format: "yyyy-mm-dd",
		autoclose: 1,
		todayHighlight: 1,
		startView: 2,
		minView: 2
    });

	$(".date-input2").datetimepicker({
        language:  'zh-CN',
        format: "yyyy-mm",
		autoclose: 1,
		todayHighlight: 1,
		startView: 3,
		minView: 3
    });

	
	$("#isSameArea").change(function() {
		var businessModel = $("#businessModel").val();
		var isSameArea = $("#isSameArea").val();
		// 同关区合同号必填,其他非必填
		 if(isSameArea=="1"){
			$("#fContractNo_i").css("display","inline-block");
		 }else{
			$("#fContractNo_i").css("display","none");
		 }
		 inDepotChange(businessModel);
	 });
	// 若销售类型改变的时候
	$("#businessModel").change(function(){
		 if($(this).val()=='3'){	// 采销执行
			$("#poNo_i").css("display","inline");			
		}else if($(this).val()=='4'){	// 当销售类型为购销-实销实结、采销执行时，PO单号非必填；
			$("#poNo_i").css("display","none");			
		}else if($(this).val()=='1'){	// 购销-整批结算类型时
			$("#poNo_i").css("display","inline");			
		}
	});
	
	//入库仓库的必填校验
	function inDepotChange(businessModel){
		var outId = $("#outDepotId").val();
		var isSameArea = $("#isSameArea").val();
		if(outId){
			$custom.request.ajaxSync("/depot/getDepotDetails.asyn", {"id":outId}, function(result){
					// 当出仓仓库为保税仓时,是否同关区必填
					if(result.data.type=='a'){
						// 1、当出仓仓库为保税仓，且是否同关区为“是”时，入库仓库必填，且入库仓库仅能为备查库
						if(isSameArea=="1"){
							$("#inDepotId").attr("disabled",false);
							$("#inDepotId_i").css("display","inline-block");
							$module.depot.getSelectBeanByMerchantRel.call($('select[name="inDepotId"]')[0],null,{"type":"b"});
							$module.depot.getSelectBeanByDepotCustomsRel.call($("select[name='inCustomsId']")[0],'',null);
						}else if(isSameArea=="0"){	// 2、当出仓仓库为保税仓，且是否同关区为“否” 且销售类型为购销-整批结算时，入库仓库禁用；
							if(businessModel == '1'){
								$("#inDepotId").val('');
								$("#inDepotId").attr("disabled",false);
								$("#inDepotId_i").css("display","none");
								$module.depot.getSelectBeanByMerchantRel.call($('select[name="inDepotId"]')[0],null,{"type":"b"});
								$module.depot.getSelectBeanByDepotCustomsRel.call($("select[name='inCustomsId']")[0],'',null);
							}else if(businessModel == '4'){	// 3、当出仓仓库为保税仓，且是否同关区为“否” 且销售类型为购销-实销实结时，入库仓库必填，可选仓库仅为备查库；
								$("#inDepotId").attr("disabled",false);
								$("#inDepotId_i").css("display","inline-block");
								$module.depot.getSelectBeanByMerchantRel.call($('select[name="inDepotId"]')[0],null,{"type":"b"});
								$module.depot.getSelectBeanByDepotCustomsRel.call($("select[name='inCustomsId']")[0],'',null);
							}
						}else{
							$module.depot.getSelectBeanByMerchantRel.call($('select[name="inDepotId"]')[0],null,null);
							$module.depot.getSelectBeanByDepotCustomsRel.call($("select[name='inCustomsId']")[0],'',null);
						}
					}else if(result.data.type=='c'){	// 当出仓仓库为海外仓时,是否同关区禁用;入库仓库禁用
						// 当出仓仓库为海外仓且销售类型为购销-实销实结时，入库仓库必填，可选仓库仅为备查库；
						if(businessModel == '4'){
							$("#inDepotId").attr("disabled",false);
							$("#inDepotId_i").css("display","inline-block");
							$module.depot.getSelectBeanByMerchantRel.call($('select[name="inDepotId"]')[0],null,{"type":"b"});
							$module.depot.getSelectBeanByDepotCustomsRel.call($("select[name='inCustomsId']")[0],'',null);
						}else if(businessModel == '1'){	// 当出仓仓库为海外仓且销售类型为购销-整批结算时，入库仓库禁用，不予选择
							$("#inDepotId").val('');
							$("#inDepotId").attr("disabled",false);
							$("#inDepotId_i").css("display","none");
							$module.depot.getSelectBeanByMerchantRel.call($('select[name="inDepotId"]')[0],null,{"type":"b"});
							$module.depot.getSelectBeanByDepotCustomsRel.call($("select[name='inCustomsId']")[0],'',null);
						}else{
							$module.depot.getSelectBeanByMerchantRel.call($('select[name="inDepotId"]')[0],null,null);
							$module.depot.getSelectBeanByDepotCustomsRel.call($("select[name='inCustomsId']")[0],'',null);
						}
					}else if(result.data.type=='d'){// 当出仓仓库为中转仓时,是否同关区非必填;
						 if(businessModel == '4'){	// 且销售类型为购销-实销实结时，入库仓库必填，可选仓库仅为备查库；
						 	$("#inDepotId").attr("disabled",false);
							$("#inDepotId_i").css("display","inline-block");
							$module.depot.getSelectBeanByMerchantRel.call($('select[name="inDepotId"]')[0],null,{"type":"b"});
							$module.depot.getSelectBeanByDepotCustomsRel.call($("select[name='inCustomsId']")[0],'',null);
						}else{
							$module.depot.getSelectBeanByMerchantRel.call($('select[name="inDepotId"]')[0],null,null);
							$module.depot.getSelectBeanByDepotCustomsRel.call($("select[name='inCustomsId']")[0],'',null);
						}
					}else{
						$module.depot.getSelectBeanByMerchantRel.call($('select[name="inDepotId"]')[0],null,null);
						$module.depot.getSelectBeanByDepotCustomsRel.call($("select[name='inCustomsId']")[0],'',null);
					}		
			});
		}
	}
	
	//出仓仓库加载必填参数提示
	// 事业部改变的时候
    $("#buId").change(function() {
    	var buId = $("#buId").val();
    	$module.depot.getSelectBeanByMerchantBuRel.call($("select[name='outDepotId']")[0],null, {"buId": buId});
    	$("#businessModel").val("");
    	//清空商品列表
		$("#table-list tr:gt(0)").remove();
		$("#unNeedIds").val('');
    });
	// 出库仓改变的时候
    $("#outDepotId").change(function() {
    	var businessModel = $("#businessModel").val();
    	var inId = $("#inDepotId").val();
    	var outId = $("#outDepotId").val();
        var buId = $("#buId").val();
        changeDeclareNecc(outId) ;//预申报单价
    	getMustParameter(1,outId,inId,businessModel);
    	checkOutDepot(outId);// 海外仓目的地和收件人信息弹出
    	inDepotChange(businessModel);    
    	$module.depot.getSelectBeanByDepotCustomsRel.call($("select[name='outCustomsId']")[0],outId,null);
    });
    $("#inDepotId").change(function() {
    	var businessModel = $("#businessModel").val();
    	var inId = $("#inDepotId").val();
    	var outId = $("#outDepotId").val();
        var buId = $("#buId").val();
    	getMustParameter(2,outId,inId,businessModel);
    	
    	$module.depot.getSelectBeanByDepotCustomsRel.call($("select[name='inCustomsId']")[0],inId,null);
    });
    
    // 邮寄方式发生改变时
    $("#mailMode").change(function() {
    	var mailMode = $("#mailMode").val();
        $("#receiverName").val('');
        $("#country").val('');
        $("#receiverAddress").val('');
    	// 邮寄
    	if ('1'==mailMode) {
            $("#receiverName").val('');
            $("#country").val('');
            $("#receiverAddress").val('');
		}else if('2'==mailMode){						
			$("#receiverName").val('卓志香港仓');
			$("#country").val('中国香港');
			$("#receiverAddress").val('香港 新界 元朗 流浮山路DD129');
		} 
    });
    
    function checkOutDepot(outId){    	
	$custom.request.ajax("/depot/getDepotDetails.asyn", {"id":outId}, function(result2){
		if(result2.data.type == 'c'){
			$("#destinationDiv").html('<label class="edit_label"><i class="red">*</i> 目的地：</label><select name="destination" id="destination" class="edit_input"><option value="">请选择</option><option value="GZJC01">广州机场</option><option value="HP01">黄埔状元谷</option><option value="HK01">香港本地</option></select>');
            $("#tallyingUnitDiv").html('<label class="edit_label"><i class="red">*</i> 理货单位：</label><select name="tallyingUnit" id="tallyingUnit" class="edit_input"><option value="00">托盘</option><option value="01">箱</option><option value="02" selected>件</option></select>');
        }else{
			$("#destinationDiv").html('');
			$("#country").val('');
			$("#receiverName").val('');
			$("#mailMode").val('');
			$("#receiverAddress").val('');
		}
		//当出库仓库类型为“中转仓”时，运输方式、承运商、托数、出库地点 箱数、托盘材质、包装、发票单号、卸货港非必填
		if(result2.data.type == 'd'){	
			$("#transport_i").css("display","none");
			$("#torusNumber_i").css("display","none");
			$("#carrier_i").css("display","none");
			$("#outdepotAddr_i").css("display","none");
			$("#boxNum_i").css("display","none");
	 		$("#torrPackType_i").css("display","none");
	 		$("#pack_i").css("display","none");
	 		$("#invoiceNo_i").css("display","none");
	 		$("#portDes_i").css("display","none");
		}else{
			$("#transport_i").css("display","inline-block");
			$("#torusNumber_i").css("display","inline-block");
			$("#carrier_i").css("display","inline-block");
			$("#outdepotAddr_i").css("display","inline-block");
			$("#boxNum_i").css("display","inline-block");
	 		$("#torrPackType_i").css("display","inline-block");
	 		$("#pack_i").css("display","inline-block");
	 		$("#invoiceNo_i").css("display","inline-block");
	 		$("#portDes_i").css("display","inline-block");
		}
	})
    }
    function getMustParameter(changeType,outId,inId,businessModel){
		if(changeType == 1){	// 改变出仓仓库的时候
			//检查是否涉及香港仓，是则添加理货单位
			if(inId){
				$custom.request.ajaxSync("/depot/getDepotDetails.asyn", {"id":inId}, function(result1){
					if(result1.data.type != 'c'){
						$custom.request.ajaxSync("/depot/getDepotDetails.asyn", {"id":outId}, function(result2){
							$("#deliveryAddr").val(result2.data.address);
							if(result2.data.type == 'c'){
								//显示
								$("#tallyingUnitDiv").html('<label class="edit_label"><i class="red">*</i>理货单位：</label><select name="tallyingUnit" id="tallyingUnit" class="edit_input"><option value="00">托盘</option><option value="01">箱</option><option value="02" selected>件</option></select>');
                                $("#destinationDiv").html('<label class="edit_label"><i class="red">*</i> 目的地：</label><select name="destination" id="destination" class="edit_input"><option value="">请选择</option><option value="GZJC01">广州机场</option><option value="HP01">黄埔状元谷</option><option value="HK01">香港本地</option></select>');
                            }else{
								//隐藏
								$("#tallyingUnitDiv").html('');
								$("#destinationDiv").html('');
							}
						});
					}
				});
			}else{
				$custom.request.ajaxSync("/depot/getDepotDetails.asyn", {"id":outId}, function(result){
					$("#deliveryAddr").val(result.data.address);
					// 当出仓仓库为保税仓时，是否同关区必填;当出仓仓库为海外仓时，是否同关区禁用;当出仓仓库为中转仓时，是否同关区非必填
					if(result.data.type == 'a'){
						$("#isSameArea").attr("disabled",false);
						$("#isSameArea_i").css("display","inline");
						//隐藏
						$("#tallyingUnitDiv").html('');
                        $("#destinationDiv").html('');
                        $("#mailModeDiv").hide();
                        $("#receiverNameDiv").hide();
                        $("#countryDiv").hide();
                        $("#receiverAddressDiv").hide();
                        $('#businessModel').find('option:eq(3)').attr('disabled','disabled');
                        $('#businessModel').val('');
					}else if(result.data.type == 'c'){
						$("#isSameArea").attr("disabled",true);
						$("#isSameArea_i").css("display","none");
						//显示
						$("#tallyingUnitDiv").html('<label class="edit_label"><i class="red">*</i> 理货单位：</label><select name="tallyingUnit" id="tallyingUnit" class="edit_input"><option value="00">托盘</option><option value="01">箱</option><option value="02" selected>件</option></select>');
                        $("#destinationDiv").html('<label class="edit_label"><i class="red">*</i> 目的地：</label><select name="destination" id="destination" class="edit_input"><option value="">请选择</option><option value="GZJC01">广州机场</option><option value="HP01">黄埔状元谷</option><option value="HK01">香港本地</option></select>');
                        $("#mailModeDiv").show();
                        $("#receiverNameDiv").show();
                        $("#countryDiv").show();
                        $("#receiverAddressDiv").show();
                        $('#businessModel').find('option:eq(3)').attr('disabled','disabled');
                        $('#businessModel').val('');
                    }else if(result.data.type == 'd'){
						$("#isSameArea").attr("disabled",false);
						$("#isSameArea_i").css("display","none");
						$("#inDepotId_i").css("display","none");
						$("#inDepotId").attr("disabled",false);						
						//隐藏
						$("#tallyingUnitDiv").html('');
                        $("#destinationDiv").html('');
                        $("#mailModeDiv").hide();
                        $("#receiverNameDiv").hide();
                        $("#countryDiv").hide();
                        $("#receiverAddressDiv").hide();
                        $('#businessModel').find('option:eq(3)').attr('disabled',false);
                        $('#businessModel').val('');
					}
				});
			}
			//清空商品列表
			$("#table-list tr:gt(0)").remove();
			$("#unNeedIds").val('');
		}else{	// 改变入仓仓库的时候
			if(outId){
				//检查是否涉及香港仓，是则添加理货单位
		    	$custom.request.ajax("/depot/getDepotDetails.asyn", {"id":outId}, function(result1){
					if(result1.data.type != 'c'){
						$custom.request.ajaxSync("/depot/getDepotDetails.asyn", {"id":inId}, function(result2){
							$("#deliveryAddr").val(result2.data.address);
							if(result2.data.type == 'c'){
								//显示
								$("#tallyingUnitDiv").html('<label class="edit_label"><i class="red">*</i> 理货单位：</label><select name="tallyingUnit" id="tallyingUnit" class="edit_input"><option value="00">托盘</option><option value="01">箱</option><option value="02" selected>件</option></select>');
                                $("#destinationDiv").html('<label class="edit_label"><i class="red">*</i> 目的地：</label><select name="destination" id="destination" class="edit_input"><option value="">请选择</option><option value="GZJC01">广州机场</option><option value="HP01">黄埔状元谷</option><option value="HK01">香港本地</option></select>');
                            }else{
								//隐藏
								$("#tallyingUnitDiv").html('');
								$("#destinationDiv").html('');
							}
						});
					}
				});
			}else{
				$custom.request.ajax("/depot/getDepotDetails.asyn", {"id":inId}, function(result){
					$("#deliveryAddr").val(result.data.address);
					if(result.data.type == 'c'){
						//显示
						$("#tallyingUnitDiv").html('<label class="edit_label"><i class="red">*</i> 理货单位：</label><select name="tallyingUnit" id="tallyingUnit" class="edit_input"><option value="00">托盘</option><option value="01">箱</option><option value="02" selected>件</option></select>');
                        $("#destinationDiv").html('<label class="edit_label"><i class="red">*</i> 目的地：</label><select name="destination" id="destination" class="edit_input"><option value="">请选择</option><option value="GZJC01">广州机场</option><option value="HP01">黄埔状元谷</option><option value="HK01">香港本地</option></select>');
                    }else{
						//隐藏
						$("#tallyingUnitDiv").html('');
						$("#destinationDiv").html('');
					}
				});
			}
		}
    }
}); 
function isExist(goodsArr, goodsId) {
	for(var i=0;i<goodsArr.length;i++){
		  if(goodsArr[i]==goodsId){	// 商品重复了
			  return true;
		  }else{
			  return false;
		  }							
	}	
}
function changeDeclareNecc(depotId){
    $custom.request.ajax("/depot/checkDepotMerchantRel.asyn", {"depotId":depotId}, function(result){
        if(result.data != null && result.data.isInOutInstruction == '1'){
            $("#declarePriceI").show() ;
            isInOutInstruction = true ;
        }else{
            $("#declarePriceI").hide() ;
            isInOutInstruction = false ;
        }
    });
}

    $(function () {
        var total = 0;
        $(".goods-num").each(function () {
            var val = $(this).val();
            total += parseFloat(val);
        })
        $("#totalNum").html(total);
    })

    $(function () {
        calculateWeight5();
    })

    $(function () {
        var total = 0;
        $(".goods-amount").each(function () {
            var val = $(this).val();
            total += parseFloat(val);
        })
        $("#totalAmount").html(total.toFixed(2));
    })

    $(function () {
        var total = 0;
        $(".goods-rough").each(function () {
            var val = $(this).val();
            if (!val) {
            	val = 0;
            }
            total += parseFloat(val);
        })
        $("#totalRough").html(total.toFixed(5));
    })

    $(function () {
        var total = 0;
        $(".goods-suttle").each(function () {
            var val = $(this).val();
            if (!val) {
            	val = 0;
            }
            total += parseFloat(val);
        })
        $("#totalSuttle").html(total.toFixed(5));
    })
	$(function () {
        var total = 0;
        $(".goods-tax").each(function () {
            var val = $(this).val();
            if (!val) {
            	val = 0;
            }
            total += parseFloat(val);
        })
        $("#totalTax").html(total.toFixed(2));
    })
    $(function () {
        var total = 0;
        $(".goods-taxAmount").each(function () {
            var val = $(this).val();
            if (!val) {
            	val = 0;
            }
            total += parseFloat(val);
        })
        $("#totalTaxAmount").html(total.toFixed(2));
    })

    function goods_change() {
        var total = 0;
        $("#tbody tr").each(function (i) {
            var ch = $(this).find('td:eq(6) input').val();
            if (!ch) {
            	ch = 0;
            }
            total = parseFloat(total) + parseFloat(ch);
        })
        $("#totalNum").html(total);

        var total2 = 0;
        $("#tbody tr").each(function (i) {
            var ch = $(this).find('td:eq(9) input').val();
            if (!ch) {
            	ch = 0;
            }
            total2 = parseFloat(total2) + parseFloat(ch);
        })
        $("#totalAmount").html(total2.toFixed(2));

        var total3 = 0;
        $("#tbody tr").each(function (i) {
            var ch = $(this).find("td").eq(14).children('input[name="grossWeight"]').val();
            if (!ch) {
            	ch = 0;
            }
            total3 = parseFloat(total3) + parseFloat(ch);
        })
        $("#totalRough").text(total3.toFixed(2));

        var total4 = 0;
        $("#tbody tr").each(function (i) {
            var ch = $(this).find("td").eq(15).children('input[name="netWeight"]').val();
            if (!ch) {
            	ch = 0;
            }
            total4 = parseFloat(total4) + parseFloat(ch);
        })
        $("#totalSuttle").text(total4.toFixed(5));
        
        var total5 = 0;
        $("#tbody tr").each(function (i) {
            var ch = $(this).find('td:eq(10) input').val();
            if (!ch) {
            	ch = 0;
            }
            total5 = parseFloat(total5) + parseFloat(ch);
        })
        $("#totalTaxAmount").html(total5.toFixed(2));
       
        var total6 = 0;
        $("#tbody tr").each(function (i) {
            var ch = $(this).find('td:eq(12) input').val();
            if(!ch){
            	ch = 0;
            }
            total6 = parseFloat(total6) + parseFloat(ch);
        })
        $("#totalTax").html(total6.toFixed(2));
    }
	//保留小数位
    function numFormat(obj,num){
		if(num == 2){
        	obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');
		}else if (num == 5){
			obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d{1,5}).*$/, '$1$2.$3');
		}
        if (obj.value.indexOf(".") < 0 && obj.value != "") {//如果没有小数点，首位不能为类似于 01、02的金额
            obj.value = parseFloat(obj.value);
        }
    }
  //是否生成采购订单
    function checkPurchase(str){
    	var poNo = $("#poNo").val();
    	var merchantId = $("#merchantId").val();
    	var customerId = $("#customerId").val();
    	var merchantName = $("#merchantName").val();
    	var url = serverAddr+"/sale/createPurchaseOrder.asyn";
    	$custom.request.ajax(serverAddr+"/sale/checkExistPurchase.asyn",{"poNo":poNo,"merchantId":merchantId,"customerId":customerId},function(result){
    		if(result.data.code =='00'){
    			$m9016.init();
    			$("#9016modalId").val(str.split("&")[1]);
    			$("#9016merchantName").html(result.data.innerMerchantName);
    		}else{			
    			$custom.alert.success(str.split("&")[0]);
                //重新加载页面
                setTimeout(function () {
                    $load.a(pageUrl + "4001");
                }, 1000);
    		}
    	});	
    }
  
    function calcItemGrossWeight(){	
    	var grossWeight = $("#billWeight").val() ;
    	var grossWeightTotal = $("#billWeight").val() ;
    	
    	/**汇总净重*/
    	var netWeightSum = 0;
    	$("input[name='netWeight']").each(function(){
    		
    		if(!$(this).val()){
    			$(this).val(0) ;
    		}
    		
    		netWeightSum += parseFloat($(this).val());
    	});

    	// 默认5 位
    	var decimalPlace = 3 ;	
    	if(grossWeight.indexOf(".") > -1){
    		decimalPlace = grossWeight.substr(grossWeight.indexOf(".") + 1).length ;		
    		decimalPlace = parseInt(decimalPlace) ;		
    		if(decimalPlace > 5){
    			decimalPlace = 5 ;
    		}
    	}
    	if(netWeightSum!=0){
    		$("input[name='netWeight']").each(function(index , item){
    			
    			var grossWeightTemp = (grossWeight * ($(this).val()/netWeightSum)).toFixed(decimalPlace) ;
    			
    			if(index != $("input[name='netWeight']").length - 1){
    			    $(this).parent().parent().find("input[name='grossWeight']").val(grossWeightTemp);
    			    grossWeightTotal -= parseFloat(grossWeightTemp) ;
    			    grossWeightTotal = parseFloat(grossWeightTotal).toFixed(decimalPlace) ;
    			}else{
    				$(this).parent().parent().find("input[name='grossWeight']").val(grossWeightTotal) ;
    			}
    			
    		});
    	}
    	$("#totalRough").text(parseFloat(grossWeight).toFixed(decimalPlace));
    }
    
    $("#buId,#currency").change(function(){
    	salePriceManage = getSalePriceManage() ;

    });
  //是否启用销售价格管理
    function getSalePriceManage(){
    	var buId = $("#buId").val();
    	var customerId = $("#customerId").val() ;
    	var sale_PriceManage ; 
    	if(buId && customerId){
    		$custom.request.ajaxSync(serverAddr + "/sale/getSalePriceManage.asyn", {"buId":buId,"customerId":customerId}, function(result){
    			if(result.state == 200 && result.data ){
    				sale_PriceManage = result.data ;
    			}else{
    				sale_PriceManage = false ;
    			}
    		});
    	}    	
    	return sale_PriceManage ;
    }
  	//统计箱数
    function calTotalBoxNum(){
    	var totalBoxNum = 0;
        $("#table-list tbody tr").each(function (index, item) {
            var boxNum = $(this).find("td").eq(17).children('input[name="itemBoxNum"]').val();//箱数
            if (!boxNum) {
            	boxNum = 0;
            }
            totalBoxNum += parseInt(boxNum);
        });
        $("#boxNum").val(totalBoxNum);
        fillPack();
    }

    $("#boxNum,#torrPackType,#torusNumber").blur(function(){
    	fillPack();
    	
    });
    //包装 取箱数+托数值+托盘材质
    function fillPack(){
    	var boxNum = $("#boxNum").val();//箱数
    	var torusNumber = $("#torusNumber").val();//托数
    	var torrPackType = $("#torrPackType").val();//托盘材质
    	var pack = "";
    	if(boxNum){
    		pack += boxNum +"箱";
    	}
    	if(torusNumber){
    		pack += torusNumber +"托";
    	}
    	if(torrPackType != ""){		
    		pack += $("#torrPackType").find("option:selected").text();//托盘材质
    	}
    	$("#pack").val(pack);
    }
    
  //获取税率下拉
    function getTaxRateSelectHtml(taxRate){
    	/**根据客户带出对应税率*/
    	var taxRateId = "" ;
    	if(!taxRate){
    		var customerId = $("#customerId").val() ;
    		var getRateurl = "/webapi/system/customer/getRateByCustomerAndMerchant.asyn" ;	
    		
    		var merchantId = $("#merchantId").val();
    		$custom.request.ajaxSync(getRateurl, {"customerId": customerId,"merchantId": merchantId}, function(result){
    			if(result.code == '10000' && result.data){			
    				taxRateId = result.data.rateId ;			
    			}
    		});
    	}
    	
    	var url = "/webapi/system/tariffRate/getSelectBean.asyn" ;	
    	var html = "<option value=''>0</option>" ;
    	$custom.request.ajaxSync(url, {}, function(result){
    		if(result.code == '10000' && result.data){
    			 var list = result.data ;
    			 $(list).each(function(index, item){
    				 var selected = "" ;
    				 if(taxRate && taxRate == item.label){
    					selected = "selected" ;
    				 }else if(taxRateId && taxRateId == item.value){
    					 selected = "selected" ;
    				 }
    				 html += "<option value='"+item.value+"' "+ selected +" >"+item.label+"</option>" ;
    			}) ;
    		}
    	});    	
    	return html ;
    }
    function calculateTaxAmount(obj){
   	 var that = $(obj);
    	//获取税率
    	var taxRate = that.find("option:selected").text();
    	
    	var tr = that.parent().parent();
         var amount = tr.find("input[name='amount']").val();
         //获取销售总金额（含税）
         var taxAmount = 0;
         if(!!taxRate && !!amount){
       	  	taxAmount = parseFloat(amount)*(1+parseFloat(taxRate));
       	  	taxAmount = parseFloat(taxAmount).toFixed(2);
         }
            
         tr.find("input[name='taxAmount']").val(taxAmount);
         var tax = parseFloat(taxAmount) - parseFloat(amount);
         tr.find("input[name='tax']").val(parseFloat(tax).toFixed(2));//商品税额=销售金额（含税）- 销售金额（不含税）
         
         goods_change();
   }
</script>
