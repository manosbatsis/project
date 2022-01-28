<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<style>
.addressInput{
	float: left;
    width: 65%;
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
          <form id="edit-form">
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
                       <li class="breadcrumb-item"><a href="#">仓库档案</a></li>
                       <li class="breadcrumb-item"><a href="javascript:$load.a('/depot/toPage.html');">仓库列表</a></li>
                       <li class="breadcrumb-item"><a href="javascript:$load.a('/depot/toEditPage.html?id=${detail.id }');">编辑仓库</a></li>
                    </ol>
                    </div>
            	</div>
            <div class="title_text">仓库信息</div>
            	   <div class="form-row mt20 ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr"><i class="red">*</i>仓库自编码<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                                <input type="text" required="" parsley-type="text" class="form-control" name="depotCode" id="depotCode" value="${detail.depotCode }"disabled="disabled"  onkeyup="this.value=this.value.replace(/^ +| +$/g,'')">
                            	<input type="hidden" value="${detail.id }" id="id" name="id">
                            </div>
                        </div>
                    </div>
                    <div class="col-7">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr"><i class="red">*</i>OP仓库编号<span>：</span></span></label>
                            <div class="col-5 ml10 line">
                                <input type="text" required="" parsley-type="text" class="form-control" name="code" id="code" value="${detail.code }" disabled="disabled"    onkeyup="this.value=this.value.replace(/^ +| +$/g,'')">
                            </div>
                        </div>
                    </div>
                </div>
              <div class="form-row ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr"><i class="red">*</i>仓库名称<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                                <input type="text" required="" parsley-type="text" class="form-control" name="name" id="name" value="${detail.name }">
                            </div>
                        </div>
                    </div>
                    <div class="col-7">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr"><i class="red">*</i>仓库类别<span>：</span></span></label>
                            <div class="col-5 ml10 line">
                            	<select class="form-control" name="type" id="type">
                                <option value="">请选择</option>
                                <option value="a" <c:if test="${detail.type eq 'a' }">selected</c:if>>保税仓</option>
                                <option value="b" <c:if test="${detail.type eq 'b' }">selected</c:if>>备查库</option>
                                <option value="c" <c:if test="${detail.type eq 'c' }">selected</c:if>>海外仓</option>
                                <option value="d" <c:if test="${detail.type eq 'd' }">selected</c:if>>中转仓</option>
                                <option value="e" <c:if test="${detail.type eq 'e' }">selected</c:if>>暂存区</option>
                                <option value="f" <c:if test="${detail.type eq 'f' }">selected</c:if>>销毁区</option>
                                <option value="g" <c:if test="${detail.type eq 'g' }">selected</c:if>>内贸仓</option>
                            </select>
                            </div>
                        </div>
                    </div>
                </div>
            
            <div class="form-row ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr"><i class="red">*</i>仓库类型<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                                <select class="form-control" name="depotType" id="depotType">
                                    <option value="1" <c:if test="${detail.depotType eq '1'}">selected</c:if>>仓库(BBC)</option>
                                    <option value="2" <c:if test="${detail.depotType eq '2'}">selected</c:if>>场站(BC)</option>
                                    <option value="3" <c:if test="${detail.depotType eq '3'}">selected</c:if>>其他</option>
                            	</select>
                            </div>
                        </div>
                    </div>
                    <div class="col-7">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><i class="red bonded" style="display:none;">*</i>申报海关编号：</label>
                            <div class="col-5 ml10 line">
                                <input type="text" required="" parsley-type="text" class="form-control" name="customsNo" id="customsNo" value="${detail.customsNo }" >
                            </div>
                        </div>
                    </div>
                </div>
                  <div class="form-row ml15">
                    <div class="col-5">
                        <div class="row">
                           <label class="col-4 col-form-label " style="white-space:nowrap;"><i class="red bonded" style="display:none;">*</i>申报国检编号：</label>
                            <div class="col-8 ml10 line">
                                <input type="text" required="" parsley-type="text" class="form-control" name="inspectNo" id="inspectNo" value="${detail.inspectNo }">
                            </div>
                        </div>
                    </div>
                    <div class="col-7">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">仓管员<span>：</span></span></label>
                            <div class="col-5 ml10 line">
                                <input type="text" required="" parsley-type="text" class="form-control" name="adminName" id="adminName" value="${detail.adminName }" >
                            </div>
                        </div>
                    </div>
                </div>
                  <div class="form-row ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">联系电话<span>：</span></span></label>
                            <div class="col-5 ml10 line">
                                <input type="text" required="" parsley-type="text" class="form-control" name="tel" id="tel" value="${detail.tel }"onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="11">
                            </div>
                        </div>
                    </div>
                    <div class="col-7">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">仓库所在国家<span>：</span></span></label>
                            <div class="col-5 ml10 line">
                                <input type="text" required="" parsley-type="text" class="form-control" name="country" id="country" value="${detail.country }" >
                            </div>
                        </div>
                    </div>
                </div>
                  <div class="form-row ml15" >
                      <div class="col-5">
                          <div class="row">
                              <label class="col-4 col-form-label" style="white-space:nowrap;"><div class="fr"><i class="red">*</i>ISV仓库类型<span>：</span></div></label>
                              <div class="col-8 ml10">
                                  <select class="form-control" name="ISVDepotType" id="ISVDepotType">
                                  </select>
                              </div>
                          </div>
                      </div>
                    <div class="col-7">
                        <div class="row">
                            <label class="col-2 col-form-label " style="white-space:nowrap;"></label>
                            <div class=" ml10 line">
                                <c:choose>
                                <c:when test="${detail.isTopBooks eq '1' }">
                                    <input type="checkbox" name="isTopBooks" value="1"  checked>是否代销仓
                                </c:when>
                                <c:otherwise>
                                    <input type="checkbox" name="isTopBooks" value="1" >是否代销仓
                                </c:otherwise>
                            </c:choose>
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <c:choose>
                                <c:when test="${detail.status eq '1' }">
                                    <input type="checkbox" name="status" value="1" checked>是否启用
                                </c:when>
                                <c:otherwise>
                                    <input type="checkbox" name="status" value="1">是否启用
                                </c:otherwise>
                            </c:choose>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="edit_box">
                        <label class="edit_label" style="flex: 0.6">仓库详细地址：</label>
                        <div id="depotAddress" style="flex: 4">
                        	<c:if test="${empty detail.address}">
                        		<input class="form-control addressInput" rows="5" name="address"  placeholder="请输入省市区地址" />
                        		<button type="button" class="btn btn-find waves-effect waves-light btn_default addressBotton" id="add-address" style="display:none;">添加</button>
                        	</c:if>
                        	<c:if test="${not empty detail.address}">
                            <c:forEach items="${detail.address}" var="item" varStatus="status">
					            <c:set value="${fn:split(item, ',')}" var="arr" />
					            <c:forEach items="${ arr }" var="s">
					                <input class="form-control addressInput" rows="5" name="address" value="${s }"/>
					                <c:if test="${status.first}">
                                    	<button type="button" class="btn btn-find waves-effect waves-light btn_default addressBotton" id="add-address">添加</button>
					            	</c:if>
					                <c:if test="${!status.first}">
                                    	<button type="button" class="btn btn-find waves-effect waves-light btn_default del-address addressBotton" style="float:left;">删除</button>
					            	</c:if>
					            </c:forEach>
					        </c:forEach>
					        </c:if>
                        </div>
                    </div>
                    
                     <div class="form-row" style="padding-left: 50px;">
                            <div class="col-10 ml30">
                                <div class="row">
                                    <label class="col-1 col-form-label " style="white-space:nowrap;">
                                        <div class="fr">是否代客管理仓库<span>：</span></div>
                                    </label>
                                    <div  class="col-11 ml10">
                                       <div class="col-8" style="padding-top: 7px;">
                                            <input style="margin-left: 15px;" type="checkbox" id="isValetManage_checked" name="isValetManage_checked">是
                                        </div>
                                    </div>
                                </div>
                            </div>
                    </div>
                    
                      <!--  关联公司 开始 -->
                   <!--  关联公司 结束 -->
                    <div class="title_text">流程设置</div>
                    &nbsp;&nbsp;&nbsp;&nbsp;
                        <div class="edit_box">
                        <div class="edit_item">
                            入仓下推指令appkey：
                            <input type="hidden" value="${detail.isMerchant }" id="hiddenIsMerchant">
                            <c:choose>
                                <c:when test="${detail.isMerchant eq '1' }">
                                    <input type="radio"  name="isMerchant"  value="1"  onclick="changekey(1);" checked>公司key
                                    <input style="margin-left: 10px;" type="radio"  name="isMerchant"  value="2"  onclick="changekey(2);">关联公司key
                                </c:when>
                                <c:otherwise>
                                    <input type="radio"  name="isMerchant"  value="1"  onclick="changekey(1);">公司key
                                    <input style="margin-left: 10px;" type="radio"  name="isMerchant"  value="2"  onclick="changekey(2);" checked>关联公司key
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"></label>
                            <div id="key-div" class="edit_input" style="text-indent: 0">
                                <select class="form-control goods_select2"   id="merchantId" name="merchantId">
                                    <option value="">选择关联公司</option>
                                    <c:forEach items="${merchantBean }" var="merchant">
                                        <c:choose>
                                            <c:when test="${merchant.id eq detail.merchantId }">
                                                <option value="${merchant.id }" selected>${merchant.name }</option>
                                            </c:when>
                                            <c:otherwise>
                                                <option value="${merchant.id }" >${merchant.name }</option>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="edit_item"></div>
                    </div>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    
                    <div class="form-row">
                       <div class="col-5">
                           <div class="row">
                               <label class="col-4 col-form-label" style="white-space:nowrap;">
                                   <div><i class="red">*</i>是否批次效期强校验<span>：</span></div>
                               </label>
                               <div class="col-8" style="padding-top: 7px;">
                               <select class="form-control" name="batchValidation" id="batchValidation">
                               </select>
                               </div>
                           </div>
                      </div>                 
                 </div>
                    
                    <div class="edit_box">                   
                         <div class="edit_item">
                         	<a href="javascript:$m9011.init(${detail.id });" >变更记录</a>
                         </div>
                    </div>

								<div class="col-12 ml10">
									<div class="row">
										<div class="col-1 mt10">
											<button type="button"
												class="btn btn-find waves-effect waves-light btn_default"
												id="addRel">新增</button>
										</div>
										<div class="col-1 mt10">
											<button type="button"
												class="btn btn-find waves-effect waves-light btn_default"
												id="deleteRel">删 除</button>
										</div>
									</div>
								</div>

								<div class="form-row mt20 ml15">
									<table id="table-list-group" class="table table-striped"
										cellspacing="0" width="100%">
										<thead>
											<tr>
												<th><input id="checkbox11" type="checkbox" name="all"></th>
												<th><i class="red">*</i>平台关区</th>
												<th>电商账册号</th>
												<th>收货人公司名称（中文）</th>
												<th>收货人公司名称（英文）</th>
												<th>仓库地址</th>
											</tr>
										</thead>
										<tbody>
										</tbody>
									</table>
								</div>



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
                  <!-- ================弹框================= -->
              		<jsp:include page="/WEB-INF/views/modals/9011.jsp" />
              </div>
          </div>
          </form>
        </div>
        <!-- container -->
    </div>

 <!-- content -->
 <script src='/resources/js/system/base.js' type="text/javascript"></script>
<script type="text/javascript">
    var ISVDepotType = "${detail.ISVDepotType}";
    $module.constant.getConstantListByNameURL.call($('select[name="ISVDepotType"]')[0],"depotInfo_ISVDepotTypeList",ISVDepotType);
    $module.constant.getConstantListByNameURL.call($('select[name="batchValidation"]')[0], "depotInfo_batchValidationList", "${detail.batchValidation}");
    var depotIdStr = "${detail.id}";
    var isValetManageStr = "${detail.isValetManage}";
    debugger
    if ("1"==isValetManageStr) { 
    	$("#isValetManage_checked").prop("checked",true);
	} 
    $(function(){
		debugger;
		$custom.request.ajax("/webapi/system/customsArea/getDepotCustomsRel.asyn", {"depotId":depotIdStr}, function(result){
			debugger
			var depotCustomsRelList=result.data;
			// 循环仓库关系回显
			$(depotCustomsRelList).each(function(index,result){
				debugger;
				var customsAreaId=result.customsAreaId;
				var onlineRegisterNo=result.onlineRegisterNo;
				if (onlineRegisterNo==null) {
					onlineRegisterNo="";
				}
				var recCompanyName=result.recCompanyName;
				if (recCompanyName==null) {
					recCompanyName="";
				}
				var recCompanyEnname=result.recCompanyEnname;
				if (recCompanyEnname==null) {
					recCompanyEnname="";
				}
				var address=result.address;
				if (address==null) {
					address="";
				}

				var url = "/webapi/system/customsArea/getCustomsSelectBean.asyn";
		    	var customerAreaHtml='<td><select style="width: 70px ;height:25px;" name="customsAreaConfigId" id="customsAreaConfigId"> <option value="">请选择</option>';
		    	$custom.request.ajax(url, {}, function(result){
		    		
		    		debugger;
		    		var list=result.data; 
		    		// 循环关区下拉框
		    		$(list).each(function(index,event){
		    			var selected1="";
		    			if (customsAreaId==event.value) {
							selected1="selected";
						}
		    			
		    			customerAreaHtml=customerAreaHtml+'<option value="'+event.value+'"'+selected1+'>'+event.label+'</option>';
		    		});
		    		var listArr = "<tr>"
		    			+ "<td><input type='checkbox' name='item-checkbox' name='all'></td>"
		    			+ customerAreaHtml
		    			+ '<td><input type="text" name="onlineRegisterNo" value="'+onlineRegisterNo+'"></td>'
		    			+ '<td><input type="text" name="recCompanyName" value="'+recCompanyName+'"></td>'
		    			+ '<td><input type="text" name="recCompanyEnname" value="'+recCompanyEnname+'"></td>'
		    			+ '<td><input type="text" name="customerAddress" value="'+address+'"></td>'
		    			+ "</tr>";
		    	$("#table-list-group tbody").append(listArr);
				});
			
			}) ;
			
		});
		
		
		//重新加载select2
		$(".goods_select2").select2({
			language:"zh-CN",
			placeholder: '请选择',
			allowClear:true
		});
		
		if($("#hiddenIsMerchant").val()=='2'){
			$("#key-div").css("display","block");
		}else{
			$("#key-div").css("display","none");
		}
		
		//初始化国检海关必填
		setHGandGJRequired() ;
		
		//仓库类型改变事件
		$("#type").change(function(){
			setHGandGJRequired() ;
		}) ;
		
		//保存按钮
		$("#save-buttons").click(function(){
			debugger;
			var url = "/depot/modifyDepot.asyn";
			var form = $("#edit-form").serializeArray();
			
			//必填项校验
			if($("#depotCode").val()==""){
				$custom.alert.error("仓库自编码不能为空！");
				return ;
			}
			
			//必填项校验
			if($("#code").val()==""){
				$custom.alert.error("OP仓库编号不能为空！");
				return ;
			}
			
			if($("#name").val()==""){
				$custom.alert.error("仓库名称不能为空！");
				return ;
			}
			
			if($("#type").val()==""){
				$custom.alert.error("仓库类别不能为空！");
				return ;
			}
			
			if($("#depotType").val()==""){
				$custom.alert.error("仓库类型不能为空！");
				return ;
			}
			
			if($("#type").val() == "a"){
				
				if($("#customsNo").val() == ""){
					$custom.alert.error("申报海关编号不能为空！");
					return ;
				}
				
				if($("#inspectNo").val() == ""){
					$custom.alert.error("申报国检编号不能为空！");
					return ;
				}
			}

            if (!$("#ISVDepotType").val()) {
                $custom.alert.error("ISV仓库类型不能为空！");
                return ;
            }
            if ($("#batchValidation").val() == "") {
                $custom.alert.error("是否批次效期强校验不能为空");
                return;
            }

			var item = {};
			var json = {};
			// 判断batchValidation 为1 时，该仓库所有商品是否都有 批次效期
			var depotId = $("#id").val();//仓库ID
			//var batchValidation = $("#batchValidation").val();//是否批次效期强校验
			var batchValidation=$("input[name='batchValidation']:checked").val();
			item['id']=depotId;
			item['batchValidation']=batchValidation;
			json['item'] = item;
			if (batchValidation == '1'){
				var json1=$module.batchValidation.queryBatchValidation(depotId);
				var jsonData = JSON.stringify(json1);
				var obj = eval('('+jsonData+')');
				if(obj.status=='0'){
					$custom.alert.error(obj.msg);
// 					$custom.alert.error("批次库存 存在商品是批次效期为空不能修改");
					return;
				}
			}
			 var isValetManage ="0";// 是否代客管理仓库： 1-是 0-否
			 if ($("#isValetManage_checked").is(":checked")==true) { 
	            	isValetManage="1"
	         }    
			var isValetManagej = {name: 'isValetManage', value: isValetManage};
            form.push(isValetManagej);
            
            
            var itemList = new Array();
            var flag=false;
            debugger;
            $('#table-list-group tbody tr').each(function(i){// 遍历 tr
            	debugger;
				var item ={};							
				var customsAreaConfigId = $(this).find('select[name="customsAreaConfigId"]').val();
				item['customsAreaConfigId']=customsAreaConfigId;
				if (!customsAreaConfigId) {
					flag="true"; 
	            }
				var onlineRegisterNo = $(this).find('input[name="onlineRegisterNo"]').val();
				item['onlineRegisterNo']=onlineRegisterNo;
				var recCompanyName = $(this).find('input[name="recCompanyName"]').val();
				item['recCompanyName']=recCompanyName;
				var recCompanyEnname = $(this).find('input[name="recCompanyEnname"]').val();
				item['recCompanyEnname']=recCompanyEnname;
				var customerAddress = $(this).find('input[name="customerAddress"]').val();
				item['customerAddress']=customerAddress;
				itemList.push(item);
			});
            if (flag) {
            	$custom.alert.error("请选择关区");
                return true;
			}
            var json={};            
            json['itemList']=itemList;
            var jsonObject= JSON.stringify(json);
            var jsonJson = {name: 'json', value: jsonObject};
            form.push(jsonJson); 
            
			
			$custom.request.ajax(url, form, function(result){
				if(result.state == '200'){
					$custom.alert.success("编辑成功！");
					setTimeout(function () {
						$load.a("/depot/toPage.html");
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
		
		/* $("#batchValidation").change(function(){
			var item = {};
			var json = {};
			debugger
			// 判断batchValidation 为1 时，该仓库所有商品是否都有 批次效期
			var depotId = $("#id").val();//仓库ID
			var batchValidation = $("#batchValidation").val();//是否批次效期强校验
			item['id']=depotId;
			item['batchValidation']=batchValidation;
			json['item'] = item;
			if (batchValidation == 1){
				var json1=$module.batchValidation.queryBatchValidation(depotId);
				var obj = eval('('+json1+')');
				if(obj.status=='0'){
					$custom.alert.error(obj.msg);
					
// 					$custom.alert.warningText("批次库存 存在商品是批次效期为空不能修改");
				}
			}
		}); */
		
		//是否代销仓选项控制仓库地址添加按钮
		$("input[name='isTopBooks']").on("change",function(){
			if($(this).is(':checked')){
				$("#add-address").show();
			}else{
				$("#add-address").hide();
				$("#add-address").nextAll().remove();
			}
		})
		
		$("input[name='isTopBooks']").change();
		
		//删除仓库详细地址
		$("#depotAddress").on("click",".del-address",function(){
			$(this).prev().remove();
			$(this).remove();
		})

		//添加仓库详细地址
		$("#add-address").on("click",function(){
			$("#depotAddress").append(
					'<input class="form-control addressInput" rows="5" name="address"  placeholder="请输入省市区地址" />'+
					'<button type="button" class="btn btn-find waves-effect waves-light btn_default del-address addressBotton" style="float:left;">删除</button>')
		})
		
		//取消按钮
		$("#cancel-buttons").click(function(){
			$load.a("/depot/toPage.html");
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
    function changekey(val){
		if(val == '2'){
			$("#key-div").css("display","block");
		}else{
			$("#key-div").css("display","none");
		}
	}
    
	function setHGandGJRequired(){
		
		var type = $("#type").val() ;
		
		if(type == 'a'){
			$(".bonded").show() ;
		}else{
			$(".bonded").hide() ;
		}
	}


    $("#addRel").click(function() {
    	debugger;
    	var url = "/webapi/system/customsArea/getCustomsSelectBean.asyn";
    	var customerAreaHtml='<td><select style="width: 70px ;height:25px;" name="customsAreaConfigId" id="customsAreaConfigId"> <option value="">请选择</option>';
    	$custom.request.ajax(url, {}, function(result){
    		
    		debugger;
    		var list=result.data; 
    		$(list).each(function(index,event){
    			customerAreaHtml=customerAreaHtml+'<option value="'+event.value+'">'+event.label+'</option>';
    		});
    		var listArr = "<tr>"
    			+ "<td><input type='checkbox' name='item-checkbox' name='all'></td>"
    			+ customerAreaHtml
    			+ "<td><input type='text' name='onlineRegisterNo' ></td>"
    			+ "<td><input type='text' name='recCompanyName' ></td>"
    			+ "<td><input type='text' name='recCompanyEnname' ></td>"
    			+ "<td><input type='text' name='customerAddress' ></td>"
    			+ "</tr>";
    	$("#table-list-group tbody").append(listArr);
		});
    	
    	
    	
    });

	//删除行
	 $("#deleteRel").click(function() {
		 debugger;
	 		var merchantIds = [];
	        $("input[name='item-checkbox']:checked").each(function() { // 遍历选中的checkbox
	            n = $(this).parents("tr").index()+1;  // 获取checkbox所在行的顺序
	            /* var merchantId = $(this).parent().find("input[name='customerMerchantRelId']").val();
	            if(merchantId){
	            	merchantIds.push(merchantId);
	            } */
	            $("#table-list-group").find("tr:eq("+n+")").remove();
	        });
	       // $("#deleteIds").val(merchantIds);
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
		
</script>
