<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- App favicon -->
<link rel="shortcut icon" href='<c:url value="/resources/assets/images/favicon.ico"/>'>

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
                       <li><i class="dripicons-location"></i> 当前位置：</li>
                        <li class="breadcrumb-item"><a href="#">客户详情</a></li>
                    </ol>
                 </div>
                    <!--  title   end  -->
                    <!--  title   客户详情  start -->
                <div class="form-row mt20 ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">客户类型 <span>：</span></span></label>
                        	<div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">所属公司<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                </div>   
                  <div class="form-row mt20 ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">主数据客户ID <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">客商类型<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                 <div></div>
                            </div>
                        </div>
                    </div>
                </div>  
                <div class="form-row mt20 ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">客户名称 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">客户简称<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                 <div></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">营业执照号 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">组织机构代码<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                 <div></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">英文名 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">英文简称<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                 <div></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">企业性质 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">注册地<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                 <div></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">客户类型 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">客户等级编码<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                 <div></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">经营范围 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">省市区代码<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                 <div></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">企业地址 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">企业英文地址<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                 <div></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">备注 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                </div>
                 <!--  title   客户详情 end -->
                  <!--  title   银行信息 start -->
                <div class="ml10 page-title col-12 borderb mt20">
                   <div class="ml10">银行信息</div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">开户银行 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">银行账号<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                 <div></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">人民币账号 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">美元账号<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                 <div></div>
                            </div>
                        </div>
                    </div>
                </div>
                   <!--  title   银行信息 end -->
                   <!--  title   联系方式 start -->
                  <div class="title_text">联系方式</div>
                	<!--  title   联系方式 end -->
                	<div class="form-row mt20 ml15">
	                    <div class="col-5">
	                        <div class="row">
	                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">法人代表 <span>：</span></span></label>
	                            <div class="col-7 ml10 line">
	                                <div></div>
	                            </div>
	                        </div>
	                    </div>
	                    <div class="col-6">
	                        <div class="row">
	                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">法人国籍<span>：</span></span></label>
	                            <div class="col-7 ml10 line">
	                                 <div></div>
	                            </div>
	                        </div>
	                    </div>
	                </div>
	                <div class="form-row mt20 ml15">
	                    <div class="col-5">
	                        <div class="row">
	                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">法人代表身份证 <span>：</span></span></label>
	                            <div class="col-7 ml10 line">
	                                <div></div>
	                            </div>
	                        </div>
	                    </div>
	                    <div class="col-6">
	                        <div class="row">
	                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">法人电话<span>：</span></span></label>
	                            <div class="col-7 ml10 line">
	                                 <div></div>
	                            </div>
	                        </div>
	                    </div>
	                </div>
	                <div class="form-row mt20 ml15">
	                    <div class="col-5">
	                        <div class="row">
	                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">联系人 <span>：</span></span></label>
	                            <div class="col-7 ml10 line">
	                                <div></div>
	                            </div>
	                        </div>
	                    </div>
	                    <div class="col-6">
	                        <div class="row">
	                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">联系电话<span>：</span></span></label>
	                            <div class="col-7 ml10 line">
	                                 <div></div>
	                            </div>
	                        </div>
	                    </div>
	                </div>
	                <div class="form-row mt20 ml15">
	                    <div class="col-5">
	                        <div class="row">
	                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">传真 <span>：</span></span></label>
	                            <div class="col-7 ml10 line">
	                                <div></div>
	                            </div>
	                        </div>
	                    </div>
	                    <div class="col-6">
	                        <div class="row">
	                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">email<span>：</span></span></label>
	                            <div class="col-7 ml10 line">
	                                 <div></div>
	                            </div>
	                        </div>
	                    </div>
	                </div>
            <!-- end row -->
            </div>
            <!-- container -->
        </div>

    </div> <!-- content -->
    </div>
</div>