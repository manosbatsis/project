<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
                                <li class="breadcrumb-item "><a href="javascript:$load.a('/brand/toPage.html');">品牌管理列表</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  过滤条件查询  start  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                    <!-- <span class="msg_title">关联品牌：</span>
                                    <input class="input_msg" type="text" name="parentName" required="" > -->
                                    <span class="msg_title">品牌名称：</span>
                                    <input class="input_msg" type="text" name="name" required="" >
                                    <!-- <span class="msg_title">匹配状态：</span>
                                    <select class="input_msg" name="isMatching">
                                    	<option value="">请选择</option>
                                    	<option value="1">已匹配</option>
                                    	<option value="0">未匹配</option>
                                    </select> -->
                                    <div class="fr">
                                        <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick='searchData();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default ">重置</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                    <!-- 过滤条件查询  end  -->

                   <div class="row col-md-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="brand_export">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="export-buttons">导出</button>
                            </shiro:hasPermission>
<%--                            <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="add-buttons">匹配</button>--%>
<%--                            <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="delete-buttons">解除匹配</button>--%>
                        </div>
                    </div>
                    <div class="row mt20">
                        <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                 <th>
                                    <!--<label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                        <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
                                        <span></span>
                                    </label>-->
                                </th> 
                                <th>品牌名称</th>
                                <th>中文品牌名称</th> 
                                <th>英文品牌名称</th>
                                <th>标准品牌</th>
                                <th>操作人</th>
                                <th>修改时间</th>
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
        <jsp:include page="/WEB-INF/views/modals/14001.jsp" />
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
            url:'/brand/list.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
                {data:'name'},{data:'chinaBrandName'},{data:'englishBrandName'},{data:'parentName'},{data:'operatorName'},{data:'modifyDate'},
                { //操作
                    orderable: false,
                    width: "100px",
                    data: null,
                    render: function (data, type, row, meta) {
                        return '<shiro:hasPermission name="brand_modify"><a href="#" onclick="$m14001.init('+row.id+');">编辑标准品牌</a></shiro:hasPermission>';
                    }
                },
            ],
            formid:'#form1'
        };
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
        	if (aData.isMatching != '1'){
        		$('td:eq(0)', nRow).html('');
        	}
        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();

        //匹配
        /* $("#add-buttons").click(function(){
            $load.a("/brand/toMatchingPage.html");
        }); */
        
        //解除匹配
        /* $("#delete-buttons").click(function(){
        	var ids=$mytable.fn.getCheckedRow();
            if(ids==null||ids==''||ids==undefined){
                $custom.alert.warningText("至少选择一条记录！");
                return ;
            }
            $custom.alert.warning("确定解除匹配选中对象？",function(){
	            $custom.request.ajax("/brand/saveUnMatching.asyn", {ids:ids}, function(result){
	            	if(result.state == '200'){
	            		$custom.alert.success("解除匹配成功");
						//删除成功，重新加载页面
						$mytable.fn.reload();
	            	}else{
						if(data.expMessage != null){
							$custom.alert.error(data.expMessage);
						}else{
							$custom.alert.error(data.message);
						}
					}
	            });
            });
        }); */

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

    $("#export-buttons").click(function () {
        var param = $("#form1").serialize();
        $custom.alert.warning("确定导出？",function(){
            //导出
            var url = "/brand/exportBrand.asyn?"+param;
            $downLoad.downLoadByUrl(url);
        });
    });
</script>