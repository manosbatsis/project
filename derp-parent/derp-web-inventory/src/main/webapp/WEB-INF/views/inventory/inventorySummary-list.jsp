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
                                <li class="breadcrumb-item "><a href="javascript:list();">商品收发汇总</a></li>
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
                                                    &lt;%&ndash; <c:forEach items="${depotBean }" var="depot">
                                                        <option value="${depot.value }">${depot.label }</option>
                                                    </c:forEach> &ndash;%&gt;
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-3 ">
                                        <div class="row">
                                            <label  class="col-5 col-form-label "><div class="fr" style="color: red;">结算月份<span>：</span></div></label>
                                            <div class="col-7 ml10">
                                                <input type="text" required="required" parsley-type="text" id="currentMonth" value="${currentMonth}" class="form-control form_datetime date-input" name="currentMonth">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group col-2">
                                        <div class="row">
                                            <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='searchData()' >查询</button>
                                            <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-row h35">
                                    <div class="col-3 ">
                                        <div class="row">
                                            <label class="col-5 col-form-label"><div class="fr">商品货号<span>：</span></div></label>
                                            <div class="col-7 ml10">
                                                <input type="text" required="" parsley-type="text" class="form-control" name="goodsNo">
                                            </div>
                                        </div>
                                    </div>
                                </div>--%>
                                <div class="form_item h35">
                                    <span class="msg_title">公司 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="merchantName">
                                    <span class="msg_title">仓库 :</span>
                                    <select name="depotId"  id="depotId" class="input_msg">
                                        <option value="">请选择</option>
                                   <%-- <c:forEach items="${depotBean }" var="depot">
                                        <option value="${depot.value }">${depot.label }</option>
                                    </c:forEach> --%>
                                    </select>
                                    <span class="msg_title">结算月份 :</span>
                                    <input type="text" required="required" parsley-type="text" id="currentMonth" value="${currentMonth}" class="input_msg form_datetime date-input" name="currentMonth">
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='searchData()' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">商品货号 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" id="goodsNo" name="goodsNo">
                                </div>
                            </div>
                            <a href="javascript:" class="unfold">展开功能</a>
                        </div>
                    </form>
                    <div class="row col-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="inventorySummary_exportInventorySummary">
                                <button type="button" id="exportInventoryBatch" class="btn btn-find waves-effect waves-light btn-sm">导出</button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                    <!--  ================================表格 ===============================================   -->
                    <div class="row mt10 table-responsive">
                        <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="1700px">
                            <thead>
                            <tr>
                                <th>
                                    <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                        <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
                                        <span></span>
                                    </label>
                                </th>
                                <!--                             <th>序号</th> -->
                                <th>仓库名称</th>
                                <th>商品货号</th>
                                <th style="min-width: 150px">商品名称</th>
                                <th>本月期初库存</th>
                                <th>本月累计入库</th>
                                <th>本月累计出库</th>
                                <th>库存余额</th>
                                <th>销售在途量</th>
                                <th>电商在途量</th>
                                <th>调拨出库在途</th>
                                <th>可用库存量</th>
                                <th>单位</th>
                                <th>库存周转天数(按120天订单算)</th>
                                <th>在库库存断销时间</th>
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

            //加载仓库下拉
            $module.depot.loadSelectData.call($("select[name='depotId']")[0],"");


            //初始化
            $mytable.fn.init();
            //配置表格参数
            $mytable.params={
                url: serverAddr+'/inventorySummary/listInventorySummary.asyn?r='+Math.random(),
                columns:[{ //复选框单元格
                    className: "td-checkbox",
                    orderable: false,
                    width: "30px",
                    data: null,
                    render: function (data, type, row, meta) {
                        return '<input type="checkbox"   value='+data.goodsNo+' name="iCheck"  class="iCheck">';
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
                    {data:'depotName'},{data:'goodsNo'},{data:'goodsName'},{data:'openingInventoryNum'},{data:'inDepotTotal'},{data:'outDepotTotal'},{data:'surplusNum'},{data:'saleOnwayNum'},{data:'eOnwayNum'},
                    {data:'transferOutNum'},{data:'availableNum'},
                    {data:'unitLabel'},
                    {data:'turnoverDays'},{data:'noSaleDate'},
                    { //操作
                        orderable: false,
                        width: "50px",
                        data: null,
                        render: function (data, type, row, meta) {
                            return '<a href="javascript:details(\''+row.goodsNo+'\','+row.depotId+')">详情</a>';
                        }
                    },
                ],
                formid:'#form1'
            };
            //生成列表信息
            $('#datatable-buttons').mydatatables();

        } );
        //查询
        function searchData(){
            if($("#currentMonth").val()==''){
                $custom.alert.error("结算月份为必选");
                return false;
            }
            $mytable.fn.reload();
        }

        //进入商品库存明细
        function details(goodsNo,depotId){
            $module.load.pageInventory("bls=6013&goodsNo="+goodsNo+"&depotId="+depotId);
        }


        function list(){
            $module.load.pageInventory("bls=6004");
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
    $("#exportInventoryBatch").on("click",function(){
        //获取选中的id信息
         var depotId=  $("#depotId").val();
         var goodsNo=  $("#goodsNo").val();
         var currentMonth=$("#currentMonth").val();
        $custom.alert.warning("确定导出筛选的数据？",function(){
          var url=serverAddr+"/inventorySummary/exportInventorySummary.asyn?depotId="+depotId+"&goodsNo="+goodsNo+"&currentMonth="+currentMonth;
           //window.open(url);
          $downLoad.downLoadByUrl(url);
        });
    });
        
    </script>
