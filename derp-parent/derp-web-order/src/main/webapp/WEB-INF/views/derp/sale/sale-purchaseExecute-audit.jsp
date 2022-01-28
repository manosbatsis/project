<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
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
                       <li class="breadcrumb-item"><a href="#">销售订单-采购执行审核</a></li>
                    </ol>
                    </div>
                   </div>
                    <!--  title   end  -->               
                          <!--  title   基本信息   start -->
                    <div class="title_text">基本信息</div>
                    <div class="info_box">
                        <div class="info_item">
                            <div class="info_text">
                            <input type="hidden" name='orderId' id='orderId' value='${detail.id}'/>
                                <span>销售订单编号：</span>
                                <span>
                                    ${detail.code}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>出仓仓库：</span>
                                <span>
                                    ${detail.outDepotName}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>订单状态：</span>
                                <span>
                                  ${detail.stateLabel}
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>创建人：</span>
                                <span>
                                    ${detail.createName}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>入仓仓库：</span>
                                <span>
                                    ${detail.inDepotName}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>参照订单：</span>
                                <span>
                                    ${detail.referToOrder}
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>创建时间：</span>
                                <span>
                                   <fmt:formatDate value="${detail.createDate}" pattern="yyyy-MM-dd HH:mm:ss" />
                                </span>
                            </div>
                            <div class="info_text">
                                <span>LBX单号：</span>
                                <span>
                                    ${detail.lbxNo}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>客户：</span>
                                <span>
                                    ${detail.customerName}
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>PO单号：</span>
                                <span>
                                    ${detail.poNo}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>PO单时间：</span>
                                <span>
                                    <fmt:formatDate value="${detail.poDate}" pattern="yyyy-MM-dd HH:mm:ss" />
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
                                <span>销售币种：</span>
                                <span>
                                    ${detail.currencyLabel}
                                </span>
                            </div>
                             <div class="info_text">
                              <span>交货日期：</span>
                                <span>
                                    <fmt:formatDate value="${detail.deliveryDate}" pattern="yyyy-MM-dd" />
                                </span>
                            </div>
                            <div class="info_text">
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
                            	<c:if test="${not empty detail.tallyingUnit}">
	                                <span>海外仓理货单位：</span>
	                                <span>
	                                    <c:if test="${detail.tallyingUnit == '00'}">托盘</c:if>
			                        	<c:if test="${detail.tallyingUnit == '01'}">箱</c:if>
			                        	<c:if test="${detail.tallyingUnit == '02'}">件</c:if>
	                                </span>
                                </c:if>
                            </div>
                            <div class="info_text">
                            	<c:if test="${not empty detail.destination}">
	                                <span>目的地：</span>
	                                <span>
	                                    <c:if test="${detail.destination == 'GZJC01'}">广州机场</c:if>
			                        	<c:if test="${detail.destination == 'HP01'}">黄埔状元谷</c:if>
			                        	<c:if test="${detail.destination == 'HK01'}">香港本地</c:if>
	                                </span>
                                </c:if>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>备注：</span>
                                <span>
                                    ${detail.remark}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>签收时间：</span>
                                <span>
                               <fmt:formatDate value="${saleOutModel.receiveDate}" pattern="yyyy-MM-dd" />
                               </span>
                            </div>
                            <div class="info_text">
                                <span>最后上架时间：</span>
                                <span><fmt:formatDate value="${listShelfList.shelfDate}" pattern="yyyy-MM-dd" /></span>
                            </div>
                        </div>
                        <div class="info_item">
                        	<div class="info_text">
                                <span>上架备注：</span>
                                <span>${listShelfList.remark}</span>
                            </div>
                            <div class="info_text">
                            	<span>归属月份：</span>
                                <span>${detail.ownMonth}</span>
                            </div>
                            <div class="info_text">
                            	<span>原销出仓仓库：</span>
                                <span>${detail.originalOutDepotName}</span>
                            </div>
                        </div>
                    </div>
                    
                    <div class="title_text"> 收件信息</div>
                    <c:if test="${not empty detail.mailMode}">
                    <div class="info_item">
                           <div class="info_text">
                               <span>提货方式：</span>
                               <span>
                                   <c:if test="${detail.mailMode == '1'}">邮寄</c:if>
			                       <c:if test="${detail.mailMode == '2'}">自提</c:if>
                               </span>
                           </div>                            
                           <div class="info_text">
                               <span>收件人姓名：</span>
                               <span>${detail.receiverName}</span>
                           </div>
                    </div>
                    <div class="info_item">
                           <div class="info_text">
                               <span>国家：</span>
                               <span>${detail.country}</span>
                           </div>                            
                           <div class="info_text">
                               <span>详细地址：</span>
                               <span>${detail.receiverAddress}</span>
                           </div>
                    </div>
                   </c:if>                                  
                    
                 <!--  title   基本信息  start -->
                   <!--   商品信息  start -->
                    <div class="col-12 ml10">
                        <div class="row">
                         <div class="col-10">
                             <div class="title_text">商品信息</div>
                        </div>
                        </div>
                    </div>                       
                    <div class="form-row mt20" style="padding-left: 15px;padding-right: 15px;">
                   <table id="table-list" class="table table-striped">
                       <thead>
                            <tr>
                           <th>序号</th>
                           <th>商品编号</th>
                           <th>商品名称</th>
                           <th>商品货号</th>
