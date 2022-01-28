<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt60">
        <!-- Start row -->
             <div class="row">
              <div class="card-box margin table-responsive" style="overflow-x:hidden;">
                 <div class="row">
              <!--  title   start  -->
              <div class="col-12">
                <div>
                   <div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                       <li><i class="block"></i> 当前位置：</li>
                       <li class="breadcrumb-item"><a href="#">供应商档案</a></li>
                       <li class="breadcrumb-item"><a href="javascript:$load.a('/supplier/toPage.html')">供应商列表</a></li>
                       <li class="breadcrumb-item"><a href="javascript:$load.a('/supplier/toEditPage.html')">供应商编辑</a></li>
                    </ol>
                    </div>
            </div>
                    <!--  title   end  -->
                  <div class="margin">
		          <form id="edit-form" action="/supplier/modifySupplier.asyn">
                      <div class="edit_box">
                          <div class="edit_item">
                              <input type="hidden" name="id" id="id" value="${detail.id }">
                              <input type="hidden" id ="deleteIds" name="deleteIds" />
                              <label class="edit_label"><i class="red">*</i>供应商名称：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" id="name" name="name" value="${detail.name }">
                          </div>
                          <div class="edit_item">
                          	  <label class="edit_label">供应商简称：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" id="simpleName" name="simpleName" value="${detail.simpleName }">
                          </div>
                          <div class="edit_item">
                              <label class="edit_label"><i class="red">*</i>营业执照号：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" id="creditCode" name="creditCode" value="${detail.creditCode }">
                          </div>
                      </div>
                      <div class="edit_box">
                          <div class="edit_item">
                              <label class="edit_label"><i class="red">*</i>组织机构代码：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input"  id="orgCode" name="orgCode" value="${detail.orgCode }">
                          </div>
                          <div class="edit_item">
                              <label class="edit_label">客户等级编码：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" value="${detail.codeGrade }" id="codeGrade" name="codeGrade">
                          </div>
                          <div class="edit_item">
                          		<label class="edit_label">企业性质：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" id="nature" name="nature" value="${detail.nature }">
                          </div>
                      </div>
                      <div class="edit_box">
                          <div class="edit_item">
                              <label class="edit_label">中文名：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" id="chinaName"name="chinaName" value="${detail.chinaName }">
                          </div>
                          <div class="edit_item">
                              <label class="edit_label">英文简称：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" id="enSimpleName"name="enSimpleName" value="${detail.enSimpleName }">
                          </div>
                          <div class="edit_item">
                              <label class="edit_label">英文名：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" id="enName"name="enName" value="${detail.enName }">
                          </div>
                      </div>
                      <div class="edit_box">
                          <div class="edit_item">
                              <label class="edit_label">主数据ID：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" id="mainId"name="mainId" value="${detail.mainId }" <c:if test="${ detail.mainId != null}">readonly</c:if>>
                          </div>
                          <div class="edit_item">
                              <label class="edit_label">业务员：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" id="salesman" name="salesman" value="${detail.salesman }">
                          </div>
                          <div class="edit_item">
                          	  <label class="edit_label">省市区代码：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" id="cityCode" name="cityCode" value="${detail.cityCode }">
                          </div>
                      </div>
                      <div class="edit_box">
                          <div class="edit_item">
                              <label class="edit_label">注册地：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" id="companyAddr"name="companyAddr" value="${detail.companyAddr }">
                          </div>
                          <div class="edit_item">
                              <label class="edit_label">企业地址：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" id="businessAddress" name="businessAddress" value="${detail.businessAddress }">
                          </div>
                          <div class="edit_item">
                          	  <label class="edit_label">企业英文地址：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" id="enBusinessAddress" name="enBusinessAddress" value="${detail.enBusinessAddress }">
                          </div>
                      </div>
                      <div class="edit_box">
                      	  <div class="edit_item">
	                            <label class="edit_label">采购币种：</label>
	                             <select class="edit_input" name="currency" id="currency" parsley-trigger="change">
	                             		<option value="">请选择</option>
                            	</select>
                            	 <input id="currency_hidden" type="hidden" value="${detail.currency }">
                          </div>
                          <div class="edit_item">
                              <label class="edit_label"><i class="red">*</i>是否内部公司：</label>
                              <select class="edit_input" name="type" id="type">
                                  <option value="">请选择</option>
                                  <c:choose>
                                      <c:when test="${detail.type=='1' }">
                                          <option value="1" selected>是</option>
                                      </c:when>
                                      <c:otherwise>
                                          <option value="1">是</option>
                                      </c:otherwise>
                                  </c:choose>
                                  <c:choose>
                                      <c:when test="${detail.type=='2' }">
                                          <option value="2" selected>否</option>
                                      </c:when>
                                      <c:otherwise>
                                          <option value="2">否</option>
                                      </c:otherwise>
                                  </c:choose>
                              </select>
                          </div>
                          <div class="edit_item">
                          		<label class="edit_label innerMerchantDiv" style="display:none; "><i class="red">*</i>内部公司：</label>
	                              <select class="edit_input innerMerchantDiv" style="display:none; " name="innerMerchantId" id="innerMerchantId">
	                              	<option value="">请选择</option>
                                       <c:forEach items="${merchantList }" var="merchant">
                                           <option value="${merchant.value }">${merchant.label }</option>
                                       </c:forEach>
	                              </select>
                          </div>
                      </div>
                      <div class="edit_box">
                          <div class="edit_item">
                              <label class="edit_label" >经营范围：</label>
                              <textarea class="edit_input" id="operationScope"name="operationScope" >${detail.operationScope }</textarea>
                          </div>
                          <div class="edit_item">
                              <label class="edit_label">传真：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" id="fax" name="fax" value="${detail.fax }">
                          </div>
                          <div class="edit_item">
                              <label class="edit_label">email：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" id="email"name="email" value="${detail.email }">
                          </div>
                      </div>
                      <div class="edit_box">
                          <div class="edit_item">
                              <label class="edit_label">备注：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" id="remark"name="remark" value="${detail.remark }">
                          </div>
                          <div class="edit_item"></div>
                          <div class="edit_item"></div>
                      </div>
                       <div class="edit_box">
                          <div class="edit_item"></div>
                          <div class="edit_item"></div>
                          <div class="edit_item"></div>
                      </div>
                      
                      <ul class="nav nav-tabs">
						<li class="nav-item">
							<a href="#" data-toggle="tab" onclick="changeTable('0');" class="nav-link active" >银行信息</a>
						</li>
						<li class="nav-item">
							<a href="#" data-toggle="tab" onclick="changeTable('1');" class="nav-link" >联系方式</a>
						</li>		
						<li class="nav-item">
							<a href="#" data-toggle="tab" onclick="changeTable('2');" class="nav-link" >资质信息</a>
						</li>	
						<li class="nav-item">
							<a href="#" data-toggle="tab" onclick="changeTable('3');" class="nav-link" >合作公司</a>
						</li>
		           	</ul>
                      
								<div class="tab-content">
                                    <div class="tab-pane fade show active" id="itemList">
                                        
                                        <div id="mytable">
                                       <!--  银行信息 -->
                                        <div class="col-12 ml10">
						                    <div class="row">
						                         <div class="col-1 mt10">
						                          <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick="addCustomerBank();">新增</button>
						                        </div>
						                         <div class="col-1 mt10">
						                          <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="deleteCustomerBank">删 除</button>
						                        </div>
						                    </div>
						                </div> 
                                       
                                          <div class="form-row mt20 ml15">
		                                    <table id="table-list-customerbank" class="table table-striped" cellspacing="0"  width="100%">		                                        
					                      		<tbody>
					                            </tbody>
		                                    </table>
		                               	   </div>                                          
                                        </div>
                                       <div id="mytable1" style="display: none;">
                                          <table class="table table-striped" cellspacing="0" width="100%">
                                              
                                               <div class="title_text">联系方式</div>
						                        <div class="edit_box mt20">
									             <div class="edit_item">
									                 <label class="edit_label">法人代表：</label>
									                 <input type="text" required="" parsley-type="email" class="edit_input" id="legalPerson"name="legalPerson" value="${detail.legalPerson }">
									             </div>
									             <div class="edit_item">
									                 <label class="edit_label">法人国籍：</label>
									                 <input type="text" required="" parsley-type="email" class="edit_input" id="legalNationality"name="legalNationality" value="${detail.legalNationality }">
									             </div>
									             <div class="edit_item"></div>
									         </div>
									         <div class="edit_box">
									             <div class="edit_item">
									                 <label class="edit_label" style="font-size: 13px;">法人代表身份证：</label>
									                 <input type="text" required="" parsley-type="email" class="edit_input" id="legalCardNo"name="legalCardNo" value="${detail.legalCardNo }">
									             </div>
									             <div class="edit_item">
									                 <label class="edit_label">法人电话：</label>
									                 <input type="text" required="" parsley-type="email" class="edit_input" id="legalTel" name="legalTel" value="${detail.legalTel }">
									             </div>
									             <div class="edit_item"></div>
									         </div>
									         <div class="edit_box">
									             <div class="edit_item">
									                 <label class="edit_label">公司电话：</label>
									                 <input type="text" required="" parsley-type="email" class="edit_input" id="oTelNo" name="oTelNo" value="${detail.oTelNo }">
									             </div>
									             <div class="edit_item">
									             </div>
									             <div class="edit_item"></div>
									         </div>
                                               
        
                                            </table>  
                                        </div>
                                        <div id="mytable2" style="display: none;">
                                          <table class="table table-striped" cellspacing="0" width="100%">
                                              
                                             <div class="title_text">资质信息</div>
						                     <div class="row col-12 mt20">
						                         <table class="table table-bordered" cellspacing="0" width="100%">
						                             <thead>
						                             <tr>
						                                 <th>资料名称</th>
						                                 <th>资料图片</th>
						                                 <th>修改人</th>
						                                 <th>修改时间</th>
						                             </tr>
						                             </thead>
						                             <tbody>
						                             <tr>
						                                 <td>营业执照副本</td>
						                                 <td style="height:12px; padding-top: 0px; width:300px">
						                                  <c:choose>
								                         	<c:when test="${not empty customerAptitude.charteredNo}">
								                         		<img style="border:0;margin-top:0px;" src="${customerAptitude.charteredNo}" id="file1img" width="100" height="100" onclick="showimage('${customerAptitude.charteredNo}')"/>
								                         		<button type="button" class="btn btn-gradient btn-file" id="btn-file">修改图片</button>
									                          	<form enctype="multipart/form-data" id="form-upload">
									                          		<input type="file" name="file" id="file" class="btn-hidden">
									                          	</form>
								                         	</c:when>
								                         	<c:otherwise>
								                         		<button type="button" class="btn btn-gradient btn-file" id="btn-file">上传图片</button>
									                          	<form enctype="multipart/form-data" id="form-upload">
									                          		<input type="file" name="file" id="file" class="btn-hidden">
									                          	</form>
								                         	</c:otherwise>
								                         </c:choose>
						                         		</td>
						                                 <td>${customerAptitude.chartModifierName}</td>
						                                 <td><fmt:formatDate value="${customerAptitude.chartModifyDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
						                             </tr>
						                             <tr>
						                                 <td>组织机构代码副本</td>
						                                 <td style="height:12px; padding-top: 0px; width:300px">
						                                 	<c:choose>
						                         	<c:when test="${not empty customerAptitude.organizationCode}">
						                         		<img style="border:0;margin-top:0px;" src="${customerAptitude.organizationCode}" id="file2img" width="100" height="100" onclick="showimage('${customerAptitude.organizationCode}')"/>
						                         		<button type="button" class="btn btn-gradient btn-file" id="btn-file1">修改图片</button>
						                          	<form enctype="multipart/form-data" id="form-upload1">
						                          		<input type="file" name="file" id="file1" class="btn-hidden">
						                          	</form>
						                         	</c:when>
						                         	<c:otherwise>
						                         		<button type="button" class="btn btn-gradient btn-file" id="btn-file1">上传图片</button>
						                          	<form enctype="multipart/form-data" id="form-upload1">
						                          		<input type="file" name="file" id="file1" class="btn-hidden">
						                          	</form>
						                         	</c:otherwise>
						                         </c:choose>
						                     </td>
						                                 <td>${customerAptitude.orgModifierName}</td>
						                                 <td><fmt:formatDate value="${customerAptitude.orgModifyDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
						                             </tr>
						                             <tr>
						                                 <td>注册登记证</td>
						                                 <td style="height:12px; padding-top: 0px; width:300px">
						<c:choose>
						                         	<c:when test="${not empty customerAptitude.registrationNo}">
						                         		<img style="border:0;margin-top:0px;" src="${customerAptitude.registrationNo}" id="file3img" width="100" height="100" onclick="showimage('${customerAptitude.registrationNo}')"/>
						                         		<button type="button" class="btn btn-gradient btn-file" id="btn-file2">修改图片</button>
						                          	<form enctype="multipart/form-data" id="form-upload2">
						                          		<input type="file" name="file" id="file2" class="btn-hidden">
						                          	</form>
						                         	</c:when>
						                         	<c:otherwise>
						                         		<button type="button" class="btn btn-gradient btn-file" id="btn-file2">上传图片</button>
						                          	<form enctype="multipart/form-data" id="form-upload2">
						                          		<input type="file" name="file" id="file2" class="btn-hidden">
						                          	</form>
						                         	</c:otherwise>
						                         </c:choose>
												</td>
						                                    <td>${customerAptitude.registModifierName}</td>
						                                 <td><fmt:formatDate value="${customerAptitude.registModifyDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
						                             </tr>
						                             <tr>
						                                 <td>供货记录</td>
						                                 <td style="height:12px; padding-top: 0px; width:300px">
						                                 	<c:choose>
						                         	<c:when test="${not empty customerAptitude.supplyRecord}">
						                         		<img style="border:0;margin-top:0px;" src="${customerAptitude.supplyRecord}" id="file4img" width="100" height="100" onclick="showimage('${customerAptitude.supplyRecord}')"/>
						                         		<button type="button" class="btn btn-gradient btn-file" id="btn-file3">修改图片</button>
						                          	<form enctype="multipart/form-data" id="form-upload3">
						                          		<input type="file" name="file" id="file3" class="btn-hidden">
						                          	</form>
						                         	</c:when>
						                         	<c:otherwise>
						                         		<button type="button" class="btn btn-gradient btn-file" id="btn-file3">上传图片</button>
						                          	<form enctype="multipart/form-data" id="form-upload3">
						                          		<input type="file" name="file" id="file3" class="btn-hidden">
						                          	</form>
						                         	</c:otherwise>
						                         </c:choose>
						                                 </td>
						                                 <td>${customerAptitude.supplyModifierName}</td>
						                                 <td><fmt:formatDate value="${customerAptitude.supplyModifyDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
						                             </tr>
						                             <tr>
						                                 <td>证明信息</td>
						                                 <td style="height:12px; padding-top: 0px; width:300px">
						                                 	<c:choose>
						                         	<c:when test="${not empty customerAptitude.proofInfo}">
						                         		<img style="border:0;margin-top:0px;" src="${customerAptitude.proofInfo}" id="file5img" width="100" height="100" onclick="showimage('${customerAptitude.proofInfo}')"/>
						                         		<button type="button" class="btn btn-gradient btn-file" id="btn-file4">修改图片</button>
						                          	<form enctype="multipart/form-data" id="form-upload4">
						                          		<input type="file" name="file" id="file4" class="btn-hidden">
						                          	</form>
						                         	</c:when>
						                         	<c:otherwise>
						                         		<button type="button" class="btn btn-gradient btn-file" id="btn-file4">上传图片</button>
						                          	<form enctype="multipart/form-data" id="form-upload4">
						                          		<input type="file" name="file" id="file4" class="btn-hidden">
						                          	</form>
						                         	</c:otherwise>
						                         </c:choose>
						                                 </td>
						                                 <td>${customerAptitude.proofModifierName}</td>
						                                 <td><fmt:formatDate value="${customerAptitude.proofModifyDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
						                             </tr>
						                             <tr>
						                                 <td>品牌授权</td>
						                                 <td style="height:12px; padding-top: 0px; width:300px">
						                                 	<c:choose>
						                         	<c:when test="${not empty customerAptitude.brandAuthorization}">
						                         		<img style="border:0;margin-top:0px;" src="${customerAptitude.brandAuthorization}" id="file6img" width="100" height="100" onclick="showimage('${customerAptitude.brandAuthorization}')"/>
						                         		<button type="button" class="btn btn-gradient btn-file" id="btn-file5">修改图片</button>
						                          	<form enctype="multipart/form-data" id="form-upload5">
						                          		<input type="file" name="file" id="file5" class="btn-hidden">
						                          	</form>
						                         	</c:when>
						                         	<c:otherwise>
						                         		<button type="button" class="btn btn-gradient btn-file" id="btn-file5">上传图片</button>
						                          	<form enctype="multipart/form-data" id="form-upload5">
						                          		<input type="file" name="file" id="file5" class="btn-hidden">
						                          	</form>
						                         	</c:otherwise>
						                         </c:choose>
						                                 </td>
						                                 <td>${customerAptitude.brandModifierName}</td>
						                                 <td><fmt:formatDate value="${customerAptitude.brandModifyDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
						                             </tr>
						                             <tr>
						                                 <td>银行流水</td>
						                                 <td style="height:12px; padding-top: 0px; width:300px">
						                                 	<c:choose>
						                         	<c:when test="${not empty customerAptitude.bankFlow}">
						                         		<img style="border:0;margin-top:0px;" src="${customerAptitude.bankFlow}" id="file7img" width="100" height="100" onclick="showimage('${customerAptitude.bankFlow}')"/>
						                         		<button type="button" class="btn btn-gradient btn-file" id="btn-file6">修改图片</button>
						                          	<form enctype="multipart/form-data" id="form-upload6">
						                          		<input type="file" name="file" id="file6" class="btn-hidden">
						                          	</form>
						                         	</c:when>
						                         	<c:otherwise>
						                         		<button type="button" class="btn btn-gradient btn-file" id="btn-file6">上传图片</button>
						                          	<form enctype="multipart/form-data" id="form-upload6">
						                          		<input type="file" name="file" id="file6" class="btn-hidden">
						                          	</form>
						                         	</c:otherwise>
						                         </c:choose>
						                                 </td>
						                                 <td>${customerAptitude.bankModifierName}</td>
						                                 <td><fmt:formatDate value="${customerAptitude.bankModifyDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
						                             </tr>
						                             <tr>
						                                 <td>企业备案表</td>
						                                 <td style="height:12px; padding-top: 0px; width:300px">
						                                 	<c:choose>
						                         	<c:when test="${not empty customerAptitude.keepRecord}">
						                         		<img style="border:0;margin-top:0px;" src="${customerAptitude.keepRecord}" id="file8img" width="100" height="100" onclick="showimage('${customerAptitude.keepRecord}')"/>
						                         		<button type="button" class="btn btn-gradient btn-file" id="btn-file7">修改图片</button>
						                          	<form enctype="multipart/form-data" id="form-upload7">
						                          		<input type="file" name="file" id="file7" class="btn-hidden">
						                          	</form>
						                         	</c:when>
						                         	<c:otherwise>
						                         		<button type="button" class="btn btn-gradient btn-file" id="btn-file7">上传图片</button>
						                          	<form enctype="multipart/form-data" id="form-upload7">
						                          		<input type="file" name="file" id="file7" class="btn-hidden">
						                          	</form>
						                         	</c:otherwise>
						                         </c:choose>
						                                 </td>
						                                 <td>${customerAptitude.keepModifierName}</td>
						                                 <td><fmt:formatDate value="${customerAptitude.keepModifyDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
						                             </tr>
						                             <tr>
						                                 <td>法人身份证</td>
						                                 <td style="height:12px; padding-top: 0px; width:300px">
						                                 	<c:choose>
						                         	<c:when test="${not empty customerAptitude.legalCardNo}">
						                         		<img style="border:0;margin-top:0px;" src="${customerAptitude.legalCardNo}" id="file9img" width="100" height="100" onclick="showimage('${customerAptitude.legalCardNo}')"/>
						                         		<button type="button" class="btn btn-gradient btn-file" id="btn-file8">修改图片</button>
						                          	<form enctype="multipart/form-data" id="form-upload8">
						                          		<input type="file" name="file" id="file8" class="btn-hidden">
						                          	</form>
						                         	</c:when>
						                         	<c:otherwise>
						                         		<button type="button" class="btn btn-gradient btn-file" id="btn-file8">上传图片</button>
						                          	<form enctype="multipart/form-data" id="form-upload8">
						                          		<input type="file" name="file" id="file8" class="btn-hidden">
						                          	</form>
						                         	</c:otherwise>
						                         </c:choose>
						                                 </td>
						                                 <td>${customerAptitude.legalModifierName}</td>
						                                 <td><fmt:formatDate value="${customerAptitude.legalModifyDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
						                             </tr>
						                             </tbody>
						                         </table>
						                     </div>
                                               
                                               
        
                                            </table>  
                                        </div>

                                        <div id="mytable3" style="display: none;">
                                         <div class="col-12 ml10">
						                    <div class="row">
						                         <div class="col-1 mt10">
						                          <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick="addMerchantRel();">新增</button>
						                        </div>
						                         <div class="col-1 mt10">
						                          <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="deleteMerchantRel">删 除</button>
						                        </div>
						                    </div>
						                    </div> 
                                           
		                                   <div class="form-row mt20 ml15">
		                                    <table id="table-list-group" class="table table-striped" cellspacing="0"  width="100%">		                                        
					                      		<tbody>
					                            </tbody>
		                                    </table>
		                               	   </div>
                                            
                                        </div>
                                    </div>
                                    <div class="tab-pane fade" id="batchDefault"></div>
                                </div>                      
                      
                      

			         </form>
        <jsp:include page="/WEB-INF/views/modals/16002.jsp" />
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
<div id="ShowImage_Form" class="modal hide">         
         <div class="modal-header">
             <button data-dismiss="modal" class="close" type="button"></button>
         </div>
           <div class="modal-body">
            <div id="img_show">
            </div>
        </div>
    </div>
