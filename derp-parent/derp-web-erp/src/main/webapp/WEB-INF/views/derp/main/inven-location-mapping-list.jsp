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
                                <li class="breadcrumb-item"><a href="#">爬虫配置</a></li>
                                <li class="breadcrumb-item "><a href="#">库位映射表</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  过滤条件查询  start  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                	<span class="msg_title">公司 :</span>
                                    <select name="merchantId" class="input_msg" id="merchantId" >
                                        <option value="">请选择</option>
                                        <c:forEach items="${merchantList }" var="merchant">
                                            <option value="${merchant.value }">${merchant.label }</option>
                                        </c:forEach>
                                    </select>
                                    <span class="msg_title">原货号：</span>
                                    <input class="input_msg" type="text" name="originalGoodsNo" >
                                    <span class="msg_title">库位类型：</span>
                                    <select name="type" class="input_msg" id="type" >
                                    </select>
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
                            <shiro:hasPermission name="inventoryLocationMapping_add">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick='$m1503.init();'>新增</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="inventoryLocationMapping_del">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick="del();">删除</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="inventoryLocationMapping_export">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="export-buttons" >导出</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="inventoryLocationMapping_import">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="import-buttons" onclick="$load.a('/list/menu.asyn?act=15031');">导入</button>
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
                                <th>公司</th>
                                <th>原货号</th>
                                <th>商品名称</th>
                                <th>库位货号</th>
                                <th>库位类型</th>
                                <th>是否指定唯一</th>
                                <th>编辑日期</th>
                                <th>编辑人</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <jsp:include page="/WEB-INF/views/modals/1503.jsp" />
        </div>
        <div class="popupbg" style="display: none">
		    <div class="popup_box">
		        <img src="/resources/assets/images/uploading.gif" alt="">
		        <p>正在校验是否被使用中...</p>
		    </div>
		</div>
        <!-- End row -->
        <!-- end row -->
    </div> <!-- container -->
</div> <!-- content -->

<script type="text/javascript">
	$module.constant.getConstantListByNameURL.call($('select[name="type"]'),"invenlocaitonMapping_TypeList",null);

    $(document).ready(function() {
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:'/inventoryLocationMapping/listInventoryLocationMapping.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
		                className: "td-checkbox",
		                orderable: false,
		                width: "30px",
		                data: null,
		                render: function (data, type, row, meta) {
		                    return '<input type="checkbox" class="iCheck">';
		                }
		            },
                {data:'merchantName'},{data:'originalGoodsNo'},{data:'goodsName'},{data:'goodsNo'},
                {data:'typeLabel'},
                {data:'isorRappointLabel'},
                {data:null,
                	render: function(data, type, row, meta){
                		
                		if(row.modifyDate){
                			return row.modifyDate ;
                		}else if(row.createDate){
                			return row.createDate ;
                		}
                		
                	}
                },
                {data:null,
                	render: function(data, type, row, meta){
                		
                		if(row.modifyName){
                			return row.modifyName ;
                		}else if(row.createName){
                			return row.createName ;
                		}
                		
                	}
                },
                { //操作
                    orderable: false,
                    width: "130px",
                    data: null,
                    render: function (data, type, row, meta) {
                    	var edit = "";
                    	edit +='<shiro:hasPermission name="inventoryLocationMapping_modify"><a href="#" onclick="$m1503.init('+row.id+');">编辑</a><br></shiro:hasPermission> '; 	
                    	if (data.isorRappoint=='0') {
                        	edit += '<shiro:hasPermission name="inventoryLocationMapping_updateIsorRappoint"><a href="javascript:updateIsorRappoint('+row.id+')">指定为唯一出库货号</a> <br></shiro:hasPermission>';							
						}
                    	return edit;
                    }
                }
            ],
            formid:'#form1'
        };
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){ };
        //生成列表信息
        $('#datatable-buttons').mydatatables();
        
    });
    
    function searchData(){
        $mytable.fn.reload();
    }
    
    function del(){
    	$custom.request.delChecked('/inventoryLocationMapping/deleteInventoryLocationMapping.asyn');
    }
    
    $("#export-buttons").click(function(){
		var url = "/inventoryLocationMapping/exportInventoryLocationMapping.asyn?" ;
		//获取选中的id信息
		var ids=$mytable.fn.getCheckedRow();
		if(ids){
			url += "ids=" + ids;
		}else{
			url += $("#form1").serialize();
		}
		
		$custom.alert.warning("确定导出选中对象？",function(){
			$downLoad.downLoadByUrl(url) ;
        });
	}) ;
    
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
    
    //修改为未转结
	function updateIsorRappoint(id){
	$custom.alert.warning("确定确定指定唯一出库货号吗？",function(){
		$custom.request.ajax("/inventoryLocationMapping/updateIsorRappoint.asyn",{"id":id},function(data){
			
				if(data.state==200){
		    		if(data.data.status=='00'){
		 			   $custom.alert.success("修改成功");
				      	//重新加载页面
				     	$mytable.fn.reload();
		    		}else{
		    			$custom.alert.error(data.data.errorMessage);
		    		}
			    }else{
			    	$custom.alert.error(data.message);
			    }
			
		})
		
	})
}

    
</script>