<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<jsp:include page="/WEB-INF/views/common.jsp" />
<!-- Start content -->
<div class="content">
	<div class="container-fluid mt80">
		<!-- Start row -->
		<div class="row">
			<div class="col-12">
				<div class="card-box table-responsive">
					<!--  title   start  -->
					<div class="col-12">
						<div>
							<div class="col-12 row">
								<div>
									<ol class="breadcrumb">
										<li><i class="block"></i> 当前位置：</li>
										<li class="breadcrumb-item"><a
											href="javascript:dh_list();">采购SD配置</a></li>
										<li class="breadcrumb-item"><a
											href="javascript:dh_add();">新增采购SD配置</a></li>
									</ol>
								</div>
							</div>
							<!--  title   end  -->
							<!--  title   基本信息   start -->
							<div class="title_text">基本信息</div>
							<form id="add-form">
								<div class="edit_box mt20">
									<div class="edit_item">
										<label class="edit_label"><i class="red">*</i> 公司：</label>
										<select class="edit_input" name="merchantId" id="merchantId" required reqMsg="公司不能为空">
											<option value="">请选择</option>
										</select>
									</div>
									<div class="edit_item">
										<label class="edit_label"><i class="red">*</i> 事业部：</label> 
										<select class="edit_input" name="buId" id="buId" required reqMsg="事业部不能为空">
											<option value="">请选择</option>
										</select>
									</div>
									<div class="edit_item">
										<label class="edit_label"><i class="red">*</i> 供应商：</label> 
										<select class="edit_input" name="supplierId" id="supplierId" required reqMsg="供应商不能为空">
											<option value="">请选择</option>
										</select>
									</div>
								</div>
								<div class="edit_box">
									<div class="edit_item">
										<label class="edit_label"><i class="red">*</i> 生效时间：</label>
										 <input type="text" class="edit_input" name="effectiveTime" id="effectiveTime" style="font-size: 13px;" required
											reqMsg="生效时间不能为空">
									</div>
									<div class="edit_item">
										<label class="edit_label"><i class="red">*</i> 失效时间：</label>
										<input type="text" class="edit_input" name="invalidTime" id="invalidTime" style="font-size: 13px;" required
											reqMsg="失效时间不能为空">
									</div>
									<div class="edit_item"></div>
								</div>
								<!--  title   基本信息  start -->
							</form>
							<!--   商品信息  start -->
							<div class="col-12 ml10">
								<div class="row">
									<div class="col-9">
										<div class="title_text">单比例配置</div>
									</div>
									<div class="col-1 mt10"></div>
									<div class="col-1 mt10">
									</div>
									<div class="col-1 mt10">
										<button type="button" class="btn btn-find waves-effect waves-light btn_default"
											id="add-single-button">新增</button>
									</div>
								</div>
							</div>
							<div class="form-row mt20"
								style="padding: 0 10px; overflow-x: auto">
								<table id="table-single-list" class="table table-striped">
									<thead>
										<tr>
											<th>SD类型</th>
											<th>简称</th>
											<th>比例</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody id="single-tbody">
									</tbody>
								</table>
							</div>
							<div class="col-12 ml10">
								<div class="row">
									<div class="col-9">
										<div class="title_text">多比例配置</div>
									</div>
									<div class="col-1 mt10">
										<button type="button" class="btn btn-find waves-effect waves-light btn_default"
											id="import-multi-button">导入</button>
									</div>
									<div class="col-1 mt10">
										<button type="button" class="btn btn-find waves-effect waves-light btn_default"
											id="del-multi-button">删除</button>
									</div>
									<div class="col-1 mt10">
										<button type="button" class="btn btn-find waves-effect waves-light btn_default"
											id="add-multi-button">选择商品</button>
									</div>
								</div>
							</div>
							<div class="form-row mt20" style="padding: 0 10px;">
								<div id="skus"></div>
								<table id="table-multi-list" class="table table-striped">
									<thead>
										<tr>
											<th>
												<label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
			                                    <input type="checkbox" name="keeperUserGroup-checkable"
			                                           class="group-checkable" /> <span></span>
			                                	</label>
		                                	</th>
											<th>SD类型</th>
											<th>简称</th>
											<th>标准品牌</th>
											<th>标准条码</th>
											<th>商品名称</th>
											<th>比例</th>
										</tr>
									</thead>
									<tbody id="multi-tbody">
									</tbody>
								</table>
							</div>
							<!--   商品信息  end -->
							<div class="form-row m-t-50">
								<div class="col-12">
									<div class="row">
										<div class="col-4"></div>
										<div class="col-4">
											<input type="button" class="btn btn-info waves-effect waves-light btn_default" id="save-buttons" value="保 存" />
											<input type="button" class="btn btn-light waves-effect waves-light btn_default" id="cancel-buttons" value="取 消" onclick="dh_list() ;" />
											<shiro:hasPermission name="sdPurchase_examine">
												<input type="button"
													class="btn btn-info waves-effect waves-light btn_default"
													id="examine-buttons" value="审核" />
											</shiro:hasPermission>
										</div>
										<div class="col-4"></div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- end row -->
			</div>
			<!-- container -->
		</div>
	<jsp:include page="/WEB-INF/views/modals/2016.jsp" />
	<jsp:include page="/WEB-INF/views/modals/2017.jsp" />
	</div>
	<!-- content -->
	<script type="text/javascript">
	
	//公司下拉
	$module.merchantAll.loadSelectData.call($('select[name="merchantId"]')[0]);
	
	$derpTimer.init("#effectiveTime", "yyyy-MM-dd") ;
	$derpTimer.init("#invalidTime", "yyyy-MM-dd") ;

	
	$(function(){
		
		$("#merchantId").change(function(){
			var merchantId = $(this).val() ;
			
			$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0], null, merchantId);
			$module.supplier.loadSelectDataByMerchantId.call($("select[name='supplierId']")[0], null, merchantId);
		}) ;
		
		$("#add-single-button").click(function(){
			
			var html = "<tr>" ;
			
			html += "<td><select class='edit_input singleSelect' onchange='getDetails(this, \"1\")' required reqMsg='SD类型不能为空'><option value=''>请选择</option></select></td>" ;
			html += "<td></td>" ;
			html += "<td><input class='edit_input proportion' type='text'required reqMsg='比例不能为空'></td>" ;
			html += "<td><a href='#' onclick='itemDel(this);'>删除</a></td>" ;
			
			html += "</tr>" ;
			
			$("#single-tbody").append(html) ;
			getSelect("1") ;
			
		});	
		
		$("#add-multi-button").click(function(){
			$m2016.init() ;			
		});
		
		$("#import-multi-button").click(function(){
			$m2017.init() ;			
		});
		
		$("#del-multi-button").click(function(){
			$("#multi-tbody").find("input[type=checkbox]:checked").each(function(index, item){
				$(item).parent().parent().remove() ;
			});
		});
		
		$("#save-buttons").click(function(){
			var singleSize = $("#single-tbody").find("tr").length ;
			var multiSize = $("#multi-tbody").find("tr").length ;
			
			if(singleSize == 0 && multiSize == 0){
				$custom.alert.error("请选择至少配置一项比例配置");
				
				return ;
			}
			
			//必填项校验
        	if(!$checker.verificationRequired()){
        		return ;
        	}
			
			var url = serverAddr + "/sdPurchaseConfig/saveOrModify.asyn" ; ;
        	var json = $("#add-form").serializeArray();
        	
			$(json).each(function(i, m){
        		
        		if(m.name == 'effectiveTime' ){
        			m.value = m.value + " 00:00:00" ;
        		}
        		
				if(m.name == 'invalidTime' ){
					m.value = m.value + " 23:59:59" ;
        		}
        		
        	});
        	
        	var itemList = [] ;
        	
        	$("#single-tbody").find("tr").each(function(i, m){
        		var sdConfigId = $(m).find(".singleSelect").val() ;
        		var proportion = $(m).find(".proportion").val() ;
        		
        		var item = {"sdConfigId": sdConfigId, "proportion": proportion} ;
        		
        		itemList.push(item) ;
        	}) ;
        	
        	$("#multi-tbody").find("tr").each(function(i, m){
        		
        		var sdConfigId = null ;
        		
        		if($(m).find(".multiSelect").length > 0){
        			sdConfigId = $(m).find(".multiSelect").val() ;
        		}else{
        			sdConfigId = $(m).find(".sdConfigId").val()
        		}
        		
        		var proportion = $(m).find(".proportion").val() ;
        		var commbarcode = $(m).find(".commbarcode").val() ;
        		
        		var item = {"sdConfigId": sdConfigId, "proportion": proportion, "commbarcode": commbarcode} ;
        		
        		itemList.push(item) ;
        	}) ;
        	
        	itemList=JSON.stringify(itemList);
        	json.push({"name": "itemList", "value": itemList}) ;
        	
        	$custom.request.ajax(url, json, function(result){
        		if(result.state == '200'){
                    $custom.alert.success("保存成功");
                    setTimeout(function () {
                        dh_list()
                    }, 1000);
                }else{
                    if(result.expMessage != null){
                        $custom.alert.error(result.expMessage);
                    }else{
                        $custom.alert.error(result.message);
                    }
                }
            });
		}) ;
		
		$("#examine-buttons").click(function(){
			$custom.alert.warning("确认提交审核？",function(){
				
				var singleSize = $("#single-tbody").find("tr").length ;
				var multiSize = $("#multi-tbody").find("tr").length ;
				
				if(singleSize == 0 && multiSize == 0){
					$custom.alert.error("请选择至少配置一项比例配置");
					
					return ;
				}
				
				//必填项校验
	        	if(!$checker.verificationRequired()){
	        		return ;
	        	}
				
				var url = serverAddr + "/sdPurchaseConfig/saveOrModify.asyn" ; ;
	        	var json = $("#add-form").serializeArray();
	        	
	        	$(json).each(function(i, m){
	        		
	        		if(m.name == 'effectiveTime' ){
	        			m.value = m.value + " 00:00:00" ;
	        		}
	        		
					if(m.name == 'invalidTime' ){
						m.value = m.value + " 23:59:59" ;
	        		}
	        		
	        	});
	        	
	        	var itemList = [] ;
	        	
	        	$("#single-tbody").find("tr").each(function(i, m){
	        		var sdConfigId = $(m).find(".singleSelect").val() ;
	        		var proportion = $(m).find(".proportion").val() ;
	        		
	        		var item = {"sdConfigId": sdConfigId, "proportion": proportion} ;
	        		
	        		itemList.push(item) ;
	        	}) ;
	        	
	        	$("#multi-tbody").find("tr").each(function(i, m){
	        		
	        		var sdConfigId = null ;
	        		
	        		if($(m).find(".multiSelect")){
	        			sdConfigId = $(m).find(".multiSelect").val() ;
	        		}else{
	        			sdConfigId = $(m).find(".sdConfigId").val()
	        		}
	        		
	        		var proportion = $(m).find(".proportion").val() ;
	        		var commbarcode = $(m).find(".commbarcode").val() ;
	        		
	        		var item = {"sdConfigId": sdConfigId, "proportion": proportion, "commbarcode": commbarcode} ;
	        		
	        		itemList.push(item) ;
	        	}) ;
	        	
	        	itemList=JSON.stringify(itemList);
	        	json.push({"name": "itemList", "value": itemList}) ;
	        	json.push({"name": "status", "value": "1"}) ;
	        	
	        	$custom.request.ajax(url, json, function(result){
	        		if(result.state == '200'){
	                    $custom.alert.success("审核成功");
	                    setTimeout(function () {
	                        dh_list()
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
		}) ;
	}) ;
	
	function dh_list(){
		$module.load.pageOrder("act=20002");
	}
	
	/**
	* 单比例获取下拉
	*/
	function getSelect(type, val){
		var url = serverAddr + "/sdTypeConfig/getList.asyn" ;
		
		$custom.request.ajaxSync(url, {"type": type}, function(result){
            if(result.state == '200'){
	            var list = result.data ;
	            
	            var html = "<option value=''>请选择</option>" ;
	            
	            $(list).each(function(i, m){
	            	
	            	html += "<option value='"+m.id+"'>"+ m.sdTypeName +"</option>" ;
	            	
	            }) ;
	            
	            if(type == "1"){
	            	$(".singleSelect:last").append(html) ;
	            }else if(type == "2"){
	            	
	            	$(".multiSelect").each(function(i ,m){
	            		if(!$(m).val()){
	            			$(m).html(html) ;
	            		}
	            	}) ;
	            	
	            	if(val){
	            		$(".multiSelect:last").val(val) ;
	            	}
	            	
	            }
	            
            }
        });
	}
	
	/**
	*	SD配置获取明细
	*/
	function getDetails(org, type){
		var id = $(org).val() ;
		
		var url = serverAddr + "/sdTypeConfig/detail.asyn" ;
		
		$custom.request.ajax(url, {"id": id}, function(result){
            if(result.state == '200'){
	            var data = result.data ;
	            
	            $(org).parent().next().html(data.sdSimpleName) ;
            }
        });
	}
	
	/**
	* 删除比例明细
	*/
	function itemDel(org){
		$(org).parent().parent().remove() ;
	}
	</script>