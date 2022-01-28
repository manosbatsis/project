<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- App favicon -->
<link rel="shortcut icon" href='<c:url value="/resources/assets/images/favicon.ico"/>'>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
          <form id="add-form" action="">
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
                           <li class="breadcrumb-item"><a href="javascript:$load.a('/rate/toPage.html');">汇率管理</a></li>
                           <li class="breadcrumb-item"><a href="javascript:$load.a('/rate/toDetailsPage.html?id=${detail.id }');">汇率管理详情</a></li>
                    </ol>
                    </div>
            </div>
            <div class="title_text">基本信息</div>
                    <div class="info_box">
                        <div class="info_item">
                            <div class="info_text">
                                <span>币别代码：</span>
                                <span>	
                                    <input type="hidden" value="${detail.id }" name="id">
                                    ${detail.origCurrencyCode}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>币别名称：</span>
                                <span>
                                    ${detail.origCurrencyCode }
                                </span>
                            </div>
                            <div class="info_text">
                                <span>记账汇率：</span>
                                <span>
                                    ${detail.rate }
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>金额小数位数：</span>
                                <span> ${detail.decimalPlace }位</span>
                            </div>
                            <div class="info_text">
                                <span>生效时间：</span>
                                <span>
                                    ${effectiveDate }
                                </span>
                            </div>
                            
                            <div class="info_text">
                                <span>失效时间：</span>
                                <span>
                                    ${expiryDate }
                                </span>
                            </div>
                        </div>
                        <div class="title_text">折算方式</div>
	                        <div class="info_item">
	                            <div class="info_text">
	                            	<c:choose>
                                          <c:when test="${detail.conversionMethod == '1' }">
                                              <p><input type="radio"  name="conversionMethod"  value="1"   checked disabled="true">原币 * 汇率 = 本位币&nbsp;</p>
                                              <p><input type="radio"  name="conversionMethod"  value="2"   disabled="true">原币 / 汇率 = 本位币</p>
                                          </c:when>
                                          <c:otherwise>
                                              <p><input type="radio"  name="conversionMethod"  value="1"  disabled="true">原币 * 汇率 = 本位币&nbsp;</p>
                                              <p><input type="radio"  name="conversionMethod"  value="2"   checked disabled="true">原币 / 汇率 = 本位币</p>
                                          </c:otherwise>
                                   </c:choose>
	                             </div>
	                            <div class="info_text"> </div>
	                            <div class="info_text"> </div>
	                        </div>
                        </div>
                    </div>
                </div>
                  <div class="form-row">
                      <div class="col-3">
                          <div class="form-row m-t-50">
                              <div class="row">
                                  <div class="col-5">
                                      <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="cancel-buttons">返 回</button>
                                  </div>
                              </div>
                          </div>
                      </div>
                  </div>
              </div>

             </div>
                               <!--======================Modal框===========================  -->
                  <!-- end row -->
           </div>
          </div>
          </form>
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->
<script type="text/javascript">
	$(function(){
		//返回
		$("#cancel-buttons").click(function(){
			$load.a("/rate/toPage.html");
		});
	});
</script>
