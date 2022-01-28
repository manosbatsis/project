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
                       <li class="breadcrumb-item"><a href="#">调拨管理</a></li>
                        <li class="breadcrumb-item"><a href="javascript:$module.load.pageOrder('act=8002');">调拨理货单列表</a></li>
                       <li class="breadcrumb-item"><a href="#">调拨理货单详情</a></li>
                    </ol>
                    </div>
            </div>
                  <div class="title_text">理货单据详情</div>
           
                  <div class="info_box">
                      <div class="info_item">
                          <div class="info_text">
                              <span>理货单据状态：</span>
                              <span>${detail.stateLabel}
                              </span>
                          </div>
                          <div class="info_text">
                              <span>理货单号：</span>
                              <span>
                                  ${detail.code}
                              </span>
                          </div>
                      </div>
                      <div class="info_item">
                          <div class="info_text">
                              <span>仓库名称：</span>
                              <span>
                                  ${detail.depotName}
                              </span>
                          </div>
						  <div class="info_text">
							  <span>事业部：</span>
							  <span>
								  ${detail.buName}
							  </span>
						  </div>

                      </div>
                      <div class="info_item">
						  <div class="info_text">
							  <span>合同号：</span>
							  <span>
								  ${detail.contractNo}
							  </span>
						  </div>
                          <div class="info_text">
                              <span>理货时间：</span>
                              <span>
                                 <fmt:formatDate value="${detail.tallyingDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                              </span>
                          </div>

                      </div>
                      <div class="info_item">
						  <div class="info_text">
							  <span>确认人：</span>
							  <span>
								  ${detail.confirmUserName}
							  </span>
						  </div>
                          <div class="info_text">
                              <span>确认时间：</span>
                              <span>
                                 <fmt:formatDate value="${detail.confirmDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                              </span>
                          </div>

                      </div>
					  <div class="info_item">
						  <div class="info_text">
							  <span>创建时间：</span>
							  <span>
                                 <fmt:formatDate value="${detail.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
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
			                            <th>商品货号</th>
			                            <th>商品名称</th>
			                            <th>商品条形码</th>
			                            <th>申报数量</th>
			                          <!--   <th>理货数量</th>
			                            <th>缺少数量</th>
			                            <th>多货数量</th> -->
			                            <th>理货数量</th>
			                        </tr>
			                        </thead>
			                        <tbody>
			                        	<c:forEach items="${detail.itemList }" var="item" varStatus="itemStatus">
			                        		<tr>
					                            <td>${itemStatus.index+1 }</td>
					                            <td>${item.goodsNo }</td>
					                            <td>${item.goodsName }</td>
					                            <td>${item.barcode }</td>
					                            <td>${item.purchaseNum }</td>
					                          <%--   <td>${item.tallyingNum }</td>
					                            <td>${item.lackNum }</td>
					                            <td>${item.multiNum }</td> --%>
					                            <td>${item.tallyingNum }</td>
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
			                            <th>效期至</th>
			                            <th>损货数量</th>
			                            <th>过期数量</th>
			                            <th>正常数量</th>
			                            <th>毛重(kg)</th>
			                            <th>净重(kg)</th>
			                            <th>体积(cm³)</th>
			                            <th>长(cm)</th>
			                            <th>宽(cm)</th>
			                            <th>高(cm)</th>
			                        </tr>
			                        </thead>
			                        <tbody>
			                        	<c:forEach items="${batchBean }" var="batch" varStatus="batchStatus">
			                        		<tr>
					                            <td>${batchStatus.index+1 }</td>
					                            <td>${batch.goodsNo }</td>
					                            <td>${batch.goodsName }</td>
					                            <td>${batch.batchNo }</td>
					                            <td><fmt:formatDate value="${batch.productionDate }" pattern="yyyy-MM-dd"/></td>
					                            <td><fmt:formatDate value="${batch.overdueDate }" pattern="yyyy-MM-dd"/></td>
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
                             <div class="form-row m-t-50">
		                          <div class="col-12">
		                              <div class="row">
		                                  <div class="col-12">
		                                       <a class="btn btn-find waves-effect waves-light btn_default" href="javascript:void(0);" onclick="cancel();">返回</a>
		                                  </div>

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
	//确认、驳回010-确认 004驳回
    function tallyConfirm(state){
		//获取选中的id信息
		var ids="${detail.id}";
	    $custom.alert.warning("确定提交？",function(){
	        var url=serverAddr+"/transferTallying/tallyConfirmTransfer.asyn";
			$custom.request.ajax(url,{"ids":ids,"state":state},function(data){
				 if(data.state==200){
				    if(data.data.failCode!=''&&data.data.failCode!=undefined){
					    $custom.alert.success('完成\n<font size="1">理货失败单号：\n'+data.data.failCode+'</font>');
					}else{
					    $custom.alert.success('完成');
					    $module.load.pageOrder('act=8002');
					}
					
				}else{
					$custom.alert.error(data.message);
				} 
			});
		});
	}

	function cancel() {
        var pageSource = '${pageSource}';
        if (pageSource == "1") {
            $load.a("/list/menu.asyn?act=100&r="+Math.random());
        } else {
            $module.load.pageOrder('act=8002')
        }
    }
</script>
                       