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
                       <li><i class="dripicons-location"></i> 当前位置：</li>
                       <li class="breadcrumb-item"><a href="#">配置管理</a></li>
                       <li class="breadcrumb-item "><a href="javascript:$load.a('/external/toPage.html');">配置信息列表</a></li>
                    </ol>
                    </div>
            </div>
                   <!--  过滤条件查询  start  -->
                    <form id="form1" >
                        <div class="form-row ml15">
                            <div class="form-group col-md-3">
                                <div class="row">
                                    <label class="col-4 col-form-label " style="white-space:nowrap;">使用对象<span>：</span></label>
                                    <div class="col-7 ml10">
                                        <input class="form-control" type="text" name="useObject" required="" >
                                    </div>
                                </div>
                            </div>
                            <div class="form-group col-md-3">
                                <div class="row">
                                    <label class="col-4 col-form-label" style="white-space:nowrap;">名称<span>：</span></label>
                                    <div class="col-7 ml10">
                                        <input class="form-control" type="text" name="name" required="" >
                                    </div>
                                </div>
                            </div>
                            <div class="form-group col-md-3">
                                <div class="row">
                                    <label class="col-4 col-form-label" style="white-space:nowrap;">key<span>：</span></label>
                                    <div class="col-7 ml10">
                                        <input class="form-control" type="text" name="key" required="" >
                                    </div>
                                </div>
                            </div>
                            <div class="form-group col-md-1">
                                <div class="row">
                                    <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick='searchData();' >查询</button>
                                </div>
                            </div>
                            <div class="form-group col-md-1">
                                <div class="row">
                                    <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                </div>
                            </div>
                        </div>
                    </form>
                    <!-- 过滤条件查询  end  -->

                    <div class="row col-md-12 mt20 ml15">
                        <div class="button-list">
                            <shiro:hasPermission name="external_toAddPage">
                                <button type="button" class="btn btn-add waves-effect waves-light btn-sm" id="add-buttons">新增</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="external_delExternal">
                                <button type="button" class="btn btn-add waves-effect waves-light btn-sm" onclick="$custom.request.delChecked('/external/delExternal.asyn');">删除</button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                    <div class="row col-md-12 mt20">
                    <table id="datatable-buttons" class="table table-striped table-bordered" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th>
                                <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                    <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
                                    <span></span>
                                </label>
                            </th>
                            <th>使用对象</th>
                            <th>名称</th>
                            <th>key</th>
                            <th>value</th>
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
            url:'/external/listExternal.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
            {data:'useObject'},{data:'name'},{data:'key'},{data:'value'},
            { //操作
                orderable: false,
                width: "130px",
                data: null,
                render: function (data, type, row, meta) {
                	var details = "";
                	details = '<shiro:hasPermission name="external_toDetailsPage"><a href="javascript:details('+row.id+')">详情</a></shiro:hasPermission>';
                	var edit = "";
                	edit = '<shiro:hasPermission name="external_toEditPage"><a href="javascript:edit('+row.id+')">编辑</a></shiro:hasPermission>';
                    return edit + " " +details;
                }
            },
            ],
            formid:'#form1'
        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();

        //新增
        $("#add-buttons").click(function(){
        	$load.a("/external/toAddPage.html");
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
    
    //详情
    function details(id){
    	
    	$module.revertList.serializeListForm() ;
    	$module.revertList.isMainList = true ; 
    	
    	$load.a("/external/toDetailsPage.html?id="+id);
    }

    //编辑
    function edit(id){
    	
    	$module.revertList.serializeListForm() ;
    	$module.revertList.isMainList = true ; 
    	
    	$load.a("/external/toEditPage.html?id="+id);
    }
</script>