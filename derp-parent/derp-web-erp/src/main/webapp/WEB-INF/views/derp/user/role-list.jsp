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
                                <li class="breadcrumb-item active"><a href="javascript:$load.a('/user/toPage.html');">角色管理</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  title   end  -->
                    <!--  过滤条件查询  start  -->
                    <!--  ================================过滤条件 start  ===============================================   -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="form_con">
                     <%--           <div class="form-row ml15 h35">
                                    <div class="col-3">
                                        <div class="row">
                                            <label  class="col-4 col-form-label"  style="white-space:nowrap;">员工姓名<span>：</span></label>
                                            <div class="col-7">
                                                <input class="form-control" type="text" name="name" required="" >
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group col-2">
                                        <div class="row">
                                            <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                            <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                        </div>
                                    </div>
                                </div>--%>
                                <div class="form_item h35">
                                    <span class="msg_title">角色名称 :</span>
                                    <input class="input_msg" type="text" name="name" required="" >
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
                    <div class="row col-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="role_add">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick='$m1021.init();'>新增</button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                    <!--  ================================表格 ===============================================   -->
                    <div class="row mt10">
                        <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>角色id</th>
                                <th>角色名称</th>
                                <th>备注</th>
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
            <!--  新增  -->
            <jsp:include page="/WEB-INF/views/modals/1021.jsp" />
             <!--  分配权限   -->
            <jsp:include page="/WEB-INF/views/modals/1022.jsp" />
            <!-- 授予用户 -->
            <jsp:include page="/WEB-INF/views/modals/1023.jsp" />
        </div>
        <!-- End row -->
        <!-- end row -->
    </div> <!-- container -->
</div> <!-- content -->



<script type="text/javascript">

   // $('#role-modal').modal('show');
    $(document).ready(function() {
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:'/role/list.asyn?r='+Math.random(),
            columns:[
                {data:'id'},{data:'name'},{data:'remark'},
                { //操作
                    orderable: false,
                    width: "180px",
                    data: null,
                    render: function (data, type, row, meta) {
                        var addPermission='\'/user/delUser.asyn\'';
                        var delURL='\'/role/del.asyn\'';
                        return '<shiro:hasPermission name="role_assignPermissions"><a  href="javascript:;"  onclick="$m1022.fn.init('+row.id+')">分配权限</a>&nbsp;&nbsp;</shiro:hasPermission>'+
                         '<shiro:hasPermission name="role_del"><a  href="javascript:;"  onclick="$custom.request.del('+row.id+','+delURL+')" class="red">删除</a>&nbsp;&nbsp;</shiro:hasPermission>'+
                         '<shiro:hasPermission name="role_grantUser"><a  href="javascript:;"  onclick="$m1023.init('+row.id+')">授予用户</a></shiro:hasPermission>';
                    }
                },
            ],
            formid:'#form1'
        };
        //生成列表信息s
        $('#datatable-buttons').mydatatables();

    } );


</script>