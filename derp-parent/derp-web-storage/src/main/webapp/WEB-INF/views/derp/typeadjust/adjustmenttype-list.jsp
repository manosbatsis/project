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
                                <li class="breadcrumb-item"><a href="#">仓储管理</a></li>
                                <li class="breadcrumb-item"><a href="javascript:dh_list();">类型调整列表</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  title   end  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                    <span class="msg_title"> 调整单号:</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="code">
                                    <span class="msg_title">入仓仓库 :</span>
                                    <select name="depotId" id="depotId" class="input_msg">
                                        <option value="">请选择</option>
                                    </select>
                                    <span class="msg_title">单据状态 :</span>
                                    <select class="input_msg" name="status">
                                    </select>
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">业务类别 :</span>
                                    <select name="model" class="input_msg">
                                    </select>
                                    <span class="msg_title">单据来源 :</span>
                                    <select name="source" class="input_msg">
                                    </select>
                                    <span class="msg_title">调整时间:</span>
                                    <input type="text" class="input_msg form_datetime date-input" name="adjustmentStartTime" id="adjustmentStartTime">
                                    -
                                    <input type="text" class="input_msg form_datetime date-input" name="adjustmentEndTime" id="adjustmentEndTime">

                                </div>
                            </div>
                            <a href="javascript:;" class="unfold">展开功能</a>
                        </div>
                    </form>
                    <div class="row mt20 col-md-12">
                       <div class="button-list">
   							<shiro:hasPermission name="adjustmentType_import">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="import-buttons">导入</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="adjustmentType_deleteAdjustment">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick="deleteObject();">删除</button>
                            </shiro:hasPermission>
                           <shiro:hasPermission name="adjustmentType_export">
                               <input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="exportAdjustmentType" value="导出"/>
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
                                <th>公司</th>
                                <th>类型调整单号</th>
                                <th>业务类别</th>
                                <th>来源单据号</th>
                                <th>仓库名称</th>
                                <th>调整时间</th>
                                <th>单据来源</th>
                                <th>单据状态</th>
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
</div> <!-- content -->
<jsp:include page="/WEB-INF/views/modals/10012.jsp" />
<script type="text/javascript">
$module.constant.getConstantListByNameURL.call($('select[name="model"]')[0],"adjustmentType_modelList",null);
$module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"adjustmentType_statusList",null);
$module.constant.getConstantListByNameURL.call($('select[name="source"]')[0],"adjustmentType_sourceList",null); 
    $(document).ready(function() {

        laydate.render({
            elem: '#adjustmentStartTime', //指定元素
            type: 'datetime',
            format: 'yyyy-MM-dd HH:mm:ss',
            ready: function () {
                $('.laydate-btns-time').remove();
            }
        });

        laydate.render({
            elem: '#adjustmentEndTime', //指定元素
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
        });

        //加载仓库
        $module.depot.getSelectBeanByMerchantRel.call($("select[name='depotId']")[0],null, {});

        //重新加载select2
        $(".goods_select2").select2({
            language:"zh-CN",
            placeholder: '请选择',
            allowClear:true
        });

        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url: serverAddr+'/adjustmentType/listAdjustment.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
                {data:'merchantName'},{data:'code'},{data:'modelLabel'},{data:'sourceCode'},{data:'depotName'},{data:'adjustmentTime'},{data:'sourceLabel'},{data:'statusLabel'},
                { //操作
                    orderable: false,
                    width: "130px",
                    data: null,
                    render: function (data, type, row, meta) {
                        var caoZuoHtml = '<shiro:hasPermission name="adjustmentType_detail"><a href="javascript:details('+row.id+')">详情</a></shiro:hasPermission>';
                        // 1、对于接口回传且单据状态为待调整的单据可操作项为：分配事业部、详情；
                        if (row.source=='01' && row.status == '020') {
                            caoZuoHtml += '<shiro:hasPermission name="inventoryType_confirm">&nbsp;&nbsp;<a href="javascript:updateBuName('+row.id + ',' + row.depotId +')">分配事业部</a></shiro:hasPermission>';
                        }
                        // 2、对于手工导入且单据状态为待调整的单据可操作项为：确认调整、详情；
                         if (row.source=='02' && row.status == '020') {
                            caoZuoHtml += '<shiro:hasPermission name="adjustmentType_confirm">&nbsp;&nbsp;<a href="javascript:confirm('+row.id +')">确认调整</a></shiro:hasPermission>';
                        }
                        return caoZuoHtml;
                    }
                },],
            formid:'#form1'
        }
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
        	/*if (aData.model == '1'){
                $('td:eq(3)', nRow).html('好坏品互转（批次无变更）');
            }else if (aData.model == '2'){
                $('td:eq(3)', nRow).html('货号变更');
            }else if (aData.model == '3'){
            	$('td:eq(3)', nRow).html('效期调整（批次无变更）');
            }else if (aData.model == '4'){
            	$('td:eq(3)', nRow).html('大货理货');
            }else if (aData.model == '5'){
            	$('td:eq(3)', nRow).html('自然过期');
            }
            if (aData.status == '019'){
                $('td:eq(7)', nRow).html('已调整');
            }else if (aData.status == '020'){
                $('td:eq(7)', nRow).html('待调整');
            }else if (aData.status == '022'){
                $('td:eq(7)', nRow).html('处理中');
            }*/
            
        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();

    } );

    function searchData(){
        $mytable.fn.reload();
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
    });
    //导入
    $("#import-buttons").click(function(){
    	$module.load.pageStorage("act=100012");
    });
    //详情
    function details(id){
        $module.load.pageStorage("act=100011&id="+id);
    }

    function dh_list(){
        $module.load.pageStorage("act=10001");
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
    // 分配事业部，展示所有商品
    function updateBuName(id, depotId) {
        $m10012.init(id,depotId);
    }
    // 删除
    function deleteObject(){
    	$custom.request.delChecked(serverAddr+'/adjustmentType/deleteAdjustment.asyn');
    }
    // 确认调整
    function confirm(id){
    	 var url = serverAddr + "/adjustmentType/confirmAdjustment.asyn";
         $custom.request.ajax(url, {"id": id}, function (data) {
        	 console.log(data);
             if (data.state == 200) {
                 $custom.alert.success('确认成功');
                 // 确认调整成功，重新加载页面
				setTimeout(function () {
					$load.a(pageUrl+"10001");
				}, 1000);
             } else {
                $custom.alert.error(data.message);
             }
         });
    }

    //导出
    $('#exportAdjustmentType').click(function () {
        var param = $("#form1").serialize();
        $custom.alert.warning("确定导出？",function(){
            //导出
            var url = serverAddr+"/adjustmentType/exportAdjustmentType.asyn?"+param;
            $downLoad.downLoadByUrl(url);
        });
    });
</script>
