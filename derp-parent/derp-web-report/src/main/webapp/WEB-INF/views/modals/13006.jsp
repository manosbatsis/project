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
 	#signup-modal {
        left: -30px;
        top: 130px;
    }
</style>
<div>
    <!-- Signup modal content -->
    <div id="signup-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-dialog">

            <div class="modal-content" style="width: 500px;">
                <div class="header" >
                    <span class="header-title" >审核</span>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" id="addForm">
                    	<input type="hidden" id="id" name="id" >
                        <div class="form-group">
                            <div class="col-12">
                                <label >审批操作<i class="red">*</i>：</label>
                            	<input type='radio' name='status' value='032'>通过
                            	<input type='radio' name='status' value='033'>不通过
                            </div>
                        </div>
                        <div class="form-group">
                            <font style="color:red">请再次确认审批操作!</font>
                        </div>
                        <div class="form-group" style="float: right;">
                            <div class="col-12">
                                <button class="btn btn-info waves-effect waves-light fr btn_default" type="button" onclick="$m13006.submit();">确定</button>
                                <button class="btn btn-light waves-effect waves-light btn_default" type="button" onclick="$m13006.cancel();">取消</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">
	
	
    var $m13006={
        init:function(){
            //重置表单
            $($m13006.params.formId)[0].reset();
            
            this.show();
        },
        submit:function(){
        	
            //保存数据
            saveData() ;

        },
        cancel:function() {
            $custom.cancelReqrCheck("你将关闭该界面，数据不会被保存，是否继续关闭？",function(){
                $m13006.hide();
            });
        },
        show:function(){
            $($m13006.params.modalId).modal({backdrop: 'static', keyboard: false});
        },
        hide:function(){
            $($m13006.params.modalId).modal("hide");
        },
        params:{
            url:serverAddr + '/settlementPrice/examine.asyn',
            formId:'#addForm',
            modalId:'#signup-modal',
        }
    }

    /*
    *保存数据
    */
    function saveData(){
    	
    	var ids=$mytable.fn.getCheckedRow();
    	var status = $("input[name='status']:checked").val();
    	
    	var dataJson = {"ids" : ids ,"status" : status }
    	
    	$custom.request.ajaxReqrCheck(
    			$m13006.params.url,
                dataJson,function(data) {
                    if (data.state == 200) {
                        $m13006.hide();
                        $custom.alert.success(data.message);
                        //重新加载table表格
                        setTimeout(function () {
                        	$module.load.pageReport("act=13006");
                        }, 500);
                    } else {
                    	if(data.state == 305){
	                        $custom.alert.error(data.expMessage);
                    	}else{
                    		$custom.alert.error(data.message);
                    	}
                    }
                },null,
                function(){
                	
                });
    }
    
</script>