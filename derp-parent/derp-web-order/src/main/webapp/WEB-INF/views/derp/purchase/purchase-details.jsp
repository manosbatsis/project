<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common.jsp"/>
<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
        <div class="row">
            <div class="col-12">
                <div class="card-box table-responsive">
                    <!--  title   start  -->
                    <div class="col-12">
                        <div>
                            <div class="col-12 row">
                                <div>
                                    <ol class="breadcrumb">
                                        <li><i class="block"></i> 当前位置：</li>
                                        <li class="breadcrumb-item"><a href="#">采购档案</a></li>
                                        <li class="breadcrumb-item"><a href="javascript:dh_list();">采购订单列表</a></li>
                                        <li class="breadcrumb-item"><a href="javascript:dh_details(${detail.id });">采购订单详情</a>
                                        </li>
                                    </ol>
                                </div>
                            </div>
                            <!--  title   end  -->
                            <!--  title   基本信息   start -->

                            <form id="add-form">
                                <div class="info_box">
	                            	<div class="title_text">基本信息</div>
                                    <div class="info_item mt20">
                                        <div class="info_text">
                                            <span>采购订单编号：</span>
                                            <span>${detail.code }</span>
                                        </div>
                                        <div class="info_text">
                                            <span>订单状态：</span>
                                            <span>
                                                ${detail.statusLabel }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                        </div>
                                    </div>
                                    <div class="info_item">
                                    	<div class="info_text">
                                            <span>供应商：</span>
                                            <span>${detail.supplierName }</span>
                                        </div>
                                        <div class="info_text">
                                            <span>付款主体：</span>
                                            <span>
			                                   <c:choose>
			                                       <c:when test="${detail.paySubject eq '2' }">
			                                           ${merchantName }
			                                       </c:when>
			                                       <c:otherwise>
			                                            ${detail.paySubjectLabel}
			                                       </c:otherwise>
			                                   </c:choose>
			                                </span>
                                        </div>
                                        <div class="info_text">
                                            <span>业务模式：</span>
                                            <span>
                                                ${detail.businessModelLabel }
                                            </span>
                                        </div>
                                    </div>
                                    <div class="info_item">
                                        <div class="info_text">
                                            <span>入仓仓库：</span>
                                            <span>
                                                ${detail.depotName }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>事业部：</span>
                                            <span>
                                                ${detail.buName }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>PO号：</span>
                                            <span>
                                                ${detail.poNo }
                                            </span>
                                        </div>
                                    </div>
                                    <div class="info_item">
                                    	<div class="info_text">
                                            <span>采购币种：</span>
                                            <span>
                                                ${detail.currencyLabel }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                        	<span>归属日期：</span>
                                            <span>
                                                <fmt:formatDate value="${detail.attributionDate }" pattern="yyyy-MM-dd"/>
                                            </span>
                                        </div>
                                        <div class="info_text">
                                        	<span>海外仓理货单位：</span>
	                                        <span>
	                                            ${detail.tallyingUnitLabel }
	                                        </span>
                                        </div>
                                    </div>
                                    <div class="info_item">
                                    	<div class="info_text">
                                            <span>LBX单号：</span>
                                            <span>
                                                ${detail.lbxNo }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>入库关区：</span>
                                            <span>
                                                ${detail.inPlatformCustoms }
                                            </span>
                                        </div>
                                    	<div class="info_text">
                                    		<div class="info_text">
	                                            <span>汇率：</span>
	                                            <span>
	                                                ${detail.rate }
	                                            </span>
	                                        </div>
                                        </div>
                                    </div>
                                    <div class="info_item">
                                    	<div class="info_text">
                                            <span>创建人：</span>
                                            <span>
                                                ${detail.createName }
                                            </span>
                                        </div>
                                         <div class="info_text">
                                            <span>创建时间：</span>
                                            <span>
                                                ${createDate }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                        </div>
                                    </div>
                                    <div class="info_item">
                                        <div class="info_text">
                                            <span>提交人：</span>
                                            <span>
                                                ${detail.submiterName }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>提交时间：</span>
                                            <span>
                                                ${submitDate }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                        </div>
                                    </div>
                                    <div class="info_item">
                                    	<div class="info_text">
                                            <span>审核人：</span>
                                            <span>
                                                ${detail.auditName }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>审核时间：</span>
                                            <span>
                                                ${auditDate }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                        </div>
                                    </div>
                                    <div class="title_text">物流信息</div>
                                    <div class="info_item mt20">
                                    	<div class="info_text">
                                        	<span>运输方式：</span>
                                            <span>
                                                ${detail.transportLabel }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                        	<span>标准箱TEU：</span>
                                            <span>
                                                ${detail.standardCaseTeu }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                        	<span>车次：</span>
                                            <span>
                                                ${detail.trainNumber }
                                            </span>
                                        </div>
                                    </div>
                                    <div class="info_item">
                                    	<div class="info_text">
                                            <span>报关合同号：</span>
                                            <span>
                                                ${detail.contractNo }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>装货港：</span>
                                            <span>
                                                ${detail.loadPort }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>卸货港：</span>
                                            <span>
                                            	<c:choose>
	                                                <c:when test="${detail.unloadPort eq '44011501' }">44011501：南沙新港口岸</c:when>
	                                            	<c:when test="${detail.unloadPort eq '44010318' }">44010318：黄埔广保通码头口岸</c:when>
	                                            	<c:when test="${detail.unloadPort eq '21021001' }">21021001：大连保税区口岸</c:when>
	                                            	<c:when test="${detail.unloadPort eq '50010001' }">50010001：重庆口岸</c:when>
	                                            </c:choose>
                                            </span>
                                        </div>
                                    </div>
                                    <div class="info_item">
                                    	<div class="info_text">
                                    		<span>承运商：</span>
                                            <span>
                                                ${detail.carrier }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>提单号：</span>
                                            <span>
                                                ${detail.ladingBill }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>提单毛重KG：</span>
                                            <span>
                                                ${detail.grossWeight }
                                            </span>
                                        </div>
                                    </div>
                                    <div class="info_item">
                                    	<div class="info_text">
                                            <span>托数：</span>
                                            <span>
                                                ${detail.torrNum }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>箱数：</span>
                                            <span>
                                                ${detail.boxNum }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>预计到港时间：</span>
                                            <span>
                                                ${arriveDate }
                                            </span>
                                        </div>
                                    </div>
                                    <div class="info_item">
                                    	<div class="info_text">
                                        	<span>目的港名称：</span>
                                            <span>
                                                ${detail.destinationPortName }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                        	<span>包装方式：</span>
                                            <span>
                                                ${detail.packType }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>付款条约：</span>
                                            <span>
                                                ${detail.payRules }
                                            </span>
                                        </div>
                                    </div>
                                    <div class="info_item">
                                    	<div class="info_text">
                                        	<span>二程提单号：</span>
                                            <span>
                                                ${detail.blNo }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>进出口口岸：</span>
                                            <span>
                                                ${detail.imExpPort }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                        	<span>境外发货人：</span>
                                            <span>
                                                ${detail.shipper }
                                            </span>
                                        </div>
                                    </div>
                                    <div class="info_item">
                                    	<div class="info_text">
                                        	<span>唛头：</span>
                                            <span>
                                                ${detail.mark }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>目的地名称：</span>
                                			<span>${detail.destinationName }</span>
                                        </div>
                                        <div class="info_text">
                                            <span>发票号：</span>
                                            <span>
                                                ${detail.invoiceNo }
                                            </span>
                                        </div>
                                    </div>
                                    <div class="info_item">
                                    	<div class="info_text">
                                            <span>预计入仓时间：</span>
                                            <span>
                                                ${arriveDepotDate }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>预计付款时间：</span>
                                            <span>
                                                ${paymentDate }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>装船时间：</span>
                                            <span>
                                                ${shipDate }
                                            </span>
                                        </div>
                                    </div>
                                    <div class="info_item">
                                    	<div class="info_text">
                                            <span>收货地点：</span>
                                            <span>
                                                ${detail.deliveryAddr }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                        	<span>销售渠道：</span>
                                            <span>
                                                ${detail.channel }
                                            </span>
                                        </div>
                                        <div class="info_text">
	                                        <span>融资申请号：</span>
	                                            <span>
	                                                ${detail.financingNo }
	                                            </span>
	                                    </div>
                                    </div>
                                    <div class="info_item">
                                        <div class="info_text">
                                            <span>在途起始日期：</span>
                                            <span>
                                            	<fmt:formatDate value="${detail.cargoCuttingDate }" pattern="yyyy-MM-dd"/>
                                            </span>
                                        </div>
                                    </div>
                                    <div class="info_item">
                                    	<div class="info_text">
                                            <span>备注：</span>
                                            <span>
                                                ${detail.remark }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                        </div>
                                        <div class="info_text">
                                        </div>
                                    </div>
                                    <div class="title_text">发票信息</div>
                                    <div class="info_item mt20">
                                        <div class="info_text">
                                            <span>开票人：</span>
                                            <span>
                                                ${detail.invoiceName }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>付款人：</span>
                                            <span>
                                                ${detail.payName }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>收到发票日期：</span>
                                            <span>
                                                <fmt:formatDate value="${detail.invoiceDate}" pattern="yyyy-MM-dd"/>
                                            </span>
                                        </div>
                                    </div>
                                    <div class="info_item">
                                        <div class="info_text">
                                            <span>付款日期：</span>
                                            <span>
                                                <fmt:formatDate value="${detail.payDate}" pattern="yyyy-MM-dd"/>
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>开发票日期：</span>
                                            <span>
                                            <fmt:formatDate value="${detail.drawInvoiceDate }" pattern="yyyy-MM-dd"/>
                                            </span>
                                        </div>
                                        <div class="info_text">                                     	
	                                        	<span>单据状态：</span>
	                                            <span>
	                                               ${detail.billStatusLabel }
	                                            </span>	                                        
                                        </div>
                                    </div>
                                    <div class="info_item">
                                        <div class="info_text">
                                            <span>发送邮件状态：</span>
                                            <span>
                                                ${detail.mailStatusLabel }
                                            </span>
                                        </div>
                                        <div class="info_text"></div>
                                        <div class="info_text"></div>
                                    </div>
                                </div>
                                <!--  title   基本信息  start -->
                            </form>
                            <!--   商品信息  start -->
                            <div class="title_text">采购商品明细</div>
                            <div class="form-row mt20">
                                <table id="table-list" class="table table-striped table-responsive" cellspacing="0"
                                       width="100%">
                                    <thead>
                                    <tr>
                                        <th>子合同号</th>
                                        <th width="20%" style="min-width: 220px">商品名称</th>
                                        <th width="8%">商品条码</th>
                                        <th>商品货号</th>
                                        <th>采购数量</th>
                                        <th>采购单价（不含税）</th>
                         				<th>采购总金额（不含税）</th>
                         				<th>采购总金额（含税）</th>
                         				<th>税率</th>
                         				<th>税额</th>
                                        <th>品牌类型</th>
                                        <th>毛重(KG)</th>
                                        <th>净重(KG)</th>
                                        <th>箱号</th>
                                        <th>生产批次号</th>
                                        <th style="min-width: 200px">成分占比</th>
                                        <th style="min-width: 200px">备注</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${detail.itemList }" var="item">
                                        <tr>
                                            <td>${item.contractNo }</td>
                                            <td>${item.goodsName }</td>
                                            <td>${item.barcode }</td>
                                            <td>${item.goodsNo }</td>
                                            <td class="num">${item.num }</td>
                                            <td><fmt:formatNumber value="${item.price }" pattern="#.##" minFractionDigits="8" > </fmt:formatNumber></td>
                                            <td class="amount">${item.amount }</td>
                                            <td class="taxAmount">${item.taxAmount }</td>
                                            <td>${item.taxRate }</td>
                                            <td class="tax">${item.tax }</td>
                                            <td>${item.brandName }</td>
                                            <td class="grossWeights">${item.grossWeightSum }</td>
                                            <td class="netWeight">${item.netWeightSum }</td>
                                            <td>${item.boxNo }</td>
                                            <td>${item.batchNo }</td>
                                            <td>${item.constituentRatio }</td>
                                            <td>${item.remark }</td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <div class="form-row mt20">
		                   		<div class="col-2">
		                            <span>总数量：</span>
		                            <span id="totalNum"></span>
		                        </div>
		                        <div class="col-2">
		                            <span>总金额（不含税）：</span>
		                            <span id="totalAmount"></span>
		                        </div>
		                        <div class="col-2">
		                            <span>税额：</span>
		                            <span id="totalTax"></span>
		                        </div>
		                        <div class="col-2">
		                            <span>总金额（含税）：</span>
		                            <span id="totalTaxAmount"></span>
		                        </div>
		                        <div class="col-2">
		                            <span>总毛重：</span>
		                            <span id="totalGrossWright"></span>
		                        </div>
		                        <div class="col-2">
		                            <span>总净重：</span>
		                            <span id="totalNetWright"></span>
		                        </div>
		                   </div>
                            <!--   商品信息  end -->
                            <div class="form-row">
                                <div class="col-3">
                                    <div class="form-row m-t-50">
                                        <div class="row">
                                            <div class="col-12">
                                                <button type="button" class="btn btn-find waves-effect waves-light btn_default"
                                                        id="cancel-buttons">返 回
                                                </button>
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
                <!-- 弹窗结束 -->
            </div>
        </div>
        <!-- container -->
    </div>

