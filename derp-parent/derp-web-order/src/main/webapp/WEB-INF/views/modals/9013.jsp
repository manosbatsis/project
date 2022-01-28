<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div>
    <!-- Signup modal content -->
    <div id="signup-modal1" class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;" data-backdrop="static">
        <div class="modal-dialog  modal-lg">
            <div class="modal-content" style="width: 920px;">
            	<div class="modal-header">
                     <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                     <h5 class="modal-title" id="myLargeModalLabel">选择单据</h5>
                </div>
                <div class="modal-body">
                <form id="order_form">
                	<div class="form-row">
                          <div class="col-6">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">销售单号<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                  <input type="text" class="form-control" name="codes" placeholder="多个订单用英文逗号隔开">
                         		 </div>
                              </div>
                          </div>
                           <div class="col-6">
                              <div class="row">
                                  <label  class="col-5 col-form-label " style="white-space:nowrap;"><div class="fr">客户<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <select class="form-control" id="queryCustomerId" name="customerId">
                                      <option value="">请选择</option>
                                      </select>
                                  </div>
                              </div>
                          </div>
                      </div>
                      <div class="form-row">
                       <div class="col-6">
                              <div class="row">
                                  <label  class="col-5 col-form-label " style="white-space:nowrap;"><div class="fr">出仓仓库<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <select class="form-control" id="queryDepotId" name="outDepotId">
                                       <option value="">请选择</option>
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
			                        <thead></thead>
			                        <tbody></tbody>
			                    </table>
                      </div>
                      <div class="form-row mt20">
                      	<div class="form-inline m0auto">
                          		<div class=form-group>
                          			<button type="button" class="btn btn-info waves-effect waves-light fr" onclick='$m9013.save();'>确定</button>
                          			<button type="button" class="btn btn-light waves-effect waves-light ml15"  onclick='$m9013.hide();'>取消</button>
                          		</div>
                          	</div>
                      </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>

<script type="text/javascript">
    var $m9013={
        init:function(){
        	//加载客户的下拉数据
        	$module.customer.loadSelectData.call($('#queryCustomerId'),"");
        	$module.depot.getSelectBeanByMerchantRel.call($('#queryDepotId'),null,{"type":"a,b,c,d,f"});
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
            $($m9013.params.modalId).modal("show")
            this.list();
        },
        hide:function(){
            $($m9013.params.modalId).modal("hide");
        }, 
        save:function(){
/*        	var customerId = $('#customerId').val();
        	var outDepotId = $('#inDepotId').val();*/
        	var ids=$mytable.fn.getCheckedRow();
        	if(ids==null || ids==""){
        	 	$custom.alert.error("请至少选择一单");
                return;
        	}
			
        	$($m9013.params.modalId).on('hidden.bs.modal', function () {
        		/** 多选订单时，需校验是否相同出仓仓库、相同客户、相同销售类型，若有不同则报错提示，不予保存提交。**/	
        	 	$custom.request.ajax(serverAddr+"/saleReturn/isSameParameter.asyn",{"ids":ids},function(data){
        			if(data.state==200){
        				$module.load.pageOrder('act=50014&ids='+ids);
					}else{
						$custom.alert.error(data.expMessage);
						return;
					}
        		});
        		
			})
			this.hide()
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
		        +'  <th>销售单号</th>'
		        +'  <th>客户</th>'
		        +'  <th>出仓仓库</th>'
		        +'  <th>PO单号</th>'
		        +'  <th>销售类型</th>'
		        +'  <th>制单人</th>'
		        +'  <th>制单时间</th>'
		        +'</tr>';
		$('#datatable-buttons1').find("thead").html(thead);
		$m9013.init();
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
   						{data : 'code'},{data : 'customerName'},{data : 'outDepotName'},{data : 'poNo'},{data : 'businessModelLabel'},{data : 'createName'},{data : 'createDate'},
   						],
                   //异步获取数据
                   ajax:function(data, callback, settings){
                	   $custom.request.ajax(serverAddr+'/sale/saleGetListSaleOrderByPage.asyn?r=' + Math.random(),getParams(data),
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
//                    fnRowCallback : $mytable.rewriter.fnRowCallback,
               });
      	//生成列表信息
		$('#datatable-buttons1').mydatatables();
       }
    
    function qureyData(){
        $m9013.list() ;
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
    /**
     * 全选
     */
    $('#datatable-buttons1').on("change", ":checkbox", function() {
        // 列表复选框
        if ($(this).is("[name='keeperUserGroup-checkable']")) {
            // 全选
            $(":checkbox", '#datatable-buttons1').prop("checked",$(this).prop("checked"));
        }else{
            // 一般复选
            var checkbox = $("tbody :checkbox", '#datatable-buttons1');
            $(":checkbox[name='cb-check-all']", '#datatable-buttons1').prop('checked', checkbox.length == checkbox.filter(':checked').length);
        }
    });
</script>