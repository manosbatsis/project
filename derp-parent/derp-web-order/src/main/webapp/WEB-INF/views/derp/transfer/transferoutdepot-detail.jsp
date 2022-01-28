<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

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
                       <li class="breadcrumb-item"><a href="#">调拨管理</a></li>
                       <li class="breadcrumb-item"><a href="javascript:$module.load.pageOrder('act=8003');">调拨出库列表</a></li>
                       <li class="breadcrumb-item"><a href="#">调拨出库详情</a></li>
                    </ol>
                    </div>
                   </div>
                    <!--  title   end  -->               
                          <!--  title   基本信息   start -->
                    <div class="title_text">调拨出库详情</div>
 
                    <div class="info_box">
                        <div class="info_item">
                            <div class="info_text">
                                <span>调拨出单号：</span>
                                <span>
                                    ${transferOutDepot.code }
                                </span>
                            </div>
                            <div class="info_text">
                                <span>调出仓库：</span>
                                <span>
                                    ${transferOutDepot.outDepotName }
                                </span>
                            </div>
                            <div class="info_text">
                                <span>服务类型：</span>
                                <span>${transferOrder.serveTypesLabel}
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>单据状态：</span>
                                <span>${transferOutDepot.statusLabel}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>调入仓库：</span>
                                <span>
                                    ${transferOutDepot.inDepotName }
                                </span>
                            </div>
                            <div class="info_text">
                                <span>业务场景：</span>
                                <span>${transferOrder.modelLabel}
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>企业调拨单号：</span>
                                <span>
                                    ${transferOutDepot.transferOrderCode }
                                </span>
                            </div>
                            <div class="info_text">
                                <span>申报海关：</span>
                                <span>
                                    ${outDepotModel.customsNo }
                                </span>
                            </div>
                            <div class="info_text">
                                <span>调拨出库时间：</span>
                                <span>
                                     <fmt:formatDate value="${transferOutDepot.transferDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>合同号：</span>
                                <span>
                                    ${transferOutDepot.contractNo }
                                </span>
                            </div>
                            <div class="info_text">
                                <span>申报地国检：</span>
                                <span>
                                    ${outDepotModel.inspectNo }
                                </span>
                            </div>
                            <div class="info_text">
                                <span>事业部：</span>
                                <span>
                                    ${transferOutDepot.buName }
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>委托单位：</span>
                                <span>
                                    ${transferOutDepot.merchantName }
                                </span>
                            </div>
                            <div class="info_text">
                                <span>调入客户：</span>
                                <span>
                                    ${transferOutDepot.inCustomerName }
                                </span>
                            </div>
                            <div class="info_text">
                                <span>创建人：</span>
                                <span>
                                    ${transferOutDepot.createName }
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>备注：</span>
                                <span>
                                    ${transferOrder.remark }
                                </span>
                            </div>
                            <div class="info_text">
                                <span>调出公司：</span>
                                <span>
                                    ${transferOutDepot.merchantName }
                                </span>
                            </div>
                            <div class="info_text">
                                <span>创建时间：</span>
                                <span>
                                    ${transferOutDepot.createDate }
                                </span>
                            </div>
                        </div>
                    </div>
                 <!--  title   基本信息  end -->
                    <div class="title_text">商品信息</div>
                <div class="mt20">
                    <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th>序号</th>
                            <th>调出商品编号</th>
                            <th>调出商品货号</th>
                            <th>调出商品名称</th>
                            <th>调拨出库数量</th>
                            <th>正常数量</th>
                            <th>坏品数量</th>
                            <th>过期数量</th>
                            <th>调出批次</th>
                            <th>生产日期</th>
                            <th>失效日期</th>
                            <th>理货单位</th>
                        </tr>
                        </thead>
                        <tbody>
                              <c:forEach items="${itemList }" var="item" varStatus="status">
	                           <tr>
		                           <td>${status.index + 1 }</td>
		                           <td>${item.outGoodsCode}</td>
			                       <td>${item.outGoodsNo}</td>
			                       <td>${item.outGoodsName}</td>
                                   <td>${item.transferNum+item.wornNum+item.expireNum}</td>
                                   <td>${item.transferNum}</td>
                                   <td>${item.wornNum}</td>
                                   <td>${item.expireNum}</td>
			                       <td>${item.transferBatchNo}</td>
			                       <td><fmt:formatDate value="${item.productionDate}" pattern="yyyy-MM-dd"/></td>
			                       <td><fmt:formatDate value="${item.overdueDate}" pattern="yyyy-MM-dd"/></td>
			                       <td>${item.tallyingUnitLabel}
			                           <%--<c:choose>--%>
                                         <%--<c:when test="${item.tallyingUnit=='00'}">托盘</c:when>--%>
                                         <%--<c:when test="${item.tallyingUnit=='01'}">箱</c:when>--%>
                                         <%--<c:when test="${item.tallyingUnit=='02'}">件</c:when>--%>
                                         <%--<c:otherwise>${item.tallyingUnit}</c:otherwise>--%>
                                       <%--</c:choose>--%>
                                   </td>
			                   </tr>
	                      </c:forEach>
                        </tbody>
                    </table>
                </div>
                    <div class="form-row m-t-50">
                        <div class="col-12">
                            <div class="row">
                                <div class="col-5">
                                    <a class="btn btn-find waves-effect waves-light btn_default" href="javascript:$module.load.pageOrder('act=8003');">返回</a>
                                </div>
                            </div>
                        </div>
                    </div>
                
                </div>
              </div>
                           <!-- 新增弹窗部分   start -->
             </div>
                  <!-- end row -->
          </form>
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->
</div>
