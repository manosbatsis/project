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

            <div class="modal-content" style="width: 680px;">
                <div class="header" >
                    <span class="header-title" style="width: 560px;">适用配置</span>
                    <button class="header-button" ><span class="header-close" onclick="$m15004.cancel()"></span></button>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" id="addForm">
                        <input type="hidden" id="id" name="id" >
                        <div class="form-group">
                            <div class="col-12">
                                <label style="padding-left: 55px;"><i class="red">*  </i>状态：</label>
                                <input type="radio" value="1" name="modal_status">启用
                                <input type="radio" value="0" name="modal_status">禁用
                            </div>
                            <div class="col-12">
                                <label style="padding-left: 55px;"><i class="red">*</i>选择关联模板：</label>
                                <input type="hidden" class="input_msg" name="modal_fileTempId">
                                <input type="hidden" class="input_msg" name="modal_fileTempCode">
                                <input type="hidden" class="input_msg" name="modal_fileTempName" style="width:300px;">
                                <select id="fileTemp_select" class="selectpicker show-tick form-control" data-live-search="true"></select>
                            </div>
                            <div class="col-12">
                                <label style="padding-left: 55px;"><i class="red">*  </i>进出仓配置：</label>
                                <input type="radio" value="0" name="depot_config">进仓
                                <input type="radio" value="1" name="depot_config">出仓
                            </div>
                             <div class="col-12" style="display:none" id="isSameAreaDiv">
                                <label style="padding-left: 55px;"><i class="red">*  </i>是否同关区：</label>
                                <input type="radio" value="1" name="modal_isSameArea">是
                                <input type="radio" value="0" name="modal_isSameArea">否
                            </div>
                            <div class="col-12">
                                <label style="padding-left: 55px;"><i class="red">*  </i>适用仓库：</label>
                                <select id="depot_select" class="selectpicker show-tick form-control" multiple data-live-search="true"></select>
                                <button class="btn btn-info waves-effect waves-light fr btn_default" type="button" onclick="callback()">确定</button>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="row col-12 table-responsive" id="commTable" >
                                <table class="table table-striped dataTable no-footer" cellspacing="0" width="100%">
                                    <thead>
                                    <tr>
                                        <th>仓库编码</th>
                                        <th>仓库名称</th>
                                        <th>平台关区</th>
                                        <th>操作</th>
                                    </tr>
                                    </thead>
                                    <tbody id="table-list">
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="form-group" style="float: right;">
                            <div class="col-12">
                                <button class="btn btn-info waves-effect waves-light fr btn_default" type="button" onclick="$m15004.submit();">确定</button>
                                <button class="btn btn-light waves-effect waves-light btn_default" type="button" onclick="$m15004.cancel();">取消</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">
    var $m15004={
        init:function(id,status,depotConfig,fileTempId,fileTempCode,fileTempName,isSameArea){
	      	//重置表单            
	        $($m15004.params.formId)[0].reset();
	        if(id != null || id != undefined){
            	$m15004.params.id = id;            	
	            $("input[name='modal_status'][value='"+ status + "']").attr("checked",true);
	            $("input[name='depot_config'][value='"+ depotConfig + "']").attr("checked",true);	            
	          	if(depotConfig == "1"){
	          		$("#isSameAreaDiv").css("display","block");
	          		$("input[name='modal_isSameArea'][value='"+ isSameArea + "']").attr("checked",true);	
	          	}else{
	          		$("#isSameAreaDiv").css("display","none");
	            	$('input[name="modal_isSameArea"]:checked').prop("checked", false);
	          	}
	          	$("#fileTemp_select").selectpicker('hide');
            	$("input[name='modal_fileTempName']").attr("type","text");
            	$("input[name='modal_fileTempName']").attr("readonly","readonly");
            	$("input[name='modal_fileTempId']").val(fileTempId);
            	$("input[name='modal_fileTempCode']").val(fileTempCode);
            	$("input[name='modal_fileTempName']").val(fileTempName);
            	
	            var url = serverAddr + "/customsFileConfig/listDepotRel.asyn";
	            $custom.request.ajax(url,{"fileId":id},function(data) {
	                if (data.state == 200) {
	                    var itemList = data.data ;
	                    $(itemList).each(function(index , item){
	                        var depotId = item.depotId ;
	                        var depotName = item.depotName ;
	                        var depotCode = item.depotCode ;
	                        var platformCustomsIds = item.platformCustomsIds ;

	                        if(!depotName){
	                            depotName = "" ;
	                        }

	                        var html = '<tr><td><input type="hidden" value="'+depotId +'"><span>'+depotCode+'</span></td>' + '<td>'+ depotName+'</td>' +
	                                '<td><select name="platformArea" style="overflow: visible !important;" class="selectpicker show-tick form-control" multiple>' ;
	                        
	                        var platFormCustomsList = $module.depot.ajax($module.params.getSelectBeanByDepotCustomsRelURL, {"depotId":depotId});
		           	    	if(platFormCustomsList){	    		 
		           	            $(platFormCustomsList).each(function(index , platFormCustoms){
		           	            	html += '<option value="'+platFormCustoms.value+'" >' +platFormCustoms.label+'</option>' ;                            	
		           	            }) ;
		           	    	}else{
		           	    		html += '<option value="" >请选择</option>' ;
		           	    	}
		           	    	 
	                        html += '</select></td><td><a href="#" onclick="delDepot(this)" >删除</a></td></tr>' ;	                       	                    
	                    
		                    $("#commTable").find("tbody").append(html) ;
		                    var obj = $("#table-list").find("tr:last").find("select") ;	
		                    if(platformCustomsIds){
			                    $(obj).selectpicker('val', platformCustomsIds.split(","));		                    	
		                    }else{
		                    	$(obj).selectpicker({noneSelectedText : '请选择'});
		                    }	              
	                    });
	                    $("#commTable").show() ;	                    
	                    var selectObj = $("#table-list").find("tr").find("select");
	                    $(selectObj).prev().prev().css({"height": "30px","margin-bottom": "5px","border": "1px solid #dadada","background": "white","font-size": "12px"}) ;
	                    $(selectObj).prev().css({"z-index":"99"}) ;
	                    $(selectObj).selectpicker('refresh');
	                    
	                    $("#id").val(id) ;
	                   
	                } else {
	                    $custom.alert.error(data.message);
	                }
	            });
            }else{
            	$("#isSameAreaDiv").css("display","none");
            	$("input[name='modal_status'][value='1']").attr("checked",true);
            	$('input[name="depot_config"]:checked').prop("checked", false);
            	$("#fileTemp_select").selectpicker('show');
            	$("input[name='modal_fileTempName']").attr("type","hidden");
            	getFileTempSelect(); 
            }
            //初始化下拉搜索框
            getDepotSelect() ;                       
            this.show();
        },
        submit:function(){
            $module.revertList.serializeListForm() ;
            $module.revertList.isMainList = true ;
            //保存数据
            saveData() ;

        },
        cancel:function() {
            $custom.cancelReqrCheck("你将关闭该界面，数据不会被保存，是否继续关闭？",function(){
                $m15004.hide();
            });
        },
        show:function(){
            $('#depot_select').selectpicker('val',['noneSelectedText']);
            $($m15004.params.modalId).modal({backdrop: 'static', keyboard: false});
        },
        hide:function(){
        	$m15004.params.id = "";
        	$('input[name="depot_config"]:checked').prop("checked", false);
        	$('input[name="modal_isSameArea"]:checked').prop("checked", false);
            $("#table-list").empty() ;
            document.getElementById("depot_select").options.selectedIndex = null;
            $('#depot_select').selectpicker('refresh');
            $($m15004.params.modalId).modal("hide");
        },
        params:{
            formId:'#addForm',
            modalId:'#signup-modal',
            id:""
        }
    }

    /**
     *	下拉搜索
     */
    $("#depot_select").prev().find(".bs-searchbox").find("input").keyup(
        function(){
            var depotName = $("#addForm .open input").val() ;
            getdepotSelect(depotName) ;
        }
    );
    
    $("#fileTemp_select").prev().find(".bs-searchbox").find("input").keyup(
         function(){
             var fileTempName = $("#addForm .open input").val() ;
             getFileTempSelect(fileTempName) ;
         }
   );
    /**
		1、当“进出仓配置”为“进仓”时，该字段禁用，不做设置；
		2、当“进出仓配置”为“出仓”时，该字段必填；
    */
    $('input[name="depot_config"]').on('click',function(){
        var bool = $(this).prop("checked");
        $('input[name="modal_isSameArea"]:checked').prop("checked", false);
        if($(this).val() == "0"){
        	$("#isSameAreaDiv").css("display","none");
    	}else{
    		$("#isSameAreaDiv").css("display","block");
    	}
    });

    /**
     * 获取适用仓库下拉
     */
    function getDepotSelect(name){
        $("#depot_select").empty();
        $custom.request.ajax($module.params.getSelectBeanByDTOURL,{"name":name},function(data) {
            if (data.state == 200) {
                var html = "" ;
                var depotList = data.data ;

                $(depotList).each(function(index , depot){
                    html += '<option value="'+depot.id+'" code="' + depot.code + '">' +depot.name+'</option>' ;
                }) ;

                $("#depot_select").append(html) ;
                $('#depot_select').selectpicker({width: '300px'});
                $('#depot_select').selectpicker({noneSelectedText : '请选择'});
                $('#depot_select').selectpicker('refresh');
                
                $("#depot_select").prev().prev().css({"height": "30px","margin-bottom": "5px","border": "1px solid #dadada","background": "white","font-size": "12px"}) ;
                $("#depot_select").prev().css({"z-index":"99"}) ;

            } else {
                $custom.alert.error(data.message);
            }
        });
    }
    /**
     * 获取关联模板下拉
     */
    function getFileTempSelect(name){
        $("#fileTemp_select").empty();
        //选择模板配置里面已有的且类型为清关单证的模板
        $custom.request.ajax(serverAddr + "/fileTemp/getFileTemp.asyn",{"title":name,"type":"3","status":"1"},function(data) {
            if (data.state == 200) {
                var html = "" ;
                var fielTemptList = data.data ;
                html += "<option value=''>请选择</option>";
                $(fielTemptList).each(function(index , fileTemp){
                    html += '<option value="'+fileTemp.id+'" code="' + fileTemp.code + '" >' +fileTemp.title+'</option>' ;
                }) ;

                $("#fileTemp_select").append(html) ;
                $('#fileTemp_select').selectpicker({width: '300px'});
                
                
                $("#fileTemp_select").prev().prev().css({"height": "30px","margin-bottom": "5px","border": "1px solid #dadada","background": "white","font-size": "12px"}) ;
                $("#fileTemp_select").prev().css({"z-index":"99"}) ;
                
                $('#fileTemp_select').selectpicker('refresh');
            } else {
                $custom.alert.error(data.message);
            }
        });
    }

    /**
     *	确定回调
     */
    function callback(){
    	var selectedValues = [];
    	$("#depot_select option:selected").each(function () {
    		var seleted = {};
    		seleted["depotId"] = $(this).val();
    		seleted["depotCode"] = $(this).attr('code');
    		seleted["depotName"] = $(this).text();
    		
    		selectedValues.push(seleted);
    	});
    	
    	 var html = "" ;
         $(selectedValues).each(function(i,m){
        	 var depotId = m.depotId;  
             var depotCode = m.depotCode; 
             var depotName = m.depotName; 

             if(judgeCodeExistOrNot(depotCode)){
                 return true ;
             }
             
	         html += '<tr><td><input type="hidden" value="'+depotId +'"><span>'+depotCode+'</span></td>' + '<td>'+ depotName+'</td>' +
	             '<td><select id="platformArea" name="platformArea" style="overflow: visible !important;" class="selectpicker show-tick form-control" multiple>' ;
	    	 var platFormCustomsList = $module.depot.ajax($module.params.getSelectBeanByDepotCustomsRelURL, {"depotId":depotId});
	    	 if(platFormCustomsList){	    		 
	             $(platFormCustomsList).each(function(index , platFormCustoms){
	            	 html += '<option value="'+platFormCustoms.value+'" >' +platFormCustoms.label+'</option>' ;                            	
	             }) ;
	    	 }else{
	    		 html += '<option value="" >请选择</option>' ;
	    	 }

		     html += '</select></td><td><a href="#" onclick="delDepot(this)" >删除</a></td></tr>' ;
         });

         $("#commTable").find("tbody").append(html) ;
         $("#commTable").show() ;
         var selectObj = $("#table-list").find("tr").find("select");
         $(selectObj).selectpicker({noneSelectedText : '请选择'});
         $(selectObj).prev().prev().css({"height": "30px","margin-bottom": "5px","border": "1px solid #dadada","background": "white","font-size": "12px"}) ;
         $(selectObj).prev().css({"z-index":"99"}) ;
         $(selectObj).selectpicker('refresh');
         

         $('#depot_select').selectpicker('val',['']);
         $('#depot_select').selectpicker('refresh');

    }

    /**
     *删除当前的表格行
     */
    function delDepot(org){
        $(org).parent().parent().remove() ;
    }

    /*
    *保存数据
    */
    function saveData(){
    	//状态
        var status = '1';
        $("input[name='modal_status']").each(function(){
            if($(this).prop("checked")){
                status = $(this).val();
            }
        });
        
        //关联模板
        var fileTempId = "";
        var fileTempCode = "";
        var fileTempName = "";
        //编辑
        if($m15004.params.id !=""){
        	fileTempId = $("input[name='modal_fileTempId']").val();
            fileTempCode = $("input[name='modal_fileTempCode']").val();
            fileTempName = $("input[name='modal_fileTempName']").val();
        }else{//新增
	        fileTempId = $("#fileTemp_select ").val();
	        fileTempCode = $("#fileTemp_select option:selected").attr('code');
	        fileTempName = $("#fileTemp_select option:selected").text();        	
        }
        if(fileTempId == null || fileTempId == "" || fileTempId == undefined){
        	$custom.alert.error("关联模板不能为空！");
        	return ;
        }       
        
      //进出仓配置
        var depotConfig = "";
        $("input[name='depot_config']").each(function(){
            if($(this).prop("checked")){
            	depotConfig = $(this).val();
            }
        });
        
        if(depotConfig == null || depotConfig == ""){
        	$custom.alert.error("进出仓配置不能为空！");
        	return ;
        }
        
      	//是否同关区
        var isSameArea = "";
        $("input[name='modal_isSameArea']").each(function(){
            if($(this).prop("checked")){
            	isSameArea = $(this).val();
            }
        });        
        
        
        if(depotConfig == "1" && (isSameArea == null || isSameArea == "")){
        	$custom.alert.error("是否同关区不能为空！");
        	return ;
        }
        //适用仓库
        var trs = $("#table-list").find("tr") ;
        var depots = [];
        $(trs).each(function(i , tr){
            var depotId = $(tr).find("td").eq(0).find("input").val() ;
            var depotCode = $(tr).find("td").eq(0).find("span").text() ;
            var depotName = $(tr).find("td").eq(1).text() ;
            var platformCustomsIds = $(tr).find("td").eq(2).find("select").val() +"";
            var depot = { "depotId" : depotId ,  "depotName" : depotName , "depotCode" : depotCode, "platformCustomsIds" : platformCustomsIds};
            depots.push(depot);
        }) ;
        
        if (!depots || depots.length == 0) {
        	$custom.alert.error("适用仓库不能为空！");
        	return ;
        }
        
        var jsonData = {};
        jsonData.id = $m15004.params.id;
        jsonData.status = status;
        jsonData.depotConfig = depotConfig;
        jsonData.isSameArea = isSameArea;
        jsonData.fileTempId = fileTempId;
        jsonData.fileTempCode = fileTempCode;
        jsonData.fileTempName = fileTempName;
        jsonData.depotRelList = depots;
        var jsonStr = JSON.stringify(jsonData);
        var url = serverAddr + "/customsFileConfig/saveOrModify.asyn" ;
        $custom.request.ajax(url, {"json" : jsonStr} ,function(data) {
            if (data.state == 200) {
            	if(data.data.code == '00'){
	            	$custom.alert.success("操作成功！");
	                setTimeout(function () {
	                    $module.load.pageOrder('act=16011');
	                }, 500);
	                $m15004.hide();
            		
            	}else{
            		$custom.alert.error(data.data.message);
            	}
            } else {
                $custom.alert.error(data.expMessage);
            }
        });
    }

    function judgeCodeExistOrNot(code){
        var trs = $("#commTable").find("tr") ;

        var flag = false ;

        $(trs).each(function(index , tr){
            temp = $(tr).find("td").eq(0).text() ;

            if(temp == code){
                flag =true ;

                return false ;
            }
        }) ;

        return flag ;
    }
</script>