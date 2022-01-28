<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">

        <div class="row">
            <!--  title   start  -->
            <div class="col-12">
                <div class="card-box table-responsive">
                    <div class="col-12 row">
                        <div>
                            <ol class="breadcrumb">
                                <li><i class="block"></i> 当前位置：</li>
                                <li class="breadcrumb-item"><a href="#">系统管理</a></li>
                                <li class="breadcrumb-item active"><a href="javascript:$load.a('/user/toPage.html');">权限管理</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  title   end  -->
                    <!--  过滤条件查询  start  -->
                    <!--  ================================过滤条件 start  ===============================================   -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                    <span class="msg_title">权限名称 ：</span>
                                    <input class="input_msg" type="text" name="authorityName" >
                                    <div class="fr">
                                        <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                    <!--  ================================过滤条件  end===============================================   -->
                    <!-- 过滤条件查询  end  -->
                    <div class="row col-12">
                        <div class="button-list mt20">
                            <shiro:hasPermission name="permission_add">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default" onclick='$m1031.init();'>新增</button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                    <!--  ================================表格 ===============================================   -->
                    <div class="row w100 mt10">
                        <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>权限编码</th>
                                <th>权限名称</th>
                                <th>状态</th>
                                <th>权限类型</th>
                                <th>授权码</th>
                                <th>权限URL</th>
                                <th>子服务URL</th>
                                <th>备注</th>
                                <th>创建时间</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                    <!--  ================================表格  end ===============================================   -->
                </div>
            </div>
            <!--======================Modal框===========================  -->
            <jsp:include page="/WEB-INF/views/modals/1031.jsp" />
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
            url:'/permission/list.asyn?r='+Math.random(),
            columns:[
                {data:'id'},{data:'authorityName'},{data:'isEnabled'},{data:'type'},{data:'permission'},{data:'url'},{data:'serverAddr'},{data:'remark'},{data:'createDate'},
                { //操作
                    orderable: false,
                    width: "30px",
                    data: null,
                    render: function (data, type, row, meta) {
                        var delURL='\'/permission/del.asyn\'';
                        return '<shiro:hasPermission name="permission_del"><a  href="javascript:;"  onclick="$custom.request.del('+row.id+','+delURL+')">删除</a></shiro:hasPermission>';
                    }
                },
            ],
            formid:'#form1'
        };

        //每一行都进行回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
            if (aData.isEnabled == '1'){
                $('td:eq(2)', nRow).html('启用');
            }else{
                $('td:eq(2)', nRow).html('禁用');
            }
            if (aData.type == '1'){
                $('td:eq(3)', nRow).html('菜单');
            }else{
                $('td:eq(3)', nRow).html('按钮');
            }
        };
        //生成列表信息s
        $('#datatable-buttons').mydatatables();

    } );


</script>