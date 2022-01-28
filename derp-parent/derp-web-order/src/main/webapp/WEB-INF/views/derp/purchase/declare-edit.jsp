<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
                       <li class="breadcrumb-item"><a href="javascript:dh_list();">预申报单</a></li>
                       <li class="breadcrumb-item"><a href="javascript:dh_edit(${detail.id });">编辑预申报单</a></li>
                    </ol>
                    </div>
                   </div>
                    <!--  title   end  -->               
                          <!--  title   基本信息   start -->
                    <div class="title_text">基本信息</div>
                <form id="edit-form">
                    <div class="edit_box mt20">
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> 预申报单号：</label>
                            <input type="hidden" id="orderId" name="id" value="${detail.id }">
                            <input type="hidden" name="status" value="${detail.status }">
                            <input type="text"  disabled parsley-type="email" class="edit_input" name="code" value="${detail.code }">
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> 供应商：</label>
                            <input type="text"  disabled parsley-type="email" class="edit_input" name="supplierName" value="${detail.supplierName }">
                            <input type="hidden" name="supplierId" value="${detail.supplierId }" id="supplierId">
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><c:choose><c:when test="${flag == '1' }"><i class="red bonded">*</i></c:when><c:otherwise><i class="red bonded" style="display:none;">*</i></c:otherwise></c:choose> 头程提单号：</label>
                            <input type="text"  parsley-type="text" class="edit_input" id="ladingBill" name="ladingBill" value="${detail.ladingBill }" <c:if test="${flag == '1' }"> required reqMsg = '头程提单号不能为空' </c:if>>
                        </div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item">
                            <label class="edit_label">业务模式：</label>
                            <select class="edit_input" name="businessModel">
                                <option value="">请选择</option>
                                <option value="1" <c:if test="${detail.businessModel=='1' }">selected</c:if> >购销</option>
                                <option value="2" <c:if test="${detail.businessModel=='2' }">selected</c:if> >代销</option>
                            </select>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><c:choose><c:when test="${flag == '1' }"><i class="red bonded">*</i></c:when><c:otherwise><i class="red bonded" style="display:none;">*</i></c:otherwise></c:choose> 发票号：</label>
                            <input type="text"  parsley-type="text" class="edit_input" name="invoiceNo" id="invoiceNo" value="${detail.invoiceNo }" <c:if test="${flag == '1' }"> required reqMsg = '发票号不能为空' </c:if>>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> 合同号：</label>
                            <input type="text"  class="edit_input" name="contractNo" id="contractNo" value="${detail.contractNo }" required reqMsg = "合同号不能为空">
                        </div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item">
                            <label class="edit_label">销售渠道：</label>
                            <input type="text"  parsley-type="text" class="edit_input" name="channel" value="${detail.channel }">
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> PO号：</label>
                            <input type="text"  class="edit_input" name="poNo" id="poNo" value="${detail.poNo }">
                        </div>
                        <div class="edit_item">
                            <label class="edit_label">运输方式：</label>
                            <select class="edit_input" name="transport">
                                <option value="">请选择</option>
                            </select>
                        </div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> 入仓仓库：</label>
                            <select class="edit_input" name="depotId" id="depotId" disabled required reqMsg = '入仓仓库不能为空' >
                                <option value="">请选择</option>
                            </select>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> 事业部：</label>
                            <select class="edit_input" name="buId" id="buId" disabled required reqMsg = '事业部不能为空' >
                                <option value="">请选择</option>
                            </select>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><c:choose><c:when test="${flag == '1' }"><i class="red bonded">*</i></c:when><c:otherwise><i class="red bonded" style="display:none;">*</i></c:otherwise></c:choose> 收货地点：</label>
                            <input type="text"  parsley-type="text" class="edit_input"  name="deliveryAddr" id="deliveryAddr" value="${detail.deliveryAddr }" <c:if test="${flag == '1' }"> required reqMsg = '收货地点不能为空' </c:if>>
                        </div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item">
                            <label class="edit_label"><c:choose><c:when test="${flag == '1' }"><i class="red bonded">*</i></c:when><c:otherwise><i class="red bonded" style="display:none;">*</i></c:otherwise></c:choose> 箱      数：</label>
                            <input type="text"  parsley-type="text" class="edit_input" name="boxNum" id="boxNum" value="${detail.boxNum }" onkeyup="value=value.replace(/[^\d]/g,'')" <c:if test="${flag == '1' }"> required reqMsg = '箱数不能为空' </c:if>>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label">目的港名称：</label>
                            <input type="text"  parsley-type="text" class="edit_input"  name="destinationPortName" value="${detail.destinationPortName }">
                        </div>
                        <div class="edit_item">
                            <label class="edit_label">预计到港时间：</label>
                            <input type="text"  parsley-type="text" class="edit_input form_datetime date-input" value="${arriveDate }" name="arriveDate2">
                        </div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item">
                            <label class="edit_label">目的地名称：</label>
                            <input type="text"  parsley-type="text" class="edit_input"  name="destinationName" value="${detail.destinationName }">
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><c:choose><c:when test="${flag == '1' }"><i class="red bonded">*</i></c:when><c:otherwise><i class="red bonded" style="display:none;">*</i></c:otherwise></c:choose> 装货港：</label>
                            <input type="text"  parsley-type="text" class="edit_input" name="portLoading" id="portLoading" value="${detail.portLoading }" <c:if test="${flag == '1' }"> required reqMsg = '装货港不能为空' </c:if>>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><c:choose><c:when test="${flag == '1' }"><i class="red bonded">*</i></c:when><c:otherwise><i class="red bonded" style="display:none;">*</i></c:otherwise></c:choose> 包装方式：</label>
                            <div class="edit_input" style="text-indent: 0">
                                <select class="form-control goods_select2" name="packType" id="packType" <c:if test="${flag == '1' }"> required reqMsg = '包装方式不能为空' </c:if>>
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
                    </div>
                    <div class="edit_box">
                        <div class="edit_item">
                            <label class="edit_label"><c:choose><c:when test="${flag == '1' }"><i class="red bonded">*</i></c:when><c:otherwise><i class="red bonded" style="display:none;">*</i></c:otherwise></c:choose> 付款条约：</label>
                            <c:choose>
                            	<c:when test="${detail.payRules == null }">
                            		<input type="text"  parsley-type="text" class="edit_input" name="payRules" id="payRules" value="CIF NANSHA" <c:if test="${flag == '1' }"> required reqMsg = '付款条约不能为空' </c:if>>
                            	</c:when>
                            	<c:otherwise>
                            		<input type="text"  parsley-type="text" class="edit_input" name="payRules" id="payRules" value="${detail.payRules }" <c:if test="${flag == '1' }"> required reqMsg = '付款条约不能为空' </c:if>>
                            	</c:otherwise>
                            </c:choose>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label">提单毛重KG：</label>
                            <input type="text"  parsley-type="text" class="edit_input" name="billWeight"  id="billWeight" value="${detail.billWeight }" onkeyup="amountVal(this, '5')">
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><c:choose><c:when test="${flag == '1' }"><i class="red bonded">*</i></c:when><c:otherwise><i class="red bonded" style="display:none;">*</i></c:otherwise></c:choose> 卸货港：</label>
                        	<select class="edit_input" name="portDestNo" id="portDestNo" <c:if test="${flag == '1' }"> required reqMsg = '卸货港不能为空' </c:if>>
                        		<option value="">请选择</option>
                        		<option value="44011501" <c:if test="${detail.portDestNo eq '44011501' }">selected</c:if>>44011501：南沙新港口岸</option>
                        		<option value="44010318" <c:if test="${detail.portDestNo eq '44010318' }">selected</c:if>>44010318：黄埔广保通码头口岸</option>
                        		<option value="21021001" <c:if test="${detail.portDestNo eq '21021001' }">selected</c:if>>21021001：大连保税区口岸</option>
                        		<option value="50010001" <c:if test="${detail.portDestNo eq '50010001' }">selected</c:if>>50010001：重庆口岸</option>
                        	</select>
                        </div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item">
                            <label class="edit_label">二程提单号：</label>
                            <input type="text"  parsley-type="text" class="edit_input"  name="blNo" id="blNo" value="${detail.blNo }">
                        </div>
                        <div class="edit_item">
                            <label class="edit_label">邮箱：</label>
                            <input type="text"  parsley-type="text" class="edit_input" name="email" id="email" value="${detail.email }">
                        </div>
                        <div class="edit_item">
                            <label class="edit_label">进出口口岸：</label>
                            <input type="text"  parsley-type="text" class="edit_input"  name="imExpPort" id="imExpPort" value="${detail.imExpPort }">
                        </div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item">
                            <label class="edit_label">LBX单号：</label>
                            <input type="text"  parsley-type="text" class="edit_input"  name="lbxNo" value="${detail.lbxNo }">
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><c:choose><c:when test="${flag == '1' }"><i class="red bonded">*</i></c:when><c:otherwise><i class="red bonded" style="display:none;">*</i></c:otherwise></c:choose> 境外发货人：</label>
                            <input type="text"  parsley-type="text" class="edit_input" name="shipper"  id="shipper" value="${detail.shipper }" <c:if test="${flag == '1' }"> required reqMsg = '境外发货人不能为空' </c:if>>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><c:choose><c:when test="${flag == '1' }"><i class="red bonded">*</i></c:when><c:otherwise><i class="red bonded" style="display:none;">*</i></c:otherwise></c:choose> 唛头：</label>
                            <input type="text"  parsley-type="text" class="edit_input"  name="mark" id="mark" value="${detail.mark }" <c:if test="${flag == '1' }"> required reqMsg = '唛头不能为空' </c:if>>
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
                    <div class="edit_box">
                        <div class="edit_item" id="tallyingUnitDiv">
                            <c:if test="${depotType eq 'c' }">
                        		<label class="edit_label"><c:choose><c:when test="${flag == '0' }"><i class="red bonded">*</i></c:when><c:otherwise><i class="red bonded" style="display:none;">*</i></c:otherwise></c:choose> 海外仓理货单位：</label>
	                            <select name="tallyingUnit" class="edit_input" id="tallyingUnit" <c:if test="${flag == '0' }"> required reqMsg = '唛头不能为空' </c:if>>
	                            	<option value="">请选择</option>
	                            	<option value="00" <c:if test="${detail.tallyingUnit eq '00' }">selected</c:if>>托盘</option>
	                            	<option value="01" <c:if test="${detail.tallyingUnit eq '01' }">selected</c:if>>箱</option>
	                            	<option value="02" <c:if test="${detail.tallyingUnit eq '02' }">selected</c:if>>件</option>
	                            </select>
                        	</c:if>
                        </div>
                        <div class="edit_item">
                        </div>
                        <div class="edit_item">
                        </div>
                    </div>
                </form> 
                 <!--  title   基本信息  start -->
                 <!-- 商品信息  start -->
                <div class="col-12 ml10">
                        <div class="row">
                         <div class="col-8">
                             <div class="title_text">商品信息</div>
                        </div>
                         <div class="col-1 mt10">
                         	<button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick="$erpPopup.orderGoods.init(1, null, null);">选择商品</button>
                        </div>
                        <div class="col-1 mt10">
                          <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="import-goods-button">商品导入</button>
                        </div>
                         <div class="col-1 mt10">
                          <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="delete-list-button">删 除</button>
                        </div> 
                        </div>
                    </div>   
                <div class="mt20 table-responsive">
                     <table id="table-list" class="table table-striped">
                         <thead>
                            <tr>
                             <th><input id="checkbox11" type="checkbox" name="all"></th>
                             <th>序号</th>
                             <th>合同号</th>
                             <th>商品编码</th>
                             <th style="min-width: 220px">商品名称</th>
                             <th>商品货号</th>
                             <th><i class="red">*</i>申报数量</th>
                             <th>采购单价</th>
                             <th><i class="red">*</i>申报单价</th>
                             <th>申报总金额（RMB）</th>
                             <!-- <th>采购单位</th> -->
                             <th>品牌类型</th>
                             <th><i class="red">*</i>毛重（KG）</th>
                             <th><i class="red">*</i>净重（KG）</th>
                             <th>箱号</th>
                             <th>生产批次号</th>
                             <th>成分占比</th>
                         </tr>
                         </thead>
                        <tbody id="itemTbody">
                        <c:forEach items="${detail.itemList}" var="item" varStatus="status">
                            <tr>
                                <td><input type='checkbox' name='item-checkbox'><input type='hidden' name='goodsId' value='${item.goodsId }'><input type="hidden" value="${item.id }" name="itemId"></td>
                                <td><input type='text' name='seqs' class="goods-seqs" style='width:70px;text-align:center;' value='${item.seq }' onkeyup="value=value.replace(/[^\d]/g,'')"></td>
                                <td style="text-align:center">
                                    <input type='text' name='contractNos' class="goods-contractNos" style='width:70px;text-align:center;' value='${item.contractNo }'>
                                </td>
                                <td>${item.goodsCode}</td>
                                <td>${item.goodsName}</td>
                                <td>${item.goodsNo}</td>
                                <td>
                                    <input type='text' name='num' style='width:70px;text-align:center;' class='goods-num' value='${item.num }' onkeyup="value=value.replace(/[^\d]/g,'')">
                                </td>
                                <td>
                                	${item.purchasePrice }
                                	<input type='hidden' name='purchasePrice' class='goods-purchase-price' style='width:70px;text-align:center;' value='${item.purchasePrice }'>
                                </td>
                                <td><input type='text' name='price' class='goods-price' style='width:100px;text-align:center;' value='<fmt:formatNumber value="${item.price}" pattern="#.########" minFractionDigits="8" />'></td>
                                <td>
                                	<input type='text' name='goods-amount' class='goods-amount' style='width:100px;text-align:center;' value='<fmt:formatNumber value="${item.price*item.num}" pattern="#.###" minFractionDigits="2" />'>
                                </td>
                                <td>
                                	<input type='text' name='brandName' class='brandName' style='width:100px;text-align:center;' value='${item.brandName }' onMouseMove="this.title=this.value">
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
                                </td>
                                <td style="text-align:center">
                                	<input type='text' name='batchNo' class="goods-batchNo" style='width:70px;text-align:center;' value='${item.batchNo }'>
                                </td>
                                <td>
                                	<textarea name='constituentRatio' class='goods-constituentRatio' cols='30' rows='3' align='center'>${item.constituentRatio }</textarea>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
