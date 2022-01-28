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
                                    <li class="breadcrumb-item"><a href="javascript:dh_list()">采购SD列表</a></li>
                                </ol>
                           </div>
                        </div>
                        <!-- 筛选条件开始 -->
                        <form id="form1">
                        <div class="form_box">
                            <div >
                                <div class="form_item h35">
                                	<span class="msg_title">采购订单号 :</span>
                                    <input class="input_msg" type="text" name="purchaseCodes" id="purchaseCodes" >
                                    <span class="msg_title">采购SD单号 :</span>
                                    <input class="input_msg" type="text" name="code" id="code" >
                                    <span class="msg_title">单据类型 :</span>
									<select class="input_msg" name="type" id="type">
									</select>
                                  	<div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='searchData();'>查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">	
                                    <span class="msg_title">PO号 :</span>
                                    <textarea class="input_msg" name="poNos" id="poNos" placeholder="多PO查询时，以&字符或换行隔开"></textarea>
                                	<span class="msg_title">入库时间 :</span>
                                    <input type="text" class="input_msg form_datetime date-input" name="inboundStartDateStr" id="inboundStartDateStr" style="font-size:13px;">
                                    -
                                    <input type="text" class="input_msg form_datetime date-input" name="inboundEndDateStr" id="inboundEndDateStr" style="font-size:13px;">
                                </div>
                            </div>
                        </div>
                    </form>
                    <!-- 筛选条件结束 -->
                        <div class="row col-md-12 mt20">
                            <div class="button-list">
                            	<shiro:hasPermission name="purchaseSdOrder_amount_import">
                                    <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="amount-import-buttons">导入调整SD</button>
                                </shiro:hasPermission>
                                <shiro:hasPermission name="purchaseSdOrder_export">
                                    <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="export-buttons">导出</button>
                                </shiro:hasPermission>
                                <shiro:hasPermission name="purchaseSdOrder_del">
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
                                    <th style="white-space:nowrap;">采购SD单号</th>
                                    <th style="white-space:nowrap;">单据类型</th>
                                    <th style="white-space:nowrap;">关联单号</th>
                                    <th style="white-space:nowrap;">数量</th>
                                    <th style="white-space:nowrap;">金额</th>
                                    <th style="white-space:nowrap;">SD类型</th>
                                    <th style="white-space:nowrap;">SD金额</th>
                                    <th style="white-space:nowrap;">出入库时间</th>
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
<jsp:include page="/WEB-INF/views/modals/4002.jsp" />
                <!-- container -->
<script type="text/javascript">

	$derpTimer.init("#inboundStartDateStr", "yyyy-MM-dd") ;
	$derpTimer.init("#inboundEndDateStr", "yyyy-MM-dd") ;
	$module.constant.getConstantListByNameURL.call($('select[name="type"]')[0],"purchaseSdOrder_typeList");

	//加载状态
	$(document).ready(function() {
	    //初始化
	    $mytable.fn.init();
	    //配置表格参数
	    $mytable.params={
	        url : serverAddr+'/purchaseSdOrder/purchaseSdOrderList.asyn?r=' + Math.random(),
	        columns:[{ //复选框单元格
	            className: "td-checkbox",
	            orderable: false,
	            width: "30px",
	            data: null,
	            render: function (data, type, row, meta) {
	                 return '<input type="checkbox" class="iCheck">';
	            }
	        },
	        {data : 'code'},{data : 'typeLabel'},
	        {	
                data: null,
                render: function (data, type, row, meta) {   
                	var html = "";
                	
                	if(row.purchaseCode){
                		html += row.purchaseCode ;
                	}
                	
                	html += "<br>" ;
                	
                	if(row.poNo){
	                	html += "PO:" ;
	                	html += row.poNo;
                	}
                	
                    return html;
                }
	        },
	        {data : 'num'},
	        {	
                data: null,
                render: function (data, type, row, meta) {   
                	
                	var currency = "" ;
                	var amount = "" ;
                	
                	if(row.amount){
                		
                		if(row.currency){
                    		currency = row.currency ;
                    	}
                		
                		amount = row.amount ;
                	}
                	
                	var html = currency;
                	html += " " ;
                	html += amount ;
                    return html;
                }
	        },
	        {data : 'sdTypeName'},
	        {
	        	data: null,
                render: function (data, type, row, meta) {   
                	
                	var currency = "" ;
                	var sdAmount = "" ;
                	
                	if(row.sdAmount){
                		
                		if(row.currency){
                			currency = row.currency ;
                    	}
                		
                		sdAmount = row.sdAmount ;
                	}
                	
                	var html = currency;
                	html += " " ;
                	html += sdAmount ;
                    return html;
                }	
	        },
	        {data : 'inboundDate'},
	            { //操作
	                orderable: false,
	                width: "130px",
	                data: null,
	                render: function (data, type, row, meta) {   
	                	var edit = "";
	                	
	                	if(row.editAble == "1"){
		                	edit +='<shiro:hasPermission name="purchaseSdOrder_edit"><a href="javascript:edit('+row.id+')">编辑</a></shiro:hasPermission> &nbsp';
	                	}
	                	
	                	edit +='<shiro:hasPermission name="purchaseSdOrder_detail"><a href="javascript:detail('+row.id+')">详情</a></shiro:hasPermission> &nbsp';
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
		$m4002.init(id) ;
	}
	//详情
    function detail(id){
        $module.load.pageOrder("act=30101&id=" + id);
    }
	
	$("#export-buttons").click(function(){
		var url = "" ;
    	
    	var ids=$mytable.fn.getCheckedRow();
    	if(ids){
    		url = serverAddr+"/purchaseSdOrder/exportOrder.asyn?ids="+ids ;
    	}else{
    		var params = $("#form1").serialize();
    		url = serverAddr+"/purchaseSdOrder/exportOrder.asyn?"+params ;
    	}
    	
    	$custom.alert.warning("确定导出选中对象？",function(){
        	$downLoad.downLoadByUrl(url);
        });
	}) ;
	
	// 删除
	$("#del-buttons").click(function(){
    	
    	var ids=$mytable.fn.getCheckedRow();
    	
    	if(ids==null||ids==''||ids==undefined){
            $custom.alert.warningText("至少选择一条记录！");
            return ;
        }
    	
    	$custom.alert.warning("确定删除选中对象？",function(){
    		$custom.request.ajax(serverAddr+"/purchaseSdOrder/delSdOrder.asyn",{"ids":ids},function(data){
    			if(data.state==200){
    				$custom.alert.success("操作成功！");
                    setTimeout(function () {
                    	$module.load.pageOrder("act=3010");
                    }, 1000);
                }else{
                	if(data.expMessage != null){
    					$custom.alert.error(data.expMessage);
    				}else{
    					$custom.alert.error(data.message);
    				}
                }
            });
        });
    	
	}) ;
	
	$("#amount-import-buttons").click(function(){
		$module.load.pageOrder("act=30102");
	}) ;
	
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
