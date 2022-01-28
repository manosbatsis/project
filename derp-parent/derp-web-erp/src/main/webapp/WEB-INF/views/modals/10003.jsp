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
    <div id="signup-editModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-dialog">

            <div class="modal-content" style="width: 560px;">
                <div class="header" >
                    <span class="header-title" ></span>
                    <button class="header-button" ><span class="header-close" onclick="$m10003.cancel()"></span></button>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" id="editForm">
                    	<input type="hidden" id="editId" name="editId" >
                        <div class="form-group">
                            <div class="col-12">
                                <label >&nbsp;公司名称&nbsp;&nbsp;&nbsp;<i class="red">*</i>：</label>
                                <input  disabled name="editMerchantId" class="input_msg" id="editMerchantId">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-12">
                                <label >事业部名称<i class="red">*</i>：</label>
                                <input disabled name="editModalBuId" class="input_msg" id="editModalBuId">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-12">
                                <label >状态<i class="red">*</i>：</label>
                                <select name="editModalStatus" class="input_msg" id="editModalStatus">
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-12">
                                <label >采购价格管理<i class="red">*</i>：</label>
                                <select name="editModalPurchasePriceManage" class="input_msg" id="editModalPurchasePriceManage">
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-12">
                                <label >销售价格管理<i class="red">*</i>：</label>
                                <select name="editModalSalePriceManage" class="input_msg" id="editModalSalePriceManage">
                                </select>
                            </div>
                        </div>
                        <div class="form-group" style="float: right;">
                            <div class="col-12">
                                <button class="btn btn-info waves-effect waves-light fr btn_default" type="button" onclick="$m10003.editSubmit();">确定</button>
                                <button class="btn btn-light waves-effect waves-light btn_default" type="button" onclick="$m10003.cancel();">取消</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">
    var $m10003={
        init:function(data){
            $module.constant.getConstantListByNameURL.call($('select[name="editModalPurchasePriceManage"]')[0],"merchant_bu_rel_purchase_price_manageList",data.purchasePriceManage);
            $module.constant.getConstantListByNameURL.call($('select[name="editModalStatus"]')[0],"merchant_bu_rel_statuslist",data.status);
            $module.constant.getConstantListByNameURL.call($('select[name="editModalSalePriceManage"]')[0],"merchant_bu_rel_sale_price_manageList",data.salePriceManage);

           	$(".header-title").html("编辑") ;
           	$("#editId").val(data.id);
            $("#editMerchantId").val(data.merchantName);
            $("#editModalBuId").val(data.buName);
            this.show();
        },
        editSubmit:function(){
            var editId = $('#editId').val();
            var purchasePriceManage = $('#editModalPurchasePriceManage').val();
            var status = $('#editModalStatus').val();
            var salePriceManage = $('#editModalSalePriceManage').val();
            var dataJson = {"id":editId,"purchasePriceManage":purchasePriceManage,"status":status,"salePriceManage":salePriceManage} ;
            if(!purchasePriceManage){
                $custom.alert.error("采购价格管理不能为空！");
                return ;
            }
            if(!status){
                $custom.alert.error("状态不能为空！");
                return ;
            }
            if(!salePriceManage){
                $custom.alert.error("销售价格管理不能为空！");
                return ;
            }
            $custom.request.ajax(
                $m10003.params.url,
                dataJson,
                function(data) {
                    if (data.state == 200) {
                        $custom.alert.success(data.message);
                        //重新加载table表格
                        setTimeout(function () {
                        	$load.a('/list/menu.asyn?act=1103');
                        }, 500);
                        $m10003.hide();
                    } else {
                        $custom.alert.error(data.expMessage);
                    }
                }
            );

        },
        cancel:function() {
            $custom.cancelReqrCheck("你将关闭该界面，数据不会被保存，是否继续关闭？",function(){
                $m10003.hide();
            });
        },
        show:function(){
            $($m10003.params.modalId).modal({backdrop: 'static', keyboard: false});
        },
        hide:function(){
            $($m10003.params.modalId).modal("hide");
            $("#editId").val("");
            $("#editMerchantId").val("");
            $("#editModalBuId").val("");
        },
        params:{
            url:'/merchantBuRel/modifyMerchantBuRel.asyn',
            formId:'#editForm',
            modalId:'#signup-editModal',
        }
    }


</script>