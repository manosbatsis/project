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
                                <li class="breadcrumb-item "><a href="javascript:list">库存期初列表</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  title   end  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="form_con">
                                <%--<div class="form-row h35" >
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
                                    <div class="col-3">
                                        <div class="row">
                                            <label class="col-6 col-form-label"><div class="fr">商品编码<span>：</span></div></label>
                                            <div class="col-6 ml10">
                                                <input type="text" required="" parsley-type="text" class="form-control" name="goodsCode">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-3 ml15">
                                        <div class="row">
                                            <label class="col-5 col-form-label"><div class="fr">商品名称<span>：</span></div></label>
                                            <div class="col-7 ml10">
                                                <input type="text" required="" parsley-type="text" class="form-control" name="goodsName">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group col-1">
                                        <div class="row">
                                            <button type="button" class="btn ml15 btn-find waves-effect waves-light" onclick='$mytable.fn.reload();' >查询</button>
                                        </div>
                                    </div>
                                    <div class="col-1">
                                        <div class="row">
                                            <button type="reset" class="btn btn-light ml15 waves-effect waves-light">重置</button>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-row h35">
                                    <div class="col-3">
                                        <div class="row">
                                            <label  class="col-5 col-form-label "><div class="fr">商品条码<span>：</span></div></label>
                                            <div class="col-7 ml10">
                                                <input type="text" required="" parsley-type="text" class="form-control" name="barcode">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-3">
                                        <div class="row">
                                            <label class="col-6 col-form-label"><div class="fr">库存类型<span>：</span></div></label>
                                            <div class="col-6 ml10">
                                                <select name="type" class="form-control goods_select2">
                                                    <option value="">请选择</option>
                                                    <option value="0">好品</option>
                                                    <option value="1">坏品</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-3 ml15">
                                        <div class="row">
                                            <label class="col-5 col-form-label"><div class="fr">数据来源<span>：</span></div></label>
                                            <div class="col-7 ml10">
                                                <select name="source" class="form-control goods_select2">
                                                    <option value="">请选择</option>
                                                    <option value="1">录入/导入</option>
                                                    <option value="2">OP</option>
                                                    <option value="3">OFC</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>--%>
                                <div class="form_item h35">
                                    <span class="msg_title">仓库 :</span>
                                    <select name="depotId" class="input_msg">
                                        <option value="">请选择</option>
                                     <%--   <c:forEach items="${depotBean }" var="depot">
                                        <option value="${depot.value }">${depot.label }</option>
                                    </c:forEach>  --%>
                                    </select>
                                    <span class="msg_title">商品名称 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="goodsName">
                                    <span class="msg_title">商品货号:</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="goodsNo">
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">商品条码 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="barcode">
                                    <span class="msg_title">库存类型 :</span>
                                    <select name="type" class="input_msg">
                                    </select>
                                    <span class="msg_title">数据来源 :</span>
                                    <select name="source" class="input_msg">
                                    </select>
                                    <span class="msg_title">状态 :</span>
                                    <select name="state" class="input_msg">
                                    </select>
                                </div>
                                <div class="form_item h35">   
                                    <span class="msg_title">状态 :</span>
                                    <select name="state" class="input_msg">
                                    </select>
                                    <span class="msg_title">事业部:</span>
                                    <select name="buId" class="input_msg" id="buId">
                                        <option value="">请选择</option>
                                    </select>
                                </div>
                            </div>
                            <a href="javascript:" class="unfold">展开功能</a>
                        </div>
                    </form>
                    <div class="row col-12 mt20">
                        <div class="button-list">
                            <!--    <button type="button" class="btn btn-add waves-effect waves-light btn-sm" id="add-buttons">新增</button> -->
                            <!--   <button type="button" class="btn btn-add waves-effect waves-light btn-sm" onclick="$custom.request.delChecked('/initInventory/delInitInventory.asyn');">删除</button> -->
                            <shiro:hasPermission name="initInventory_import">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="import-buttons">批量导入</button>
                            </shiro:hasPermission>
