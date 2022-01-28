<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="/WEB-INF/views/common.jsp" />
<style>
    .date-input {
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
                                <li class="breadcrumb-item"><a href="#">调拨管理</a></li>
                                <li class="breadcrumb-item"><a href="javascript:$load.a('serverAddr+/transferOutIn/toPage.html');">调出调入流水</a></li>
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
                                            <label  class="col-6 col-form-label "><div class="fr">调拨单号<span>：</span></div></label>
                                            <div class="col-6 ml10">
                                                <input class="form-control" type="text" name="code" required="" >
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-3">
                                        <div class="row">
                                            <label class="col-6 col-form-label"><div class="fr">调入仓库<span>：</span></div></label>
                                            <div class="col-6 ml10">
                                                <select name="inDepotId" class="form-control goods_select2">
                                                    <option value="">请选择</option>
                                                    <c:forEach items="${depotBean }" var="depot">
                                                        <option value="${depot.value }">${depot.label }</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-3 ml15">
                                        <div class="row">
                                            <label class="col-5 col-form-label"><div class="fr">调出仓库<span>：</span></div></label>
                                            <div class="col-7 ml10">
                                                <select name="outDepotId" class="form-control goods_select2">
                                                    <option value="">请选择</option>
                                                    <c:forEach items="${depotBean }" var="depot">
                                                        <option value="${depot.value }">${depot.label }</option>
                                                    </c:forEach>
                                                </select>
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
                                            <label  class="col-6 col-form-label "><div class="fr">调出商品货号<span>：</span></div></label>
                                            <div class="col-6 ml10">
                                                <input type="text" required="" parsley-type="text" class="form-control" name="outGoodsNo">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-3">
                                        <div class="row">
                                            <label class="col-6 col-form-label"><div class="fr">调拨订单时间<span>：</span></div></label>
                                            <div class="col-6 ml10">
                                                <input type="text" required="" parsley-type="text" class="form-control form_datetime date-input" name="createDate">
                                            </div>
                                        </div>
                                    </div>

                                </div>--%>
                                <div class="form_item h35">
                                    <span class="msg_title">调拨单号 :</span>
                                    <input class="input_msg" type="text" name="code" required="" >
                                    <span class="msg_title">调入仓库 :</span>
                                    <select name="inDepotId" class="input_msg">
                                        <option value="">请选择</option>
                                    </select>
                                    <span class="msg_title">调出仓库 :</span>
                                    <select name="outDepotId" class="input_msg">
                                        <option value="">请选择</option>
                                    </select>
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">事业部 :</span>
                                    <select name="buId" class="input_msg">
                                        <option value="">请选择</option>
                                    </select>
                                    <span class="msg_title">调出商品货号 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="outGoodsNo">
                                    <span class="msg_title">调拨订单时间 :</span>
                                    <input type="text" class="input_msg form_datetime date-input" name="createDateStart" id="createDateStart">
                                    -
                                    <input type="text" class="input_msg form_datetime date-input" name="createDateEnd" id="createDateEnd">

                                </div>
                            </div>
                            <a href="javascript:" class="unfold">展开功能</a>
                        </div>
                    </form>

                    <!--  <div class="row col-12 mt20 ml100">
                         <div class="button-list">
                             <button type="button" class="btn btn-add waves-effect waves-light btn-sm">审&nbsp;核</button>
                         </div>
                     </div>   -->
                    <!--  ================================表格 ===============================================   -->
                    <div class="mt-5 table-responsive">
                        <table id="datatable-buttons" class="table table-striped twoRows" cellspacing="0" style="width: 2200px;">
                            <thead>
                            <tr>
                                <th>序号</th>
                                <th style="min-width: 100px">调拨订单号</th>
                                <th>调出仓库</th>
                                <th>调入仓库</th>
                                <th>事业部</th>
                                <th>调入商品货号</th>
                                <th style="width: 200px">调入商品名称</th>
                                <th>调出商品货号</th>
                                <th style="width: 200px">调出商品名称</th>
                                <th>调出数量</th>
                                <th>调出单位</th>
                                <th>调入数量</th>
                                <th>调入单位</th>
                                <th>调拨出库单号</th>
                                <th>出库商品货号</th>
                                <th style="width: 200px">出库商品名称</th>
                                <th>调拨出库批次</th>
                                <th>调出正常数量</th>
                                <th>调出坏品数量</th>
                                <th>调出过期数量</th>
                                <th>调拨出库数量</th>
                                <th>调拨入库单号</th>
                                <th>入库商品货号</th>
                                <th style="width: 200px">入库商品名称</th>
                                <th>调拨入库批次</th>
                                <th>调入正常数量</th>
                                <th>调入坏品数量</th>
                                <th>调入过期数量</th>
                                <th>调入数量</th>
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
</div>
<script type="text/javascript">
    $(document).ready(function() {

        laydate.render({
            elem: '#createDateStart', //指定元素
            type: 'datetime',
            format: 'yyyy-MM-dd HH:mm:ss',
            ready: function () {
                $('.laydate-btns-time').remove();
            }
        });

        laydate.render({
            elem: '#createDateEnd', //指定元素
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
        //加载仓库
        $module.depot.getSelectBeanByMerchantRel.call($("select[name='outDepotId']")[0],null, {});
        $module.depot.getSelectBeanByMerchantRel.call($("select[name='inDepotId']")[0],null, {});

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
            url:serverAddr+'/transferOutIn/transferOutInList.asyn?r='+Math.random(),
            columns:[{  //序号
                data : null,
                bSortable : false,
                className : "",
                render : function(data, type, row, meta) {
                    // 显示行号
                    var startIndex = meta.settings._iDisplayStart;
                    return startIndex + meta.row + 1;
                }
            },
                {data:'code'},{data:'outDepotName'},{data:'inDepotName'},{data:'buName'},{data:'inGoodsNo'},{data:'inGoodsName'},{data:'outGoodsNo'},{data:'outGoodsName'},{data:'transferNum'},{data:'tallyingUnitLabel'},{data:'orderInTransferNum'},{data:'inTallyingUnitLabel'},
                {data:'outDepotCode'},{data:'outGoodsNoItem'},{data:'outGoodsNameItem'},{data:'outTransferBatchNo'},{data:'outTransferNum'},{data:'outWornNum'},{data:'outExpireNum'},{data:'outTransferNumAll'},
                {data:'inDepotCode'},{data:'inGoodsNoItem'},{data:'inGoodsNameItem'},{data:'inTransferBatchNo'},{data:'inTransferNum'},{data:'wornNum'},{data:'expireNum'},{data:'inTransferNumAll'},
            ],
            formid:'#form1'
        };
        $mytable.rewriter.fnRowCallback = function (nRow, aData, iDisplayIndex) {
            if(aData.inGoodsName != null && aData.inGoodsName != undefined &&  aData.inGoodsName.length > 30 ){
               $('td:eq(6)', nRow).html("<span title='" + aData.inGoodsName + "'>" + aData.inGoodsName.substring(0, 30) + "...</span>");
            }
            if(aData.outGoodsName != null && aData.outGoodsName != undefined &&  aData.outGoodsName.length > 30 ){
                $('td:eq(8)', nRow).html("<span title='" + aData.outGoodsName + "'>" + aData.outGoodsName.substring(0, 30) + "...</span>");
            }
            if(aData.outGoodsNameItem != null && aData.outGoodsNameItem != undefined &&  aData.outGoodsNameItem.length > 30 ){
                $('td:eq(15)', nRow).html("<span title='" + aData.outGoodsNameItem + "'>" + aData.outGoodsNameItem.substring(0, 30) + "...</span>");
            }
            if(aData.inGoodsNameItem != null && aData.inGoodsNameItem != undefined &&  aData.inGoodsNameItem.length > 30 ){
                $('td:eq(23)', nRow).html("<span title='" + aData.inGoodsNameItem + "'>" + aData.inGoodsNameItem.substring(0, 30) + "...</span>");
            }
            //理货单位 00-托盘 01-箱 02-件
            /*if(aData.tallyingUnit=='00'){
            	 $('td:eq(9)', nRow).html('托盘');
            }else if(aData.tallyingUnit=='01'){
          	     $('td:eq(9)', nRow).html('箱');
            }else if(aData.tallyingUnit=='02'){
            	 $('td:eq(9)', nRow).html('件');
            }
            if(aData.inTallyingUnit=='00'){
          	     $('td:eq(11)', nRow).html('托盘');
            }else if(aData.inTallyingUnit=='01'){
        	     $('td:eq(11)', nRow).html('箱');
            }else if(aData.inTallyingUnit=='02'){
          	     $('td:eq(11)', nRow).html('件');
            }*/
     
        };

        //生成列表信息
        $('#datatable-buttons').mydatatables();
    } );

    //加载事业部
    $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null, null,null);

    function searchData(){
        $mytable.fn.reload();
    }

    //详情
    function detail(id){
        $load.a(serverAddr+"/transferOut/toDetailPage.html?id="+id);
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
