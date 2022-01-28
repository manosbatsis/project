<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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

<!-- Custom Modals -->
        <div>
            <!-- Signup modal content -->
            <div id="add-signup-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;top:10px;">
                <div class="modal-dialog">
                    <div class="modal-content" style="width: 500px;">
                    	<div class="header" >
		                    <span class="header-title" >SD类型配置</span>
		                </div>
                        <div class="modal-body">
                            <form class="form-horizontal" id="addForm">
                                <div class="form-group m-b-25">
                                   	<input type="hidden" id="id" name="id">
                                    <div class="col-12" style="line-height: 40px;">
                                        <i class="red" style="">*</i> <label style="margin-left: 10px;">SD类型 :</label>
                                        <input class="edit_input" type="text" id="modelSdTypeName" name="sdTypeName" require reqrMsg="SD类型不能为空！">
                                    </div>
                                    <div class="col-12" style="line-height: 40px;">
                                    	<i class="red" style="">*</i> <label style="margin-left: 28px;">简称 :</label>
                                        <input class="edit_input" type="text" id="modelSdSimpleName" name="sdSimpleName" require reqrMsg="简称不能为空！">
                                    </div>
                                    <div class="col-12" style="line-height: 40px;">
                                    	<i class="red">*</i> <label >配置类型 :</label>
                                    	<select class="edit_input" name="type" id="modelType" require reqrMsg="配置类型不能为空！">
			                            </select>
                                    </div>
                                    <div class="col-12" style="line-height: 40px;">
                                    	<i class="red">*</i> <label >状态 :</label>
                                    	<label >
	                                    	<input type="radio" name="status" id="status1"  require reqrMsg="状态不能为空！" value="1"> 启用
	                                    </label>
	                                    <label >
	                                    	<input type="radio" name="status" id="status0" require reqrMsg="状态不能为空！" value="0"> 禁用
                                        </label>
                                    </div>
                                </div>
                                <div class="form-group" style="float: right;">
                                    <div class="col-12">
                                        <button class="btn btn-info waves-effect waves-light fr btn_default" type="button" onclick="$m20001.submit();">确定</button>
                                        <button class="btn btn-light waves-effect waves-light btn_default" type="button" onclick="$m20001.cancel();">取消</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
<script type="text/javascript">
    var $m20001={
        init:function(id){
            this.show();
            $module.constant.getConstantListByNameURL.call($('#modelType'),"sdtypeconfig_typeList",null);
            //重置表单
            $($m20001.params.formId)[0].reset();
            
            $("#modelSdSimpleName").removeAttr("readonly") ;
            $("#modelSdTypeName").removeAttr("readonly") ;
            $("#modelType").removeAttr("disabled") ;
            
            if(id){
            	var url = serverAddr + "/sdTypeConfig/detail.asyn?r='+Math.random()" ;
            	$custom.request.ajax(url,{"id":id},function(data) {
                    if (data.state == 200) {
                    	$("#modelSdSimpleName").attr("readonly", "readonly") ;
                        $("#modelSdSimpleName").val(data.data.sdSimpleName) ;
                        $("#modelSdTypeName").attr("readonly", "readonly") ;
                        $("#modelSdTypeName").val(data.data.sdTypeName) ;
                        $("#modelType").attr("disabled", "disabled") ;
                        $("#modelType").val(data.data.type) ;
                        $("#id").val(data.data.id) ;
                        
                        if(data.data.status == '1'){
                        	$("#status1").prop("checked", "checked") ;
                        }else{
                        	$("#status0").prop("checked", "checked") ;
                        }
                        
                    } else {
                        $custom.alert.error(data.message);
                    }
                });
            }else{
            	$("#id").val("") ;
            }
        },
        submit:function(){
        	var id = $("#id").val() ;
        	$m20001.add();
        },
        add:function(){
        	
        	//必填项校验
        	if(!$checker.verificationRequired()){
        		return ;
        	}
        	
        	var status = $('input:radio[name="status"]:checked').val();
        	if(!status){
        		$custom.alert.error("类型不能为空");
    			
    			return ;
        	}
        	
        	$custom.request.ajaxReqrCheck(
                    $m20001.params.addUrl,
                    $($m20001.params.formId).serializeArray(),
                    $m20001.succFun,
                    null,
                    $m20001.logicFun
            );
        },
        show:function(){
             $($m20001.params.modalId).modal({backdrop: 'static', keyboard: false});
        },
        hide:function(){
             $($m20001.params.modalId).modal("hide");
        },
        params:{
            addUrl: serverAddr + '/sdTypeConfig/saveOrModify.asyn?r='+Math.random(),
            formId:'#addForm',
            modalId:'#add-signup-modal',
        },
        cancel:function() {
            $custom.cancelReqrCheck("你将关闭该界面，数据不会被保存，是否继续关闭？",function(){
                $m20001.hide();
            });
        },
        succFun:function(data){
            if(data.state==200){
                $custom.alert.success(data.message);
                //成功隐藏
                $m20001.hide();
                //重新加载table表格
                setTimeout(function () {
                	$module.load.pageOrder("act=20001");
                }, 500);
            }else{
            	if(data.expMessage != null){
					$custom.alert.error(data.expMessage);
				}else{
					$custom.alert.error(data.message);
				}
            }
            
		},
		logicFun:function(){
        	$module.revertList.serializeListForm() ;
            $module.revertList.isMainList = true ;
        }
    }

</script>