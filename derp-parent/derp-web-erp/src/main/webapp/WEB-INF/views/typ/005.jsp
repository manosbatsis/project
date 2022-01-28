<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- App favicon -->
<link rel="shortcut icon" href='<c:url value="/resources/assets/images/favicon.ico"/>'>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
          <form id="add-form" action="/merchandise/saveMerchandise.asyn">
          <div class="row">
           <div class="col-12">
              <div class="card-box" >
                                         <!--  title   start  -->
              <div class="col-12">
                <div>
                   <div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                       <li><i class="dripicons-location"></i> 当前位置：</li>
                        <li class="breadcrumb-item"><a href="#">新增仓库</a></li>
                    </ol>
                    </div>
            </div>
                    <!--  title   end  -->
                      <div class="form-row">
                          <div class="col-4">
                              <div class="row">
                                  <label  class="col-4 col-form-label " style="white-space:nowrap;"><div class="fr">仓库编号<span>：</span></div></label>
                                  <div class="col-8 ml10">
                                      <input type="text" required="" parsley-type="email" class="form-control" name="goodsNo">
                                  </div>
                              </div>
                          </div>
                          <div class="col-5">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">仓库名称<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <input type="text" required="" parsley-type="email" class="form-control" name="name">
                                  </div>
                              </div>
                          </div>
                      </div>
                        <div class="form-row mt10">
                          <div class="col-4">
                              <div class="row">
                                  <label  class="col-4 col-form-label " style="white-space:nowrap;"><div class="fr">仓库类型<span>：</span></div></label>
                                  <div class="col-8 ml10">
                                      <input type="text" required="" parsley-type="email" class="form-control" name="goodsNo">
                                  </div>
                              </div>
                          </div>
                          <div class="col-5">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">仓管员<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <input type="text" required="" parsley-type="email" class="form-control" name="name">
                                  </div>
                              </div>
                          </div>
                      </div>
                      <div class="form-row mt10">
                          <div class="col-4">
                              <div class="row">
                                  <label  class="col-4 col-form-label " style="white-space:nowrap;"><div class="fr">申报地海关编号<span>：</span></div></label>
                                  <div class="col-8 ml10">
                                      <input type="text" required="" parsley-type="email" class="form-control" name="goodsNo">
                                  </div>
                              </div>
                          </div>
                          <div class="col-5">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">申报地国检编号<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <input type="text" required="" parsley-type="email" class="form-control" name="name">
                                  </div>
                              </div>
                          </div>
                      </div>
                     <div class="form-row mt10">
                         <div class="col-12 ml30">
                              <div class="row">
                                  <label  class="col-1 col-form-label " style="white-space:nowrap;"><div class="fr">仓库详细地址<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <textarea class="form-control" rows="5" name="describe"></textarea>
                                  </div>
                              </div>
                          </div>
                      </div>
                      <div class="form-row mt10">
                      	<div>
                      		关联公司：<button type="button"  onclick='$m5011.init();'>新增</button>
                      	</div>
                      </div>
                      <div class="form-row m-t-50">
                          <div class="col-12">
                              <div class="row">
                                  <div class="col-5">
                                      <button type="button" class="btn btn-info waves-effect waves-light fr" id="save-buttons">保 存</button>
                                  </div>
                                  <div class="col-1"></div>
                                  <div class="col-5">
                                      <button type="button" class="btn btn-light waves-effect waves-light" id="cancel-buttons">取 消</button>
                                  </div>
                              </div>
                          </div>
                      </div>
                </div>
              </div>
             </div>
                               <!--======================Modal框===========================  -->
        <jsp:include page="/WEB-INF/views/modals/5011.jsp" />
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
			var url = "/merchandise/saveMerchandise.asyn";
			var form = $("#add-form").serializeArray();
			$custom.request.ajax(url, form, function(result){
				if(result.state == '200'){
					$custom.alert.success("添加成功！");
					setTimeout(function () {
						$load.a("/merchandise/toPage.html");
					}, 1000);
				}else{
					$custom.alert.error("添加失败！");
				}
			});
		});
		
		//取消按钮
		$("#cancel-buttons").click(function(){
			$load.a("/merchandise/toPage.html");
		});
		
	});
	
	 function searchData(){
	     $mytable.fn.reload();
	 }

	 //新增
	 $("#add-buttons").click(function(){
	 	$load.a("/merchandise/toAddPage.html");
	 });

	 //详情
	 function details(id){
	 	$load.a("/merchandise/toDetailsPage.html?id="+id);
	 }
	 
	 //编辑
	 function edit(id){
	 	$load.a("/merchandise/toEditPage.html?id="+id);
	 }

	 /**
	  * 全选
	  */
	 $('#datatable-buttons').on("change", ":checkbox", function() {
	     // 列表复选框
	     if ($(this).is("[name='keeperUserGroup-checkable']")) {
	         // 全选
	         $(":checkbox", '#datatable-buttons').prop("checked",$(this).prop("checked"));
	     }else{
	         // 一般复选
	         var checkbox = $("tbody :checkbox", '#datatable-buttons');
	         $(":checkbox[name='cb-check-all']", '#datatable-buttons').prop('checked', checkbox.length == checkbox.filter(':checked').length);
	     }
	 });
</script>
