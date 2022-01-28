<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="/WEB-INF/views/common.jsp" />

<!-- Start content -->
<div class="content">
	<div class="container-fluid mt80">
		<!-- Start row -->
		<div class="row">
			<div class="col-12">
				<div class="card-box table-responsive">
					<div class="col-12 row">
						<div>
							<ol class="breadcrumb">
								<li><i class="block"></i> 当前位置：</li>
								<li class="breadcrumb-item"><a href="javascript:dh_list();">业务单列表</a></li>
								<li class="breadcrumb-item"><a
									href="javascript:dh_details('${code }');">附件管理</a></li>
							</ol>
						</div>
					</div>
					<div class="title_text">附件上传</div>
					<div class="info_box">
						<div class="info_item">
							<div class="form-row  col-12">
								<div class="col-12">
									<div class="row col-12">
										<label class="col-form-label" style="white-space: nowrap;">附件上传<span>：</span></label>
										<div class="col-8 mll">
											<button type="button" class="btn btn-gradient btn-file"
												id="btn-file">选择文件</button>
											<button type="button" class="btn btn-gradient btn-file"
												id="btn-allUploadFile">全部上传</button>
											<span style="color:red">附件大小限制：20MB</span>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div id="attachmentContent" class="pre-scrollable info_box"
						style="overflow-y: auto; height: 180px; border: 1px solid #f0f0f0; padding: inherit;">
					</div>
				</div>
				<!-- end row -->
			</div>
			<!-- container -->
		</div>
		<!--  ================================表格 ===============================================   -->
		<div class="row mt20 attachDetail">
			<div class='col-12'>
				<div class="card-box">
					<div class="title_text">附件列表</div>
					<div id="attachmentTable" multiple></div>
				</div>
			</div>
		</div>
		<div class="row mt20">
			<div class='col-12'>
				<div class="card-box">
					<div class="form-row">
						<div class="col-12">
							<div class="row">
								<div class="col-5">
									<button type="button"
										class="btn btn-find waves-effect waves-light btn_default"
										id="cancel-buttons">返回</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!--  ================================表格  end ===============================================   -->
</div>

<input id="code" type="hidden" value="${code}">

