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
                                <li class="breadcrumb-item"><a href="#">库存管理</a></li>
                                <li class="breadcrumb-item "><a href="javascript:list();">商品冻结和解冻明细列表</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  title   end  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                    <span class="msg_title">公司 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="merchantName">
                                    <span class="msg_title">仓库 :</span>
                                    <select name="depotId"  id="depotId" class="input_msg">
                                        <option value="">请选择</option>
                                  <%--       <c:forEach items="${depotBean }" var="depot">
                                        <option value="${depot.value }">${depot.label }</option>
                                    </c:forEach> --%>
                                    </select>
                                    <span class="msg_title">商品货号 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" id="goodsNo" name="goodsNo">
                                    <div class="fr">
                                        <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">单据类型 :</span>
                                    <select name="status"   id="status" class="input_msg">
                                    </select>
                                     <span class="msg_title">来源单据号 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" id="orderNo"  name="orderNo">
                                    <span class="msg_title">业务单据号 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg"  id="businessNo" name="businessNo">
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">操作类型 :</span>
                                    <select name="operateType"   id="operateType" class="input_msg">
                                    </select>
                                </div>
                            </div>
                            <a href="javascript:;" class="unfold">展开功能</a>
                        </div>

                    </form>
                    <div class="row col-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="inventoryFreezeDetails_exportInventoryFreezeDetail">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="exportInventoryDetails" >导出</button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                    <!--  ================================表格 ===============================================   -->
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
                                <!--                             <th>序号</th> -->
                                <th>公司</th>
                                <th>仓库名称</th>
                                <th>商品货号</th>
                                <th style="min-width: 220px">商品名称</th>
                                <th>单据类型</th>
                                <th>来源单据号</th>
                                <th>业务单据号</th>
                                <th>单据日期</th>                                
                                <th>数量</th>
                                <th>单位</th>
                                <th>操作类型</th>
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
$module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"inventory_sourceTypeList",null);
$module.constant.getConstantListByNameURL.call($('select[name="operateType"]')[0],"inventoryFreeze_operateTypeList",null);

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
        });

        $(".date-input").datetimepicker({
            language:  'zh-CN',
            format: "yyyy-mm-dd",
            autoclose: 1,
            todayHighlight: 1,
            startView: 2,
            minView: 2
        });

        //加载仓库下拉
        $module.depot.loadSelectData.call($("select[name='depotId']")[0],"");


        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url: serverAddr+'/inventoryFreezeDetails/listInventoryFreezeDetails.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
        
                    return '<input type="checkbox" value='+data.id+' name="iCheck" class="iCheck">';
                }
            },
//         {  //序号
//             data : null,
//             bSortable : false,
//             className : "text-right",
//             width : "30px",
//             render : function(data, type, row, meta) {
//                 // 显示行号
//                 var startIndex = meta.settings._iDisplayStart;
//                 return startIndex + meta.row + 1;
//                     }
//         },
                {data:'merchantName'},{data:'depotName'}
                ,{data:'goodsNo'}, {data:'goodsName'},{data:'status',
                    'render': function (data, type, row, meta) {
                    	if( data == '001'){
                            return '采购入库';
                        }else if( data == '002'){
                            return '销售订单';
                        }else if( data == '003'){
                            return '销售出库';
                        }else if( data == '004'){
                            return '销售退货订单';
                        }else if( data == '005'){
                            return '销售退货入库';
                        }else if( data == '006'){
                            return '销售退货出库';
                        }else if( data == '007'){
                            return '电商订单';
                        }else if( data == '008'){
                            return '电商订单出库';
                        }else if( data == '009'){
                            return '调拨订单';
                        }else if( data == '0010'){
                            return '调拨入库';
                        }else if( data == '0011'){
                            return '调拨出库';
                        }else if( data == '0012'){
                            return '月结损益';
                        }else if( data == '0013'){
                            return '销毁';
                        }else if( data == '0014'){
                            return '其他出入';
                        }else if( data == '0015'){
                            return '盘点结果单';
                        }else if( data == '0016'){
                            return '好坏互转';
                        }else if( data == '0017'){
                            return '货号变更';
                        }else if( data == '0018'){
                            return '效期调整';
                        }else{
                            return "";
                        }

                    },
                },{data:'orderNo'},{data:'businessNo'},{data:'divergenceDate'},{data:'num'},
                {data:'unit',
                    'render': function (data, type, row, meta) {
                        if( data == '0' ) {
                            return '托盘';
                        }else if( data == '1' ) {
                            return '箱';
                        }else if( data == '2' ) {
                            return '件';
                        }{
                            return '';
                        }
                    },
                },
                {data:'operateType',
                    'render': function (data, type, row, meta) {
                        if( data == '0' ) {
                            return '冻结';
                        }else if( data == '1' ) {
                            return '解冻';
                        }else{
                            return '';
                        }
                    },
                },
            ],
            formid:'#form1'
        }
        //生成列表信息
        $('#datatable-buttons').mydatatables();

    } );

    function list(){
        $module.load.pageInventory("bls=6017");
    }


    function searchData(){
        $mytable.fn.reload();
    }
    /**
     * 全选
     */
    $('#datatable-buttons').on("change", ":checkbox", function() {
        // 列表复选框
        if ($(this).is("[name='keeperUserGroup-checkable']")) {
            // 全选
            $(":checkbox", '#datatable-buttons').prop("checked",$(this).prop("checked"));
            if($(this).is(':checked')){
                $('#datatable-buttons tbody tr').addClass('success');
            }else{
                $('#datatable-buttons tbody tr').removeClass('success');
                $(this).parents('tr').toggleClass('success');
            }
        }else{
            // 一般复选
            var checkbox = $("tbody :checkbox", '#datatable-buttons');
            $(":checkbox[name='cb-check-all']", '#datatable-buttons').prop('checked', checkbox.length == checkbox.filter(':checked').length);
            $(this).parents('tr').toggleClass('success');
        }
    });
    // 点击展开功能
    $(".unfold").click(function () {
        $(".form_con").toggleClass('hauto');
        if($(this).text() == '展开功能'){
            $(this).text('收起功能');
        }else{
            $(this).text('展开功能');
        }
    })
    
     //导出
    $("#exportInventoryDetails").on("click",function(){
        //获取选中的id信息
/*         var ids="";
        $("input[name='iCheck']:checked").each(function() { // 遍历选中的checkbox
            ids+=$(this).val()+",";
        });
        if(ids==null||ids==''||ids==undefined){
            $custom.alert.warningText("请选择一条记录！");
            return ;
        }
        ids=ids.substring(0,ids.length-1); */
        
        var depotId=  $("#depotId").val();
        var goodsNo=  $("#goodsNo").val();
        var status=  $("#status").val();
        var orderNo=  $("#orderNo").val();
        var businessNo=  $("#businessNo").val();
        var urlParm="depotId="+depotId+"&goodsNo="+goodsNo+"&status="+status+"&orderNo="+orderNo+"&businessNo="+businessNo;
        $custom.alert.warning("确定导出筛选的数据？",function(){
            var url=serverAddr+"/inventoryFreezeDetails/exportInventoryFreezeDetails.asyn?"+urlParm;
            //window.open(url);
            $downLoad.downLoadByUrl(url);
        });
    });
</script>
