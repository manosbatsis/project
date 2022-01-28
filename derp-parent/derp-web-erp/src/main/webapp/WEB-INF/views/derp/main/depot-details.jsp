<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
        <form id="modify-form" action="/depot/getDepotDetails.asyn">
            <div class="row">
                <div class="col-12">
                    <div class="card-box">
                        <!--  title   start  -->
                        <div class="col-12">
                            <div>
                                <div class="col-12 row">
                                    <div>
                                        <ol class="breadcrumb">
                                            <li><i class="block"></i> 当前位置：</li>
                                            <li class="breadcrumb-item"><a href="#">仓库档案</a></li>
                                            <li class="breadcrumb-item"><a
                                                    href="javascript:$load.a('/depot/toPage.html');">仓库列表</a></li>
                                            <li class="breadcrumb-item"><a
                                                    href="javascript:$load.a('/depot/toDetailsPage.html?id=${detail.id }');">仓库详情</a>
                                            </li>
                                        </ol>
                                    </div>
                                </div>
                                <div class="title_text">仓库信息</div>
                                <div class="info_box">
                                
                                <div class="info_item">
                                        <input type="hidden" parsley-type="email" class="form-control" name="id"
                                               value="${detail.id}">
                                        <div class="info_text">
                                            <span>仓库自编码：</span>
                                            <span>
                                                ${detail.depotCode}
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>OP仓库编号：</span>
                                            <span>${detail.code}</span>
                                        </div>
                                        <div class="info_text">
                                            <span>仓库名称：</span>
                                            <span>${detail.name}</span>
                                        </div>
                                    </div>
                                    <div class="info_item">
                                        <div class="info_text">
                                            <span>仓库类别：</span>
                                            <span>
                                                ${detail.typeLabel }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>仓库类型：</span>
                                            <span>
                                                ${detail.depotTypeLabel }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>申报海关编号：</span>
                                            <span>
                                                ${detail.customsNo}
                                            </span>
                                        </div>
                                    </div>
                                    <div class="info_item">
                                        <div class="info_text">
                                            <span>申报国检编号：</span>
                                            <span>
                                                ${detail.inspectNo}
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>仓管员：</span>
                                            <span>
                                                ${detail.adminName}
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>联系电话：</span>
                                            <span>
                                                ${detail.tel}
                                            </span>
                                        </div>
                                    </div>
                                    <div class="info_item">
                                        <div class="info_text">
                                            <span>仓库所在国家：</span>
                                            <span>
                                                ${detail.country}
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>ISV仓库类型：</span>
                                            <span>
                                                ${detail.ISVDepotTypeLabel}
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>
                                                <c:choose>
                                                    <c:when test="${detail.isTopBooks eq '1' }">
                                                        <input type="checkbox" name="isTopBooks" value="1" checked
                                                               disabled="true">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="checkbox" name="isTopBooks" value="1"
                                                               disabled="true">
                                                    </c:otherwise>
                                                </c:choose>
                                                是否代销仓
                                                &nbsp;&nbsp;&nbsp;&nbsp;
                                                <c:choose>
                                                    <c:when test="${detail.status eq '1' }">
                                                        <input type="checkbox" name="status" value="1" checked
                                                               disabled="true">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="checkbox" name="status" value="1" disabled="true">
                                                    </c:otherwise>
                                                </c:choose>
                                                是否启用
                                            </span>
                                        </div>
                                    </div>
                                    <div class="info_item">
                                    	<div class="info_text">
                                            <span>仓库详细地址：</span>
                                            <span>
                                                ${detail.address}
                                            </span>
                                        </div>
                                    </div>
                                    <div class="info_item">
                                        <div class="info_text">
                                        </div>
                                         <div class="info_text">
                                        </div>
                                        <div class="info_text">
                                        </div>
                                </div>
                                <!--  关联公司 结束 -->
                                </div>
                                 <div class="title_text">流程设置</div>
                                 <div class="info_box">
                                 <div class="info_item">
                                        <div class="info_text">
                                            <span>入仓下推指令appkey：</span>
                                            <span>
                                                  <input type="hidden" value="${detail.isMerchant }" id="hiddenIsMerchant">
		                                  <input type="hidden" value="${detail.merchantId }" id="merchantId">
		                                  <c:choose>
                                              <c:when test="${detail.isMerchant == '1' }">
                                                  <input type="radio"  name="isMerchant"  value="1"  onclick="changekey(1);" checked disabled="true">公司key
                                                  <input type="radio" style="margin-left:15px;"  name="isMerchant"  value="2"  onclick="changekey(2);" disabled="true">关联公司key
                                              </c:when>
                                              <c:otherwise>
                                                  <input type="radio"  name="isMerchant"  value="1"  onclick="changekey(1);" disabled="true">公司key
                                                  <input type="radio" style="margin-left:15px;" name="isMerchant"  value="2"  onclick="changekey(2);" checked disabled="true">关联公司key
                                              </c:otherwise>
                                          </c:choose>
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <c:forEach items="${merchantBean }" var="merchant">
                                                <c:choose>
                                                    <c:when test="${merchant.id eq detail.merchantId }">${merchant.name }</c:when>
                                                </c:choose>
                                            </c:forEach>
                                        </div>
                                    </div>
                                    </div>
                                 <div class="info_box">
                                 	<div class="info_item">
                                 		<div class="info_text">
				                            <span>是否批次效期强校验：</span>
				                            <span>
				                               <c:choose>
	                                              <c:when test="${detail.batchValidation == '1' }">
	                                                  <input type="radio"  name="batchValidation"  value="1"   checked disabled="true">是
	                                                  <input type="radio" style="margin-left:15px;"  name="batchValidation"  value="0"   disabled="true">否
	                                              </c:when>
	                                              <c:otherwise>
	                                                  <input type="radio"  name="batchValidation"  value="1"   disabled="true">是
	                                                  <input type="radio" style="margin-left:15px;"  name="batchValidation"  value="0"   checked disabled="true">否
	                                              </c:otherwise>
                                          		</c:choose>
				                            </span>
                        				</div>
                                 	</div>
                                 </div>
                                 
								     <div class="info_item">
                                        <div class="info_text">
                                            <span>变更记录：</span>
                                            <span>
                                            </span>
                                        </div>
                                         <div class="info_text">
                                        </div>
                                        <div class="info_text">
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
                                
                            </div>
                        </div>
                    </div>
                    <!-- end row -->
        </form>
    </div>
    <!-- container -->
</div>

<!-- content -->
<script type="text/javascript">
    $(function () {
        //取消按钮
        $("#cancel-buttons").click(function () {
            $load.a("/depot/toPage.html");
        });

    });
    
</script>
