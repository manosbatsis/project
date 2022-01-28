<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!-- Start content  -->
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
                                <li class="breadcrumb-item"><a href="#">公司档案</a></li>
                                <li class="breadcrumb-item "><a href="javascript:$load.a('/businessUnit/toPage.html');">事业部管理</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  过滤条件查询  start  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                    <span class="msg_title">事业部编码 :</span>
                                    <input class="input_msg" type="text" name="code" >
                                    <span class="msg_title">事业部名称 :</span>
                                    <input class="input_msg" type="text" name="name" >
                                    <span class="msg_title">部门名称 :</span>
                                    <input class="input_msg" type="text" name="departmentName" >
                                    <div class="fr">
                                        <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick='searchData();'>查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light reset btn_default">重置</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                    <!-- 过滤条件查询  end  -->

                    <div class="row col-md-12 mt20">
                        <shiro:hasPermission name="businessUnit_add">
                            <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="add-buttons" onclick="$m10001.init(0, null);">新增</button>
                        </shiro:hasPermission>
                    </div>
                    <div class="row mt20">
                        <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th class="tc">事业部编码</th>
                                <th class="tc">事业部名称</th>
                                <th class="tc">部门名称</th>
                                <th class="tc">编辑时间</th>
                                <th class="tc">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
                <jsp:include page="/WEB-INF/views/modals/10001.jsp" />
            </div>
        </div>

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
            url:'/businessUnit/listBusinessUnits.asyn?r='+Math.random(),
            columns:[
                {data:'code'},{data:'name'},{data:'departmentName'},{data:'modifyDate'},
                { //操作
                    orderable: false,
                    width: "100px",
                    data: null,
                    render: function (data, type, row, meta) {
                        return '<shiro:hasPermission name="businessUnit_edit"><a href="javascript:edit('+row.id+')">编辑</a></shiro:hasPermission>  ' +
                        '<shiro:hasPermission name="apiExternal_delBusinessUnits"><a href="javascript:del('+row.id+')">删除</a></shiro:hasPermission>';
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

    });

    function searchData(){
        $mytable.fn.reload();
    }

    //编辑
    function edit(id){
        $m10001.init(1, id);
    }
    
    //删除
    function del(id){
    	$custom.alert.warning("是否删除该事业部信息" , function(){
    		var url = "/businessUnit/delBusinessUnits.asyn" ;
        	
        	$custom.request.ajax(url,{"id":id},function(data) {
                if (data.state == 200) {
                	$custom.alert.success(data.message);
                    //重新加载table表格
                	$mytable.fn.reload();
                } else {
                	
                	if(data.state == 313){
                		$custom.alert.error("该事业部已被引用，无法删除");
                		
                		return ;
                	}
                	
                    $custom.alert.error(data.message);
                }
            });
    	}) ;
    	
    }

</script>