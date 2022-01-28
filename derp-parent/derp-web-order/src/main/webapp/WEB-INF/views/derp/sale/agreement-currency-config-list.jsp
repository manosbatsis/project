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
								<li class="breadcrumb-item"><a href="#">协议单价列表</a></li>
							</ol>
						</div>
					</div>
					<!--  title   end  -->
					<form id="form1" autocomplete="off">
						<div class="form_box">
							<div class="form_con">
								<div class="form_item h35">
									<span class="msg_title">商品货号 :</span>
									<input class="input_msg" name="goodsNo" />
									<span class="msg_title">商品名称 :</span>
									<input class="input_msg" name="goodsName" />
									<span class="msg_title">移入事业部 :</span>
									<select  class="input_msg" name="inBuId"></select>
									<div class="fr">
										<button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();'>查询</button>
										<button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
									</div>
								</div>
								<div class="form_item h35">
									<span class="msg_title">移出事业部 :</span>
									<select  class="input_msg" name="outBuId"></select>
								</div>
							</div>
							<a href="javascript:" class="unfold">展开功能</a>
						</div>
					</form>
					<div class="row col-12 mt-5">
						<shiro:hasPermission name="agreementCurrencyConfig_add">
							<button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default" id="save-buttons">新增</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="agreementCurrencyConfig_import">
							<button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default" id="import-buttons">导入</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="agreementCurrencyConfig_export">
							<button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default" id="export-button">导出</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="agreementCurrencyConfig_del">
							<button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default" onclick="del();">删除</button>
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
									<th>公司</th>
									<th>移入事业部</th>
									<th>移出事业部</th>
									<th>商品货号</th>
									<th>商品名称</th>
									<th>协议单价</th>
									<th>协议币种</th>
									<th>创建人</th>
									<th>创建时间</th>
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
	//加载事业部
	$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='inBuId']")[0],null,null, null,null);
	$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='outBuId']")[0],null,null, null,null);

	$(document).ready(function() {
			//初始化
			$mytable.fn.init();
			//配置表格参数
			$mytable.params = {
				url : serverAddr+'/agreementCurrencyConfig/listAgreementCurrencyConfig.asyn?r=' + Math.random(),
				columns : [
						{ //复选框单元格
							className : "td-checkbox",
							orderable : false,
							data : null,
							render : function(data, type, row, meta) {
								return '<input type="checkbox" class="iCheck" value='+data.id+' >';
							}
						},
						{data : 'merchantName'},{data : 'inBuName'},{data : 'outBuName'},{data : 'goodsNo'},{data : 'goodsName'},
						{data : 'price'},{data : 'currencyLabel'}, {data : 'createName'},{data : 'createDate'},
						 ],
				formid : '#form1'
			};
			//每一行都进行 回调
	        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
	        };
			//生成列表信息
			$('#datatable-buttons').mydatatables();
		});
		function searchData() {
			$mytable.fn.reload();
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
	$("#export-button").on("click",function(){
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

		$custom.request.ajax(serverAddr+"/agreementCurrencyConfig/getOrderCount.asyn", form, function(data){//判断导出的数量是否超出1W
			if(data.state==200){
				var total = data.data.total;//总数
				if(total>10000){
					$custom.alert.error("导出的数量过大，请填写筛选条件再导出");
					return;
				}
				//导出数据
				var url = serverAddr+"/agreementCurrencyConfig/exportAgreementCurrencyConfig.asyn";
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
   		$module.load.pageOrder('act=15002')
   	});
    //删除
    function del(){
		//获取选中的id信息
    	$custom.request.delChecked(serverAddr+'/agreementCurrencyConfig/delAgreementCurrencyConfig.asyn');
    }
	// 导入
	$("#import-buttons").click(function(){
		$module.load.pageOrder("act=15003");
	});
	</script>