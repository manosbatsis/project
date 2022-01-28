<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

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
                       <li class="breadcrumb-item"><a href="#">公司档案</a></li>
                       <li class="breadcrumb-item "><a href="javascript:$load.a('/merchant/toPage.html');">公司列表</a></li>
                       <li class="breadcrumb-item "><a href="javascript:$load.a('/merchant/toAddPage.html?id=${detail.id }');">编辑公司信息</a></li>
                    </ol>
                    </div>
                   </div>
                    <!--  title   end  -->               
                          <!--  title   基本信息   start -->

                <form id="add-form">
                  <div class="title_text">公司信息</div>
                  <div class="form-row mt20 ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr"><i class="red">* </i>公司简称<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                                <input type="text"  required reqMsg="公司简称不能为空！" parsley-type="text" class="form-control" id="name" name="name" value="${detail.name }">
                                 <input type="hidden" parsley-type="text" class="form-control" id="yname" name="yname" value="${detail.name }">
                            	<input type="hidden" value="${detail.id }" name="id" id="merchantId">
                            </div>
                        </div>
                    </div>
                    <div class="col-5">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr"><i class="red">* </i>卓志编码<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                                <input type="text" required reqMsg="卓志编码不能为空！" parsley-type="text" class="form-control" id="topidealCode" name="topidealCode" value="${detail.topidealCode }">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr"><i class="red">* </i>公司全称<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                                <input type="text" required reqMsg="公司全称不能为空！" parsley-type="text" class="form-control" name="fullName" id="fullName" value="${detail.fullName }">
                            </div>
                        </div>
                    </div>
                    <div class="col-5">
                        
                    </div>
                </div>
                <div class="form-row ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">提醒财务付款邮箱<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                                <input type="text" parsley-type="text" class="form-control" name="financePayEmail" id="financePayEmail" value="${detail.financePayEmail }">
                            </div>
                        </div>
                    </div>
                    <div class="col-5">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">盘点结果通知邮箱<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                                <input type="text" parsley-type="text" class="form-control" name="inventoryResultEmail" id="inventoryResultEmail"  value="${detail.inventoryResultEmail }">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row ml15">
                    <%-- <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr"><i class="red">* </i>商品备案价申报系数<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                                <input type="text" required reqMsg="商品备案价申报系数不能为空！" parsley-type="text" class="form-control" name="priceDeclareRatio" id="priceDeclareRatio"  value="${detail.priceDeclareRatio }">
                            </div>
                        </div>
                    </div> --%>
                    <div class="col-5">
	                        <div class="row">
	                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr"><i class="red">* </i>注册地址(英文)<span>：</span></span></label>
	                            <div class="col-8 ml10 line">
	                                <input type="text" required reqMsg="注册地址(英文)不能为空！" parsley-type="text" class="form-control" name="englishRegisteredAddress" id="englishRegisteredAddress" value="${detail.englishRegisteredAddress }">
	                            </div>
	                        </div>
	                </div>
                    <div class="col-5">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">接收清关资料邮箱<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                                <input type="text" parsley-type="text" class="form-control" name="email" value="${detail.email }">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">备注<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                                <input type="text" parsley-type="text" class="form-control" name="remark" value="${detail.remark }">
                            </div>
                        </div>
                    </div>
                    <div class="col-5">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">是否代理<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                                <select class="form-control" name="isProxy" id="isProxy">
                                	<c:choose>
                                		<c:when test="${detail.isProxy eq '0' }">
                                			<option value="0" selected>否</option>
                                		</c:when>
                                		<c:otherwise>
                                			<option value="0">否</option>
                                		</c:otherwise>
                                	</c:choose>
                              		<c:choose>
                                		<c:when test="${detail.isProxy eq '1' }">
                                			<option value="1" selected>是</option>
                                		</c:when>
                                		<c:otherwise>
                                			<option value="1">是</option>
                                		</c:otherwise>
                                	</c:choose>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row ml15"> 
                     <div class="col-5">
                        <div class="row"> 
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">授权码 <span>：</span></span></label>
                            <div class="col-8 ml10 line"> 
                                 <input type="text" parsley-type="text" class="form-control" name="permission" value="${detail.permission }">
                             </div> 
                         </div> 
                    </div> 
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">联系电话<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                                <input type="text" parsley-type="text" class="form-control" name="tel"  value="${detail.tel }">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row ml15"> 
                     <div class="col-5">
                     <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr"><i class="red">*</i>勾稽完结百分比(%)<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                                <input type="text" required reqMsg="勾稽完结百分比不能为空！" parsley-type="text" class="form-control" id="articulationPercent"
                                       name="articulationPercent" <c:if test="${ detail.articulationPercent!=null}">value="${detail.articulationPercent*100 }"</c:if>
                                       onkeyup="value=value.replace(/[^\d^\.]/g,'')">
                            </div>
                        </div>
                    </div> 
                   <div class="col-5">
               			<div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">英文名称<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                                <input type="text" parsley-type="text" class="form-control" name="englishName"  value="${detail.englishName }">
                            </div>
                        </div>
                    </div>
                </div>
                    <div class="form-row ml15">
                        <div class="col-5">
                            <div class="row">
                                <label class="col-4 col-form-label "
                                       style="white-space: nowrap;"><span class="fr"><i
                                        class="red">* </i>成本核算币种<span>：</span></span></label>
                                <div class="col-8 ml10 line">
                                    <select class="form-control" name="accountCurrency" id="accountCurrency" required reqMsg="成本核算币种不能为空！">
                                        <option value="">请选择</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="col-5">
                            <div class="row">
                                <label class="col-4 col-form-label "
                                       style="white-space: nowrap;"><span class="fr"><i
                                        class="red">* </i>核算单价<span>：</span></span></label>
                                <div class="col-8 ml10 line">
                                    <select class="form-control" name="accountPrice" id="accountPrice" required reqMsg="核算单价不能为空！" >
                                        <option value="">请选择</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                     <div class="form-row ml15">
	                    <div class="col-5">
	                        <div class="row">
	                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr"><i class="red">* </i>注册地址<span>：</span></span></label>
	                            <div class="col-8 ml10 line">
	                                <input type="text" required reqMsg="注册地址不能为空！" parsley-type="text" class="form-control" name="registeredAddress" id="registeredAddress" value="${detail.registeredAddress }">
	                            </div>
	                        </div>
	                    </div>
                         <div class="col-5">
                             <div class="row">
                                 <label class="col-4 col-form-label "
                                        style="white-space: nowrap;"><span class="fr"><i
                                         class="red">* </i>注册地类型<span>：</span></span></label>
                                 <div class="col-8 ml10 line">
                                     <select class="form-control" name="registeredType" id="registeredType" required reqMsg="注册地类型不能为空！" >
                                         <option value="">请选择</option>
                                     </select>
                                 </div>
                             </div>
                         </div>
	                    
                	</div>
                    
                    <div class="title_text">银行信息</div>
                    <div class="form-row ml15 mt20">
                        <div class="col-5">
                            <div class="row">
                                <label class="col-4 col-form-label "
                                       style="white-space: nowrap;"><span class="fr"><i
                                        class="red">* </i>开户银行<span>：</span></span></label>
                                <div class="col-8 ml10 line">
                                    <input type="text" parsley-type="text"
                                           class="form-control" name="depositBank" required reqMsg="开户银行不能为空！"
                                           id="depositBank"  value="${detail.depositBank }">
                                </div>
                            </div>
                        </div>
                        <div class="col-5">
                            <div class="row">
                                <label class="col-4 col-form-label"
                                       style="white-space: nowrap;"><span class="fr"><i
                                        class="red">* </i>银行账号<span>：</span></span></label>
                                <div class="col-8 ml10 line">
                                    <input type="text" parsley-type="text"
                                           class="form-control" name="bankAccount" required reqMsg="银行账号不能为空！"
                                           id="bankAccount" value="${detail.bankAccount }">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-row ml15">
                        <div class="col-5">
                            <div class="row">
                                <label class="col-4 col-form-label "
                                       style="white-space: nowrap;"><span class="fr"><i
                                        class="red">* </i>银行账户<span>：</span></span></label>
                                <div class="col-8 ml10 line">
                                    <input type="text" required reqMsg="银行账户不能为空！" parsley-type="text"
                                           class="form-control" name="beneficiaryName"
                                           id="beneficiaryName" value="${detail.beneficiaryName }">
                                </div>
                            </div>
                        </div>
                        <div class="col-5">
                            <div class="row">
                                <label class="col-4 col-form-label"
                                       style="white-space: nowrap;"><span class="fr"><i
                                        class="red">* </i>Swift Code<span>：</span></span></label>
                                <div class="col-8 ml10 line">
                                    <input type="text" required reqMsg="Swift Code不能为空！" parsley-type="text"
                                           class="form-control" name="swiftCode"
                                           id="swiftCode" value="${detail.swiftCode }">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-row ml15">
                        <div class="col-5">
                            <div class="row">
                                <label class="col-4 col-form-label "
                                       style="white-space: nowrap;"><span class="fr"><i
                                        class="red">* </i>开户行地址<span>：</span></span></label>
                                <div class="col-8 ml10 line">
                                    <input type="text" required reqMsg="开户行地址不能为空！" parsley-type="text"
                                           class="form-control" name="bankAddress"
                                           id="bankAddress" value="${detail.bankAddress }">
                                </div>
                            </div>
                        </div>
                        <div class="col-5">

                        </div>
                    </div>
                </form>
                
                <!--  关联仓库 开始 -->
				<div>
					<div class="title_text">仓库信息</div>
					<div class="form-row mt10">
						<div class="col-12 ml30">
							<div class="row">
								<label class="col-1 col-form-label "
									style="white-space: nowrap;"><div class="fr">
										仓库信息<span>：</span>
									</div></label>
								<button type="button"
									class="btn btn-find waves-effect waves-light btn_default"
									id="add-depot-list-button">新 增</button>
								&nbsp;
								<button type="button"
									class="btn btn-find waves-effect waves-light btn_default"
									id="delete-depot-list-button">删 除</button>
							</div>
						</div>
					</div>

					<div class="form-row mt20 ml15">
						<table id="table-depot-list" class="table table-striped">
							<thead>
								<tr>
									<th><input id="checkbox11" type="checkbox" name="depot-all"></th>
									<th style="width: 20%;">仓库名称</th>
									<th>仓库编号</th>
									<th>合同编号</th>
									<th>进出接口指令</th>
									<th>统计存货跌价</th>
									<th>选品限制</th>
									<th>以入定出</th>
									<th>以出定入</th>
									<th>绑定事业部</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${depotMerchantRelList }" var="merchant" varStatus="idxStatus">
		                          <tr>
		                              <td>
		                                  <input type='checkbox' name='item-checkbox'>
		                                  <input type="hidden" class="depot_id" name="depot_id" value="${merchant.depotId }">
		                              </td>
		                              <td>${merchant.depotName}</td>
		                              <td>${merchant.depotCode}</td>
		                              <td>
		                              <input type="text"  name="contractCode" value="${merchant.contractCode }">
		                              </td>
		                              <td><input type='checkbox' name='isInOutInstruction' onclick='inAndOutDisabled(this)' value='1' <c:if test='${ merchant.isInOutInstruction == "1"}'>checked</c:if> <c:if test='${ merchant.isInDependOut == "1" || merchant.isOutDependIn == "1"}'>disabled</c:if>>是</td>
									  <td><input type='checkbox' name='isInvertoryFallingPrice' value='1' <c:if test='${merchant.isInvertoryFallingPrice == "1"}'>checked</c:if>>是</td>
									  <td><select class='form-control modal_input' name='productRestriction'>
										<option value=''>请选择</option>
										<option value='1' <c:if test='${merchant.productRestriction == "1"}'>selected</c:if>>仅备案商品</option>
										<option value='2' <c:if test='${merchant.productRestriction == "2"}'>selected</c:if>>仅外仓统一码</option>
										<option value='3' <c:if test='${merchant.productRestriction == "3"}'>selected</c:if>>无限制</option>
										</select> </td>
									 <td><input type='checkbox' name='isInDependOut' value='1' onclick='inOutInstructioDisabled(this)' <c:if test='${merchant.isInDependOut == "1"}'>checked</c:if> <c:if test='${ merchant.isInOutInstruction == "1"}'>disabled</c:if>>是</td>
									 <td><input type='checkbox' name='isOutDependIn' value='1' onclick='inOutInstructioDisabled(this)' <c:if test='${merchant.isOutDependIn == "1"}'>checked</c:if> <c:if test='${ merchant.isInOutInstruction == "1"}'>disabled</c:if>>是</td>
									 <td><select name='bu_select' id='bu_select${idxStatus.index}'  class='selectpicker show-tick form-control' multiple data-title='请选择' depot-id='${merchant.depotId}'>
									 		<c:forEach items="${merchantBuRelList }" var="merchantBuRel" >
									 			<option value='${merchantBuRel.value }' >${merchantBuRel.label}</option>
									 		</c:forEach>
									 	</select>
									 </td>
		                          </tr>
		                      </c:forEach>
							</tbody>
						</table>
					</div>
				</div>
                
                <div id="isProxyDiv">
                    <div class="title_text">代理公司</div>
                	<div class="form-row mt10">
                         <div class="col-12 ml30">
                              <div class="row">
                                  <label  class="col-1 col-form-label " style="white-space:nowrap;"><div class="fr">代理公司<span>：</span></div></label>
                                      <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="add-list-button">新 增</button>&nbsp;
                                      <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="delete-list-button">删 除</button>
                              </div>
                          </div>
                      </div>
                    <div class="form-row mt20">
	                   <table id="table-list" class="table table-striped">
                           <thead>
                                <tr>
                               <th><input id="checkbox11" type="checkbox" name="all"></th>
                               <th>公司简称</th>
                               <th>公司编号</th>
                           </tr>
                           </thead>
                           <tbody>
                           <c:forEach items="${list }" var="merchant">
                               <tr>
                                   <td>
                                       <input type='checkbox' name='item-checkbox'>
                                       <input type="hidden" class="merchant_id" name="merchant_id" value="${merchant.id }">
                                   </td>
                                   <td>${merchant.name}</td>
                                   <td>${merchant.code}</td>
                               </tr>
                           </c:forEach>
                           </tbody>
	                  </table> 
	                 </div>
	               </div>
                 <!--   商品信息  end -->
                      <div class="form-row m-t-50">
                          <div class="col-12">
                              <div class="row">
                                  <div class="col-6">
                                      <button type="button" class="btn btn-info waves-effect waves-light fr btn_default" id="save-buttons">保 存</button>
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
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->
<script src='/resources/js/system/base.js' type="text/javascript"></script>
<script type="text/javascript">
    var accountCurrency = "${detail.accountCurrency}";
    var accountPrice = "${detail.accountPrice}";
    var registeredType= "${detail.registeredType}";
    $module.constant.getConstantListByNameURL.call($('select[name="accountCurrency"]')[0],"currencyCodeList",accountCurrency);
    $module.constant.getConstantListByNameURL.call($('select[name="accountPrice"]')[0],"merchantInfo_accountPriceList",accountPrice);
    $module.constant.getConstantListByNameURL.call($('select[name="registeredType"]')[0],"merchantInfo_registeredTypeList",registeredType);
	//仓库ID
	var depotId = [];
	//以json方式存储关联关系
	var depotBuRel = {};
	initSelectPicker() ;
	
	var isProxy = $("#isProxy").val();
	if(isProxy == '0'){
		$("#isProxyDiv").css("display","block");
	}else{
		$("#isProxyDiv").css("display","none");
	}
	
	$("#isProxy").change(function(){
		if($(this).val()=='0'){
			$("#isProxyDiv").css("display","block");
		}else{
			$("#isProxyDiv").css("display","none");
		}
	});

	//保存按钮
	$("#save-buttons").click(function(){
		var url = "/merchant/modifyMerchant.asyn";	
		var form = $("#add-form").serializeArray();
		//必填项校验
        if(!$checker.verificationRequired()){
            return ;
        }
        if($("#articulationPercent").val() == ""){
            $custom.alert.error("勾稽完结百分比请填入数值！");
            return ;
        } else if($("#articulationPercent").val() > 100) {
            $custom.alert.error("勾稽完结百分比不能大于100！");
            return ;
        }
		//获取公司信息
		var ids="";//储存所有公司ID
		var obj_ids = document.getElementsByName("merchant_id");
		for(var i = 0; i < obj_ids.length; i++){
			if(ids == ""){
				ids = obj_ids[i].value;
			}else{
				ids = ids + "," + obj_ids[i].value;
			}
		}

		//仓库信息
		var depotIds="";//储存所有仓库ID
		var isInOutInstructions = "" ; //进出接口指令
		var isInvertoryFallingPrices = "" ; //统计存货跌价
		var productRestrictions = "" ; //选品限制
		var isInDependOuts = "" ; //已入定出
		var isOutDependIns = "" ; //已出定入
		var depotBuRels = "" ;    //绑定事业部
		var contractCodes = "" ;//合同编码
		
		var flag = false ;
		
		var trs = $("#table-depot-list tbody").find("tr") ;
		$(trs).each(function(index, tr){
			var depotId = $(tr).find("input[name='depot_id']").val() ;
			
			if(!depotId){
				flag = true ;
				$custom.alert.error("第"+(index+1)+"条数据，请选择仓库");
				return false ;
			}
			
			if(depotIds == ""){
				depotIds = depotId;
			}else{
				depotIds +=  "," + depotId;
			}
			
			var depotBuRelTemp = depotBuRel[depotId] ; 
			if(depotBuRelTemp){
				depotBuRels += depotId + ":" + depotBuRelTemp + ";" ;
			}
			
			
			var productRestriction = $(tr).find("select[name='productRestriction']").val() ;
			if(!productRestriction){
				
				var depotName = $(tr).find("td").eq(1).text() ;
				flag = true ;
				$custom.alert.error("仓库："+ depotName +"选品限制不能为空！");
				return false ;
			}
			
			if(productRestrictions == ""){
				productRestrictions = productRestriction;
			}else{
				productRestrictions += "," + productRestriction;
			}
			
			var isInOutInstruction = $(tr).find("input[name='isInOutInstruction']:checked").val() ;
			if(!isInOutInstruction){
				isInOutInstruction = '0' ;
			}
			
			if(isInOutInstructions == ""){
				isInOutInstructions = isInOutInstruction;
			}else{
				isInOutInstructions += "," + isInOutInstruction;
			}
			
			var isInvertoryFallingPrice = $(tr).find("input[name='isInvertoryFallingPrice']:checked").val() ;
			if(!isInvertoryFallingPrice){
				isInvertoryFallingPrice = '0' ;
			}
			
			if(isInvertoryFallingPrices == ""){
				isInvertoryFallingPrices = isInvertoryFallingPrice;
			}else{
				isInvertoryFallingPrices += "," + isInvertoryFallingPrice;
			}
			
			var isInDependOut = $(tr).find("input[name='isInDependOut']:checked").val() ;
			if(!isInDependOut){
				isInDependOut = '0' ;
			}
			
			if(isInDependOuts == ""){
				isInDependOuts = isInDependOut;
			}else{
				isInDependOuts += "," + isInDependOut;
			}
			
			var isOutDependIn = $(tr).find("input[name='isOutDependIn']:checked").val() ;
			if(!isOutDependIn){
				isOutDependIn = '0' ;
			}
			
			if(isOutDependIns == ""){
				isOutDependIns = isOutDependIn;
			}else{
				isOutDependIns += "," + isOutDependIn;
			}
			
			var contractCode=$(tr).find("input[name='contractCode']").val();
			if(contractCodes == ""){
				contractCodes = "contractCode:"+contractCode;
			}else{
				contractCodes += "," + "contractCode:"+contractCode;
			}
			
		}) ; 
		
		if(flag){
			return ;
		}
		
		var data_ids = {name:"ids",value:ids};
		var data_depotIds = {name:"depotids",value:depotIds};
		var data_isInOutInstructions = {name:"isInOutInstructions",value:isInOutInstructions};
		var data_isInvertoryFallingPrices = {name:"isInvertoryFallingPrices",value:isInvertoryFallingPrices};
		var data_productRestrictions = {name:"productRestrictions",value:productRestrictions};
		var data_isInDependOuts = {name:"isInDependOuts",value:isInDependOuts};
		var data_isOutDependIns = {name:"isOutDependIns",value:isOutDependIns}; 
		var data_depotBuRels = {name:"depotBuRels",value:depotBuRels}; 
		var data_contractCodes = {name:"contractCodes",value:contractCodes}; 
		
		form.push(data_ids);
		form.push(data_depotIds);
		form.push(data_isInOutInstructions);
		form.push(data_isInvertoryFallingPrices);
		form.push(data_productRestrictions);
		form.push(data_isInDependOuts);
		form.push(data_isOutDependIns);
		form.push(data_depotBuRels);
		form.push(data_contractCodes);
		
		$custom.request.ajax(url, form, function(result){
			if(result.state == '200'){
				$custom.alert.success("编辑成功！");
				setTimeout(function () {
					$load.a("/merchant/toPage.html");
				}, 1000);
			}else if(result.state=='315'){
				$custom.alert.error("公司名已存在！");
			}else{
				$custom.alert.error("编辑失败！");
			}
		});
	});
	
	//限制新增条数每次只能一条，填完信息后才可以继续填写
	var flag = 0;
	//储存已经选择过的商品ID
	var merchantId = [];
	//新增公司列表按钮
	$("#add-list-button").click(function(){
		if(flag == 0){
			var listArr = "<tr>"+
			"<td style='width:50px;text-align:center'><input type='checkbox' name='item-checkbox'><input type='hidden' name='merchant_id'></td>"+
			"<td><select class='form-control modal_input goods_select2' id='goods_select2'></select></td>"+
			"<td></td>"+
			"</tr>";
			$("#table-list").append(listArr);
			var url = "/merchant/getSelectBean.asyn?r="+Math.random();
			$custom.request.ajax(url, {"isProxy":"1"}, function(result){
				selLoad(result.data, "goods_select2");
				//数据库中的商品移出下拉框
				$(".merchant_id").each(function(){
					merchantId.push($(this).val());
				});
				//选择过的商品移出下拉框
				if(merchantId.length>0){
					for(var i = 0; i < merchantId.length; i++){
						$("#goods_select2").children().each(function(){
							if($(this).val()==merchantId[i]){
								$(this).remove();
							}
						});
					}
				}
				//重新加载select2
				$(".goods_select2").select2({
					language:"zh-CN",
					placeholder: '请选择',
					allowClear:true
				});
			});
			flag = 1;
		}
		//选择商品
		$("#goods_select2").change(function(){
			var that = $(this);
			var url = "/merchant/getMerchantInfoDetails.asyn?r="+Math.random();
			var data = {"id":that.val()};
			$custom.request.ajax(url, data, function(result){
				//回写公司id
				that.parent().prev().find("input[name='merchant_id']").val(result.data.id);
				//回写公司编码
				that.parent().next().text(result.data.code);
				//回写公司名称
				that.parent().text(result.data.name);
				flag = 0;
				merchantId.push(result.data.id);
			});
		});
	});
	
	var depotFlag = 0;
	//新增仓库列表按钮
	var bu_select_index = 100 ;
	$("#add-depot-list-button").click(function() {
		if (depotFlag == 0) {
			
			bu_select_index ++ ;
			
			var listArr = "<tr>"
					+ "<td><input type='checkbox' name='item-checkbox'><input type='hidden' name='depot_id'></td>"
					+ "<td><select class='form-control modal_input goods_select2' id='depot_select'></select></td>"
					+ "<td></td>"
					+ "<td><input type='text' name='contractCode' ></td>"
					+ "<td><input type='checkbox' name='isInOutInstruction' onclick='inAndOutDisabled(this)' value='1'>是</td>"
					+ "<td><input type='checkbox' name='isInvertoryFallingPrice' value='1'>是</td>"
					+ "<td><select class='form-control modal_input' name='productRestriction'>"
					+ "<option value=''>请选择</option>"
					+ "<option value='1'>仅备案商品</option>"
					+ "<option value='2'>仅外仓统一码</option>"
					+ "<option value='3'>无限制</option>"
					+ "</select></td>"
					+ "<td><input type='checkbox' name='isInDependOut' value='1' onclick='inOutInstructioDisabled(this)'>是</td>"
					+ "<td><input type='checkbox' name='isOutDependIn' value='1' onclick='inOutInstructioDisabled(this)'>是</td>"
					+ "<td><select name='bu_select' id='bu_select" + bu_select_index + "'  class='selectpicker show-tick form-control' multiple data-title='请选择'></select></td>"
					+ "</tr>";
			$("#table-depot-list tbody").append(listArr);
			var url = "/depot/getSelectBean.asyn?r=" + Math.random();
			$custom.request .ajax( url, {}, function(result) {
				selLoad(result.data, "depot_select");
				//选择过的商品移出下拉框
				if (depotId.length > 0) {
					for (var i = 0; i < depotId.length; i++) {
						$("#depot_select").children().each( function() {
							if ($(this).val() == depotId[i]) {
								$(this).remove();
							}
						});
					}
				}
				//重新加载select2
				$("#depot_select").select2({
					language : "zh-CN",
					placeholder : '请选择',
					allowClear : true
				});
			});
			
			var merchantId = $("#merchantId").val() ;
			var url = "/merchantBuRel/getSelectBean.asyn?r=" + Math.random();
			$custom.request .ajax( url, {"merchantId":merchantId}, function(result) {
				selLoad(result.data, "bu_select" + bu_select_index);
				$("#bu_select" + bu_select_index).selectpicker({width: '150px'});
			    $("#bu_select" + bu_select_index).selectpicker('refresh');
			    
			    $("#bu_select" + bu_select_index).prev().prev().css({"height": "30px","margin-bottom": "5px","border": "1px solid #dadada","background": "white"}) ;
			    $("#bu_select" + bu_select_index).prev().css({"z-index":"99"});
			});
			
			depotFlag = 1;
		}
		//选择仓库
		$("#depot_select").change(function() {
			var that = $(this);
			var url = "/depot/getDepotDetails.asyn?r=" + Math.random();
			var data = { "id" : that.val() };
			$custom.request.ajax(url,data,function(result) {
				//回写仓库id
				that.parent().prev().find("input[name='depot_id']").val(result.data.id);
				//回写仓库编码
				that.parent().next().text(result.data.code);
				//回写仓库名称
				that.parent().text(result.data.name);
				depotFlag = 0;
				depotId.push(result.data.id);
			});
		});
		
		//选择事业部
		$("select[name='bu_select']").on('change', function() {
			var that = $(this);
			var depotId = that.parent().parent().parent().find("input[name='depot_id']").val();
			
			if(!depotId){
				that.selectpicker('deselectAll');
				that.selectpicker('refresh');
				$custom.alert.error("请先确认仓库");
				return ;
			}
			
			var buIds = that.val();
			buIds = buIds.join(",") ;
			depotBuRel[depotId] = buIds ;
			
		});
	});
	
	//删除选中行
 	$("#delete-list-button").click(function() {
 		var del_ids = [];//储存删除行的id
        $("input[name='item-checkbox']:checked").each(function() { // 遍历选中的checkbox
            n = $(this).parents("tr").index();  // 获取checkbox所在行的顺序
            del_ids.push($(this).parents("tr").find("input[name='merchant_id']").val());
            $("#table-list").find("tr:eq("+(n+1)+")").remove();
        });
 		for (var i = 0; i < merchantId.length; i++) {
			for (var j = 0; j < del_ids.length; j++) {
				if(merchantId[i] == del_ids[j]){
					merchantId.splice(i, 1);
				}
			}
		}
 		flag = 0;
        $("input[name='all']").prop("checked",false);
    });
	
 	//删除仓库选中行
	$("#delete-depot-list-button").click(
			function() {
				var del_ids = [];//储存删除行的id
				$("input[name='item-checkbox']:checked").each(
						function() { // 遍历选中的checkbox
							n = $(this).parents("tr").index(); // 获取checkbox所在行的顺序
							del_ids.push($(this).parents("tr").find("input[name='depot_id']").val());
							$("#table-depot-list").find("tr:eq(" + (n + 1) + ")")
									.remove();
						});
				for (var i = 0; i < depotId.length; i++) {
					for (var j = 0; j < del_ids.length; j++) {
						if (depotId[i] == del_ids[j]) {
							depotId.splice(i, 1);
						}
					}
				}
				depotFlag = 0;
				$("input[name='depot-all']").prop("checked", false);
			});
	
	//全选
	$("input[name='all']").click(function() {
		//判断当前点击的复选框处于什么状态$(this).is(":checked") 返回的是布尔类型
		if ($(this).is(":checked")) {
			$("#table-list").find("input[name='item-checkbox']").prop("checked", true);
		} else {
			$("#table-list").find("input[name='item-checkbox']").prop("checked", false);
		}
	});
	
	//仓库全选
	$("input[name='depot-all']").click(function() {
		//判断当前点击的复选框处于什么状态$(this).is(":checked") 返回的是布尔类型
		if ($(this).is(":checked")) {
			$("#table-depot-list").find("input[name='item-checkbox']").prop("checked", true);
		} else {
			$("#table-depot-list").find("input[name='item-checkbox']").prop("checked", false);
		}
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

	//返回
	$("#cancel-buttons").click(function(){
		$load.a("/merchant/toPage.html");
	});
	
	//当前面勾选了“进出接口指令”为是时，以入定出不予勾选，仅能为否；
	function inAndOutDisabled(org){
		
		var checked = org.checked ;
		
		if(checked == true ){
			$(org).parent().parent().find("input[name='isInDependOut']").attr("disabled",true);
			$(org).parent().parent().find("input[name='isOutDependIn']").attr("disabled",true);
		}else{
			$(org).parent().parent().find("input[name='isInDependOut']").removeAttr("disabled");
			$(org).parent().parent().find("input[name='isOutDependIn']").removeAttr("disabled");
		}
		
	}
	
	//当前面勾选了“进出接口指令”为是时，以入定出不予勾选，仅能为否；
	function inOutInstructioDisabled(org){
		
		var checked =
			$(org).parent().parent().find("input[name='isInDependOut']").is(':checked') || 
			$(org).parent().parent().find("input[name='isOutDependIn']").is(':checked');
		
		if(checked == true){
			$(org).parent().parent().find("input[name='isInOutInstruction']").attr("disabled",true);
		}else{
			$(org).parent().parent().find("input[name='isInOutInstruction']").removeAttr("disabled");
		}
		
	}
	
	function initSelectPicker(){
		var merchantId = $("#merchantId").val() ;
		$(".selectpicker").each(function(index, item){
			var id = $(item).attr("depot-id") ;
			depotId.push(id) ;
			
			var data = {"depotId" : id , "merchantId" : merchantId}
			$custom.request.ajax("/merchantDepotBuRel/getSelectInfoByMerchantId.asyn",data,function(result) {
				var buIds = result.data ; 
				
				if(buIds){
					var arr=buIds.split(',');
					$(item).selectpicker('val', arr);
					depotBuRel[id] = buIds ;
				}
			});
			
		}) ;
		
		//选择事业部
		$("select[name='bu_select']").on('change', function() {
			var that = $(this);
			var depotId = that.parent().parent().parent().find("input[name='depot_id']").val();
			
			if(!depotId){
				that.selectpicker('deselectAll');
				that.selectpicker('refresh');
				$custom.alert.error("请先确认仓库");
				return ;
			}
			
			var buIds = that.val();
			buIds = buIds.join(",") ;
			depotBuRel[depotId] = buIds ;
			
		});
		
		$(".selectpicker").selectpicker({width: '150px'});
	    $(".selectpicker").selectpicker('refresh');
	    
	    $(".selectpicker").prev().prev().css({"height": "30px","margin-bottom": "5px","border": "1px solid #dadada","background": "white"}) ;
	    $(".selectpicker").prev().css({"z-index":"99"});
	}
</script>