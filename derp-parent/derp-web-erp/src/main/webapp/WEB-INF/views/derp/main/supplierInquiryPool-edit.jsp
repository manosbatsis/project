<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!-- Start content -->
<div class="content">
    <div class="container-fluid mt60">
        <!-- Start row -->
         <form id="edit-form">
        <div class="row">
            <div class="card-box margin table-responsive" id="border">
                 <!--  title   start  -->
              <div class="col-12 ml10">
                 <div class="col-12 row">
                       <ol class="breadcrumb">
                       <li><i class="block"></i> 当前位置：</li>
                       <li class="breadcrumb-item"><a href="#">供应商档案</a></li>
                       <li class="breadcrumb-item"><a href="javascript:$load.a('/supplierInquiryPool/toPage.html');">供应商询价池列表</a></li>
                       <li class="breadcrumb-item"><a href="javascript:$load.a('/supplierInquiryPool/toEditPage.html?id=${detail.id }');">编辑供应商询价池</a></li>
                    </ol>
                 </div>
                    <!--  title   end  -->
                    <!--  title   供应商详情  start -->
                  <div class="title_text">供应商详情</div>
                <input type="hidden" value="${detail.id}" name="id">
                <div class="form-row mt20 ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">供应商编号<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>${detail.customerCode}</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">供应商名称<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>${detail.customerName}</div>
                            </div>
                        </div>
                    </div>
                </div>   
                  <div class="form-row ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">供应商简称<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>${detail.customerName}</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">结算方式<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                 <c:choose>
                              	  	  <c:when test="${detail.settlementMode eq '1'}"> 预付</c:when>
                              	  	  <c:when test="${detail.settlementMode eq '2'}"> 到付</c:when>
                              	  	  <c:when test="${detail.settlementMode eq '3'}"> 月结</c:when>
                              	  </c:choose>
                            </div>
                        </div>
                    </div>
                </div>  
                 <!--  title   供应商详情  start -->
                 <!--   商品报价  start -->
                  <div class="title_text">询价池</div>
              <%--  <div class="form-row ml15 mt20">
                    <div class="col-3">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">品类 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                               <select name="merchandiseCatId" class="form-control goods_select2">
                               	    <option value="">请选择</option>
                               	    <c:forEach items="${productTypeBean }" var="productType">
                               	    	<option value="${productType.value }" <c:if test="${detail.merchandiseCatId == productType.value}"> selected = 'selected'</c:if>>${productType.label }</option>
                               	    </c:forEach>
                               	</select>
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">品牌<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <select name="brandId" class="form-control goods_select2">
                               	    <option value="">请选择</option>
                               	    <c:forEach items="${brandBean }" var="brand">
                               	    	<option value="${brand.value }" <c:if test="${detail.brandId == brand.value}"> selected = 'selected'</c:if>>${brand.label }</option>
                               	    </c:forEach>
                               	</select>
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">原产国<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <select name="countryId" class="form-control goods_select2">
                               		<option value="">请选择</option>
                               		<c:forEach items="${countryBean }" var="country">
                               			<option value="${country.value }" <c:if test="${detail.countryId == country.value}"> selected = 'selected'</c:if>>${country.label }</option>
                               		</c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">计量单位<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <select class="form-control"">
                               		<option value="">请选择</option>
                               </select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row ml15">
                    <div class="col-3">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">商品名称<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <input class="form-control"" type="text" name="goodsName" value="${detail.goodsName}"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">最小起订量<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <input class="form-control"" type="text" name="minimum" value="${detail.minimum}"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">最大供货量<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <input class="form-control"" type="text" name="maximum" value="${detail.maximum}"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">供货价<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <input class="form-control"" type="text" name="supplyPrice" value="${detail.supplyPrice}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row ml15">
                    <div class="col-3">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">规格型号<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <input class="form-control"" type="text" name="spec" value="${detail.spec}"/>
                            </div>
                        </div>
                    </div>
                </div>--%>
                  <div class="edit_box mt20">
                      <div class="edit_item">
                          <label class="edit_label"><i class="red">*</i>品类：</label>
                          <div class="edit_input" style="text-indent: 0">
                              <select name="merchandiseCatId" id="merchandiseCatId" class="form-control goods_select2">
                                      <option value="">请选择</option>
                                      <c:forEach items="${productTypeBean }" var="productType">
                               	    	<option value="${productType.value }" 
                               	    		<c:if test="${detail.merchandiseCatId == productType.value}"> selected = 'selected'</c:if>>
                               	    		${productType.label }
                               	    	</option>
                               	    </c:forEach>
                                  </select>
                          </div>
                      </div>
                      <div class="edit_item">
                          <label class="edit_label"><i class="red">*</i>品牌：</label>
                          <div class="edit_input">
                              <select name="brandId" id="brandId"  class="form-control goods_select2">
                                  <option value="">请选择</option>
                                  <c:forEach items="${brandBean }" var="brand">
                                      <option value="${brand.value }" <c:if test="${detail.brandId == brand.value}"> selected = 'selected'</c:if>>${brand.label }</option>
                                  </c:forEach>
                              </select>
                          </div>
                      </div>
                      <div class="edit_item">
                          <label class="edit_label"><i class="red">*</i>原产国：</label>
                          <div class="edit_input">
                              <select name="countryId" id="countryId" class="form-control goods_select2">
                                  <option value="">请选择</option>
                                  <c:forEach items="${countryBean }" var="country">
                                      <option value="${country.value }" <c:if test="${detail.countryId == country.value}"> selected = 'selected'</c:if>>${country.label }</option>
                                  </c:forEach>
                              </select>
                          </div>
                      </div>
                  </div>
                  <div class="edit_box">
                      <div class="edit_item">
                          <label class="edit_label"><i class="red">*</i>计量单位：</label>
                              <div class="edit_input" style="text-indent: 0">
                              <select name="unitId" id="unitId" class="form-control goods_select2">
                                      <option value="">请选择</option>
                                      <c:forEach items="${unitBean }" var="unit">
                                      <option value="${unit.value }" 
                                      	<c:if test="${detail.unitId eq unit.value}"> selected = 'selected'</c:if>>${unit.label }
                                      </option>
                                  </c:forEach>
                                  </select>
                              </div>
                      </div>
                      <div class="edit_item">
                          <label class="edit_label">商品名称：</label>
                          <input class="edit_input" type="text" name="goodsName" value="${detail.goodsName}"/>
                      </div>
                      <div class="edit_item">
                          <label class="edit_label">最小起订量：</label>
                          <input class="edit_input" type="text" name="minimum" id="minimum" value="${detail.minimum}"/>
                      </div>

                  </div>
                  <div class="edit_box">
                      <div class="edit_item">
                          <label class="edit_label">最大供货量：</label>
                          <input class="edit_input" type="text" name="maximum" id="maximum" value="${detail.maximum}"/>
                      </div>
                      <div class="edit_item">
                          <label class="edit_label">供货价：</label>
                          <input class="edit_input" type="text" name="supplyPrice" id="supplyPrice" value="${detail.supplyPrice}"/>
                      </div>
                      <div class="edit_item">
                          <label class="edit_label">规格型号：</label>
                          <input class="edit_input" type="text" name="spec" value="${detail.spec}"/>
                      </div>
                  </div>
                 <!--   商品报价  end -->
                 <div class="form-row mt20">
                      	<div class="form-inline m0auto">
                          		<div class=form-group>
                          			<button type="button" class="btn btn-info waves-effect waves-light fr btn_default" id="save-buttons">保存</button>
                          			<button type="button" class="btn btn-light waves-effect waves-light ml15 btn_default" id="cancel-buttons">取消</button>
                          		</div>
                          	</div>
                      </div>
            <!-- end row -->
              </div>
            </div>
        </div>
            </form>
            </div>
            <!-- container -->
        </div>

    </div> <!-- content -->
