<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page isELIgnored="false" %>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
          <form id="add-form">
           <div class="col-12">
              <div class="card-box" >
                                         <!--  title   start  -->
              <div class="col-12">
                <div>
                   <div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                       <li><i class="block"></i> 当前位置：</li>
                           <li class="breadcrumb-item"><a href="javascript:$load.a('/reptile/toPage.html');">爬虫配置</a></li>
                           <li class="breadcrumb-item"><a href="javascript:$load.a('/reptile/toEditPage.html?id=${detail.id }');">编辑爬虫配置</a></li>
                    </ol>
                    </div>
            </div>
                    <!--  title   end  -->
                    <div class="edit_box">
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> 使用平台：</label>
                            <select class="edit_input" name="platformType" id="platformType"></select>
                            <input type="hidden" value="${detail.id }" name="id">
                        </div>
                         <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> 用户名：</label>
                            <input type="text" required="" parsley-type="email" class="edit_input" id="loginName" name="loginName" value="${detail.loginName }">
                        </div>
                        <div class="edit_item">
                            <label class="edit_label">爬取账单时点：</label>
                            <input type="text" required="" parsley-type="email" class="edit_input" name="timePoint" value="${detail.timePoint }">天
                        </div>
                    </div>
                    <div class="edit_box">
                       
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> 公司：</label>
                            <select class="edit_input"   id="merchantId" name="merchantId"  onchange="gradeChange();">
                                <option value="">请选择</option>
                                <c:forEach items="${merchantBean }" var="merchant">                            
                                <c:choose>
                                        <c:when test="${detail.merchantId eq merchant.id }">
                                            <option value="${merchant.id }" selected>${merchant.name }</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option value="${merchant.id }">${merchant.name }</option>
                                        </c:otherwise>
                              </c:choose>
                                </c:forEach>
                            </select>
                        </div>
                          <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> 客户：</label>                       
                            <select class="edit_input"   id="customerId" name="customerId" >  	  	
                                <c:forEach items="${customerList }" var="customer">	
                                    <c:choose>
                                        <c:when test="${detail.customerId == customer.value }">
                                            <option value="${customer.value }" selected>${customer.label }</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option value="${customer.value }">${customer.label }</option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="edit_item">
                        	<label class="edit_label"> 店铺名称：</label>
                            <select class="edit_input"   id="shopId" name="shopId" >
                             </select>
                        </div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item">
		                            <label class="edit_label"><i class="red">*</i>出仓库：</label>
		                            <select class="edit_input"   id="outDepotId" name="outDepotId" onchange="reloadBu();">
		                            </select>
	                    </div>
	                    <div class="edit_item">
		                            <label class="edit_label"><i class="red">*</i>入仓库：</label>
		                            <select class="edit_input"   id="inDepotId" name="inDepotId" onchange="reloadBu();">
		                            </select>
	                    </div>
	                    <div class="edit_item">

	                    </div> 
                    </div>
                      <div class="form-row m-t-50">
                          <div class="col-12">
                              <div class="row">
                                  <div class="col-6">
                                      <input type="button" class="btn btn-info waves-effect waves-light fr btn_default" id="save-buttons" value="保存"/>
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
                               <!--======================Modal框===========================  -->
                  <!-- end row -->
           </div>
          </div>
          </form>
        </div>
        <!-- container -->
    </div>

 <!-- content -->
 <script src='/resources/js/system/base.js' type="text/javascript"></script>
