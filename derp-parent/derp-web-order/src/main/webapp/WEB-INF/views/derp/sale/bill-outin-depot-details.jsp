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
                        <li class="breadcrumb-item"><a href="#">账单出库单详情</a></li>
                    </ol>
                </div>
                <div class="card-box table-responsive">
                    <div class="title_text">账单出库单详情</div>
                    <div class="info_box">
                        <div class="info_item">
                            <div class="info_text">
                                <span>账单出库单号：</span>
                                <span>
                                    ${detail.code}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>出仓仓库：</span>
                                <span>
                                    ${detail.depotName}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>平台账单号：</span>
                                <span>
                                	${detail.billCode}
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>客户：</span>
                                <span>
                                    ${detail.customerName}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>处理类型：</span>
                                <span>
                                    ${detail.processingTypeLabel}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>账单出库日期：</span>
                                <span>
                                    <fmt:formatDate value="${detail.deliverDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>调整类型：</span>
                                <span>
                                   ${detail.operateTypeLabel}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>单据状态：</span>
                                <span>
                                	${detail.stateLabel}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>结算币种：</span>
                                <span>
                                    ${detail.currencyLabel}
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
	                        	<span>事业部：</span>
	                            <span>
	                                ${detail.buName}
	                            </span>
	                        </div>
	                        <div class="info_text">
	                            <span>账单来源：</span>
	                            <span>
	                                ${detail.billSourceLabel}
	                            </span>
	                        </div>
	                        <div class="info_text">
	                        	<span>创建人：</span>
	                            <span>
	                                ${detail.creater}
	                            </span>
	                        </div>
	                        
	                    </div>
	                    <div class="info_item">
                            <div class="info_text">
	                        	<span>创建时间：</span>
	                            <span>
	                                <fmt:formatDate value="${detail.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
	                            </span>
	                        </div>
	                   </div>
                    </div>
                    <div class="title_text">出库商品信息</div>
                    <form id="form1">
						<input type="hidden" name='outinDepotId' id='outinDepotId' value='${detail.id}' />
					</form>
                    <!--   商品信息  start -->
                    <ul class="nav nav-tabs">
						<li class="nav-item"><a href="#itemList" data-toggle="tab"
							class="nav-link active">商品列表</a></li>
						<li class="nav-item"><a href="#batchDefault"
							data-toggle="tab" class="nav-link">批次明细</a></li>
					</ul>
					<div class="tab-content">
	                    <div class="tab-pane fade show active table-responsive" id="itemList">
	                        <table id="datatable-items" class="table table-striped" width="100%">
	                            <thead>
		                            <tr>
		                                <th>商品货号</th>
		                                <th>平台SKU条码</th>
		                                <th>标准条码</th>
		                                <th>商品名称</th>
		                                <th>PO号</th>
		                                <th>数量</th>
		                                <th>结算单价</th>
		                                <th>结算金额</th>
		                            </tr>
	                            </thead>
	                            <tbody>
	                            </tbody>
	                        </table>
	                    </div>
	                    <div class="tab-pane fade table-responsive" id="batchDefault">
	                        <table id="datatable-batchs" class="table table-striped" width="100%">
	                            <thead>
		                            <tr>
		                                <th>标准条码</th>
		                                <th>商品名称</th>
		                                <th>商品货号</th>
		                                <th>数量</th>
		                                <th>批次号</th>
		                                <th>生产日期</th>
		                                <th>失效日期</th>
		                            </tr>
	                            </thead>
	                            <tbody>
	                            </tbody>
	                        </table>
	                    </div>
                    </div>
                 <!--   商品上架明细  end -->
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
           	$module.load.pageOrder('act=4011');
        });
        
        initDataTabel() ;

    });
    
    //tab切换时，初始化对应表格
    $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
    	  console.log(e.target) ;
    	  var activeTab = $(e.target).text(); 
    	  
    	  initDataTabel(activeTab) ;
    }) ;
    
  	function initDataTabel(activeTab){
  		//初始化
  	    $mytable.fn.init();
  		
  		var tableUrl = serverAddr+'/billOutinDepot/listBillOutinDepotItem.asyn?r='+Math.random() ;
  		var tableColumns = [
  	            {data:'goodsNo'},{data:'platformSku'},{data:'commbarcode'},
  	            {data:'goodsName'},{data:'poNo'},{data:'num'},{data:'price'},{data:'amount'}
  	        ] ;
  		var tableId = "datatable-items" ;
  		
  		if(activeTab == '商品信息'){
  			tableUrl = serverAddr+'/billOutinDepot/listBillOutinDepotItem.asyn?r='+Math.random() ;
  	  		tableColumns = [
  	  	            {data:'goodsNo'},{data:'platformSku'},{data:'commbarcode'},
  	  	            {data:'goodsName'},{data:'poNo'},{data:'platformSaleCode'},
  	  	            {data:'num'},{data:'price'},{data:'amount'}
  	  	        ] ;
  	  		tableId = "datatable-items" ;
  		}else if(activeTab == '批次明细'){
  			tableUrl = serverAddr+'/billOutinDepot/listBillOutinDepotBatch.asyn?r='+Math.random() ;
  	  		tableColumns = [
  	  	            {data:'commbarcode'},{data:'goodsName'},{data:'goodsNo'},{data:'num'},{data:'batchNo'},
  	  	            {data:'productionDate'},{data:'overdueDate'}
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
  	    	
  	    };
  	    //生成列表信息
  	    $('#' + tableId).mydatatables(); 
  	}
</script>
