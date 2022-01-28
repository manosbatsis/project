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
                       <li class="breadcrumb-item "><a href="javascript:$load.a('/merchantShop/toPage.html');">店铺信息</a></li>
                       <li class="breadcrumb-item "><a href="javascript:$load.a('/merchantShop/toDetailsPage.html?id=${detail.id }');">详情</a></li>
                    </ol>
                    </div>
                   </div>
                    <!--  title   end  -->               
                          <!--  title   基本信息   start -->
                    <div class="title_text">店铺信息</div>
                <form id="add-form">
                  <div class="info_box">
                      <div class="info_item">
                          <div class="info_text">
                              <span>店铺类型：</span>
                              <span>
                                  ${detail.storeTypeName }
                              </span>
                          </div>
                          <div class="info_text">
                              <span>店铺统一ID：</span>
                              <span>
                                  ${detail.shopUnifyId }
                              </span>
                          </div>
                      </div>
                  	<div class="info_item">
                        <div class="info_text">
                            <span>店铺名称：</span>
                            <span>
                               ${detail.shopName }
                            </span>
                        </div>
                        <div class="info_text">
                            <span>店铺编码：</span>
                            <span>
                                ${detail.shopCode }
                            </span>
                        </div>
                    </div>
                    <div class="info_item">
                        <div class="info_text">
                            <span>开店公司：</span>
                            <span>
                               ${detail.merchantName }
                            </span>
                        </div>
                        <div class="info_text">
                            <span>客户名称：</span>
                            <span>
                                ${detail.customerName }
                            </span>
                        </div>
                    </div>
                    <div class="info_item">
                        <div class="info_text">
                            <span>卓志编码：</span>
                            <span>
                               ${detail.topidealCode }
                            </span>
                        </div>
                        <div class="info_text">
                            <span>状态：</span>
                            <span>
                                ${detail.statusLabel }
                            </span>
                        </div>
                    </div>
                    
                    
                    <div class="info_item">
                        <div class="info_text">
                            <span>仓库名称：</span>
                            <span>
                               ${detail.depotName }
                            </span>
                        </div>
                        <div class="info_text">
 						<span>电商平台：</span>
                            <span>
                               ${detail.storePlatformName }
                            </span>
                        </div>
                    </div>
                      <div class="info_item">
                        <div class="info_text">
                            <span>运营类型：</span>
                            <span>
                               ${detail.shopTypeName }
                            </span>
                        </div>
                        <div class="info_text">
 						<span>数据来源：</span>
                            <span>
                               ${detail.dataSourceName }
                            </span>
                        </div>
                    </div>
                      <div class="info_item">
                          <div class="info_text">
                              <span>开店事业部：</span>
                              <span>
                                  ${detail.buName }
                              </span>
                          </div>
                          <div class="info_text">
                              <span>专营母品牌：</span>
                              <span>
                                  ${detail.superiorParentBrandName }
                              </span>
                          </div>
                      </div>
                      <div class="info_item">
                          <div class="info_text">
                              <span>结算币种：</span>
                              <span>
                                  ${detail.currencyLabel }
                              </span>
                          </div>
                      </div>
                  </div>
                  <div style="display:none;">
                  <div class="title_text">菜鸟商品同步</div>
                  <div class="info_box">
                  		<div class="info_item">
	                        <div class="info_text">
	                            <span>app key：</span>
	                            <span>
	                               ${detail.appKey }
	                            </span>
	                        </div>
	                        <div class="info_text">
	 						<span>session key：</span>
	                            <span>
	                               ${detail.sessionKey }
	                            </span>
	                        </div>
	                    </div>
	                    <div class="info_item">
	                        <div class="info_text">
	                            <span>app secret：</span>
	                            <span>
	                               ${detail.appSecret }
	                            </span>
	                        </div>
	                        <div class="info_text">
	 						<span>是否同步菜鸟商品：</span>
	                            <span>
	                               ${detail.isSycnMerchandiseLabel }
	                            </span>
	                        </div>
	                    </div>
                  
                  </div>
                  </div>
                </form>
                
                 <!--   公司主体  start -->
                     <div class="title_text">店铺货主信息</div>
                     <div class="row col-12 mt20 table-responsive">
                         <table id="datatable-buttons" class="table table-striped" cellspacing="0"
                                width="100%">
                             <thead>
                             <tr>
                                 <th>货主公司</th>
                                 <th>货主事业部</th>
                             </tr>
                             </thead>
                             <tbody>
                               <c:forEach items="${shipperList }" var="item">
                                   <tr>
								      <td>${item.merchantName}</td>
								      <td>${item.buName}</td>
							       </tr>
                               </c:forEach>
                             </tbody>
                         </table>
                     </div>
                     
                     
                       <!--   返回按钮 end -->
                      <div class="form-row m-t-50">
                          <div class="col-12">
                              <div class="row">
                                  <div class="col-6">
                                      <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="cancel-buttons">返回</button>
                                  </div>
                                  <div class="col-6">
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
<script type="text/javascript">
	//返回
	$("#cancel-buttons").click(function(){
		$load.a("/merchantShop/toPage.html");
	});

</script>