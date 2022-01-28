<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<style>
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
                        <li class="breadcrumb-item "><a href="javascript:$load.a('/merchandise/toAddPage.html');">商品新增</a></li>
                    </ol>
                    </div>
           		 </div>
           		 
                  <div class="title_text">基础信息</div>
                  <div class="margin">
                      <div class="edit_box">
                          <div class="edit_item">
                              <label class="edit_label"><i class="red">*</i> 商品货号：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" name="goodsNo" id="goodsNo">
                          </div>
                          <div class="edit_item">
                              <label class="edit_label"><i class="red">*</i> 商品条形码：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" name="barcode" id="barcode">
                          </div>
                          <div class="edit_item">
                              <label class="edit_label"><i class="red">*</i> 商品名称：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" name="name" id="name">
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
                              <label class="edit_label">hs编码：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" name="hsCode">
                          	</div>
                            <div class="edit_item">
                              <label class="edit_label">商品英文名称：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" name="englishGoodsName">
                          	</div>
                      </div>
                      
                      <div class="edit_box">
                           <div class="edit_item">
                              <label class="edit_label">企业商品编码：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" name="factoryNo">                              
                           </div>
                           <div class="edit_item">
                          	</div>
                            <div class="edit_item">
                          	</div>
                      </div>
                      
                      <div class="title_text">备案信息</div>
                      <div class="edit_box">
                          <div class="edit_item">
                              <label class="edit_label">商品净重：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" name="netWeight" oninput = "value=value.replace(/[^\.\d]/g,'')">kg
                          </div>
                          <div class="edit_item">
                              <label class="edit_label">商品毛重：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" name="grossWeight" oninput = "value=value.replace(/[^\.\d]/g,'')">kg
                          </div>
                           <div class="edit_item">
                              <label class="edit_label">规格型号：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" name="spec">
                          </div>
                      </div>
                      <div class="edit_box">
                          <div class="edit_item">
                              <label class="edit_label">保质天数：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" name="shelfLifeDays" oninput = "value=value.replace(/[^\d]/g,'')">
                          </div>
                          <div class="edit_item">
                              <label class="edit_label">备案单价：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" name="filingPrice" oninput = "value=value.replace(/[^\.\d]/g,'')">元
                          </div>
                           <div class="edit_item">
                              <label class="edit_label"><i class="red">*</i> 计量单位：</label>
                              <div class="edit_input" style="text-indent: 0;">
                                  <select name="unit" class="form-control goods_select2" id="unit">
                                      <option value="">请选择</option>
                                      <c:forEach items="${unitBean }" var="unit">
                                          <option value="${unit.value }">${unit.label }</option>
                                      </c:forEach>
                                  </select>
                              </div>
                          </div>
                      </div>
                      
                      <div class="edit_box">
                         <div class="edit_item">
                              <label class="edit_label"><i class="red">*</i> 原产国：</label>
                              <div class="edit_input" style="text-indent: 0;">
                                  <select name="countyId" class="form-control goods_select2" id="countyId">
                                      <option value="">请选择</option>
                                      <c:forEach items="${countryBean }" var="country">
                                          <option value="${country.value }">${country.label }</option>
                                      </c:forEach>
                                  </select>
                              </div>
                          </div>
                          <div class="edit_item">
                              <label class="edit_label">原产地区：</label>
                              <input type="text"  class="edit_input" name="countyArea">
                          </div>
                           <div class="edit_item">
                              <label class="edit_label">申报要素：</label>
                              <input type="text"  class="edit_input" name="declareFactor">
                          </div>
                      </div>
                      
                       <div class="edit_box">
                          <div class="edit_item">
                              <label class="edit_label">商品成分：</label>
                              <input type="text"  class="edit_input" name="component">
                          </div>
                          <div class="edit_item">
                              <label class="edit_label">生产企业名称：</label>
                              <input type="text"  class="edit_input" name="manufacturer">
                          </div>
                           <div class="edit_item">
                              <label class="edit_label">生产企业地址：</label>
                              <input type="text"  class="edit_input" name="manufacturerAddr">
                          </div>
                      </div>
                      <div class="edit_box">
                          <div class="edit_item">
                              <label class="edit_label">第一法定单位：</label>
                              <input type="text"  class="edit_input" name="unitNameOne">
                          </div>
                          <div class="edit_item">
                              <label class="edit_label">第二法定单位：</label>
                              <input type="text"  class="edit_input" name="unitNameTwo">
                          </div>
                           <div class="edit_item">
                              <label class="edit_label">海关商品备案号：</label>
                              <input type="text"  class="edit_input" name="customsGoodsRecordNo">
                          </div>
                      </div>
                      
                      <div class="title_text">箱规信息</div>
                      <div class="form-row mt20 ml15">
                          <div class="edit_item">
                              <label class="edit_label" style="width: 15%">箱转换：</label>
                              <span class="edit_span">1箱 转换为</span>
                              <input type="text" class="box_input" name="boxToUnit" oninput = "value=value.replace(/[^\d]/g,'')">
                              <span class="edit_span">件</span>
                          </div>
                          <div class="edit_item">
                              <label class="edit_label" style="width: 15%">托转换：</label>
                              <span class="edit_span">1托 转换为</span>
                              <input type="text" class="box_input" name="torrToUnit" oninput = "value=value.replace(/[^\d]/g,'')">
                              <span class="edit_span">件</span>
                          </div>
                      </div>
                      
                      <div class="title_text">规格描述</div>
                      <div class="edit_box">
                          <div class="edit_item">
                              <label class="edit_label">长：</label>
                              <input type="text"  class="edit_input" name="length" oninput = "value=value.replace(/[^\.\d]/g,'')">cm
                          </div>
                          <div class="edit_item">
                              <label class="edit_label">宽：</label>
                              <input type="text"  class="edit_input" name="width" oninput = "value=value.replace(/[^\.\d]/g,'')">cm
                          </div>
                          <div class="edit_item">
                              <label class="edit_label">高：</label>
                              <input type="text"  class="edit_input" name="height" oninput = "value=value.replace(/[^\.\d]/g,'')">cm
                          </div>
                      </div>
                      
                      
                     <div class="edit_box">
                          <div class="edit_item">
                              <label class="edit_label">体积：</label>
                              <input type="text"  class="edit_input" name="volume" oninput = "value=value.replace(/[^\.\d]/g,'')">cm³
                          </div>
                          <div class="edit_item">
                              <label class="edit_label">包装方式：</label>
                              <div class="edit_input">
                                  <select name="packType" class="form-control goods_select2">
                                      <option value="">请选择</option>
                                      <c:forEach items="${packBean }" var="pack">
                                          <option value="${pack.value }">${pack.label }</option>
                                      </c:forEach>
                                  </select>
                              </div>
                          </div>
                          <div class="edit_item">
                             
                          </div>
                      </div>     
                      
                      <div class="edit_box">                        
                          <div class="edit_item">
                              <label class="edit_label">描述：</label>
                              <textarea class="edit_input" rows="1" name="describe"></textarea>
                          </div>
                          <div class="edit_item">
                          </div>
                          <div class="edit_item">
                          </div>
                      </div> 
                                       
                      <div class="form-row m-t-50">
                          <div class="col-12">
                              <div class="row">
                                  <div class="col-6">
                                      <button type="button"  class="btn btn-info waves-effect waves-light fr btn_default" id="save-buttons" onsubmit="return(chec());">保 存</button>
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
          <jsp:include page="/WEB-INF/views/modals/7011.jsp" />
        </div>
        <!-- container -->
    </div>
</div> <!-- content -->
<script src='/resources/js/system/base.js' type="text/javascript"></script>
<script type="text/javascript">
$module.customsAreaConfig.loadSelectDataIds.call($('select[name="customsAreaId"]')[0],null);

	$(function(){				
		//重新加载select2
		$(".goods_select2").select2({
			language:"zh-CN",
			placeholder: '请选择',
			allowClear:true
		});
		
		//保存按钮
		$("#save-buttons").click(function(){
			var url = "/merchandise/saveMerchandise.asyn";
			var form = $("#add-form").serializeArray();
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
			 //禁用保存按钮
			$custom.request.ajax(url, form, function(result){
				if(result.state == '200'){
					$custom.alert.success("添加成功！");
					setTimeout(function () {
						$load.a("/merchandise/toPage.html");
					}, 1000);
				}else{
					if(result.expMessage != null){
						$custom.alert.error(result.expMessage);
					}else{
						$(self).click(f);  
						$custom.alert.error("添加失败！");
					}
				}
			});
		});
		
		//取消按钮
		$("#cancel-buttons").click(function(){
			$load.a("/merchandise/toPage.html");
		});

	});


</script>
