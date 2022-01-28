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
                                            <td ></td>
                                            <td style="border-collapse: collapse; border-spacing: 0px; padding: 0px 0.2em; height: 40px; font-size: 18px; word-break: break-all;" colspan="3"><strong>健太有限公司</strong></td>
                                            <td colspan="2"></td>
                                        </tr>
                                        <tr>
                                            <td ></td>
                                            <td style="text-align: center; font-weight: bold; font-size: 18px; padding-bottom: 20px;"colspan="3">Q TOP<br>LIMITED</td>
                                            <td colspan="2"></td>
                                        </tr>
                                        <tr>
                                            <td ></td>
                                            <td style="text-align: center; font-weight: bold; font-size: 18px; padding-bottom: 20px; word-break: break-all; text-decoration: underline;" colspan="3">INVOICE</td>
                                            <td style="font-size: 12px; text-align: left;">INV No.</td>
                                            <td style="font-size: 12px; text-align: left;">：${invoiceNo}</td>
                                        </tr>

                                        <tr>
                                            <td colspan="4"></td>
                                            <td style="font-size: 12px; text-align: left;">Date</td>
                                            <td style="font-size: 12px; text-align: left;">：
                                                <input type="text" id="invoiceDate" name="detail.invoiceDate"
                                                       value="<fmt:formatDate value="${invoiceDate}" pattern="yyyy/MM/dd"/>" ></td>
                                        </tr>
                                        <tr>
                                            <td colspan="4" style="font-size: 12px; text-align: left;">
                                                <div style="width: 70px; display: inline-block;">CUSTOMER</div>
                                                <div style="display: inline-block;">：Bayer HealthCare Limited</div>
                                            </td>
                                            <td style="font-size: 12px; text-align: left;">Due date</td>
                                            <td style="font-size: 12px; text-align: left;">：
                                                <input type="text" id="dueDate" name="detail.dueDate"
                                                       value="<fmt:formatDate value="${dueDate}" pattern="yyyy/MM/dd"/>" ></td>
                                        </tr>
                                        <tr>
                                            <td colspan="4" style="font-size: 10px; text-align: left;">
                                                <div style="width: 70px; display: inline-block;">ADDRESS</div>
                                                <div style="display: inline-block;">：14/F, Oxford House, Taikoo Place, 979 King’s Road, </div>
                                            </td>
                                            <td style="font-size: 12px; text-align: left;">TEL</td>
                                            <td style="font-size: 12px; text-align: left;">：020-62981476</td>
                                        </tr>
                                        <tr>
                                            <td colspan="4" style="font-size: 10px; text-align: left;">
                                                <div style="width: 70px; display: inline-block;"></div>
                                                <div style="display: inline-block; padding-left: 15px;">Quarry Bay, Hong Kong</div>
                                            </td>
                                            <td style="font-size: 12px; text-align: left;">PO No.</td>
                                            <td style="font-size: 12px; text-align: left;">：<input type="text" name="detail.poNo" /></td>
                                        </tr>
                                        <tr>
                                            <td colspan="3" style="font-size: 10px; text-align: left;">
                                                <div style="width: 70px; display: inline-block;">ATTN</div>
                                                <div style="display: inline-block;">：</div>
                                            </td>
                                            <td style="font-size: 12px; text-align: left;" colspan="3"></td>
                                        </tr>
                                        <tr>
                                            <td colspan="3" style="font-size: 10px; text-align: left;">
                                                <div style="width: 150px; display: inline-block;">CUSTOMER CODE.</div>
                                                <div style="display: inline-block;">：4066998</div>
                                            </td>
                                            <td style="font-size: 12px; text-align: left;" colspan="3"></td>
                                        </tr>
                                        <tr>
                                            <td colspan="3" style="font-size: 10px; text-align: left;">
                                                <div style="width: 150px; display: inline-block;">DISCRIPTION</div>
                                                <div style="display: inline-block;">：Bayer E-commerce platforms promotion fee</div>
                                            </td>
                                            <td style="font-size: 12px; text-align: left;" colspan="3"></td>
                                        </tr>
                                        <tr>
                                            <td colspan="3" style="font-size: 10px; text-align: left;">
                                                <div style="width: 150px; display: inline-block;">DATE</div>
                                                <div style="display: inline-block;">：
                                                    <input type="text" name="detail.date" required reqMsg="DATE不能为空！">
                                                </div>
                                            </td>
                                            <td style="font-size: 12px; text-align: left;" colspan="3"></td>
                                        </tr>

                                        <tr>
                                            <td style="text-align: left; font-size: 12px;border-bottom:2px solid #000;">CHARGE ITEM</td>
                                            <td style="text-align: left; font-size: 12px;border-bottom:2px solid #000;">QTY</td>
                                            <td style="text-align: left; font-size: 12px;border-bottom:2px solid #000;">UNIT</td>
                                            <td style="text-align: center; font-size: 12px;border-bottom:2px solid #000;"  colspan="2">CHARGE RATE</td>
                                            <td style="text-align: right; font-size: 12px;border-bottom:2px solid #000;">AMOUNT(${currency})</td>
                                        </tr>

                                        <c:forEach items="${itemLists }" var="item">
                                            <c:choose>
                                                <c:when test="${item.totalAllAmount != null}">
                                                    <tr>
                                                        <td colspan="4" style="font-weight: bolder;"></td>
                                                        <td style="font-size: 12px;">Total</td>
                                                        <td style="text-align: right; word-break: break-all; word-wrap: break-word; font-size: 12px;border-bottom: double;border-top:#000 solid 1px;">${item.totalAllAmount}</td>
                                                        <input type="hidden" name="detail.totalAllAmount" value="${item.totalAllAmount}">
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <tr>
                                                        <td>${item.goodsName }</td>
                                                        <input type="hidden" name="goods.goodsName" value="${item.goodsName}">
                                                        <td>${item.totalNum }</td>
                                                        <input type="hidden" name="goods.totalNum" value="${item.totalNum}">
                                                        <td></td>
                                                        <td colspan="2">${item.totalPrice }</td>
                                                        <input type="hidden" name="goods.price" value="${item.totalPrice}">
                                                        <td>${item.totalPrice }</td>
                                                    </tr>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>

                                        <tr style="height: 20px; font-size: 12px; text-align: left;">
                                            <td colspan="6">BANK INFORMATION: -	</td>
                                        </tr>
                                        <tr style="height: 20px; font-size: 12px; text-align: left;">
                                            <td colspan="6">BENEFICIARY</td>
                                        </tr>
                                        <tr style="height: 20px; font-size: 12px; text-align: left;">
                                            <td colspan="6">Cheque should be crossed and make payable to "Q TOP LIMITED" by mail or forward directly to our bank account:</td>
                                        </tr>
                                        <tr style="height: 20px; font-size: 12px; text-align: left;">
                                            <td colspan="6">Bank Name: The Hongkong and Shanghai Banking Corporation Limited</td>
                                        </tr>
                                        <tr style="height: 20px; font-size: 12px; text-align: left;">
                                            <td colspan="6">Bank Address: NO.1 Queen's Road Central HONG KONG</td>
                                        </tr>
                                        <tr style="height: 20px; font-size: 12px; text-align: left;">
                                            <td colspan="6">Swift Code: HSBCHKHHHKH</td>
                                        </tr>
                                        <tr style="height: 20px; font-size: 12px; text-align: left;">
                                            <td colspan="6">HKD A/C NO.:124-341777-838</td>
                                        </tr>
                                        <tr style="height: 20px; font-size: 12px; text-align: right;">
                                            <td colspan="3"></td>
                                            <td colspan="3" style="border-top:1px dashed #000;">Authorized Signature(s)</td>
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

    laydate.render({
        elem: '#dueDate',
        format: 'yyyy/MM/dd'
    });
</script>