<%--                             <shiro:hasPermission name="initInventory_toConfirmPage">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="confirm-buttons">确认期初建账</button>
                            </shiro:hasPermission> --%>

                        </div>
                    </div>
                    <!--  ================================表格 ===============================================   -->
                    <div class="mt10 table-responsive">
                        <table id="datatable-buttons" class="table table-striped " cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>
                                    <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                        <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
                                        <span></span>
                                    </label>
                                </th>
                                <!--                             <th>序号</th> -->
                                <th>仓库</th>
                                <th>事业部</th>
                                <th>商品货号</th>
                                <th>商品名称</th>
                                <th>商品条码</th>
                                <th>批次号</th>
                                <th>生产日期</th>
                                <th>有效期至</th>
                                <th>库存类型</th>
                                <th>库存数量</th>
                                <th>录入时间</th>
                                <th>状态</th>
                                <th>理货单位</th>
                                <th>标准条码</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                    <!--  ================================表格  end ===============================================   -->
                </div>
                <!--======================Modal框===========================  -->
                <%--         <jsp:include page="/WEB-INF/views/modals/1011.jsp" /> --%>
                <!-- end row -->
            </div>
            <!-- container -->
        </div>
    </div>
</div> <!-- content -->
<script type="text/javascript">
$module.constant.getConstantListByNameURL.call($('select[name="type"]')[0],"initInventory_typeList",null);
$module.constant.getConstantListByNameURL.call($('select[name="source"]')[0],"initInventory_sourceList",null);
$module.constant.getConstantListByNameURL.call($('select[name="state"]')[0],"initInventory_stateList",null);
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

        $module.depot.loadSelectData.call($("select[name='depotId']")[0],"");

        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url: serverAddr+'/initInventory/listInitInventory.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" value='+data.state+' name="iCheck" class="iCheck">';
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
                {data:'depotName'},{data:'buName'},{data:'goodsNo'},{data:'goodsName'},{data:'barcode'},{data:'batchNo'},{data:'productionDate'},{data:'overdueDate'},
                {data:'typeLabel'},
                {data:'initNum'}, {data:'createDate'},
                {data:'stateLabel'},
                {data:'unitLabel'},
                {data:'commbarcode'}
            ],
            formid:'#form1'
        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();

    } );




    //导入
    $("#import-buttons").click(function(){
        /* 	$load.a(pageUrl+"6008"); */
        $module.load.pageInventory("bls=6008");
    });
    //期初建账
/*     $("#confirm-buttons").click(function(){
        var ids=$mytable.fn.getCheckedRow();
        if(ids==null||ids==''||ids==undefined){
            $custom.alert.warningText("至少选择一条记录！");
            return ;
        }
        
        var state="";
        var stateArray=[];
        $("input[name='iCheck']:checked").each(function() { // 遍历选中的checkbox
        	state+=$(this).val()+",";
        });
        state=state.substring(0,state.length-1);
        stateArray = state.split(",");
        for(var i=0;i<stateArray.length;i++){
        	if(stateArray[i]=='2'){
        	     $custom.alert.warningText("库存已确认！");
                 return ;
        	}
        }
        $custom.alert.warning("确定更新选中对象？",function(){
            $custom.request.ajax(serverAddr+"/initInventory/toConfirmPage.asyn",{"ids":ids},function(data){
                if(data.state==200){
                    $custom.alert.success("确认期初建账成功");
                    //审核成功，重新加载页面
                    $mytable.fn.reload();
                }else{
                    $custom.alert.error(data.message);
                }
            });
        });


    }); */

    function list(){
        $module.load.pageInventory("bls=6001");
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
    $(function () {
        $("#datatable-buttons_wrapper").removeClass('container-fluid');
    })
</script>
