<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<style>
    .select2-container{border: 1px solid #dadada;}
</style>
<div>
    <!-- Signup modal content -->
    <div id="popup-signup-modal" class="modal fade bs-example-modal-lg" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-dialog  modal-lg">
            <div class="modal-content">
            	<div class="modal-header">
                     <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                     <h5 class="modal-title" id="myLargeModalLabel">选择商品</h5>
                </div>
                <div class="modal-body">
                	<form id="popup-goods-form">
                	<div class="form-row">
                          <input type="hidden" class="form-control" id="unNeedIds" name="unNeedIds">
                          <div class="col-6">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">商品名称<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <input type="text" class="form-control" name="name">
                                  </div>
                              </div>
                          </div> 
                          <div class="col-6">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">商品条码<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <input type="text" class="form-control" name="barcode">
                                  </div>
                              </div>
                          </div>
                      </div>
                      <div class="form-row mt10">
                          <div class="col-6">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">商品货号<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                     <input type="text" class="form-control" name="goodsNo" id="goodsNo">
                                  </div>
                              </div>
                          </div>
                      	  <div class="col-6">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">商品品牌<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                  <select class="form-control goods_select2" name="brandId" id="brandId">
                                     	<option value="">请选择</option>
                                     </select>
                                  </div>
                              </div>
                          </div>
                      </div>
                      <div class="form-row mt20">
                      	<div class="form-inline m0auto">
                          		<div class=form-group>
                          			<button type="button" class="btn btn-info waves-effect waves-light fr" onclick='$popupGoods.orderGoods.reloadTable();'>查询</button>
                          			<button type="reset" class="btn btn-light waves-effect waves-light ml15" >重置</button>
                          		</div>
                          	</div>
                      </div>
                      </form>
                      <div class="form-row mt20">
                   			<table id="popup-datatable-buttons" class="table table-bordered" cellspacing="0" width="100%">
			                        <thead>
			                        <tr>
			                            <th>
			                                <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
			                                    <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
			                                    <span>全选</span>
			                                </label>
			                            </th>
			                            <th>商品货号</th>
			                            <th>商品条码</th>
			                            <th>商品名称</th>
			                            <th>品牌名称</th>
			                        </tr>
			                        </thead>
			                    </table>
			                    <div style="display: block;float: right;width: 100%;">
				                    <div class="page_txt" style="display: inline-block;float: left;line-height: 38px;"></div>
				                    <div class="pageUtils" style="display: inline-block;float: right;"></div>
				                </div>
                      </div>
                      <div class="form-row mt20">
                      	<div class="form-inline m0auto">
                          		<div class=form-group>
                          			<button type="button" class="btn btn-info waves-effect waves-light fr" onclick='$popupGoods.save();'>确定</button>
                          			<button type="button" class="btn btn-light waves-effect waves-light ml15"  onclick='$popupGoods.orderGoods.hide();'>取消</button>
                          		</div>
                          	</div>
                      </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>

<script type="text/javascript">
	
	var thisObj ;
	if("${unNeedIds}"){
		$("#unNeedIds").val('${unNeedIds}');
	}
	//重置按钮
	$("button[type='reset']").click(function(){
		$("#popup-goods-form").find(".goods_select2").each(function(){
	    	$(this).val("");
	    });
		$("#popup-goods-form").find(".goods_select2").select2({
			language:"zh-CN",
			placeholder: '请选择',
			allowClear:true
		});
	});
	
	$module.brand.loadSelectData.call($("select[name='brandId']")[0], "");
	
	//sava方法
    var $popupGoods={
        save:function(){
        	var checkIds = document.getElementsByName("checkId");
        	var ids = "";
        	var i = 0;
        	for(k in checkIds){
                if(checkIds[k].checked){
                	if(i==0){
                		ids = checkIds[k].value;
                		i=1;
                	}else{
                		ids = ids + "," + checkIds[k].value;
                	}
                }
            }
        	
        	if(ids){
        		var resultJson = $erpPopup.merchandise.loadMerchandisesByIds(ids);
	            var tr = "";
	            var str = $("#unNeedIds").val();
	            var unNeedIds = str.split(",");
	            $.each(resultJson,function(index,event){
	            	
	            	var originCountry = "" ;
	            	var qualityGuaranteeDates = "" ;
	            	var spec = "" ;
	            	
	            	$custom.request.ajaxSync(serverAddr+"/purchase/getGoodsProductInfo.asyn", {"goodsId":event.id}, function(data){
	            		if(data.data.originCountry){
	            			originCountry = data.data.originCountry ;
	            		}
	            		
	            		if(data.data.qualityGuaranteeDates){
	            			qualityGuaranteeDates = data.data.qualityGuaranteeDates ;
	            		}
	            		
	            		if(data.data.spec){
	            			spec = data.data.spec ;
	            		}
	            	});
	            	
	            	tr+='<tr>' +
		   	         '<td class="tc nowrap"><input type="checkbox" name="item-checkbox"><input type="hidden" name="goodsId" value="'+event.id+'"></td>' +
		   	         '<td>'+(event.goodsNo==null?'':event.goodsNo)+'</td>'+ 
		   	         '<td>'+(event.name==null?'':event.name)+'</td>' +
		   	         '<td>'+(event.barcode==null?'':event.barcode)+'</td>' +
		   	         '<td>' + originCountry + '</td>' +
		   	         '<td>' + qualityGuaranteeDates + '</td>' +
		   	         '<td><input type="text" class="purchaseQuantity" value="" onblur="calcPurchasePrice(this)" required reqMsg="采购数量不能为空" onkeyup="value=value.replace(/[^\\\d]/g, \"\")"></td>' +
		   	         '<td class="purchasePrice"></td>' +
		   	         '<td><input type="text" class="purchaseAmount" value="" onblur="calcPurchasePrice(this)" required reqMsg="采购金额不能为空" onkeyup="value=value.replace(/[^\\\d^\\\.]/g, \"\") ; amountVal(this, \"6\")"></td>' +
		   	      	 '<td>' + spec + '</td>' +
		   	         '</tr>'
	               unNeedIds.push(event.id);
	            });
	            $("#unNeedIds").val(unNeedIds);
	            
	           	if($(".total")){
	           		$(".total").remove() ;
	           	}
	           	
	           	tr += "<tr class='total'>" ;
	           	tr += "<td style='text-align: center;' colspan='6'>合计：</td>" ;
	           	tr += "<td class='totalNum'></td>" ;
	           	tr += "<td></td>" ;
	           	tr += "<td class='totalAmount'></td>" ;
	           	tr += "<td></td>" ;
	           	tr += "</tr>" ;
	            
	            $("#itemTable").append(tr);
        	}
        	$($popupGoods.orderGoods.params.modalId).modal("hide");
        },
        orderGoods:{
            params:{
                formId:'#popup-goods-form',
                modalId:'#popup-signup-modal',
                tableId:'#popup-datatable-buttons',
            },
            init:function() {
                // 初始化重选按钮
                $(".group-checkable").prop("checked", false);
                var unIds = $("#unNeedIds").val();
                if (unIds == null || unIds == "") {
                    unNeedIds = [];
                } else {
                    unNeedIds = unIds.split(",");
                }
                var obj_ids = document.getElementsByName("goodsId");
                for(var i = 0; i < obj_ids.length; i++){
                    unNeedIds.push(obj_ids[i].value);
                }
                var unNeedIds_temp = "";
                if (unNeedIds != []) {
                    for (var i = 0; i < unNeedIds.length; i++) {
                        if (unNeedIds_temp == "") {
                            unNeedIds_temp = unNeedIds[i];
                        } else {
                            unNeedIds_temp = unNeedIds_temp + "," + unNeedIds[i];
                        }
                    }
                }
                $("#unNeedIds").val(unNeedIds_temp);
                        
                $popupGoods.orderGoods.show();
            },
            list:function() {
                // 重新加载select2
                $(".goods_select2").select2({
                    language:"zh-CN",
                    placeholder:'请选择',
                    allowClear:true
                });
                $popupGoods.orderGoods.loadTable(1, 10);
            },
            // 加载表格
            loadTable:function(pageNo, pageSize) {
                $($popupGoods.orderGoods.params.tableId + ' tr:gt(0)').remove();
                var form = $($popupGoods.orderGoods.params.formId).serializeArray();
                // 当前页，每页显示条数
                var data_pageNo = {
                    name:"begin",
                    value:(pageNo-1)*pageSize
                };
                var data_pageSize = {
                    name:"pageSize",
                    value:pageSize
                };
                form.push(data_pageNo);
                form.push(data_pageSize);
                var resultJson = $erpPopup.merchandise.loadOrderMerchandisesByPage(form);

                var $tr = "";
                //渲染表格
                $.each(resultJson.list,function(i,e){
 					$tr += "<tr>"+
	                     "<td><input type='checkbox' class='iCheck' name='checkId' value="+(e.id==null?'':e.id)+"></td>"+
	                     "<td>"+(e.goodsNo==null?'':e.goodsNo)+"</td>"+
	                     "<td>"+(e.barcode==null?'':e.barcode)+"</td>"+
	                     "<td>"+(e.name==null?'':e.name)+"</td>"+
	                     "<td>"+(e.brandName==null?'':e.brandName)+"</td>"+
	                     "</tr>";
                });
                $($popupGoods.orderGoods.params.tableId).append($tr);
                var begin = resultJson.list.length > 0 ? resultJson.begin:0;
                var end = resultJson.list.length > 0 ? resultJson.end:0;
                var total = resultJson.list.length > 0 ? resultJson.total:0;
                $('.page_txt').html("当前显示第 " + (begin + 1) + " 至 " + end + " 项，共 " + total + " 项");
                $popupGoods.orderGoods.loadPageUtils(resultJson);
            },
            // 加载分页插件
            loadPageUtils:function(resultJson) {
                $('.pageUtils').pagination({
                    totalData:resultJson.list.length > 0 ? resultJson.total:1, // 数据总条数
                    showData:resultJson.list.length > 0 ? resultJson.pageSize:1, // 每页显示的条数
                    current:resultJson.list.length > 0 ? resultJson.pageNo:1, // 当前第几页
                    coping:true, // 开启首页和尾页
                    homePage:'首页', // 首页节点内容
                    endPage:'末页', // 末页节点内容
                    prevContent:'上页', // 上一页内容
                    nextContent:'下页', // 下一页内容
                    jump:false, // 关闭跳转到指定页数
                    callback:function(api) { // 回调方法
                        $popupGoods.orderGoods.loadTable(api.getCurrent(), resultJson.pageSize); // 重新加载页面表格数据
                    }
                });
            },
            reloadTable:function(){
                $popupGoods.orderGoods.loadTable(1,10);
            },
            show:function() {
                $popupGoods.orderGoods.list();
                $($popupGoods.orderGoods.params.modalId).modal("show");
            },
            hide:function() {
                $($popupGoods.orderGoods.params.modalId).modal("hide");
                //将自定义选商品的链接还原，否则会影响其他页面的商品选择
                $popupGoods.orderGoods.params.getMerchandisesByPageURL = "/merchandise/getOrderPopupList.asyn";
            },
    	}
    }
    
    //全选
    $('#popup-datatable-buttons').on("change", ":checkbox", function() {
        // 列表复选框
        if ($(this).is("[name='keeperUserGroup-checkable']")) {
            // 全选
            $(":checkbox", '#popup-datatable-buttons').prop("checked",$(this).prop("checked"));
        }else{
            // 一般复选
            var checkbox = $("tbody :checkbox", '#popup-datatable-buttons');
            $(":checkbox[name='cb-check-all']", '#popup-datatable-buttons').prop('checked', checkbox.length == checkbox.filter(':checked').length);
        }
    });
	
	
</script>