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

<!-- Custom Modals -->
        <div>
            <!-- Signup modal content -->
            <div id="add-signup-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;top:120px;">
                <div class="modal-dialog">
                    <div class="modal-content" style="width: 560px;">
                    	<div class="header" >
		                    <span class="header-title" >新增标准品牌</span>
		                </div>
                        <div class="modal-body">
                            <form class="form-horizontal" id="addForm">
                                <div class="form-group m-b-25">
                                    <div class="col-12">
                                        
                                        <i class="red">*</i> <label >标准品牌  :</label>
                                        <input class="input_msg" type="text" name="name" id="editName" require reqrMsg="标准品牌不能为空！" placeholder="请输入中文名称">
                                    	<input type="hidden" id="oldName" name="oldName">
                                    	<input type="hidden" id="id" name="id">
                                    	<i class="red">*</i> <label >上级母品牌 :</label>
										<select class="input_msg" name="superiorParentBrandCodeEditOrAdd" id="superiorParentBrandCodeEditOrAdd"></select>                                       
                                    	
                                    </div>
                                    <div class="col-12">
                                    	<label style="margin-left: 15px;">英文名称  :</label>
                                        <input type="text" name="englishName" id="englishName" class="input_msg">
                                        <i class="red"> * </i><label >授权方  :</label>
		                                <select class="input_msg" name="authorizer" id="authorizer" require reqrMsg="授权方不能为空！">
		                                    <option value="">请选择</option>
		                                    <option value="1">品牌方</option>
		                                    <option value="2">经销商</option>
		                                    <option value="3">无授权</option>
		                                </select>
                                    </div>
                                    <div class="col-12">
                                    	<i class="red">&nbsp&nbsp&nbsp* </i><label >分类  :</label>
		                                <select class="input_msg" name="type" id="type" require reqrMsg="分类不能为空！">
		                                    <option value="">请选择</option>
		                                    <option value="1">跨境电商</option>
		                                    <option value="2">内贸</option>
		                                </select>
                                    </div>
                                </div>
                                <div class="form-group" style="float: right;">
                                    <div class="col-12">
                                        <button class="btn btn-info waves-effect waves-light fr btn_default" type="button" onclick="$m2011.submit();">确定</button>
                                        <button class="btn btn-light waves-effect waves-light btn_default" type="button" onclick="$m2011.cancel();">取消</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->
        </div>
<script type="text/javascript">

    var $m2011={
        init:function(id){
            this.show();
            //重置表单
            $($m2011.params.formId)[0].reset();
            getSuperiorList($('select[name="superiorParentBrandCodeEditOrAdd"]'));
            if(id){
            	var url = "/brandParent/detail.asyn?r='+Math.random()" ;
            	$custom.request.ajax(url,{"id":id},function(data) {
                    if (data.state == 200) {
                    	debugger;
                    	$(".header-title").html("编辑标准品牌");
                        $('#editName').val(data.data.name);
                        $('#oldName').val(data.data.name);
                        $('#superiorParentBrandCodeEditOrAdd').val(data.data.superiorParentBrandId);
                        // var superiorParentBrandCode= data.data.superiorParentBrandCode;
                		// $module.constant.getConstantListByNameURL.call($('select[name="superiorParentBrandCodeEditOrAdd"]')[0],"brandParent_superiorParentBrandCodeList",superiorParentBrandCode);

                        $('#englishName').val(data.data.englishName);
                        $('#authorizer').val(data.data.authorizer);
                        $('#type').val(data.data.type);
                        $('#id').val(data.data.id);

	                   		 
                    } else {
                        $custom.alert.error(data.message);
                    }
                });
            }else{
        		// $module.constant.getConstantListByNameURL.call($('select[name="superiorParentBrandCodeEditOrAdd"]')[0],"brandParent_superiorParentBrandCodeList",null);
                $(".header-title").html("新增标准品牌");
            }
        },
        submit:function(){
        	var id = $("#id").val() ;
        	if(id){
        		$m2011.modify();
        	}else{
        		$m2011.add();
        	}
        },
        add:function(){
        	$custom.request.ajaxReqrCheck(
                    $m2011.params.addUrl,
                    $($m2011.params.formId).serializeArray(),
                    $m2011.succFun,
                    null,
                    $m2011.logicFun
            );
        },
        modify:function(){
        	$custom.request.ajaxReqrCheck(
                    $m2011.params.modifyUrl,
                    $($m2011.params.formId).serializeArray(),
                    $m2011.succFun,
                    null,
                    $m2011.logicFun
            );
        },
        del:function(id){
        	$custom.alert.warning("是否删除该标准品牌信息" , function(){
	        	var url = '/brandParent/delete.asyn?r='+Math.random() ;
	        	$custom.request.ajax(url,{"ids":id},function(data) {
	        		if (data.state == 200) {
	                	$custom.alert.success(data.message);
	                    //重新加载table表格
	                	$mytable.fn.reload();
	                } else {
	                    $custom.alert.error(data.expMessage);
	                }
	            });
        	});
        },
        show:function(){
             $($m2011.params.modalId).modal({backdrop: 'static', keyboard: false});
        },
        hide:function(){
             $($m2011.params.modalId).modal("hide");
        },
        params:{
            addUrl:'/brandParent/save.asyn?r='+Math.random(),
            modifyUrl:'/brandParent/modify.asyn?r='+Math.random(),
            formId:'#addForm',
            modalId:'#add-signup-modal',
        },
        cancel:function() {
            $custom.cancelReqrCheck("你将关闭该界面，数据不会被保存，是否继续关闭？",function(){
                $m2011.hide();
            });
        },
        succFun:function(data){
            if(data.state==200){
                $custom.alert.success(data.message);
                //成功隐藏
                $m2011.hide();
                //重新加载table表格
                setTimeout(function () {
                	$load.a('/list/menu.asyn?act=1602');
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
        	
        	var editName = $("#editName").val() ;
        	var englishName = $("#englishName").val() ;

        	
        	if (editName.length > 30) {
                $custom.alert.error("标准品牌名长度不能超过30个字");
                return false;
            }
        	
			var regEng = /^[a-zA-Z\s]*$/ ;
        	
        	if(!regEng.test(englishName)){
        		 $custom.alert.error("英文名称必须为英文");
                 return false;
        	}
        	
        	if (englishName.length > 50) {
                $custom.alert.error("英文名称长度不能超过50个字");
                return false;
            }
        	

        	
        	$module.revertList.serializeListForm() ;
            $module.revertList.isMainList = true ;
        }
    }

</script>