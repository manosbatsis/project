<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div>
    <!-- Signup modal content -->
    <div id="signup-modal1" class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;" data-backdrop="static">
        <div class="modal-dialog  modal-lg">
            <div class="modal-content">
            	<div class="modal-header">
                     <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                     <h5 class="modal-title" id="myLargeModalLabel">选择单据</h5>
                </div>
                <div class="modal-body">
                <form id="order_form">
                	<div class="form-row">
                          <div class="col-6">
                              <div class="row">
                                  <label  class="col-5 col-form-label " style="white-space:nowrap;"><div class="fr">单据类型<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <select class="form-control" id="order_type">
                                      		<option value="1">调拨订单</option>
                                      		<option value="2">采购订单</option>
                                      </select>
                                  </div>
                              </div>
                          </div>
                          <div class="col-6">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">PO单号<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <input type="text" class="form-control" name="poNo">
                                  </div>
                              </div>
                          </div> 
                      </div>
                      </form>
                      <div class="form-row mt10">
                      	<div class="col-6">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">单号<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                  <input type="text" class="form-control" name="code">
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
                      <div class="form-row mt20">
                   			<table id="datatable-buttons1" class="table table-bordered" cellspacing="0" width="100%">
			                        <thead></thead>
			                        <tbody></tbody>
			                    </table>
                      </div>
                      <div class="form-row mt20">
                      	<div class="form-inline m0auto">
                          		<div class=form-group>
                          			<button type="button" class="btn btn-info waves-effect waves-light fr" onclick='$m9012.save();'>确定</button>
                          			<button type="button" class="btn btn-light waves-effect waves-light ml15"  onclick='$m9012.hide();'>取消</button>
                          		</div>
                          	</div>
                      </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>

<script type="text/javascript">

    var $m9012={
        init:function(orderType){
            this.show(orderType);
        },
        list:function(orderType){
        	 dttable = $('#datatable-buttons1').dataTable();
             dttable.fnClearTable(); //清空一下table
             dttable.fnDestroy(); //还原初始化了的datatable
	        //加载表格  （orderType不同，加载不同的表格）
	        if("1" == orderType){
	        	transferOrder();
	        }else if("2" == orderType){
	        	purchaseOrder();
	        }
        },
        show:function(orderType){
            $($m9012.params.modalId).modal("show")
            this.list(orderType);
        },
        hide:function(){
            $($m9012.params.modalId).modal("hide");
        }, 
        save:function(){
        	var orderType = $('#order_type').val();
        	var ids=$mytable.fn.getCheckedRow();
			this.hide()
        	$($m9012.params.modalId).on('hidden.bs.modal', function () {
        		$load.a(serverAddr+'/sale/toAddPage.html?ids='+ids+'&type='+orderType);
			})
        },
        params:{
            formId:'#order_form',
            modalId:'#signup-modal1',
        }
    }
    //不同单据类型有不同的表头
	$('#order_type').on("change",function(){
		var orderType = $(this).val();
		selectTable(orderType);
	});
    function selectTable(orderType){
		var thead = "";
		if("1" == orderType){
			var thead = '<tr>'
		        +' <th>'
		        +'<label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">'
		        +'<input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />'
		        +'<span></span>'
		        +'</label>'
		        +'</th>'
		        +'  <th>单据类型</th>'
		        +'  <th>单号</th>'
		        +'  <th>合同号</th>'
		        +'  <th>调出仓库</th>'
		        +'  <th>调入仓库</th>'
		        +'  <th>调出日期</th>'
		        +'  <th>制单人</th>'
		        +'  <th>制单时间</th>'
		        +'</tr>';
		}else if("2" == orderType){
			var thead = '<tr>'
				+' <th>'
		        +'<label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">'
		        +'<input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />'
		        +'<span></span>'
		        +'</label>'
		        +'</th>'
		        +'  <th>单据类型</th>'
		        +'  <th>单号</th>'
		        +'  <th>PO号</th>'
		        +'  <th>仓库</th>'
		        +'  <th>供应商</th>'
		        +'  <th>入库日期</th>'
		        +'  <th>制单人</th>'
		        +'  <th>制单时间</th>'
		        +'</tr>';
		}
		$('#datatable-buttons1').find("thead").html(thead);
		$m9012.init(orderType);
    }

    //原生的datatable
    function purchaseOrder(){
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
						{ //单据类型
							className : "",
							orderable : false,
							data : null,
							render : function(data, type, row, meta) {
								return '采购订单';
							}
						},
						{data : 'code'},{data : 'poNo'},{data : 'depotName'},{data : 'supplierName'},{data : 'endDate'},{data : 'creater'},{data : 'createDate'},
						],
                //异步获取数据
                ajax:function(data, callback, settings){
                    $.ajax({
                        type: "POST",
                        url: serverAddr+'/purchase/listPurchaseOrder.asyn',
                        cache : false,  //禁用缓存
                        data:$mytable.fn.getFormParams(data), 
                        dataType: "json",
                        success: function(result) {
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
                        },
                        error: function(XMLHttpRequest, textStatus, errorThrown) {
                        	$custom.alert.error("查询失败");
                        }
                    }); 
                 },
                //每创建一行都回调
//                 fnRowCallback : $mytable.rewriter.fnRowCallback,
            });
   		//生成列表信息
		$('#datatable-buttons1').mydatatables();
    }
    
    function transferOrder(){
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
   						{ //单据类型
   							className : "",
   							orderable : false,
   							data : null,
   							render : function(data, type, row, meta) {
   								return '调拨订单';
   							}
   						},
   						{data : 'code'},{data : 'contractNo'},{data : 'outDepotName'},{data : 'inDepotName'},{data : 'createDate'},{data : 'creater'},{data : 'createDate'},
   						],
                   //异步获取数据
                   ajax:function(data, callback, settings){
                       $.ajax({
                           type: "POST",
                           url: serverAddr+'/transfer/listTransferOrder.asyn',
                           cache : false,  //禁用缓存
                           data:$mytable.fn.getFormParams(data), 
                           dataType: "json",
                           success: function(result) {
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
                           },
                           error: function(XMLHttpRequest, textStatus, errorThrown) {
                           	$custom.alert.error("查询失败");
                           }
                       }); 
                    },
                   //每创建一行都回调
//                    fnRowCallback : $mytable.rewriter.fnRowCallback,
               });
      	//生成列表信息
		$('#datatable-buttons1').mydatatables();
       }
</script>