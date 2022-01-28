<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<jsp:include page="/WEB-INF/views/common.jsp" />
<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
          <div class="row">
            <div class="col-12">
                <div class="card-box">
                        <div class="col-12 row">
                            <div>
                                <ol class="breadcrumb">
                                    <li><i class="block"></i> 当前位置：</li>
                                    <li class="breadcrumb-item"><a href="javascript:dh_list()">SD类型配置</a></li>
                                </ol>
                           </div>
                        </div>
                        <!-- 筛选条件开始 -->
                        <form id="form1">
                        <div class="form_box">
                            <div class="form_con">
                               
                                <div class="form_item h35">
                                    <span class="msg_title">SD名称 :</span>
                                    <input type="text"  parsley-type="text" class="input_msg" name="sdTypeName">
                                    <span class="msg_title">简称 :</span>
                                    <input type="text"  parsley-type="text" class="input_msg" id="sdSimpleName" name="sdSimpleName">
                                   <span class="msg_title">配置类型 :</span>
                                   <select class="input_msg" name="type" id="type"  >
                                    <option value="">请选择</option>
                                    </select>
                                  <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='searchData();'>查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                    <!-- 筛选条件结束 -->
                        <div class="row col-md-12 mt20">
                            <div class="button-list">
                                <shiro:hasPermission name="sdTypeConfig_add">
                                    <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="add-buttons">新增</button>
                                </shiro:hasPermission>
                            </div>
                        </div>
                        <div class="row mt10 table-responsive">
                            <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                                <thead>
                                <tr>
	                                <th><label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
	                                    <input type="checkbox" name="keeperUserGroup-checkable"
	                                           class="group-checkable" /> <span></span>
	                                </label></th>
                                    <th style="white-space:nowrap;">SD类型</th>
                                    <th style="white-space:nowrap;">简称</th>
                                    <th style="white-space:nowrap;">配置类型</th>
                                    <th style="white-space:nowrap;">创建时间</th>
                                    <th style="white-space:nowrap;">创建人</th>
                                    <th style="white-space:nowrap;">状态</th>
                                    <th style="white-space:nowrap;">操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <!--======================Modal框===========================  -->
                    <!-- end row -->
			</div>
		</div>
	</div>
</div>
                <!-- container -->
<jsp:include page="/WEB-INF/views/modals/20001.jsp" />
<script type="text/javascript">

	$module.constant.getConstantListByNameURL.call($('select[name="type"]')[0],"sdtypeconfig_typeList",null);


	//加载状态
	$(document).ready(function() {
	    //初始化
	    $mytable.fn.init();
	    //配置表格参数
	    $mytable.params={
	        url : serverAddr+'/sdTypeConfig/sdTypeConfigList.asyn?r=' + Math.random(),
	        columns:[{ //复选框单元格
	            className: "td-checkbox",
	            orderable: false,
	            width: "30px",
	            data: null,
	            render: function (data, type, row, meta) {
	                 return '<input type="checkbox" class="iCheck">';
	            }
	        },
	        {data : 'sdTypeName'},{data : 'sdSimpleName'},{data : 'typeLabel'},{data : 'createDate'},{data : 'creator'},
	        {data : 'statusLabel'},
	            { //操作
	                orderable: false,
	                width: "130px",
	                data: null,
	                render: function (data, type, row, meta) {   
	                	var edit = "";
	                	edit='<shiro:hasPermission name="sdTypeConfig_edit"><a href="javascript:edit('+row.id+')">编辑</a></shiro:hasPermission> &nbsp';
	                    return edit;
	                }
	            },
	        ],
	        formid:'#form1'
	    }
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
		$m20001.init(id) ;
	}
	//新增
	$("#add-buttons").click(function(){	
		$m20001.init() ;
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
    }) ;
    $('#datatable-buttons').on("change", ":checkbox", function() {
        $(this).parents('tr').toggleClass('success');
    }) ;
	

</script>
