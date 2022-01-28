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
                    <button class="header-button" ><span class="header-close" onclick="$m12001.cancel()"></span></button>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" id="addForm">
                    	<input type="hidden" id="id" name="id" >
                        <div class="form-group">
                            <div class="col-12">
                                <label >选择标准条码<i class="red">*</i>：</label>
                                <select id="commbarcode_select" class="selectpicker show-tick form-control" multiple data-live-search="true">
                                </select>
                                <button class="btn btn-info waves-effect waves-light fr btn_default" type="button" onclick="callback()">确定</button>
                            </div>
                        </div>
                        <div class="form-group">
                        	<div class="row col-12 table-responsive" id="commTable" style="display:none;">
								<table class="table table-striped dataTable no-footer"
									cellspacing="0" width="100%">
									<thead>
										<tr>
											<th>标准条码</th>
											<th>标准品名</th>
											<th><i class="red">*</i>标识为组码</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</div>
                        </div>
                        <div class="form-group" style="float: right;">
                            <div class="col-12">
                                <button class="btn btn-info waves-effect waves-light fr btn_default" type="button" onclick="$m12001.submit();">确定</button>
                                <button class="btn btn-light waves-effect waves-light btn_default" type="button" onclick="$m12001.cancel();">取消</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">
	//初始化下拉搜索框
	getCommbarcodeSelect() ;

    var $m12001={
        init:function(flag, id){
            //重置表单
            $($m12001.params.formId)[0].reset();
            //判断新增/编辑
            if (flag == 0) {
            	
            	$(".header-title").html("新增组") ; 
            	
            } else if (flag ==1) {
            	
            	$(".header-title").html("编辑组") ;
            	
                var url = "/groupCommbarcode/detail.asyn";

                $custom.request.ajax(url,{"id":id},function(data) {
                    if (data.state == 200) {
                        var itemList = data.data.list ;
                        
                        var html = "" ;
                        $(itemList).each(function(index , item){
                        	debugger ;
                        	var commbarcode = item.commbarcode ;
                        	var goodName = item.groupName ;
                        	
                        	if(!goodName){
                        		goodName = "" ;
                        	}
                        	
                        	var groupCommbarcode = item.groupCommbarcode ;
                        	
                        	html += '<tr><td>'+commbarcode+'</td>' + '<td>'+goodName+'</td>' + 
                    		'<td><input type="radio" name="groupCommbarcode" ';
                    		
                    		if(groupCommbarcode == commbarcode){
                    			html += 'checked';
                    		}
                    		
                    		html +=' value="'+commbarcode+'">是</td>' + 
                    		'<td><a href="#" onclick="delCommbarcodeItem(this)" >删除</a></td></tr>' ;
                        }); 
                        
                        $("#commTable").find("tbody").append(html) ; 
                		$("#commTable").show() ;
                		$("#id").val(id) ;
                        
                    } else {
                        $custom.alert.error(data.message);
                    }
                });
            }
            this.show();
        },
        submit:function(){
        	$module.revertList.serializeListForm() ;
            $module.revertList.isMainList = true ;
        	
            //保存数据
            saveData() ;

        },
        del:function(id){
        	$custom.alert.warning("是否删除该组码信息" , function(){
	        	var url = '/groupCommbarcode/delete.asyn?r='+Math.random() ;
	        	$custom.request.ajax(url,{"id":id},function(data) {
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
        cancel:function() {
            $custom.cancelReqrCheck("你将关闭该界面，数据不会被保存，是否继续关闭？",function(){
                $m12001.hide();
            });
        },
        show:function(){
        	$('#commbarcode_select').selectpicker('val',['noneSelectedText']);
            $($m12001.params.modalId).modal({backdrop: 'static', keyboard: false});
        },
        hide:function(){
        	$("#commTable").hide() ;
        	$("#commTable").find("tbody").empty() ;
        	document.getElementById("commbarcode_select").options.selectedIndex = null;
        	$('#commbarcode_select').selectpicker('refresh');
            $($m12001.params.modalId).modal("hide");
        },
        params:{
            url:'/businessUnit/saveBusinessUnit.asyn',
            formId:'#addForm',
            modalId:'#signup-modal',
        }
    }

    /**
    *	下拉搜索
    */
	$("#commbarcode_select").prev().find(".bs-searchbox").find("input").keyup(
		function(){
			var commbarcode = $("#addForm .open input").val() ;
			getCommbarcodeSelect(commbarcode) ;
		}
	);
    
    /**
    * 获取标准条码下拉
    */
    function getCommbarcodeSelect(commbarcode){
    	var url = "/groupCommbarcode/getCommbarcode.asyn" ;
		$custom.request.ajax(url,{"commbarcode":commbarcode},function(data) {
            if (data.state == 200) {
                
            	var html = "" ;
            	
            	var commbarcodeList = data.data.list ;
            	
            	$(commbarcodeList).each(function(index , comm){
            		html += '<option value="'+comm.commGoodsName+'">'+comm.commbarcode+'</option>' ;
            	
            	}) ;
            	
           		$("#commbarcode_select").append(html) ;
           		$('#commbarcode_select').selectpicker({width: '300px'});
   				$('#commbarcode_select').selectpicker('refresh');
            	
            } else {
                $custom.alert.error(data.message);
            }
        });
    }
    
    /**
    *	确定回调
    */
    function callback(){
    	
    	var val = "", vals = [];
        //循环的取出插件选择的元素(通过是否添加了selected类名判断)
        //获取标准条码
        for (var i = 0; i < $("li.selected").length; i++) {
            val = $("li.selected").eq(i).find(".text").text();
            if (val != '') {
            	vals.push(val);
            }
        }
        
        //获取品名
        var goodNames = [] ;
        $($("#commbarcode_select").find("option:selected").selectpicker('val')).each(function(i , m){
        	var val = $(m).val() ;
            if (val != '') {
            	goodNames.push(val);
            }
        });
        
        var html = "" ;
        $(goodNames).each(function( i , m){
        	var commbarcode = $(vals).get(i) ;
        	
        	var goodName = '' ;
        	if(judgeCodeExistOrNot(commbarcode)){
        		return true ;
        	}
        	
        	if('null' != m){
        		goodName = m ;
        	}
        	
        	html += '<tr><td>'+commbarcode+'</td>' + '<td>'+goodName+'</td>' + 
        		'<td><input type="radio" name="groupCommbarcode" value="'+commbarcode+'">是</td>' + 
        		'<td><a href="#" onclick="delCommbarcodeItem(this)" >删除</a></td></tr>' ;
        	
        });
		
		$("#commTable").find("tbody").append(html) ; 
		$("#commTable").show() ;
		
		//清空选择框
		$('#commbarcode_select').selectpicker('val',['noneSelectedText']);
		$('#commbarcode_select').selectpicker('refresh');
    }
    
    /**
    *删除当前的表格行
    */
    function delCommbarcodeItem(org){
    	$(org).parent().parent().remove() ;
    }
    
    /*
    *保存数据
    */
    function saveData(){
    	
    	var trs = $("#commTable").find("tr") ;
    	
    	var groupCommbarcode = $('input:radio[name="groupCommbarcode"]:checked').val(); 
    	
    	if(!groupCommbarcode){
    		$custom.alert.error("组码未选择");
    		
    		return ;
    	}
    	
    	var groupName = "" ;
    	var commbarcode = "" ;
    	$(trs).each(function(i , tr){
    		var temp = $(tr).find("td").eq(0).text() ;

    		commbarcode += temp;
    		if(i < trs.length && temp){
    			commbarcode += "," ;
    		}
    		
    		if(temp == groupCommbarcode){
    			groupName = $(tr).find("td").eq(1).text() ; 
    		}
    		
    	}) ;
    	
    	var id = $("#id").val() ;
    	
    	var jsonData = {"id":id ,
    			"groupCommbarcode" : groupCommbarcode , 
    			"groupName" : groupName , 
    			"commbarcode" : commbarcode} ;
    	
    	var url = "/groupCommbarcode/saveGroupCommbarcodes.asyn" ;
		$custom.request.ajax(url, jsonData ,function(data) {
            if (data.state == 200) {
            	setTimeout(function () {
                	$load.a('/list/menu.asyn?act=2002');
                }, 500);
                $m12001.hide();            	
            } else {
                $custom.alert.error(data.expMessage);
            }
        });
    }
    
    function judgeCodeExistOrNot(commbarcode){
    	var trs = $("#commTable").find("tr") ;
    	
    	var flag = false ;
    	
    	$(trs).each(function(index , tr){
    		temp = $(tr).find("td").eq(0).text() ;
    		
    		if(temp == commbarcode){
    			flag =true ;
    			
    			return false ;
    		}
    	}) ;
    	
    	return flag ;
    }
</script>