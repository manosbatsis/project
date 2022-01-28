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
                                    <li class="breadcrumb-item"><a href="javascript:dh_list()">采购SD配置</a></li>
                                </ol>
                           </div>
                        </div>
                        <!-- 筛选条件开始 -->
                        <form id="form1">
                        <div class="form_box">
                            <div >
                                <div class="form_item h35">
                                    <span class="msg_title">公司 :</span>
                                    <select class="input_msg" name="merchantId" id="merchantId"  >
                                    </select>
                                    <span class="msg_title">事业部 :</span>
                                    <select class="input_msg" name="buId" id="buId"  >
                                    </select>
                                   <span class="msg_title">供应商 :</span>
                                   <select class="input_msg" name="supplierId" id="supplierId"  >
                                    <option value="">请选择</option>
                                    </select>
                                  <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='searchData();'>查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                    <!-- 筛选条件结束 -->
                        <div class="row col-md-12 mt20">
                            <div class="button-list">
                                <shiro:hasPermission name="sdPurchaseConfig_add">
                                    <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="add-buttons">新增</button>
                                </shiro:hasPermission>
                                <shiro:hasPermission name="sdPurchaseConfig_copy">
                                    <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="cp-buttons">复制</button>
                                </shiro:hasPermission>
                                <shiro:hasPermission name="sdPurchaseConfig_del">
                                    <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="del-buttons">删除</button>
                                </shiro:hasPermission>
                            </div>
                        </div>
                        <div class="row mt10 table-responsive">
                            <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                                <thead>
                                <tr>
	                                <th><label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
	                                    <input type="checkbox" name="keeperUserGroup-checkable"
	                                           class="group-checkable" /> <span></span>
	                                </label></th>
                                    <th style="white-space:nowrap;">公司</th>
                                    <th style="white-space:nowrap;">事业部</th>
                                    <th style="white-space:nowrap;">供应商</th>
                                    <th style="white-space:nowrap;">生效时间</th>
                                    <th style="white-space:nowrap;">失效时间</th>
                                    <th style="white-space:nowrap;">创建人</th>
                                    <th style="white-space:nowrap;">创建时间</th>
                                    <th style="white-space:nowrap;">状态</th>
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
<script type="text/javascript">

	//根据供应商加载
	$module.supplier.loadAllSelectData.call($("select[name='supplierId']")[0]);
	//公司下拉
	$module.merchantAll.loadSelectData.call($('select[name="merchantId"]')[0]);
	//事业部下拉
	$module.businessUnit.getAllSelectBean.call($('select[name="buId"]')[0]);
	

	//加载状态
	$(document).ready(function() {
	    //初始化
	    $mytable.fn.init();
	    //配置表格参数
	    $mytable.params={
	        url : serverAddr+'/sdPurchaseConfig/sdPurchaseConfigList.asyn?r=' + Math.random(),
	        columns:[{ //复选框单元格
	            className: "td-checkbox",
	            orderable: false,
	            width: "30px",
	            data: null,
	            render: function (data, type, row, meta) {
	                 return '<input type="checkbox" class="iCheck">';
	            }
	        },
	        {data : 'merchantName'},{data : 'buName'},{data : 'supplierName'},{data : 'effectiveTime'},{data : 'invalidTime'},
	        {data : 'creator'},{data : 'createDate'},
	        {data : 'statusLabel'},
	            { //操作
	                orderable: false,
	                width: "130px",
	                data: null,
	                render: function (data, type, row, meta) {   
	                	var edit = "";
	                	
	                	if(row.status == "0"){
		                	edit +='<shiro:hasPermission name="sdPurchaseConfig_edit"><a href="javascript:edit('+row.id+')">编辑</a></shiro:hasPermission> &nbsp';
	                	}
	                	
	                	edit +='<shiro:hasPermission name="sdPurchaseConfig_detail"><a href="javascript:detail('+row.id+')">详情</a></shiro:hasPermission> &nbsp';
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
	
	//编辑
	function edit(id){
		$module.load.pageOrder("act=200022&id=" + id);
	}
	//详情
    function detail(id){
        $module.load.pageOrder("act=200024&id=" + id);
    }
	//新增
	$("#add-buttons").click(function(){	
		$module.load.pageOrder("act=200021");
	});
	//复制
	$("#cp-buttons").click(function(){	
		
		var id=$mytable.fn.getCheckedRow();
        if(!id || id.indexOf(",") > -1){
            $custom.alert.warningText("请选择一条记录！");
            return ;
        }
		
		$module.load.pageOrder("act=200023&id=" + id);
	});
	//删除
    $("#del-buttons").click(function(){  
    	var ids=$mytable.fn.getCheckedRow();
        if(ids==null||ids==''||ids==undefined){
            $custom.alert.warningText("至少选择一条记录！");
            return ;
        }
        
        $custom.alert.warning("确定删除选中对象？",function(){
	        $custom.request.ajax(serverAddr+"/sdPurchaseConfig/delOrders.asyn",{"ids":ids},function(result){
	        	if(result.state == '200'){
                    $custom.alert.success("删除成功");
                    setTimeout(function () {
                    	$module.load.pageOrder("act=20002");
                    }, 1000);
                }else{
                    if(result.expMessage != null){
                        $custom.alert.error(result.expMessage);
                    }else{
                        $custom.alert.error(result.message);
                    }
                }
	        });
        });
    });
	
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
    }) ;
    $('#datatable-buttons').on("change", ":checkbox", function() {
        $(this).parents('tr').toggleClass('success');
    }) ;
	

</script>
