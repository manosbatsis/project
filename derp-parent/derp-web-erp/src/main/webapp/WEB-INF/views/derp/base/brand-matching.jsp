<%-- <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- App favicon -->
<link rel="shortcut icon" href='<c:url value="/resources/assets/images/favicon.ico"/>'>
<link href='<c:url value="/resources/assets/css/common.css" />' rel="stylesheet" type="text/css"/>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
        <form id="add-form">
            <div class="row">
                <div class="col-12">
                    <div class="card-box">
                        <!--  title   start  -->
                        <div class="col-12">
                            <div>
                                <div class="col-12 row">
                                    <div>
                                        <ol class="breadcrumb">
                                            <li><i class="block"></i> 当前位置：</li>
                                            <li class="breadcrumb-item"><a href="#">品牌管理</a></li>
                                            <li class="breadcrumb-item "><a href="javascript:$load.a('/brand/toPage.html');">品牌管理列表</a></li>
                                            <li class="breadcrumb-item"><a href="javascript:$load.a('/brand/toMatchingPage.html');">品牌匹配</a></li>
                                        </ol>
                                    </div>
                                </div>
                                <!--  title   end  -->
                                <div class="title_text mb-2">选择品牌</div>
                                <div class="mb-2">
                                	<button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick="$m1111.init();">选择</button>
                                	<button type="button" class="btn btn-find waves-effect waves-light btn_default" id="delete-list-button">删除</button>
                                </div>
                                <div class="brand_matching">
                                    <div class="brand_matching_table">
                                        <table class="table table-striped" cellspacing="0" width="100%">
                                            <thead>
                                            <tr>
                                            	<th><input id="checkbox11" type="checkbox" name="all"></th>
                                                <th>品牌名称</th>
                                            </tr>
                                            </thead>
                                            <tbody id="table-list">
                                            </tbody>
                                        </table>
                                    </div>
                                    <span class="matching mt-5">→</span>
                                    <div class="form-group">
                                        <label for="">关联品牌：</label>
                                        <div class="edit_input" style="text-indent: 0">
	                                        <select class="form-control goods_select2" id="parentId" name="parentId">
	                                        	<option value="">请选择</option>
	                                        	<c:forEach items="${brandList }" var="brand">
	                                        		<option value="${brand.value }">${brand.label }</option>
	                                        	</c:forEach>
	                                        </select>
                                        </div>
                                    </div>
                                </div>

                                <div class="form-row m-t-50">
                                    <div class="col-12">
                                        <div class="row">
                                            <div class="col-6">
                                                <button type="button"
                                                        class="btn btn-info waves-effect waves-light fr btn_default"
                                                        id="save-buttons">确认匹配
                                                </button>
                                            </div>
                                            <div class="col-6">
                                                <button type="button"
                                                        class="btn btn-light waves-effect waves-light btn_default"
                                                        id="cancel-buttons">取 消
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!--======================Modal框===========================  -->
                    <!-- end row -->
                </div>
            </div>
        </form>
        <jsp:include page="/WEB-INF/views/modals/1111.jsp" />
    </div>
    <!-- container -->
</div>

</div> <!-- content -->
<script type="text/javascript">

	//重新加载select2
	$(".goods_select2").select2({
		language:"zh-CN",
		placeholder: '请选择',
		allowClear:true
	});
	
	//返回
	$("#cancel-buttons").click(function(){
		$load.a("/brand/toPage.html");
	});
	
	//确认匹配
	$("#save-buttons").click(function(){
		//必填项校验
		if($("#unNeedIds").val()==""){
			$custom.alert.error("至少选择一个品牌！");
			return ;
		}
		if($("#parentId").val()==""){
			$custom.alert.error("匹配品牌名称必选！");
			return ;
		}
		var url = "/brand/saveMatching.asyn";
		var form = $("#add-form").serializeArray();
		var brandIds="";//储存所有品牌ID
		var obj_brandIds = document.getElementsByName("brandId");
		for(var i = 0; i < obj_brandIds.length; i++){
			if(brandIds == ""){
				brandIds = obj_brandIds[i].value;
			}else{
				brandIds = brandIds + "," + obj_brandIds[i].value;
			}
		}
		var data_brandIds = {name:"brandIds",value:brandIds};
		form.push(data_brandIds);
		$custom.request.ajax(url, form, function(result){
			if(result.state == '200'){
				$custom.alert.success("匹配成功！");
				setTimeout(function () {
					$load.a("/brand/toPage.html");
				}, 1000);
			}else{
				if(result.expMessage != null){
					$custom.alert.error(result.expMessage);
				}else{
					$custom.alert.error(result.message);
				}
			}
		});
	});
	
	$("input[name='all']").click(function(){
		//判断当前点击的复选框处于什么状态$(this).is(":checked") 返回的是布尔类型
		if($(this).is(":checked")){
			$("input[name='brand-checkbox']").prop("checked",true);
		}else{
			$("input[name='brand-checkbox']").prop("checked",false);
		}
	});
	
	//删除选中行
 	$("#delete-list-button").click(function() {
 		var brandId = [];
        $("input[name='brand-checkbox']:checked").each(function() { // 遍历选中的checkbox
            n = $(this).parents("tr").index();  // 获取checkbox所在行的顺序
            brandId.push($(this).next().val());
            $("#table-list").find("tr:eq("+n+")").remove();
        });
        var unNeedIds = $("#unNeedIds").val();
        var temp = unNeedIds.split(",");
        for (var i = 0; i < temp.length; i++) {
        	for (var j = 0; j < brandId.length; j++) {
				if(temp[i] == brandId[j]){
					temp.splice(i,1);
				}
			}
		}
        $("#unNeedIds").val(temp);
        $("input[name='all']").prop("checked",false);
    });

</script>
 --%>