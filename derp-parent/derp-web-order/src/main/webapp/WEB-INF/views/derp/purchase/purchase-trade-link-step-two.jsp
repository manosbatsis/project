<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
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

.error-msg-div {
	top: 0px;
	min-height: 90px;
	background: inherit;
	background-color: rgba(250, 205, 145, 0.47843137254902);
	box-sizing: border-box;
	border-width: 1px;
	border-style: solid;
	border-color: rgba(255, 255, 128, 1);
	border-radius: 0px;
	-moz-box-shadow: none;
	-webkit-box-shadow: none;
	box-shadow: none;
	text-align: left;
	line-height: 20px;
	margin: 10px;
}

#nav li {
	list-style: none;
	width: 120px;
	margin-top: 0;
	padding: 4px 6px;
}

#nav li a {
	text-decoration: none;
	color: #666;
}

#nav li a:hover {
	color: #333;
}

.aa {
	border-right: 1px solid #AAC7E9;
	background: #E8F5FE;
	cursor: hand;
	border-top: 3px solid #AAC7E9
}

.bb {
	border: 1px solid #AAC7E9;
	background: #FFFFDD;
	cursor: hand;
	border-top: 3px solid #ff9900;
}

.cc {
	border-top: 4px solid #ff66ff;
	background: #fcf;
	cursor: hand;
}

.list2 {
	font-size: 13px;
	line-height: 20px;
	padding: 3px;
	text-align: left;
	background: #FFFFFF;
}

.list2   li {
	color: #555;
	font-size: 13px;
	line-height: 24px;
	padding: 0 0 0 10px;
}

.list2    a {
	text-decoration: underline;
}

.lfloat {
	float: left;
}

.rfloat {
	float: right;
}

.ctt {
	padding: 10px;
	clear: both;
	text-align: left;
	height: 600px;
	overflow-y:scroll;
}

.dis {
	display: block;
}

.undis {
	display: none;
}

.content-div li {
	list-style: none;
}

