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
		                    <span class="header-title" >编辑标准品牌</span>
		                </div>
                        <div class="modal-body">
                            <form class="form-horizontal" id="addForm">
                                <div class="form-group m-b-25">
                                    <div class="col-12">
                                        <i>&nbsp;&nbsp;</i><label >品牌名称：</label><span id="brandName" ></span>
                                    	<input type="hidden" id="id" name="id">
                                    </div>
                                    <div class="col-12">
                                        <i class="red">*&nbsp;</i><label >标准品牌：</label>
                                        <select style="width: 200px;" name="parentId" id="parentId" class="input_msg" require reqrMsg="标准品牌不能为空！" >
                                            <option value="">请选择</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group" style="float: right;">
                                    <div class="col-12">
                                        <button class="btn btn-info waves-effect waves-light fr btn_default" type="button" onclick="$m14001.modify();">确定</button>
                                        <button class="btn btn-light waves-effect waves-light btn_default" type="button" onclick="$m14001.cancel();">取消</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->
        </div>
<script type="text/javascript">

    var $m14001={
        init:function(id){
            this.show();
            //重置表单
            $($m14001.params.formId)[0].reset();

            if(id){
            	var url = "/brand/detail.asyn?r='+Math.random()" ;
            	$custom.request.ajax(url,{"id":id},function(data) {
                    if (data.state == 200) {
                        $('#brandName').text(data.data.name);
                        $module.brand.loadParentBrandSelect.call($('select[name="parentId"]')[0],data.data.parentId +'');
                        $('#id').val(data.data.id);
                    } else {
                        $custom.alert.error(data.message);
                    }
                });
            }
        },
        modify:function(){
        	$custom.request.ajaxReqrCheck(
                    $m14001.params.modifyUrl,
                    $($m14001.params.formId).serializeArray(),
                    $m14001.succFun,
                    null,
                    function() {
                        $module.revertList.serializeListForm() ;
                        $module.revertList.isMainList = true ;
                    }
            );
        },
        show:function(){
             $($m14001.params.modalId).modal({backdrop: 'static', keyboard: false});
        },
        hide:function(){
             $($m14001.params.modalId).modal("hide");
        },
        params:{
            modifyUrl:'/brand/modify.asyn?r='+Math.random(),
            formId:'#addForm',
            modalId:'#add-signup-modal',
        },
        cancel:function() {
            $custom.cancelReqrCheck("你将关闭该界面，数据不会被保存，是否继续关闭？",function(){
                $m14001.hide();
            });
        },
        succFun:function(data){
            if(data.state==200){
                $custom.alert.success(data.message);
                //成功隐藏
                $m14001.hide();
                //重新加载table表格
                setTimeout(function () {
                	$load.a('/list/menu.asyn?act=1601');
                }, 500);
            }else{
            	if(data.expMessage != null){
					$custom.alert.error(data.expMessage);
				}else{
					$custom.alert.error(data.message);
				}
            }
            
		}
    }

</script>