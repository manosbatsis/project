<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style>

    label[class^=col-],div[class^=col-]{padding: 2px 0;}

	.edit_span{
		text-align: right;
		line-height: 30px;
	}

	.box_input{
		width: 38px;
	    height: 30px;
	    border: 1px solid #dadada;
	    text-indent: 5px;
	    margin: 0 10px 0 10px;
	}

</style>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
        <form id="add-form">
            <div class="row">
                <div class="card-box margin table-responsive">
                    <div class="col-12 row">
                        <div>
                            <ol class="breadcrumb">
                                <li><i class="block"></i> 当前位置：</li>
                                <li class="breadcrumb-item"><a href="#">商品档案</a></li>
                                <li class="breadcrumb-item "><a href="javascript:$load.a('/merchandise/toPage.html');">商品列表</a>
                                </li>
                                <li class="breadcrumb-item "><a
                                        href="javascript:$load.a('/merchandise/toMainEditPage.html?id=${detail.id}');">主数据商品编辑</a>
                                </li>
                            </ol>
                        </div>
                    </div>
                    <div class="title_text">商品基础信息</div>
                    <div class="edit_box mt20">
                        <div class="edit_item">
                            <label class="edit_label">所属公司：</label>
                            <div class="no_edit">
                                ${detail.merchantName }
                                <input type="hidden" id="id" name="id" value="${detail.id}">
                                <input type="hidden" value="${groupListSize }" id="groupListSize">
                            </div>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label">商品编码：</label>
                            <div class="no_edit">
                                ${detail.goodsCode }
                            </div>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label">商品名称：</label>
                            <div class="no_edit">
                                ${detail.name }
                            </div>
                        </div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item">
                            <label class="edit_label">商品货号：</label>
                            <div class="no_edit">
                                ${detail.goodsNo }
                            </div>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label red">单价：</label>
                            <input type="text" required="" parsley-type="email" class="edit_input"
                                   name="filingPrice" value="${detail.filingPrice }">
                        </div>
                        <div class="edit_item">
                            <label class="edit_label red">商品描述：</label>
                                <input type="text" required="" parsley-type="email" class="edit_input"
                                       name="describe" value="${detail.describe }">
                        </div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item">
                            <label class="edit_label red">工厂编码：</label>
                            <input type="text" required="" parsley-type="email" class="edit_input"
                                   name="factoryNo" value="${detail.factoryNo }">
                        </div>
                        <div class="edit_item">
                            <label class="edit_label">唯一标识OPG：</label>
                            <div class="no_edit">
                                ${detail.uniques }
                            </div>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label red">备注：</label>
                            <input type="text" required="" parsley-type="email" class="edit_input"
                                   name="remark" value="${detail.remark }">
                        </div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item">
                            <label class="edit_label">包装方式：</label>
                            <div class="no_edit">
                                ${detail.packType }
                            </div>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label">修改人：</label>
                            <div class="no_edit">
                                ${detail.updateName }
                            </div>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label">修改时间：</label>
                            <div class="no_edit">
                                ${modifyDate }
                            </div>
                        </div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item">
                            <label class="edit_label">是否备案：</label>
                            <div class="no_edit">
                                <c:choose>
                                    <c:when test="${detail.isRecord eq '1' }">
    						                                    是
                                    </c:when>
                                    <c:otherwise>
 							                                       否
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label red">是否组合：</label>
                            <select class="edit_input" name="isGroup" id="isGroup">
                                <option value="">请选择</option>
                                <c:choose>
                                    <c:when test="${detail.isGroup eq '1' }">
                                        <option value="1" selected>是</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="1">是</option>
                                    </c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${detail.isGroup != '1' }">
                                        <option value="0" selected>否</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="0">否</option>
                                    </c:otherwise>
                                </c:choose>
                            </select>
                        </div>
                        <div class="edit_item">
                                <label class="edit_label red">一级类目：</label>
                                <select name="productTypeId0" id = "productTypeId0"class="edit_input ">
                                       <option value="">请选择</option>
                                      <c:forEach items="${oneCatBean }" var="productType">
                               	    	<option value="${productType.value }" <c:if test='${productInfo.productTypeId0 eq productType.value}'>selected</c:if>> 

                               	    		${productType.label }
                               	    	</option>
                               	    </c:forEach>
                                  </select>
                        </div>
                    </div>
                    
                    
                    <div class="edit_box">
                    	<div class="edit_item">
                                <label class="edit_label red">二级类目：</label>
                                <select name="productTypeId" id = "productTypeId"class="edit_input ">
                                       <option value="">请选择</option>
                                      <c:forEach items="${twoCatBean }" var="productType">
                               	    	<option value="${productType.value }" 
                               	    		<c:if test="${productInfo.productTypeId == productType.value}"> selected = 'selected'</c:if>>
                               	    		${productType.label }
                               	    	</option>
                               	    </c:forEach>
                                  </select>
                        </div>
                        <div class="edit_item"> </div>
                        <div class="edit_item"> </div>
                        
                    </div>
                    
                    
                    
                    
                    
                    
                    
                    
                </div>
                <!-- 组合 -->
                <div id="group-div" style="display:none;">
                     <div class="card-box margin table-responsive"> 
                        <div class="title_text">组合信息</div>
                        <div class="title_text"></div>
                        <div class="form-row mt20 ml15">
                            <table  id="table-list-group" class="table table-striped"  style="width: 1200px">
                                <tr>
                                    <th>商品条码</th>
                                    <th>商品货号</th>
                                    <th>商品名称</th>
                                    <th>商品编码</th>
                                    <th>单品数量</th>
                                    <th>商品价格</th>
                                    <th>操作</th>
                                </tr>
                                <c:choose>
                                    <c:when test="${groupListSize > 0 }">
                                        <c:forEach items="${groupList }" var="group">
                                            <tr>
						                         <td><input type="hidden" name="needId" value="${group.goodsId }"><input type='hidden' name='goodsId' value='${group.goodsId }'>${group.barcode }</td>
						                         <td>${group.goodsNo }</td>
						                         <td>${group.goodsName }</td>
						                         <td>${group.goodsCode }</td>
						                         <td><input type='text' name='group-num' style='width:70px;text-align:center;' class='goods-num' value='${group.num }'></td>
						                         <td><input type='text' name='group-price' class='goods-price' style='width:70px;text-align:center;' value='${group.price }'></td>
						                         <td><i class="fi-plus group-fi-plus"></i>&nbsp;&nbsp;<i class="fi-minus group-fi-minus"></i></td>
						                      </tr>	
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
					                         <td><button type="button" class="btn btn-add waves-effect waves-light" onclick="$m7012.init(this);">选择商品</button></td>
					                         <td></td>
					                         <td></td>
					                         <td></td>
					                         <td>0</td>
					                         <td>0</td>
					                         <td><i class='fi-plus group-fi-plus'></i>&nbsp;&nbsp;<i class='fi-minus group-fi-minus'></i></td>
					                      </tr>
                                    </c:otherwise>
                                </c:choose>
                            </table>
                        </div>
                   </div>
                </div>
				                
                <div class="card-box margin table-responsive">
                	<div class="title_text">箱规设置</div>
					<div class="form-row mt20 ml15">
					    <div class="edit_item">
					        <label class="edit_label" style="width: 15%">箱转换：</label>
					        <span class="edit_span">1箱 转换为</span>
					        <input type="text" class="box_input" name="boxToUnit" oninput = "value=value.replace(/[^\d]/g,'')"  value="${detail.boxToUnit }">
					        <span class="edit_span">件</span>
					    </div>
					    <div class="edit_item">
					        <label class="edit_label" style="width: 15%">托转换：</label>
					        <span class="edit_span">1托 转换为</span>
					        <input type="text" class="box_input" name="torrToUnit" oninput = "value=value.replace(/[^\d]/g,'')"  value="${detail.torrToUnit }">
					        <span class="edit_span">件</span>
					    </div>
					</div>
                </div>
                <!-- 货品 -->
                <div class="card-box margin table-responsive">
                    <div class="title_text">货品基本信息</div>
                    <div class="info_box">
                        <div class="info_item">
							<div class="info_text">
                                <span>计量单位：</span>
                                <span>
                                    ${detail.unitName }
                                </span>
                            </div>
                            <div class="info_text">
                                <span>原产国：</span>
                                <span>
                                    ${productInfo.countyName }
                                </span>
                            </div>
                            <div class="info_text">
                                <span>条形码：</span>
                                <span>
                                    ${detail.barcode }
                                </span>
                            </div>
                            <div class="info_text">
                                <span>规格型号：</span>
                                <span>
                                    ${productInfo.spec }
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>品牌：</span>
                                <span>
                                    ${productInfo.brandName }
                                </span>
                            </div>
                            <div class="info_text">
                                <span>保质天数：</span>
                                <span>
                                    ${productInfo.shelfLifeDays }&nbsp;天
                                </span>
                            </div>
                            <div class="info_text">
                                <span>毛重：</span>
                                <span>
                                    ${productInfo.grossWeight }&nbsp;kg
                                </span>
                            </div>
                            <div class="info_text">
                                <span>净重：</span>
                                <span>
                                    ${productInfo.netWeight }&nbsp;kg
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>长：</span>
                                <span>
                                    ${productInfo.length }&nbsp;cm
                                </span>
                            </div>
                            <div class="info_text">
                                <span>宽：</span>
                                <span>
                                    ${productInfo.width }&nbsp;cm
                                </span>
                            </div>
                            <div class="info_text">
                                <span>高：</span>
                                <span>
                                    ${productInfo.height }&nbsp;cm
                                </span>
                            </div>
                            <div class="info_text">
                                <span>体积：</span>
                                <span>
                                    ${productInfo.volume }&nbsp;cm³
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>生产厂家：</span>
                                <span>
                                    ${productInfo.manufacturer }
                                </span>
                            </div>
                            <div class="info_text">
                                <span>生产地址：</span>
                                <span>
                                    ${productInfo.manufacturerAddr }
                                </span>
                            </div>
                            <div class="info_text">
                                <span>备注：</span>
                                <span>
                                    ${productInfo.remark }
                                </span>
                            </div>
                             <div class="info_text">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="card-box margin table-responsive">
                    <div class="form-row">
                        <label class="col-1 col-form-label "><span class="fr">商品图片<span>：</span></span></label>
                        <div class="col-11 ml10 line">
                            <div>
                                <img alt="商品图片1" src="/resources/assets/images/bg-1.png" width="150px" height="150px">
                                <img alt="商品图片2" src="/resources/assets/images/bg-1.png" width="150px" height="150px">
                                <img alt="商品图片3" src="/resources/assets/images/bg-1.png" width="150px" height="150px">
                                <img alt="商品图片4" src="/resources/assets/images/bg-1.png" width="150px" height="150px">
                                <img alt="商品图片5" src="/resources/assets/images/bg-1.png" width="150px" height="150px">
                            </div>
                        </div>
                    </div>
                    <div class="form-row m-t-50">
                        <div class="col-12">
                            <div class="row">
                                <div class="col-6">
                                    <button type="button" class="btn btn-info waves-effect waves-light fr"
                                            id="save-buttons">保 存
                                    </button>
                                </div>
                                <div class="col-6">
                                    <button type="button" class="btn btn-light waves-effect waves-light"
                                            id="cancel-buttons">取 消
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
        </form>
        <!-- end row -->
        <jsp:include page="/WEB-INF/views/modals/7012.jsp"/>
    </div>
    <!-- container -->
