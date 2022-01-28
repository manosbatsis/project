<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/views/common.jsp" />
<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
          <form id="add-form" action="">
          <div class="row">
               <div class="col-12">
                      <div class="card-box" >
                                                  <!--  title   start  -->
                      <div class="col-12">
                        <div>
                           <div class="col-12 row">
                            <div>
                               <ol class="breadcrumb">
                                   <li><i class="block"></i> 当前位置：</li>
                                       <li class="breadcrumb-item"><a href="javascript:$module.load.pageOrder('act=11001');">爬虫商品对照表</a></li>
                                       <li class="breadcrumb-item">详情</li>
                            </ol>
                            </div>
                           </div>
                            <!--  title   end  -->
                                  <!--  title   基本信息   start -->
                        <div class="title_text">基本信息</div>
                            <div class="info_box">
                                <div class="info_item">
                                    <div class="info_text">
                                        <span>平台名称：</span>
                                        <span>
                                            ${detail.platformName}
                                      </span>
                                    </div>
                                    <div class="info_text">
                                        <span>平台用户名：</span>
                                        <span>
                                            ${detail.platformUsername}
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>公司：</span>
                                        <span>
                                            ${detail.merchantName}
                                        </span>
                                    </div>
                                </div>
                                <div class="info_item">
                                    <div class="info_text">
                                        <span>事业部：</span>
                                        <span>
                                            ${detail.buName}
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>爬虫商品货号：</span>
                                        <span>
                                            ${detail.crawlerGoodsNo}
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>爬虫商品名称：</span>
                                        <span>
                                            ${detail.crawlerGoodsName}
                                        </span>
                                    </div>
                                </div>
                                <div class="info_item">
                                    <div class="info_text">
                                        <span>状态：</span>
                                        <span>
                                            ${detail.statusLabel}
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>创建人：</span>
                                        <span>
                                            ${detail.createUsername}
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>创建时间：</span>
                                        <span>
                                            <fmt:formatDate value="${detail.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </span>
                                    </div>
                                </div>
                                <div class="info_item">
                                    <div class="info_text">
                                        <span>修改人：</span>
                                        <span>
                                            ${detail.updateUsername }
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>修改时间：</span>
                                        <span>
                                            <fmt:formatDate value="${detail.modifyDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </span>
                                    </div>
                                    <div class="info_text">
                                    </div>
                                </div>
                            </div>


                            <!--  title   基本信息  start -->
                            <ul class="nav nav-tabs">
                                <li class="nav-item">
                                    <a href="#itemList" data-toggle="tab"
                                       class="nav-link active">商品信息</a>
                                </li>
                                <form id="form1">
                                    <input type="hidden" name='orderId' id='orderId' value=''/>
                                </form>
                                <li class="nav-item">
                                    <a href="#batchDefault" data-toggle="tab" class="nav-link">变更记录</a>
                                </li>
                            </ul>
                            <div class="tab-content">
                                <div class="tab-pane fade show active table-responsive" id="itemList">
                                    <table  id="datatable-items" class="table table-striped"  width="100%">
                                        <thead>
                                        <tr>
                                            <th>商品货号</th>
                                            <th>商品名称</th>
                                            <th>条形码</th>
                                            <th><i class="red">*</i>数量</th>
                                            <th><i class="red">*</i>销售标准单价</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach items="${itemModels}" var="item" varStatus="status">
                                            <tr>
                                                <td>${item.goodsNo}</td>
                                                <td>${item.goodsName}</td>
                                                <td>${item.barcode}</td>
                                                <td>${item.num}</td>
                                                <td>${item.price}</td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="tab-pane fade table-responsive" id="batchDefault">
                                    <table class="table table-striped"  width="100%" id="datatable-batchs">
                                        <thead>
                                        <tr>
                                            <th>商品货号</th>
                                            <th>商品名称</th>
                                            <th>条形码</th>
                                            <th><i class="red">*</i>数量</th>
                                            <th><i class="red">*</i>销售标准单价</th>
                                            <th>修改人</th>
                                            <th>修改时间</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach items="${itemModels}" var="item" varStatus="status">
                                            <tr>
                                                <td>${item.goodsNo}</td>
                                                <td>${item.goodsName}</td>
                                                <td>${item.barcode}</td>
                                                <td>${item.num}</td>
                                                <td>${item.price}</td>
                                                <td>${item.updateUsername}</td>
                                                <td>${item.modifyDate}</td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>

                             <!--   商品信息  end -->
                          <div class="form-row m-t-50">
                              <div class="col-12">
                                  <div class="row">
                                      <div class="col-5">
                                           <input type="button" class="btn btn-find waves-effect waves-light fr btn_default" id="cancel-buttons" value="返回"/>
                                      </div>
                                      <div class="col-1"></div>

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

<script type="text/javascript">

 
//返回
$("#cancel-buttons").click(function(){
	$module.load.pageOrder("act=11001");
});


</script>