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
                       <li class="breadcrumb-item"><a href="#">邮件提醒管理</a></li>
                       <li class="breadcrumb-item "><a href="javascript:$load.a('/email/toPage.html');">邮件提醒列表</a></li>
                    </ol>
                    </div>
            </div>
                    <!--  title   end  -->
                      
                     <form id="form1" >
                         <div class="form_box">
                             <div class="form_con">
                                 <div class="form_item h35">
                                     <span class="msg_title">公司:</span>
                                    <select name="merchantId" class="input_msg">
                                        <option value="">请选择</option>
                                          <c:forEach items="${merchantSelectBean }" var="merchant">
                                        <option value="${merchant.value }">${merchant.label }</option>
                                    </c:forEach> 
                                    </select>
									<span class="msg_title">供应商 :</span>
                                    <select name="customerId" class="input_msg">
                                        <option value="">请选择</option>
                                          <c:forEach items="${customerSelectBean }" var="customer">
                                        <option value="${customer.value }">${customer.label }</option>
                                    </c:forEach> 
                                    </select>
                                     <span class="msg_title">状态</span>
                                     <select class="input_msg" name="status">
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
                            <shiro:hasPermission name="email_toAddPage">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="add-buttons">新增</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="email_delEmail">
                                <input type="button" class="btn btn-find waves-effect waves-light btn-sm" value="删 除" onclick="$custom.request.delChecked('/email/delEmail.asyn');" />
                            </shiro:hasPermission>
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
							<th>公司</th>
                            <th>供应商</th>
                            <th>提醒类型</th>
                            <th>付款账期</th>
                            <th>基准时间</th>
                            <th>账期单位</th>
                            <th>编辑时间</th>
                            <th>状态</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
                    <!--  ================================表格  end ===============================================   -->
                </div>

                  <!-- end row -->
        </div>
        <!-- container -->
    </div>
    </div>

</div> <!-- content -->
<script type="text/javascript">
$module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"emailConfig_statusList",null);

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
         url:'/email/listEmail.asyn?r='+Math.random(),
         columns:[{ //复选框单元格
             className: "td-checkbox",
             orderable: false,
             width: "30px",
             data: null,
             render: function (data, type, row, meta) {
                 return '<input type="checkbox" class="iCheck">';
             }
         },
		 {data:'merchantName'},
         {data:'customerName'},
         {data:'reminderTypeLabel'},
         {data:'accountPeriodDay'},
         {data:'baseTimeTypeLabel'},
       	 {data:'accountUnitTypeLabel'},
       	 {data:'modifyDate'},
       	 {data:'statusLabel'},
         { //操作
             orderable: false,
             width: "130px",
             data: null,
             render: function (data, type, row, meta) {
            	var status = "";
             	if(row.status == '1'){
             		status = '  <shiro:hasPermission name="email_auditEmail"><a href="javascript:audit('+row.id+',0)">禁用</a></shiro:hasPermission>';
             	}else{
             		status = '  <shiro:hasPermission name="email_auditEmail"><a href="javascript:audit('+row.id+',1)">启用</a></shiro:hasPermission>';
             	}
                 return '<shiro:hasPermission name="email_toDetailsPage"><a href="javascript:details('+row.id+')">详情</a></shiro:hasPermission>  '+'  <shiro:hasPermission name="email_toEditPage"><a href="javascript:edit('+row.id+')">编辑</a></shiro:hasPermission>'  +status;
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
	 	$load.a("/email/toAddPage.html");
	 });

	 //详情
	 function details(id){
    	$module.revertList.serializeListForm() ;
    	$module.revertList.isMainList = true ;  
		 
	 	$load.a("/email/toDetailsPage.html?id="+id);
	 }
 
	 //编辑
	 function edit(id){
		 
    	$module.revertList.serializeListForm() ;
    	$module.revertList.isMainList = true ; 
		 
	 	$load.a("/email/toEditPage.html?id="+id);
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
		    	var url = "/email/auditEmail.asyn";
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
							
							$load.a("/email/toPage.html");
						}, 1000);
					}else{
						$custom.alert.error(state + "失败！");
					}
				});
	    	});
	    }
</script>
