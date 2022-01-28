<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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
</style>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
        <form id="add-form" action="/merchandise/saveMerchandise.asyn">
            <div class="row">
                <div class="col-12 ml10">
                    <div class="col-12 row">
                        <ol class="breadcrumb">
                            <li><i class="block"></i> 当前位置：</li>
                            <li class="breadcrumb-item"><a href="#">电商订单详情</a></li>
                        </ol>
                    </div>
                    <div class="card-box">
                        <div id="step"></div>
                        <div class="title_text mt15">订单信息</div>
                        <div class="info_box">
                            <div class="info_item">
                                <div class="info_text">
                                    <span>单据状态：</span>
                                    <span>
                                    	${detail.statusLabel}
                                    </span>
                                </div>
                                <div class="info_text">
                                    <span>订单编号：</span>
                                    <span>
                                        ${detail.code}
                                    </span>
                                </div>
                                <div class="info_text">
                                    <span>国检状态：</span>
                                    <span>
                                    	${detail.inspectStatusLabel}
		                            </span>
                                </div>
                            </div>
                            <div class="info_item">
                                <div class="info_text">
                                    <span>物流企业名称：</span>
                                    <span>
                                      <c:choose>
                                          <c:when test="${detail.logisticsNameLabel==null or detail.logisticsNameLabel==''}">
                                              ${detail.logisticsName}
                                          </c:when>
                                          <c:otherwise>
                                              ${detail.logisticsNameLabel}
                                          </c:otherwise>
                                      </c:choose>
                                    </span>
                                </div>
                                <div class="info_text">
                                    <span>外部交易单号：</span>
                                    <span>
                                        ${detail.externalCode}
                                    </span>
                                </div>
                                <div class="info_text">
                                    <span>海关状态：</span>
                                    <span>
                                    		${detail.customsStatusLabel}
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
                                    <span>公司：</span>
                                    <span>
                                        ${detail.merchantName}
                                    </span>
                                </div>
                                <div class="info_text">
                                    <span>平台名称：</span>
                                    <span>
                                        ${detail.storePlatformNameLabel}
                                    </span>
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
                                    <span>出库仓库：</span>
                                    <span>
                                        ${detail.depotName}
                                    </span>
                                </div>
                                <div class="info_text">
                                    <span>进口模式：</span>
                                    <span>
                                    	${detail.importModeLabel}
                                    </span>
                                </div>
                            </div>
                            <div class="info_item">
                                <div class="info_text">
                                    <span>订单来源：</span>
                                    <span>
                                    	 ${detail.orderSourceLabel}
                                    </span>
                                </div>
                                <div class="info_text">
                                    <span>交易时间：</span>
                                    <span>
                                         <fmt:formatDate value="${detail.tradingDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </span>
                                </div>
                                <div class="info_text">
	                                 <span>店铺名称：</span>
	                                    <span>
	                                        ${detail.shopName}
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
                                 		<span>平台订单号：</span>
	                                    <span>
	                                        ${detail.exOrderId}
	                                    </span>
                                </div>
                                <div class="info_text">
                                  <span>运营类型：</span>
                                  <span>
                                      ${detail.shopTypeCodeLabel}
                                  </span>
                              </div>
                            </div>
                            <div class="info_item">
                                <div class="info_text">
                                    <span>包裹重量：</span>
                                    <span>
                                        ${detail.weight}
                                    </span>
                                </div>
                                <div class="info_text">
                                    <span>客户：</span>
                                    <span>
                                        ${detail.customerName}
                                    </span>
                                </div>
                                <div class="info_text"></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-12 row_half">
                        <div class="card-box col-6" style="margin-right: 15px;">
                            <div class="title_text">订购人信息</div>
                            <%--     <div class="form-row mt20 ml15">
                                     <div class="col-4">
                                         <div class="row">
                                             <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">订购人 <span>：</span></span></label>
                                             <div class="col-7 ml10 line">
                                                 <div>${detail.makerName}</div>
                                             </div>
                                         </div>
                                     </div>
                                     <div class="col-4">
                                         <div class="row">
                                             <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">注册号 <span>：</span></span></label>
                                             <div class="col-7 ml10 line">
                                                 <div>${detail.makerRegisterNo}</div>
                                             </div>
                                         </div>
                                     </div>
                                     <div class="col-4">
                                         <div class="row">
                                             <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">电话号码 <span>：</span></span></label>
                                             <div class="col-7 ml10 line">
                                                 <div>${detail.makerTel}</div>
                                             </div>
                                         </div>
                                     </div>
                                 </div>--%>
                            <div class="info_box">
                                <div class="info_item">
                                    <div class="info_text">
                                        <span>订购人：</span>
                                        <span>${detail.makerName}</span>
                                    </div>
                                </div>
                                <div class="info_item">
                                    <div class="info_text">
                                        <span>注册号：</span>
                                        <span>${detail.makerRegisterNo}</span>
                                    </div>
                                </div>
                                <div class="info_item">
                                    <div class="info_text">
                                        <span>电话号码：</span>
                                        <span>${detail.makerTel}</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="card-box col-6">
                            <div class="title_text">收件人信息</div>
                            <%--<div class="form-row mt20 ml15">
                                <div class="col-4">
                                    <div class="row">
                                        <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">收件人 <span>：</span></span></label>
                                        <div class="col-7 ml10 line">
                                            <div>${detail.receiverName}</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-4">
                                    <div class="row">
                                        <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">电话号码 <span>：</span></span></label>
                                        <div class="col-7 ml10 line">
                                            <div>${detail.receiverTel}</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-row">
                                <div class="col-9">
                                    <div class="row">
                                        <label class="col-2 col-form-label " style="white-space:nowrap;"><span class="fr">收件地址 <span>：</span></span></label>
                                        <div class="col-10 ml10 line">
                                            <div>${detail.receiverAddress}</div>
                                        </div>
                                    </div>
                                </div>
                            </div>--%>
                            <div class="info_box">
                                <div class="info_item">
                                    <div class="info_text">
                                        <span>收件人：</span>
                                        <span>${detail.receiverName}</span>
                                    </div>
                                    <div class="info_text">
                                        <span>电话号码：</span>
                                        <span>${detail.receiverTel}</span>
                                    </div>
                                    <div class="info_text">

                                    </div>
                                </div>
                                <div class="info_item">
                                    <div class="info_text">
                                        <span>收件人省份：</span>
                                        <span>${detail.receiverProvince}</span>
                                    </div>
                                    <div class="info_text">
                                        <span>收件人城市：</span>
                                        <span>${detail.receiverCity}</span>
                                    </div>
                                    <div class="info_text">
                                        <span>收件人地址：</span>
                                        <span>${detail.receiverAddress}</span>
                                    </div>
                                </div>
                                <div class="info_item"></div>
                            </div>
                        </div>
                    </div>
                    <div class="card-box">
                        <div class="title_text">商品信息</div>
                        <div class="row col-12 mt20">
                            <div class="w100">
                                <ul class="nav nav-tabs">
                                    <li class="nav-item">
                                        <a href="javascript:void(0);" onclick="changeTable('0');" data-toggle="tab"
                                           class="nav-link active">商品信息</a>
                                    </li>
                                    <li class="nav-item">
                                        <a href="javascript:void(0);" onclick="changeTable('1');" data-toggle="tab"
                                           class="nav-link">结算信息</a>
                                    </li>
                                    <li class="nav-item">
                                        <a href="javascript:void(0);" onclick="changeTable('2');" data-toggle="tab"
                                           class="nav-link">批次信息</a>
                                    </li>
                                </ul>
                                <div class="tab-content">
                                    <div class="tab-pane fade show active" id="itemList">
                                        <div id="mytable">
                                            <table class="table table-striped" cellspacing="0" width="100%">
                                                <thead>
                                                <tr>
                                                    <th style="white-space:nowrap;">序号</th>
                                                    <th style="white-space:nowrap;">事业部</th>
                                                    <th style="white-space:nowrap;">商品货号</th>
                                                    <th style="white-space:nowrap;">商品编号</th>
                                                    <th style="white-space:nowrap;">商品名称</th>
                                                    <th style="white-space:nowrap;">商品条形码</th>
                                                    <th style="white-space:nowrap;">销售数量</th>
                                                    <th style="white-space:nowrap;">销售单价</th>
                                                    <th style="white-space:nowrap;">销售总金额</th>
                                                    <th style="white-space:nowrap;">商品优惠金额</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach var="item" items="${detail.itemList}" varStatus="status">
                                                    <tr>
                                                        <th style="font: 13px bold">${status.index+1}</th>
                                                        <th style="font: 13px bold">${item.buName}</th>
                                                        <th style="font: 13px bold">${item.goodsNo}</th>
                                                        <th style="font: 13px bold">${item.goodsCode}</th>
                                                        <th style="font: 13px bold">${item.goodsName}</th>
                                                        <th style="font: 13px bold">${item.barcode}</th>
                                                        <th style="font: 13px bold">${item.num}</th>
                                                   		<th style="font: 13px bold">${item.originalPrice}</th>
                                                        <th style="font: 13px bold"><fmt:formatNumber value="${item.originalDecTotal}" pattern="#.##" minFractionDigits="2" /></th>
                                                        <th style="font: 13px bold">${item.goodsDiscount}</th>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                          <div id="mytable1" style="display: none;">
                                            <table class="table table-striped" cellspacing="0" width="100%">
                                                <thead>
                                                <tr>
                                                    <th style="white-space:nowrap;">序号</th>
                                                    <th style="white-space:nowrap;">商品货号</th>
                                                    <th style="white-space:nowrap;">商品编号</th>
                                                    <th style="white-space:nowrap;">商品名称</th>
                                                    <th style="white-space:nowrap;">商品条形码</th>
                                                    <th style="white-space:nowrap;">结算数量</th>
                                                    <th style="white-space:nowrap;">结算单价</th>
                                                    <th style="white-space:nowrap;">结算总额</th>
                                                    <th style="white-space:nowrap;">佣金</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach var="item" items="${detail.itemList}" varStatus="status">
                                                    <tr>
                                                        <th style="font: 13px bold">${status.index+1}</th>
                                                        <th style="font: 13px bold">${item.goodsNo}</th>
                                                        <th style="font: 13px bold">${item.goodsCode}</th>
                                                        <th style="font: 13px bold">${item.goodsName}</th>
                                                        <th style="font: 13px bold">${item.barcode}</th>
                                                        <th style="font: 13px bold">${item.num}</th>
                                                        <th style="font: 13px bold">${item.price}</th>
                                                        <th style="font: 13px bold">${item.decTotal}</th>
                                                        <th style="font: 13px bold">${item.saleCom}</th>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                        <div id="mytable2" style="display: none;">
                                            <table class="table table-striped" cellspacing="0" width="100%">
                                                <thead>
                                                <tr>
                                                    <th>序号</th>
                                                    <th>商品货号</th>
                                                    <th>商品编号</th>
                                                    <th>商品名称</th>
                                                    <th>商品条形码</th>
                                                    <th>批次</th>
                                                    <th>生产日期</th>
                                                    <th>失效日期</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach var="item" items="${batchData}" varStatus="status">
                                                    <tr>
                                                        <th style="font: 13px bold">${status.index+1}</th>
                                                        <th style="font: 13px bold">${item.goodsNo}</th>
                                                        <th style="font: 13px bold">${item.goodsCode}</th>
                                                        <th style="font: 13px bold">${item.goodsName}</th>
                                                        <th style="font: 13px bold">${item.barcode}</th>
                                                        <th style="font: 13px bold">${item.batchNo}</th>
                                                        <th style="font: 13px bold"><fmt:formatDate value="${item.productionDate }" pattern="yyyy-MM-dd"/></th>
                                                        <th style="font: 13px bold"><fmt:formatDate value="${item.overdueDate }" pattern="yyyy-MM-dd"/></th>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <div class="tab-pane fade" id="batchDefault">
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row col-12 mt20">
                            <div class="col-2"></div>
                            <div class="col-2">
                                <span>运费：</span>
                                <span>${detail.wayFrtFee}</span>
                            </div>
                            <div class="col-2">
                                <span>税费：</span>
                                <span>${detail.taxes}</span>
                            </div>
                            <div class="col-2">
                                <span>保费：</span>
                                <span>${detail.wayIndFee}</span>
                            </div>
                            <div class="col-2">
                                <span>优惠金额：</span>
                                <span>${detail.discount}</span>
                            </div>
                            <div class="col-2">
                                <span title="实付总额=∑（货款）+运费+税款-优惠金额">实付总额<i class="mdi mdi-comment-alert-outline"/>：</span>
                                <span>${detail.payment}</span>
                               <%-- <div class="row">
                                    <label class="col-10 col-form-label " style="white-space:nowrap;"
                                           title="实付总额=∑（货款）+运费+税款-优惠金额"><span class="fr">
					      	  实付总额<i class="mdi mdi-comment-alert-outline"/><span>：</span></span></label>
                                    <div class="col-1 ml10 line">
                                        <div>
                                            ${detail.payment}
                                        </div>
                                    </div>
                                </div>--%>
                            </div>
                        </div>
                        <div class="form-row m-t-50">
                            <div class="col-12">
                                <div class="row">
                                    <div class="col-5">
                                        <button type="button" class="btn btn-find waves-effect waves-light btn_default"
                                                id="cancel-buttons">返回
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
                <!-- end row -->
            </div>
        </form>
    </div>
    <!-- container -->
</div>

<script type="text/javascript">
    $(document).ready(function () {
        var index = 0;
        var flag = '${empty detail.status}';
        if (flag != "true") {
            index = '${detail.status-1}';
            if ('${detail.status}' == '5') {
                index = -1;
            } else if ('${detail.status}' == '1') {
                index = 0;
            } else {
                index = parseInt('${detail.status}');
            }
        }
        var $step = $("#step");
        $step.step({
            index: index,
            time: 500,
            title: ["制单时间<br>${detail.tradingDate}",  "申报时间<br>${detail.declareDate}", "放行时间<br>${detail.releaseDate}", "发货时间<br>${detail.deliverDate}"]
        });
        //取消按钮
        $("#cancel-buttons").click(function () {
            $module.load.pageOrder('act=4002');
        });
    });

    function changeTable(type) {
        if (type == '0') {
            $("#mytable").show();
            $("#mytable1").hide();
            $("#mytable2").hide();
        }else  if (type == '1') {
            $("#mytable1").show();
            $("#mytable").hide();
            $("#mytable2").hide();
        }else  if (type == '2') {
            $("#mytable2").show();
            $("#mytable").hide();
            $("#mytable1").hide();
        }
    }
</script>