<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<jsp:include page="/WEB-INF/views/common.jsp" />
<!-- Start content -->
<style>
    .laydateStart,.laydateEnd{
        font-size: 13px;
    }
</style>
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
                                <li class="breadcrumb-item "><a href="javascript:list();">商品收发明细列表</a></li>
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
                                    </select>
                                    <span class="msg_title">事业部:</span>
                                    <select name="buId" class="input_msg" id="buId">
                                        <option value="">请选择</option>
                                    </select>
                                    <div class="fr">
                                        <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                               		 <span class="msg_title">商品货号 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" id="goodsNo" name="goodsNo">                                
                                   <!--  <span class="msg_title">开始时间 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg form_datetime date-input"  id="startTime" name="startTime">
                                    <span class="msg_title">结束时间 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg form_datetime date-input" id="endTime" name="endTime">
                                     -->
                                    <span class="msg_title">归属时间 :</span>
                                    <input type="text"  name="startTime" id="startTime" class="laydateStart">
                                    -
                                    <input type="text"   name="endTime" id="endTime" class="laydateEnd">
                                </div>
                                <div class="form_item h35">
                                  	<span class="msg_title">批次号 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg"  id="batchNo"  name="batchNo">                               
                                    <span class="msg_title">来源单据号 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" id="orderNo"  name="orderNo">
                                    <span class="msg_title">业务单据号 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg"  id="businessNo" name="businessNo">
                                </div>
                                <div class="form_item h35">
									<span class="msg_title">事物类型 :</span>
                                    <select name="thingStatus"   id="thingStatus" class="input_msg">
                                    </select>
                                    <span class="msg_title">条码:</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" id="barcode"  name="barcode">
                                    <span class="msg_title">标准条码:</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg"  id="commbarcode" name="commbarcode">
                                    
                                </div>
                                 <div class="form_item h35">		                              
                                      <span class="msg_title">订单范围 :</span>
                                      <select class="input_msg" name="orderTimeRange" id="orderTimeRange">
                                      		<option value ="1">近12个月数据</option>
                                      		<option value ="2">12个月以前数据</option>
                                      </select>
                                      <span class="msg_title">操作类型 :</span>
                                      <select class="input_msg" name="operateType" id="operateType">                                   
                                      </select>
                                  </div>                               
                                    
                            </div>
                            <a href="javascript:" class="unfold">展开功能</a>
                        </div>

                    </form>
                    <div class="row col-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="inventoryDetails_exportInventoryDetails">
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
                                <th>事业部</th>
                                <th>事物类型</th>
                                <th style="min-width: 100px;">来源单据号</th>
                                <th style="min-width: 100px;">业务单据号</th>
                                <th>变更日期</th>
                                <th>商品货号</th>
                                <th style="min-width: 100px;">条码</th>
                                <th style="min-width: 100px;">标准条码</th>
                                <th style="min-width: 220px">商品名称</th>
                                <th>批次号</th>
                                <th>数量</th>
                                <th>单位</th>
                                <th>库存类型</th>
                                <th>是否过期</th>
                                <th>库位货号</th>
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
$module.constant.getConstantListByNameURL.call($('select[name="thingStatus"]')[0],"inventory_thingStatusList",null);
$module.constant.getConstantListByNameURL.call($('select[name="operateType"]')[0],"inventory_operateTypeList",null);
$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null, {});
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

        $(".laydateStart").each(function(){
     	   laydate.render({
    	        elem: this, //指定元素
    	        type: 'datetime',
    	        format: 'yyyy-MM-dd HH:mm:ss',
    	        ready: function () {
    	            $('.laydate-btns-time').remove();
    	        }
    	    });
     	});        
        $(".laydateEnd").each(function(){
          	 laydate.render({
       	        elem: this, //指定元素
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
          });
        /* $(".date-input").datetimepicker({
            language:  'zh-CN',
            format: "yyyy-mm-dd HH:mm:ss",
            autoclose: 1,
            todayHighlight: 1,
            startView: 2,
            minView: 2
        }); */

        //加载仓库下拉
        $module.depot.loadSelectData.call($("select[name='depotId']")[0],"");


        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url: serverAddr+'/inventoryDetails/listInventoryDetails.asyn?r='+Math.random(),
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
                {data:'merchantName'},{data:'depotName'},{data:'buName'},{data:'thingStatusLabel'},{data:'orderNo'},{data:'businessNo'},{data:'divergenceDate'},{data:'goodsNo'},
                {data:'barcode'},{data:'commbarcode'},{data:'goodsName'},{data:'batchNo'},{data:'num'},
                {data:'unitLabel'},
                {data:'typeLabel'},{data:'isExpireLabel'},{data:'locationGoodsNo'},
                {data:'operateTypeLabel'},
            ],
            formid:'#form1'
        };
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
            if(aData.goodsName != null && aData.goodsName != undefined && aData.goodsName.length > 30){
                $('td:eq(11)', nRow).html("<span title='" + aData.goodsName + "'>" + aData.goodsName.substring(0, 30) + "...</span>");
            }
        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();

    } );

    function list(){
        $module.load.pageInventory("bls=6003");
    }


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
        var orderTimeRange=  $("#orderTimeRange").val();
        var depotId=  $("#depotId").val();
        var goodsNo=  $("#goodsNo").val();
        var batchNo=  $("#batchNo").val();
        var startTime=  $("#startTime").val();
        var endTime=  $("#endTime").val();
        var thingStatus=  $("#thingStatus").val();
        var operateType=  $("#operateType").val();       
        var orderNo=  $("#orderNo").val();
        var businessNo=  $("#businessNo").val();
        var buId=  $("#buId").val();
        var barcode=  $("#barcode").val();
        var commbarcode=  $("#commbarcode").val();
        var urlParm="depotId="+depotId+"&goodsNo="+goodsNo+"&batchNo="+batchNo+"&startTime="+startTime+"&endTime="+endTime+"&thingStatus="+thingStatus+"&orderNo="+orderNo+"&businessNo="+businessNo+"&buId="+buId+"&barcode="+barcode+"&commbarcode="+commbarcode+"&orderTimeRange="+orderTimeRange+"&operateType="+operateType;
        $custom.alert.warning("确定导出筛选的数据？",function(){
            var url=serverAddr+"/inventoryDetails/exportInventoryDetails.asyn?"+urlParm;

            $downLoad.downLoadByUrl(url);
        });
    });
</script>
