<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
							                      <li class="breadcrumb-item"><a href="javascript:$module.load.pageReport('act=17003');">结算单列表</a></li>
							                      <li class="breadcrumb-item"><a href="javascript:;">结算单详情</a></li>
							            </ol>
                                    </div>
                                </div>
                                <ul class="nav nav-tabs" >
									<li class="nav-item">
										<a href="#total" data-toggle="tab" class="nav-link active" >汇总</a>
									</li>
		
									<li class="nav-item">
										<a href="#detail" data-toggle="tab" class="nav-link" >事业部分账</a>
									</li>
				           		</ul>
				           		<div class="tab-content">
				           			<div class="tab-pane fade show active table-responsive" id="total">
				           				<div class="title_text_new">${month}月费用结算单</div>
		                                <div class="info_box" style="float: right;margin-right: 20px;">
		                                	<font style="color:blue;">计费周期：${ detail.billDate}</font>
		                                </div>
				           				<div class="info_box" style="margin-top: 50px;">
					                        <div class="info_item">
					                            <div class="info_text">
													<span>结算对象：</span>
													<span>${detail.customerName}</span>
			                                    </div>
			                                    <div class="info_text">
			                                    </div>
			                                    <div class="info_text">
			                                    	<span>仓库名称：</span>
													<span>${detail.depotName}</span>
			                                    </div>
		                                	</div>
		                                	<div class="info_item">
					                            <div class="info_text">
					                            	<span>创建人：</span>
					                            	<span>${detail.createrName}</span>
					                            </div>
					                            <div class="info_text">
					                            </div>
					                            <div class="info_text">
						                            <span>创建时间：</span>
						                            <span><fmt:formatDate value="${detail.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
					                            </div>
		                                    </div>
		                                    
		                                    <table class="pure-table pure-table-bordered">
		                                    	<tbody>
		                                    		<tr>
		                                    			<td colspan="4">应收款项</td>
		                                    		</tr>
		                                    		<tr>
		                                    			<td colspan="2">项目</td>
		                                    			<td >金额</td>
		                                    			<td >币种</td>
		                                    		</tr>
		                                    		<tr>
		                                    			<td rowspan="3">快递服务费</td>
		                                    			<td>快递费</td>
		                                    			<td>${totalMap.kdFree }</td>
		                                    			<td>${detail.currency }</td>
		                                    		</tr>
		                                    		<tr>
		                                    			<td>包装费</td>
		                                    			<td>${totalMap.packagingCost }</td>
		                                    			<td>${detail.currency }</td>
		                                    		</tr>
		                                    		<tr>
		                                    			<td>超件/重附加费</td>
		                                    			<td>${totalMap.attachCost + totalMap.overweightCost}</td>
		                                    			<td>${detail.currency}</td>
		                                    		</tr>
		                                    		<tr>
		                                    			<td rowspan="2">其他费用</td>
		                                    			<td>取消订单服务费</td>
		                                    			<td>${totalMap.qxFree }</td>
		                                    			<td>${detail.currency }</td>
		                                    		</tr>
		                                    		<tr>
		                                    			<td>快递理赔费</td>
		                                    			<td>${totalMap.lpFree }</td>
		                                    			<td>${detail.currency }</td>
		                                    		</tr>
		                                    		<tr>
		                                    			<td>税费</td>
		                                    			<td>综合税金</td>
		                                    			<td>${totalMap.zhFree }</td>
		                                    			<td>${detail.currency }</td>
		                                    		</tr>
													<tr>
														<td>堆存费</td>
														<td>堆存费</td>
														<td>${totalMap.dcFree }</td>
														<td>${detail.currency }</td>
													</tr>
													<tr>
														<td rowspan="2">入仓费用</td>
														<td>一线进口清关费</td>
														<td>${totalMap.clearanceCost }</td>
														<td>${detail.currency }</td>
													</tr>
													<tr>
														<td>入库验收费</td>
														<td>${totalMap.acceptanceCost }</td>
														<td>${detail.currency }</td>
													</tr>
													<tr>
														<td rowspan="3">出仓费用</td>
														<td>转关费</td>
														<td>${totalMap.zgFree }</td>
														<td>${detail.currency }</td>
													</tr>
													<tr>
														<td>调拨费</td>
														<td>${totalMap.dbFree}</td>
														<td>${detail.currency }</td>
													</tr>
													<tr>
														<td>退运费</td>
														<td>${totalMap.tyFree }</td>
														<td>${detail.currency}</td>
													</tr>
		                                    		<tr>
		                                    			<td colspan="2">未分配金额</td>
		                                    			<td>${totalMap.unSplitTotal }</td>
		                                    			<td>${detail.currency }</td>
		                                    		</tr>
		                                    		<tr>
		                                    			<td colspan="2">合计总额：${totalMap.totalLabel }</td>
		                                    			<td>${totalMap.total }</td>
		                                    			<td>${detail.currency }</td>
		                                    		</tr>
		                                    	</tbody>
		                                    </table>
		                                </div>
				           			</div>
				           			<div class="tab-pane fade  table-responsive" id="detail">
				           				<div class="title_text_new">${month}月费用结算单</div>
		                                <div class="info_box" style="float: right;margin-right: 20px;">
		                                	<font style="color:blue;">计费周期：${ detail.billDate}</font>
		                                </div>
				           				<div class="info_box" style="margin-top: 50px;">
					                        <div class="info_item">
					                            <div class="info_text">
													<span>结算对象：</span>
													<span>${detail.customerName}</span>
			                                    </div>
			                                    <div class="info_text">
			                                    </div>
			                                    <div class="info_text">
			                                    	<span>仓库名称：</span>
													<span>${detail.depotName}</span>
			                                    </div>
		                                	</div>
		                                	<div class="info_item">
					                            <div class="info_text">
					                            	<span>创建人：</span>
					                            	<span>${detail.createrName}</span>
					                            </div>
					                            <div class="info_text">
					                            </div>
					                            <div class="info_text">
						                            <span>创建时间：</span>
						                            <span>${detail.createDate}</span>
					                            </div>
		                                    </div>
		                                    
		                                    <div style="height:500px;overflow-y:auto">
		                                    	<c:forEach items="${list}" var="item" >  
			                                    <table class="pure-table pure-table-bordered">
			                                    	<tbody>
			                                    		<tr>
			                                    			<td colspan="4">应收款项-${item.buName }</td>
			                                    		</tr>
			                                    		<tr>
			                                    			<td colspan="2">项目</td>
			                                    			<td >金额</td>
			                                    			<td >币种</td>
			                                    		</tr>
			                                    		<tr>
			                                    			<td rowspan="3">快递服务费</td>
			                                    			<td>快递费</td>
			                                    			<td>${item.kdFree }</td>
			                                    			<td>${detail.currency }</td>
			                                    		</tr>
			                                    		<tr>
			                                    			<td>包装费</td>
			                                    			<td>${item.packagingCost}</td>
			                                    			<td>${detail.currency }</td>
			                                    		</tr>
			                                    		<tr>
			                                    			<td>超件/重附加费</td>
			                                    			<td>${item.attachCost + item.overweightCost}</td>
			                                    			<td>${detail.currency}</td>
			                                    		</tr>
			                                    		<tr>
			                                    			<td rowspan="2">其他费用</td>
			                                    			<td>取消订单服务费</td>
			                                    			<td>${item.qxFree }</td>
			                                    			<td>${detail.currency }</td>
			                                    		</tr>
			                                    		<tr>
			                                    			<td>快递理赔费</td>
			                                    			<td>${item.lpFree }</td>
			                                    			<td>${detail.currency }</td>
			                                    		</tr>
			                                    		<tr>
			                                    			<td>税费</td>
			                                    			<td>综合税金</td>
			                                    			<td>${item.zhFree }</td>
			                                    			<td>${detail.currency }</td>
			                                    		</tr>
														<tr>
															<td>堆存费</td>
															<td>堆存费</td>
															<td>${item.dcFree }</td>
															<td>${detail.currency }</td>
														</tr>
														<tr>
															<td rowspan="2">入仓费用</td>
															<td>一线进口清关费</td>
															<td>${item.clearanceCost }</td>
															<td>${detail.currency }</td>
														</tr>
														<tr>
															<td>入库验收费</td>
															<td>${item.acceptanceCost }</td>
															<td>${detail.currency }</td>
														</tr>
														<tr>
															<td rowspan="3">出仓费用</td>
															<td>转关费</td>
															<td>${item.zgFree }</td>
															<td>${detail.currency }</td>
														</tr>
														<tr>
															<td>调拨费</td>
															<td>${item.dbFree}</td>
															<td>${detail.currency }</td>
														</tr>
														<tr>
															<td>退运费</td>
															<td>${item.tyFree }</td>
															<td>${detail.currency}</td>
														</tr>
			                                    		<tr>
			                                    			<td colspan="2">合计总额：${item.totalLabel }</td>
			                                    			<td>${item.total }</td>
			                                    			<td>${detail.currency }</td>
			                                    		</tr>
			                                    	</tbody>
			                                    </table>
			                                    </c:forEach>
		                                    </div>
		                                </div>
				           			</div>
				           		</div>
				                <!--   价格变更记录  end   -->
                                <div class="form-row m-t-50">
		                          <div class="col-12">
		                              <div class="row">
		                                  <div class="col-5">
		                                      <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="cancel-buttons">返回</button>
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
    </div>
    <!-- container -->
</div>

<script type="text/javascript">
	$(function(){
		//取消按钮
		$("#cancel-buttons").click(function(){
			$module.load.pageReport("act=17003");
		});
	});
</script>
