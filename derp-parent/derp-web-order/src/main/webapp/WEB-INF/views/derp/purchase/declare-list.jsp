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
                                <li class="breadcrumb-item"><a href="javascript:dh_list();">预申报单</a></li>
                            </ol>
                        </div>
                    </div>
                    <form id="form1">
                        <!--  title   end  -->
                        <div class="form_box">
                            <div class="">
                                <div class="form_item h35">
                                    <span class="msg_title">预申报单号 :</span>
                                    <input class="input_msg" type="text" name="code" id="code" required="" />
                                    <span class="msg_title">入仓仓库 :</span>
                                    <select name="depotId" class="input_msg" id="depotId">
                                        <option value="">全部</option>
                                        <c:forEach items="${depotBean }" var="depot">
                                            <option value="${depot.value }">${depot.label }</option>
                                        </c:forEach>
                                    </select>
                                    <span class="msg_title">事业部 :</span>
                                    <select class="input_msg" name="buId" id="buId">
                                    </select>
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='searchData();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">订单状态 :</span>
                                    <select  class="input_msg" name="status" id="status">
                                    </select>
                                    <span class="msg_title">预计到港时间 :</span>
                                	<input type="text" class="input_msg form_datetime date-input" name="arriveStartDate" id="arriveStartDate" style="font-size:13px;">
                                    -
                                    <input type="text" class="input_msg form_datetime date-input" name="arriveEndDate" id="arriveEndDate" style="font-size:13px;">
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">采购订单编号 :</span>
                                    <input type="text" class="input_msg" type="text" name="purchaseCode" id="purchaseCode" required="" >
                                	<span class="msg_title">供应商 :</span>
                                    <select name="supplierId" class="input_msg" id="supplierId">
                                        <option value="">全部</option>
                                    </select>
                                </div>
                            </div>
                            <!-- <a href="javascript:" class="unfold">展开功能</a>  -->
                        </div>
                    </form>
                    <div class="row col-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="declare_submitDeclareOrder">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="submit-buttons">提交</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="declare_cancelDeclare">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm"  onclick="remove();">取消</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="declare_exportDeclare">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="export-button">导出</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="declare_exportCustomsDeclare">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="export-customs-button">导出清关资料</button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                    <!--  ================================表格 ===============================================   -->
                    <div class="row mt10">
                        <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <!--                             <th style="white-space:nowrap;" class="tc">序号</th> -->
                                <th>
                                    <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                        <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
                                        <span></span>
                                    </label>
                                </th>
                                <th style="white-space:nowrap;" class="tc">预申报单号</th>
                                <th style="white-space:nowrap;" class="tc">入仓仓库</th>
                                <th style="white-space:nowrap;" class="tc">事业部</th>
                                <th style="white-space:nowrap;" class="tc">供应商</th>
                                <th style="white-space:nowrap;" class="tc">采购订单编号</th>
                                <th style="white-space:nowrap;" class="tc">预计到港时间</th>
                                <th style="white-space:nowrap;" class="tc">订单状态</th>
                                <th style="white-space:nowrap;" class="tc">操作</th>
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
$module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"declareOrder_statusList",null);
    $(document).ready(function() {

        //重置按钮
        $("button[type='reset']").click(function(){
        	
        });

        $module.depot.loadSelectData.call($("select[name='depotId']")[0],"");
        $module.supplier.loadSelectData.call($("select[name='supplierId']")[0],"");
      	//事业部
        $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null, null,null);

        laydate.render({
            elem: '#arriveStartDate', //指定元素
            type: 'datetime',
            format: 'yyyy-MM-dd HH:mm:ss',
            ready: function () {
                $('.laydate-btns-time').remove();
            },
            trigger: 'click'
        });

        laydate.render({
            elem: '#arriveEndDate', //指定元素
            type: 'datetime',
            format: 'yyyy-MM-dd HH:mm:ss',
            ready: function () {
                $('.laydate-btns-time').remove();
            },
            done: function(value){
                this.dateTime.hours = 23;
                this.dateTime.minutes = 59;
                this.dateTime.seconds = 59;
            }

        });

        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url: serverAddr+'/declare/listDeclare.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
                {data:'code'},{data:'depotName'},{data:'buName'},{data:'supplierName'},{data:'purchaseCode'},{data:'arriveDate'},{data:'statusLabel'},
                { //操作
                    orderable: false,
                    width: "80px",
                    data: null,
                    render: function (data, type, row, meta) {
                        var edit = "";
                        if(row.status == '001'){
                            edit =  '<shiro:hasPermission name="declare_edit"><a href="javascript:edit('+row.id+')">编辑</a></shiro:hasPermission>';
                        }
                        var details = '<shiro:hasPermission name="declare_detail"><a href="javascript:details('+row.id+')">详情</a></shiro:hasPermission>';
                        
                        var first_info = '<shiro:hasPermission name="declare_firstInfo"><a href="javascript:first_info('+row.id+')">编辑清关资料</a></shiro:hasPermission>';
                        var first_info_details = '<shiro:hasPermission name="declare_firstInfoDetail"><a href="javascript:first_info_details('+row.id+')">清关资料详情</a></shiro:hasPermission>';
                        
                        return edit +'  '+ details + '<br/>' + first_info + '<br/>' + first_info_details;
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
        $module.load.pageOrder("act=30022&id="+id);
    }

    //编辑
    function edit(id){
        $module.load.pageOrder("act=30021&id="+id);
    }
    
    //编辑清关资料
    function first_info(id){
    	$module.load.pageOrder("act=30023&id="+id);
    }
    
    //清关资料详情
    function first_info_details(id){
    	$module.load.pageOrder("act=30024&id="+id);
    }

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

    //取消
    function remove(){
        $custom.alert.warning("你确认要取消吗？",function(){
            var ids=$mytable.fn.getCheckedRow();
            if(ids==null||ids==''||ids==undefined){
                $custom.alert.warningText("至少选择一条记录！");
                return ;
            }
            var url = serverAddr+"/declare/cancelDeclare.asyn";
            var form = {"ids":ids};
            $custom.request.ajax(url, form, function(result){
                if(result.state == '200'){
                    $custom.alert.success("取消成功！");
                    $mytable.fn.reload();
                }else{
                	if(result.expMessage != null){
						$custom.alert.error(result.expMessage);
					}else{
						$custom.alert.error(result.message);
					}
                }
            });
        });
    }

    //提交
    $("#submit-buttons").click(function(){
        var ids=$mytable.fn.getCheckedRow();
        if(ids==null||ids==''||ids==undefined){
            $custom.alert.warningText("至少选择一条记录！");
            return ;
        }
        $custom.alert.warning("确定确认选中对象？",function(){
            $custom.request.ajax(serverAddr+"/declare/submitDeclareOrder.asyn",{"ids":ids},function(data){
                if(data.state==200){
                    if(data.data=="成功"){
                        $custom.alert.success("提交成功");
                        //删除成功，重新加载页面
                        $mytable.fn.reload();
                    }else{
                        $custom.alert.error(data.data);
                    }
                }else{
                    $custom.alert.error(data.message);
                }
            });
        });
    });

    function dh_list(){
        $module.load.pageOrder("act=3002");
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
    
    //导出
    $("#export-button").click(function(){
    	var code = $("#code").val();
    	var depotId = $("#depotId").val();
    	var supplierId = $("#supplierId").val();
    	var status = $("#status").val();
    	var arriveStartDate = $("#arriveStartDate").val();
    	var arriveEndDate = $("#arriveEndDate").val();
    	var purchaseCode = $("#purchaseCode").val();
    	var buId = $("#buId").val();
        $custom.alert.warning("确定导出预申报单？",function(){
        	
        	$downLoad.downLoadByUrl(serverAddr+"/declare/exportDeclare.asyn?code="+code+"&purchaseCode="
            		+purchaseCode+"&depotId="+depotId+"&supplierId="+supplierId+"&buId="+ buId
            		+"&status="+status+"&arriveStartDate="+arriveStartDate+"&arriveEndDate="+arriveEndDate) ;
        });
    });
    
    //导出清关资料
    $("#export-customs-button").click(function(){
    	var ids=$mytable.fn.getCheckedRow();
        if(ids==null||ids==''||ids==undefined){
            $custom.alert.warningText("至少选择一条记录！");
            return ;
        }
        if(ids.indexOf(",")!=-1){
            $custom.alert.warningText("目前只支持单条数据导出！");
            return ;
        }
        
        //校验状态
        $downLoad.downLoadByUrl(serverAddr+"/declare/exportCustomsDeclare.asyn?id="+ids) ;
        
    });
</script>
