<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<jsp:include page="/WEB-INF/views/common.jsp" />
<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
          <div class="row">
            <div class="col-12">
                <div class="card-box">
                        <div class="col-12 row">
                            <div>
                                <ol class="breadcrumb">
                                    <li><i class="block"></i> 当前位置：</li>
                                    <li class="breadcrumb-item"><a href="javascript:dh_list()">账单管理/费项配置</a></li>
                                </ol>
                           </div>
                        </div>
                        <ul class="nav nav-tabs">
						<shiro:hasPermission name="settlementConfig_list">
							<li class="nav-item">
								<a href="#" data-toggle="tab" class="nav-link active" onclick="$module.load.pageOrder('act=14003');">费项配置</a>
							</li>
						</shiro:hasPermission> 
						<shiro:hasPermission name="platformSettlementConfig_list"> 
								<li class="nav-item">
									<a href="#poList" data-toggle="tab" class="nav-link " onclick="$module.load.pageOrder('act=14017');">平台费用映射</a>
								</li>
				 		</shiro:hasPermission> 
		           	</ul>
                        <!-- 筛选条件开始 -->
                        <form id="form1">
                        <div class="form_box">
                            <div>
                               
                                <div class="form_item h35">
                                    <span class="msg_title">所属层级 :</span>
                                    <select class="input_msg" name="level" id="level">
                                    </select>
                                    
                                    <span class="msg_title">上级费项名称 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" id="parentProjectName" name="parentProjectName">
                                   <span class="msg_title">本级费项名称 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" id="projectName" name="projectName">
                                  <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='searchData();'>查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                
                                <div class="form_item h35">
                                    <span class="msg_title">主数据编码 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" id="inExpCode" name="inExpCode">
                                    <span class="msg_title">NC收支费项 :</span>
                                    <select class="input_msg" name="paymentSubjectId" id="paymentSubjectId" >
                                    <option value="">请选择</option>
                                    </select>
                                    
                                    <span class="msg_title">状态 :</span>
                                    <select class="input_msg" name="status" id="status">
                                    </select>

                                </div>
                                 <div class="form_item h35">                                    
                                    <span class="msg_title">适用类型 :</span>
                                     <select class="input_msg" name="type" id="type" >
                                    </select>
                                     <span class="msg_title">适用模块 :</span>
                                     <select class="input_msg" name="moduleType" id="moduleType" >
                                     </select>
                                </div>
                                <!--  <a href="javascript:;" class="unfold">展开功能</a> -->
                            </div>
                        </div>
                    </form>
                    <!-- 筛选条件结束 -->
                        <div class="row col-md-12 mt20">
                            <div class="button-list">
                                <shiro:hasPermission name="settlementConfig_toAddPage">
                                    <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="add-buttons">新增</button>
                                </shiro:hasPermission>
                                <shiro:hasPermission name="settlementConfig_export">
                                    <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="export">导出</button>
                                </shiro:hasPermission>
                            </div>
                        </div>
                        <div class="mt20">
                            <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                                <thead>
                                <tr>
		                            
                                    <th style="white-space:nowrap;">序号</th>
                                    <th style="white-space:nowrap;">所属层级</th>
                                    <th style="white-space:nowrap;">上级费项名称</th>
                                    <th style="white-space:nowrap;">本级费项名称</th>
                                    <th style="white-space:nowrap;">本级编码</th>
                                    <th style="white-space:nowrap;">主数据编码</th>
                                    <th style="white-space:nowrap;">NC收支费项</th>
                                    <th style="white-space:nowrap;">适用客户</th>
                                    <th style="white-space:nowrap;">适用类型</th>
                                    <th style="white-space:nowrap;">状态</th>
                                    <th style="white-space:nowrap;">最近编辑时间</th>
                                    <th style="white-space:nowrap;">编辑人</th>
                                    <th style="white-space:nowrap;">操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <!--======================Modal框===========================  -->
                    <!-- end row -->
			</div>
		</div>
	</div>
</div>
                <!-- container -->
<jsp:include page="/WEB-INF/views/modals/14001.jsp" />
<jsp:include page="/WEB-INF/views/modals/14002.jsp" />
<script type="text/javascript">
$module.constant.getConstantListByNameURL.call($('select[name="level"]')[0],"settlementConfig_levelList",null);
$module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"settlementConfig_statusList",null);
$module.constant.getConstantListByNameURL.call($('select[name="type"]')[0],"settlementConfig_typeList",null);
$module.constant.getConstantListByNameURL.call($('select[name="moduleType"]')[0],"settlementConfig_moduleTypeList",null);
$custom.request.ajax(serverAddr+"/settlementConfig/ncReceivePayment.asyn", {}, function(data){
	var jsonData=data.data;
	var selectObj=$("#paymentSubjectId");
	jsonData.forEach(function(o,i){
        selectObj.append("<option value='"+ o.id+"'>"+o.name+"</option>");
    });								
});

