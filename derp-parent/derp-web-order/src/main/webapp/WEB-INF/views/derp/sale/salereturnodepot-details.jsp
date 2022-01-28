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
                       <li class="breadcrumb-item"><a href="#">销售退货出库单详细</a></li>
                    </ol>
                    </div>
                   </div>
                    <!--  title   end  -->               
                          <!--  title   基本信息   start -->
                    <div class="title_text">基本信息</div>
     <%--           <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">退运出库单号<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                            	<input type="hidden" value="${detail.id}" name="pId" id="pId">
                            	${detail.code}
                            </div>
                        </div>
                    </div>
                  </div>   
                <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">单据状态<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                            	<c:if test="${detail.status == '016'}">已出仓</c:if>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">退出仓库<span>：</span></span></label>
                            <div class="col-5 ml10 line">
                           		${detail.outDepotName}
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">服务类型 <span>：</span></span></label>
                            <div class="col-8 ml10 line">
                            	<c:if test="${detail.serveTypes == 'E0'}">区内调拨调出服务</c:if>
                                <c:if test="${detail.serveTypes == 'F0'}">区内调拨调入服务</c:if>
                                <c:if test="${detail.serveTypes == 'G0'}">库内调拨服务</c:if>
                            </div>
                        </div>
                    </div>
                  </div>   
                  <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">企业退运单号<span>：</span></span></label>
                            <div class="col-5 ml10 line">
                                ${detail.merchantReturnNo}
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">退入仓库<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                            	${detail.inDepotName}
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                    	<div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">业务场景<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                            	<c:if test="${detail.model == '40'}">账册内货权转移</c:if>
                           		<c:if test="${detail.model == '50'}">账册内货权转移调仓</c:if>
                            </div>
                        </div>
                    </div>
                 </div>
                <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">合同号<span>：</span></span></label>
                            <div class="col-5 ml10 line">
                            	${detail.contractNo}
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                        	<label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">申报地海关编码<span>：</span></span></label>
                            <div class="col-5 ml10 line">
                            	${detail.customsNo}
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">退运出库时间<span>：</span></span></label>
                            <div class="col-5 ml10 line">
                            	<fmt:formatDate value="${detail.returnOutDate}" pattern="yyyy-MM-dd HH:mm:ss" />
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">委托单位 <span>：</span></span></label>
                            <div class="col-8 ml10 line">
                                ${detail.merchantName}
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">申报地国检编码 <span>：</span></span></label>
                            <div class="col-8 ml10 line">
                                ${detail.inspectNo}
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">退货客户 <span>：</span></span></label>
                            <div class="col-8 ml10 line">
                                ${detail.customerName}
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                	<div class=col-12>
                		<div class="row">
                			 <label class="col-1 col-form-label " style="white-space:nowrap;"><span class="fr">备注 <span>：</span></span></label>
                			 <div class="col-11 ml10 line">
                			 	${detail.remark}
                            </div>
                		</div>
                	</div>
                </div>--%>
                    <div class="info_box">
                        <div class="info_item">
                            <div class="info_text">
                                <span>销退出库单号：</span>
                                <span>
                                   <input type="hidden" value="${detail.id}" name="pId" id="pId">
                            	    ${detail.code}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>单据状态：</span>
                                <span>
                                   ${detail.statusLabel}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>退出仓库：</span>
                                <span>
                                    ${detail.outDepotName}
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>服务类型：</span>
                                <span>
                                	  ${detail.serveTypesLabel}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>销退订单号：</span>
                                <span>
                                    ${detail.orderCode}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>退入仓库：</span>
                                <span>
                                    ${detail.inDepotName}
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>业务场景：</span>
                                <span>
                                ${detail.modelLabel}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>合同号：</span>
                                <span>
                                    ${detail.contractNo}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>申报地海关编码：</span>
                                <span>
                                    ${detail.customsNo}
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>退货出库时间：</span>
                                <span>
                                   <fmt:formatDate value="${detail.returnOutDate}" pattern="yyyy-MM-dd HH:mm:ss" />
                                </span>
                            </div>
                            <div class="info_text">
                                <span>委托单位：</span>
                                <span>
                                    ${detail.merchantName}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>申报地国检编码：</span>
                                <span>
                                    ${detail.inspectNo}
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>退货客户：</span>
                                <span>
                                    ${detail.customerName}
                                </span>
                            </div>
                            <div class="info_text">
                             	<span>事业部：</span>
                                <span>
                                    ${detail.buName}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>备注：</span>
                                <span>
                                    ${detail.remark}
                                </span>
                            </div>
                            
                        </div>
                    </div>
                 <!--  title   基本信息  start -->
                   <!--   商品信息  start -->

                    <div class="title_text">销售退货出库商品明细</div>
                    <div class="form-row mt20">
                   <table id="table-list" class="table table-striped">
                      <tr>
                      	 <th>序号</th>
                         <th>退出商品编号</th>
                         <th>退出商品货号</th>
                         <th>退出商品名称</th>
                         <th>退货出库数量</th>
                         <th>退货好品数量</th>
                         <th>退货过期数量</th>
                         <th>退货坏品数量</th>
                         <th>退出批次</th>
                         <th>生产日期</th>
                         <th>失效日期</th>
                      </tr>
                      <c:forEach items="${detail.itemList}" var="item" varStatus="status">
                    	<tr>
                    		<td>${status.index+1}</td>
                            <td>${item.outGoodsCode}</td>
                            <td>${item.outGoodsNo}</td>
                            <td>${item.outGoodsName}</td>
                            <td>${item.returnNum+item.wornNum+item.expireNum}</td>
                            <td>${item.returnNum}</td>
                            <td>${item.expireNum}</td>
                            <td>${item.wornNum}</td>
                            <td>${item.returnBatchNo}</td>
                            <td><fmt:formatDate value="${item.productionDate}" pattern="yyyy-MM-dd" /> </td>
                            <td><fmt:formatDate value="${item.overdueDate}" pattern="yyyy-MM-dd" /></td>
                        </tr>
                      </c:forEach>
                  </table>                              
                   </div>
                 <!--   商品信息  end -->
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
			$module.load.pageOrder("act=5003");
		});
 }); 
</script>