<!--                            <th>海外仓理货单位</th> -->
                           <th>条码</th>
                           <th>销售数量</th>
                           <th>销售单价</th>
                           <th>销售总金额</th>
                           <th>品牌类型</th>
                           <th>箱号</th>
                           <th>合同号</th> 
                           <th>备注</th>
                       </tr>
                       </thead>
                        <tbody>
                            <c:forEach items="${detail.itemList}" var="item" varStatus="status">
                            <tr>
                                <td>${status.index+1}</td>
                                <td>${item.goodsCode}</td>
                                <td>${item.goodsName}</td>
                                <td>${item.goodsNo}</td>
<!--                                 <td> -->
<%--                                 	<c:choose> --%>
<%--                                  		<c:when test="${item.tallyingUnit eq '00' }">托盘</c:when> --%>
<%--                                  		<c:when test="${item.tallyingUnit eq '01' }">箱</c:when> --%>
<%--                                  		<c:when test="${item.tallyingUnit eq '02' }">件</c:when> --%>
<%--                                  	</c:choose> --%>
<!--                                 </td> -->
                                <td>${item.barcode}</td>
                                <td>${item.num}</td>
                                <td>${item.price}</td>
                                <td name='amount'>${item.amount}</td>
                                <td>${item.brandName}</td>
                                <td>${item.boxNo}</td>
                                <td>${item.contractNo}</td>
                                <td>${item.remark}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                  </table>
                   </div>
                 <!--   商品信息  end -->
                      <div class="form-row m-t-50">
                          <div class="col-12">
                              <div class="row">
                                  <div class="col-5">
                                        <shiro:hasPermission name="sale_addSaleOrder">
                                      		<input type="button" class="btn btn-info waves-effect waves-light btn_default" id="save-buttons" value="保存并审核"/>
                                 	  	</shiro:hasPermission>
                                  </div>
                                  <div class="col-5">
                                      <button type="button" class="btn btn-info waves-effect waves-light btn_default" id="cancel-buttons">返回</button>
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
			$module.load.pageOrder('act=4001');
		});
		
		//初始化
		$mytable.fn.init();
		//配置表格参数
		$mytable.params = {
			url : serverAddr+'/sale/listSaleShelf.asyn?r=' + Math.random(),
			columns : [
					{  //序号
		            data : null,
		            bSortable : false,
		            className : "",
		            render : function(data, type, row, meta) {
		                // 显示行号
		                var startIndex = meta.settings._iDisplayStart;
		                return startIndex + meta.row + 1;
		               }
		            },
					{data : 'goodsNo'},{data : 'goodsName'},{data : 'barcode'},{data : 'tallyingUnit'},
					{data : 'num'},{data : 'shelfNum'},{data : 'damagedNum'},{data : 'lackNum'},{data : 'shelfDate'},{data : 'poNo'},{data : 'remark'},
					{data : 'operator'},{data : 'createDate'}],
			formid : '#form1'
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
		$('#table-shelf-list').mydatatables();
		
		//提交并审核存按钮
		$("#save-buttons").click(function(){
	 	    $custom.alert.warning("确定审核该销售订单？",function(){
			var url = serverAddr+"/sale/modifyPurchaseExecute.asyn";
			var orderId = $("#orderId").val();
 			// 审核操作
			$custom.request.ajax(url, {"id":orderId}, function(data){
				if(data.state==200){
					var str = data.data;
					$custom.alert.success(str);
					//重新加载页面
					setTimeout(function () {
						$load.a(pageUrl+"4001");
					}, 1000);
				}else{
					if(!!data.expMessage){
						$custom.alert.error(data.expMessage);
					}else{
						$custom.alert.error(data.message);
					}
				}
			});
	 	});
		});
 }); 
</script>
