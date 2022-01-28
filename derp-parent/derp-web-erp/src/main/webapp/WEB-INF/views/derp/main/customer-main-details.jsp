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
                       <li class="breadcrumb-item"><a href="javascript:$load.a('/customerMain/toPage.html')">客商列表</a></li>
                       <li class="breadcrumb-item"><a href="javascript:$load.a('/customerMain/toDetailsPage.html')">客商详情</a></li>
                    </ol>
                 </div>
                    <!--  title   end  -->
                    <!--  title   客户详情  start -->
                  <div class="info_box" style="padding: 20px;">
                      <div class="info_item">
                          <div class="info_text">
                              <span>客商名称：</span>
                              <span>
                                ${detail.cname}
                              </span>
                              
                          </div>
                          <div class="info_text">
                              <span>客商简称：</span>
                              <span>${detail.cshortname }</span>
                          </div>
                          <div class="info_text">
                              <span>主数据客户ID：</span>
                              <span>${detail.ccode }</span>
                          </div>
                      </div>
                      <div class="info_item">
                          <div class="info_text">
                              <span>组织机构代码：</span>
                              <span>
                                ${detail.ccodethk }
                              </span>
                          </div>
                          <div class="info_text">
                              <span>营业执照号：</span>
                              <span>${detail.busilicenseno }</span>
                          </div>
                          <div class="info_text">
                              <span>客户等级编码：</span>
                              <span>${detail.ccodegrade }</span>
                          </div>
                      </div>
                      <div class="info_item">
                          <div class="info_text">
                              <span>英文名：</span>
                              <span>${detail.cnameen }</span>
                          </div>
                          <div class="info_text">
                              <span>英文简称：</span>
                              <span>${detail.cshortnameen }</span>
                          </div>
                          <div class="info_text">
                              <span>企业性质：</span>
                              <span>${detail.cmprop }</span>
                          </div>
                      </div>
                      <div class="info_item">
                          <div class="info_text">
                              <span>注册地：</span>
                              <span>${detail.registeredaddr }</span>
                          </div>
                          <div class="info_text">
                              <span>省市区代码：</span>
                              <span>${detail.citycode }</span>
                          </div>
                          <div class="info_text">
                              <span>企业地址：</span>
                              <span>${detail.addr }</span>
                          </div>
                      </div>
                      <div class="info_item">
                          <div class="info_text">
                              <span>企业英文地址：</span>
                              <span>${detail.addren }</span>
                          </div>
                          <div class="info_text">
                              <span>备注：</span>
                              <span>${detail.remark }</span>
                          </div>
                          <div class="info_text">
                              <span>经营范围：</span>
                              <span>${detail.businessscope }</span>
                          </div>
                      </div>
                      <div class="info_item">
                        <div class="info_text">
                            <span>客商类型：</span>
                            <span>${detail.ccodetypeLabel }</span>
                        </div>
                      </div>
                  </div>
                  <ul class="nav nav-tabs">
					<li class="nav-item"><a href="#bankInfo" data-toggle="tab"
						class="nav-link active">银行信息</a></li>
					<li class="nav-item"><a href="#contractInfo"
						data-toggle="tab" class="nav-link">联系方式</a></li>
				</ul>
				<div class="tab-content" style="padding: 20px;min-height: 200px;">
                    <div class="tab-pane fade show active table-responsive" id="bankInfo">
                    	<!--  title   银行信息 start -->
		                  <div class="info_box">
		                      <div class="info_item">
		                          <div class="info_text">
		                              <span>开户银行：</span>
		                              <span>${detail.depositbank }</span>
		                          </div>
		                          <div class="info_text">
		                              <span>美元账号：</span>
		                              <span>${detail.usdaccount }</span>
		                          </div>
		                          <div class="info_text">
		                              <span>人民币账号：</span>
		                              <span>${detail.cnyaccount}</span>
		                          </div>
		                      </div>
		                  </div>
                    </div>
                    <div class="tab-pane fade table-responsive" id="contractInfo">
                    	<div class="info_box">
		                      <div class="info_item">
		                          <div class="info_text">
		                              <span>法人代表：</span>
		                              <span>${detail.lgrepresentative }</span>
		                          </div>
		                          <div class="info_text">
		                              <span>法人国籍：</span>
		                              <span>${detail.lgrncode }</span>
		                          </div>
		                          <div class="info_text">
		                              <span>法人代表身份证：</span>
		                              <span>${detail.lgrid }</span>
		                          </div>
		                      </div>
		                      <div class="info_item">
		                          <div class="info_text">
		                              <span>法人电话：</span>
		                              <span>${detail.lgrtel }</span>
		                          </div>
		                          <div class="info_text">
		                              <span>联系人：</span>
		                              <span>${detail.linkman }</span>
		                          </div>
		                          <div class="info_text">
		                              <span>联系电话：</span>
		                              <span>${detail.linktel }</span>
		                          </div>
		                      </div>
		                      <div class="info_item">
		                          <div class="info_text">
		                              <span>传真：</span>
		                              <span>${detail.fax }</span>
		                          </div>
		                          <div class="info_text">
		                              <span>Email：</span>
		                              <span>${detail.email }</span>
		                          </div>
		                          <div class="info_text">
		                          	  <span>公司电话：</span>
		                              <span>${detail.otelno }</span>
		                          </div>
		                      </div>
		                  </div>
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
			$load.a("/customerMain/toPage.html");
		});
	});
</script>    