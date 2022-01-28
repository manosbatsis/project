<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
        width: 1000px;
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
    .span_input {
        width: 100px;
    }
    .totalPrice_input {
        width: 100px;
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
                                <div style="overflow-x: auto;">
                                    <input type="hidden" name="detail.codes" value="${codes}">
                                    <input type="hidden" name="detail.ids" value="${ids}">
                                    <input type="hidden" name="detail.tempId" value="${tempId}">
                                    <input type="hidden" name="detail.fileTempCode" value="${fileTempCode}">
                                    <input type="hidden" name="detail.currency" value="${currency}">
                                    <input type="hidden" name="detail.customerId" value="${customerId}">
                                    <input type="hidden" name="detail.customerName" value="${customerInfo.name}">
                                    <table class="pure-table pure-table-bordered" width="1000">
                                        <tr style="text-align:center;font-weight: bolder;">
                                            <td style="font-size: 24px;font-style: italic;">Supplier<br>（${merchantInfo.englishName}）</td>
                                            <input type="hidden" name="detail.merchantEnName" value="${merchantInfo.englishName}">
                                            <td colspan="9" style="font-size: 14px;text-align: left;">
                                                Address:(${merchantInfo.englishRegisteredAddress})<br><br>
                                                <input type="hidden" name="detail.englishRegisteredAddress" value="${merchantInfo.englishRegisteredAddress}">
                                                Contact:（jing.lin@topideal.com.cn/020-62262741）
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan="10" style="font-size: 36px; text-align: center;">COMMERCIAL INVOICE</td>
                                        </tr>
                                        <tr>
                                            <td style="text-align:left;font-weight:bold;font-size: 16px; text-decoration:underline;">BILL TO (BUYER’S COMPANY NAME)</td>
                                            <td colspan="9"></td>
                                        </tr>
                                        <tr>
                                            <td style="text-align: left;">${customerInfo.enName}</td>
                                            <input type="hidden" name="detail.customerEnName" value="${customerInfo.enName}">
                                            <td colspan="9" style="text-align: left;">INVOICE DATE :
                                                <input type="text" id="invoiceDate" name="detail.date" value="<fmt:formatDate value="${invoiceDate}"
                                                                pattern="yyyy/MM/dd"/>" >
                                            </td>
                                        </tr>
                                        <tr>
                                            <td style="text-align: left;">Address：${customerInfo.enBusinessAddress}</td>
                                            <input type="hidden" name="detail.enBusinessAddress" value="${customerInfo.enBusinessAddress}">
                                            <td colspan="9" style="text-align: left;"></td>
                                        </tr>
                                        <tr>
                                            <td></td>
                                            <td colspan="9" style="text-align: left;">INVOICE NUMBER:</td>
                                        </tr>
                                        <tr>
                                            <td style="text-align: left;">Attn: </td>
                                            <td colspan="9" style="text-align: left;"></td>
                                        </tr>
                                        <tr>
                                            <td style="text-align: left;">Contact：siyi.lu@vipshop.com </td>
                                            <td colspan="9" style="text-align: left;"></td>
                                        </tr>
                                        <tr>
                                            <td colspan="10" style="font-size: 20px;text-decoration:underline; text-align: left; padding-top: 20px;">INVOICE PARTICULARS</td>
                                        </tr>

                                        <tr>
                                            <td>Item Description</td>
                                            <td>Quantity</td>
                                            <td>Unit Price</td>
                                            <td>客退补贴Amount Of Customer Return Allowance</td>
                                            <td>客退折让Promotion DiscountsCustomer Return Details Amount</td>
                                            <td>活动折扣Promotion Discounts</td>
                                            <td>补偿折扣Extra discount</td>
                                            <td>返利金额Extra discount Amount</td>
                                            <td>Currency</td>
                                            <td>Amount</td>
                                        </tr>

                                        <c:forEach items="${itemLists }" var="item">
                                            <c:choose>
                                                <c:when test="${item.totalAllAmount != null}">
                                                    <tr>
                                                        <td colspan="3" style="font-weight: bolder;"></td>
                                                        <td> <input id="customerReturnAmount" readonly class="totalPrice_input" type="text" value="${item.customerReturnAmount}" ></td>
                                                        <td> <input id="promotionDiscountsCustAmount" readonly class="totalPrice_input" type="text"  value="${item.promotionDiscountsCustAmount}" ></td>
                                                        <td> <input id="promotionDiscountsAmount" readonly class="totalPrice_input" type="text" value="${item.promotionDiscountsAmount}" ></td>
                                                        <td> <input id="extraDiscountAmount" readonly class="totalPrice_input" type="text"  value="${item.extraDiscountAmount}" ></td>
                                                        <td> <input id="extraAmountAmount" readonly class="totalPrice_input" type="text"  value="${item.extraAmountAmount}" ></td>
                                                        <td>Total(${currency})</td>
                                                        <td> <input id="totalAllAmount" readonly class="totalPrice_input" type="text" name="detail.totalAllAmount" value="${item.totalAllAmount}" ></td>
                                                    </tr>
                                                </c:when>
                                                <c:when test="${item.brandName != null}">
                                                    <tr bgcolor="#cfe6f5">
                                                        <td>${item.brandName }</td>
                                                        <input type="hidden" name="goods.goodsName" value="${item.brandName}">
                                                        <td>${item.totalNum }<input type="hidden" name="goods.totalNum" value="${item.totalNum}"></td>
                                                        <td>${item.price }<input type="hidden" name="goods.price" value="${item.price}"></td>
                                                        <td><input class="span_input" readonly type="text" name="goods.customerReturn" value="${item.customerReturn=='0.00' ? '' :  item.customerReturn}" ></td>
                                                        <td><input class="span_input" readonly type="text" name="goods.promotionDiscountsCust" value="${item.promotionDiscountsCust=='0.00' ? '' :  item.promotionDiscountsCust }" ></td>
                                                        <td><input class="span_input" readonly type="text" name="goods.promotionDiscounts" value="${item.promotionDiscounts=='0.00' ? '' :  item.promotionDiscounts }" ></td>
                                                        <td><input class="span_input" readonly type="text" name="goods.extraDiscount" value="${item.extraDiscount=='0.00' ? '' :  item.extraDiscount }" ></td>
                                                        <td><input class="span_input" readonly type="text" name="goods.extraAmount" value="${item.extraAmount=='0.00' ? '' :  item.extraAmount }" ></td>
                                                        <td>${currency }</td>
                                                        <input type="hidden" name="goods.currency" value="${currency}">
                                                        <td><input class="totalPrice_input" readonly type="text" name="goods.totalPrice" value="${item.totalPrice}"></td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <tr>
                                                        <td>${item.goodsName }</td>
                                                        <input type="hidden" name="goods.goodsName" value="${item.goodsName}">
                                                        <td>${item.totalNum }<input type="hidden" name="goods.totalNum" value="${item.totalNum}"></td>
                                                        <td>${item.price }<input type="hidden" name="goods.price" value="${item.price}"><input type="hidden" value="${item.exactPrice}"></td>
                                                        <td><input class="span_input" type="text" name="goods.customerReturn" value="${item.customerReturn=='0.00' ? '' :  item.customerReturn}" onchange="sumPrice(this)"></td>
                                                        <td><input class="span_input" type="text" name="goods.promotionDiscountsCust" value="${item.promotionDiscountsCust=='0.00' ? '' :  item.promotionDiscountsCust }" onchange="sumPrice(this)"></td>
                                                        <td><input class="span_input" type="text" name="goods.promotionDiscounts" value="${item.promotionDiscounts=='0.00' ? '' :  item.promotionDiscounts }" onchange="sumPrice(this)"></td>
                                                        <td><input class="span_input" type="text" name="goods.extraDiscount" value="${item.extraDiscount=='0.00' ? '' :  item.extraDiscount }" onchange="sumPrice(this)"></td>
                                                        <td><input class="span_input" type="text" name="goods.extraAmount" value="${item.extraAmount=='0.00' ? '' :  item.extraAmount }" onchange="sumPrice(this)" ></td>
                                                        <td>${currency }</td>
                                                        <input type="hidden" name="goods.currency" value="${currency}">
                                                        <td><input class="totalPrice_input" readonly type="text" name="goods.totalPrice" value="${item.totalPrice}" ></td>
                                                    </tr>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>

                                        <tr>
                                            <td colspan="10" style="text-align:left;font-weight:bold;font-size: 14px; text-decoration:underline;" >Notes</td>
                                        </tr>
                                        <tr>
                                            <td colspan="10" style="text-align: left;font-size: 12px;">(1) Order will only be confirmed once the buyer has settled full payment</td>
                                        </tr>
                                        <tr>
                                            <td colspan="10" style="text-align: left;font-size: 12px;padding-bottom: 20px;">(2) Any bank charges or extra expenses incurred on behalf of the Buyer shall be borne by the Buyer.</td>
                                        </tr>
                                        <tr>
                                            <td colspan="10" style="text-align:left;font-weight:bold;font-size: 14px; " >For and on behalf of:</td>
                                        </tr>
                                        <tr>
                                            <td colspan="10" style="text-align:left;font-weight:bold;font-size: 14px; padding-bottom: 20px;" >(Seller) Supplier A Co. Ltd
                                                <span>(${merchantInfo.englishName})</span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan="10" style="text-align:left;font-weight:bold;font-size: 16px; " >BANK DETAILS</td>
                                        </tr>
                                        <tr>
                                            <td colspan="10" style="text-align:left;font-size: 16px; " >BANK NAME：<span>${merchantInfo.depositBank }</span></td>
                                            <input type="hidden" name="detail.depositBank" value="${merchantInfo.depositBank}">
                                        </tr>
                                        <tr>
                                            <td colspan="10" style="text-align:left;font-size: 16px; " ><span>${merchantInfo.bankAddress }</span></td>
                                            <input type="hidden" name="detail.bankAddress" value="${merchantInfo.bankAddress}">
                                        </tr>
                                        <tr>
                                            <td colspan="10" style="text-align:left;font-size: 16px; " >ACCOUNT NO.<span>${merchantInfo.bankAccount }</span></td>
                                            <input type="hidden" name="detail.bankAccount" value="${merchantInfo.bankAccount}">
                                        </tr>
                                        <tr>
                                            <td colspan="10" style="text-align:left;font-size: 16px; padding-bottom: 20px;" >SWIFT CODE<span> ${merchantInfo.swiftCode }</span></td>
                                            <input type="hidden" name="detail.swiftCode" value="${merchantInfo.swiftCode}">
                                        </tr>
                                        <tr>
                                            <td colspan="10" style="text-align: left;font-size: 12px;border-top: 1px solid #000000;">Authorized Signature, company chop & date</td>
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
    
    function calTotal(obj) {
        var val = $(obj).val();
        var changeVal = val.split(",");
        var aVal = changeVal.join("");//注意拼接字符串

        var regPos = /^(-?\d+)(\.\d+)?$/;
        if(!regPos.test(aVal)){
            $custom.alert.error("请填写正确的数值");
            $(obj).val('');
            return;
        }else{
            $(obj).val(formatQ(aVal));
        }




    }

    /**
     * 校验只要是数字（包含整数，0以及浮点数）就返回true
     **/
    function isNumber(obj){
        var val = $(obj).val();
        var changeVal = val.split(",");
        var aVal = changeVal.join("");//注意拼接字符串

        var regPos = /^(-?\d+)(\.\d+)?$/;
        if(!regPos.test(aVal)){
            $custom.alert.error("请填写正确的数值");
            $(obj).val('');
            return;
        }else{
            $(obj).val(formatQ(aVal));
        }

    }

    /**
     * 数字格式转换成千分位
     *@param{Object}num
     */
    function formatQ(num){
        if((num+"").trim()==""){
            return "";
        }
        if(isNaN(num)){
            return "";
        }
        num = num+"";
        if(/^.*\..*$/.test(num)){
            var pointIndex =num.lastIndexOf(".");
            var intPart = num.substring(0,pointIndex);
            var pointPart =num.substring(pointIndex+1,num.length);
            intPart = intPart +"";
            var re =/(-?\d+)(\d{3})/
            while(re.test(intPart)){
                intPart =intPart.replace(re,"$1,$2")
            }
            num = intPart+"."+pointPart;
        }else{
            num = num +"";
            var re =/(-?\d+)(\d{3})/
            while(re.test(num)){
                num =num.replace(re,"$1,$2")
            }
        }
        return num;
    }


    function sumPrice(obj) {
        var val = $(obj).val();
        var changeVal = val.split(",");
        var aVal = changeVal.join("");//注意拼接字符串

        var regPos = /^(-?\d+)(\.\d+)?$/;
        if(!regPos.test(aVal)){
            $custom.alert.error("请填写正确的数值");
            $(obj).val('');
            return;
        }else{
            $(obj).val(formatQ(aVal));
        }


        var tdIndex = $(obj).parent().index(); //当前列
        var trObj = $(obj).parents("tr"); //当前行
        var num = formatNum($(trObj).find("td").eq(1).find("input").val());
        var exactPrice = formatNum($(trObj).find("td").eq(2).find("input").eq(1).val());
        var customerReturn = formatNum($(trObj).find("td").eq(3).find("input").val());
        var promotionDiscountsCust = formatNum($(trObj).find("td").eq(4).find("input").val());
        var promotionDiscounts = formatNum($(trObj).find("td").eq(5).find("input").val());
        var extraDiscount = formatNum($(trObj).find("td").eq(6).find("input").val());
        var extraAmount = formatNum($(trObj).find("td").eq(7).find("input").val());
        var preTotalPrice = formatNum($(trObj).find("td").eq(9).find("input").val());
        var totalPrice = (exactPrice*num).add(parseNum(customerReturn)).add(parseNum(promotionDiscountsCust)).add(parseNum(promotionDiscounts)).add(parseNum(extraDiscount)).add(parseNum(extraAmount));
        $(trObj).find("td").eq(9).find("input").val(formatQ(totalPrice));
        var totalAllAmount = formatNum($("#totalAllAmount").val());
        var diffVal = parseNum(totalPrice).sub(parseNum(preTotalPrice));
        var finalAllAmount = parseNum(formatNum(totalAllAmount)).add(diffVal);
        $("#totalAllAmount").val(formatQ(finalAllAmount));

        var tempObj = trObj.next();
        while ($(tempObj).find("td").eq(0).text().indexOf("-合计") == -1) {
            tempObj = tempObj.next();
        }

        //品牌合计修改
        var preTotVal = $(tempObj).find("td").eq(tdIndex-1).find("input").val();
        var totVal = parseNum(diffVal).add(formatNum(preTotVal));
        $(tempObj).find("td").eq(tdIndex-1).find("input").val(formatQ(totVal));
        //品牌合计的合计总额修改
        var preTotalVal = $(tempObj).find("td").eq(9).find("input").val();
        var totalVal = parseNum(diffVal).add(formatNum(preTotalVal));
        $(tempObj).find("td").eq(9).find("input").val(formatQ(totalVal));


        //最后一行所有合计
        if (tdIndex == 4) {
            var customerReturnAmount = formatNum($("#customerReturnAmount").val());
            var customerReturnAllAmount = parseNum(formatNum(customerReturnAmount)).add(diffVal);
            $("#customerReturnAmount").val(formatQ(customerReturnAllAmount));
        }

        if (tdIndex == 5) {
            var promotionDiscountsCustAmount = formatNum($("#promotionDiscountsCustAmount").val());
            var promotionDiscountsCustAllAmount = parseNum(formatNum(promotionDiscountsCustAmount)).add(diffVal);
            $("#promotionDiscountsCustAmount").val(formatQ(promotionDiscountsCustAllAmount));
        }

        if (tdIndex == 6) {
            var promotionDiscountsAmount = formatNum($("#promotionDiscountsAmount").val());
            var promotionDiscountsAllAmount = parseNum(formatNum(promotionDiscountsAmount)).add(diffVal);
            $("#promotionDiscountsAmount").val(formatQ(promotionDiscountsAllAmount));
        }

        if (tdIndex == 7) {
            var extraDiscountAmount = formatNum($("#extraDiscountAmount").val());
            var extraDiscountAllAmount = parseNum(formatNum(extraDiscountAmount)).add(diffVal);
            $("#extraDiscountAmount").val(formatQ(extraDiscountAllAmount));
        }

        if (tdIndex == 8) {
            var extraAmountAmount = formatNum($("#extraAmountAmount").val());
            var extraAmountAllAmount = parseNum(formatNum(extraAmountAmount)).add(diffVal);
            $("#extraAmountAmount").val(formatQ(extraAmountAllAmount));
        }
    }


    //判断数值，为空返回0， 转化千位符
    function formatNum(num) {
        if (!num) {
            return 0;
        }
        num = num.replace(/,/gi,'');
        return num;
    }

    function parseNum(num) {
        if (num) {
            return parseFloat(num);
        }
        return 0;
    }

    function accSub(arg1,arg2){
        var r1,r2,m,n;
        try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
        try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
        m=Math.pow(10,Math.max(r1,r2));
        //last modify by deeka
        //动态控制精度长度
        n=(r1>=r2)?r1:r2;
        return ((arg1*m-arg2*m)/m).toFixed(n);
    }

    function accAdd(arg1, arg2) {

        var r1, r2, m, c;

        try { r1 = arg1.toString().split(".")[1].length } catch (e) { r1 = 0 }

        try { r2 = arg2.toString().split(".")[1].length } catch (e) { r2 = 0 }

        c = Math.abs(r1 - r2);
        m = Math.pow(10, Math.max(r1, r2))
        if (c > 0) {
            var cm = Math.pow(10, c);
            if (r1 > r2) {
                arg1 = Number(arg1.toString().replace(".", ""));
                arg2 = Number(arg2.toString().replace(".", "")) * cm;
            }
            else {
                arg1 = Number(arg1.toString().replace(".", "")) * cm;
                arg2 = Number(arg2.toString().replace(".", ""));
            }
        }
        else {
            arg1 = Number(arg1.toString().replace(".", ""));
            arg2 = Number(arg2.toString().replace(".", ""));
        }
        return (arg1 + arg2) / m

    }

    Number.prototype.add = function(arg) {
        return accAdd(arg, this);
    }

    Number.prototype.sub = function(arg) {
        return accSub(this, arg);
    }

</script>
