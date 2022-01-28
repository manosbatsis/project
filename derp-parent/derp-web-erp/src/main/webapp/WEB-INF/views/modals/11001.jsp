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

</style>
<div>
    <!-- Signup modal content -->
    <div id="signup-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-dialog">

            <div class="modal-content" style="width: 560px;">
                <div class="header" >
                    <span class="header-title" ></span>
                    <button class="header-button" ><span class="header-close" onclick="$m11001.cancel()"></span></button>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" id="addForm">
                    	<input type="hidden" id="id" name="id" >
                        <div class="form-group">
                            <div class="col-12">
                                <label >标准品牌<i class="red">*</i>：</label>
                                <select name="commBrandParentId" id="modalCommBrandParentId" class="edit_input" require reqrMsg="标准品牌不能为空！" >
                                   	<option value="">请选择</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-12">
                                <label >商品名称<i class="red">*</i>：</label>
                                <select style="width:70%" name="commGoodsName" id="modalGoodsName" class="edit_input" require reqrMsg="商品名称不能为空！" >
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-12">
                                <label >二级分类<i class="red">*</i>：</label>
                                <select style="width:70%" name="commTypeName" id="modalCommTypeName" class="edit_input" require reqrMsg="二级分类不能为空！" >
                                </select>
                            </div>
                        </div>
                        <div class="form-group" style="float: right;">
                            <div class="col-12">
                                <button class="btn btn-info waves-effect waves-light fr btn_default" type="button" onclick="$m11001.submit();">确定</button>
                                <button class="btn btn-light waves-effect waves-light btn_default" type="button" onclick="$m11001.cancel();">取消</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">
    var $m11001={
        init:function(id){
            //重置表单
            $($m11001.params.formId)[0].reset();
            //$module.brand.loadParentBrandSelect.call($('#modalCommBrandParentId'));
            
           	$(".header-title").html("编辑信息") ;
           	
            var url = "/commbarcode/detail.asyn";

            $custom.request.ajax(url,{"id":id},function(data) {
                if (data.state == 200) {
                	
                	var detail = data.data.detail ;
                	var itemList =data.data.itemList ;
                	$("#id").val(detail.id) ;
                    //$('#modalCommBrandParentId').selectpicker('val', detail.commBrandParentId);
                    $module.brand.loadParentBrandSelect.call($('select[id="modalCommBrandParentId"]')[0],detail.commBrandParentId +'');
                    
                    var goodsNameHtml = '<option value="">请选择</option>' ;
                    var typeIdHtml = '<option value="">请选择</option>' ;
                    $(itemList).each(function(index , item){
                    	
                    	if(item.goodsName){
	                    	goodsNameHtml += '<option value="'+ item.goodsName + '" ';
	                    	if(detail.commGoodsName == item.goodsName){
	                    		goodsNameHtml += 'selected ' ;
	                    	}
	                    	goodsNameHtml +='>'+item.goodsName+'</option>' ;
                    	}
                    	
                    	if(item.typeId){
	                    	typeIdHtml += '<option value="'+ item.typeId + '" ';
	                    	if(detail.commTypeId == item.typeId){
	                    		typeIdHtml += 'selected'
	                    	}
	                    	typeIdHtml +='>'+item.typeName+'</option>' ;
                    	}
                    	
                    });
                    $("#modalGoodsName").html(goodsNameHtml) ;
                    $("#modalCommTypeName").html(typeIdHtml) ;
                    
                } else {
                    $custom.alert.error(data.message);
                }
            });
            
            this.show();
        },
        submit:function(){
            var commBrandParentId = $('#modalCommBrandParentId').val();
            var commBrandParentName = $('#modalCommBrandParentId').find("option:selected").text() ;
            var commGoodsName = $('#modalGoodsName').val();
            var id = $('#id').val();
            var commTypeId = $('#modalCommTypeName').val();
            var commTypeName = $('#modalCommTypeName').find("option:selected").text() ;
            
            var dataJson = {"id":id, "commBrandParentId":commBrandParentId,"commBrandParentName":commBrandParentName,
            		"commGoodsName":commGoodsName , "commTypeId":commTypeId , "commTypeName" : commTypeName} ;

            $custom.request.ajaxReqrCheck(

                $m11001.params.url,
                dataJson,
                function(data) {
                    if (data.state == 200) {
                        $custom.alert.success(data.message);
                        //重新加载table表格
                        setTimeout(function () {
                        	$load.a('/list/menu.asyn?act=2001');
                        }, 500);
                        $m11001.hide();
                    } else {
                        $custom.alert.error(data.message);
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
                $m11001.hide();
            });
        },
        show:function(){
            $($m11001.params.modalId).modal({backdrop: 'static', keyboard: false});
        },
        hide:function(){
            $($m11001.params.modalId).modal("hide");
        },
        params:{
            url:'/commbarcode/modifyCommbarcode.asyn',
            formId:'#addForm',
            modalId:'#signup-modal',
        }
    }


</script>