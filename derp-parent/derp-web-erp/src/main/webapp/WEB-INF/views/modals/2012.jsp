<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div class="row">
    <!-- amount Adjust content -->
    <div id="brandSuperior-modal"  class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none; overflow-y: auto;margin-left: auto;">
        <div class="modal-dialog">
            <div class="modal-content modal-lg" style="width:400px;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h5 class="modal-title" id="myLargeModalLabel">新增母品牌</h5>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" id="addForm">
                        <div class="form-group m-b-25" style="width:100%;text-align:center">
                            <div class="col-12">
                                <i class="red">* </i> <label >母品牌  :</label>
                                <input class="input_msg" type="text" name="name" id="name" require reqrMsg="母品牌不能为空！" placeholder="请输入名称">
                                <input type="hidden" id="id" name="id">
                            </div>
                            <br>
                            <div class="col-12">
                                <label > &nbsp&nbsp NC编码  :</label>
                                <input class="input_msg" type="text" name="ncCode" id="ncCode" placeholder="请输入NC编码">
                            </div>
                            <br>
                            <div class="col-12">
                                <i class="red">* </i> <label >申报系数  :</label>
                                <input class="input_msg" type="text" name="priceDeclareRatio" id="priceDeclareRatio" require reqrMsg="申报系数不能为空！" placeholder="请输入">
                            </div>
                           <!--  <div class="col-12">
                                <i class="red"> * </i><label >授权方  :</label>
                                <select class="input_msg" name="authorizer" id="authorizer" require reqrMsg="授权方不能为空！">
                                    <option value="">请选择</option>
                                    <option value="1">品牌方</option>
                                    <option value="2">经销商</option>
                                    <option value="3">无授权</option>
                                </select>
                            </div> -->
                            <br>
                            <!-- <div class="col-12">
                                <i class="red">&nbsp&nbsp&nbsp* </i><label >分类  :</label>
                                <select class="input_msg" name="type" id="type" require reqrMsg="分类不能为空！">
                                    <option value="">请选择</option>
                                    <option value="1">跨境电商</option>
                                    <option value="2">内贸</option>
                                </select>
                            </div> -->
                            <br>
                        </div>
                    </form>
                    <div class="form-row mt20">
                        <div class="form-inline m0auto">
                            <div class=form-group>
                                <button type="button" class="btn btn-info waves-effect waves-light fr delayedBtn" onclick='$m2012.submit();'>确定</button>
                                <button type="button" class="btn btn-light waves-effect waves-light ml15"  onclick='$m2012.hide();'>取消</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">

    var $m2012={
        init:function(id){
            
            //重置表单
            $($m2012.params.formId)[0].reset();
            
            if(id){
            	var url = "/brandSuperior/findById.asyn?r='+Math.random()" ;
            	$custom.request.ajax(url,{"id":id},function(result) {
                    if (result.state == 200) {
                    	$(".modal-title").html("编辑母品牌");
                        $('#id').val(result.data.id);
                        $('#name').val(result.data.name);
                        $('#ncCode').val(result.data.ncCode);
                        $('#authorizer').val(result.data.authorizer);
                        $('#type').val(result.data.type);
                        $('#priceDeclareRatio').val(result.data.priceDeclareRatio);
                        $('#name').attr("disabled",true);
	                   		 
                    } else {
                        $custom.alert.error(data.message);
                    }
                });
            }else{
        		$(".modal-title").html("新增母品牌");
            }
            this.show();
        },
        submit:function(){
        	var id = $("#id").val() ;
        	if(id){
        		$m2012.modify();
        	}else{
        		$m2012.add();
        	}
        },
        add:function(){
        	$custom.request.ajaxReqrCheck(
                    $m2012.params.addUrl,
                    $($m2012.params.formId).serializeArray(),
                    $m2012.succFun,
                    null,$m2012.logicFun
            );
        },
        modify:function(){
        	$('#name').attr("disabled",false);
        	$custom.request.ajaxReqrCheck(
                    $m2012.params.modifyUrl,
                    $($m2012.params.formId).serializeArray(),
                    $m2012.succFun,
                    null,$m2012.logicFun
            );        	
        },
        del:function(id){
        	$custom.alert.warning("是否删除该母品牌信息" , function(){
	        	var url = '/brandSuperior/delete.asyn?r='+Math.random() ;
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
             $($m2012.params.modalId).modal({backdrop: 'static', keyboard: false});
        },
        hide:function(){
        	 $('#id').val("");
        	 $('#name').attr("disabled",false);
             $($m2012.params.modalId).modal("hide");
        },
        params:{
            addUrl:'/brandSuperior/save.asyn?r='+Math.random(),
            modifyUrl:'/brandSuperior/modify.asyn?r='+Math.random(),
            formId:'#addForm',
            modalId:'#brandSuperior-modal',
        },
        cancel:function() {
            $custom.cancelReqrCheck("你将关闭该界面，数据不会被保存，是否继续关闭？",function(){
                $m2012.hide();
            });
        },
        succFun:function(data){
            if(data.state==200){
                $custom.alert.success(data.message);
                //成功隐藏
                $m2012.hide();
                //重新加载table表格
                setTimeout(function () {
                    $mytable.fn.reload();
                }, 500);
            }else{
            	if(data.expMessage != null){
					$custom.alert.error(data.expMessage);
				}else{
					$custom.alert.error(data.message);
				}
            }
            
		},
        logicFun:function(){}
    }

</script>