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
                       <li class="breadcrumb-item"><a href="javascript:$load.a('/supplierInquiryPool/toPage.html');">供应商询价池列表</a></li>
                       <li class="breadcrumb-item"><a href="javascript:$load.a('/supplierInquiryPool/toDetailsPage.html?id=${detail.id }');">供应商询价池详情</a></li>
                    </ol>
                 </div>
                    <!--  title   end  -->
                    <!--  title   供应商详情  start -->
                  <div class="title_text">供应商详情</div>
             <%--   <div class="form-row mt20 ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">供应商编号<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>${detail.customerCode}</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">供应商名称<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>${detail.customerName}</div>
                            </div>
                        </div>
                    </div>
                </div>   
                  <div class="form-row ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">供应商简称<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>${detail.customerName}</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">结算方式<span>：</span></span></label>
                            <div class="col-7 ml10 line">
&lt;%&ndash;                                  <div>${detail.settlementMode}</div> &ndash;%&gt;
                            </div>
                        </div>
                    </div>
                </div>  --%>
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
                  <div class="title_text">询价池</div>
     <%--           <div class="form-row mt20 ml15">
                    <div class="col-3">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">品类<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                            	${detail.merchandiseCatName}
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">品牌<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                ${detail.brandName}
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">原产国<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                ${detail.countryName}
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">计量单位<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                            	${detail.unit}
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row ml15">
                    <div class="col-3">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">商品名称<span>：</span></span></label>
                            <div class="col-7 ml10">
                                ${detail.goodsName}
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">最小起订量<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                ${detail.minimum}
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">最大供货量<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                ${detail.maximum}
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">供货价<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                ${detail.supplyPrice}
                            </div>
                        </div>
                    </div>
                </div>--%>
                  <div class="info_box">
                      <div class="info_item">
                          <div class="info_text">
                              <span>品类：</span>
                              <span>${detail.merchandiseCatName}</span>
                          </div>
                          <div class="info_text">
                              <span>品牌：</span>
                              <span>${detail.brandName}</span>
                          </div>
                          <div class="info_text">
                              <span>原产国：</span>
                              <span>${detail.countryName}</span>
                          </div>
                      </div>
                      <div class="info_item">
                          <div class="info_text">
                              <span>计量单位：</span>
                              <span>${detail.unit}</span>
                          </div>
                          <div class="info_text">
                              <span>商品名称：</span>
                              <span>${detail.goodsName}</span>
                          </div>
                          <div class="info_text">
                              <span>最小起订量：</span>
                              <span> ${detail.minimum}</span>
                          </div>
                      </div>
                      <div class="info_item">
                          <div class="info_text">
                              <span>最大供货量：</span>
                              <span>${detail.maximum}</span>
                          </div>
                          <div class="info_text">
                              <span>规格型号：</span>
                              <span>${detail.spec}</span>
                          </div>
                          <div class="info_text">
                              <span>供货价：</span>
                              <span>${detail.supplyPrice}</span>
                          </div>
                      </div>
                  </div>
                 <!--   商品报价  end -->
                 <div class="form-row">
                      	<div class="form-inline">
                          		<div class=form-group>
                          			<button type="button" class="btn btn-find waves-effect waves-light ml15 btn_default" id="cancel-buttons">返回</button>
                          		</div>
                          	</div>
                      </div>
            <!-- end row -->
            </div>
            <!-- container -->
        </div>

    </div> <!-- content -->

<script type="text/javascript">
	$(function(){
		//取消按钮
		$("#cancel-buttons").click(function(){
			$load.a("/supplierInquiryPool/toPage.html");
		});
	});
</script>