<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!-- Start content -->

<div class="content">
    <div class="container-fluid mt80">
        <!-- end row -->
        <div class="row">
            <div class="col-12">
                <div class="card-box table-responsive">
                    <div class="col-12 row">
                        <div>
                            <ol class="breadcrumb">
                                <li><i class="block"></i> 当前位置：</li>
                                <li class="breadcrumb-item"><a href="#">接口管理</a></li>
                                <li class="breadcrumb-item "><a href="javascript:$load.a('/apiExternal/toPage.html');">对外接口管理列表</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  过滤条件查询  start  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                    <span class="msg_title">使用对象 :</span>
                                    <input class="input_msg" type="text" name="platformName" required="" >
                                    <span class="msg_title">公司 :</span>
                                    <input class="input_msg" type="text" name="merchantName" id="merchantName"  required="" >
                                    <div class="fr">
                                        <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick='searchData();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default ">重置</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                    <!-- 过滤条件查询  end  -->

                    <div class="row col-md-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="apiExternal_toAddPage">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="add-buttons">新增</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="apiExternal_delApiExternal">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick="$custom.request.delChecked('/apiExternal/delApiExternal.asyn');">删除</button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                    <div class="row mt20">
                        <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>
                                    <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                        <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
                                        <span></span>
                                    </label>
                                </th>
                                <th>使用对象</th>
                                <th>公司</th>
                                <th>app_key</th>
                                <th>密钥</th>
                                <th>状态</th>
                                <th>备注</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <!-- End row -->
        <!-- end row -->
    </div> <!-- container -->
</div> <!-- content -->
<footer class="footer text-right">
</footer>

<script type="text/javascript">


    $(document).ready(function() {
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:'/apiExternal/listApiExternal.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
                {data:'platformName'},{data:'merchantName'},{data:'appKey'},{data:'appSecret'},{data:'statusLabel'},{data:'remark'},
                { //操作
                    orderable: false,
                    width: "100px",
                    data: null,
                    render: function (data, type, row, meta) {
                        var details = "";
                        details = '<shiro:hasPermission name="apiExternal_toDetailsPage"><a href="javascript:details('+row.id+')">详情</a></shiro:hasPermission>';
                        var edit = "";
                        edit = '<shiro:hasPermission name="apiExternal_toEditPage"><a href="javascript:edit('+row.id+')">编辑</a></shiro:hasPermission>';
                        var status = "";
                        if(row.status=='1'){
                            status = '<shiro:hasPermission name="apiExternal_auditApiExternal"><a href="javascript:audit('+row.id+',0)">禁用</a></shiro:hasPermission>';
                        }else{
                            status = '<shiro:hasPermission name="apiExternal_auditApiExternal"><a href="javascript:audit('+row.id+',1)">启用</a></shiro:hasPermission>';
                        }
                        return status + " " + edit + " " +details;
                    }
                },
            ],
            formid:'#form1'
        };
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();

        //新增
        $("#add-buttons").click(function(){
            $load.a("/apiExternal/toAddPage.html");
        });

        /**
         * 全选
         */
        $("input[name='keeperUserGroup-checkable']").on("change", function(){
            if($(this).is(':checked')){
                $(":checkbox", '#datatable-buttons').prop("checked",$(this).prop("checked"));
                $('#datatable-buttons tbody tr').addClass('success');
            }else{
                $(":checkbox", '#datatable-buttons').prop("checked",false);
                $('#datatable-buttons tbody tr').removeClass('success');
            }
        })
        $('#datatable-buttons').on("change", ":checkbox", function() {
            $(this).parents('tr').toggleClass('success');
        })
    });
    function searchData(){
        $mytable.fn.reload();
    }
    //禁用/启用
    function audit(id,status){
        $custom.alert.warning("你确认要禁用/启用吗？",function(){
            var url = "/apiExternal/auditApiExternal.asyn";
            var form = {"id":id, "status":status};
            var state = "";
            if(status == '1'){
                state = "启用";
            }else{
                state = "禁用";
            }
            $custom.request.ajax(url, form, function(result){
                if(result.state == '200'){
                    $custom.alert.success(state + "成功！");
                    setTimeout(function () {
                    	
                    	//列表菜单缓存参数清空
						$module.revertList.serializeListForm() ;
				    	$module.revertList.isMainList = true ; 
                    	
                        $load.a("/apiExternal/toPage.html");
                    }, 1000);
                }else{
                    $custom.alert.error(state + "失败！");
                }
            });
        });
    }

    //详情
    function details(id){
    	
    	$module.revertList.serializeListForm() ;
    	$module.revertList.isMainList = true ; 
    	
        $load.a("/apiExternal/toDetailsPage.html?id="+id);
    }

    //编辑
    function edit(id){
    	
    	$module.revertList.serializeListForm() ;
    	$module.revertList.isMainList = true ; 
    	
        $load.a("/apiExternal/toEditPage.html?id="+id);
    }
</script>