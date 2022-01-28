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
                                <li class="breadcrumb-item "><a href="javascript:list();">效期预警</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  title   end  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="form_con">
                               <%-- <div class="form-row h35" >
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
                                                    &lt;%&ndash;    <c:forEach items="${depotBean }" var="depot">
                                                           <option value="${depot.value }">${depot.label }</option>
                                                       </c:forEach> &ndash;%&gt;
                                                </select>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="col-3 ml15">
                                        <div class="row">
                                            <label class="col-5 col-form-label"><div class="fr">商品<span>：</span></div></label>
                                            <div class="col-7 ml10">
                                                <input type="text" required="" parsley-type="text" class="form-control" name="goodsName">
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
                                            <label  class="col-5 col-form-label "><div class="fr">效期<span>：</span></div></label>
                                            <div class="col-7 ml10">
                                                <select name="validityType" class="form-control goods_select2">
                                                    <option value="">请选择</option>
                                                    <option value="1">低于1/3</option>
                                                    <option value="2">低于1/2</option>
                                                    <option value="3">高于1/2</option>
                                                    <option value="4">高于2/3</option>
                                                    <option value="5">过期</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>--%>
                                <div class="form_item h35">
                                    <span class="msg_title">公司 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" id="merchantName"  name="merchantName">
                                    <span class="msg_title">仓库 :</span>
                                    <select name="depotId" id="depotId"  class="input_msg">
                                        <option value="">请选择</option>
                                         <%-- <c:forEach items="${depotBean }" var="depot">
                                        <option value="${depot.value }">${depot.label }</option>
                                    </c:forEach> --%>
                                    </select>
                                    <span class="msg_title">商品货号:</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" id="goodsNo" name="goodsNo">
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='searchData()' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35" style="min-width:136px;max-width: 635px;display: inline-block;">
                                    <span class="msg_title">效期 :</span>
                                    <div style="min-width:136px;max-width: 635px;display: inline-block;">
                                    <select name="validityType" id="validityType" class="input_msg goods_select2">
                                        <option value="">请选择</option>
                                        <option value="1">0＜X≤1/10</option>
                                        <option value="2">1/10＜X≤1/5</option>
                                        <option value="3">1/5＜X≤1/3</option>
                                        <option value="4">1/3＜X≤1/2</option>
                                        <option value="5">1/2＜X≤2/3</option>
                                        <option value="6">2/3以上</option>                                       
                                        <option value="7">过期</option>
                                    </select>
                                    </div>
                                     <input class="input_msg" type="hidden" name="validityTypes" id="validityTypes">
                                    
                                </div>
                            </div>
                            <a href="javascript:" class="unfold">展开功能</a>
                        </div>
                    </form>
                    <div class="row col-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="inventoryWarning_exportInventoryWarning">
                                <button type="button" id="inventoryWarning" class="btn btn-find waves-effect waves-light btn-sm">导出</button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                    <!--  ================================表格 ===============================================   -->
                    <div class="mt10 table-responsive">
                        <table id="datatable-buttons" class="table table-striped twoRows" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>
                                    <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                        <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
                                        <span></span>
                                    </label>
                                </th>
                                <!--                             <th>序号</th> -->
                                <th>公司名称</th>
                                <th>仓库名称</th>
                                <th>商品货号</th>
                                <th>商品名称</th>
                                <th>生产日期</th>
                                <th>失效日期</th>
                                <th>批次号</th>
                                <th>总数量</th>
                                <th>单位</th>
                                <th>剩余失效(天)</th>
                                <th>总效期(天)</th>
                                <th>剩余失效</th>
                                <th>效期区间</th>
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

        $(".date-input").datetimepicker({
            format: 'yyyy-mm',
            autoclose: true,
            todayBtn: true,
            startView: 'year',
            minView:'year',
            maxView:'decade',
            language:  'zh-CN',
        });
        $("#validityType").select2({
   		 language:"zh-CN",
   		 placeholder: '请选择',
   		 multiple: true,
   		 closeOnSelect: true
   		});
        var depotId = '${depotId}';
        //加载仓库下拉
        $module.depot.loadSelectData.call($("select[name='depotId']")[0],depotId);


        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url: serverAddr+'/inventoryWarning/listInventoryWarning.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" value='+data.id+'  name="iCheck" class="iCheck">';
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
                {data:'merchantName'},{data:'depotName'},{data:'goodsNo'},{data:'goodsName'},{data:'productionDate'},{data:'overdueDate'},{data:'batchNo'},{data:'surplusNum'},
                {data:'unitLabel'},
                {data:'surplusDays'},{data:'totalDays'},{data:'surplusDate'},
                {data:'surplusDate',
                    'render': function (data, type, row, meta) {
                    	var surplusDays=row.surplusDays;
                    	var totalDays=row.totalDays;
                        if( surplusDays/totalDays>0&&surplusDays/totalDays<=1/10 ) {
                            return '0＜X≤1/10';
                        }else if( surplusDays/totalDays>1/10&&surplusDays/totalDays<=1/5 ) {
                            return '1/10＜X≤1/5';
                        }else if( surplusDays/totalDays>1/5&&surplusDays/totalDays<=1/3 ) {
                            return '1/5＜X≤1/3';
                        }else if( surplusDays/totalDays>1/3&&surplusDays/totalDays<=1/2 ) {
                            return '1/3＜X≤1/2';
                        }else if( surplusDays/totalDays>1/2&&surplusDays/totalDays<=2/3 ) {
                            return '1/2＜X≤2/3';
                        }else if( surplusDays/totalDays>2/3 ) {
                            return '2/3以上';
                        }else if( surplusDays/totalDays<=0 ) {
                            return '过期';
                        }{
                            return '';
                        }
                    },
                },
                
            ],
            formid:'#form1'
        };
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
            if(aData.goodsName != null && aData.goodsName.length>25){
                $('td:eq(4)', nRow).html(aData.goodsName.substring(0, 25)+"...");
                $('td:eq(4)', nRow).attr("title",aData.goodsName);
            }
        }
        //生成列表信息
        $('#datatable-buttons').mydatatables();

    } );

    function list(){
        $module.load.pageInventory("bls=6006");
    }


    function searchData(){
    	var validityTypes = $('select[name="validityType"]').val();
    	$("#validityTypes").val(validityTypes);
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
    $("#inventoryWarning").on("click",function(){
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
        var validityType=  $("#validityType").val();
        var validityTypes = $('select[name="validityType"]').val();
        
        $custom.alert.warning("确定导出筛选的数据？",function(){
            var url=serverAddr+"/inventoryWarning/exportInventoryWarning.asyn?depotId="+depotId+"&goodsNo="+goodsNo+"&validityType="+validityType+"&validityTypes="+validityTypes;
            //window.open(url);
            $downLoad.downLoadByUrl(url);
        });
    });
    $(function () {
        $("#datatable-buttons_wrapper").removeClass('container-fluid');
    })
</script>