</div> <!-- content -->
<script src='/resources/js/system/base.js' type="text/javascript"></script>
<script type="text/javascript">
	$(function(){
		// 默认访问第一个页面
		changeTable(0);
		$module.currency.loadSelectData.call($("select[name='currency']")[0],"");
		//币种赋值
		var currency = $("#currency_hidden").val() ;
		if(currency){
			$("#currency").val(currency) ;
		}
		
		
		if($("#type").val() == '1'){
			$(".innerMerchantDiv").show() ;
			
			var innerMerchantId = '${detail.innerMerchantId}' ;
			$("#innerMerchantId").val(innerMerchantId) ;
		}
		
		$("#type").change(function(){
    		var val = $("#type").val() ;
    		
    		if('1' == val){
    			$(".innerMerchantDiv").show() ;
    		}else{
    			$("#innerMerchantId").val('');
    			$(".innerMerchantDiv").hide() ;
    		}
    	}) ;
		
		//保存按钮
		$("#save-buttons").click(function(){
			//var form = $("#edit-form").serializeArray();
			var obj = {};
 			var id = $("#id").val();
 			obj['id']=id;
 			var name = $("#name").val();
 			obj['name']=name;
 			var simpleName = $("#simpleName").val();
 			obj['simpleName']=simpleName;
 			var creditCode = $("#creditCode").val();
 			obj['creditCode']=creditCode;
 			var orgCode = $("#orgCode").val();
 			obj['orgCode']=orgCode;
 			var codeGrade = $("#codeGrade").val();
 			obj['codeGrade']=codeGrade;
 			var nature = $("#nature").val();
 			obj['nature']=nature;
 			var chinaName = $("#chinaName").val();
 			obj['chinaName']=chinaName;
 			var enSimpleName = $("#enSimpleName").val();
 			obj['enSimpleName']=enSimpleName;
 			var enName = $("#enName").val();
 			obj['enName']=enName;
 			var mainId = $("#mainId").val();
 			obj['mainId']=mainId;
 			var salesman = $("#salesman").val();
 			obj['salesman']=salesman;
 			var cityCode = $("#cityCode").val();
 			obj['cityCode']=cityCode;
 			var companyAddr = $("#companyAddr").val();
 			obj['companyAddr']=companyAddr;
 			var businessAddress = $("#businessAddress").val();
 			obj['businessAddress']=businessAddress;
 			var enBusinessAddress = $("#enBusinessAddress").val();
 			obj['enBusinessAddress']=enBusinessAddress;
 			var currency = $("#currency").val();
 			obj['currency']=currency;
 			var currency_hidden = $("#currency_hidden").val();
 			obj['currency_hidden']=currency_hidden;
 			var type = $("#type").val();
 			obj['type']=type;
 			var innerMerchantId = $("#innerMerchantId").val();
 			obj['innerMerchantId']=innerMerchantId;
 			var operationScope = $("#operationScope").val();
 			obj['operationScope']=operationScope;
 			var fax = $("#fax").val();
 			obj['fax']=fax;
 			var email = $("#email").val();
 			obj['email']=email;			
 			var remark = $("#remark").val();
 			obj['remark']=remark;
 			var depositBank = $("#depositBank").val();
 			obj['depositBank']=depositBank;
 			var bankAccount = $("#bankAccount").val();
 			obj['bankAccount']=bankAccount;
 			var beneficiaryName = $("#beneficiaryName").val();
 			obj['beneficiaryName']=beneficiaryName;
 			var bankAddress = $("#bankAddress").val();
 			obj['bankAddress']=bankAddress;
 			var swiftCode = $("#swiftCode").val();
 			obj['swiftCode']=swiftCode;
 			var legalPerson = $("#legalPerson").val();
 			obj['legalPerson']=legalPerson;
 			var legalNationality = $("#legalNationality").val();
 			obj['legalNationality']=legalNationality;
 			var legalCardNo = $("#legalCardNo").val();
 			obj['legalCardNo']=legalCardNo;
 			var legalTel = $("#legalTel").val();
 			obj['legalTel']=legalTel;
 			var oTelNo = $("#oTelNo").val();
 			obj['oTelNo']=oTelNo;

			
			//必填项校验
			if($("#name").val()==""){
				$custom.alert.error("客户名称不能为空！");
				return ;
			}
			if($("#creditCode").val()==""){
				$custom.alert.error("营业执照号不能为空！");
				return ;
			}
			if($("#orgCode").val()==""){
				$custom.alert.error("组织机构代码不能为空！");
				return ;
			}
			if($("#type").val()==""){
				$custom.alert.error("是否内部公司不能为空！");
				return ;
			}
			
			if($("#type").val()=="1" && $("#innerMerchantId").val() == ""){
				$custom.alert.error("请选择内部公司！");
				return ;
			}
			
			var itemBankList = new Array();
			$('#table-list-customerbank tbody tr').each(function(i){// 遍历 tr
            	debugger;
				var item ={};
				var aa=i;
				
				
				var ss=$(this);
				//var ss1=$(this).find("#bankMerchantIds']").val();
				var bankMerchantIdsArr = $(this).find('select[id="bankMerchantIds"]').val();//商家id
		
		        var bankMerchantIdsStr='';// 多个商家用逗号隔开
		        if(bankMerchantIdsArr!='' && bankMerchantIdsArr!=null){
		        	bankMerchantIdsStr=bankMerchantIdsArr.toString();
		        }
		        item['bankMerchantIdsStr']=bankMerchantIdsStr;

				var depositBank = $(this).find('input[name="depositBank"]').val();//开户银行
				item['depositBank']=depositBank;
				var bankAccount = $(this).find('input[name="bankAccount"]').val();// 银行账号
				item['bankAccount']=bankAccount;
				var beneficiaryName = $(this).find('input[name="beneficiaryName"]').val();//银行账户
				item['beneficiaryName']=beneficiaryName;
				var bankAddress = $(this).find('input[name="bankAddress"]').val();// 开户行地址
				item['bankAddress']=bankAddress;
				var swiftCode = $(this).find('input[name="swiftCode"]').val();// Swift Code
				item['swiftCode']=swiftCode;
			
				itemBankList.push(item);
			});
			
			
            var itemList = new Array();
            var falg="";
            $('#table-list-group tbody tr').each(function(i){// 遍历 tr
            	debugger;
				var item ={};			
				var merchantId = $(this).find('input[name="merchantId"]').val();//商家id
				item['merchantId']=merchantId;
				var merchantName = $(this).find('input[name="merchantName"]').val();// 商家名称
				item['merchantName']=merchantName;


				var purchasePriceManage ="0";// 是否启用销售价格管理 1-启用 0-不启用
				if ($(this).find('input[name="purchasePriceManage"]').is(':checked')==true) { 
					purchasePriceManage='1';
				} 
				item['purchasePriceManage']=purchasePriceManage;
				var tariffRateConfigId = $(this).find('select[name="tariffRateConfigId"]').val();
				item['tariffRateConfigId']=tariffRateConfigId;
				if(!tariffRateConfigId){// 标识税率必填标识
					falg="1";
				}
				itemList.push(item);
			});
            if("1"==falg){
				$custom.alert.error("请选择税率！");
				return ;
			}
            var json = {};
            json['itemBankList']=itemBankList;
 			var deleteIds = $("#deleteIds").val();
 			json['deleteIds']=deleteIds;
            json['merchantList']=itemList;
			var jsonObject= JSON.stringify(json); 
			obj['json']=jsonObject;
			
			
            var url = "/supplier/modifySupplier.asyn";
			$custom.request.ajax(url, obj, function(result){
				if(result.state == '200'){
					//1成功 0失败
					if (result.data.status=='1') {
						$custom.alert.success("编辑成功！");
						setTimeout(function () {
							$load.a("/supplier/toPage.html");
						}, 1000);
					}else {
						$custom.alert.error(result.data.massage);
					}										
				}else{
					$custom.alert.error(result.message);
				}
			});
		});
		
		//取消按钮
		$("#cancel-buttons").click(function(){
			$load.a("/supplier/toPage.html");
		});
		
		//点击上传文件
		$("#btn-file").click(function(){
			var input = '<input type="file" name="file" id="file" class="btn-hidden file-import">';
			$("#form-upload").empty();
			$("#form-upload").append(input);
			$("#file").click();
		});

		//上传文件到后端
		$("#form-upload").on("change",'.file-import',function(){
			//判断是否为图片格式
			var data = $(this).val().split(".");
			var suffix = data[data.length-1].toLowerCase();
			if(suffix=="bmp" || suffix=="jpg" || suffix=="jpeg" || suffix=="png" || suffix=="tif" || suffix=="gif" 
					|| suffix=="pcx" || suffix=="tga" || suffix=="exif" || suffix=="fpx" || suffix=="svg" 
					|| suffix=="psd"|| suffix=="cdr"|| suffix=="pcd"|| suffix=="dxf" || suffix=="ufo" || suffix=="eps"
					|| suffix=="ai" || suffix=="raw" || suffix=="wmf" || suffix=="webp"){
				var formData = new FormData($("#form-upload")[0]); 
				var id = $('#id').val();
		        var type = "1";
		        formData.append("id",id);
		        formData.append("type",type);
				$custom.request.ajaxUpload("/supplier/uploadFile.asyn", formData, function(result){
					if(result.state == '200'){
						if($('#file1img').length > 0){
							$("#file1img").remove();
						}
						$('#btn-file').before('<img style="border:0;margin-top:0px;" src="'+result.data+'" id="file1img" width="100" height="100" onclick="showimage(\''+result.data+'\')"/> ');
						$('#btn-file').html("修改图片");
						$custom.alert.success("上传完成！");
					}else{
						$custom.alert.error("上传失败！");
					}
				});
			}else{
				$custom.alert.error("请上传正确格式的图片");
			}
		});
		//------------------------营业执照副本上传文件end-------------------------

		//------------------------组织机构代码副本上传文件-------------------------
		//点击上传文件
		$("#btn-file1").click(function(){
			var input = '<input type="file" name="file" id="file1" class="btn-hidden file-import">';
			$("#form-upload1").empty();
			$("#form-upload1").append(input);
			$("#file1").click();
		});

		//上传文件到后端
		$("#form-upload1").on("change",'.file-import',function(){
			//判断是否为图片格式
			var data = $(this).val().split(".");
			var suffix = data[data.length-1].toLowerCase();
			if(suffix=="bmp" || suffix=="jpg" || suffix=="jpeg" || suffix=="png" || suffix=="tif" || suffix=="gif" 
					|| suffix=="pcx" || suffix=="tga" || suffix=="exif" || suffix=="fpx" || suffix=="svg" 
					|| suffix=="psd"|| suffix=="cdr"|| suffix=="pcd"|| suffix=="dxf" || suffix=="ufo" || suffix=="eps"
					|| suffix=="ai" || suffix=="raw" || suffix=="wmf" || suffix=="webp"){
				var formData = new FormData($("#form-upload1")[0]); 
				var id = $('#id').val();
		        var type = "2";
		        formData.append("id",id);
		        formData.append("type",type);
				$custom.request.ajaxUpload("/supplier/uploadFile.asyn", formData, function(result){
					if(result.state == '200'){
						if($('#file2img').length > 0){
							$("#file2img").remove();
						}
						$('#btn-file1').before('<img style="border:0;margin-top:0px;" src="'+result.data+'" id="file2img" width="100" height="100" onclick="showimage(\''+result.data+'\')"/> ');
						$('#btn-file1').html("修改图片");
						$custom.alert.success("上传完成！");
					}else{
						$custom.alert.error("上传失败！");
					}
				});
			}else{
				$custom.alert.error("请上传正确格式的图片");
			}
		});
		//------------------------组织机构代码副本上传文件end-------------------------

		//------------------------注册登记证上传文件-------------------------
		//点击上传文件
		$("#btn-file2").click(function(){
			var input = '<input type="file" name="file" id="file2" class="btn-hidden file-import">';
			$("#form-upload2").empty();
			$("#form-upload2").append(input);
			$("#file2").click();
		});

		//上传文件到后端
		$("#form-upload2").on("change",'.file-import',function(){
			//判断是否为图片格式
			var data = $(this).val().split(".");
			var suffix = data[data.length-1].toLowerCase();
			if(suffix=="bmp" || suffix=="jpg" || suffix=="jpeg" || suffix=="png" || suffix=="tif" || suffix=="gif" 
					|| suffix=="pcx" || suffix=="tga" || suffix=="exif" || suffix=="fpx" || suffix=="svg" 
					|| suffix=="psd"|| suffix=="cdr"|| suffix=="pcd"|| suffix=="dxf" || suffix=="ufo" || suffix=="eps"
					|| suffix=="ai" || suffix=="raw" || suffix=="wmf" || suffix=="webp"){
				var formData = new FormData($("#form-upload2")[0]); 
				var id = $('#id').val();
		        var type = "3";
		        formData.append("id",id);
		        formData.append("type",type);
				$custom.request.ajaxUpload("/supplier/uploadFile.asyn", formData, function(result){
					if(result.state == '200'){
						if($('#file3img').length > 0){
							$("#file3img").remove();
						}
						$('#btn-file2').before('<img style="border:0;margin-top:0px;" src="'+result.data+'" id="file3img" width="100" height="100" onclick="showimage(\''+result.data+'\')"/> ');
						$('#btn-file2').html("修改图片");
						$custom.alert.success("上传完成！");
					}else{
						$custom.alert.error("上传失败！");
					}
				});
			}else{
				$custom.alert.error("请上传正确格式的图片");
			}
		});
		//------------------------注册登记证上传文件end-------------------------

		//------------------------供货记录上传文件-------------------------
		//点击上传文件
		$("#btn-file3").click(function(){
			var input = '<input type="file" name="file" id="file3" class="btn-hidden file-import">';
			$("#form-upload3").empty();
			$("#form-upload3").append(input);
			$("#file3").click();
		});

		//上传文件到后端
		$("#form-upload3").on("change",'.file-import',function(){
			//判断是否为图片格式
			var data = $(this).val().split(".");
			var suffix = data[data.length-1].toLowerCase();
			if(suffix=="bmp" || suffix=="jpg" || suffix=="jpeg" || suffix=="png" || suffix=="tif" || suffix=="gif" 
					|| suffix=="pcx" || suffix=="tga" || suffix=="exif" || suffix=="fpx" || suffix=="svg" 
					|| suffix=="psd"|| suffix=="cdr"|| suffix=="pcd"|| suffix=="dxf" || suffix=="ufo" || suffix=="eps"
					|| suffix=="ai" || suffix=="raw" || suffix=="wmf" || suffix=="webp"){
				var formData = new FormData($("#form-upload3")[0]); 
				var id = $('#id').val();
		        var type = "4";
		        formData.append("id",id);
		        formData.append("type",type);
				$custom.request.ajaxUpload("/supplier/uploadFile.asyn", formData, function(result){
					if(result.state == '200'){
						if($('#file4img').length > 0){
							$("#file4img").remove();
						}
						$('#btn-file3').before('<img style="border:0;margin-top:0px;" src="'+result.data+'" id="file4img" width="100" height="100" onclick="showimage(\''+result.data+'\')"/> ');
						$('#btn-file3').html("修改图片");
						$custom.alert.success("上传完成！");
					}else{
						$custom.alert.error("上传失败！");
					}
				});
			}else{
				$custom.alert.error("请上传正确格式的图片");
			}
		});
		//------------------------供货记录上传文件end-------------------------

		//------------------------证明信息上传文件-------------------------
		//点击上传文件
		$("#btn-file4").click(function(){
			var input = '<input type="file" name="file" id="file4" class="btn-hidden file-import">';
			$("#form-upload4").empty();
			$("#form-upload4").append(input);
			$("#file4").click();
		});

		//上传文件到后端
		$("#form-upload4").on("change",'.file-import',function(){
			//判断是否为图片格式
			var data = $(this).val().split(".");
			var suffix = data[data.length-1].toLowerCase();
			if(suffix=="bmp" || suffix=="jpg" || suffix=="jpeg" || suffix=="png" || suffix=="tif" || suffix=="gif" 
					|| suffix=="pcx" || suffix=="tga" || suffix=="exif" || suffix=="fpx" || suffix=="svg" 
					|| suffix=="psd"|| suffix=="cdr"|| suffix=="pcd"|| suffix=="dxf" || suffix=="ufo" || suffix=="eps"
					|| suffix=="ai" || suffix=="raw" || suffix=="wmf" || suffix=="webp"){
				var formData = new FormData($("#form-upload4")[0]); 
				var id = $('#id').val();
		        var type = "5";
		        formData.append("id",id);
		        formData.append("type",type);
				$custom.request.ajaxUpload("/supplier/uploadFile.asyn", formData, function(result){
					if(result.state == '200'){
						if($('#file5img').length > 0){
							$("#file5img").remove();
						}
						$('#btn-file4').before('<img style="border:0;margin-top:0px;" src="'+result.data+'" id="file5img" width="100" height="100" onclick="showimage(\''+result.data+'\')"/> ');
						$('#btn-file4').html("修改图片");
						$custom.alert.success("上传完成！");
					}else{
						$custom.alert.error("上传失败！");
					}
				});
			}else{
				$custom.alert.error("请上传正确格式的图片");
			}
		});
		//------------------------证明信息上传文件end-------------------------

		//------------------------品牌授权上传文件-------------------------
		//点击上传文件
		$("#btn-file5").click(function(){
			var input = '<input type="file" name="file" id="file5" class="btn-hidden file-import">';
			$("#form-upload5").empty();
			$("#form-upload5").append(input);
			$("#file5").click();
		});

		//上传文件到后端
		$("#form-upload5").on("change",'.file-import',function(){
			//判断是否为图片格式
			var data = $(this).val().split(".");
			var suffix = data[data.length-1].toLowerCase();
			if(suffix=="bmp" || suffix=="jpg" || suffix=="jpeg" || suffix=="png" || suffix=="tif" || suffix=="gif" 
					|| suffix=="pcx" || suffix=="tga" || suffix=="exif" || suffix=="fpx" || suffix=="svg" 
					|| suffix=="psd"|| suffix=="cdr"|| suffix=="pcd"|| suffix=="dxf" || suffix=="ufo" || suffix=="eps"
					|| suffix=="ai" || suffix=="raw" || suffix=="wmf" || suffix=="webp"){
				var formData = new FormData($("#form-upload5")[0]); 
				var id = $('#id').val();
		        var type = "6";
		        formData.append("id",id);
		        formData.append("type",type);
				$custom.request.ajaxUpload("/supplier/uploadFile.asyn", formData, function(result){
					if(result.state == '200'){
						if($('#file6img').length > 0){
							$("#file6img").remove();
						}
						$('#btn-file5').before('<img style="border:0;margin-top:0px;" src="'+result.data+'" id="file6img" width="100" height="100" onclick="showimage(\''+result.data+'\')"/> ');
						$('#btn-file5').html("修改图片");
						$custom.alert.success("上传完成！");
					}else{
						$custom.alert.error("上传失败！");
					}
				});
			}else{
				$custom.alert.error("请上传正确格式的图片");
			}
		});
		//------------------------品牌授权上传文件end-------------------------

		//------------------------银行流水上传文件-------------------------
		//点击上传文件
		$("#btn-file6").click(function(){
			var input = '<input type="file" name="file" id="file6" class="btn-hidden file-import">';
			$("#form-upload6").empty();
			$("#form-upload6").append(input);
			$("#file6").click();
		});

		//上传文件到后端
		$("#form-upload6").on("change",'.file-import',function(){
			//判断是否为图片格式
			var data = $(this).val().split(".");
			var suffix = data[data.length-1].toLowerCase();
			if(suffix=="bmp" || suffix=="jpg" || suffix=="jpeg" || suffix=="png" || suffix=="tif" || suffix=="gif" 
					|| suffix=="pcx" || suffix=="tga" || suffix=="exif" || suffix=="fpx" || suffix=="svg" 
					|| suffix=="psd"|| suffix=="cdr"|| suffix=="pcd"|| suffix=="dxf" || suffix=="ufo" || suffix=="eps"
					|| suffix=="ai" || suffix=="raw" || suffix=="wmf" || suffix=="webp"){
				var formData = new FormData($("#form-upload6")[0]); 
				var id = $('#id').val();
		        var type = "7";
		        formData.append("id",id);
		        formData.append("type",type);
				$custom.request.ajaxUpload("/supplier/uploadFile.asyn", formData, function(result){
					if(result.state == '200'){
						if($('#file7img').length > 0){
							$("#file7img").remove();
						}
						$('#btn-file6').before('<img style="border:0;margin-top:0px;" src="'+result.data+'" id="file7img" width="100" height="100" onclick="showimage(\''+result.data+'\')"/> ');
						$('#btn-file6').html("修改图片");
						$custom.alert.success("上传完成！");
					}else{
						$custom.alert.error("上传失败！");
					}
				});
			}else{
				$custom.alert.error("请上传正确格式的图片");
			}
		});
		//------------------------银行流水上传文件end-------------------------

		//------------------------企业备案表上传文件-------------------------
		//点击上传文件
		$("#btn-file7").click(function(){
			var input = '<input type="file" name="file" id="file7" class="btn-hidden file-import">';
			$("#form-upload7").empty();
			$("#form-upload7").append(input);
			$("#file7").click();
		});

		//上传文件到后端
		$("#form-upload7").on("change",'.file-import',function(){
			//判断是否为图片格式
			var data = $(this).val().split(".");
			var suffix = data[data.length-1].toLowerCase();
			if(suffix=="bmp" || suffix=="jpg" || suffix=="jpeg" || suffix=="png" || suffix=="tif" || suffix=="gif" 
					|| suffix=="pcx" || suffix=="tga" || suffix=="exif" || suffix=="fpx" || suffix=="svg" 
					|| suffix=="psd"|| suffix=="cdr"|| suffix=="pcd"|| suffix=="dxf" || suffix=="ufo" || suffix=="eps"
					|| suffix=="ai" || suffix=="raw" || suffix=="wmf" || suffix=="webp"){
				var formData = new FormData($("#form-upload7")[0]); 
				var id = $('#id').val();
		        var type = "8";
		        formData.append("id",id);
		        formData.append("type",type);
				$custom.request.ajaxUpload("/supplier/uploadFile.asyn", formData, function(result){
					if(result.state == '200'){
						if($('#file8img').length > 0){
							$("#file8img").remove();
						}
						$('#btn-file7').before('<img style="border:0;margin-top:0px;" src="'+result.data+'" id="file8img" width="100" height="100" onclick="showimage(\''+result.data+'\')"/> ');
						$('#btn-file7').html("修改图片");
						$custom.alert.success("上传完成！");
					}else{
						$custom.alert.error("上传失败！");
					}
				});
			}else{
				$custom.alert.error("请上传正确格式的图片");
			}
		});
		//------------------------企业备案表上传文件end-------------------------

		//------------------------法人身份证上传文件-------------------------
		//点击上传文件
		$("#btn-file8").click(function(){
			var input = '<input type="file" name="file" id="file8" class="btn-hidden file-import">';
			$("#form-upload8").empty();
			$("#form-upload8").append(input);
			$("#file8").click();
		});

		//上传文件到后端
		$("#form-upload8").on("change",'.file-import',function(){
			//判断是否为图片格式
			var data = $(this).val().split(".");
			var suffix = data[data.length-1].toLowerCase();
			if(suffix=="bmp" || suffix=="jpg" || suffix=="jpeg" || suffix=="png" || suffix=="tif" || suffix=="gif" 
					|| suffix=="pcx" || suffix=="tga" || suffix=="exif" || suffix=="fpx" || suffix=="svg" 
					|| suffix=="psd"|| suffix=="cdr"|| suffix=="pcd"|| suffix=="dxf" || suffix=="ufo" || suffix=="eps"
					|| suffix=="ai" || suffix=="raw" || suffix=="wmf" || suffix=="webp"){
				var formData = new FormData($("#form-upload8")[0]); 
				var id = $('#id').val();
		        var type = "9";
		        formData.append("id",id);
		        formData.append("type",type);
				$custom.request.ajaxUpload("/supplier/uploadFile.asyn", formData, function(result){
					if(result.state == '200'){
						if($('#file9img').length > 0){
							$("#file9img").remove();
						}
						$('#btn-file8').before('<img style="border:0;margin-top:0px;" src="'+result.data+'" id="file9img" width="100" height="100" onclick="showimage(\''+result.data+'\')"/> ');
						$('#btn-file8').html("修改图片");
						$custom.alert.success("上传完成！");
					}else{
						$custom.alert.error("上传失败！");
					}
				});
			}else{
				$custom.alert.error("请上传正确格式的图片");
			}
		});
		//------------------------法人身份证上传文件end-------------------------
		
	});
	
	//显示大图    
	function showimage(source){
	      $("#ShowImage_Form").find("#img_show").html("<center><image src='"+source+"' class='carousel-inner img-responsive img-rounded' style='width:950px;height:950px;'/></center>");
	      $("#ShowImage_Form").modal();
	}
	//关闭大图
	$("#ShowImage_Form").click(function(){//再次点击淡出消失弹出层
		 $("#ShowImage_Form").modal('hide');  //手动关闭
	});
	
	
	
	 //新增
	 function addMerchantRel(){
		 $m16002.init() ;
	 } 
	 
		//删除行
	 $("#deleteMerchantRel").click(function() {
		 debugger;
	 		var merchantIds = [];
	        $("input[name='item-checkbox']:checked").each(function() { // 遍历选中的checkbox
	            n = $(this).parents("tr").index()+1;  // 获取checkbox所在行的顺序
	            var merchantId = $(this).parent().find("input[name='customerMerchantRelId']").val();
	            if(merchantId){
	            	merchantIds.push(merchantId);
	            }
	            $("#table-list-group").find("tr:eq("+n+")").remove();
	        });
	        $("#deleteIds").val(merchantIds);
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
	
	var addNum=0;// 用于计算整个 客户银行的行数
	function changeTable(type) {
		debugger;
        if (type == '0') {
        	

        	var customerId = $("#id").val() ;
        	$("#table-list-customerbank").empty();
        	debugger;
        	$custom.request.ajax("/customerBank/getCustomerBankList.asyn", {"customerId": customerId}, function(res){
        		var list=res.data;
        		debugger;
        		if(res.state == 200){	
        			var html2 = '<thead><tr><th><input id="checkbox11" type="checkbox" name="bankall"></th><th>开户银行</th><th>银行账号</th><th>银行账户</th><th>开户行地址</th><th>Swift Code</th><th>合作公司</th></tr></thead>';
        			html2=html2+'<tbody>';
        			
        			var strArray= [];
        			var strMerchantIdArray= [];
        			
        			$(list).each(function(index,event){
        				debugger;
        				
        				var merchantIdStr =event.merchantIdStr;
        				var depositBank =event.depositBank;
        				var bankAccount =event.bankAccount;
        				var beneficiaryName =event.beneficiaryName;
        				var bankAddress =event.bankAddress;
        				var swiftCode =event.swiftCode;
        				
        				debugger;
        				addNum+=1;
        				var bankMerchantIds="bankMerchantIds"+addNum.toString(); // 下拉框是根据name赋值 name要 
        				var str="select[name='"+bankMerchantIds+"']";
        				strMerchantIdArray.push(merchantIdStr);
        				strArray.push(str);
        				var bankMerchantHtml='<td><select   multiple  name="'+bankMerchantIds+'"  id="bankMerchantIds" title="请选择"  ></td>';			

        				        				
        				
        				
        				html2+="<tr>"+
    	            	"<td> <input type='checkbox' class='iCheck' name='item-bank-checkbox'></td>"+
                    	"<td><input type='text' name='depositBank' value='"+(depositBank==null?'':depositBank)+"'/></td>"+
                    	"<td><input type='text' name='bankAccount' value='"+(bankAccount==null?'':bankAccount)+"'/></td>"+
                    	"<td><input type='text' name='beneficiaryName' value='"+(beneficiaryName==null?'':beneficiaryName)+"'/></td>"+
                    	"<td><input type='text' name='bankAddress' value='"+(bankAddress==null?'':bankAddress)+"'/></td>"+
                    	"<td><input type='text' name='swiftCode' value='"+(swiftCode==null?'':swiftCode)+"'/></td>"+
                    	

                    	bankMerchantHtml+
                        "</tr>";
        				
 
					}) ;
        			html2=html2+'</tbody>';
        			$("#table-list-customerbank").append(html2);
        			
        			for(var i in strArray){
        				debugger;
        				var str=strArray[i];
        				var merchantIds=strMerchantIdArray[i];
        				$module.merchant.getUserMerchantSelectBeanURL.call($(str)[0],merchantIds);
        			}
        			$("#mytable").show();
                    $("#mytable1").hide();
                    $("#mytable2").hide();
                    $("#mytable3").hide();
        		}else{
        			$custom.alert.error	(res.expMessage);
        		}
        	});
        	
        	

            
        }else  if (type == '1') {
            $("#mytable1").show();
            $("#mytable").hide();
            $("#mytable2").hide();
            $("#mytable3").hide();
        }else  if (type == '2') {
            $("#mytable2").show();
            $("#mytable").hide();
            $("#mytable1").hide();
            $("#mytable3").hide();
        }else  if (type == '3') {
        	
        	debugger;
        	var customerId = $("#id").val() ;
        	$("#table-list-group").empty();
        	$custom.request.ajax("/customer/getCustomerMerchantRelList.asyn", {"customerId": customerId}, function(res){
        		var list=res.data.relList;
        		var settlementTypeList=res.data.settlementTypeList;
        		var accountPeriodList=res.data.accountPeriodList;
        		var tariffRateConfigList=res.data.tariffRateConfigList;
        		if(res.state == 200){	
        			var html2 = '<thead><tr><th><input id="checkbox11" type="checkbox" name="all"></th><th>公司编码</th><th>公司名称</th><th>采购价管理</th><th><i class="red">*</i>税率</th></tr></thead>';
        			html2=html2+'<tbody>';
        			
        			$(list).each(function(index,event){
                		debugger;

        				var checked3="";
        				if (event.purchase_price_manage=='1') {
        					checked3="checked";
						}
        				
        				var tariffRateConfigHtml='<td><select name="tariffRateConfigId" style="width: 70px ;height:25px;" ><option value="">请选择</option>';
        				$(tariffRateConfigList).each(function(index,tariffRateConfig){
        					var selected5="";
        	        		debugger;
        					if (event.rate_id==tariffRateConfig.id) {
        						selected5="selected";
    						}
        					tariffRateConfigHtml=tariffRateConfigHtml+
        						  '<option value="'+tariffRateConfig.id+'"'+selected5+'>'+tariffRateConfig.rate+'</option>';
        				});
        				tariffRateConfigHtml=tariffRateConfigHtml+'</select></td>';
        				
        				
        				html2+="<tr>"+
    	            	"<td> <input type='hidden' name='customerMerchantRelId' value='"+event.id+"'/> <input type='checkbox'   class='iCheck' name='item-checkbox'><input type='hidden' name='merchantId' value='"+event.merchant_id+"'/></td>"+
                    	"<td>"+(event.code==null?'':event.code)+"<input type='hidden' name='merchantCode' value='"+(event.code==null?'':event.code)+"'/></td>"+
                        "<td>"+(event.merchant_name==null?'':event.merchant_name)+"<input type='hidden' name='merchantName' value='"+(event.name==null?'':event.merchant_name)+"'/></td>"+
                        '<td><input type="checkbox"  name="purchasePriceManage" '+checked3+'/>启用</td>'+   
                        tariffRateConfigHtml+
                        "</tr>";
					}) ;
        			html2=html2+'</tbody>';
        			$("#table-list-group").append(html2); 
        			 $("#mytable3").show();
                     $("#mytable").hide();
                     $("#mytable2").hide();
                     $("#mytable1").hide();
        		}else{
        			$custom.alert.error	(res.expMessage);
        		}
        	});
        }
    }
	

	//新增
	 function addCustomerBank(){
		addNum+=1;
		var bankMerchantIds="bankMerchantIds"+addNum.toString(); // 下拉框是根据name赋值 name要 
		var str="select[name='"+bankMerchantIds+"']";
		debugger;		
		var bankMerchantHtml='<td><select   multiple  name="'+bankMerchantIds+'"  id="bankMerchantIds" title="请选择"  ></td>';			
		var bankHtml="<tr>"+
     	"<td><input type='checkbox'   class='iCheck' name='item-bank-checkbox'></td>"+
     	"<td><input type='text' name='depositBank' /></td>"+
     	"<td><input type='text' name='bankAccount' /></td>"+
     	"<td><input type='text' name='beneficiaryName' /></td>"+
     	"<td><input type='text' name='bankAddress' /></td>"+
     	"<td><input type='text' name='swiftCode' /></td>"+
     	bankMerchantHtml+
         "</tr>";
         $("#table-list-customerbank").append(bankHtml); 
         $module.merchant.getUserMerchantSelectBeanURL.call($(str)[0],null);
	 } 

	
		//删除行
	 $("#deleteCustomerBank").click(function() {
		 debugger;
	 		var merchantIds = [];
	        $("input[name='item-bank-checkbox']:checked").each(function() { // 遍历选中的checkbox
	            n = $(this).parents("tr").index()+1;  // 获取checkbox所在行的顺序	           
	            $("#table-list-customerbank").find("tr:eq("+n+")").remove();
	        });
	        $("input[name='bankall']").prop("checked",false);
	    });
	
	//全选
	$("input[name='bankall']").click(function(){
			//判断当前点击的复选框处于什么状态$(this).is(":checked") 返回的是布尔类型
			if($(this).is(":checked")){
				$("input[name='item-bank-checkbox']").prop("checked",true);
			}else{
				$("input[name='item-bank-checkbox']").prop("checked",false);
			}
		});	
	

</script>
