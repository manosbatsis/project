<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/views/common.jsp" />
<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
          <form id="add-form" action="/transfer/saveTransfer.asyn">
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
                       <li class="breadcrumb-item"><a href="#">调拨管理</a></li>
                       <li class="breadcrumb-item"><a href="javascript:$module.load.pageOrder('act=8001');">调拨订单列表</a></li>
                       <li class="breadcrumb-item"><a href="#">详情</a></li>
                    </ol>
                    </div>
                   </div>
                    <!--  title   end  -->               
                          <!--  title   基本信息   start -->
                <div class="title_text">基本信息</div>
                
                    <div class="info_box">
                        <div class="info_item">
                            <div class="info_text">
                                <span>调拨订单编号：</span>
                                <span>
                                    ${transferOrder.code}
							  </span>
                            </div>
                            <div class="info_text">
                                <span>单据状态：</span>
                                <span>${transferOrder.statusLabel}
                                    <%--<c:choose>--%>
                                        <%--<c:when test="${transferOrder.status==013}">待提交</c:when>--%>
                                        <%--<c:when test="${transferOrder.status==014}">已提交</c:when>--%>
                                        <%--<c:when test="${transferOrder.status==023}">调拨已出库</c:when>--%>
                                        <%--<c:when test="${transferOrder.status==024}">调拨已入库</c:when>--%>
                                        <%--<c:when test="${transferOrder.status==007}">已完结</c:when>--%>
                                        <%--<c:when test="${transferOrder.status==006}">已删除</c:when>--%>
                                        <%--<c:otherwise>${transferOrder.status}</c:otherwise>--%>
                                    <%--</c:choose>--%>
                                </span>
                            </div>
                            <div class="info_text">
                                <span>事业部：</span>
                                <span>${transferOrder.buName}
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>调出仓库：</span>
                                <span>
                                    ${transferOrder.outDepotName}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>调入仓库：</span>
                                <span>
                                    ${transferOrder.inDepotName}
                                </span>
                            </div>
                           <div class="info_text">
                                <span>合同号：</span>
                                <span>
                                    ${transferOrder.contractNo}
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                             <div class="info_text">
                                <span>调出公司：</span>
                                <span>
                                    ${transferOrder.merchantName}
                                </span>
                            </div>
                             <div class="info_text">
                                <span>调入客户：</span>
                                <span>
                                    ${transferOrder.inCustomerName }
                                </span>
                            </div>
                            <div class="info_text">
                                <span>LBX单号：</span>
                                <span>
                                    ${transferOrder.lbxNo}
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                             <div class="info_text">
                                <span>发票号：</span>
                                <span>
                                    ${transferOrder.invoiceNo }
                                </span>
                            </div>
                            <div class="info_text">
                                <span>包装方式：</span>
                                <span>${transferOrder.packType}</span>
                            </div>
                            <div class="info_text">
                                <span>箱数：</span>
                                <span>
                                    ${transferOrder.cartons }
                                </span>
                            </div>
             
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>运输方式：</span>
                                <span>
                                    ${transferOrder.transportLabel }
                                </span>
                            </div>
                            <div class="info_text">
                                <span>标准箱TEU：</span>
                                <span>${transferOrder.standardCaseTeu}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>车次：</span>
                                <span>
                                    ${transferOrder.trainNumber }
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>承运商：</span>
                                <span>
                                    ${transferOrder.carrier }
                                </span>
                            </div>
                            <div class="info_text">
                                <span>托数：</span>
                                <span>${transferOrder.torrNum}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>出库地点：</span>
                                <span>
                                    ${transferOrder.outdepotAddr }
                                </span>
                            </div>
                        </div>
                         <div class="info_item">
                             <div class="info_text">
                                <span>委托单位：</span>
                                <span>
                                    ${transferOrder.merchantName}
                                </span>
                            </div>
                             <div class="info_text">
                                <span>头程提单号：</span>
                                <span>
                                    ${transferOrder.firstLadingBillNo }
                                </span>
                            </div>
                            <div class="info_text">
                                <span>海外仓理货单位：</span>
                                <span>${transferOrder.tallyingUnitLabel}
                                     <%--<c:choose>--%>
                                        <%--<c:when test="${transferOrder.tallyingUnit=='00'}">托盘</c:when>--%>
                                        <%--<c:when test="${transferOrder.tallyingUnit=='01'}">箱</c:when>--%>
                                        <%--<c:when test="${transferOrder.tallyingUnit=='02'}">件</c:when>--%>
                                        <%--<c:otherwise>${transferOrder.tallyingUnit}</c:otherwise>--%>
                                     <%--</c:choose>--%>
                                </span>
                            </div>
                         </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>申报海关：</span>
                                <span>
                                    ${outDepot.customsNo}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>创建人：</span>
                                <span>
                                    ${transferOrder.createUsername}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>创建时间：</span>
                                <span>
                                    <fmt:formatDate value="${transferOrder.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </span>
                            </div>
                           
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>申报地国检：</span>
                                <span>
                                    ${outDepot.inspectNo}
                                </span> 
                            </div>
                             <div class="info_text">
                                <span>提交人：</span>
                                <span>
                                    ${transferOrder.submitUsername}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>提交时间：</span>
                                <span>
                                    <fmt:formatDate value="${transferOrder.submitTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </span>
                            </div>

                        </div>
                        
                        <div class="info_item">
                            <div class="info_text">
                                <span>服务类型：</span>
                                <span>${transferOrder.serveTypesLabel}
                                   <%--<c:choose>--%>
                                        <%--<c:when test="${transferOrder.serveTypes=='G0'}">库内调拨服务</c:when>--%>
                                        <%--<c:when test="${transferOrder.serveTypes=='E0'}">区内调拨调出服务</c:when>--%>
                                        <%--<c:otherwise>${transferOrder.serveTypes}</c:otherwise>--%>
                                   <%--</c:choose>--%>
                                </span>
                            </div>
                            <div class="info_text">
                                <span>业务场景：</span>
                                <span>${transferOrder.modelLabel}
                                     <%--<c:choose>--%>
                                            <%--<c:when test="${transferOrder.model=='10'}">账册内调仓</c:when>--%>
                                            <%--<c:when test="${transferOrder.model=='40'}">账册内货权转移</c:when>--%>
                                            <%--<c:when test="${transferOrder.model=='50'}">账册内货权转移调仓</c:when>--%>
                                            <%--<c:otherwise>${transferOrder.model}</c:otherwise>--%>
                                     <%--</c:choose>--%>
                                </span>
                            </div>
                             <div class="info_text">
                                <span>境外发货人：</span>
                                <span>
                                    ${transferOrder.shipper}
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                             <div class="info_text">
                                <span>唛头：</span>
                                <span>
                                   ${transferOrder.mark}
                                </span>
                            </div>
                             <div class="info_text">
                                <span>收货地址：</span>
                                <span>
                                    ${transferOrder.receivingAddress }
                                </span>
                            </div>
                            <div class="info_text">
                             <span>PO号：</span>
                                 <span> 
                                    ${transferOrder.poNo }
                                 </span>
                            </div>
                        </div>
                         <div class="info_item">
                             <div class="info_text">
                                <span>目的地：</span>
                                <span>${transferOrder.destinationLabel }
                                    <%--<c:choose>--%>
                                        <%--<c:when test="${transferOrder.destination=='HP01'}">黄埔状元谷</c:when>--%>
                                        <%--<c:when test="${transferOrder.destination=='HK01'}">香港本地</c:when>--%>
                                        <%--<c:when test="${transferOrder.destination=='GZJC01'}">广州机场</c:when>--%>
                                        <%--<c:otherwise>${transferOrder.destination}</c:otherwise>--%>
                                    <%--</c:choose>--%>
                                </span>
                            </div>
                            <div class="info_text">
                                <span>收货人姓名：</span>
                                <span>
                                ${transferOrder.consigneeUsername}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>国家：</span>
                                <span>
                                   ${transferOrder.country}
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>付款条约：</span>
                                <span>
                                    ${transferOrder.payRules}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>托盘材质：</span>
                                <span>
                                    ${transferOrder.palletMaterialLabel}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>装货港：</span>
                                <span>${transferOrder.portLoading}
                                </span>
                            </div>

                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>出库关区：</span>
                                <span>
                                    ${transferOrder.outPlatformCustoms}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>入库关区：</span>
                                <span>
                                    ${transferOrder.inPlatformCustoms}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>卸货港：</span>
                                <span>${transferOrder.unloadPort}
                                </span>
                            </div>

                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>提单毛重：</span>
                                <span>
                                    ${transferOrder.billWeight}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>备注：</span>
                                <span>
                                    ${transferOrder.remark}
                                </span>
                            </div>
                            <div class="info_text">
	                            <span>是否同关区：</span>
	                             <span>${transferOrder.isSameAreaLabel}
	                             </span>
                            </div>

                        </div>

                          
                        
                    </div>

                 <!--  title   基本信息  start -->
                   <!--   商品信息  start -->
                    <div class="title_text">商品信息</div>
                <input type="hidden" id="customerId" value="${customerId}">
                <div class="mt20 table-responsive">
                    <table id="datatable-buttons" class="table table-striped" cellspacing="0" style="width: 1800px">
                        <thead>
                        <tr>
                            <th>序号</th>
                            <th>调出商品编号</th>
                            <th>调出商品货号</th>
                            <th>调出商品名称</th>
                            <th>调出单位</th>
                            <th>调出数量</th>
                            <th>调入商品编号</th>
                            <th>调入商品货号</th>
                            <th>调入商品名称</th>
                            <th>调入单位</th>
                            <th>调入数量</th>
                            <th>&nbsp;品牌类型&nbsp;</th>
                            <th>调拨单价</th>
                            <th>毛重(kg)</th>
                            <th>净重(kg)</th>
                            <th>箱&nbsp;号</th>
                            <th>合同号</th>
                            <th>条形码</th>
                            <th>托盘号</th>
                            <th>箱数</th>
                        </tr>
                        </thead>
                        <tbody id="item-table">
                           <c:forEach items="${orderItem }" var="item" varStatus="itemStatus">
	                           <tr>
	                               <td>${item.seq }</td>
			                       <td>${item.outGoodsCode}</td>
		                           <td>${item.outGoodsNo}</td>
			                       <td>${item.outGoodsName}</td>
			                       <td>${item.outUnitLabel}
			                            <%--<c:choose>--%>
	                                        <%--<c:when test="${item.outUnit=='00'}">托盘</c:when>--%>
	                                        <%--<c:when test="${item.outUnit=='01'}">箱</c:when>--%>
	                                        <%--<c:when test="${item.outUnit=='02'}">件</c:when>--%>
	                                        <%--<c:when test="${item.outUnit=='03'}">KG</c:when>--%>
	                                        <%--<c:otherwise>${item.outUnit}</c:otherwise>--%>
	                                    <%--</c:choose>--%>
			                       </td>
			                       <td>${item.transferNum}<input type="hidden" name="transferNum" value="${item.transferNum}"></td>
			                       <td>${item.inGoodsCode}</td>
		                           <td>${item.inGoodsNo}</td>
			                       <td>${item.inGoodsName}</td>
			                       <td>${item.inUnitLabel}
			                         <%--<c:choose>--%>
                                        <%--<c:when test="${item.inUnit=='00'}">托盘</c:when>--%>
                                        <%--<c:when test="${item.inUnit=='01'}">箱</c:when>--%>
                                        <%--<c:when test="${item.inUnit=='02'}">件</c:when>--%>
                                        <%--<c:when test="${item.inUnit=='03'}">KG</c:when>--%>
                                        <%--<c:otherwise>${item.inUnit}</c:otherwise>--%>
	                                 <%--</c:choose>--%>
	                               </td>
			                       <td>${item.inTransferNum}<input type="hidden" name="inTransferNum" value="${item.inTransferNum}"></td>
			                       <td>${item.brandName}</td>
								   <td>${item.price}</td>
								   <td>${item.grossWeightSum}<input type="hidden" name="grossWeight" value="${item.grossWeightSum}"></td>
								   <td>${item.netWeightSum}<input type="hidden" name="netWeight" value="${item.netWeightSum}"></td>
								   <td>${item.contNo}</td>
								   <td>${item.bargainno}</td>
								   <td>${item.outBarcode}</td>
								   <td>${item.torrNo}</td>
								   <td>${item.boxNum}</td>
			                   </tr>
	                      </c:forEach>
                        	
                        </tbody>
                    </table>
                </div>
                 <!--   商品信息  end -->
                    <div class="form-row mt20">
                        <div class="col-2"></div>
                        <div class="col-2">
                            <span>调出总数量：</span>
                            <span id="transferOutTotalNum"></span>
                        </div>
                        <div class="col-2">
                            <span>调入总数量：</span>
                            <span id="transferInTotalNum"></span>
                        </div>
                        <div class="col-2">
                            <span>总毛重：</span>
                            <span id="totalGrossWeight"></span>
                        </div>
                        <div class="col-2">
                            <span>总净重：</span>
                            <span id="totalNetWeight"></span>
                        </div>
                    </div>
                    <div class="form-row m-t-50">
                        <div class="col-12">
                            <div class="row">
                                <div class="col-5">
                                    <a class="btn btn-find waves-effect waves-light btn_default" href="javascript:void(0);" onclick="returnBack();">返回</a>
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

