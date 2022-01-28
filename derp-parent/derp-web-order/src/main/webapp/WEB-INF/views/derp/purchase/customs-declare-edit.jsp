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
                                			<li class="breadcrumb-item"><a href="javascript:dh_list();">预申报单</a></li>
                                            <li class="breadcrumb-item"><a href="#">编辑清关资料</a></li>
                                        </ol>
                                    </div>
                                </div>
                                
                                <c:set var="totalNum" value="0"></c:set>
                                <c:set var="totalAmount" value="0"></c:set>
                                <c:set var="totalgrossWeight" value="0"></c:set>
                                <c:set var="totalnetWeight" value="0"></c:set>
                                
                                <!--  title   end  -->
                                <ul class="nav nav-tabs">
                                    <li class="nav-item">
                                        <a href="#contract" data-toggle="tab" class="nav-link active">合同</a>
                                    </li>
                                    <li class="nav-item">
                                        <a href="#invoice" data-toggle="tab" class="nav-link">发票</a>
                                    </li>
                                    <li class="nav-item">
                                        <a href="#packingList" data-toggle="tab" class="nav-link">装箱单</a>
                                    </li>
                                    <li class="nav-item">
                                        <a href="#packingDetail" data-toggle="tab" class="nav-link">装箱明细</a>
                                    </li>
                                    <li class="nav-item">
                                        <a href="#rawMaterial" data-toggle="tab" class="nav-link">原料成分占比</a>
                                    </li>
                                </ul>
                                <div class="tab-content">
                                    <div class="tab-pane fade show active" id="contract">
                                        <p class="customs_clearance_titile">合同</p>
                                        <div class="datum_head_right">
                                            <span>合同编号：</span>
                                            <input type="text" name="contractNo" value="${detail.contractNo }">
                                            <input type="hidden" name="id" value="${detail.id }">
                                            <input type="hidden" name="declareOrderId" value="${detail.declareOrderId }">
                                        </div>
                                        <div class="datum_head_right">
                                            <span>签订日期：</span>
                                            <input type="text" class="form_datetime date-input" name="signingDateStr" value="<fmt:formatDate value="${detail.signingDate}" pattern="yyyy-MM-dd"/>">
                                        </div>
                                        <div class="datum_head_right">
                                            <span>签订地点：</span>
                                            <input type="text" name="signingAddr" value="${detail.signingAddr }">
                                        </div>
                                        <div class="datum_head_left">
                                            <span>甲方：</span>
                                            <input type="text" name="firstParty" value="${detail.firstParty }">
                                        </div>
                                        <div class="datum_head_left">
                                            <span>甲方地址：</span>
                                            <input type="text" name="firstPartyAddr" value="${detail.firstPartyAddr }">
                                        </div>
                                        <div class="datum_head_left">
                                            <span>乙方：</span>
                                            <input type="text" name="secondParty" value="${detail.secondParty }" onblur="setSecondParty(this)">
                                        </div>
                                        <p>基于甲方与 <span id="secondPartySpan">${detail.secondParty }</span> 签订的编号为 ${detail.contractNo }的《委托服务协议》（下称“《委托服务协议》”），甲方现委托乙方办理如下商品在中国南沙保税港区的申报通关、仓储服务：</p>
                                        <p class="datum_title">一、商品资料明细：</p>
                                        <div class="batch_import">
                                            <table>
                                                <thead>
                                                <tr>
                                                    <td>商品名称</td>
                                                    <td>货号</td>
                                                    <td>单位</td>
                                                    <td>数量</td>
                                                    <td>单价(RMB)</td>
                                                    <td>总价(RMB)</td>
                                                    <td>原产地</td>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach items="${detail.itemList }" var="item">
                                                	<tr>
	                                                    <td>${item.goodsName }</td>
	                                                    <td>${item.goodsNo }</td>
	                                                    <td>${item.unit }</td>
	                                                    <td>${item.num }</td>
	                                                    <td>${item.price }</td>
	                                                    <td>${item.amount }</td>
	                                                    <td>${item.countryName }</td>
	                                                    
	                                                    <c:set var="totalNum" value="${totalNum +  item.num}"></c:set>
						                                <c:set var="totalAmount" value="${totalAmount + item.amount }"></c:set>
						                                <c:set var="totalgrossWeight" value="${totalgrossWeight + item.grossWeight }"></c:set>
						                                <c:set var="totalnetWeight" value="${totalnetWeight + item.netWeight }"></c:set>
	                                                    
	                                                </tr>
                                                </c:forEach>
	                                                <tr>
	                                                    <td>TOTAL：</td>
	                                                    <td></td>
	                                                    <td></td>
	                                                    <td>${totalNum }</td>
	                                                    <td></td>
	                                                    <td>${totalAmount }</td>
	                                                    <td></td>
	                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                        <div class="datum_explain">
                                            <p>商品原产地：${detail.countryOrigin }</p>
                                            <p>特殊操作要求：</p>
                                        </div>
                                        <p class="datum_title">二、其他</p>
                                        <div class="other_con">
                                            <p>1.本协议一式贰份，甲、乙双方各持一份，具有同等法律效力。</p>
                                            <p>2.本协议经甲、乙双方签章后生效，通过传真件或扫描件方式签订同样具备法律效力。</p>
                                            <p>3.本协议作为《委托服务协议》不可分割的组成部分，未尽之处，适用《委托服务协议》的约定。</p>
                                        </div>
                                        <div class="main_text">
                                            （以下无正文）
                                        </div>
                                        <div class="form-row m-t-50">
                                            <div class="col-12">
                                                <div class="row">
                                                    <div class="col-5">
                                                        <span class="fr">甲方（签章）</span>
                                                    </div>
                                                    <div class="col-2"></div>
                                                    <div class="col-5">
                                                        <span>乙方（签章）</span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="tab-pane fade" id="invoice">
                                        <p class="customs_clearance_titile">发票</p>
                                        <div class="datum_head_right">
                                            <span>DATE时间：</span>
                                            <input type="text" class="form_datetime date-input" name="invoiceDateStr" value='<fmt:formatDate value="${detail.invoiceDate}" pattern="yyyy-MM-dd"/>'>
                                        </div>
                                        <div class="datum_head_right">
                                            <span>INVOICE NO发票号：</span>
                                            <span class="datum_head_right_con">${detail.invoiceNo }</span>
                                        </div>
                                        <div class="datum_head_right">
                                            <span>CONTRACT NO合同号：</span>
                                            <span class="datum_head_right_con">${detail.contractNo }</span>
                                        </div>
                                        <div class="datum_head_left">
                                            <span>Consignee：</span>
                                            <input type="text" name="eAddressee" value="${detail.eAddressee }">
                                        </div>
                                        <div class="datum_head_left">
                                            <span>Address：</span>
                                            <input type="text" name="eAddresseeAddr" value="${detail.eAddresseeAddr }">
                                        </div>
                                        <div class="datum_head_left">
                                            <span>收货人：</span>
                                            <input type="text" name="addressee" value="${detail.addressee }">
                                        </div>
                                        <div class="datum_head_left">
                                            <span>收货人地址：</span>
                                            <input type="text" name="addresseeAddr" value="${detail.addresseeAddr }">
                                        </div>
                                        <div class="batch_import table_invoice ">
                                            <table>
                                                <thead>
	                                                <tr>
	                                                    <td>
	                                                        <p>COMMODITY.</p>
	                                                        <p>商品名称</p>
	                                                    </td>
	                                                    <td>
	                                                        <p>Article number</p>
	                                                        <p>货号</p>
	                                                    </td>
	                                                    <td>
	                                                        <p>Unit</p>
	                                                        <p>单位</p>
	                                                    </td>
	                                                    <td>
	                                                        <p>QUANTITY( PCS )</p>
	                                                        <p>数量</p>
	                                                    </td>
	                                                    <td>
	                                                        <p>UNITY PRICE</p>
	                                                        <p>单价(RMB)</p>
	                                                    </td>
	                                                    <td>
	                                                        <p>AMOUNT</p>
	                                                        <p>总价（RMB）</p>
	                                                    </td>
	                                                    <td>
	                                                        <p></p>
	                                                        <p>原产国</p>
	                                                    </td>
	                                                </tr>
                                                </thead>
                                                <tr>
                                                    <td></td>
                                                    <td></td>
                                                    <td></td>
                                                    <td></td>
                                                    <td colspan="2">CIF NANSHA</td>
                                                    <td></td>
                                                </tr>
                                                <tbody>
                                                <c:forEach items="${detail.itemList }" var="item">
                                                	<tr>
	                                                    <td>${item.goodsName }</td>
	                                                    <td>${item.goodsNo }</td>
	                                                    <td>${item.unit }</td>
	                                                    <td>${item.num }</td>
	                                                    <td>${item.price }</td>
	                                                    <td>${item.amount }</td>
	                                                    <td>${item.countryName }</td>
	                                                </tr>
                                                </c:forEach>
                                                <tr>
                                                    <td>TOTAL：</td>
                                                    <td></td>
                                                    <td></td>
                                                    <td>${totalNum }</td>
                                                    <td></td>
                                                    <td>${totalAmount }</td>
                                                    <td></td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                        <div class="invoice_info">
                                            <p><span>1.Shipment before（装船时间）：</span><input type="text" onblur="setSameVale('shipDateSpan', this)" name="shipDateStr" class="form_datetime date-input " value='<fmt:formatDate value="${detail.shipDate}" pattern="yyyy-MM-dd"/>'></p>
                                            <p><span>2.Payment（付款方式）：</span><input type="text" name="payment" onblur="setSameVale('paymentSpan', this)"  value="${detail.payment }"></p>
                                            <p><span>3.Packing（包装）：</span><input type="text" name="pack" onblur="setSameVale('packSpan', this)" value="${detail.pack }"></p>
                                            <p><span>4.Port of Loading（装货港）：</span><input type="text" name="portLoading" onblur="setSameVale('portLoadingSpan', this)"  value="${detail.portLoading }"></p>
                                            <p><span>5.Port of Discharge（卸货港）：</span><input type="text" name="portDest" onblur="setSameVale('portDestSpan', this)"  value="${detail.portDest }"></p>
                                            <p><span>6.Payment RULES（付款条约）：</span><input type="text" name="payRules" onblur="setSameVale('payRulesSpan', this)" value="${detail.payRules }"></p>
                                            <p><span>7.Country of Origin（原产国）：</span><input type="text" name="country" onblur="setSameVale('countrySpan', this)"  value="${detail.country }"></p>
                                            <p><span>8.Shipper（境外发货人）：</span><input type="text" name="shipper" value="${detail.shipper }" onblur="setSameVale('shipperSpan', this)"></p>
                                            <p><span>9.Marks and Numbers（唛头）：</span><input type="text" name="mark" value="${detail.mark }" onblur="setSameVale('markSpan', this)"></p>
                                        </div>
                                        <p class="invoice_remark">注：本发票通过扫描件方式出具同样具有法律效力。</p>
                                    </div>
                                    <div class="tab-pane fade" id="packingList">
                                        <p class="customs_clearance_titile">装箱单</p>
                                        <div class="datum_head_right">
                                            <span>DATE时间：</span>
                                            <span class="datum_head_right_con"><fmt:formatDate value="${detail.invoiceDate}" pattern="yyyy-MM-dd"/></span>
                                        </div>
                                        <div class="datum_head_right">
                                            <span>INVOICE NO发票号：</span>
                                            <span class="datum_head_right_con">${detail.invoiceNo }</span>
                                        </div>
                                        <div class="datum_head_right">
                                            <span>CONTRACT NO合同号：</span>
                                            <span class="datum_head_right_con">${detail.contractNo }</span>
                                        </div>
                                        <div class="datum_head_left">
                                            <span>Consignee：</span>
                                            ${detail.eAddressee }
                                        </div>
                                        <div class="datum_head_left">
                                            <span>Address：</span>
                                            ${detail.eAddresseeAddr }
                                        </div>
                                        <div class="datum_head_left">
                                            <span>收货人：</span>
                                            ${detail.addressee }
                                        </div>
                                        <div class="datum_head_left">
                                            <span>收货人地址：</span>
                                            ${detail.addresseeAddr }
                                        </div>
                                        <div class="batch_import table_invoice ">
                                            <table>
                                                <thead>
                                                <tr>
                                                    <td>
                                                        <p>COMMODITY.</p>
                                                        <p>商品名称</p>
                                                    </td>
                                                    <td>
                                                        <p>Specifications</p>
                                                        <p>规格</p>
                                                    </td>
                                                    <td>
                                                        <p>Article number</p>
                                                        <p>货号</p>
                                                    </td>
                                                    <td>
                                                        <p>Unit</p>
                                                        <p>单位</p>
                                                    </td>
                                                    <td>
                                                        <p>QUANTITY( PCS )</p>
                                                        <p>数量</p>
                                                    </td>
                                                    <td>
                                                        <p>NET WEIGHT（KGS）</p>
                                                        <p>净重</p>
                                                    </td>
                                                    <td>
                                                        <p>GROSS WEIGHT（KGS）</p>
                                                        <p>毛重</p>
                                                    </td>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach items="${detail.itemList }" var="item">
                                                	<tr>
	                                                    <td>${item.goodsName }</td>
	                                                    <td>${item.goodsSpec }</td>
	                                                    <td>${item.goodsNo }</td>
	                                                    <td>${item.unit }</td>
	                                                    <td>${item.num }</td>
	                                                    <td>${item.netWeight }</td>
	                                                    <td>${item.grossWeight }</td>
	                                                </tr>
                                                </c:forEach>
                                                <tr>
                                                    <td>TOTAL：</td>
                                                    <td></td>
                                                    <td></td>
                                                    <td></td>
                                                    <td>${totalNum }</td>
                                                    <td></td>
                                                    <td>${totalgrossWeight }</td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                        <div class="invoice_info">
                                            <p><span>1.Shipment before（装船时间）：</span><span id="shipDateSpan"><fmt:formatDate value="${detail.shipDate}" pattern="yyyy-MM-dd"/></span></p>
                                            <p><span>2.Payment（付款方式）：</span><span id="paymentSpan">${detail.payment }</span></p>
                                            <p><span>3.Packing（包装）：</span><span id="packSpan">${detail.pack }</span></p>
                                            <p><span>4.Port of Loading（装货港）：</span><span id="portLoadingSpan">${detail.portLoading }</span></p>
                                            <p><span>5.Port of Discharge（卸货港）：</span><span id="portDestSpan">${detail.portDest }</span></p>
                                            <p><span>6.Payment RULES（付款条约）：</span><span id="payRulesSpan">${detail.payRules }</span></p>
                                            <p><span>7.Country of Origin（原产国）：</span><span id="countrySpan">${detail.country }</span></p>
                                            <p><span>8.Shipper（境外发货人）：</span><span id="shipperSpan">${detail.shipper }</span></p>
                                            <p><span>9.Marks and Numbers（唛头）：</span><span id="markSpan">${detail.mark }</span></p>
                                        </div>
                                        <p class="invoice_remark">注：本发票通过扫描件方式出具同样具有法律效力。</p>
                                    </div>
                                    <div class="tab-pane fade" id="packingDetail">
                                        <p class="customs_clearance_titile">装箱明细</p>
                                        <div class="datum_head_left">
                                            <span>Consignee：</span>
                                            ${detail.eAddressee }
                                        </div>
                                        <div class="datum_head_left">
                                            <span>收货人：</span>
                                            ${detail.addressee }
                                        </div>
                                        <div class="batch_import table_invoice ">
                                            <table>
                                                <thead>
                                                <tr>
                                                    <td>
                                                        <p>The pallet no.</p>
                                                        <p>托盘号</p>
                                                    </td>
                                                    <td>
                                                        <p>COMMODITY.</p>
                                                        <p>商品名称</p>
                                                    </td>
                                                    <td>
                                                        <p>Article number</p>
                                                        <p>货号</p>
                                                    </td>
                                                    <td>
                                                        <p>QUANTITY ( PCS )</p>
                                                        <p>数量</p>
                                                    </td>
                                                    <td>
                                                        <p>Cartons</p>
                                                        <p>箱数</p>
                                                    </td>
                                                    <td>
                                                        <p>NET WEIGHT（KGS）</p>
                                                        <p>净重</p>
                                                    </td>
                                                    <td>
                                                        <p>GROSS WEIGHT（KGS）</p>
                                                        <p>毛重</p>
                                                    </td>
                                                    <td>
                                                        <p>Container No.</p>
                                                        <p>柜号</p>
                                                    </td>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:set var="totalCarton" value="0"></c:set>
                                                <c:forEach items="${detail.itemList }" var="item">
                                                	<tr>
                                                		<td>
                                                			<input type="hidden" name="ids" value="${item.goodsId }">
                                                			<c:choose>
                                                				<c:when test="${item.palletNo == null }">
                                                					<input type="text" name="palletNos" style='width:70px;text-align:center;' value=" ">
                                                				</c:when>
                                                				<c:otherwise>
                                                					<input type="text" name="palletNos" style='width:70px;text-align:center;' value="${item.palletNo }">
                                                				</c:otherwise>
                                                			</c:choose>
                                                		</td>
	                                                    <td>${item.goodsName }</td>
	                                                    <td>${item.goodsNo }</td>
	                                                    <td>${item.num }</td>
	                                                    <td>
	                                                    	<c:choose>
                                                				<c:when test="${item.cartons == null }">
                                                					<input type="text" name="carton" style='width:70px;text-align:center;' onkeyup="value=value.replace(/[^\d]/g,'')" value="0">
                                                				</c:when>
                                                				<c:otherwise>
                                                					<c:set var="totalCarton" value="${totalCarton + item.cartons }"></c:set>
                                                					<input type="text" name="carton" style='width:70px;text-align:center;' onkeyup="value=value.replace(/[^\d]/g,'')" value="${item.cartons }">
                                                				</c:otherwise>
                                                			</c:choose>
	                                                    </td>
	                                                    <td>${item.netWeight }</td>
	                                                    <td>${item.grossWeight }</td>
	                                                    <td>/</td>
	                                                </tr>
                                                </c:forEach>
                                                <tr>
                                                    <td>TOTAL：</td>
                                                    <td></td>
                                                    <td></td>
                                                    <td>${totalNum }</td>
                                                    <td>${totalCarton }</td>
                                                    <td>${totalnetWeight }</td>
                                                    <td>${totalgrossWeight }</td>
                                                    <td></td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <div class="tab-pane fade" id="rawMaterial">
                                        <p class="customs_clearance_titile">原料成分占比</p>
                                        <div class="batch_import table_invoice ">
                                            <table>
                                                <thead>
                                                <tr>
                                                    <td>商品名称</td>
                                                    <td>成分占比</td>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach items="${detail.itemList }" var="item">
                                                	<tr>
	                                                    <td>${item.goodsName }</td>
	                                                    <td>${item.constituentRatio }</td>
	                                                </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                        <p class="invoice_remark">注：本成分占比通过扫描件方式出具同样具有法律效力。</p>
                                    </div>
                                </div>
								<div class="form-row m-t-50">
			                          <div class="col-12">
			                              <div class="row">
			                                  <div class="col-6">
			                                      <input type="button" class="btn btn-info waves-effect waves-light fr btn_default" id="save-buttons" value="保 存">
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
	//返回
	$("#cancel-buttons").click(function(){
		$module.load.pageOrder("act=3002");
	});
	
	$(".date-input").datetimepicker({
        language:  'zh-CN',
        format: "yyyy-mm-dd",
		autoclose: 1,
		todayHighlight: 1,
		startView: 2,
		minView: 2
    });
	
	//保存按钮
	$("#save-buttons").click(function(){
		var url = serverAddr+"/declare/modifyCustomsDeclare.asyn";
		var form = $("#edit-form").serializeArray();
		$custom.request.ajax(url, form, function(result){
			if(result.state == '200'){
				$custom.alert.success("编辑成功！");
				setTimeout(function () {
					$module.load.pageOrder("act=3002");
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
	
	function setSecondParty(org){
		var secondParty = $(org).val() ;
		
		$("#secondPartySpan").text(secondParty) ;
	}
	
	function setSameVale(id, org){
		var val = $(org).val() ;
		
		$("#" + id).text(val) ;
	}
	
</script>
