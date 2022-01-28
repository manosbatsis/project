<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

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
                       <li class="breadcrumb-item"><a href="#">客户档案</a></li>
                       <li class="breadcrumb-item"><a href="javascript:$load.a('/customer/toPage.html')">客户列表</a></li>
                       <li class="breadcrumb-item"><a href="javascript:$load.a('/customer/toDetailsPage.html')">客户详情</a></li>
                    </ol>
                 </div>
                    <!--  title   end  -->
                    <!--  title   客户详情  start -->
                  <div class="info_box">
                      <div class="info_item">
                          <div class="info_text">
                              <span>客户名称：</span>
                              <span>
                                ${detail.name }
                              </span>
                          </div>
                          <div class="info_text">
                              <span>客户简称：</span>
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
                              <span>企业性质：</span>
                              <span>${detail.nature }</span>
                          </div>
                          <div class="info_text">
                          		<span>销售币种：</span>
                              <span>${detail.currencyLabel }</span>
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
                              <span>注册地：</span>
                              <span>${detail.companyAddr }</span>
                          </div>
                          <div class="info_text">
                              <span>客户等级编码：</span>
                              <span>${detail.codeGrade }</span>
                          </div>
                          <div class="info_text">
                              <span>客户授权码：</span>
                              <span>${detail.authNo }</span>
                          </div>

                      </div>
                      <div class="info_item">
                          <div class="info_text">
                              <span>主数据ID：</span>
                              <span>${detail.mainId}</span>
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
                              <span>
                                ${detail.operationScope}
                              </span>
                          </div>
                          <div class="info_text">
	                          <span>税号：</span>
	                          <span>${detail.taxNo} </span>
                          </div>
                          <div class="info_text">
                          	<span></span>
	                          <span></span>
                          </div>
                      </div>
                      <div class="info_item">
                          <div class="info_text">
                              <span>NC平台名称：</span>
                              <span>
                                ${detail.ncPlatformCodeLabel}
                              </span>
                          </div>
                          <div class="info_text">
	                          <span>NC渠道名称：</span>
	                          <span>${detail.ncChannelCodeLabel} </span>
                          </div>
                          <div class="info_text">
	                          <span>渠道分类</span>
	                          <span>${detail.channelClassifyLabel} </span>
                          </div>
                      </div>
                  </div>
                  
                  
                  
                  
                  <ul class="nav nav-tabs">
                  	<li class="nav-item"><a href="#bankInfo" data-toggle="tab" class="nav-link active">银行信息</a></li>
					<li class="nav-item"><a href="#contractInfo" data-toggle="tab" class="nav-link">联系方式</a></li>
					<li class="nav-item"><a href="#ncPlatformCode" data-toggle="tab" class="nav-link">外部编码配置</a></li>
					<li class="nav-item"><a href="#merchantRel" data-toggle="tab" class="nav-link">关联公司</a></li>
				</ul>
				<div class="tab-content" style="padding: 20px;min-height: 200px;">
				
					<div class="tab-pane fade show active table-responsive" id="bankInfo">
						<!--  title   银行信息 start -->
		                  <div class="info_box">
		                      <div class="info_item">
		                          <div class="info_text">
		                              <span>开户银行：</span>
		                              <span>${detail.depositBank }</span>
		                          </div>
		                          <div class="info_text">
		                              <span>银行账号：</span>
		                              <span>${detail.bankAccount }</span>
		                          </div>
		                          <div class="info_text">
		                          </div>
		                      </div>
		                      <div class="info_item">
		                          <div class="info_text">
		                              <span>银行账户：</span>
		                              <span>${detail.beneficiaryName }</span>
		                          </div>
		                          <div class="info_text">
		                          		<span>开户行地址：</span>
		                              	<span>${detail.bankAddress }</span>
		                          </div>
		                          <div class="info_text"></div>
		                      </div>
		                      <div class="info_item">
		                          <div class="info_text">
		                              <span>Swift Code：</span>
		                              <span>${detail.swiftCode }</span>
		                          </div>
		                          <div class="info_text">
		                          </div>
		                          <div class="info_text"></div>
		                      </div>
		                  </div>
		                   <!--  title   银行信息 end -->
					</div>
				
                 
                 	<div class="tab-pane fade table-responsive" id="contractInfo">
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
		                         	<span>公司电话：</span>
		                             <span>${detail.oTelNo }</span>
		                         </div>
		                         <div class="info_text">
		                         </div>
		                     </div>
		                 </div>
	                 </div>
	                 
	                 <div class="tab-pane fade table-responsive" id="ncPlatformCode">
		                 <div class="info_box">
		                     <div class="info_item">
		                         <div class="info_text">
		                             <span>NC平台编码：</span>
		                             <span>${detail.ncPlatformCodeLabel }</span>
		                         </div>
		                         <div class="info_text">
		                             <span>NC渠道编码：</span>
		                             <span>${detail.ncChannelCodeLabel }</span>
		                         </div>
		                         <div class="info_text">
		                         	<span>NC渠道编码：</span>
		                             <span>${detail.ncChannelCodeLabel }</span>
		                         </div>
		                     </div>
		                     
		                 </div>
	                 </div>
	                 
	                 <div class="tab-pane fade table-responsive" id="merchantRel">
						<table id="datatable-merchant" class="table table-striped" cellspacing="0" width="100%">
			                        <thead>
			                        <tr>
			                            <th>公司编码</th>
			                            <th>公司名称</th>
			                            <th>结算类型</th>
			                            <th>账期</th>
			                            <th>销售价格</th>
			                            <th>税率</th>
			                            <th>销售模式</th>
			                        </tr>
			                        </thead>
			                        <tbody id="data-tbody">
			                        	<c:forEach items="${relList }" var="merchant">
                                           <tr>
                                            <td>${merchant.code }</td>
			                            	<td>${merchant.name }</td>
			                            	<td>${merchant.settlementTypeLabel }</td>
			                            	<td>${merchant.accountPeriodLabel }</td>
			                            	<td>${merchant.salePriceManageLabel }</td>
			                            	<td>${merchant.rate }</td>
			                            	<td>${merchant.businessModelLabel }</td>
			                            	</tr>
                                       </c:forEach>
			                        </tbody>
			                    </table>
					</div>
                  </div>
	                <div class="form-row">
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
    </div> <!-- content -->
<script type="text/javascript">
	$(function(){
		//取消按钮
		$("#cancel-buttons").click(function(){
			$load.a("/customer/toPage.html");
		});
	});
</script>    