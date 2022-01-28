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
                                <li class="breadcrumb-item"><a href="#">枚举值管理</a></li>
                                <li class="breadcrumb-item "><a href="javascript:$load.a('/enum/toPage.html');">枚举类型列表</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  过滤条件查询  start  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="form_con">
                            <%--    <div class="form-row ml15">
                                    <div class="form-group col-md-3">
                                        <div class="row">
                                            <label class="col-4 col-form-label " style="white-space:nowrap;">枚举名称<span>：</span></label>
                                            <div class="col-7 ml10">
                                                <input class="form-control" type="text" name="enumName" required="" >
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
                                </div>--%>
                                <div class="form_item h35">
                                    <span class="msg_title">枚举名称 :</span>
                                    <input class="input_msg" type="text" name="enumName" required="" >
                                    <div class="fr">
                                        <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick='searchData();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                    <!-- 过滤条件查询  end  -->

                    <div class="row col-md-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="enum_add">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick="$m6011.init();">新增</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="enum_delete">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick="$custom.request.delChecked('/enum/delEnum.asyn');">删除</button>
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
                                <th>枚举名称</th>
                                <th>备注</th>
                                <th>创建时间</th>
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
        <jsp:include page="/WEB-INF/views/modals/6011.jsp" />
        <jsp:include page="/WEB-INF/views/modals/6012.jsp" />
        <!-- End row -->
        <!-- end row -->
    </div> <!-- container -->
</div> <!-- content -->


<script type="text/javascript">


    $(document).ready(function() {
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:'/enum/listEnum.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
                /* {  //序号
                    data : null,
                    bSortable : false,
                    className : "text-right",
                    width : "30px",
                    render : function(data, type, row, meta) {
                        // 显示行号
                        var startIndex = meta.settings._iDisplayStart;
                        return startIndex + meta.row + 1;
                    }
                }, */
                {data:'enumName'},{data:'remark'},{data:'createDate'},
                { //操作
                    orderable: false,
                    width: "130px",
                    data: null,
                    render: function (data, type, row, meta) {
                        var details = "";
                        details = '<shiro:hasPermission name="enum_toDetailsPage"><a href="javascript:details('+row.id+')">详情</a></shiro:hasPermission>';
                        var edit = "";
                        edit = '<shiro:hasPermission name="enum_edit"><a href="javascript:$m6012.init('+row.id+')">编辑</a></shiro:hasPermission>';
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
            $load.a("/customer/toAddPage.html");
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
    	
        $load.a("/enum/toDetailsPage.html?id="+id);
    }

</script>