//导出
$("#export").on("click",function(){
	//根据筛选条件导出
	var level = $("#level").val();
	var parentProjectName = $("#parentProjectName").val();
	var projectName = $("#projectName").val();
	var paymentSubjectId = $("#paymentSubjectId").val();
	var status = $("#status").val();
    var inExpCode = $("#inExpCode").val();
    url=serverAddr+"/settlementConfig/export.asyn?level="+level+"&parentProjectName="+parentProjectName+"&projectName="+projectName+"&paymentSubjectId="+paymentSubjectId+"&status="+status+"&inExpCode="+inExpCode;
    $downLoad.downLoadByUrl(url);
   
});
//加载状态
$(document).ready(function() {
    //初始化
    $mytable.fn.init();
    //配置表格参数
    $mytable.params={
        url : serverAddr+'/settlementConfig/settlementConfigList.asyn?r=' + Math.random(),
        columns:[{ //复选框单元格
            className: "td-checkbox",
            orderable: false,
            width: "30px",
            data: null,
            render: function (data, type, row, meta) {
                 return '<input type="checkbox" class="iCheck">'; itemProjectCodes
            }
        },
        {data : 'levelLabel'},{data : 'parentProjectName'},{data : 'projectName'},{data : 'projectCode'},{data : 'inExpCode'},{data : 'paymentSubjectName'},
        {data : 'customerNames'},{data : 'typeLabel'},{data : 'statusLabel'},{data : 'modifyDate'},{data : 'modifierName'},
            { //操作
                orderable: false,
                width: "130px",
                data: null,
                render: function (data, type, row, meta) {   
                	var edit = "";
                	edit='<shiro:hasPermission name="settlementConfig_edit"><a href="javascript:edit('+row.id+')">编辑</a></shiro:hasPermission> &nbsp';
                	if (row.status=='0') {
                    	edit=edit+'<shiro:hasPermission name="settlementConfig_isOrNotEnable"><a href="javascript:isOrNotEnable('+row.id+","+1+')">启用</a></shiro:hasPermission> &nbsp';
					}else {
                    	edit=edit+'<shiro:hasPermission name="settlementConfig_isOrNotEnable"><a href="javascript:isOrNotEnable('+row.id+","+0+')">禁用</a></shiro:hasPermission> &nbsp';
					}
                    return edit;
                }
            },
        ],
        formid:'#form1'
    }
    //每一行都进行 回调
    $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
    };
        
    //生成列表信息
    $('#datatable-buttons').mydatatables();
   
});

