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
                       <li class="breadcrumb-item"><a href="#">预售订单详情</a></li>
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
                                    ${detail.code}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>创建人：</span>
                                <span>
                                    ${detail.createName}
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
                                <span>客户：</span>
                                <span>
                                    ${detail.customerName}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>公司：</span>
                                <span>
                                    ${detail.merchantName}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>销售类型：</span>
                                <span>
                                    ${detail.businessModelLabel}
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>出仓仓库：</span>
                                <span>
                                    ${detail.outDepotName}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>事业部：</span>
                                <span>
                                    ${detail.buName}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>PO单号：</span>
                                <span>
                                    ${detail.poNo}
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>销售币种：</span>
                                <span>
                                    ${detail.currencyLabel}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>备注：</span>
                                <span>
                                    ${detail.remark}
                                </span>
                            </div>
                            <div class="info_text"></div>
                        </div>
                        <div class="info_item">
                        	<div class="info_text">
                                <span>理货单位：</span>
                                <span>
                                    ${detail.tallyingUnitLabel}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>审核人：</span>
                                <span>
                                    <span>${detail.auditName}</span>
                                </span>
                            </div>
                            <div class="info_text">
                                <span>审核时间：</span>
                                <span>${detail.auditDate}</span>
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
							                           <th>条码</th>
							                           <th>预售数量</th>
							                           <th>预售单价</th>
							                           <th>预售总金额</th>
							                           <th>品牌类型</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach items="${detail.itemList}" var="item" varStatus="status">
						                            <tr>
						                                <td>${item.goodsNo}</td>
                                                        <td>${item.goodsName}</td>
						                                <td>${item.barcode}</td>
						                                <td>${item.num}</td>
						                                <td>${item.price}</td>
						                                <td name='amount'><fmt:formatNumber value="${item.amount}" pattern="#.##" minFractionDigits="2" /></td>
						                                <td>${item.brandName}</td>
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
            $module.load.pageOrder('act=6001');
		});
     initDataTabel() ;
 }); 
 
 //tab切换时，初始化对应表格
 $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
 	  var activeTab = $(e.target).text(); 
 	  initDataTabel(activeTab) ;
 }) ;
 
	function initDataTabel(activeTab){
		//初始化
	    $mytable.fn.init();

		 if(activeTab == '商品上架明细'){
				tableUrl = serverAddr+'/sale/listSaleShelf.asyn?r='+Math.random() ;
		  		tableColumns = [
						{data : 'goodsNo'},{data : 'goodsName'},{data : 'barcode'},{data : 'tallyingUnit'},
						{data : 'num'},{data : 'shelfNum'},{data : 'damagedNum'},{data : 'lackNum'},{data : 'shelfDate'},{data : 'poNo'},{data : 'remark'},
						{data : 'operator'},{data : 'createDate'}
		  	        ] ;
		  		tableId = "datatable-batchs" ;
			}
		
	    //配置表格参数
	    $mytable.params={
	        url:tableUrl,
	        columns:tableColumns,
	        formid:'#form1'
	    };
	    
		//每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
        	 if (aData.tallyingUnit == '00'){
                 $('td:eq(4)', nRow).html('托盘');
             }else if (aData.tallyingUnit == '01'){
                $('td:eq(4)', nRow).html('箱');
            }else if (aData.tallyingUnit == '02'){
                $('td:eq(4)', nRow).html('件');
            }
        };
	    //生成列表信息
	    $('#' + tableId).mydatatables(); 
	}
</script>
