<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<jsp:include page="/WEB-INF/views/common.jsp"/>
<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
        <form id="add-form" action="/transfer/saveTransfer.asyn">
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
                                            <li class="breadcrumb-item"><a href="#">调拨管理</a></li>
                                            <li class="breadcrumb-item"><a
                                                    href="javascript:$module.load.pageOrder('act=8001');">调拨订单列表</a>
                                            </li>
                                            <li class="breadcrumb-item"><a href="#">编辑调拨订单</a></li>
                                        </ol>
                                    </div>
                                </div>
                                <!--  title   end  -->
                                <!--  title   基本信息   start -->
                                <div class="title_text">基本信息</div>
                        
                                <div class="edit_box mt20">
                                    <div class="edit_item">
                                        <input type="hidden" id="orderId" value="${transferOrder.id}">
                                        <label class="edit_label">调拨订单号：</label>
                                        <div class="no_edit">${transferOrder.code}</div>
                                    </div>
                                    <div class="edit_item"></div>
                                    <div class="edit_item"></div>
                                </div>
                                <div class="edit_box">
                                    <div class="edit_item">
                                        <label class="edit_label"><i class="red">*</i>调出仓库：</label>
                                        <select id="outDepotId" name="outDepotId"
                                                class="edit_input" required reqMsg="调出仓库不能为空！">
                                            <option value="">请选择</option>
                                        </select>
                                    </div>
                                    <div class="edit_item">
                                        <label class="edit_label"><i class="red">*</i> 调入仓库：</label>
                                        <select id="inDepotId" name="inDepotId"
                                                class="edit_input" required reqMsg="调入仓库不能为空！">
                                            <option value="">请选择</option>
                                        </select>
                                    </div>
                                   <div class="edit_item">
	                                      <label class="edit_label"><i class="red" id="isSameAreaLabel">*</i>是否同关区：</label>
	                                        <div class="edit_input">
		                                        <select id="isSameArea" class="form-control">
											  		<option value="">请选择</option>
											  		<option value="1" <c:if test="${transferOrder.isSameArea == '1'}"> selected = 'selected'</c:if>>是</option>
											  		<option value="0" <c:if test="${transferOrder.isSameArea == '0'}"> selected = 'selected'</c:if>>否</option>	                                          
		                                        </select>
	                                        </div>
	                                 </div>
                                </div>
                                <div class="edit_box">
                                    <div class="edit_item">
                                        <label class="edit_label"><i class="red">*</i> 事业部：</label>
                                        <select id="buId" name="buId" required reqMsg="事业部不能为空！"
                                                class="edit_input">
                                        </select>
                                    </div>
                                    <div class="edit_item">
                                        <label class="edit_label">调出公司：</label>
                                        <div class="no_edit">${user.merchantName}</div>
                                    </div>
                                    <div class="edit_item">
                                        <label class="edit_label"><i class="red" name="inCustomerIdLabel"></i> 调入客户：</label>
                                        <select id="inCustomerId" name="inCustomerId" class="edit_input">
                                            <option value="">请选择</option>
                                            <option value='${user.merchantId}'>${user.merchantName}</option>
                                        </select>
                                    </div>
                                </div>
                           
                               
                                <div class="edit_box">
                                    <div class="edit_item">
                                        <label class="edit_label"><i class="red" name="invoiceNoLabel"></i> 发票号：</label>
                                        <input type="text" parsley-type="text" class="edit_input"
                                               id="invoiceNo" value="${transferOrder.invoiceNo }"
                                               maxlength="50">
                                    </div>
                                     <div class="edit_item">
                                        <label class="edit_label"><i class="red" name="packTypeLabel"></i> 包装方式：</label>
                                         <input type="text" parsley-type="text" class="edit_input" id="packType"
                                                value="${transferOrder.packType }">
                                    </div>
                                    <div class="edit_item">
                                        <label class="edit_label"><i class="red">*</i> 箱数：</label>
                                        <input type="text" parsley-type="text" class="edit_input"
                                               id="cartons" value="${transferOrder.cartons }" maxlength="20" onkeyup="value=value.replace(/[^\d]/g,'')"  onblur="setPackType();">
                                    </div>
                                </div>
                                <div class="edit_box">
                                    <div class="edit_item">
                                        <label class="edit_label"><i class="red">*</i> 运输方式：</label>
                                        <div class="edit_input">
                                            <select class="form-control" name="transport" id="transport" required reqMsg="运输方式不能为空！">
                                                <option value="">请选择</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="edit_item">
                                        <label class="edit_label"><i class="red" name="standardCaseTeuLabel"></i> 标准箱TEU：</label>
                                        <input type="text" parsley-type="text" class="edit_input"
                                               id="standardCaseTeu" value="${transferOrder.standardCaseTeu}" >
                                    </div>
                                    <div class="edit_item">
                                        <label class="edit_label"><i class="red" name="trainNumberLabel"></i> 车次：</label>
                                        <input type="text" parsley-type="text" class="edit_input" value="${transferOrder.trainNumber}"
                                               id="trainNumber" >
                                    </div>
                                </div>
                                <div class="edit_box">
                                    <div class="edit_item">
                                        <label class="edit_label"><i class="red" name="carrier"></i> 承运商：</label>
                                        <input type="text" parsley-type="text" class="edit_input" value="${transferOrder.carrier}"
                                               id="carrier" >
                                    </div>
                                    <div class="edit_item">
                                        <label class="edit_label"><i class="red">*</i> 托数：</label>
                                        <input type="text" parsley-type="text" class="edit_input" onkeyup="value=value.replace(/[^\d]/g,'')"
                                               id="torrNum" required reqMsg="托数不能为空！" value="${transferOrder.torrNum}"  onblur="setPackType();">
                                    </div>
                                    <div class="edit_item">
                                        <label class="edit_label"><i class="red">*</i> 出库地点：</label>
                                        <input type="text" parsley-type="text" class="edit_input" value="${transferOrder.outdepotAddr}"
                                               id="outdepotAddr" required reqMsg="出库地点不能为空！">
                                    </div>
                                </div>
                               <div class="edit_box">
                                    <div class="edit_item">
                                        <label class="edit_label"><i class="red" name="firstLadingBillNoLabel"></i> 提单号：</label>
                                        <input type="text"  class="edit_input" id="firstLadingBillNo" value="${transferOrder.firstLadingBillNo }" maxlength="50">
                                    </div>
                                    <div class="edit_item">
                                        <label class="edit_label"><i class="red">*</i> 收货地址：</label>
                                        <input type="text" class="edit_input" id="receivingAddress" required reqMsg="收货地址不能为空！"
                                               value="${transferOrder.receivingAddress }" maxlength="200">
                                    </div>
                                    <div class="edit_item">
	                                   <div class="edit_item">
	                                        <label class="edit_label"><i class="red" name="consigneeUsernameLabel"></i>收货人姓名：</label>
	                                        <input type="text"  class="edit_input" id="consigneeUsername" maxlength="90" value="${transferOrder.consigneeUsername}">
	                                    </div>
                                    </div>
                                   
                                </div>
                                <div>
                                   <div class="edit_box">
	                                    <div class="edit_item">
	                                        <label class="edit_label"><i class="red" name="countryLabel"></i>国家：</label>
	                                        <input type="text"  class="edit_input" id="country" maxlength="50" value="${transferOrder.country}">
	                                    </div>
	                                    <div class="edit_item" id="HKDiv2-null" >
	                                     
                                        </div>
	                                    <div class="edit_item" id="HKDiv2" style="display:none;">
	                                        <label class="edit_label"><i class="red">*</i>目的地：</label>
	                                        <div class="edit_input">
		                                        <select id="destination" class="form-control">
											  		<option value="">请选择</option>
											  		<option value="GZJC01">广州机场</option>
										  			<option value="HP01">黄埔状元谷</option>
	                                            	<option value="HK01">香港本地</option>
		                                        </select>
	                                        </div>
	                                    </div>
	                                     <div class="edit_item" id="HKDiv1-null" >
                                    	 </div>
	                                     <div class="edit_item" id="HKDiv1" style="display:none;">
	                                         <label class="edit_label"><i class="red">*</i> 海外仓理货单位：</label>
	                                         <div class="edit_input">
	                                          <select id="tallyingUnit" class="form-control">
												  <option value="">请选择</option>
												  <option value="02">件</option>
	                                              <option value="00">托盘</option>
												  <option value="01">箱</option>
	                                          </select>
	                                         </div>
	                                     </div>
                                </div>
                                </div>
                                <div class="edit_box">
                                    <div class="edit_item">
                                        <label class="edit_label"> 唛头：</label>
                                        <input type="text"  class="edit_input" id="mark" maxlength="50" value="${transferOrder.mark}">
                                    </div>
                                    <div class="edit_item">
                                        <label class="edit_label"> 境外发货人：</label>
                                        <input type="text"  class="edit_input" id="shipper" maxlength="50" value="${transferOrder.shipper}">
                                    </div>
                                    <div class="edit_item" >
                                      <label class="edit_label"> <i class="red" name="poNoLabel"></i>PO号：</label>
                                        <input type="text"  class="edit_input" id="poNo" value="${transferOrder.poNo}" maxlength="200">
                                    </div>
                                </div>
                                
	                            <div class="edit_box">	                                    	                                    
	                                    
	                                    <div class="edit_item">
                                        	<label class="edit_label"><i class="red" name="contractNoLabel"></i> 合同号：</label>
                                        	<input type="text" parsley-type="text" class="edit_input"
                                               id="contractNo" value="${transferOrder.contractNo}" maxlength="100">
                                    	</div>
	                                    
	                                   <div class="edit_item">
	                                        <label class="edit_label"><i class="red" name="depotScheduleIdLabel">*</i>调入仓地址：</label>
	                                        <div class="edit_input">
		                                        <select id="depotScheduleId" name="depotScheduleId" disabled="disabled" class="form-control">
		                                            <option value="">请选择</option>
		                                        </select>
	                                        </div>
	                                    </div>
	                                    <div class="edit_item">
                                            <label class="edit_label"> 提单毛重：</label>
                                            <div class="edit_input">
                                                <input type="text" class="form-control" onblur="calcItemGrossWeight();" onkeyup="amount5Val(this)"
                                                       id="billWeight" name="billWeight" value="${transferOrder.billWeight}">
                                            </div>
                                        </div>
	                              </div>
                                <div class="edit_box">
                                    <div class="edit_item">
                                        <label class="edit_label"><i class="red">*</i> 付款条约：</label>
                                        <input type="text" parsley-type="text" class="edit_input" required reqMsg="付款条约不能为空！"
                                               id="payRules" value="${transferOrder.payRules}">
                                    </div>
                                    <div class="edit_item">
                                        <label class="edit_label"><i class="red">*</i> 托盘材质：</label>
                                        <div class="edit_input">
                                            <select class="form-control" name="palletMaterial" id="palletMaterial" required reqMsg="托盘材质不能为空！" onblur="setPackType();">
                                                <option value="">请选择</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="edit_item">
                                        <label class="edit_label"><i class="red" name="portLoadingLabel"></i> 装货港：</label>
                                        <input type="text" parsley-type="text" class="edit_input"
                                               id="portLoading" value="${transferOrder.portLoading}">
                                    </div>
                                </div>
                                <div class="edit_box">
                                    <div class="edit_item">
                                        <label class="edit_label"><i class="red">*</i> 卸货港：</label>
                                        <input type="text" parsley-type="text" class="edit_input" required reqMsg="卸货港不能为空！"
                                               id="unloadPort" value="${transferOrder.unloadPort}">
                                    </div>
                                    <div class="edit_item">
                                        <label class="edit_label"> 入库关区：</label>
                                        <div class="edit_input">
                                            <select class="form-control" name="inCustomsId" id="inCustomsId" >
                                            </select>
                                        </div>
                                    </div>
                                    <div class="edit_item">
                                        <label class="edit_label"> 出库关区：</label>
                                        <div class="edit_input">
                                            <select class="form-control" name="outCustomsId" id="outCustomsId" >
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <div class="edit_box">
                                    <div class="edit_item">
                                        <label class="edit_label"><i class="red" name="lbxNoLabel"></i> LBX单号：</label>
                                        <input type="text" parsley-type="text" class="edit_input"
                                               id="lbxNo" value="${transferOrder.lbxNo}" maxlength="20">
                                    </div>
                                    <div class="edit_item" >
                                        <label class="edit_label" >备注：</label>
                                        <textarea rows="" cols="" class="edit_input" id="remark"
                                                  maxlength="400">${transferOrder.remark}</textarea>
                                    </div>
                                    <div class="edit_item"></div>
                                </div>
                                <!--  title   基本信息  start -->
                                <!--   商品信息  start -->
                                <div class="col-12 ml10">
			                       <div class="row">
			                         <div class="col-10">
			                             <div class="title_text">商品信息</div>
			                        </div>
			                        <div class="col-1 mt10">
			                            <button type="button" class="btn btn-find waves-effect waves-light btn_default"  onclick='$m3013.init()'>商品导入</button>
			                        </div>
                                    <div class="col-1 mt10">
                                       <button type="button" class="btn btn-find waves-effect waves-light btn_default"  onclick="$m3014.init(${transferOrder.id})">装箱明细导入</button>
                                    </div>
                                   </div>
			                   </div>
                                <div class="mt20 table-responsive">
                                    <table id="datatable-buttons" class="table table-striped" cellspacing="0"
                                           width="100%">
                                        <thead>
                                        <tr>
                                            <!--  <th>序号</th> -->
                                            <th>操作</th>
                                            <th>序号</th>
                                            <th><i class="red">*</i>调出商品编号</th>
                                            <th>调出商品货号</th>
                                            <th>调出商品名称</th>
                                            <th>调出单位</th>
                                            <th><i class="red">*</i> 调出数量</th>
                                            <th><i class="red">*</i>调入商品编号</th>
                                            <th>调入商品货号</th>
                                            <th>调入商品名称</th>
                                            <th>调入单位</th>
                                            <th><i class="red">*</i>调入数量</th>
                                            <th>品牌类型</th>
                                            <th><i class="red" name="priceLabel"></i>调拨单价</th>
                                            <th><i class="red">*</i>毛重(kg)</th>
                                            <th><i class="red">*</i>净重(kg)</th>
                                            <th><i class="red" name="contNoLabel"></i>箱&nbsp;号&nbsp;&nbsp;&nbsp;</th>
                                            <th><i class="red" name="bargainnoLabel"></i>合同号&nbsp;&nbsp;&nbsp;</th>
                                            <th>条形码</th>
                                            <th>托盘号</th>
                                            <th>箱数</th>
                                        </tr>
                                        </thead>
                                        <tbody id="item-table">
                                        <c:forEach items="${orderItem }" var="item">
                                            <tr>
                                                <td class="tc nowrap">
                                                    <i class="fi-plus" onclick="addtr();"></i>&nbsp;&nbsp;&nbsp;<i
                                                        class="fi-minus" onclick="deltr(this);"></i>
                                                </td>
                                                <td><input class="form-control" name="seq" value="${item.seq}"></td>
                                                <td><input type="hidden" class="form-control" name="outGoodsId"
                                                           value="${item.outGoodsId}">${item.outGoodsCode}
                                                    <button type="button" class="btn btn-find waves-effect waves-light btn_default"
                                                            onclick="initGoodsBefore(this,'outDepotId');">选择商品
                                                    </button>
                                                </td>
                                                <td>${item.outGoodsNo}</td>
                                                <td><input type="hidden"  class="form-control" value="${item.outCommbarcode}">${item.outGoodsName}</td>
                                                <td>
	                                               <select name="outUnit" class="edit_input" style="width: 60px;">
													  <option value="02" ${item.outUnit=='02'?"selected='selected'":''}>件</option>
	                                                  <option value="00" ${item.outUnit=='00'?"selected='selected'":''}>托盘</option>
													  <option value="01" ${item.outUnit=='01'?"selected='selected'":''}>箱</option>
	                                               </select>
	                                        </td>
											<td><input type="text" class="form-control" name="transferNum" value="${item.transferNum}"
                                                           onchange="setTransferNum(this);" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"></td>
                                            <td><input type="hidden" class="form-control" name="inGoodsId"
                                                           value="${item.inGoodsId}">${item.inGoodsCode}
                                                    <button type="button" class="btn btn-find waves-effect waves-light btn_default indepot_goods"
                                                            onclick="initGoodsBefore(this,'inDepotId');">选择商品
                                                    </button>
                                            </td>
                                            <td>${item.inGoodsNo}</td>
                                            <td><input type="hidden"  class="form-control" value="${item.inCommbarcode}">${item.inGoodsName}</td>
	                                        <td>
	                                               <select name="inUnit" class="edit_input" style="width: 60px;">
													  <option value="02" ${item.inUnit=='02'?"selected='selected'":''}>件</option>
	                                                  <option value="00" ${item.inUnit=='00'?"selected='selected'":''}>托盘</option>
													  <option value="01" ${item.inUnit=='01'?"selected='selected'":''}>箱</option>
	                                               </select>
	                                         </td>
                                             <td><input type="text" class="form-control" name="inTransferNum"
                                                          onchange="setTransferNum(this);" value="${item.inTransferNum}"
                                                           onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"></td>
                                             <td><input type="text" class="form-control" name="brandName"
                                                           value="${item.brandName}" readonly="readonly"></td>
                                             <td><input type="text" class="form-control" name="price"
                                                           value="${item.price}"
                                                           onkeyup="value=value.replace(/[^\d^\.]/g,'')" maxlength="11"></td>
                                             <td><input type="hidden"  name="grossWeight-hidden" value="${item.grossWeight}">
                                                 <input type="text" class="form-control" name="grossWeight"
                                                           value="${item.grossWeightSum}"  onchange="calculateWeight()"
                                                           onkeyup="amount5Val(this)"></td>
                                              <td><input type="hidden"  name="netWeight-hidden" value="${item.netWeight}">
                                                  <input type="text" class="form-control" name="netWeight"
                                                           value="${item.netWeightSum}" onchange="sumTotal()"
                                                            onkeyup="amount5Val(this)"></td>
                                                <td><input type="text" class="form-control" name="contNo"
                                                           value="${item.contNo}"></td>
                                                <td><input type="text" class="form-control" name="bargainno"
                                                           value="${item.bargainno}"></td>
                                                <td><input type="text" class="form-control" name="outBarcode" value="${item.outBarcode}" style="width:100px;"></td>
                                                <td><input type="text" class="form-control" name="torrNo" value="${item.torrNo}"></td>
                                                <td><input type="text" class="form-control" name="boxNum" value="${item.boxNum}" ></td>
                                            </tr>
                                        </c:forEach>

                                        </tbody>
                                    </table>
                                </div>
                                <!--   商品信息  end -->
                                <div class="form-row mt20">
                                    <div class="col-2"></div>
                                    <div class="col-2">
                                        <span>调出总数量：</span>
                                        <span id="transferOutTotalNum"></span>
                                    </div>
                                    <div class="col-2">
                                        <span>调入总数量：</span>
                                        <span id="transferInTotalNum"></span>
                                    </div>
                                    <div class="col-2">
                                        <span>总毛重：</span>
                                        <span id="totalGrossWeight"></span>
                                    </div>
                                    <div class="col-2">
                                        <span>总净重：</span>
                                        <span id="totalNetWeight"></span>
                                    </div>
                                </div>
                                <div class="form-row m-t-50">
                                    <div class="col-12">
                                        <div class="row">
                                            <div class="col-4">
                                            </div>
                                            <div class="col-4">
                                                <button type="button"
                                                        class="btn btn-info waves-effect waves-light btn_default"
                                                        id="save-temp-buttons" onclick="saveTempTransfer();">保 存
                                                </button>
                                                <a class="btn btn-light waves-effect waves-light btn_default"
                                                   href="javascript:void(0);" onclick="cancel()">取 消</a>
                                                <shiro:hasPermission name="transfer_saveTransferOrder">
	                                                <button type="button" class="btn btn-find waves-effect waves-light btn_default"
	                                                        id="save-buttons" onclick="saveAndAudit();">保存并审核
	                                                </button>
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
                </div>
            </div>
        </form>
        <!-- 弹窗开始 -->
        <jsp:include page="/WEB-INF/views/modals/3011-V2.jsp"/>
        <jsp:include page="/WEB-INF/views/modals/3013.jsp"/>
        <jsp:include page="/WEB-INF/views/modals/3014.jsp"/>
        <!-- 弹窗结束 -->
    </div>
    <!-- container -->
