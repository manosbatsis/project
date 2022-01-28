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
                       <li class="breadcrumb-item "><a href="javascript:$module.load.pageOrder('act=13001');">事业部移库单列表</a></li>
                       <li class="breadcrumb-item"><a href="#">事业部移库单详情</a></li>
                    </ol>
                    </div>
                   </div>
                    <!--  title   end  -->               
                    <!--  title   基本信息   start -->
                    <div class="title_text">基本信息</div>
                    <div class="info_box">
                        <div class="info_item">
                            <div class="info_text">
                                <span>移库单号：</span>
                                <span>
                                    ${detail.code}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>移入事业部：</span>
                                <span>
                                    ${detail.inBuName}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>移出事业部：</span>
                                <span>
                                    ${detail.outBuName}
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>移库仓库：</span>
                                <span>
                                    ${detail.depotName}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>来源业务单号：</span>
                                <span>
                                    ${detail.businessNo}
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
                                <span>移库状态：</span>
                                <span>
                                    ${detail.statusLabel}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>创建人：</span>
                                <span>
                                    ${detail.createName}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>移库日期：</span>
                                <span>
                               	<fmt:formatDate value="${detail.moveDate}" pattern="yyyy-MM-dd HH:mm:ss" />
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>移库币种：</span>
                                <span>
                                    ${detail.currencyLabel}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>协议币种：</span>
                                <span>
                                    ${detail.agreementCurrencyLabel}
                                </span>
                            </div>
                            <div class="info_text"></div>
                        </div>
                    </div>
                      <!--  title   基本信息  start -->
                                <ul class="nav nav-tabs">
                                    <li class="nav-item">
                                        <a href="#itemList" data-toggle="tab" 
                                           class="nav-link active">商品信息</a>
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
							                           <th>商品货号</th>
							                           <th>商品名称</th>
							                           <th>商品条码</th>
							                           <th>销售数量</th>
							                           <th>协议单价</th>
							                           <th>移库单价</th>
							                           <th>移库金额</th>
							                           <th>汇率</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach items="${detail.itemList}" var="item" varStatus="status">
						                            <tr>
						                                <td>${item.goodsNo}</td>
                                                        <td>${item.goodsName}</td>
						                                <td>${item.barcode}</td>
						                                <td>${item.num}</td>
						                                <td>${item.agreementPrice}</td>
						                                <td>${item.price}</td>
						                                <td>${item.amount}</td>
						                                <td>${item.rate}</td>
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
            $module.load.pageOrder('act=13001');
		});
     initDataTabel() ;
 });
</script>
