<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common.jsp"/>
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
                                        <li class="breadcrumb-item"><a href="#">销售退货订单详细</a></li>
                                    </ol>
                                </div>
                            </div>
                            <!--  title   end  -->
                            <!--  title   基本信息   start -->
                            <div class="title_text">基本信息</div>
                        <%--    <div class="form-row mt20 ml15">
                                <div class="col-4">
                                    <div class="row">
                                        <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                                class="fr">退运单号<span>：</span></span></label>
                                        <div class="col-8 ml10 line">
                                            <input type="hidden" value="${detail.id}" name="pId" id="pId">
                                            ${detail.code}
                                        </div>
                                    </div>
                                </div>
                                <div class="col-4">
                                    <div class="row">
                                        <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                                class="fr">业务场景<span>：</span></span></label>
                                        <div class="col-5 ml10 line">
                                            <c:if test="${detail.model == '40'}">账册内货权转移</c:if>
                                            <c:if test="${detail.model == '50'}">账册内货权转移调仓</c:if>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-row ml15">
                                <div class="col-4">
                                    <div class="row">
                                        <label class="col-4 col-form-label" style="white-space:nowrap;"><span
                                                class="fr">客户<span>：</span></span></label>
                                        <div class="col-5 ml10 line">
                                            ${detail.customerName}
                                        </div>
                                    </div>
                                </div>
                                <div class="col-4">
                                    <div class="row">
                                        <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                                class="fr">服务类型<span>：</span></span></label>
                                        <div class="col-8 ml10 line">
                                            <c:if test="${detail.serveTypes == 'E0'}">区内调拨调出服务</c:if>
                                            <c:if test="${detail.serveTypes == 'F0'}">区内调拨调入服务</c:if>
                                            <c:if test="${detail.serveTypes == 'G0'}">库内调拨服务</c:if>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-4">
                                    <div class="row">
                                        <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                                class="fr">头程提单号<span>：</span></span></label>
                                        <div class="col-5 ml10 line">
                                            ${detail.ladingBill}
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-row ml15">
                                <div class="col-4">
                                    <div class="row">
                                        <label class="col-4 col-form-label" style="white-space:nowrap;"><span
                                                class="fr">退出仓库<span>：</span></span></label>
                                        <div class="col-5 ml10 line">
                                            ${detail.outDepotName}
                                        </div>
                                    </div>
                                </div>
                                <div class="col-4">
                                    <div class="row">
                                        <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                                class="fr">申报地国检编码<span>：</span></span></label>
                                        <div class="col-5 ml10 line">
                                            ${detail.inspectNo}
                                        </div>
                                    </div>
                                </div>
                                <div class="col-4">
                                    <div class="row">
                                        <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                                class="fr">二程提单号<span>：</span></span></label>
                                        <div class="col-5 ml10 line">
                                            ${detail.blNo}
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-row ml15">
                                <div class="col-4">
                                    <div class="row">
                                        <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                                class="fr">退入仓库 <span>：</span></span></label>
                                        <div class="col-5 ml10 line">
                                            ${detail.inDepotName}
                                        </div>
                                    </div>
                                </div>
                                <div class="col-4">
                                    <div class="row">
                                        <label class="col-4 col-form-label" style="white-space:nowrap;"><span
                                                class="fr">申报地海关编码<span>：</span></span></label>
                                        <div class="col-5 ml10 line">
                                            ${detail.customsNo}
                                        </div>
                                    </div>
                                </div>
                                <div class="col-4">
                                    <div class="row">
                                        <label class="col-4 col-form-label" style="white-space:nowrap;"><span
                                                class="fr">卸货港编码<span>：</span></span></label>
                                        <div class="col-5 ml10 line">
                                            ${detail.portDestNo}
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-row ml15">
                                <div class="col-4">
                                    <div class="row">
                                        <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                                class="fr">企业退运单号<span>：</span></span></label>
                                        <div class="col-5 ml10 line">
                                            ${detail.merchantReturnNo}
                                        </div>
                                    </div>
                                </div>
                                <div class="col-4">
                                    <div class="row">
                                        <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                                class="fr">包装方式<span>：</span></span></label>
                                        <div class="col-5 ml10 line">
                                            ${detail.packType}
                                        </div>
                                    </div>
                                </div>
                                <div class="col-4">
                                    <div class="row">
                                        <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                                class="fr">邮箱地址<span>：</span></span></label>
                                        <div class="col-5 ml10 line">
                                            ${detail.email}
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-row ml15">
                                <div class="col-4">
                                    <div class="row">
                                        <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                                class="fr">合同号<span>：</span></span></label>
                                        <div class="col-5 ml10 line">
                                            ${detail.contractNo}
                                        </div>
                                    </div>
                                </div>
                                <div class="col-4">
                                    <div class="row">
                                        <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                                class="fr">箱数<span>：</span></span></label>
                                        <div class="col-5 ml10 line">
                                            ${detail.boxNum}
                                        </div>
                                    </div>
                                </div>
                                <div class="col-4">
                                    <div class="row">
                                        <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                                class="fr">创建人<span>：</span></span></label>
                                        <div class="col-5 ml10 line">
                                            ${detail.createName}
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-row ml15">
                                <div class="col-4">
                                    <div class="row">
                                        <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                                class="fr">发票号<span>：</span></span></label>
                                        <div class="col-8 ml10 line">
                                            ${detail.invoiceNo}
                                        </div>
                                    </div>
                                </div>
                                <div class="col-4">
                                    <div class="row">
                                        <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                                class="fr">付款条约<span>：</span></span></label>
                                        <div class="col-5 ml10 line">
                                            ${detail.payRules}
                                        </div>
                                    </div>
                                </div>
                                <div class="col-4">
                                    <div class="row">
                                        <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                                class="fr">创建时间<span>：</span></span></label>
                                        <div class="col-8 ml10 line">
                                            <fmt:formatDate value="${detail.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-row ml15">
                                <div class="col-4">
                                    <div class="row">
                                        <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                                class="fr">LBX单号<span>：</span></span></label>
                                        <div class="col-5 ml10 line">
                                            ${detail.lbxNo}
                                        </div>
                                    </div>
                                </div>
                                <div class="col-4">
                                    <div class="row">
                                        <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                                class="fr">提单毛重<span>：</span></span></label>
                                        <div class="col-5 ml10 line">
                                            ${detail.billWeight}
                                        </div>
                                    </div>
                                </div>
                                <div class="col-4">
                                    <div class="row">
                                        <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                                class="fr">提交人<span>：</span></span></label>
                                        <div class="col-5 ml10 line">
                                            ${detail.auditName}
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-row ml15">
                                <div class="col-4">
                                    <div class="row">
                                        <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                                class="fr">收货地址<span>：</span></span></label>
                                        <div class="col-5 ml10 line">
                                            ${detail.deliveryAddr}
                                        </div>
                                    </div>
                                </div>
                                <div class="col-4">
                                    <div class="row">
                                        <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                                class="fr">装货港<span>：</span></span></label>
                                        <div class="col-5 ml10 line">
                                            ${detail.portLoading}
                                        </div>
                                    </div>
                                </div>
                                <div class="col-4">
                                    <div class="row">
                                        <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                                class="fr">提交时间<span>：</span></span></label>
                                        <div class="col-8 ml10 line">
                                            <fmt:formatDate value="${detail.auditDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </div>
                                    </div>
                                </div>
                            </div>--%>
                            <div class="info_box">
                                <div class="info_item">
                                    <div class="info_text">
                                        <span>销售退货订单号：</span>
                                        <span>
                                     <input type="hidden" value="${detail.id}" name="pId" id="pId">
                                            ${detail.code}
                                </span>
                                    </div>
                                    <div class="info_text">
                                        <span>业务场景：</span>
                                        <span>
                                        	${detail.modelLabel}
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>服务类型：</span>
                                        <span>
                                        	${detail.serveTypesLabel}
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
                                        <span>头程提单号：</span>
                                        <span>
                                            ${detail.ladingBill}
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>退出仓库：</span>
                                        <span>
                                            ${detail.outDepotName}
                                        </span>
                                    </div>
                                </div>
                                <div class="info_item">
                                    <div class="info_text">
                                        <span>申报地国检编码：</span>
                                        <span>
                                            ${detail.inspectNo}
                                </span>
                                    </div>
                                    <div class="info_text">
                                        <span>二程提单号：</span>
                                        <span>
                                            ${detail.blNo}
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>退入仓库：</span>
                                        <span>
                                            ${detail.inDepotName}
                                        </span>
                                    </div>
                                </div>
                                <div class="info_item">
                                    <div class="info_text">
                                        <span>申报地海关编码：</span>
                                        <span>
                                            ${detail.customsNo}
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>卸货港编码：</span>
                                        <span>
                                            ${detail.portDestNo}
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>装货港：</span>
                                        <span>
                                            ${detail.portLoading}
                                        </span>
                                    </div>
                                </div>
                                <div class="info_item">
                                    <div class="info_text">
                                        <span>包装方式：</span>
                                        <span>
                                            ${detail.packType}
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>邮箱地址：</span>
                                        <span>
                                            ${detail.email}
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>合同号：</span>
                                        <span>
                                            ${detail.contractNo}
                                        </span>
                                    </div>
                                </div>
                                <div class="info_item">
                                    <div class="info_text">
                                        <span>箱数：</span>
                                        <span>
                                            ${detail.boxNum}
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>创建人：</span>
                                        <span>
                                            ${detail.createName}
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>发票号：</span>
                                        <span>
                                            ${detail.invoiceNo}
                                        </span>
                                    </div>
                                </div>
                                <div class="info_item">
                                    <div class="info_text">
                                        <span>付款条约：</span>
                                        <span>
                                            ${detail.payRules}
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>创建时间：</span>
                                        <span>
                                            <fmt:formatDate value="${detail.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>LBX单号：</span>
                                        <span>
                                            ${detail.lbxNo}
                                        </span>
                                    </div>
                                </div>
                                <div class="info_item">
                                    <div class="info_text">
                                        <span>提单毛重：</span>
                                        <span>
                                            ${detail.billWeight}
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>提交人：</span>
                                        <span>
                                            ${detail.auditName}
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>收货地址：</span>
                                        <span>
                                            ${detail.deliveryAddr}
                                        </span>
                                    </div>
                                </div>
                                <div class="info_item">
                                    <div class="info_text">
                                        <span>提交时间：</span>
                                        <span>
                                            <fmt:formatDate value="${detail.auditDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </span>
                                    </div>
                                    <div class="info_text">
										<span>唛头：</span>
                                        <span>
                                            ${detail.mark}
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>境外发货人：</span>
                                        <span>
                                            ${detail.shipper}
                                        </span>
                                    </div>
                                </div>
                                <div class="info_item">
                                    <div class="info_text">
										<span>退货原因：</span>
                                        <span>
                                            ${detail.remark}
                                        </span>
                                    </div>
                                    <div class="info_text">
	                                    <span>是否同关区：</span>
	                                    <span>
	                                    ${detail.isSameAreaLabel}
	                                    </span>                      
                                    </div>
                                    <div class="info_text">
                                   		<span>事业部：</span>
	                                    <span>
	                                    ${detail.buName}
	                                    </span>      
                                    </div>
                                </div>
                            </div>
                            
                            <!--  title   基本信息  start -->
                            <!--   商品信息  start -->
                            <div class="col-12 ml10">
                                <div class="row">
                                    <div class="title_text">销售退货商品明细</div>
                                </div>
                            </div>
                            <div class="form-row mt20 table-responsive">
                                <table id="table-list" class="table table-striped" style="width: 1800px;">
                                    <thead>
                                        <tr>
                                        <th>序号</th>
                                        <th style="display: none">销售订单号</th>
                						<th>PO号码</th>
                						<th>PO时间</th>
                                        <th>退出商品货号</th>
                                        <th>退入商品货号</th>
                                        <th>退入商品名称</th>
                                        <th>退入商品条形码</th>
                                        <th>退货商品单价</th>
                                        <th>退货商品金额</th>
                                        <th>退货商品数量</th>
                                        <th>好品数量</th>
                                        <th>坏品数量</th>
                                        <th>退货商品毛重</th>
                                        <th>退货商品净重</th>
                                        <th>退货商品箱号</th>
                                        <th>退货商品合同号</th>
                                        <th>退运批次</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${detail.itemList}" var="item" varStatus="status">
                                        <tr>
                                            <td>${item.seq}</td>
                                            <td style="display: none">${item.saleOrderCode}</td>                                            
                                         	<td>${item.poNo}</td>
                                            <td>${item.poDate}</td>
                                            <td>${item.outGoodsNo}</td>
                                            <td>${item.inGoodsNo}</td>
                                            <td>${item.inGoodsName}</td>
                                            <td>${item.barcode}</td>
                                            <td><fmt:formatNumber value="${item.price}" pattern="#.########" minFractionDigits="8" /></td>
                                            <td><fmt:formatNumber value="${item.price * (item.returnNum+item.badGoodsNum)}" pattern="#.##" minFractionDigits="2" /></td>
                                            <td>${item.returnNum+item.badGoodsNum}</td>
                                            <td>${item.returnNum}</td>
                                            <td>${item.badGoodsNum}</td>
                                            <td>${item.grossWeight}</td>
                                            <td>${item.netWeight}</td>
                                            <td>${item.boxNo}</td>
                                            <td>${item.contractNo}</td>
                                            <td>${item.returnBatchNo}</td>
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
                                            <button type="button" class="btn btn-find waves-effect waves-light btn_default"
                                                    id="cancel-buttons">返回
                                            </button>
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

</div>
<!-- content -->
<script type="text/javascript">
    //table
    $(document).ready(function () {
        //取消按钮
        $("#cancel-buttons").click(function () {
            var pageSource = '${pageSource}';
            if (pageSource == "1") {
                $load.a("/list/menu.asyn?act=100&r="+Math.random());
            } else {
                $module.load.pageOrder("act=5001");
            }
        });
    });
</script>
