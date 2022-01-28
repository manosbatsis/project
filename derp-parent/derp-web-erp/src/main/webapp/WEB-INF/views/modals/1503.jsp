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
		                    <span class="header-title" >库位映射新增/编辑</span>
		                </div>
                        <div class="modal-body">
                            <form class="form-horizontal" id="addForm">
                                <div class="form-group m-b-25">
                                    <div class="col-12">
                                        <i class="red" style="margin-left: 28px;">*</i> <label>公司 :</label>
	                                    <select name="merchantId" style="" class="input_msg" id="modalMerchantIdModel" require reqrMsg="公司不能为空！" >
	                                        <option value="">请选择</option>
	                                        <c:forEach items="${merchantList }" var="merchant">
	                                            <option value="${merchant.value }">${merchant.label }</option>
	                                        </c:forEach>
	                                    </select>
                                        <i class="red" style="margin-left: 14px;">*</i> <label >原货号:</label>
                                        <input class="input_msg" type="text" name="originalGoodsNo" id="originalGoodsNoModel" require reqrMsg="原货号不能为空！" placeholder="请输入原货号">
                                    	<input type="hidden" id="id" name="id">
                                    </div>
                                    <div class="col-12">
                                    	<i class="red">*</i> <label >库位货号 :</label>
	                                    <input class="input_msg" style="width: 136px;" type="text" name="goodsNo" id="goodsNoModel" require reqrMsg="库位货号不能为空！" placeholder="请输入库位货号">
                                        <i class="red">*</i> <label style="">库位类型:</label>
                                        <select name="type" style="" class="input_msg" id="typeModel" require reqrMsg="库位类型不能为空！" >
	                                    </select>
                                    </div>
                                </div>
                                <div class="form-group" style="float: right;">
                                    <div class="col-12">
                                        <button class="btn btn-info waves-effect waves-light fr btn_default" type="button" onclick="$m1503.submit();">确定</button>
                                        <button class="btn btn-light waves-effect waves-light btn_default" type="button" onclick="$m1503.cancel();">取消</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->
        </div>
<script type="text/javascript">
    var $m1503={
        init:function(id){
            this.show();
            //重置表单
            $($m1503.params.formId)[0].reset();
            
            if(id){
            	var url = "/inventoryLocationMapping/detail.asyn?r='+Math.random()" ;
            	$custom.request.ajax(url,{"id":id},function(data) {
                    if (data.state == 200) {
                    	$(".header-title").html("库位映射编辑");
                        $('#modalMerchantIdModel').val(data.data.merchantId);
                        $('#originalGoodsNoModel').val(data.data.originalGoodsNo);
                        $('#goodsNoModel').val(data.data.goodsNo);
                        $('#typeModel').val(data.data.type);
                        $('#id').val(data.data.id);
                        
                    } else {
                        $custom.alert.error(data.message);
                    }
                });
            }else{
            	$(".header-title").html("库位映射新增");
            }
        },
        submit:function(){
        	var id = $("#id").val() ;
        	$m1503.add();
        },
        add:function(){
        	$custom.request.ajaxReqrCheck(
                    $m1503.params.addUrl,
                    $($m1503.params.formId).serializeArray(),
                    $m1503.succFun,
                    null,
                    $m1503.logicFun
            );
        },
        show:function(){
             $($m1503.params.modalId).modal({backdrop: 'static', keyboard: false});
        },
        hide:function(){
             $($m1503.params.modalId).modal("hide");
        },
        params:{
            addUrl:'/inventoryLocationMapping/saveOrModify.asyn?r='+Math.random(),
            formId:'#addForm',
            modalId:'#add-signup-modal',
        },
        cancel:function() {
            $custom.cancelReqrCheck("你将关闭该界面，数据不会被保存，是否继续关闭？",function(){
                $m1503.hide();
            });
        },
        succFun:function(data){
            if(data.state==200){
                $custom.alert.success(data.message);
                //成功隐藏
                $m1503.hide();
                //重新加载table表格
                setTimeout(function () {
                	$load.a('/list/menu.asyn?act=1503');
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