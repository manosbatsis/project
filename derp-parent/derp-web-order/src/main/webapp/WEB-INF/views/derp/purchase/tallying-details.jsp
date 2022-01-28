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
              <div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                       <li><i class="block"></i> 当前位置：</li>
                       <li class="breadcrumb-item"><a href="#">采购档案</a></li>
                       <li class="breadcrumb-item"><a href="javascript:dh_list();">理货单列表</a></li>
                       <li class="breadcrumb-item"><a href="javascript:dh_details(${detail.id });">理货订单详情</a></li>
                    </ol>
                    </div>
            </div>
				  <div class="title_text">理货单据详情</div>
				  <div class="info_box">
					  <div class="info_item">
						  <div class="info_text">
							  <span>理货单据状态：</span>
							  <span>
							     ${detail.stateLabel}
							  </span>
						  </div>
						  <div class="info_text">
							  <span>理货单号：</span>
							  <span>
								  ${detail.code}
                                </span>
						  </div>
						  <div class="info_text">
							  <span>仓库名称：</span>
							  <span>
								  ${detail.depotName}
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
							  <span>理货时间：</span>
							  <span>
								  <fmt:formatDate value="${detail.tallyingDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
							  </span>
						  </div>
						  <div class="info_text">
							  <span>确认时间：</span>
							  <span>
								  <fmt:formatDate value="${detail.confirmDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
							  </span>
						  </div>
					  </div>
				  </div>
             </div>
             </div>
                  <!-- end row -->
        </div>
        <!-- container -->
    </div>
 	<!--  ================================表格 ===============================================   -->
       <div class="row mt20 pdlr15">
	           		<div class='col-12'>
	           			<div class="card-box">
	           			<ul class="nav nav-tabs">
	           				<li class="nav-item">
	           					<a href="#itemList" data-toggle="tab" class="nav-link active">商品列表</a>
	           				</li>
	           				<li class="nav-item">
	           					<a href="#batchDefault" data-toggle="tab" class="nav-link">批次明细</a>
	           				</li>
	           			</ul>
	           			<div class="tab-content">
	           				<div class="tab-pane fade show active table-responsive" id="itemList">
	           					<table id="datatable-buttons1" class="table table-striped" cellspacing="0" width="100%">
			                        <thead>
			                        <tr>
			                            <th>序号</th>
			                            <th style="white-space:nowrap;">商品货号</th>
			                            <th style="max-width: 200px">商品名称</th>
			                            <th style="white-space:nowrap;">商品条形码</th>
			                            <th style="white-space:nowrap;">海外仓理货单位</th>
			                            <th style="white-space:nowrap;">采购数量</th>
			                            <th style="white-space:nowrap;">理货数量</th>
			                            <th style="white-space:nowrap;">缺少数量</th>
			                            <th style="white-space:nowrap;">多货数量</th>
			                            <th style="white-space:nowrap;">正常数量</th>
			                        </tr>
			                        </thead>
			                        <tbody>
			                        	<c:forEach items="${detail.itemList }" var="item" varStatus="itemStatus">
			                        		<tr>
					                            <td style="white-space:nowrap;">${itemStatus.index+1 }</td>
					                            <td style="white-space:nowrap;">${item.goodsNo }</td>
					                            <td>${item.goodsName }</td>
					                            <td style="white-space:nowrap;">${item.barcode }</td>
					                            <td style="white-space:nowrap;">
					                            	<c:choose>
					                            		<c:when test="${item.tallyingUnit eq '00' }">托盘</c:when>
					                            		<c:when test="${item.tallyingUnit eq '01' }">箱</c:when>
					                            		<c:when test="${item.tallyingUnit eq '02' }">件</c:when>
					                            		<c:otherwise></c:otherwise>
					                            	</c:choose>
					                            </td>
					                            <td style="white-space:nowrap;">${item.purchaseNum }</td>
					                            <td style="white-space:nowrap;">${item.tallyingNum }</td>
					                            <td style="white-space:nowrap;">${item.lackNum }</td>
					                            <td style="white-space:nowrap;">${item.multiNum }</td>
					                            <td style="white-space:nowrap;">${item.normalNum }</td>
					                        </tr>
			                        	</c:forEach>
			                        </tbody>
			                    </table>
                            </div>
                            <div class="tab-pane fade table-responsive" id="batchDefault">
                            	<table id="datatable-buttons2" class="table table-striped" cellspacing="0" width="100%">
			                        <thead>
			                        <tr>
			                            <th>序号</th>
			                            <th>商品货号</th>
			                            <th>商品名称</th>
			                            <th>批次号</th>
			                            <th>生产日期</th>
			                            <th>失效日期</th>
			                            <th>海外仓理货单位</th>
			                            <th>损货数量</th>
			                            <th>过期数量</th>
			                            <th>正常数量</th>
			                            <th>毛重</th>
			                            <th>净重</th>
			                            <th>体积</th>
			                            <th>长</th>
			                            <th >宽</th>
			                            <th>高</th>
			                        </tr>
			                        </thead>
			                        <tbody>
			                        	<c:forEach items="${batchBean }" var="batch" varStatus="batchStatus">
			                        		<tr>
					                            <td>${batchStatus.index+1 }</td>
					                            <td>${batch.goodsNo }</td>
					                            <td>${batch.goodsName }</td>
					                            <td>${batch.batchNo }</td>
					                            <td>${batch.productionDateStr }</td>
					                            <td>${batch.overdueDateStr }</td>
					                            <td>
					                            	<c:choose>
					                            		<c:when test="${batch.tallyingUnit eq '00' }">托盘</c:when>
					                            		<c:when test="${batch.tallyingUnit eq '01' }">箱</c:when>
					                            		<c:when test="${batch.tallyingUnit eq '02' }">件</c:when>
					                            		<c:otherwise></c:otherwise>
					                            	</c:choose>
					                            </td>
					                            <td>${batch.wornNum }</td>
					                            <td>${batch.expireNum }</td>
					                            <td>${batch.normalNum }</td>
					                            <td>${batch.grossWeight }</td>
					                            <td>${batch.netWeight }</td>
					                            <td>${batch.volume }</td>
					                            <td>${batch.length }</td>
					                            <td>${batch.width }</td>
					                            <td>${batch.height }</td>
					                        </tr>
			                        	</c:forEach>
			                        </tbody>
			                    </table>
                            </div>
	           			</div>
							<div class="form-row">
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
  <!--  ================================表格  end ===============================================   -->
</div> <!-- content -->
<script type="text/javascript">
	function dh_list(){
		$module.load.pageOrder("act=3003");
	}
	
	function dh_details(id){
		$module.load.pageOrder("act=30031&id="+id);
	}
	
    //返回
    $("#cancel-buttons").click(function () {
        var pageSource = '${pageSource}';
        if (pageSource == "1") {
            $load.a("/list/menu.asyn?act=100&r="+Math.random());
        } else {
            $module.load.pageOrder("act=3003");
        }
    });
</script>