<script>
    $(function () {
        sumTotal();
    });
    function returnBack() {
        var pageSource = '${pageSource}';
        if (pageSource == "1") {
            $load.a("/list/menu.asyn?act=100&r="+Math.random());
        } else {
            $module.load.pageOrder('act=8001');
        }
    }

    /**
     *	汇总调出数量、调入数量
     */
    function sumTotal() {
        var transferInTotalNum = 0 ;
        $("#item-table").find("input[name='inTransferNum']").each(function(i, m){
            let val = $(m).val() ;

            if(val && isNumber(val)){
                val = parseInt(val);
                transferInTotalNum = parseInt(transferInTotalNum) ;
                transferInTotalNum += val ;
                $("#transferInTotalNum").html(transferInTotalNum) ;
            }
        }) ;


        var transferOutTotalNum = 0.0 ;
        $("#item-table").find("input[name='transferNum']").each(function(i, m){
            let val = $(m).val() ;

            if(val && isNumber(val)){
                val = parseInt(val);
                transferOutTotalNum = parseInt(transferOutTotalNum) ;
                transferOutTotalNum += val ;
                $("#transferOutTotalNum").html(transferOutTotalNum) ;
            }

        }) ;

        var totalNetWeight = 0.0 ;
        $("#item-table").find("input[name='netWeight']").each(function(i, m){
            let val = $(m).val() ;

            if(val && isNumber(val)){
                val = parseFloat(val);
                totalNetWeight = parseFloat(totalNetWeight) ;
                totalNetWeight += val ;
                totalNetWeight = parseFloat(totalNetWeight).toFixed(3);

                $("#totalNetWeight").html(totalNetWeight) ;
            }

        }) ;

        var totalGrossWeight = 0.0 ;
        $("#item-table").find("input[name='grossWeight']").each(function(i, m){
            let val = $(m).val() ;

            if(val && isNumber(val)){
                val = parseFloat(val);
                totalGrossWeight = parseFloat(totalGrossWeight) ;
                totalGrossWeight += val ;
                totalGrossWeight = parseFloat(totalGrossWeight).toFixed(3);

                $("#totalGrossWeight").html(totalGrossWeight) ;
            }

        }) ;
    }

    /**判断是否数字*/
    function isNumber(val){
        if(val === "" || val ==null){
            return false;
        }
        if(!isNaN(val)){
            //对于空数组和只有一个数值成员的数组或全是数字组成的字符串，isNaN返回false，例如：'123'、[]、[2]、['123'],isNaN返回false,
            //所以如果不需要val包含这些特殊情况，则这个判断改写为if(!isNaN(val) && typeof val === 'number' )
            return true;
        }else{
            return false;
        }

    }
</script>
