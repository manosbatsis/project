<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div class="row">
    <div class="col-12">
        <div>
            <!-- Signup modal content -->
            <div id="signup-modal"  class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true"
                 style="display: none;">
                <div class="modal-dialog">
                    <div class="modal-content" style="width: 680px;text-align: center;">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                            <h5 class="modal-title" id="myLargeModalLabel">选择结算项目</h5>
                        </div>
                        <div class="modal-body">
                            <form id="popup-goods-form">
                                <div class="form-row mt10">
                                    <div class="col-5">
                                        <div class="row">
                                            <input type="hidden" id="type" name="type" value="1">
                                            <label class="col-5 col-form-label"><div class="fr">1级费项<span>：</span></div></label>
                                            <div class="col-7 ml10">
                                                <select class="form-control" name="parentId" id="parentId"></select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-5">
                                        <div class="row">
                                            <label class="col-5 col-form-label"><div class="fr">2级费项<span>：</span></div></label>
                                            <div class="col-7 ml10">
                                                <select class="form-control" name="projectId" id="projectId"></select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-2">
                                        <div class="row">
                                            <button type="button" class="btn btn-info waves-effect waves-light fr btn-sm" onclick='$m13003.show();'>查询</button>
                                        </div>
                                    </div>
                                </div>
                            </form>
                            <div class="form-row mt20">
                                <table id="datatable-detail" class="table table-bordered" cellspacing="0" width="100%">
                                    <thead></thead>
                                    <tbody></tbody>
                                </table>
                            </div>
                            <div class="form-row mt20">
                                <div class="form-inline m0auto">
                                    <div class=form-group>
                                        <button type="button" class="btn btn-info waves-effect waves-light fr" onclick='$m13003.save();'>确定</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->
        </div>
    </div><!-- end col -->
</div>
<script type="text/javascript">
    var $m13003={
        init:function(obj){
            $m13003.params.thisObj = obj;
            $module.settlementConfig.getSettlementConfig.call($("select[name='projectId']")[0], '2', $m13003.params.customerId, "1", "2");
            $module.settlementConfig.getSettlementConfig.call($("select[name='parentId']")[0], '1', $m13003.params.customerId, "1", "2");
            this.show();
        },
        list:function(){
            dttable = $('#datatable-detail').dataTable();
            dttable.fnClearTable(false); //清空一下table
            dttable.fnDestroy(); //还原初始化了的datatable
            //加载表格
            saleOrder();
        },
        show:function(){
            $($m13003.params.modalId).modal("show")
            this.list();
        },
        hide:function(){
            $($m13003.params.modalId).modal("hide");
        },
        save:function () {
            var count = 0;
            var projectId = null;
            var projectName = null;
            var nTrs = $mytable.tableObj.fnGetNodes();//fnGetNodes获取表格所有行，nTrs[i]表示第i行tr
            for(var i = 0; i < nTrs.length; i++){
                if($(nTrs[i]).find("input[type=checkbox]").prop('checked')){
                    var row = $mytable.tableObj.fnGetData(nTrs[i]);
                    projectId = row.id
                    projectName = row.projectName;
                    count++;
                }
            }
            if (count == 0) {
                $custom.alert.warningText("请选择项目！");
                return;
            }
            if (count > 1) {
                $custom.alert.warningText("只能选择一个项目！");
                return;
            }
            $($m13003.params.thisObj).parent("td").find("input[name=projectId]").val(projectId);
            $($m13003.params.thisObj).val(projectName);
            $m13003.hide();
        },
        params:{
            projectUrl:serverAddr + '/settlementConfig/settlementConfigListByModuleType.asyn?r='+Math.random(),
            modalId:'#signup-modal',
            thisObj:null,
            customerId:null
        }
    };
    function selectTable(obj, customerId){
        $m13003.params.customerId = customerId;
        var thead = '<tr>'
            +' <th style="text-align: center">'
            +''
            +'</th>'
            +'  <th style="text-align: center">1级费项</th>'
            +'  <th style="text-align: center">2级费项</th>'
            +'</tr>';
        $('#datatable-detail').find("thead").html(thead);
        $m13003.init(obj);
    }
    //原生的datatable
    function saleOrder(){
        var $table = $('#datatable-detail');
        var _table = $table.dataTable({
            //自定义
            language: $mytable.lang,
            //是否允许用户改变表格每页显示的记录数
            // lengthChange: false,
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
            "dom": '<"top"rt><"bottom"lip><"clear">',
            //显示字段数据
            columns:[
                { //复选框单元格
                    className : "td-checkbox",
                    orderable : false,
                    data : null,
                    render : function(data, type, row, meta) {
                        return '<input type="checkbox" class="iCheck" onclick="selectOrder()">';
                    }
                },
                {data : 'parentProjectName'},{data : 'projectName'}
            ],
            //异步获取数据
            ajax:function(data, callback, settings){

                $custom.request.ajax($m13003.params.projectUrl,getParams(data),

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
        });
        //生成列表信息
        $('#datatable-detail').mydatatables();

        function getParams(data){
            var jsonData=null;
            //设置开始页和每页最大记录数
            var type = $('#type').val();
            var parentId = $('#parentId').val();
            jsonData=$json.setJson(jsonData,"begin",data.start);
            jsonData=$json.setJson(jsonData,"pageSize",data.length);
            jsonData=$json.setJson(jsonData, "customerId", $m13003.params.customerId);
            jsonData=$json.setJson(jsonData, "parentId", parentId);
            jsonData=$json.setJson(jsonData, "id", projectId);
            jsonData=$json.setJson(jsonData, "type", type);
            jsonData=$json.setJson(jsonData, "moduleType", '2');
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
    }
</script>