.content-div ul {
	padding: 0;
	margin: 0;
}

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
                       <li class="breadcrumb-item"><a href="#">采购链路步骤二</a></li>
                    </ol>
                    </div>
                   </div>
                    <!--  title   end  -->     
                    <div id="step"></div>          
                          <!--  title   基本信息   start -->
                    <form id="add-form">
                    	<input type="hidden" id="purchaseTradeLinkId" value="${id }">
                    	<c:if test="${not empty map.errorList}">
	                    	<div class="error-msg-div">
	                    		<c:forEach items="${map.errorList}" var="errorMsg" varStatus="errorIndex">
	                    			<div style="margin: 10px;">${errorIndex.index + 1}、${ errorMsg}</div>
	                    		</c:forEach>
	                    	</div>
                    	</c:if>
                    	<ul class="nav nav-tabs">
                    		<c:forEach items="${map}" var="item" varStatus="index">
                    			<c:if test="${ item.key != 'errorList'}">
									<li class="nav-item" >
										<a href="#${item.key }" data-toggle="tab" class="nav-link <c:if test="${ index.index == 0}">active</c:if>" onclick="">${item.key }</a>
									</li>
								</c:if>
							</c:forEach>
						</ul>
						<div class="tab-content">
						<c:forEach items="${map}" var="item" varStatus="index">
							<c:if test="${ item.key != 'errorList'}">
								<div id="${item.key }" class="content-div tab-pane fade <c:if test="${ index.index == 0}"> in active show</c:if>">
									<div>
										 <div class="lfloat" style="width:10%;">
											 <ul id="nav">
												 <li class="bb tb" id="tb_${item.key }_1"  onClick="x:hoverli('${item.key }_1');">销售订单(${fn:length(item.value['saleOrderList'])})</li>
												 <li class="aa tb" id="tb_${item.key }_2" onClick="x:hoverli('${item.key }_2');">采购订单(${fn:length(item.value['purchaseList'])})</li>
												 <li class="aa tb" id="tb_${item.key }_3" onClick="x:hoverli('${item.key }_3');">PO  合同(${fn:length(item.value['purchaseContractList'])})</li>
											 </ul>
										 </div>
										 <div class="lfloat" style="width:90%;">
											  <div id="newinfo">
												     <div class="ctt list2 ">
													    <div class="dis tbc saleOrder" id="tbc_${item.key }_1">
													    	<c:forEach items="${item.value['saleOrderList']}" var="order" >
													    		<div class="info_item">
										                            <div class="info_text">
										                                <span>客户：</span>
										                                <span>
										                                    ${order.customerName}
										                                </span>
										                            </div>
										                            <div class="info_text">
										                                <span>销售类型：</span>
										                                <span>
										                                    	<c:if test="${order.businessModel == '1' }">购销-整批结算</c:if>
										                                    	<c:if test="${order.businessModel == '4' }">购销-实销实结</c:if>
										                                    	<c:if test="${order.businessModel == '0' }">购销-预售订单</c:if>
										                                    	<c:if test="${order.businessModel == '3' }">采销执行</c:if>
										                                </span>
										                            </div>
										                            <div class="info_text">
										                                <span>事业部：</span>
										                                <span>
										                               	 	${order.buName}	
										                                </span>
										                            </div>
										                        </div>
										                        <div class="info_item">
										                            <div class="info_text">
										                                <span>出库仓库：</span>
										                                <span>
										                                    ${order.outDepotName}
										                                </span>
										                            </div>
										                            <div class="info_text">
										                                <span>是否同关区：</span>
										                                <span>
										                                    	跨关区
										                                </span>
										                            </div>
										                            <div class="info_text">
										                                <span>入库仓库：</span>
										                                <span>
										                                </span>
										                            </div>
										                        </div>
										                        <div class="info_item">
										                            <div class="info_text">
										                                <span>PO 号：</span>
										                                <span>
										                                    ${order.poNo}
										                                </span>
										                            </div>
										                            <div class="info_text">
										                                <span>销售币种：</span>
										                                <span>
										                                    ${order.currencyLabel}
										                                </span>
										                            </div>
										                            <div class="info_text">
										                                <span>运输方式：</span>
										                                <span>
										                                	${order.transportLabel}
										                                </span>
										                            </div>
										                        </div>
										                        <div class="info_item">
										                            <div class="info_text">
										                                <span>标准箱TEU：</span>
										                                <span>
										                                    ${order.teu}
										                                </span>
										                            </div>
										                            <div class="info_text">
										                                <span>承运商：</span>
										                                <span>
										                                    ${order.carrier}
										                                </span>
										                            </div>
										                            <div class="info_text">
										                                <span>车次：</span>
										                                <span>
										                                	${order.trainno}
										                                </span>
										                            </div>
										                        </div>
										                        <div class="info_item">
										                        	<div class="info_text">
										                                <span>拖数：</span>
										                                <span>
										                                    ${order.torusNumber}
										                                </span>
										                            </div>
										                            <div class="info_text">
										                                <span></span>
										                                <span>
										                                </span>
										                            </div>
										                            <div class="info_text">
										                                <span></span>
										                                <span>
										                                </span>
										                            </div>
										                        </div>
										                        <div class="title_text">商品明细</div>
									                            <div class="form-row mt20">
									                                <table id="table-list" class="table table-striped table-responsive" cellspacing="0"
									                                       width="100%">
									                                    <thead>
									                                    <tr>
									                                    	<th width="8%">商品编码</th>
									                                        <th width="20%" style="min-width: 220px">商品名称</th>
									                                        <th>商品货号</th>
									                                        <th>条码</th>
									                                        <th>销售数量</th>
									                                        <th>销售单价</th>
									                                        <th>销售总金额</th>
									                                        <th>申报单价</th>
									                                        <th>毛重（KG)</th>
									                                        <th>净重（KG)</th>
									                                    </tr>
									                                    </thead>
									                                    <tbody>
									                                    <c:set var="totalNum" value="0"></c:set>
									                                    <c:set var="totalAmount" value="0.0"></c:set>
									                                    <c:set var="grossWeightSum" value="0.0"></c:set>
									                                    <c:set var="netWeightSum" value="0.0"></c:set>
									                                    <c:forEach items="${order.itemList }" var="goods">
									                                        <tr>
									                                            <td>${goods.goodsCode }</td>
									                                            <td>${goods.goodsName }</td>
									                                            <td>${goods.goodsNo }</td>
									                                            <td>${goods.barcode }</td>
									                                            <td class="num">
									                                            	${goods.num }
									                                            	<c:set var="totalNum" value="${totalNum + goods.num }"></c:set>
									                                            </td>
									                                            <input type="hidden" class="goodsId" value="${goods.goodsId }">
									                                            <td class="price"><fmt:formatNumber value="${goods.price }" pattern="#.##" minFractionDigits="3" > </fmt:formatNumber></td>
									                                            <td class="amount">
									                                            	<input type="text" class="purchaseOrderGoodsAmount" onblur="changeAmount(this)" onkeyup="value=value.replace(/[^\d^\.]/g,'')" value="<fmt:formatNumber value="${goods.amount }" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>">
									                                            	<c:set var="totalAmount" value="${totalAmount + goods.amount }"></c:set>
									                                            </td>
									                                            <td><fmt:formatNumber value="${goods.declarePrice }" pattern="#.##" minFractionDigits="3" ></fmt:formatNumber></td>
									                                            <td class="grossWeights">
									                                            	${goods.grossWeightSum }
									                                            	<c:set var="grossWeightSum" value="${grossWeightSum + goods.grossWeightSum }"></c:set>
									                                            </td>
									                                            <td class="netWeight">
									                                            	${goods.netWeightSum }
									                                            	<c:set var="netWeightSum" value="${netWeightSum + goods.netWeightSum }"></c:set>
									                                            </td>
									                                        </tr>
									                                    </c:forEach>
									                                    <tr>
								                                            <td>合计</td>
								                                            <td></td>
								                                            <td></td>
								                                            <td></td>
								                                            <td>${ totalNum }</td>
								                                            <td></td>
								                                            <td class="totalAmount"><fmt:formatNumber value="${totalAmount}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber></td>
								                                            <td></td>
								                                            <td class="grossWeights">${ grossWeightSum }</td>
								                                            <td class="netWeight">${ netWeightSum }</td>
								                                        </tr>
									                                    </tbody>
									                                </table>
									                            </div>
													    	</c:forEach>
														</div>
													    <div class="undis tbc purchaseOrder" id="tbc_${item.key }_2">
													    	<c:forEach items="${item.value['purchaseList']}" var="order" >
													    		<div class="info_item">
										                            <div class="info_text">
										                                <span>供应商：</span>
										                                <span>
										                                    ${order.supplierName}
										                                </span>
										                            </div>
										                            <div class="info_text">
										                                <span>付款主体：</span>
										                                <span>
										                                    ${order.merchantName}
										                                </span>
										                            </div>
										                            <div class="info_text">
										                                <span>事业部：</span>
										                                <span>
										                               	 	${order.buName}	
										                                </span>
										                            </div>
										                        </div>
										                        <div class="info_item">
										                            <div class="info_text">
										                                <span>业务模式：</span>
										                                <span>
									                                    	<c:if test="${order.businessModel == '1' }">购销</c:if>
									                                    	<c:if test="${order.businessModel == '3' }">采销执行</c:if>
										                                </span>
										                            </div>
										                            <div class="info_text">
										                                <span>入库仓库：</span>
										                                <span>
										                                    ${order.depotName}	
										                                </span>
										                            </div>
										                            <div class="info_text">
										                                <span>归属日期：</span>
										                                <span>
										                                	<fmt:formatDate value="${order.attributionDate }" pattern="yyyy-MM-dd"/>
										                                </span>
										                            </div>
										                        </div>
										                        <div class="info_item">
										                            <div class="info_text">
										                                <span>PO 号：</span>
										                                <span>
										                                    ${order.poNo}
										                                </span>
										                            </div>
										                            <div class="info_text">
										                                <span>采购币种：</span>
										                                <span>
										                                    ${order.currencyLabel}
										                                </span>
										                            </div>
										                            <div class="info_text">
										                                <span>提单号：</span>
										                                <span>
										                                	${order.ladingBill}
										                                </span>
										                            </div>
										                        </div>
										                        <div class="info_item">
										                            <div class="info_text">
										                                <span>装货港：</span>
										                                <span>
										                                    ${order.loadPort}
										                                </span>
										                            </div>
										                            <div class="info_text">
										                                <span>卸货港：</span>
										                                <span>
										                                    ${order.unloadPort}
										                                </span>
										                            </div>
										                            <div class="info_text">
										                                <span>运输方式：</span>
										                                <span>
										                                	${order.transportLabel}
										                                </span>
										                            </div>
										                        </div>
										                        <div class="info_item">
										                        	<div class="info_text">
										                                <span>标准箱TEU：</span>
										                                <span>
										                                    ${order.standardCaseTeu}
										                                </span>
										                            </div>
										                            <div class="info_text">
										                                <span>承运商：</span>
										                                <span>
										                                	${order.carrier}
										                                </span>
										                            </div>
										                            <div class="info_text">
										                                <span>车次：</span>
										                                <span>
										                                	${order.trainNumber}
										                                </span>
										                            </div>
										                        </div>
										                        <div class="info_item">
										                        	<div class="info_text">
										                                <span>拖数：</span>
										                                <span>
										                                    ${order.torrNum}
										                                </span>
										                            </div>
										                            <div class="info_text">
										                                <span>提单毛重KG：</span>
										                                <span>
										                                	${order.grossWeight}
										                                </span>
										                            </div>
										                            <div class="info_text">
										                                <span></span>
										                                <span>
										                                </span>
										                            </div>
										                        </div>
										                        <div class="title_text">商品明细</div>
									                            <div class="form-row mt20">
									                                <table id="table-list" class="table table-striped table-responsive" cellspacing="0"
									                                       width="100%">
									                                    <thead>
									                                    <tr>
									                                    	<th width="8%">商品编码</th>
									                                        <th width="20%" style="min-width: 220px">商品名称</th>
									                                        <th>商品货号</th>
									                                        <th>条码</th>
									                                        <th>采购数量</th>
									                                        <th>采购单价</th>
									                                        <th>采购金额</th>
									                                    </tr>
									                                    </thead>
									                                    <tbody>
									                                    <c:set var="totalPurchaseNum" value="0"></c:set>
									                                    <c:set var="totalPurchaseAmount" value="0.0"></c:set>
									                                    <c:forEach items="${order.itemList }" var="goods">
									                                        <tr>
									                                            <td>${goods.goodsCode }</td>
									                                            <td>${goods.goodsName }</td>
									                                            <td>${goods.goodsNo }</td>
									                                            <td>${goods.barcode }</td>
									                                            <td class="num">
									                                            	${goods.num }
									                                            	<c:set var="totalPurchaseNum" value="${totalPurchaseNum + goods.num }"></c:set>
									                                            </td>
									                                            <input type="hidden" class="goodsId" value="${goods.goodsId }">
									                                            <td class="price"><fmt:formatNumber value="${goods.price }" pattern="#.##" minFractionDigits="3" > </fmt:formatNumber></td>
									                                            <td class="amount">
									                                            	<input type="text" class="salesOrderGoodsAmount" onblur="changeAmount(this)" onkeyup="value=value.replace(/[^\d^\.]/g,'')" value="<fmt:formatNumber value="${goods.amount }" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>">
									                                            	<c:set var="totalPurchaseAmount" value="${totalPurchaseAmount + goods.amount }"></c:set>
									                                            </td>
									                                        </tr>
									                                    </c:forEach>
									                                    <tr>
								                                            <td>合计</td>
								                                            <td></td>
								                                            <td></td>
								                                            <td></td>
								                                            <td>${ totalPurchaseNum }</td>
								                                            <td></td>
								                                            <td class="totalAmount"><fmt:formatNumber value="${totalPurchaseAmount}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber></td>
								                                        </tr>
									                                    </tbody>
									                                </table>
									                            </div>
													    	</c:forEach>
													    </div>
													    <div class="undis tbc" id="tbc_${item.key }_3">
													    	<c:set var="merchantName" value="${item.key }"></c:set>
													    	<c:forEach items="${item.value['purchaseContractList']}" var="contract" >
													    	<div>
						                                        <p class="purchase_contract_title">采购订单（C）</p>
						                                        <p class="purchase_contract_title">Purchase Contract</p>
						                                        <div class="purchase_contract_head_left">
						                                            <span>采购合同编号：</span>
						                                            <span class="detail-span" style="text-align: left;">${contract.purchaseContractNo }</span>
						                                            <input type="hidden" id="orderId" name="orderId" value="${contract.orderId }">
						                                            <span>采购订单号：</span>
						                                            <span class="detail-span" style="text-align: left;">${contract.purchaseOrderNo }</span>
						                                        </div>
						                                        <div class="purchase_contract_head_left">
						                                            <span>Purchase contract No.：</span>
						                                            <span class="detail-span" style="text-align: left;">${contract.purchaseContractNo }</span>
						                                            <span>Purchase order No.：</span>
						                                            <span class="detail-span" style="text-align: left;">${contract.purchaseOrderNo }</span>
						                                        </div>
						                                        <div class="purchase_contract_head_left">
						                                            <span>甲方：</span>
						                                            <span class="detail-span" style="text-align: left;">${contract.buyerCnName }</span>
						                                            <span>乙方：</span>
						                                            <span class="detail-span" style="text-align: left;">${contract.sellerCnName }</span>
						                                        </div>
						                                        <div class="purchase_contract_head_left">
						                                            <span>Buyer：</span>
						                                            <span class="detail-span" style="text-align: left;">${contract.buyerEnName }</span>
						                                            <span>Seller：</span>
						                                            <span class="detail-span" style="text-align: left;">${contract.sellerEnName }</span>
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
						                                                    <td>单价(${contract.currency })<br>Unit Value</td>
						                                                    <td>总价(${contract.currency })<br>Total Value</td>
						                                                </tr>
						                                                </thead>
						                                                <tbody>
						                                                <c:set var="totalPurchaseNum" value="0"></c:set>
									                                    <c:set var="totalPurchaseAmount" value="0.0"></c:set>
						                                                <c:forEach items="${contract.itemList }" var="item">
						                                                	<tr>
							                                                    <td>${item.brandName }</td>
							                                                    <td>${item.goodsName }</td>
							                                                    <td>${item.barcode }</td>
							                                                    <td>${item.spec }</td>
							                                                    <td>${item.num }</td>
							                                                    <c:set var="totalPurchaseNum" value="${totalPurchaseNum + item.num }"></c:set>
							                                                    <td><fmt:formatNumber value="${item.price }" pattern="#.##" minFractionDigits="3" > </fmt:formatNumber></td>
							                                                    <td><fmt:formatNumber value="${item.amount }" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber></td>
							                                                    <c:set var="totalPurchaseAmount" value="${totalPurchaseAmount + item.amount }"></c:set>
							                                                </tr>
						                                                </c:forEach>
						                                                <tr>
						                                                	<td colspan="4">合计 Total</td>
						                                                	<td>${totalPurchaseNum }</td>
						                                                	<td>/</td>
						                                                	<td><fmt:formatNumber value="${totalPurchaseAmount }" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber></td>
						                                                </tr>
						                                                </tbody>
						                                            </table>
						                                        </div>
						                                        <div class="mt20">
						                                        	<span class="purchase_contract_sub_title">2、</span>
						                                        	<div class="purchase_contract_sub_content">
							                                            <span class="purchase_contract_inline" style="width: 18%;">运输方式：</span>
							                                            <input type="checkbox" name="meansOfTransportation" value="CIF"  disabled>CIF
							                                            <input type="checkbox" name="meansOfTransportation" value="CFR"  disabled>CFR
							                                            <input type="checkbox" name="meansOfTransportation" value="FOB"  disabled>FOB
							                                            <input type="checkbox" name="meansOfTransportation" value="DPA"  disabled>DPA
							                                            <input type="checkbox" name="meansOfTransportation" value="EXW"  disabled>EXW
							                                            <input type="checkbox" name="meansOfTransportation" value="BY SEA"  disabled>海运
							                                            <input type="checkbox" name="meansOfTransportation" value="BY AIR"  disabled>空运
							                                            <input type="checkbox" name="meansOfTransportation" value="BY LAND"  disabled>陆运
						                                            </div>
						                                        </div>
						                                        <div class="mt10">
						                                        	<div class="purchase_contract_sub_content">
							                                            <span class="purchase_contract_inline" style="width: 18%;">Means of Transportation：</span>
							                                            <input type="checkbox"  value="CIF"  disabled>CIF
							                                            <input type="checkbox" value="CFR"  disabled>CFR
							                                            <input type="checkbox" value="FOB"  disabled>FOB
							                                            <input type="checkbox" value="DPA"  disabled>DPA
							                                            <input type="checkbox" value="EXW"  disabled>EXW
							                                            <input type="checkbox" value="BY SEA"  disabled>BY SEA
							                                            <input type="checkbox" value="BY AIR"  disabled>BY AIR
							                                            <input type="checkbox" value="BY LAND"  disabled>BY LAND
						                                            </div>
						                                        </div>
						                                        <div class="mt20">
						                                        	<span class="purchase_contract_sub_title">3、</span>
						                                        	<div class="purchase_contract_sub_content meansOfTransportation1">
							                                            <span class="purchase_contract_inline purchase_contract_w20 ">始发地（港）：</span>
							                                            <span class="detail-span" style="text-align: left;">${contract.loadingCnPort }</span>
							                                            <span class="purchase_contract_inline purchase_contract_w20 ">目的地（港）：</span>
							                                            <span class="detail-span" style="text-align: left;">${contract.destinationdCn }</span>
						                                            </div>
						                                            <div class="purchase_contract_sub_content meansOfTransportation2" style="display:none ;">
							                                            <span class="purchase_contract_inline purchase_contract_w20 ">发货地址：</span>
							                                            <span class="detail-span" style="text-align: left; width:45% ;">${contract.deliveryAddress }</span>
						                                            </div>
						                                            <div class="purchase_contract_sub_content meansOfTransportation3" style="display:none ;">
							                                            <span class="purchase_contract_inline purchase_contract_w20 ">提货地址：</span>
							                                            <span class="detail-span" style="text-align: left; width:45% ;">${contract.pickingUpAddress }</span>
						                                            </div>
						                                        </div>
						                                        <div class="mt10 ">
						                                        	<span class="purchase_contract_sub_title"></span>
						                                        	<div class="purchase_contract_sub_content meansOfTransportation1">
							                                            <span class="purchase_contract_inline purchase_contract_w20 ">Loading Port：</span>
							                                            <span class="detail-span" style="text-align: left;">${contract.loadingEnPort }</span>
							                                            <span class="purchase_contract_inline purchase_contract_w20 ">Destination：</span>
							                                            <span class="detail-span" style="text-align: left;">${contract.destinationdEn }</span>
						                                            </div>
						                                            <div class="purchase_contract_sub_content meansOfTransportation2" style="display:none ;">
							                                            <span class="purchase_contract_inline purchase_contract_w20 ">Delivery Address:</span>
							                                            <span class="detail-span" style="text-align: left; width:45% ;">${contract.deliveryAddressEn }</span>
						                                            </div>
						                                            <div class="purchase_contract_sub_content meansOfTransportation3" style="display:none ;">
							                                            <span class="purchase_contract_inline purchase_contract_w20 ">Picking up Address：</span>
							                                            <span class="detail-span" style="text-align: left; width:45% ;">${contract.pickingUpAddressEn }</span>
						                                            </div>
						                                        </div>
						                                        <div class="mt20">
						                                        	<span class="purchase_contract_sub_title">4、</span>
						                                        	<div class="purchase_contract_sub_content">
						                                        		<span class="purchase_contract_w15 purchase_contract_inline">交货日期：</span>
						                                        		<span style="width:4%;" class="contractYear"> </span> 年 
						                                        		<span style="width:4%;" class="contractMonth"> </span> 月 
						                                        		<span style="width:4%;" class="contractDays"> </span> 日 
						                                        		(若此交货日期与甲方与最终买方签署的框架采购合同不一致的,以此交货期为准)
						                                        	</div>
						                                        </div>
						                                        <div class="mt10">
						                                        	<span class="purchase_contract_sub_title"></span>
						                                        	<div class="purchase_contract_sub_content">
						                                        		<span class="purchase_contract_w15 purchase_contract_inline">Delivery date：</span>
						                                        		<span style="width:10%;" class="deliveryEngDate"></span> (If this delivery date is in conflict with the delivery date stipulated in the Purchase Frame Contract signed by the Buyer and the ultimate buyer, this one will prevail.)
						                                        	</div>
						                                        </div>
						                                        <div class="mt20">
						                                        	<span class="purchase_contract_sub_title" style="width: 10%;">付款方式 ：</span>
						                                            <input type="radio" name="paymentMethod"   value="1" disabled> 一次结清
						                                            <input type="radio" name="paymentMethod"  value="2" disabled> 分批付款
						                                            <input type="radio" name="paymentMethod"  value="3" disabled> 其他
						                                        </div>
						                                        <div class="mt20">
						                                        	<span class="purchase_contract_sub_title">5、</span>
						                                        	<div class="purchase_contract_sub_content paymentMethodText_1">
						                                        		<span class="purchase_contract_w15 purchase_contract_inline">付款方式：</span>
						                                        		双方确认本订单之日起 <span class="paymentMethodText_1_span"></span> 个工作日内甲方向乙方支付订单总金额100%货款
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
						                                        		TT 100% within <span class="paymentMethodText_1_en_span"></span> working days from the date of this order.
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
						                                        		【<span class="detail-span" style="text-align: left;">${contract.specialAgreement }</span>】
						                                        	</div>
						                                        </div>
						                                        <div class="mt10">
						                                        	<span class="purchase_contract_sub_title"></span>
						                                        	<div class="purchase_contract_sub_content">
						                                        		<span class="purchase_contract_w15 purchase_contract_inline">Special agreement: </span>
						                                        		【<span class="detail-span" style="text-align: left;">${contract.specialAgreementEn }</span>】
						                                        	</div>
						                                        </div>
						                                        <div class="mt20">
						                                        	<span class="purchase_contract_sub_title">7、乙方（供货方）收款账户  Banking information of the seller：</span>
						                                        	<div class="purchase_contract_content_left">
							                                            <span>账号：</span>
							                                            <span class="detail-span" style="text-align: left; width:45% ;">${contract.accountNo }</span>
							                                        </div>
							                                        <div class="purchase_contract_content_left">
							                                            <span>采购币种：</span>
							                                            <span class="detail-span" style="text-align: left; width:45% ;">${contract.currency }</span>
							                                        </div>
							                                        <div class="purchase_contract_content_left">
							                                            <span>账户：</span>
							                                            <span class="detail-span" style="text-align: left; width:45% ;">${contract.beneficiaryName }</span>
							                                        </div>
							                                        <div class="purchase_contract_content_left">
							                                            <span>开户行：</span>
							                                            <span class="detail-span" style="text-align: left; width:45% ;">${contract.beneficiaryBankName }</span>
							                                        </div>
							                                        <div class="purchase_contract_content_left">
							                                            <span>地址：</span>
							                                            <span class="detail-span" style="text-align: left; width:45% ;">${contract.bankAddress }</span>
							                                        </div>
							                                        <div class="purchase_contract_content_left">
							                                            <span>Swift Code：</span>
							                                            <span class="detail-span" style="text-align: left; width:45% ;">${contract.swiftCode }</span>
							                                        </div>
						                                        </div>
						                                        <div class="mt20">
						                                        	<div class="purchase_contract_content_left">
							                                            <span>Account No：</span>
							                                            <span class="detail-span" style="text-align: left; width:45% ;">${contract.accountNo }</span>
							                                        </div>
							                                        <div class="purchase_contract_content_left">
							                                            <span>Currency：</span>
							                                            <span class="detail-span" style="text-align: left; width:45% ;">${contract.currency }</span>
							                                        </div>
							                                        <div class="purchase_contract_content_left">
							                                            <span>Beneficiary Name：</span>
							                                            <span class="detail-span" style="text-align: left; width:45% ;">${contract.beneficiaryName }</span>
							                                        </div>
							                                        <div class="purchase_contract_content_left">
							                                            <span>Beneficiary Bank Name：</span>
							                                            <span class="detail-span" style="text-align: left; width:45% ;">${contract.beneficiaryBankName }</span>
							                                        </div>
							                                        <div class="purchase_contract_content_left">
							                                            <span>Bank Address：</span>
							                                            <span class="detail-span" style="text-align: left; width:45% ;">${contract.bankAddress }</span>
							                                        </div>
							                                        <div class="purchase_contract_content_left">
							                                            <span>Swift Code：</span>
							                                            <span class="detail-span" style="text-align: left; width:45% ;">${contract.swiftCode }</span>
							                                        </div>
						                                        </div>
						                                        <div class="mt60">
						                                        	<div class="purchase_contract_head_left">
							                                            <span>甲方：</span>
							                                            <span class="detail-span" style="text-align: left;">${contract.buyerAuthorizedSignature }</span>  (盖章)
							                                        </div>
							                                        <div class="purchase_contract_head_left">
							                                            <span>Buyer：</span>
							                                            <span class="detail-span" style="text-align: left;">${contract.buyerAuthorizedSignatureEn }</span> (Common Seal)
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
							                                            <span class="detail-span" style="text-align: left;">${contract.sellerAuthorizedSignature }</span>  (盖章)
							                                        </div>
							                                        <div class="purchase_contract_head_left">
							                                            <span>Seller：</span>
							                                            <span class="detail-span" style="text-align: left;">${contract.sellerAuthorizedSignatureEn }</span>  (Common Seal)
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
						                                    <input type="hidden" id="${merchantName }_conMeansOfTransportation" value="${contract.meansOfTransportation}">
						                                    <input type="hidden" id="${merchantName }_conDeliveryDate" value="${contract.deliveryDate}">
						                                    <input type="hidden" id="${merchantName }_conPaymentMethod" value="${contract.paymentMethod}">
						                                    <input type="hidden" id="${merchantName }_conPaymentMethodText" value="${contract.paymentMethodText}">
						                                    <input type="hidden" id="${merchantName }_conPaymentMethodTextEn" value="${contract.paymentMethodTextEn}">
						                                    </c:forEach>
													    </div>
												   </div>
											    </div>
										 </div>
									</div>
								</div>
							</c:if>
						</c:forEach>
						</div>
		           		
                    </form>
                    <div class="form-row m-t-50">
                        <div class="col-12">
                            <div class="row">
                                <div class="col-4">

                                </div>
                                <div class="col-4">
                                    <input type="button" class="btn btn-light waves-effect waves-light btn_default" id="cancel-buttons" value="取 消" />
                                 	<c:if test="${empty map.errorList}">
                                 	<input type="button" class="btn btn-info waves-effect waves-light btn_default " id="save-buttons" value="下一步"/>
                                 	</c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
              </div>
             </div>
                  <!-- end row -->
                  <!-- 弹窗开始 -->
                  <!-- 弹窗结束 -->
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->
<script type="text/javascript">

	
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
            index: 1,
            time: 500,
            title: ["<font style=\"font-size:12px;\">确定交易链路<br>确定交易链路的参与公司及交易数据</font>",  "<font style=\"font-size:12px;\">预览交易信息<br>预览各公司的采购、销售单据，确认价格等信息</font>", "<font style=\"font-size:12px;\">生成单据<br>生成各公司的采购、销售单据</font>"]
        });
		
		//弹框取消按钮
		$("#cancel-buttons").click(function(){
			var purchaseTradeLinkId = $("#purchaseTradeLinkId").val() ;
			$load.a($module.params.loadOrderPageURL+"act=300171&purchaseTradeLinkId=" + purchaseTradeLinkId);
		});
		
		//跳转step 3
		$("#save-buttons").click(function(){
			var purchaseTradeLinkId = $("#purchaseTradeLinkId").val() ;
			
			var url = serverAddr+"/purchase/saveSaleStepGoodsInfo.asyn";
			
			var goodsInfoStr = structureGoodsInfo() ;
			
			
			
			$custom.request.ajax(url, {"purchaseTradeLinkId":purchaseTradeLinkId,
			    "goodsInfoJson":goodsInfoStr}, function(result){
				if(result.state == '200'){
                    setTimeout(function () {
                    	$load.a($module.params.loadOrderPageURL+"act=30019&id=" + purchaseTradeLinkId);
                    }, 1000);
                }else{
                    if(result.expMessage != null){
                        $custom.alert.error(result.expMessage);
                    }else{
                        $custom.alert.error(result.message);
                    }
                }
			}) ;
			
			
		}) ;
	
	}) ;
	
	<c:forEach items="${map}" var="item" varStatus="index">
		<c:if test="${ item.key != 'errorList'}">
			contractInit("${item.key}") ;
		</c:if>
	</c:forEach>
	
	function contractInit(merchantName){
		var meansOfTransportation = $("#" + merchantName + "_conMeansOfTransportation").val() ;
		var date = $("#" + merchantName + "_conDeliveryDate").val()  ;
		var paymentMethod = $("#" + merchantName + "_conPaymentMethod").val() ;
		var paymentMethodText = $("#" + merchantName + "_conPaymentMethodText").val() ;
		var paymentMethodTextEn = $("#" + merchantName + "_conPaymentMethodTextEn").val() ;
		
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
			
			$(".deliveryEngDate").text(deliveryEndDate) ;
			
			var dateArr = date.split("-") ;
			
			$(".contractYear").text(dateArr[0]) ;
			$(".contractMonth").text(dateArr[1]) ;
			$(".contractDays").text(dateArr[2]) ;
			
		}
		
		if(paymentMethod){
			
			var inputs = $("input[name='paymentMethod'][value='"+paymentMethod+"']") ; 
			$(inputs).each(function(index, input){
				$(input).prop('checked', 'checked') ;
			}) ;
			
			if('1' == paymentMethod){
				$(".paymentMethodText_1").show() ;
				$(".paymentMethodText_2").hide() ;
				$(".paymentMethodText_3").hide() ;
				
				if(paymentMethodText){
					$(".paymentMethodText_1_span").text(paymentMethodText) ;
				}
				
				if(paymentMethodTextEn){
					$(".paymentMethodText_1_en_span").text(paymentMethodTextEn) ;
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
	
	/**左侧tab*/
	function hoverli(n){
		$(".tb").removeClass("bb") ;
		$(".tbc").removeClass("list2") ;
		
		$(".tb").addClass("aa") ;
		$(".tbc").addClass("undis") ;
		
		$("#tb_" + n).removeClass("aa") ;
		$("#tbc_" + n).removeClass("undis") ;
		
		$("#tb_" + n).addClass("bb") ;
		$("#tbc_" + n).addClass("list2") ;
	}
	/**左侧tab*/
	
	/**
	*金额改变时，改变单价总数量
	*/
	function changeAmount(org){
		
		var amount = $(org).val();
		amount = parseFloat(amount) ;
		amount = amount.toFixed(2) ;
		
		$(org).val(amount);
		
		var tr = $(org).parent().parent() ;
		var num = $(tr).find(".num").text() ;
		
		var price = parseFloat(amount) / parseFloat(num) ;
		price = parseFloat(price).toFixed(3) ;
		
		$(tr).find(".price").text(price) ;
		
		var tbody = $(tr).parent() ;
		
		var totalAmount = 0.0 ;
		tbody.find(".amount").each(function(index, item){
			var tempAmount = $(item).find("input").val() ;
			
			totalAmount += parseFloat(tempAmount) ;
			
		}) ;
		
		totalAmount = totalAmount.toFixed(2) ;
		
		$(tbody).find(".totalAmount").text(totalAmount) ;
		
	}
	
	/**
	*  构造修改表体
	*/
	function structureGoodsInfo(){
		
		var goodsInfoJSON = {} ;
		
		// 销售
		$(".saleOrder").each(function(saleOrderIndex, saleOrder){
			
			var id = $(saleOrder).attr("id") ;
	        
	        var merchantName = id.split("_")[1] ;
	        
	        $(saleOrder).find("tbody").each(function(index, tbody){
	            
	            var key = merchantName + "_saleOrder_" + index ; 
	            var arr = new Array() ;
	            
	            $(tbody).find(".amount").each(function(j, amount){
	                
	                var tr = $(amount).parent() ;
	                
	                var goodsId = $(tr).find(".goodsId").eq(0).val() ;
	                var amount = $(amount).find("input").val() ;
	                
	                var json = {"goodsId":goodsId, "amount":amount} ;
	                
	                arr.push(json) ;
	            }) ;
	            
	            goodsInfoJSON[key] = arr ;
	            
	        }) ;
		}) ;
			
			
		// 采购
		$(".purchaseOrder").each(function(purchaseOrderIndex, purchaseOrder){
			
			var id = $(purchaseOrder).attr("id") ;
		        
	        var merchantName = id.split("_")[1] ;
	        
	        $(purchaseOrder).find("tbody").each(function(index, tbody){
	            
	            var key = merchantName + "_purchaseOrder_" + index ; 
	            var arr = new Array() ;
	            
	            $(tbody).find(".amount").each(function(j, amount){
	                
	                var tr = $(amount).parent() ;
	                
	                var goodsId = $(tr).find(".goodsId").eq(0).val() ;
	                var amount = $(amount).find("input").val() ;
	                
	                var json = {"goodsId":goodsId, "amount":amount} ;
	                
	                arr.push(json) ;
	            }) ;
	            
	            goodsInfoJSON[key] = arr ;
	            
	        }) ;
		}) ;
        
       return JSON.stringify(goodsInfoJSON); 
	}
	
</script>
