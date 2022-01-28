<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<style>

	.edit_label{
		margin-left: -15px;
	}

</style>

<!-- Start content -->
<div class="content">
	<div class="container-fluid mt80">
		<!-- Start row -->
		<form id="add-form">
			<div class="col-12">
				<div class="card-box table-responsive">
					<!--  title   start  -->
					<div class="col-12">
						<div style="min-height: 450px;">
							<div class="col-12 row">
								<div>
									<ol class="breadcrumb">
										<li><i class="block"></i> 当前位置：</li>
										<li class="breadcrumb-item"><a
											href="javascript:$load.a('/settlementBill/toPage.html');">账单管理</a></li>
										<li class="breadcrumb-item"><a
											href="javascript:$load.a('/settlementBill/toAddPage.html');">结算单导入</a></li>
									</ol>
								</div>
							</div>
							<div class="edit_box mt20">
								<div class="edit_item">
									<label class="edit_label"><i class="red" id="outDepotId_i">*</i>结算对象：</label>
										<select name="customerId" class="input_msg">
										<option value="">请选择</option>
										<c:forEach items="${ relList}" var="item">
											<option value="${item.customerId }">${item.name }</option>
										</c:forEach>
									</select>
								</div>
								<div class="edit_item">
									<label class="edit_label"><i class="red" id="outDepotId_i">*</i>结算币种 ：</label>
									<select name="currency" class="input_msg">
									   <option value="">请选择</option>
									</select>
								</div>
								<div class="edit_item">
									<label class="edit_label"><i class="red" id="shopCode_i">*</i>计费周期：</label>
									<input type="text" class="input_msg form_datetime date-input"
										name="checkStartDate" id="checkStartDate">
									<span style="margin-left: 4px; margin-right: 4px;">至</span>
									<input type="text" class="input_msg form_datetime date-input"
										name="checkEndDate" id="checkEndDate">
								</div>
							</div>
							<div class="edit_box mt20">
								<div class="edit_item">
									<label class="edit_label"><i class="red"
										id="storePlatformCode_i">*</i>账单月份 ：</label> 
									<input type="text" required="" id="month" class="input_msg date-input" name="month">
								</div>
								<div class="edit_item">
									<label class="edit_label">仓库名称 ：</label> 
									<select name="depotId" class="input_msg">
									</select>
								</div>
								<div class="edit_item">
									<label class="edit_label">平台名称 ：</label> 
									<select class="input_msg" name="platformCode"></select>
								</div>
							</div>
							<div class="edit_box mt20">
								<div class="edit_item">
									<label class="edit_label"><i class="red" id="file_i">*</i>上传文件 ：</label>
									<button type="button" class="btn btn-gradient btn-file"
										id="btn-file">选择文件</button>
									<form enctype="multipart/form-data" id="form-upload">
										<input type="file" name="file" id="file" class="btn-hidden">
									</form>
								</div>
								<div class="edit_item"></div>
								<div class="edit_item"></div>
							</div>
							<!--  ================================导入信息显示部分  start ===============================================   -->
							<div class="form-row m-t-50">
								<div class="col-12">
									<div class="row">
										<div class="col-6">
											<button type="button"
												class="btn btn-info waves-effect waves-light fr btn_default"
												id="save-buttons">提交</button>
										</div>
										<div class="col-6">
											<button type="button"
												class="btn btn-light waves-effect waves-light btn_default"
												id="cancel-buttons">取 消</button>
										</div>
									</div>
								</div>
							</div>
							
							<div class="title_text">说明</div>
						    <div style="margin: 10px;">
						        <p> 1、导入结算对象为：广东卓志跨境电商供应链服务有限公司 时，仅支持对电商订单各费用类型进行分隔事业部，可支持对<font style="color:blue">标准服务费（包干费）、包材费、超件附加费、取消订单服务费、快递理赔费、税金</font>等六项费用进行按事业部分账；</p>
		                        <p> 2、导入模板可直接使用原数据平台导出模板；但需确保各sheet规范命名及表中必填字段信息，如下简要说明：</p>
								<ul style="margin: 25px;">
									<li><p>1)、<font style="color:red">“sheet 仓内费用”</font>表必填字段有<font style="color:red">“订单号、标准服务费、超件附加费、包材费”</font>四个必填字段；增加<font style="color:red">“超重附加费”</font>非必填字段；</p></li>
									<li><p>2)、<font style="color:red">“sheet 快递费”</font>表必填字段有<font style="color:red">“订单号、运费”</font>两个必填字段；</p></li>
									<li><p>3)、<font style="color:red">“sheet 取消订单服务费”</font>表必填字段有<font style="color:red">“订单号、费用”</font>两个必填字段；</p></li>
									<li><p>4)、<font style="color:red">“sheet 综合税金”</font>表必填字段有<font style="color:red">“业务单号、商品货号、仓库、收费金额、是否红冲”</font>五个必填字段；</p></li>
									<li><p>5)、<font style="color:red">“sheet 一线进口清关费”</font>表必填字段有<font style="color:red">“业务单号、一线进口清关费、入库验收费、合计”</font>四个必填字段；</p></li>
									<li><p>6)、<font style="color:red">“sheet 堆存费”</font>表必填字段有<font style="color:red">“仓库、商品编码、库存数量、计费日期、费用”</font>五个必填字段；</p></li>
									<li><p>7)、<font style="color:red">“sheet 转关费”</font>表必填字段有<font style="color:red">“业务单号、合计”</font>两个必填字段；</p></li>
									<li><p>8)、<font style="color:red">“sheet 调拨费”</font>表必填字段有<font style="color:red">“业务单号、合计”</font>两个必填字段；</p></li>
									<li><p>9)、<font style="color:red">“sheet 退运费”</font>表必填字段有<font style="color:red">“业务单号、合计”</font>两个必填字段；</p></li>
								</ul>
						    </div>
							<!--  ================================导入信息显示部分  end ===============================================   -->
						</div>
					</div>
					<!--======================Modal框===========================  -->
					<div class="popupbg" style="display: none">
						<div class="popup_box">
							<img src="/resources/assets/images/uploading.gif" alt="">
							<p>正在上传中...</p>
						</div>
					</div>
					<!-- end row -->
				</div>
			</div>
		</form>
	</div>
	<!-- container -->