<script type="text/javascript">
	var platformType = "${detail.platformType}";
	var merchantId = "${detail.merchantId}";
	var customerId = "${detail.customerId}";
	var shopId = "${detail.shopId}";
	var outDepotId = "${detail.outDepotId}";
	var inDepotId = "${detail.inDepotId}";
	$module.constant.getConstantListByNameURL.call($('select[name="platformType"]')[0],"crawler_typeList",platformType);
	$module.depot.getSelectBeanByMerchantRel.call($("select[name='outDepotId']")[0],outDepotId,{"merchantId":merchantId});
	$module.depot.getSelectBeanByMerchantRel.call($("select[name='inDepotId']")[0],inDepotId,{"merchantId":merchantId});
	
	initShop(merchantId, shopId) ;
	
	function gradeChange(){
	    var merchantId=$("#merchantId").val();
		$module.depot.getSelectBeanByMerchantRel.call($("select[name='outDepotId']")[0],null,{"merchantId":merchantId});
		$module.depot.getSelectBeanByMerchantRel.call($("select[name='inDepotId']")[0],null,{"merchantId":merchantId});
	    $("#customerId").empty();
	    $.post("/reptile/queryReptile.asyn",{'merchantId':merchantId},function(data){
	        var dataRole = data.data.customerList;  
	        var html = "<option value=''>请选择客户</option>";
	        console.log(dataRole)
	        for(var i=0;i<dataRole.length;i++){
	            html += "<option value='"+dataRole[i].value
	            +"'>"+dataRole[i].label+"</option>";
	        }
	        $("#customerId").append(html);
	    },"json");
	}
	
	$("#merchantId").change(function(){
		
		var merchantId=$("#merchantId").val();

		if(!merchantId){
			return ;
		}
		
		$custom.request.ajax("/reptile/getShopList.asyn", {"merchantId": merchantId}, function(result){
			if(result.state == '200'){
				
				if(result.data){
					
					var html = "<option value=''>请选择</option>" ;
					
					$(result.data).each(function(index, item){
						html += "<option value='" + item.id + "'>"+ item.shopName +"</option>" ;
					}) ;
					
					$("#shopId").html(html) ;
				}
				
				
			}else{
				$custom.alert.error(result.data.message);
			}
		});
		
	}) ;
	
	$(function(){
		
		if($("#hiddenisProxy").val()=='1'){
			$("#proxyId").css("display","block");
		}else{
			$("#proxyId").css("display","none");
		}
	
	
		//保存按钮
		$("#save-buttons").off().on('click',function() {
			var url = "/reptile/modifyReptile.asyn";
			var form = $("#add-form").serializeArray();
			//必填项校验
			if($("#platformType").val()==""){
				$custom.alert.error("请选择使用平台");
				return ;
			}
			if($("#loginName").val()==""){
				$custom.alert.error("请输入用户名");
				return ;
			}
			if($("#merchantId").val()==""){
				$custom.alert.error("请选择公司");
				return ;
			}
			if($("#customerId").val()==""){
				$custom.alert.error("请选择客户");
				return ;
			}
			if($("#outDepotId").val()==""){
				$custom.alert.error("请选择出仓库");
				return ;
			}
			if($("#inDepotId").val()==""){
				$custom.alert.error("请选择入仓库");
				return ;
			}
			
			$custom.request.ajax(url, form, function(result){
				if(result.data.code == '00'){
					$custom.alert.success("编辑成功！");
					setTimeout(function () {
						$load.a("/reptile/toPage.html");
					}, 1000);
				}else{
					$custom.alert.error(result.data.message);
				}
			});
		});
		
		//返回
		$("#cancel-buttons").click(function(){
			$load.a("/reptile/toPage.html");
		});
	});

	function initShop(merchantId, shopId){
		$custom.request.ajax("/reptile/getShopList.asyn", {"merchantId": merchantId}, function(result){
			if(result.state == '200'){
				
				if(result.data){
					
					var html = "<option value=''>请选择</option>" ;
					
					$(result.data).each(function(index, item){
						
						var selected = "" ;
						
						if(item.id == shopId){
							selected = "selected" ;
						}
						
						html += "<option value='" + item.id + "' "+ selected +">"+ item.shopName +"</option>" ;
					}) ;
					
					$("#shopId").html(html) ;
				}
				
				
			}else{
				$custom.alert.error(result.data.message);
			}
		});
	}

</script>
