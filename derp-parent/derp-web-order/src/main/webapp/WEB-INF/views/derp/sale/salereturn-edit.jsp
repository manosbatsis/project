<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<jsp:include page="/WEB-INF/views/common.jsp" />

<!-- Start content-->
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
                       <li class="breadcrumb-item"><a href="#">编辑销售退货订单</a></li>
                    </ol>
                    </div>
                   </div>
                    <!--  title   end  -->               
                          <!--  title   基本信息   start -->
                    <div class="title_text">基本信息</div>
                    <div class="edit_box mt20">
                     	<input type="hidden" value="${detail.id}" name="orderReturnId" id="orderReturnId">
                     	<input type="hidden" value="${detail.code}" name="orderReturnCode" id="orderReturnCode">
                   		<input type="hidden" value="${isForbid}" name="isForbid" id="isForbid"/>
                        <input type="hidden" class="form-control" name="referToOrder" id="referToOrder" value="${detail.saleOrderRelCode}">
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> 公   司：</label>
                            <input type="hidden" value="${detail.merchantId}" name="merchantId" id="merchantId"/>
                            <input type="text" class="edit_input" value="${detail.merchantName}" name = "merchantName" id = "merchantName" disabled>
                        </div> 
                        	<c:if test="${isForbid=='no'}">
                        	<div class="edit_item">
	                            <label class="edit_label"><i class="red">*</i> 退货类型：</label>
		                            <select class="edit_input" name="returnType" id="returnType">
		                                <option value="1" <c:if test="${detail.returnType == '1'}"> selected = 'selected'</c:if>>消费者退货</option>
		                                <option value="2" disabled="disabled"<c:if test="${detail.returnType == '2'}"> selected = 'selected'</c:if>>代销退货</option>
		                                <option value="3" <c:if test="${detail.returnType == '3'}"> selected = 'selected'</c:if>>购销退货</option>
		                            </select>
	                        </div>
                        	</c:if>
	                      <c:if test="${isForbid!='no'}">
                        	<div class="edit_item">
	                            <label class="edit_label"><i class="red">*</i> 退货类型：</label>
		                            <select class="edit_input" name="returnType" id="returnType">
		                            	<option value="">请选择</option>
		                                <option value="1" <c:if test="${detail.returnType == '1'}"> selected = 'selected'</c:if> >消费者退货</option>
		                                <option value="2" disabled="disabled"<c:if test="${detail.returnType == '2'}"> selected = 'selected'</c:if>>代销退货</option>
		                                <option value="3"  disabled="disabled">购销退货</option>
		                            </select>
	                        </div>
                        	</c:if>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> 客   户：</label>
                            <select class="edit_input" name="customerId" id="customerId" parsley-trigger="change" required>
                                <option value="">请选择</option>
                            </select>
                        </div>
                    </div>
                    <div class="edit_box">
                          <div class="edit_item">
                            <label class="edit_label"><i class="red" id="inDepotId_i">*</i> 退入仓库：</label>
                            <select class="edit_input" name="inDepotId" id="inDepotId">
                                <option value="">请选择</option>
                            </select>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red" style="display:none;" id="isSameArea_i">*</i>是否同关区：</label>
                            <select class="edit_input" name="isSameArea" id="isSameArea">
                               <option value="">请选择</option>
                                <option value="1" <c:if test="${detail.isSameArea == '1'}"> selected = 'selected'</c:if>>是</option>
                                <option value="0" <c:if test="${detail.isSameArea == '0'}"> selected = 'selected'</c:if>>否</option>
                            </select>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red"style="display:none;"  id="outDepotId_i">*</i> 退出仓库：</label>
                            <select class="edit_input" name="outDepotId" id="outDepotId" parsley-trigger="change" required>
                                <option value="">请选择</option>
                            </select>
                        </div>
                    </div>
                    <div class="edit_box">
                       <div class="edit_item">
                           <label class="edit_label"><i class="red">*</i> 事业部：</label>
                            <select class="edit_input" name="buId" id="buId" parsley-trigger="change" required>
                                <option value="">请选择</option>
                            </select>
                         </div>
                    	<div class="edit_item">
                            <label class="edit_label"><i class="red" style="display:none;" id="contractNo_i">*</i> 报关合同号：</label>
                            <input type="text" class="edit_input" name="fContractNo" id="contractNo"  value="${detail.contractNo}">
                        </div>
                        <div class="edit_item">
                             <label class="edit_label"><i class="red" style="display:none;" id="ladingBill_i">*</i> 头程提单号：</label>
                             <input type="text" required="" parsley-type="text" class="edit_input" id="ladingBill" name="ladingBill" value="${detail.ladingBill}"maxlength="50">
                         </div>
                    </div>
                     <div class="edit_box">
                         <div class="edit_item">
                             <label class="edit_label"><i class="red" style="display:none;" id="deliveryAddr_i">*</i> 收货地址：</label>
                             <input type="text" parsley-type="text"  class="edit_input" id="deliveryAddr" maxlength="200"  value="${detail.deliveryAddr}">
                         </div>
                         <div class="edit_item">
                              <label class="edit_label"><i class="red"style="display:none;"  id="boxNum_i">*</i> 箱数：</label>
                              <input type="text" required="" parsley-type="text" class="edit_input"
                                     id="boxNum" maxlength="20" onkeyup="value=value.replace(/[^\d]/g,'')" value="${detail.boxNum}">
                          </div>
                          <div class="edit_item">
                            <label class="edit_label"><i class="red" style="display:none;"id="portLoading_i">*</i>装货港：</label>
                            <input type="text" required="" parsley-type="text" class="edit_input" name="portLoading" id="portLoading" value="${detail.portLoading}">
                          </div>         
                    </div>
                    <div class="edit_box">                    
                         <div class="edit_item">
                            <label class="edit_label"><i class="red" style="display:none;"id="packType_i">*</i>包装方式：</label>
                            <div class="edit_input" style="text-indent: 0">
	                            <select class="form-control goods_select2" name="packType" id="packType">
	                                <option value="">请选择</option>
		                            <c:forEach items="${packTypeBean }" var="packType">
		                            	<c:choose>
                                            <c:when test="${packType.value eq detail.packType }">
                                                <option value="${packType.value }" selected>${packType.label }</option>
                                            </c:when>
                                            <c:otherwise>
                                                <option value="${packType.value }">${packType.label }</option>
                                            </c:otherwise>
                                        </c:choose>
		                            </c:forEach>
		                        </select>
		                    </div>
                        </div>
	                        <div class="edit_item">
	                        	<label class="edit_label"><i class="red"style="display:none;" id="payRules_i">*</i>付款条约：</label>
	                            <input type="text" required="" parsley-type="text" class="edit_input" name="payRules" id="payRules" value="${detail.payRules}">
                       		 </div>
                       		 <div class="edit_item">
	                            <label class="edit_label"><i class="red"style="display:none;" id="billWeight_i">*</i>提单毛重KG：</label>
	                            <input type="text" required="" disabled parsley-type="text" class="edit_input" name="billWeight" id="billWeight" value="${detail.billWeight}">
                        	</div>         
                    </div>
                    <div class="edit_box">
                    		 <div class="edit_item">
	                            <label class="edit_label"><i class="red" style="display:none;"id="portDestNo_i">*</i>卸货港：</label>
	                            <select class="edit_input" name="portDestNo" id="portDestNo">
	                        		<option value="">请选择</option>
	                        	</select>
                       		 </div>
                       		 <div class="edit_item">
	                        	<label class="edit_label"><i class="red"style="display:none;" id="shipper_i">*</i>境外发货人：</label>
	                            <input type="text" required="" parsley-type="text" class="edit_input" name="shipper" id="shipper" value="${detail.shipper}">
                        	</div>
	                    	 <div class="edit_item">
                            <label class="edit_label"><i class="red" style="display:none;"id="mark_i">*</i>唛头：</label>
                            <input type="text" required="" parsley-type="text" class="edit_input" name="mark" id="mark" value="${detail.mark}">
                        	</div>
                    </div> 
                     	<div class="edit_box">
                    	 	 <div class="edit_item">
	                            <label class="edit_label">LBX单号：</label>
	                            <input type="text" required="" parsley-type="text" class="edit_input" name="lbxNo" id="lbxNo"  value="${detail.lbxNo}">
                        	</div>
                          <div class="edit_item">
	                            <label class="edit_label"><i class="red" id="currency_i">*</i>退货币种：</label>
	                             <select class="edit_input" name="currency" id="currency" parsley-trigger="change" required>
                                	<option value="">请选择</option>
                            	</select>
                            	<input id="currency_hidden" type="hidden" value="${detail.currency }">
                        	</div>
                        <div class="edit_item">
	                            <label class="edit_label"><i class="red" id="remark_i"></i>退货原因：</label>
	                            <input type="text" required="" parsley-type="text" class="edit_input" name="remark" id="remark"value="${detail.remark}">
                        </div> 
                     	</div>
                     <div class="edit_box">
                     		<div class="edit_item">
	                            <label class="edit_label"><i class="red" id="saleOrderRelCode_i"></i>关联销售单：</label>
	                            <input type="text" required="" parsley-type="text" class="edit_input" name="saleOrderRelCode" id="saleOrderRelCode" value="${detail.saleOrderRelCode}" readonly="readonly">
	                        </div> 
	                        <div class="edit_item" >
		                        	<label class="edit_label" ><i class="red" style="display:none;"  id="tallyingUnit_i">*</i> 理货单位：</label>
		                        	 <select class="edit_input" name="tallyingUnit" id="tallyingUnit" parsley-trigger="change" required>
                                	<option value="00" <c:if test="${detail.tallyingUnit == '00'}"> selected = 'selected'</c:if>>托盘</option>
		                        	<option value="01" <c:if test="${detail.tallyingUnit == '01'}"> selected = 'selected'</c:if>>箱</option>
		                        	<option value="02" <c:if test="${detail.tallyingUnit == '02'}"> selected = 'selected'</c:if>>件</option>
                            		</select>
	                        </div>    
	                     <div class="edit_item">
                             <label class="edit_label"><i class="red" style="display:none;"  id="invoiceNo_i">*</i> 发票号：</label>
                             <input type="text" parsley-type="text" class="edit_input" id="invoiceNo" maxlength="50" value="${detail.invoiceNo}">
                         </div>
                        </div>
                   </div>  	
                 <!--  title   基本信息  start -->
                     <!--   商品信息  start -->
                                <div class="title_text">销售退货商品明细</div>
                                <%--   <input type="hidden" id="mId" value="${user.merchantId}"> --%>
                                <%--  <input type="hidden" id="customerId" value="${user.customerId}"> --%>
                                <div class="row col-12 mt20 table-responsive">
                                    <table id="datatable-buttons" class="table table-striped" cellspacing="0"
                                           width="100%">
                                        <thead>
                                        <tr>
                                            <!--  <th>序号</th> -->
                                            <th>操作</th>
                                            <th>序号</th>
                                            <th><i  class="red" style="display:none;"id="poNo_i">*</i>PO单号</th>
                                            <th>PO时间</th>
                                            <th><i class="red">*</i> 退入商品货号</th>
                                            <th><i class="red" id="outGoodsId_i">*</i> 退出商品货号</th>
                                            <th>退入商品名称</th>
                                            <th>退入商品条形码</th>
                                            <th><i class="red">*</i>退货商品单价</th>
                                            <th> 退货商品数量</th>
                                            <th><i class="red">*</i>好品数量</th>
                                            <th><i class="red">*</i>坏品数量</th>
                                            <th>批次号</th>
                                            <th><i class="red" style="display:none;"id="brandName_i">*</i>品牌类型</th>
                                            <th><i class="red" style="display:none;"id="grossWeight_i">*</i>毛重(kg)</th>
                                            <th><i class="red" style="display:none;"id="netWeight_i">*</i>净重(kg)</th>
                                            <th><i class="red" style="display:none;"id="boxNo_i">*</i>&nbsp;&nbsp;&nbsp;箱&nbsp;号&nbsp;&nbsp;&nbsp;</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                     <c:forEach items="${detail.itemList}"  var="item">
                                        <tr>
                                            <!-- <td>1</td> -->
                                            <td class="tc nowrap"><i class="fi-plus" onclick="addtr();"></i>&nbsp;&nbsp;&nbsp;<i
                                                    class="fi-minus" onclick="deltr(this);"></i></td>

                                            <td><input type="text" style="width: 80px;" class="form-control" name="seq"  value="${item.seq}"></td>

                                            <input type='hidden' name='id' value='${item.id}'>
	                    					<input type='hidden' name='goodsId' value='${item.outGoodsId}'></td>
                                            <td><input type="text" style="width: 80px;" class="form-control" name="poNo"  value="${item.poNo}"></td>
                                            <td><input type="text" style="width: 80px;" class="form-control" name="poDate"   value="${item.poDate}"></td>
                                             <td>
                                             <input type="hidden"  class="form-control" name="inGoodsId" value="${item.inGoodsId}">
                                            	${item.inGoodsNo}
                                                <button type="button"
                                                        class="btn btn-find waves-effect waves-light btn_default indepot_goods"
                                                        onclick="initGoodsBefore(this, 'inDepotId');">选择商品
                                                </button>
                                            </td>
                                            <td>
                                            	<input type="hidden"  class="form-control" name="outGoodsId" value="${item.outGoodsId}">
                                            	<span class="outGoodsNo">${item.outGoodsNo}</span>
                                                <button type="button"
                                                        class="btn btn-find waves-effect waves-light btn_default"
                                                        onclick="initGoodsBefore(this, 'outDepotId')">选择商品
                                                </button>
                                            </td>
                                            <td><input type="hidden"  class="form-control" name="inGoodsName" value="${item.inGoodsName}">
                                            	${item.inGoodsName}</td>
                                            <td><input type="hidden"  class="form-control" name="barcode" value="${item.barcode}">
                                            	${item.barcode}</td>
                                            <td><input type="text" class="form-control" name="price"
                                                       onkeyup="value=value.replace(/[^\d^\.]/g,'')" onblur ="value=parseFloat(value).toFixed(8)" value="${item.price}"></td>
                                          <td><input type="text" class="form-control" name="totalNum" value="${item.returnNum+item.badGoodsNum}" onkeyup="value=value.replace(/[^\d]/g,'')" readonly="readonly"  maxlength="9"></td>
                                           <td><input type="text" class="form-control" name="returnNum"
                                                   onchange="setTotalNum(this);"   onkeyup="value=value.replace(/[^\d]/g,'')"value="${item.returnNum}" maxlength="9">
                                            </td>
                                            <td><input type="text" class="form-control" name="badGoodsNum"
                                                      onchange="setTotalNum(this);" onkeyup="value=value.replace(/[^\d]/g,'')"value="${item.badGoodsNum}" maxlength="9">
                                            </td>
                                            <td><input type="text" class="form-control" style="width: 50px;" name="returnBatchNo" value="${item.returnBatchNo}"/></td>
                                             <td><input type="text" class="form-control" name="brandName" value="${item.brandName}"/></td>
                                              <td><input type="hidden"  name="grossWeight-hidden" value="${item.orignGrossWeight}">
                                                  <input type="text" class="form-control" name="grossWeight" value="${item.grossWeight}" onkeyup="value=value.replace(/[^\d^\.]/g,'')" onchange="calculateWeight()" maxlength="11"></td>
                                              <td><input type="hidden"  name="netWeight-hidden" value="${item.orignNetWeight}">
                                                  <input type="text" class="form-control" name="netWeight"  value="${item.netWeight}"   onkeyup="value=value.replace(/[^\d^\.]/g,'')" maxlength="11"></td>
                                              <td><input type="text" class="form-control" name="boxNo" value="${item.boxNo}"></td>                     
                                        </tr>
                                  </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                                <!--   商品信息  end -->
                      <div class="form-row m-t-50">
                          <div class="col-12">
                              <div class="row">
                                  <div class="col-4">
                                  </div>
                                  <div class="col-4">
                                      <input type="button" class="btn btn-info waves-effect waves-light btn_default" id="save-buttons" value="保 存"/>
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
			       <jsp:include page="/WEB-INF/views/modals/9013-V2.jsp"/>
                  <!-- 弹窗结束 -->
                  <!-- end row -->
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->
<script type="text/javascript">
var inDepotTypeVal=null;	// 入仓仓库类型
var inIsInOutInstructionVal=null;	// 入仓仓库进出接口指令
var inDepotNameVal=null;	// 入仓仓库名字
var outDepotCodeVal=null;	// 出仓仓库编码
var businessModel= '${businessModel}';
var buId= '${buId}';
var inDepotId= '${inDepotId}';
var outDepotId= '${outDepotId}';
var outDepotType = null; //出仓仓库类型
var outDepotIsTopBooks=null;//出仓仓库是否代销仓
 $(document).ready(function() {
	 //加载事业部
     $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],buId,null,outDepotId,inDepotId);
     //暂存仓且标识为代销仓
     var depotE = $module.depot.ajax($module.params.getSelectBeanByMerchantRelURL, {"type":"e", "isTopBooks":"1"});

	 var isForbid=$("#isForbid").val();
     var returnType = $("#returnType").val();
	 if(isForbid=="no"){
		 $("#returnType").attr("disabled",true);
		 $("#customerId").attr("disabled",true);
		 if(returnType=="1"){		//  消费者退货类型该字段非必填，其他类型下为必填
			 $("#isSameArea_i").css("display","none");
			 $("#contractNo_i").css("display","none");
			 $("#outGoodsId_i").css("display","none");
			 $("#poNo_i").css("display","none");
		 }else{
			 $("#poNo_i").css("display","inline-block");
			 $("#isSameArea_i").css("display","inline-block");
			 $("#contractNo_i").css("display","inline-block");
			 $("#outGoodsId_i").css("display","inline-block");
	         $("#brandName_i").css("display","inline-block");
	         $("#boxNo_i").css("display","inline-block");
		 }
	 }else{
		 if(returnType=="1"){		//  消费者退货类型该字段非必填，其他类型下为必填
			 $("#outGoodsId_i").css("display","none");
		 }else{
			 $("#outGoodsId_i").css("display","inline-block");
		 }
	 }

     /**
      * 1. 购销退货
      *    1) 销售退货单据选择单据类型为“代销”时，退入仓库仅能选保税仓、海外仓、销毁区，退出仓库默认是代销订单中的出库仓库，可重新选择，重选仅能选择备查库、暂存仓（暂存仓且标识为非代销仓）；
      *    2) 销售退货单据选择单据类型为“购销-整批结算”时，退入仓库默认为销售订单的出库仓库，可重新选择，重选时仅能选择保税仓、海外仓、销毁区；是否同关区、退出仓库均为空（不设值）；是否同关区必填，退出仓根据是否同关区判断必填（同关区必填，跨关区禁用，必填可选退出仓库为备查库、暂存仓（暂存仓且标识为非代销仓））；
      *    3) 销售退货单据选择单据类型为“购销-实销实结”时，退入仓库默认为销售订单的出库仓库，可重新选择，重选时仅能选择保税仓、海外仓、销毁区；是否同关区为空（不设值）；退出仓库必填且默认为销售订单中的入库仓库，可重新选择，重选仅能选择备查库、暂存仓（暂存仓且标识为非代销仓）；
      * 2. 非购销退货：退货类型为消费者退货时，退入仓仅为销毁区，退出仓库仅能为备查库、暂存仓（暂存仓且标识为非代销仓）
      */
     if(returnType!="1"){ //购销退货
         //加载退入仓库的下拉数据
         $module.depot.getSelectBeanByMerchantRel.call($('select[name="inDepotId"]')[0],'${detail.inDepotId}',{"type":"a,c,d,f"});
         //加载退出仓库的下拉数据
         $module.depot.getSelectBeanByMerchantRel.call($('select[name="outDepotId"]')[0],'${detail.outDepotId}',{"type":"b,e"});
         if (depotE) {
             depotE.forEach(function(o,i){
                 $("#outDepotId option[value='" + o.value + "']").remove();
             });
         }
     } else { //消费者退货
         //加载退入仓库的下拉数据
         $module.depot.getSelectBeanByMerchantRel.call($('select[name="inDepotId"]')[0], '${detail.inDepotId}',{"type":"f"});
         //加载退出仓库的下拉数据
         $module.depot.getSelectBeanByMerchantRel.call($('select[name="outDepotId"]')[0],'${detail.outDepotId}',{"type":"b,e"});
         if (depotE) {
             depotE.forEach(function(o,i){
                 $("#outDepotId option[value='" + o.value + "']").remove();
             });
         }
     }

     var outDepotData = $module.depot.getDepotById($("#outDepotId").val());
     if (outDepotData) {
         outDepotType = outDepotData.type;
         outDepotIsTopBooks = outDepotData.isTopBooks;
     }
     var inDepotData = $module.depot.getDepotById($("#inDepotId").val());
     if (inDepotData) {
         inDepotTypeVal = inDepotData.type;
     }

     var isSameArea = $("#isSameArea").val();
     if (isSameArea == '0') {
         var returnType = $("#returnType").val();
         var inId = $("#inDepotId").val();	// 退入仓库
         var outId = $("#outDepotId").val();	// 退出仓库
   //      getMustParameter(isSameArea,outId,inId,returnType);
     }

	// 卸货港
	$module.constant.getConstantListByNameURL.call($('select[name="portDestNo"]')[0],"portDestList","${detail.portDestNo}");
	//加载客户的下拉数据
	$module.customer.loadSelectData.call($('select[name="customerId"]')[0],'${detail.customerId}');			
	//加载退货币种的下拉数据	
	$module.currency.loadSelectData.call($("select[name='currency']")[0],"");
	// 海外理货单位
	$module.constant.getConstantListByNameURL.call($('select[name="tallyingUnit"]')[0],"order_tallyingUnitList","${detail.tallyingUnit}");
	//币种赋值
	var currency = $("#currency_hidden").val() ;
	if(currency){
		$("#currency").val(currency) ;
	}
    //根据客户选择带出对应的币种
    $("#customerId").change(function(){
        var val = $(this).val() ;
        if(val){
            $module.currency.loadSelectData.call($("select[name='currency']")[0],val);
        }
    }) ;
		

	var count = 0;
    var flag = " ${empty itemCount}";
    if(flag!="true"){
        count = ${itemCount};
    }

    $(function(){
       		inDepotTypeVal = "${inDepotTypeVal}";
       		inIsInOutInstructionVal =  "${inIsInOutInstructionVal}";
       		inDepotNameVal= "${inDepotNameVal}";
	     	// 购销退货、代销退货类型中，退入仓为保税仓（并在对应公司下的“进出接口指令”标识为“是”的保税仓库）时必填，其他情况下非必填
			if("${isRequired}"==1){
				//发票号
				$("#invoiceNo_i").css("display","inline-block");
				//包装方式
				$("#packType_i").css("display","inline-block");
				//收货地址
				$("#deliveryAddr_i").css("display","inline-block");
				//唛头
				$("#mark_i").css("display","inline-block");
				//箱数
				$("#boxNum_i").css("display","inline-block");
				//头程提单号
				$("#ladingBill_i").css("display","inline-block");
				// 境外发货人
				$("#shipper_i").css("display","inline-block");
				// 装货港
				$("#portLoading_i").css("display","inline-block");
				//付款条约
				$("#payRules_i").css("display","inline-block");
				//提单毛重KG
				$("#billWeight_i").css("display","inline-block");
				//卸货港
				$("#portDestNo_i").css("display","inline-block");
				$("#tallyingUnit_i").css("display","none");
			}
			// 购销退货、代销退货类型，退入仓为卓志海外仓时必填，其他情况下非必填
			else if(returnType=='3' && inDepotTypeVal == 'c'||
					returnType=='2' && inDepotTypeVal== 'c'){
				// 海外理货单位 显示
				$("#tallyingUnit_i").css("display","inline-block");
				//发票号
				$("#invoiceNo_i").css("display","none");
				//包装方式
				$("#packType_i").css("display","none");
				//收货地址
				$("#deliveryAddr_i").css("display","none");
				//唛头
				$("#mark_i").css("display","none");
				//箱数
				$("#boxNum_i").css("display","none");
				//头程提单号
				$("#ladingBill_i").css("display","none");
				// 境外发货人
				$("#shipper_i").css("display","none");
				// 装货港
				$("#portLoading_i").css("display","none");
				//付款条约
				$("#payRules_i").css("display","none");
				//提单毛重KG
				$("#billWeight_i").css("display","none");
				//卸货港
				$("#portDestNo_i").css("display","none");
			}else{
				//发票号
				$("#invoiceNo_i").css("display","none");
				//包装方式
				$("#packType_i").css("display","none");
				//收货地址
				$("#deliveryAddr_i").css("display","none");
				//唛头
				$("#mark_i").css("display","none");
				//箱数
				$("#boxNum_i").css("display","none");
				//头程提单号
				$("#ladingBill_i").css("display","none");
				// 境外发货人
				$("#shipper_i").css("display","none");
				// 装货港
				$("#portLoading_i").css("display","none");
				//付款条约
				$("#payRules_i").css("display","none");
				//提单毛重KG
				$("#billWeight_i").css("display","none");
				//卸货港
				$("#portDestNo_i").css("display","none");
				$("#tallyingUnit_i").css("display","none");
			}
        //取消按钮
        $("#cancel-buttons").click(function(){
              $module.load.pageOrder('act=5001');
        });
        $("#packType").select2({
      		 language:"zh-CN",
      		 placeholder: '请选择',
      		 multiple: false	// 是否允许选择多个值
      		});
        var oDepotId = $("#outDepotId").val();
		if(oDepotId){
			$custom.request.ajaxSync("/depot/getDepotDetails.asyn", {"id":oDepotId}, function(result1){
				// 当类型为代销退货/购销退货两种类型中退出仓为“唯品代销仓”时则需要必填；其余情况非必填
				if(returnType=="2"&&result1.data.depotCode=='VIP001'||
						returnType=="3"&&result1.data.depotCode=='VIP001'){
					$("#poNo_i").css("display","inline-block");
				}else{
					$("#poNo_i").css("display","none");
				}
			});
		}
    });

		//提交按钮
		$("#save-buttons").click(function(){
			var url = serverAddr+"/saleReturn/modifySaleReturnOrder.asyn";
			var form = {
			};
			var json = {};
			var merchantId = $("#merchantId").val();
			var merchantName = $("#merchantName").val();
			var returnType = $("#returnType").val();
			if(!returnType){
				$custom.alert.error("请选择退货类型");
				return;
			}
			var customerId = $("#customerId").val();
			if(!customerId){
				$custom.alert.error("请选择客户");
				return;
			}
			var inDepotId = $("#inDepotId").val();
			if(!inDepotId){
				$custom.alert.error("请选择退入仓库");
				return;
			}
			var isSameArea = $("#isSameArea").val();
			if(returnType!="1" && !isSameArea){	// 消费者退货类型该字段非必填，其他类型下为必填
				$custom.alert.error("请选择是否同关区");
				return;
			}
			
			var outDepotId = $("#outDepotId").val();
			// 当“是否同关区”为同关区时，该字段必填；跨关区时为非必填(仅当类型为代销退货时,跨关区依旧必填退出仓库)；
			if(isSameArea=="1" && !outDepotId){
				$custom.alert.error("退出仓库不能为空");
				return;
			}else if(returnType=="2"&&isSameArea=="0" && !outDepotId){
				$custom.alert.error("退货类型为代销为跨关区，退出仓库不能为空");
				return;
			}
			var contractNo = $("#contractNo").val();
			if(returnType!="1" && !contractNo){	// 消费者退货类型该字段非必填，其他类型下为必填
				$custom.alert.error("报关合同号不能为空");
				return;
			}

            if (returnType == '1') {
                if(inDepotTypeVal != 'f') {
                    $custom.alert.error("退货类型为消费者退货时，退入仓仅为销毁区");
                    return;
                }

                if (outDepotId && !(outDepotType =='b' || (outDepotType == 'e' && outDepotIsTopBooks == '0'))) {
                    $custom.alert.error("退出仓库仅能为备查库、暂存仓（暂存仓且标识为非代销仓）；");
                    return;
                }
            }
            if (returnType != '1') {
                if(!(inDepotTypeVal == 'f' || inDepotTypeVal == 'a' || inDepotTypeVal == 'd' || inDepotTypeVal == 'c')) {
                    $custom.alert.error("退入仓库仅能选中转仓、保税仓、海外仓、销毁区");
                    return;
                }

                if (outDepotId && !(outDepotType =='b' || (outDepotType == 'e' && outDepotIsTopBooks == '0'))) {
                    $custom.alert.error("退出仓库仅能为备查库、暂存仓（暂存仓且标识为非代销仓）；");
                    return;
                }
            }
			
			var ladingBill = $("#ladingBill").val();	// 头程提单号 
			var invoiceNo = $("#invoiceNo").val();	// 发票号 
			var deliveryAddr = $("#deliveryAddr").val();	// 收货地址
			var boxNum = $("#boxNum").val();	// 箱数
			var portLoading = $("#portLoading").val();	// 装货港 
			var packType = $("#packType").val();	// 包装方式
			var payRules = $("#payRules").val();	// 付款条约
			var billWeight = $("#billWeight").val();	// 提单毛重
			var portDestNo = $("#portDestNo").val();	// 卸货港 
			var shipper = $("#shipper").val();	// 境外发货人 
			var mark = $("#mark").val();	// 唛头 
			var lbxNo = $("#lbxNo").val();	// LBX 
			
			
			var remark = $("#remark").val();	// 退货原因
			var currency = $("#currency").val();	// 退货币种
			var buId = $("#buId").val();	// 事业部ID
			if(!currency){
				$custom.alert.error("请选择退货币种");
				return;
			}
			if(!buId){
				$custom.alert.error("请选择事业部");
				return;
			}
			var saleOrderRelCode = $("#saleOrderRelCode").val();	// 关联销售单
			
			// 购销退货、代销退货类型，退入仓为保税仓（并在对应公司下的“进出接口指令”标识为“是”的保税仓库）时必填，其他情况下非必填
	     	 if(inDepotId){	// 退入仓库
					// 购销退货、代销退货类型，退入仓为保税仓（并在对应公司下的“进出接口指令”标识为“是”的保税仓库）时必填，其他情况下非必填
					if((returnType=='3' && inDepotTypeVal=='a'  && inIsInOutInstructionVal=='1')||
                        (returnType=='2' && inDepotTypeVal=='a'  && inIsInOutInstructionVal=='1')){
							if(!ladingBill){
								$custom.alert.error("头程提单号不能为空");
								return;
							}
							if(!invoiceNo){
								$custom.alert.error("发票号不能为空");
								return;
							}
							if(!deliveryAddr){
								$custom.alert.error("收货地址不能为空");
								return;
							}
							if(!boxNum){
								$custom.alert.error("箱数不能为空");
								return;
							}
							if(!portLoading){
								$custom.alert.error("装货港不能为空");
								return;
							}
							if(!packType){
								$custom.alert.error("包装方式不能为空");
								return;
							}
							if(!payRules){
								$custom.alert.error("付款条约不能为空");
								return;
							}
							if(!billWeight){
								$custom.alert.error("提单毛重不能为空");
								return;
							}
							if(!portDestNo){
								$custom.alert.error("卸货港不能为空");
								return;
							}
							if(!shipper){
								$custom.alert.error("境外发货人不能为空");
								return;
							}
							if(!mark){
								$custom.alert.error("唛头不能为空");
								return;
							}
					}
		      }
	     	if(outDepotId){
				if(returnType=='3'){	//当购销退货时，退出仓不能为云集代销仓！
					if(outDepotCodeVal=='ERPWMS_360_0407'){
						$custom.alert.error("当购销退货时，退出仓不能为云集代销仓！");
			             return;
					}
				}
			}
			var tallyingUnit ="";	// 海外理货单位
            // 入库仓库是海外仓 必填字段校验
            var inDepotData = $module.depot.getDepotById(inDepotId);
            var inDepotType = inDepotData.type;//退入仓库类型
            if ((returnType=="2"&&'c'==inDepotType)||
                (returnType=="3"&&'c'==inDepotType)) {
                tallyingUnit = $("#tallyingUnit").val();	// 海外理货单位
                if (!tallyingUnit) {
                    $custom.alert.error("退入仓为卓志海外仓时海外理货单位必填");
                    return;
                }
            }

	
			var itemList = [];
			var flag = true;
			/**-开始-**/
	    //商品
        var goodsList = [];
        var checkInventoryGoods = [];
        // var flag2 = true;
		var goodsArr = [];
        $("#datatable-buttons tbody tr").each(function (index, item) {
        	
        	var id = $(this).find('input[name="id"]').val();

            var seq = $(this).find('input[name="seq"]').val();

            item['id']=id;
        	var poNo = $(this).find("td").eq(2).find("input").val();// PO单号
        	var poDate = $(this).find("td").eq(3).find("input").val();// PO时间
            var outGoodsId = $(this).find("td").eq(5).find("input").val();//退出商品id
            var inGoodsId = $(this).find("td").eq(4).find("input").val();//退入商品id
            var inGoodsName = $(this).find("td").eq(6).find("input").val();//退入商品名称
            var barcode = $(this).find("td").eq(7).find("input").val();//退入商品条形码
            var price = $(this).find("td").eq(8).find("input").val();//退货商品单价
            var totalNum = $(this).find("td").eq(9).find("input").val();//退货商品数量
            var returnNum = $(this).find("td").eq(10).find("input").val();//好品数量
            var badNum = $(this).find("td").eq(11).find("input").val();//坏品数量
            var returnBatchNo = $(this).find("td").eq(12).find("input").val();//批次号
            var brandName = $(this).find("td").eq(13).find("input").val();//品牌类型
            var grossWeight = $(this).find("td").eq(14).children('input[name="grossWeight"]').val();//毛重
            var netWeight = $(this).find("td").eq(15).children('input[name="netWeight"]').val();//净重
            var boxNo = $(this).find("td").eq(16).find("input").val();//箱号
            var inCommbarcode = $(this).find("td").eq(17).find("input").val();// 退入商品标准条码
            var outCommbarcode = $(this).find("td").eq(18).find("input").val();// 退出商品标准条码
            if(returnType=="2" || returnType=="3"){	// 代销购销必填
            	if (outGoodsId == null || outGoodsId == "" || outGoodsId == undefined) {
                    $custom.alert.error("请选择退出商品！");
                    flag = false;
                    return;
                }
            }
            if (inGoodsId == null || inGoodsId == "" || inGoodsId == undefined) {
                $custom.alert.error("请选择退入商品！");
                flag = false;
                return;
            }
            if (outCommbarcode!= inCommbarcode) {
                $custom.alert.error("退入商品与退出商品标准条码不一致，退入商品:" + inGoodsName);
                flag = false;
                return;
            }
	       	 if(!price){
	       		 $custom.alert.error("请选择退货商品单价！");
	                flag = false;
	                return;
	       	 }
	       	var indexOf = price.indexOf(".");
	    	if (indexOf != -1) {
	            var count = (parseFloat(price)+"").length - 1 - indexOf;
	            if (count > 8) {
	            	$(this).val("");
	                $custom.alert.error("单价只能为8位小数");
	                return;
	            }
	        }
	      	if((!returnNum || isNaN(returnNum) ||returnNum<=0) &&(!badNum || isNaN(badNum) ||badNum<=0)){
				$custom.alert.error("好品数量和坏品数量必须是有一个值是大于0的数");
	       		flag = false;
	    		return;
			}
            if(netWeight && grossWeight){
                if (grossWeight*1.0 < netWeight*1.0) {
                    $custom.alert.error("毛重不能小于净重，退入商品:" + barcode);
                    flag = false;
                    return;
                }
            }
	       	// 当类型为购销退货时字段必填，
      		if(returnType=="3"){
      			// 品牌类型均默认为“境外品牌(其他)”
      			$(this).find("td").eq(12).find("input").val("境外品牌(其他)");
      		   	if(!brandName){
      	       		 $custom.alert.error("品牌类型不能为空");
      	    	     flag = false;
      				 return;
      	       	}
	      		 if(!grossWeight){
	   	       		 $custom.alert.error("毛重不能为空");
      	    	     flag = false;
      				 return;
	   	       	}
	      		if(!netWeight){
	 	       		 $custom.alert.error("净重不能为空");
      	    	     flag = false;
      				 return;
	 	       	}
                if (grossWeight*1.0 < netWeight*1.0) {
                    $custom.alert.error("毛重不能小于净重，退入商品:" + barcode);
                    flag = false;
                    return;
                }
	 	       if(!boxNo){
	 	       		 $custom.alert.error("箱号不能为空");
      	    	     flag = false;
      				 return;
	 	       	}
      		}
         
         	if(isExist(goodsArr, outGoodsId)) {
	       		var newArr = checkInventoryGoods.filter(function(p){
	       		  return p.goodsId === outGoodsId;
	       		});
	       		newArr[0].totalNum = parseInt(newArr[0].totalNum) +parseInt(totalNum);
	       		checkInventoryGoods.push({"goodsId":newArr[0].goodsId, "num": newArr[0].totalNum });
	       	} else {
	       		goodsArr.push(outGoodsId);
	       	 	checkInventoryGoods.push({"goodsId":outGoodsId,"num":totalNum});
	       	}

	       	// if(seq.trim().length == 0 || seq == null){
            //     flag2 = false;
            // }


            var goods = {};

            goods.seq = seq;

            goods.poNo = poNo;
            goods.poDate = poDate;
            goods.outGoodsId = outGoodsId;
            goods.inGoodsId = inGoodsId;
            goods.inGoodsName = inGoodsName;
            goods.barcode = barcode;
            goods.price = price;
            goods.totalNum = totalNum;
            goods.returnNum = returnNum;
            goods.badNum = badNum;
            goods.returnBatchNo = returnBatchNo;
            goods.brandName = brandName;
            goods.grossWeight = grossWeight;
            goods.netWeight = netWeight;
            goods.boxNo = boxNo;
            goodsList.push(goods);
        });
			/**-结束-**/
	    	if(!flag){
				return;
			}
        	if(!goodsList || goodsList.length==0){
				$custom.alert.error("至少选择一个商品");
				return;
			}
        	var orderReturnId =$("#orderReturnId").val();
        	var orderReturnCode =$("#orderReturnCode").val();
        	
			json['orderReturnId']=orderReturnId;
			json['orderReturnCode']=orderReturnCode;
			json['merchantId']=merchantId;
			json['merchantName']=merchantName;
			json['returnType']=returnType;
			json['customerId']=customerId;
			json['inDepotId']=inDepotId;
			json['isSameArea']=isSameArea;
			json['outDepotId']=outDepotId;
			json['contractNo']=contractNo;
			json['ladingBill']=ladingBill;
			json['invoiceNo']=invoiceNo;
			json['deliveryAddr']=deliveryAddr;
			json['boxNum']=boxNum;
			json['portLoading']=portLoading;
			json['packType']=packType;
			json['payRules']=payRules;
			json['billWeight']=billWeight;
			json['portDestNo']=portDestNo;
			json['shipper']=shipper;
			json['mark']=mark;
			json['lbxNo']=lbxNo;
			json['remark']=remark;
			json['currency']=currency;
			json['buId']=buId;
			json['saleOrderRelCode']=saleOrderRelCode;
			json['tallyingUnit']=tallyingUnit;			
			json['itemList'] = goodsList;
			var jsonStr= JSON.stringify(json);			
			form['json']=jsonStr;

			// 接下来进行保存
            // if (!flag2) {
             //    $custom.alert.warning("存在排序序号为空的商品，是否按现有排序提交？",function () {
             //        $custom.request.ajax(url, form, function(data){
             //            if(data.state==200){
             //                var str = data.data;
             //                $custom.alert.success(str);
             //                //重新加载页面
             //                setTimeout(function () {
             //                    $load.a(pageUrl+"5001");
             //                }, 1000);
             //            }else{
             //                if(!!data.expMessage){
             //                    $custom.alert.error(data.expMessage);
             //                }else{
             //                    $custom.alert.error(data.message);
             //                }
             //            }
             //        });
             //    })
            // }else {
                $custom.request.ajax(url, form, function(data){
                    if(data.state==200){
                        var str = data.data;
                        $custom.alert.success(str);
                        //重新加载页面
                        setTimeout(function () {
                            $load.a(pageUrl+"5001");
                        }, 1000);
                    }else{
                        if(!!data.expMessage){
                            $custom.alert.error(data.expMessage);
                        }else{
                            $custom.alert.error(data.message);
                        }
                    }
                });
            // }


		});

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
	// 退货类型选择
	 $("#returnType").change(function() {
		 var returnType = $("#returnType").val();
		 if(returnType=="1"){		//  消费者退货类型该字段非必填，其他类型下为必填
			 $("#isSameArea_i").css("display","none");
			 $("#contractNo_i").css("display","none");
				//发票号
				$("#invoiceNo_i").css("display","none");
				//包装方式
				$("#packType_i").css("display","none");
				//收货地址
				$("#deliveryAddr_i").css("display","none");
				//唛头
				$("#mark_i").css("display","none");
				//箱数
				$("#boxNum_i").css("display","none");
				//头程提单号
				$("#ladingBill_i").css("display","none");
				// 境外发货人
				$("#shipper_i").css("display","none");
				// 装货港
				$("#portLoading_i").css("display","none");
				//付款条约
				$("#payRules_i").css("display","none");
				//提单毛重KG
				$("#billWeight_i").css("display","none");
				//卸货港
				$("#portDestNo_i").css("display","none");
				// 海外理货单位
				$("#tallyingUnit_i").css("display","none");
				$("#outGoodsId_i").css("display","none");
                $("#grossWeight_i").css("display","none");
                $("#netWeight_i").css("display","none");
                $("#boxNo_i").css("display","none");
                $("#brandName_i").css("display","none");
		 }else if(returnType=="3"){    // 购销退货
             $("#contractNo_i").css("display","inline-block");
             $("#isSameArea_i").css("display","inline-block");
             $("#outGoodsId_i").css("display","inline-block");
             $("#brandName_i").css("display","inline-block");
             $("[name='brandName']").val("境外品牌(其他)");
             $("#grossWeight_i").css("display","inline-block");
             $("#netWeight_i").css("display","inline-block");
             $("#boxNo_i").css("display","inline-block");
         }else{ // 代销退货
			 $("#isSameArea_i").css("display","inline-block");
			 $("#contractNo_i").css("display","inline-block");
			 $("#outGoodsId_i").css("display","inline-block");
             $("#grossWeight_i").css("display","none");
             $("#netWeight_i").css("display","none");
             $("#boxNo_i").css("display","none");
             $("#brandName_i").css("display","none");
		 }
	 });
	//同关区选择
	 $("#isSameArea").change(function() {
		 var returnType = $("#returnType").val();
		 var isSameArea = $("#isSameArea").val();
         var inId = $("#inDepotId").val();	// 退入仓库
         var outId = $("#outDepotId").val();	// 退出仓库
		 if(isSameArea=="1"){		// 同关区
			 $("#outDepotId_i").css("display","inline-block");
		 }else if(returnType=="3"&&isSameArea=="0"){	// 当类型为购销退货且为跨关区时，字段必填
             if (businessModel == '1') { //购销-整批结算时跨关区，退出仓非必填
                 $("#outDepotId_i").css("display","none");
                 $("#outDepotId").attr("disabled","disabled");
             }
             if (businessModel == '4') { //购销-实销实结时跨关区，退出仓必填
                 $("#outDepotId_i").css("display","inline-block");
                 $("#outDepotId").removeAttr("disabled");
             }
		 }else if(returnType=="2"&&isSameArea=="0"){	// 当类型为代销退货且为跨关区时，出仓仓库字段必填
			 $("#outDepotId_i").css("display","inline-block");
		 }else{	// 跨关区
			 $("#outDepotId_i").css("display","none");
		 }
         getMustParameter(isSameArea,outId,inId,returnType);
	 });

	// 选退出仓库加载必填参数提示
    $("#outDepotId").change(function() {
        var outDepotData = $module.depot.getDepotById($("#outDepotId").val());
        if(outDepotData) {
            outDepotType = outDepotData.type;
            outDepotIsTopBooks = outDepotData.isTopBooks;
        }
    	var returnType = $("#returnType").val();
    	var inId = $("#inDepotId").val();	// 退入仓库
    	var outId = $("#outDepotId").val();	// 退出仓库
    	var isSameArea = $("#isSameArea").val();   		// 是否同关区
    	getMustParameter(isSameArea,outId,inId,returnType);
    	//重新加载事业部
        $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null,null, outId,inId);
    });
     $("#inDepotId").change(function() {
        var inDepotData = $module.depot.getDepotById($("#outDepotId").val());
        if (inDepotData) {
            inDepotTypeVal = inDepotData.type;
        }
   		var returnType = $("#returnType").val();
       	var inId = $("#inDepotId").val();	// 退入仓库
       	var outId = $("#outDepotId").val();	// 退出仓库
       	var isSameArea = $("#isSameArea").val();   		// 是否同关区
       	getMustParameter(isSameArea,outId,inId,returnType);
      	//重新加载事业部
        $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null,null, outId,inId);
    });
 }); 
 function getMustParameter(isSameArea,outId,inId,returnType){
		//检查是否涉及香港仓，是则添加理货单位
		if(inId){	// 退入仓库
			$custom.request.ajax("/depot/checkDepotMerchantRel.asyn", {"depotId":inId}, function(result1){
				inDepotTypeVal = result1.data.type;
				inIsInOutInstructionVal = result1.data.isInOutInstruction;
				inDepotNameVal=result1.data.depotName;
				// 购销退货、代销退货类型，退入仓为保税仓（并在对应公司下的“进出接口指令”标识为“是”的保税仓库）时必填，其他情况下非必填
				if((returnType=='3' && inDepotTypeVal=='a' && inIsInOutInstructionVal=='1')||
                    (returnType=='2' && inDepotTypeVal=='a' && inIsInOutInstructionVal=='1')){
					//发票号
					$("#invoiceNo_i").css("display","inline-block");
					//包装方式
					$("#packType_i").css("display","inline-block");
					//收货地址
					$("#deliveryAddr_i").css("display","inline-block");
					//唛头
					$("#mark_i").css("display","inline-block");
					//箱数
					$("#boxNum_i").css("display","inline-block");
					//头程提单号
					$("#ladingBill_i").css("display","inline-block");
					// 境外发货人
					$("#shipper_i").css("display","inline-block");
					// 装货港
					$("#portLoading_i").css("display","inline-block");
					//付款条约
					$("#payRules_i").css("display","inline-block");
					//提单毛重KG
					$("#billWeight_i").css("display","inline-block");
					//卸货港
					$("#portDestNo_i").css("display","inline-block");
					$("#tallyingUnit_i").css("display","none");
				}else if((returnType=='3' && inDepotTypeVal == 'c')||
                    (returnType=='2'&&  inDepotTypeVal== 'c')){		// 购销退货、代销退货类型，退入仓为卓志海外仓时必填，其他情况下非必填
					// 海外理货单位 显示
					$("#tallyingUnit_i").css("display","inline-block");
					//发票号
					$("#invoiceNo_i").css("display","none");
					//包装方式
					$("#packType_i").css("display","none");
					//收货地址
					$("#deliveryAddr_i").css("display","none");
					//唛头
					$("#mark_i").css("display","none");
					//箱数
					$("#boxNum_i").css("display","none");
					//头程提单号
					$("#ladingBill_i").css("display","none");
					// 境外发货人
					$("#shipper_i").css("display","none");
					// 装货港
					$("#portLoading_i").css("display","none");
					//付款条约
					$("#payRules_i").css("display","none");
					//提单毛重KG
					$("#billWeight_i").css("display","none");
					//卸货港
					$("#portDestNo_i").css("display","none");
				}else{
					//发票号
					$("#invoiceNo_i").css("display","none");
					//包装方式
					$("#packType_i").css("display","none");
					//收货地址
					$("#deliveryAddr_i").css("display","none");
					//唛头
					$("#mark_i").css("display","none");
					//箱数
					$("#boxNum_i").css("display","none");
					//头程提单号
					$("#ladingBill_i").css("display","none");
					// 境外发货人
					$("#shipper_i").css("display","none");
					// 装货港
					$("#portLoading_i").css("display","none");
					//付款条约
					$("#payRules_i").css("display","none");
					//提单毛重KG
					$("#billWeight_i").css("display","none");
					//卸货港
					$("#portDestNo_i").css("display","none");
					$("#tallyingUnit_i").css("display","none");
					$("#lbxNo_i").css("display","none");
				}
			});
		}
		if(outId){
			// 清空退出商品
			$("input[name='outGoodsId']").val('');
			$(".outGoodsNo").html("");
			$custom.request.ajaxSync("/depot/getDepotDetails.asyn", {"id":outId}, function(result1){
				outDepotCodeVal = result1.data.depotCode;
				// 当类型为代销退货/购销退货两种类型中退出仓为“唯品代销仓”时则需要必填；其余情况非必填
				if(returnType=="2"&& outDepotCodeVal=='VIP001'||
						returnType=="3"&& outDepotCodeVal=='VIP001'){
					$("#poNo_i").css("display","inline-block");
				}else{
					$("#poNo_i").css("display","none");
				}
				if(returnType=='3'){	//当购销退货时，退出仓不能为云集代销仓！
					if(outDepotCodeVal=='ERPWMS_360_0407'){
						$custom.alert.error("当购销退货时，退出仓不能为云集代销仓！");
			             return;
					}
				}
			});
		}
		//清空商品列表
		$("#table-list tr:gt(0)").remove();
		$("#unNeedIds").val('');
}
 //添加行
 function addtr() {
     $("#datatable-buttons").append('<tr>' +
         '<td class="tc nowrap"><i class="fi-plus" onclick="addtr();"></i>&nbsp;&nbsp;&nbsp;<i class="fi-minus" onclick="deltr(this);"></i></td>' +
         ' <td><input type="text" style="width: 80px;" class="form-control" name="seq" /></td>' +
         ' <td><input type="text" style="width: 80px;" class="form-control" name="poNo" /></td>' +
         ' <td><input type="text" style="width: 80px;" class="form-control" name="poDate"  /></td>' +
         '<td><button type="button"class="btn btn-find waves-effect waves-light btn_default"onclick="initGoodsBefore(this, \'inDepotId\')">选择商品</button></td>' +
         '<td><button type="button"class="btn btn-find waves-effect waves-light btn_default indepot_goods"onclick="initGoodsBefore(this, \'outDepotId\');">选择商品</button></td>'+ 
         '<td></td>' +
         '<td></td>' +
         '<td><input type="text" class="form-control" name="price" onkeyup="value=value.replace(/[^\\d.]/g,\'\')" onblur ="value=parseFloat(value).toFixed(8)"></td>' +
         '<td><input type="text" class="form-control" name="totalNum" onkeyup="value=value.replace(/[^\\d.]/g,\'\')"  maxlength="9" readonly="readonly"></td>'+
         '<td><input type="text" class="form-control" name="returnNum" onchange="setTotalNum(this);" onkeyup="value=value.replace(/[^\\d.]/g,\'\')" maxlength="9" value="0"></td>' +
         '<td><input type="text" class="form-control" name="badGoodsNum" onchange="setTotalNum(this);" onkeyup="value=value.replace(/[^\\d.]/g,\'\')" maxlength="9" value="0"> </td>'+
         '<td><input type="text" style="width: 50px;" class="form-control" name="returnBatchNo"/></td>' +
         '<td><input type="text"  class="form-control" name="brandName"></td>' +
         '<td><input type="text"  class="form-control" name="grossWeight" onkeyup="value=value.replace(/[^\\d.]/g,\'\')" onchange="calculateWeight()" maxlength="11"></td>' +
         '<td><input type="text"  class="form-control" name="netWeight" onkeyup="value=value.replace(/[^\\d.]/g,\'\')" maxlength="11"></td>' +
         '<td><input type="text" class="form-control" name="boxNo"></td> ' +
         '</tr>');
     calculateWeight()
 }
 /**当选择退入商品时，校验退入仓库是否选择(退入仓库一定要先选，退出仓库可以为空；
 退出商品货号根据选择的退出仓库做过滤，若无退出仓则不弹框，不可选商品)*/
 function initGoodsBefore(obj, depotType) {
	 var isSameArea = $("#isSameArea").val();   		// 是否同关区
     var inDepotId = $("#inDepotId").val();
     var outDepotId = $("#outDepotId").val();
     var returnType = $("#returnType").val();
     var inGoodsId = $(obj).parents("tr").children("td").eq(4).find("input").val();//退入商品id

     if(returnType==null || returnType==""){
		 $custom.alert.error("请先选择退货类型！");
         return;
	 }
     if(depotType=="inDepotId"){	// 如果是选择退入商品货号
		 if(inDepotId==null || inDepotId=="") {
        	 $custom.alert.error("请先选择退入仓库！");
             return;
         }
		// 如果是选择退入商品货号并且是同关区时(退出仓库必填)
		if(isSameArea=="1"){
			 if(outDepotId==null || outDepotId=="") {
	        	 $custom.alert.error("请先选择退出仓库！");
	             return;
		     }
		}
	}
    if(depotType == 'outDepotId' && (inGoodsId==null || inGoodsId=="")) {
        $custom.alert.error("请先选择退入商品！");
        return;
    }
    if(depotType=="outDepotId"){// 如果是选择退出商品货号
        if(outDepotId==null || outDepotId=="") {
       	 $custom.alert.error("请先选择退出仓库！");
            return;
	     }
	}
     $erpPopup.orderGoods.init('3',obj, depotType);
 }
	// 计算退货商品数量
 function setTotalNum(thisObj){
 	 	 var name = $(thisObj).attr("name");
 		 if(name=='returnNum'){	// 如果修改了好品数量
 			 var totalNum=parseInt($(thisObj).val())+parseInt($(thisObj).parent("td").next().children('input[name="badGoodsNum"]').val());
 			 $(thisObj).parent("td").prev().children('input[name="totalNum"]').val(totalNum);
     	 }else if(name=='badGoodsNum'){		// 如果修改了坏品数量
     		var totalNum=parseInt($(thisObj).val())+parseInt($(thisObj).parent("td").prev() .children('input[name="returnNum"]').val());
     		$(thisObj).parent("td").prev().prev().children('input[name="totalNum"]').val(totalNum);
     	 }
         var grossWeight = $(thisObj).parents("tr").children('td').eq(13).children('input[name="grossWeight-hidden"]').val();//毛重
         var netWeight = $(thisObj).parents("tr").children('td').eq(14).children('input[name="netWeight-hidden"]').val();//净重

         if (!grossWeight) {
             grossWeight = 0;
         }
         if (!netWeight) {
             netWeight = 0;
         }
         var transferNum = $(thisObj).parents("tr").children('td').eq(8).children('input[name="totalNum"]').val();//数量
         if (!transferNum) {
             transferNum = 1;
         }
         var totalGrossWeight = parseFloat(grossWeight)*transferNum;
         var totalNetWeight = parseFloat(netWeight)*transferNum;
         $(thisObj).parents("tr").children('td').eq(13).children('input[name="grossWeight"]').val(totalGrossWeight.toFixed(3));
         $(thisObj).parents("tr").children('td').eq(14).children('input[name="netWeight"]').val(totalNetWeight.toFixed(3));
         calculateWeight();
 }
 //删除行
 function deltr(obj) {
     var rows = $(obj).parent("td").parent("tr").parent("tbody").find("tr").length;
     if (rows > 1) {
         $(obj).parent("td").parent("tr").remove();
     } else {
         $(obj).parent("td").parent("tr").replaceWith('<tr>' +
                 '<td class="tc nowrap"><i class="fi-plus" onclick="addtr();"></i>&nbsp;&nbsp;&nbsp;<i class="fi-minus" onclick="deltr(this);"></i></td>' +
                 ' <td><input type="text" style="width: 80px;" class="form-control" name="poNo" /></td>' +
                 ' <td><input type="text" style="width: 80px;" class="form-control" name="poDate"  /></td>' +
                 '<td><button type="button"class="btn btn-find waves-effect waves-light btn_default"onclick="initGoodsBefore(this, \'inDepotId\')">选择商品</button></td>' +
                 '<td><button type="button"class="btn btn-find waves-effect waves-light btn_default indepot_goods"onclick="initGoodsBefore(this, \'outDepotId\');">选择商品</button></td>'+ 
                 '<td></td>' +
                 '<td></td>' +
                 '<td><input type="text" class="form-control" name="price" onkeyup="value=value.replace(/[^\\d.]/g,\'\')" onblur ="value=parseFloat(value).toFixed(8)"></td>' +
                 '<td><input type="text" class="form-control" name="totalNum" onkeyup="value=value.replace(/[^\\d.]/g,\'\')"  maxlength="9" readonly="readonly"></td>'+
                 '<td><input type="text" class="form-control" name="returnNum" onchange="setTotalNum(this);" onkeyup="value=value.replace(/[^\\d.]/g,\'\')" maxlength="9" value="0"></td>' +
                 '<td><input type="text" class="form-control" name="badGoodsNum" onchange="setTotalNum(this);" onkeyup="value=value.replace(/[^\\d.]/g,\'\')" maxlength="9" value="0"> </td>'+
                 '<td><input type="text" style="width: 50px;" class="form-control" name="returnBatchNo"/></td>' +
                 '<td><input type="text"  class="form-control" name="brandName"></td>' +
                 '<td><input type="text"  class="form-control" name="grossWeight" onkeyup="value=value.replace(/[^\\d.]/g,\'\')" onchange="calculateWeight()"  maxlength="11"></td>' +
                 '<td><input type="text"  class="form-control" name="netWeight" onkeyup="value=value.replace(/[^\\d.]/g,\'\')" maxlength="11"></td>' +
                 '<td><input type="text" class="form-control" name="boxNo"></td> ' +
                 '</tr>');
     }
     calculateWeight()
 }
 function isExist(goodsArr, goodsId) {
		for(var i=0;i<goodsArr.length;i++){
			  if(goodsArr[i]==goodsId){	// 商品重复了
				  return true;
			  }else{
				  return false;
			  }							
		}	
 }

</script>
