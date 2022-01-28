<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="/WEB-INF/views/common.jsp" />
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
                       <li class="breadcrumb-item"><a href="#">邮件管理</a></li>
                       <li class="breadcrumb-item"><a href="javascript:$load.a('/settlementPriceWarnconfig/toPage.html');">单价预警配置列表</a></li>
                       <li class="breadcrumb-item"><a href="javascript:$load.a('/settlementPriceWarnconfig/toAddPage.html');">单价预警配置新增</a></li>
                    </ol>
                    </div>
            </div>
            	<form id="add-form">
                    <!--  title   end  -->
                    <div class="title_text">单价预警配置</div>
                    <div class="info_box">
                      <div class="form-row">
                          <div class="col-4">
                              <div class="row">
                                  <label  class="col-4 col-form-label " style="white-space:nowrap;"><div class="fr"><i class="red">*</i>公司<span>：</span></div></label>
                                  <div class="col-8 ml10">
                                      <select id="merchantId" name="merchantId" class="form-control" >
                                      <option value=''>请选择</option>
                                      <c:forEach items="${merchantList }" var="merchant">
                                            <option value="${merchant.value }" <c:if test="${merchant.value==detail.merchantId }">selected = 'selected'</c:if>>${merchant.label }</option>
                                        </c:forEach>
                                    </select>
                                  </div>
                              </div>
                          </div>
                          <div class="col-5">
                                 <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr"><i class="red">*</i>事业部<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <select class="edit_input" name="buId" id="buId" parsley-trigger="change" required>
                                      <option value=''>请选择</option>
                                      </select>
                                  </div>
                              </div>
                          </div>
                      </div>
                    <div class="form-row">
                          <div class="col-4">
                              <div class="row">
                                  <label  class="col-4 col-form-label " style="white-space:nowrap;"><div class="fr"><i class="red">*</i>波动范围<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <input type="text" required="" class="form-control" id="waveRange" name="waveRange" >                   
                                  </div>
                                 <span style="font-weight: bold;font-size:16px">%</span>
                              </div>
                          </div>
                          <div class="col-5"></div>
                      </div>
                      </div>
                       <div class="title_text">邮件提醒设置</div>
                      <div class="form-row">
                          <div class="col-4">
                              <div class="row">
                                  <label  class="col-4 col-form-label " style="white-space:nowrap;"><div class="fr"><i class="red">*</i>邮件主题<span>：</span></div></label>
                                  <div class="col-8 ml10">
                                        <input type="text" required="" class="form-control" id="emailTitle" name="emailTitle" >          
                                  </div>
                              </div>
                          </div>
                          <div class="col-5"></div>
                      </div>
                      <div class="form-row">
                          <div class="col-4">
                              <div class="row">
                                  <label  class="col-4 col-form-label " style="white-space:nowrap;"><div class="fr"><i class="red">*</i>收件人邮箱<span>：</span></div></label>
                                  <div class="col-8 ml10">
                                   <shiro:hasPermission name="settlement_price_warnconfig_toAddPage">
		                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="chooseUser" onclick="$m18011.init()">选择用户</button>
		                            </shiro:hasPermission>
                                        <textarea style="width: 800px;height:260px;word-break: break-all;" id="receiverEmail" ></textarea>
                                        <input type="hidden"  id="receiverId" name="receiverId" />
                                        <input type="hidden"  id="receiverName" name="receiverName" />
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
            <!-- 授予用户 -->
            <jsp:include page="/WEB-INF/views/modals/18011.jsp" />
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->

<script src='/resources/js/system/base.js' type="text/javascript"></script>
<script type="text/javascript">
var userType="${userType}";
if(userType!=1){
	 //加载事业部
	$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null,null,null,null); 
}
	$(function(){
		
		//重新加载select2
		$(".goods_select2").select2({
			language:"zh-CN",
			placeholder: '请选择',
			allowClear:true
		});
		
		//取消按钮
		$("#cancel-buttons").click(function(){
			 $module.load.pageReport('act=15001');
		});
		 //改变商家
		$("select[name='merchantId']").change(function(){
			 var merchantId=$("select[name='merchantId']").val();
			//加载事业部
			 $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null,merchantId,null,null);
		 });
		//保存按钮
		$("#save-buttons").click(function(){
			
			var url =serverAddr+ "/settlementPriceWarnconfig/saveSettlementPriceWarnconfig.asyn";
			var form = $("#add-form").serializeArray();
			//必填项校验
			  var merchantId= $("#merchantId").val();
				if(!merchantId){
					$custom.alert.error("公司不能为空");
					return ;
				} 	
				var buId= $("#buId").val();
				if(!buId){
					$custom.alert.error("事业部不能为空！");
					return ;
				}
				var waveRange= $("#waveRange").val();
				if(!waveRange){
					$custom.alert.error("波动范围不能为空！");
					return ;
				}
				var emailTitle= $("#emailTitle").val();
				if(!emailTitle){
					$custom.alert.error("邮件主题不能为空！");
					return ;
				}
				var receiverEmail= $("#receiverEmail").val();
				if (!receiverEmail) {
					$custom.alert.error("收件人邮箱不能为空！");
					return ;
				}
			var receiverId= $("#receiverId").val();
			var receiverName= $("#receiverName").val();
			var merchantName=$('#merchantId option:selected').text(); // 文本值
			var buName= $('#buId option:selected').text(); // 文本值
			var data_merchant_name ={name:"merchantName",value:merchantName};
			var data_customer_name ={name:"buName",value:buName};
			var data_receiver_email ={name:"receiverEmail",value:receiverEmail};
			form.push(data_merchant_name);
			form.push(data_customer_name);
			form.push(data_receiver_email);
			$custom.request.ajax(url, form, function(result){
				if(result.state == '200'){
					if (result.data.code=='00') {
						$custom.alert.success("新增成功！");
						setTimeout(function () {
							$module.load.pageReport('act=15001');
						}, 1000);
					}else{
						$custom.alert.error(result.data.message);
					}
					
				}else{
					if(result.expMessage != null){
						$("#customerId").find("option").remove();
						$custom.alert.error(result.expMessage);
				}else{
					$custom.alert.error("新增失败！");
				}
				}
			});
		});		
	});
	// 时间插件
    laydate.render({
        elem: '#sendDate	', //指定元素
        type: 'datetime',
        format: 'yyyy-MM-dd HH:mm:ss',
        ready: function () {
            $('.laydate-btns-time').remove();
        }
    });

</script>
