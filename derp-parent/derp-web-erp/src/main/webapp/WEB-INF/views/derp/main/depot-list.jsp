<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
        <div class="row">
            <!--  title   start  -->
            <div class="col-12">
                <div class="card-box">
                <div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                       <li><i class="block"></i> 当前位置：</li>
                       <li class="breadcrumb-item"><a href="#">仓库档案</a></li>
                       <li class="breadcrumb-item "><a href="javascript:$load.a('/depot/toPage.html');">仓库列表</a></li>
                    </ol>
                    </div>
            </div>
                    <!--  title   end  -->
                      
                     <form id="form1" >
                         <div class="form_box">
                             <div class="form_con">
                                 <div class="form_item h35">
                                     <span class="msg_title">仓库编号 :</span>
                                     <input type="text" required="" parsley-type="text" class="input_msg" name="code">
                                     <span class="msg_title">仓库名称 :</span>
                                     <input type="text" required="" parsley-type="text" class="input_msg" name="name">
                                     <span class="msg_title">仓库类别 :</span>
                                     <select class="input_msg" name="type">
                                     </select>
                                     <div class="fr">
                                         <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                         <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                     </div>
                                 </div>
                             </div>
                         </div>
                     </form>
                      <div class="row col-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="depot_toAddPage">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="add-buttons">新增</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="depot_delDepot">
                                <input type="button" class="btn btn-find waves-effect waves-light btn-sm" value="删 除" onclick="delDepot()" />
                            </shiro:hasPermission>
<!--                             <button type="button" class="btn btn-add waves-effect waves-light btn-sm" id="import-buttons">批量导入</button> -->
                        </div>
                    </div>     
                      <!--  ================================表格 ===============================================   -->
                    <div class="row mt10">
                    <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                        	<th>
                                <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                    <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
                                    <span></span>
                                </label>
                            </th>
<!--                             <th>序号</th> -->
							<th>仓库自编码</th>
                            <th>op仓库编号</th>
                            <th>仓库名称</th>
                            <th>仓库地址</th>
                            <th>仓库类别</th>
                            <th>仓管员</th>
                            <th>状态</th>
                            <th>创建时间</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
                    <!--  ================================表格  end ===============================================   -->
                </div>
                    <!--======================Modal框===========================  -->
        <jsp:include page="/WEB-INF/views/modals/1011.jsp" />
                  <!-- end row -->
        </div>
        <!-- container -->
    </div>
    </div>

