<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="/WEB-INF/views/common.jsp" />
<style type="text/css">
	.title_text_new{
	    line-height: 40px;
	    font-size: 24px;
	    font-weight: bold;
	    text-align: center;
	}
	.info_box { 
		padding: 10px;
	}
	
	table {
	    border-collapse: collapse;
	    border-spacing: 0;
	}
	 
	td,th {
	    padding: 0;
	}
	 
	.pure-table {
	    border-collapse: collapse;
	    border-spacing: 0;
	    empty-cells: show;
	    border: 1px solid #cbcbcb;
	    width: 95% ;
	    margin-top:25px ;
	    text-align: center;
	}
	 
	.pure-table caption {
	    color: #000;
	    font: italic 85%/1 arial,sans-serif;
	    padding: 1em 0;
	    text-align: center;
	}
	 
	.pure-table td,.pure-table th {
	    border-left: 1px solid #cbcbcb;
	    border-width: 0 0 0 1px;
	    font-size: inherit;
	    margin: 0;
	    overflow: visible;
	    padding: .5em 1em;
	}
	 
	.pure-table thead {
	    background-color: #e0e0e0;
	    color: #000;
	    text-align: left;
	    vertical-align: bottom;
	}
	 
	.pure-table td {
	    background-color: transparent;
	}
	 
	.pure-table-bordered td {
	    border-bottom: 1px solid #cbcbcb;
	}
	 
	.pure-table-bordered tbody>tr:last-child>td {
	    border-bottom-width: 0;
	}
	.text_font {
		font: 14px blod;
	}
	.long_span {
		word-break:normal;
		width: 250px;
		display:block;
		white-space:pre-wrap;
		word-wrap : break-word ;
		overflow: hidden ;
	}
	.totalDiv {
		float: right;
		padding: 10px;
	}

	.form-table{width: 100%;margin: 0 auto;text-align: center;table-layout: fixed;}
	.scroll-box{width: 100%;height: 300px;overflow: auto;overflow-x:hidden;}
	.scroll-box::-webkit-scrollbar {
		width: 6px;}
	.scroll-box::-webkit-scrollbar-thumb{
		background-color:#ccc;
		border-radius: 5px;
	}
	.scroll-box::-webkit-scrollbar-track{
		background-color:#eee;
	}
	.scroll-box::-webkit-scrollbar-thumb:hover {
		background-color:rgb(17, 177, 174)
	}
	.scroll-box::-webkit-scrollbar-thumb:active {
		background-color:rgb(9, 136, 134)
	}
	#receive-detail-th-table th {
		white-space:pre-wrap;
		word-wrap: break-word;
		text-align: center;
	}

	#receive-cost-th-table th {
		white-space:pre-wrap;
		word-wrap: break-word;
		text-align: center;
	}
