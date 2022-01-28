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
                                <li class="breadcrumb-item "><a href="javascript:list();">库存商品快照列表</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  title   end  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="form_con">
                              <%--  <div class="form-row h35" >
                                    <div class="col-3">
                                        <div class="row">
                                            <label class="col-5 col-form-label"><div class="fr">公司<span>：</span></div></label>
                                            <div class="col-7 ml10">
                                                <input type="text" required="" parsley-type="text" class="form-control" name="merchantName">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-3">
                                        <div class="row">
                                            <label  class="col-5 col-form-label "><div class="fr">仓库<span>：</span></div></label>
                                            <div class="col-7 ml10">
                                                <select name="depotId" class="form-control goods_select2">
                                                    <option value="">请选择</option>
                                                    &lt;%&ndash; <c:forEach items="${depotBean }" var="depot">
                                                        <option value="${depot.value }">${depot.label }</option>
                                                    </c:forEach> &ndash;%&gt;
                                                </select>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="col-3 ml15">
                                        <div class="row">
                                            <label class="col-5 col-form-label"><div class="fr">商品货号<span>：</span></div></label>
                                            <div class="col-7 ml10">
                                                <input type="text" required="" parsley-type="text" value="${goodsNo}"  class="form-control" name="goodsNo">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group col-2">
                                        <div class="row">
                                            <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                            <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-row h35">
                                    <div class="col-3">
                                        <div class="row">
                                            <label  class="col-5 col-form-label "><div class="fr">批次<span>：</span></div></label>
                                            <div class="col-7 ml10">
                                                <input type="text" required="" parsley-type="text" class="form-control" name="batchNo">
                                            </div>
                                        </div>
                                    </div>
                                </div>--%>
                                <div class="form_item h35">
                                <span class="msg_title">快照日期:</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg form_datetime date-input"  id="snapshotDate" name="snapshotDate">
                                    <span class="msg_title">仓库 :</span>
                                    <select name="depotId" class="input_msg" id="depotId">
                                        <option value="">请选择</option>
                                        <%--  <c:forEach items="${depotBean }" var="depot">
                                        <option value="${depot.value }">${depot.label }</option>
                                    </c:forEach> --%>
                                    </select>
                                    <span class="msg_title">商品货号 :</span>
                                    <input type="text" required="" parsley-type="text" value="${goodsNo}" id="goodsNo" class="input_msg" name="goodsNo">
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">公司 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="merchantName">
                                	<span class="msg_title">商品条码 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="barcode">
                                    <span class="msg_title">品牌 :</span>
                                    <select name="brandId" id="brandId"  class="input_msg">
                                        <option value="">请选择</option>
                                    </select>
                                </div>
                            </div>
                            <a href="javascript:" class="unfold">展开功能</a>
                        </div>
                    </form>
                    <div class="row col-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="inventoryGoodsSnapshot_exportInventoryGoodSnapshot">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="exportInventoryBatch" >导出</button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                    <!--  ================================表格 ===============================================   -->
                    <div class="table-responsive">
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
                                <th>商品名称</th>
                                <th>商品条码</th>
                                <th>品牌名称</th>
                                <th>库存数量</th>
                                <th>冻结数量</th>
                                <th>坏品数量</th>
                                <th>过期数量</th>
                                <th> 可用量</th>                              
                                <th>理货单位</th>
                                <th>快照时间</th>
                                
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
    	 var yesterday = new Date(new Date());
         var y = yesterday.getFullYear();
         var m = yesterday.getMonth()+1;//获取当前月份的日期
         var d = yesterday.getDate();
         var day = y + "-" + (m<10?'0'+m:m) + "-" + d;
         $("#snapshotDate").val(day);
         
    	$module.brand.loadSelectData.call($("select[name='brandId']")[0],"");
    	 $(".date-input").datetimepicker({
             language:  'zh-CN',
             format: "yyyy-mm-dd",
             autoclose: 1,
             todayHighlight: 1,
             startView: 2,
             minView: 2
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
        var depotId= '${depotId}';
        //加载仓库下拉
        $module.depot.loadSelectData.call($("select[name='depotId']")[0],"");
       $("#depotId").val(depotId);
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url: serverAddr+'/inventoryGoodsSnapshot/listInventoryGoodsSnapshot.asyn?r='+Math.random(),
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
                {data:'merchantName'},{data:'depotName'},{data:'goodsNo'},{data:'goodsName'},{data:'barcode'},{data:'brandName'},
                {data:'surplusNum'},{data:'freezeNum'},{data:'badNum'},{data:'expireNum'},{data:'availableNum'},
                {data:'unitLabel'},{data:'createDate'},
            ],
            formid:'#form1'
        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();

        $("#datatable-buttons_wrapper").removeClass('container-fluid');
    } );

    function list(){
        $module.load.pageInventory("bls=6015");
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
    });
    
     //导出
    $("#exportInventoryBatch").on("click",function(){
        //获取选中的id信息
  /*       var ids="";
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
        var snapshotDate=  $("#snapshotDate").val();
        $custom.alert.warning("确定导出筛选的数据？",function(){
           var url=serverAddr+"/inventoryGoodsSnapshot/exportInventoryGoodsSnapshotModelMap.asyn?depotId="+depotId+"&goodsNo="+goodsNo+"&snapshotDate="+snapshotDate;
           //window.open(url);
           $downLoad.downLoadByUrl(url);
        });
    });
    $(function () {
        $("#datatable-buttons_wrapper").removeClass('container-fluid');
    })
    
</script>
