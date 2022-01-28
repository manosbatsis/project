<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/views/common.jsp" />
<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
          <div class="row">
           <div class="col-12">
              <div class="card-box table-responsive" >
                                          <!--  title   start  -->
              <div class="col-12">
                <div>
                   <div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                       <li><i class="block"></i> 当前位置：</li>
                       <li class="breadcrumb-item"><a href="#">库位调整单详情</a></li>
                    </ol>
                    </div>
                   </div>
                    <!--  title   end  -->               
                    <!--  title   基本信息   start -->
                    <div class="title_text">基本信息</div>
                    <div class="info_box">
                        <div class="info_item">
                            <div class="info_text">
                                <span>仓库：</span>
                                <span>
                                    ${detail.depotName}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>事业部：</span>
                                <span>
                                    ${detail.buName}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>归属日期：</span>
                                <span>
                                    <fmt:formatDate value="${detail.ownDate}" pattern="yyyy-MM-dd" />
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                        	 <div class="info_text">
                                <span>单据类型：</span>
                                <span>
                                    ${detail.typeLabel}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>创建人：</span>
                                <span>
                                    ${detail.createName}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>创建时间：</span>
                                <span>
                                    <fmt:formatDate value="${detail.createDate}" pattern="yyyy-MM-dd HH:mm:ss" />
                                </span>
                            </div>
                           
                        </div>
                        <div class="info_item">
                         	<div class="info_text">
                                <span>审核人：</span>
                                <span>
                                    ${detail.auditName}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>审核时间：</span>
                                <span>
                                    <fmt:formatDate value="${detail.auditDate}" pattern="yyyy-MM-dd HH:mm:ss" />
                                </span>
                            </div>
                            <div class="info_text">
                                <span>调整备注：</span>
                                <span>
                                    ${detail.remark}
                                </span>
                            </div>
                        </div>
                         <div class="info_item">
                         	<div class="info_text">
                                <span>客户名称：</span>
                                <span>
                                    ${detail.customerName}
                                </span>
                            </div>
                            <div class="info_text">
                                
                            </div>
                            <div class="info_text">
                                
                            </div>
                        </div>
                    </div>
                      <!--  title   基本信息  start -->
                                <ul class="nav nav-tabs">
                                    <li class="nav-item">
                                        <a href="#itemList" data-toggle="tab" 
                                           class="nav-link active">调整商品信息</a>
                                    </li>
					                 <form id="form1">
					                  	<input type="hidden" name='orderId' id='orderId' value='${detail.id}'/>
					                  </form>
                                </ul>
                                <div class="tab-content">
                                    <div class="tab-pane fade show active table-responsive" id="itemList">
                                            <table  id="datatable-items" class="table table-striped"  width="100%">
                                                <thead>
                                                <tr>
							                           <th>关联外部订单号</th>
							                           <th>系统订单号</th>
							                           <th>调增商品货号</th>
							                           <th>商品名称</th>
							                           <th>调减商品货号</th>
							                           <th>调整数量</th>
							                           <th>库存类型</th>
							                           <th>平台名称</th>
							                           <th>店铺名称</th>
							                           <th>理货单位</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach items="${detail.itemList}" var="item" varStatus="status">
						                            <tr>
						                                <td>${item.externalCode}</td>
                                                        <td>${item.orderCode}</td>
						                                <td>${item.increaseGoodsNo}</td>
						                                <td>${item.increaseGoodsName}</td>
						                                <td>${item.reduceGoodsNo}</td>
						                                <td name='amount'>${item.adjustNum}</td>
						                                <td>${item.inventoryTypeLabel}</td>
						                                <td>${item.platformName}</td>
						                                <td>${item.shopName}</td>
						                                <td>${item.tallyingUnitLabel}</td>
						                            </tr>
						                        </c:forEach>
                                                </tbody>
                                            </table>
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
             </div>
                  <!-- end row -->
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->
<script type="text/javascript">
//table
 $(document).ready(function() {
		//取消按钮
		$("#cancel-buttons").click(function(){
            $module.load.pageOrder('act=18001');
		});
 });
</script>
