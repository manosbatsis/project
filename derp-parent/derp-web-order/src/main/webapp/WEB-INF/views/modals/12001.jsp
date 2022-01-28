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
        padding-right: 400px;
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

</style>
<div>
    <!-- Signup modal content -->
    <div id="signup-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-dialog">

            <div class="modal-content" style="width: 560px;">
                <div class="header" >
                    <span class="header-title" >促销信息配置</span>
                    <button class="header-button" ><span class="header-close" onclick="$m12001.cancel()"></span></button>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" id="addForm">
                        <div class="form-group">
                            <div class="col-12">
                                <label >公司名称<i class="red">*</i>：</label>
                                <select name="merchantId1" class="input_msg" id="merchantId1" require reqrMsg="公司不能为空！">
                                    <option value="">请选择</option>
                                    <c:forEach items="${merchantList }" var="merchant">
                                        <option value="${merchant.value }">${merchant.label }</option>
                                    </c:forEach>
                                </select>
                                <label >平台名称<i class="red">*</i>：</label>
                                <select class="input_msg" name="storePlatformCode1" id="storePlatformCode1" require reqrMsg="平台名称不能为空！">
                                    <option value="">请选择</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-12">
                                <label >促销名称<i class="red">*</i>：</label>
                                <input style="width: 360px;" type="text" name="promotionName" id="promotionName" require reqrMsg="促销名称不能为空！" >
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-12">
                                <label >促销日期<i class="red">*</i>：</label>
                                <input type="text" style="width: 160px;" class="input_msg form_datetime date-input"
                                       name="startDate" id="startDate" require reqrMsg="促销日期不能为空！">
                                ——
                                <input type="text" style="width: 160px;" class="input_msg form_datetime date-input"
                                       name="endDate" id="endDate" require reqrMsg="促销日期不能为空！">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-12">
                                <label >促销投入（万元）<i class="red">*</i>：</label>
                                <input type="text" name="promotionInvestment" id="promotionInvestment" style="width: 308px;"
                                       onkeyup="value=value.replace(/[^\d\.]/g,'')" require reqrMsg="促销投入不能为空！" >
                                <input type="hidden" id="id" >
                            </div>
                        </div>
                        <div class="form-group" style="float: right;">
                            <div class="col-12">
                                <button class="btn btn-info waves-effect waves-light fr btn_default" type="button" onclick="$m12001.submit();">确定</button>
                                <button class="btn btn-light waves-effect waves-light btn_default" type="button" onclick="$m12001.cancel();">取消</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">
    var $m12001={
        init:function(flag, id){
            //重置表单
            $($m12001.params.formId)[0].reset();
            $("#storePlatformCode1 option:not(:first)").remove();
            //判断新增/编辑
            if (flag == 0) {
                //加载电商平台
                $module.constant.getConstantListByNameURL.call($('select[name="storePlatformCode1"]')[0],"storePlatformList",null);

            } else if (flag ==1) {
                var url = serverAddr + "/promotionRecord/detail.asyn";

                $custom.request.ajax(url,{"id":id},function(data) {
                    if (data.state == 200) {
                        $module.shopCode.loadSelectData.call($("select[name='storePlatformCode1']")[0], data.data.storePlatformCode);
                        $('#startDate').val(data.data.startDate);
                        $('#endDate').val(data.data.endDate);
                        $('#promotionName').val(data.data.promotionName);
                        $('#promotionInvestment').val(data.data.promotionInvestment);
                        $('#id').val(data.data.id);
                        $('#merchantId1').val(data.data.merchantId);
                    } else {
                        $custom.alert.error(data.message);
                    }
                });
            }
            this.show();
        },
        submit:function(){
            var merchantId1 = $('#merchantId1').val();
            var storePlatformCode1 = $('#storePlatformCode1').val();
            var promotionName = $('#promotionName').val();
            var startDate = $('#startDate').val();
            var endDate = $('#endDate').val();
            var promotionInvestment = $('#promotionInvestment').val();
            var id = $('#id').val();
            var dataJson = {"id":id, "merchantId":merchantId1,"storePlatformCode":storePlatformCode1,"promotionName":promotionName,
                "startDateStr":startDate, "endDateStr":endDate, "promotionInvestment": promotionInvestment} ;

            $custom.request.ajaxReqrCheck(

                $m12001.params.url,
                dataJson,
                function(data) {
                    if (data.state == 200) {
                        $custom.alert.success(data.message);
                        //成功隐藏

                        //重新加载table表格
//                        $mytable.fn.reload();
                        setTimeout(function () {
                            $module.load.pageOrder("act=12001");
                        }, 500);
                        $m12001.hide();
                    } else {
                        $custom.alert.error(data.message);
                    }
                },
                null,
                function() {
                    if (promotionName.length > 20) {
                        $custom.alert.error("促销名称长度不能超过20个字");
                        return false;
                    }
                    if (promotionInvestment > 9999.99 || promotionInvestment < 0) {
                        $custom.alert.error("促销投入区间为[0.00~9999.99]");
                        return false;
                    }
                    var startTime= new Date(Date.parse(startDate));
                    var endTime=new Date(Date.parse(endDate));
                    if (startTime > endTime) {
                        $custom.alert.error("促销日期开始时间大于结束时间！");
                        return false;
                    }
                    $module.revertList.serializeListForm() ;
                    $module.revertList.isMainList = true ;
                }
            );

        },
        cancel:function() {
            $custom.cancelReqrCheck("你将关闭该界面，数据不会被保存，是否继续关闭？",function(){
                $m12001.hide();
            });
        },
        show:function(){
//            $($m12001.params.modalId).modal("show")
            $($m12001.params.modalId).modal({backdrop: 'static', keyboard: false});
        },
        hide:function(){
            $($m12001.params.modalId).modal("hide");
        },
        params:{
            url:serverAddr + '/promotionRecord/saveRecord.asyn',
            formId:'#addForm',
            modalId:'#signup-modal',
        }
    }


</script>