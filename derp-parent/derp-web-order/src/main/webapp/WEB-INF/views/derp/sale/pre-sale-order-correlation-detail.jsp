<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common.jsp"/>
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
                                        <li class="breadcrumb-item"><a href="#">销售管理</a></li>
                                        <li class="breadcrumb-item"><a href="#">预售勾稽详情</a></li>
                                    </ol>
                                </div>
                            </div>
                            <!--  title   end  -->
                            <!--  title   基本信息   start -->
                            <div class="title_text">基本信息</div>

                            <div class="info_box">
                                <div class="info_item">
                                    <div class="info_text">
                                        <span>预售单号：</span>
                                        <span>
                                            ${mainInfo.preSaleOrderCode }
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>审核时间：</span>
                                        <span>
                                            <fmt:formatDate value="${mainInfo.auditDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>审核人：</span>
                                        <span>${mainInfo.auditName}
                                        </span>
                                    </div>
                                </div>
                                <div class="info_item">
                                    <div class="info_text">
                                        <span>客户：</span>
                                        <span>${mainInfo.customerName}
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>商品货号：</span>
                                        <span>
                                            ${mainInfo.goodsNo }
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>事业部：</span>
                                        <span>${mainInfo.buName}
                                        </span>
                                    </div>
                                </div>
                                <div class="info_item">

                                    <div class="info_text">
                                        <span>公司：</span>
                                        <span>
                                            ${mainInfo.merchantName }
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>商品名称：</span>
                                        <span>
                                            ${mainInfo.goodsName }
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>商品条码：</span>
                                        <span>${mainInfo.barcode }</span>
                                    </div>
                                </div>
                            </div>
                            <!--  title   基本信息  end -->
                            <div class="title_text">预售库存信息</div>
                            <div class="mt20">
                                <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                                    <thead>
                                    <tr>
                                        <th>预售数量</th>
                                        <th>销售数量</th>
                                        <th>待销数量</th>
                                        <th>出库数量</th>
                                        <th>预售余量</th>
                                        <th>预售单价</th>
                                        <th>预售总金额</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>${mainInfo.preNum}</td>
                                            <td>${mainInfo.saleNum == null ? 0 : mainInfo.saleNum}</td>
                                            <td>${mainInfo.preNum-mainInfo.saleNum}</td>
                                            <td>${mainInfo.outNum == null ? 0 : mainInfo.outNum}</td>
                                            <td>${mainInfo.preNum-mainInfo.outNum}</td>
                                            <td>${mainInfo.price}</td>
                                            <td>${mainInfo.amount}</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div class="title_text">勾稽明细</div>
                            <div class="mt20">
                                <table id="detail-buttons" class="table table-striped" cellspacing="0" width="100%">
                                    <thead>
                                    <tr>
                                        <th>销售订单</th>
                                        <th>销售数量</th>
                                        <th>审核人</th>
                                        <th>审核时间</th>
                                        <th>出库单号</th>
                                        <th>出库量</th>
                                        <th>出库时间</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${details }" var="item" varStatus="status">
                                        <tr>
                                            <td>${item.saleOrderCode}</td>
                                            <td>${item.saleNum}</td>
                                            <td>${item.auditName}</td>
                                            <td><fmt:formatDate value="${item.auditDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                            <td>${item.saleOutDepotCode}</td>
                                            <td>${item.outNum}</td>
                                            <td><fmt:formatDate value="${item.outDepotDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${details.size() == 0 }" >
                                        <tr align="center">
                                            <td colspan="7">暂无数据</td>
                                        </tr>
                                    </c:if>
                                    </tbody>
                                </table>
                            </div>
                            <div class="row mt20">
                                <div class='col-12'>
                                    <div class="form-row">
                                        <div class="col-12">
                                            <div class="row">
                                                <div class="col-5">
                                                    <button type="button"
                                                            class="btn btn-find waves-effect waves-light btn_default"
                                                            id="cancel-buttons" >返回
                                                    </button>
                                                </div>
                                            </div>
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

    </div> <!-- content -->
</div>
<script type="application/javascript">
    //取消按钮
    $("#cancel-buttons").click(function () {
        $load.a($module.params.loadOrderPageURL+'act=7001');
    });
</script>