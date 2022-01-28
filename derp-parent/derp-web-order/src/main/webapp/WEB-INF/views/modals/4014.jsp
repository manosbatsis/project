<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div>
    <!-- Signup modal content -->
    <div id="signup-modal" class="modal fade bs-example-modal-lg" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-dialog  modal-lg">
            <div class="modal-content">
            	<div class="modal-header">
                     <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                     <h5 class="modal-title" id="myLargeModalLabel">选择商品</h5>
                </div>
                <div class="modal-body">
                	<form id="add-goods-form">
                	<div class="form-row">
                          <div class="col-6">
                              <div class="row">
                                  <label  class="col-5 col-form-label " style="white-space:nowrap;"><div class="fr">公司名称<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <input type="text" class="form-control" name="merchantName">
                                      <input type="hidden" class="form-control" id="id" name="depotId">
                                      <input type="hidden" class="form-control" id="id2" name="customerId">
                                      <input type="hidden" class="form-control" id="unNeedIds" name="unNeedIds">
                                      <input type="hidden" class="form-control" name="cloudMerchantId" value="1000013">
                                  </div>
                              </div>
                          </div>
                          <div class="col-6">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">商品名称<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <input type="text" class="form-control" name="name">
                                  </div>
                              </div>
                          </div> 
                      </div>
                      <div class="form-row mt10">
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
                          <div class="col-6">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">商品货号<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                     <input type="text" class="form-control" name="goodsNo">
                                  </div>
                              </div>
                          </div>
                      </div>
                      <div class="form-row mt10">
                      	<div class="col-6">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">商品条码<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                  	<input type="text" class="form-control" name="barcode">
                                  </div>
                              </div>
                          </div>
                          <div class="col-6">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">工厂编码<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                     <input type="text" class="form-control" name="factoryNo">
                                  </div>
                              </div>
                          </div>
                      </div>
                      <div class="form-row mt20">
                      	<div class="form-inline m0auto">
                          		<div class=form-group>
                          			<button type="button" class="btn btn-info waves-effect waves-light fr" onclick='searchData();'>查询</button>
                          			<button type="reset" class="btn btn-light waves-effect waves-light ml15" >重置</button>
                          		</div>
                          	</div>
                      </div>
                      </form>
                      <div class="form-row mt20">
                   			<table id="datatable-buttons" class="table table-bordered" cellspacing="0" width="100%">
			                        <thead>
			                        <tr>
			                            <th>
			                                <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
			                                    <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
			                                    <span>全选</span>
			                                </label>
			                            </th>
			                            <th>公司名称</th>
			                            <th>商品名称</th>
			                            <th>商品货号</th>
			                            <th>品牌名称</th>
			                            <th>商品条形码</th>
			                            <th>工厂编码</th>
			                            <th>计量单位</th>
                                        <th>平台备案关区</th>
			                        </tr>
			                        </thead>
			                    </table>
                      </div>
                      <div class="form-row mt20">
                      	<div class="form-inline m0auto">
                          		<div class=form-group>
                          			<button type="button" class="btn btn-info waves-effect waves-light fr" onclick='$m4011.save();'>确定</button>
                          			<button type="button" class="btn btn-light waves-effect waves-light ml15"  onclick='$m4011.hide();'>取消</button>
                          		</div>
                          	</div>
                      </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>

