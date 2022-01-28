<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- App favicon -->
<link rel="shortcut icon" href='<c:url value="/resources/assets/images/favicon.ico"/>'>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
          <form id="add-form" action="/merchandise/saveMerchandise.asyn">
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
                           <li class="breadcrumb-item"><a href="javascript:$load.a('/reptile/toPage.html');">爬虫配置</a></li>
                           <li class="breadcrumb-item"><a href="javascript:$load.a('/reptile/toDetailsPage.html?id=${detail.id }');">详情</a></li>
                    </ol>
                    </div>
            </div>
                    <!--  title   end  -->
                    <div class="title_text">基本信息</div>
                    <div class="info_box">
                        <div class="info_item">
                            <div class="info_text">
                                <span>使用平台：</span>
                                <span>	
                                    <input type="hidden" value="${detail.id }" name="id">
                                    ${detail. platformName}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>用户名：</span>
                                <span>
                                    ${detail.loginName }
                                </span>
                            </div>
                            <div class="info_text">
                                <span>爬取账单时点：</span>
                                <span> ${detail.timePoint }天</span>
                            </div>
                           
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>公司：</span>
                                <span>
                                    ${detail.merchantName }
                                </span>
                            </div>
                            <div class="info_text">
                                <span>客户：</span>
                                <span>
                                    ${detail.customerName }
                                </span>
                            </div>
                            <div class="info_text">
                            	<span>店铺名称：</span>
                                <span>
                                    ${detail.shopName }
                                </span>
                            </div>
                         </div>
                          <div class="info_item">
                            <div class="info_text">
                                <span>出仓库：</span>
                                <span>
                                     ${detail.outDepotName }
                                </span>
                            </div>
                            <div class="info_text">
                                <span>入仓库：</span>
                                <span>
                                    ${detail.inDepotName }
                                </span>
                            </div>
                            <div class="info_text">

                            </div>
                            
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
			$load.a("/reptile/toPage.html");
		});
	});
</script>