<!-- content -->
<script type="text/javascript">
	var code =  $("#code").val()   ;
	$("#attachmentTable").createAttachTables(code);
	
	//初始化plupload
	var uploader = new plupload.Uploader({ //实例化一个plupload上传对象
		browse_button : 'btn-file',
		url : serverAddr + '/attachment/uploadFiles.asyn?token='+token + "&code=" + code,
		max_file_size: '20MB',//限制为25MB
		multi_selection:true,
		filters: {mime_types :[{ title : "图片文件", extensions : "jpg,jpeg,png,bmp,JPG,JPEG,PNG,BMP" }, 
		    { title : "RAR压缩文件", extensions : "zip,rar,ZIP,RAR" },
		    { title : "文档文件", extensions : "docx,xlsx,doc,xls,txt,pdf,eml,DOCX,XLSX,DOC,XLS,TXT,PDF,EML" }]},//文件限制
		init : {
			PostInit: function (a) {
            },
            FilesAdded: function (uder, files) {
                var html = '' ;
                //单个文件进度显示
                $(files).each(function(index , file){
                	html += '<div class="info_item" id="item'+ file.id +'">' +
								'<div class="form-row  col-12">'+
									'<div class="col-12">'+
										'<div class="row col-12">' + 
											'<label style="white-space: nowrap;">附件<span>：</span></label>'+
											'<div class="col-9 mll">' + file.name +
												'<span style="float:right">' +
												'<a href="javascript:removeFile(\''+ file.id +'\')" id="a'+file.id+'" style="padding-left:8px;">取消</a>' + 
												'</span>' +
											'</div>' + 
										'</div>' +
									'</div>' +
								'</div>'+
							'</div>'+
							'<div class="progress" style="width:80%">'+
								  '<div id="bar'+file.id+'" class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="min-width: 2em;">' + 
								    '<span id="span'+file.id+'">0%</span>'+
								  '</div>' +
							'</div>' ;
                }) ; 
                
               $("#attachmentContent").append(html) ;
            },
            UploadProgress: function (uder, file) {
            	//显示进度条
                progress(file.id, file.percent);
            },
            UploadFile: function (uder , file) {
                changeStatus(file.id) ;
            },
            FileUploaded: function (uder, file, resObject) {
            	
                var result = resObject.response;
                result = JSON.parse(result) ;
                if( result.state == 200 ){
                	if(result.data && result.data.code == 200){
                		removeFile(file.id) ; 
                    	$attachTable.fn.reload();
                    	
                    	return ;
                	}else{
                		$custom.alert.error(file.name + "上传失败");
                	}
                }else{
                	$custom.alert.error(result.message);
                }
                
           		uploadErrorDisplay(file.id) ;
                
            },
            UploadComplete: function (uder, files) {
            	$attachTable.fn.reload();
            },
            Error: function (uder, res) {
            	
            	if(res.code == -600){
            		$custom.alert.error(res.file.name + "超过20M");
            		return ;
            	}
            	
            	uploadErrorDisplay(res.file.id) ;
            	$custom.alert.error(res.file.name + "上传失败");
            }
		}
		    
	});
	uploader.init(); //初始化
	
	function dh_list(){
		
		var act = "";
		
		if(code.indexOf("CGRK") > -1){
			act = "act=3004";
		}else if(code.indexOf("DBRK") > -1){
			act = "act=8004" ;
		}else if(code.indexOf("XSTR") > -1){
			act = "act=5002" ;
		}else if(code.indexOf("XSO") > -1){
			act = "act=4001" ;
		}else if(code.indexOf("DBO") > -1){
            act = "act=8001" ;
		}else if(code.indexOf("CGO") > -1){
            act = "act=3001" ;
		}else if(code.indexOf("SX") > -1 || code.indexOf("GY")){
            act = "act=14012" ;
		}
		
	 	$module.load.pageOrder(act);
	}
	
	function dh_details(id){
	 $module.load.pageOrder("act=200001&codeid="+id);
	}
	
	//返回
	$("#cancel-buttons").click(function () {
		var act = "";
		
		if(code.indexOf("CGRK") > -1){
			act = "act=3004";
		}else if(code.indexOf("DBRK") > -1){
			act = "act=8004" ;
		}else if(code.indexOf("XSTR") > -1){
			act = "act=5002" ;
		}else if(code.indexOf("XSO") > -1){
			act = "act=4001" ;
		}else if(code.indexOf("DBO") > -1){
            act = "act=8001" ;
		}else if(code.indexOf("CGO") > -1){
            act = "act=3001" ;
		}else if(code.indexOf("SX") > -1 || code.indexOf("GY")){
            act = "act=14012" ;
		}
		
	 	$module.load.pageOrder(act);
	});
	//上传
	$("#btn-allUploadFile").click(function(){
		//如第一次上传成功后，存在失败文件，修改对应的状态，才能重新上传
		var files = uploader.files ;
		for(var index = 0 ; index < files.length ; index ++){
			if(files[index].status == 1){
				continue ;
			}
			
			files[index].percent = 0 ;
			files[index].loaded = 0 ;
			files[index].status = 1 ;
		}
		
		uploader.start() ; 
	});
	
	//点击取消触发事件
	function removeFile(fileId){
		$("#item" + fileId).next().remove() ;
		$("#item" + fileId).remove() ; 
		
		uploader.removeFile(fileId);
	}
	
	//上传中进度条显示
	function progress(id, percent){
		$("#bar" + id).removeClass("progress-bar-danger") ;
		$("#bar" + id).addClass("progress-bar-success") ;
		$("#bar" + id).attr("aria-valuenow" , percent) ; 
		$("#bar" + id).css("width" , percent + "%" ) ;
		$("#span" + id).text(percent + "%") ; 
	}
	
	//修改显示状态
	function changeStatus(id){
		$("#error" + id).remove();
		$("#a" + id).attr("href","#") ;
		$("#a" + id).text("上传中...") ;
	}
	
	function uploadErrorDisplay(id){
		$("#bar" + id).removeClass("progress-bar-success") ;
		$("#bar" + id).addClass("progress-bar-danger") ;
		$("#a" + id).attr("href",'javascript:removeFile(\''+ id +'\')') ;
		$("#a" + id).html("取消") ;
		$("#a" + id).before('<a href="#" id="error'+id+'" style="padding-left:8px;color:red;">上传失败</a>') ;
	}
	
</script>