<script type="text/javascript">

	$("button[type='reset']").click(function(){
		$("#add-goods-form").find(".goods_select2").each(function(){
	    	$(this).val("");
	    });
		$("#add-goods-form").find(".goods_select2").select2({
			language:"zh-CN",
			placeholder: '请选择',
			allowClear:true
		});
	});
	//储存选择过的商品id
	var unNeedIds = [];
	
    var $m4011={
        init:function(){
	        	//先选择仓库
	        	var depotId = $("#depotId").val();
				if(depotId==null || depotId==""){
					$custom.alert.error("请先选择仓库！");
				}else{
					//初始化重选按钮
					$(".group-checkable").prop("checked",false);
			        //重置表单
			        $($m4011.params.formId)[0].reset();
			        var unIds = $("#unNeedIds").val();
			    	if(unIds==null || unIds==""){
			    		unNeedIds = [];
			    	}else{
			    		unNeedIds = unIds.split(",");
			    	}
			    	var obj_ids = document.getElementsByName("goodsId");
			    	for(var i = 0; i < obj_ids.length; i++){
			    		unNeedIds.push(obj_ids[i].value);
			    	}
			        $("#id").val(depotId);
			        var unNeedIds2 = [];
			        var unNeedIds_temp = "";
			        if(unNeedIds!=[]){
			        	for (var i = 0; i < unNeedIds.length; i++) {
			        		if(unNeedIds2.indexOf(unNeedIds[i])==-1){
			        			unNeedIds2.push(unNeedIds[i]);
			        		}
						}
			        	for (var i = 0; i < unNeedIds2.length; i++) {
			        		if(unNeedIds_temp == ""){
			        			unNeedIds_temp = unNeedIds2[i];
							}else{
								unNeedIds_temp = unNeedIds_temp + "," + unNeedIds2[i];
							}
			        	}
			        }
			        $("#unNeedIds").val(unNeedIds_temp);
			        this.show();
				}
        },
        list:function(){
	        //加载表格
	        $("#datatable-buttons tr:gt(0)").remove();
	        $module.brand.loadSelectData.call($("select[name='brandId']")[0],"");
	        //重新加载select2
    		$(".goods_select2").select2({
    			language:"zh-CN",
    			placeholder: '请选择',
    			allowClear:true
    		});
	        searchData();
        },
        save:function(){
        	var ids=$mytable.fn.getCheckedRow();
        	if(ids!=""){
	        	$custom.request.ajax(
	        		$m4011.params.getListByIdsUrl,{"ids":ids},function(data){
	                	if(data.state==200){
	                        var $tr = "";
	                        $.each(data.data,function(index,event){
	                        	$tr+="<tr><td><input type='checkbox' name='item-checkbox'><input type='hidden' name='goodsId' value='"+event.id+"'></td>"+
	                        	"<td><input type='text' name='contractNos' class='goods-contractNos' style='width:70px;text-align:center;' value=' '></td>"+
	                            "<td>"+(event.name==null?'':event.name)+"</td>"+
	                            "<td>"+(event.barcode==null?'':event.barcode)+"</td>"+
	                            "<td>"+(event.goodsNo==null?'':event.goodsNo)+"</td>"+
	                            "<td><input type='text' name='num' style='width:70px;text-align:center;' class='goods-num' value='1'></td>"+
	                            "<td><input type='text' name='price' class='goods-price' style='width:70px;text-align:center;' value='"+(event.supplyMinPrice==null?0:event.supplyMinPrice)+"'></td>"+
	                            "<td><input type='text' name='goods-amount' class='goods-amount' style='width:70px;text-align:center;' value='0'></td>"+
	                            "<td><select class='goods-unit' style='width:70px;height:25px;' name='purchase-unit'><option value=' '>请选择</option><option value='00'>托盘</option><option value='01'>箱</option><option value='02'>件</option><option value='03'>KG</option></select></td>"+
	                            "<td><input type='hidden' name='brandName' value='"+(event.brandName==null?' ':event.brandName)+"'>"+(event.brandName==null?'':event.brandName)+"</td>"+
	                            "<td><input type='text' name='grossWeight' class='goods-grossWeight' style='width:70px;text-align:center;' value='"+(event.grossWeight==null?0:event.grossWeight)+"'></td>"+
	                            "<td><input type='text' name='netWeight' class='goods-netWeight' style='width:70px;text-align:center;' value='"+(event.netWeight==null?0:event.netWeight)+"'></td>"+
	                            "<td><input type='text' name='boxNo' class='goods-boxNo' style='width:70px;text-align:center;' value=' '></td>"+
	                            "<td><input type='text' name='batchNo' class='goods-batchNo' style='width:70px;text-align:center;' value=' '></td>"+
	                            "<td><input type='text' name='constituentRatio' class='goods-constituentRatio' style='width:70px;text-align:center;' value='0'></td>"+
	                            "</tr>";
	                        	unNeedIds.push(event.id);
	                        });
	                        $("#unNeedIds").val(unNeedIds);
	                        $("#table-list").append($tr); 
	                    }else{
	                        $custom.alert.error(data.message);
	                    }
	            	}
	        	);
        	}
        	$($m4011.params.modalId).modal("hide");
        },
        show:function(){
	        this.list();
	        $($m4011.params.modalId).modal("show");
        },
        hide:function(){
            $($m4011.params.modalId).modal("hide");
        },
        params:{
            getListUrl:'/merchandise/getList.asyn?r='+Math.random(),
            getListByIdsUrl:'/merchandise/getListByIds.asyn?r='+Math.random(),
            formId:'#add-goods-form',
            modalId:'#signup-modal',
        }
    }
    
    //配置表格参数
    $mytable.params={
        url:$m4011.params.getListUrl,
        columns:[{ //复选框单元格
            className: "td-checkbox",
            orderable: false,
            width: "30px",
            data: null,
            render: function (data, type, row, meta) {
                return '<input type="checkbox" class="iCheck">';
            }
        },
        {data:'merchantName'},{data:'name'},{data:'goodsNo'},{data:'brandName'},{data:'barcode'},{data:'factoryNo'},{data:'unitName'},
        ],
        formid:$m4011.params.formId
    }
    $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
    };
    //生成列表信息
    $('#datatable-buttons').mydatatables();
    
    /**
     * 全选
     */
    $('#datatable-buttons').on("change", ":checkbox", function() {
        // 列表复选框
        if ($(this).is("[name='keeperUserGroup-checkable']")) {
            // 全选
            $(":checkbox", '#datatable-buttons').prop("checked",$(this).prop("checked"));
        }else{
            // 一般复选
            var checkbox = $("tbody :checkbox", '#datatable-buttons');
            $(":checkbox[name='cb-check-all']", '#datatable-buttons').prop('checked', checkbox.length == checkbox.filter(':checked').length);
        }
    });
    
    function searchData(){
        $mytable.fn.reload();
    }
    

</script>