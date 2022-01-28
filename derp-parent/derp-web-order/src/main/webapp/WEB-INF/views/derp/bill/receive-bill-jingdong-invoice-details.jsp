<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common.jsp"/>
<style type="text/css">
    .title_text_new {
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

    td, th {
        padding: 0;
    }

    .pure-table {
        border-collapse: collapse;
        border-spacing: 0;
        empty-cells: show;
        border: 2px solid #cbcbcb;
        width: 95%;
        margin-top: 25px;
        text-align: center;
    }

    .pure-table caption {
        color: #000;
        font: italic 85%/1 arial, sans-serif;
        padding: 1em 0;
        text-align: center;
    }

    .pure-table td, .pure-table th {
        border-left: 2px solid #cbcbcb;
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
        border-bottom: 2px solid #cbcbcb;
    }

    .pure-table-bordered tbody > tr:last-child > td {
        border-bottom-width: 0;
    }
</style>
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
                                            <li class="breadcrumb-item"><a href="#">账单管理</a></li>
                                            <li class="breadcrumb-item"><a href="javascript:dh_list();">应收账单列表 </a></li>
                                            <li class="breadcrumb-item"><a href="#">发票详情</a></li>
                                        </ol>
                                    </div>
                                </div>
                                <!--  title   end  -->
                                <div>
                                    <input type="hidden" name="detail.codes" value="${codes}">
                                    <input type="hidden" name="detail.ids" value="${ids}">
                                    <input type="hidden" name="detail.tempId" value="${tempId}">
                                    <input type="hidden" name="detail.fileTempCode" value="${fileTempCode}">
                                    <input type="hidden" name="detail.currency" value="${currency}">
                                    <input type="hidden" name="detail.customerId" value="${customerId}">
                                    <input type="hidden" name="detail.customerName" value="${customerInfo.name}">
                                    <table class="pure-table pure-table-bordered">
                                        <tr style="text-align:center;">
                                            <td colspan="6">
                                                ${merchantInfo.fullName}
                                                <input type="hidden" name="detail.merchantInvoiceName" value="${merchantInfo.fullName}">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan="6" style="text-align:center;">
                                                ${merchantInfo.englishRegisteredAddress}
                                                <input type="hidden" name="detail.englishRegisteredAddress" value="${merchantInfo.englishRegisteredAddress}">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan="6"
                                                style="text-align:center;font-weight:700;font-size: 16px; text-decoration:underline;">
                                                INVOICE<br>发票
                                            </td>
                                        </tr>

                                        <tr>
                                            <td style="font-weight: bold; text-align: left;">BILL TO</td>
                                            <td colspan="4" style="text-align: right">
                                                DATE 时间：
                                            </td>
                                            <td style="width:150px;">
                                                <input type="text" id="invoiceDate" name="detail.date"
                                                       value="<fmt:formatDate value="${invoiceDate}" pattern="yyyy/MM/dd"/>" required reqMsg="Date不能为空！">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td style="text-align: left;">JD.COM INTERNATIONAL LIMITED</td>
                                            <td colspan="4" style="text-align: right">
                                                INVOICE NO. 发票号：
                                            </td>
                                            <td style="width:150px;">

                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan="5" style="text-align: right">
                                                Purchase Order 采购订单：
                                            </td>
                                            <td style="width:150px;">
                                                <input type="text" id="poNo" name="detail.purchaseOrder" value="${invoice.purchaseOrder}" required reqMsg="Purchase Order不能为空！">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan="5" style="text-align: right">
                                                备注：
                                            </td>
                                            <td style="width:150px;">
                                                <input type="text" name="detail.remarks" value="${invoice.remarks}">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan="6" style="font-weight: bold; text-align: left">BANK INFO</td>
                                        </tr>
                                        <tr>
                                            <td style=" text-align: left">
                                                Beneficiary Name
                                            </td>
                                            <td colspan="5">
                                                ${merchantInfo.fullName}&nbsp;${merchantInfo.englishName}
                                                <input type="hidden" name="detail.merchantName" value="${merchantInfo.fullName}&nbsp;${merchantInfo.englishName}">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td style=" text-align: left">
                                                Beneficiary Address
                                            </td>
                                            <td colspan="5">
                                                ${merchantInfo.englishRegisteredAddress}
                                            </td>
                                        </tr>
                                        <tr>
                                            <td style=" text-align: left">
                                                Bank name
                                            </td>
                                            <td colspan="5">
                                                ${merchantInfo.depositBank}
                                                <input type="hidden" name="detail.depositBank" value="${merchantInfo.depositBank}">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td style=" text-align: left">
                                                Bank Address
                                            </td>
                                            <td colspan="5">
                                                ${merchantInfo.bankAddress}
                                                <input type="hidden" name="detail.bankAddress" value="${merchantInfo.bankAddress}">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td style=" text-align: left">
                                                Account No.
                                            </td>
                                            <td colspan="5">
                                                ${merchantInfo.bankAccount}
                                                <input type="hidden" name="detail.bankAccount" value="${merchantInfo.bankAccount}">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td style=" text-align: left">
                                                SWIFT
                                            </td>
                                            <td colspan="5">
                                                ${merchantInfo.swiftCode}
                                                <input type="hidden" name="detail.swiftCode" value="${merchantInfo.swiftCode}">
                                            </td>
                                        </tr>

                                        <tr>
                                            <td>COMMODITY<br/>商品名称</td>
                                            <td>条形码</td>
                                            <td>ARTICLE NUMBER<br/>货号</td>
                                            <td>QUANTITY<br>数量（PCS）</td>
                                            <td>UNITY PRICE单价<br/>(${currency})</td>
                                            <td>AMOUNT总价<br/>(${currency})</td>
                                        </tr>

                                        <c:forEach items="${itemLists }" var="item">
                                            <c:choose>
                                                <c:when test="${item.totalAllAmount != null}">
                                                    <tr>
                                                        <td colspan="3" style="font-weight: bolder;">合计：</td>
                                                        <td>${item.totalAllNum }</td>
                                                        <input type="hidden" name="detail.totalAllNum" value="${item.totalAllNum}">
                                                        <td></td>
                                                        <td>${item.totalAllAmount }</td>
                                                        <input type="hidden" name="detail.totalAllAmount" value="${item.totalAllAmount}">
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <tr>
                                                        <td>${item.goodsName }</td>
                                                        <input type="hidden" name="goods.goodsName" value="${item.goodsName}">
                                                        <td>${item.barcode }</td>
                                                        <input type="hidden" name="goods.barcode" value="${item.barcode}">
                                                        <td>${item.goodsNo }</td>
                                                        <input type="hidden" name="goods.goodsNo" value="${item.goodsNo}">
                                                        <td>${item.totalNum }</td>
                                                        <input type="hidden" name="goods.totalNum" value="${item.totalNum}">
                                                        <td>${item.price }</td>
                                                        <input type="hidden" name="goods.price" value="${item.price}">
                                                        <td>${item.totalPrice }</td>
                                                        <input type="hidden" name="goods.totalPrice" value="${item.totalPrice}">
                                                    </tr>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>

                                        <tr>
                                            <td style="text-align: left;">
                                                1）Origin city 装货港：
                                            </td>
                                            <td colspan="5" style="text-align: left">
                                                <input type="text" id="originCity" name="detail.originCity" value="${invoice.originCity}" >
                                            </td>
                                        </tr>
                                        <tr>
                                            <td style="text-align: left;">
                                                2）Destination city 卸货港：
                                            </td>
                                            <td colspan="5" style="text-align: left">
                                                <input type="text" name="detail.destinationCity" value="${invoice.destinationCity}">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td style="text-align: left;">
                                                3）REMARKS 备注：
                                            </td>
                                            <td colspan="5" style="text-align: left">
                                                <input type="text" name="detail.remark" value="${invoice.remark}">
                                            </td>
                                        </tr>
                                    </table>
                                    <div class="form-row m-t-50">
                                        <div class="col-12">
                                            <div class="row">
                                                <div class="col-6">
                                                    <input type="button"
                                                           class="btn btn-info waves-effect waves-light fr btn_default"
                                                           id="confirm-buttons" value="确认开票">
                                                </div>
                                                <div class="col-6">
                                                    <input type="button"
                                                           class="btn btn-light waves-effect waves-light btn_default"
                                                           id="cancel-buttons" onclick="returnBack();" value="返回">
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
            </div>
        </form>
        <!-- container -->
    </div>
</div>
<!-- content -->
<script type="text/javascript">

    //返回
    function returnBack() {
        var invoiceSource = '${invoiceSource}';
        if (invoiceSource == '1') {
            $module.load.pageOrder("act=14001");
        } else {
            $module.load.pageOrder("act=14012");
        }
    };

    //保存按钮
    $("#confirm-buttons").click(function () {
        $custom.alert.warning("请确认开票信息，确认无误即开票！",function(){
            //检查必填参数
            if(!$checker.verificationRequired()){
                return ;
            }
            var ids = "${ids}";
            var tempId = "${tempId}";
            var url = serverAddr+"/receiveBill/modifyJsonContract.asyn";
            var json = getFormJson($("#edit-form")) ;
            json["invoiceSource"] = '${invoiceSource}';
            json = JSON.stringify(json);
            $custom.request.ajaxJson(url, json, function(result){
                if(result.state == '200'){
                    $custom.alert.success("编辑成功！");
                    returnBack();
                }else{
                    if(result.expMessage != null){
                        $custom.alert.error(result.expMessage);
                    }else{
                        $custom.alert.error(result.message);
                    }
                }
            });
        });
    });

    //导出按钮
    $("#export-buttons").click(function(){

        var codes = '${codes}' ;
        var codeArr = codes.split(',');
        var fileTempCode = '${fileTempCode}' ;
        var url = serverAddr+"/receiveBill/exportTempDateFile.asyn?orderCode=" + codeArr[0] + "&fileTempCode=" + fileTempCode;


        window.open(url);
    });

    function getFormJson(form) {
        var o = {};
        var a = $(form).serializeArray();
        $.each(a, function () {
            if (o[this.name] !== undefined) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {

                if(this.name.indexOf("goods.") > -1){
                    o[this.name] = new Array() ;
                    o[this.name].push(this.value || '');
                }else{
                    o[this.name] = this.value || '';
                }

            }
        });
        return o;
    }

    laydate.render({
        elem: '#invoiceDate',
        format: 'yyyy/MM/dd'
    });
</script>