<script src='/resources/js/system/base.js' type="text/javascript"></script>
<script type="text/javascript">
	$(function(){
		//取消按钮
		$("#cancel-buttons").click(function(){
			$load.a("/supplierInquiryPool/toPage.html");
		});
		//保存按钮
		$("#save-buttons").click(function(){
			var url = "/supplierInquiryPool/modifySIPool.asyn";
			var form = $("#edit-form").serializeArray();
			if($("#merchandiseCatId").val()==""){
				$custom.alert.error("商品品类不能为空！");
				return ;
			}
			if($("#countryId").val()==""){
				$custom.alert.error("原产国不能为空！");
				return ;
			}
			if($("#unitId").val()==""){
				$custom.alert.error("计量单位不能为空！");
				return ;
			}
			if($("#brandId").val()==""){
				$custom.alert.error("商品品牌不能为空！");
				return ;
			}
			if(isNotNum($("#minimum").val() , "最小起订量不为数字")){
				return ;
			}
			if(isNotNum($("#maximum").val() , "最大供货量不为数字")){
				return ;
			}
			if(isNotNum($("#supplyPrice").val() , "供货价不为数字")){
				return ;
			}
			
			$custom.request.ajax(url, form, function(result){
				if(result.state == '200'){
					$custom.alert.success("编辑成功！");
					setTimeout(function () {
						$load.a("/supplierInquiryPool/toPage.html");
					}, 1000);
				}else{
					$custom.alert.error("编辑失败！");
				}
			});
		});
	});
	
	function isNotNum(val , msg){
    	if(isNaN(val)){
        	$custom.alert.error(msg);
        	return true ;
        }
    	
    	return false ;
    }
</script>