<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common.jsp"/>
<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
        <div class="row">
            <div class="col-12 ml10">
                <div class="col-12 row">
                    <ol class="breadcrumb">
                        <li><i class="block"></i> 当前位置：</li>
                        <li class="breadcrumb-item"><a href="#">销售出库详情</a></li>
                    </ol>
                </div>
                <div class="card-box table-responsive">
                    <div class="title_text">基本信息</div>
                <%--    <div class="form-row mt20 ml15">
                        <div class="col-md-4">
                            <div class="row">
                                <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                        class="fr">单据状态 <span>：</span></span></label>
                                <div class="ml10"><span class="text-success">
                            		<c:if test="${detail.status == '017'}">待出库</c:if>
                            		<c:if test="${detail.status == '018'}">已出库</c:if>
                            </span></div>
                            </div>
                        </div>
                    </div>
                    <div class="form-row ml15">
                        <div class="col-md-4">
                            <div class="row">
                                <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">出库清单编号 <span>：</span></span></label>
                                <div class="col-7 ml10 line">
                                    <div>${detail.code}</div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="row">
                                <label class="col-4 col-form-label" style="white-space:nowrap;"><span
                                        class="fr">销售订单编号<span>：</span></span></label>
                                <div class="col-7 ml10 line">
                                    <div>${detail.saleOrderCode}</div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="row">
                                <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">物流企业名称 <span>：</span></span></label>
                                <div class="col-7 ml10 line">
                                    <div>${detail.logisticsName}</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-row ml15">
                        <div class="col-md-4">
                            <div class="row">
                                <label class="col-4 col-form-label" style="white-space:nowrap;"><span
                                        class="fr">公司<span>：</span></span></label>
                                <div class="col-7 ml10 line">
                                    <div>${detail.merchantName}</div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="row">
                                <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">唯品账单号 <span>：</span></span></label>
                                <div class="col-7 ml10 line">
                                    <div>${detail.vipsBillNo}</div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="row">
                                <label class="col-4 col-form-label" style="white-space:nowrap;"><span
                                        class="fr">发货时间<span>：</span></span></label>
                                <div class="col-7 ml10 line">
                                    <div><fmt:formatDate value="${detail.deliverDate}"
                                                         pattern="yyyy-MM-dd HH:mm:ss"/></div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-row ml15">
                        <div class="col-md-4">
                            <div class="row">
                                <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                        class="fr">PO单号 <span>：</span></span></label>
                                <div class="col-7 ml10 line">
                                    <div>${detail.poNo}</div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="row">
                                <label class="col-4 col-form-label" style="white-space:nowrap;"><span
                                        class="fr">LBX单号<span>：</span></span></label>
                                <div class="col-7 ml10 line">
                                    <div>${detail.lbxNo}</div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="row">
                                <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                        class="fr">提单号 <span>：</span></span></label>
                                <div class="col-7 ml10 line">
                                    <div>${detail.blNo}</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-row ml15">
                        <div class="col-md-4">
                            <div class="row">
                                <label class="col-4 col-form-label" style="white-space:nowrap;"><span
                                        class="fr">出库仓库<span>：</span></span></label>
                                <div class="col-7 ml10 line">
                                    <div>${detail.outDepotName}</div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="row">
                                <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                        class="fr">进口类型<span>：</span></span></label>
                                <div class="col-7 ml10 line">
                                    <div>
                                        <c:if test="${detail.importMode =='10'}">BBC</c:if>
                                        <c:if test="${detail.importMode =='20'}">BC</c:if>
                                        <c:if test="${detail.importMode =='30'}">保留</c:if>
                                        <c:if test="${detail.importMode =='40'}">CC</c:if>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="row">
                                <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                        class="fr">运单号<span>：</span></span></label>
                                <div class="col-7 ml10 line">
                                    <div>${detail.wayBillNo}</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-row ml15">
                        <div class="col-md-4">
                            <div class="row">
                                <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                        class="fr">单据类型<span>：</span></span></label>
                                <div class="col-7 ml10 line">
                                    <div>
                                        <c:if test="${detail.saleType == '1'}">购销</c:if>
                                        <c:if test="${detail.saleType == '2'}">代销</c:if>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>--%>
                    <div class="info_box">
                        <div class="info_item">
                            <div class="info_text">
                                <span>单据状态：</span>
                                <span>
                                	${detail.statusLabel}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>出库清单编号：</span>
                                <span>
                                    ${detail.code}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>销售订单编号：</span>
                                <span>
                                    ${detail.saleOrderCode}
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>物流企业名称：</span>
                                <span>
                                    ${detail.logisticsName}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>公司：</span>
                                <span>
                                    ${detail.merchantName}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>账单号：</span>
                                <span>
                                    ${detail.vipsBillNo}
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>发货时间：</span>
                                <span>
                                   <fmt:formatDate value="${detail.deliverDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </span>
                            </div>
                            <div class="info_text">
                                <span>PO单号：
                                <textarea readonly="readonly" style="width: 100%;overflow: auto;word-break: break-all;">${detail.poNo}</textarea>
                                </span>
                            </div>
                            <div class="info_text">
                                <span>LBX单号：</span>
                                <span>
                                    ${detail.lbxNo}
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                        <div class="info_text">
                            <span>提单号：</span>
                            <span>
                                ${detail.blNo}
                            </span>
                        </div>
                        <div class="info_text">
                            <span>出库仓库：</span>
                            <span>
                                ${detail.outDepotName}
                            </span>
                        </div>
                        <div class="info_text">
                            <span>进口类型：</span>
                            <span>
                            		${detail.importModeLabel}
                            </span>
                        </div>
                    </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>运单号：</span>
                                <span>
                                    ${detail.wayBillNo}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>签收时间：</span>
                                <span>
                                	<fmt:formatDate value="${detail.receiveDate}" pattern="yyyy-MM-dd"/>
                                </span>
                            </div>
                            <div class="info_text">
 								<span>最后上架时间：</span>
                                <span>
                                	<fmt:formatDate value="${detail.shelfDate}" pattern="yyyy-MM-dd"/>
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>单据类型：</span>
                                <span>
                                		${detail.saleTypeLabel}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>备注：</span>
                                <span>
                                    ${detail.remark}
                                </span>
                            </div>
                            <div class="info_text">
								<span>PO单时间：</span>
                                <span>
                                    <fmt:formatDate value="${detail.poDate}" pattern="yyyy-MM-dd HH:mm:ss" />
                                </span>
                            </div>
                        </div>
                         <div class="info_item">
                            <div class="info_text">
                                <span>币种：</span>
                                <span>
                                	${detail.currencyLabel}
                                </span>
                            </div>
                            <div class="info_text">
                            	 <span>事业部：</span>
                                <span>
                                	${detail.buName}
                                </span>
                            </div>
                            <div class="info_text">
                            </div>
                        </div>
                    </div>
                    
                           <ul class="nav nav-tabs">
                               <li class="nav-item">
                                   <a href="#itemList"  data-toggle="tab" class="nav-link active">出库商品明细</a>
                               </li>
                                <form id="form1">
					                  	<input type="hidden" name='orderId' id='orderId' value='${detail.saleOrderId}'/>
					           </form>
                                <li class="nav-item">
                                <a href="#batchDefault" data-toggle="tab" class="nav-link">商品上架明细</a>
                            	</li>
                           </ul>
                                <div class="tab-content">
                                   <div class="tab-pane fade show active table-responsive" id="itemList">
                                            <table class="table table-striped" cellspacing="0" width="100%">
                                                <thead>
                                                <tr>
                                                        <th>序号</th>
						                                <th>商品货号</th>
						                                <th>商品编号</th>
						                                <th>商品名称</th>
						                                <th>商品条形码</th>
						                                <th>数量</th>
						                                <th>好品数量</th>
						                                <th>坏品数量</th>
						                                <th>批次号</th>
						                                <th>海外仓理货单位</th>
						                                <th>标准条码</th>
						                                <th>账单类型</th>
						                                <th>销售单价</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach var="item" items="${detail.itemList}" varStatus="status">
                                                    <tr>
                                                     <td>${status.index+1}</td>
				                                    <td>${item.goodsNo}</td>
				                                    <td>${item.goodsCode}</td>
				                                    <td>${item.goodsName}</td>
				                                    <td>${item.barcode}</td>
				                                    <td>${item.transferNum+item.wornNum}</td>
				                                    <td>${item.transferNum}</td>
				                                    <td>${item.wornNum}</td>
				                                    <td>${item.transferBatchNo }</td>
				                                    <td>${item.tallyingUnitLabel}</td>
				                                    <td>${item.commbarcode}</td>
				                                    <td>${item.billTypeLabel}</td>
				                                    <td>${item.price}</td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                        <div class="tab-pane fade table-responsive" id="batchDefault">
                                            <table class="table table-striped"  width="100%" id="datatable-batchs">
                                                <thead>
                                                <tr>
						                           <th>商品货号</th>
						                           <th>商品名称</th>
						                           <th>条码</th>
						                           <th>海外仓理货单位</th>
						                           <th>销售数量</th>
						                           <th>本次上架数量</th>
						                           <th>本次残损数量</th>
						                           <th>本次少货数量</th>
						                           <th>本次上架时间</th>
						                           <th>po单号</th>
						                           <th>备注</th>
						                           <th>操作人</th>
						                           <th>操作时间</th>
                                                </tr>
                                                </thead>
                                                <tbody></tbody>
                                            </table>
                                    </div>
                                </div>
                    <div class="form-row m-t-50">
                        <div class="col-12">
                            <div class="row">
                                <div class="col-5">
                                    <button type="button" class="btn btn-find waves-effect waves-light"
                                            id="cancel-buttons">返回
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- end row -->
        </div>
        <!-- container -->
    </div>

