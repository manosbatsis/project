<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common.jsp" />
<style type="text/css">
	.not-arrow{
		padding: 5px 10px;
		border:1px solid #dcd8d8;
		-webkit-appearance:none;
		-moz-appearance:none;
		appearance:none; /*去掉下拉箭头*/
	}
	.not-arrow::-ms-expand { display: none; }
</style>
<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
        <form id="add-form">
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
							                      <li class="breadcrumb-item"><a href="#">标准成本单价管理</a></li>
							                      <li class="breadcrumb-item"><a href="javascript:$module.load.pageReport('act=13001');">标准成本单价列表</a></li>
							                      <li class="breadcrumb-item"><a href="javascript:$module.load.pageReport('act=13002');">标准成本单价新增</a></li>
							            </ol>
                                    </div>
                                </div>
                                <!--   商品信息  start -->
                                <div class="col-12 ml10">
                                	<div class="row">
				                         <div class="col-10">
				                             <div class="title_text">选择事业部</div>
				                        </div>
				                    </div>
				                    <div class="row">
				                         <div class="col-10 mt10">
				                             <select class="input_msg" name="buId" id="buId">
                                    		 </select>
				                        </div>
				                    </div>
                                </div>
				                <div class="col-12 ml10">
				                    <div class="row">
				                         <div class="col-10">
				                             <div class="title_text">商品信息</div>
				                        </div>
				                         <div class="col-1 mt10">
				                          <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick="pickGoods();">选择商品</button>
				                        </div>
				                         <div class="col-1 mt10">
				                          <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="delete-list-button">删 除</button>
				                        </div> 
				                    </div>
				                 </div>                       
				                 <div class="form-row mt20 ml15">
                                    <table id="table-list" class="table table-striped" cellspacing="0"  width="100%">
                                        <thead>
	                                        <tr>
	                                           <th><input id="checkbox11" type="checkbox" name="all"></th>
					                           <th>条形码</th>
					                           <th>商品名称</th>
					                           <th>品牌</th>
					                           <th>规格</th>
					                           <th><i class="red">*</i> 结算币种</th>
					                           <th><i class="red">*</i> 标准成本单价</th>
					                           <th><i class="red">*</i> 生效年月</th>
					                           <th>是否组合</th>
					                           <th>操作</th>
				                      		</tr>
			                      		</thead>
			                      		<tbody>
			                         	</tbody>
                                    </table>
                                </div>
                                <!--   商品信息  end -->
                                <div class="form-row m-t-50">
                                    <div class="col-12">
                                        <div class="row">
                                            <div class="col-6">
                                                <button type="button" class="btn btn-info waves-effect waves-light fr btn_default" id="save-buttons" >保 存</button>
                                            </div>
                                            <div class="col-6">
                                                <button type="button" class="btn btn-light waves-effect waves-light btn_default" id="cancel-buttons">取 消</button>
                                  			</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- end row -->
                </div>
            </div>
        </form>
        <!-- 弹窗开始 -->
                <jsp:include page="/WEB-INF/views/modals/17011.jsp" />
             
        <!-- 弹窗结束 -->
    </div>
    <!-- container -->
</div>

