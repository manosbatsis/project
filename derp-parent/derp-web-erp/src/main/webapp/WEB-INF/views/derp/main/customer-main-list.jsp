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
                       <li><i class="blcok"></i> 当前位置：</li>
                       <li class="breadcrumb-item"><a href="#">客商档案</a></li>
                       <li class="breadcrumb-item "><a href="javascript:list();">待引入客商列表</a></li>
                    </ol>
                    </div>
            </div>
                   <!--  过滤条件查询  start  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                    <span class="msg_title">主数据客户ID :</span>
                                    <input class="input_msg" type="text" name="ccode" id="ccode" required="" >
                                    <span class="msg_title">客商名称 :</span>
                                    <input class="input_msg" type="text" name="cname" id="cname" required="" >
                                    <span class="msg_title">客商简称 :</span>
                                    <input class="input_msg" type="text" name="cshortname" id="cshortname" required="" >
                                    <div class="fr">
                                        <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick='searchData();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
	                                <span class="msg_title">是否供应商 :</span>
	                                <select class="input_msg" name="isSupplier">
                                     </select>
	                                <span class="msg_title">是否客户:</span>
	                                <select class="input_msg" name="isCustomer">
                                     </select>
                                     <span class="msg_title">客商类型:</span>
                                    <select class="input_msg" name="ccodetypes">
                                      </select>
                                     <span class="msg_title">状态:</span>
	                                <select class="input_msg" name="mainStatus">
                                     </select>
                                     
                                </div>
                            </div>
                            <a href="javascript:" class="unfold">展开功能</a>
                        </div>
                    </form>
                    <!-- 过滤条件查询  end  -->
                    
                    <div class="row col-12 mt20">
                    </div>
					
					<div class="row mt10">
                    <table id="datatable-buttons" class="table table-striped twoRows" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th>
                                <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                    <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
                                    <span></span>
                                </label>
                            </th>
                            <th>主数据客户ID</th>
                            <th>客商名称</th>
                            <th>客商简称</th>
                            <th>是否供应商</th>
                            <th>是否客户</th>
                            <th>客商类型</th>
                            <th>状态</th>
                            <th style="width:100px;">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- End row -->
        <!-- end row -->
    </div> <!-- container -->
</div> <!-- content -->




<script type="text/javascript">

	

    $(document).ready(function() {
    	$module.constant.getConstantListByNameURL.call($('select[name="isSupplier"]')[0],"customerMainIsList",null);
    	$module.constant.getConstantListByNameURL.call($('select[name="isCustomer"]')[0],"customerMainIsList",null);
    	$module.constant.getConstantListByNameURL.call($('select[name="mainStatus"]')[0],"customerMainStatusList",null);
    	$module.constant.getConstantListByNameURL.call($('select[name="ccodetypes"]')[0],"customerMainTypeList",null);
    	//重置按钮
        $("button[type='reset']").click(function(){
        	
        });
    	
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:'/customerMain/listCustomerMain.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
            {data:'ccode'},{data:'cname'},{data:'cshortname'},{data:'isSupplierLabel'},{data:'isCustomerLabel'},
            {data: 'ccodetypeLabel'},{data:'mainStatusLabel'},
            { //操作
                orderable: false,
                width: "100px",
                data: null,
                render: function (data, type, row, meta) {
                	var status = "";
                	if (row.mainStatus=='1') {
                		if(row.isSupplier != '1' && (row.ccodetypes == '10' || row.ccodetypes == '11')){
                    		status += '<shiro:hasPermission name="customer_main_modify"><a href="javascript:setSupplierOrCustomer('+row.id+',2)">设定为供应商</a><br></shiro:hasPermission>';
                    	}

                    	if(row.isCustomer != '1' && (row.ccodetypes == '01' || row.ccodetypes == '11')){
                    		status += '<shiro:hasPermission name="customer_main_modify"><a href="javascript:setSupplierOrCustomer('+row.id+',1)">设定为客户</a><br></shiro:hasPermission>';
                    	} 
					}
                	               	
                	
                	var details = "";
                	details = '<shiro:hasPermission name="customer_main_toDetailsPage"><a href="javascript:details('+row.id+')">详情</a><br></shiro:hasPermission>';
                    return status + details;
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
    
    //详情
    function details(id){
    	
    	$module.revertList.serializeListForm() ;
    	$module.revertList.isMainList = true ; 
    	
    	$load.a("/customerMain/toDetailsPage.html?id="+id);
    }
    
    //禁用/启用
    function setSupplierOrCustomer(id, type){
    	
    	var msg = "" ;
    	if('2' == type){
    		msg = "供应商" ;
    	}else if('1' == type){
    		msg = "客户" ;
    	}
    	
    	$custom.alert.warning("确认要设置为"+ msg +"？",function(){
	    	var form = {"id":id, "type":type};
			
	    	$module.revertList.serializeListForm() ;
	    	$module.revertList.isMainList = true ; 
	    	
	    	if('2' == type){
	    		$load.a("/supplier/toAddPage.html?mainId="+id);
	    	}else if('1' == type){
	    		$load.a("/customer/toAddPage.html?mainId="+id);
	    	}
	    	
    	});
    }
    
 	// 点击展开功能
    $(".unfold").click(function () {
        $(".form_con").toggleClass('hauto');
        if($(this).text() == '展开功能'){
            $(this).text('收起功能');
        }else{
            $(this).text('展开功能');
        }
    });
</script>