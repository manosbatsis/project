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
                                  <label  class="col-5 col-form-label " style="white-space:nowrap;"><div class="fr">商品编号<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <input type="text" class="form-control" name="goodsCode">
                                      <input type="hidden" class="form-control" id="id" name="depotId">
                                      <input type="hidden" class="form-control" id="id2" name="customerId">
                                      <input type="hidden" class="form-control" id="unNeedIds" name="unNeedIds">
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
                                  <select class="form-control" name="brandId">
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
                      <div class="form-row mt20">
                      	<div class="form-inline m0auto">
                          		<div class=form-group>
                          			<button type="button" class="btn btn-info waves-effect waves-light fr" >查询</button>
                          			<button type="button" class="btn btn-light waves-effect waves-light ml15" >重置</button>
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
			                            <th>商品编号</th>
			                            <th>商品名称</th>
			                            <th>商品货号</th>
			                            <th>品牌名称</th>
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
	//储存选择过的商品id
	var unNeedIds = [];
	
    var $m4011={
        init:function(){
        	//先选择供应商
        	var supplierId = $("#supplierId").val();
			if(supplierId==null || supplierId==""){
				$custom.alert.error("请先选择供应商！");
			}else{
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
			        $("#id2").val(supplierId);
			        var unNeedIds_temp = "";
			        if(unNeedIds!=[]){
			        	for (var i = 0; i < unNeedIds.length; i++) {
			        		if(unNeedIds_temp == ""){
			        			unNeedIds_temp = unNeedIds[i];
							}else{
								unNeedIds_temp = unNeedIds_temp + "," + unNeedIds[i];
							}
						}
			        }
			        $("#unNeedIds").val(unNeedIds_temp);
			        this.show();
				}
			}
        },
        list:function(){
	        //加载表格
	        $("#datatable-buttons tr:gt(0)").remove();
	        searchData();
        },
        save:function(){
        	var ids=$mytable.fn.getCheckedRow();
        	$custom.request.ajax(
        		$m4011.params.getListByIdsUrl,{"ids":ids},function(data){
                	if(data.state==200){
                        var $tr = "";
                        $.each(data.data,function(index,event){
                        	$tr+="<tr><td style='text-align:center'><input type='checkbox' name='item-checkbox'><input type='hidden' name='goodsId' value='"+event.id+"'></td>"+
                            "<td style='text-align:center'><input type='text' name='contractNos' style='width:70px;text-align:center;' value=' '></td>"+
                            "<td style='text-align:center'>"+(event.name==null?'':event.name)+"</td>"+
                            "<td style='text-align:center'>"+(event.barcode==null?'':event.barcode)+"</td>"+
                            "<td style='text-align:center'>"+(event.goodsNo==null?'':event.goodsNo)+"</td>"+
                            "<td style='text-align:center'><input type='text' name='num' style='width:70px;text-align:center;' class='goods-num' value='0'></td>"+
                            "<td style='text-align:center'><input type='text' name='price' class='goods-price' style='width:70px;text-align:center;' value='"+(event.supplyMinPrice==null?0:event.supplyMinPrice)+"'></td>"+
                            "<td style='text-align:center'>0</td>"+
                            "<td style='text-align:center'>"+(event.brandName==null?'':event.brandName)+"</td>"+
                            "<td style='text-align:center'>"+(event.grossWeight==null?'':event.grossWeight)+"</td>"+
                            "<td style='text-align:center'>"+(event.netWeight==null?'':event.netWeight)+"</td>"+
                            "<td style='text-align:center'><input type='text' name='boxNo' style='width:70px;text-align:center;' value=' '></td>"+
                            "<td style='text-align:center'><input type='text' name='batchNo' style='width:70px;text-align:center;' value=' '></td>"+
                            "<td style='text-align:center'><input type='text' name='constituentRatio' style='width:70px;text-align:center;' value=' '></td>"+
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
        {data:'goodsCode'},{data:'name'},{data:'goodsNo'},{data:'brandName'},
        ],
        formid:$m4011.params.formId
    }
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