<script type="text/javascript">
	//事业部
	$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null, null,null);
	
	//选择商品(自定义选择商品的链接)
	function pickGoods(){
		$erpPopup.orderGoods.params.getMerchandisesByPageURL = "/merchandise/getListForSettlment.asyn";
		$erpPopup.orderGoods.init(7, null, null);
	}
	$(function(){
		//取消按钮
		$("#cancel-buttons").click(function(){
			$module.load.pageReport("act=13001");
		});
		//删除商品选中行
	 	$("#delete-list-button").click(function() {
	 		var goodsId = [];
	        $("input[name='item-checkbox']:checked").each(function() { // 遍历选中的checkbox
	            n = $(this).parents("tr").index()+1;  // 获取checkbox所在行的顺序
	            goodsId.push($(this).parent().find("input[name='goodsId']").val());
	            $("#table-list").find("tr:eq("+n+")").remove();
	        });
	        var unNeedIds = $("#unNeedIds").val();
	        var temp = unNeedIds.split(",");
	        var newUnNeedIds = delArrElement(goodsId,temp);
	        $("#unNeedIds").val(newUnNeedIds);
	        $("input[name='all']").prop("checked",false);
	    });
	 	function delArrElement(goodsId,temp){
			for (var i = 0; i < goodsId.length; i++) {
	        	for (var j = 0; j < temp.length; j++) {
					if(goodsId[i] == temp[j]){
						temp.splice(j,1);
						delArrElement(goodsId,temp);
					}
				}
			}
			return temp;
		}
		//全选
		$("input[name='all']").click(function(){
			//判断当前点击的复选框处于什么状态$(this).is(":checked") 返回的是布尔类型
			if($(this).is(":checked")){
				$("input[name='item-checkbox']").prop("checked",true);
			}else{
				$("input[name='item-checkbox']").prop("checked",false);
			}
		});
		//保存
		$("#save-buttons").click(function(){
			
			var url = serverAddr+"/settlementPrice/saveSettlementPrice.asyn";
			var form = {};
			var json = {};
			var itemList = [];
			var flag = true;
			
			var buId = $("#buId").val() ;
			
			if(!buId){
				$custom.alert.error("事业部不能为空");
	    		return;
			}
			
			json['buId']=buId;
			
			$('#table-list tbody tr').each(function(i){// 遍历 tr
				var item ={};
				var goodsId = $(this).find('input[name="goodsId"]').val();
				var goodsNo = $(this).find('input[name="goodsNo"]').val();
				var barcode = $(this).find('input[name="barcode"]').val();
				var goodsName = $(this).find('input[name="goodsName"]').val();
				var brandId = $(this).find('input[name="brandId"]').val();
				console.log(brandId) ;
				var brandName = $(this).find('input[name="brandName"]').val();
				var goodsSpec = $(this).find('input[name="goodsSpec"]').val();
				var currency = $(this).find('select[name="currency"]').val();
				var price = $(this).find('input[name="price"]').val();
				var effectiveDate = $(this).find('input[name="effectiveDate"]').val();
				var startDate = $(this).find('input[name="startDate"]').val();
				var isGroup = $(this).find('input[name="isGroup"]').val();
				var unitId = $(this).find('input[name="unitId"]').val();
				var unitName = $(this).find('input[name="unitName"]').val();
				var productId = $(this).find('input[name="productId"]').val();
				//===============参数校验==========================================
				//价格
				if(!price || price < 0){
					$custom.alert.error("标准成本单价不能为空或为负");
		       		flag = false;
		    		return;
				}
		    	var reg1 = /^\d+(\.\d+)?$/;
		       	if (!reg1.test(price)) {
		       		$custom.alert.error("标准成本单价只能输入数字");
		       		flag = false;
		    		return;
		       	}
		       	if(price.length>20){
		       		$custom.alert.error("标准成本单价长度最多只能输入20位（包含小数点）");
		       		flag = false;
		    		return;
		       	}
		       	if(!effectiveDate){
					$custom.alert.error("生效年月不能为空");
		       		flag = false;
		    		return;
				}
		       	item['goodsId']=goodsId;
		       	item['goodsNo']=goodsNo;
		       	item['barcode']=barcode;
		       	item['goodsName']=goodsName;
		       	item['brandId']=brandId;
		       	item['brandName']=brandName;
		       	item['goodsSpec']=goodsSpec;
		       	item['currency']=currency;
		       	item['price']=price;
		       	item['effectiveDate']=effectiveDate;
		       	item['startDate']=startDate;
		       	item['isGroup']=isGroup;
		       	item['unitId']=unitId;
		       	item['unitName']=unitName;
		        item['productId']=productId; 
				itemList.push(item);
			});
			//商品参数是否通过校验
			if(!flag){
				return;
			}
			if(!itemList || itemList.length==0){
				$custom.alert.error("至少选择一个商品");
				return;
			}
			json['itemList']=itemList;
			var jsonStr= JSON.stringify(json);			
			form['json']=jsonStr;
			$custom.request.ajax(url, form, function(result){
				if(result.state == '200'){
					$custom.alert.success("新增成功！");
					setTimeout(function () {
						$module.load.pageReport("act=13001");
					}, 1000);
				}else{
					if(!!result.expMessage){
						$custom.alert.error("新增失败："+result.expMessage);
					}else{
						$custom.alert.error(result.message);
					}
					
				}
			});
		});
	});
</script>
