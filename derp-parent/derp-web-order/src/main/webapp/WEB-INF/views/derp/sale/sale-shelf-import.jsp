<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
                       <li class="breadcrumb-item "><a href="javascript:$module.load.pageOrder('act=4001');">销售订单列表</a></li>
                       <li class="breadcrumb-item "><a href="#">批量导入</a></li>
                    </ol>
                    </div>
            </div>
                    <!--  title   end  -->
                    <!--  ================================上传文件部分 start  ===============================================   -->
                        <div class="form-row  col-12">
                            <div class="col-4">
                                <div class="row col-12">
                                    <label  class="col-4 col-form-label"  style="white-space:nowrap;">上传文件<span>：</span></label>
                                    <div class="col-7 mll">
                                      <button type="button" class="btn btn-gradient btn-file" id="btn-file">选择文件</button>
                                    	<form enctype="multipart/form-data" id="form-upload">
                                    		<input type="file" name="file" id="file" class="btn-hidden file-import">
                                    	</form>
                                    </div>
                                </div>
                            </div>
                            <div class="col-4" style="margin-top:10px;">
                            </div>
                           <div class="col-4" style="margin-top:10px;" id="downloadTemplate">
<!--                               <a href="javascript:void(0);" onclick="downloadTemp();"> 模板下载</a> -->
									<a href="#">模板下载</a>
                           </div>
                        </div>
                    <!--  ================================上传文件部分  end===============================================   -->
                    <!--  ================================导入信息显示部分  start ===============================================   -->
                    <div class="col-9 mt10 p-20 ml30"  id="border">
                       <div class="col-12 row mt10">
                           <h5 class="black">导入详情:</h5>
                       </div>
                        <div class="col-12 row mt10">
                              <div class="col-1"  style="white-space:nowrap;">新增数据<span>：</span></div>
                              <div class="col-2 ml15"><span id="successCount">0</span>条</div>
                        </div>
                        <div class="col-12 row mt10">
                              <div class="col-1"  style="white-space:nowrap;">错误数据<span>：</span></div>
                              <div class="col-2 ml15"><span id= "failureCount" class="red">0</span>条</div>
                        </div>
                        <div class="col-12 row mt10">
	                        <table id="table-list" class="table table-striped">
			                   <thead>
				                   <tr>
					                   <td>错误行</td>
					                   <td>错误原因</td>
				                   </tr>
			                   </thead>
			                   <tbody></tbody>
			                 </table> 
		                 </div>   
                   </div>
                   <div class="col-9 mt10 p-20 ml30"  id="border">
                       <div class="col-12 row mt10">
                          <h5 class="black"> 填写Excle模板小贴士:</h5>
                       </div>
                        <div class="col-12 row mt10">
                              <div class="col-1"  style="white-space:nowrap;">1.按照格式进行填写数据;</div>
                        </div>
                   </div>
                    <!--  ================================导入信息显示部分  end ===============================================   -->
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
    </div> <!-- container -->
</div> <!-- content -->
<footer class="footer text-right">
</footer>



<script type="text/javascript">
//点击上传文件
$("#btn-file").click(function(){
	var input = '<input type="file" name="file" id="file" class="btn-hidden file-import">';
	$("#form-upload").empty();
	$("#form-upload").append(input);
	$("#file").click();
});
//上传文件到后端
$("#form-upload").on("change",'.file-import',function(){
	//判断是否为excel格式
	var data = $(this).val().split(".");
	var suffix = data[data.length-1];
	if(suffix=="xlsx" || suffix=="xls"){
		$(".popupbg").show();
		var formData = new FormData($("#form-upload")[0]); 
		$custom.request.ajaxUpload(serverAddr+"/sale/importSaleShelf.asyn?r=1", formData, function(result){
			$(".popupbg").hide();
			if(result.state == '200'){
				$custom.alert.success("导入完成。");
			}else{
				$custom.alert.error("导入失败！");
			}
			$("#successCount").text(result.data["success"]);//新增条数
			$("#failureCount").text(result.data["failure"]);//错误条数
			//错误信息
			var msgList = result.data.msgList;
			var str="";
			for(var i = 0; i<msgList.length;i++){
				var map = msgList[i];
				str+="<tr>"
		           +"   <td>"+map.row+"</td>"
		           +"   <td>"+map.msg+"</td>"
		           +"</tr>";
			}
			$('#table-list tbody').html(str);
		});
	}else{
		$custom.alert.error("请上传正确的excel格式文件");
	}
});
	// 模板下载
	$("#downloadTemplate").click(function(){
		$downLoad.downLoadByUrl("/excelTemplate/download.asyn?ids=142");
	})
</script>