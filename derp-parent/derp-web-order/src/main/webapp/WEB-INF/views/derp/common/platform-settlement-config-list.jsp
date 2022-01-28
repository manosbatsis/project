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
								<a href="#" data-toggle="tab" class="nav-link " onclick="$module.load.pageOrder('act=14003');">费项配置</a>
							</li>
						</shiro:hasPermission> 
						<shiro:hasPermission name="platformSettlementConfig_list"> 
								<li class="nav-item">
									<a href="#poList" data-toggle="tab" class="nav-link active" onclick="$module.load.pageOrder('act=14017');">平台费用映射</a>
								</li>
				 		</shiro:hasPermission> 
		           	</ul>
                        <!-- 筛选条件开始 -->
                        <form id="form1">
                        <div class="form_box">
                            <div>
                               
                                <div class="form_item h35">
                                    <span class="msg_title">平台名称 :</span>
                                    <select class="input_msg" name="storePlatformCode" id="storePlatformCode" >
                                    </select>
                                    
                                    <span class="msg_title">平台费项名称 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" id="name" name="name">
                                   <span class="msg_title">本级费项名称 :</span>
                                   <select class="input_msg" name="settlementConfigId" id="settlementConfigId" >
                                     <option value="">请选择</option>
                                     <c:forEach var="settlement" items="${settlementConfigList}">
								        <option value="${settlement.id}">${settlement.projectName}</option>
								     </c:forEach>
                                    </select>
                                  <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='searchData();'>查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                
                                <div class="form_item h35">
                                    <span class="msg_title">NC收支费项 :</span>
                                    <select class="input_msg" name="ncPaymentId" id="ncPaymentId" >
                                    <option value="">请选择</option>
                                    <c:forEach var="receivePayment" items="${receivePaymentSubjectList}">
								        <option value="${receivePayment.id}">${receivePayment.name}</option>
								     </c:forEach>
                                    </select>
                                    
                                    <span class="msg_title">状态 :</span>
                                    <select class="input_msg" name="status" id="status" >
                                    </select>

                                </div>

                                <!--  <a href="javascript:;" class="unfold">展开功能</a> -->
                            </div>
                        </div>
                    </form>
                    <!-- 筛选条件结束 -->
                        <div class="row col-md-12 mt20">
                            <div class="button-list">
                                <shiro:hasPermission name="platformSettlementConfig_toAddPage">
                                    <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="add-buttons">新增</button>
                                </shiro:hasPermission>
                                <shiro:hasPermission name="platformSettlementConfig_export">
                                    <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="export">导出</button>
                                </shiro:hasPermission>
                                <shiro:hasPermission name="platformSettlementConfig_import">
                                    <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="importPage">导入</button>
                                </shiro:hasPermission>
                            </div>
                        </div>
                        <div class="mt20">
                            <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                                <thead>
                                <tr>
		                            
                                    <th style="white-space:nowrap;">序号</th>
                                    <th style="white-space:nowrap;">平台名称</th>
                                    <th style="white-space:nowrap;">平台费项名称</th>
                                    <th style="white-space:nowrap;">本级费项名称</th>
                                    <th style="white-space:nowrap;">NC收支费项</th>
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
<jsp:include page="/WEB-INF/views/modals/14004.jsp" />
<jsp:include page="/WEB-INF/views/modals/14005.jsp" />
<script type="text/javascript">
$module.constant.getConstantListByNameURL.call($('select[name="storePlatformCode"]')[0],"storePlatformList",null);
$module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"settlementConfig_statusList",null);

