<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<style>
    label[class^=col-],div[class^=col-]{padding: 2px 0;}

	.edit_span{
		text-align: right;
		line-height: 30px;
	}

	.box_input{
		width: 38px;
	    height: 30px;
	    border: 1px solid #dadada;
	    text-indent: 5px;
	    margin: 0 10px 0 10px;
	}

</style>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt60">
        <!-- Start row -->
          <form id="add-form">
              <div class="row">
              <div class="card-box margin w100">
              	<div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                       <li><i class="block"></i> 当前位置：</li>
                        <li class="breadcrumb-item"><a href="#">商品档案</a></li>
                        <li class="breadcrumb-item "><a href="javascript:$load.a('/merchandise/toPage.html');">商品列表</a></li>
                        <li class="breadcrumb-item "><a href="javascript:$load.a('/merchandise/toEditPage.html?id=${detail.id }');">商品编辑</a></li>
                    </ol>
                    </div>
            </div>
                  <div class="title_text">基础信息</div>
                  <div class="margin">
                      <div class="edit_box">
                          <div class="edit_item">
                              <label class="edit_label"><i class="red">*</i> 商品货号：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" id="goodsNo" name="goodsNo"  readonly="readonly" value="${detail.goodsNo }">
                              <input type="hidden" value="${detail.id }" name="id" id="id">
                              <input type="hidden" value="${customsAreaIdStr}" id="customsAreaIdStr" name="customsAreaIdStr">
                              <input type="hidden"  id="urlType" name="urlType">
                             
                          </div>
                          <div class="edit_item">
                              <label class="edit_label"><i class="red">*</i> 商品条形码：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" id="barcode" name="barcode" value="${detail.barcode }" readonly="readonly">
                          </div>
                          <div class="edit_item">
                              <label class="edit_label"><i class="red">*</i> 商品名称：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" id="name"name="name" value="${detail.name }">
                          </div>                         
                          
                      </div>
                      <div class="edit_box">
                          <div class="edit_item">
                              <label class="edit_label">平台备案关区：</label>
                              <select class="edit_input" name="customsAreaId" multiple="multiple"  id="customsAreaId">
                                <option value="">请选择</option>
                           	  </select>
                           </div>
                          <div class="edit_item">
                              <label class="edit_label">HS编码：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" name="hsCode" value="${detail.hsCode}">
                          </div>
                          <div class="edit_item">
                              <label class="edit_label">商品英文名称：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" name="englishGoodsName" value="${detail.englishGoodsName }">
                          </div>                       
                          
                      </div>
                      
                      <div class="edit_box">
                          <div class="edit_item">
                              <label class="edit_label">企业商品编码：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" name="factoryNo" value="${detail.factoryNo }">                              
                           </div>
                          
                           <div class="edit_item">
                             <label class="edit_label red">一级类目：</label>
                                <select name="productTypeId0" id = "productTypeId0"class="edit_input ">
                                       <option value="">请选择</option>
                                      <c:forEach items="${oneCatBean }" var="productType">
                               	    	<option value="${productType.value }" <c:if test='${detail.productTypeId0 eq productType.value}'>selected</c:if>> 
                               	    		${productType.label }
                               	    	</option>
                               	    </c:forEach>
                                  </select>
                          </div>
                          
                          <div class="edit_item">
                                <label class="edit_label red">二级类目：</label>
                                <select name="productTypeId" id = "productTypeId"class="edit_input ">
                                       <option value="">请选择</option>
                                      <c:forEach items="${twoCatBean }" var="productType">
                               	    	<option value="${productType.value }" 
                               	    		<c:if test="${detail.productTypeId == productType.value}"> selected = 'selected'</c:if>>
                               	    		${productType.label }
                               	    	</option>
                               	    </c:forEach>
                                  </select>
                         </div>                     
                          
                      </div>
                      
                      <div class="title_text">备案信息</div>
                      <div class="edit_box">
                          <div class="edit_item">
                              <label class="edit_label">商品净重：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" name="netWeight" value="${detail.netWeight }"> kg
                          </div>
                          <div class="edit_item">
                              <label class="edit_label">商品毛重：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" name="grossWeight" value="${detail.grossWeight }"> kg
                          </div>
                          <div class="edit_item">
                              <label class="edit_label">规格型号：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" name="spec" value="${detail.spec }">
                          </div>
                      </div>
                      <div class="edit_box">
                          <div class="edit_item">
                              <label class="edit_label">保质天数：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" name="shelfLifeDays" value="${detail.shelfLifeDays }"> 天
                          </div>
                          <div class="edit_item">
                              <label class="edit_label">备案单价：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" name="filingPrice" value="${detail.filingPrice }" oninput = "value=value.replace(/[^\.\d]/g,'')">元
                          </div>
                           <div class="edit_item">
                              <label class="edit_label"><i class="red">*</i> 计量单位：</label>
                              <div class="edit_input" style="text-indent: 0">
                                  <select name="unit" id="unit" class="form-control goods_select2">
                                      <option value="">请选择</option>
                                      <c:forEach items="${unitBean }" var="unit">
                                          <c:choose>
                                              <c:when test="${detail.unit eq unit.value }">
                                                  <option value="${unit.value }" selected>${unit.label }</option>
                                              </c:when>
                                              <c:otherwise>
                                                  <option value="${unit.value }">${unit.label }</option>
                                              </c:otherwise>
                                          </c:choose>
                                      </c:forEach>
                                  </select>
                              </div>
                          </div>
                      </div>
                      
                      <div class="edit_box">
                         <div class="edit_item">
                              <label class="edit_label"><i class="red">*</i> 原产国：</label>
                              <div class="edit_input" style="text-indent: 0">
                                  <select name="countyId" id="countyId" class="form-control goods_select2">
                                      <option value="">请选择</option>
                                      <c:forEach items="${countryBean }" var="country">
                                          <c:choose>
                                              <c:when test="${detail.countyId eq country.value }">
                                                  <option value="${country.value }" selected>${country.label }</option>
                                              </c:when>
                                              <c:otherwise>
                                                  <option value="${country.value }">${country.label }</option>
                                              </c:otherwise>
                                          </c:choose>
                                      </c:forEach>
                                  </select>
                              </div>
                          </div>
                          <div class="edit_item">
                              <label class="edit_label">原产地区：</label>
                              <input type="text"  class="edit_input" name="countyArea" value="${detail.countyArea }">
                          </div>
                           <div class="edit_item">
                              <label class="edit_label">申报要素：</label>
                              <input type="text"  class="edit_input" name="declareFactor" value="${detail.declareFactor }">
                          </div>
                      </div>
                      
                      <div class="edit_box">
                          <div class="edit_item">
                              <label class="edit_label">商品成分：</label>
                              <input type="text"  class="edit_input" name="component" value="${detail.component }">
                          </div>
                          <div class="edit_item">
                              <label class="edit_label">生产厂家：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" name="manufacturer" value="${detail.manufacturer }">
                          </div>
                          <div class="edit_item">
                              <label class="edit_label">厂家地址：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" name="manufacturerAddr" value="${detail.manufacturerAddr }">
                          </div>
                           
                      </div>
                      <div class="edit_box">
                          <div class="edit_item">
                              <label class="edit_label">第一法定单位：</label>
                              <input type="text"  class="edit_input" name="unitNameOne" value="${detail.unitNameOne }">
                          </div>
                          <div class="edit_item">
                              <label class="edit_label">第二法定单位：</label>
                              <input type="text"  class="edit_input" name="unitNameTwo" value="${detail.unitNameTwo }">
                          </div>
                           <div class="edit_item">
                              <label class="edit_label">海关商品备案号：</label>
                              <input type="text"  class="edit_input" name="customsGoodsRecordNo" value="${detail.customsGoodsRecordNo }">
                          </div>
                      </div>
                      
                      <div class="title_text">箱规设置</div>
						<div class="form-row mt20 ml15">
						    <div class="edit_item">
						        <label class="edit_label" style="width: 15%">箱转换：</label>
						        <span class="edit_span">1箱 转换为</span>
						        <input type="text" class="box_input" name="boxToUnit" oninput = "value=value.replace(/[^\d]/g,'')"  value="${detail.boxToUnit }">
						        <span class="edit_span">件</span>
						    </div>
						    <div class="edit_item">
						        <label class="edit_label" style="width: 15%">托转换：</label>
						        <span class="edit_span">1托 转换为</span>
						        <input type="text" class="box_input" name="torrToUnit" oninput = "value=value.replace(/[^\d]/g,'')"  value="${detail.torrToUnit }">
						        <span class="edit_span">件</span>
						    </div>
						</div>
                      
                       <div class="title_text">规格描述</div>
                      <div class="edit_box">
                          <div class="edit_item">
                              <label class="edit_label">长：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" name="length" value="${detail.length }"> cm
                          </div>
                          <div class="edit_item">
                              <label class="edit_label">宽：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" name="width" value="${detail.width }"> cm
                          </div>
                          <div class="edit_item">
                              <label class="edit_label">高：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" name="height" value="${detail.height }"> cm
                          </div>
                      </div>
                      
                      
                     <div class="edit_box">
                          <div class="edit_item">
                              <label class="edit_label">体积：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" name="volume" value="${detail.volume }"> cm³
                          </div>
                          <div class="edit_item">
                              <label class="edit_label">包装方式：</label>
                              <div class="edit_input" style="text-indent: 0">
                                  <select name="packType" class="form-control goods_select2">
                                      <option value="">请选择</option>
                                      <c:forEach items="${packBean }" var="pack">
                                          <c:choose>
                                              <c:when test="${detail.packType eq pack.value }">
                                                  <option value="${pack.value }" selected>${pack.label }</option>
                                              </c:when>
                                              <c:otherwise>
                                                  <option value="${pack.value }">${pack.label }</option>
                                              </c:otherwise>
                                          </c:choose>
                                      </c:forEach>
                                  </select>
                              </div>
                          </div>
                          <div class="edit_item">
                             
                          </div>
                      </div>     
                      
                      <div class="edit_box">                        
                          <div class="edit_item" style="flex: 2">
                              <label class="edit_label" style="flex: 0.4">描述：</label>
                              <textarea class="edit_input" rows="5" name="describe" >${detail.describe }</textarea>
                          </div>
                          <div class="edit_item">
                          </div>
                          <div class="edit_item">
                          </div>
                      </div> 
                      
                      <!-- <div class="edit_box">
                            
                      
                             <div class="edit_item">
                                  <label class="edit_label">商品图片：</label>
                                  <div class="edit_item">
                                      <div class="fileupload fileupload-new" data-provides="fileupload">
                                          <div class="fileupload-new thumbnail" style="width: 200px; height: 150px;">
                                              <img src="/resources/assets/images/11.jpg" alt="image" />
                                          </div>
                                          <div class="fileupload-preview fileupload-exists thumbnail" style="max-width: 200px; max-height: 150px; line-height: 20px;"></div>                                         
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
                                  </div>
                              </div>
                           
                           
                      </div> -->
                      
                      
                      <table class="table table-striped" cellspacing="0" width="100%">
                                              
                                             <div class="title_text">商品信息</div>
						                     <div class="row col-12 mt20">
						                         <table class="table table-bordered" cellspacing="0" width="100%">
						                             <thead>
						                             <tr>
						                                 <th>图片1</th>
						                                 <th>图片2</th>
						                                 <th>图片3</th>
						                                 <th>图片4</th>
						                                 <th>图片5</th>
						                             </tr>
						                             </thead>
						                             <tbody>
						                             
						                             <tr>
						                                <td style="height:12px; padding-top: 0px; width:300px">
						                         		<img style="border:0;margin-top:0px;" src="${detail.imageUrl1 }" alt="image" id="file1img" width="100" height="100" onclick="showimage('${detail.imageUrl1 }')"/>
						                         		<br>
						                         		<button type="button" class="btn btn-gradient btn-file" id="btn-file1" onclick='uploadData("1")'>上传图片</button>
							                          	<div  id="form-upload1">
							                          		<input type="file" name="file" id="file1" class="btn-hidden">
							                          	</div>						                         							                         					                        
						                     			</td>
						                                <td style="height:12px; padding-top: 0px; width:300px">
						                         		<img style="border:0;margin-top:0px;" src="${detail.imageUrl2 }" alt="image" id="file2img" width="100" height="100" onclick="showimage('${detail.imageUrl2 }')"/>
						                         		<br>
						                         		<button type="button" class="btn btn-gradient btn-file" id="btn-file2" onclick='uploadData("2")'>上传图片</button>
							                          	<!-- <div  id="form-upload1">
							                          		<input type="file" name="file" id="file1" class="btn-hidden">
							                          	</div> -->						                         							                         					                        
						                     			</td>
						                                <td style="height:12px; padding-top: 0px; width:300px">
						                         		<img style="border:0;margin-top:0px;" src="${detail.imageUrl3 }" alt="image" id="file3img" width="100" height="100" onclick="showimage('${detail.imageUrl3 }')"/>
						                         		<br>
						                         		<button type="button" class="btn btn-gradient btn-file" id="btn-file3" onclick='uploadData("3")'>上传图片</button>
							                          	<!-- <div  id="form-upload1">
							                          		<input type="file" name="file" id="file1" class="btn-hidden">
							                          	</div>	 -->					                         							                         					                        
						                     			</td>
						                                <td style="height:12px; padding-top: 0px; width:300px">
						                         		<img style="border:0;margin-top:0px;" src="${detail.imageUrl4 }" alt="image" id="file4img" width="100" height="100" onclick="showimage('${detail.imageUrl4 }')"/>
						                         		<br>
						                         		<button type="button" class="btn btn-gradient btn-file" id="btn-file4" onclick='uploadData("4")'>上传图片</button>
							                          	<!-- <div  id="form-upload1">
							                          		<input type="file" name="file" id="file1" class="btn-hidden">
							                          	</div>	 -->					                         							                         					                        
						                     			</td>
						                     			<td style="height:12px; padding-top: 0px; width:300px">
						                         		<img style="border:0;margin-top:0px;" src="${detail.imageUrl5 }" alt="image" id="file5img" width="100" height="100" onclick="showimage('${detail.imageUrl5 }')"/>
						                         		<br>
						                         		<button type="button" class="btn btn-gradient btn-file" id="btn-file5" onclick='uploadData("5")'>上传图片</button>
							                          	<!-- <div  id="form-upload1">
							                          		<input type="file" name="file" id="file1" class="btn-hidden">
							                          	</div>	 -->					                         							                         					                        
						                     			</td>
						                             </tr>
			
						                            
						                             </tbody>
						                         </table>
						                     </div>
                                               
                                               
        
                                            </table>  
                      
                      
                      
                      
                      <div class="form-row m-t-50">
                          <div class="col-12">
                              <div class="row">
                                  <div class="col-6">
                                      <button type="button" class="btn btn-info waves-effect waves-light fr btn_default" id="save-buttons">保 存</button>
                                  </div>
                                  <div class="col-6">
                                      <button type="button" class="btn btn-light waves-effect waves-light btn_default" id="cancel-buttons">取 消</button>
                                  </div>
                              </div>
                          </div>
                      </div>
                  </div>
              </div>
              </div>
                  <!-- end row -->
          </form>
          <jsp:include page="/WEB-INF/views/modals/7012.jsp" />
        </div>
        <!-- container -->
    </div>
    <div id="ShowImage_Form" class="modal hide">         
         <div class="modal-header">
             <button data-dismiss="modal" class="close" type="button"></button>
         </div>
           <div class="modal-body">
            <div id="img_show" >
            </div>
        </div>
    </div>
    
