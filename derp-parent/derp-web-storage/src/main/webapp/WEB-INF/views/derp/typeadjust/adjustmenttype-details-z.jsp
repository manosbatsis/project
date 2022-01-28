<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
        <form id="add-form" action="/merchandise/saveMerchandise.asyn">
            <div class="row">
                <!--  title   start  -->
                <div class="col-12">
                    <div class="card-box">
                        <div class="col-12 row">
                            <div>
                                <ol class="breadcrumb">
                                    <li><i class="block"></i> 当前位置：</li>
                                    <li class="breadcrumb-item"><a href="#">仓储管理</a></li>
                                	<li class="breadcrumb-item"><a href="javascript:dh_list();">类型调整列表</a></li>
                                	<li class="breadcrumb-item"><a href="javascript:dh_details(${detail.id });">类型调整详情</a></li>
                                </ol>
                            </div>
                        </div>
                        <!--  title   end  -->
                        <div class="form-row">
                            <form id="form1">
                                <div class="col-5">
                                    <div class="row">
                                        <!-- <label class="col-5 col-form-label "> -->
                                        <label class="col-5">
                                            <div class="fr">类型调整单号<span>：</span></div>
                                        </label>
                                        <div class="col-7 ml10">
                                            ${detail.code }
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </div>
                        <div class="form-row">
                            <div class="col-5">
                                <div class="row">
                                    <label class="col-5">
                                        <div class="fr">单据状态<span>：</span></div>
                                    </label>
                                    <div class="col-7 ml10">
                                        ${detail.statusLabel}
                                    </div>
                                </div>
                            </div>
                            <div class="col-5">
                                <div class="row">
                                    <label class="col-5">
                                        <div class="fr">调整仓库<span>：</span></div>
                                    </label>
                                    	${detail.depotName }
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="col-5">
                                <div class="row">
                                    <label class="col-5 ">
                                        <div class="fr">业务类别<span>：</span></div>
                                    </label>
                                    <div class="col-7 ml10">
                                        ${detail.modelLabel}
                                    </div>
                                </div>
                            </div>
                            <div class="col-5">
                                <div class="row">
                                    <label class="col-5 ">
                                        <div class="fr">单据日期<span>：</span></div>
                                    </label>
                                     <fmt:formatDate value="${detail.codeTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="col-5">
                                <div class="row">
                                    <label class="col-5  ">
                                        <div class="fr">单据类型调整名称<span>：</span></div>
                                    </label>
                                    <div class="col-7 ml10">
                                        ${detail.adjustmentTypeName }
                                    </div>
                                </div>
                            </div>
                            <div class="col-5">
                                <div class="row">
                                    <label class="col-5 ">
                                        <div class="fr">推送时间<span>：</span></div>
                                    </label>
                                     <fmt:formatDate value="${detail.pushTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="col-5">
                                <div class="row">
                                    <label class="col-5 ">
                                        <div class="fr">来源单据号<span>：</span></div>
                                    </label>
                                    <div class="col-7 ml10">
                                        ${detail.sourceCode }
                                    </div>
                                </div>
                            </div>
                            <div class="col-5">
                                <div class="row">
                                    <label class="col-5 ">
                                        <div class="fr">调整时间<span>：</span></div>
                                    </label>
                                    <fmt:formatDate value="${detail.adjustmentTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="col-5">
                                <div class="row">
                                    <label class="col-5  ">
                                        <div class="fr">调整原因<span>：</span></div>
                                    </label>
                                    <div class="col-7 ml10">
                                    ${detail.adjustmentRemark }
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="title_text">商品信息</div>
                        <div class="row col-12 mt10">
                            <table id="datatable-buttons" class="table table-striped" cellspacing="0"
                                   width="100%">
                                <thead>
                                <tr>
                                    <th style="white-space:nowrap;">序号</th>
                                    <th style="white-space:nowrap;">事业部</th>
                                    <th style="white-space:nowrap;">商品货号</th>
                                    <th style="white-space:nowrap;">商品名称</th>
                                    <th style="white-space:nowrap;">商品条形码</th>
                                    <th style="white-space:nowrap;">原批次号</th>
                                    <th style="white-space:nowrap;">生产日期</th>                                   
                                    <th style="white-space:nowrap;">失效日期</th> 
                                    <th style="white-space:nowrap;">调整前类型</th>                                   
                                    <th style="white-space:nowrap;">调整后类型</th>                                   
                                    <th style="white-space:nowrap;">总调整数量</th>
                                    <th style="white-space:nowrap;">理货单位</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${detail.itemList }" var="item" varStatus="status">
	                                <tr>
	                                    <td>${status.index+1}</td>
	                                    <td>${item.buName }</td>
	                                    <td>${item.goodsNo }</td>
	                                    <td>${item.goodsName }</td>
	                                    <td>${item.barcode }</td>
	                                    <td>${item.oldBatchNo }</td>
	                                    <td><fmt:formatDate value="${item.productionDate }" pattern="yyyy-MM-dd"/></td>
	                                    <td><fmt:formatDate value="${item.overdueDate }" pattern="yyyy-MM-dd"/></td>
	                                    <td>未过期</td>
	                                    <td>已过期</td>
	                                    <td>${item.adjustTotal }</td>
	                                    <td>
                                           ${item.tallyingUnitLabel}
	                                    </td>
	                                </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                        
                        <div class="form-row">
                            <div class="col-3">
                                <div class="form-row m-t-50">
                                    <div class="row">
                                        <div class="col-12">
                                              <a class="btn btn-find waves-effect waves-light btn_default" href="javascript:$module.load.pageStorage('act=10001');">返回</a>                                          
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
        </form>
    </div> <!-- content -->
</div>
<script type="text/javascript">
$(document).ready(function() {
	//详情
    function dh_details(id){
        $module.load.pageStorage("act=100011&id="+id);
    }

    function dh_list(){
        $module.load.pageStorage("act=10001");
    }
});
</script>