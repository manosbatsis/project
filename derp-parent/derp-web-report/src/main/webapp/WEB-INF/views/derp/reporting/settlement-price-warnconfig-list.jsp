<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<jsp:include page="/WEB-INF/views/common.jsp" />
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
                       <li class="breadcrumb-item"><a href="#">邮件管理</a></li>
                       <li class="breadcrumb-item "><a href="javascript:$load.a('/settlementPriceWarnconfigtoPage.html');">单价预警配置列表</a></li>
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
                                        <c:forEach items="${merchantList }" var="merchant">
                                            <option value="${merchant.value }" <c:if test="${merchant.value==detail.merchantId }">selected = 'selected'</c:if>>${merchant.label }</option>
                                        </c:forEach>
                                    </select>
									<span class="msg_title">事业部 :</span>
                                    <select class="input_msg" name="buId" id="buId"><option value=''>请选择</option></select>
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
                            <shiro:hasPermission name="settlement_price_warnconfig_toAddPage">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="add-buttons">新增</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="settlement_price_warnconfig_delete">
                                <input type="button" class="btn btn-find waves-effect waves-light btn-sm" value="删除" onclick="del();"/>
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
                            <th>事业部</th>
                            <th>波动范围</th>
                            <th>编辑日期</th>
                            <th>编辑人</th>
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
// 状态
$module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"settlementPriceWarnconfig_statusList",null);
var userType="${userType}";
if(userType!=1){
	 //加载事业部
	$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null,null,null,null); 
}

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
         url:serverAddr+'/settlementPriceWarnconfig/listSettlementPriceWarnconfig.asyn?r='+Math.random(),
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
         {data:'buName'},
         {data:'waveRange'},
         {data:'modifyDate'},
         {data:'modifierUsername'},
       	 {data:'statusLabel'},
         { //操作
             orderable: false,
             width: "130px",
             data: null,
             render: function (data, type, row, meta) {
            	var status = "";
             	if(row.status == '1'){
             		status = ' <shiro:hasPermission name="settlementPriceWarnconfig_audit"><a href="javascript:audit('+row.id+',0)">禁用</a></shiro:hasPermission>';
             	}else{
             		status = ' <shiro:hasPermission name="settlementPriceWarnconfig_audit"><a href="javascript:audit('+row.id+',1)">启用</a></shiro:hasPermission>';
             	}
                 return  '<shiro:hasPermission name="settlementPriceWarnconfig_toDetailsPage"><a href="javascript:details('+row.id+')">详情</a></shiro:hasPermission>  '
                 +'  <shiro:hasPermission name="settlementPriceWarnconfig_edit"><a href="javascript:edit('+row.id+')">编辑</a></shiro:hasPermission>'  +status;
             }
         },
         ],
         formid:'#form1'
     };
  	//每一行都进行 回调
     $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
 		 var waveRangeLabel =aData.waveRange+"%";
    	 $('td:eq(3)', nRow).html("<p>"+waveRangeLabel+"</p>");
     }; 
     
     //生成列表信息
     $('#datatable-buttons').mydatatables();

     } );

	 function searchData(){
	     $mytable.fn.reload();
	 }
	 //改变商家
	$("select[name='merchantId']").change(function(){
		 var merchantId=$("select[name='merchantId']").val();
		//加载事业部
		 $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null,merchantId,null,null);
	 });
	 //新增
	 $("#add-buttons").click(function(){
	 	$module.load.pageReport('act=15002');
	 });
	 // 编辑
	 function edit(id){
	 	$module.load.pageReport("act=15003&id=" + id);
	 }
	 // 详情
	 function details(id){
	 	$module.load.pageReport("act=15004&id=" + id);
	 }
    // 删除
    function del(){
		$custom.request.delChecked(serverAddr+'/settlementPriceWarnconfig/delSettlementPriceWarnconfig.asyn');
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
		    	var url = serverAddr+"/settlementPriceWarnconfig/auditSettlementPriceWarnconfig.asyn";
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
					    	//重新加载页面
							$mytable.fn.reload();
						}, 1000);
					}else{
						$custom.alert.error(state + "失败！");
					}
				});
	    	});
	    }
</script>
