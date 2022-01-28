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
                            <li class="breadcrumb-item "><a href="javascript:$module.load.pageOrder('act=4008');">电商订单退货</a></li>
                            <li class="breadcrumb-item"><a href="#">退货订单详情</a></li>
                        </ol>
                    </div>
                    <div class="card-box">
                        <div id="step"></div>
                        <div class="title_text mt15">订单信息</div>
                        <div class="info_box">
                            <div class="info_item">
                                <div class="info_text">
                                    <span>退货订单编号：</span>
                                    <span>
                                        ${detail.returnCode}
                                    </span>
                                </div>
                                <div class="info_text">
                                    <span>单号：</span>
                                    <span>
                                        ${detail.code}
                                    </span>
                                </div>
                                 <div class="info_text">
                                    <span>物流企业名称：</span>
                                    <span>
                                        ${detail.logisticsName}
                                    </span>
                                    </div>
                                </div>
                                 <div class="info_item">
                                 <div class="info_text">
                                    <span>来源交易单号：</span>
                                    <span>
                                        ${detail.externalCode}
                                    </span>
                                </div>
                                <div class="info_text">
                                    <span>运单号：</span>
                                    <span>
                                        ${detail.logisticsWayBillNo}
                                    </span>
                                </div>
                                 <div class="info_text">
                                    <span>入库单号：</span>
                                    <span>
                                        ${detail.inDepotCode}
                                    </span>
                                </div>
                              </div>
                              <div class="info_item">  
                              	<div class="info_text">
                                    <span>原发货单号：</span>
                                    <span>
                                        ${detail.originalDeliveryCode}
                                    </span>
                                </div>
                              	<div class="info_text">
                                    <span>公司：</span>
                                    <span>
                                        ${detail.merchantName}
                                    </span>
                                </div>
                              <div class="info_text">
                                    <span>电商平台：</span>
                                    <span>
                                        ${detail.storePlatformName}
                                    </span>
                                </div>
                            </div>
                            <div class="info_item">
                                <div class="info_text">
                                    <span>退货单类型：</span>
                                    <span>
                                        ${detail.returnType}
                                    </span>
                                </div>
                                <div class="info_text">
                                    <span>入库仓库：</span>
                                    <span>
                                        ${detail.returnInDepotName}
                                    </span>
                                </div>
                                <div class="info_text">
                                    <span>退货入库时间：</span>
                                    <span>
                                        <fmt:formatDate value="${detail.returnInDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </span>
                                </div>
                            </div>
                            <div class="info_item">
                                <div class="info_text">
                                    <span>店铺名称：</span>
                                    <span>
                                        ${detail.shopName}
                                    </span>
                                </div>
                                <div class="info_text">
	                                <span>币种：</span>
	                                <span>
	                                    ${detail.currencyLabel}
	                                </span>
                                </div>
                                <div class="info_text">
                                    <span>订单来源：</span>
                                    <span>
                                     		${detail.orderReturnSourceLabel}
                                    </span>
                                </div>
                            </div>
                            <div class="info_item">
                                <div class="info_text">
                                    <span>运营类型：</span>
                                    <span>
                                        ${detail.shopTypeCodeLabel}
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
                            <div class="title_text">买家信息</div>
                            <div class="info_box">
                                <div class="info_item">
                                    <div class="info_text">
                                        <span>买家姓名：</span>
                                        <span>${detail.buyerName}</span>
                                    </div>
                                </div>
                                <div class="info_item">
                                    <div class="info_text">
                                        <span>买家手机号：</span>
                                        <span>${detail.buyerPhone}</span>
                                    </div>
                                </div>
                                <div class="info_item">
                                    <div class="info_text">
                                        <span>邮编：</span>
                                        <span>${detail.postcode}</span>
                                    </div>
                                </div>
                                 <div class="info_item">
                                    <div class="info_text">
                                        <span>退货人地址：</span>
                                        <span>${detail.returnAddr}</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="card-box">
                        <div class="title_text">退货商品信息</div>
                        <div class="row col-12 mt20">
                            <div class="w100">
                                <ul class="nav nav-tabs">
                                    <li class="nav-item">
                                        <a href="javascript:void(0);" onclick="changeTable('0');" data-toggle="tab"
                                           class="nav-link active">商品信息</a>
                                    </li>
                                    <li class="nav-item">
                                        <a href="javascript:void(0);" onclick="changeTable('1');" data-toggle="tab"
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
                                                    <th style="white-space:nowrap;">商品条形码</th>
                                                    <th style="white-space:nowrap;">商品名称</th>
                                                    <th style="white-space:nowrap;">正常品数量</th>
                                                    <th style="white-space:nowrap;">残次品数量</th>
                                                    <th style="white-space:nowrap;">退货总数量</th>
                                                    <th style="white-space:nowrap;">销售单价</th>
                                                    <th style="white-space:nowrap;">销售金额</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach var="item" items="${detail.itemList}" varStatus="status">
                                                    <tr>
                                                        <th style="font: 13px bold">${status.index+1}</th>
                                                        <th style="font: 13px bold">${item.buName}</th>
                                                        <th style="font: 13px bold">${item.inGoodsNo}</th>
                                                        <th style="font: 13px bold">${item.barcode}</th>
                                                        <th style="font: 13px bold">${item.inGoodsName}</th>
                                                        <th style="font: 13px bold">${item.returnNum}</th>
                                                        <th style="font: 13px bold">${item.badGoodsNum}</th>
                                                        <th style="font: 13px bold">${item.returnNum+item.badGoodsNum}</th>
                                                        <th style="font: 13px bold">${item.price}</th>
                                                    	<th style="font: 13px bold">${(item.returnNum+item.badGoodsNum)*item.price}</th>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                        <div id="mytable1" style="display: none;">
                                            <table class="table table-striped" cellspacing="0" width="100%">
                                                <thead>
                                                <tr>
                                                    <th>序号</th>
                                                    <th>商品货号</th>
                                                    <th>商品条形码</th>
                                                    <th>商品名称</th>
                                                    <th>批次</th>
                                                    <th>失效日期</th>
                                                    <th>生产日期</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach var="item" items="${batchData}" varStatus="status">
                                                    <tr>
                                                        <th style="font: 13px bold">${status.index+1}</th>
                                                        <th style="font: 13px bold">${item.goodsNo}</th>
                                                        <th style="font: 13px bold">${item.barcode}</th>
                                                        <th style="font: 13px bold">${item.goodsName}</th>
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
                             <div class="col-2"></div>
                             <div class="col-2"></div>
                              <div class="col-2"></div>
                              <div class="col-2"></div>
                            <div class="col-2">
                                <span>实际退款总金额：</span>
                                <span>${detail.amount}</span>
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
            title: ["退货创建时间<br>${detail.returnInCreateDate}",  "退货入库时间<br>${detail.returnInDate}"]
        });
        //取消按钮
        $("#cancel-buttons").click(function () {
            $module.load.pageOrder('act=4008');
        });
    });

    function changeTable(type) {
        if (type == '1') {
            $("#mytable1").show();
            $("#mytable").hide();
        } else {
            $("#mytable").show();
            $("#mytable1").hide();
        }
    }
</script>