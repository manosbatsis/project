<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>

<jsp:include page="/WEB-INF/views/common.jsp" />

<!DOCTYPE html>
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
								<li class="breadcrumb-item"><a href="#">账单管理</a></li>
								<li class="breadcrumb-item"><a href="#">结算单列表</a></li>
							</ol>
						</div>
					</div>
					<form id="form1">
						<div class="form_box">
							<div class="">
								<div class="form_item h35">
									<span class="msg_title">结算单号 :</span> <input type="text"
										parsley-type="text" class="input_msg" id="code" name="code">
									<span class="msg_title">仓库名称 :</span> <select name="depotId"
										class="input_msg">
										<option value="">请选择</option>
									</select> 
									<span class="msg_title">结算对象 :</span> 
									<select name="customerId" class="input_msg">
										<option value="">请选择</option>
									</select>
									<div class="fr">
										<button type="button"
											class="btn ml15 btn-find waves-effect waves-light btn_default"
											onclick='$mytable.fn.reload();'>查询</button>
										<button type="reset"
											class="btn btn-light waves-effect waves-light btn_default">重置</button>
									</div>
								</div>
								<div class="form_item h35">
									<span class="msg_title">账单月份 :</span> 
									<input type="text" required="" parsley-type="text" class="input_msg date-input" name="month"> 
									<span class="msg_title">处理状态 :</span> 
									<select class="input_msg" name="status" id="status"></select> 
									<span class="msg_title">确认状态 :</span> 
									<select class="input_msg" name="confirmStatus">
										<option value="">请选择</option>
										<option value="1">已确认</option>
										<option value="0">待确认</option>
									</select>
								</div>
							</div>
							<!-- <a href="javascript:" class="unfold">展开功能</a>  -->
						</div>
					</form>
					<div class="row col-md-12 mt20">
						<div class="button-list">
							<shiro:hasPermission name="settlementBill_import">
								<input type="button"
									class="btn btn-find waves-effect waves-light btn-sm"
									id="importBtn" value="导入结算单" />
							</shiro:hasPermission>
							<shiro:hasPermission name="settlementBill_del">
								<input type="button"
									class="btn btn-find waves-effect waves-light btn-sm"
									onclick="changeStatus('006');" value="删除" />
							</shiro:hasPermission>
							<shiro:hasPermission name="settlementBill_flash">
								<input type="button"
									class="btn btn-find waves-effect waves-light btn-sm"
									onclick="changeStatus('1');" value="刷新" />
							</shiro:hasPermission>
							<shiro:hasPermission name="settlementBill_confirm">
								<input type="button"
									class="btn btn-find waves-effect waves-light btn-sm"
									onclick="changeStatus('4');" value="确认" />
							</shiro:hasPermission>
						</div>
					</div>

					<!--  ================================表格 ===============================================   -->
					<div class="row mt20">
						<table id="datatable-buttons" class="table table-striped"
							cellspacing="0" width="100%">
							<thead>
								<tr>
									<th><label
										class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
											<input type="checkbox" name="keeperUserGroup-checkable"
											class="group-checkable" /> <span></span>
									</label></th>
									<th>公司</th>
									<th>结算单号</th>
									<th>仓库名称</th>
									<th>结算对象</th>
									<th>平台</th>
									<th>结算金额</th>
									<th>结算币种</th>
									<th>账单月份</th>
									<th>计费周期</th>
									<th>创建人</th>
									<th>创建时间</th>
									<th>处理状态</th>
									<th>确认状态</th>
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
				<!-- end row -->
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	
	$module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"settlementBill_stateList",null);
	$('#status option[value="4"]').remove() ;
	$module.depot.getSelectBeanByMerchantRel.call($("select[name='depotId']")[0],null, {});
	//加载客户的下拉数据
	$module.customer.loadSelectData.call($('select[name="customerId"]')[0]);
	
	$(".date-input").datetimepicker({
		 format: 'yyyy-mm',
	       autoclose: true,
	       todayBtn: true,
	       startView: 'year',
	       minView:'year',
	       maxView:'decade',
	       language:  'zh-CN',
	});
	
	$(document).ready(function() {
		//初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:serverAddr+'/settlementBill/listSettlementBill.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
            {data:'merchantName'},{data:'code'},{data:'depotName'},{data:'customerName'},{data:'platformLabel'},
            {data:'settlementAccount'},{data:'currencyLabel'},{data:'month'},{data:'billDate'},
            {data:'createrName'},{data:'createDate'},{data:'statusLabel'},{data:'confirmStatusLabel'},
            { //操作
                orderable: false,
                width: "100px",
                data: null,
                render: function (data, type, row, meta) {
                	var html = "" ;
            		
                	if(row.status != '3'){
                		if (row.status != '1') {
							html += '<shiro:hasPermission name="settlementBill_export"><a href="javascript:download(\''+row.id+'\')">导出</a> </shiro:hasPermission>  ' ;
						}
	            		html += '<shiro:hasPermission name="settlementBill_details"><a href="javascript:details(\''+row.id+'\')">详情</a></shiro:hasPermission>' ;
                	}else{
                		html += '<a href="javascript:errorDetails(\''+row.id+'\')">失败信息</a>'
                	}
            		
            	    return html;
                }
            },
            ],
            formid:'#form1'
        };
        
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();
        searchData() ;
        
        function searchData() {
			$mytable.fn.reload();
		}
	})

    // 点击展开功能
    $(".unfold").click(function () {
        $(".form_con").toggleClass('hauto');
        if($(this).text() == '展开功能'){
            $(this).text('收起功能');
        }else{
            $(this).text('展开功能');
        }
    });
    
    //导入
   	$("#importBtn").click(function(){
   		$module.load.pageReport("act=170031");
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

    
    function changeStatus(status){
   	    var flag=0;
		  //获取选中的id信息
        var ids=$mytable.fn.getCheckedRow();
		if(ids==null||ids==''||ids==undefined){
			$custom.alert.warningText("请选择一条记录！");
			return ;
		}
		
		var msg = "" ;
        if('006' == status){
            msg = "确定删除?" ;
        }else if('4' == status){
            msg = "对勾选的账单进行确认操作<br>注确认后将不予刷新，请知悉！" ;
        }else if('1' == status){
            msg = "确认刷新?" ;
        }

		$custom.alert.warning(msg ,function(){
			$custom.request.ajax(serverAddr+"/settlementBill/changeStatus.asyn", {"ids" : ids, "status" : status}, function(result){
				if(result.state == 200){
	                $custom.alert.success("操作成功");
	                
	                setTimeout("$module.load.pageReport('act=17003');", 1000) ;
	            }else{
	                $custom.alert.error(result.expMessage);
	            }
			}); 
		});
 	}
    
	// 导出
	function download(id){
		var url = serverAddr+"/settlementBill/downLoad.asyn?id="+id;
		$downLoad.downLoadByUrl(url);
	}
	 
	function details(id){
		$module.load.pageReport("act=170032&id="+id);
	}
	
	function errorDetails(id){
		$custom.request.ajax(serverAddr+"/settlementBill/getErrorMsg.asyn", {"id" : id}, function(result){
			if(result.state == 200){
				if (result.data) {
					$custom.alert.error(result.data);
				} else {
					$custom.alert.error("");
				}
            }else{
                $custom.alert.error(result.expMessage);
            }
		}); 
	}
</script>