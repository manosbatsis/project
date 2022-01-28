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
                                        <li class="breadcrumb-item"><a href="#">收款管理</a></li>
                                        <li class="breadcrumb-item"><a href="javascript:dh_list();">To C应收账单列表 </a></li>
                                        <li class="breadcrumb-item"><a href="#">发票详情</a></li>
                                    </ol>
                                </div>
                            </div>
                            <!--  title   end  -->
                            <div>
                                <input type="hidden" id="billCodes">
                                <form id="edit-form">
                                    <input type="hidden" name="detail.codes" value="${codes}">
                                    <input type="hidden" name="detail.ids" value="${ids}">
                                    <input type="hidden" name="detail.tempId" value="${tempId}">
                                    <input type="hidden" name="detail.fileTempCode" value="${fileTempCode}">
                                    <input type="hidden" name="detail.currency" value="${currency}">
                                    <input type="hidden" name="detail.customerId" value="${customerId}">
                                    <input type="hidden" name="detail.customerName" value="${customerInfo.name}">
                                    <table class="pure-table pure-table-bordered">
                                        <tbody>
                                        <tr style="height:34px" class="firstRow" height="43">
                                            <td colspan="5"
                                                style="border-collapse: collapse; border-spacing: 0px;  padding: 0px 0.2em; height: 40px; font-size: 18px; word-break: break-all;">
                                                <strong>健太有限公司</strong>
                                            </td>
                                        </tr>
                                        <tr style="height:36px">
                                            <td colspan="5"
                                                style="text-align:center;font-weight:700;font-size: 18px; padding-bottom: 20px; ">
                                                Q TOP LIMITED
                                            </td>
                                        </tr>
                                        <tr style="height:28px">
                                            <td colspan="5"
                                                style="text-align: center; font-weight: 700; font-size: 18px; padding-bottom: 20px; word-break: break-all;text-decoration:underline;">
                                                INVOICE
                                            </td>
                                        </tr>
                                        <tr style="height:19px; ">
                                            <td style="text-align:left;word-break: break-all; word-wrap:break-word;font-size: 12px;">
                                                <div>
                                                    <strong>客户名称：卓烨贸易有限公司</strong>
                                                </div>
                                            </td>
                                            <td></td>
                                            <td style="text-align:center;word-break: break-all; word-wrap:break-word;font-size: 12px;">
                                                发票号：
                                            </td>
                                            <td colspan="2" style="text-align:left;word-break: break-all; word-wrap:break-word;font-size: 12px;">
                                            </td>
                                        </tr>
                                        <tr style="height:19px; ">
                                            <td style="text-align:left;word-break: break-all; word-wrap:break-word;font-size: 12px;">
                                                <div>
                                                    <strong>账单时期：${billDate}</strong>
                                                    <input type="hidden" id="billDate" name="detail.billMonth" value="${billDate}">
                                                </div>
                                            </td>
                                            <td colspan="4" style="text-align:left;word-break: break-all; word-wrap:break-word;font-size: 12px;">

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
                                                账单所属月份
                                            </td>
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);text-align:center;font-size: 12px;">
                                                商品名称
                                            </td>
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);text-align:center;font-size: 12px;">
                                                数量
                                            </td>
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);text-align:center;font-size: 12px;"
                                                width="74">
                                                经销部
                                            </td>
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);text-align:center;font-size: 12px;"
                                                width="90">
                                                总金额
                                            </td>
                                        </tr>
                                        <c:forEach items="${itemLists }" var="item">
                                            <c:choose>
                                                <c:when test="${item.totalAllAmount != null}">
                                                </c:when>
                                                <c:otherwise>
                                                    <tr>
                                                        <td>${billDate }</td>
                                                        <input type="hidden" name="goods.billDate"
                                                               value="${billDate}">
                                                        <td>
                                                        <input type="text" name="goods.goodsName" style="width: 450px;"
                                                               value="${item.goodsName}"></td>
                                                        <td>${item.totalNum }</td>
                                                        <input type="hidden" name="goods.totalNum"
                                                               value="${item.totalNum}">
                                                        <td>${item.totalAmount }</td>
                                                        <td>${item.totalAmount }</td>
                                                        <input type="hidden" name="goods.totalAmount"
                                                               value="${item.totalAmount}">
                                                    </tr>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                        <tr style="height:20px; ">
                                            <td colspan="5" style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);font-size: 10px; text-align: left;">
                                                账单明细查看附件
                                            </td>
                                        </tr>
                                        <tr style="height:20px;">
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);font-size: 12px;">
                                                合计（${currency}）
                                            </td>
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);"></td>
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);"></td>
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);word-break: break-all; word-wrap:break-word;font-size: 12px;padding-right: 5px;">
                                                ${totalAllAmount }
                                            </td>
                                            <td style="border-collapse: collapse; border: 1px solid rgb(0, 0, 0);word-break: break-all; word-wrap:break-word;font-size: 12px;padding-right: 5px;">
                                                ${totalAllAmount }
                                                <input type="hidden" name="detail.totalAllAmount"
                                                       value="${totalAllAmount}">
                                            </td>
                                        </tr>
                                        <tr style="height:40px">
                                            <td style="" height="18"></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr style="height:20px">
                                            <td colspan="5" style="text-align:left;font-size: 12px;" height="19">
                                                账户信息：
                                            </td>
                                        </tr>
                                        <tr style="height:20px">
                                            <td colspan="5" style="text-align:left;font-size: 12px;" height="19">
                                                公司名称：${merchantInfo.name}
                                                <input type="hidden" name="detail.merchantName" value="${merchantInfo.name}">
                                            </td>
                                        </tr>
                                        <tr style="height:20px">
                                            <td colspan="5" style="text-align:left;font-size: 12px;" height="19">
                                                开户银行：${merchantInfo.depositBank}
                                                <input type="hidden" name="detail.depositBank" value="${merchantInfo.depositBank}">
                                            </td>
                                        </tr>
                                        <tr style="height:20px">
                                            <td colspan="5" style="text-align:left;font-size: 12px;" height="19">
                                                银行账号：${merchantInfo.bankAccount}
                                                <input type="hidden" name="detail.bankAccount" value="${merchantInfo.depositBank}">
                                            </td>
                                        </tr>
                                        <tr style="height:20px">
                                            <td colspan="5" style="text-align:left;font-size: 12px;" height="19">
                                                SWIFT CODE：${merchantInfo.swiftCode}
                                                <input type="hidden" name="detail.swiftCode" value="${merchantInfo.swiftCode}">
                                            </td>
                                        </tr>
                                        <tr style="height:20px">
                                            <td style="" height="18"></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr style="height:20px">
                                            <td colspan="5" style="text-align:left;font-size: 12px;" height="19">
                                                联系方式：
                                            </td>
                                        </tr>
                                        <tr style="height:20px">
                                            <td colspan="5" style="text-align:left;font-size: 12px;" height="19">
                                                联系人：林靖
                                            </td>
                                        </tr>
                                        <tr style="height:20px">
                                            <td colspan="5" style="text-align:left;font-size: 12px;" height="19">
                                                电  话：13922796237
                                            </td>
                                        </tr>
                                        <tr style="height:20px">
                                            <td colspan="5" style="text-align:left;font-size: 12px;" height="19">
                                                邮  箱：jing.lin@topideal.com
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </form>
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
        <!-- container -->
    </div>
</div>
<!-- content -->
<script type="text/javascript">

    //返回
    function returnBack() {
        $module.load.pageOrder("act=14018");
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
            var url = serverAddr + "/toCReceiveBill/saveSubmitInvoice.asyn";
            var json = getFormJson($("#edit-form"));
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

</script>
