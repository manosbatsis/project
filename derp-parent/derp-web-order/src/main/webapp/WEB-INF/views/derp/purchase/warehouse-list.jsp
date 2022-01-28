<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
                                <li class="breadcrumb-item"><a href="#">采购档案</a></li>
                                <li class="breadcrumb-item"><a href="javascript:dh_list();">采购入库单列表</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  title   end  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                    <span class="msg_title">采购订单编码 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="purchaseCode">
                                    <span class="msg_title">入库单号 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="code">
                                    <span class="msg_title">入仓仓库 :</span>
                                    <select name="depotId" class="input_msg">
                                        <option value="">全部</option>
                                        <c:forEach items="${depotBean }" var="depot">
                                            <option value="${depot.value }">${depot.label }</option>
                                        </c:forEach>
                                    </select>
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                	<span class="msg_title">事业部 :</span>
                                    <select class="input_msg" name="buId" id="buId">
                                    </select>
                                    <span class="msg_title">单据状态 :</span>
                                    <select name="state" class="input_msg">
                                    </select>
                                    <span class="msg_title">入库时间 :</span>
                                    <input type="text" class="input_msg form_datetime date-input" name="warehouseStartDate" id="warehouseStartDate" style="font-size:13px;">
                                    -
                                    <input type="text" class="input_msg form_datetime date-input" name="warehouseEndDate" id="warehouseEndDate" style="font-size:13px;">
                                </div>
                                <div class="form_item h35">
                                	<span class="msg_title">勾稽状态 :</span>
                                    <select class="input_msg" name="correlationStatus" id="correlationStatus">
                                    </select>
                                </div>
                            </div>
                            <a href="javascript:" class="unfold">展开功能</a>
                        </div>
                    </form>
                    <div class="row col-12 mt20">
                        <shiro:hasPermission name="warehouse_import">
                            <div class="btn-group mr13">
                                <button type="button" class="btn btn-find dropdown-toggle waves-effect waves-light btn-sm" data-toggle="dropdown" aria-expanded="false">批量导入<span class="caret"></span> </button>
                                <div class="dropdown-menu" aria-labelledby="btnGroupDrop1" x-placement="bottom-start" style="position: absolute; transform: translate3d(0px, 36px, 0px); top: 0px; left: 0px; will-change: transform;">
                                    <a class="dropdown-item" href="javascript:dh_import();">入库单导入</a>
                                    <a class="dropdown-item" href="javascript:dh_relation_import();">关联采购单导入</a>
                                </div>
                            </div>
                        </shiro:hasPermission>

                        <div class="button-list">
                            <shiro:hasPermission name="warehouse_confirmDepot">
                                <input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="confirm-buttons" value="确认入仓"/>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="warehouse_exportRelation">
                                <input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="export-buttons" value="明细导出"/></button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="warehouse_checkWarehouseDepotType">
                                <input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="purchase-export-buttons" value="入库勾稽关联导出"/>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="warehouse_delBatchByIds">
                                <input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="delbatch" value="删除">
                            </shiro:hasPermission>
                        </div>
                    </div>
                    <!--  ================================表格 ===============================================   -->
                    <div class="row mt10">
                        <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>
                                    <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                        <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
                                        <span></span>
                                    </label>
                                </th>
                                <th>入库单号</th>
                                <th>仓库</th>
                                <th>事业部</th>
                                <th>采购订单编码</th>
                                <th>理货单号</th>
                                <th>进仓单生效日期</th>
                                <th>合同号</th>
                                <th>外部单号</th>
                                <th>状态</th>
                                <th>勾稽状态</th>
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
                <jsp:include page="/WEB-INF/views/modals/1011.jsp" />
                <!-- end row -->
            </div>
            <!-- container -->
        </div>
    </div>
