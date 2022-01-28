<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

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
                           <li class="breadcrumb-item"><a href="javascript:$load.a('/reptile/toAddPage.html');">新增爬虫配置</a></li>
                    </ol>
                    </div>
            </div>
                    <!--  title   end  -->
                    <div class="edit_box mt20">
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> 使用平台：</label>
                            <select class="edit_input" name="platformType" id="platformType"></select>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> 用户名：</label>
                            <input type="text" required="" parsley-type="email" class="edit_input" name="loginName" id="loginName">
                        </div>
                        <div class="edit_item">
                            <label class="edit_label">爬取账单时点：</label>
                            <input type="text" required="" parsley-type="email" class="edit_input" name="timePoint">天
                        </div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> 公司：</label>
                            <select class="edit_input"   id="merchantId" name="merchantId" onchange="gradeChange();">
                            	 <option value="">请选择</option>
                                <c:forEach items="${merchantBean }" var="merchant">
                                    <option value="${merchant.id }">${merchant.name }</option>
                                </c:forEach>
                             </select>
                        </div>
                        <div class="edit_item">
	                            <label class="edit_label"><i class="red">*</i> 客户：</label>
	                            <select class="edit_input"   id="customerId" name="customerId" >
	                                <option value="">选择客户</option>
		                                <c:forEach items="${customerList}" var="customer">
		                                   <option value="${customer.value }">${customer.label }</option>
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
		                            <select class="edit_input"   id="outDepotId" name="outDepotId" >
		                            </select>
	                    </div>
	                    <div class="edit_item">
		                            <label class="edit_label"><i class="red">*</i>入仓库：</label>
		                            <select class="edit_input"   id="inDepotId" name="inDepotId">
		                            </select>
	                    </div>
	                    <div class="edit_item">

	                    </div> 
                 </div>
                  
                 </div>
                    <div class="form-row m-t-50">
                        <div class="col-12">
                            <div class="row">
                                <div class="col-6">
                                    <input type="button" class="btn btn-info waves-effect waves-light fr btn_default" id="save-buttons" value="保 存"/>
                                </div>
                                <div class="col-6">
                                    <button type="button" class="btn btn-light waves-effect waves-light btn_default" id="cancel-buttons">取 消</button>
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
    //平台
    $module.constant.getConstantListByNameURL.call($('select[name="platformType"]')[0],"crawler_typeList",null);
    function gradeChange(){
         //加载仓库
        var merchantId=$("#merchantId").val();
        $module.depot.getSelectBeanByMerchantRel.call($("select[name='outDepotId']")[0],null,{"merchantId":merchantId});
        $module.depot.getSelectBeanByMerchantRel.call($("select[name='inDepotId']")[0],null,{"merchantId":merchantId});

        $("#customerId").empty();
        $.post("/reptile/queryReptile.asyn",{'merchantId':merchantId},function(data){
            var dataRole = data.data.customerList;
            var html = "<option value=''>请选择关联客户</option>";
            for(var i=0;i<dataRole.length;i++){
                html += "<option value='"+dataRole[i].value +"'>"+dataRole[i].label+"</option>";
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
		
		//保存按钮
		$("#save-buttons").off().on('click',function() {
			var url = "/reptile/saveReptile.asyn";	
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
			
			// 禁用点击事件
			$("#save-buttons").attr("disabled",true);
			$custom.request.ajax(url, form, function(result){
				if(result.data.code == '00'){
					$custom.alert.success("新增成功！");
					setTimeout(function () {
						$load.a("/reptile/toPage.html");
					}, 1000); 
				}else{
					$custom.alert.error(result.data.message);
				}
				$("#save-buttons").removeAttr("disabled");
			});
		});
		
		//返回
		$("#cancel-buttons").click(function(){
			$load.a("/reptile/toPage.html");
		});
	});
	
	function changekey(obj){
		  if(obj.checked){
		    $("#proxyId").show();
		  }else{
		    $("#proxyId").hide();
		  }
	}

</script>
