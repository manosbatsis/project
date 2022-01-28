<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<style type="text/css">
	.not-arrow{
		padding: 5px 10px;
		border:1px solid #dcd8d8;
		-webkit-appearance:none;
		-moz-appearance:none;
		appearance:none; /*去掉下拉箭头*/
	}
	.not-arrow::-ms-expand { display: none; }
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
                       <li class="breadcrumb-item"><a href="#">客户档案</a></li>
                        <li class="breadcrumb-item"><a href="javascript:$load.a('/customerSale/toPage.html');">销售价格管理</a></li>
                        <li class="breadcrumb-item"><a href="javascript:$load.a('/customerSale/toEditPage.html');">销售价格管理编辑</a></li>
                    </ol>
                    </div>
            </div>
                  <div class="title_text">客户信息</div>
					<div class="edit_box mt20">
								 <input type='hidden' name='customerSaleId' value='${customerSale.id }'> 
						<div class="edit_item">
							<label class="edit_label"><i class="red">*</i> 客户名称：</label>
							<select class="edit_input"   id="customerId" name="customerId"  onchange="gradeChange();">
                                <option value="">请选择</option>
                            </select>
						</div>
						<div class="edit_item">
                        </div>
                          <div class="edit_item">
                          </div>
					</div>
					<div class="edit_box">
						<div class="edit_item">
							<label class="edit_label">客户编号：</label>
							<select class="not-arrow"   id="customerCode" name="customerCode" disabled="disabled" style="width: 67%;height: 30px;background-color: #EEEEEE;" >
										<option value="">${customerSale.customerCode }</option>
	                                <c:forEach items="${customerList}" var="customer">
	                                   <option value="${customer.id }">${customer.code }</option>
                                    </c:forEach>
	                       </select>
						</div>
						<div class="edit_item">
							<label class="edit_label">主数据客户ID：</label>
							<select class="not-arrow"   id="mainId" name="mainId" disabled="disabled" style="width: 67%;height: 30px;background-color: #EEEEEE;" >
							            <option value="">${customerSale.mainId }</option>
	                                <c:forEach items="${customerList}" var="customer">
	                                   <option value="${customer.id }">${customer.mainId }</option>
                                    </c:forEach>
	                       </select>
                          </div>
                          <div class="edit_item">
                          </div>
					</div>
                    <div class="col-12 ml10">
	                    <div class="row">
	                         <div class="col-10">
	                             <div class="title_text">设置商品报价</div>
	                        </div>
	                         <div class="col-1 mt10">
	                          <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick="pickGoods();">选择商品</button>
	                        </div>
	                         <div class="col-1 mt10">
	                          <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="delete-list-button">删 除</button>
	                        </div> 
	                    </div>
	                </div>                       
				    <div class="form-row mt20 ml15">
				    	<input type="hidden" id ="deleteIds" name="deleteIds" />
	                	<table id="table-list-group" class="table table-striped">
                          <thead>
		                      <tr>
		                      	   <th><input id="checkbox11" type="checkbox" name="all"></th>
		                           <th>标准条码</th>
		                           <th>商品名称</th>
		                           <th>品牌</th>
		                           <th>品类</th>
		                           <th><i class="red">*</i> 币种</th>
		                           <th><i class="red">*</i> 销售价</th>
		                           <th><i class="red">*</i> 生效日期</th>
		                           <th><i class="red">*</i> 失效日期</th>
		                           <th>是否组合</th>
		                      </tr>
		                      </thead>
			                  <c:forEach items="${ customerSalePriceList}" var="item">
			                      <tbody>
				                      <tr>
			                      		 <td style='text-align:center'>
		                      				<input type='checkbox' name='item-checkbox'>
		                      				<%-- <input type='hidden' name='goodsId' value='${item.goodsId}'/> --%>
		                      				<input type="hidden" name='id' value='${item.id}'/>
		                      			 </td>
		                      			  <td>${item.commbarcode }<input type="hidden" name="commbarcode" value="${item.commbarcode }"/></td>
		                      			  <input type='hidden' name='salePriceId' value='${item.salePriceId }'>

				                         <td>${item.goodsName }<input type="hidden" name="goodsName" value="${item.goodsName }"/></td>
			                            
			                             <td>${item.brandName }<input type="hidden" name="brandName" value="${item.brandName }"/></td>
			                             <td>${item.goodsClassifyName }<input type="hidden" name="goodsClassifyName" value="${item.goodsClassifyName }"/></td>
			                             <td>
		                                	<select name="currency" style="width: 70px ;height:25px;" >
                           						<option value="">请选择</option>
                          					</select>
                          					<input type="hidden" class="currency" value="${item.currency }">
			                              </td>
			                              <td><input type="text" name="salePrice" value="${item.salePrice }" style='width:70px;text-align:center;' onkeyup="this.value=this.value.replace(/[^\d.]/g,'')"/></td>
			                              <td><input type='text' class='date-input' name='effectiveDate' id='effectiveDate' value='<fmt:formatDate value="${item.effectiveDate }" pattern="yyyy-MM-dd" />' onchange='effectiveDateChange(this)'></td>
							              <td><input type='text' class='date-input' name='expiryDate' id='expiryDate' value='<fmt:formatDate value="${item.expiryDate }" pattern="yyyy-MM-dd" />' onchange='expiryDateChange(this)'></td>
			                              <td>
			                              	<c:choose>
                                               <c:when test="${item.isGroup =='0'}">否</c:when>
                                               <c:when test="${item.isGroup =='1'}">是</c:when>
                                          	</c:choose>			                               
			                             </td>
			                      	</tr>
			                      </tbody>
			                  </c:forEach>
			              </table>
	                  </div>
                      <div class="form-row m-t-50">
                          <div class="col-12">
                              <div class="row">
                                  <div class="col-6">
                                      <button type="button" class="btn btn-info waves-effect waves-light fr btn_default"  id="save-buttons">保 存</button>
                                  </div>
                                  <div class="col-6">
                                      <button type="button" class="btn btn-light waves-effect waves-light btn_default" id="cancel-buttons">取 消</button>
                                  </div>
                              </div>
                          </div>
                      </div>
              	 </div>
              </div>
                  <!-- end row -->
          </form>
          <jsp:include page="/WEB-INF/views/modals/8012.jsp" />
        </div>
        <!-- container -->
    </div>
