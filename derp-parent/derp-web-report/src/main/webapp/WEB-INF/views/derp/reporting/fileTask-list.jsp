<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<jsp:include page="/WEB-INF/views/common.jsp" />
<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->

        <div class="row">
            <!--  title   start  -->
            <div class="col-12">
                <div class="card-box">
                    <div class="col-12 row">
                        <div>
                            <ol class="breadcrumb">
                                <li><i class="block"></i> 当前位置：</li>
                                <li class="breadcrumb-item"><a href="#">报表管理</a></li>
                                <li class="breadcrumb-item"><a href="javascript:$load.a('/transferIn/toPage.html');">报表任务列表</a></li>
                            </ol>
                        </div>
                    </div>
                    <form id="form1">
                        <!--  title   end  -->
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                    <span class="msg_title">任务类型:</span>
                                    <select name="taskType" class="input_msg">
                                    </select>

                                    <span class="msg_title">任务模块:</span>
                                    <select name="module" class="input_msg">
                                    </select>

                                    <!-- <span class="msg_title">仓库 :</span>
                                    <select name="depotId" class="input_msg">
                                       <option value="">请选择</option>
                                    </select> -->
                                    <span class="msg_title">年月 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg date-input"
                                           name="ownMonth">

                                    <div class="fr">
                                        <button type="button"
                                                class="btn ml15 btn-find waves-effect waves-light btn_default"
                                                onclick='$mytable.fn.reload();'>查询
                                        </button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                     
                    <!--  ================================表格 ===============================================   -->
                     <div class="table-responsive">
                        <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>序号</th>
                                <th>所属模块</th>
                                <th>任务类型</th>
                              <!--   <th>仓库名称</th> -->
                                <th>归属月份</th>
                                <th>状态</th>
                                <th>描述</th>
                                <th>操作员</th>
                                <th>创建时间</th>
                                <th>结束时间</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                    <!--  ================================表格  end ===============================================   -->
                </div>
                <!--======================Modal框===========================  -->
                <!-- end row -->
            </div>
            <!-- container -->
        </div>
    </div>
</div>
<script type="text/javascript">
    $module.constant.getConstantListByNameURL.call($('select[name="taskType"]')[0], "fileTask_taskTypeList", null);
    $module.constant.getConstantListByNameURL.call($('select[name="module"]')[0], "fileTask_moduleList", null);
    //加载仓库
//$module.depot.loadSelectData.call($('select[name="depotId"]')[0]);
$(document).ready(function() {
    $(".date-input").datetimepicker({
		 format: 'yyyy-mm',
	        autoclose: true,
	        todayBtn: true,
	        startView: 'year',
	        minView:'year',
	        maxView:'decade',
	        language:  'zh-CN',
   });
	
	//初始化
    $mytable.fn.init();
    //配置表格参数
    $mytable.params={
    		url:serverAddr+'/fileTask/fileTaskList.asyn?r='+Math.random(),
        columns: [{  //序号
            data: null,
            bSortable: false,
            className: "",
            render: function (data, type, row, meta) {
                // 显示行号
                var startIndex = meta.settings._iDisplayStart;
                return startIndex + meta.row + 1;
            }
        },
            {data: 'moduleLabel'}, {data: 'taskName'}, {data: 'ownMonth'}, {data: 'stateLabel'}, {data: 'remark'}, {data: 'username'}, {data: 'createDate'}, {data: 'modifyDate'},
            { //操作
                orderable: false,
                width: "80px",
                data: null,
                render: function (data, type, row, meta) {
                    //任务状态 1-待执行 2-执行中 3-已完成 4-失败
                    if (row.state == '3') {
                        return '<shiro:hasPermission name="fileTask_downLoad"><a href="javascript:download(\'' + row.code + '\')">下载报表</a></shiro:hasPermission>';
                    } else {
                        return '';
                    }
                }
            },
        ],
        formid: '#form1'
    };
    //每一行都进行 回调
    /* $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
    	//任务状态 1-待执行 2-执行中 3-已完成 4-失败
        if(aData.state=='1'){
        	$('td:eq(3)', nRow).html('待执行');
        }else if(aData.state=='2'){
        	$('td:eq(3)', nRow).html('执行中');
        }else if(aData.state=='3'){
        	$('td:eq(3)', nRow).html('已完成');
        }else if(aData.state=='4'){
        	$('td:eq(3)', nRow).html('失败');
        }
    }; */
    //生成列表信息
    $('#datatable-buttons').mydatatables();
});

    //下载
    function download(code) {
        //导出
        var url = serverAddr + "/fileTask/downLoad.asyn?code=" + code;
        $downLoad.downLoadByUrl(url);
    }

</script>