</div>

<!-- content -->
<script type="text/javascript">
	//点击上传文件
	$("#btn-file").click(function(){
		var depotId = $("[name='depotId']").val();
		var customerId = $("[name='customerId']").val();
		var platformCode = $("[name='platformCode']").val(); 
		var currency = $("[name='currency']").val();
		var month = $("[name='month']").val();
		var checkStartDate = $("[name='checkStartDate']").val();
		var checkEndDate = $("[name='checkEndDate']").val();
		//必填项校验
		if(!customerId){
			$custom.alert.error("结算对象不能为空！");
			return ;
		}
		
		if(!currency){
			$custom.alert.error("币种不能为空！");
			return ;
		}
		
		if(!month){
			$custom.alert.error("账单月份不能为空！");
			return ;
		}
		
		if(!checkStartDate || !checkEndDate ){
			$custom.alert.error("计费周期不能为空！");
			return ;
		}
		
		var input = '<input type="file" name="file" id="file" class="btn-hidden file-import">';
		$("#form-upload").empty();
		$("#form-upload").append(input);			
		$("#file").click();
		
	});
	$module.constant.getConstantListByNameURL.call($('select[name="platformCode"]')[0],"storePlatformList",null);
	//加载仓库的下拉数据
	$module.depot.getSelectBeanByMerchantRel.call($("select[name='depotId']")[0],null, {});
	$module.currency.loadSelectData.call($("select[name='currency']")[0],"");

	//上传文件到后端
	$("#save-buttons").on("click",function(){
		var depotId = $("[name='depotId']").val();
		var customerId = $("[name='customerId']").val();
		var platformCode = $("[name='platformCode']").val(); 
		var currency = $("[name='currency']").val();
		var month = $("[name='month']").val();
		var checkStartDate = $("[name='checkStartDate']").val();
		var checkEndDate = $("[name='checkEndDate']").val();
	
		
		//判断是否为excel格式
		var file = $.find('#file')[0].files[0];
		
		if(!file){
			$custom.alert.error("请上传文件");
	        return ;
		}
		
		var suffix = file.name.split(".")[1];
		if(suffix=="xlsx" || suffix=="xls"){
			$(".popupbg").show();
			var formData = new FormData(); 
			formData.append('depotId', depotId) ;
			formData.append('customerId', customerId) ;
			formData.append('platformCode', platformCode) ;
			formData.append('currency', currency) ;
			formData.append('checkStartDate', checkStartDate) ;
			formData.append('checkEndDate', checkEndDate) ;
			formData.append('month', month) ;
			formData.append('file', file) ;
			$custom.request.ajaxUpload(serverAddr+"/settlementBill/importSettlementBill.asyn", formData, function(result){
				$(".popupbg").hide();
				if(result.state == 200){
					$custom.alert.success("上传成功<br>正在生成数据，请稍候");
					setTimeout('$("#cancel-buttons").click();', 1000) ;
				}else{
	                $custom.alert.error(result.expMessage);
	            }
			});
		}else{
			$custom.alert.error("请上传正确的excel格式文件");
		} 
	});

	$(function(){
		laydate.render({
			elem: '#checkStartDate', //指定元素
			type: 'date'
		});
		laydate.render({
			elem: '#checkEndDate', //指定元素
			type: 'date'
		});
		var initYear;
		laydate.render({
			elem: '#month', //指定元素
			type: 'month',
			showBottom: false,
			ready: function(date){ // 控件在打开时触发，回调返回一个参数：初始的日期时间对象
				initYear = date.year;
			},
			change: function(value, date, endDate){ // 年月日时间被切换时都会触发。回调返回三个参数，分别代表：生成的值、日期时间对象、结束的日期时间对象
				var selectYear = date.year;
				var differ = selectYear-initYear;
				if (differ == 0) {
					if($(".layui-laydate").length){
						$("#month").val(value);
						$(".layui-laydate").remove();
					}
				}
				initYear = selectYear;
			}
		});
		//返回
		$("#cancel-buttons").click(function(){
			$module.revertList.isMainList = true;
		    $module.load.pageReport("act=17003");
		});
	});
	

</script>