<script src='/resources/js/system/base.js' type="text/javascript"></script>
<script type="text/javascript">
//加载客户的下拉数据
$module.customer.loadSelectData.call($('select[name="customerId"]')[0],'${customerSale.customerId}');

$module.currency.loadSelectData.call($("select[name='currency']"));

$(".currency").each(function(index, item){
	
	var currency = $(item).val() ;
	
	var currencySelect = $(item).prev() ;
	
	//加载销售币种的下拉数据	
	$(currencySelect).val(currency) ;
	
}) ;

function gradeChange(){
    var customerId=$("#customerId").val();
    $("#mainId").empty();
    $("#customerCode").empty();
    $.post("/customerSale/getCodeById.asyn",{'customerId':customerId},function(data){
        var dataRole = data.data.customerList;  
         var html = "";
         var html1 = "";
        for(var i=0;i<dataRole.length;i++){
            html += "<option value='"+dataRole[i].mainId
            +"'>"+dataRole[i].mainId+"</option>";
            html1 += "<option value='"+dataRole[i].code
            +"'>"+dataRole[i].code+"</option>";
        }
        $("#mainId").append(html);
        $("#customerCode").append(html1);
    },"json");
}
	$(function(){
		//重新加载select2
		$(".goods_select2").select2({
			language:"zh-CN",
			placeholder: '请选择',
			allowClear:true
		}); 
		
		//商品储存条数
		var groupCount = 1;
		
		//保存按钮
		$("#save-buttons").click(function(){
		 	var f = arguments.callee, self = this; 
			var url = "/customerSale/editCustomerPrice.asyn";
			var form = {
			}; 
			var json = {};
			var itemList = new Array();
			var flag = true;  
		 	//-------客户信息获取----------------
		 	var customerInfo = {};
		 	var customerCode = $("#customerCode").val();
			var customerId = $("#customerId").val();
			var mainId = $("#mainId").val();
			var salePriceId = $("input[name='salePriceId']").val();
			var customerSaleId= $("input[name='customerSaleId']").val();// id

			
 			customerInfo['customerCode']=customerCode;
 			customerInfo['customerId']=customerId;
 			customerInfo['mainId']=mainId;
 			customerInfo['salePriceId']=salePriceId;
 			customerInfo['customerSaleId']=customerSaleId;
 			
 			
 			json['customerInfo'] = customerInfo; 
 			//必填项校验
			if($("#customerId").val()==""){
				$custom.alert.error("请填写客户名称！");
				return ;
			}
//-------------------
			var deleteIds = $("#deleteIds").val();
			form['deleteIds']=deleteIds;
		 	$('#table-list-group tbody tr').each(function(i){// 遍历 tr
				var item ={};
				var id = $(this).find('input[name="id"]').val();
				item['id']=id;
				/* var goodsId = $(this).find('input[name="goodsId"]').val();
				item['goodsId']=goodsId; */
				var salePriceId = $(this).find('input[name="salePriceId"]').val();
				item['salePriceId']=salePriceId;
				var goodsName = $(this).find('input[name="goodsName"]').val();
				item['goodsName']=goodsName;
				/* var goodsNo = $(this).find('input[name="goodsNo"]').val();
				item['goodsNo']=goodsNo; */
				var commbarcode = $(this).find('input[name="commbarcode"]').val();
				item['commbarcode']=commbarcode;
				var salePrice = $(this).find('input[name="salePrice"]').val();
				item['salePrice']=salePrice;
				var effectiveDate = $(this).find('input[name="effectiveDate"]').val();//生效时间
				item['effectiveDate']=effectiveDate;
				var expiryDate = $(this).find('input[name="expiryDate"]').val();//失效时间
				item['expiryDate']=expiryDate;
				var isGroup = $(this).find('input[name="isGroup"]').val();
				item['isGroup']=isGroup;
				var goodsClassifyName = $(this).find('input[name="goodsClassifyName"]').val();
				item['goodsClassifyName']=goodsClassifyName;
				var brandName = $(this).find('input[name="brandName"]').val();
				item['brandName']=brandName;
				
				var currency = $(this).find('select[name="currency"]').val();//币种
				item['currency']=currency;
				if (!currency ) {
					$custom.alert.error("币种不能为空");
		       		flag = false;
		    		return;
				}
				
				

				if(!salePrice || salePrice == 0){
					$custom.alert.error("销售价格不能为空或0");
		       		flag = false;
		    		return;
			    }
				if(!effectiveDate || effectiveDate == 0){
					$custom.alert.error("请填写生效时间");
		       		flag = false;
		    		return;
			    }
				if(!expiryDate || expiryDate == 0){
					$custom.alert.error("请填写失效时间");
		       		flag = false;
		    		return;
			    }
				itemList.push(item);
			});  
			if(!flag){
				return;
			}
			if(!itemList || itemList.length==0){
				$custom.alert.error("至少选择一个商品");
				return;
			}

 			json['itemList']=itemList;
			var jsonObject= JSON.stringify(json);
			form['json']=jsonObject;
	
			$custom.request.ajax(url, form, function(result){
				if(result.state == '200'){
					$custom.alert.success("修改成功！");
					setTimeout(function () {
						$load.a("/customerSale/toPage.html");
					}, 1000);
				}else{
						$custom.alert.error("修改失败,失败原因！"+result.expMessage);
				}
			});    
		});
		
		//取消按钮
		$("#cancel-buttons").click(function(){
			$load.a("/customerSale/toPage.html");
		});
		//删除商品选中行
	 	$("#delete-list-button").click(function() {
	 		var ids = [];
	        $("input[name='item-checkbox']:checked").each(function() { // 遍历选中的checkbox
	            n = $(this).parents("tr").index()+1;  // 获取checkbox所在行的顺序
	            var id = $(this).parent().find("input[name='id']").val();
	            if(id){
	            	ids.push(id);
	            }
	            $("#table-list-group").find("tr:eq("+n+")").remove();
	        });
	        $("#deleteIds").val(ids);
	        $("input[name='all']").prop("checked",false);
	    });
		
		//全选
		$("input[name='all']").click(function(){
			//判断当前点击的复选框处于什么状态$(this).is(":checked") 返回的是布尔类型
			if($(this).is(":checked")){
				$("input[name='item-checkbox']").prop("checked",true);
			}else{
				$("input[name='item-checkbox']").prop("checked",false);
			}
		});
	});
	
	//日期格式转换
	$(".date-input").datetimepicker({
	    language:  'zh-CN',
	    format: "yyyy-mm-dd",
		autoclose: 1,
		todayHighlight: 1,
		startView: 2,
		minView: 2
	});
	//渲染 下拉框
    function selLoad(data,id){
    	$("#"+id).empty();
        var ops="<option value=''>请选择</option>";
        $.each(data,function(index,event){
        	if(event.value!=null){
            	ops+="<option value='"+event.value+"'>"+event.label+"</option>";
            }
        });
        $("#"+id).append(ops);
    }
    //选择商品(自定义选择商品的链接)
	function pickGoods(){
		$erpPopup.orderGoods.params.getMerchandisesByPageURL = '/customerSale/getSaleListByPage.asyn?r='+Math.random();
		$erpPopup.orderGoods.init(7, null, null);
	}
</script>
