<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!-- Start content  -->
<jsp:include page="/WEB-INF/views/common.jsp" />
<div class="content">
    <div class="container-fluid mt80">
        <!-- end row -->
        <div class="row">
            <div class="col-12">
                <div class="card-box table-responsive">
                    <div class="col-12 row">
                        <div>
                            <ol class="breadcrumb">
                                <li><i class="block"></i> 当前位置：</li>
                                <li class="breadcrumb-item"><a href="#">采购模型管理</a></li>
                                <li class="breadcrumb-item "><a href="javascript:$load.a('/modelSetting/toPage.html');">模型配置</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  过滤条件查询  start  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                    <span>公司 :</span>
                                    <select name="merchantId" class="input_msg" id="merchantId">
                                        <option value="">请选择</option>
                                        <c:forEach items="${merchantList }" var="merchant">
                                            <option value="${merchant.value }">${merchant.label }</option>
                                        </c:forEach>
                                    </select>
                                    <div class="fr">
                                        <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick='searchData();'>查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light reset btn_default">重置</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                    <!-- 过滤条件查询  end  -->

                    <div class="row col-md-12 mt20">
                        <shiro:hasPermission name="modelSetting_add">
                            <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="add-buttons" onclick="$m12002.init(0, null);">新增</button>
                        </shiro:hasPermission>

                    </div>
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
                                <th class="tc">公司</th>
                                <th class="tc">货期（天）</th>
                                <th class="tc">采购周期</th>
                                <th class="tc">生产周期（天）</th>
                                <th class="tc">安全库存天数</th>
                                <th class="tc">预警天数</th>
                                <th class="tc">日销统计类型</th>
                                <th class="tc">日销修正系数参考范围</th>
                                <th class="tc">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
                <jsp:include page="/WEB-INF/views/modals/12002.jsp" />
            </div>
        </div>

        <!-- End row -->
        <!-- end row -->
    </div> <!-- container -->
</div> <!-- content -->



<script type="text/javascript">
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

    //重新加载select2
    $(".goods_select2").select2({
        language:"zh-CN",
        placeholder: '请选择',
        allowClear:true
    });

    $(document).ready(function() {

        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:serverAddr + '/modelSetting/listSettings.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
                {data:'merchantName'},{data:'deliveryPeriod'},{data:'purchasingCycle'},{data:'produceCycle'},
                {data:'safetyStockDays'},{data:'warnDays'},{data:'dailyStatisticalType'},{data:'dailyStatisticalRange'},
                { //操作
                    orderable: false,
                    width: "100px",
                    data: null,
                    render: function (data, type, row, meta) {
                        return '<shiro:hasPermission name="modelSetting_edit"><a href="javascript:edit('+row.id+')">编辑</a></shiro:hasPermission>';
                    }
                },
            ],
            formid:'#form1'
        };
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){

            if (aData.dailyStatisticalType == '0'){
                $('td:eq(7)', nRow).html('平常日');
            }else if (aData.dailyStatisticalType == '1'){
                $('td:eq(7)', nRow).html('平常日+促销日');
            }
            if(aData.dailyStatisticalRange == '0'){
                $('td:eq(8)', nRow).html('1个月');
            }else if(aData.dailyStatisticalRange == '1'){
                $('td:eq(8)', nRow).html('3个月');
            }else if(aData.dailyStatisticalRange == '2'){
                $('td:eq(8)', nRow).html('6个月');
            }else if(aData.dailyStatisticalRange == '3'){
                $('td:eq(8)', nRow).html('年初至今');
            }


        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();

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
    });

    function searchData(){
        $mytable.fn.reload();
    }

    //编辑
    function edit(id){
        $m12002.init(1, id);
    }

</script>