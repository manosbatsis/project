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
                                            <col width="230" style="width:185px"/>
                                            <col width="185" style="width:149px" span="4"/>
                                            <col width="95" style="width:77px"/>
                                            <col width="110" style="width:88px"/>
                                        </colgroup>
                                        <tbody>
                                        <tr height="41" style="height:33px" class="firstRow">
                                            <td colspan="7"
                                                style="font-weight: bold;border-collapse: collapse; border-spacing: 0px;  padding: 0px 0.2em; height: 40px; font-size: 22px; word-break: break-all;">
                                                香港宝信有限公司(HONG KONG HONOUR FAITH LIMITED)
                                            </td>
                                        </tr>
                                        <tr height="41" style="height:33px">
                                            <td colspan="7" height="33" style="">
                                                ROOM 1 36/F SUNSHINE PLAZA 353 LOCKHART ROAD WANCHAI HK
                                            </td>
                                        </tr>
                                        <tr height="15" style="height:12px">
                                            <td height="12" style=""></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr height="60" style="height:48px">
                                            <td colspan="7"
                                                style="text-align:center;font-weight:700;font-size: 20px; padding-bottom: 20px;text-decoration:underline ">
                                                INVOICE 发票
                                            </td>
                                        </tr>
                                        <tr height="20" style="height:16px">
                                            <td height="16" width="185" style=""></td>
                                            <td width="149" style=""></td>
                                            <td width="149" style=""></td>
                                            <td width="149" style=""></td>
                                            <td width="149" style=""></td>
                                            <td width="77" style=""></td>
                                            <td width="88" style=""></td>
                                        </tr>
                                        <tr height="41" style="height:33px;">
                                            <td height="33" colspan="4"
                                                style="background-color: #DDDDDD;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);text-align: left;font-weight: bold">
                                                &nbsp;&nbsp;BILL TO
                                            </td>
                                            <td style="text-align:left;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                &nbsp;DATE 时间：
                                            </td>
                                            <td colspan="2"
                                                style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                <input type="text" id="invoiceDate" name="detail.date"
                                                       value="<fmt:formatDate value="${invoiceDate}" pattern="yyyy/MM/dd"/>"  required reqMsg="Date不能为空！">
                                            </td>
                                        </tr>
                                        <tr height="66" style="height:55px">
                                            <td rowspan="2"  colspan="4" height="36" width="630"
                                                style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);border-right: 1px solid rgb(0, 0, 0);">
                                                ${customerInfo.enName}
                                                <input type="hidden" name="detail.toCustomerName" value="${customerInfo.enName} ">
                                            </td>
                                            <td width="313"
                                                style="text-align:left;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);border-right: 1px solid rgb(0, 0, 0);">
                                                <span>&nbsp;INVOICE NO. 发票号：</span><span></span>
                                            </td>
                                            <td colspan="2" style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                            </td>
                                        </tr>
                                        <tr height="66" style="height:36px">
                                            <td  width="313"
                                                style="text-align:left;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                <span>&nbsp;Purchase Order 采购订单：</span>
                                            </td>
                                            <td colspan="2"
                                                style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                <input type="text" id="poNo" name="detail.poNos" value="${poNos}" required reqMsg="Purchase Order不能为空！">
                                            </td>
                                        </tr>
                                        <tr height="41"
                                            style="background-color: #DDDDDD;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                            <td colspan="7" height="33"
                                                style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);text-align: left;font-weight: bold">
                                                &nbsp;&nbsp;BANK INFO
                                            </td>
                                        </tr>
                                        <tr height="33" style="height:27px">
                                            <td colspan="2" height="27" width="185"
                                                style="text-align: left;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                &nbsp;&nbsp;Beneficiary Name
                                            </td>
                                            <td colspan="5"
                                                style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                ${merchantInfo.beneficiaryName}
                                                <input type="hidden" name="detail.beneficiaryName" value="${merchantInfo.beneficiaryName}">
                                            </td>
                                        </tr>
                                        <tr height="33" style="height:27px;">
                                            <td colspan="2" height="27" width="185"
                                                style="text-align: left;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                &nbsp;&nbsp;Beneficiary Address
                                            </td>
                                            <td colspan="5"
                                                style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                ${merchantInfo.registeredAddress}
                                                <input type="hidden" name="detail.registeredAddress" value="${merchantInfo.registeredAddress}">
                                            </td>
                                        </tr>
                                        <tr height="33" style="height:27px">
                                            <td colspan="2" height="27" width="185"
                                                style="text-align: left;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                &nbsp;&nbsp;Bank name
                                            </td>
                                            <td colspan="5"
                                                style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                ${merchantInfo.depositBank}
                                                <input type="hidden" name="detail.depositBank" value="${merchantInfo.depositBank}">
                                            </td>
                                        </tr>
                                        <tr height="33" style="height:27px">
                                            <td colspan="2" height="27" width="185"
                                                style="text-align: left;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                &nbsp;&nbsp;Bank Address
                                            </td>
                                            <td colspan="5"
                                                style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                ${merchantInfo.bankAddress}
                                                <input type="hidden" name="detail.bankAddress" value="${merchantInfo.bankAddress}">
                                            </td>
                                        </tr>
                                        <tr height="33" style="height:27px">
                                            <td colspan="2" height="27" width="185"
                                                style="text-align: left;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                &nbsp;&nbsp;Account No.
                                            </td>
                                            <td colspan="5"
                                                style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                ${merchantInfo.bankAccount}
                                                <input type="hidden" name="detail.bankAccount" value="${merchantInfo.bankAccount}">
                                            </td>
                                        </tr>
                                        <tr height="33" style="height:27px">
                                            <td colspan="2" height="27" width="185"
                                                style="text-align: left;border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                &nbsp;&nbsp;SWIFT
                                            </td>
                                            <td colspan="5"
                                                style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                ${merchantInfo.swiftCode}
                                                <input type="hidden" name="detail.swiftCode" value="${merchantInfo.swiftCode}">
                                            </td>
                                        </tr>
                                        <tr height="57" style="background-color: #DDDDDD;height:46px">
                                            <td colspan="2" height="46" width="500"
                                                style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                COMMODITY<br/>商品名称
                                            </td>
                                            <td width="170"
                                                style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                ARTICLE NUMBER<br/>货号
                                            </td>
                                            <td width="149"
                                                style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                QUANTITY<br/>数量（PCS）
                                            </td>
                                            <td width="80"
                                                style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                <span>UNITY PRICE<br/>单价</span><span>（${currency}）</span>
                                            </td>
                                            <td colspan="2" width="165"
                                                style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                <span>AMOUNT<br/>总价</span><span>（${currency}）</span>
                                            </td>
                                        </tr>
                                        <c:forEach items="${itemLists }" var="item">
                                            <c:choose>
                                                <c:when test="${item.totalAllNum != null}">
                                                    <tr height="41" style="height:33px;background-color: #DDDDDD;">
                                                        <td colspan="7" height="33" width="779" style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                        </td>
                                                    </tr>
                                                    <tr height="41" style="height:33px">
                                                        <td colspan="3" height="33" width="779"
                                                            style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);">
                                                            Sub Total
                                                        </td>
                                                        <td colspan="1" style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);text-align:right">
                                                                ${item.totalAllNum}&nbsp;
                                                                <input type="hidden" name="detail.totalAllNum" value="${item.totalAllNum}">
                                                        </td>
                                                        <td colspan="1" style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);"></td>
                                                        <td colspan="2" width="165" x:num="0"
                                                            style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);text-align:right">${item.totalAllAmount }&nbsp;
                                                            <input type="hidden" name="detail.totalAllAmount" value="${item.totalAllAmount}">
                                                        </td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <tr height="41" style="height:33px">
                                                        <td colspan="2" height="33"
                                                            style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);text-align:left">
                                                            &nbsp;${item.goodsName }
                                                            <input type="hidden" name="goods.goodsName" value="${item.goodsName}">
                                                        </td>
                                                        <td width="149"
                                                            style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);text-align:left">
                                                            &nbsp;${item.barcode }
                                                            <input type="hidden" name="goods.barcode" value="${item.barcode}">
                                                        </td>
                                                        <td width="149"
                                                            style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);text-align:right">
                                                            ${item.totalNum }&nbsp;
                                                            <input type="hidden" name="goods.totalNum" value="${item.totalNum}">
                                                        </td>
                                                        <td width="149"
                                                            style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);text-align:right">
                                                            ${item.price }&nbsp;
                                                            <input type="hidden" name="goods.price" value="${item.price}">
                                                        </td>
                                                        <td colspan="2"
                                                            style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);text-align:right">
                                                            ${item.totalPrice }&nbsp;
                                                            <input type="hidden" name="goods.totalPrice" value="${item.totalPrice}">
                                                        </td>
                                                    </tr>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                        <tr><td colspan="7">&nbsp&nbsp&nbsp</td></tr>
                                        <tr><td colspan="7">&nbsp&nbsp&nbsp</td></tr>
                                        <tr height="29" style="height:23px">
                                            <td colspan="2" height="23" style="text-align: left">
                                                1）Origin city 装货港：
                                            </td>
                                            <td style="text-align: left">
                                                <input type="text" id="originCity" name="detail.originCity" value="${invoice.originCity}" >
                                            </td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr height="29" style="height:23px">
                                            <td colspan="2" height="23" style="text-align: left">
                                                2）Destination city 卸货港：
                                            </td>
                                            <td style="text-align: left">
                                                <input type="text" name="detail.destinationCity" value="${invoice.destinationCity}">
                                            </td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td width="88" style=""></td>
                                        </tr>
                                        <tr height="29" style="height:23px">
                                            <td colspan="2" height="23" style="text-align: left">
                                                3）REMARKS 备注：
                                            </td>
                                            <td style="text-align: left">
                                                <input type="text" name="detail.remark" value="${invoice.remark}">
                                            </td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td width="88" style=""></td>
                                        </tr>
                                        </tbody>
                                    </table>
                                    <p>
                                        <br/>
                                    </p>
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

    laydate.render({
        elem: '#invoiceDate',
        format: 'yyyy/MM/dd'
    });
</script>
