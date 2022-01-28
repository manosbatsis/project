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
                       <li class="breadcrumb-item"><a href="#">供应商档案</a></li>
                       <li class="breadcrumb-item "><a href="javascript:$load.a('/supplier/toPage.html');">供应商列表</a></li>
                    </ol>
                    </div>
            </div>
                   <!--  过滤条件查询  start  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="">
                                <div class="form_item h35">
                                    <span class="msg_title">供应商编码 :</span>
                                    <input class="input_msg" type="text" name="code" id="code"  >
                                    <span class="msg_title">供应商名称 :</span>
                                    <select class="input_msg"  name="id" id="id" title="请选择" >
                              		</select>
                                    <span class="msg_title">状态 :</span>
                                    <select class="input_msg "  name="status" id="status">
	                                </select>
                                    <div class="fr">
                                        <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick='searchData();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
	                                <span class="msg_title">主数据ID :</span>
	                                <input class="input_msg" type="text" name="mainId" id="mainId" >
	                                <span class="msg_title">公司 :</span>
	                                <select class="input_msg "  name="merchantId" id="merchantId">
		                              	<option value="">请选择</option>
	                                       <c:forEach items="${merchantList }" var="merchant">
	                                           <option value="${merchant.value }">${merchant.label }</option>
	                                       </c:forEach>
		                              </select>
	                                <span class="msg_title">数据来源 :</span>
	                                <select class="input_msg "  name="source" id="source">
	                                </select>
                                </div>
                                <a href="javascript:" class="unfold">展开功能</a>
                            </div>
                        </div>
                    </form>
                    <!-- 过滤条件查询  end  -->

                    <div class="row col-md-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="supplier_toAddPage">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="add-buttons">新增</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="supplier_delSupplier">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick="$custom.request.delChecked('/supplier/delSupplier.asyn');">删除</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="supplier_toImportPage">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="import-buttons">批量导入</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="supplier_exportSupplier">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick="exportLogs();">批量导出</button>
                            </shiro:hasPermission>

                        </div>
                    </div>
                    <div class="row mt20">
                    <table id="datatable-buttons" class="table table-striped twoRows" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th>
                                <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                    <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
                                    <span></span>
                                </label>
                            </th>
                            <th>供应商编码</th>
                            <th>供应商名称</th>
                            <th>主数据ID</th>
                            <th>合作公司</th>
                            <th>数据来源</th>
                            <th>采购币种</th>
                            <th>状态</th>
                            <th style="width: 120px">创建时间</th>
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
		<input type="hidden" id="curentMerchantId" value="${merchantId }">
        <!-- End row -->
        <!-- end row -->
    </div> <!-- container -->
</div> <!-- content -->
<jsp:include page="/WEB-INF/views/modals/3010.jsp" />
<footer class="footer text-right">
</footer>