</div>
<!-- content -->
<script type="text/javascript">
    $(function () {

        //财务相关收起，展开
        $("#finance").click(function () {
            $("#finance-list").toggle();
        });

        //款项信息收起，展开
        $("#term").click(function () {
            $("#term-list").toggle();
        });

        function dh_list() {
            $module.load.pageOrder("act=3001");
        }

        function dh_details(id) {
            $module.load.pageOrder("act=30013&id=" + id);
        } 

        //返回
        $("#cancel-buttons").click(function () {
            var pageSource = '${pageSource}';
            if (pageSource == "1") {
                $load.a("/list/menu.asyn?act=100&r="+Math.random());
            } else {
                $module.load.pageOrder("act=3001");
            }
        });
        
        sumTotal() ;
    });
    
    /**
  	*	汇总净重,总毛重，总数，总金额
  	*/
  	function sumTotal() {

  		var totalGrossWeights = 0.0 ;
  		$("#table-list").find(".grossWeights").each(function(i, m){
    		let val = $(m).text() ;
    		
    		if(val){
    			val = parseFloat(val);
    			totalGrossWeights = parseFloat(totalGrossWeights) ;
    			totalGrossWeights += val ;
    			totalGrossWeights = parseFloat(totalGrossWeights).toFixed(5);
    			
    			$("#totalGrossWright").html(totalGrossWeights) ;
    		}
    		
    	}) ;
  		
  		var totalNum = 0 ;
  		$("#table-list").find(".num").each(function(i, m){
    		let val = $(m).text() ;
    		
    		if(val){
    			val = parseInt(val);
    			totalNum = parseInt(totalNum) ;
    			totalNum += val ;
    			
    			$("#totalNum").html(totalNum) ;
    		}
    		
    	}) ;
  		
  		var totalAmount = 0.0 ;
  		$("#table-list").find(".amount").each(function(i, m){
    		let val = $(m).text() ;
    		
    		if(val){
    			val = parseFloat(val);
    			totalAmount = parseFloat(totalAmount) ;
    			totalAmount += val ;
    			totalAmount = parseFloat(totalAmount).toFixed(2);
    			
    			$("#totalAmount").html(totalAmount) ;
    		}
    		
    	}) ;
  		
  		var totalTaxAmount = 0.0 ;
  		$("#table-list").find(".taxAmount").each(function(i, m){
    		let val = $(m).text() ;
    		
    		if(val){
    			val = parseFloat(val);
    			totalTaxAmount = parseFloat(totalTaxAmount) ;
    			totalTaxAmount += val ;
    			totalTaxAmount = parseFloat(totalTaxAmount).toFixed(2);
    			
    			$("#totalTaxAmount").html(totalTaxAmount) ;
    		}
    		
    	}) ;
  		
  		var totalTax = 0.0 ;
  		$("#table-list").find(".tax").each(function(i, m){
    		let val = $(m).text() ;
    		
    		if(val){
    			val = parseFloat(val);
    			totalTax = parseFloat(totalTax) ;
    			totalTax += val ;
    			totalTax = parseFloat(totalTax).toFixed(2);
    			
    			$("#totalTax").html(totalTax) ;
    		}
    		
    	}) ;
  		
  		var totalNetWright = 0.0 ;
  		$("#table-list").find(".netWeight").each(function(i, m){
    		let val = $(m).text() ;
    		
    		if(val){
    			val = parseFloat(val);
    			totalNetWright = parseFloat(totalNetWright) ;
    			totalNetWright += val ;
    			totalNetWright = parseFloat(totalNetWright).toFixed(5);
    			
    			$("#totalNetWright").html(totalNetWright) ;
    		}
    		
    	}) ;
  	}
</script>
