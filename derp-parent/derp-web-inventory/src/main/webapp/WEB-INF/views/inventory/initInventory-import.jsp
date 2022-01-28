<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="/WEB-INF/views/common.jsp" />
<!-- Start content -->

<div class="content">
    <div class="container-fluid mt80">
 
        <div class="row">
                     <!--  title   start  -->
            <div class="col-12">
                <div class="card-box">
                <div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                       <li><i class="dripicons-location"></i> 当前位置：</li>
                        <li class="breadcrumb-item "><a href="#">库存期初列表</a></li>
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
                                    		<input type="file" name="file" id="file" class="btn-hidden">
                                    	</form>
                                    </div>
                                </div>
                            </div>
                           <div class="col-4" style="margin-top:10px;" id ="downTemplate">
                              <!-- <a href="/excelTemplate/download.asyn?ids=105"> 模板下载</a> -->
                              <a href="#"> 模板下载</a>
                           </div>
                        </div>
                    <!--  ================================上传文件部分  end===============================================   -->
                    <!--  ================================导入信息显示部分  start ===============================================   -->
                    <div class="col-12 mt10 p-20" id="border">
                       <div class="col-12 row mt10">
                           <h5 class="black">导入详情:</h5>
                       </div>
                        <div class="col-12 row mt10">
                              <div class="col-1"  style="white-space:nowrap;">成功数据<span>：</span></div>
                              <div class="col-2 ml15"><span id="success">0</span>条</div>
                        </div>
                        <div class="col-12 row mt10">
                              <div class="col-1"  style="white-space:nowrap;">错误数据<span>：</span></div>
                              <div class="col-2 ml15"><span class="red" id="failure">0</span>条</div>
                        </div>
                            <!--  ================================表格 ===============================================   -->
                    <div class="row col-12 mt10">
                    <table id="datatable-buttons" class="table table-striped table-responsive table-bordered" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th style="white-space:nowrap;" class="tc">序号</th>
                            <th style="white-space:nowrap; class="tc">仓库编码</th>
                            <th style="white-space:nowrap; class="tc">事业部</th>
                            <th style="white-space:nowrap;" class="tc">商品货号</th>
                            <th style="white-space:nowrap;" class="tc">库存类型</th>
                            <th style="white-space:nowrap;" class="tc">库存数量</th>
                            <th style="white-space:nowrap;" class="tc">批次号</th>
                            <th style="white-space:nowrap;" class="tc">生产日期</th>
                            <th style="white-space:nowrap;" class="tc">有效期至</th>
                            <th style="white-space:nowrap;" class="tc">理货单位</th>
                        </tr>
                        </thead>
                        <tbody id="tbodys">
                        </tbody>
                    </table>
                  
                </div>
                    <!--  ================================表格  end ===============================================   -->
                    
                    
                    <form  id="add-form" action="">
                    <input type="hidden" id="jsonObj" />
                      <div class="form-row m-t-50">
                          <div class="col-12">
                              <div class="row">
                                  <div class="col-5">
                                      <button type="button" class="btn btn-info waves-effect waves-light fr" id="save-buttons">确认并保存</button>
                                  </div>
                                  <div class="col-1"></div>
                                  <div class="col-5">
                                      <button type="button" class="btn btn-light waves-effect waves-light" id="cancel-buttons">取 消</button>
                                  </div>
                              </div>
                          </div>
                      </div>
                      </form>
                        </div>
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
			$custom.request.ajaxUpload(serverAddr+"/initInventory/importInitInventory.asyn", formData, function(result){
				if(result.state == '200'){
					
						if(result.data["failure"]!=0){
							$custom.alert.error(result.data["errorMessage"]);
						}else{
							$custom.alert.success(result.data["errorMessage"]);
						}
						var objList=  result.data["list"];
					    var trHtml="";
					    $("#tbodys").html(trHtml);
					    var num=0;
						if(objList!=null&&objList.length>0){
					    	 for(var i=0;i<objList.length;i++){
					    		  ++num;
					    		  var typeStr='';
					    		  if (objList[i].type==0) {
					    			  typeStr='好品';
								 }else if (objList[i].type==1) {
									 typeStr='坏品';
								 }
					    		 var unitStr='';
					    		 if (objList[i].unit==0) {
					    			 unitStr='托盘';
								 }else if (objList[i].unit==1) {
									 unitStr='箱';
								 }else if (objList[i].unit==2) {
									 unitStr='件';
								 }	 
						    		    
					    		 trHtml+='<tr>';
					    		 trHtml+='<td>'+num+'</td>';
					    	     trHtml+='<td>'+objList[i].depotCode+'</td>';
					    	     trHtml+='<td>'+objList[i].buName+'</td>';
					    	     trHtml+='<td>'+objList[i].goodsNo+'</td>';
					    	     trHtml+='<td>'+typeStr+'</td>';
					    	     trHtml+='<td>'+objList[i].initNum+'</td>';
					    	     trHtml+='<td>'+objList[i].batchNo+'</td>';
					    	     trHtml+='<td>'+objList[i].productionDate+'</td>';
					    	     trHtml+='<td>'+objList[i].overdueDate+'</td>';
					    	     trHtml+='<td>'+unitStr+'</td>';
					    		 trHtml+='</tr>'
					    	 }
					    	 $("#tbodys").html(trHtml);
					    	 $("#jsonObj").val(JSON.stringify(objList));
					    	// console.log("jsonObject====>"+JSON.stringify(objList));
					    }
					
				}else{
					$custom.alert.error("导入失败！");
				}
				$(".popupbg").hide();
				$("#success").text(result.data["success"]);//成功条数
				$("#failure").text(result.data["failure"]);//错误条数
			});
		}else{
			$custom.alert.error("请上传正确的excel格式文件");
		}
	});
	

//模板下载
/* function download(){
	this.location.href="/excelTemplate/download.asyn?ids=005";
} */
//保存按钮
	$("#save-buttons").click(function(){
		var url = serverAddr+"/initInventory/saveInitInventory.asyn";
		var jsonObj = $("#jsonObj").val();
		if(jsonObj==null||jsonObj==""){
			$custom.alert.error("当前没有数据可以提交！");
			return false;
		}
		$(".popupbg").show();
		$custom.request.ajax(url, {"jsonObj":jsonObj}, function(result){
			$(".popupbg").hide();
			if(result.state == '200'){
			var initSuccess=result.data["initSuccess"];//成功条数
			var initFailure=result.data["initFailure"];//错误条数
			var innventorySuccess=result.data["innventorySuccess"];//成功条数
			var innventoryFailure=result.data["innventoryFailure"];//成功条数
			var updteSuccess=result.data["updteSuccess"];//成功条数
			var updteFailure=result.data["updteFailure"];//成功条数
				if(initFailure==0&&innventoryFailure==0&&updteFailure==0){
					$custom.alert.success("添加成功！");	
				}else{
					var messgae="";
					messgae+="期初新增失败"+initFailure+"条，失败"+"库存新增失败"+innventoryFailure+"条，失败"+"库存修改失败"+updteFailure+"条，失败";
					$custom.alert.success(messgae);
				}
				setTimeout(function () {
					$module.load.pageInventory("bls=6001");
				}, 1000);
			}else{
				$custom.alert.error("添加失败！");
			}
		});

	});
	
	//取消按钮
	$("#cancel-buttons").click(function(){
		$module.load.pageInventory("bls=6001");
	});
	
	//模板下载
	$("#downTemplate").click(function(){
		$downLoad.downLoadByUrl("/excelTemplate/download.asyn?ids=105");
	})
	
</script>