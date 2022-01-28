<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
                                            <li class="breadcrumb-item "><a href="javascript:$load.a('/brand/toPage.html');">品牌管理列表</a></li>
                                            <li class="breadcrumb-item"><a href="#">解除品牌匹配</a></li>
                                        </ol>
                                    </div>
                                </div>
                                <!--  title   end  -->
                                <div class="brand_matching">
                                    <div class="brand_matching_table">
                                        <div class="title_text">品牌</div>
                                        <div class="mb-2 mt-2">
                                        	<!-- <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="delete-list-button">解绑</button> -->
                                        </div>
                                        <table class="table table-striped" cellspacing="0" width="100%">
                                            <thead>
                                            <tr>
                                                <th><input id="checkbox11" type="checkbox" name="all"></th>
                                                <th>品牌名称</th>
                                            </tr>
                                            </thead>
                                            <tbody id="table-list">
                                            	<c:forEach items="${detail.brandList }" var="brand">
                                            		<tr>
                                            			<td>
                                            				<input type='checkbox' name='brand-checkbox'><input type='hidden' name='brandId' value='${brand.id }'>
                                            			</td>
                                            			<td>${brand.name }</td>
                                            		</tr>
                                            	</c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                    <span class="matching mt-5">→</span>
                                    <div class="brand_matching_table">
                                        <div class="title_text">匹配品牌名称</div>
                                        <div class="mt-2 mb-2" style="height: 30px;">
                                        	${detail.name }
                                        	<input type="hidden" name="parentId" value="${detail.id }">
                                        </div>
                                    </div>
                                </div>
                                <div class="form-row m-t-50">
                                    <div class="col-12">
                                        <div class="row">
                                            <div class="col-6">
                                                <button type="button"
                                                        class="btn btn-info waves-effect waves-light fr btn_default"
                                                        id="save-buttons">确认解绑
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
    </div>
    <!-- container -->
</div>

</div> <!-- content -->
<script type="text/javascript">
	
	//返回
	$("#cancel-buttons").click(function(){
		$load.a("/brand/toPage.html");
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
 		var unNeedIds = $("#unNeedIds").val();
        var brandId = unNeedIds.split(",");
        $("input[name='brand-checkbox']:checked").each(function() { // 遍历选中的checkbox
            n = $(this).parents("tr").index();  // 获取checkbox所在行的顺序
            brandId.push($(this).next().val());
            $("#table-list").find("tr:eq("+n+")").remove();
        });
        $("#unNeedIds").val(brandId);
        console.log(brandId);
        $("input[name='all']").prop("checked",false);
    });
	
 	//确认解绑
	$("#save-buttons").click(function(){
		var url = "/brand/saveUnMatching.asyn";
		var form = $("#add-form").serializeArray();
		var brandId = [];
		$("input[name='brand-checkbox']:checked").each(function() { // 遍历选中的checkbox
            brandId.push($(this).next().val());
        });
		var data_brandId = {name:"unBrandIds",value:brandId};
		form.push(data_brandId);
		$custom.request.ajax(url, form, function(result){
			if(result.state == '200'){
				$custom.alert.success("解绑成功！");
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
</script>
