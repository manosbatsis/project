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
                                <input type="hidden" name="detail.currencyLabel" value="${currencyLabel}">
                                <input type="hidden" name="detail.customerId" value="${customerId}">
                                <input type="hidden" name="detail.customerName" value="${customerInfo.name}">
                                <div>
                                    <table style="text-align: center; border-collapse: collapse; border-spacing: 0px;">
                                        <colgroup>
                                            <col width="454" style="width:364px"/>
                                            <col width="132" style="width:106px"/>
                                            <col width="95" style="width:77px"/>
                                            <col width="164" style="width:131px"/>
                                            <col width="180" style="width:144px"/>
                                        </colgroup>
                                        <tbody>
                                        <tr height="26" style="height:21px" class="firstRow">
                                            <td height="21" width="1154" style=""></td>
                                            <td width="60" style=""></td>
                                            <td width="77" style=""></td>
                                            <td width="131" style=""></td>
                                        </tr>
                                        <tr style="text-align:center;width: 1050px;font-weight: bolder; "
                                            class="firstRow">
                                            <td colspan="5"
                                                style="border-collapse: collapse; border-spacing: 0px;  padding: 0px 0.2em; height: 80px; font-size: 22px; word-break: break-all;">
                                                <span>HONG KONG HONOUR FAITH LIMITED<span>&nbsp;&nbsp;&nbsp;&nbsp;</span></span><span>香港宝信有限公司</span>
                                            </td>

                                        </tr>
                                        <tr height="26" style="height:21px">
                                            <td height="21" style="" width="790"></td>
                                            <td width="60"></td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr height="31" style="height:25px">
                                            <td colspan="5"
                                                style="text-align:center;font-weight:700;font-size: 20px; padding-bottom: 20px; ">
                                                Invoice<span>&nbsp; </span>Information
                                            </td>
                                            <td colspan="1" height="25" style=""></td>
                                        </tr>
                                        <tr height="26" style="height:21px">
                                            <td colspan="2" height="44" style="font-weight:bold;text-align:left;" width="790">
                                                <span>DATE/</span><span>日期</span><span>:
                                            <input type="text" id="invoiceDate" name="detail.date"
                                                   value="<fmt:formatDate value="${invoiceDate}" pattern="yyyy/MM/dd"/>"
                                                   required reqMsg="Date不能为空！"></span>
                                            </td>
                                        </tr>
                                        <tr height="28" style="height:23px">
                                            <td colspan="3" height="43" style="font-weight:bold;text-align:left;" width="790">
                                                NO:
                                            </td>
                                            </td>
                                            <td height="43"></td>
                                            <td width="56" height="43"></td>
                                            <td width="184" height="43"></td>
                                            <td height="43"></td>
                                        </tr>
                                        <tr height="27" style="height:22px">
                                            <td height="39" style="font-weight:bold;text-align:left;" width="790">
                                                To.
                                            </td>
                                            <td colspan="4" width="676" height="39" style="font-weight:bold;text-align:left;">
                                                ${customerInfo.enName}
                                                <input type="hidden" name="detail.toCustomerName"
                                                       value="${customerInfo.enName} ">
                                            </td>
                                            <td colspan="1" width="184" height="39"></td>
                                            <td colspan="1" height="39"></td>
                                        </tr>
                                        <tr height="50" style="height:40px">
                                            <td height="40" style="font-weight:bold;text-align:left;" width="790">
                                                地址:
                                            </td>
                                            <td colspan="4" width="676" style="font-weight:bold;text-align:left;">
                                                ${customerInfo.companyAddr}
                                                <input type="hidden" name="detail.companyAddr"
                                                       value="${customerInfo.companyAddr} ">
                                            </td>
                                            <td colspan="1" width="184"></td>
                                            <td colspan="1" width="458"></td>
                                        </tr>
                                        <tr height="27" style="height:22px">
                                            <td height="42" style="font-weight:bold;text-align:left;" width="790">
                                                税号：
                                            </td>
                                            <td colspan="4" width="676" height="42" style="font-weight:bold;text-align:left;">
                                                ${customerInfo.taxNo}
                                                <input type="hidden" name="detail.taxNo" value="${customerInfo.taxNo} ">
                                            </td>
                                            <td colspan="1" width="184" height="42"></td>
                                            <td colspan="1" height="42"></td>
                                        </tr>
                                        <tr height="28">
                                            <td height="43" style="font-weight:bold;text-align:left;" width="790">
                                                开户行账号：
                                            </td>
                                            <td colspan="4" width="676" height="43" style="font-weight:bold;text-align:left;">
                                                <span>&nbsp;</span>HSBC CAUSEWAY BAY BRANCH 1/F<span>&nbsp; </span>809-808041-838
                                            </td>
                                            <td width="184" height="43"></td>
                                            <td height="43"></td>
                                        </tr>
                                        <tr height="27"
                                            style="width:1050px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                            <td colspan="2" height="22"
                                                style="font-size: 19px;font-weight:bold;width:1050px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                货物或劳务名称
                                            </td>
                                            <td style="font-size: 19px;font-weight:bold;width:1050px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                数量
                                            </td>
                                            <td style="font-size: 19px;font-weight:bold;width:1050px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                <span>单价</span><span>(${currency}）</span>
                                            </td>
                                            <td width="56"
                                                style="font-size: 19px;font-weight:bold;width:1050px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                <span>总金额</span><span>(${currency})</span>
                                            </td>
                                        </tr>
                                        <c:forEach items="${itemLists }" var="item">
                                            <c:choose>
                                                <c:when test="${item.totalAllPrice != null}">
                                                    <tr height="28" style="height:23px"
                                                        style="border-right: 1px solid rgb(0, 0, 0); word-break: break-all;">
                                                        <td colspan="2" height="23"
                                                            style="font-weight:bold;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);"
                                                            width="790">
                                                            进项合计（${currency})
                                                        </td>

                                                        <td style="font-weight:bold;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                                ${item.totalAllNum}
                                                            <input type="hidden" name="detail.totalAllNum"
                                                                   value="${item.totalAllNum}">
                                                        </td>
                                                        <td colspan="2" width="56"
                                                            style="font-weight:bold;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                                ${item.totalAllAmount}
                                                            <input type="hidden" name="detail.totalAllAmount"
                                                                   value="${item.totalAllAmount}" id="totalAllPrice">
                                                        </td>
                                                    </tr>
                                                    <tr height="28"
                                                        style="font-weight:bold;width:1050px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                        <td colspan="2" height="23"
                                                            style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                            金额大写(${currencyLabel}):
                                                        </td>
                                                        <td colspan="3" width="56"
                                                            style="font-weight:bold;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                                ${item.totalAllAmountLabel}
                                                            <input type="hidden" name="detail.totalAllAmountLabel"
                                                                   value="${item.totalAllAmountLabel}">
                                                        </td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <tr height="24"
                                                        style="width:1050px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                        <td height="19"
                                                            style="width:1050px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);"
                                                            width="790">
                                                                ${item.goodsName}
                                                            <input type="hidden" name="goods.goodsName"
                                                                   value="${item.goodsName}">
                                                        </td>
                                                        <td style="width:1050px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);"
                                                            width="60">
                                                                ${item.barcode}
                                                            <input type="hidden" name="goods.barcode"
                                                                   value="${item.barcode}">
                                                        </td>
                                                        <td style="width:1050px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                                ${item.totalNum}
                                                            <input type="hidden" name="goods.totalNum"
                                                                   value="${item.totalNum}">
                                                        </td>
                                                        <td style="width:1050px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                            <span>&nbsp;</span>${item.price}<span>&nbsp;</span>
                                                            <input type="hidden" name="goods.price"
                                                                   value="${item.price}">
                                                        </td>
                                                        <td width="56"
                                                            style="width:1050px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                            <span></span><span>${item.totalPrice}</span>
                                                            <input type="hidden" name="goods.totalPrice"
                                                                   value="${item.totalPrice}">
                                                        </td>
                                                    </tr>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                        <tr height="24"
                                            style="width:1050px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                            <td colspan="2" height="19" width="790"
                                                style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                预付款
                                            </td>
                                            <td width="60"
                                                style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);"></td>
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);"></td>
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                <input type="text" id="prepayments" name="detail.prepayments">
                                            </td>
                                        </tr>
                                        <tr height="28"
                                            style="width:1050px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                            <td colspan="2" height="23" width="790"
                                                style="font-weight:bold;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                预付款合计（${currency})
                                            </td>
                                            <td width="60"
                                                style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);"></td>
                                            <td colspan="2" width="56"
                                                style="font-weight:bold;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                <span id="prepaymentTotalSpan"></span>
                                                <input type="hidden" id="prepaymentTotal" name="detail.prepaymentTotal">
                                            </td>
                                        </tr>
                                        <c:forEach items="${costItemList }" var="item">
                                            <c:choose>
                                                <c:when test="${item.totalPriceLabel != null}">
                                                    <tr height="28"
                                                        style="width:1050px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                        <td colspan="2" height="23"
                                                            style="font-weight:bold;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);"
                                                            width="790">
                                                            扣减项合计（${currency})
                                                        </td>
                                                        <td width="60"
                                                            style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);"></td>
                                                        <td colspan="2" width="56"
                                                            style="font-weight:bold;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                            <span>${item.totalPrice}</span>
                                                            <input type="hidden" name="detail.deductionTotal"
                                                                   value="${item.totalPrice}" id="deductionTotal">
                                                        </td>
                                                    </tr>
                                                    <tr height="28"
                                                        style="width:1050px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                        <td colspan="2" height="23"
                                                            style="font-weight:bold;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                            金额大写(${currencyLabel}):
                                                        </td>
                                                        <td colspan="3" width="56"
                                                            style="font-weight:bold;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                            <span>${item.totalPriceLabel}</span>
                                                            <input type="hidden" name="detail.deductionTotalLabel"
                                                                   value="${item.totalPriceLabel}">
                                                        </td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <tr height="24"
                                                        style="width:1050px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                        <td colspan="2" height="19" width="790"
                                                            style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                                ${item.projectName}
                                                            <input type="hidden" name="costItem.projectName"
                                                                   value="${item.projectName}">
                                                        </td>
                                                        <td width="60"
                                                            style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);"></td>
                                                        <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);"></td>
                                                        <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                                ${item.totalPrice}
                                                            <input type="hidden" name="costItem.totalPrice"
                                                                   value="${item.totalPrice}">
                                                        </td>
                                                    </tr>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                        <tr height="24"
                                            style="width:1050px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                            <td colspan="2" height="19" width="790"
                                                style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                调整项－
                                            </td>
                                            <td width="60"
                                                style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);"></td>
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);"></td>
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                <input type="text" id="adjustment" name="detail.adjustment">
                                            </td>
                                        </tr>
                                        <tr height="28"
                                            style="width:1050px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                            <td colspan="2" height="23"
                                                style="font-weight:bold;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);"
                                                width="790">
                                                调整项合计（${currency})
                                            </td>
                                            <td width="60"
                                                style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);"></td>
                                            <td colspan="2" width="56"
                                                style="font-weight:bold;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                <span id="adjustmentTotalSpan"></span>
                                                <input type="hidden" id="adjustmentTotal" name="detail.adjustmentTotal">
                                            </td>
                                        </tr>
                                        <tr height="28"
                                            style="width:1050px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                            <td colspan="2" height="23"
                                                style="font-weight:bold;width:1050px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                金额大写（${currencyLabel}）
                                            </td>
                                            <td colspan="3" width="56"
                                                style="font-weight:bold;width:1050px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                <span id="adjustmentTotalLSpan"></span>
                                                <input type="hidden" id="adjustmentTotalLabel"
                                                       name="detail.adjustmentTotalLabel">
                                            </td>
                                        </tr>
                                        <tr height="24"
                                            style="width:1050px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                            <td colspan="2" height="19"
                                                style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);"
                                                width="790">
                                                市场补贴费用-
                                            </td>
                                            <td width="60"
                                                style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);"></td>
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);"></td>
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                <input type="text" id="subsidy" name="detail.subsidy">
                                            </td>
                                        </tr>
                                        <tr height="28"
                                            style="width:1050px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                            <td colspan="2" height="23"
                                                style="font-weight:bold;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);"
                                                width="790">
                                                市场补贴费用合计（${currency})
                                            </td>
                                            <td width="60"
                                                style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);"></td>
                                            <td colspan="2" width="56"
                                                style="font-weight:bold;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                <span id="subsidySpan"></span>
                                                <input type="hidden" id="subsidyTotal" name="detail.subsidyTotal">
                                            </td>
                                        </tr>
                                        <tr height="28"
                                            style="width:1050px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                            <td colspan="2" height="23"
                                                style="font-weight:bold;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                金额大写（${currencyLabel}）
                                            </td>
                                            <td colspan="3" width="56"
                                                style="font-weight:bold;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                <span id="subsidyTotalLSpan"></span>
                                                <input type="hidden" id="subsidyTotalLabel"
                                                       name="detail.subsidyTotalLabel">
                                            </td>
                                        </tr>
                                        <tr height="28"
                                            style="width:1050px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                            <td colspan="2" height="23"
                                                style="font-weight:bold;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);"
                                                width="790">
                                                首次付款比例：
                                            </td>
                                            <td width="60"
                                                style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);"></td>
                                            <td colspan="2" width="60"
                                                style="font-weight:bold;text-align:right;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                <input type="text" id="proportion" name="detail.proportion"
                                                       value="100%">
                                            </td>
                                        </tr>
                                        <tr height="28"
                                            style="width:1050px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                            <td colspan="2" height="23"
                                                style="font-weight:bold;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);"
                                                width="790">
                                                首次付款金额：
                                            </td>
                                            <td width="60"
                                                style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);"></td>
                                            <td colspan="2" width="56"
                                                style="font-weight:bold;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                <span id="proportionPrice"></span>
                                                <input type="hidden" id="proportionTotalPrice"
                                                       name="detail.proportionTotalPrice">
                                            </td>
                                        </tr>
                                        <tr height="28"
                                            style="width:1050px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                            <td colspan="2" height="23"
                                                style="font-weight:bold;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                &nbsp金额大写(${currencyLabel}):
                                            </td>
                                            <td colspan="3" width="56"
                                                style="font-weight:bold;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                <span id="proportionPriceLSpan"></span>
                                                <input type="hidden" id="proportionPriceLabel"
                                                       name="detail.proportionPriceLabel">
                                            </td>
                                        </tr>
                                        <tr height="78" style="height:63px"
                                            style="width:1050px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                            <td colspan="2" height="63"
                                                style="font-weight:bold;text-align:left;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                &nbsp备注：<input name="detail.remark">
                                            </td>
                                            <td colspan="3"
                                                style="font-weight:bold;text-align:left;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                &nbsp账单编号：${relCodes}
                                                <input name="detail.relCodes" type="hidden" value="${relCodes}">
                                            </td>
                                        </tr>
                                        <tr height="27" style="height:22px"
                                            style="width:1050px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                            <td colspan="2" height="22" width="790"
                                                style="font-weight:bold;text-align:left;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                &nbsp销货单位:
                                            </td>
                                            <td colspan="3" width="676"
                                                style="font-weight:bold;text-align:left;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                &nbspHONG KONG HONOUR FAITH LIMITED 香港宝信有限公司
                                            </td>
                                        </tr>
                                        <tr height="25" style="height:20px"
                                            style="width:1050px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                            <td colspan="2" height="20" width="790"
                                                style="font-weight:bold;text-align:left;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                &nbsp地址电话:
                                            </td>
                                            <td colspan="3" width="676"
                                                style="font-weight:bold;text-align:left;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                &nbspROOM 2 36/F SUNSHINE PLAZA 353 LOCKHART ROAD WANCHAI HK
                                            </td>
                                        </tr>
                                        <tr height="27" style="height:22px"
                                            style="width:1050px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                            <td colspan="2" height="22" width="790"
                                                style="font-weight:bold;text-align:left;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                &nbsp联系地址:
                                            </td>
                                            <td colspan="3" width="676"
                                                style="font-weight:bold;text-align:left;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                &nbsp广东省广州市黄埔区黄埔大道东983号港航中心11层 020-62262432
                                            </td>
                                        </tr>
                                        <tr height="27" style="height:22px"
                                            style="width:1050px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                            <td colspan="2" height="22" width="790"
                                                style="font-weight:bold;text-align:left;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                &nbsp银行识别码:
                                            </td>
                                            <td colspan="3" width="676"
                                                style="font-weight:bold;text-align:left;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                ${merchantInfo.swiftCode}
                                                <input type="hidden" name="detail.swiftCode"
                                                       value="${merchantInfo.swiftCode}">
                                            </td>
                                        </tr>
                                        <tr height="28" style="height:23px"
                                            style="width:1050px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                            <td colspan="2" height="23" width="790"
                                                style="font-weight:bold;text-align:left;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                &nbsp开户行及账号:(${currency})
                                            </td>
                                            <td colspan="3" width="676"
                                                style="font-weight:bold;text-align:left;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                ${merchantInfo.depositBank}${merchantInfo.bankAccount}
                                                <input type="hidden" name="detail.bankAccount"
                                                       value="${merchantInfo.depositBank}${merchantInfo.bankAccount}">
                                            </td>
                                            <td colspan="1" width="92"></td>
                                            <td colspan="1"></td>
                                        </tr>
                                        <tr height="26" style="height:21px">
                                            <td height="21" style="" width="790"></td>
                                            <td width="60"></td>
                                            <td></td>
                                            <td></td>
                                            <td width="56"></td>
                                            <td width="92"></td>
                                            <td></td>
                                        </tr>
                                        <tr height="27" style="height:22px">
                                            <td height="22" style="font-weight:bold;text-align:left;" width="790">
                                                付款方式:<span>&nbsp; </span>银行转账
                                            </td>
                                            <td width="60"></td>
                                            <td></td>
                                            <td></td>
                                            <td width="56"></td>
                                            <td width="92"></td>
                                            <td></td>
                                        </tr>
                                        <tr height="27" style="height:22px">
                                            <td height="22" style="font-weight:bold;text-align:left;" width="790">
                                                <span>出票人签章:</span><span><input name="detail.drawer"></span>
                                            </td>
                                            <td width="60"></td>
                                            <td></td>
                                            <td></td>
                                            <td width="56"></td>
                                            <td width="92"></td>
                                            <td></td>
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
    }

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
        return symbol+strOutput.replace(/零角零分$/, '整').replace(/零[仟佰拾]/g, '零').replace(/零{2,}/g, '零').replace(/零([亿|万])/g, '$1').replace(/零+元/, '元').replace(/亿零{0,3}万/, '亿').replace(/^元/, "零元");
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
