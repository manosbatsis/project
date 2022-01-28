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
                                <li class="breadcrumb-item"><a href="#">品牌管理</a></li>
                                <li class="breadcrumb-item "><a href="javascript:$load.a('/brandSuperior/toPage.html');">母品牌列表</a></li>
                            </ol>
                        </div>
                    </div>
                    <ul class="nav nav-tabs">
                        <shiro:hasPermission name="brandParentList">
                            <li class="nav-item">
                                <a href="#" data-toggle="tab" class="nav-link"  onclick="javascript:$load.a('/brandParent/toPage.html');">标准品牌</a>
                            </li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="brandSuperiorList">
                            <li class="nav-item">
                                <a href="#" data-toggle="tab" class="nav-link active"  onclick="javascript:$load.a('/brandSuperior/toPage.html');">母品牌</a>
                            </li>
                        </shiro:hasPermission>
                    </ul>
                    <!--  过滤条件查询  start  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                    <span class="msg_title">母品牌：</span>
                                    <input class="input_msg" type="text" name="name" >
                                   

                                    <div class="fr">
                                        <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default ">重置</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                    <!-- 过滤条件查询  end  -->

                    <div class="row col-md-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="brandSuperior_add">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick='$m2012.init();'>新增</button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                    <div class="row mt20">
                        <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>母品牌</th>
                                <th>NC编码</th>
                                <th>申报系数</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <jsp:include page="/WEB-INF/views/modals/2012.jsp" />
        </div>
    </div> <!-- container -->
</div> <!-- content -->

<script type="text/javascript">
    $(document).ready(function() {
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:'/brandSuperior/list.asyn?r='+Math.random(),
            columns:[
                {data:'name'},{data:'ncCode'},{data:'priceDeclareRatio'},
                { //操作
                    orderable: false,
                    width: "130px",
                    data: null,
                    render: function (data, type, row, meta) {
                        return '<shiro:hasPermission name="brandSuperior_modify"><a href="#" onclick="$m2012.init('+row.id+');">编辑</a></shiro:hasPermission> '+
                        '<shiro:hasPermission name="brandSuperior_delete"><a href="#" onclick="$m2012.del('+row.id+');">删除</a></shiro:hasPermission>';
                    }
                }
            ],
            formid:'#form1'
        };
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();
        
    });

    
</script>