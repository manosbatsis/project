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
        text-align: center;
        font-weight: bolder;
        font-size: 30px;
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
    <div id="customerSalePriceAudit-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-dialog">

            <div class="modal-content" style="width: 560px;">
                <div class="header" >
                    <span class="header-title" ></span>
                    <button class="header-button" style="margin-left: 499px;"><span class="header-close" onclick="$m17001.cancel()"></span></button>
                </div>
                <div class="modal-body">
                    <input type="hidden" id="ids" name="ids" >
                    <div class="form-group" style="height: 200px;margin:0 auto;">
                        <div class="col-12" style="text-align: center;">
                            <label><span class="header-title">商品审核结果</span></label>
                           <div style=" text-align: center;font-weight: bolder;margin-top: 50px;">
                               <input type="radio" name="type" value="1" > 通过
                               &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                               <input type="radio" name="type" value="2" > 驳回
                           </div>
                        </div>
                    </div>
                    <div class="form-group" style="float: right;">
                        <div class="col-12">
                            <button class="btn btn-info waves-effect waves-light fr btn_default" type="button" onclick="$m17001.submit();">确定</button>
                            <button class="btn btn-light waves-effect waves-light btn_default" type="button" onclick="$m17001.cancel();">取消</button>
                        </div>
                    </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">
    var $m17001={
        init:function(ids){
            $('input:radio:first').attr('checked', 'checked');

            $("#ids").val(ids);
           /* var url = "/businessUnit/detail.asyn";
            $custom.request.ajax(url,{"id":id},function(data) {
                if (data.state == 200) {
                    $('#code').val(data.data.code);
                    $("#code").attr("disabled","disabled");
                    $('#name').val(data.data.name);
                    $('#id').val(data.data.id);
                } else {
                    $custom.alert.error(data.message);
                }
            });*/
            this.show();
        },
        submit:function(){
            var type = $("input[type='radio']:checked").val();
            var id = $('#ids').val();
            var dataJson = {"ids":id, "type":type} ;

            $custom.request.ajaxReqrCheck(
                $m17001.params.url,
                dataJson,
                function(result) {
                    if(result.data.code =='00') {
                        $custom.alert.success(result.data.message);
                        $m17001.hide();
                        setTimeout("$load.a('/customerSale/toPage.html');", 1000) ;
                    } else {
                        $custom.alert.error(result.data.message);
                        $m17001.hide();
                        return;
                    }
                },
                null,
                function() {
                    /*$module.revertList.serializeListForm() ;
                    $module.revertList.isMainList = true ;*/
                }
            );

        },
        cancel:function() {
            $m17001.hide();
        },
        show:function(){
            $($m17001.params.modalId).modal({backdrop: 'static', keyboard: false});
        },
        hide:function(){
            $($m17001.params.modalId).modal("hide");
        },
        params:{
            url:'/customerSale/auditSMPrice.asyn',
            //formId:'#addForm',
            modalId:'#customerSalePriceAudit-modal',
        }
    }


</script>