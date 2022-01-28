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
                    <span class="header-title" >修改货期</span>
                    <button class="header-button" ><span class="header-close" onclick="$m12003.cancel()"></span></button>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" id="addForm">
                        <div class="form-group">
                            <div class="col-12">
                                <label style="padding-left: 52px;">标准条码：</label>
                                <input class="input_span" type="text" name="commbarcode" id="commbarcode"   readonly="readonly">
                            </div>
                        </div>
                          <div class="form-group">
                            <div class="col-12">
                              <label style="padding-left: 52px;">商品名称：</label>
                                <input class="input_span" type="text" name="goodsName" id="goodsName"  readonly="readonly">
                            </div>
                            </div>
                          <div class="form-group">
                            <div class="col-12">
                             <label style="padding-left: 52px;">货期(月)<i class="red">*</i>：</label>
                                <input type="radio"   name="deliveryPeriod"  value="2" >2个月
                                <input type="radio"   name="deliveryPeriod" value="4" >4个月
                                <input type="hidden" id="id" name="id">
                            </div>
                            </div>

                        <div class="form-group" style="float: right;">
                            <div class="col-12">
                                <button class="btn btn-info waves-effect waves-light fr btn_default" type="button" onclick="$m12003.submit();">确定</button>
                                <button class="btn btn-light waves-effect waves-light btn_default" type="button" onclick="$m12003.cancel();">取消</button>
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
    var $m12003={
        init:function(flag, id){
            //重置表单
            $($m12003.params.formId)[0].reset();
            //判断新增/编辑
            if (flag == 0) { //新增
            } else if (flag ==1) { //编辑
                var url = serverAddr + "/goodsTimeConfig/detail.asyn";
                $custom.request.ajax(url,{"id":id},function(data) {
                    if (data.state == 200) {
                    	$('#id').val(data.data.id);
                        $('#commbarcode').val(data.data.commbarcode);
                        $('#goodsName').val(data.data.goodsName);
                        if(data.data.deliveryPeriod==2){
                        	$("input:radio[value='2']").attr("checked","checked")
                        }else if(data.data.deliveryPeriod==4){
                        	$("input:radio[value='4']").attr("checked","checked")
                        }
                    } else {
                        $custom.alert.error(data.message);
                    }
                });
            }
            this.show();
        },
        submit:function(){
        	var id = $('#id').val();
            var deliveryPeriod = $("input[name='deliveryPeriod']:checked").val();
            var dataJson = {"id":id,"deliveryPeriod":deliveryPeriod} ;
            
            $custom.request.ajaxReqrCheck(
                $m12003.params.url,
                dataJson,
                function(data) {
                    if (data.state == 200) {
                        $custom.alert.success(data.message);
                        //成功隐藏
                        setTimeout(function () {
                            $module.load.pageOrder("act=12003");
                        }, 500);
                        $m12003.hide();
                    } else {
                        $custom.alert.error(data.expMessage);
                    }
                },
                null,
                function() {
                    if (deliveryPeriod == null) {
                        $custom.alert.error("货期未选择");
                        return false;
                    }
                    $module.revertList.serializeListForm() ;
                    $module.revertList.isMainList = true ;
                }
            );

        },
        cancel:function() {
            $custom.cancelReqrCheck("你将关闭该界面，数据不会被保存，是否继续关闭？",function(){
                $m12003.hide();
            });
        },
        show:function(){
            $($m12003.params.modalId).modal({backdrop: 'static', keyboard: false})
        },
        hide:function(){
            $($m12003.params.modalId).modal("hide");
        },
        params:{
            url:serverAddr + '/goodsTimeConfig/modifyGoodsTimeConfig.asyn',
            formId:'#addForm',
            modalId:'#signup-modal',
        }
    }


</script>