<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<jsp:include page="/WEB-INF/views/common.jsp" />
<!-- Start content -->
<style>
	#step {
		overflow: hidden;
	}
	.ui-step-item-title {
        height: 42px;
    }

    .ui-step-wrap .ui-step .ui-step-item .ui-step-item-num {
        margin-top: 0;
    }
	table {
	    border-collapse: collapse;
	    border-spacing: 0;
	}
	 
	td,th {
	    padding: 0;
	}
	 
	.pure-table {
	    border-collapse: collapse;
	    border-spacing: 0;
	    empty-cells: show;
	    border: 1px solid #cbcbcb;
	    width: 95% ;
	    margin-top:25px ;
	    text-align: center;
	}
	 
	.pure-table caption {
	    color: #000;
	    font: italic 85%/1 arial,sans-serif;
	    padding: 1em 0;
	    text-align: center;
	}
	 
	.pure-table td,.pure-table th {
	    border-left: 1px solid #cbcbcb;
	    border-width: 0 0 0 1px;
	    font-size: inherit;
	    margin: 0;
	    overflow: visible;
	    padding: .5em 1em;
	}
	 
	.pure-table thead {
	    background-color: #e0e0e0;
	    color: #000;
	    text-align: center;
	    vertical-align: bottom;
	}
	 
	.pure-table td {
	    background-color: transparent;
	}
	 
	.pure-table-bordered td {
	    border-bottom: 1px solid #cbcbcb;
	}
	 
	.pure-table-bordered tbody>tr:last-child>td {
	    border-bottom-width: 0;
	}
</style>
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
                       <li class="breadcrumb-item"><a href="#">采购档案</a></li>
                       <li class="breadcrumb-item"><a href="#">采购订单列表</a></li>
                       <li class="breadcrumb-item"><a href="#">采购链路步骤三</a></li>
                    </ol>
                    </div>
                   </div>
                    <!--  title   end  -->     
                    <div id="step"></div>          
                          <!--  title   基本信息   start -->
                    <form id="add-form">
                    	<input type="hidden" id="purchaseTradeLinkId" value="${id }">
                    	<table class="pure-table pure-table-bordered">
               			<thead>
               				<tr>
               					<td>公司</td>
               					<td>单据类型</td>
               					<td>生成状态</td>
               					<td>单据编号</td>
               					<td>单据状态</td>
               				</tr>
               			</thead>
                       	<tbody>
                       		<c:forEach items="${map}" var="item" varStatus="index">
								<c:if test="${ item.key != 'errorList'}">
									<c:forEach items="${item.value['purchaseList']}" var="order" >
	                       			<tr>
	                        			<td>${item.key }</td>
                      					<td>采购订单</td>
                      					<td class="status">待生成</td>
                      					<td class="code"></td>
                      					<td class="state"></td>
	                     			</tr>
	                     			</c:forEach>
									<c:forEach items="${item.value['saleOrderList']}" var="order" >
	                       			<tr>
	                        			<td>${item.key }</td>
                      					<td>
                      						<c:choose>
                      							<c:when test="${order.businessModel == '0' }">预售订单</c:when>
                      							<c:otherwise>销售订单</c:otherwise>
                      						</c:choose>
                      					</td>
                      					<td class="status">待生成</td>
                      					<td class="code"></td>
                      					<td class="state"></td>
	                     			</tr>
	                     			</c:forEach>
	                     			<c:forEach items="${item.value['purchaseContractList']}" var="order" >
	                       			<tr>
	                        			<td>${item.key }</td>
                      					<td>PO 合同</td>
                      					<td class="status">待生成</td>
                      					<td></td>
                      					<td></td>
	                     			</tr>
	                     			</c:forEach>
	                     		</c:if>
                       		</c:forEach>
                       	</tbody>
                       </table>
                       <div>注：生成过程中<font style="color:red">请勿关闭或离开</font>该页面，否则将导致单据不能正常生成</div>
                    </form>
                    <div class="form-row m-t-50">
                        <div class="col-12">
                            <div class="row">
                                <div class="col-4">

                                </div>
                                <div class="col-4">
                                    <input type="button" class="btn btn-light waves-effect waves-light btn_default" id="cancel-buttons" value="取 消" />
                                 	<input type="button" class="btn btn-info waves-effect waves-light btn_default " id="save-buttons" value="开始生成"/>
                                 	<input type="button" class="btn btn-info waves-effect waves-light btn_default " id="list-buttons" value="返回列表"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
              </div>
             </div>
                  <!-- end row -->
                  <!-- 弹窗开始 -->
                  <!-- 弹窗结束 -->
        </div>
        <!-- container -->
    </div>
    <img id="loadingDiv"  src="/resources/assets/images/loading.gif" width="180" height="180" style="display: none;position:absolute; z-index:2; filter:alpha(opacity=60);opacity:0.3;top: 25%;left: 50%"  alt="加载中...">

