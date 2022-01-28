<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div>
    <!-- Signup modal content -->
    <div id="signup-modal1" class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;" data-backdrop="static">
        <div class="modal-dialog  modal-lg">
            <div class="modal-content" style="width: 920px;">
            	<div class="modal-header">
                     <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                     <h5 class="modal-title" id="myLargeModalLabel">选择商品</h5>
                </div>
                <div class="modal-body">
                <form id="order_form">
                	<div class="form-row">
                          <div class="col-6">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">标准条码<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                  <input type="text" class="form-control" name="commbarcode" >
                         		 </div>
                              </div>
                          </div>
                           <div class="col-6">
                              <div class="row">
                                  <label  class="col-5 col-form-label " style="white-space:nowrap;"><div class="fr">标准品牌<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <select class="form-control" id="commBrandParentId" name="commBrandParentId">
                                      	<option value="">请选择</option>
                                      </select>
                                  </div>
                              </div>
                          </div>
                      </div>
                      <div class="form-row mt20">
                      	<div class="form-inline m0auto">
                          		<div class=form-group>
                          			<button type="button" class="btn btn-info waves-effect waves-light fr" onclick='qureyData();'>查询</button>
                          			<button type="reset" class="btn btn-light waves-effect waves-light ml15" >重置</button>
                          		</div>
                          	</div>
                      </div>
                      </form>
                      <div class="form-row mt20">
                   			<table id="datatable-buttons1" class="table table-bordered" cellspacing="0" width="100%">
			                        <thead>
			                        	<tr>
			                        		<th>
										        <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
										        <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
										        <span>全选</span>
										        </label>
									        </th>
									        <th>标准品牌</th>
									        <th>标准条码</th>
									        <th>标准品名</th>
									        <th>二级分类</th>
								        </tr>
			                        </thead>
			                        <tbody></tbody>
			                    </table>
                      </div>
                      <div class="form-row mt20">
                      	<div class="form-inline m0auto">
                          		<div class=form-group>
                          			<button type="button" class="btn btn-info waves-effect waves-light fr delayedBtn" onclick='$m2016.save();'>确定</button>
                          			<button type="button" class="btn btn-light waves-effect waves-light ml15"  onclick='$m2016.hide();'>取消</button>
                          		</div>
                          	</div>
                      </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>

