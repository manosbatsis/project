<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

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
                                    <li class="breadcrumb-item"><a href="javascript:$load.a('/reptile/toPage.html');">爬虫数据列表</a></li>
                                </ol>
                           </div>
                        </div>
                        <!-- 筛选条件开始 -->
                        <form id="form1">
                        <div class="form_box">
                            <div class="">
                                <div class="form_item h35">
                                    <span class="msg_title">使用平台 :</span>
                                    <select class="input_msg" name="platformType" id="platformType"></select>
                                    <span class="msg_title">用户名 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="loginName">
                                    <span class="msg_title">创建时间 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="createDate">
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='searchData();'>查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                	<span class="msg_title">店铺名称 :</span>
                                    <select class="input_msg" name="shopId" id="shopId"></select>
                                </div>
                            </div>
                        </div>
                    </form>
                    <!-- 筛选条件结束 -->
                        <div class="row col-md-12 mt20">
                            <div class="button-list">
                                <shiro:hasPermission name="reptile_toAddPage">
                                    <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="add-buttons">新增</button>
                                </shiro:hasPermission>
                                <shiro:hasPermission name="reptile_delReptile">
                                    <button type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick="$custom.request.delChecked('/reptile/delReptile.asyn');">删除</button>
                                </shiro:hasPermission>
                            </div>
                        </div>
                        <div class="mt20">
                            <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                                <thead>
                                <tr>
		                            <th>
		                                <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
		                                    <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
		                                    <span></span>
		                                </label>
		                            </th>
                                    <th style="white-space:nowrap;">使用平台</th>
                                    <th style="white-space:nowrap;">用户名</th>
                                    <th style="white-space:nowrap;">关联公司</th>
                                    <th style="white-space:nowrap;">关联客户</th>
                                    <th style="white-space:nowrap;">出仓库</th>
                                    <th style="white-space:nowrap;">入仓库</th>
                                    <th style="white-space:nowrap;">店铺名称</th>
                                    <th style="white-space:nowrap;">爬取账单时点</th>
                                    <th style="white-space:nowrap;">创建时间</th>
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
<script type="text/javascript">
	$module.constant.getConstantListByNameURL.call($('select[name="platformType"]')[0],"crawler_typeList",null);
	$(document).ready(function() {
	    //初始化
	    $mytable.fn.init();
	    //配置表格参数
	    $mytable.params={
	        url : '/reptile/listReptile.asyn?r=' + Math.random(),
	        columns:[{ //复选框单元格
	            className: "td-checkbox",
	            orderable: false,
	            width: "30px",
	            data: null,
	            render: function (data, type, row, meta) {
	                return '<input type="checkbox" class="iCheck">';
	            }
	        },
	        {data : 'platformName'},{data : 'loginName'},{data : 'merchantName'},{data : 'customerName'},
	        {data : 'outDepotName'},{data : 'inDepotName'},{data : 'shopName'},{data : 'timePoint'},{data : 'createDate'},
	            { //操作
	                orderable: false,
	                width: "130px",
	                data: null,
	                render: function (data, type, row, meta) {
	                	var details = "";
	                	details = ' <shiro:hasPermission name="reptile_toDetailsPage"><a href="javascript:details('+row.id+')">详情</a></shiro:hasPermission>';
	                    return '<shiro:hasPermission name="reptile_toEditPage"><a href="javascript:edit('+row.id+')">编辑</a></shiro:hasPermission>   '+'   '+details;
	                }
	            },
	        ],
	        formid:'#form1'
	    };
	    //生成列表信息
	    $('#datatable-buttons').mydatatables();
	
	    //新增
	    $("#add-buttons").click(function(){
	        $load.a("/reptile/toAddPage.html");
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
	    });
	    $('#datatable-buttons').on("change", ":checkbox", function() {
	        $(this).parents('tr').toggleClass('success');
	    });
	    
	    getShopList() ;
	});

	function searchData(){
	    $mytable.fn.reload();
	}
	
	//详情
	function details(id){
		
		$module.revertList.serializeListForm() ;
		$module.revertList.isMainList = true ; 
		
	    $load.a("/reptile/toDetailsPage.html?id="+id);
	}

	//编辑
	function edit(id){
		
		$module.revertList.serializeListForm() ;
		$module.revertList.isMainList = true ; 
		
	    $load.a("/reptile/toEditPage.html?id="+id);
	}
	
	//新增
	$("#add-buttons").click(function(){
		$load.a("/reptile/toAddPage.html");
	});
	$(function () {
	    $("#datatable-buttons_wrapper").removeClass('container-fluid');
	})
	
	function getShopList(){
		$custom.request.ajax("/reptile/getShopList.asyn", {}, function(result){
			if(result.state == '200'){
				
				if(result.data){
					
					var html = "<option value=''>请选择</option>" ;
					
					$(result.data).each(function(index, item){
						html += "<option value='" + item.id + "'>"+ item.shopName +"</option>" ;
					}) ;
					
					$("#shopId").html(html) ;
				}
				
				
			}else{
				$custom.alert.error(result.data.message);
			}
		});
	}

</script>
