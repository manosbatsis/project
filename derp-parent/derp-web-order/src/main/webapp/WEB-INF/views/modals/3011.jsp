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
                                  <label class="col-5 col-form-label"><div class="fr">商品名称<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <input type="text" class="form-control" name="name">
                                      <input type="hidden" class="form-control" id="id" name="depotId">
                                      <input type="hidden" class="form-control" id="unNeedIds" name="unNeedIds">
                                  </div>
                              </div>
                          </div> 
                          <div class="col-6">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">条形码<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <input type="text" class="form-control" name="barcode">
                                  </div>
                              </div>
                          </div> 
                      </div>
                      <div class="form-row mt10">
                      	<div class="col-6">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">商品品牌<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                     <select class="form-control" id="brandId" name="brandId">
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
                   			<table id="datatable-buttons2" class="table table-bordered" cellspacing="0" width="100%">
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
			                            <th>计量单位</th>
			                            <th>条形码</th>
			                            <th>工厂编码</th>
			                        </tr>
			                        </thead>
			                    </table>
                      </div>
                      <div class="form-row mt20">
                      	<div class="form-inline m0auto">
                          		<div class=form-group>
                          			<button type="button" class="btn btn-info waves-effect waves-light fr" onclick='$m3011.save();'>确定</button>
                          			<button type="button" class="btn btn-light waves-effect waves-light ml15"  onclick='$m3011.hide();'>取消</button>
                          		</div>
                          	</div>
                      </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>