</style>
<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
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
							                      <li class="breadcrumb-item"><a href="#">账单管理</a></li>
							                      <li class="breadcrumb-item"><a href="javascript:$module.load.pageOrder('act=14001');">应收账单列表</a></li>
							                      <li class="breadcrumb-item"><a href="javascript:;">应收详情</a></li>
							            </ol>
                                    </div>
                                </div>
                                <ul class="nav nav-tabs" >
									<li class="nav-item">
										<a href="#total" data-toggle="tab" class="nav-link active" >应收汇总</a>
									</li>
		
									<li class="nav-item">
										<a href="#detail" data-toggle="tab" class="nav-link" >应收详情</a>
									</li>

									<li class="nav-item">
										<a href="#attachment" data-toggle="tab" class="nav-link" id="attachmentTab">附件审批记录</a>
									</li>

									<li class="nav-item">
										<a href="#verify" data-toggle="tab" class="nav-link" >核销记录</a>
									</li>
				           		</ul>
				           		<div class="tab-content">
				           			<div class="tab-pane fade show active table-responsive" id="total">
				           				<div class="title_text_new"><c:if test="${month != null}" >${month}月</c:if>${bill.customerName}应收账单</div>
										<div class="info_box" style="float: left;">
											<font style="color:blue;">事业部：${ bill.buName}</font>
										</div>
										<div class="info_box" style="float: right;margin-right: 20px;">
		                                	<font style="color:blue;">账单创建日期：<fmt:formatDate value="${ bill.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></font>
		                                </div>
				           				<div class="info_box" style="margin-top: 40px;">
					                        <div class="info_item text_font">
					                            <div class="info_text">
													<span>客户：</span>
													<span>${bill.customerName}</span>
			                                    </div>
			                                    <div class="info_text">
													<span>应收账单号：</span>
													<span>${bill.code}</span>
			                                    </div>
			                                    <div class="info_text">
			                                    	<span style="float: left">关联业务单：</span>
													<c:choose>
														<c:when test="${fn:indexOf(bill.relCode,'&')==-1}">
															<span class="long_span">${bill.relCode}</span>
														</c:when>
														<c:otherwise>
															<c:set value="${fn:split(bill.relCode, '&')}" var="codeList" />
															<c:forEach items="${codeList}" var="code" varStatus="i">
																<c:if test="${i.index == 0}">
																	<span >${code}<a href='###' onclick='showHide(this, 0)'>...查看更多</a></span>
																	<span class="long_span" style='display:none;'>${bill.relCode}<a href='###' onclick='showHide(this, 1)'>收起</a></span>

																</c:if>
															</c:forEach>
														</c:otherwise>
													</c:choose>
			                                    </div>
		                                	</div>
		                                	<div class="info_item text_font">
					                            <div class="info_text">
					                            	<span>开票状态：</span>
					                            	<span>${bill.invoiceStatusLabel}</span>
					                            </div>
					                            <div class="info_text">
													<span>发票号码：</span>
													<span>${bill.invoiceNo}</span>
					                            </div>
					                            <div class="info_text">
						                            <span>账单状态：</span>
						                            <span>${bill.billStatusLabel}</span>
					                            </div>
		                                    </div>

											<div class="info_item text_font">
												<div class="info_text">
													<span style="float: left">PO号：</span>
													<c:choose>
														<c:when test="${fn:indexOf(bill.poNo,'&')==-1}">
															<span class="long_span">${bill.poNo}</span>
														</c:when>
														<c:otherwise>
															<c:set value="${fn:split(bill.poNo, '&')}" var="codeList" />
															<c:forEach items="${codeList}" var="code" varStatus="i">
																<c:if test="${i.index == 0}">
																	<span >${code}<a href='###' onclick='showHide(this, 0)'>...查看更多</a></span>
																	<span class="long_span" style='display:none;'>${bill.poNo}<a href='###' onclick='showHide(this, 1)'>收起</a></span>

																</c:if>
															</c:forEach>
														</c:otherwise>
													</c:choose>
												</div>
												<div class="info_text">
													<span style="float: left">渠道：</span>
													<span class="long_span">${bill.ncChannelName}</span>
												</div>
												<div class="info_text">
													<span style="float: left">销售模式：</span>
													<span>${bill.saleModelLabel}</span>
												</div>
											</div>

											<div class="info_item text_font">
												<div class="info_text">
													<span style="float: left">分类：</span>
													<span class="long_span">${bill.sortTypeLabel}</span>
												</div>
												<div class="info_text">
												</div>
												<div class="info_text">
												</div>
											</div>
		                                    
		                                    <table class="pure-table pure-table-bordered">
		                                    	<tbody>
		                                    		<tr>
		                                    			<td colspan="7">应收款项</td>
		                                    		</tr>
													<tr>
														<td colspan="2">项目</td>
														<td>数量</td>
														<td>金额（含税）</td>
														<td>税额</td>
														<td>金额（不含税）</td>
														<td>币种</td>
													</tr>
													<c:forEach items="${receiveMap}" var="item" varStatus="status">
														<tr>
															<c:if test="${status.index == 0}">
																<td rowspan="${receiveMap.size()}">商品销售收入</td>
															</c:if>
															<td>${item.project_name}</td>
															<td>${item.totalNum }</td>
															<td>${item.totalTaxAmount }</td>
															<td>${item.totalTax }</td>
															<td>${item.totalPrice }</td>
															<td>${bill.currency}</td>
														</tr>
													</c:forEach>
													<c:forEach items="${deductionMap}" var="item" varStatus="status">
														<tr>
															<td>${item.parentProjectName}</td>
															<td>${item.projectName}</td>
															<td>${item.num }</td>
															<td>${item.taxAmount }</td>
															<td>${item.tax }</td>
															<td>${item.price }</td>
															<td>${bill.currency}</td>
														</tr>
													</c:forEach>
													<tr>
														<td colspan="2">合计总额：${totalPriceLabel }</td>
														<td>${totalNum}</td>
														<td>${totalTaxAmount}</td>
														<td>${totalTax}</td>
														<td>${totalPrice}</td>
														<td>${bill.currency }</td>
													</tr>
		                                    	</tbody>
		                                    </table>
		                                </div>
				           			</div>
				           			<div class="tab-pane fade  table-responsive" id="detail">
				           				<div class="title_text_new"><c:if test="${month != null}" >${month}月</c:if>${bill.customerName}应收账单</div>
										<div class="info_box" style="float: left;">
											<font style="color:blue;">事业部：${ bill.buName}</font>
										</div>
										<div class="info_box" style="float: right;margin-right: 20px;">
											<font style="color:blue;">账单创建日期：<fmt:formatDate value="${ bill.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></font>
										</div>
				           				<div class="info_box" style="margin-top: 50px;">
					                        <div class="info_item text_font">
					                            <div class="info_text">
													<span>账期：</span>
													<span>${bill.accountPeriod}</span>
			                                    </div>
			                                    <div class="info_text">
													<span>开票日期：</span>
													<span><fmt:formatDate value="${ bill.invoiceDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
			                                    </div>
			                                    <div class="info_text">
			                                    	<span>结算总额：</span>
													<span>${totalPrice}</span>
			                                    </div>
		                                	</div>
		                                	<div class="info_item text_font">
					                            <div class="info_text">
					                            	<span>结算币种：</span>
					                            	<span>${bill.currency }</span>
					                            </div>
					                            <div class="info_text">
													<span>已回款：</span>
													<span>${alreadyPrice}</span>
					                            </div>
					                            <div class="info_text">
						                            <span>未回款：</span>
						                            <span>${totalPrice - alreadyPrice}</span>
					                            </div>
		                                    </div>
		                                    
		                                    <div style="margin-top: 20px;">
												<ul class="nav nav-tabs" >
													<li class="nav-item">
														<a href="#receiveDetail" data-toggle="tab" class="nav-link active">应收明细</a>
													</li>

													<li class="nav-item">
														<a href="#costDetail" data-toggle="tab" class="nav-link" >费用明细</a>
													</li>
												</ul>
												<div class="tab-content">
													<div class="tab-pane fade show active table-responsive" id="receiveDetail">
														<table id="receive-detail-th-table" class="table table-striped form-table" cellspacing="0" >
															<thead>
															<tr>
																<th><label class="mt-checkbox mt-checkbox-single mt-checkbox-outline"style="width: 50px;">
																	<input id="receive-item-checkAll" type="checkbox" class="group-checkable" /><span></span></label></th>
																<th>系统业务单号</th>
																<th><i class="red">*</i>结算费项</th>
																<th>PO号</th>
																<th>平台SKU编码</th>
																<th>标准条码</th>
																<th>商品货号</th>
																<th>商品名称</th>
																<th><i class="red">*</i>税率</th>
																<th><i class="red">*</i>数量</th>
																<th><i class="red">*</i>结算金额（不含税）</th>
																<th>税额</th>
																<th>结算金额（含税）</th>
																<th>母品牌</th>
																<th>NC收支费项</th>
															</tr>
															</thead>
														</table>

														<div class="scroll-box">
															<table id="receive-detail-td-table" class="table table-striped form-table" cellpadding="0" cellspacing="0">
																<tbody>
																<c:if test="${itemDTOS.size() ==0}">
																	<tr>
																		<td colspan="15" align="center">表中数据为空</td>
																	</tr>
																</c:if>
																<c:forEach var="item" items="${itemDTOS}"  varStatus="status">
																	<tr>
																		<td>
																			<input  name="itemId" class="form-control" type="hidden" value="${item.id}">
																			<label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
																			<input type="checkbox" name="receive-item-check" class="group-checkable" /></label></td>
																		<td><input type="hidden" value="${item.projectId}">${item.code}</td>
																		<td>
																			<input  name="projectId" class="form-control" type="hidden" value="${item.projectId}">
																			<input  name="projectName" class="form-control" type="text" value="${item.projectName}"
																					onclick="selectTable(this, ${bill.customerId});">
																		</td>
																		<td>${item.poNo}</td>
																		<td>${item.platformSku}</td>
																		<td>${item.commbarcode}</td>
																		<td><input type="hidden" value="${item.goodsId}">${item.goodsNo}</td>
																		<td>${item.goodsName}</td>
																		<td>${item.taxRate}</td>
																		<td>${item.num}</td>
																		<td>${item.price}</td>
																		<td>${item.tax}</td>
																		<td>${item.taxAmount}</td>
																		<td><input type="hidden" value="${item.parentBrandId}">${item.parentBrandName}</td>
																		<td>${item.paymentSubjectName}</td>
																	</tr>
																</c:forEach>
																</tbody>
															</table>
														</div>

														<div class="totalDiv" id="receiveItemTotal" style="display: none">
															总数量：${itemTotalNum} &nbsp;&nbsp; 总金额：${itemTotalPrice}
														</div>
													</div>
													<div class="tab-pane fade  table-responsive" id="costDetail">
														<table id="receive-cost-th-table" class="table table-striped form-table" cellspacing="0" >
															<thead>
															<tr>
																<th>
																	<label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
																	<input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" /></label></th>
																<th>费用类型</th>
																<th><i class="red">*</i>类型</th>
																<th>PO号</th>
																<th>标准条码</th>
																<th>商品货号</th>
																<th>商品名称</th>
																<th>发票描述</th>
																<th><i class="red">*</i>母品牌</th>
																<th>数量</th>
																<th><i class="red">*</i>税率</th>
																<th>费用金额（不含税）</th>
																<th>税额</th>
																<th><i class="red">*</i>费用金额（含税）</th>
																<th>备注</th>
															</tr>
															</thead>
														</table>

														<div class="scroll-box">
															<table id="receive-cost-td-table" class="table table-striped form-table" cellpadding="0" cellspacing="0">
																<tbody>
																<c:if test="${costItemDTOS.size() ==0}">
																	<tr>
																		<td colspan="15" align="center">表中数据为空</td>
																	</tr>
																</c:if>
																<c:forEach var="item" items="${costItemDTOS}"  varStatus="status">
																	<tr>
																		<td>
																			<input name="costId" class="form-control" type="hidden" value="${item.id}">
																			<label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
																			<input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" /></label></td>
																		<td>
																			<input  name="projectId" class="form-control" type="hidden" value="${item.projectId}">
																			<input  name="projectName" class="form-control" type="text" value="${item.projectName}"
																					onclick="selectTable(this, ${bill.customerId});">
																		</td>
																		<td>${item.billTypeLabel}</td>
																		<td>${item.poNo}</td>
																		<td>${item.commbarcode}</td>
																		<td>${item.goodsNo}</td>
																		<td>${item.goodsName}</td>
																		<td>${item.invoiceDescription}</td>
																		<td>${item.parentBrandName}</td>
																		<td>${item.num}</td>
																		<td>${item.taxRate}</td>
																		<td>${item.price}</td>
																		<td>${item.tax}</td>
																		<td>${item.taxAmount}</td>
																		<td>${item.remark}</td>
																	</tr>
																</c:forEach>

																</tbody>
															</table>
														</div>
														<div class="totalDiv" id="receiveCostItemTotal" style="display: none">
															总数量：${costTotalNum} &nbsp;&nbsp; 总金额：${costTotalPrice}
														</div>
													</div>

												</div>
		                                    </div>
		                                </div>
				           			</div>
									<div class="tab-pane fade  table-responsive" id="attachment" style="overflow-x: hidden;">
										<div class="form-row mt20 attachDetail">
											<div class='col-12'>
												<div class="info-box">
													<div class="title_text">附件列表</div>
													<button type="button" style="margin-top: 10px; margin-left: 20px;"
															class="btn btn-find waves-effect waves-light btn-sm btn_default"
															id="btn-file">上传附件
													</button>
													<form enctype="multipart/form-data" id="form-upload">
														<input type="file" name="file" id="file" class="btn-hidden file-import">
													</form>
													<div id="attachmentTable" ></div>
												</div>
											</div>
										</div>
										<div class="form-row mt20">
											<div class='col-12'>
												<div class="info-box">
													<div class="title_text">操作记录</div>
													<table class="table table-striped" cellspacing="0" width="100%">
														<thead>
														<tr>
															<th style="white-space:nowrap;">操作人</th>
															<th style="white-space:nowrap;">操作结果</th>
															<th style="white-space:nowrap;">备注</th>
															<th style="white-space:nowrap;">操作时间</th>
														</tr>
														</thead>
														<tbody>
														<c:if test="${receiveBillOperateModels.size() ==0}" >
															<tr><td colspan="9" align="center">表中数据为空</td></tr>
														</c:if>
														<c:forEach var="item" items="${receiveBillOperateModels}" varStatus="status">
															<tr>
																<td>${item.operator}</td>
																<td>${item.operateNodeLabel}</td>
																<td>${item.remark}</td>
																<td><fmt:formatDate value="${item.operateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
															</tr>
														</c:forEach>
														</tbody>
													</table>
												</div>
											</div>
										</div>
									</div>
									<div class="tab-pane fade  table-responsive" id="verify" style="overflow-x: hidden;">
										<div class="form-row mt20">
											<div class='col-12 info_box'>
												<div class="title_text">NC信息</div>
												<div class="info_item text_font ml15 mt10">
													<div class="info_text">
														<span>NC单据号：</span>
														<span>${bill.ncCode}</span>
													</div>
													<div class="info_text">
														<span>NC单据状态：</span>
														<span>${bill.ncStatusLabel}</span>
													</div>
													<div class="info_text">
														<span>NC同步时间：</span>
														<span><fmt:formatDate value="${bill.ncSynDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
													</div>
												</div>
												<div class="info_item text_font ml15">
													<div class="info_text">
														<span>NC同步人：</span>
														<span>${bill.synchronizer}</span>
													</div>
													<div class="info_text">
														<span>凭证编号：</span>
														<span>${bill.voucherCode}</span>
													</div>
													<div class="info_text">
														<span>凭证状态：</span>
														<span>${bill.voucherStatusLabel}</span>
													</div>
												</div>
												<div class="info_item text_font ml15">
													<div class="info_text">
														<span>凭证名称：</span>
														<span>${bill.voucherName}</span>
													</div>
													<div class="info_text">
														<span>凭证序号：</span>
														<span>${bill.voucherIndex}</span>
													</div>
													<div class="info_text">
														<span>会计期间：</span>
														<span>${bill.period}</span>
													</div>
												</div>
											</div>
											<div class='col-12'>
												<div class="title_text">核销记录</div>
												<table class="table table-striped" cellspacing="0" width="100%">
													<thead>
													<tr>
														<th style="white-space:nowrap;">核销单号</th>
														<th style="white-space:nowrap;">单据类型</th>
														<th style="white-space:nowrap;">收款日期</th>
														<th style="white-space:nowrap;">核销金额</th>
														<th style="white-space:nowrap;">收款流水单号</th>
														<th style="white-space:nowrap;">核销人</th>
														<th style="white-space:nowrap;">核销时间</th>
													</tr>
													</thead>
													<tbody>
													<c:if test="${verifyItems.size() ==0}" >
														<tr><td colspan="8" align="center">表中数据为空</td></tr>
													</c:if>
													<c:forEach var="item" items="${verifyItems}" varStatus="status">
														<tr>
															<td style="font: 13px bold">${item.advanceCode}</td>
															<td style="font: 13px bold">${item.typeLabel}</td>
															<td style="font: 13px bold">${item.receiveDate}</td>
															<td style="font: 13px bold">${item.price}</td>
															<td style="font: 13px bold">${item.receiceNo}</td>
															<td style="font: 13px bold">${item.verifier}</td>
															<td style="font: 13px bold">${item.verifyDate}</td>
														</tr>
													</c:forEach>
													</tbody>
												</table>
											</div>
										</div>
									</div>
				           		</div>
								<div class="form-row mt20">
									<div class="col-12 mt20">
										<div class="row">
											<div class="col-12">
												<span><i class="red">*</i>结算类型：${bill.settlementTypeLabel}</span>
											</div>
										</div>
									</div>
									<div class="col-12 mt20" id="creditDiv">
										<div class="row">
											<div class="col-12">
												<span><i class="red" id="creditI">*</i>入账日期：</span>
												<input type="text" class="input_msg date-input"
													   name="creditDate" id="creditDate">
											</div>
										</div>
									</div>
									<div class="col-12 mt20">
										<div class="row">
											<div class="col-12">
												<span><i class="red">*</i>审核结果：</span>
												<input type="radio" name="auditResult" value="00" checked>审批通过
												<input type="radio" name="auditResult" value="01">驳回
											</div>
										</div>
									</div>
									<div class="col-12 mt20">
										<div class="row">
											<div class="col-12">
												<span style="vertical-align: top;"><i class="red">*</i>审核备注：</span>
												<textarea id="auditRemark" rows="5" style="width: 500px;"></textarea>
											</div>
										</div>
									</div>
								</div>
                                <div class="form-row m-t-50">
		                          <div style="margin: 0 auto;">
									  <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="audit-buttons">审核</button>
									  <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="cancel-buttons">返回</button>
		                          </div>
		                      </div>
                            </div>
                        </div>
                    </div>
                    <!-- end row -->
                </div>
            </div>
    </div>
    <!-- container -->
	<jsp:include page="/WEB-INF/views/modals/13017.jsp"/>
