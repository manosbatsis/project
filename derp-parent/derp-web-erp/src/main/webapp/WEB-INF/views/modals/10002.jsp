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
        padding-right: 460px;
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
                    <span class="header-title" ></span>
                    <button class="header-button" ><span class="header-close" onclick="$m10002.cancel()"></span></button>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" id="addForm">
                    	<input type="hidden" id="id" name="id" >
                        <div class="form-group">
                            <div class="col-12">
                                <label >&nbsp;公司名称&nbsp;&nbsp;&nbsp;<i class="red">*</i>：</label>
                                <select name="merchantId" class="input_msg" id="merchantId" require reqrMsg="公司名称不能为空！">
                                        <option value="">请选择</option>
                                        <c:forEach items="${merchantList }" var="merchant">
                                            <option value="${merchant.value }">${merchant.label }</option>
                                        </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-12">
                                <label >事业部名称<i class="red">*</i>：</label>
                                <select name="modalBuId" class="input_msg" id="modalBuId" require reqrMsg="事业部名称不能为空！">
                                        <option value="">请选择</option>
                                        <c:forEach items="${businessUnitList }" var="businessUnit">
                                            <option value="${businessUnit.id }">${businessUnit.name }</option>
                                        </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-12">
                                <label >状态<i class="red">*</i>：</label>
                                <select name="modalStatus" class="input_msg" id="modalStatus" require reqrMsg="状态不能为空！">
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-12">
                                <label >采购价格管理<i class="red">*</i>：</label>
                                <select name="modalPurchasePriceManage" class="input_msg" id="modalPurchasePriceManage" require reqrMsg="采购价格管理不能为空！">
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-12">
                                <label >销售价格管理<i class="red">*</i>：</label>
                                <select name="modalSalePriceManage" class="input_msg" id="modalSalePriceManage" require reqrMsg="销售价格管理不能为空！">
                                </select>
                            </div>
                        </div>
                        <div class="form-group" style="float: right;">
                            <div class="col-12">
                                <button class="btn btn-info waves-effect waves-light fr btn_default" type="button" onclick="$m10002.submit();">确定</button>
                                <button class="btn btn-light waves-effect waves-light btn_default" type="button" onclick="$m10002.cancel();">取消</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">
    var $m10002={
        init:function(){
            $module.constant.getConstantListByNameURL.call($('select[name="modalPurchasePriceManage"]')[0],"merchant_bu_rel_purchase_price_manageList",'');
            $module.constant.getConstantListByNameURL.call($('select[name="modalStatus"]')[0],"merchant_bu_rel_statuslist",'');
            $module.constant.getConstantListByNameURL.call($('select[name="modalSalePriceManage"]')[0],"merchant_bu_rel_sale_price_manageList",'');

            //重置表单
            $($m10002.params.formId)[0].reset();
            	
           	$(".header-title").html("新建") ; 
           	
            this.show();
        },
        submit:function(){
            var merchantId = $('#merchantId').val();
            var buId = $('#modalBuId').val();
            var purchasePriceManage = $('#modalPurchasePriceManage').val();
            var purchasePriceManage = $('#modalStatus').val();
            var salePriceManage = $('#modalSalePriceManage').val();
            var dataJson = {"merchantId":merchantId, "buId":buId,"purchasePriceManage":purchasePriceManage,"status":status,"salePriceManage":salePriceManage} ;

            $custom.request.ajaxReqrCheck(

                $m10002.params.url,
                dataJson,
                function(data) {
                    if (data.state == 200) {
                        $custom.alert.success(data.message);
                        //重新加载table表格
                        setTimeout(function () {
                        	$load.a('/list/menu.asyn?act=1103');
                        }, 500);
                        $m10002.hide();
                    } else {
                        $custom.alert.error(data.expMessage);
                    }
                },
                null,
                function() {
                    $module.revertList.serializeListForm() ;
                    $module.revertList.isMainList = true ;
                }
            );

        },
        cancel:function() {
            $custom.cancelReqrCheck("你将关闭该界面，数据不会被保存，是否继续关闭？",function(){
                $m10002.hide();
            });
        },
        show:function(){
            $($m10002.params.modalId).modal({backdrop: 'static', keyboard: false});
        },
        hide:function(){
            $($m10002.params.modalId).modal("hide");
        },
        params:{
            url:'/merchantBuRel/saveMerchantBuRel.asyn',
            formId:'#addForm',
            modalId:'#signup-modal',
        }
    }


</script>