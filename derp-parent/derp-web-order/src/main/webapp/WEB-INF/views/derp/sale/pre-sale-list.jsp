<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<jsp:include page="/WEB-INF/views/common.jsp" />
<!-- Start content -->
<style>
    .date-input {
        font-size: 13px;
    }
</style>
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
								<li class="breadcrumb-item"><a href="#">预售单列表</a></li>
							</ol>
						</div>
					</div>
					<!--  title   end  -->
					<form id="form1" autocomplete="off">
						<div class="form_box">
							<div class="">
								<div class="form_item h35">
									<span class="msg_title">预售单号 :</span>
									<input class="input_msg" name="code" />
									<span class="msg_title">客户 :</span>
									<select class="input_msg" name="customerId" id="customerId">
		                                <option value="">请选择</option>
		                            </select>
									<span class="msg_title">单据状态 :</span>
									<select class="input_msg" name="state">
									</select>
									<div class="fr">
										<button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();'>查询</button>
										<button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
									</div>
								</div>
								<div class="form_item h35">
									<span class="msg_title">事业部 :</span>
									<select class="input_msg" name="buId" id="buId"></select>
									<span class="msg_title">出仓仓库 :</span>
									<select class="input_msg" name="outDepotId">
									</select>
									<span class="msg_title">创建时间 :</span>
									<input type="text" class="input_msg form_datetime date-input" name="createDateStartDate" id="createDateStartDate">
									-
									<input type="text" class="input_msg form_datetime date-input" name="createDateEndDate" id="createDateEndDate">
								</div>
								<div class="form_item h35">
									<span class="msg_title">PO号 :</span>
									<input type="text" class="input_msg" name="poNo" id="poNo">
								</div>
							</div>
							<!-- <a href="javascript:" class="unfold">展开功能</a>  -->
						</div>
					</form>
					<div class="row col-12 mt-5">
						<shiro:hasPermission name="preSale_add">
						<button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default" id="save-buttons">新增</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="preSale_del">
							<button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default" onclick="del();">删除</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="preSale_to_saleOrder">
							<button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default" onclick="preSaleToSaleOrder();">预售转销</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="preSale_createPurchase">
							<input type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default" id="createPurchase" value="生成采购订单"/>
						</shiro:hasPermission>
						<shiro:hasPermission name="preSale_export">
							<button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default" id="exportOrder">导出</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="preSale_import">
							<button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default" id="importOrder-buttons">导入</button>
						</shiro:hasPermission>
					</div>
					<!--  ================================表格 ===============================================   -->
					<div class="mt10 table-responsive">
						<table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>
		                                <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
		                                    <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
		                                    <span></span>
		                                </label>
		                            </th>
									<th>预售单号</th>
									<th>客户</th>
									<th>事业部</th>
									<th>出仓仓库</th>
									<th>PO号</th>
									<th>预售数量</th>
									<th>预售总金额</th>
									<th>创建时间</th>
									<th>创建人</th>
									<th>状态</th>
									<th style="min-width: 60px">操作</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
					<!--  ================================表格  end ===============================================   -->
				</div>
				 <jsp:include page="/WEB-INF/views/modals/9061.jsp" />
				 <jsp:include page="/WEB-INF/views/modals/9061-V2.jsp" />
				<!-- end row -->
			</div>
			<!-- container -->
		</div>
		</div>
	</div>
	<!-- content -->
	<script type="text/javascript">
	$module.constant.getConstantListByNameURL.call($('select[name="state"]')[0],"preSaleOrder_stateList",null);
	//加载仓库的下拉数据
	$module.depot.getSelectBeanByMerchantRel.call($("select[name='outDepotId']")[0],"", {});
	//加载客户的下拉数据
	$module.customer.loadSelectData.call($('select[name="customerId"]')[0],"");
	//加载事业部
	$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null,null,null,null);

	$(document).ready(function() {
			//初始化
			$mytable.fn.init();
			//配置表格参数
			$mytable.params = {
				url : serverAddr+'/preSale/listPreSaleOrder.asyn?r=' + Math.random(),
				columns : [
						{ //复选框单元格
							className : "td-checkbox",
							orderable : false,
							data : null,
							render : function(data, type, row, meta) {
								return '<input type="checkbox" class="iCheck">';
							}
						},
						{data : 'code'},{data : 'customerName'},{data : 'buName'},{data : 'outDepotName'},
						{data : 'poNo'},{data : 'totalNum'},{data : 'totalAmount'},
						{data : 'createDate'},{data : 'createName'},{data : 'stateLabel'},
						{ //操作
							orderable : false,
							data : null,
							render : function(data, type, row, meta) {
								var edit = "";
								// 订单状态:001:待审核
								if (row.state == '001') {
									edit = '<shiro:hasPermission name="preSale_edit"><a href="javascript:edit('+ row.id +')">编辑</a></shiro:hasPermission>&nbsp'
								}
								return edit + '<shiro:hasPermission name="preSale_detail"><a href="javascript:details(' + row.id + ')">详情</a></shiro:hasPermission>';
							}
						}, ],
				formid : '#form1'
			};
			//每一行都进行 回调
	        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
	        	$('td:eq(7)', nRow).html(parseFloat(aData.totalAmount).toFixed(2));
	        };
			//生成列表信息
			$('#datatable-buttons').mydatatables();
		});

		function searchData() {
			$mytable.fn.reload();
		}
		//详情
		function details(id) {
			$module.load.pageOrder("act=60011&id=" + id);
		}
		//编辑
		function edit(id) {
			$module.load.pageOrder("act=60013&id=" + id);
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
		
	    laydate.render({
	        elem: '#createDateStartDate', //指定元素
	        type: 'datetime',
	        format: 'yyyy-MM-dd HH:mm:ss',
	        ready: function () {
	            $('.laydate-btns-time').remove();
	        }
	    });
	    laydate.render({
	        elem: '#createDateEndDate', //指定元素
	        type: 'datetime',
	        format: 'yyyy-MM-dd HH:mm:ss',
	        ready: function () {
	            $('.laydate-btns-time').remove();
	        },
	        done: function(value){
	            this.dateTime.hours = 23;
	            this.dateTime.minutes = 59;
	            this.dateTime.seconds = 59;
	        }
	    });

    // 点击展开功能
    $(".unfold").click(function () {
        $(".form_con").toggleClass('hauto');
        if($(this).text() == '展开功能'){
            $(this).text('收起功能');
        }else{
            $(this).text('展开功能');
        }
    });
	$(function () {
        $("#datatable-buttons_wrapper").removeClass('container-fluid');
    });

	//导出
	$("#exportOrder").on("click",function(){
		var jsonData=null;
		//获取选中的id信息
		var ids=$mytable.fn.getCheckedRow();
		if(ids){
			jsonData=$json.setJson(jsonData,"ids",ids);
		}else{
			//根据筛选条件导出
			var jsonArray=$("#form1").serializeArray();
			$.each(jsonArray, function(i, field){
				if(field.value!=null&&field.value!=''&&field.value!=undefined){
					jsonData=$json.setJson(jsonData,field.name,field.value);
				}
			});
		}
		var form = JSON.parse(jsonData);

		$custom.request.ajax(serverAddr+"/preSale/getOrderCount.asyn", form, function(data){//判断导出的数量是否超出1W
			if(data.state==200){
				var total = data.data.total;//总数
				if(total>10000){
					$custom.alert.error("导出的数量过大，请填写筛选条件再导出");
					return;
				}
				//导出数据
				var url = serverAddr+"/preSale/exportPreSaleOrder.asyn";
				var i =0;
				if(!!form){
					for(var key in form){
						if(i==0){
							url +="?";
						}else{
							url +="&";
						}
						url +=key+"="+form[key];
						i++;
					}
				}
				$downLoad.downLoadByUrl(url);
			}else{
				if(!!data.expMessage){
					$custom.alert.error(data.expMessage);

				}else{
					$custom.alert.error(data.message);

				}
			}
		});
	});
    // 新增
   	$("#save-buttons").click(function(){
   		$module.load.pageOrder('act=60012')
   	});
    //删除
    function del(){
    	$custom.request.delChecked(serverAddr+'/preSale/delPreSaleOrder.asyn');
    }
	//预售转销
	function preSaleToSaleOrder(){
		//获取选中的id信息
		var ids=$mytable.fn.getCheckedRow();
		if(ids==null||ids==''||ids==undefined){
			$custom.alert.warningText("请选择一条记录！");
			return ;
		}
		$custom.request.ajax(serverAddr+"/preSale/checkPreSale.asyn",{"ids":ids,"codes":null},function(data1){
			if(data1.state==200){
				$m9061.init(data1.data,data1.data[0],ids);
			}else{
				if(!!data1.expMessage){
					$custom.alert.error(data1.expMessage);
				}else{
					$custom.alert.error(data1.message);
				}
			}
		});
	}
	//生成采购订单
	$("#createPurchase").on("click",function(){
		//获取选中的id信息
		var id=$mytable.fn.getCheckedRow();
		if(id==null||id==''||id==undefined || id.split(",").length > 1){
			$custom.alert.warningText("请选择一条记录！");
			return ;
		}
		
		$custom.request.ajax(serverAddr+"/preSale/checkOrderState.asyn",{"id":id},function(data){
			if(data.state==200){
				$m9016V2.init(data.data);
			}else{
				if(!!data.expMessage){
					$custom.alert.error(data.expMessage);
				}else{
					$custom.alert.error(data.message);
				}
			}
		});
	});
	// 导入
	$("#importOrder-buttons").click(function(){
		$module.load.pageOrder("act=60014");
	});
	</script>