function searchData(){
    $mytable.fn.reload();
}
/* 启用禁用 */
function isOrNotEnable(id,status){
	$custom.request.ajax(serverAddr+"/settlementConfig/isOrNotEnable.asyn", {"id":id,"status":status}, function(result){
		if(result.state == '200'){
			 $custom.alert.success("修改成功");			 			
			 setTimeout("dh_list();" , 100) ;

		}else{
			$custom.alert.error("修改失败");			
		}						
	});
	$load.a("/settlementConfig/toPage.html");
}
//编辑
function edit(id){
	$('#itemSettlement-modal').modal('show');
    $('input[name="module-checkable"]').show();
    $('input[name="type-checkable"]').show();
    $('input[name="module-checkable"]').prop("checked",false);
    $('input[name="type-checkable"]').prop("checked",false);
		// 获取编辑详情
	  $custom.request.ajax(serverAddr+"/settlementConfig/toDetail.asyn", {"id" : id }, function(data){		  
				if(data.state == '200'){
					var inExpCode=data.data.inExpCode;
					var levelLabelEdit=data.data.levelLabel;
					var levelEdit=data.data.level;
					var projectNameEdit=data.data.projectName;
					var paymentSubjectIdEdit=data.data.paymentSubjectId;
					var parentIdEdit=data.data.parentId;
					var statusEdit=data.data.status;
					var suitableCustomerEdit=data.data.suitableCustomer;
					var customerIdsStr=data.data.customerIdsStr;
					var type=data.data.type;
					var moduleTypeStr=data.data.moduleType;
					
					if (!customerIdsStr) {
						customerIdsStr=null;
					}
					var idEdit=data.data.id;
					$('#inExpCodeEdit').val(inExpCode);
					$('#oldProjectNameEdit').val(projectNameEdit);
					$('#idEdit').val(idEdit);
					$('#levelEdit').val(levelEdit);
					$('#levelLabelEdit').val(levelLabelEdit);
					$('#projectNameEdit').val(projectNameEdit);
					if(suitableCustomerEdit =='1'){
						$("#suitableCustomerEdit1").prop("checked",true);			             
			     	}else if(suitableCustomerEdit=="2"){
			     		$("#suitableCustomerEdit2").prop("checked",true);
					}
					if(statusEdit =='0'){
						$("#statusEdit0").prop("checked",true);			             
			     	}else {
			     		$("#statusEdit1").prop("checked",true);
					}
					
					$custom.request.ajax(serverAddr+"/settlementConfig/ncReceivePayment.asyn", {}, function(data1){
						var jsonData1=data1.data;
						$("#receivePaymentSubjectEdit").empty();
						var selectObj1=$("#receivePaymentSubjectEdit");
						selectObj1.append("<option selected value=''>"+"请选择"+"</option>");
						jsonData1.forEach(function(o,i){
							if (paymentSubjectIdEdit== o.id) {
								selectObj1.append("<option selected value='"+ o.id+"'>"+o.name+"</option>");
							}else {
								selectObj1.append("<option value='"+ o.id+"'>"+o.name+"</option>");
							}
					    });								
					});
					$custom.request.ajax(serverAddr+"/settlementConfig/parentProjectNameList.asyn", {}, function(data2){
						var jsonData2=data2.data;
						$("#parentProjectNameEdit").empty();
						var selectObj2=$("#parentProjectNameEdit");						
						if ('2'==levelEdit) {
                            $("#redEdit").show();
							selectObj2.append("<option selected value=''>"+"请选择"+"</option>");
							jsonData2.forEach(function(o,i){
								if (parentIdEdit==o.id) {
									selectObj2.append("<option selected value='"+ o.id+"'>"+o.projectName+"</option>");
								}else {
									selectObj2.append("<option value='"+ o.id+"'>"+o.projectName+"</option>");
								}
						    });	
						}else {
							$("select[name='parentProjectNameEdit']").attr("disabled","disabled");
                            $("#redEdit").hide();
						}
													
					});
					debugger
					if(type!=null&&type!=""){
                        $(type.split(",")).each(function (i,e){
                            $("#editTypeId input[name='type-checkable'][value='"+e+"']").prop("checked",true);
                        });
                    }
                    if(moduleTypeStr!=""){
                        var flagShow=false;
                        $(moduleTypeStr.split(",")).each(function (i,e){
                            $("#editModuleId input[name='module-checkable'][value='"+e+"']").prop("checked",true);
                        });
                        test("3");
                    }
					$module.customer.loadSelectDataIds.call($('select[name="customerIdEdit"]')[0],customerIdsStr);
					$module.constant.getConstantListByNameURL.call($('select[name="typeEdit"]')[0],"settlementConfig_typeList",type);
				}else{
					$custom.alert.error(result.message);
				}
			});
	  
	  
	  
	  
}
//新增
$("#add-buttons").click(function(){

	$('#settlement-modal').modal('show');
	//加载客户的下拉数据
	$("#customerIdAdd").empty();
    $('input[name="module-checkable"]').show();
    $('input[name="type-checkable"]').show();
    $('input[name="module-checkable"]').prop("checked",false);
    $('input[name="type-checkable"]').prop("checked",false);
	$("#levelAdd").empty();
	$("#receivePaymentSubjectAdd").empty();
	$("#parentProjectNameEdit").val('');
	$module.customer.loadSelectDataIds.call($('select[name="customerIdAdd"]')[0],null);
	$module.constant.getConstantListByNameURL.call($('select[name="levelAdd"]')[0],"settlementConfig_levelList",null);
	/*$module.constant.getConstantListByNameURL.call($('select[name="typeAdd"]')[0],"settlementConfig_typeList",null);*/
	//获取NC收支费项下拉框	
	$custom.request.ajax(serverAddr+"/settlementConfig/ncReceivePayment.asyn", {}, function(data){
		var jsonData=data.data;
		$("#receivePaymentSubjectAdd").empty();
		var selectObj=$("#receivePaymentSubjectAdd");
		selectObj.append("<option selected value=''>"+"请选择"+"</option>");
		jsonData.forEach(function(o,i){
	        selectObj.append("<option value='"+ o.id+"'>"+o.name+"</option>");
	    });								
	});
	
});



function dh_list(){
    $module.load.pageOrder("act=14003");
}


// 点击展开功能
$(".unfold").click(function () {
    $(".form_con").toggleClass('hauto');
    if($(this).text() == '展开功能'){
        $(this).text('收起功能');
    }else{
        $(this).text('展开功能');
    }
})

</script>
