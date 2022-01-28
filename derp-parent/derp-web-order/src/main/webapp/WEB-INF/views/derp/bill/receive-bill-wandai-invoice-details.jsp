<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common.jsp"/>
<style type="text/css">
    table {
        border-collapse: collapse;
        border-spacing: 0;
    }

    td, th {
        padding: 0;
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
                                <input type="hidden" name="detail.codes" value="${codes}">
                                <input type="hidden" name="detail.ids" value="${ids}">
                                <input type="hidden" name="detail.tempId" value="${tempId}">
                                <input type="hidden" name="detail.fileTempCode" value="${fileTempCode}">
                                <input type="hidden" name="detail.currency" value="${currency}">
                                <input type="hidden" name="detail.currencyLabel" value="${currencyLabel}">
                                <input type="hidden" name="detail.customerId" value="${customerId}">
                                <input type="hidden" name="detail.customerName" value="${customerInfo.name}">
                                <div>

                                    <table style="text-align: center; border-collapse: collapse; border-spacing: 0px;word-wrap: break-word; word-break: break-all; width: 680px;">
                                        <tbody>
                                        <tr style="height:33px" class="firstRow">
                                            <td colspan="7" style="font-weight: bold;border-collapse: collapse; border-spacing: 0px;  padding: 0px 0.2em; height: 40px; font-size: 18px; word-break: break-all;">
                                                COMMERCIAL INVOICE  商 业 发 票
                                            </td>
                                        </tr>

                                        <tr style="height:16px">
                                            <td style="width: 50px;"></td>
                                            <td style="width: 290px;"></td>
                                            <td style="width: 60px;"></td>
                                            <td style="width: 80px;"></td>
                                            <td style="width: 40px;"></td>
                                            <td style="width: 80px;"></td>
                                            <td style="width: 80px;"></td>
                                        </tr>
                                        <tr style="height:33px">
                                            <td colspan="2" style="text-align:left;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);font-size: 12px;padding-left: 4px; width: 340px;">
                                                DATE  日期:
                                                <input type="text" id="invoiceDate" name="detail.date"
                                                                 value="<fmt:formatDate value="${invoiceDate}" pattern="yyyy/MM/dd"/>"
                                                                 required reqMsg="Date不能为空！"></span>
                                            </td>
                                            <td colspan="2" style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);border-right: 1px solid rgb(0, 0, 0);font-size: 12px; width: 140px;">
                                                INVOICE NO. 发票号
                                            </td>
                                            <td colspan="3" style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);border-right: 1px solid rgb(0, 0, 0);font-size: 12px; width: 200px;">
                                                ${invoiceNo}
                                            </td>
                                        </tr>
                                        <tr style="height:33px">
                                            <td colspan="2" style="text-align:left;border-collapse: collapse; border: 1px solid rgb(0, 0, 0); font-size: 10px; padding-left: 4px;">
                                                SHIPPER/EXPORTER (Complete name & address & telephone)<br>发货人或出口商的详细名称、地址及电话
                                            </td>
                                            <td colspan="5" style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);border-right: 1px solid rgb(0, 0, 0); font-size: 12px;">
                                                CONSIGNEE(Complete name & address & telephone)<br>收货人的详细名称、地址及电话
                                            </td>
                                        </tr>
                                        <tr style="height:33px">
                                            <td colspan="2" style="text-align:left;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);font-size: 11px;padding-left: 4px;padding-bottom: 10px;">
                                                From: ${merchantInfo.englishName}
                                                <input type="hidden" name="detail.merchantEnName" value="${merchantInfo.englishName}"><br><br>
                                                联系人：<input type="text" name="detail.fContactPerson" required reqMsg="联系人不能为空！">
                                            </td>
                                            <td colspan="5" style="text-align:left;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);border-right: 1px solid rgb(0, 0, 0);font-size: 11px;padding-bottom: 10px;">
                                                TO:${customerInfo.name}(${customerInfo.enName})
                                                <input type="hidden" name="detail.customerAllName" value="${customerInfo.name}(${customerInfo.enName})"><br><br>
                                                联系人：<input type="text" name="detail.tContactPerson" required reqMsg="联系人不能为空！">
                                            </td>
                                        </tr>
                                        <tr style="height:20px">
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);font-size: 11px; width: 50px;">
                                                ITEM NO.
                                            </td>
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);font-size: 11px;width: 290px;">
                                                DESCRIPTION OF GOODS
                                            </td>
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);font-size: 11px; width: 60px;">
                                                Bar Code
                                            </td>
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);font-size: 11px; width: 80px;">
                                                QTY<br/>(PIECE)
                                            </td>
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);font-size: 12px; width: 40px;">
                                                UNIT
                                            </td>
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);font-size: 11px; width: 80px;">
                                                UNIT PRICE
                                            </td>
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);font-size: 11px;width: 80px;">
                                                TOTAL VALUE
                                            </td>
                                        </tr>
                                        <tr style="height:20px">
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);font-size: 12px; width: 50px;">
                                                项号
                                            </td>
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);font-size: 12px;width: 250px;">
                                                货品详细描述
                                            </td>
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);font-size: 12px; width: 40px;">
                                                条码
                                            </td>
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);font-size: 12px; width: 80px;">
                                                数量
                                            </td>
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);font-size: 12px; width: 40px;">
                                                单位
                                            </td>
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);font-size: 12px; width: 80px;">
                                                单价（${currency}）
                                            </td>
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);font-size: 12px;width: 80px;">
                                                总价（${currency}）
                                            </td>
                                        </tr>
                                        <c:forEach items="${itemLists }" var="item">
                                            <c:choose>
                                                <c:when test="${item.totalAllAmount != null}">
                                                    <tr style="height:20px">
                                                        <td colspan="3" style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);font-size: 12px;">
                                                            TOTAL
                                                        </td>
                                                        <td colspan="1" style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);word-break: break-all; word-wrap:break-word;font-size: 12px;padding-right: 5px;">
                                                            ${totalAllNum}
                                                            <input type="hidden" name="detail.totalAllNum" value="${item.totalAllNum}">
                                                        </td>
                                                        <td colspan="2" style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);"></td>
                                                        <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);word-break: break-all; word-wrap:break-word;font-size: 12px;padding-right: 5px;">
                                                            ${totalAllAmount}
                                                            <input type="hidden" name="detail.totalAllAmount"  value="${item.totalAllAmount}" >
                                                        </td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <tr style="height:20px">
                                                        <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);font-size: 12px; width: 50px;">
                                                            ${item.index}
                                                            <input type="hidden" name="goods.index" value="${item.index}">
                                                        </td>
                                                        <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);font-size: 12px;width: 250px;">
                                                            ${item.goodsName}
                                                            <input type="hidden" name="goods.goodsName" value="${item.goodsName}">
                                                        </td>
                                                        <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);font-size: 12px; width: 40px;">
                                                            ${item.barcode}
                                                            <input type="hidden" name="goods.barcode" value="${item.barcode}">
                                                        </td>
                                                        <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);font-size: 12px; width: 80px;">
                                                            ${item.totalNum}
                                                            <input type="hidden" name="goods.totalNum" value="${item.totalNum}">
                                                        </td>
                                                        <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);font-size: 12px; width: 40px;">
                                                            ${item.unit}
                                                            <input type="hidden" name="goods.unit" value="${item.unit}">
                                                        </td>
                                                        <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);font-size: 12px; width: 80px;">
                                                            ${item.price}
                                                            <input type="hidden" name="goods.price" value="${item.price}">
                                                        </td>
                                                        <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);font-size: 12px;width: 80px;">
                                                            ${item.totalPrice}
                                                            <input type="hidden" name="goods.totalPrice" value="${item.totalPrice}">
                                                        </td>
                                                    </tr>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                        <tr style="height:16px">
                                            <td style="width: 50px;"></td>
                                            <td style="width: 290px;"></td>
                                            <td style="width: 60px;"></td>
                                            <td style="width: 80px;"></td>
                                            <td style="width: 40px;"></td>
                                            <td style="width: 80px;"></td>
                                            <td style="width: 80px;"></td>
                                        </tr>
                                        <tr style="height:16px">
                                            <td style="width: 50px;"></td>
                                            <td style="width: 290px;"></td>
                                            <td style="width: 60px;"></td>
                                            <td style="width: 80px;"></td>
                                            <td style="width: 40px;"></td>
                                            <td style="width: 80px;"></td>
                                            <td style="width: 80px;"></td>
                                        </tr>
                                        <tr style="height:23px">
                                            <td colspan="7" style="text-align: left;font-size: 12px;">
                                                Bank Account No.:${merchantInfo.bankAccount}
                                                <input type="hidden" name="detail.bankAccount" value="${merchantInfo.bankAccount}">
                                            </td>
                                        </tr>
                                        <tr style="height:23px">
                                            <td colspan="7" style="text-align: left;font-size: 12px;">
                                                Beneficiary Name：${merchantInfo.beneficiaryName}
                                                <input type="hidden" name="detail.beneficiaryName" value="${merchantInfo.beneficiaryName}">
                                            </td>
                                        </tr>
                                        <tr style="height:23px">
                                            <td colspan="7" style="text-align: left;font-size: 12px;">
                                                Swift Code：${merchantInfo.swiftCode}
                                                <input type="hidden" name="detail.swiftCode" value="${merchantInfo.swiftCode}">
                                            </td>
                                        </tr>
                                        <tr style="height:23px">
                                            <td colspan="7" style="text-align: left;font-size: 12px;">
                                                Bank Name：${merchantInfo.depositBank}
                                                <input type="hidden" name="detail.depositBank" value="${merchantInfo.depositBank}">
                                            </td>
                                        </tr>
                                        </tbody>
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

    $(function () {
        laydate.render({
            elem: '#invoiceDate',
            format: 'yyyy/MM/dd'
        });
        sumTotal();
    });

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
        $custom.alert.warning("请确认开票信息，确认无误即开票！", function () {
            //检查必填参数
            if (!$checker.verificationRequired()) {
                return;
            }
            var ids = "${ids}";
            var tempId = "${tempId}";
            var url = serverAddr + "/receiveBill/modifyJsonContract.asyn";
            var json = getFormJson($("#edit-form"));
            json["invoiceSource"] = '${invoiceSource}';
            json = JSON.stringify(json);
            $custom.request.ajaxJson(url, json, function (result) {
                if (result.state == '200') {
                    $custom.alert.success("编辑成功！");
                    returnBack();
                } else {
                    if (result.expMessage != null) {
                        $custom.alert.error(result.expMessage);
                    } else {
                        $custom.alert.error(result.message);
                    }
                }
            });
        });
    });

    //导出按钮
    $("#export-buttons").click(function () {

        var codes = '${codes}';
        var codeArr = codes.split(',');
        var fileTempCode = '${fileTempCode}';
        var url = serverAddr + "/receiveBill/exportTempDateFile.asyn?orderCode=" + codeArr[0] + "&fileTempCode=" + fileTempCode;
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

                if (this.name.indexOf("goods.") > -1) {
                    o[this.name] = new Array();
                    o[this.name].push(this.value || '');
                } else {
                    o[this.name] = this.value || '';
                }

            }
        });
        return o;
    }

    //金额转大写
    function toCnMoney(num) {
        var symbol = "";//正负值标记
        if (!/^-?(0|[1-9]\d*)(\.\d+)?$/.test(num)) {
            return "数据非法";
        }
        if (num < 0) {
            num = -num;
            symbol = "负";
        }
        var strOutput = "";
        var strUnit = '仟佰拾亿仟佰拾万仟佰拾元角分';
        num += "00";
        var intPos = num.indexOf('.');
        if (intPos >= 0)
            num = num.substring(0, intPos) + num.substr(intPos + 1, 2);
        strUnit = strUnit.substr(strUnit.length - num.length);
        for (var i = 0; i < num.length; i++)
            strOutput += '零壹贰叁肆伍陆柒捌玖'.substr(num.substr(i, 1), 1) + strUnit.substr(i, 1);
        return symbol + strOutput.replace(/零角零分$/, '整').replace(/零[仟佰拾]/g, '零').replace(/零{2,}/g, '零').replace(/零([亿|万])/g, '$1').replace(/零+元/, '元').replace(/亿零{0,3}万/, '亿').replace(/^元/, "零元");
    }

    $("#prepayments").bind("input propertychange", function (event) {
        var val = $('#prepayments').val();
        $('#prepaymentTotalSpan').text(val);
        $('#prepaymentTotal').val(val);
        sumTotal();
    });

    $("#adjustment").bind("input propertychange", function (event) {
        var val = $('#adjustment').val();
        $('#adjustmentTotalSpan').text(val);
        $('#adjustmentTotal').val(val);
        var valLabel = toCnMoney(val)
        $('#adjustmentTotalLSpan').text(valLabel);
        $('#adjustmentTotalLabel').val(valLabel);
        sumTotal();
    });

    $("#subsidy").bind("input propertychange", function (event) {
        var val = $('#subsidy').val();
        $('#subsidySpan').text(val);
        $('#subsidyTotal').val(val);
        var valLabel = toCnMoney(val)
        $('#subsidyTotalLSpan').text(valLabel);
        $('#subsidyTotalLabel').val(valLabel);
        sumTotal();
    });

    $("#proportion").bind("input propertychange", function (event) {
        var val = $('#proportion').val();
        var reg = /^((\d+\.?\d*)|(\d*\.\d+))\%$/;
        if (!reg.test(val)) {
            $("#proportion").val('');
            $custom.alert.error('首次付款比例不是百分比格式');
        } else {
            sumTotal();
        }
    });

    //计算首次付款金额
    function sumTotal() {
        var val = $('#proportion').val();
        if (val) {
            var totalAllPrice = $('#totalAllPrice').val();
            var prepaymentTotal = $('#prepaymentTotal').val();
            var deductionTotal = $('#deductionTotal').val();
            var adjustmentTotal = $('#adjustmentTotal').val();
            var subsidyTotal = $('#subsidyTotal').val();
            var total = parseNum(totalAllPrice) + parseNum(prepaymentTotal) + parseNum(deductionTotal) +
                parseNum(adjustmentTotal) + parseNum(subsidyTotal);
            var totalAmount = (total * val.replace("%", "") / 100).toFixed(2);
            $('#proportionPrice').text(totalAmount);
            $('#proportionTotalPrice').val(totalAmount);
            var valLabel = toCnMoney(totalAmount)
            $('#proportionPriceLSpan').text(valLabel);
            $('#proportionPriceLabel').val(valLabel);
        }
    }

    function parseNum(num) {
        if (num) {
            return parseFloat(num);
        }
        return 0;
    }
</script>
