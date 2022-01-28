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
                                    <table class="pure-table pure-table-bordered">
                                        <tr style="text-align:center;">
                                            <td colspan="5" style="font-weight: bold;">
                                                香港元森泰有限公司
                                                <br>
                                                HONG KONG YUEN SUM TAI LIMITED
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan="5"
                                                style="text-align:center;font-weight:700;font-size: 16px; text-decoration:underline;">
                                                INVOICE
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan="4" style="text-align: right">
                                                Date:
                                            </td>
                                            <td style="width:150px;">
                                                <input type="text" name="detail.date"
                                                       value="<fmt:formatDate value="${invoiceDate}" pattern="yyyy/MM/dd"/>"
                                                       id="invoiceDate"
                                                       required reqMsg="Date不能为空！">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan="4" style="text-align: right">
                                                INVOICE NO.:
                                            </td>
                                            <td style="width:150px;">
                                                ${invoice.invoiceNo}
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan="4" style="text-align: right">
                                                PO NO.：
                                            </td>
                                            <td style="width:150px;">
                                                <input type="text" name="detail.poNo" value="${invoice.poNo}"
                                                       required reqMsg="PO NO.不能为空！">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                Supplier:
                                            </td>
                                            <td colspan="4">
                                                HONG KONG YUEN SUM TAI LIMITED<br/>
                                                RMS 1804-05,18/F HUA QIN INT'L BLDG 340 QUEEN'S RD CENTRAL HK
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                发货人：
                                            </td>
                                            <td colspan="4">
                                                香港元森泰有限公司
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                Consignee:
                                            </td>
                                            <td colspan="4">
                                                ${customerInfo.enName }
                                                <input type="hidden" name="detail.customerEnName" value="${customerInfo.enName}">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                收货人：
                                            </td>
                                            <td colspan="4">
                                                ${customerInfo.name }
                                                    <input type="hidden" name="detail.customerId" value="${customerId}">
                                                <input type="hidden" name="detail.customerName" value="${customerInfo.name}">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>SKU</td>
                                            <td>Description of item</td>
                                            <td>Quantity/ Unit</td>
                                            <td>Unit Price(${currency})</td>
                                            <td>Amount(${currency})</td>
                                        </tr>

                                        <c:forEach items="${itemLists }" var="item">
                                            <c:choose>
                                                <c:when test="${item.totalAllNum}">
                                                    <tr>
                                                        <td colspan="2" style="font-weight: bolder;border-collapse: collapse;">Total：</td>
                                                        <td colspan="1" style="text-align:right;border-collapse: collapse; ">${totalAllNum }&nbsp; <input type="hidden" name="detail.totalAllNum" value="${item.totalAllNum}"></td>
                                                        <td colspan="1" style="border-collapse: collapse;"></td>
                                                        <td colspan="1" style="text-align:right;border-collapse: collapse;">${item.totalAllAmount }<input type="hidden" name="detail.totalAllAmount" value="${item.totalAllAmount}"></td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <tr>
                                                        <td style="text-align:left">&nbsp;${item.goodsNo }</td>
                                                        <input type="hidden" name="goods.goodsNo" value="${item.goodsNo}">
                                                        <td style="text-align:left">&nbsp;${item.goodsName }</td>
                                                        <input type="hidden" name="goods.goodsName" value="${item.goodsName}">
                                                        <td style="text-align:right">${item.totalNum }&nbsp;</td>
                                                        <input type="hidden" name="goods.totalNum" value="${item.totalNum}">
                                                        <td style="text-align:right">${item.price }&nbsp;</td>
                                                        <input type="hidden" name="goods.price" value="${item.price}">
                                                        <td style="text-align:right">${item.totalPrice }&nbsp;</td>
                                                        <input type="hidden" name="goods.totalPrice" value="${item.totalPrice}">
                                                    </tr>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>

                                        <tr>
                                            <td colspan="5" style="text-align: left;">BANK DETAILS</td>
                                        </tr>
                                        <tr>
                                            <td>BANK NAME</td>
                                            <td colspan="4">${merchantInfo.depositBank}</td>
                                            <input type="hidden" name="detail.depositBank" value="${merchantInfo.depositBank}">
                                        </tr>
                                        <tr>
                                            <td></td>
                                            <td colspan="4">${merchantInfo.bankAddress}</td>
                                            <input type="hidden" name="detail.bankAddress" value="${merchantInfo.bankAddress}">
                                        </tr>
                                        <tr>
                                            <td>ACCOUNT NAME</td>
                                            <td colspan="4">${merchantInfo.beneficiaryName}</td>
                                            <input type="hidden" name="detail.beneficiaryName" value="${merchantInfo.beneficiaryName}">
                                        </tr>
                                        <tr>
                                            <td>ACCOUNT NO.</td>
                                            <td colspan="4">${merchantInfo.bankAccount}</td>
                                            <input type="hidden" name="detail.bankAccount" value="${merchantInfo.bankAccount}">
                                        </tr>
                                        <tr>
                                            <td>SWIFT CODE</td>
                                            <td colspan="4">${merchantInfo.swiftCode}</td>
                                            <input type="hidden" name="detail.swiftCode" value="${merchantInfo.swiftCode}">
                                        </tr>
                                        <tr>

                                        </tr>
                                        <tr style="width:1050px; border-collapse: collapse;">
                                            <td colspan="6" style="text-align: center;border-collapse: collapse; border-spacing: 0; padding-top: 20px;"></td>
                                        </tr>
                                        <tr>
                                            <td colspan="5">HONG KONG YUEN SUM TAI LIMITED</td>
                                        </tr>
                                        <tr>
                                            <td colspan="5">(RMS 1804-05,18/F HUA QIN INT'L BLDG 340 QUEEN'S RD CENTRAL HK)
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
    laydate.render({
        elem: '#invoiceDate',
        format: 'yyyy/MM/dd'
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
</script>
