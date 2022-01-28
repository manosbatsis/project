<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<style>
	.span-br {
	    word-break:normal; 
	    width:auto; 
	    display:block; 
	    white-space:pre-wrap;
	    word-wrap : break-word ;
	    overflow: hidden ;
	} 

</style>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt60">
        <!-- Start row -->
        <div class="row">
            <div class="card-box margin table-responsive" id="border">
                 <!--  title   start  -->
              <div class="col-12 ml10">
                 <div class="col-12 row">
                       <ol class="breadcrumb">
                       <li><i class="block"></i> 当前位置：</li>
                       <li class="breadcrumb-item"><a href="#">供应商档案</a></li>
                       <li class="breadcrumb-item "><a href="javascript:$load.a('/supplier/toPage.html');">供应商列表</a></li>
                       <li class="breadcrumb-item"><a href="javascript:$load.a('/supplier/toDetailsPage.html')">供应商详情</a></li>
                    </ol>
                 </div>
                    <!--  title   end  -->
                    <!--  title   客户详情  start -->
                  <div class="info_box">
                      <input type="hidden" parsley-type="email" class="form-control" name="id" value="${detail.id}">
                      <input type="hidden" parsley-type="email" class="form-control" name="id" value="${aptitude.id}">
                      <div class="info_item">
                          <div class="info_text">
                              <span>供应商名称：</span>
                              <span>
                                ${detail.name }
                              </span>
                          </div>
                          <div class="info_text">
                              <span>供应商简称：</span>
                              <span>${detail.simpleName}</span>
                          </div>
                          <div class="info_text">
                              <span>营业执照号：</span>
                              <span>${detail.creditCode }</span>
                          </div>
                      </div>
                      <div class="info_item">
                          <div class="info_text">
                              <span>组织机构代码：</span>
                              <span>
                                ${detail.orgCode }
                              </span>
                          </div>
                          <div class="info_text">
                              <span>客户等级编码：</span>
                              <span>${detail.codeGrade }</span>
                          </div>
                          <div class="info_text">
                              <span>企业性质：</span>
                              <span>${detail.nature }</span>
                          </div>
                      </div>
                      <div class="info_item">
                          <div class="info_text">
                              <span>中文名：</span>
                              <span>${detail.chinaName }</span>
                          </div>
                          <div class="info_text">
                              <span>英文简称：</span>
                              <span>${detail.enSimpleName }</span>
                          </div>
                          <div class="info_text">
                              <span>英文名：</span>
                              <span>${detail.enName }</span>
                          </div>
                      </div>
                      <div class="info_item">
                          <div class="info_text">
                              <span>主数据ID：</span>
                              <span>${detail.mainId}</span>
                          </div>
                          <div class="info_text">
                              <span>业务员：</span>
                              <span>${detail.salesman }</span>
                          </div>
                          <div class="info_text">
                              <span>省市区代码：</span>
                              <span>${detail.cityCode }</span>
                          </div>
                      </div>
                      <div class="info_item">
                          <div class="info_text">
                              <span>注册地：</span>
                              <span>${detail.companyAddr }</span>
                          </div>
                          <div class="info_text">
                              <span>企业地址：</span>
                              <span>${detail.businessAddress}</span>
                          </div>
                          <div class="info_text">
                              <span>企业英文地址：</span>
                              <span>${detail.enBusinessAddress}</span>
                          </div>

                      </div>
                      <div class="info_item">
                          <div class="info_text">
                              <span>采购币种：</span>
                              <span>${detail.currencyLabel }</span>
                          </div>
                          <div class="info_text">
                              <span>是否内部公司：</span>
                              <span>${detail.typeLabel }</span>
                          </div>
                          <div class="info_text">
                              <span>内部公司：</span>
                              <span>${detail.innerMerchantName }</span>
                          </div>
                      </div>
                      <div class="info_item">
                          <div class="info_text">
                              <span>经营范围：</span>
                              <span>${detail.operationScope}</span>
                          </div>
                          <div class="info_text">
                              <span>备注：</span>
                              <span>${detail.remark}</span>
                          </div>
                          <div class="info_text"></div>
                      </div>
                  </div>
                  <ul class="nav nav-tabs">
					<li class="nav-item"><a href="#bankInfo" data-toggle="tab"
						class="nav-link active">银行信息</a></li>
					<li class="nav-item"><a href="#contractInfo"
						data-toggle="tab" class="nav-link">联系方式</a></li>
					<li class="nav-item"><a href="#qualification"
						data-toggle="tab" class="nav-link">资质信息</a></li>
					<li class="nav-item"><a href="#merchantRel"
					data-toggle="tab" class="nav-link">关联公司</a></li>
				</ul>
				<div class="tab-content" style="padding: 20px;min-height: 200px;">
					<div class="tab-pane fade show active table-responsive" id="bankInfo">
						<!--  title   银行信息 start -->
						
						<table id="datatable-merchant" class="table table-striped" cellspacing="0" width="100%">
			                        <thead>
			                        <tr>
			                            <th>开户银行</th>
			                            <th>银行账号</th>
			                            <th>银行账户</th>
			                            <th>开户行地址</th>
			                            <th>Swift Code</th>
			                            <th>合作公司</th>
			                        </tr>
			                        </thead>
			                        <tbody id="data-tbody">
			                        	<c:forEach items="${customerBankList }" var="bank">
                                           <tr>
                                           
                                          	<td>${bank.depositBank }</td>
			                            	<td>${bank.bankAccount }</td>
			                            	<td>${bank.beneficiaryName }</td>
			                            	<td>${bank.bankAddress }</td>
			                            	<td>${bank.swiftCode }</td>
			                            	<td>${bank.merchantNameStr }</td>
			                            	</tr>
                                       </c:forEach>
			                        </tbody>
			                    </table>

		                   <!--  title   银行信息 end -->
					</div>
					<div class="tab-pane fade table-responsive" id="contractInfo">
						<!--  title   联系方式 end -->
		                  <div class="info_box">
		                      <div class="info_item">
		                          <div class="info_text">
		                              <span>法人代表：</span>
		                              <span>${detail.legalPerson }</span>
		                          </div>
		                          <div class="info_text">
		                              <span>法人国籍：</span>
		                              <span>${detail.legalNationality }</span>
		                          </div>
		                          <div class="info_text">
		                              <span>法人代表身份证：</span>
		                              <span>${detail.legalCardNo }</span>
		                          </div>
		                      </div>
		                      <div class="info_item">
		                          <div class="info_text">
		                              <span>法人电话：</span>
		                              <span>${detail.legalTel }</span>
		                          </div>
		                          <div class="info_text">
		                              <span>联系人：</span>
		                              <span>${detail.legalPerson }</span>
		                          </div>
		                          <div class="info_text">
		                              <span>联系电话：</span>
		                              <span>${detail.oTelNo }</span>
		                          </div>
		                      </div>
		                      <div class="info_item">
		                          <div class="info_text">
		                              <span>传真：</span>
		                              <span>${detail.fax }</span>
		                          </div>
		                          <div class="info_text">
		                              <span>email：</span>
		                              <span>${detail.email }</span>
		                          </div>
		                          <div class="info_text"></div>
		                      </div>
		                  </div>
					</div>
					<div class="tab-pane fade table-responsive" id="qualification">
						<div class="row col-12 mt20">
                    	<input type="hidden" id="id" value="${detail.id}"/>
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
			                        	</c:when>
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
			                        	</c:when>
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
			                        	</c:when>
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
			                        	</c:when>
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
			                        	</c:when>
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
			                        	</c:when>
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
			                        	</c:when>
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
			                        	</c:when>
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
			                        	</c:when>
                        			</c:choose>
                                </td>
                                <td>${customerAptitude.legalModifierName}</td>
                                <td><fmt:formatDate value="${customerAptitude.legalModifyDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
					</div>
					<div class="tab-pane fade table-responsive" id="merchantRel">
						<table id="datatable-merchant" class="table table-striped" cellspacing="0" width="100%">
			                        <thead>
			                        <tr>
			                            <th>公司编码</th>
			                            <th>公司名称</th>
			                            <th>采购价格管理</th>
			                            <th>税率</th>
			                        </tr>
			                        </thead>
			                        <tbody id="data-tbody">
			                        	<c:forEach items="${relList1 }" var="merchant">
                                           <tr>
                                           <td>${merchant.code }</td>
			                            	<td>${merchant.name }</td>
			                            	<td>${merchant.purchasePriceManageLabel }</td>
			                            	<td>${merchant.rate }</td>
			                            	</tr>
                                       </c:forEach>
			                        </tbody>
			                    </table>
					</div>
				</div>
                <div class="form-row m-t-50">
                         <div class="col-12">
                             <div class="row">
                                 <div class="col-5">
                                     <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="cancel-buttons">返回</button>
                                 </div>
                             </div>
                         </div>
                     </div>
            <!-- end row -->
            </div>
            <!-- container -->
        </div>
        </div>
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
<script type="text/javascript">
//显示大图    
function showimage(source){
      $("#ShowImage_Form").find("#img_show").html("<center><image src='"+source+"' class='carousel-inner img-responsive img-rounded' style='width:950px;height:950px;'/></center>");
      $("#ShowImage_Form").modal();
}
//关闭大图
$("#ShowImage_Form").click(function(){//再次点击淡出消失弹出层
	 $("#ShowImage_Form").modal('hide');  //手动关闭
});
	$(function(){
		//取消按钮
		$("#cancel-buttons").click(function(){
			$load.a("/supplier/toPage.html");
		});
	});
</script>    