</div> <!-- content -->
<script type="text/javascript">

	var goodsIds = "${goodsIds}" ;
	var goodsNos = "${goodsNos}" ;
	var goodsNums = "${goodsNums}" ;
	var outDepotId = "${outDepotId}" ;
	
	var clickAbleFlag = true ;
	
	$(function(){
		
		$("#list-buttons").hide() ;
		
		/**根据页面宽度渲染进度条长度*/
		var width = $(".content").width() ;
		width -= 100 ;
		
		if(width % 3 != 0){
			width = Math.floor(width / 3) * 3 ;
		}
		$("#step").width(width) ;
		
		var $step = $("#step");
        $step.step({
            index: 2,
            time: 500,
            title: ["<font style=\"font-size:12px;\">确定交易链路<br>确定交易链路的参与公司及交易数据</font>",  "<font style=\"font-size:12px;\">预览交易信息<br>预览各公司的采购、销售单据，确认价格等信息</font>", "<font style=\"font-size:12px;\">生成单据<br>生成各公司的采购、销售单据</font>"]
        });
		
		//弹框取消按钮
		$("#cancel-buttons").click(function(){
			
			if(!clickAbleFlag){
				$custom.alert.warningText("单据已生成，不能重复点击");
				return ;
			}
			
			var purchaseTradeLinkId = $("#purchaseTradeLinkId").val() ;
			$load.a($module.params.loadOrderPageURL+"act=30018&id=" + purchaseTradeLinkId);
		});
		
		$("#list-buttons").click(function(){
			$module.load.pageOrder("act=3001");
		}) ;
		
		//跳转step 3
		$("#save-buttons").click(function(){
			
			if(!clickAbleFlag){
				$custom.alert.warningText("单据已生成，不能重复点击");
				
				return ;
			}
			
			var purchaseTradeLinkId = $("#purchaseTradeLinkId").val() ;
			
			$("#loadingDiv").show();
			
			var firstUrl = serverAddr + "/purchase/saveFirstOrderStatusAndIdepot.asyn" ;
			$custom.request.ajax(firstUrl, {"purchaseTradeLinkId": purchaseTradeLinkId}, function(result){
				if(result.state == 200){
					
					
					var goodsIdsArr = goodsIds.split(",") ;
					var goodsNosArr = goodsNos.split(",") ;
					var goodsNumsArr = goodsNums.split(",") ;
					var flag = true ;
					
					for(var i = 0 ; i < goodsIdsArr.length; i ++){
						
						var goodsId = goodsIdsArr[i] ;
						var goodsNo = goodsNosArr[i] ;
						var goodsNum = goodsNumsArr[i] ;
						
						//查询可用量
		        	  	var availableNum = null ;
						
						availableNum = $module.inventory.queryAvailability(outDepotId,goodsId);
				       	
						var tempGoodsNo = $module.inventoryLocationMapping.getOriginalGoodsNo(goodsId, goodsNo) ;

						// 判断库存量是否足够
			        	  if(!availableNum || availableNum ==-1){
			        		  flag = false;
			        		  
			        		  if(tempGoodsNo){
			        			  $custom.alert.warningText("原货号：" + tempGoodsNo + " 商品货号："+goodsNo+",未查到库存可用量");
			        		  }else{
				        		  $custom.alert.warningText("商品货号："+goodsNo+",未查到库存可用量");
			        		  }
			        		  
						      break ;
			        	  }else if(parseInt(goodsNum) > parseInt(availableNum)){
			        		  flag = false;
									        		  
			        		  if(tempGoodsNo){
								  $custom.alert.warningText("库存不足，"+"原货号：" + tempGoodsNo +" 商品货号："+goodsNo+",可用量："+availableNum);
			        		  }else{
								  $custom.alert.warningText("库存不足，商品货号："+goodsNo+",可用量："+availableNum);
			        		  }
			        		  break ;
						  }
					}
					
					if(!flag){
						
						$("#loadingDiv").hide();
						
						return;
					}
					
					var url = serverAddr + "/purchase/saveLinkOrderAndDepot.asyn" ;
					$custom.request.ajax(url, {"purchaseTradeLinkId": purchaseTradeLinkId}, function(result){
						
						$("#loadingDiv").hide();
						
						if(result.state == 200){
							clickAbleFlag = false ;
							
							$("#cancel-buttons").hide() ;
							$("#save-buttons").hide() ;
							
							var codes = $(result.data) ;
							
							$(codes).each(function(index, str){
								
								var arr = str.split(";") ;
								
								var code = arr[0] ;
								var state = arr[1] ;
								
								$(".code").eq(index).html(code) ;
								$(".code").eq(index).next().html(state) ;
							}) ;
							
							$(".status").html("已生成") ;
							
							$("#list-buttons").show() ;
							
						}else{
							if(result.expMessage != null){
								$custom.alert.error(result.expMessage);
							}else{
								$custom.alert.error(result.message);
							}
						}
					});
					
				}else{
					
					$("#loadingDiv").hide();
					
					if(result.expMessage != null){
						$custom.alert.error(result.expMessage);
					}else{
						$custom.alert.error(result.message);
					}
				}
			}) ;
			
			
		}) ;
	
	}) ;
	
</script>