<script type="text/javascript">
$module.constant.getConstantListByNameURL.call($('select[name="source"]')[0],"customerInfo_sourceList",null);
$module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"customerInfo_statusList",null);
//1.查询全部客户(包含启用禁用客户 客户列表用)2.查询全部供应商 (包含启用禁用供应商列表用) 3.查询启用客户(全部)4.查询启用供应商(全部) 5.查询当前商家的启用客户6.查询当前商家启用供应商
$module.customer.getCusOrSurpSelectBean.call($('select[name="id"]')[0],'2',null);
    $(document).ready(function() {
    	
    	//重置按钮
        $("button[type='reset']").click(function(){
        	$(".goods_select2").each(function(){
        		$(this).val("");
        	});
        	//重新加载select2
    		$(".goods_select2").select2({
    			language:"zh-CN",
    			placeholder: '请选择',
    			allowClear:true
    		});
    		$("#id").selectpicker('refresh')
        });
    	
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:'/supplier/listSupplier.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
            {data:'code'},{data:'name'},{data:'mainId'},{data:'merchantNameStr'},{data:'sourceLabel'},{data:'currencyLabel'},{data:'statusLabel',width:'40px'},{data:'createDate'},
            { //操作
                orderable: false,
                width: "100px",
                data: null,
                render: function (data, type, row, meta) {
                	var status = "";
                	if(row.status == '1'){
                		status = '<shiro:hasPermission name="supplier_modifyEnabledSupplier"><a href="javascript:audit('+row.id+',0)">禁用</a></shiro:hasPermission>';
                	}else{
                		status = '<shiro:hasPermission name="supplier_modifyEnabledSupplier"><a href="javascript:audit('+row.id+',1)">启用</a></shiro:hasPermission>';
                	}
                	var details = "";
                	details = '<shiro:hasPermission name="supplier_toDetailsPage"><a href="javascript:details('+row.id+')">详情</a></shiro:hasPermission>';
                    
                	var rel = "" ;
                	rel = '<shiro:hasPermission name="supplier_relMerchant"><br><a href="javascript:relMerchant('+row.id+')">合作公司</a></shiro:hasPermission>';
                	
                	return '<shiro:hasPermission name="supplier_toEditPage"><a href="javascript:edit('+row.id+')">编辑</a></shiro:hasPermission>'+ '   '+status+ '   ' + details + rel;
                }
            },
            ],
            formid:'#form1'
        };
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
            if(aData.merchantNameStr!=null&&aData.merchantNameStr.indexOf(",")!=-1){
                var itemList = aData.merchantNameStr.split(",");
                //如果有多个公司名称
                if(itemList.length>1){
                    $('td:eq(4)', nRow).html("<div name='partMerchantName'>"+aData.merchantNameStr.split(",")[0]+"</div><div name = 'showdiv' style='display:none;'>"+aData.merchantNameStr+"</div><a href='###' onclick='showHideCode(this)''>查看更多</a>");
                }
            }
        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();

    } );

    function searchData(){
        $mytable.fn.reload();
    }

    //新增
    $("#add-buttons").click(function(){
    	$load.a("/supplier/toAddPage.html");
    });
    
    //编辑
    function edit(id){
    	$module.revertList.serializeListForm() ;
    	$module.revertList.isMainList = true ; 
    	
    	$load.a("/supplier/toEditPage.html?id="+id);
    }
    
    //详情
    function details(id){
    	$module.revertList.serializeListForm() ;
    	$module.revertList.isMainList = true ; 
    	
    	$load.a("/supplier/toDetailsPage.html?id="+id);
    }
    
    //禁用/启用
    function audit(id, status){
    	$custom.alert.warning("你确认要禁用/启用吗？",function(){
	    	var url = "/supplier/modifyEnabledSupplier.asyn";
	    	var form = {"id":id, "status":status};
	    	var state = "";
	    	if(status == '1'){
	    		state = "启用";
	    	}else{
	    		state = "禁用";
	    	}
			$custom.request.ajax(url, form, function(result){
				if(result.state == '200'){
					$custom.alert.success(state + "成功！");
					setTimeout(function () {
						
						$module.revertList.serializeListForm() ;
				    	$module.revertList.isMainList = true ; 
						
						$load.a("/supplier/toPage.html");
					}, 1000);
				}else{
					$custom.alert.error(state + "失败！");
				}
			});
    	});
    }
    
    //批量导入
    $("#import-buttons").click(function(){
    	$load.a("/supplier/toImportPage.html");	
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
    })
    $('#datatable-buttons').on("change", ":checkbox", function() {
        $(this).parents('tr').toggleClass('success');
    })
    
    // 点击展开功能
    $(".unfold").click(function () {
        $(".form_con").toggleClass('hauto');
        if($(this).text() == '展开功能'){
            $(this).text('收起功能');
        }else{
            $(this).text('展开功能');
        }
    });
    
    //导出
    function exportLogs(){
    	
    	var data = $("form").serialize();
    	
   		$custom.alert.warning("是否确认导出？",function(){
   		    location.href="/supplier/exportSupplier.asyn?" + data;
   		});
    }

	function relMerchant(id){
		$m3010.init(id) ;
	}
    function showHideCode(obj){
        $(obj).prev().toggle();
        //被隐藏了
        if($(obj).prev().is(":hidden")){
            $(obj).prev().prev().show();
        }else{
            $(obj).prev().prev().hide();
        }
    }
</script>