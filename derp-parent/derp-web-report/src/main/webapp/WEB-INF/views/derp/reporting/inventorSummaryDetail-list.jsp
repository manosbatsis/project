<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>



<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
            <div class="row">
                <!--  title   start  -->
                <div class="col-12">
                    <div class="card-box">
                        <div class="col-12 row">
                            <div>
                                <ol class="breadcrumb">
                                    <li><i class="block"></i> 当前位置：</li>
                                    <li class="breadcrumb-item"><a href="javascript:$module.load.pageReport('act=12001');">进销存汇总表</a></li>
                                    <li class="breadcrumb-item"><a href="#">出/入库详情</a></li>
                                </ol>
                            </div>
                        </div>
                        <!--  title   end  -->
                        <form id="form1">
                          
                        <div class="row col-12 mt20">
                            <table id="datatable-buttons" class="table table-striped" cellspacing="0"
                                   width="100%">
                                <thead>
                                <tr>
                                    <th style="vertical-align: middle;">序号</th>
                                    <th style="vertical-align: middle;">来源单号</th>
                                    <th style="vertical-align: middle;">业务单号</th>
                                    <th style="vertical-align: middle;">单据类型</th>
                                    <th style="vertical-align: middle;">仓库</th>
                                    <th style="vertical-align: middle;">出入时间</th>
                                    <th style="vertical-align: middle;">合同号</th>
                                    <th style="white-space:nowrap;">PO号</th>
                                    <th style="white-space:nowrap;">客户/供应商</th>
                                    <th style="white-space:nowrap;">商品货号</th>
                                    <th style="white-space:nowrap;">商品名称</th>
                                    <th style="white-space:nowrap;">数量</th>
                                    <th style="white-space:nowrap;">理货单位</th>
                                    <th style="white-space:nowrap;">备注</th>
                                </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${listItem }" var="item" varStatus="itemStatus">
		                               <tr>
		                                    <td>${itemStatus.index+1 }</td>
		                                    <td>${item.code}</td>
		                                    <td>${item.businessNo}</td>
		                                    <td>${item.thingName}</td>
		                                    <td>${item.depotName}</td>
		                                    <td> <fmt:formatDate value="${item.divergenceDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
		                                    <td>${item.contractNo}</td>
		                                    <td>${item.poNo}</td>
		                                    <td>${item.customerName}</td>
		                                    <td>${item.goodsNo}</td>
		                                    <td>${item.goodsName}</td>
		                                    <td>${item.num}</td>
		                                    <td>${item.unitLabel}</td>
		                                    <td>${item.remark}</td>
		                               </tr>
	                               </c:forEach>
                                </tbody>
                            </table>
                        </div>
                        </form>
                        <div class="form-row">
                            <div class="col-3">
                                <div class="form-row m-t-50">
                                    <div class="row">
                                        <div class="col-12">
                                            <button type="button" class="btn btn-find waves-effect waves-light btn_default"
                                                    id="cancel-buttons" onclick="$module.load.pageReport('act=12001');">返 回
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!--======================Modal框===========================  -->
                    <!-- end row -->

                </div>
                <!-- container -->
            </div>
       

    </div> <!-- content -->
</div>