</div> <!-- content -->
<script type="text/javascript">
$module.constant.getConstantListByNameURL.call($('select[name="state"]')[0],"purchaseWarehouse_stateList",null);
$module.constant.getConstantListByNameURL.call($('select[name="correlationStatus"]')[0],"purchaseWarehouse_correlationStatusList",null);

    $(document).ready(function() {
    	//$('[type="reset"]').click();
        //重置按钮
        $("button[type='reset']").click(function(){
        	
        });

        $module.depot.loadSelectData.call($("select[name='depotId']")[0],"");
      	//事业部
        $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null, null,null);
        
        laydate.render({
            elem: '#warehouseStartDate', //指定元素
            type: 'datetime',
            format: 'yyyy-MM-dd HH:mm:ss',
            ready: function () {
                $('.laydate-btns-time').remove();
            },
            trigger: 'click'
        });

        laydate.render({
            elem: '#warehouseEndDate', //指定元素
            type: 'datetime',
            format: 'yyyy-MM-dd HH:mm:ss',
            ready: function () {
                $('.laydate-btns-time').remove();
            },
            done: function(value){
                this.dateTime.hours = 23;
                this.dateTime.minutes = 59;
                this.dateTime.seconds = 59;
            },
            trigger: 'click'

        });

        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url: serverAddr+'/warehouse/listPurchaseWarehouse.asyn?r='+Math.random()+'wDate='+$('#wDate').val(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
                {data:'code'},{data:'depotName'},{data:'buName'},{data:'purchaseCode'},{data:'tallyingOrderCode'},{data:'inboundDate'},
                {data:'contractNo'},{data:'extraCode'},{data:'stateLabel'},{data:'correlationStatusLabel'},
                { //操作
                    orderable: false,
                    width: "120px",
                    data: null,
                    render: function (data, type, row, meta) {
                        return '<shiro:hasPermission name="warehouse_detail"><a href="javascript:details('+row.id+')">详情</a></shiro:hasPermission>'
                            + ' <shiro:hasPermission name="warehouse_toAttachment"><a href="javascript:toAttachment(\''+row.code+'\')">附件管理</a></shiro:hasPermission>';
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

    } );

    function searchData(){
        $mytable.fn.reload();
    }
    //详情
    function details(id){
        $module.load.pageOrder("act=30041&id="+id);
    }

    //导出
    $("#export-buttons").click(function(){
    	var ids=$mytable.fn.getCheckedRow();
    	
    	var params = "" ;
    	
        if(ids==null||ids==''||ids==undefined){
        	params = $('#form1').serialize() ;
        }else{
        	params = "ids="+ids ;
        }
        
        $custom.alert.warning("确定导出选中对象？",function(){
        	var url = serverAddr+"/warehouse/exportRelation.asyn?" + params;
        	$downLoad.downLoadByUrl(url);
        });
    });

    //明细导出
    $("#purchase-export-buttons").click(function(){
        var ids=$mytable.fn.getCheckedRow();
        if(ids==null||ids==''||ids==undefined){
            $custom.alert.warningText("至少选择一条记录！");
            return ;
        }
        $custom.alert.warning("确定导出选中对象？",function(){
        	//判断勾选的仓库是否包含在途仓
        	$custom.request.ajax(serverAddr+"/warehouse/checkWarehouseDepotType.asyn",{"ids":ids},function(data){
        		if(data.data == ""){
        			var url = serverAddr+"/warehouse/purchaseExportRelation.asyn?ids="+ids;
        			$downLoad.downLoadByUrl(url);
        		}else{
        			$custom.alert.error(data.data);
        		}
        	});
            
        });
    });

    //确认入仓
    $("#confirm-buttons").click(function(){
        var ids=$mytable.fn.getCheckedRow();
        if(ids==null||ids==''||ids==undefined){
            $custom.alert.warningText("至少选择一条记录！");
            return ;
        }
        $custom.alert.warning("确定入仓选中对象？",function(){
            $custom.request.ajax(serverAddr+"/warehouse/confirmDepot.asyn",{"ids":ids},function(data){
                if(data.state==200){
                    if(data.data == "成功"){
                        $custom.alert.success("确认入仓成功");
                        //成功，重新加载页面
                        setTimeout(function () {
                        	$module.load.pageOrder("act=3004");
                  		}, 1000);
                    }else{
                        $custom.alert.error(data.data);
                    }
                }else{
                	if(data.expMessage != null){
    					$custom.alert.error(data.expMessage);
    				}else{
    					$custom.alert.error(data.message);
    				}
                }
            });
        });
    });
    //删除
    $("#delbatch").click(function(){
        $custom.request.delChecked(serverAddr+'/warehouse/delBatchByIds.asyn');
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

    function dh_list(){
        $module.load.pageOrder("act=3004");
    }

    function dh_import(){
        $module.load.pageOrder("act=30042");
    }
    function dh_relation_import(){
        $module.load.pageOrder("act=30043");
    }
    //附件管理
    function toAttachment(code){
    	$module.load.pageOrder("act=200001&codeid=" + code);
    }
    // 点击展开功能
    $(".unfold").click(function () {
        $(".form_con").toggleClass('hauto');
        if($(this).text() == '展开功能'){
            $(this).text('收起功能');
        }else{
            $(this).text('展开功能');
        }
    })
        
</script>
