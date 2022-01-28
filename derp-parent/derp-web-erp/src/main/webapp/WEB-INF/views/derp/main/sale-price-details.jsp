<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt60">
        <!-- Start row -->
          <form id="add-form">
              <div class="row">
              <div class="card-box margin w100">
              	<div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                       <li><i class="block"></i> 当前位置：</li>
                       <li class="breadcrumb-item"><a href="#">客户档案</a></li>
                        <li class="breadcrumb-item"><a href="javascript:$load.a('/customerSale/toPage.html');">销售价格管理</a></li>
                        <li class="breadcrumb-item"><a href="javascript:$load.a('/customerSale/toDetailsPage.html');">销售价格管理详情</a></li>
                    </ol>
                    </div>
            </div>
                  
                       <div class="margin">
                       <div class="title_text">客户详情</div>
                    <div class="info_box">
                      <div class="info_item">
                          <div class="info_text">
                              <span>客户名称：</span>
                              <span>${customerSale.customerName }</span>
                          </div>
                          <div class="info_text">
                              <span>客户编号：</span>
                              <span>${customerSale.customerCode }</span>
                          </div>
                          <div class="info_text"></div>
                      </div>
                      <div class="info_item">
                          <div class="info_text">
                              <span>主数据客户ID：</span>
                              <span>${customerSale.mainId }</span>
                          </div>
                          <div class="info_text">
                          </div>
                          <div class="info_text"></div>
                      </div>
                  </div>
                   <div class="title_text">商品详情</div>
                   	<div class="info_box">
                   	<div class="info_item">
	                   	<div class="info_text">
	                   		<span>标准条码：</span>
	                   		<span>${customerSale.commbarcode }</span>
	                   		</div>
	                   		<%-- <div class="info_text">
	                   		<span>条形码：</span>
	                   		<span>${customerSale.barcode }</span>
	                   		</div> --%>
	                   		<div class="info_text">
	                   		<span>品牌：</span>
	                   		<span>${customerSale.brandName }</span>
	                   		</div>
	                   	</div>
                   	</div>
	                   	<div class="info_box">
	                   	<div class="info_item">
	                   	<div class="info_text">
	                   		<span>商品名称：</span>
	                   		<span>${customerSale.goodsName }</span>
	                   		</div>
	                   		<div class="info_text">
	                   		<span>规格型号：</span>
	                   		<span>${customerSale.spec }</span>
	                   		</div>
	                   		<div class="info_text">
	                   		<span>品类：</span>
	                   		<span>${customerSale.goodsClassifyName }</span>
	                   		</div>
	                   		</div>
	                   	</div>
	                   	<div class="info_box">
	                   	<div class="info_item">
	                   		<div class="info_text">
		                   		<span>计量单位：</span>
		                   		<span>${customerSale.unitName }</span>
	                   		</div>
	                   		<div class="info_text">
		                   		<span>是否组合：</span>
		                   		<span>
		                   		  <c:choose>
                                        <c:when test="${customerSale.isGroup =='0'}">否</c:when>
                                         <c:when test="${customerSale.isGroup =='1'}">是</c:when>
                                  </c:choose>	
		                   		
		                   		</span>
	                   		</div>
	                   	<div class="info_text">
	                   		<span></span>
	                   		<span></span>
	                   	</div>
	                   	</div>
	                   	</div>
	                   	<div class="title_text">商品报价记录</div>
	                      <div class="form-row mt20 ml15">
	                          <table id="table-list-group" class="table table-striped">
			                      <tr>
										<th>序号</th>
									   <th>生效日期</th>
			                           <th>失效日期</th>
			                           <th>币种</th>
			                           <th>销售价</th>
			                           <th>创建日期</th>
			                           <th>修改日期</th>
			                      </tr>
			                      <c:forEach items="${customerSalePriceList}" var="item" varStatus="status">
			                      <tr>
			                       
				                           <td>${status.index+1}</td>
			                               <td><fmt:formatDate value="${item.effectiveDate}" pattern="yyyy-MM-dd"/></td>
			                               <td><fmt:formatDate value="${item.expiryDate}" pattern="yyyy-MM-dd"/></td>
			                               
			                               <td>
			                                  ${item.currencyLabel}
			                               </td>
			                            
			                               <td>${item.salePrice }</td>
											<td><fmt:formatDate value="${item.createDate}" pattern="yyyy-MM-dd"/></td>
			                               <td><fmt:formatDate value="${item.modifyDate}" pattern="yyyy-MM-dd"/></td>
			                      </tr>
			                      </c:forEach>
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
                  <!-- end row -->
          </form>
        </div>
        <!-- container -->
    </div>
</div> <!-- content -->
<script type="text/javascript">

		//取消按钮
		$("#cancel-buttons").click(function(){
			$load.a("/customerSale/toPage.html");
		});

</script>