</div>

</div> <!-- content -->
<script type="text/javascript">
    var param = '';
    var XGCId = null;
    var ZTId = null;
    var outDepotType = null;
    var inDepotType = null;
    var productRestriction = null;
    var inDepotIsInOutInstruction = '${inDepotIsInOutInstruction}';
    var outDepotIsInOutInstruction = '${outDepotIsInOutInstruction}';
    var outIsTopBooks = null;
    var existSeq = null;
    var TBindex = null;
    var priceDeclareRatio = '${priceDeclareRatio}';
    $(document).ready(function () {

        //选择调出、调入仓库
        var outDepotId = "${transferOrder.outDepotId}";
        var inDepotId = "${transferOrder.inDepotId}";
        var inCustomerId = "${transferOrder.inCustomerId}";
        var packType = "${transferOrder.packType}";
        var tallyingUnit = "${transferOrder.tallyingUnit}";
        var destination = "${transferOrder.destination}";
        var buId = "${transferOrder.buId}";
        var transport = "${transferOrder.transport}"
        var palletMaterial = "${transferOrder.palletMaterial}"
        $("#packType").val(packType);//包装方式
        $("#tallyingUnit").val(tallyingUnit);
        $("#destination").val(destination);
        $module.constant.getConstantListByNameURL.call($('select[name="transport"]')[0],"transportList",transport);
        $module.constant.getConstantListByNameURL.call($('select[name="palletMaterial"]')[0],"order_torrpacktypeList",palletMaterial);
        //加载事业部
        $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],buId,null, outDepotId,inDepotId);
        //加载调出仓
        $module.depot.getSelectBeanByMerchantRel.call($("select[name='outDepotId']")[0],outDepotId, {"type":"a,b,c,e"});
        var outDepotData = $module.depot.getDepotById($("#outDepotId").val());
        outDepotType = outDepotData.type;
        outIsTopBooks = outDepotData.isTopBooks;
        var inDepotData = $module.depot.getDepotById(inDepotId);
        inDepotType = inDepotData.type;
        if (outDepotType == 'a') {
            $module.depot.getSelectBeanByMerchantRel.call($("select[name='inDepotId']")[0],inDepotId, {"type" : "a,c"});
            $("#inDepotId option[value='" + $("#outDepotId").val() + "']").remove();
        } else if (outDepotType == 'b') {
            $module.depot.getSelectBeanByMerchantRel.call($("select[name='inDepotId']")[0],inDepotId, {"type" : "e"});
        } else if (outDepotType == 'c') {
            $module.depot.getSelectBeanByMerchantRel.call($("select[name='inDepotId']")[0],inDepotId, {"type" : "a"});
        } else if (outDepotType == 'e' && outIsTopBooks == '1') {
            $module.depot.getSelectBeanByMerchantRel.call($("select[name='inDepotId']")[0],inDepotId, {"type" : "f"});
        } else if (outDepotType == 'e' && outIsTopBooks == '0') {
            $module.depot.getSelectBeanByMerchantRel.call($("select[name='inDepotId']")[0],inDepotId, {"type" : "b"});
        }

        $custom.request.ajax("/depot/checkDepotMerchantRel.asyn", {"depotId" : inDepotId}, function (result) {
            if (result.state == '200') {
                productRestriction = result.data.productRestriction;
                inDepotIsInOutInstruction = result.data.isInOutInstruction;
            }
        });

        $custom.request.ajax("/depot/checkDepotMerchantRel.asyn", {"depotId" : outDepotId}, function (result) {
            if (result.state == '200') {
                outDepotIsInOutInstruction = result.data.isInOutInstruction;
            }
        });

        //根据出库仓库加载出库关区
        $module.depot.getSelectBeanByDepotCustomsRel.call($("select[name='outCustomsId']")[0],outDepotId,'${transferOrder.outCustomsId }');
        //根据入库仓库加载入库关区
        $module.depot.getSelectBeanByDepotCustomsRel.call($("select[name='inCustomsId']")[0],inDepotId,'${transferOrder.inCustomsId }');
        //加载客户
        $('#inCustomerId').val(inCustomerId);
        //重新加载select2
        $(".goods_select2").select2({
            language: "zh-CN",
            placeholder: '请选择',
            allowClear: true
        });
        getMustParameter();
        setUnit();//设置理货单位
        sumTotal();
        calculateWeight();