</div>
<!-- content -->
<script type="text/javascript">
    $(document).ready(function () {
        //取消按钮
        $("#cancel-buttons").click(function(){
            var pageSource = '${pageSource}';
            if (pageSource == "1") {
                $load.a("/list/menu.asyn?act=100&r="+Math.random());
            } else {
                $module.load.pageOrder('act=4003');
            }
        });

		//生成列表信息
		$('#table-shelf-list').mydatatables();
    });
    //tab切换时，初始化对应表格
    $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
    	  var activeTab = $(e.target).text(); 
    	  initDataTabel(activeTab) ;
    }) ;
	function initDataTabel(activeTab){
		//初始化
	    $mytable.fn.init();

		 if(activeTab == '商品上架明细'){
				tableUrl = serverAddr+'/saleOut/listSaleOutShelf.asyn?r='+Math.random() ;
		  		tableColumns = [
		  		          	{data : 'goodsNo'},{data : 'goodsName'},{data : 'barcode'},{data : 'tallyingUnit'},
							{data : 'num'},{data : 'shelfNum'},{data : 'damagedNum'},{data : 'lackNum'},{data : 'shelfDate'},{data : 'poNo'},{data : 'remark'},{data : 'operator'}
							,{data : 'createDate'}
		  	        ] ;
		  		tableId = "datatable-batchs" ;
			}
		
	    //配置表格参数
	    $mytable.params={
	        url:tableUrl,
	        columns:tableColumns,
	        formid:'#form1'
	    };
	    
		//每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
        	 if (aData.tallyingUnit == '00'){
                 $('td:eq(4)', nRow).html('托盘');
             }else if (aData.tallyingUnit == '01'){
                $('td:eq(4)', nRow).html('箱');
            }else if (aData.tallyingUnit == '02'){
                $('td:eq(4)', nRow).html('件');
            }
        };
	    //生成列表信息
	    $('#' + tableId).mydatatables(); 
	}
    
</script>
