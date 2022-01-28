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
                                    <table style="text-align: center; border-collapse: collapse; overflow-wrap: break-word; word-break: break-all; width: 680px; table-layout: fixed;">
                                        <colgroup>
                                            <col style="width:230px"/>
                                            <col style="width:80px"/>
                                            <col style="width:100px"/>
                                            <col style="width:120px"/>
                                            <col style="width:150px"/>
                                            <col/>
                                        </colgroup>
                                        <tbody>
                                        <tr style="height:34px" class="firstRow" height="43">
                                            <td colspan="5"
                                                style="border-collapse: collapse; border-spacing: 0px;  padding: 0px 0.2em; height: 40px; font-size: 18px; word-break: break-all;">
                                                <input type="hidden" name="detail.merchantInvoiceName" value="${merchantInfo.fullName} ">
                                                <strong>${merchantInfo.fullName}</strong>
                                            </td>
                                        </tr>
                                        <tr style="height:36px">
                                            <td colspan="5"
                                                style="text-align:center;font-weight:700;font-size: 18px; padding-bottom: 20px; ">
                                                <input type="hidden" name="detail.englishName" value="${merchantInfo.englishName}">
                                                ${merchantInfo.englishName}
                                            </td>
                                        </tr>
                                        <tr style="height:28px">
                                            <td colspan="5"
                                                style="text-align: center; font-weight: 700; font-size: 18px; padding-bottom: 20px; word-break: break-all;text-decoration:underline;">
                                                INVOICE
                                            </td>
                                        </tr>
                                        <tr style="height:19px; ">
                                            <td colspan="6"
                                                style="text-align:left;word-break: break-all; word-wrap:break-word;font-size: 12px;">
                                                <div><strong>MESSRS:</strong>${customerInfo.name}</div>
                                            </td>
                                        </tr>
                                        <tr style="height:18px">
                                            <td colspan="6"
                                                style="text-align:left;word-break: break-all; word-wrap:break-word;font-size: 12px;">
                                                <input type="hidden" name="detail.customerCompanyAddr" value="${customerInfo.companyAddr}">
                                                <div>${customerInfo.companyAddr}</div>
                                            </td>
                                        </tr>
                                        <tr style="height:18px">
                                            <td colspan="6"
                                                style="text-align:left;word-break: break-all; word-wrap:break-word;font-size: 12px;">
                                                <div>ATTN:<input type="text" name="detail.attn" required reqMsg="ATTN不能为空！"></div>
                                            </td>
                                        </tr>
                                        <tr style="height:18px">
                                            <td colspan="6"
                                                style="text-align:left;word-break: break-all; word-wrap:break-word;font-size: 12px;">
                                                <div>Invoice Date:<input type="text" id="invoiceDate" name="detail.date"
                                                                         value="<fmt:formatDate value="${invoiceDate}" pattern="yyyy/MM/dd"/>"
                                                                         required reqMsg="Date不能为空！"></div>
                                            </td>
                                        </tr>
                                        <tr style="height:18px">
                                            <td colspan="6"
                                                style="text-align:left;word-break: break-all; word-wrap:break-word;font-size: 12px;">
                                                <div>Invoice No.:</div>
                                            </td>
                                        </tr>
                                        <tr style="height:18px">
                                            <td colspan="6"
                                                style="text-align:left;word-break: break-all; word-wrap:break-word;font-size: 12px;">
                                                <div>PO No.:<input type="text" name="detail.poNo" ></div>
                                            </td>
                                        </tr>
                                        <tr style="height:18px">
                                            <td style="" height="18"></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr style="height:18px">
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);text-align:center;font-size: 12px;">
                                                DESCRIPTION
                                            </td>
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);text-align:center;font-size: 12px;">
                                                QUANTITY
                                            </td>
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);text-align:center;font-size: 12px;">
                                                UNIT PRICE
                                            </td>
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);text-align:center;font-size: 12px;"
                                                width="74">
                                                <span>TOTAL AMOUNT</span><br/><span>CNY</span>
                                            </td>
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);text-align:center;font-size: 12px;"
                                                width="90">
                                                <span>TOTAL AMOUNT</span><br/><span>(${currency})</span>
                                            </td>
                                        </tr><!--#liststart-->
                                        <c:forEach items="${itemLists }" var="item">
                                            <c:choose>
                                                <c:when test="${item.totalAllNum != null}">
                                                    <tr style="height:20px;">
                                                        <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);font-size: 12px;">
                                                            TOTAL
                                                        </td>
                                                        <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);"></td>
                                                        <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);"></td>
                                                        <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);"></td>
                                                        <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);text-align:right;word-break: break-all; word-wrap:break-word;font-size: 12px;padding-right: 5px;">
                                                            ${totalAllAmount}
                                                            <input type="hidden" name="detail.totalAllAmount" value="${item.totalAllAmount}"><span>&nbsp;</span>
                                                        </td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <tr>
                                                        <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);word-break: break-all; word-wrap:break-word;font-size: 12px;padding-left: 5px;"
                                                            width="119" height="20px">
                                                                <input type="text" name="goods.goodsName" value="${item.goodsName}">
                                                        </td>
                                                        <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);word-break: break-all; word-wrap:break-word;font-size: 12px;padding-left: 5px;">
                                                                ${item.totalNum }
                                                                <input type="hidden" name="goods.totalNum" value="${item.totalNum}">
                                                        </td>
                                                        <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);word-break: break-all; word-wrap:break-word;font-size: 12px;padding-right: 5px;">
                                                                ${item.price }
                                                                <input type="hidden" name="goods.price" value="${item.price}">
                                                        </td>
                                                        <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);word-break: break-all; word-wrap:break-word;font-size: 12px;padding-right: 5px;">
                                                        </td>
                                                        <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);text-align:right;word-break: break-all; word-wrap:break-word;font-size: 12px;padding-right: 5px;">
                                                                ${item.totalPrice }
                                                                <input type="hidden" name="goods.totalPrice" value="${item.totalPrice}">
                                                        </td>
                                                    </tr>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>

                                        <tr style="height:40px">
                                            <td style="" height="18"></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr style="height:20px">
                                            <td colspan="5" style="text-align:left;font-size: 12px;" height="19">
                                                <div style="width: 120px; text-align: left;display:inline-block;">
                                                    Beneficiary Name
                                                </div>
                                                <div style="display:inline-block;">
                                                    <input type="hidden" name="detail.beneficiaryName" value="${merchantInfo.beneficiaryName}">
                                                    ${merchantInfo.beneficiaryName}
                                                </div>
                                            </td>
                                        </tr>
                                        <tr style="height:20px">
                                            <td colspan="5" style="text-align:left;font-size: 12px;" height="19">
                                                <div style="width: 120px; text-align: left;display:inline-block;">
                                                    Beneficiary Bank
                                                </div>
                                                <div style="display:inline-block;">
                                                    ${merchantInfo.depositBank}
                                                    <input type="hidden" name="detail.depositBank" value="${merchantInfo.depositBank}">
                                                </div>
                                            </td>
                                        </tr>
                                        <tr style="height:20px">
                                            <td colspan="5" style="text-align:left;font-size: 12px;" height="19">
                                                <div style="width: 120px; text-align: left;display:inline-block;">
                                                    Account Number
                                                </div>
                                                <div style="display:inline-block;">
                                                    ${merchantInfo.bankAccount}
                                                    <input type="hidden" name="detail.bankAccount" value="${merchantInfo.bankAccount}">
                                                </div>
                                            </td>
                                        </tr>
                                        <tr style="height:20px">
                                            <td colspan="5" style="text-align:left;font-size: 12px;" height="19">
                                                <div style="width: 120px; text-align: left;display:inline-block;">
                                                    Bank Code
                                                </div>
                                                <div style="display:inline-block;">
                                                    004
                                                </div>
                                            </td>
                                        </tr>
                                        <tr style="height:20px">
                                            <td colspan="5" style="text-align:left;font-size: 12px;" height="19">
                                                <div style="width: 120px; text-align: left;display:inline-block;">
                                                    Swift Code
                                                </div>
                                                <div style="display:inline-block;">
                                                    ${merchantInfo.swiftCode}
                                                    <input type="hidden" name="detail.swiftCode" value="${merchantInfo.swiftCode}">
                                                </div>
                                            </td>
                                        </tr>
                                        <tr style="height:20px">
                                            <td colspan="5" style="text-align:left;font-size: 12px;" height="19">
                                                <div style="width: 120px; text-align: left;display:inline-block;">
                                                    Bank Add
                                                </div>
                                                <div style="display:inline-block;">
                                                    ${merchantInfo.bankAddress}
                                                    <input type="hidden" name="detail.bankAddress" value="${merchantInfo.bankAddress}">
                                                </div>
                                            </td>
                                        </tr>
                                        <tr style="height:20px">
                                            <td style="" height="20"></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr style="height:20px">
                                            <td style="" height="21"></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr height="20px">
                                            <td colspan="5" style="font-size: 12px;">
                                                For and on behalf of
                                            </td>
                                        </tr>
                                        <tr height="20px">
                                            <td colspan="5" style="text-align:center;font-size: 12px;">
                                                ${merchantInfo.englishName}
                                            </td>
                                        </tr>
                                        <tr height="23">
                                            <td colspan="5" style="text-align:center;font-size: 12px;">
                                                ADD:<input type="hidden" name="detail.englishRegisteredAddress" value="${merchantInfo.englishRegisteredAddress}">
                                                ${merchantInfo.englishRegisteredAddress}
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
