<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
          <form id="add-form">
           <div class="col-12">
              <div class="card-box" >
                                         <!--  title   start  -->
              <div class="col-12">
                <div>
                   <div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                       <li><i class="block"></i> 当前位置：</li>
                           <li class="breadcrumb-item"><a href="javascript:$load.a('/automaticCheckTask/toPage.html');">自动检验任务列表</a></li>
                           <li class="breadcrumb-item"><a href="#">新增核对任务</a></li>
                    </ol>
                    </div>
            </div>
                 <div class="edit_box mt20">
                        <div class="edit_item">
                        		<span class="msg_title"><i class="red" id="file_i">*</i>文件 :</span>
                         		<button type="button" class="btn btn-gradient btn-file"
									id="btn-file">选择文件</button>
								<form enctype="multipart/form-data" id="form-upload">
									<input type="file" name="file" id="file" class="btn-hidden">
								</form>
                        </div>
                        <div class="edit_item">
                        	<div style="padding-left: inherit; padding-top: 1.3%;" id="downloadTemplate">
								<a href="#">导入模板</a>
							</div>
                        </div>    
	                    <div class="edit_item"></div>
                    </div>
                                        <!--  ================================导入信息显示部分  start ===============================================   -->
                        <div class="form-row m-t-50">
                        <div class="col-12">
                            <div class="row">
                                <div class="col-6">
                                   <button type="button" class="btn btn-info waves-effect waves-light fr btn_default" id="save-buttons">开始核对</button>
                                </div>
                                <div class="col-6">
                                    <button type="button" class="btn btn-light waves-effect waves-light btn_default" id="cancel-buttons">取 消</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-9 mt10 p-20 ml30"  id="border">
                       <div class="col-12 row mt10">
                          <h5 class="black"> 填写Excle模板小贴士:</h5>
                       </div>
                        <div class="col-12 row mt10">
                            <div style="width:730px;word-break: break-all; word-wrap: break-word;">
								<div class="col-1"  style="white-space:nowrap;">1.按照格式进行填写数据;</div>
							</div>
                        </div>
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
		var input = '<input type="file" name="file" id="file" class="btn-hidden file-import">';
		$("#form-upload").empty();
		$("#form-upload").append(input);			
		$("#file").click();
	});

	//上传文件到后端
	$("#save-buttons").on("click",function(){
		
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
			formData.append('taskType', "3") ;
			formData.append('file', file) ;
			$custom.request.ajaxUpload(serverAddr+"/automaticCheckTask/saveCheckResult.asyn", formData, function(result){
				$(".popupbg").hide();
				if(result.state == 200){
					$custom.alert.success("已创建任务:"+result.data+",请在列表查看进度");
					setTimeout('$("#cancel-buttons").click();', 1000) ;
				}else{
	                $custom.alert.error(result.data.message);
	            }
			});
		}else{
			$custom.alert.error("请上传正确的excel格式文件");
		} 
	});

	$(function(){
		//返回
		$("#cancel-buttons").click(function(){
			$module.revertList.isMainList = true;
		    $module.load.pageReport("act=14003");
		});
	});
	
	$("#downloadTemplate").click(function(){
        $downLoad.downLoadByUrl("/excelTemplate/download.asyn?ids=144");
    })
	
	
</script>
