<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
        <form id="add-form">
            <div class="row">
                <div class="card-box margin table-responsive">
                    <div class="col-12 row">
                        <div>
                            <ol class="breadcrumb">
                                <li><i class="block"></i> 当前位置：</li>
                                <li class="breadcrumb-item"><a href="#">商品档案</a></li>
                                <li class="breadcrumb-item "><a href="javascript:$load.a('/product/toPage.html');">货品列表</a>
                                </li>
                                <li class="breadcrumb-item "><a
                                        href="javascript:$load.a('/product/toEditPage.html?id=${detail.id}');">货品编辑</a>
                                </li>
                            </ol>
                        </div>
                    </div>
                    <div class="title_text">货品基础信息</div>
                    <div class="edit_box mt20">
                        <%-- <div class="edit_item">
                        <input type="hidden" id="id" name="id" value="${detail.id}">
                            <label class="edit_label red">产品类别：</label>
                                <select name="productTypeId" id = "productTypeId"class="edit_input ">
                                       <option value="">请选择</option>
                                      <c:forEach items="${productTypeBean }" var="productType">
                               	    	<option value="${productType.value }" 
                               	    		<c:if test="${detail.productTypeId == productType.value}"> selected = 'selected'</c:if>>
                               	    		${productType.label }
                               	    	</option>
                               	    </c:forEach>
                              </select>
                        </div> --%>
                        <div class="edit_item">
                        <input type="hidden" id="id" name="id" value="${detail.id}">
                             <label class="edit_label red">一级类目：</label>
                                <select name="productTypeId0" id = "productTypeId0"class="edit_input ">
                                       <option value="">请选择</option>
                                      <c:forEach items="${oneCatBean }" var="productType0">
                               	    	<option value="${productType0.value }" <c:if test='${detail.productTypeId0 eq productType0.value}'>selected</c:if>> 

                               	    		${productType0.label }
                               	    	</option>
                               	    </c:forEach>
                                  </select>
                          </div>
                          
                          
                          <div class="edit_item">
                                <label class="edit_label red">二级类目：</label>
                                <select name="productTypeId" id = "productTypeId"class="edit_input ">
                                       <option value="">请选择</option>
                                      <c:forEach items="${twoCatBean }" var="productType2">
                               	    	<option value="${productType2.value }" 
                               	    		<c:if test="${detail.productTypeId == productType2.value}"> selected = 'selected'</c:if>>
                               	    		${productType2.label }
                               	    	</option>
                               	    </c:forEach>
                                  </select>
                        </div>
                        
                        
                        
                        
                        
                        <div class="edit_item">
                            <label class="edit_label">条形码：</label>
                            <div class="no_edit">
                                ${detail.barcode }
                            </div>
                        </div>
                    </div>
                    <div class="edit_box mt20">
                        <div class="edit_item">
                             <label class="edit_label red">品牌：</label>
                             <select name="brandId" id="brandId"  class="edit_input ">
                                  <option value="">请选择</option>
                                  <c:forEach items="${brandBean }" var="brand">
                                      <option value="${brand.value }" <c:if test="${detail.brandId == brand.value}"> selected = 'selected'</c:if>>
                                      ${brand.label }</option>
                                  </c:forEach>
                             </select>  
                        </div>
                        <div class="edit_item">
                            <label class="edit_label">规格型号：</label>
                            <div class="no_edit">
                                ${detail.spec }
                            </div>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label">保质天数：</label>
                            <div class="no_edit">
                                ${detail.shelfLifeDays }
                                </div>
                        </div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item">
                            <label class="edit_label">毛重：</label>
                            <div class="no_edit">
                            ${detail.grossWeight }
                            </div>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label">净重：</label>
                            <div class="no_edit">
                                ${detail.netWeight }
                            </div>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label">长：</label>
                            <div class="no_edit">
                            ${detail.length }
                            </div>
                        </div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item">
                            <label class="edit_label">宽：</label>
                            <div class="no_edit">
                                ${detail.width }
                            </div>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label">高：</label>
                            <div class="no_edit">
                                ${detail.height }
                            </div>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label">体积：</label>
                            <div class="no_edit">
                                ${detail.volume }
                            </div>
                        </div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item">
                            <label class="edit_label">生产厂家：</label>
                            <div class="no_edit">
                                ${detail.manufacturer }
                            </div>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label">生产地址：</label>
                          <div class="no_edit">
                            	${detail.manufacturerAddr }
                            	</div>
                        </div>
                        <div class="edit_item">
                                <label class="edit_label ">计量单位：</label>
                                <div class="no_edit">
                                ${detail.unitName }
                                </div>
                        </div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item">
                            <label class="edit_label">备注：</label>
                            <div class="no_edit">
                                ${detail.remark }
                            </div>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label">原产国：</label>
                            <div class="no_edit">
                                 ${detail.countyName }
                            </div>
                        </div>
                        <div class="edit_item">
                                <label class="edit_label red"></label>

                        </div>
                    </div>
                </div>
                <div class="card-box margin table-responsive">
                    <div class="form-row">
                        <label class="col-1 col-form-label "><span class="fr">商品图片<span>：</span></span></label>
                        <div class="col-11 ml10 line">
                            <div>
                                <img alt="商品图片1" src="/resources/assets/images/bg-1.png" width="150px" height="150px">
                                <img alt="商品图片2" src="/resources/assets/images/bg-1.png" width="150px" height="150px">
                                <img alt="商品图片3" src="/resources/assets/images/bg-1.png" width="150px" height="150px">
                                <img alt="商品图片4" src="/resources/assets/images/bg-1.png" width="150px" height="150px">
                                <img alt="商品图片5" src="/resources/assets/images/bg-1.png" width="150px" height="150px">
                            </div>
                        </div>
                    </div>
                    <div class="form-row m-t-50">
                        <div class="col-12">
                            <div class="row">
                                <div class="col-6">
                                    <button type="button" class="btn btn-info waves-effect waves-light fr"
                                            id="save-buttons">保 存
                                    </button>
                                </div>
                                <div class="col-6">
                                    <button type="button" class="btn btn-light waves-effect waves-light"
                                            id="cancel-buttons">取 消
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
        </form>
        <!-- end row -->
    </div>
    <!-- container -->
</div>

</div> <!-- content -->
<script src='/resources/js/system/base.js' type="text/javascript"></script>
<script type="text/javascript">

/* 根据一级类目获取二级类目*/	    	
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
//保存按钮
$("#save-buttons").click(function(){
	var url = "/product/modifyProduct.asyn";	
	var form = $("#add-form").serializeArray();
	if($("#productTypeId0").val()==""){
		$custom.alert.error("一级类目不能为空！");
		return ;
	}
	if($("#productTypeId").val()==""){
		$custom.alert.error("二级类目不能为空！");
		return ;
	}
	if($("#brandId").val()==""){
		$custom.alert.error("品牌不能为空！");
		return ;
	}
	
	$custom.request.ajax(url, form, function(result){
		if(result.state == '200'){
			$custom.alert.success("编辑成功！");
			setTimeout(function () {
				$load.a("/product/toPage.html");
			}, 1000);
		}else{
			$custom.alert.error("编辑失败！");
		}
	});
});


$(function(){
	//返回按钮
	$("#cancel-buttons").click(function(){
		$load.a("/product/toPage.html");
	});
});
</script>
