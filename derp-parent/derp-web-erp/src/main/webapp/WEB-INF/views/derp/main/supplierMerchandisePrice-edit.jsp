<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt60">
        <!-- Start row -->
        <form id="edit-form">
            <div class="row">
                <div class="card-box margin table-responsive" id="border">
                    <!--  title   start  -->
                    <div class="col-12 ml10">
                        <div class="col-12 row">
                            <ol class="breadcrumb">
                                <li><i class="block"></i> 当前位置：</li>
                                <li class="breadcrumb-item"><a href="#">供应商档案</a></li>
                                <li class="breadcrumb-item "><a
                                        href="javascript:$load.a('/supplierMerchandisePrice/toPage.html');">采购价格管理</a>
                                </li>
                                <li class="breadcrumb-item"><a
                                        href="javascript:$load.a('/supplierMerchandisePrice/toEditPage.html?id=${detail.id }');">编辑价目表</a>
                                </li>
                            </ol>
                        </div>
                        <!--  title   end  -->
                        <!--  title   供应商详情  start -->

                        <div class="title_text">供应商详情</div>
                        <div class="info_box">
                            <div class="info_item">
                                <div class="info_text">
                                    <span>供应商编号：</span>
                                    <span>${detail.customerCode}</span>
                                </div>
                                <div class="info_text">
                                    <span>供应商名称：</span>
                                    <span>${detail.customerName}</span>
                                </div>
                                <div class="info_text">
                                    <span>供应商简称：</span>
                                    <span>${detail.customerName}</span>
                                </div>
                            </div>
                            <div class="info_item">
                                <div class="info_text">
                                    <span>结算方式：</span>
                                    <c:choose>
                              	  	  <c:when test="${detail.settlementMode eq '1'}"> 预付</c:when>
                              	  	  <c:when test="${detail.settlementMode eq '2'}"> 到付</c:when>
                              	  	  <c:when test="${detail.settlementMode eq '3'}"> 月结</c:when>
                              	  </c:choose>
                                </div>
                                <div class="info_text"></div>
                                <div class="info_text"></div>
                            </div>
                        </div>
                        <!--  title   供应商详情  start -->
                        <!--   商品报价  start -->

                        <div class="title_text">货品详情</div>
                        <div class="info_box">
                            <div class="info_item">
                                <div class="info_text">
                                    <span>货品条形码：</span>
                                    <span>${detail.barcode}</span>
                                </div>
                                <div class="info_text">
                                    <span>品牌：</span>
                                    <span>${detail.brandName}</span>
                                </div>
                                <div class="info_text">
                                    <span>规格：</span>
                                    <span>${detail.spec}</span>
                                </div>
                            </div>
                            <div class="info_item">
                                <div class="info_text">
                                    <span>原产国：</span>
                                    <span>${detail.countryName}</span>
                                </div>
                                <div class="info_text">
                                    <span>工厂源码：</span>
                                    <span>${detail.barcode}</span>
                                </div>
                                <div class="info_text">
                                    <span>保质天数：</span>
                                    <span>${detail.shelfLifeDays}</span>
                                </div>
                            </div>
                            <div class="info_item">
                                <div class="info_text">
                                    <span>货品名称：</span>
                                    <span>${detail.productName}</span>
                                </div>
                            </div>
                        </div>
                        <!--   商品报价  end -->
                        <!-- 货品报价 start -->

                        <div class="title_text">货品报价</div>
                        <div class="edit_box mt20">
                            <div class="edit_item">
                                <input type="hidden" value="${detail.id}" name="pId" id="pId">
                                <label class="edit_label">最大供货量：</label>
                                <input type="text" class="edit_input" name="maximum" id="maximum"
                                       value="${detail.maximum}">
                            </div>
                            <div class="edit_item">
                                <label class="edit_label">报价来源：</label>
                                <select class="edit_input" name="source" id="source">
                                    <option value="">请选择</option>
                                    <option value="1" <c:if test="${detail.source == '1'}"> selected = 'selected'</c:if>>第e仓</option>
                                    <option value="2" <c:if test="${detail.source == '2'}"> selected = 'selected'</c:if>>外部</option>
                                    <option value="3" <c:if test="${detail.source == '3'}"> selected = 'selected'</c:if>>品牌经销</option>
                                </select>
                            </div>
                            <div class="edit_item">
                                <label class="edit_label">最小起订量：</label>
                                <input type="text" class="edit_input" name="minimum" id="minimum"
                                       value="${detail.minimum}">
                            </div>
                        </div>
                        <div class="edit_box">
                            <div class="edit_item">
                                <label class="edit_label">报价币种：</label>
                                <select class="edit_input" name="currency" id="currency">
                                </select>
                                <input id="currency_hidden" type="hidden" value="${detail.currency }">
                            </div>
                            <div class="edit_item">
                                <label class="edit_label">报价模式：</label>
                                <select class="edit_input" name="priceModel" id="priceModel">
                                    <option value="1" <c:if test="${detail.priceModel eq '1' }">selected</c:if>>阶梯报价</option>
                                    <option value="2" <c:if test="${detail.priceModel eq '2' }">selected</c:if>>固定报价</option>
                                </select>
                            </div>
                            <div class="edit_item">
                                <label class="edit_label">备货天数：</label>
                                <input type="text" class="edit_input" name="stockUpDay" id="stockUpDay"
                                       value="${detail.stockUpDay}">
                            </div>
                        </div>
                        <div class="edit_box">
                            <div class="edit_item">
                                <label class="edit_label">报价生效日期：</label>
                                <input type="date" class="edit_input" name="effectiveDate" id="effectiveDate"
                                       value="${effectiveDate}">
                            </div>
                            <div class="edit_item">
                                <label class="edit_label" style="flex: 0.3">至：</label>
                                <input type="date" class="edit_input" name="expiryDate" id="expiryDate"
                                       value="${expiryDate}">
                            </div>
                            <div class="edit_item"></div>
                        </div>
                        <c:choose>
		                    <c:when test="${detail.priceModel eq '1' }">
		                        <div class="form-row ml15" id="ladderDiv">
		                            <div class="col-12">
		                                <div class="card-box">
		                                    <h4 class="header-title m-t-0">阶梯报价</h4>
		                                    <div class="table-responsive">
		                                        <table class="table table-bordered" cellspacing="0" width="100%"
		                                               id="table-list-ladder">
		                                            <thead>
		                                            <tr>
		                                                <th width="200px">剩余效期</th>
		                                                <th>数量下限</th>
		                                                <th>数量上限</th>
		                                                <th>供货下限</th>
		                                                <th>供货上限</th>
		                                                <th>操作</th>
		                                            </tr>
		                                            </thead>
		                                            <tbody>
		                                            <c:forEach items="${detail.itemList}" var="rel" varStatus="relStatus">
		                                                <tr>
		                                                    <input type="hidden" value="${rel.id}" name="id">
		                                                    <td>
		                                                        <select class="form-control" name="residualMaturity">
		                                                        	<option value="最新效期" <c:if
		                                                                    test="${rel.residualMaturity == '最新效期'}"> selected = 'selected'</c:if>>
		                                                                最新效期
		                                                            </option>
		                                                            <option value="低于1/3" <c:if
		                                                                    test="${rel.residualMaturity == '低于1/3'}"> selected = 'selected'</c:if>>
		                                                                低于1/3
		                                                            </option>
		                                                            <option value="低于1/2" <c:if
		                                                                    test="${rel.residualMaturity == '低于1/2'}"> selected = 'selected'</c:if>>
		                                                                低于1/2
		                                                            </option>
		                                                            <option value="高于2/3" <c:if
		                                                                    test="${rel.residualMaturity == '高于2/3'}"> selected = 'selected'</c:if>>
		                                                                高于2/3
		                                                            </option>
		                                                            <option value="高于1/2" <c:if
		                                                                    test="${rel.residualMaturity == '高于1/2'}"> selected = 'selected'</c:if>>
		                                                                高于1/2
		                                                            </option>
		                                                        </select>
		
		                                                    </td>
		                                                    <td>
		                                                        <input type="text" class="form-control" name="minNum"
		                                                               value="${rel.minNum}">
		                                                    </td>
		                                                    <td>
		                                                        <input type="text" class="form-control" name="maxNum"
		                                                               value="${rel.maxNum}">
		                                                    </td>
		                                                    <td>
		                                                        <input type="text" class="form-control" name="supplyMinPrice"
		                                                               value="${rel.supplyMinPrice}">
		                                                    </td>
		                                                    <td>
		                                                        <input type="text" class="form-control" name="supplyMaxPrice"
		                                                               value="${rel.supplyMaxPrice}">
		                                                    </td>
		                                                    <td class="tc nowrap"><i class="fi-plus"
		                                                                             style="cursor:pointer;"></i>&nbsp;&nbsp;<i
		                                                            class="fi-minus" style="cursor:pointer;"
		                                                            onclick="del(this)"></i></td>
		                                                </tr>
		                                            </c:forEach>
		                                            </tbody>
		                                        </table>
		                                    </div>
		                                </div>
		                            </div>
		                        </div>
		                        <div class="form-row ml15" id="setDiv" style="display:none;">
		                            <div class="col-12">
		                                <div class="card-box">
		                                    <h4 class="header-title m-t-0">固定报价</h4>
		                                    <div class="table-responsive">
		                                        <table class="table table-bordered" cellspacing="0" width="100%"
		                                               id="table-list-set">
		                                            <thead>
		                                            <tr>
		                                                <th width="200px">剩余效期</th>
		                                                <th>供货下限</th>
		                                                <th>供货上限</th>
		                                                <th>操作</th>
		                                            </tr>
		                                            </thead>
		                                            <tbody>
		                                            <c:forEach items="${detail.itemList}" var="rel" varStatus="relStatus">
		                                                <tr>
		                                                    <input type="hidden" value="${rel.id}" name="id">
		                                                    <td>
		                                                        <select class="form-control" name="residualMaturity">
		                                                            <option value="最新效期" <c:if
		                                                                    test="${rel.residualMaturity == '最新效期'}"> selected = 'selected'</c:if>>
		                                                                最新效期
		                                                            </option>
		                                                            <option value="低于1/3" <c:if
		                                                                    test="${rel.residualMaturity == '低于1/3'}"> selected = 'selected'</c:if>>
		                                                                低于1/3
		                                                            </option>
		                                                            <option value="低于1/2" <c:if
		                                                                    test="${rel.residualMaturity == '低于1/2'}"> selected = 'selected'</c:if>>
		                                                                低于1/2
		                                                            </option>
		                                                            <option value="高于2/3" <c:if
		                                                                    test="${rel.residualMaturity == '高于2/3'}"> selected = 'selected'</c:if>>
		                                                                高于2/3
		                                                            </option>
		                                                            <option value="高于1/2" <c:if
		                                                                    test="${rel.residualMaturity == '高于1/2'}"> selected = 'selected'</c:if>>
		                                                                高于1/2
		                                                            </option>
		                                                        </select>
		                                                    </td>
		                                                    <td>
		                                                        <input type="text" class="form-control" name="supplyMinPrice"
		                                                               value="${rel.supplyMinPrice}">
		                                                    </td>
		                                                    <td>
		                                                        <input type="text" class="form-control" name="supplyMaxPrice"
		                                                               value="${rel.supplyMaxPrice}">
		                                                    </td>
		                                                    <td class="tc nowrap"><i class="fi-plus"
		                                                                             style="cursor:pointer;"></i>&nbsp;&nbsp;<i
		                                                            class="fi-minus" style="cursor:pointer;"
		                                                            onclick="del(this)"></i></td>
		                                                </tr>
		                                            </c:forEach>
		                                            </tbody>
		                                        </table>
		                                    </div>
		                                </div>
		                            </div>
		                        </div>
		                    </c:when>
		                    <c:when test="${detail.priceModel eq '2' }">
		                    	<div class="form-row ml15" id="ladderDiv" style="display:none;">
		                            <div class="col-12">
		                                <div class="card-box">
		                                    <h4 class="header-title m-t-0">阶梯报价</h4>
		                                    <div class="table-responsive">
		                                        <table class="table table-bordered" cellspacing="0" width="100%"
		                                               id="table-list-ladder">
		                                            <thead>
		                                            <tr>
		                                                <th width="200px">剩余效期</th>
		                                                <th>数量下限</th>
		                                                <th>数量上限</th>
		                                                <th>供货下限</th>
		                                                <th>供货上限</th>
		                                                <th>操作</th>
		                                            </tr>
		                                            </thead>
		                                            <tbody>
		                                            <c:forEach items="${detail.itemList}" var="rel" varStatus="relStatus">
		                                                <tr>
		                                                    <input type="hidden" value="${rel.id}" name="id">
		                                                    <td>
		                                                        <select class="form-control" name="residualMaturity">
		                                                        	<option value="最新效期" <c:if
		                                                                    test="${rel.residualMaturity == '最新效期'}"> selected = 'selected'</c:if>>
		                                                                最新效期
		                                                            </option>
		                                                            <option value="低于1/3" <c:if
		                                                                    test="${rel.residualMaturity == '低于1/3'}"> selected = 'selected'</c:if>>
		                                                                低于1/3
		                                                            </option>
		                                                            <option value="低于1/2" <c:if
		                                                                    test="${rel.residualMaturity == '低于1/2'}"> selected = 'selected'</c:if>>
		                                                                低于1/2
		                                                            </option>
		                                                            <option value="高于2/3" <c:if
		                                                                    test="${rel.residualMaturity == '高于2/3'}"> selected = 'selected'</c:if>>
		                                                                高于2/3
		                                                            </option>
		                                                            <option value="高于1/2" <c:if
		                                                                    test="${rel.residualMaturity == '高于1/2'}"> selected = 'selected'</c:if>>
		                                                                高于1/2
		                                                            </option>
		                                                        </select>
		
		                                                    </td>
		                                                    <td>
		                                                        <input type="text" class="form-control" name="minNum"
		                                                               value="${rel.minNum}">
		                                                    </td>
		                                                    <td>
		                                                        <input type="text" class="form-control" name="maxNum"
		                                                               value="${rel.maxNum}">
		                                                    </td>
		                                                    <td>
		                                                        <input type="text" class="form-control" name="supplyMinPrice"
		                                                               value="${rel.supplyMinPrice}">
		                                                    </td>
		                                                    <td>
		                                                        <input type="text" class="form-control" name="supplyMaxPrice"
		                                                               value="${rel.supplyMaxPrice}">
		                                                    </td>
		                                                    <td class="tc nowrap"><i class="fi-plus"
		                                                                             style="cursor:pointer;"></i>&nbsp;&nbsp;<i
		                                                            class="fi-minus" style="cursor:pointer;"
		                                                            onclick="del(this)"></i></td>
		                                                </tr>
		                                            </c:forEach>
		                                            </tbody>
		                                        </table>
		                                    </div>
		                                </div>
		                            </div>
		                        </div>
		                        <div class="form-row ml15" id="setDiv">
		                            <div class="col-12">
		                                <div class="card-box">
		                                    <h4 class="header-title m-t-0">固定报价</h4>
		                                    <div class="table-responsive">
		                                        <table class="table table-bordered" cellspacing="0" width="100%"
		                                               id="table-list-set">
		                                            <thead>
		                                            <tr>
		                                                <th width="200px">剩余效期</th>
		                                                <th>供货下限</th>
		                                                <th>供货上限</th>
		                                                <th>操作</th>
		                                            </tr>
		                                            </thead>
		                                            <tbody>
		                                            <c:forEach items="${detail.itemList}" var="rel" varStatus="relStatus">
		                                                <tr>
		                                                    <input type="hidden" value="${rel.id}" name="id">
		                                                    <td>
		                                                        <select class="form-control" name="residualMaturity">
		                                                        <option value="最新效期" <c:if
		                                                                    test="${rel.residualMaturity == '最新效期'}"> selected = 'selected'</c:if>>
		                                                                最新效期
		                                                            </option>
		                                                            <option value="低于1/3" <c:if
		                                                                    test="${rel.residualMaturity == '低于1/3'}"> selected = 'selected'</c:if>>
		                                                                低于1/3
		                                                            </option>
		                                                            <option value="低于1/2" <c:if
		                                                                    test="${rel.residualMaturity == '低于1/2'}"> selected = 'selected'</c:if>>
		                                                                低于1/2
		                                                            </option>
		                                                            <option value="高于2/3" <c:if
		                                                                    test="${rel.residualMaturity == '高于2/3'}"> selected = 'selected'</c:if>>
		                                                                高于2/3
		                                                            </option>
		                                                            <option value="高于1/2" <c:if
		                                                                    test="${rel.residualMaturity == '高于1/2'}"> selected = 'selected'</c:if>>
		                                                                高于1/2
		                                                            </option>
		                                                        </select>
		                                                    </td>
		                                                    <td>
		                                                        <input type="text" class="form-control" name="supplyMinPrice"
		                                                               value="${rel.supplyMinPrice}">
		                                                    </td>
		                                                    <td>
		                                                        <input type="text" class="form-control" name="supplyMaxPrice"
		                                                               value="${rel.supplyMaxPrice}">
		                                                    </td>
		                                                    <td class="tc nowrap"><i class="fi-plus"
		                                                                             style="cursor:pointer;"></i>&nbsp;&nbsp;<i
		                                                            class="fi-minus" style="cursor:pointer;"
		                                                            onclick="del(this)"></i></td>
		                                                </tr>
		                                            </c:forEach>
		                                            </tbody>
		                                        </table>
		                                    </div>
		                                </div>
		                            </div>
		                        </div>
		                    </c:when>
		                </c:choose>
                        <!-- 货品报价 end -->
                        <div class="form-row">
                            <div class="form-inline m0auto">
                                <div class=form-group>
                                    <button type="button" class="btn btn-info waves-effect waves-light fr btn_default"
                                            id="save-buttons">保存
                                    </button>
                                    <button type="button" class="btn btn-light waves-effect waves-light ml15 btn_default"
                                            id="cancel-buttons">取消
                                    </button>
                                </div>
                            </div>
                        </div>
                        <!-- end row -->
                    </div>
                </div>
            </div>
        </form>
    </div>
    <!-- container -->