</div>

<script type="text/javascript">
	var billStatus = '${bill.billStatus}';

	$(function(){
		//点击上传文件
		$("#btn-file").click(function () {
			var input = '<input type="file" name="file" id="file" class="btn-hidden file-import">';
			$("#form-upload").empty();
			$("#form-upload").append(input);
			$("#file").click();
		});
		var code = "${bill.code}"
		//上传文件到后端
		$("#form-upload").on("change", '.file-import', function () {
			var formData = new FormData($("#form-upload")[0]);
			$custom.request.ajaxUpload(serverAddr + "/attachment/uploadFiles.asyn?code=" + code, formData, function (result) {
				if (result.state == 200 && result.data && result.data.code == 200) {
					$attachTable.fn.reload();
					$custom.alert.success("上传成功");
				} else {
					$custom.alert.error("上传失败");
				}
			});
		});
		//取消按钮
		$("#cancel-buttons").click(function(){
			$module.load.pageOrder("act=14001");
		});

		$("#audit-buttons").click(function(){
			var id = "${bill.id}";
			var billStauts = '${bill.billStatus}';
			var auditResult = '00';
			$("input[name='auditResult']").each(function () {
				if ($(this).prop("checked")) {
					auditResult = $(this).val();
				}
			});
			var auditRemark = $("#auditRemark").val();
			var creditDate = $("#creditDate").val();

			if (!auditResult) {
				$custom.alert.error("请选择审批结果！");
				return;
			}
			if (!auditRemark) {
				$custom.alert.error("请输入审批备注！");
				return;
			}

			if (billStauts == '01' && auditResult == '00' && !creditDate) {
				$custom.alert.error("请选择入账日期！");
				return;
			}

			var check = true;
			var itemList = [];
			var costItemList = [];

			if ($("#receive-detail-td-table tbody tr").length == 1) {
				if ($("#receive-detail-td-table tbody tr")[0].children.length > 1) {
					$("#receive-detail-td-table tbody tr").each(function (index, item) {

						var itemId = $(this).find("td").eq(0).find("input[name=itemId]").val();
						var projectId = $(this).find("td").eq(2).find("input[name=projectId]").val();
						var projectName = $(this).find("td").eq(2).find("input[name=projectName]").val();
						if (!projectId) {
							$custom.alert.error("费用项目不能为空！");
							check = false;
							return false;
						}

						var goods = {};
						goods.id = itemId;
						goods.projectId = projectId;
						goods.projectName = projectName;
						itemList.push(goods);
					});
				}
			} else {
				$("#receive-detail-td-table tbody tr").each(function (index, item) {
					var itemId = $(this).find("td").eq(0).find("input[name=itemId]").val();
					var projectId = $(this).find("td").eq(2).find("input[name=projectId]").val();
					var projectName = $(this).find("td").eq(2).find("input[name=projectName]").val();
					if (!projectId) {
						$custom.alert.error("费用项目不能为空！");
						check = false;
						return false;
					}

					var goods = {};
					goods.id = itemId;
					goods.projectId = projectId;
					goods.projectName = projectName;
					itemList.push(goods);
				});
			}



			if (check) {

				if ($("#receive-cost-td-table tbody tr").length == 1) {
					if ($("#receive-cost-td-table tbody tr")[0].children.length > 1) {
						$("#receive-cost-td-table tbody tr").each(function (index, item) {
							var costId = $(this).find("td").eq(0).find("input[name=costId]").val();
							var projectId = $(this).find("td").eq(1).find("input[name=projectId]").val();
							var projectName = $(this).find("td").eq(1).find("input[name=projectName]").val();
							if (!projectId) {
								$custom.alert.error("费用项目不能为空！");
								check = false;
								return false;
							}

							var goods = {};
							goods.id = costId;
							goods.projectId = projectId;
							goods.projectName = projectName;
							costItemList.push(goods);
						});
					}
				} else {
					$("#receive-cost-td-table tbody tr").each(function (index, item) {

						var costId = $(this).find("td").eq(0).find("input[name=costId]").val();
						var projectId = $(this).find("td").eq(1).find("input[name=projectId]").val();
						var projectName = $(this).find("td").eq(1).find("input[name=projectName]").val();
						if (!projectId) {
							$custom.alert.error("费用项目不能为空！");
							check = false;
							return false;
						}

						var goods = {};
						goods.id = costId;
						goods.projectId = projectId;
						goods.projectName = projectName;
						costItemList.push(goods);
					});
				}

			}

			if (check) {
				auditBill(id, auditResult, auditRemark, creditDate, itemList, costItemList);
			}

			/*var validUrl = serverAddr+"/receiveBill/isExistRelInvoice.asyn";  ——本期删除该校验（2020.10.25）
			//当账单状态为作废待审时且账单已开票，若审核通过，则检查该发票是否关联多张应收账单，若关联多张应收账单，系统提示“发票{发票编号}关联的{账单号}”将一并作废，是否继续？
			if (billStauts == '05' && invoiceStatus == '01' && auditResult == '00') {
				$custom.request.ajax(validUrl,{"ids":id},function(result){
					if (result.state != '200' ) {
						$custom.alert.warningText(result.message);
					} else if (result.data.code == '01'){
						$custom.alert.warningText(result.data.message);
						return;
					} else if (result.data.code == '02'){
						var msg = result.data.msg;
						$custom.alert.warningBtnText(msg,"继续审核","重新选择", function(){
							auditBill(id, auditResult, auditRemark, settlementType)
						});
					} else {
						auditBill(id, auditResult, auditRemark, settlementType);
					}
				});
			} else {
				auditBill(id, auditResult, auditRemark, settlementType);
			}*/

		});

		$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
			var targetId = e.target.id;//获取事件触发元素id
			if (targetId == 'attachmentTab') {
				var code = "${bill.code}";
				var attachmentHtml = $('#attachmentTable').html();
				if (!attachmentHtml) {
					$("#attachmentTable").createAttachTables(code);
				}

			}
		});

		function auditBill(id, auditResult, auditRemark, creditDate, itemList, costItemList) {
			var json = {
				"billId": id,
				"auditResult": auditResult,
				"auditRemark": auditRemark,
				"creditDate": creditDate,
				"itemDTOS": itemList,
				"costItemDTOS": costItemList
			};
			var jsonStr = JSON.stringify(json);
			var url = serverAddr + "/receiveBill/auditItem.asyn";
			$custom.request.ajaxSync(url, {"json": jsonStr}, function (result) {
				if (result.state == '200' && result.data.code == '00') {
					$custom.alert.success("保存成功！");
					$module.load.pageOrder('act=14001');
				} else {
					var message = result.message;
					if (message != null && message != '' && message != undefined) {
						$custom.alert.error(message);
					} else {
						$custom.alert.error("保存失败！");
					}
				}
			});
		}

		var today = new Date();
		laydate.render({
			elem: '#creditDate',//指定元素
			max: getNowFormatDate()
		});

		if (billStatus == '05') {
			$("#creditDiv").hide();
		}

		// 点击事件change
		$('input[type=radio][name=auditResult]').change(function () {
			var auditResult = $(this).val();

			if (auditResult == '00') {
				$("#creditI").show();
			} else {
				$("#creditI").hide();
			}
		});
	});

	function showHide(obj, type) {
		$(obj).parent().toggle();
		//收起
		if (type == '1') {
			$(obj).html('查看更多');
			$(obj).parent().prev().show();
		}
		//展开
		if (type == '0') {
			$(obj).parent().next().show();
			$(obj).parent().hide()
		}
	}

    function getNowFormatDate() {
        var date = new Date();
        var seperator1 = "-";
        var seperator2 = ":";
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
            + " " + date.getHours() + seperator2 + date.getMinutes()
            + seperator2 + date.getSeconds();
        return currentdate;
    }

</script>
