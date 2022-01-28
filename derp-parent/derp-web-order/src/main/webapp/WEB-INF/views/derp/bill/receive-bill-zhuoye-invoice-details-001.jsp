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
                                <input type="hidden" name="detail.codes" value="${codes}">
                                <input type="hidden" name="detail.ids" value="${ids}">
                                <input type="hidden" name="detail.tempId" value="${tempId}">
                                <input type="hidden" name="detail.fileTempCode" value="${fileTempCode}">
                                <input type="hidden" name="detail.currency" value="${currency}">
                                <input type="hidden" name="detail.customerId" value="${customerId}">
                                <input type="hidden" name="detail.customerName" value="${customerInfo.name}">
                                <div>
                                    <table style="text-align: center; border-collapse: collapse; border-spacing: 0px;">
                                        <colgroup>
                                            <col width="149" style="width:119px"/>
                                            <col width="181" style="width:145px"/>
                                            <col width="424" style="width:339px"/>
                                            <col width="51" style="width:41px"/>
                                            <col width="92" style="width:74px"/>
                                            <col width="112" style="width:90px"/>
                                        </colgroup>
                                        <tbody>
                                        <tr height="43" style="height:34px" class="firstRow">
                                            <td colspan="6"
                                                style="border-collapse: collapse; border-spacing: 0px;  padding: 0px 0.2em; height: 80px; font-size: 22px; word-break: break-all;">
                                                卓烨贸易有限公司
                                            </td>
                                        </tr>
                                        <tr height="45" style="height:36px">
                                            <td colspan="6"
                                                style="text-align:center;font-weight:700;font-size: 20px; padding-bottom: 20px; ">
                                                TWINKLE TRADING COMPANY LIMITED
                                            </td>
                                        </tr>
                                        <tr height="23" style="height:18px">
                                            <td height="18" style=""></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr height="35" style="height:28px">
                                            <td colspan="6"
                                                style="text-align:center;font-weight:700;font-size: 20px; padding-bottom: 20px; ">
                                                INVOICE
                                            </td>
                                        </tr>
                                        <tr height="23" style="height:18px">
                                            <td height="18" style=""></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr height="24" style="height:19px">
                                            <td height="19" style="text-align:left;">
                                                BILL TO:<span>&nbsp;</span>
                                            </td>
                                            <td colspan="2" style="text-align:left;">
                                                ${customerInfo.enName}
                                                <input type="hidden" name="detail.toCustomerName" value="${customerInfo.enName} ">
                                            </td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr height="23" style="height:18px">
                                            <td height="40" style="text-align:left;">
                                                Invoice Date:
                                            </td>
                                            <td colspan="2" style="text-align:left;">
                                                <input type="text" id="invoiceDate" name="detail.date"
                                                       value="<fmt:formatDate value="${invoiceDate}" pattern="yyyy/MM/dd"/>"
                                                       required reqMsg="Date不能为空！">
                                            </td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr height="23" style="height:18px">
                                            <td height="40" style="text-align:left;">
                                                Invoice No.:
                                            </td>
                                            <td colspan="2" style="text-align:left;">

                                            </td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr height="23" style="height:18px">
                                            <td height="40" style="text-align:left;">
                                                Payment Period:
                                            </td>
                                            <td colspan="2" style="text-align:left;">
                                                <input type="text" name="detail.paymentPeriod"
                                                       value="${invoice.paymentPeriod}" >
                                            </td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr height="23" style="height:18px">
                                            <td height="18" style=""></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr height="78" style="height:63px;">
                                            <td colspan="2" height="63"
                                                style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);text-align:center;">
                                                BARCODE
                                            </td>
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);text-align:center;">
                                                DESCRIPTION
                                            </td>
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);text-align:center;">
                                                QTY
                                            </td>
                                            <td width="74"
                                                style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);text-align:center;">
                                                <span>UNIT <br/> PRICE</span><br/><span>（${currency}）</span>
                                            </td>
                                            <td width="90"
                                                style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);text-align:center;">
                                                <span>TOTAL AMOUNT</span><span>（${currency}）</span>
                                            </td>
                                        </tr>
                                        <c:forEach items="${itemLists }" var="item">
                                            <c:choose>
                                                <c:when test="${item.totalAllNum != null}">
                                                    <tr height="40" style="height:32px">
                                                        <td colspan="3" style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                            TOTAL
                                                        </td>
                                                        <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);text-align:right;">
                                                            <span>&nbsp;${item.totalAllNum }</span>
                                                            <input type="hidden" name="detail.totalAllNum" value="${item.totalAllNum}"><span>&nbsp;</span>
                                                        </td>
                                                        <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                        </td>
                                                        <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);text-align:right;">
                                                            <span>&nbsp;${item.totalAllAmount }</span>
                                                            <input type="hidden" name="detail.totalAllAmount" value="${item.totalAllAmount}"><span>&nbsp;</span>
                                                        </td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <tr height="40" style="height:32px">
                                                        <td colspan="2" height="32" width="119"
                                                            style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);text-align:left;">
                                                            ${item.barcode }
                                                            <input type="hidden" name="goods.barcode" value="${item.barcode}">
                                                        </td>
                                                        <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);text-align:left;">
                                                            ${item.goodsName }
                                                            <input type="hidden" name="goods.goodsName" value="${item.goodsName}">
                                                        </td>
                                                        <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);text-align:right;">
                                                            ${item.totalNum }
                                                            <input type="hidden" name="goods.totalNum" value="${item.totalNum}">
                                                        </td>
                                                        <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);text-align:right;">
                                                            <span>&nbsp;</span>${item.price }
                                                            <input type="hidden" name="goods.price" value="${item.price}"><span>&nbsp;</span>
                                                        </td>
                                                        <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);text-align:right;">
                                                            <span>&nbsp;</span>${item.totalPrice }
                                                            <input type="hidden" name="goods.totalPrice" value="${item.totalPrice}"><span>&nbsp;&nbsp;&nbsp;</span>
                                                        </td>
                                                    </tr>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                        <tr height="23" style="height:18px">
                                            <td height="18" style=""></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr height="24" style="height:19px">
                                            <td height="19" style="text-align:left;">
                                                Beneficiary Name
                                            </td>
                                            <td colspan="2" style="text-align:left;">
                                                ${merchantInfo.englishName}
                                                <input type="hidden" name="detail.englishName" value="${merchantInfo.englishName}">
                                            </td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr height="24" style="height:19px">
                                            <td height="19" style="text-align:left;">
                                                Beneficiary Bank
                                            </td>
                                            <td colspan="2" style="text-align:left;">
                                                ${merchantInfo.depositBank}
                                                <input type="hidden" name="detail.depositBank" value="${merchantInfo.depositBank}">
                                            </td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr height="24" style="height:19px">
                                            <td height="19" style="text-align:left;">
                                                Account Number
                                            </td>
                                            <td colspan="2" style="text-align:left;">
                                                ${merchantInfo.bankAccount}
                                                <input type="hidden" name="detail.bankAccount" value="${merchantInfo.bankAccount}">
                                            </td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr height="23" style="height:18px">
                                            <td height="18" style="text-align:left;">
                                                Bank Code
                                            </td>
                                            <td colspan="2" style="text-align:left;">
                                                004
                                            </td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr height="24" style="height:19px">
                                            <td height="19" style="text-align:left;">
                                                Swift Code
                                            </td>
                                            <td colspan="2" style="text-align:left;">
                                                ${merchantInfo.swiftCode}
                                                <input type="hidden" name="detail.swiftCode" value="${merchantInfo.swiftCode}">
                                            </td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr height="24" style="height:19px">
                                            <td height="19" style="text-align:left;">
                                                Bank Add
                                            </td>
                                            <td colspan="2" style="text-align:left;">
                                                ${merchantInfo.bankAddress}
                                                <input type="hidden" name="detail.bankAddress" value="${merchantInfo.bankAddress}">
                                            </td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr height="23" style="height:18px">
                                            <td height="18" style=""></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr height="23" style="height:18px">
                                            <td height="18" style=""></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr height="23" style="height:18px">
                                            <td height="18" style=""></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr height="26" style="height:21px">
                                            <td height="21" style=""></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td colspan="2" style="text-align:right">
                                                For and on behalf of
                                            </td>
                                        </tr>
                                        <tr height="26" style="height:21px">
                                            <td height="21" style=""></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td width="90" style=""></td>
                                        </tr>
                                        <tr height="26" style="height:21px">
                                            <td height="21" style=""></td>
                                            <td></td>
                                            <td colspan="4" style="text-align:right">
                                                TWINKLE TRADING COMPANY LIMITED
                                            </td>
                                        </tr>
                                        <tr height="23" style="height:18px">
                                            <td height="18" style=""></td>
                                            <td colspan="5" style="text-align:right">
                                                Add: Unit 1302, 13/F., Hua Fu Commercial Building, 111 Queen’s Road
                                                West, HONG KONG
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

    laydate.render({
        elem: '#invoiceDate',
        format: 'yyyy/MM/dd'
    });
</script>
