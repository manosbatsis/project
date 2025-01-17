<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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
                                        <li class="breadcrumb-item"><a href="#">采购退货订单</a></li>
                                        <li class="breadcrumb-item"><a href="#">打托出库</a></li>
                                    </ol>
                                </div>
                            </div>
                            <div class="title_text">基本信息</div>
                            <form id="add-form">
	                            <div class="info_box">
	                                <div class="info_item">
	                                    <div class="info_text">
	                                        <input value="${detail.id}" type="hidden" id="purchaseReturnId" name="purchaseReturnId"/>
	                                        <span>采购退货订单号：</span>
	                                        <span>
	                                            ${detail.code}
	                                        </span>
	                                    </div>
	                                    <div class="info_text">
	                                        <span>出库仓库：</span>
	                                        <span>
	                                            ${detail.outDepotName}
	                                        </span>
	                                    </div>
	                                    <div class="info_text">
	                                        <span>入库仓库：</span>
	                                        <span>
	                                            ${detail.inDepotName}
	                                        </span>
	                                    </div>
	                                </div>
	                                <div class="info_item">
	                                    <div class="info_text">
	                                        <span>PO号：</span>
	                                        <span>
	                                            ${detail.poNo}
	                                        </span>
	                                    </div>
	                                    <div class="info_text">
	                                        <span>事业部：</span>
	                                        <span>
	                                            ${detail.buName}
	                                        </span>
	                                    </div>
	                                    <div class="info_text">
	                                        <span>供应商：</span>
	                                        <span>
	                                            ${detail.supplierName}
	                                        </span>
	                                    </div>
	                                </div>
	                            </div>
	                            <div class="col-12 ml10">
	                                <div class="row">
	                                    <div class="col-10">
	                                        <div class="title_text">出库信息</div>
	                                    </div>
	                                </div>
	                            </div>
	                            <div class="col-12 ml10">
	                                <div class="info_box">
	                                    <div class="info_item">
	                                        <div class="info_text">
	                                            <label><i class="red">*</i>出库时间： </label>
	                                            <input type="text" class="edit_input form_datetime "
	                                                   name="returnOutDate" id="returnOutDate" required reqMsg = "出库时间不能为空"/>
	                                        </div>
	                                        <div class="info_text">
	                                        </div>
	                                        <div class="info_text">
	                                        </div>
	                                    </div>
	                                </div>
	                            </div>
                            </form>
                            <div class="form-row mt20 ml15 table-responsive">

                                <table id="table-list" class="table table-striped" >
                                    <thead>
                                    <tr>
                                        <th>商品货号</th>
                                        <th>商品名称</th>
                                        <th>条形码</th>
                                        <th>退货商品数量</th>
                                        <th><i class="red">*</i>退货出库数量</th>
                                        <th>批次号</th>
                                        <th>生产日期</th>
                                        <th>失效日期</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${itemList }" var="item">
                                        <tr>
                                            <td>
                                                ${item.goodsNo}
                                            </td>
                                            <td>${item.goodsName}</td>
                                            <td>${item.barcode}</td>
                                            <td>${item.returnNum}</td>
                                            <td>
                                            	<input type='hidden' name='goodsId' value='${item.goodsId}'>
                                            	<input type="hidden" name="goodsNo" value="${item.goodsNo}" />
                                            	<input type="hidden" name="oldNum" value="${item.returnNum}" />
                                                <input type="text" style='width:70px;text-align:center;' name="num" value="${item.returnNum}" onkeyup="value=value.replace(/[^\d]/g,'')" required reqMsg = "退货出库数量不能为空"/>
                                            </td>
                                            <td>
                                            	<input type="text" name=batchNo style='width:70px;text-align:center;' />
                                            </td>
                                            <td>
                                            	<input type="text" class="date-input" name="productionDate" style='width:120px;text-align:center;' />
                                            </td>
                                            <td>
                                            	<input type="text" class="date-input" name="overdueDate" style='width:120px;text-align:center;' />
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <!--   商品信息  end -->
                            <div class="form-row m-t-50">
                                <div class="col-12">
                                    <div class="row">
                                        <div class="col-6">
                                            <button type="button"
                                                    class="btn btn-info waves-effect waves-light fr btn_default"
                                                    id="save-buttons">保 存
                                            </button>
                                        </div>
                                        <div class="col-6">
                                            <button type="button"
                                                    class="btn btn-light waves-effect waves-light btn_default"
                                                    id="cancel-buttons">取 消
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
            <!-- end row -->
        </div>
        <!-- container -->
    </div>

</div>
<!-- content -->
<script type="text/javascript">
    $(document).ready(function () {
    	
    	$derpTimer.init(".date-input", "yyyy-MM-dd") ;
    	$derpTimer.lessThanNowDateTimer("#returnOutDate", "yyyy-MM-dd") ;
    	
        $(function () {
            //取消按钮
            $("#cancel-buttons").click(function () {
                $module.load.pageOrder('act=3008');
            });
        });

        //保存按钮
        $("#save-buttons").click(function () {

        	//必填项校验
        	if(!$checker.verificationRequired()){
        		return ;
        	}
        	
        	var flag = true ;
        	var itemList = [] ;
        	
            $("#table-list").find("input[name='num']").each(function(index, item){
            	
            	var num = $(item).val() ;
            	var oldNum = $(this).prev().val() ;
            	var goodsNo = $(this).prev().prev().val() ;
            	var goodsId = $(this).prev().prev().prev().val() ;
            	var batchNo = $(this).parent().parent().find("input[name='batchNo']").val() ;
            	var productionDate = $(this).parent().parent().find("input[name='productionDate']").val() ;
            	var overdueDate = $(this).parent().parent().find("input[name='overdueDate']").val() ;
            	
            	if(parseInt(num) <= 0){
            		$custom.alert.error("商品货号：" + goodsNo + "退货出库数量必须大于0");
            		flag = false ;
            		
            		return false ;
            	}
            	
            	if(parseInt(oldNum) < parseInt(num)){
            		$custom.alert.error("商品货号：" + goodsNo + "退货出库数量不能超过退货数量");
            		flag = false ;
            		
            		return false ;
            	}
            	
            	if(!batchNo){
            		batchNo = "" ;
            	}
            	
            	if(!productionDate){
            		productionDate = "" ;
            	}
            	
            	if(!overdueDate){
            		overdueDate = "" ;
            	}
            	
            	var itemJson = {"goodsId": goodsId, "goodsNo": goodsNo, "num": num, "batchNo":batchNo, "productionDate":productionDate, "overdueDate":overdueDate} ;
            	itemList.push(itemJson) ;
            	
            }) ;
            
            if(!flag){
            	return ;
            }
            
            $("#returnOutDate").val($("#returnOutDate").val() + " 00:00:00") ;
            var json = $("#add-form").serializeArray();
            
            itemList = JSON.stringify(itemList);
            
            json.push({"name": "itemList", "value": itemList}) ;
            
            var url = serverAddr+"/purchaseReturn/purchaseReturnDelivery.asyn"
            $custom.request.ajax(url, json, function(result){
            	if(result.state==200){
                	
                    $custom.alert.success("保存成功");
                    //重新加载table表格
                    setTimeout(function () {
                    	$module.load.pageOrder("act=3008");
                    }, 500);
                    
                }else{
                	if(result.expMessage != null){
    					$custom.alert.error(result.expMessage);
    				}else{
    					$custom.alert.error(result.message);
    				}
                }
            });
        });

    });

</script>
