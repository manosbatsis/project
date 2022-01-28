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

                                    <table style="text-align: center; border-collapse: collapse;">
                                        <tbody>

                                        <tr style="width:630px; border-collapse: collapse; font-size: 12px;">
                                            <td colspan="6"
                                                style="text-align: right;border-collapse: collapse; border-spacing: 0; padding: 3px;">
                                                Invoice No.：
                                            </td>
                                            <td style="border-collapse: collapse; border-spacing: 0;padding: 3px; text-align: left;">
                                                ${invoiceNo}
                                            </td>
                                        </tr>
                                        <tr style="width:630px; border-collapse: collapse;font-size: 12px;">
                                            <td colspan="6"
                                                style="text-align: right;border-collapse: collapse; border-spacing: 0; padding: 3px;">
                                                Date：
                                            </td>
                                            <td style="border-collapse: collapse; border-spacing: 0;padding: 3px;text-align: left;">
                                                <input type="text" id="invoiceDate" name="detail.date"
                                                       value="<fmt:formatDate value="${invoiceDate}" pattern="yyyy/MM/dd"/>"
                                                       required reqMsg="Date不能为空！"></span>
                                            </td>
                                        </tr>
                                        <tr style="width:630px; border-collapse: collapse;font-size: 12px;">
                                            <td colspan="6"
                                                style="text-align: right;border-collapse: collapse; border-spacing: 0; padding: 3px;">
                                                Contract No.：
                                            </td>
                                            <td style="border-collapse: collapse; border-spacing: 0;padding: 3px;text-align: left;overflow-wrap: break-word; word-break: break-all;">
                                                ${poNos}
                                                <input type="hidden" name="detail.poNos"  value="${poNos}" >
                                            </td>
                                        </tr>
                                        <tr style="width:630px; border-collapse: collapse;font-size: 12px;">
                                            <td colspan="6"
                                                style="text-align: right;border-collapse: collapse; border-spacing: 0; padding: 3px;">
                                                Invoice No (buyer)：
                                            </td>
                                            <td style="border-collapse: collapse; border-spacing: 0;padding: 3px;text-align: left;overflow-wrap: break-word; word-break: break-all;">
                                                <input type="text" name="detail.buyerInvoice"  >
                                            </td>
                                        </tr>
                                        <tr style="text-align:center;width: 630px;font-weight: bolder;"
                                            class="firstRow">
                                            <td colspan="6"
                                                style="border-collapse: collapse; border-spacing: 0px;  padding: 20px 0.2em; height: 80px; font-size: 18px;word-break: break-all;">
                                                INVOICE 发票
                                            </td>
                                        </tr>
                                        <tr style="width:630px; border-collapse: collapse;font-size: 12px;">
                                            <td colspan="2" rowspan="2"
                                                style="text-align: center;  padding-top: 10px;font-weight: bolder;border: 1px solid #000000;">
                                                SELLERS:
                                            </td>
                                            <td colspan="5" style="text-align: left;  padding-top: 10px;border: 1px solid #000000;">
                                                ${merchantInfo.englishName}
                                                <input type="hidden" name="detail.merchantEnName" value="${merchantInfo.englishName}">
                                            </td>
                                        </tr>
                                        <tr style="width:630px; border-collapse: collapse;font-size: 12px;border: 1px solid #000000;">
                                            <td colspan="5" style="text-align: left;  padding-top: 10px;border: 1px solid #000000;">
                                                    ${merchantInfo.englishRegisteredAddress}
                                                <input type="hidden" name="detail.englishRegisteredAddress" value="${merchantInfo.englishRegisteredAddress}">
                                            </td>
                                        </tr>
                                        <tr style="width:630px; border-collapse: collapse;font-size: 12px;">
                                            <td colspan="2" rowspan="2"
                                                style="text-align: center;  padding-top: 10px;font-weight: bolder;border: 1px solid #000000;">
                                                BUYER:
                                            </td>
                                            <td colspan="5" style="text-align: left;  padding-top: 10px;border: 1px solid #000000;">
                                                ${customerInfo.enName}
                                                <input type="hidden" name="detail.customerEnName" value="${customerInfo.enName}">
                                            </td>

                                        </tr>
                                        <tr style="width:630px; border-collapse: collapse;font-size: 12px;">
                                            <td colspan="5" style="text-align: left;  padding-top: 10px;border: 1px solid #000000;">
                                                ${customerInfo.companyAddr}
                                                <input type="hidden" name="detail.companyAddr" value="${customerInfo.companyAddr}">
                                            </td>
                                        </tr>
                                        <tr style="width:630px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);font-size: 12px;">
                                            <td style="text-align: center;border-collapse: collapse; border-spacing: 0; border: 1px solid #000000;padding: 3px;width: 20px;">
                                                Item
                                            </td>
                                            <td style="text-align: center;border-collapse: collapse; border-spacing: 0; border: 1px solid #000000;padding: 3px;width: 100px;">
                                                Commodity item<br>商品货号
                                            </td>
                                            <td style="text-align: center;border-collapse: collapse; border-spacing: 0; border: 1px solid #000000;padding: 3px;width: 180px;">
                                                Commodity & Specificaton<br>货物名称及规格(商品上架中文品名)
                                            </td>
                                            <td style="text-align: center;border-collapse: collapse; border-spacing: 0; border: 1px solid #000000;padding: 3px;width: 80px;">
                                                Quantity<br>数 量
                                            </td>
                                            <td style="text-align: center;border-collapse: collapse; border-spacing: 0; border: 1px solid #000000;padding: 3px;width: 60px;">
                                                Unit<br>单位
                                            </td>
                                            <td style="text-align: center;border-collapse: collapse; border-spacing: 0; border: 1px solid #000000;padding: 3px;width: 120px;">
                                                Unit price<br>单价（${currency}）
                                            </td>
                                            <td style="text-align: center;border-collapse: collapse; border-spacing: 0; border: 1px solid #000000;padding: 3px;">
                                                Total Amount<br>总价（${currency}）
                                            </td>
                                        </tr>
                                        <c:forEach items="${itemLists }" var="item">
                                            <c:choose>
                                                <c:when test="${item.totalAllAmount != null}">
                                                    <tr style="width:630px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);font-size: 12px;">
                                                        <td colspan="3"
                                                            style="font-weight: bolder;border-collapse: collapse; border-spacing: 0; border: 1px solid #000000;padding: 3px;">
                                                            TOTAL
                                                        </td>
                                                        <td style="text-align: right;border-collapse: collapse; border-spacing: 0; border: 1px solid #000000;padding: 3px;">
                                                            ${item.totalAllNum }
                                                            <input type="hidden" name="detail.totalAllNum" value="${item.totalAllNum}">
                                                        </td>
                                                        <td colspan="2" style="border-collapse: collapse; border-spacing: 0; border: 1px solid #000000;padding: 3px;"></td>
                                                        <td style="text-align: right;border-collapse: collapse; border-spacing: 0; border: 1px solid #000000;padding: 3px;">
                                                            ${item.totalAllAmount }
                                                            <input type="hidden" name="detail.totalAllAmount"  value="${item.totalAllAmount}" >
                                                        </td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <tr style="width:630px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);font-size: 12px;">
                                                        <td style="text-align: left;border-collapse: collapse; border-spacing: 0; border: 1px solid #000000;padding: 3px;width: 20px;">
                                                            ${item.index }
                                                            <input type="hidden" name="goods.index" value="${item.index}">
                                                        </td>
                                                        <td style="text-align: left;border-collapse: collapse; border-spacing: 0; border: 1px solid #000000;padding: 3px;width: 100px;">
                                                            <input type="text" name="goods.goodsNo" value="${item.goodsNo}" >
                                                        </td>
                                                        <td style="text-align: left;border-collapse: collapse; border-spacing: 0; border: 1px solid #000000;padding: 3px;width: 180px;">
                                                            <input type="text" name="goods.goodsName" value="${item.goodsName}" class="requestName">
                                                        </td>
                                                        <td style="text-align: right;border-collapse: collapse; border-spacing: 0; border: 1px solid #000000;padding: 3px;width: 80px;">
                                                            ${item.totalNum }
                                                            <input type="hidden" name="goods.totalNum" value="${item.totalNum}">
                                                        </td>
                                                        <td style="text-align: right;border-collapse: collapse; border-spacing: 0; border: 1px solid #000000;padding: 3px;width: 60px;">
                                                            <input type="text" name="goods.unit" value="${item.unit}">
                                                        </td>
                                                        <td style="text-align: right;border-collapse: collapse; border-spacing: 0; border: 1px solid #000000;padding: 3px;width: 120px;">
                                                            ${item.price }
                                                            <input type="hidden" name="goods.price" value="${item.price}">
                                                        </td>
                                                        <td style="text-align: right;border-collapse: collapse; border-spacing: 0; border: 1px solid #000000;padding: 3px;">
                                                            ${item.totalPrice }
                                                            <input type="hidden" name="goods.totalPrice" value="${item.totalPrice}">
                                                        </td>
                                                    </tr>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                        <tr style="width:630px; border-collapse: collapse;font-size: 12px;">
                                            <td colspan="7" style="text-align: left;  padding-top: 20px;">
                                                <div style="width: 130px; text-align: left;display:inline-block;">
                                                    （7）Payment Terms:
                                                </div>
                                                <div style="display:inline-block;">
                                                    <input type="text" name="detail.paymentEN" value="T/T">
                                                    <span style="padding-left: 20px;">付款条件：</span>
                                                    <input type="text" name="detail.paymentCN" value="电汇">
                                                </div>
                                            </td>
                                        </tr>
                                        <tr style="width:630px; border-collapse: collapse;font-size: 12px;">
                                            <td colspan="7" style="text-align: left;  padding-top: 10px;">
                                                <div style="width: 130px; text-align: left;display:inline-block;">
                                                    （8）Incoterms:
                                                </div>
                                                <div style="display:inline-block;">
                                                    <input type="text" name="detail.incotermsEN" value="CIF  guangzhou">
                                                    <span style="padding-left: 20px;">贸易方式：</span>
                                                    <input type="text" name="detail.incotermsCN" value="CIF 广州">
                                                </div>
                                            </td>
                                        </tr>
                                        <tr style="width:630px; border-collapse: collapse;font-size: 12px;">
                                            <td colspan="7" style="text-align: left;  padding-top: 10px;">
                                                <div style="width: 130px; text-align: left;display:inline-block;">
                                                    （9）Place of Origin:
                                                </div>
                                                <div style="display:inline-block;">
                                                    <input type="text" name="detail.palceEN" value="Hong Kong">
                                                    <span style="padding-left: 20px;">原产地：</span>
                                                    <input type="text" name="detail.palceCN" value="香港">
                                                </div>
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
            var check = true;
            $('.requestName').each(function(index,ele) {
                var val = $(ele).val();
                if (!val) {
                    $custom.alert.error("货物名称及规格不能为空！");
                    check = false;
                    return;
                }
            });
            if (check) {
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
            }
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
