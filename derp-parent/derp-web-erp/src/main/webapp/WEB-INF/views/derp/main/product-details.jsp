<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
        <div class="row">
<!--             <div class="margin w100"> -->
<!--                 <button type="button" class="btn btn-primary waves-effect waves-light fr">编 辑</button> -->
<!--             </div> -->
            <div class="card-box margin table-responsive">
            	<div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                       <li><i class="block"></i> 当前位置：</li>
                        <li class="breadcrumb-item"><a href="#">商品档案</a></li>
                        <li class="breadcrumb-item "><a href="javascript:$load.a('/product/toPage.html');">货品列表</a></li>
                        <li class="breadcrumb-item "><a href="javascript:$load.a('/product/toDetailsPage.html?id=${detail.id}');">货品详情</a></li>
                    </ol>
                    </div>
            	</div>
                <div class="title_text">货品基本信息</div>
              <%--  <div class="form-row mt20">
                    <div class="col-3">
                        <div class="row">
                            <label class="col-5 col-form-label "><span class="fr">产品类别<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>${detail.goodsClassifyName }</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label class="col-5 col-form-label "><span class="fr">原产国<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>${detail.countyName }</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label class="col-5 col-form-label "><span class="fr">条形码<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>${detail.barcode }</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label class="col-5 col-form-label "><span class="fr">规格型号<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>${detail.spec }</div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row">
                    <div class="col-3">
                        <div class="row">
                            <label class="col-5 col-form-label "><span class="fr">品牌<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>${detail.brandName }</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label class="col-5 col-form-label "><span class="fr">保质天数<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>${detail.shelfLifeDays }</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label class="col-5 col-form-label "><span class="fr">毛重<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>${detail.grossWeight }</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label class="col-5 col-form-label "><span class="fr">净重<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>${detail.netWeight }</div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row">
                    <div class="col-3">
                        <div class="row">
                            <label class="col-5 col-form-label "><span class="fr">长<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>${detail.length }</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label class="col-5 col-form-label "><span class="fr">宽<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>${detail.width }</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label class="col-5 col-form-label "><span class="fr">高<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>${detail.height }</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label class="col-5 col-form-label "><span class="fr">体积<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>${detail.volume }</div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row">
                    <div class="col-3">
                        <div class="row">
                            <label class="col-5 col-form-label "><span class="fr">生产厂家<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>${detail.manufacturer }</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label class="col-5 col-form-label "><span class="fr">生产地址<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>${detail.manufacturerAddr }</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label class="col-5 col-form-label "><span class="fr">计量单位<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>${detail.unitName }</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label class="col-5 col-form-label "><span class="fr">备注<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>${detail.productRemark }</div>
                            </div>
                        </div>
                    </div>
                </div>--%>
                <div class="info_box">
                    <div class="info_item">
                        <div class="info_text">
                            <span>一级类目：</span>
                            <span>
                                ${detail.productTypeName0 }
                            </span>
                        </div>
                        <div class="info_text">
                            <span>二级类目：</span>
                            <span>
                                ${detail.productTypeName }
                            </span>
                        </div>
                        <div class="info_text">
                            <span>原产国：</span>
                            <span>
                                ${detail.countyName }
                            </span>
                        </div>
                        <div class="info_text">
                            <span>条形码：</span>
                            <span>
                                ${detail.barcode }
                            </span>
                        </div>
                        <%-- <div class="info_text">
                            <span>规格型号：</span>
                            <span>
                                ${detail.spec }
                            </span>
                        </div> --%>
                    </div>
                    <div class="info_item">
                        <div class="info_text">
                            <span>品牌：</span>
                            <span>
                                ${detail.brandName }
                            </span>
                        </div>
                        <div class="info_text">
                            <span>保质天数：</span>
                            <span>
                                ${detail.shelfLifeDays }
                            </span>
                        </div>
                        <div class="info_text">
                            <span>毛重：</span>
                            <span>
                                ${detail.grossWeight }
                            </span>
                        </div>
                        <div class="info_text">
                            <span>净重：</span>
                            <span>
                                ${detail.netWeight }
                            </span>
                        </div>
                    </div>
                    <div class="info_item">
                        <div class="info_text">
                            <span>长：</span>
                            <span>
                                ${detail.length }
                            </span>
                        </div>
                        <div class="info_text">
                            <span>宽：</span>
                            <span>
                                ${detail.width }
                            </span>
                        </div>
                        <div class="info_text">
                            <span>高：</span>
                            <span>
                                ${detail.height }
                            </span>
                        </div>
                        <div class="info_text">
                            <span>体积：</span>
                            <span>
                                ${detail.volume }
                            </span>
                        </div>
                    </div>
                    <div class="info_item">
                        <div class="info_text">
                            <span>生产厂家：</span>
                            <span>
                                ${detail.manufacturer }
                            </span>
                        </div>
                        <div class="info_text">
                            <span>生产地址：</span>
                            <span>
                                ${detail.manufacturerAddr }
                            </span>
                        </div>
                        <div class="info_text">
                            <span>计量单位：</span>
                            <span>
                                ${detail.unitName }
                            </span>
                        </div>
                        
                        <div class="info_text">
                            <span>规格型号：</span>
                            <span>
                                ${detail.spec }
                            </span>
                        </div>
                    </div>
                    <div class="info_item">
                    	<div class="info_text">
                            <span>备注：</span>
                            <span>
                                ${detail.remark }
                            </span>
                        </div>
                        <div class="info_text"></div>
                        <div class="info_text"></div>
                        <div class="info_text"></div>
                    </div>
                </div>
            </div>
            <div class="card-box margin table-responsive">
            	<div class="form-row">
                    <label class="col-1 col-form-label "><span class="fr">商品图片<span>：</span></span></label>
                    <div class="col-11 ml10 line">
                        <div>
                        	<img alt="商品图片1" src="/resources/assets/images/bg-1.png"  width="150px" height="150px">
                        	<img alt="商品图片2" src="/resources/assets/images/bg-1.png"  width="150px" height="150px">
                        	<img alt="商品图片3" src="/resources/assets/images/bg-1.png"  width="150px" height="150px">
                        	<img alt="商品图片4" src="/resources/assets/images/bg-1.png"  width="150px" height="150px">
                        	<img alt="商品图片5" src="/resources/assets/images/bg-1.png"  width="150px" height="150px">
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
            <!-- end row -->
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->
<script type="text/javascript">

		$(function(){
			//返回按钮
			$("#cancel-buttons").click(function(){
				$load.a("/product/toPage.html");
			});
		});
</script>
