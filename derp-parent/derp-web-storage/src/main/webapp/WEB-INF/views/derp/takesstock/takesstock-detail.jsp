<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- Start content -->
<jsp:include page="/WEB-INF/views/common.jsp" />
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
            <div class="row">
                <div class="col-12">
                    <div class="card-box table-responsive">
                        <!--  title   start  -->
                        <div class="col-12">
                            <div>
                                <div class="col-12 row">
                                    <div>
                                        <ol class="breadcrumb">
                                            <li><i class="block"></i> 当前位置：</li>
                                            <li class="breadcrumb-item"><a href="#">盘点指令详情</a></li>
                                        </ol>
                                    </div>
                                </div>
                                
                                <div class="info_box">
                                    <div class="info_item">
                                        <div class="info_text">
                                            <span>盘点单号：</span>
                                            <span>
                                                ${takesStock.code }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <input type="hidden" id="takesStockId" value="${takesStock.id }">
                                            <span>盘点仓库：</span>
                                            <span>
                                                ${takesStock.depotName }
                                            </span>
                                        </div>
                                       <div class="info_text">
                                            <span>备注： </span>
                                            <span>${takesStock.remark }</span>
                                        </div>
                                    </div>
                                    <div class="info_item">
                                        <div class="info_text">
                                            <span>服务类型： </span>
                                            <span>${takesStock.serverTypeLabel}
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>单据类型： </span>
                                            <span>${takesStock.statusLabel}
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>业务场景： </span>
                                            <span>${takesStock.modelLabel}
                                            </span>
                                        </div>
                                    </div>
                                    <div class="info_item">
                                        <div class="info_text">
                                            <span>提交人： </span>
                                            <span>${takesStock.submitUsername }</span>
                                        </div>
                                        <div class="info_text">
                                            <span>创建人： </span>
                                            <span>${takesStock.createUsername }</span>
                                        </div>
                                        <div class="info_text">
                                            <span>委托单位： </span>
                                            <span>${takesStock.merchantName }</span>
                                        </div>
                                    </div>
                                    <div class="info_item">
                                        <div class="info_text">
                                            <span>提交时间： </span>
                                            <span>${takesStock.submitTime }</span>
                                        </div>
                                        <div class="info_text">
                                            <span>创建时间 ： </span>
                                            <span>${takesStock.createTime }</span>
                                        </div>
                                        <div class="info_text"></div>
                                    </div>
                                </div>


                                <!--  title   基本信息  start -->
                                <!--   商品信息  start -->
                                <div class="title_text">商品信息</div>
                                <div class="mt10">
                                    <table id="datatable-buttons" class="table table-striped" cellspacing="0"
                                           width="100%">
                                        <thead>
                                        <tr>
                                            <th>序号</th>
                                            <th>商品编号</th>
                                            <th>商品货号</th>
                                            <th>商品名称</th>
                                            <th>条码</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                           <c:forEach items="${itemList}" var="item" varStatus="itemStatus">
						                         <tr>
						                            <td>${itemStatus.index+1 }</td>
					                        		 <td>${item.goodsCode}</td>
						                             <td>${item.goodsNo}</td>
						                             <td>${item.goodsName}</td>
						                             <td>${item.barcode}</td>
					                        	</tr>
					                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div> 
                                <!--   商品信息  end -->
                                <div class="form-row m-t-50">
			                          <div class="col-12">
			                              <div class="row">
			                                  <div class="col-12">
			                                       <a class="btn btn-find waves-effect waves-light btn_default" href="javascript:$module.load.pageStorage('act=9001');">返回</a>
			                                  </div>
			                              </div>
			                          </div>
			                      </div>
                            </div>
                        </div>
                        <!-- 新增弹窗部分   start -->
                    </div>
                    <!-- end row -->
    </div>
    <!-- container -->
</div>
</div> <!-- content -->

