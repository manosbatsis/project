<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
          <div class="row">
           <div class="col-12">
              <div class="card-box table-responsive" >
                                          <!--  title   start  -->
              <div class="col-12">
                <div>
                   <div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                       <li><i class="block"></i> 当前位置：</li>
                       <li class="breadcrumb-item"><a href="#">接口管理</a></li>
                       <li class="breadcrumb-item "><a href="javascript:$load.a('/apiExternal/toPage.html');">接口管理列表</a></li>
                       <li class="breadcrumb-item "><a href="javascript:$load.a('/apiExternal/toAddPage.html');">新增外部接口配置</a></li>
                    </ol>
                    </div>
                   </div>
                    <!--  title   end  -->               
                          <!--  title   基本信息   start -->
                    <div class="title_text">基本信息</div>
                <form id="add-form">
                    <div class="edit_box mt20">
                    <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i>使用对象：</label>
                            <input type="text" required="" parsley-type="text" class="edit_input"  id="platformName" name="platformName" onkeyup="this.value=this.value.replace(/\s+/g,'')">
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> 选择公司：</label>
                            <select class="edit_input" name="merchantId" id="merchantId">
                                <option value="">请选择</option>
                            </select>
                        </div>
                        
                        <div class="edit_item"></div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> app_key：</label>
                            <input type="text" required="" parsley-type="text" class="edit_input"  name="appKey" id="appKey" onkeyup="this.value=this.value.replace(/\s+/g,'')">
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> 秘钥：</label>
                            <input type="text" required="" parsley-type="text" class="edit_input" name="appSecret" id="appSecret" onkeyup="this.value=this.value.replace(/\s+/g,'')">
                        </div>
                        <div class="edit_item">
                        <label class="edit_label"></label>
                        	<button type="button" class="btn btn-info waves-effect waves-light fr"   id="create-button" >生成</button>
                        </div>
                    </div>
                    
                    <div class="edit_box">
                        <div class="edit_item" style="flex: 2;">
                              <label class="edit_label" style="width: 20.5%">备注：</label>
                              <textarea class="edit_input" rows="5" name="remark" onkeyup="this.value=this.value.replace(/\s+/g,'')"></textarea>
                          </div>
                        <div class="edit_item">
                        </div>
                        <div class="edit_item"></div>
                    </div>
                    
                    
                </form>
                 <!--  title   基本信息  start -->
                      <div class="form-row m-t-50">
                          <div class="col-12">
                              <div class="row">
                                  <div class="col-6">
                                      <button type="button" class="btn btn-info waves-effect waves-light fr" id="save-buttons">保 存</button>
                                  </div>
                                  <div class="col-6">
                                      <button type="button" class="btn btn-light waves-effect waves-light" id="cancel-buttons">取 消</button>
                                  </div>
                              </div>
                          </div>
                      </div>
                </div>
              </div>
                           <!-- 新增弹窗部分   start -->
             </div>
                  <!-- end row -->
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->

<script src='/resources/js/system/base.js' type="text/javascript"></script>
<script type="text/javascript">

    $module.merchant.loadSelectData.call($("#merchantId")) ;
//生成app_key和秘钥
$("#create-button").click(function(){
	$.post("/apiExternal/getAppKeyAndAppSecret.asyn",function(data){
		var dataRole = data.data;
		$("#appKey").val(dataRole.appKey);
		$("#appSecret").val(dataRole.appSecret);
	});
});

	$(function(){
		//重新加载select2
		$(".goods_select2").select2({
			language:"zh-CN",
			placeholder: '请选择',
			allowClear:true
		});
		
		//保存按钮
		$("#save-buttons").click(function(){
			var url = "/apiExternal/saveApiExternal.asyn";	
			var form = $("#add-form").serializeArray();
			//必填项校验
			if($("#platformName").val()==""){
				$custom.alert.error("使用对象不能为空！");
				return ;
			}
			if($("#merchantId").val()==""){
				$custom.alert.error("公司不能为空！");
				return ;
			}
			if($("#appKey").val()==""){
				$custom.alert.error("app_key不能为空！");
				return ;
			}
			if($("#appSecret").val()==""){
				$custom.alert.error("秘钥不能为空！");
				return ;
			}
			
			$custom.request.ajax(url, form, function(result){
				if(result.state == '200'){
					$custom.alert.success("新增成功！");
					setTimeout(function () {
						$load.a("/apiExternal/toPage.html");
					}, 1000);
				}else{
					if(result.expMessage != null){
						$custom.alert.error(result.expMessage);
					}else{
						$custom.alert.error("添加失败！");
					}
				}
			});
		});
		
		//返回
		$("#cancel-buttons").click(function(){
			$load.a("/apiExternal/toPage.html");
		});
	});
</script>