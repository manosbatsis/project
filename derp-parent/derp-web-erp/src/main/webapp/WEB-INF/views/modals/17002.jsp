<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<style>
    .header {
        background-color: #fff;
        color: #333;
        font-family: fantasy;
        border-top-left-radius: .3rem;
        border-top-right-radius: .3rem;
        display: flex;
        border-bottom: solid 1px #aaa;
    }
    .header-title{
        text-align: center;
        font-weight: bolder;
        font-size: 20px;
        margin-left: 30px;
        margin-top: 10px;
    }
</style>
<div>
    <!-- Signup modal content -->
    <div id="customerSalePriceEdit-modal"  class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true"
         style="display: none;margin-left: -170px;">
        <div class="modal-dialog" style="max-width: 1000px">
            <div class="modal-content" style="width: 1000px;height: 500px;line-height: 50px;">
                <div class="header">
                    <span class="header-title">编辑采购价格</span>
                    <button class="header-button" style="margin-left: 750px;font-size: 25px;"><span class="header-close" onclick="$m17002.cancel()"></span></button>
                </div>
                <div class="modal-body" >
                    <form class="form-horizontal" id="settlement-from">
                        <!-- 自定义参数  -->
                        <div class="form-group" style="padding-left: 21px;">
                            <div class="col-12">
                                <input type="hidden" class="input_msg" id="id" name="id" style="width: 202px;">
                                <label ><i class="red">*</i>公司 : </label>
                                <input type="text" readonly="readonly"  class="input_msg" id="merchantName" name="merchantName" style="width: 202px;background-color: #FFFFFF;">
                                <label ><i class="red">*</i>事业部 : </label>
                                <input type="text" readonly="readonly"  class="input_msg" id="buName" name="buName" style="width: 202px;background-color: #FFFFFF;">
                                <label style="padding-left: 11px;"><i class="red">*</i>客户编码 : </label>
                                <input type="text" readonly="readonly" class="input_msg" id="customerCode" name="customerCode" style="width: 202px;background-color: #FFFFFF;">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-12">
                                <label ><i class="red">*</i>标准条码: </label>
                                <input type="text" readonly="readonly" class="input_msg" id="commbarcodeVal" name="commbarcodeVal" style="width: 202px;background-color: #FFFFFF;">
                                <label ><i class="red">*</i>客户名称: </label>
                                <input type="text" readonly="readonly" class="input_msg" id="customerName" name="customerName"style="width: 202px;background-color: #FFFFFF;">
                                <label ><i class="red">*</i>商品名称: </label>
                                <input type="text"readonly="readonly"  class="input_msg" id="goodsNameVal" name="goodsNameVal" style="width: 202px;background-color: #FFFFFF;">
                            </div>
                        </div>
                        <div class="form-group" style="padding-left: 25px;">
                            <div class="col-12">
                                <label ><i class="red">*</i>品牌: </label>
                                <input type="text"readonly="readonly"  class="input_msg" id="brandName" name="brandName" style="width: 202px;background-color: #FFFFFF;">
                                <label ><i class="red">*</i>规格型号: </label>
                                <input type="text" readonly="readonly" class="input_msg" id="spec" name="spec" style="width: 202px;background-color: #FFFFFF;">
                                <label ><i class="red">*</i>币种: </label>
                                <select class="input_msg" name="currency" id="currency"  style="width: 202px;">
                                </select>
                            </div>
                        </div>

                        <div class="form-group" style="padding-left: 9px;">
                            <div class="col-12">
                                <label ><i class="red">*</i>销售价 : </label>
                                <input type="text"class="input_msg"  id="salePrice" name="salePrice" value="" style="width: 202px;">
                                <label ><i class="red">*</i>报价生效日期 : </label>
                                <input type="text" class="input_msg form_datetime date-input" name="effectiveDate" id="effectiveDate" >
                                <label ><i class="red">*</i>报价失效时间 : </label>
                                <input type="text" class="input_msg form_datetime date-input" name="expiryDate" id="expiryDate">
                            </div>
                        </div>

                    </form>
                    <div class="form-group" style="text-align: center">
                        <div class="col-12">
                            <button class="btn btn-info waves-effect waves-light btn_default" type="button" onclick="$m17002.submit();">保存</button>
                            <button class="btn btn-light waves-effect waves-light btn_default" type="button" onclick="$m17002.cancel();">取消</button>
                        </div>
                    </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">

    //日期格式
    $(".date-input").datetimepicker({
        language:  'zh-CN',
        format: "yyyy-mm-dd",
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        minView: 2
    });

    var $m17002={
        init:function(id,status){
            $("#id").val(id);
            $module.constant.getConstantListByNameURL.call($('select[name="currency"]')[0], "currencyCodeList", '');

            $custom.request.ajax($m17002.params.url,{"id":id},function(data) {
                console.log(data+'----------------------');
                if (data.state == 200) {
                    $("#merchantName").val(data.data.merchantName);
                    $("#buName").val(data.data.buName);
                    $("#customerCode").val(data.data.customerCode);
                    $("#commbarcodeVal").val(data.data.commbarcode);
                    $("#customerName").val(data.data.customerName);
                    $("#goodsNameVal").val(data.data.goodsName);
                    $("#brandName").val(data.data.brandName);
                    $("#spec").val(data.data.spec);
                    $("#salePrice").val(data.data.salePrice);
                    $("#spec").val(data.data.spec);
                    $("#currency").val(data.data.currency);
                    $("#effectiveDate").val(formatDate(data.data.effectiveDate,"yyyy-MM-dd"));
                    $("#expiryDate").val(formatDate(data.data.expiryDate, "yyyy-MM-dd"));
                    debugger
                } else {
                    $custom.alert.error(data.message);
                }
            });
            this.show();
        },
        submit:function(){
            var id= $("#id").val();
            var salePrice=$("#salePrice").val();
            var effectiveDate=$("#effectiveDate").val();
            var expiryDate=$("#expiryDate").val();
            var currency=$("#currency").val();
            if(!salePrice || salePrice <= 0){
                $custom.alert.error("销售价格不能为空或小于等于0");
                return;
            }
            if(isNaN(salePrice)){
                $custom.alert.error("销售价格请输入数字");
                return ;
            }
            var indexOf = salePrice.indexOf(".");
            if (indexOf != -1) {
                var count = (parseFloat(salePrice)+"").length - 1 - indexOf;
                if (count > 8) {
                    $(this).val("");
                    $custom.alert.error("销售价格只能为8位小数");
                    return;
                }
            }
            if(effectiveDate==null ||effectiveDate==""){
                $custom.alert.error("报价失效日期不能为空！");
                return;
            }
            if(expiryDate==null ||expiryDate==""){
                $custom.alert.error("报价生效日期不能为空！");
                return;
            }
            if(currency==null ||currency==""){
                $custom.alert.error("币种不能为空！");
                return;
            }
            $custom.request.ajaxReqrCheck('/customerSale/editSMPrice.asyn',{"id":id,"salePrice":salePrice,"effectiveDate":effectiveDate,"expiryDate":expiryDate,"currency":currency},
                function(result) {
                    if(result.data.code =='00') {
                        $custom.alert.success(result.data.message);
                        $m17002.hide();
                        setTimeout("$load.a('/customerSale/toPage.html');", 1000) ;
                    } else {
                        $custom.alert.error(result.data.message);
                        return;
                    }
                },null,function(){

                }
            );

        },
        cancel:function() {
            $m17002.hide();
        },
        show:function(){
            $($m17002.params.modalId).modal({backdrop: 'static', keyboard: false});
        },
        hide:function(){
            $($m17002.params.modalId).modal("hide");
        },
        params:{
            url:'/customerSale/getSalePriceDetails.asyn',
            modalId:'#customerSalePriceEdit-modal',
        }
    }


    function formatDate(date, fmt) {
        if (!date) {
            return '';
        }
        date = date;
        fmt=fmt || 'yyyy-MM-dd hh:mm:ss';
        if(Object.prototype.toString.call(date).slice(8,-1)!=='Date'){
            date=new Date(date)
        }
        if (/(y+)/.test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
        }
        let o = {
            'M+': date.getMonth() + 1,
            'd+': date.getDate(),
            'h+': date.getHours(),
            'm+': date.getMinutes(),
            's+': date.getSeconds()
        }
        for (let k in o) {
            if (new RegExp("(" + k + ")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
        return fmt
    }



</script>