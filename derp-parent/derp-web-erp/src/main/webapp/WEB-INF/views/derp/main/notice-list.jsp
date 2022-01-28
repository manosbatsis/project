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
                                <li class="breadcrumb-item"><a href="#">系统管理</a></li>
                                <li class="breadcrumb-item "><a href="javascript:$load.a('/notice/toPage.html');">公告管理</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  过滤条件查询  start  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                    <span class="msg_title">公告类型 :</span>
                                    <select id="type" name="type" class="input_msg">
                                    </select>
                                    <span class="msg_title">发布时间 :</span>
                                    <input type="text" class="input_msg form_datetime date-input" name="publishStartDateStr" id="publishStartDateStr">
                                    -
                                    <input type="text" class="input_msg form_datetime date-input" name="publishEndDateStr" id="publishEndDateStr">
                                    <span class="msg_title">状态 :</span>
                                    <select id="status" name="status" class="input_msg">
                                    </select>
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
                        <shiro:hasPermission name="notice_add">
                            <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="add-buttons" onclick="$load.a('/notice/toEditPage.html');">新建公告</button>
                        </shiro:hasPermission>
                    </div>
                    <div class="row mt20">
                        <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th class="tc">公告类型</th>
                                <th class="tc">标题</th>
                                <th class="tc">状态</th>
                                <th class="tc">创建日期</th>
                                <th class="tc">发布日期</th>
                                <th class="tc">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="/WEB-INF/views/modals/3001.jsp" />
        <!-- End row -->
        <!-- end row -->
    </div> <!-- container -->
</div> <!-- content -->



<script type="text/javascript">
$module.constant.getConstantListByNameURL.call($('select[name="type"]')[0],"notice_typelist",null);
$module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"notice_statuslist",null);

	$(".date-input").datetimepicker({
	    language:  'zh-CN',
	    format: "yyyy-mm-dd",
	    autoclose: 1,
	    todayHighlight: 1,
	    startView: 2,
	    minView: 2
	});

    $(document).ready(function() {

        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:'/notice/listNotice.asyn?r='+Math.random(),
            columns:[
                {data:'typeLabel'},
                {
                	data:'title',
                	render: function (data, type, row, meta) {
                		return '<a href= "javascript:detail('+row.id+')" >'+data+'</a>' ;
                	}
                },
                {data:'statusLabel'},{data:'createDate'},{data:'publishDate'},
                { //操作
                    orderable: false,
                    width: "100px",
                    data: null,
                    render: function (data, type, row, meta) {
                    	
                    	var html = "" ;
                    	
                    	if(row.status == '001'){
                    		html += '<shiro:hasPermission name="notice_edit"><a href="javascript:edit('+row.id+')">编辑</a></shiro:hasPermission>  ' +
                    		'<shiro:hasPermission name="notice_publish"><a href="javascript:changeStatus('+row.id+' , \'002\')" style="color:#32CD32;">发布</a></shiro:hasPermission>  ' +
                            '<shiro:hasPermission name="notice_del"><a href="javascript:changeStatus('+row.id+', \'006\')" style="color:red;">删除</a></shiro:hasPermission>' ;
                    	}
                    	
                    	return html ;
                    	
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
    	$module.revertList.serializeListForm() ;
    	$module.revertList.isMainList = true ;
    	$load.a('/notice/toEditPage.html?id='+id);
    }
    
    //详情
    function detail(id){
    	$m3001.init(id) ;
    }
    
    //删除
    function changeStatus(id,status){
    	
    	var msg = "" ;
    	if(status == "006"){
    		msg = "是否删除该公告" ;
    	}else if(status == "002"){
    		msg = "是否发布该公告" ;
    	}else{
    		return ;
    	}
    	
        $custom.alert.warning(msg , function(){
            var url = "/notice/modifyStatus.asyn" ;
            
            $custom.request.ajax(url,{"id":id,"status":status},function(data) {
                if (data.state == 200) {
                    $custom.alert.success(data.message);
                    //重新加载
                    $module.revertList.serializeListForm() ;
                    $module.revertList.isMainList = true ;
                    setTimeout( "$load.a('/notice/toPage.html')", 1000) ;
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