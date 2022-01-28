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
                       <li class="breadcrumb-item"><a href="#">供应商档案</a></li>
                       <li class="breadcrumb-item "><a href="javascript:$load.a('/supplierMerchandisePrice/toPage.html');">采购价格管理</a></li>
                       <li class="breadcrumb-item"><a href="javascript:$load.a('/supplierMerchandisePrice/toDetailsPage.html?id=${detail.id }');">价目表详情</a></li>
                    </ol>
                 </div>
                    <!--  title   end  -->
                    <!--  title   供应商详情  start -->
                  <div class="title_text">供应商详情</div>
  <%--              <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">供应商编号 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>${detail.customerCode}</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">供应商名称<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>${detail.customerName}</div>
                            </div>
                        </div>
                    </div>
                </div>   
                  <div class="form-row ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">供应商简称 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>${detail.customerName}</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">结算方式<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                 <div>${detail.settlementMode}</div>
                            </div>
                        </div>
                    </div>
                </div> --%>
                  <div class="info_box">
                      <div class="info_item">
                          <div class="info_text">
                              <span>供应商编号：</span>
                              <span>${detail.customerCode}</span>
                          </div>
                          <div class="info_text">
                              <span>供应商名称：</span>
                              <span>${detail.customerName}</span>
                          </div>
                          <div class="info_text"></div>
                      </div>
                      <div class="info_item">
                          <div class="info_text">
                              <span>供应商简称：</span>
                              <span>${detail.customerName}</span>
                          </div>
                          <div class="info_text">
                              <span>结算方式：</span>
                              <span>
                              	  <c:choose>
                              	  	  <c:when test="${detail.settlementMode eq '1'}"> 预付</c:when>
                              	  	  <c:when test="${detail.settlementMode eq '2'}"> 到付</c:when>
                              	  	  <c:when test="${detail.settlementMode eq '3'}"> 月结</c:when>
                              	  </c:choose>
                              </span>
                          </div>
                          <div class="info_text"></div>
                      </div>
                  </div>
                 <!--  title   供应商详情  start -->
                 <!--   商品报价  start -->
                  <div class="title_text">货品详情</div>
                  <div class="info_box">
                      <div class="info_item">
                          <div class="info_text">
                              <span>货品条形码：</span>
                              <span>${detail.barcode}</span>
                          </div>
                          <div class="info_text">
                              <span>品牌：</span>
                              <span>${detail.brandName}</span>
                          </div>
                          <div class="info_text">
                              <span>规格：</span>
                              <span>${detail.spec}</span>
                          </div>
                      </div>
                      <div class="info_item">
                          <div class="info_text">
                              <span>货品名称：</span>
                              <span>${detail.productName}</span>
                          </div>
                          <div class="info_text">
                              <span>原产国：</span>
                              <span>${detail.countryName}</span>
                          </div>
                          <div class="info_text">
                              <span>工厂源码：</span>
                              <span>${detail.barcode}</span>
                          </div>
                      </div>
                      <div class="info_item">
                          <div class="info_text">
                              <span>保质天数：</span>
                              <span>${detail.shelfLifeDays}</span>
                          </div>
                      </div>
                  </div>
                 <!--   商品报价  end -->
                 <!-- 货品报价 start -->
                  <div class="title_text">货品报价</div>
                  <div class="info_box">
                      <div class="info_item">
                          <div class="info_text">
                              <span>最大供货量：</span>
                              <span>${detail.maximum}</span>
                          </div>
                          <div class="info_text">
                              <span>报价来源：</span>
                              <span>
                                <c:choose>
                                    <c:when test="${detail.source eq '1' }">第e仓</c:when>
                                    <c:when test="${detail.source eq '2' }">外部</c:when>
                                    <c:when test="${detail.source eq '3' }">品牌经销</c:when>
                                </c:choose>
                              </span>
                          </div>
                          <div class="info_text">
                              <span>最小起订量：</span>
                              <span>${detail.minimum}</span>
                          </div>
                      </div>
                      <div class="info_item">
                          <div class="info_text">
                              <span>报价生效日期：</span>
                              <span>${effectiveDate} 至 ${expiryDate}</span>
                          </div>
                          <div class="info_text">
                              <span>备货天数：</span>
                              <span>${detail.stockUpDay}</span>
                          </div>
                          <div class="info_text">
                              <span>报价币种：</span>
                              <span>
                              	${detail.currencyLabel}
                              </span>
                          </div>
                      </div>
                      <div class="info_item">
                          <div class="info_text">
                              <span>报价模式：</span>
                              <span>
                                  <c:choose>
                                  	  <c:when test="${detail.priceModel eq '1' }">
                                  	  	   	阶梯报价
                                  	  </c:when>
                                  	  <c:when test="${detail.priceModel eq '2' }">
                                  	  	   	固定报价
                                  	  </c:when>
                                  </c:choose>
                              </span>
                          </div>
                      </div>
                  </div>
                <c:choose>
                    <c:when test="${detail.priceModel eq '1' }">
		                <div class="form-row">
		                	<div class="col-12">
		                		<div class="card-box">
		                			<h4 class="header-title m-t-0">阶梯报价</h4>
		                			<div class="table-responsive">
		                				<table class="table table-bordered" cellspacing="0" width="100%">
					                        <thead>
					                        <tr>
					                            <th>剩余效期</th>
					                            <th>数量下限</th>
					                            <th>数量上限</th>
					                            <th>供货下限</th>
					                            <th>供货上限</th>
					                        </tr>
					                        </thead>
					                        <tbody>
					                       	  <c:forEach items="${detail.itemList }" var="rel" varStatus="relStatus">
						                      	  <tr>
							                         <td>${rel.residualMaturity}</td>
							                         <td>
							                         	<c:choose>
				                      	 				 <c:when test="${empty rel.minNum}">
							                         		-
							                             </c:when>
							                             <c:otherwise>
				                                                ${rel.minNum}
														 </c:otherwise>
				                      					</c:choose>
							                         </td>
							                         <td>
							                         	<c:choose>
				                      	 				 <c:when test="${empty rel.maxNum}">
							                         		-
							                             </c:when>
							                             <c:otherwise>
				                                                ${rel.maxNum}
														 </c:otherwise>
				                      					</c:choose>
							                         </td>
							                         <td>
				                                          ${rel.supplyMinPrice}
							                         </td>
							                         <td>
				                                          ${rel.supplyMaxPrice}
							                         </td>
							                      </tr>
						                      </c:forEach>
					                        </tbody>
					                    </table>
		                			</div>
		                		</div>
		                    </div>
		                </div>
		            </c:when>
		            <c:when test="${detail.priceModel eq '2' }">
		            	<div class="form-row">
		                	<div class="col-12">
		                		<div class="card-box">
		                			<h4 class="header-title m-t-0">固定报价</h4>
		                			<div class="table-responsive">
		                				<table class="table table-bordered" cellspacing="0" width="100%">
					                        <thead>
					                        <tr>
					                            <th>剩余效期</th>
					                            <th>供货下限</th>
					                            <th>供货上限</th>
					                        </tr>
					                        </thead>
					                        <tbody>
					                       	  <c:forEach items="${detail.itemList }" var="rel" varStatus="relStatus">
						                      	  <tr>
							                         <td>${rel.residualMaturity}</td>
							                         <td>
				                                          ${rel.supplyMinPrice}
							                         </td>
							                         <td>
				                                          ${rel.supplyMaxPrice}
							                         </td>
							                      </tr>
						                      </c:forEach>
					                        </tbody>
					                    </table>
		                			</div>
		                		</div>
		                    </div>
		                </div>
		            </c:when>
		        </c:choose>
                <div class="form-row">
                      	<div class="form-inline">
                          		<div class=form-group>
                          			<button type="button" class="btn btn-find waves-effect waves-light ml15 btn_default" id="cancel-buttons">返回</button>
                          		</div>
                          	</div>
                      </div>

                 <!-- 货品报价 end -->
            <!-- end row -->
            </div>
            <!-- container -->
        </div>

    </div> <!-- content -->
    </div>
</div>
<script type="text/javascript">
	$(function(){
		//取消按钮
		$("#cancel-buttons").click(function(){
			$load.a("/supplierMerchandisePrice/toPage.html");
		});
		
	});
</script>
