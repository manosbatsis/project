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
                                      <input type="hidden" class="form-control" id="id2" name="merchantId">
                                      <input type="hidden" class="form-control" id="id" name="outDepotId">
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
                          <div class="col-6">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">商品条形码<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                     <input type="text" class="form-control" name="barcode">
                                  </div>
                              </div>
                          </div>
                          <div class="col-6">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">工厂源码<span>：</span></div></label>
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
                   			<table id="datatable-buttons2" class="table table-bordered" cellspacing="0" width="100%">
			                        <thead>
			                        <tr>
			                            <th>
			                                <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
			                                    <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
			                                    <span></span>
			                                </label>
		                           		 </th>
			                            <th>商品条形码</th>
			                            <th>商品名称</th>
			                            <th>计量单位</th>
			                            <th>商品货号</th>
			                            <th>品牌名称</th>
			                            <th>工厂源码</th>
			                            <th>公司名称</th>
			                        </tr>
			                        </thead>
			                    </table>
                      </div>
                      <div class="form-row mt20">
                      	<div class="form-inline m0auto">
                          		<div class=form-group>
                          			<button type="button" class="btn btn-info waves-effect waves-light fr" onclick='$m9011.save();'>确定</button>
                          			<button type="button" class="btn btn-light waves-effect waves-light ml15"  onclick='$m9011.hide();'>取消</button>
                          		</div>
                          	</div>
                      </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>

