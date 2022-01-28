<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt60">
        <!-- Start row -->
          <form id="edit-form" action="/supplier/modifySupplier.asyn">
             <div class="row">
              <div class="card-box margin table-responsive" style="overflow-x:hidden;">
                 <div class="row">
              <!--  title   start  -->
              <div class="col-12">
                <div>
                   <div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                       <li><i class="block"></i> 当前位置：</li>
                       <li class="breadcrumb-item"><a href="#">供应商档案</a></li>
                       <li class="breadcrumb-item"><a href="javascript:$load.a('/supplier/toPage.html')">供应商列表</a></li>
                       <li class="breadcrumb-item"><a href="javascript:$load.a('/supplier/toRatioPage.html')">编辑资金方加价系数</a></li>
                    </ol>
                    </div>
            </div>
                    <!--  title   end  -->
                  <div class="title_text">基础信息</div>
				<input type="hidden" value="${detail.id}" name="id">
				<div class="info_box">
					 <div class="info_item">
                          <div class="info_text">
                              <span style="color:#F00">所属公司：</span>
                              <span>${detail.merchantName}</span>
                          </div>
                          <div class="info_text">
                              <span>客户名称：</span>
                              <span>${detail.name}</span>
                          </div>
                          <div class="info_text"></div>
                      </div>
                       <div class="info_item">
                          <div class="info_text">
                              <span>客户简称：</span>
                              <span>${detail.simpleName}</span>
                          </div>
                          <div class="info_text">
                              <span>营业执照号：</span>
                              <span>${detail.creditCode}</span>
                          </div>
                          <div class="info_text"></div>
                      </div>
                      <div class="info_item">
                          <div class="info_text">
                              <span>组织机构代码：</span>
                              <span>${detail.orgCode}</span>
                          </div>
                          <div class="info_text">
                          </div>
                          <div class="info_text"></div>
                      </div>
				</div>
                      <div class="title_text">资金方加价系统</div>
                      <div class="info_box">
                      	<div class="info_item">
                          <div class="info_text">
                              <span style="color:#F00">卓烨加价系数：</span>
                              <span><input type="text" required="" style="width:60%" parsley-type="email" class="edit_input" id="yeMarkupRatio"name="yeMarkupRatio" value="${detail.yeMarkupRatio }"  onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|^\d{1,}\%|\d{1,}]/g,'')" maxlength="9">
                            </span>
                          </div>
                          <div class="info_text">
                          <span style="color:#F00">卓普信加价系数：</span>
                              <span>
                              <input type="text" required="" style="width:60%"parsley-type="email" class="edit_input" id="xinMarkupRatio"name="xinMarkupRatio" value="${detail.xinMarkupRatio }"  onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|^\d{1,}\%|\d{1,}]/g,'')" maxlength="9">
                     </span>
                          </div>
                          <div class="info_text"></div>
                      	</div>
                      </div>
		         <div class="edit_box">
		             <div class="edit_item">
		             </div>
		             <div class="edit_item">
	                      <div class="form-row m-t-50">
	                          <div class="col-10">
	                              <div class="row">
	                                  <div class="col-5">
	                                      <button type="button" class="btn btn-info waves-effect waves-light fr btn_default" id="save-buttons" >保 存</button>
	                                  </div>
	                                  <div class="col-5">
	                                      <button type="button" class="btn btn-light waves-effect waves-light btn_default" id="cancel-buttons">取 消</button>
	                                  </div>
	                              </div>
	                          </div>
	                      </div>
		           </div>
		      </div>
                  <!-- end row -->
          </form>
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->
<script type="text/javascript">
	$(function(){
		//保存按钮
		$("#save-buttons").click(function(){
			var url = "/supplier/modifySupplier.asyn";
			var form = $("#edit-form").serializeArray();
			//必填项校验
			if($("#yeMarkupRatio").val()==""){
				$custom.alert.error("卓烨加价系数不能为空！");
				return ;
			}
			if($("#xinMarkupRatio").val()==""){
				$custom.alert.error("卓普信加价系数不能为空！");
				return ;
			}
			$custom.request.ajax(url, form, function(result){
				if(result.state == '200'){
					$custom.alert.success("编辑成功！");
					setTimeout(function () {
						$load.a("/supplier/toPage.html");
					}, 1000);
				}else{
					$custom.alert.error("编辑失败！");
				}
			});
		});
		
		//取消按钮
		$("#cancel-buttons").click(function(){
			$load.a("/supplier/toPage.html");
		});
		
	});
</script>
