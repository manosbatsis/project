<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common.jsp"/>
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
        <div class="row">
            <div class="col-12">
                <div class="card-box table-responsive">
                    <!--  title   start  -->
                    <div class="col-12">
                        <div>
                            <div class="col-12 row">
                                <div>
                                    <ol class="breadcrumb">
                                        <li><i class="block"></i> 当前位置：</li>
                                        <li class="breadcrumb-item"><a href="#">调拨管理</a></li>
                                        <li class="breadcrumb-item"><a
                                                href="javascript:$module.load.pageOrder('act=8004');">调拨入库列表</a></li>
                                        <li class="breadcrumb-item"><a href="#">调拨入库详情</a></li>
                                    </ol>
                                </div>
                            </div>
                            <!--  title   end  -->
                            <!--  title   基本信息   start -->
                            <div class="title_text">调拨入库详情</div>

                            <div class="info_box">
                                <div class="info_item">
                                    <div class="info_text">
                                        <span>调拨入单号：</span>
                                        <span>
                                            ${transferInDepot.code }
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>调出仓库：</span>
                                        <span>
                                            ${transferInDepot.outDepotName }
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>服务类型：</span>
                                        <span>${transferOrder.serveTypesLabel}
                                </span>
                                    </div>
                                </div>
                                <div class="info_item">
                                    <div class="info_text">
                                        <span>单据状态：</span>
                                        <span>${transferInDepot.statusLabel}
                                </span>
                                    </div>
                                    <div class="info_text">
                                        <span>调入仓库：</span>
                                        <span>
                                            ${transferInDepot.inDepotName }
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>事业部：</span>
                                        <span>${transferInDepot.buName}
                                        </span>
                                    </div>
                                </div>
                                <div class="info_item">
                                    <div class="info_text">
                                        <span>企业调拨单号：</span>
                                        <span>
                                            ${transferInDepot.transferOrderCode }
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>申报海关：</span>
                                        <span>
                                            ${outDepotModel.customsNo }
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>调拨入库时间：</span>
                                        <span>
                                    <fmt:formatDate value="${transferInDepot.transferDate}"
                                                    pattern="yyyy-MM-dd HH:mm:ss"/>
                                </span>
                                    </div>
                                </div>
                                <div class="info_item">
                                    <div class="info_text">
                                        <span>合同号：</span>
                                        <span>
                                            ${transferInDepot.contractNo }
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>申报地国检：</span>
                                        <span>
                                            ${outDepotModel.inspectNo }
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>委托单位：</span>
                                        <span>
                                            ${transferInDepot.merchantName }
                                        </span>
                                    </div>
                                </div>
                                <div class="info_item">
                                    <div class="info_text">
                                        <span>调出公司：</span>
                                        <span>
                                            ${transferInDepot.merchantName }
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>调入客户：</span>
                                        <span>
                                            ${transferInDepot.inCustomerName }
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>业务场景：</span>
                                        <span>${transferOrder.modelLabel}
                                        </span>
                                    </div>

                                </div>
                                <div class="info_item">
                                    <div class="info_text">
                                        <span>备注：</span>
                                        <span>
                                            ${transferOrder.remark }
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>创建人：</span>
                                        <span>
                                            ${transferInDepot.createName }
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>创建时间：</span>
                                        <span>
                                            ${transferInDepot.createDate }
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <!--  title   基本信息  end -->
                            <div class="title_text">商品信息</div>
                            <div class="mt20">
                                <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                                    <thead>
                                    <tr>
                                        <th>序号</th>
                                        <th>调入商品编号</th>
                                        <th>调入商品货号</th>
                                        <th>调入商品名称</th>
                                        <th>调入数量</th>
                                        <th>正常数量</th>
                                        <th>坏品数量</th>
                                        <th>过期数量</th>
                                        <th>调入批次</th>
                                        <th>生产日期</th>
                                        <th>失效日期</th>
                                        <th>理货单位</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${itemList }" var="item" varStatus="status">
                                        <tr>
                                            <td>${status.index + 1 }</td>
                                            <td>${item.inGoodsCode}</td>
                                            <td>${item.inGoodsNo}</td>
                                            <td>${item.inGoodsName}</td>
                                            <td>${item.transferNum+item.wornNum+item.expireNum}</td>
                                            <td>${item.transferNum}</td>
                                            <td>${item.wornNum}</td>
                                            <td>${item.expireNum}</td>
                                            <td>${item.transferBatchNo}</td>
                                            <td><fmt:formatDate value="${item.productionDate}"
                                                                pattern="yyyy-MM-dd"/></td>
                                            <td><fmt:formatDate value="${item.overdueDate}" pattern="yyyy-MM-dd"/></td>
                                            <td>${item.tallyingUnitLabel}
                                                <%--<c:choose>
                                                    <c:when test="${item.tallyingUnit=='00'}">托盘</c:when>
                                                    <c:when test="${item.tallyingUnit=='01'}">箱</c:when>
                                                    <c:when test="${item.tallyingUnit=='02'}">件</c:when>
                                                    <c:otherwise>${item.tallyingUnit}</c:otherwise>
                                                </c:choose>--%>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <div class="row mt20 order">
								<div class='col-12'>
									<div class="title_text">附件列表</div>
									<div id="attachmentTable" multiple ></div>
								</div>
							</div>
							<div class="row mt20">
								<div class='col-12'>
										<div class="form-row">
											<div class="col-12">
												<div class="row">
													<div class="col-5">
														<button type="button"
															class="btn btn-find waves-effect waves-light btn_default"
															id="cancel-buttons" onclick="$module.load.pageOrder('act=8004');">返回</button>
													</div>
												</div>
											</div>
									</div>
								</div>
							</div>
                            <!-- <div class="form-row m-t-50">
                                <div class="col-12">
                                    <div class="row">
                                        <div class="col-5">
                                            <a class="btn btn-find waves-effect waves-light btn_default" href="javascript:$module.load.pageOrder('act=8004');">返回</a>
                                        </div>
                                    </div>
                                </div>
                            </div> -->

                        </div>
                    </div>
                    <!-- 新增弹窗部分   start -->
                </div>
                <!-- end row -->
                </form>
            </div>
            <!-- container -->
        </div>

    </div> <!-- content -->
<input id="code" type="hidden" value="${transferInDepot.code}">
<script type="text/javascript">
	var code =  $("#code").val()   ;
	$("#attachmentTable").createAttachTables(code);
</script>