<script type="text/javascript">

    var thisObj=null;//当前点击对象
    var outOrInDepotId=null;//当前是调出/调入
    var $m3011={
        init:function(obj,thisDepotId){
                thisObj=obj;
                outOrInDepotId=thisDepotId;
               
	        	//先选择仓库
	        	var depotId = $("#"+thisDepotId).val();
				if(depotId==null || depotId==""){
					$custom.alert.error("请先选择仓库！");
				}else{
					//获取已选中的商品id
					this.setunNeedIds();
					//初始化重选按钮
					$(".group-checkable").prop("checked",false);
			        //重置表单
			        $($m3011.params.formId)[0].reset();
			        $("#id").val(depotId);
			        this.show();
        	    } 
        },
        setunNeedIds:function(){
        	var unNeedIds ="";//已经选中的商品id
        	 $("#datatable-buttons tbody tr").each(function (index, item) {
                 var outGoodsId = $(this).find("td").eq(1).find("input").val();//调出商品id
                 var inGoodsId = $(this).find("td").eq(4).find("input").val();//调入商品id
                 if(outGoodsId!=''&&outGoodsId!=undefined&&inGoodsId!=''&&inGoodsId!=undefined){
                	 unNeedIds = unNeedIds+","+outGoodsId+","+inGoodsId;
                 }
                
             });
        	 unNeedIds = unNeedIds.substr(1);
        	 $("#unNeedIds").val(unNeedIds);
        },
        list:function(){
	        //加载表格
	        $("#datatable-buttons2 tr:gt(0)").remove();
	        searchData();
        },
        save:function(){
        	var ids=$mytable.fn.getCheckedRow();
         	if(ids==''){
        		$($m3011.params.modalId).modal("hide");
        		return;
        	}
        	$custom.request.ajax(
        		$m3011.params.getListByIdsUrl,{"ids":ids},function(data){
                	if(data.state==200){
                         //选择商品后把商品信息读取过新增页面
                          if(outOrInDepotId=='outDepotId'){//调出
                                 var tr = "";
					             $.each(data.data,function(index,event){
					                    tr+='<tr>'+
					                           '<td class="tc nowrap">'+ 
					                               '<i class="fi-plus" onclick="addtr();"></i>&nbsp;&nbsp;&nbsp;<i class="fi-minus" onclick="deltr(this);"></i>'+ 
					                           '</td>'+ 
					                           '<td><input type="hidden"  class="form-control" name="outGoodsId" value="'+event.id+'">'+(event.goodsCode==null?'':event.goodsCode)+
					                                '<button type="button" class="btn btn-add waves-effect waves-light" onclick="$m3011.init(this,\'outDepotId\');">选择商品</button>'+
					                           '</td>'+ 
						                       '<td>'+(event.goodsNo==null?'':event.goodsNo)+'</td>'+ 
						                       '<td>'+(event.name==null?'':event.name)+'</td>'+
						                       '<td>'+
								                   '<select id="outUnit" class="edit_input" style="width: 60px;">'+
								                       '<option value="02">件</option>'+
								   					   '<option value="00">托盘</option>'+
								                       '<option value="01">箱</option>'+
								   					   '<option value="03">KG</option>'+
								   				   '</select>'+
								               '</td>'+
					                           '<td><input type="hidden"  class="form-control" name="inGoodsId" value="'+event.id+'">'+(event.goodsCode==null?'':event.goodsCode)+
					                               '<button type="button" class="btn btn-add waves-effect waves-light" onclick="$m3011.init(this,\'inDepotId\');">选择商品</button>'+
					                           '</td>'+ 
						                       '<td>'+(event.goodsNo==null?'':event.goodsNo)+'</td>'+ 
						                       '<td>'+(event.name==null?'':event.name)+'</td>'+
						                       '<td>'+
								                   '<select id="outUnit" class="edit_input" style="width: 60px;">'+
								                       '<option value="02">件</option>'+
								   					   '<option value="00">托盘</option>'+
								                       '<option value="01">箱</option>'+
								   					   '<option value="03">KG</option>'+
								   				   '</select>'+
								               '</td>'+
						                       '<td><input type="text"  class="form-control" name="transferNum" onkeyup="value=value.replace(/[^\\d]/g,\'\')" maxlength="11"></td>'+
						                       '<td><input type="text"  class="form-control" name="brandName" value="'+(event.brandName==null?'':event.brandName)+'" readonly="readonly"></td>'+
											   '<td><input type="text"  class="form-control" name="price" onkeyup="value=value.replace(/[^\\d.]/g,\'\')" maxlength="11"></td>'+
											   '<td><input type="text"  class="form-control" name="grossWeight" value="'+(event.grossWeight==null?'':event.grossWeight)+'" onkeyup="value=value.replace(/[^\\d.]/g,\'\')" maxlength="11"></td>'+
											   '<td><input type="text"  class="form-control" name="netWeight" value="'+(event.netWeight==null?'':event.netWeight)+'" onkeyup="value=value.replace(/[^\\d.]/g,\'\')" maxlength="11"></td>'+
											   '<td><input type="text"  class="form-control" name="contNo"></td>'+
											   '<td><input type="text"  class="form-control" name="bargainno"></td>'+
						                       '</tr>'; 
					            });
					            $(thisObj).parent("td").parent("tr").replaceWith(tr);
                         }else{//调入
                       
                               var event = data.data[0];
                               //更新调入三个单元格
                               var td = '<td><input type="hidden"  class="form-control" name="inGoodsId" value="'+event.id+'">'+(event.goodsCode==null?'':event.goodsCode)+
					                      '<button type="button" class="btn btn-add waves-effect waves-light" onclick="$m3011.init(this,\'inDepotId\');">选择商品</button>'+
					                    '</td>';
                               $(thisObj).parent("td").next().replaceWith('<td>'+(event.goodsNo==null?'':event.goodsNo)+'</td>');
                               $(thisObj).parent("td").next().next().replaceWith('<td>'+(event.name==null?'':event.name)+'</td>'); 
                               $(thisObj).parent("td").replaceWith(td); //要放最后面替换
                        }  
                    }else{
                        $custom.alert.error(data.message);
                    }
            	}
        	);
        	$($m3011.params.modalId).modal("hide");
        },
        loadBrand:function(){
	        //加载表格
	        $custom.request.ajax("/brand/getSelectBean.asyn",{},function(data){
	        	selLoad(data.data);
	        	//重新加载select2
	    		$(".goods_select2").select2({
	    			language:"zh-CN",
	    			placeholder: '请选择',
	    			allowClear:true
	    		});
	        });
        },
        show:function(){
        	this.loadBrand();
	        this.list();
	        $($m3011.params.modalId).modal("show");
        },
        hide:function(){
            $($m3011.params.modalId).modal("hide");
        },
        params:{
            getListUrl:'/merchandise/getListForType.asyn?r='+Math.random(),
            getListByIdsUrl:'/merchandise/getListByIds.asyn?r='+Math.random(),
            formId:'#add-goods-form',
            modalId:'#signup-modal',
        }
    }
    
    //配置表格参数
    $mytable.params={
        url:$m3011.params.getListUrl,
        columns:[{ //复选框单元格
            className: "td-checkbox",
            orderable: false,
            width: "30px",
            data: null,
            render: function (data, type, row, meta) {
                return '<input type="checkbox" class="iCheck">';
            }
        },
        {data:'merchantName'},{data:'name'},{data:'goodsNo'},{data:'brandName'},{data:'unitName'},{data:'barcode'},{data:'factoryNo'},
        ],
        formid:$m3011.params.formId
    }
    $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){

    };
    //生成列表信息
    $('#datatable-buttons2').mydatatables();
    
    //渲染 下拉框
    function selLoad(data){
    	$("#brandId").empty();
        var ops="<option value=''>请选择</option>";
        $.each(data,function(index,event){
        	if(event.value!=null){
            	ops+="<option value='"+event.value+"'>"+event.label+"</option>";
            }
        });
        $("#brandId").append(ops);
    }
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
    //重置
   	$("button[type='reset']").click(function(){
   		$("#brandId").each(function(){
   			$(this).val("");
   		});
   		//重新加载
   		 $("#brandId").select2({
   			language:"zh-CN",
   			placeholder: '请选择',
   			allowClear:true
   		}); 
   	});
    

</script>