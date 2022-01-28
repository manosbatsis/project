<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
        <form id="add-form">
            <div class="row">
                <!--  title   start  -->
                <div class="col-12">
                    <div class="card-box">
                        <div class="col-12 row">
                            <div>
                                <ol class="breadcrumb">
                                    <li><i class="block"></i> 当前位置：</li>
                                    <li class="breadcrumb-item"><a href="#">仓储管理</a></li>
                               		<li class="breadcrumb-item"><a href="javascript:dh_list();">库存调整列表</a></li>
                               		<li class="breadcrumb-item"><a href="javascript:dh_details(${detail.id });">库存调整详情</a></li>
                                </ol>
                            </div>
                        </div>
                      
                        <div class="info_box">
                            <div class="info_item">
                                <div class="info_text">
                                    <span>库存调整单号：</span>
                                    <span>
                                        ${detail.code }
                                        <input type="hidden" class="input_msg" id="id" value="${detail.id}">
                                    </span>
                                </div>
                                <div class="info_text">
                                    <span>单据状态：</span>
                                    <span>
                                        <%--<c:choose>--%>
                                            <%--<c:when test="${detail.status eq '020' }">待调整</c:when>--%>
                                            <%--<c:when test="${detail.status eq '019' }">已调</c:when>--%>
                                            <%--<c:when test="${detail.status eq '006' }">已删除</c:when>--%>
                                            <%--<c:when test="${detail.status eq '022' }">处理中</c:when>--%>
                                        <%--</c:choose>--%>
                                        ${detail.statusLabel}
                                    </span>
                                </div>
                                <div class="info_text">
                                    <span>调整仓库：</span>
                                    <span>
                                        ${detail.depotName }
                                    </span>
                                </div>
                            </div>
                            <div class="info_item">
                                <div class="info_text">
                                    <span>业务类别：</span>
                                    <span>
                                        <%--<c:choose>--%>
                                            <%--<c:when test="${detail.model eq '1' }">销毁</c:when>--%>
                                            <%--<c:when test="${detail.model eq '2' }">月结损益</c:when>--%>
                                            <%--<c:when test="${detail.model eq '3' }">其他出入</c:when>--%>
                                            <%--<c:when test="${detail.model eq '4' }">唯品红冲</c:when>--%>
                                        <%--</c:choose>--%>
                                        ${detail.modelLabel}
                                    </span>
                                </div>
                                <div class="info_text">
                                    <span>创建时间：</span>
                                    <span>
                                      <fmt:formatDate value="${detail.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </span>
                                </div>
                                <div class="info_text">
                                    <span>来源单据号：</span>
                                    <span>
                                        ${detail.sourceCode }
                                    </span>
                                </div>
                            </div>
                            <div class="info_item">
                                <div class="info_text">
                                    <span>PO单号：</span>
                                    <span>
                                        ${detail.poNo }
                                    </span>
                                </div>
                                <div class="info_text">
                                    <span>调整时间：</span>
                                    <span>
                                        <fmt:formatDate value="${detail.adjustmentTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </span>
                                </div>
                                <div class="info_text">
                                    <span>创建人：</span>
                                    <span>
                                        ${detail.createUsername }
                                    </span>
                                </div>
                            </div>
                            <div class="info_item">
                                <div class="info_text">
                                    <span>归属月份：</span>
                                    <span>
                                        ${detail.months }
                                    </span>
                                </div>
                                <div class="info_text">
                                    <span>归属日期：</span>
                                    <c:choose>
										<c:when test="${detail.status eq '020' }">
											<span>
		                                         <input type="text" class="input_msg date-input" name="sourceTime" 
		                                                value="<fmt:formatDate value='${detail.sourceTime}' pattern='yyyy-MM-dd'/>">&nbsp;
		                                    </span>
										</c:when>
										<c:otherwise>
										    <span>
		                                         <fmt:formatDate value="${detail.sourceTime}" pattern="yyyy-MM-dd"/>
		                                    </span>
										</c:otherwise>
								    </c:choose>
                                </div>
                                <div class="info_text">
                                    <span>单据来源：</span>
                                    <%--<c:choose>--%>
                                        <%--<c:when test="${detail.source eq '01' }">接口回传</c:when>--%>
                                        <%--<c:when test="${detail.source eq '02' }">手工录入</c:when>--%>
                                    <%--</c:choose>--%>
                                    ${detail.sourceLabel}
                                </div>
                            </div>
                             <div class="info_item">
                                 <div class="info_text">
                                    <span>&nbsp;&nbsp;备&nbsp;注：&nbsp;</span>
                                    <span>
                                        <textarea  class="edit_input" id="remark" maxlength="1000" style="width: 500px;height: 60px;">${detail.remark}</textarea>
                                    </span>
                                </div>
                             </div>
                              <div class="info_item">
                                 <div class="info_text"></div>
                                 <div class="info_text">
                                    <input type="button" id="modfiyRemarkAndSourceTime" class="btn btn-find waves-effect waves-light btn-sm" value="保存" style="height: 30px;width: 60px;"/>
                                 </div>
                                 <div class="info_text"></div>
                              </div>
                        </div>


                        <div class="title_text">商品信息</div>
                        <div class="mt10">
                            <table id="datatable-buttons" class="table table-striped" cellspacing="0"
                                   width="100%">
                                <thead>
                                <tr>
                                    <th>序号</th>
                                    <th>PO单号</th>
                                    <th>商品货号</th>
                                    <th>商品名称</th>
                                    <th>商品条形码</th>
                                    <th>调整类型</th>
                                    <th>原始批次号</th>
                                    <th>是否坏品</th>
                                    <th>总调整数量</th>
                                    <th>是否过期</th> 
                                    <th>海外仓理货单位</th>
                                    <!-- <th>生产日期</th>
                                    <th>失效日期</th> -->                                  
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${detail.itemList }" var="item" varStatus="status">
	                                <tr>
	                                    <td>${status.index+1}</td>
	                                    <td>${item.poNo }</td>
	                                    <td>${item.goodsNo }</td>
	                                    <td>${item.goodsName }</td>
	                                    <td>${item.barcode }</td>
	                                    <td>
	                                    	<%--<c:choose>--%>
	                                    		<%--<c:when test="${item.type eq '0' }">调减</c:when>--%>
	                                    		<%--<c:when test="${item.type eq '1' }">调增</c:when>--%>
	                                    		<%--<c:when test="${item.type eq '2' }">其他</c:when>--%>
	                                    		<%--<c:otherwise>${item.type}</c:otherwise>--%>
	                                    	<%--</c:choose>--%>
                                            ${item.typeLabel}
	                                    </td>
	                                    <td>${item.oldBatchNo }</td>
	                                    <td>
	                                    	<%--<c:choose>--%>
	                                    		<%--<c:when test="${item.isDamage eq '0' }">否</c:when>--%>
	                                    		<%--<c:when test="${item.isDamage eq '1' }">是</c:when>--%>
	                                    	<%--</c:choose>--%>
                                            ${item.isDamageLabel}
	                                    </td>
	                                    <td>${item.adjustTotal }</td>
	                                   <%--  <td><fmt:formatDate value="${item.productionDate }" pattern="yyyy-MM-dd"/></td>
	                                    <td><fmt:formatDate value="${item.overdueDate }" pattern="yyyy-MM-dd"/></td> --%>
	                                	<td>
	                                		<%--<c:if test="${item.isExpire == '1'}">否</c:if>--%>
                            				<%--<c:if test="${item.isExpire == '0'}">是</c:if>--%>
                                            ${item.isExpireLabel}
	                                	</td>
	                                	
	                                	<td>
	                                		<%--<c:if test="${item.tallyingUnit == '00'}">托盘</c:if>--%>
                            				<%--<c:if test="${item.tallyingUnit == '01'}">箱</c:if>--%>
                            				<%--<c:if test="${item.tallyingUnit == '02'}">件</c:if> --%>
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
                                            <!-- <button type="button" class="btn btn-find waves-effect waves-light btn_default"
                                                    id="cancel-buttons">返 回
                                            </button> -->
                                                <a class="btn btn-find waves-effect waves-light btn_default" href="javascript:$module.load.pageStorage('act=10002');">返回</a>  
                                            
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
        $module.load.pageStorage("act=100022&id="+id);
    }

    function dh_list(){
        $module.load.pageStorage("act=10002");
    }
    
    $(".date-input").datetimepicker({
        language:  'zh-CN',
        format: "yyyy-mm-dd",
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        minView: 2
    });
});

$("#modfiyRemarkAndSourceTime").click(function(){
	  var id = $("#id").val();
	  var sourceTime = $("input[name=sourceTime]").val();
	  var remark = $("#remark").val();
	  var url=serverAddr+"/adjustmentInventory/modfiyRemarkAndSourceTimeById.asyn";
      $custom.request.ajax(url,{"id":id,"remark":remark,"sourceTime":sourceTime},function(result){
          if(result.data.code=='00'){
               $custom.alert.success('保存成功');
               $module.load.pageStorage("act=10002");
          }else{
              $custom.alert.error(result.data.message);
          }
      });
})
</script>