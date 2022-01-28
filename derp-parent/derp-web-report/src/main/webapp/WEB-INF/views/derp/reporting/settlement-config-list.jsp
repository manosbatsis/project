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
								<li class="breadcrumb-item"><a href="#">账单管理</a></li>
								<li class="breadcrumb-item"><a href="#">结算项目</a></li>
							</ol>
						</div>
					</div>
					<!--  title   end  -->
					<form id="form1">
						<div class="form_box">
							<div class="form_con">
								<div class="form_item h35">
									<span class="msg_title">项目名称 :</span>
									<select name="parentProjectCode" class="input_msg">
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
									<th style="white-space:nowrap;" class="tc">项目ID</th>
									<th style="white-space:nowrap;" class="tc">项目名称</th>
									<th style="white-space:nowrap;" class="tc">项目小类</th>
									<th style="white-space:nowrap;" class="tc">最近编辑时间</th>
									<th style="white-space:nowrap;" class="tc">操作</th>
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
<jsp:include page="/WEB-INF/views/modals/19011.jsp" />
	<!-- content -->
	<script type="text/javascript">
	$module.constant.getConstantListByNameURL.call($('select[name="parentProjectCode"]')[0],"settlementConfig_projectList",null);
	$(document).ready(function() {
			//初始化
			$mytable.fn.init();
			//配置表格参数
			$mytable.params = {
				url : serverAddr+'/settlementConfig/listSettlementConfig.asyn?r=' + Math.random(),
				columns : [
						{ //复选框单元格
							className : "td-checkbox",
							orderable : false,
							data : null,
							render : function(data, type, row, meta) {
								return '<input type="checkbox" class="iCheck">';
							}
						},
					{data : 'parentProjectCode'},{data : 'parentProjectName'},{data : 'allProjectName'},{data : 'modifyDate'},
						{ //操作
							orderable : false,
							data : null,
							render : function(data, type, row, meta) {
								return  '<shiro:hasPermission name="settlementConfig_edit"><a href=\'javascript:void(0);\' onclick=\'edit(this)\'>编辑</a><div name ="showdiv" style="display:none;">'+data.parentProjectCode+'</div></shiro:hasPermission>';
							}
						}, ],
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
		//编辑
		function edit(obj) {
			var parentProjectCode=$(obj).next().text();
			$m19011.init(parentProjectCode);
		}
	</script>