<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
    .header-title {
        padding: 15px;
        padding-right: 520px;
        font-weight: bolder;
        font-size: 18px;
    }
    .header-button {
        background-color: #fff;
        border: none;
    }
    .header-close {
    }
    .header-close::after {
        content: "\2716";
    }
    .input_span {
        width: 180px;
    }
    .label_content {
        padding-left: 26px;
    }

</style>
<div>
    <!-- Signup modal content -->
    <div id="signup-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-dialog">

            <div class="modal-content" style="width: 680px;">
                <div class="header" >
                    <span class="header-title" >模型配置信息</span>
                    <button class="header-button" ><span class="header-close" onclick="$m12002.cancel()"></span></button>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" id="addForm">
                        <div class="form-group">
                            <div class="col-12">
                                <label class="label_content">公司名称<i class="red">*</i>：</label>
                                <select name="merchantId1" class="input_msg input_span" id="merchantId1" require reqrMsg="公司不能为空！">
                                    <option value="">请选择</option>
                                    <c:forEach items="${merchantList }" var="merchant">
                                        <option value="${merchant.value }">${merchant.label }</option>
                                    </c:forEach>
                                </select>
                                <label style="padding-left: 52px;">货期<i class="red">*</i>：</label>
                                <input class="input_span" type="text" name="deliveryPeriod" id="deliveryPeriod"
                                       onkeyup="value=value.replace(/[^\d]/g,'')" require reqrMsg="周期不能为空！" >
                                <input type="hidden" id="id" name="id">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-12">
                                <label class="label_content">采购周期<i class="red">*</i>：</label>
                                <input class="input_span" type="text" name="purchasingCycle" id="purchasingCycle"
                                       onkeyup="value=value.replace(/[^\d]/g,'')" require reqrMsg="采购周期不能为空！" >
                                <label class="label_content">生产周期<i class="red">*</i>：</label>
                                <input class="input_span" type="text" name="produceCycle" id="produceCycle"
                                       onkeyup="value=value.replace(/[^\d]/g,'')" require reqrMsg="生产周期不能为空！" >
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-12">
                                <label >安全库存天数<i class="red">*</i>：</label>
                                <input class="input_span" type="text" name="safetyStockDays" id="safetyStockDays"
                                       onkeyup="value=value.replace(/[^\d]/g,'')" require reqrMsg="安全库存天数不能为空！" >
                                <label class="label_content">预警天数<i class="red">*</i>：</label>
                                <input class="input_span" type="text" name="warnDays" id="warnDays"
                                       onkeyup="value=value.replace(/[^\d]/g,'')" require reqrMsg="预警天数不能为空！" >
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-12">
                                <label >日销统计类型<i class="red">*</i>：</label>
                                <select class="input_msg input_span" name="dailyStatisticalType" id="dailyStatisticalType" require reqrMsg="日销统计类型不能为空！">
                                    <option value="">请选择</option>
                                    <c:forEach items="${dailySaleTypes }" var="dailySaleType">
                                        <option value="${dailySaleType.value }">${dailySaleType.label }</option>
                                    </c:forEach>
                                </select>
                                <label>日销统计范围<i class="red">*</i>：</label>
                                <select class="input_msg input_span" name="dailyStatisticalRange" id="dailyStatisticalRange" require reqrMsg="日销统计范围不能为空！">
                                    <option value="">请选择</option>
                                    <c:forEach items="${dailySaleRanges }" var="dailySaleRange">
                                        <option value="${dailySaleRange.value }">${dailySaleRange.label }</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="form-group" style="float: right;">
                            <div class="col-12">
                                <button class="btn btn-info waves-effect waves-light fr btn_default" type="button" onclick="$m12002.submit();">确定</button>
                                <button class="btn btn-light waves-effect waves-light btn_default" type="button" onclick="$m12002.cancel();">取消</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">
    //重新加载select2
    $(".goods_select2").select2({
        language:"zh-CN",
        placeholder: '请选择',
        allowClear:true
    });
    var $m12002={
        init:function(flag, id){
            //重置表单
            $($m12002.params.formId)[0].reset();
//            $("#merchantId1 option:not(:first)").remove();
            //判断新增/编辑
            if (flag == 0) { //新增
                //加载公司
                <%--var merchantId = '${merchantId}';--%>
//                $module.merchantAll.loadSelectData.call($("select[name='merchantId1']")[0], merchantId);
            } else if (flag ==1) { //编辑
                var url = serverAddr + "/modelSetting/detail.asyn";
                $custom.request.ajax(url,{"id":id},function(data) {
                    if (data.state == 200) {
                        $('#merchantId1').val(data.data.merchantId);
                        $('#deliveryPeriod').val(data.data.deliveryPeriod);
                        $('#purchasingCycle').val(data.data.purchasingCycle);
                        $('#produceCycle').val(data.data.produceCycle);
                        $('#safetyStockDays').val(data.data.safetyStockDays);
                        $('#warnDays').val(data.data.warnDays);
                        $('#id').val(data.data.id);
                        $('#dailyStatisticalRange').val(data.data.dailyStatisticalRange);
                        $('#dailyStatisticalType').val(data.data.dailyStatisticalType);

                    } else {
                        $custom.alert.error(data.message);
                    }
                });
            }
            this.show();
        },
        submit:function(){
            var merchantId1 = $('#merchantId1').val();
            var deliveryPeriod = $('#deliveryPeriod').val();
            var purchasingCycle = $('#purchasingCycle').val();
            var produceCycle = $('#produceCycle').val();
            var safetyStockDays = $('#safetyStockDays').val();
            var warnDays = $('#warnDays').val();
            var id = $('#id').val();
            var dailyStatisticalRange = $('#dailyStatisticalRange').val();
            var dailyStatisticalType = $('#dailyStatisticalType').val();
            var dataJson = {"id":id, "merchantId":merchantId1,"deliveryPeriod":deliveryPeriod,"purchasingCycle":purchasingCycle,
                "produceCycle":produceCycle, "safetyStockDays":safetyStockDays, "warnDays": warnDays,
                "dailyStatisticalRange":dailyStatisticalRange, "dailyStatisticalType":dailyStatisticalType} ;

            $custom.request.ajaxReqrCheck(

                $m12002.params.url,
                dataJson,
                function(data) {
                    if (data.state == 200) {
                        $custom.alert.success(data.message);
                        //成功隐藏
                        setTimeout(function () {
                            $module.load.pageOrder("act=12002");
                        }, 500);
                        $m12002.hide();
                    } else {
                        $custom.alert.error(data.expMessage);
                    }
                },
                null,
                function() {
                    if (deliveryPeriod > 99 || deliveryPeriod < 0) {
                        $custom.alert.error("货期取值区间为[0~99]");
                        return false;
                    }
                    if (purchasingCycle > 99 || purchasingCycle < 0) {
                        $custom.alert.error("采购周期取值区间为[0~99]");
                        return false;
                    }
                    if (produceCycle > 99 || produceCycle < 0) {
                        $custom.alert.error("生产周期取值区间为[0~99]");
                        return false;
                    }
                    if (safetyStockDays > 99 || safetyStockDays < 0) {
                        $custom.alert.error("安全库存天数取值区间为[0~99]");
                        return false;
                    }
                    if (warnDays > 99 || warnDays < 0) {
                        $custom.alert.error("预警天数取值区间为[0~99]");
                        return false;
                    }
                    $module.revertList.serializeListForm() ;
                    $module.revertList.isMainList = true ;
                }
            );

        },
        cancel:function() {
            $custom.cancelReqrCheck("你将关闭该界面，数据不会被保存，是否继续关闭？",function(){
                $m12002.hide();
            });
        },
        show:function(){
            $($m12002.params.modalId).modal({backdrop: 'static', keyboard: false})
        },
        hide:function(){
            $($m12002.params.modalId).modal("hide");
        },
        params:{
            url:serverAddr + '/modelSetting/saveSetting.asyn',
            formId:'#addForm',
            modalId:'#signup-modal',
        }
    }


</script>