<!--                    商品信息  end -->
                      <div class="form-row m-t-50">
                          <div class="col-12">
                              <div class="row">
                                  <div class="col-6">
                                      <button type="button" class="btn btn-info waves-effect waves-light fr btn_default" id="save-buttons">保 存</button>
                                  </div>
                                  <div class="col-6">
                                      <button type="button" class="btn btn-light waves-effect waves-light btn_default" id="cancel-buttons">取 消</button>
                                  </div>
                              </div>
                          </div>
                      </div>
                      
                </div>
              </div>
                           <!-- 新增弹窗部分   start -->
                           <!-- 弹窗开始 -->
		                  <jsp:include page="/WEB-INF/views/modals/4014-V2.jsp" />
		                  <jsp:include page="/WEB-INF/views/modals/2018.jsp" />
		                  <!-- 弹窗结束 -->
             </div>
                  <!-- end row -->
        </div>
        <!-- container -->
    </div>
<script type="text/javascript">
	$(function(){
		
		$module.depot.getSelectBeanByMerchantRel.call($("select[name='depotId']")[0],"${detail.depotId}", {"type":"a,c,d"});
		$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],'${detail.buId }', null, '${detail.depotId}');
		$module.constant.getConstantListByNameURL.call($('select[name="transport"]')[0],"transportList",'${detail.transport}');
		$derpTimer.init(".date-input", "yyyy-MM-dd") ;
		
		//重新加载select2
		$(".goods_select2").select2({
			language:"zh-CN",
			placeholder: '请选择',
			allowClear:true
		});
		
		setRequired() ;
		
		//仓库做了变更
		$("#depotId").change(function(){
			setRequired() ;
		});
		
		//选择仓库，回写仓库地址
		$("#depotId").change(function(){
			var depot_id = $(this).val();
			$custom.request.ajax("/depot/getDepotDetails.asyn", {"id":depot_id}, function(result){
				$("#deliveryAddr").val(result.data.address);
				if(result.data.type == 'c'){
					$("#tallyingUnitDiv").html('<label class="edit_label"><i class="red">*</i> 海外仓理货单位：</label><select name="tallyingUnit" id="tallyingUnit" class="edit_input"><option value="">请选择</option><option value="00">托盘</option><option value="01">箱</option><option value="02">件</option></select>');
				}else{
					$("#tallyingUnitDiv").html('');
				}
			});
			//清空商品列表
			$("#table-list tr:gt(0)").remove();
			$("#unNeedIds").val('');
		});
		
		//保存按钮
		$("#save-buttons").click(function(){
			
			//必填项校验
        	if(!$checker.verificationRequired()){
        		return ;
        	}
			
			var url = serverAddr+"/declare/modifyDeclare.asyn";
			var form = $("#edit-form").serializeArray();
			
			var id = $("#depotId").val();
			form.push({name:"depotId", value:id}) ;
			
			var depotType = "";
			$custom.request.ajaxSync("/depot/checkDepotType.asyn", {"id":id}, function(result){
				//保税仓
				if(result.data == '1'){
					depotType = "1";
				}
				//海外仓
				else{
					depotType = "0";
				}
			});
			
			var billWeight = $("#billWeight").val();
			//获取所有毛重之和
	    	var obj_grossWeight = document.getElementsByName("grossWeights");
	    	var grossWeight = 0;
			for(var i = 0; i < obj_grossWeight.length; i++){
				grossWeight += Number(obj_grossWeight[i].value);
			}
			if(billWeight == null || billWeight==""){
				$("#billWeight").val(grossWeight);
			}else{
				if(parseFloat(billWeight).toFixed(5) != grossWeight.toFixed(5)){
					$custom.alert.error("提单毛重要和商品毛重之和相等！");
					return ;
				}
			}
			
			//箱数
			if($("#boxNum").val() && isNaN($("#boxNum").val() )){
				$custom.alert.error("箱数不为数值！");
				return ;
			}
			
			//提单毛重
			if($("#billWeight").val() && isNaN($("#billWeight").val() )){
				$custom.alert.error("提单毛重不为数值！");
				return ;
			}
			
			//商品必填校验
			//数量
			var num_flag = 0;
			$(".goods-num").each(function(){
				if(isNaN($(this).val()) || $(this).val()==0 || $(this).val()==''){
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
				if(isNaN($(this).val()) || $(this).val()<=0 || $(this).val()==''){
					price_flag = 1;
				}
			});
			if(price_flag==1){
				$custom.alert.error("价格要为大于0的数！");
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
					grossWeight_flag = 1;
				}
			});
			if(grossWeight_flag==1){
				$custom.alert.error("毛重至少为0！");
				return ;
			}
			//净重
			var netWeight_flag = 0;
			$(".goods-netWeight").each(function(){
				if(isNaN($(this).val()) || $(this).val()==''){
					netWeight_flag = 1;
				}
			});
			if(netWeight_flag==1){
				$custom.alert.error("净重至少为0！");
				return ;
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
			
			var items = new Array() ;
			
			var continueFlag = true ;
            
            $("#itemTbody").find("tr").each(function(index, tr){
            	
            	var contractNo = $(tr).find("input[name='contractNos']").val() ;
            	var seqs = $(tr).find("input[name='seqs']").val() ;
                var goodsId = $(tr).find("input[name='goodsId']").val() ;
                var num = $(tr).find("input[name='num']").val() ;
                var price = $(tr).find("input[name='price']").val() ;
                var purchasePrice = $(tr).find("input[name='purchasePrice']").val() ;
                var amount = $(tr).find("input[name='goods-amount']").val() ;
                var boxNo = $(tr).find("input[name='boxNo']").val() ;
                var constituentRatio = $(tr).find("textarea[name='constituentRatio']").val() ;
                var brandName = $(tr).find("input[name='brandName']").val() ;
                var grossWeight = $(tr).find("input[name='grossWeights-hidden']").val() ;
                var grossWeightSum = $(tr).find("input[name='grossWeights']").val() ;
                var netWeight = $(tr).find("input[name='netWeight-hidden']").val() ;
                var netWeightSum = $(tr).find("input[name='netWeight']").val() ;
                var batchNo = $(tr).find("input[name='batchNo']").val() ;
            	
                var item = {"contractNo": contractNo, "seq":seqs,"goodsId": goodsId, "num":num, "amount": amount,
                        "price":price, "boxNo":boxNo, "constituentRatio":constituentRatio,
                        "brandName":brandName, "grossWeight":grossWeight, "grossWeightSum":grossWeightSum,
                        "netWeight":netWeight, "netWeightSum":netWeightSum, "purchasePrice":purchasePrice, "batchNo":batchNo} ;
                
                if(!seqs){
					continueFlag =false ;
				}
                
                items.push(item) ;
            }) ;
			
			items = JSON.stringify(items);
            
            form.push({name:"items", value:items }) ;
            
			if(!continueFlag){
				$custom.alert.warning("存在排序序号为空的商品，是否按现有排序提交？",function(){
					$custom.request.ajax(url, form, function(result){
						if(result.state == '200'){
							if(result.data=='成功'){
								$custom.alert.success("编辑成功！");
								setTimeout(function () {
									$module.load.pageOrder("act=3002");
								}, 1000);
							}else{
								$custom.alert.error(result.data);
							}
						}else{
							$custom.alert.error(result.message);
						}
					});
				});
			}else{
				$custom.request.ajax(url, form, function(result){
					if(result.state == '200'){
						if(result.data=='成功'){
							$custom.alert.success("编辑成功！");
							setTimeout(function () {
								$module.load.pageOrder("act=3002");
							}, 1000);
						}else{
							$custom.alert.error(result.data);
						}
					}else{
						$custom.alert.error(result.message);
					}
				});
			}
			
			
		});
	
		//监听采购价格的点击事件
		$("#table-list").on("click",'.goods-price',function(){
			if(Number($(this).val())=='0'){
				$(this).val("");
			}
		});
		
		//监听毛重的点击事件
		$("#table-list").on("click",'.goods-grossWeight',function(){
			if(Number($(this).val())=='0'){
				$(this).val("");
			}
		});
		
		//监听净重的点击事件
		$("#table-list").on("click",'.goods-netWeight',function(){
			if(Number($(this).val())=='0'){
				$(this).val("");
			}
		});
		
		//监听总金额的离开事件
	    $("#table-list").on("blur",'.goods-amount',function(){
	    	var that = $(this);
	    	//获取当前总价
	    	var amount = that.val();
	    	//获取采购数量
	    	var num = that.parent().prev().prev().prev().find("input").val();
	    	//设置价格
	    	var price = amount/num;
	    	that.parent().prev().find('input').val(price.toFixed(2));
	    });
		
		//监听采购价格的离开事件
	    $("#table-list").on("blur",'.goods-price',function(){
	    	var that = $(this);
	    	//获取当前价格
	    	var price = that.val();
	    	//获取采购数量
	    	var num = that.parent().prev().prev().find("input").val();
	    	//设置总金额
	    	var sum = price*num;
	    	that.parent().next().find('input').val(sum.toFixed(2));
	    });
		
	    //监听数量的离开事件
	    $("#table-list").on("blur",'.goods-num',function(){
	    	var that = $(this);
	    	//获取当前数量
	    	var num = that.val();
	    	//获取价格
	    	var price = that.parent().next().next().find("input").val();
	    	//设置总金额
	    	var sum = price*num;
	    	that.parent().next().next().next().find('input').val(sum.toFixed(2));
	    	//设置总毛重、总净重
	    	var grossWeight = that.parent().parent().find(".goods-grossWeight-hidden").val();
	    	that.parent().parent().find(".goods-grossWeight").val((grossWeight*num).toFixed(5));
	    	var netWeight = that.parent().parent().find(".goods-netWeight-hidden").val();
	    	that.parent().parent().find(".goods-netWeight").val((netWeight*num).toFixed(5));
	    	
	    	//获取所有毛重之和
	    	sumBillWeight() ;
	    });
	    
	    //监听商品毛重的离开事件
	    $("#table-list").on("blur",'.goods-grossWeight',function(){
	    	//获取所有毛重之和
	    	sumBillWeight() ;
	    });
		
		//取消按钮
		$("#cancel-buttons").click(function(){
			$load.a(pageUrl+'3002');
		});
	
		//删除选中行
	 	$("#delete-list-button").click(function() {
	 		var goodsId = [];
	        $("input[name='item-checkbox']:checked").each(function() { // 遍历选中的checkbox
	            n = $(this).parents("tr").index();  // 获取checkbox所在行的顺序
	            goodsId.push($(this).next().val());
	            $("#table-list").find("tr:eq("+(n+1)+")").remove();
	        });
	        var unNeedIds = $("#unNeedIds").val();
	        var temp = unNeedIds.split(",");
	        for (var i = 0; i < temp.length; i++) {
	        	for (var j = 0; j < goodsId.length; j++) {
					if(temp[i] == goodsId[j]){
						temp.splice(i,1);
					}
				}
			}
	        $("#unNeedIds").val(temp);
	        $("input[name='all']").prop("checked",false);
	        $("input[name='item-checkbox']").prop("checked",false);
	    });
		
	 	$("input[name='all']").click(function(){
			//判断当前点击的复选框处于什么状态$(this).is(":checked") 返回的是布尔类型
			if($(this).is(":checked")){
				$("input[name='item-checkbox']").prop("checked",true);
			}else{
				$("input[name='item-checkbox']").prop("checked",false);
			}
		});
	 	
	 	//计算毛重
	    $("#billWeight").on("blur" ,function(){
	    	var grossWeight = $("#billWeight").val();
	    	var grossWeightTotal = $("#billWeight").val();
	    	if(grossWeight){
	    		if(!isNaN(grossWeight)){
	    			
	    			// 默认5 位
	    			var decimalPlace = 3 ;
	    			
	    			if(grossWeight.indexOf(".") > -1){
	    				decimalPlace = grossWeight.substr(grossWeight.indexOf(".") + 1).length ;
	    				
	    				decimalPlace = parseInt(decimalPlace) ;
	    				
	    				if(decimalPlace > 5){
	    					decimalPlace = 5 ;
	    				}
	    			}
	    			
	    			//获取总净重的比例
	    			var netWeightSum = 0;
	    			$("input[name='netWeight']").each(function(){
	    				netWeightSum+=parseFloat($(this).val());
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
	    		}else{
	    			$custom.alert.error("提单毛重要求输入合法(数值)！");
	    		}
	    	}else{
	    		$custom.alert.error("请输入提单毛重！");
	    	}
	    });
	});
	
	//渲染 下拉框
    function selLoad(data,id){
    	$("#"+id).empty();
        var ops="<option value=''>请选择</option>";
        $.each(data,function(index,event){
        	if(event.value!=null){
            	ops+="<option value='"+event.value+"'>"+event.label+"</option>";
            }
        });
        $("#"+id).append(ops);
    }
	
	function dh_list(){
		$module.load.pageOrder("act=3002");
	}
	
	function dh_edit(id){
		$module.load.pageOrder("act=30021&id="+id);
	}
	
	function setRequired(){
		var id = $("#depotId").val();
		var url = "/depot/checkDepotType.asyn";
		$custom.request.ajax(url, {"id":id}, function(result){
			if(result.data == '1'){
				$(".bonded").css("display","inline-block");
				$(".overseas").css("display","none");
			}else{
				$(".overseas").css("display","inline-block");
				$(".bonded").css("display","none");
			}
		});
	}
	
	function concatItemStr(elementName){
		
		var str = "" ;
		
		var obj = document.getElementsByName(elementName);
		for(var i = 0; i < obj.length; i++){
			
			var temp = obj[i].value ;
			
			if(!temp){
				temp = "";
			}
			
			if(i == 0){
				str = temp ;
			}else{
				str = str + "," + temp;
			}
			
		}
		
		return str ;
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
	
	$("#import-goods-button").click(function(){
		var orderId = $("#orderId").val() ;
		
		$m2018.init(orderId) ;
	}) ;
	
	function sumBillWeight(){
		//获取所有毛重之和
    	var obj_grossWeight = document.getElementsByName("grossWeights");
    	var grossWeight = 0;
		for(var i = 0; i < obj_grossWeight.length; i++){
			grossWeight += Number(obj_grossWeight[i].value);
		}
		$("#billWeight").val(parseFloat(grossWeight).toFixed(5));
	}
	
</script>