<script type="text/javascript">
    var $m2016={
        init:function(){
        	//加载客户的下拉数据
        	$module.brand.loadParentBrandSelect.call($('select[id="commBrandParentId"]'), '');
            this.show();
        },
        list:function(){
        	 dttable = $('#datatable-buttons1').dataTable();
             dttable.fnClearTable(false); //清空一下table
             dttable.fnDestroy(); //还原初始化了的datatable
	        //加载表格
	        saleOrder();
        },
        show:function(){
            $($m2016.params.modalId).modal("show")
            this.list();
        },
        hide:function(){
            $($m2016.params.modalId).modal("hide");
        }, 
        save:function(){
        	var ids=$mytable.fn.getCheckedRow();
        	if(ids==null || ids==""){
        	 	$custom.alert.error("请至少选择一项");
                return;
        	}
        	
        	var row = null ;
        	var nTrs = $mytable.tableObj.fnGetNodes();
        	for(var i = 0; i < nTrs.length; i++){
                if($(nTrs[i]).find("input[type=checkbox]").prop('checked')){
                    row = $mytable.tableObj.fnGetData(nTrs[i]);
                    
                    var commBrandParentName = "" ;
                	if(row.commBrandParentName){
                		commBrandParentName = row.commBrandParentName ;
                	}
                    
                    var html = "<tr>" ;
        			
        			html += "<td><input type='checkbox' class='muti-iCheck'></td>" ;
        			html += "<td><select style='width: 80px;' class='edit_input multiSelect' onchange='getDetails(this, \"2\")' required reqMsg='SD类型不能为空'><option value=''>请选择</option></select></td>" ;
        			html += "<td></td>" ;
        			html += "<td>" + commBrandParentName + "</td>" ;
        			html += "<td><input type='hidden' class='commbarcode' value='"+ row.commbarcode +"'>" + row.commbarcode + "</td>" ;
        			html += "<td>" + row.commGoodsName + "</td>" ;
        			html += "<td><input class='edit_input proportion' type='text'required reqMsg='比例不能为空'></td>" ;
        			
        			html += "</tr>" ;
        			
        			$("#multi-tbody").append(html) ;
                }
            }
        	
        	getSelect("2") ;
        	
        	$("#skus").html("合计SKU：" + $("#multi-tbody").find("tr").length) ;
        	
			this.hide() ;
        },
        params:{
            formId:'#order_form',
            modalId:'#signup-modal1',
        }
    }
    function selectTable(){
		var thead = "";
			var thead = '<tr>'
		        +' <th>'
		        +'<label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">'
		        +'<input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />'
		        +'<span>全选</span>'
		        +'</label>'
		        +'</th>'
		        +'  <th>标准品牌</th>'
		        +'  <th>标准条码</th>'
		        +'  <th>标准品名</th>'
		        +'  <th>二级分类</th>'
		        +'</tr>';
		$('#datatable-buttons1').find("thead").html(thead);
		$m2016.init();
    }

    //原生的datatable
    function saleOrder(){
        var $table = $('#datatable-buttons1');
        var _table = $table.dataTable({
                   //自定义
                   language: $mytable.lang,
                   //是否允许用户改变表格每页显示的记录数
                   lengthChange: false,
                   //启用服务器端分页
                   serverSide: true,
                   //禁用原生搜索
                   searching: false,
                   //允许多次初始化同一表格
                   retrieve: true,
                   //禁用排序
                   "ordering": false, // 禁止排序
                   //显示数字的翻页样式
                   sPaginationType: "full_numbers",
                   //显示字段数据
                   columns:[
   						{ //复选框单元格
   							className : "td-checkbox",
   							orderable : false,
   							data : null,
   							render : function(data, type, row, meta) {
   								return '<input type="checkbox" class="iCheck">';
   							}
   						},
   						{data : 'commBrandParentName'},{data : 'commbarcode'},{data : 'commGoodsName'},{data : 'commTypeName'},
   						],
                   //异步获取数据
                   ajax:function(data, callback, settings){
                	   $custom.request.ajax('/commbarcode/listCommbarcodes.asyn?r='+Math.random(), getParams(data),
                     	   function(result) {
                		   
                               //异常判断与处理
                               if (result.errorCode) {
                               	$custom.alert.error("查询失败");
                                   return;
                               }
                               //封装返回数据
                               var returnData = {};
                               returnData.draw = data.draw;//这里直接自行返回了draw计数器,应该由后台返回
                               returnData.recordsTotal = result.data.total;//总记录数
                               returnData.recordsFiltered = result.data.total;//后台不实现过滤功能，每次查询均视作全部结果
                               returnData.data = result.data.list;
                               //调用DataTables提供的callback方法，代表数据已封装完成并传回DataTables进行渲染
                               //此时的数据需确保正确无误，异常判断应在执行此回调前自行处理完毕
                               callback(returnData);
                       }); 
                    },
                   //每创建一行都回调
               });
      	//生成列表信息
		$('#datatable-buttons1').mydatatables();
       }
    
    function qureyData(){
    	$m2016.list() ;
    }
    
    function getParams(data){
        var jsonData=null;
        //设置开始页和每页最大记录数
        jsonData=$json.setJson(jsonData,"begin",data.start);
        jsonData=$json.setJson(jsonData,"pageSize",data.length);
        //设置过滤条件参数
        var formid="#order_form";
        var jsonArray=$(formid).serializeArray();
        $.each(jsonArray, function(i, field){
            if(field.value!=null&&field.value!=''&&field.value!=undefined){
                jsonData=$json.setJson(jsonData,field.name,field.value);
            }
        });
        return JSON.parse(jsonData);
    }
    
    $("input[name='keeperUserGroup-checkable']").on("change", function(){
        if($(this).is(':checked')){
            $(":checkbox", '#table-multi-list').prop("checked",$(this).prop("checked"));
            $('#table-multi-list tbody tr').addClass('success');
        }else{
            $(":checkbox", '#table-multi-list').prop("checked",false);
            $('#table-multi-list tbody tr').removeClass('success');
        }
    }) ;
    $('#table-multi-list').on("change", ":checkbox", function() {
        $(this).parents('tr').toggleClass('success');
    }) ;
    
</script>