<script type="text/javascript">
	$module.brand.loadSelectData.call($("select[name='brandId']")[0],"");
	//储存选择过的商品id
	var unNeedIds = [];
    var $m9011={
        init:function(){
        	var outDepotId = $("#outDepotId").val();
        	var merchantId = $('#merchantId').val();
			//初始化重选按钮
			$(".group-checkable").prop("checked",false);
	        //重置表单
	        $($m9011.params.formId)[0].reset();
	        var unIds = $("#unNeedIds").val();
	    	if(unIds==null || unIds==""){
	    		unNeedIds = [];
	    	}else{
	    		unNeedIds = unIds.split(",");
	    	}
	        $("#id2").val(merchantId);
	        $("#id").val(outDepotId);
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
        },
        list:function(){
	        //加载表格
	        $("#datatable-buttons2 tr:gt(0)").remove();
	        searchData();
        },
        save:function(){
        	var ids=$mytable.fn.getCheckedRow();
        	if(ids==null||ids==''||ids==undefined){
				$custom.alert.warningText("至少选择一条记录！");
				return ;
			}
        	$custom.request.ajax(
        		$m9011.params.getListByIdsUrl,{"ids":ids},function(data){
                	if(data.state==200){
                        var $tr = "";
                        $.each(data.data,function(index,event){
                        	$tr+="<tr><td style='text-align:center'><input type='checkbox' name='item-checkbox'><input type='hidden' name='goodsId' value='"+event.id+"'/></td>"+
                            "<td style='text-align:center'>"+(event.goodsCode==null?'':event.goodsCode)+"<input type='hidden' name='goodsCode' value='"+(event.goodsCode==null?'':event.goodsCode)+"'/></td>"+
                            "<td style='text-align:center'>"+(event.name==null?'':event.name)+"<input type='hidden' name='goodsName' value='"+(event.name==null?'':event.name)+"'/></td>"+
                            "<td style='text-align:center'>"+(event.goodsNo==null?'':event.goodsNo)+"<input type='hidden' name='goodsNo' value='"+(event.goodsNo==null?'':event.goodsNo)+"'/></td>"+
                            "<td style='text-align:center'><input type='hidden' name='unitName' value='"+(event.unitName==null?'':event.unitName)+"'/><input type='hidden' name='unit' value='"+(event.unit==null?'':event.unit)+"'/>"+
                            "<select class='goods-unit' style='width:70px;height:25px;' name='tallyingUnit' id='tallyingUnit'><option value=' '>请选择</option><option value='00'>托盘</option><option value='01'>箱</option><option value='02'>件</option><option value='03'>KG</option></select>"+
                            "</td>"+
                            "<td style='text-align:center'>"+(event.barcode==null?'':event.barcode)+"<input type='hidden' name='barcode' value='"+(event.barcode==null?'':event.barcode)+"'/></td>"+
                            "<td style='text-align:center'><input type='text' name='num' style='width:70px;text-align:center;' class='goods-num' value='0'></td>"+
                            "<td style='text-align:center'><input type='text' name='price' class='goods-price' style='width:70px;text-align:center;' value='0'></td>"+
                            "<td style='text-align:center' name='amount'>0</td>"+
                            "<td style='text-align:center'>"+(event.brandName==null?'':event.brandName)+"<input type='hidden' name='brandName' value='"+(event.brandName==null?'':event.brandName)+"'/></td>"+
                            "<td style='text-align:center'><input type='text' name='grossWeight' value='"+(event.grossWeight==null?'':event.grossWeight)+"'/></td>"+
                            "<td style='text-align:center'><input type='text' name='netWeight' value='"+(event.netWeight==null?'':event.netWeight)+"'/></td>"+
                            "<td style='text-align:center'><input type='text' name='boxNo' value=''/></td>"+
                            "<td style='text-align:center'><input type='text' name='itemContractNo' value=''/></td>"+
                            "</tr>";
                        	unNeedIds.push(event.id);
                        });
                        $("#unNeedIds").val(unNeedIds);
                        $("#table-list").append($tr); 
                      	//监听采购价格的离开事件-添加进去的需要重新设置监听
                		$("input[name='num']").bind('input porpertychange',function(){
                			var that = $(this);
                	    	//获取采购数量
                	    	var num = that.val();
                	    	var tr = that.parent().parent();
                	    	//获取当前价格
                	    	var price= tr.find("input[name='price']").val();
                	    	//设置总金额
                	    	var sum = 0;
                	    	if(!!num && !!price){
	                	    	sum = Number((price*num).toFixed(3));
                	    	}
                	    	tr.find("td[name='amount']").html(sum);
                	    });
                		$("input[name='price']").bind('input porpertychange',function(){
                			var that = $(this);
                			//获取当前价格
                	    	var price= that.val();
                	    	var tr = that.parent().parent();
                	    	//获取采购数量
                	    	var num = tr.find("input[name='num']").val();
                	    	//设置总金额
                	    	var sum = 0;
                	    	if(!!num && !!price){
	                	    	sum = Number((price*num).toFixed(3));
                	    	}
                	    	tr.find("td[name='amount']").html(sum);
                	    });
                    }else{
                        $custom.alert.error(data.message);
                    }
            	}
        	);
        	$($m9011.params.modalId).modal("hide");
        },
        show:function(){
	        this.list();
	        $($m9011.params.modalId).modal("show");
        },
        hide:function(){
            $($m9011.params.modalId).modal("hide");
        },
        params:{
            getListUrl:'/merchandise/getListByMerchantId.asyn?r='+Math.random(),
            getListByIdsUrl:'/merchandise/getListByIds.asyn?r='+Math.random(),
            formId:'#add-goods-form',
            modalId:'#signup-modal',
        }
    }
    
    //配置表格参数
    $mytable.params={
        url:$m9011.params.getListUrl,
        columns:[{ //复选框单元格
            className: "td-checkbox",
            orderable: false,
            width: "30px",
            data: null,
            render: function (data, type, row, meta) {
                return '<input type="checkbox" class="iCheck">';
            }
        },
        {data:'barcode'},{data:'name'},{data:'unitName'},{data:'goodsNo'},{data:'brandName'},{data:'factoryNo'},{data:'merchantName'}
        ],
        formid:$m9011.params.formId
    }
  	//生成列表信息
    $('#datatable-buttons2').mydatatables();
    /**
     * 全选
     */
    $('#datatable-buttons2').on("change", ":checkbox", function() {
        // 列表复选框
        if ($(this).is("[name='keeperUserGroup-checkable']")) {
            // 全选
            $(":checkbox", '#datatable-buttons2').prop("checked",$(this).prop("checked"));
        }else{
            // 一般复选
            var checkbox = $("tbody :checkbox", '#datatable-buttons2');
            $(":checkbox[name='cb-check-all']", '#datatable-buttons2').prop('checked', checkbox.length == checkbox.filter(':checked').length);
        }
    });
    
    function searchData(){
        $mytable.fn.reload();
    }
    

</script>