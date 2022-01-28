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
                                <li class="breadcrumb-item "><a href="javascript:list();">实时库存</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  title   end  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="">
                                <div class="form_item h35">
                                    <span class="msg_title">公司 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="merchantName">
                                    <span class="msg_title">仓库 :</span>
                                    <select name="depotId" id="depotId" class="input_msg">
                                         <%--  <c:forEach items="${depotBean }" var="depot">
                                        <option value="${depot.id }">${depot.name }</option>
                                    </c:forEach>  --%>
                                    </select>
                                    <span class="msg_title">商品货号 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" id="goodsNo"   name="goodsNo">
                                    <div class="fr"> 
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                
                                <div class="form_item h35">
                                    <span class="msg_title">条码:</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" id="barcode" name="barcode">
                                </div>
 
                            </div>
                     <%--       <a href="javascript:" class="unfold">展开功能</a>--%>
                        </div>
                    </form>
                    <div class="row col-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="inventoryRealTime_exportInventoryRealTime">
                                <button type="button" id="exportInventoryBatch" class="btn btn-find waves-effect waves-light btn-sm">导出</button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                    <!--  ================================表格 ===============================================   -->
                    <div class="row mt20">
                        <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>序号</th>
                                <th>公司</th>
                                <th style="min-width: 100px">仓库名称</th>
                                <th>商品货号</th>
                                <th>条码</th>
                                <th style="min-width: 150px">商品名称</th>
                                <th style="min-width: 100px">OPG号</th>
                                <th>生产日期</th>
                                <th>失效日期</th>
                                <th style="min-width: 120px">批次号</th>
                                <th>库存数量</th>
                                <th>冻结数量</th>
                                <th>锁定数量</th>
                                <th>可用数量</th>
                                <th>临保天数</th>
                                <th>库存类型</th>
                                <th>理货单位</th>
                                <th>托盘号</th>

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

        //加载仓库下拉
        $module.depot.loadSelectData.call($("select[name='depotId']")[0],"");


        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url: serverAddr+'/inventoryRealTime/searchInventoryRealTime.asyn?r='+Math.random(),
            columns:[
            	/* { //复选框单元格
	                className: "td-checkbox",
	                orderable: false,
	                width: "30px",
	                data: null,
	                render: function (data, type, row, meta) {
	                    return '<input type="checkbox"  value='+data.goodsNo+' name="iCheck" class="iCheck">';
	                }
	            }, */
		        {  //序号
	             data : null,
	             bSortable : false,
	             className : "text-right",
	             width : "30px",
	             render : function(data, type, row, meta) {
	                 // 显示行号
	                 var startIndex = meta.settings._iDisplayStart;
	                 return startIndex + meta.row + 1;
	                     }
		         },
	            {data:'merchantName'},{data:'depotName'},{data:'goodsNo'},{data:'barcode'},{data:'goodsName'},{data:'opgCode'},{data:'productionDate'},{data:'overdueDate'},
	            {data:'batchNo'},{data:'qty'},{data:'realFrozenStockNum'},{data:'lockStockNum'},{data:'realStockNum'},{data:'overDays'},
	            {data:'stockTypeLabel'},{data:'uomLabel'},{data:'lpn'},
            ],
            formid:'#form1'
        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();

    } );

    function searchData(){
        $mytable.fn.reload();
    }

    function list(){
        $module.load.pageInventory("bls=6007");
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
/*    $(".unfold").click(function () {
        $(".form_con").toggleClass('hauto');
        if($(this).text() == '展开功能'){
            $(this).text('收起功能');
        }else{
            $(this).text('展开功能');
        }
    });*/
    
    
         //导出
    $("#exportInventoryBatch").on("click",function(){
        //获取选中的id信息
/*          var goodsNo="";
        $("input[name='iCheck']:checked").each(function() { // 遍历选中的checkbox
        	goodsNo+=$(this).val()+",";
        });
        if(goodsNo==null||goodsNo==''||goodsNo==undefined){
            $custom.alert.warningText("请选择一条记录！");
            return ;
        }
        goodsNo=goodsNo.substring(0,goodsNo.length-1);
        */
        var depotId=$("#depotId").val();
        if(depotId==null||depotId==''||depotId==undefined){
            $custom.alert.warningText("请选择仓库！");
            return ;
        }        
        var depotId=  $("#depotId").val();
        var goodsNo=  $("#goodsNo").val();
        //var batchNo=  $("#batchNo").val();
        $custom.alert.warning("确定导出筛选的数据？",function(){
           var url=serverAddr+"/inventoryRealTime/exportInventoryRealTime.asyn?goodsNo="+goodsNo+"&depotId="+depotId;//+"&batchNo="+batchNo;
           //window.open(url);
           $downLoad.downLoadByUrl(url);
        });
    });
    
</script>
