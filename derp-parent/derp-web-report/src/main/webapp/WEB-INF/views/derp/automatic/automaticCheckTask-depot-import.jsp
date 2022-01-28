<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!-- Start content -->
<jsp:include page="/WEB-INF/views/common.jsp" />
<div class="content">
	<div class="container-fluid mt80">

		<div class="row">
			<!--  title   start  -->
			<div class="col-12">
				<div class="card-box table-responsive">
					<div class="col-12 row">
						<div>
							<ol class="breadcrumb">
								<li><i class="block"></i> 当前位置：</li>
								<li class="breadcrumb-item"><a href="#">自动化校验</a></li>
								<li class="breadcrumb-item"><a href="#">自动校验任务列表</a></li>
								<li class="breadcrumb-item"><a href="#">仓库流水核对</a></li>
							</ol>
						</div>
					</div>
					<!--  title   end  -->
					<!--  ================================上传文件部分 start  ===============================================   -->
					<div class="title_text">基本信息</div>
					<div class="edit_box mt20">
						<div class="edit_item">
							<label class="edit_label"><i class="red">*</i> 数据源：</label>
							<div>
								<input type="radio" name="dataSource" value="1" > GSS报表 
								<input type="radio" name="dataSource" value="2" > 菜鸟后台
							</div>
						</div>
					</div>
					<div class="edit_box mt20">
						<div class="edit_item">
							<label class="edit_label"><i class="red">*</i> 核对仓库：</label>
							<div id="depotRadio"></div>
						</div>
					</div>
					<div class="edit_box mt20">
						<div class="edit_item">
							<label class="edit_label"><i class="red">*</i> 核对日期：</label> <input
								type="text" class="input_msg form_datetime date-input"
								style="width: inherit;" name="checkStartDate"
								id="checkStartDate"> &nbsp; - &nbsp; <input type="text"
								class="input_msg form_datetime date-input"
								style="width: inherit;" name="checkEndDate" id="checkEndDate">
						</div>
					</div>
					<div class="edit_box mt20">
						<div class="edit_item">
							<label class="edit_label"><i class="red">*</i>上传文件：</label>
							<div class="col-7 mll" style="display: inline-flex;">
								<button type="button" class="btn btn-gradient btn-file"
									id="btn-file">选择文件</button>
								<form enctype="multipart/form-data" id="form-upload">
									<input type="file" name="file" id="file" class="btn-hidden">
								</form>
								<div style="padding-left: inherit; padding-top: 1.3%;" id="downloadBSCTemplate">
									<a href="#">卓志保税仓导入模板</a>
								</div>
								<div style="padding-left: inherit; padding-top: 1.3%;" id="downloadCLTemplate">
									<a href="#">菜鸟仓导入模板</a>
								</div>
							</div>
						</div>
					</div>
					<div class="form-row m-t-50">
						<div class="col-12">
							<div class="row">
								<div class="col-4"></div>
								<div class="col-4">
									<input type="button"
										class="btn btn-info waves-effect waves-light btn_default"
										id="save-buttons" value="开始核对" /> <input type="button"
										class="btn btn-light waves-effect waves-light btn_default"
										id="cancel-buttons" value="取 消" />
								</div>
								<div class="col-4"></div>
							</div>
						</div>
					</div>
					<!--  ================================上传文件部分  end===============================================   -->
				
				    <div class="title_text">说明</div>
				    <div style="padding: inherit;">
				        <p> 1、请按导入模板格式及字段导入数据 </p>
                        <p> 2、若核对的仓库是卓志保税仓，导入的数据来自GSS库存流水报表；若核对的仓库是菜鸟仓，导入的数据来自菜鸟后台库存流水</p>
						<p> 3、若核对的是卓志保税仓： </p>
						<ul style="padding-left: inherit;">
							<li><p>1)、系统只核对业务类型为：3 4 5 6 13 14 15 16 31 32 62 63 66 67 80 81 84 85 100 104的数据。</p></li>
							<li><p>2)、当业务类型为3/4时，业务单号填入ERP采购单或调拨指令单或代销退货订单</p></li>
							<li><p>3)、当业务类型为5/6时，业务单号填入ERP外部电商订单编号或调拨指令单或购销订单或采购退货单</p></li>
							<li><p>4)、当业务类型为13/14/15/16时，业务单号填入ERP盘点单</p></li>
							<li><p>5)、当业务类型为31/3/2/62/63/66/67/80/81/84/85，业务单号填入ERP库存类型调整单</p></li>
							<li><p>6)、当业务类型为100/104，业务单号填入ERP库存调整单</p></li>
						</ul>
						<p> 4、若核对的是菜鸟仓：</p>
						<ul style="padding-left: inherit;">
							<li><p>1）、系统只核对单据类型为：交易出库  退货入库  采购入库  盘点出库  盘点入库  普通出库  的数据</p></li>
							<li><p>2）、请先将EXCEL的货品编码替换成ERP货号</p></li>
						</ul>
				    </div>
				</div>

			</div>
		</div>

		<div class="popupbg" style="display: none">
			<div class="popup_box">
				<img src="/resources/assets/images/uploading.gif" alt="">
				<p>正在上传中...</p>
			</div>
		</div>



		<!-- End row -->
		<!-- end row -->
	</div>
	<!-- container -->