</div> <!-- content -->
<script src='/resources/js/system/base.js' type="text/javascript"></script>
<script type="text/javascript">
	//上传的是哪个主图
	var type = "";
	$(function(){
		
		//------------------------商品图片上传文件-------------------------
		  //点击上传文件
		/* $("#btn-file1").click(function(){
			debugger;
			var input = '<input type="file" name="file" id="file1" class="btn-hidden file-import">';
			$("#form-upload1").empty();
			$("#form-upload1").append(input);
			$("#file1").click();
		}); */

		//上传文件到后端
		$("#form-upload1").on("change",'.file-import',function(){
			//判断是否为图片格式
			var data = $(this).val().split(".");
			var suffix = data[data.length-1].toLowerCase();
			if(suffix=="bmp" || suffix=="jpg" || suffix=="jpeg" || suffix=="png" || suffix=="tif" || suffix=="gif" 
					|| suffix=="pcx" || suffix=="tga" || suffix=="exif" || suffix=="fpx" || suffix=="svg" 
					|| suffix=="psd"|| suffix=="cdr"|| suffix=="pcd"|| suffix=="dxf" || suffix=="ufo" || suffix=="eps"
					|| suffix=="ai" || suffix=="raw" || suffix=="wmf" || suffix=="webp"){
				
				var formData = new FormData(); 
				var id = $('#id').val();
		        var type = $('#urlType').val();;
		        var file=this.files[0];
		        formData.append("id",id);
		        formData.append("type",type);
		        formData.append("file",file);
				$custom.request.ajaxUpload("/merchandise/uploadFile.asyn", formData, function(result){
					if(result.state == '200'){
						if (type=='1') {
							if($('#file1img').length > 0){
								$("#file1img").remove();
							}
							$('#btn-file1').before('<img style="border:0;margin-top:0px;" src="'+result.data+'" id="file1img" width="100" height="100" onclick="showimage(\''+result.data+'\')"/>  <br>');
							$('#btn-file1').html("上传图片");
						}
						if (type=='2') {
							if($('#file2img').length > 0){
								$("#file2img").remove();
							}
							$('#btn-file2').before('<img style="border:0;margin-top:0px;" src="'+result.data+'" id="file2img" width="100" height="100" onclick="showimage(\''+result.data+'\')"/> <br>');
							$('#btn-file2').html("上传图片");
						}
						if (type=='3') {
							if($('#file3img').length > 0){
								$("#file3img").remove();
							}
							$('#btn-file3').before('<img style="border:0;margin-top:0px;" src="'+result.data+'" id="file3img" width="100" height="100" onclick="showimage(\''+result.data+'\')"/> <br>');
							$('#btn-file3').html("上传图片");
						}
						if (type=='4') {
							if($('#file4img').length > 0){
								$("#file4img").remove();
							}
							$('#btn-file4').before('<img style="border:0;margin-top:0px;" src="'+result.data+'" id="file4img" width="100" height="100" onclick="showimage(\''+result.data+'\')"/> <br>');
							$('#btn-file4').html("上传图片");
						}
						if (type=='5') {
							if($('#file5img').length > 0){
								$("#file5img").remove();
							}
							$('#btn-file5').before('<img style="border:0;margin-top:0px;" src="'+result.data+'" id="file5img" width="100" height="100" onclick="showimage(\''+result.data+'\')"/> <br>');
							$('#btn-file5').html("上传图片");
						}

						$custom.alert.success("上传完成！");
					}else{
						$custom.alert.error("上传失败！");
					}
				});
			}else{
				$custom.alert.error("请上传正确格式的图片");
			}
		});
		

  

  //------------------------商品图片上传文件end-------------------------
		
		
		
		var customsAreaIdStr=$("#customsAreaIdStr").val();
		$module.customsAreaConfig.loadSelectDataIds.call($('select[name="customsAreaId"]')[0],customsAreaIdStr);
		/* 根据一级分类获取二级分类 */	    	
	    	$("#productTypeId0").change(function(){
				$("#productTypeId").find("option").remove();
				$("#productTypeId").append("<option value=''>请选择</option>");
				var productTypeId0=$("#productTypeId0").val();
				var url = "/merchandise/getTwoLevel.asyn";
				var data = {"id":productTypeId0};
				$custom.request.ajax(url, data, function(result){
					var jsonData=result.data;
					var selectObj=$("#productTypeId");
					jsonData.forEach(function(o,i){
		                selectObj.append("<option value='"+ o.value+"'>"+o.label+"</option>");
		            });								
				});						
			}); 
		
		
		//重新加载select2
		$(".goods_select2").select2({
			language:"zh-CN",
			placeholder: '请选择',
			allowClear:true
		});
		
		//保存按钮
		$("#save-buttons").click(function(){
			var url = "/merchandise/modifyMerchandise.asyn";
			var form = $("#add-form").serializeArray();
			if($("#productTypeId0").val()==""){
				$custom.alert.error("一级类目不能为空！");
				return ;
			}
			if($("#productTypeId0").val()==""){
				$custom.alert.error("二级类目不能为空！");
				return ;
			}
			//必填项校验
			if($("#goodsNo").val()==""){
				$custom.alert.error("商品货号不能为空！");
				return ;
			}
			if($("#barcode").val()==""){
				$custom.alert.error("商品条形码不能为空！");
				return ;
			}

			if($("#name").val()==""){
				$custom.alert.error("商品名称不能为空！");
				return ;
			}
			if($("#unit").val()==""){
				$custom.alert.error("计量单位不能为空！");
				return ;
			}

			if($("#countyId").val()==""){
				$custom.alert.error("原产国不能为空！");
				return ;
			}

			$custom.request.ajax(url, form, function(result){
				if(result.state == '200'){
					$custom.alert.success("修改成功！");
					setTimeout(function () {
						$load.a("/merchandise/toPage.html");
					}, 1000);
				}else{
					if(result.expMessage != null){
						$custom.alert.error(result.expMessage);
					}else{
						$custom.alert.error("修改失败！");
					}
				}
			});
		});
		
		//取消按钮
		$("#cancel-buttons").click(function(){
			$load.a("/merchandise/toPage.html");
		});
		
		 
	});
	//点击事件
    function uploadData(urlType) {
    	$("#urlType").val(urlType);
    	var input = '<input type="file" name="file" id="file1" class="btn-hidden file-import">';
		$("#form-upload1").empty();
		$("#form-upload1").append(input);
		$("#file1").click();
    }
	//显示大图    
	function showimage(source){
		window.open(source);
	    /*   $("#ShowImage_Form").find("#img_show").html("<center><image src='"+source+"' class='carousel-inner img-responsive img-rounded' /></center>");
	      $("#ShowImage_Form").modal(); */
	}
	//关闭大图
	$("#ShowImage_Form").click(function(){//再次点击淡出消失弹出层
		 $("#ShowImage_Form").modal('hide');  //手动关闭
	});
	
 
</script>
