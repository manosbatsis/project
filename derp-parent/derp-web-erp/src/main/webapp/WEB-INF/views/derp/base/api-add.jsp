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
                       <li class="breadcrumb-item "><a href="javascript:$load.a('/api/toPage.html');">接口管理列表</a></li>
                       <li class="breadcrumb-item "><a href="javascript:$load.a('/api/toAddPage.html');">新增接口配置</a></li>
                    </ol>
                    </div>
                   </div>
                    <!--  title   end  -->               
                          <!--  title   基本信息   start -->
                    <div class="title_text">基本信息</div>
                <form id="add-form">
         <%--       <div class="form-row mt20 ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr"><i class="red">*</i>选择公司<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                            	<select class="form-control goods_select2" name="platformName">
                            		<option value="">请选择</option>
                            		<c:forEach items="${merchantList }" var="m">
                            			<option value="${m.label }">${m.label }</option>
                            		</c:forEach>
                            	</select>
                            </div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">备注<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                                <input type="text" required="" parsley-type="text" class="form-control"  name="remark">
                            </div>
                        </div>
                    </div>
                </div>   
                  <div class="form-row ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr"><i class="red">*</i>app_key<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                                <input type="text" required="" parsley-type="text" class="form-control"  name="appKey">
                            </div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr"><i class="red">*</i>app_secret<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                                <input type="text" required="" parsley-type="text" class="form-control" name="appSecret">
                            </div>
                        </div>
                    </div>
                </div>--%>
                    <div class="edit_box mt20">
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> 选择公司：</label>
                            <select class="edit_input" name="merchantId" id="merchantId">
                                <option value="">请选择</option>
                                <c:forEach items="${merchantList }" var="merchant">
                                    <option value="${merchant.value }">${merchant.label }</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label">备注：</label>
                            <input type="text" required="" parsley-type="text" class="edit_input"  name="remark">
                        </div>
                        <div class="edit_item"></div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> app_key：</label>
                            <input type="text" required="" parsley-type="text" class="edit_input"  name="appKey">
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> app_secret：</label>
                            <input type="text" required="" parsley-type="text" class="edit_input" name="appSecret">
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
	$(function(){
		//重新加载select2
		$(".goods_select2").select2({
			language:"zh-CN",
			placeholder: '请选择',
			allowClear:true
		});
		
		//保存按钮
		$("#save-buttons").click(function(){
			var url = "/api/saveApi.asyn";	
			var form = $("#add-form").serializeArray();
			$custom.request.ajax(url, form, function(result){
				if(result.state == '200'){
					$custom.alert.success("新增成功！");
					setTimeout(function () {
						$load.a("/api/toPage.html");
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
			$load.a("/api/toPage.html");
		});
	});
</script>