//导出
$("#export").on("click",function(){
	//根据筛选条件导出
	var storePlatformCode = $("#storePlatformCode").val();
	var name = $("#name").val();
	var settlementConfigId = $("#settlementConfigId").val();
	var status = $("#status").val();
    var ncPaymentId = $("#ncPaymentId").val();
    url=serverAddr+"/platformSettlementConfig/export.asyn?storePlatformCode="+storePlatformCode+"&name="+name+"&settlementConfigId="+settlementConfigId+
    		"&status="+status+"&ncPaymentId="+ncPaymentId;
    $downLoad.downLoadByUrl(url);
   
});
//加载状态
$(document).ready(function() {
    //初始化
    $mytable.fn.init();
    //配置表格参数
    $mytable.params={
        url : serverAddr+'/platformSettlementConfig/platformSettlementConfigList.asyn?r=' + Math.random(),
        columns:[{ //复选框单元格
            className: "td-checkbox",
            orderable: false,
            width: "30px",
            data: null,
            render: function (data, type, row, meta) {
                 return '<input type="checkbox" class="iCheck">'; itemProjectCodes
            }
        },
        {data : 'storePlatformName'},{data : 'name'},{data : 'settlementConfigName'},{data : 'ncPaymentName'},{data : 'statusLabel'},
        {data : 'modifyDate'},{data : 'modifierName'},
            { //操作
                orderable: false,
                width: "130px",
                data: null,
                render: function (data, type, row, meta) {   
                	var edit = "";
                	edit='<shiro:hasPermission name="platformSettlementConfig_edit"><a href="javascript:edit('+row.id+')">编辑</a></shiro:hasPermission> &nbsp';
                	if (row.status=='0') {
                    	edit=edit+'<shiro:hasPermission name="platformSettlementConfig_isOrNotEnable"><a href="javascript:isOrNotEnable('+row.id+","+1+')">启用</a></shiro:hasPermission> &nbsp';
					}else {
                    	edit=edit+'<shiro:hasPermission name="platformSettlementConfig_isOrNotEnable"><a href="javascript:isOrNotEnable('+row.id+","+0+')">禁用</a></shiro:hasPermission> &nbsp';
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

//跳转导入页面
$("#importPage").click(function(){
	$module.load.pageOrder("act=14020");	
});	

/* 启用禁用 */
function isOrNotEnable(id,status){
	$custom.request.ajax(serverAddr+"/platformSettlementConfig/isOrNotEnable.asyn", {"id":id,"status":status}, function(result){
		if(result.state == '200'){
			 $custom.alert.success("修改成功");			 			
			 setTimeout("dh_list();" , 100) ;

		}else{
			$custom.alert.error("修改失败");			
		}						
	});
	$load.a("/platformSettlementConfig/toPage.html");
}
//编辑
function edit(id){
	$('#platedit-settlement-modal').modal('show'); 
	$("#ncPaymentIdEdit").empty();
	$('#ncPaymentNameEdit').empty();
	$('#ncPaymentIdEditText').empty()

		// 获取编辑详情
	  $custom.request.ajax(serverAddr+"/platformSettlementConfig/toDetail.asyn", {"id" : id }, function(data){		  
				if(data.state == '200'){
					debugger;
					var nameEdit=data.data.name;
					var storePlatformCodeEdit=data.data.storePlatformCode;
					var statusEdit=data.data.status;
					var settlementConfigIdEdit=data.data.settlementConfigId;
					var ncPaymentIdEdit=data.data.ncPaymentId;
					var ncPaymentNameEdit=data.data.ncPaymentName;
					var idEdit=data.data.id;

					$('#oldNameEdit').val(nameEdit);
					$('#nameEdit').val(nameEdit);
					$('#idEdit').val(idEdit);
					$('#ncPaymentIdEdit').val(ncPaymentIdEdit);
					$('#ncPaymentNameEdit').val(ncPaymentNameEdit);
					$('#ncPaymentIdEditText').val(ncPaymentNameEdit);
					if(statusEdit =='0'){
						$("#statusEdit0").prop("checked",true);			             
			     	}else {
			     		$("#statusEdit1").prop("checked",true);
					}
					debugger;
					$module.constant.getConstantListByNameURL.call($('select[name="storePlatformCodeEdit"]')[0],"storePlatformList",storePlatformCodeEdit);
					
					$custom.request.ajax(serverAddr+"/platformSettlementConfig/getAllSelectBean.asyn", {}, function(data1){
						debugger;
						var jsonData1=data1.data.settlementConfigList;
						$("#settlementConfigIdEdit").empty();
						var selectObj1=$("#settlementConfigIdEdit");
						selectObj1.append("<option selected value=''>"+"请选择"+"</option>");
						jsonData1.forEach(function(o,i){
							if (settlementConfigIdEdit== o.id) {
								selectObj1.append("<option selected value='"+ o.id+"'>"+o.projectName+"</option>");
							}else {
								selectObj1.append("<option value='"+ o.id+"'>"+o.projectName+"</option>");
							}			       
					    });	
						
						/* var jsonData2=data1.data.receivePaymentSubjectList;
						$("#ncPaymentIdEdit").empty();
						var selectObj2=$("#ncPaymentIdEdit");		
						selectObj2.append("<option selected value=''>"+"请选择"+"</option>");
						jsonData2.forEach(function(o,i){
							if (ncPaymentIdEdit== o.id) {
								selectObj2.append("<option selected value='"+ o.id+"'>"+o.name+"</option>");
							}else {
								selectObj2.append("<option value='"+ o.id+"'>"+o.name+"</option>");
							}
					       
					    }); */
						
						
					});



				}else{
					$custom.alert.error(result.message);
				}
			});
	  
	  
	  
	  
}
//新增
$("#add-buttons").click(function(){	
	$('#platsave-settlement-modal').modal('show');
	//加载客户的下拉数据
	$("#storePlatformCodeAdd").empty();
	$("#settlementConfigIdAdd").empty();
	$("#ncPaymentIdAdd").empty();
	$("#ncPaymentIdAdd").empty();
	$("#nameAdd").val('');
	$module.constant.getConstantListByNameURL.call($('select[name="storePlatformCodeAdd"]')[0],"storePlatformList",null);
	$custom.request.ajax(serverAddr+"/platformSettlementConfig/getAllSelectBean.asyn", {}, function(data1){
		debugger;
		var jsonData1=data1.data.settlementConfigList;
		$("#settlementConfigIdAdd").empty();
		var selectObj1=$("#settlementConfigIdAdd");
		jsonData1.forEach(function(o,i){
			selectObj1.append("<option selected value='"+ o.id+"'>"+o.projectName+"</option>");				       
	    });	
		selectObj1.append("<option selected value=''>"+"请选择"+"</option>");
		/* var jsonData2=data1.data.receivePaymentSubjectList;
		$("#ncPaymentIdAdd").empty();
		var selectObj2=$("#ncPaymentIdAdd");
		jsonData2.forEach(function(o,i){
			selectObj2.append("<option selected value='"+ o.id+"'>"+o.name+"</option>");
	       
	    });
		selectObj2.append("<option selected value=''>"+"请选择"+"</option>"); */
	});
	
	
	
});






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
