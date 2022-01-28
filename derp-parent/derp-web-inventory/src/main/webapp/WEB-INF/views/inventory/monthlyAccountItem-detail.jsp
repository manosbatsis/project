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
                       <li class="breadcrumb-item"><a href="#">库存管理</a></li>
                       <li class="breadcrumb-item"><a href="javascript:list();">月库存转结列表</a></li>
                       <li class="breadcrumb-item"><a href="#">库存月结详情</a></li>
                    </ol>
                    </div>
                   </div>
                    <!--  title   end  -->               
                          <!--  title   基本信息   start -->

                    <div class="title_text">库存月结详情</div>
                    <div class="info_box">
                        <div class="info_item">
                            <div class="info_text">
                                <span>结转状态：</span>
                                <span>
                                ${monthlyAccountModel.stateLabel}                                     
                                </span>
                            </div>
                            <div class="info_text">
                                <span>结转月份：</span>
                                <span>
                                    ${monthlyAccountModel.settlementMonth}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>结算时间：</span>
                                <span>
                                    <fmt:formatDate value="${monthlyAccountModel.settlementDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>公司：</span>
                                <span>
                                    ${monthlyAccountModel.merchantName}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>期初时间：</span>
                                <span>
                                    <fmt:formatDate value="${monthlyAccountModel.firstDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </span>
                            </div>
                            <div class="info_text">
                                <span>结算人：</span>
                                <span>
                                    ${monthlyAccountModel.planName}
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>期末时间：</span>
                                <span>
                                     <fmt:formatDate value="${monthlyAccountModel.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </span>
                            </div>
                            <div class="info_text">
                                <span>创建时间：</span>
                                <span>
                                    <fmt:formatDate value="${monthlyAccountModel.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </span>
                            </div>
                            <div class="info_text">

                            </div>
                        </div>
                    </div>
                 <!--  title   基本信息  end -->
                    <div class="title_text">商品信息</div>
                <div class="row col-12 mt20">
                    <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th>序号</th>
                            <th>仓库名称</th>
                            <th>商品货号</th>
                            <th>商品名称</th>
                            <th>商品条码</th>
                            <th>批次</th>
                            <th>库存余量</th>
                            <c:if test="${userType == '2' ||userType == '1'}">
                            	<th>仓库现存量</th>
                            </c:if>                           
                            <th>单位</th>
                            <th>库存类型</th>
                            <th>期末结转库存</th>
                            <th>是否过期</th>
                            <th>标准条码</th>
                        </tr>
                        </thead>
                        <tbody>
                              <c:forEach items="${monthlyAccountItemModelList}" var="item" varStatus="status">
	                           <tr>
		                           <td>${status.index + 1 }</td>
		                           <td>${item.depotName}</td>
		                           <td>${item.goodsNo}</td>
		                           <td>${item.goodsName}</td>
		                           <td>${item.barcode}</td>
		                           <td>${item.batchNo}</td>
		                           <td>${item.surplusNum}</td>
		                           <c:if test="${userType == '2' ||userType == '1'}">
		                           		<td>${item.availableNum}</td>
		                           </c:if>
		                           <td>
		                           ${item.unitLabel}		                           		
		                           </td>
		                           <td>
		                           ${item.typeLabel}
		                           </td>
		                           <td>${item.settlementNum}</td>
		                           <td>
		                           ${item.isExpireLabel}		                           		
		                           </td>
		                           <td>${item.commbarcode}</td>
			                   </tr>
	                      </c:forEach>
                        </tbody>
                    </table>
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
                           <!-- 新增弹窗部分   start -->
             </div>
                  <!-- end row -->
          </form>
        </div>
        <!-- container -->
    </div>
    </div>
</div> <!-- content -->
<script type="text/javascript">

function list(){
	$module.load.pageInventory("bls=6005");
}

//返回
$("#cancel-buttons").click(function () {
    $module.load.pageInventory("bls=6005");
});

</script>