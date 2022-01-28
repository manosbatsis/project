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
                <div class="card-box table-responsive">
                    <!--  title   start  -->
                    <div class="col-12">
                        <div>
                            <div class="col-12 row">
                                <div>
                                    <ol class="breadcrumb">
                                        <li><i class="block"></i> 当前位置：</li>
                                        <li class="breadcrumb-item"><a href="#">欧莱雅管理</a></li>
                                        <li class="breadcrumb-item"><a href="javascript:details(${detail.id });">采购订单详情</a>
                                        </li>
                                    </ol>
                                </div>
                            </div>
                            <!--  title   end  -->
                            <!--  title   基本信息   start -->

                            <div class="title_text">基本信息</div>
                            <form id="add-form">
                                <div class="info_box">
                                    <div class="info_item">
                                        <div class="info_text">
                                            <span>采购单号：</span>
                                            <span>${detail.vordercode }</span>
                                        </div>
                                        <div class="info_text">
                                            <span>CSR单号：</span>
                                            <span>
                                                ${detail.vdef7 }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>订单日期：</span>
                                            <span>${detail.dorderdate }</span>
                                        </div>
                                    </div>
                                    <div class="info_item">
            							<div class="info_text">
                                            <span>公司：</span>
                                            <span>
                                                ${detail.merchantName }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>事业部：</span>
                                            <span>
                                                ${detail.buName }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>品牌：</span>
                                            <span>
                                                ${detail.vdef1 }
                                            </span>
                                        </div>
                                        
                                    </div>
                                    <div class="info_item">
                                        <div class="info_text">
                                            <span>供应商：</span>
                                            <span>
                                                ${detail.custname }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>业务类型：</span>
                                            <span>
                                                ${detail.vdef13 }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>采购类型：</span>
                                            <span>
                                                ${detail.docname }
                                            </span>
                                        </div>
                                    </div>
                                    <div class="info_item">
                                        <div class="info_text">
                                            <span>收货地址：</span>
                                            <span>
                                                ${detail.adress }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>创建时间：</span>
                                            <span>
                                                ${detail.createDate }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>数据来源：</span>
                                            <span>
                                                ${detail.sourceLabel }
                                            </span>
                                        </div>
                                    </div>


                                </div>
                                <!--  title   基本信息  start -->
                            </form>
                            <!--   商品信息  start -->
                            <div class="form-row mt20">
                                <table id="table-list" class="table table-striped table-responsive" cellspacing="0"
                                       width="100%">
                                    <thead>
                                    <tr>
                                        <th>行号</th>
                                        <th>厂商编码</th>
                                        <th>经销商编码</th>
                                        <th>商品名称</th>
                                        <th>建议采购量</th>
                                        <th>CSR确认量</th>
                                        <th>建议零售价</th>
                                        <th>备注</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${itemList }" var="item" varStatus="status">
                                        <tr>
                                        	<td style="font: 13px bold">${status.index+1}</td>
                                            <td>${item.invbasdoc }</td>
                                            <td>${item.cinvmecode }</td>
                                            <td>${item.invname }</td>
                                            <td>${item.vdef5 }</td>
                                            <td>${item.nordernum }</td>
                                            <td>${item.refsaleprice }</td>
                                            <td>${item.vmemo }</td>
   
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <div class="form-row mt20">
		                   		<div class="col-2"></div>
		                        <div class="col-2">
		                            <span>合计：</span>
		                            <span></span>
		                        </div>
		                        <div class="col-2">
		                            <span>建议采购量：${vdef5Total }</span>
		                            <span></span>
		                        </div>
		                        <div class="col-2">
		                            <span>CSR确认量：${nordernumTotal }</span>
		                            <span></span>
		                        </div>
		                        <div class="col-2">
		                        </div>
		                   </div>
                            <!--   商品信息  end -->
                            <div class="form-row">
                                <div class="col-3">
                                    <div class="form-row m-t-50">
                                        <div class="row">
                                            <div class="col-12">
                                                <button type="button" class="btn btn-find waves-effect waves-light btn_default"
                                                        id="cancel-buttons">返 回
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
                <!-- 弹窗开始 -->
                <!-- 弹窗结束 -->
            </div>
        </div>
        <!-- container -->
    </div>

</div>
<!-- content -->
<script type="text/javascript">
    $(function () {
    	
        function details(id) {
            $module.load.pageOrder("act=22002&id=" + id);
        } 

        //返回
        $("#cancel-buttons").click(function () {
        	$module.load.pageOrder("act=22001");
        });

    });

</script>
