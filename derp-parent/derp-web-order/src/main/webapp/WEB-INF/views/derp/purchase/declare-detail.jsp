<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="/WEB-INF/views/common.jsp" />
<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
          <form id="add-form" action="/merchandise/saveMerchandise.asyn">
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
                       <li class="breadcrumb-item"><a href="#">采购档案</a></li>
                       <li class="breadcrumb-item"><a href="javascript:dh_list();">预申报单</a></li>
                       <li class="breadcrumb-item"><a href="javascript:dh_details(${detail.id });">预申报单详情</a></li>
                    </ol>
                    </div>
                   </div>
                    <!--  title   end  -->               
                          <!--  title   基本信息   start -->
                    <div class="title_text">基本信息</div>
                    <div class="info_box">
                        <div class="info_item">
                            <div class="info_text">
                                <span>预申报单号：</span>
                                <span>${detail.code}</span>
                            </div>
                            <div class="info_text">
                                <span>供应商：</span>
                                <span> ${detail.supplierName}</span>
                            </div>
                            <div class="info_text">
                                <span>头程提单号：</span>
                                <span>${detail.ladingBill}</span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>业务模式：</span>
                                <span>
                                    ${detail.businessModelLabel}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>发票号：</span>
                                <span>${detail.invoiceNo}</span>
                            </div>
                            <div class="info_text">
                                <span>合同号：</span>
                                <span>${detail.contractNo}</span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>销售渠道：</span>
                                <span>${detail.channel}</span>
                            </div>
                            <div class="info_text">
                                <span>PO号：</span>
                                <span>${detail.poNo}</span>
                            </div>
                            <div class="info_text">
                                <span>运输方式：</span>
                                <span>
                                    ${detail.transportLabel }
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>入仓仓库：</span>
                                <span>${detail.depotName}</span>
                            </div>
                            <div class="info_text">
                                <span>事业部：</span>
                                <span>${detail.buName}</span>
                            </div>
                            <div class="info_text">
                                <span>预计到港时间：</span>
                                <span>
                                	<fmt:formatDate value="${detail.arriveDate}" pattern="yyyy-MM-dd"/>
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>箱数：</span>
                                <span>${detail.boxNum}</span>
                            </div>
                            <div class="info_text">
                                <span>目的港名称：</span>
                                <span>
                                    ${detail.destinationPortName}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>收货地址：</span>
                                <span>${detail.deliveryAddr}</span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>包装方式：</span>
                                <span> ${detail.packType }</span>
                            </div>
                            <div class="info_text">
                                <span>付款条约：</span>
                                <span>${detail.payRules }</span>
                            </div>
                            <div class="info_text">
                                <span>提单毛重KG：</span>
                                <span> ${detail.billWeight }</span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>卸货港：</span>
                                <span>
                                    ${detail.portDestNoLabel }
                                </span>
                            </div>
                            <div class="info_text">
                                <span>二程提单号：</span>
                                <span>${detail.blNo }</span>
                            </div>
                            <div class="info_text">
                                <span> 邮箱：</span>
                                <span>${detail.email }</span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span> 进出口口岸：</span>
                                <span>${detail.imExpPort }</span>
                            </div>
                            <div class="info_text">
                                <span>订单状态：</span>
                                <span>
                                    ${detail.statusLabel }
                                </span>
                            </div>
                            <div class="info_text">
                                <span> LBX单号：</span>
                                <span>${detail.lbxNo }</span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>创建人：</span>
                                <span>${detail.createName }</span>
                            </div>
                            <div class="info_text">
                                <span>创建时间：</span>
                                <span>
                                    <fmt:formatDate value="${detail.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </span>
                            </div>
                            <div class="info_text">
                                <span>境外发货人：</span>
                                <span>${detail.shipper }</span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>唛头：</span>
                                <span>${detail.mark }</span>
                            </div>
                            <div class="info_text">
                            	<span>审核人：</span>
                                <span>${detail.submitter }</span>
                            </div>
                            <div class="info_text">
                            	<span>审核时间：</span>
                                <span>
                                	<fmt:formatDate value="${detail.submitDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
	                            	<span>海外仓理货单位：</span>
	                                <span>
	                                   ${detail.tallyingUnitLabel}
	                                </span>
                            </div>
                            <div class="info_text">
                            	<span>目的地名称：</span>
                                <span>${detail.destinationName }</span>
                            </div>
                            <div class="info_text">
                                <span>备注：</span>
                                <span>${detail.remark}</span>
                            </div>
                        </div>
                    </div>

                 <!--  title   基本信息  start -->
                   <!--   商品信息  start -->
                    <div class="title_text">商品信息</div>
                      <div class="row mt20">
                        <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th>序号</th>
                            <th>合同号</th>
                            <th>商品编码</th>
                            <th>商品名称</th>
                            <th>商品货号</th>
                            <th>申报数量</th>
                            <th>采购单价</th>
                            <th>申报单价</th>
                            <th><div>采购总金额</div><div>（RMB）</div></th>
                            <!-- <th>采购单位</th> -->
                            <th>品牌类型</th>
                            <th>毛重（KG）</th>
                            <th>净重（KG）</th>
                            <th>箱号</th>
                            <th>生产批次号</th>
                            <th style="min-width: 200px">成分占比</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${detail.itemList}" var="item" varStatus="status">
                            <tr>
                                <td>${item.seq}</td>
                                <td>${item.contractNo}</td>
                                <td>${item.goodsCode}</td>
                                <td>${item.goodsName}</td>
                                <td>${item.goodsNo}</td>
                                <td>${item.num}</td>
                                <td>${item.purchasePrice}</td>
                                <td>${item.price}</td>
                                <td>${item.amount}</td>
                                <td>${item.brandName}</td>
                                <td>
                                	${item.grossWeightSum}
                                </td>
                                <td>
                                	${item.netWeightSum}
                                </td>
                                <td>${item.boxNo}</td>
                                <td>${item.batchNo}</td>
                                <td>${item.constituentRatio}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                 <!--   商品信息  end -->
                      <div class="form-row m-t-50">
                          <div class="col-12">
                              <div class="row">
<!--                                   <div class="col-5"> -->
<!--                                       <button type="button" class="btn btn-info waves-effect waves-light fr" id="save-buttons">保 存</button> -->
<!--                                   </div> -->
                                  <div class="col-5">
                                      <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="cancel-buttons">返 回</button>
                                  </div>
                              </div>
                          </div>
                      </div>
                </div>
              </div>
                           <!-- 新增弹窗部分   start -->
             </div>
                  <!-- end row -->
           </div>
          </div>
          </form>

        </div>
        <!-- container -->
    </div>

</div> <!-- content -->


<script type="text/javascript">
$(function(){
	//返回按钮
	$("#cancel-buttons").click(function(){
		$module.load.pageOrder("act=3002");
	});
});

function dh_list(){
	$module.load.pageOrder("act=3002");
}

function dh_details(id){
	$module.load.pageOrder("act=30022&id="+id);
}
</script>
