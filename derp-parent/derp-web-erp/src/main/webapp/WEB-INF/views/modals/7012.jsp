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
                                      <input type="hidden" class="form-control" id="unNeedIds" name="unNeedIds">
                                  	  <input type="hidden" class="form-control" id="isRecord" name="isRecord">
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
                                  <select class="form-control" name="brandId1" id="brandId1">
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
                          			<button type="button" class="btn btn-info waves-effect waves-light fr" onclick='searchData();'>查询</button>
                          			<button type="reset" class="btn btn-light waves-effect waves-light ml15" >重置</button>
                          		</div>
                          	</div>
                      </div>
                      </form>
                      <div class="form-row mt20">
                   			<table id="datatable-buttons7012" class="table table-striped" cellspacing="0" width="100%">
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
                          			<button type="button" class="btn btn-info waves-effect waves-light fr" onclick='$m7012.save();'>确定</button>
                          			<button type="button" class="btn btn-light waves-effect waves-light ml15"  onclick='$m7012.hide();'>取消</button>
                          		</div>
                          	</div>
                      </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>

<script type="text/javascript">
$module.brand.loadSelectData.call($("select[name='brandId1']")[0], "");
	//储存选择过的商品id
	var unNeedIds = [];
 
	//当前点击对象
	var thisObj=null;
	
    var $m7012={
        init:function(obj){
        	thisObj=obj;
        	var isRecord = $("#isRecord2").val();
        	$("#isRecord").val(isRecord);
			//初始化重选按钮
			$(".group-checkable").prop("checked",false);
			//重置表单
			$($m7012.params.formId)[0].reset();
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
        },
        list:function(){
	        //加载表格
	        $("#datatable-buttons7012 tr:gt(0)").remove();
	        searchData();
        },
        save:function(){
        	var ids=$mytable.fn.getCheckedRow();
        	if(ids!=""){
        		if(ids.indexOf(",")!=-1){
        			$custom.alert.error("目前只支持一次选一个商品");
        		}else{
		        	$custom.request.ajax(
		        		$m7012.params.getListByIdsUrl,{"ids":ids},function(data){
		                	if(data.state==200){
		                        var $tr = "";
		                        $.each(data.data,function(index,event){
		                        	$tr+="<tr>"+
		                            "<td><input type='hidden' name='goodsId' value='"+event.id+"'>"+(event.barcode==null?'':event.barcode)+"</td>"+
		                            "<td>"+(event.goodsNo==null?'':event.goodsNo)+"</td>"+
		                            "<td>"+(event.name==null?'':event.name)+"</td>"+
		                            "<td>"+(event.goodsCode==null?'':event.goodsCode)+"</td>"+
		                            "<td><input type='text' name='group-num' style='width:70px;text-align:center;' class='goods-num' value='1'></td>"+
		                            "<td><input type='text' name='group-price' class='goods-price' style='width:70px;text-align:center;' value='"+event.filingPrice+"'></td>"+
		                            "<td><i class='fi-plus group-fi-plus'></i>&nbsp;&nbsp;<i class='fi-minus group-fi-minus'></i></td>"+
		                            "</tr>";
		                        	unNeedIds.push(event.id);
		                        });
		                        $("#unNeedIds").val(unNeedIds);
		                        $(thisObj).parent("td").parent("tr").replaceWith($tr);
		                    }else{
		                        $custom.alert.error(data.message);
		                    }
		            	}
		        	);
        		}
        	}
        	$($m7012.params.modalId).modal("hide");
        },
        show:function(){
	        this.list();
	        $($m7012.params.modalId).modal("show");
        },
        hide:function(){
            $($m7012.params.modalId).modal("hide");
        },
        params:{
            getListUrl:'/merchandise/getListByMerchant.asyn?r='+Math.random(),
            getListByIdsUrl:'/merchandise/getListByIds.asyn?r='+Math.random(),
            formId:'#add-goods-form',
            modalId:'#signup-modal',
        }
    };
    
    //配置表格参数
    $mytable.params={
        url:$m7012.params.getListUrl,
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
        formid:$m7012.params.formId
    };
    //生成列表信息
    $('#datatable-buttons7012').mydatatables();
    
    /**
     * 全选
     */
    $('#datatable-buttons7012').on("change", ":checkbox", function() {
        // 列表复选框
        if ($(this).is("[name='keeperUserGroup-checkable']")) {
            // 全选
            $(":checkbox", '#datatable-buttons7012').prop("checked",$(this).prop("checked"));
        }else{
            // 一般复选
            var checkbox = $("tbody :checkbox", '#datatable-buttons7012');
            $(":checkbox[name='cb-check-all']", '#datatable-buttons7012').prop('checked', checkbox.length == checkbox.filter(':checked').length);
        }
    });
    
    function searchData(){
        $mytable.fn.reload();
    }
    

</script>