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
                                        <li class="breadcrumb-item"><a href="#">账单管理</a></li>
                                        <li class="breadcrumb-item"><a href="javascript:dh_list();">应收账单列表 </a></li>
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
                                        <tr style="text-align:center; font-weight: bolder; font-size: 16px; font-family: SansSerif">
                                            <td colspan="4">
                                                健太有限公司
                                                <br>
                                                Q TOP LIMITED
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan="4"
                                                style="text-align:center;font-weight:700;font-size: 16px; text-decoration:underline;">
                                                INVOICE
                                            </td>
                                        </tr>

                                        <tr>
                                            <td colspan="2" style="text-align: left">
                                                ${customerInfo.name}
                                            </td>
                                            <td>Date:</td>
                                            <td>
                                                <input type="text" name="detail.date" value="<fmt:formatDate value="${invoiceDate}" pattern="yyyy/MM/dd"/>"
                                                       required reqMsg="Date不能为空！" id="invoiceDate">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan="2" rowspan="2" style="text-align: left">
                                                ${customerInfo.companyAddr}
                                                <input type="hidden" name="detail.companyAddr" value="${customerInfo.companyAddr}">
                                            </td>
                                            <td>INVOICE NO.:</td>
                                            <td>
                                                ${invoice.invoiceNo}
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>REMARK:</td>
                                            <td>
                                                <input type="text" name="detail.remark" value="${invoice.remark}">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td style="height:46px;">Description of item</td>
                                            <td style="height:46px;">Quantity/ Unit</td>
                                            <td style="height:46px;">Unit Price（${currency}）</td>
                                            <td style="height:46px;">Amount（${currency}）</td>
                                        </tr>

                                        <c:forEach items="${itemLists }" var="item">
                                            <c:choose>
                                                <c:when test="${item.totalAllNum != null && item.totalAllAmount != null}">
                                                </c:when>
                                                <c:otherwise>
                                                    <tr>
                                                        <td>${item.goodsName }</td>
                                                        <input type="hidden" name="goods.goodsName" value="${item.goodsName}">
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
                                            <td colspan="3" style="height:46px;">Total(${currency})</td>
                                            <td>
                                                ${totalAllAmount }
                                                <input type="hidden"    name="detail.totalAllAmount" value="${totalAllAmount}">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan="4" style="text-align: left;">BANK DETAILS</td>
                                        </tr>
                                        <tr>
                                            <td>BANK NAME</td>
                                            <td colspan="3">${merchantInfo.depositBank}</td>
                                            <input type="hidden" name="detail.depositBank" value="${merchantInfo.depositBank}">
                                        </tr>
                                        <tr>
                                            <td></td>
                                            <td colspan="3">${merchantInfo.bankAddress}</td>
                                            <input type="hidden" name="detail.bankAddress" value="${merchantInfo.bankAddress}">
                                        </tr>
                                        <tr>
                                            <td>ACCOUNT NAME</td>
                                            <td colspan="3">${merchantInfo.beneficiaryName}</td>
                                            <input type="hidden" name="detail.beneficiaryName" value="${merchantInfo.beneficiaryName}">
                                        </tr>
                                        <tr>
                                            <td>ACCOUNT NO.</td>
                                            <td colspan="3">${merchantInfo.bankAccount}</td>
                                            <input type="hidden" name="detail.bankAccount" value="${merchantInfo.bankAccount}">
                                        </tr>
                                        <tr>
                                            <td>SWIFT CODE</td>
                                            <td colspan="3">${merchantInfo.swiftCode}</td>
                                            <input type="hidden" name="detail.swiftCode" value="${merchantInfo.swiftCode}">
                                        </tr>
                                        <tr style="height:26px">
                                            <td colspan="4" style="text-align: center;border-collapse: collapse; border-spacing: 0;padding: 0 .2em;">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan="4">Q TOP LIMITED</td>
                                        </tr>
                                        <tr>
                                            <td colspan="4">(UNIT 1302 13/F HUA FU COMMERCIAL BUILDING 111 QUEEN’S ROAD
                                                WEST HK)
                                            </td>
                                        </tr>
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