</div> <!-- content -->
<script type="text/javascript">
//加载仓库类别
$module.constant.getConstantListByNameURL.call($('select[name="type"]')[0],"depotInfo_typeList",null);
$(document).ready(function() {
	
	//重置按钮
    $("button[type='reset']").click(function(){
    	$(".goods_select2").each(function(){
    		$(this).val("");
    	});
    	//重新加载select2
		$(".goods_select2").select2({
			language:"zh-CN",
			placeholder: '请选择',
			allowClear:true
		});
    });
	
     //初始化
     $mytable.fn.init();
     //配置表格参数
     $mytable.params={
         url:'/depot/listDepot.asyn?r='+Math.random(),
         columns:[{ //复选框单元格
             className: "td-checkbox",
             orderable: false,
             width: "30px",
             data: null,
             render: function (data, type, row, meta) {
                 return '<input type="checkbox" class="iCheck">';
             }
         },
		 {data:'depotCode'},
         {data:'code'},
         {data:'name'},
         {data:'address'},
         {data:'typeLabel'},
       	 {data:'adminName'},
       	 {data:'statusLabel'},
       	 {data:'createDate'},
         { //操作
             orderable: false,
             width: "130px",
             data: null,
             render: function (data, type, row, meta) {
            	var status = '<shiro:hasPermission name="depot_toEditPage"><a href="javascript:edit('+row.id+')">编辑</a></shiro:hasPermission>' + '  <shiro:hasPermission name="depot_toDetailsPage"><a href="javascript:details('+row.id+')">详情</a></shiro:hasPermission>  ';
             	if(row.status == '1'){
             		status += '<shiro:hasPermission name="depot_auditDepot"><a href="javascript:audit('+row.id+',0)">禁用</a></shiro:hasPermission>';
             	}else if(row.status == '0'){
             		status += '<shiro:hasPermission name="depot_auditDepot"><a href="javascript:audit('+row.id+',1)">启用</a></shiro:hasPermission>';
             	}else{
             		status = "" ;
             	}
             	
                 return status;
             }
         },
         ],
         formid:'#form1'
     };
     //生成列表信息
     $('#datatable-buttons').mydatatables();

     } );

	 function searchData(){
	     $mytable.fn.reload();
	 }
	
	 //新增
	 $("#add-buttons").click(function(){
	 	$load.a("/depot/toAddPage.html");
	 });
	//导入
	 $("#import-buttons").click(function(){
		$load.a("/depot/toImportPage.html");
	});
	 //详情
	 function details(id){
		
    	$module.revertList.serializeListForm() ;
    	$module.revertList.isMainList = true ; 
		 
	 	$load.a("/depot/toDetailsPage.html?id="+id);
	 }
 
	 //编辑
	 function edit(id){
		
    	$module.revertList.serializeListForm() ;
    	$module.revertList.isMainList = true ; 
		 
	 	$load.a("/depot/toEditPage.html?id="+id);
	 }

	 /**
	  * 全选
	  */
     $("input[name='keeperUserGroup-checkable']").on("change", function(){
         if($(this).is(':checked')){
             $(":checkbox", '#datatable-buttons').prop("checked",$(this).prop("checked"));
             $('#datatable-buttons tbody tr').addClass('success');
         }else{
             $(":checkbox", '#datatable-buttons').prop("checked",false);
             $('#datatable-buttons tbody tr').removeClass('success');
         }
     })
    $('#datatable-buttons').on("change", ":checkbox", function() {
    $(this).parents('tr').toggleClass('success');
})
 
	    // 禁用/启用
	    function audit(id, status){
	    	$custom.alert.warning("你确认要禁用/启用吗？",function(){
		    	var url = "/depot/auditDepot.asyn";
		    	var form = {"id":id, "status":status};
		    	var state = "";
		    	if(status == '1'){
		    		state = "启用";
		    	}else{
		    		state = "禁用";
		    	}
				$custom.request.ajax(url, form, function(result){
					if(result.state == '200'){
						$custom.alert.success(state + "成功！");
						setTimeout(function () {
							
							//列表菜单缓存参数清空
							$module.revertList.serializeListForm() ;
					    	$module.revertList.isMainList = true ; 
							
							$load.a("/depot/toPage.html");
						}, 1000);
					}else{
						$custom.alert.error(state + "失败！");
					}
				});
	    	});
	    }
	 
     function delDepot(){
 		
    	 var url = "/depot/delDepot.asyn" ;
    	 
    	 //获取选中的id信息
 		var ids=$mytable.fn.getCheckedRow();
 		if(ids==null||ids==''||ids==undefined){
 			$custom.alert.warningText("至少选择一条记录！");
 			return ;
 		}
 		
 		var flag = false ;
 		$custom.alert.warning("确定删除选中对象？",function(){
 			
 			var checkDepotUrl = inventoryWebhost + "/checkGoodsByInventory/checkDepot.asyn"
 			$custom.request.ajax(checkDepotUrl,{"ids":ids},function(data){
 				if(data.state == 200 && data.data == true){
 					$custom.request.ajax(url,{"ids":ids},function(data){
 		 				if(data.state==200){
 		 					$custom.alert.success("删除成功");
 		 					//删除成功，重新加载页面
 		 					$mytable.fn.reload();
 		 				}else{
 		 					if(data.expMessage != null){
 		 						$custom.alert.error(data.expMessage);
 		 					}else{
 		 						$custom.alert.error(data.message);
 		 					}
 		 				}
 		 			});
 				}else{
					if(data.expMessage != null){
 						$custom.alert.error(data.expMessage);
 					}else{
 						$custom.alert.error(data.message);
 					}
 				}
 			});
 			
 		});
     }
</script>