</div>

</div> <!-- content -->
<script src='/resources/js/system/base.js' type="text/javascript"></script>
<script type="text/javascript">
	var index = 0;
    var count = 0;
    var flag = " ${empty itemCount}";
    if (flag != "true") {
        count = ${itemCount};
        index = count;
    }
    
    $module.currency.loadSelectData.call($("select[name='currency']")[0],"");
	//币种赋值
	var currency = $("#currency_hidden").val() ;
	if(currency){
		$("#currency").val(currency) ;
	}
    
    $(function () {
    	
        //取消按钮
        $("#cancel-buttons").click(function () {
            $load.a("/supplierMerchandisePrice/toPage.html");
        });
        
        //报价模式发生变更
        $("#priceModel").change(function(){
        	if($(this).val()=='1'){
        		$("#ladderDiv").css("display","block");
        		$("#setDiv").css("display","none");
        	}else{
        		$("#ladderDiv").css("display","none");
        		$("#setDiv").css("display","block");
        	}
        });

    });
    //保存按钮
    $("#save-buttons").click(function () {
    	
        var url = "/supplierMerchandisePrice/modifySMPrice.asyn";
        var form = {};
        var json = {};
        var sMPriceId = $("#pId").val();
        var maximum = $("#maximum").val().trim();
        
        if(isNotNum(maximum , "最大供货量不为数字！")){
        	return ;
        } 
        
        var source = $("#source").val();
        var minimum = $("#minimum").val().trim();
        
        if(isNotNum(minimum , "最小起订量不为数字！")){
        	return ;
        }
        
        var effectiveDate = $("#effectiveDate").val();
        var expiryDate = $("#expiryDate").val();
        var stockUpDay = $("#stockUpDay").val().trim();
        
        if(isNotNum(stockUpDay , "备货天数不为数字！")){
        	return ;
        }
        
        var currency = $("#currency").val();
        var priceModel = $("#priceModel").val();
        var itemList = [];
        
        var flag = false ;
        
        if($("#priceModel").val()=='1'){
	        $('#table-list-ladder tbody tr').each(function (i) {// 遍历 tr
	            var item = {};
	            var id = $(this).find("input[name='id']").val();
	            item['id'] = id;
	            var residualMaturity = $(this).find('select[name="residualMaturity"]').val();
	            item['residualMaturity'] = residualMaturity;
	            var minNum = $(this).find('input[name="minNum"]').val().trim();
	            
	            if(isNotNum(minNum , "数量下限不为数字！")){
	            	
	            	flag = true ;
	            	
	            	return false;
	            }
	            
	            item['minNum'] = minNum;
	            var maxNum = $(this).find('input[name="maxNum"]').val().trim();
	            
	            if(isNotNum(maxNum , "数量上限不为数字！")){
	            	
	            	flag = true ;
	            	
	            	return false;
	            }
	            
	            item['maxNum'] = maxNum;
	            var supplyMinPrice = $(this).find('input[name="supplyMinPrice"]').val().trim();
	            
	            if(isNotNum(supplyMinPrice , "供货下限不为数字！")){
	            	
	            	flag = true ;
	            	
	            	return false;
	            }
	            
	            item['supplyMinPrice'] = supplyMinPrice;
	            var supplyMaxPrice = $(this).find('input[name="supplyMaxPrice"]').val().trim();
	            
	            if(isNotNum(supplyMaxPrice , "供货上限不为数字！")){
	            	
	            	flag = true ;
	            	
	            	return false;
	            }
	            
	            item['supplyMaxPrice'] = supplyMaxPrice;
	            itemList.push(item);
	        });
        }else{
        	$('#table-list-set tbody tr').each(function (i) {// 遍历 tr
	            var item = {};
	            var id = $(this).find("input[name='id']").val();
	            item['id'] = id;
	            var residualMaturity = $(this).find('select[name="residualMaturity"]').val();
	            item['residualMaturity'] = residualMaturity;
	            item['minNum'] = 0;
	            item['maxNum'] = 0;
	            var supplyMinPrice = $(this).find('input[name="supplyMinPrice"]').val().trim();
	            
				if(isNotNum(supplyMinPrice , "供货下限不为数字！")){
	            	
	            	flag = true ;
	            	
	            	return false;
	            }
	            
	            item['supplyMinPrice'] = supplyMinPrice;
	            var supplyMaxPrice = $(this).find('input[name="supplyMaxPrice"]').val().trim();
	            
				if(isNotNum(supplyMaxPrice , "供货上限不为数字！")){
	            	
	            	flag = true ;
	            	
	            	return false;
	            }
	            
	            item['supplyMaxPrice'] = supplyMaxPrice;
	            itemList.push(item);
	        });
        }
        
        if(flag){
        	return ;
        }
        
        json['sMPriceId'] = sMPriceId;
        json['maximum'] = maximum;
        json['source'] = source;
        json['minimum'] = minimum;
        json['effectiveDate'] = effectiveDate;
        json['priceModel'] = priceModel;
        json['expiryDate'] = expiryDate;
        json['stockUpDay'] = stockUpDay;
        json['currency'] = currency;
        json['itemList'] = itemList;
        var jsonStr = JSON.stringify(json);
        form['json'] = jsonStr;
        $custom.request.ajax(url, form, function (result) {
        	
            if (result.state == '200') {
                $custom.alert.success("编辑成功！");
                setTimeout(function () {
                    $load.a("/supplierMerchandisePrice/toPage.html");
                }, 1000);
            } else {
                $custom.alert.error("编辑失败！");
            }
        });
    });
    
    //增加一条记录
    $("#table-list-ladder").on("click", '.fi-plus', function () {
    	var listArr = "<tr>"
	            + "<input type='hidden' name='id'>"
	            + "<td width='200px'>"
	            + "<select class='form-control' name = 'residualMaturity'>"
	            + "	<option value='最新效期'>最新效期</option>"
	            + "	<option value='低于1/3'>低于1/3</option>"
	            + "	<option value='低于1/2'>低于1/2</option>"
	            + "	<option value='高于2/3'>高于2/3</option>"
	            + "	<option value='高于1/2'>高于1/2</option>"
	            + "</select>"
	            + "</td>"
	            + "<td>"
	            + "	<input type='text' class='form-control' name='minNum'>"
	            + "</td>"
	            + "<td>"
	            + "<input type='text' class='form-control' name='maxNum'>"
	            + "</td>"
	            + "<td>"
	            + "	<input type='text' class='form-control' name='supplyMinPrice'>"
	            + "</td>"
	            + "<td>"
	            + "	<input type='text' class='form-control' name='supplyMaxPrice'>"
	            + "</td>"
	            + "<td class='tc nowrap'><i class='fi-plus' style='cursor:pointer;'></i>&nbsp;&nbsp;<i class='fi-minus' style='cursor:pointer;'onclick='del(this)'></i></td>"
	            + "</tr>";
	        	$("#table-list-ladder").append(listArr);
        count++;
    });
    
  //增加一条记录
    $("#table-list-set").on("click", '.fi-plus', function () {
    	var listArr = "<tr>"
            + "<input type='hidden' name='id'>"
            + "<td width='200px'>"
            + "<select class='form-control' name = 'residualMaturity'>"
            + "	<option value='最新效期'>最新效期</option>"
            + "	<option value='低于1/3'>低于1/3</option>"
            + "	<option value='低于1/2'>低于1/2</option>"
            + "	<option value='高于2/3'>高于2/3</option>"
            + "	<option value='高于1/2'>高于1/2</option>"
            + "</select>"
            + "</td>"
            + "<td>"
            + "	<input type='text' class='form-control' name='supplyMinPrice'>"
            + "</td>"
            + "<td>"
            + "	<input type='text' class='form-control' name='supplyMaxPrice'>"
            + "</td>"
            + "<td class='tc nowrap'><i class='fi-plus' style='cursor:pointer;'></i>&nbsp;&nbsp;<i class='fi-minus' style='cursor:pointer;'onclick='del(this)'></i></td>"
            + "</tr>";
    		$("#table-list-set").append(listArr);
        count++;
    });
    
    //删除一条记录
    function del(obj) {
        if (count - 1 < 1) {
            $custom.alert.warningText("至少保留一条记录！");
            return;
        }
        var obj1 = $(obj).parents("tr").index() + 1;  // 获取所在行的顺序
        if($("#priceModel").val()=='1'){
        	$("#table-list-ladder").find("tr:eq(" + obj1 + ")").remove();
        }else{
        	$("#table-list-set").find("tr:eq(" + obj1 + ")").remove();
        }
        count--;
    }
    
    function isNotNum(val , msg){
    	if(isNaN(val)){
        	$custom.alert.error(msg);
        	return true ;
        }
    	
    	return false ;
    }
</script>
