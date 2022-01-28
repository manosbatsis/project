<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- Start content -->
<jsp:include page="/WEB-INF/views/common.jsp" />
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
        <form id="add-form" action="/merchandise/saveMerchandise.asyn">
            <div class="row">
                <!--  title   start  -->
                <div class="col-12">
                    <div class="card-box">
                        <div class="col-12 row">
                            <div>
                                <ol class="breadcrumb">
                                    <li><i class="block"></i> 当前位置：</li>
                                    <li class="breadcrumb-item"><a href="#">盘点结果详情</a></li>
                                </ol>
                            </div>
                        </div>
                        <!--  title   end  -->
                   
                        <div class="info_box">
                            <div class="info_item">
                                <div class="info_text">
                                    <span>盘点单号：</span>
                                    <span>
                                        ${takesStockResult.code }
                                    </span>
                                </div>
                            </div>
                            <div class="info_item">
                                <div class="info_text">
                                    <span>单据状态：</span>
                                    <span>
                                        ${takesStockResult.statusLabel}
                                    </span>
                                </div>
                                <div class="info_text">
                                    <span>服务类型：</span>
                                    <span>
                                        ${takesStockResult.serverTypeLabel}
                                    </span>
                                </div>
                                <div class="info_text">
                                    <span>创建时间：</span>
                                    <span>
                                        <fmt:formatDate value="${takesStockResult.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </span>
                                </div>
                            </div>
                            <div class="info_item">
                                <div class="info_text">
                                    <span>盘点仓库： </span>
                                    <span>${takesStockResult.depotName}</span>
                                </div>
                                <div class="info_text">
                                    <span>业务场景： </span>
                                    <span>
                                        ${takesStockResult.modelLabel}
                                    </span>
                                </div>
                                <div class="info_text">
                                    <span>接收时间： </span>
                                    <span>
                                        <fmt:formatDate value="${takesStockResult.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </span>
                                </div>
                            </div>
                            <div class="info_item">
                                <div class="info_text">
                                    <span>备注： </span>
                                    <span>${takesStockResult.remark}</span>
                                </div>
                                <div class="info_text">
                                    <span>确认人： </span>
                                    <span>${takesStockResult.confirmUsername}</span>
                                </div>
                                <div class="info_text">
                                    <span>确认时间：</span>
                                    <span>
                                        <fmt:formatDate value="${takesStockResult.confirmTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </span>
                                </div>
                            </div>
                            <div class="info_item">
                                <div class="info_text">
                                    <span>盘点指令单号： </span>
                                    <span>${takesStockResult.takesStockCode}</span>
                                </div>
                                <div class="info_text">
                                    <span>驳回原因： </span>
                                    <span>${takesStockResult.dismissRemark}</span>
                                </div>
                                <div class="info_text">
                                    <span>来源： </span>
                                    <span>${takesStockResult.sourceTypeLabel}
                                    </span>
                                </div>
                            </div>
                            <div class="info_item">
                                <div class="info_text">
                                    <span>币种： </span>
                                    <span>${takesStockResult.currencyLabel}
                                    </span>
                                </div>
                                <div class="info_text">
                                    <span>入库确认人： </span>
                                    <span>${takesStockResult.inConfirmUsername}</span>
                                </div>
                                <div class="info_text">
                                    <span>入库确认时间： </span>
                                    <span>
                                        <fmt:formatDate value="${takesStockResult.inConfirmTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </span>
                                </div>
                            </div>
                              <div class="info_item">
                                <div class="info_text">
                                    <span>创建人： </span>
                                    <span>${takesStockResult.createUsername}
                                    </span>
                                </div>
                                 <div class="info_text">
                                     <span>来源单号 ： </span>
                                     <span>${takesStockResult.sourceCode }</span>
                                  </div>
                                <div class="info_text"></div>
                            </div>
                        </div>
                        <div class="title_text">商品信息</div>
                        <div class="mt10 table-responsive"> 
                            <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                                <thead>
                                <tr>
                                    <th style="white-space:nowrap;">序号</th>
                                    <th style="white-space:nowrap;">事业部</th>
                                    <th style="white-space:nowrap;">商品货号</th>
                                    <th style="white-space:nowrap;">商品名称</th>
                                    <th style="white-space:nowrap;">商品条形码</th>
                                    <th style="white-space:nowrap;">结算单价</th>
                                    <th style="white-space:nowrap;">盘盈数量</th>
                                    <th style="white-space:nowrap;">盘亏数量</th>
                                    <th style="white-space:nowrap;">调整类型</th>
                                    <th style="white-space:nowrap;">批次号</th>
                                    <th style="white-space:nowrap;">是否坏品</th>
                                    <th style="white-space:nowrap;">总调整数量</th>
                                    <th style="white-space:nowrap;">生产日期</th>
                                    <th style="white-space:nowrap;">失效日期</th>
                                    <th style="white-space:nowrap;">海外仓理货单位</th>
                                    <th style="white-space:nowrap;">盘点原因</th>
                                </tr>
                                </thead>
                                <tbody>
                                 <c:forEach items="${itemList}" var="item" varStatus="itemStatus">
			                         <tr>
			                            <td>${itemStatus.index+1 }</td>
			                             <td>${item.buName}</td>
			                             <td>${item.goodsNo}</td>
			                             <td>${item.goodsName}</td>
			                             <td>${item.barcode}</td>
			                             <td>${item.settlementPrice}</td>
			                             <td>${item.surplusNum}</td>
			                             <td>${item.deficientNum}</td>
			                             <td>${item.typeLabel}</td>
			                             <td>${item.batchNo}</td>
			                             <td>${item.isDamageLabel}</td>
			                             <td>${item.adjustTotal}</td>
			                             <td><fmt:formatDate value="${item.productionDate}" pattern="yyyy-MM-dd"/></td>
			                             <td><fmt:formatDate value="${item.overdueDate}" pattern="yyyy-MM-dd"/></td>
		                        		 <td>${item.tallyUnitLabel}</td>
		                        		 <td>${item.reasonCodeLabel}</td>
		                        	</tr>
		                        </c:forEach>
                                </tbody>
                            </table>
                        </div>
                         <div class="form-row m-t-50">
	                          <div class="col-12">
	                              <div class="row">
	                                  <div class="col-5">
	                                       <a class="btn btn-find waves-effect waves-light btn_default" href="javascript:$module.load.pageStorage('act=9002');">返回</a>
	                                  </div>
	                              </div>
	                          </div>
	                      </div>
                    </div>
                    <!-- end row -->
                </div>
                <!-- container -->
            </div>
        </form>
    </div> <!-- content -->
</div>