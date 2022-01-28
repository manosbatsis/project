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
        padding-left: 120px;
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

            <div class="modal-content" style="width: 680px;">
                <div class="header" >
                    <span class="header-title" >标识核对结果</span>
                    <button class="header-button" ><span class="header-close" onclick="$m17013.cancel()"></span></button>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" id="addForm">
                        <div class="form-group">
                                 <label style="padding-left: 52px;"><i class="red">*</i>核对结果：</label>
		                                <input type="radio"   name="checkResult1"  value="0" >未对平
		                                <input type="radio"   name="checkResult1" value="1" >已对平
		                                <input type="hidden" id="idTask" name="idTask">
                            </div>
                              <div class="form-group">
		                           <label style="padding-left: 52px;">核对备注：</label>     
		                           <textarea style="width: 100%;overflow: auto;word-break: break-all;"id="remark1"></textarea>
                            </div>
                        </div>
                        <div class="form-group" style="float: right;">
                            <div class="col-12">
                            <button class="btn btn-light waves-effect waves-light btn_default" type="button" onclick="$m17013.cancel();">取消</button>
                                <button class="btn btn-info waves-effect waves-light fr btn_default" type="button" onclick="$m17013.submit();">确定</button>
                                
                            </div>
                        </div>
                    </form>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">
    var $m17013={
        init:function(ids){
            //重置表单
            $($m17013.params.formId)[0].reset();
            $("#idTask").val(ids);
            this.show();
        },
        submit:function(){
            var checkResultVal=$('input:radio[name="checkResult1"]:checked').val();
            var checkResultText=$('input:radio[name="checkResult1"]:checked').text();	
            var remarkVal = $('#remark1').val();
            var ids = $('#idTask').val();
            var dataJson = {"ids":ids, "checkResult":checkResultVal,"remark":remarkVal} ;

            $custom.request.ajaxReqrCheck(
                $m17013.params.url,
                dataJson,
                function(data) {
                    if (data.state == 200) {
                        $custom.alert.success(data.message);
                        //成功隐藏
                        //重新加载table表格
                       $mytable.fn.reload();
                        $m17013.hide();
                    } else {
                        $custom.alert.error(data.message);
                    }
                },
                null,
                function() {
                         if(checkResultVal==null){
                        	 $custom.alert.error("请选择核对结果");
                             return false;
                         }

                    $module.revertList.serializeListForm() ;
                    $module.revertList.isMainList = true ;
                }
            );

        },
        cancel:function() {
            $custom.cancelReqrCheck("你将关闭该界面，数据不会被保存，是否继续关闭？",function(){
                $m17013.hide();
            });
        },
        show:function(){
            $($m17013.params.modalId).modal({backdrop: 'static', keyboard: false});
        },
        hide:function(){
            $($m17013.params.modalId).modal("hide");
        },
        params:{
            url: serverAddr+'/automaticCheckTask/modifyCheckResult.asyn',
            formId:'#addForm',
            modalId:'#signup-modal',
        }
    }
</script>