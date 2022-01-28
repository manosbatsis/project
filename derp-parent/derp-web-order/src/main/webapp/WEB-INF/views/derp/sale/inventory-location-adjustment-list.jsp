<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
								<li class="breadcrumb-item"><a href="#">库位调整单列表</a></li>
							</ol>
						</div>
					</div>
					<!--  title   end  -->
					<form id="form1" autocomplete="off">
						<div class="form_box">
							<div class="">
								<div class="form_item h35">
									<span class="msg_title">库位调整单号 :</span>
									<input class="input_msg" name="code" />
									<span class="msg_title">仓库 :</span>
									<select class="input_msg" name="depotId">
									</select>
									<span class="msg_title">事业部 :</span>
									<select class="input_msg" name="buId" id="buId"></select>
									<div class="fr">
										<button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();'>查询</button>
										<button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
									</div>
								</div>
								<div class="form_item h35">
                                    <span class="msg_title">单据状态 :</span>
                                    <select class="input_msg" name="type"></select>
                                    <span class="msg_title">客户 :</span>
									<select class="input_msg" name="customerId" id="customerId">
		                                <option value="">请选择</option>
		                            </select>
                                </div>
							</div>
							<!-- <a href="javascript:" class="unfold">展开功能</a> -->
						</div>
					</form>
					<div class="row col-12 mt-5">
						<shiro:hasPermission name="inventoryLocationAdjust_import">
						<button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default" id="importOrder-buttons">导入</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="inventoryLocationAdjust_del">
							<button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default" onclick="del();">删除</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="inventoryLocationAdjust_export">
							<button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default" id="exportOrder">导出</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="inventoryLocationAdjust_audit">
							<button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default" id="audit-buttons">审核</button>
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
									<th>库位调整单</th>									
									<th>单据类型</th>
									<th>仓库</th>
									<th>事业部</th>
									<th>客户名称</th>
									<th>归属日期</th>
									<th>创建日期</th>
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
				<!-- end row -->
			</div>
			<!-- container -->
		</div>
		</div>
	</div>
	<!-- content -->
	<script type="text/javascript">
	 //加载客户的下拉数据
	$module.customer.loadSelectData.call($('select[name="customerId"]')[0]);
	//加载仓库的下拉数据
	$module.depot.getSelectBeanByMerchantRel.call($("select[name='depotId']")[0],"", {});
	//加载事业部
	$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null,null,null,null);
	//加载单据类型
	$module.constant.getConstantListByNameURL.call($('select[name="type"]')[0],"inventoryLocationAdjustmentOrder_typeList",null);
	
	$(document).ready(function() {
			//初始化
			$mytable.fn.init();
			//配置表格参数
			$mytable.params = {
				url : serverAddr+'/inventoryLocationAdjustment/listInventoryLocationAdjust.asyn?r=' + Math.random(),
				columns : [
						{ //复选框单元格
							className : "td-checkbox",
							orderable : false,
							data : null,
							render : function(data, type, row, meta) {
								return '<input type="checkbox" name="item-checkbox" class="iCheck" value="'+data.id+'">';
							}
						},
						{data : 'code'},{data : 'typeLabel'},{data : 'depotName'},{data : 'buName'},{data : 'customerName'},{data : 'ownDate'},
						{data : 'createDate'},{data : 'createName'},{data : 'stateLabel'},
						{ //操作
							orderable : false,
							data : null,
							render : function(data, type, row, meta) {
								return  '<shiro:hasPermission name="inventoryLocationAdjust_detail"><a href="javascript:details(' + row.id + ')">详情</a></shiro:hasPermission>';
							}
						}],
				formid : '#form1'
			};
			//每一行都进行 回调
	        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
				// 返回日期格式化校验
				var ownDate=new Date(aData.ownDate);
				var year = ownDate.getFullYear();
				var month = ownDate.getMonth()+ 1, month = month < 10 ? '0' + month : month;
				var day = ownDate.getDate(), day = day < 10 ? '0' + day : day;
				$('td:eq(6)', nRow).html(year + '-' + month + '-' + day);
	        };
			//生成列表信息
			$('#datatable-buttons').mydatatables();
		});

		function searchData() {
			$mytable.fn.reload();
		}
		//详情
		function details(id) {
			$module.load.pageOrder("act=18003&id=" + id);
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

		$custom.request.ajax(serverAddr+"/inventoryLocationAdjustment/getOrderCount.asyn", form, function(data){//判断导出的数量是否超出1W
			if(data.state==200){
				var total = data.data.total;//总数
				if(total>10000){
					$custom.alert.error("导出的数量过大，请填写筛选条件再导出");
					return;
				}
				//导出数据
				var url = serverAddr+"/inventoryLocationAdjustment/exportInventoryLocationAdjust.asyn";
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
    //删除
    function del(){
    	$custom.request.delChecked(serverAddr+'/inventoryLocationAdjustment/delInventoryLocationAdjust.asyn');
    }

	// 导入
	$("#importOrder-buttons").click(function(){
		$module.load.pageOrder("act=18002");
	});
	//审核
	$("#audit-buttons").click(function(){
		var checkeds = $.find("input[name='item-checkbox']:checked") ;
		var ids = new Array() ;

		if(checkeds.length == 0){
			$custom.alert.warningText("至少选择一条记录！");
			return ;
		}
		$custom.alert.warning("确定审核？",function(){
			$(checkeds).each(function(i , m){
				ids.push($(m).val());
			}) ;
			var idsStr = ids.join(",") ;
			//执行审核
			$custom.request.ajax(serverAddr+"/inventoryLocationAdjustment/auditInventoryLocationAdjust.asyn",{"ids":idsStr},function(data){
				if(data.state==200){
					$custom.alert.success("审核成功");
					//重新加载页面
					$mytable.fn.reload();
				}else{
					if(!!data.expMessage){
						$custom.alert.error(data.expMessage);
					}else{
						$custom.alert.error(data.message);
					}
				}
			});
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
    });
	</script>