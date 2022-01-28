<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<style>
.addressInput{
	float: left;
    width: 85%;
    margin-inline-end: 10px;
    margin-block-end: 10px;
}
.addressBotton{
	float: left;
}

</style>
<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
          <div class="row">
           <div class="col-12">
              <div class="card-box" >
                   <!--  title   start  -->
              <div class="col-12">
                <div>
                   <div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                       <li><i class="block"></i> 当前位置：</li>
                       <li class="breadcrumb-item"><a href="#">邮件提醒</a></li>
                       <li class="breadcrumb-item"><a href="javascript:$load.a('/email/toPage.html');">邮件提醒列表</a></li>
                       <li class="breadcrumb-item"><a href="javascript:$load.a('/email/toAddPage.html');">邮件提醒修改</a></li>
                    </ol>
                    </div>
            </div>
            	<form id="add-form">
                    <!--  title   end  -->
                    <div class="title_text">邮件提醒配置信息</div>
                    <div class="info_box">
                    <div class="form-row">
                          <div class="col-4">
                              <div class="row">
                                  <label  class="col-4 col-form-label " style="white-space:nowrap;"><div class="fr"><i class="red">*</i>公司<span>：</span></div></label>
                                  <div class="col-8 ml10">
                                      <input type="hidden" required="" parsley-type="email" class="form-control"  id="id" name="id" value="${detail.id }">                                  
                                      <select id="merchantId" name="merchantId" class="form-control" disabled="disabled" >
                                        <option value="">请选择</option>
                                          <c:forEach items="${merchantSelectBean }" var="merchant">
                                        <option value="${merchant.value }" <c:if test="${detail.merchantId == merchant.value}"> selected = 'selected'</c:if> >${merchant.label }</option>
                                    </c:forEach> 
                                    </select>
                                  </div>
                              </div>
                          </div>
                          <div class="col-5">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr"><i class="red">*</i>供应商<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <select id="customerId" name="customerId" class="form-control"  disabled="disabled" >
                                        <option value="">请选择供应商</option>
                                          <c:forEach items="${customerSelectBean }" var="customer">
                                        <option value="${customer.value }" <c:if test="${detail.customerId == customer.value}"> selected = 'selected'</c:if>>${customer.label }</option>
                                    </c:forEach> 
                                    </select>
                                  </div>
                              </div>
                          </div>
                      </div>
                      </div>
                      <div class="form-row">
                          <div class="col-4">
                              <div class="row">
                                  <label  class="col-4 col-form-label " style="white-space:nowrap;"><div class="fr"><i class="red">*</i>提醒类型<span>：</span></div></label>
                                  <div class="col-8 ml10">
                                      <select class="form-control" name="reminderType" id="reminderType">
                                          <option value="">请选择</option>
                                          <option value="1" <c:if test="${detail.reminderType == '1'}"> selected = 'selected'</c:if>>付款提醒</option>
                                      </select>
                                  </div>
                              </div>
                          </div>
                          <div class="col-5">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr"><i class="red">*</i>基准时间<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <select class="form-control" name="baseTimeType" id="baseTimeType">
                                          <option value="">请选择</option>
                                          <option value="1" <c:if test="${detail.baseTimeType == '1'}"> selected = 'selected'</c:if>>发票时间</option>
                                      </select>
                                  </div>
                              </div>
                          </div>
                      </div>
                        <div class="form-row">
                         <div class="col-4">
                              <div class="row">
                                  <label  class="col-4 col-form-label " style="white-space:nowrap;"><div class="fr"><i class="red">*</i>付款账期<span>：</span></div></label>
                                  <div class="col-8 ml10">
                                    <input type="text" required="" parsley-type="email" class="form-control" id="accountPeriodDay" name="accountPeriodDay" value="${detail.accountPeriodDay }">                                     
                                  </div>
                              </div>
                          </div>
                          <div class="col-5">
                              <div class="row">
                                  <label  class="col-5 col-form-label " style="white-space:nowrap;"><div class="fr"><i class="red">*</i>账期单位<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <input type="radio"  id="accountUnitType" name="accountUnitType"  value="1"  <c:if test="${detail.accountUnitType == '1'}"> checked</c:if>>自然日
                      				  <input type="radio"  id="accountUnitType"  name="accountUnitType"  value="2"  <c:if test="${detail.accountUnitType == '2'}"> checked</c:if>>工作日                                 
                                  </div>
                              </div>
                          </div>
                      </div>
                      <div class="form-row">
                          <div class="col-4">
                              <div class="row">
                                  <label  class="col-4 col-form-label " style="white-space:nowrap;"><div class="fr"><i class="red">*</i>提前天数提醒<span>：</span></div></label>
                                  <div class="col-8 ml10">
                                      <input type="text" required="" parsley-type="email" class="form-control"  id="advanceReminderDay" name="advanceReminderDay" value="${detail.advanceReminderDay }">
                                  </div>
                              </div>
                          </div>
                          <div class="col-5">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr"><i class="red">*</i>提醒单位<span>：</span></div></label>
                                  <div class="col-7 ml10">
 									<input type="radio"  id="reminderUnitType" name="reminderUnitType"  value="1"  <c:if test="${detail.reminderUnitType == '1'}"> checked</c:if>>自然日
                      				<input type="radio"  id="reminderUnitType"  name="reminderUnitType"  value="2"  <c:if test="${detail.reminderUnitType =='2'}"> checked</c:if>>工作日                                           
                      			  </div>
                              </div>
                          </div>
                       </div>

                    	</div>
                      </div>
                      </form>

                      <div class="form-row m-t-50">
                          <div class="col-12">
                              <div class="row">
                                  <div class="col-6">
                                      <input type="button" class="btn btn-info waves-effect waves-light fr btn_default" id="save-buttons" value="保 存" />
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
          <!--======================Modal框===========================  -->
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->
<script src='/resources/js/system/base.js' type="text/javascript"></script>
<script type="text/javascript">
	$(function(){
		
		//重新加载select2
		$(".goods_select2").select2({
			language:"zh-CN",
			placeholder: '请选择',
			allowClear:true
		});
		
		//取消按钮
		$("#cancel-buttons").click(function(){
			$load.a("/email/toPage.html");
		});
		
		// 根据公司id获取 供应商列表
		$("#merchantId").change(function(){
			$("#customerId").find("option").remove();
			var merchantId= $("#merchantId").val();
			var url = "/email/getSelectBeanBySupplier.asyn?r="+Math.random();
			var data = {"merchantId":merchantId};
			$custom.request.ajax(url, data, function(result){
				var dataSupplier =result.data; 
		        var html = "<option value=''>请选择供应商</option>";
		        for(var i=0;i<dataSupplier.length;i++){
		            html += "<option value='"+dataSupplier[i].value
		            +"'>"+dataSupplier[i].label+"</option>";
		        }
		        $("#customerId").append(html);
			});
			
	    })
		//保存按钮
		$("#save-buttons").click(function(){
			var url = "/email/modifyEmail.asyn";
			var form = $("#add-form").serializeArray();
			//必填项校验
			/* var merchantId= $("#merchantId").val();
			if(!merchantId){
				$custom.alert.error("公司不能为空");
				return ;
			} */	
			/* var customerId= $("#customerId").val();
			if(!customerId){
				$custom.alert.error("供应商不能为空！");
				return ;
			} */
			
			var reminderType= $("#reminderType").val();
			if(!reminderType){
				$custom.alert.error("提醒类型不能为空！");
				return ;
			}
			var baseTimeType= $("#baseTimeType").val();
			if(!baseTimeType){
				$custom.alert.error("基准时间不能为空！");
				return ;
			}
			var accountPeriodDay= $("#accountPeriodDay").val();
			if(!accountPeriodDay){
				$custom.alert.error("付款账期不能为空！");
				return ;
			}
			
			var ex = /^\d+$/;
			if (!ex.test(accountPeriodDay)) {
				$custom.alert.error("付款账期必须为正整数！");
				return ;
			}
			if (accountPeriodDay>360) {
				$custom.alert.error("付款账期不能大于360天");
				return ;
			}
			
			var accountUnitType= $("#accountUnitType").val();
			if(!accountUnitType){
				$custom.alert.error("账期单位不能为空！");
				return ;
			}
			var advanceReminderDay= $("#advanceReminderDay").val();
			if(!advanceReminderDay){
				$custom.alert.error("提前提醒天数不能为空！");
				return ;
			}
			
			var advanceReminderDayList = advanceReminderDay.split(',');
			for(var i  in advanceReminderDayList){
				var advanceReminder=advanceReminderDayList[i];
				if (!ex.test(advanceReminder)) {
					$custom.alert.error("提前提醒天数必须为正整数！");
					return ;
				}
				if (advanceReminder>360) {
					$custom.alert.error("提前提醒天数不能大于360！");
					return ;
				}
			}
			
			var reminderUnitType= $("#reminderUnitType").val();
			if(!reminderUnitType){
				$custom.alert.error("提醒单位不能为空！");
				return ;
			}
			var id= $("#id").val();
			if(!id){
				$custom.alert.error("邮件配置表id不能为空！");
				return ;
			}
			/* var merchantName=$('#merchantId option:selected').text(); // 文本值
			var customerName= $('#customerId option:selected').text(); // 文本值
			var data_merchant_name ={name:"merchantName",value:merchantName};
			var data_customer_name ={name:"customerName",value:customerName};
			form.push(data_merchant_name);
			form.push(data_customer_name); */
			$custom.request.ajax(url, form, function(result){
				console.log(result);
				if(result.state == '200'){
					if (result.data.code=='00') {
						$custom.alert.success("修改成功！");
						setTimeout(function () {
							$load.a("/email/toPage.html");
						}, 1000);
					}else{
						$custom.alert.error(result.data.message);
					}
					
				}else{
					if(result.expMessage != null){
						$custom.alert.error(result.expMessage);
				}else{
					$custom.alert.error("修改失败！");
				}
				}
			});
		});		
	});


</script>