</div>

</div> <!-- content -->
<script src='/resources/js/system/base.js' type="text/javascript"></script>
<script type="text/javascript">

    $(function () {
    	
    	
    	/* 根据一级分类获取二级分类 */
    	$("#productTypeId0").change(function(){
			$("#productTypeId").find("option").remove();
			$("#productTypeId").append("<option value=''>请选择</option>");
			var productTypeId0=$("#productTypeId0").val();
			var url = "/merchandise/getTwoLevel.asyn";
			var data = {"id":productTypeId0};
			$custom.request.ajax(url, data, function(result){
				var jsonData=result.data;
				var selectObj=$("#productTypeId");
				jsonData.forEach(function(o,i){
	                selectObj.append("<option value='"+ o.value+"'>"+o.label+"</option>");
	            });								
			});						

		});
    	
    	
    	
    	
    	//组合商品储存条数
    	var groupCount = 0;
    	if($("#groupListSize").val()==0){
    		groupCount = 1;
    	}else{
    		groupCount = $("#groupListSize").val();
    	}
    	
        //返回按钮
        $("#cancel-buttons").click(function () {
            $load.a("/merchandise/toPage.html");
        });

        //选择是否组合，为是
        $("#isGroup").change(function () {
            var value = $(this).val();
            if (value == '1') {
                $("#group-div").css("display", "block");
            } else {
                $("#group-div").css("display", "none");
            }
        });

        if ($("#isGroup").val() == '1') {
            $("#group-div").css("display", "block");
        }

        //增加一条组合商品
        $("#table-list-group").on("click", '.group-fi-plus', function () {
        	var listArr = "<tr>"+
			'<td><button type="button" class="btn btn-add waves-effect waves-light" onclick="$m7012.init(this);">选择商品</button></td>'+
            '<td></td>'+
            '<td></td>'+
            '<td></td>'+
            '<td>0</td>'+
            '<td>0</td>'+
            '<td><i class="fi-plus group-fi-plus"></i>&nbsp;&nbsp;<i class="fi-minus group-fi-minus"></i></td>'+
            "</tr>";
            $("#table-list-group").append(listArr);
            groupCount++;
        });

        //删除一条组合商品
        $("#table-list-group").on("click", '.group-fi-minus', function () {
        	var goodsId = [];
        	//判断是否只剩下一个商品
			var obj_group_goods_ids = document.getElementsByName("goodsId");
			if(obj_group_goods_ids.length==1 && groupCount==1){
				var n = $(this).parents("tr").index();  // 获取所在行的顺序
			    $("#table-list-group").find("tr:eq("+n+")").remove();
			    goodsId.push($(this).parents("tr").find("input[name='goodsId']").val());
			    var listArr = "<tr>"+
				'<td><button type="button" class="btn btn-add waves-effect waves-light" onclick="$m7012.init(this);">选择商品</button></td>'+
                '<td></td>'+
                '<td></td>'+
                '<td></td>'+
                '<td>0</td>'+
                '<td>0</td>'+
                '<td><i class="fi-plus group-fi-plus"></i>&nbsp;&nbsp;<i class="fi-minus group-fi-minus"></i></td>'+
                "</tr>";
                $("#table-list-group").append(listArr);
			}else{
				if(groupCount>1){
					var obj = $(this).parents("tr").index();  // 获取所在行的顺序
				    $("#table-list-group").find("tr:eq("+obj+")").remove();
				    goodsId.push($(this).parents("tr").find("input[name='goodsId']").val());
				    groupCount--;
				}
			}
			var unNeedIds = $("#unNeedIds").val();
	        var temp = unNeedIds.split(",");
	        for (var i = 0; i < temp.length; i++) {
	        	for (var j = 0; j < goodsId.length; j++) {
					if(temp[i] == goodsId[j]){
						temp.splice(i,1);
					}
				}
			}
	        $("#unNeedIds").val(temp);
        });

        //保存按钮
        $("#save-buttons").click(function () {
            var url = "/merchandise/modifyMerchandiseMain.asyn";
            var form = $("#add-form").serializeArray();
			if($("#productTypeId0").val()==""){
				$custom.alert.error("一级类目不能为空！");
				return ;
			}
			if($("#productTypeId").val()==""){
				$custom.alert.error("二级类目不能为空！");
				return ;
			}
            if($("#isGroup").val()=="1"){
				var obj_goodsId = document.getElementsByName("goodsId");
				if(obj_goodsId.length==0){
					$custom.alert.error("组合品至少必须选择一个单品！");
					return ;
				}else if(obj_goodsId.length==1){
					if($(".goods-num").val()<=1){
						$custom.alert.error("组合单品仅有一个的情况下，数量必须大于1！");
						return ;
					}
				}
			}
			//数量要大于0
			var num_flag = 0;
			$(".goods-num").each(function(){
				if($(this).val()==0 || $(this).val()==''){
					num_flag = 1;
				}
			});
			if(num_flag==1){
				$custom.alert.error("数量要为大于0的数！");
				return ;
			}
			//价格
			var price_flag = 0;
			$(".goods-price").each(function(){
				if($(this).val()==''){
					price_flag = 1;
				}
			});
			if(price_flag==1){
				$custom.alert.error("价格不能为空，至少为0！");
				return ;
			}	
            //获取组合信息
            var group_goods_ids = "";//储存所有组合单品id
            var obj_group_goods_ids = document.getElementsByName("goodsId");
            for (var i = 0; i < obj_group_goods_ids.length; i++) {
                if (group_goods_ids == "") {
                    group_goods_ids = obj_group_goods_ids[i].value;
                } else {
                    group_goods_ids = group_goods_ids + "," + obj_group_goods_ids[i].value;
                }
            }
            var group_nums = "";//储存所有组合单品的数量
            var obj_group_nums = document.getElementsByName("group-num");
            for (var i = 0; i < obj_group_nums.length; i++) {
                if (group_nums == "") {
                    group_nums = obj_group_nums[i].value;
                } else {
                    group_nums = group_nums + "," + obj_group_nums[i].value;
                }
            }
            var group_prices = "";//储存所有组合单品的价格
            var obj_group_prices = document.getElementsByName("group-price");
            for (var i = 0; i < obj_group_prices.length; i++) {
                if (group_prices == "") {
                    group_prices = obj_group_prices[i].value;
                } else {
                    group_prices = group_prices + "," + obj_group_prices[i].value;
                }
            }
            var data_group_goods_ids = {name: "groupIds", value: group_goods_ids};
            var data_group_nums = {name: "groupNums", value: group_nums};
            var data_group_prices = {name: "groupPrices", value: group_prices};
            form.push(data_group_goods_ids);
            form.push(data_group_nums);
            form.push(data_group_prices);
            $custom.request.ajax(url, form, function (result) {
                if (result.state == '200') {
                    $custom.alert.success("修改成功！");
                    setTimeout(function () {
                        $load.a("/merchandise/toPage.html");
                    }, 1000);
                } else {
                	if(result.expMessage != null){
						$custom.alert.error(result.expMessage);
					}else{
						$custom.alert.error("修改失败！");
					}
                }
            });
        });
    });
</script>