//        removeChooseGoodsBtn();
    });

    //添加行
    function addtr() {
        $("#datatable-buttons").append('<tr>' +
            '<td class="tc nowrap"><i class="fi-plus" onclick="addtr();"></i>&nbsp;&nbsp;&nbsp;<i class="fi-minus" onclick="deltr(this);"></i></td>' +
            '<td><input class="form-control" name="seq" ></td>'+
            '<td><button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick="initGoodsBefore(this,\'outDepotId\');">选择商品</button></td>' +
            '<td></td>' +
            '<td></td>' +
            '<td>'+
	            '<select name="outUnit" class="edit_input" style="width: 60px;">'+
	                '<option value="02">件</option>'+
					'<option value="00">托盘</option>'+
	                '<option value="01">箱</option>'+
				'</select>'+
	        '</td>'+ 
            '<td><input type="text"  class="form-control" onchange="setTransferNum(this);" name="transferNum" onkeyup="value=value.replace(/[^\\d]/g,\'\')" maxlength="9"></td>' +
            '<td><button type="button" class="btn btn-find waves-effect waves-light btn_default indepot_goods" onclick="initGoodsBefore(this,\'inDepotId\');">选择商品</button></td>' +
            '<td></td>' +
            '<td></td>' +
            '<td>'+
	            '<select name="inUnit" class="edit_input" style="width: 60px;">'+
	                '<option value="02">件</option>'+
					'<option value="00">托盘</option>'+
	                '<option value="01">箱</option>'+
				'</select>'+
	        '</td>'+ 
            '<td><input type="text"  class="form-control" onchange="setTransferNum(this);" name="inTransferNum" onkeyup="value=value.replace(/[^\\d]/g,\'\')" maxlength="9"></td>' +
            '<td><input type="text"  class="form-control" name="brandName"></td>' +
            '<td><input type="text"  class="form-control" name="price" onkeyup="value=value.replace(/[^\\d.]/g,\'\')" maxlength="11"></td>' +
            '<td><input type="text"  class="form-control" name="grossWeight" onchange="calculateWeight()" onkeyup="amount5Val(this)"></td>' +
            '<td><input type="text"  class="form-control" name="netWeight" onchange="sumTotal()" onkeyup="amount5Val(this)"></td>' +
            '<td><input type="text"  class="form-control" name="contNo"></td>' +
            '<td><input type="text"  class="form-control" name="bargainno"></td>' +
            '<td><input type="text"  class="form-control" name="outBarcode" style="width:100px;"></td>' +
            '<td><input type="text"  class="form-control" name="torrNo"></td>' +
            '<td><input type="text"  class="form-control" name="boxNum"></td>' +
            '</tr>');
 	        setUnit();//设置理货单位
    }

    //删除行
    function deltr(obj) {
        // var delSeq = $(obj).parent("td").next().find("input").val();
        var rows = $(obj).parent("td").parent("tr").parent("tbody").find("tr").length;
        if (rows > 1) {
            $(obj).parent("td").parent("tr").remove();
            /*$("#datatable-buttons tbody tr").each(function (index, item) {
                var seq = $(this).find("td").eq(1).find("input").val();
                if (seq && delSeq && seq > delSeq) {
                    $(this).find("td").eq(1).find("input").val(seq-1);
                }
            });*/
        } else {
            $(obj).parent("td").parent("tr").replaceWith('<tr>' +
                '<td class="tc nowrap"><i class="fi-plus" onclick="addtr();"></i>&nbsp;&nbsp;&nbsp;<i class="fi-minus" onclick="deltr(this);"></i></td>' +
                '<td><input class="form-control" name="seq" ></td>'+
                '<td><button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick="initGoodsBefore(this,\'outDepotId\');">选择商品</button></td>' +
                '<td></td>' +
                '<td></td>' +
                '<td>'+
	                '<select name="outUnit" class="edit_input" style="width: 60px;">'+
	                    '<option value="02">件</option>'+
						'<option value="00">托盘</option>'+
	                    '<option value="01">箱</option>'+
					'</select>'+
	            '</td>'+
		        '<td><input type="text"  class="form-control" onchange="setTransferNum(this);" name="transferNum" onkeyup="value=value.replace(/[^\\d]/g,\'\')" maxlength="9"></td>' +
                '<td><button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick="initGoodsBefore(this,\'inDepotId\');">选择商品</button></td>' +
                '<td></td>' +
                '<td></td>' +
                '<td>'+
		            '<select name="inUnit" class="edit_input" style="width: 60px;">'+
		                  '<option value="02">件</option>'+
				  		  '<option value="00">托盘</option>'+
		                  '<option value="01">箱</option>'+
			     '</select>'+
		        '</td>'+ 
                '<td><input type="text"  class="form-control" onchange="setTransferNum(this);" name="inTransferNum" onkeyup="value=value.replace(/[^\\d]/g,\'\')" maxlength="9"></td>' +
                '<td><input type="text"  class="form-control" name="brandName"></td>' +
                '<td><input type="text"  class="form-control" name="price" onkeyup="value=value.replace(/[^\\d.]/g,\'\')" maxlength="11"></td>' +
                '<td><input type="text"  class="form-control" name="grossWeight" onkeyup="amount5Val(this)"></td>' +
                '<td><input type="text"  class="form-control" name="netWeight" onchange="sumTotal()" onkeyup="amount5Val(this)"></td>' +
                '<td><input type="text"  class="form-control" name="contNo"></td>' +
                '<td><input type="text"  class="form-control" name="bargainno"></td>' +
                '<td><input type="text"  class="form-control" name="outBarcode" style="width:100px;"></td>' +
                '<td><input type="text"  class="form-control" name="torrNo"></td>' +
                '<td><input type="text"  class="form-control" name="boxNum"></td>' +
                '</tr>');
        }
        setUnit();//设置理货单位
        calculateWeight();
        sumTotal();
    }
        
    //加载必填参数提示
    $("#outDepotId").change(function() {
        var outDepotId = $("#outDepotId").val();
        var outDepotData = $module.depot.getDepotById($("#outDepotId").val());
        if(outDepotData) {
            outDepotType = outDepotData.type;
            outIsTopBooks = outDepotData.isTopBooks;
        }
        if (outDepotType == 'a') {
            $module.depot.getSelectBeanByMerchantRel.call($("select[name='inDepotId']")[0],null, {"type" : "a,c"});
            if (outDepotId) {
                $("#inDepotId option[value='" + $("#outDepotId").val() + "']").remove();
            }
        } else if (outDepotType == 'b') {
            $module.depot.getSelectBeanByMerchantRel.call($("select[name='inDepotId']")[0],null, {"type" : "e"});
        } else if (outDepotType == 'c') {
            $module.depot.getSelectBeanByMerchantRel.call($("select[name='inDepotId']")[0],null, {"type" : "a"});
        } else if (outDepotType == 'e' && outIsTopBooks == '1') {
            $module.depot.getSelectBeanByMerchantRel.call($("select[name='inDepotId']")[0],null, {"type" : "f"});
        } else if (outDepotType == 'e' && outIsTopBooks == '0') {
            $module.depot.getSelectBeanByMerchantRel.call($("select[name='inDepotId']")[0],null, {"type" : "b"});
        }
        if (outDepotId) {
            //获取调出仓库公司关联关系
            $custom.request.ajax("/depot/checkDepotMerchantRel.asyn", {"depotId" : outDepotId}, function (result) {
                if (result.state == '200') {
                    outDepotIsInOutInstruction = result.data.isInOutInstruction;
                }
            });
        }

        //根据出库仓库加载出库关区
        $module.depot.getSelectBeanByDepotCustomsRel.call($("select[name='outCustomsId']")[0],outDepotId,'');

        getMustParameter();
        $("#isSameArea").val('');
        setUnit();

        //当调出仓库发生改变时，清空所有的商品信息
        $("#datatable-buttons tbody tr").each(function (index, item) {
            $(this).remove();
        });
        addtr();
        //加载事业部
        $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null,null, outDepotId,null);
    });
    $("#inDepotId").change(function() {
        var outDepotId = $("#outDepotId").val();
        var inDepotId = $("#inDepotId").val();
        var inDepotData = $module.depot.getDepotById($("#inDepotId").val());
        if(inDepotData) {
            inDepotType = inDepotData.type;
        }
        getMustParameter();
        $("#isSameArea").val('');


        if (inDepotId) {
            //获取调入仓库公司关联关系
            $custom.request.ajax("/depot/checkDepotMerchantRel.asyn", {"depotId" : inDepotId}, function (result) {
                if (result.state == '200') {
                    productRestriction = result.data.productRestriction;
                    inDepotIsInOutInstruction = result.data.isInOutInstruction;
                }
            });
        }
        //根据出库仓库加载出库关区
        $module.depot.getSelectBeanByDepotCustomsRel.call($("select[name='inCustomsId']")[0],inDepotId,'');


        //当调入仓库发生改变，清空调入商品信息
        $("#datatable-buttons tbody tr").each(function (index, item) {

            var goodsId = $(this).find("td").eq(7).find("input").val();
            if (goodsId) {
                $(this).find("td").eq(7).find("input").val('');//调入商品id
                $(this).find("td").eq(7).text('');//调入商品id
                $(this).find("td").eq(7).html('<button type="button" class="btn btn-find waves-effect waves-light btn_default indepot_goods" onclick="initGoodsBefore(this,\'inDepotId\');">选择商品</button>');//调入商品id
                $(this).find("td").eq(8).text('');//调入商品id
                $(this).find("td").eq(9).text('');//调入商品id
                $(this).find("td").eq(10).find("select").val('02');//调入单位
                // $(this).find("td").eq(10).find("input").val('');//调入数量
                $(this).find("td").eq(12).find("input").val('');//品牌类型
                $(this).find("td").eq(13).find("input").val('');//采购单价
                $(this).find("td").eq(14).find("input").val('');//毛重
                $(this).find("td").eq(15).find("input").val('');//净重
                $(this).find("td").eq(16).find("input").val('');//箱号
                $(this).find("td").eq(17).find("input").val('');//合同号
                $(this).find("td").eq(18).find("input").val('');//备注
            }
        });
        setUnit();
        //加载事业部
        $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null,null, outDepotId,inDepotId);
    });
    $("#isSameArea").change(function() {
        getMustParameter();
    });

    function getMustParameter(){

        // 根据仓库id获取仓库信息
        var inDepotId = $("#inDepotId").val();//调入仓库
        var inDepotData=null; //入库仓信息

        if(outDepotType=='c'||inDepotType=='c'){
              $("#HKDiv1").show();
              $("#HKDiv1-null").hide();
        }else{
              $("#HKDiv1").hide();
              $("#HKDiv1-null").show();
        }
        //调出仓为香港仓目的地显示
        if(outDepotType=='c'){
              $("#HKDiv2").show();
              $("#HKDiv2-null").hide();
              XGCId = $("#outDepotId").val();
        }else{
            $("#HKDiv2").hide();
            $("#HKDiv2-null").show();
        }

        // 如果出入库仓需要下推指令 是否同关区必填
        if((inDepotIsInOutInstruction=='1' || outDepotIsInOutInstruction == '1') ||
            (outDepotType=='a' && inDepotType == 'a')){
            $('#isSameArea').removeAttr("disabled");
            $('#isSameAreaLabel').text('*');
            $('#isSameArea').attr("required", "");
            $('#isSameArea').attr("reqMsg", "是否同关区不能为空");
        }else{
            // $("#isSameArea").val('');
            $('#isSameAreaLabel').text('');
            $('#isSameArea').removeAttr("required");
            $('#isSameArea').removeAttr("reqMsg");
            // $('#isSameArea').attr("disabled","disabled");
        }

        //调入仓为备查仓时显示 显示 仓库地址
        if(inDepotType == 'b'){
            $('#depotScheduleId').removeAttr("disabled");
             $module.depotSchedule.loadSelectByDepotId.call($('select[name="depotScheduleId"]')[0],inDepotId,"${transferOrder.depotScheduleId}");
        }else{
            $("#depotScheduleId").val('');
            $('#depotScheduleId').attr("disabled","disabled");
        }

        //清空
        var paramStr = "inCustomerIdLabel,contractNoLabel,invoiceNoLabel,packTypeLabel,cartonsLabel,firstLadingBillNoLabel,priceLabel"+
                         "destinationLabel,consigneeUsernameLabel,countryLabel,depotScheduleIdLabel,poNoLabel,bargainnoLabel, contNoLabel";
        var paramArr = paramStr.split(",");
        $.each(paramArr,function(index,value){
            $('[name='+value+']').text('');
        });
        validSetting(paramStr, true);

        var outDepotId = $("#outDepotId").val();
        var inDepotId = $("#inDepotId").val();
        var isSameArea = $("#isSameArea").val();

        if(outDepotId==null||outDepotId==''||outDepotId==undefined||inDepotId==null||inDepotId==''||inDepotId==undefined) return false;
        var purl=serverAddr + "/transfer/getMustParameter.asyn";
        $custom.request.ajax(purl,{"outDepotId":outDepotId,"inDepotId":inDepotId,"isSameArea":isSameArea},function(result){
            param = result.data;
            if(param!=''){
                var paramArr = param.split(",");
                $.each(paramArr,function(index,value){
                    $('[name='+value+']').text('*');
                });
            }
        });
    }

    $("#tallyingUnit").change(function(){
    	setUnit();
    })

    //运输方式
    $("#transport").change(function () {
        var transport = $("#transport").val();
        //当运输方式为海运、空运时必填(若在当前场景下提单号必填则不作处理)
        if (param.indexOf("firstLadingBillNoLabel") == -1) {
            if (transport == '1' || transport == '2') {
                $('[name=firstLadingBillNoLabel]').text('*');
                $('#firstLadingBillNo').attr("required", "");
                $('#firstLadingBillNo').attr("reqMsg", "提单号不能为空");
            } else {
                $('[name=firstLadingBillNoLabel]').text('');
                $('#firstLadingBillNo').removeAttr("required");
                $('#firstLadingBillNo').removeAttr("reqMsg");
            }
        }
        //当运输方式为陆运、港到仓拖车时必填
        if (transport == '3' || transport == '4') {
            $('[name=trainNumberLabel]').text('*');
            $('#trainNumber').attr("required", "");
            $('#trainNumber').attr("reqMsg", "车次不能为空");
        } else {
            $('[name=trainNumberLabel]').text('');
            $('#trainNumber').removeAttr("required");
            $('#trainNumber').removeAttr("reqMsg");
        }
        //当运输方式为海运时必填
        if (transport == '2') {
            $('[name=standardCaseTeuLabel]').text('*');
            $('#standardCaseTeu').attr("required", "");
            $('#standardCaseTeu').attr("reqMsg", "标准箱TEU不能为空");
        } else {
            $('[name=standardCaseTeuLabel]').text('');
            $('#standardCaseTeu').removeAttr("required");
            $('#standardCaseTeu').removeAttr("reqMsg");
        }
    })

    //设置调出、调入单位联动
    function setUnit(){
        var tallyingUnit = $("#tallyingUnit").val();//海外仓理货单位
        //调出仓为海外仓调出单位默认为海外仓理货单位且不可编辑
        if(outDepotType=='c'){
           $("select[name='outUnit']").each(function(index,item) {
              $(this).val(tallyingUnit);
              $(this).attr("disabled","disabled");
           });
        }else{
           $("select[name='outUnit']").each(function(index,item) {
              $(this).removeAttr("disabled");
           });
        }

        //调入仓为海外仓调入单位默认为海外仓理货单位且不可编辑
        if(inDepotType=='c'){
           $("select[name='inUnit']").each(function(index,item) {
              $(this).val(tallyingUnit);
              $(this).attr("disabled","disabled");
           });
            $("select[name='outUnit']").each(function(index,item) {
                $(this).val('02');
                $(this).attr("disabled","disabled");
            });
        }else{
           $("select[name='inUnit']").each(function(index,item) {
              $(this).removeAttr("disabled");
           });
        }

        //禁用/恢复调出数量文本框
        $("input[name='inTransferNum']").each(function(index,item){
              if((outDepotType=='c'||inDepotType=='c')&&tallyingUnit!='02'){
                  $(this).removeAttr("disabled");
              }else{
                   $(this).attr("disabled","disabled");
              }
        });
    }
   
    function setTransferNum(thisObj){
    	 var name = $(thisObj).attr("name");
    	 var tallyingUnit = $("#tallyingUnit").val();//海外仓理货单位
    	 if(outDepotType=='c'||inDepotType=='c'){
    		 if(name=='transferNum'&&tallyingUnit=='02'){
        		 $(thisObj).parent("td").next().next().next().next().next()
    	            .children('input[name="inTransferNum"]').val($(thisObj).val());
        	 }else if(name=='inTransferNum'&&tallyingUnit=='02'){
        		 $(thisObj).parent("td").prev().prev().prev().prev().prev()
    	            .children('input[name="transferNum"]').val($(thisObj).val());
        	 }
    	 }else{
    		 if(name=='transferNum'){
        		 $(thisObj).parent("td").next().next().next().next().next()
    	            .children('input[name="inTransferNum"]').val($(thisObj).val());
        	 }else if(name=='inTransferNum'){
        		 $(thisObj).parent("td").prev().prev().prev().prev().prev()
    	            .children('input[name="transferNum"]').val($(thisObj).val());
        	 }
    	 }
        var grossWeight = $(thisObj).parents("tr").children('td').eq(14).children('input[name="grossWeight-hidden"]').val();//毛重
        var netWeight = $(thisObj).parents("tr").children('td').eq(15).children('input[name="netWeight-hidden"]').val();//净重

        if (!grossWeight) {
            grossWeight = 0;
        }
        if (!netWeight) {
            netWeight = 0;
        }
        var transferNum = $(thisObj).parents("tr").children('td').eq(11).children('input[name="inTransferNum"]').val();//数量
        if (!transferNum) {
            transferNum = 1;
        }
        var totalGrossWeight = parseFloat(grossWeight)*transferNum;
        var totalNetWeight = parseFloat(netWeight)*transferNum;
        $(thisObj).parents("tr").children('td').eq(14).children('input[name="grossWeight"]').val(totalGrossWeight.toFixed(5));
        $(thisObj).parents("tr").children('td').eq(15).children('input[name="netWeight"]').val(totalNetWeight.toFixed(5));
        calculateWeight();
        sumTotal();
    }

    //判断select中是否存在值为value的项
    function isExistOption(id,value) {
        var isExist = false;
        var count = $('#' + id).find('option').length;
        for (var i = 0; i < count; i++) {
            if ($('#' + id).get(0).options[i].text == value) {
                isExist = true;
                break;
            }
        }
        return isExist;
    }

    //保存，保存时只校验仓库、商品是否有值，这两个字段用户有输入值即可保存不做其他任何校验
    function saveTempTransfer() {

        var orderId = $("#orderId").val();//调拨单id
        var inDepotId = $("#inDepotId").val();//调入仓库
        var buId = $("#buId").val();//事业部
        var inDepotName = $("#inDepotId").find("option:selected").text();
        var outDepotId = $("#outDepotId").val();//调出仓库
        var outDepotName = $("#outDepotId").find("option:selected").text();
        var inCustomerId = $("#inCustomerId").val();//调入客户id
        var inCustomerName = $("#inCustomerId").find("option:selected").text();//调入客户名称
        var contractNo = $("#contractNo").val();//合同号
        var lbxNo = $("#lbxNo").val();//合同号
        var invoiceNo = $("#invoiceNo").val();//发票号
        var packType = $("#packType").val();//包装方式
        var cartons = $("#cartons").val();//箱数
        var firstLadingBillNo = $("#firstLadingBillNo").val();//头程提单号
        var receivingAddress = $("#receivingAddress").val();//收货地址
        var consigneeUsername = $("#consigneeUsername").val();//收货人姓名
        var destination = $("#destination").val();//目的地
        var country = $("#country").val();//国家
        var tallyingUnit = $("#tallyingUnit").val();//理货单位
        var mark = $("#mark").val();//唛头
        var shipper = $("#shipper").val();//境外发货人
        var outBarcode = $("#outBarcode").val();//调出条形码
        var boxNum = $("#boxNum").val();//箱数
        var torrNo = $("#torrNo").val();//托盘号
        var isSameArea=$("#isSameArea").val();//调入仓地址
        var depotScheduleId=$("#depotScheduleId").val();// 仓库附表id
        var depotScheduleAddress=$("#depotScheduleId").find("option:selected").text();// 调入仓地址
        var poNo = $("#poNo").val();//PO号
        poNo=poNo.toUpperCase();
        var billWeight = $("#billWeight").val();
        var trainNumber = $("#trainNumber").val();
        var standardCaseTeu = $("#standardCaseTeu").val();
        var torrNum = $("#torrNum").val();
        var carrier = $("#carrier").val();
        var outdepotAddr = $("#outdepotAddr").val();
        var transport = $("#transport").val();
        var payRules = $("#payRules").val();
        var palletMaterial = $("#palletMaterial").val();
        var portLoading = $("#portLoading").val();
        var unloadPort = $("#unloadPort").val();
        var inCustomsId = $("#inCustomsId").val();
        var outCustomsId = $("#outCustomsId").val();

        //检查必填参数
        if (outDepotId == null || outDepotId == "" || outDepotId == undefined) {
            $custom.alert.error("请选择调出仓库！");
            return false;
        }
        if (inDepotId == null || inDepotId == "" || inDepotId == undefined) {
            $custom.alert.error("请选择调入仓库！");
            return false;
        }

        if (!buId) {
            $custom.alert.error("请选择事业部！");
            return false;
        }

        var json = {};
        json.isSameArea=isSameArea;
        json.depotScheduleId=depotScheduleId;
        json.depotScheduleAddress=depotScheduleAddress;
        json.orderId = orderId;
        json.buId = buId;
        json.inDepotId = inDepotId;
        json.inDepotName = inDepotName;
        json.outDepotId = outDepotId;
        json.outDepotName = outDepotName;
        json.inCustomerId = inCustomerId;
        json.inCustomerName = inCustomerName;
        json.contractNo = contractNo;
        json.lbxNo = lbxNo;
        json.invoiceNo = invoiceNo;//发票号
        json.packType = packType;//包装方式
        json.cartons = cartons;//箱数
        json.firstLadingBillNo = firstLadingBillNo;//头程提单号
        json.receivingAddress = receivingAddress;//收货地址
        json.consigneeUsername = consigneeUsername;//收货人姓名
        json.destination = destination;//目的地
        json.country = country;//国家
        if(tallyingUnit!=null&&tallyingUnit!=undefined&&tallyingUnit!=''){
            json.tallyingUnit = tallyingUnit;
        }
        json.mark = mark;
        json.shipper = shipper;
        json.outBarcode = outBarcode;
        json.boxNum = boxNum;
        json.torrNo = torrNo;
        json.poNo = poNo;
        json.billWeight = billWeight;
        json.trainNumber = trainNumber;
        json.standardCaseTeu = standardCaseTeu;
        json.torrNum = torrNum;
        json.carrier = carrier;
        json.outdepotAddr = outdepotAddr;
        json.transport = transport;
        json.payRules = payRules;
        json.palletMaterial = palletMaterial;
        json.portLoading = portLoading;
        json.unloadPort = unloadPort;
        json.inCustomsId = inCustomsId;
        json.outCustomsId = outCustomsId;

        //商品
        var goodsList = [];
        var check = true;
        $("#datatable-buttons tbody tr").each(function (index, item) {
            var seq = $(this).find("td").eq(1).find("input").val();
            var outGoodsId = $(this).find("td").eq(2).find("input").val();//调出商品id
            var outUnit = $(this).find("td").eq(5).find("select").val();//调出单位
            var transferNum = $(this).find("td").eq(6).find("input").val();//调出数量
            var inGoodsId = $(this).find("td").eq(7).find("input").val();//调入商品id
            var inUnit = $(this).find("td").eq(10).find("select").val();//调入单位
            var inTransferNum = $(this).find("td").eq(11).find("input").val();//调入数量
            var brandName = $(this).find("td").eq(12).find("input").val();//品牌类型
            var price = $(this).find("td").eq(13).find("input").val();//采购单价
            var grossWeight = $(this).find("td").eq(14).children('input[name="grossWeight-hidden"]').val();//毛重
            var grossWeightSum = $(this).find("td").eq(14).children('input[name="grossWeight"]').val();//总毛重
            var netWeight = $(this).find("td").eq(15).children('input[name="netWeight-hidden"]').val();//净重
            var netWeightSum = $(this).find("td").eq(15).children('input[name="netWeight"]').val();//总净重
            var contNo = $(this).find("td").eq(16).find("input").val();//箱号
            var bargainno = $(this).find("td").eq(17).find("input").val();//合同号
            var outBarcode = $(this).find("td").eq(18).find("input").val();//调出条形码
            var torrNo = $(this).find("td").eq(19).find("input").val();//托盘号
            var boxNum = $(this).find("td").eq(20).find("input").val();//箱数
            if (outGoodsId == null || outGoodsId == "" || outGoodsId == undefined) {
                $custom.alert.error("请选择调出商品！");
                check = false;
                return;
            }

            if (outUnit == null || outUnit == "" || outUnit == undefined) {
                outUnit = "";
            }

            if (inUnit == null || inUnit == "" || inUnit == undefined) {
                inUnit = "";
            }

            var goods = {};
            goods.seq = seq;
            goods.outGoodsId = outGoodsId;
            goods.outUnit = outUnit;
            goods.transferNum = transferNum;//调出数量
            goods.inGoodsId = inGoodsId;
            goods.inUnit = inUnit;
            goods.inTransferNum = inTransferNum;//调入数量
            goods.brandName = brandName;//品牌类型
            goods.price = price;//采购单价
            goods.grossWeight = grossWeight;//毛重
            goods.grossWeightSum = grossWeightSum;//毛重
            goods.netWeight = netWeight;
            goods.netWeightSum = netWeightSum;
            goods.contNo = contNo;//箱号
            goods.bargainno = bargainno;//合同号
            goods.outBarcode = outBarcode;//调出条形码
            goods.boxNum = boxNum;//箱数
            goods.torrNo = torrNo;//托盘号
            goodsList.push(goods);
        });
        if (check == false) return false;
        //禁用保存按钮
        $("#save-temp-buttons").attr('disabled','disabled');
        json.goodsList = goodsList;
        var jsonStr = JSON.stringify(json);
        var url = serverAddr + "/transfer/saveTempTransferOrder.asyn?token=" + token;
        $custom.request.ajaxSync(url,{"json":jsonStr},function(result){
            if (result.state == '200') {
                if (result.data.code == '00') {
                    $custom.alert.success("保存成功！");
                    setTimeout(function () {
                        $module.load.pageOrder('act=8001');
                    }, 1000);
                } else {
                    var message = result.data.message;
                    if (message) {
                        $custom.alert.error(message);
                    } else {
                        $custom.alert.error("保存失败！");
                    }
                    $("#save-buttons").attr('disabled',false);
                }
            } else {
                var message = result.message;
                var expMessage = result.expMessage;
                if (message != null && message != '' && message != undefined) {
                    $custom.alert.error(expMessage);
                }else if (message != null && message != '' && message != undefined) {
                    $custom.alert.error(message);
                } else {
                    $custom.alert.error("保存失败！");
                }
                $("#save-temp-buttons").attr('disabled',false);
            }
        });


    }

    //保存并审核：1.二次确认是否审核 2.校验必填项 3.校验库存可用量 4.提交保存
    function saveAndAudit() {
        $custom.alert.warning("确定保存并审核该调拨订单？",function(){
            var buId = $("#buId").val();//事业部
            var orderId = $("#orderId").val();//调拨单id
            var inDepotId = $("#inDepotId").val();//调入仓库
            var inDepotName = $("#inDepotId").find("option:selected").text();
            var outDepotId = $("#outDepotId").val();//调出仓库
            var outDepotName = $("#outDepotId").find("option:selected").text();
            var inCustomerId = $("#inCustomerId").val();//调入客户id
            var inCustomerName = $("#inCustomerId").find("option:selected").text();//调入客户名称
            var contractNo = $("#contractNo").val();//合同号
            var lbxNo = $("#lbxNo").val();//合同号
            var invoiceNo = $("#invoiceNo").val();//发票号
            var packType = $("#packType").val();//包装方式
            var cartons = $("#cartons").val();//箱数
            var firstLadingBillNo = $("#firstLadingBillNo").val();//头程提单号
            var receivingAddress = $("#receivingAddress").val();//收货地址
            var consigneeUsername = $("#consigneeUsername").val();//收货人姓名
            var destination = $("#destination").val();//目的地
            var country = $("#country").val();//国家
            var tallyingUnit = $("#tallyingUnit").val();//理货单位
            var mark = $("#mark").val();//唛头
            var shipper = $("#shipper").val();//境外发货人
            var outBarcode = $("#outBarcode").val();//调出条形码
            var boxNum = $("#boxNum").val();//箱数
            var torrNo = $("#torrNo").val();//托盘号
            var isSameArea=$("#isSameArea").val();//调入仓地址
            var depotScheduleId=$("#depotScheduleId").val();// 仓库附表id
            var depotScheduleAddress=$("#depotScheduleId").find("option:selected").text();// 调入仓地址
            var poNo = $("#poNo").val();//PO号
            poNo=poNo.toUpperCase();
            var billWeight = $("#billWeight").val();
            var trainNumber = $("#trainNumber").val();
            var standardCaseTeu = $("#standardCaseTeu").val();
            var torrNum = $("#torrNum").val();
            var carrier = $("#carrier").val();
            var outdepotAddr = $("#outdepotAddr").val();
            var transport = $("#transport").val();
            var payRules = $("#payRules").val();
            var palletMaterial = $("#palletMaterial").val();
            var portLoading = $("#portLoading").val();
            var unloadPort = $("#unloadPort").val();
            var inCustomsId = $("#inCustomsId").val();
            var outCustomsId = $("#outCustomsId").val();

            //不同场景必填项
            validSetting(param, false);
            //检查必填参数
            if(!$checker.verificationRequired()){
                return ;
            }

            if (outDepotType =='a' && !(inDepotType == 'a' || inDepotType == 'c')) {
                $custom.alert.error("当调出仓库为保税仓时，调入仓库仅能选海外仓和保税仓！");
                return false;
            }

            if (outDepotType =='c' && inDepotType != 'a') {
                $custom.alert.error("当调出仓库为海外仓时，调入仓库仅能选保税仓！");
                return false;
            }

            if (outDepotType =='b' && inDepotType != 'e') {
                $custom.alert.error("当调出仓库为备查库时，调入仓库仅为暂存仓！");
                return false;
            }

            if (outDepotType =='e' && outIsTopBooks == '1' && inDepotType != 'f') {
                $custom.alert.error("当调出仓库为暂存仓且标识为代销仓时，调入仓库仅能选销毁区！");
                return false;
            }

            if (outDepotType =='e' && outIsTopBooks == '0' && inDepotType != 'b') {
                $custom.alert.error("当调出仓库为暂存仓且标识为非代销仓时，调入仓库仅能选备查库！");
                return false;
            }

            if (outDepotType=='a' && inDepotType == 'a') {
                if (!isSameArea) {
                    $custom.alert.error("是否同关区不能为空");
                    return false;
                }
            }

            // 当入库仓是备案仓时  仓库地址 和 是否同关区不能为空
            if (inDepotType == 'b') {
                if (!depotScheduleId) {
                    $custom.alert.error("入库仓为备查仓时,调入仓地址不能为空");
                    return false;
                }
            }

            //检查是否涉及香港仓，是则添加理货单位
            if(outDepotType=='c'||inDepotType=='c'){
                if (!tallyingUnit) {
                    $custom.alert.error("请选择理货单位！");
                    return false;
                }
            }

            if (outDepotType=='c') {
                if (!destination) {
                    $custom.alert.error("请输入目的地");
                    return false;
                }
            }

            var json = {};
            json.isSameArea=isSameArea;
            json.depotScheduleId=depotScheduleId;
            json.depotScheduleAddress=depotScheduleAddress;
            json.orderId = orderId;
            json.inDepotId = inDepotId;
            json.buId = buId;
            json.inDepotName = inDepotName;
            json.outDepotId = outDepotId;
            json.outDepotName = outDepotName;
            json.inCustomerId = inCustomerId;
            json.inCustomerName = inCustomerName;
            json.contractNo = contractNo;
            json.lbxNo = lbxNo;
            json.invoiceNo = invoiceNo;//发票号
            json.packType = packType;//包装方式
            json.cartons = cartons;//箱数
            json.firstLadingBillNo = firstLadingBillNo;//头程提单号
            json.receivingAddress = receivingAddress;//收货地址
            json.consigneeUsername = consigneeUsername;//收货人姓名
            json.destination = destination;//目的地
            json.country = country;//国家
            if(tallyingUnit!=null&&tallyingUnit!=undefined&&tallyingUnit!=''){
                json.tallyingUnit = tallyingUnit;
            }
            json.mark = mark;
            json.shipper = shipper;
            json.outBarcode = outBarcode;
            json.boxNum = boxNum;
            json.torrNo = torrNo;
            json.poNo = poNo;
            json.billWeight = billWeight;
            json.trainNumber = trainNumber;
            json.standardCaseTeu = standardCaseTeu;
            json.torrNum = torrNum;
            json.carrier = carrier;
            json.outdepotAddr = outdepotAddr;
            json.transport = transport;
            json.payRules = payRules;
            json.palletMaterial = palletMaterial;
            json.portLoading = portLoading;
            json.unloadPort = unloadPort;
            json.inCustomsId = inCustomsId;
            json.outCustomsId = outCustomsId;

            //商品
            var goodsList = [];
            var checkInventoryGoods = [];
            var check = true;
            var isSeqNull = false;
            $("#datatable-buttons tbody tr").each(function (index, item) {
                var seq = $(this).find("td").eq(1).find("input").val();
                var outGoodsId = $(this).find("td").eq(2).find("input").val();//调出商品id
                var outGoodsNo = $(this).find("td").eq(3).text();//调出商品货号
                var outCommbarcode = $(this).find("td").eq(4).find("input").val();//调出商品标准条码
                var outUnit = $(this).find("td").eq(5).find("select").val();//调出单位
                var transferNum = $(this).find("td").eq(6).find("input").val();//调出数量
                var inGoodsId = $(this).find("td").eq(7).find("input").val();//调入商品id
                var inCommbarcode = $(this).find("td").eq(9).find("input").val();//调入商品标准条码
                var inUnit = $(this).find("td").eq(10).find("select").val();//调入单位
                var inTransferNum = $(this).find("td").eq(11).find("input").val();//调入数量
                var brandName = $(this).find("td").eq(12).find("input").val();//品牌类型
                var price = $(this).find("td").eq(13).find("input").val();//采购单价
                var grossWeight = $(this).find("td").eq(14).children('input[name="grossWeight-hidden"]').val();//毛重
                var grossWeightSum = $(this).find("td").eq(14).children('input[name="grossWeight"]').val();//总毛重
                var netWeight = $(this).find("td").eq(15).children('input[name="netWeight-hidden"]').val();//净重
                var netWeightSum = $(this).find("td").eq(15).children('input[name="netWeight"]').val();//总净重
                var contNo = $(this).find("td").eq(16).find("input").val();//箱号
                var bargainno = $(this).find("td").eq(17).find("input").val();//合同号
                var outBarcode = $(this).find("td").eq(18).find("input").val();//调出条形码
                var torrNo = $(this).find("td").eq(19).find("input").val();//托盘号
                var boxNum = $(this).find("td").eq(20).find("input").val();//箱数
                if (outGoodsId == null || outGoodsId == "" || outGoodsId == undefined) {
                    $custom.alert.error("请选择调出商品！");
                    check = false;
                    return;
                }
                if (transferNum == null || transferNum == "" || transferNum == undefined) {
                    $custom.alert.error("请输入调出数量！");
                    check = false;
                    return;
                }
                if (transferNum < 1) {
                    $custom.alert.error("调出数量不正确！");
                    check = false;
                    return;
                }
                if (inTransferNum == null || inTransferNum == "" || inTransferNum == undefined) {
                    $custom.alert.error("请输入调入数量！");
                    check = false;
                    return;
                }
                if (inTransferNum < 1) {
                    $custom.alert.error("调入数量不正确！");
                    check = false;
                    return;
                }
                if (inGoodsId == null || inGoodsId == "" || inGoodsId == undefined) {
                    $custom.alert.error("请选择调入商品！");
                    check = false;
                    return;
                }

                if ((outDepotType=='a'&&inDepotType=='a'&&isSameArea=='0'&&inDepotIsInOutInstruction=='1') || (outDepotType=='c' && inDepotType=='b')
                    || (outDepotType=='c' && inDepotType=='a')) {
                    if (!price || price < 0) {
                        $custom.alert.error("请选择调拨单价！");
                        check = false;
                        return;
                    }
                }

                if (inDepotIsInOutInstruction == '1' || outDepotIsInOutInstruction == '1') {
                    if (!price || price < 0) {
                        $custom.alert.error("请选择调拨单价！");
                        check = false;
                        return;
                    }
                }

                if (price && price < 0) {
                    $custom.alert.error("调拨单价不能小于0！");
                    check = false;
                    return;
                }

                if (!grossWeightSum) {
                    $custom.alert.error("【商品信息】毛重不能为空！");
                    check = false;
                    return;
                }

                if (!netWeightSum) {
                    $custom.alert.error("【商品信息】净重不能为空！");
                    check = false;
                    return;
                }

                if (outDepotType=='c' && inDepotType=='a') {
                    if (!contNo) {
                        $custom.alert.error("【商品信息】箱号不能为空！");
                        check = false;
                        return;
                    }
                    if (!bargainno) {
                        $custom.alert.error("【商品信息】合同号不能为空！");
                        check = false;
                        return;
                    }
                }

                if (outCommbarcode!= inCommbarcode) {
                    $custom.alert.error("调入商品与调出商品标准条码不一致，调出商品货号:" + outGoodsNo);
                    check = false;
                    return;
                }

                if (parseFloat(grossWeightSum) < parseFloat(netWeightSum)) {
                    $custom.alert.error("毛重不能小于净重，调出商品货号:" + outGoodsNo);
                    check = false;
                    return;
                }

                if (!seq) {
                    isSeqNull = true;
                }

                var goods = {};
                goods.seq = seq;
                goods.outGoodsId = outGoodsId;
                goods.outUnit = outUnit;
                goods.transferNum = transferNum;//调出数量
                goods.inGoodsId = inGoodsId;
                goods.inUnit = inUnit;
                goods.inTransferNum = inTransferNum;//调入数量
                goods.brandName = brandName;//品牌类型
                goods.price = price;//采购单价
                goods.grossWeight = grossWeight;//毛重
                goods.grossWeightSum = grossWeightSum;//毛重
                goods.netWeight = netWeight;
                goods.netWeightSum = netWeightSum;
                goods.contNo = contNo;//箱号
                goods.bargainno = bargainno;//合同号
                goods.outBarcode = outBarcode;//调出条形码
                goods.boxNum = boxNum;//箱数
                goods.torrNo = torrNo;//托盘号
                goodsList.push(goods);

                checkInventoryGoods.push({"goodsId":outGoodsId, "goodsNo":outGoodsNo, "transferNum":transferNum});

            });

            var mergeJson = {};
            if (check) {
                //合并相同项
                $.each(checkInventoryGoods, function (index, value) {
                    var originalGoodsId = $module.inventoryLocationMapping.getOriginalGoodsId(value.goodsId, value.goodsNo) ;
                    if (originalGoodsId) {
                        if (!mergeJson[originalGoodsId]) {
                            mergeJson[originalGoodsId] = value;
                        } else {
                            var num = mergeJson[originalGoodsId].transferNum;
                            value.transferNum = parseInt(num) + parseInt(value.transferNum);
                            mergeJson[originalGoodsId] = value;
                        }
                    } else {
                        mergeJson[value.goodsId] = value;
                    }
                });
                //查询可用量
                $.each(mergeJson, function (index, value) {
                    var DepotData = $module.depot.getDepotById(outDepotId);
                    var depotType = DepotData.type;
                    var searchUnit = null;
                    if (depotType == 'c') {
                        if(tallyingUnit == "00"){//转换为库存的理货单位
                            searchUnit = "0";//托盘
                        }else if(tallyingUnit == "01"){
                            searchUnit = "1";//箱
                        }else if(tallyingUnit == "02"){
                            searchUnit = "2";//件
                        }
                    }

                    var availableNum =$module.inventory.queryAvailability(outDepotId,index,DepotData.code,searchUnit,null,null,null);
                    var tempGoodsNo = $module.inventoryLocationMapping.getOriginalGoodsNo(index, value.goodsNo) ;
                    if(availableNum ==-1){
                        check = false;
                        if(tempGoodsNo){
                            $custom.alert.warningText("原货号：" + tempGoodsNo + " 商品货号："+value.goodsNo+",未查到库存可用量");
                        }else{
                            $custom.alert.warningText("未查到库存余量货号："+value.goodsNo+",仓库:"+DepotData.name);
                        }
                        return;
                    }else if(value.transferNum>availableNum){
                        check = false;
                        if(tempGoodsNo){
                            $custom.alert.warningText("库存不足，"+"原货号：" + tempGoodsNo +" 商品货号："+value.goodsNo+",余量："+availableNum);
                        }else{
                            $custom.alert.warningText("库存不足，货号："+value.goodsNo+",仓库:"+DepotData.name+",余量："+availableNum);
                        }
                        return;
                    }
                });
            }
            if (!check) return false;

            if (isSeqNull) {
                $custom.alert.warning("存在排序序号为空的商品，是否按现有排序提交？",function(){
                    //禁用保存按钮
                    $("#save-buttons").attr('disabled','disabled');
                    json.goodsList = goodsList;
                    var jsonStr = JSON.stringify(json);
                    var url = serverAddr + "/transfer/updateTransferOrder.asyn?token=" + token;
                    $custom.request.ajaxSync(url,{"json":jsonStr},function(result){
                        if (result.state == '200' && result.data.code == '00') {
                            $custom.alert.success("保存成功！");
                            setTimeout(function () {
                                $module.load.pageOrder('act=8001');
                            }, 1000);
                        } else {
                            var message = result.data.message;
                            var expMessage = result.expMessage;
                            if (message) {
                                $custom.alert.error(message);
                            }else if (expMessage) {
                                $custom.alert.error(expMessage);
                            } else {
                                $custom.alert.error("保存失败！");
                            }
                            $("#save-buttons").attr('disabled',false);
                        }
                    });
                });
            } else {
                //禁用保存按钮
                $("#save-buttons").attr('disabled','disabled');
                json.goodsList = goodsList;
                var jsonStr = JSON.stringify(json);
                var url = serverAddr + "/transfer/updateTransferOrder.asyn?token=" + token;
                $custom.request.ajaxSync(url,{"json":jsonStr},function(result){
                    if (result.state == '200' && result.data.code == '00') {
                        $custom.alert.success("保存成功！");
                        setTimeout(function () {
                            $module.load.pageOrder('act=8001');
                        }, 1000);
                    } else {
                        var message = result.data.message;
                        var expMessage = result.expMessage;
                        if (message) {
                            $custom.alert.error(message);
                        }else if (expMessage) {
                            $custom.alert.error(expMessage);
                        } else {
                            $custom.alert.error("保存失败！");
                        }
                        $("#save-buttons").attr('disabled',false);
                    }
                });
            }
        });
    }

    function validSetting(param, isClear) {
        var paramJson =  {
            "inCustomerIdLabel": ["inCustomerId", "调入客户"],
            "contractNoLabel": ["contractNo","合同号"],
            "invoiceNoLabel": ["invoiceNo","发票号"],
            "packTypeLabel": ["packType","包装方式"],
            "cartonsLabel": ["cartons","箱数"],
            "firstLadingBillNoLabel" : ["firstLadingBillNo","头程提单号"],
            "destinationLabel": ["destination","目的地"],
            "consigneeUsernameLabel": ["consigneeUsername","收货人姓名"],
            "countryLabel": ["country","国家"],
            "poNoLabel": ["poNo","PO号"],
            "depotScheduleIdLabel": ["depotScheduleId","调入仓地址"]};
        var pArr = param.split(",");
        if (isClear) {
            $.each(pArr,function(index,value){
                if (paramJson[value]) {
                    $('#'+ paramJson[value][0]).removeAttr("required");
                    $('#'+ paramJson[value][0]).removeAttr("reqMsg");
                }
            });
        } else {
            $.each(pArr,function(index,value){
                if (paramJson[value]) {
                    $('#'+ paramJson[value][0]).attr("required", "");
                    $('#'+ paramJson[value][0]).attr("reqMsg", paramJson[value][1] + "不能为空！");
                }
            });
            var transport = $("#transport").val();
            //当运输方式为海运、空运时必填
            if (param.indexOf("firstLadingBillNoLabel") == -1) {
                if (transport == '1' || transport == '2') {
                    $('[name=firstLadingBillNoLabel]').text('*');
                    $('#firstLadingBillNo').attr("required", "");
                    $('#firstLadingBillNo').attr("reqMsg", "提单号不能为空");
                }
            }
        }
    }

    //取消
    function cancel() {
        var pageSource = '${pageSource}';
        if (pageSource == "1") {
            $load.a("/list/menu.asyn?act=100&r="+Math.random());
        } else {
            $module.load.pageOrder('act=8001');
        }
    }

    //当调入仓库在本公司下的选品限制为"仅外仓统一码"时，调入商品置灰不可编辑
    function removeChooseGoodsBtn() {
        if (productRestriction == "2") {
            $(".indepot_goods").attr("disabled","disabled");
        } else {
            $(".indepot_goods").removeAttr("disabled");
        }
    }

    //当选择调出商品时，校验调入调出仓库是否选择
    function initGoodsBefore(obj, depotType) {
        var inDepotId = $("#inDepotId").val();
        var outDepotId = $("#outDepotId").val();
        var outGoodsId = $(obj).parents("tr").children("td").eq(2).find("input").val();//调出商品id

        if(outDepotId==null || outDepotId=="") {
            $custom.alert.error("请先选择调出仓库！");
            return;
        }

        if(inDepotId==null || inDepotId=="") {
            $custom.alert.error("请先选择调入仓库！");
            return;
        }

        if(depotType == 'inDepotId' && (outGoodsId==null || outGoodsId=="")) {
            $custom.alert.error("请先选择调出商品！");
            return;
        }

        if (depotType == 'outDepotId') {
            var seq = $(obj).parents("tr").children("td").eq(1).find("input").val();//序号
            TBindex = $(obj).parents("tr").index();
            existSeq = seq;
        } else {
            existSeq = null;
            TBindex = null;
        }
        $erpPopup.orderGoods.init('4',obj, depotType);
    }

    function sortSeq() {
        if (existSeq && (TBindex || TBindex == 0)) {
            var ind = TBindex+1;
            $("#datatable-buttons").find("tr:eq("+ ind +") td:eq(1)").find("input").val(existSeq)
        } else {
            var seqArr = []; //序号集合
            var indexArr  = []; //未排序序号集合
            $("#datatable-buttons tbody tr").each(function (index, item) {
                var seq = $(this).find("td").eq(1).find("input").val();
                if (seq) {
                    seqArr.push(seq);
                } else {
                    indexArr.push(index);
                }
            });
            seqArr.sort(function(a,b){
                return a - b;
            });
            if (seqArr.length == 0) {
                seqArr.push(0);
            }
            indexArr.forEach((item,index,array)=>{
                $("#datatable-buttons tbody").find('tr').eq(item).find('td').eq(1).find("input").val(parseInt(seqArr[seqArr.length-1])+index+1);
            });
        }


    }

    function calcItemGrossWeight(){

        var grossWeight = $("#billWeight").val() ;
        var grossWeightTotal = $("#billWeight").val() ;

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
                    $(this).parent().parent().find("input[name='grossWeight']").val(grossWeightTemp);
                    grossWeightTotal -= parseFloat(grossWeightTemp) ;
                    grossWeightTotal = parseFloat(grossWeightTotal).toFixed(decimalPlace) ;
                }else{
                    $(this).parent().parent().find("input[name='grossWeight']").val(grossWeightTotal) ;
                }

            });
        }
        $("#totalGrossWeight").html(grossWeight) ;
    }

    function setPackType() {

        var packType = "" ;

        var boxNum = $("#cartons").val() ;

        if(boxNum){
            packType += boxNum + "箱" ;
        }

        var torrNum = $("#torrNum").val() ;

        if(torrNum){
            packType += torrNum + "托" ;
        }

        var palletMaterial = "";
        var palletMaterialVal = $("#palletMaterial").find("option:selected").val();
        if (palletMaterialVal) {
            palletMaterial = $("#palletMaterial").find("option:selected").text();
        }

        if(palletMaterial){
            packType += palletMaterial ;
        }
        $("#packType").val(packType) ;

    }
</script>
