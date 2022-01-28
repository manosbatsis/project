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
                       <li class="breadcrumb-item"><a href="#">销售订单详情</a></li>
                    </ol>
                    </div>
                   </div>
                    <!--  title   end  -->               
                          <!--  title   基本信息   start -->
                    <div class="title_text">基本信息</div>
                    <div class="info_box">
                        <div class="info_item">
                            <div class="info_text">
                                <span>销售订单编号：</span>
                                <span>
                                    ${detail.code}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>出仓仓库：</span>
                                <span>
                                    ${detail.outDepotName}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>订单状态：</span>
                                <span>
                               	 ${detail.stateLabel}	
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>创建人：</span>
                                <span>
                                    ${detail.createName}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>入仓仓库：</span>
                                <span>
                                    ${detail.inDepotName}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>参照订单：</span>
                                <span>
                                    ${detail.referToOrder}
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>创建时间：</span>
                                <span>
                                   <fmt:formatDate value="${detail.createDate}" pattern="yyyy-MM-dd HH:mm:ss" />
                                </span>
                            </div>
                            <div class="info_text">
                                <span>LBX单号：</span>
                                <span>
                                    ${detail.lbxNo}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>客户：</span>
                                <span>
                                    ${detail.customerName}
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>审核人：</span>
                                <span>
                                    ${detail.auditName}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>PO单号：
                                <textarea readonly="readonly"  style="width: 100%;overflow: auto;word-break: break-all;">${detail.poNo}</textarea>
                                </span>
                            </div>
                            <div class="info_text">
                                <span>销售类型：</span>
                                <span>
                                    ${detail.businessModelLabel}
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                        	<div class="info_text">
                                <span>审核时间：</span>
                                <span>
                                    <fmt:formatDate value="${detail.auditDate}" pattern="yyyy-MM-dd HH:mm:ss" />
                                </span>
                            </div>
                            <div class="info_text">
                                <span>PO单时间：</span>
                                <span>
                                    <fmt:formatDate value="${detail.poDate}" pattern="yyyy-MM-dd HH:mm:ss" />
                                </span>
                            </div>
                            <div class="info_text">
                                <span>销售币种：</span>
                                <span>${detail.currencyLabel}</span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>备注：</span>
                                <span>
                                    ${detail.remark}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>签收时间：</span>
                                <span>
                               <fmt:formatDate value="${detail.receiveDate}" pattern="yyyy-MM-dd" />
                               </span>
                            </div>
                            <div class="info_text">
                                <span>合同号：</span>
                                <span>
                                    ${detail.contractNo}
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>交货日期：</span>
                                <span>
                                    <fmt:formatDate value="${detail.deliveryDate}" pattern="yyyy-MM-dd" />
                                </span>
                            </div>
                            <div class="info_text">
                            	<span>归属月份：</span>
                                <span>${detail.ownMonth}</span>
                            </div>
                            <div class="info_text">
                            	<span>原销出仓仓库：</span>
                                <span>${detail.originalOutDepotName}</span>
                            </div>
                        </div>
                    </div>
                      <div class="info_item">
                        <div class="info_text">
                            <span>事业部：</span>
                            <span>${detail.buName}</span>
                        </div>
                        <div class="info_text">
                            <span>单据标识：</span>
                            <span>${detail.orderTypeLabel}</span>
                        </div>
                        <div class="info_text">
                            <span>承运商：</span>
                            <span>${detail.carrier}</span>
                        </div>
                    </div>
                    <div class="info_item">
                        <div class="info_text">
                            <span>出库地点：</span>
                            <span>${detail.outdepotAddr}</span>
                        </div>
                        <div class="info_text">
                            <span>提单毛重：</span>
                            <span>${detail.billWeight}</span>
                        </div>
                        <div class="info_text">
                            <span>运输方式：</span>
                            <span>${detail.transportLabel}</span>
                        </div>
                    </div>
                    <div class="info_item">
                        <div class="info_text">
                            <span>标准箱TEU：</span>
                            <span>${detail.teu}</span>
                        </div>
                        <div class="info_text">
                            <span>车次：</span>
                            <span>${detail.trainno}</span>
                        </div>
                        <div class="info_text">
                            <span>托数：</span>
                            <span>${detail.torusNumber}</span>
                        </div>
                    </div>
                    <div class="info_item">
                        <div class="info_text">
                            <c:if test="${not empty detail.tallyingUnit}">
                                <span>海外仓理货单位：</span>
                                <span>
                                        ${detail.tallyingUnitLabel}
                                </span>
                            </c:if>
                        </div>
                        <div class="info_text">
                            <c:if test="${not empty detail.destination}">
                                <span>目的地：</span>
                                <span>
                                        ${detail.destinationLabel}
                                </span>
                            </c:if>
                        </div>
                        <div class="info_text"></div>
                    </div>
                    <div class="info_item">
                        <div class="info_text">
                            <span>关联预售单单号：</span>
                            <span>${detail.preSaleOrderRelCode}</span>
                        </div>
                        <div class="info_text">
                            <span>调整人：</span>
                            <span>${detail.adjusterUsername}</span>
                        </div>
                        <div class="info_text">
                            <span>调整时间：</span>
                            <span> <fmt:formatDate value="${detail.adjustDate}" pattern="yyyy-MM-dd HH:mm:ss" /></span>
                        </div>
                    </div>
                    <div class="info_item">
                        <div class="info_text">
                            <span>提交人：</span>
                            <span>${detail.submiterName}</span>
                        </div>
                        <div class="info_text">
                            <span>提交时间：</span>
                            <span><fmt:formatDate value="${detail.submitDate}" pattern="yyyy-MM-dd HH:mm:ss" /></span>
                        </div>
                    </div>
                    <div class="title_text"> 收件信息</div>
                    <div class="info_item">
                           <div class="info_text">
                               <span>提货方式：</span>
                               <span>
                                   ${detail.mailModeLabel}
                               </span>
                           </div>                            
                           <div class="info_text">
                               <span>收件人姓名：</span>
                               <span>${detail.receiverName}</span>
                           </div>
                    </div>
                    <div class="info_item">
                           <div class="info_text">
                               <span>国家：</span>
                               <span>${detail.country}</span>
                           </div>                            
                           <div class="info_text">
                               <span>详细地址：</span>
                               <span>${detail.receiverAddress}</span>
                           </div>
                    </div>
                      <!--  融资信息  start -->
                    <c:if test="${finance != null}">
                        <div class="title_text">融资信息</div>
                        <div class="info_box">
                            <div class="info_item">
                                <div class="info_text">
                                    <span>融资金额：</span>
                                    <c:choose>
                                        <c:when test="${finance.saleAmount != null }">
                                            <span><fmt:formatNumber value="${finance.saleAmount}" pattern="#.##" minFractionDigits="2"/></span>
                                        </c:when>
                                        <c:otherwise>
                                            <span>${finance.saleAmount}</span>
                                        </c:otherwise>
                                    </c:choose>

                                </div>
                                <div class="info_text">
                                    <span>保证金：</span>
                                    <c:choose>
                                        <c:when test="${finance.marginAmount != null }">
                                            <span><fmt:formatNumber value="${finance.marginAmount}" pattern="#.##" minFractionDigits="2"/></span>
                                        </c:when>
                                        <c:otherwise>
                                            <span> </span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="info_text">
                                    <span>起算日期：</span>
                                    <c:choose>
                                        <c:when test="${finance.applyDate != null }">
                                            <span><fmt:formatDate value="${finance.applyDate}" pattern="yyyy-MM-dd" /></span>
                                        </c:when>
                                        <c:otherwise>
                                            <span></span>
                                        </c:otherwise>
                                    </c:choose>
                                    <span></span>
                                </div>
                            </div>
                            <div class="info_item">
                                <div class="info_text">
                                    <span>实际还款日：</span>
                                    <c:choose>
                                        <c:when test="${finance.deadlineDate != null }">
                                            <span><fmt:formatDate value="${finance.deadlineDate}" pattern="yyyy-MM-dd" /></span>
                                        </c:when>
                                        <c:otherwise>
                                            <span> </span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="info_text">
                                    <span>资金占用费：</span>
                                    <c:choose>
                                        <c:when test="${finance.occupationAmount != null }">
                                            <span><fmt:formatNumber value="${finance.occupationAmount}" pattern="#.##" minFractionDigits="2"/></span>
                                        </c:when>
                                        <c:otherwise>
                                            <span> </span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="info_text">
                                    <span>还款本金：</span>
                                    <c:choose>
                                        <c:when test="${finance.principalAmount != null }">
                                            <span><fmt:formatNumber value="${finance.principalAmount}" pattern="#.##" minFractionDigits="2"/></span>
                                        </c:when>
                                        <c:otherwise>
                                            <span> </span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            <div class="info_item">
                                <div class="info_text">
                                    <span>代理费：</span>
                                    <c:choose>
                                        <c:when test="${finance.agencyAmount != null }">
                                            <span><fmt:formatNumber value="${finance.agencyAmount}" pattern="#.##" minFractionDigits="2"/></span>
                                        </c:when>
                                        <c:otherwise>
                                            <span> </span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="info_text">
                                    <span>滞纳金：</span>
                                    <c:choose>
                                        <c:when test="${finance.delayAmount != null }">
                                            <span><fmt:formatNumber value="${finance.delayAmount}" pattern="#.##" minFractionDigits="2" /></span>
                                        </c:when>
                                        <c:otherwise>
                                            <span> </span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="info_text">
                                    <span>应还本金：</span>
                                    <c:choose>
                                        <c:when test="${finance.payableAmount != null }">
                                            <span><fmt:formatNumber value="${finance.payableAmount}" pattern="#.##" minFractionDigits="2"/></span>
                                        </c:when>
                                        <c:otherwise>
                                            <span> </span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </c:if>
                    <!-- 融资信息 end -->
                    <ul class="nav nav-tabs">
                        <li class="nav-item">
                            <a href="#itemList" data-toggle="tab"
                               class="nav-link active">商品信息</a>
                        </li>
                         <form id="form1">
                            <input type="hidden" name='orderId' id='orderId' value='${detail.id}'/>
                          </form>
                          <li class="nav-item">
                            <a href="#batchDefault" data-toggle="tab" class="nav-link">商品上架明细</a>
                         </li>
                        <c:if test="${finance != null}">
                            <li class="nav-item">
                                <a href="#financeItemList" data-toggle="tab" class="nav-link">商品融资信息</a>
                            </li>
                        </c:if>
                    </ul>
                    <div class="tab-content">
                        <div class="tab-pane fade show active table-responsive" id="itemList">
                            <table  id="datatable-items" class="table table-striped"  width="100%">
                                <thead>
                                <tr>
                                       <th>序号</th>
                                       <th>商品编号</th>
                                       <th>商品名称</th>
                                       <th>商品货号</th>
                                       <th>条码</th>
                                       <th>销售数量</th>
                                       <th>销售单价(不含税)</th>
                                       <th>申报单价</th>
                                       <th>销售总金额(不含税)</th>
                                       <th>销售总金额(含税)</th>
                                       <th>税率</th>
                                       <th>税额</th>
                                       <th>品牌类型</th>
                                       <th>箱号</th>
                                       <th>合同号</th>
                                       <th>备注</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${detail.itemList}" var="item" varStatus="status">
                                    <tr>
                                        <td>${item.seq}</td>
                                        <td>${item.goodsCode}</td>
                                        <td>${item.goodsName}</td>
                                        <td>${item.goodsNo}</td>
                                        <td>${item.barcode}</td>
                                        <td class="goods-num">${item.num}</td>
                                        <td>${item.price}</td>
                                        <td>${item.declarePrice}</td>
                                        <td name='amount' class="goods-amount"><fmt:formatNumber value="${item.amount}" pattern="#.##" minFractionDigits="2" /></td>
                                        <td name='taxAmount' class="goods-taxAmount"><fmt:formatNumber value="${item.taxAmount}" pattern="#.##" minFractionDigits="2" /></td>
                                        <td >${item.taxRate}</td>
                                        <td name='tax' class="goods-tax"><fmt:formatNumber value="${item.tax}" pattern="#.##" minFractionDigits="2" /></td>
                                        <td>${item.brandName}</td>
                                        <td>${item.boxNo}</td>
                                        <td>${item.contractNo}</td>
                                        <td>${item.remark}</td>
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
                                <tbody>
                                <c:forEach items="${saleShelfDTO.saleShelfDTOList}" var="item" varStatus="status">
                                    <tr>
                                        <td>${item.goodsNo}</td>
                                        <td>${item.goodsName}</td>
                                        <td>${item.barcode}</td>
                                        <td>${item.tallyingUnit}</td>
                                        <td>${item.num}</td>
                                        <td>${item.shelfNum}</td>
                                        <td>${item.damagedNum}</td>
                                        <td>${item.lackNum}</td>
                                        <td>${item.shelfDate}</td>
                                        <td>${item.poNo}</td>
                                        <td>${item.remark}</td>
                                        <td>${item.operator}</td>
                                        <td>${item.createDate}</td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                        <c:if test="${finance != null}">
                            <div class="tab-pane fade table-responsive" id="financeItemList">
                                <table class="table table-striped"  width="100%" id="finance-items">
                                    <thead>
                                    <tr>
                                        <th>子合同编号</th>
                                        <th>商品编号</th>
                                        <th>商品名称</th>
                                        <th>商品货号</th>
                                        <th>条形码</th>
                                        <th>销售数量</th>
                                        <th>理货单位</th>
                                        <th>销售单价</th>
                                        <th>销售总金额</th>
                                        <th>赎回单价</th>
                                        <th>赎回金额</th>
                                        <th>备注</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${finance.itemList}" var="item" varStatus="status">
                                        <tr>
                                            <td>${item.contractNo}</td>
                                            <td>${item.goodsCode}</td>
                                            <td>${item.goodsName}</td>
                                            <td>${item.goodsNo}</td>
                                            <td>${item.barcode}</td>
                                            <td>${item.num}</td>
                                            <td>${item.tallyingUnitLabel}</td>
                                            <td><fmt:formatNumber value="${item.price}" pattern="#.####" /></td>
                                            <td><fmt:formatNumber value="${item.amount}" pattern="#.##" /></td>
                                            <c:choose>
                                                <c:when test="${item.ransomPrice != null}">
                                                    <td><fmt:formatNumber value="${item.ransomPrice}" pattern="#.####" minFractionDigits="4" /></td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td> </td>
                                                </c:otherwise>
                                            </c:choose>
                                            <c:choose>
                                                <c:when test="${item.ransomPrice != null}">
                                                    <td><fmt:formatNumber value="${item.ransomAmount}" pattern="#.##" minFractionDigits="2"/></td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td> </td>
                                                </c:otherwise>
                                            </c:choose>
                                            <td>${item.remark}</td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:if>
                    </div>
              </div>
                    <div class="form-row m-t-20">
                        <div class="col-12">
                            <div class="row">
                                <div class="col-3">
                                </div>
                                <div class="col-6">
                                    <table>
                                        <tr>
                                            <td align="center">销售数量：<span name="totalNum" id="totalNum"></span></td>
                                            <td width="150px"></td>
                                            <td align="center">销售总金额：<span name="totalAmount" id="totalAmount"></span></td>
                                        </tr>
                                    </table>
                                </div>
                                <div class="col-3">
                                </div>
                            </div>
                        </div>
                    </div>
                 <!--   商品上架明细  end -->
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
             </div>
                  <!-- end row -->
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->
<script type="text/javascript">
//table
 $(document).ready(function() {
		//取消按钮
		$("#cancel-buttons").click(function(){
            var pageSource = '${pageSource}';
            if (pageSource == "1") {
                $load.a("/list/menu.asyn?act=100&r="+Math.random());
            } else {
                $module.load.pageOrder('act=4001');
            }
		});
		   // initDataTabel() ;
 }); 
 
 //tab切换时，初始化对应表格
 // $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
 // 	  var activeTab = $(e.target).text();
 // 	  initDataTabel(activeTab) ;
 // }) ;
 //
	// function initDataTabel(activeTab){
	// 	//初始化
	//     $mytable.fn.init();
 //
	// 	 if(activeTab == '商品上架明细'){
	// 			tableUrl = serverAddr+'/sale/listSaleShelf.asyn?r='+Math.random() ;
	// 	  		tableColumns = [
	// 					{data : 'goodsNo'},{data : 'goodsName'},{data : 'barcode'},{data : 'tallyingUnit'},
	// 					{data : 'num'},{data : 'shelfNum'},{data : 'damagedNum'},{data : 'lackNum'},{data : 'shelfDate'},{data : 'poNo'},{data : 'remark'},
	// 					{data : 'operator'},{data : 'createDate'}
	// 	  	        ] ;
	// 	  		tableId = "datatable-batchs" ;
	// 		}
 //
	//     //配置表格参数
	//     $mytable.params={
	//         url:tableUrl,
	//         columns:tableColumns,
	//         formid:'#form1'
	//     };
 //
	// 	//每一行都进行 回调
 //        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
 //        	 if (aData.tallyingUnit == '00'){
 //                 $('td:eq(3)', nRow).html('托盘');
 //             }else if (aData.tallyingUnit == '01'){
 //                $('td:eq(3)', nRow).html('箱');
 //            }else if (aData.tallyingUnit == '02'){
 //                $('td:eq(3)', nRow).html('件');
 //            }
 //        };
	//     //生成列表信息
	//     $('#' + tableId).mydatatables();
	// }


$(function () {
    var total = 0;
    $(".goods-num").each(function () {
        var val = $(this).html();
        total += parseFloat(val);
    })
    $("#totalNum").html(total);
})

$(function () {
    var total = 0;
    $(".goods-amount").each(function () {
        var val = $(this).html();
        total += parseFloat(val);
    })
    $("#totalAmount").html(total.toFixed(2));
})

</script>
