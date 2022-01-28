<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
 
        <div class="row">
                     <!--  title   start  -->
            <div class="col-12">
                <div class="card-box table-responsive">
                <div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                       <li><i class="dripicons-location"></i> 当前位置：</li>
                        <li class="breadcrumb-item"><a href="#">调拨订单导入</a></li>
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
                                    		<input type="file" name="file" id="file" class="btn-hidden">
                                    	</form>
                                    </div>
                                </div>
                            </div>
                            <div class="col-4" style="margin-top:10px;">
                            <div class="checkbox">
                              <input id="checkbox1" type="checkbox">
                                    <label for="checkbox1">
                                      同一编号信息保留原有信息
                                    <label>                                  
                            </div>
                            </div>
                           <div class="col-4" style="margin-top:10px;" id="downloadTemplate">
<!--                              <a href="/excelTemplate/download.asyn?ids=111" > 模版下载</a> -->
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
                        	<table class="table table-bordered" cellspacing="0" width="100%">
		                        <thead>
		                        <tr>
		                            <th>错误行</th>
		                            <th>错误原因</th>
		                        </tr>
		                        </thead>
		                        <tbody id="errorList" style="color: red;">
		                        	
		                        </tbody>
		                    </table>
                        </div>
                   </div>
                   <div class="col-9 mt10 p-20 ml30"  id="border">
                       <div class="col-12 row mt10">
                          <h5 class="black"> 填写Excle模板小贴士:</h5>
                       </div>
                        <div class="col-12 row mt10">
                              <div class="col-1"  style="white-space:nowrap;">1.无法对同一商品编号的商品做多次导入；</div>
                        </div>
                        <div class="col-12 row mt10">
                              <div class="col-1"  style="white-space:nowrap;">2.按照格式进行填写数据;</div>
                        </div>
                   </div>
                    <!--  ================================导入信息显示部分  end ===============================================   -->
            </div>
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
	$("#file").click();
});

//上传文件到后端
$("#file").change(function(){
	//判断是否为excel格式
	var data = $(this).val().split(".");
	var suffix = data[data.length-1];
	if(suffix=="xlsx" || suffix=="xls"){
		     var formData = new FormData($("#form-upload")[0]); 
		     $custom.request.ajaxUpload(serverAddr+"/transfer/imporTransfer.asyn", formData, function(result){
		     var html = "";
		     $("#errorList").html(html);//清空错误消息
		     $("#successCount").text("0");//新增条数
		     $("#failureCount").text("0");//错误条数
			if(result.state == '200'&& result.data.code == '00'){
				$custom.alert.success("导入成功！");
				$("#successCount").text(result.data.allRows);//新增条数
			}else{
                $custom.alert.error("导入失败！");
				$("#failureCount").text(result.data.errorRows);//错误条数
				
				$.each(result.data.errorList, function(index, item){
						  html += "<tr><td>"+ (item.row+1)+"</td><td>"+item.message+"</td></tr>";
					  });
				$("#errorList").html(html);
			}
			
			
		});
	}else{
		$custom.alert.error("请上传正确的excel格式文件");
	}
	$("#file").val("");//清空
});

// 模板下载
$("#downloadTemplate").click(function(){
	$downLoad.downLoadByUrl("/excelTemplate/download.asyn?ids=111");
})
</script>