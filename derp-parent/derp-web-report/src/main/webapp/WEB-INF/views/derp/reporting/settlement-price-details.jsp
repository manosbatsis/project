<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common.jsp" />
<style type="text/css">
	.not-arrow{
		padding: 5px 10px;
		border:1px solid #dcd8d8;
		-webkit-appearance:none;
		-moz-appearance:none;
		appearance:none; /*去掉下拉箭头*/
	}
	.not-arrow::-ms-expand { display: none; }
</style>
<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
            <div class="row">
                <div class="col-12">
                    <div class="card-box">
                        <!--  title   start  -->
                        <div class="col-12">
                            <div>
                                <div class="col-12 row">
                                    <div>
                                        <ol class="breadcrumb">
                                            <li><i class="block"></i> 当前位置：</li>
							                      <li class="breadcrumb-item"><a href="#">标准成本单价管理</a></li>
							                      <li class="breadcrumb-item"><a href="javascript:$module.load.pageReport('act=13001');">标准成本单价列表</a></li>
							                      <li class="breadcrumb-item"><a href="javascript:$module.load.pageReport('act=13004&id=' + ${detail.id});">标准成本单价详情</a></li>
							            </ol>
                                    </div>
                                </div>
                                <form id="form1">
        	                       <input type="hidden" name='settlementPriceId' id='settlementPriceId' value='${detail.id}'/>
                                </form>
                                <div class="title_text">事业部</div>
                                <div class="info_box">
                                	<div class="info_item">
			                            <div class="info_text">
											<span>公司：</span>
											<span>${detail.merchantName}</span>
	                                    </div>
	                                    <div class="info_text">
	                                    	<span>事业部：</span>
											<span>${detail.buName}</span>
	                                    </div>
	                                    <div class="info_text">
	                                    </div>
                                	</div>
                                </div>
                                <div class="title_text">商品详情</div>
                                <div class="info_box">
			                        <div class="info_item">
			                            <div class="info_text">
											<span>商品名称：</span>
											<span>${detail.goodsName}</span>
	                                    </div>
	                                    <div class="info_text">
	                                    	<span>条形码：</span>
											<span>${detail.barcode}</span>
	                                    </div>
	                                    <div class="info_text">
	                                    	<span>品牌：</span>
											<span>${detail.brandName}</span>
	                                    </div>
                                	</div>
                                	<div class="info_item">
			                            <div class="info_text">
			                            	<span>规格型号：</span>
			                            	<span>${detail.goodsSpec}</span>
			                            </div>
			                            <div class="info_text">
			                            	<span>品类：</span>
			                            	<span>${detail.productTypeName}</span>
			                            </div>
			                            <div class="info_text">
				                            <span>原产国：</span>
				                            <span>${detail.countyName}</span>
			                            </div>
                                    </div>
                                    <div class="info_item">
                                    	<div class="info_text">
				                            <span>计量单位：</span>
				                            <span>${detail.unitName}</span>
			                            </div>
			                            <div class="info_text">
				                            <span>是否组合：</span>
				                            <span>
				                            	<c:if test="${detail.isGroup eq '1' }">是</c:if>
				                            	<c:if test="${detail.isGroup eq '0' }">否</c:if>
				                            </span>
			                            </div>
			                            <div class="info_text">
			                            </div>
                                	</div>
                                </div>
                                <div>
    <!-- Signup modal content -->
               	<c:if test="${detail.isGroup eq '0' }">否</c:if>
               	<c:if test="${detail.isGroup eq '1' }">
                 <div class="col-12 ml10">
				                    <div class="row">
				                         <div class="col-10">
				                             <div class="title_text">组合品详情</div>
				                        </div>
				                    </div>
				                 </div>                       
				                 <div class="form-row mt20" style="padding-left: 15px;padding-right: 15px;">
                                    <table id="table-list" class="table table-striped" cellspacing="0"  width="100%">
                                        <thead>
	                                        <tr>
					                            <th>商品条码</th>
					                            <th>商品货号</th>
					                            <th>商品名称</th>
					                            <th>商品编码</th>
					                            <th>单品数量</th>
					                            <th>商品价格</th>
				                      		</tr>
			                      		</thead>
			                      		<tbody>
			                      			<c:forEach items="${groupList}" var="item"  varStatus="status">
				                      			<tr>
					                      			<td>${item.barcode}</td>
					                      			<td>${item.goodsNo}</td>
					                      			<td>${item.name}</td>
					                      			<td>${item.goodsCode}</td>
					                      			<td>${item.groupNum}</td>
					                      			<td>${item.groupPrice}</td>
						                        </tr>  
			                      			</c:forEach>
			                         	</tbody>
                                    </table>    
                                </div>
               	
               	
               	</c:if>

				                <div class="col-12 ml10">
				                    <div class="row">
				                         <div class="col-10">
				                             <div class="title_text">标准成本单价记录</div>
				                        </div>
				                    </div>
				                 </div>                       
				                 <div class="form-row mt20" style="padding-left: 15px;padding-right: 15px;">
                                    <table id="table-list" class="table table-striped" cellspacing="0"  width="100%">
                                        <thead>
	                                        <tr>
					                           <th>序号</th>
					                           <th>生效年月</th>
					                           <th>标准成本单价</th>
					                           <th>结算币种</th>
					                           <th>创建日期</th>
					                           <th>修改日期</th>
				                      		</tr>
			                      		</thead>
			                      		<tbody>
			                      			<c:forEach items="${itemList}" var="item"  varStatus="status">
				                      			<tr>
					                      			<td>${status.index+1}</td>
					                      			<td>${item.effectiveDate}</td>
					                      			<td>${item.price}</td>
					                      			<td>
					                      				${item.currency }
													</td>
					                      			<td><fmt:formatDate value="${item.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					                      			<td><fmt:formatDate value="${item.modifyDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						                        </tr>
			                      			</c:forEach>
			                         	</tbody>
                                    </table>
                                </div>
                                <!--   商品信息  end -->
                                <!--   价格变更记录    -->
                                <div class="col-12 ml10">
				                    <div class="row">
				                         <div class="col-10">
				                             <div class="title_text">修改历史记录</div>
				                        </div>
				                    </div>
				                </div>   
				                <div class="form-row mt20">
                                    <table id="table-record-list" class="table table-striped" cellspacing="0"  width="100%">
                                        <thead>
	                                        <tr>
	                                           <th>序号</th>
					                           <th>生效年月</th>
					                           <th>标准成本单价</th>
					                           <th>结算币种</th>
					                           <th>修改时间</th>
					                           <th>修改人员</th>
					                           <th>调价原因</th>
					                           <th>审核时间</th>
                                               <th>审核人员</th>
                                               <th>状态</th>
				                      		</tr>
			                      		</thead>
			                      		<tbody>
			                         	</tbody>
                                    </table>
                                </div>  
				                <!--   价格变更记录  end   -->
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
                    <!-- end row -->
                </div>
            </div>
    </div>
    <!-- container -->
</div>

<script type="text/javascript">
	$(function(){
		//取消按钮
		$("#cancel-buttons").click(function(){
			$module.load.pageReport("act=13001");
		});
		//初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params = {
            url: serverAddr+'/settlementPrice/listRecordPrice.asyn?r=' + Math.random(),
            columns: [{  //序号
                data : null,
                bSortable : false,
                className : "",
                render : function(data, type, row, meta) {
                    // 显示行号
                    var startIndex = meta.settings._iDisplayStart;
                    return startIndex + meta.row + 1;
                   }
                },
            {data: 'effectiveDate'}, {data: 'price'}, {data: 'currencyLabel'}, 
            {data: 'modifyDate'},{data:'modifier'},{data: 'adjustPriceResult'},
            {data: 'examineDate'},{data: 'examiner'},
            {data: 'statusLabel'}
            ],
            formid: '#form1'
        };
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
            
        };
        //生成列表信息
        $('#table-record-list').mydatatables();
	});
</script>