</div>
<!-- content -->

<script type="text/javascript">

	//取消按钮
	$("#cancel-buttons").click(function() {
	    $module.revertList.isMainList = true;
	    $module.load.pageReport("act=14003");
	});

	$("[name='checkStartDate']").datetimepicker({
	    language:  'zh-CN',
	    format: 'yyyy-mm-dd 00:00:00',
	    minView: 2,
	});
	
	$("[name='checkEndDate']").datetimepicker({
	    language:  'zh-CN',
	    format: 'yyyy-mm-dd 23:59:59',
	    minView: 2,
	});
	
	//根据仓库类型获取仓库参数
	$('input[type=radio][name=dataSource]').change(function() {
         getDepotByDataSource(this.value) ;
    });
	
	function getDepotByDataSource(dataSource){
		
		var depotCode = "" ;
		if(dataSource == '1'){
			depotCode = "ERPWMS_360_0402,ERPHK0103" ;
		}
		
		$custom.request.ajax(serverAddr+"/automaticCheckTask/getDepotByDataSource.asyn", {"dataSource":dataSource,"depotCode":depotCode}, function(result){
            var depotList = result.data ;
            var html = "" ;
            $(depotList).each(function(index, item){
            	
            	var checked = "" ;
            	
            	html += '<input type="radio" name="outDepotId" value="'+ item.id +'">' + item.name ;
            	
            	html += '&nbsp;' ;
            	
            	if(index != 0 && index % 4 == 0){
            		html += '<br/>';
            	}
            }) ;
            
            $("#depotRadio").html(html) ;
        });
	}

	$(function(){
		
		//点击上传文件
		$("#btn-file").click(function(){
			var input = '<input type="file" name="file" id="file" class="btn-hidden file-import">';
			$("#form-upload").empty();
			$("#form-upload").append(input);
			$("#file").click();
		});
		
		//上传文件到后端
		$("#save-buttons").on("click",function(){
			
			var taskType = "2" ;
			
			var dataSource = $('input[name="dataSource"]:checked').val() ;
			if(!dataSource){
				$custom.alert.error("请选择数据源");
				return ;
			}
			
			var outDepotId = $('input[name="outDepotId"]:checked').val() ;
            if(!outDepotId){
                $custom.alert.error("请选择核对仓库");
                return ;
            }
            
            var checkStartDate = $('#checkStartDate').val() ;
            var checkEndDate = $('#checkEndDate').val() ;
            if(!checkStartDate || !checkEndDate){
                $custom.alert.error("请选择核对时间");
                return ;
            }
			
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
				formData.append('dataSource', dataSource) ;
				formData.append('outDepotId', outDepotId) ;
				formData.append('checkStartDate', checkStartDate) ;
				formData.append('checkEndDate', checkEndDate) ;
				formData.append('taskType', taskType) ;
				formData.append('file', file) ;
				$custom.request.ajaxUpload(serverAddr+"/automaticCheckTask/saveCheckResult.asyn", formData, function(result){
					$(".popupbg").hide();
					if(result.state == 200){
						$custom.alert.success("已创建任务:"+result.data+"请在列表查看进度");
						
						setTimeout('$("#cancel-buttons").click();', 1000) ;
						
					}else{
		                $custom.alert.error(result.data.message);
		            }
				});
			}else{
				$custom.alert.error("请上传正确的excel格式文件");
			} 
		});
		
		getDepotByDataSource("1") ;
	});
	
	// 模板下载
	$("#downloadBSCTemplate").click(function(){
		$downLoad.downLoadByUrl("/excelTemplate/download.asyn?ids=130");
	})
	
	$("#downloadCLTemplate").click(function(){
        $downLoad.downLoadByUrl("/excelTemplate/download.asyn?ids=131");
    })
</script>