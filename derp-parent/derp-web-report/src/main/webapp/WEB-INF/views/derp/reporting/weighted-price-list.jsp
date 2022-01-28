<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<jsp:include page="/WEB-INF/views/common.jsp"/>

<!DOCTYPE html>
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
                                <li class="breadcrumb-item"><a href="#">核算单价管理</a></li>
                                <li class="breadcrumb-item"><a href="#">加权单价列表</a></li>
                            </ol>
                        </div>
                    </div>
                    <form id="form1">
                        <div class="form_box">
                            <div >
                                <div class="form_item h35">
                                    <span class="msg_title">标准条码 :</span>
                                    <input type="text" parsley-type="text" class="input_msg" id="barcode"
                                           name="commbarcode">
                                    <span class="msg_title">事业部 :</span>
                                    <div style="min-width:136px;max-width: 635px;display: inline-block;">
                                        <select name="buId" class="input_msg goods_select2" id="buId">
                                            <option value="">请选择</option>
                                        </select>
                                    </div>
                                    <span class="msg_title">生效年月 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg date-input"
                                           name="effectiveMonth" id="effectiveMonth">
                                    <div class="fr">
                                        <button type="button"
                                                class="btn ml15 btn-find waves-effect waves-light btn_default"
                                                onclick='$mytable.fn.reload();'>查询
                                        </button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">
                                            重置
                                        </button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">商品名称 :</span>
                                    <input type="text" parsley-type="text" class="input_msg" name="goodsName">
                                    <span class="msg_title">标准品牌 :</span>
                                    <select name="brandId" class="input_msg" id="brandId" multiple data-title="请选择">
                                        <option value="">请选择</option>
                                    </select>
                                    <span class="msg_title">更新时间 :</span>
                                    <input type="text" class="input_msg form_datetime date-input" name="createDateStart" id="createDateStart">
                                    -
                                    <input type="text" class="input_msg form_datetime date-input" name="createDateEnd" id="createDateEnd">
                                </div>
                            </div>
                            <a href="javascript:" class="unfold">展开功能</a>
                        </div>
                    </form>
                    <div class="row col-md-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="weightedPrice_export">
                                <input type="button"
                                       class="btn btn-find waves-effect waves-light btn-sm"
                                       onclick="exportWeightedPrice();" value="导出"/>
                            </shiro:hasPermission>
                        </div>
                    </div>

                    <!--  ================================表格 ===============================================   -->
                    <div class="row mt20">
                        <table id="datatable-buttons" class="table table-striped"
                               cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>序号</th>
                                <th>事业部</th>
                                <th>商品名称</th>
                                <th>标准条码</th>
                                <th>标准品牌</th>
                                <th>结算币种</th>
                                <th>加权单价</th>
                                <th>生效年月</th>
                                <th>更新时间</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                    <!--  ================================表格  end ===============================================   -->
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $("button[type='reset']").click(function(){
        $(".goods_select2").each(function(){
            $(this).val("");
        });
        $("#buId").select2({
            language:"zh-CN",
            placeholder: '请选择',
            multiple: true,
            closeOnSelect: true
        });
    });
    //加载标准品牌
    $module.brand.loadParentBrandSelect.call($('select[name="brandId"]')[0],null);
    $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null, {});
    $(document).ready(function () {
        $("#buId").select2({
            language:"zh-CN",
            placeholder: '请选择',
            multiple: true,
            closeOnSelect: true
        });
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
        var initYear;
        laydate.render({
            elem: '#effectiveMonth', //指定元素
            type: 'month',
            showBottom: false,
            ready: function(date){ // 控件在打开时触发，回调返回一个参数：初始的日期时间对象
                initYear = date.year;
            },
            change: function(value, date, endDate){ // 年月日时间被切换时都会触发。回调返回三个参数，分别代表：生成的值、日期时间对象、结束的日期时间对象
                var selectYear = date.year;
                var differ = selectYear-initYear;
                if (differ == 0) {
                    if($(".layui-laydate").length){
                        $("#effectiveMonth").val(value);
                        $(".layui-laydate").remove();
                    }
                }
                initYear = selectYear;
            }
        });
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params = {
            url: serverAddr + '/weightedPrice/listPrice.asyn?r=' + Math.random(),
            columns: [{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    // 显示行号
                    var startIndex = meta.settings._iDisplayStart;
                    return startIndex + meta.row + 1;
                }
            },
                {data: 'buName'}, {data: 'goodsName'}, {data: 'commbarcode'}, {data: 'brandName'},
                {data: 'currencyLabel'},{data: 'price'}, {data: 'effectiveMonth'}, {data: 'createDate'},
            ],
            formid: '#form1'
        };

        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback = function (nRow, aData, iDisplayIndex) {
        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();
        searchData();

        function searchData() {
            $mytable.fn.reload();
        }
    })

    // 点击展开功能
    $(".unfold").click(function () {
        $(".form_con").toggleClass('hauto');
        if ($(this).text() == '展开功能') {
            $(this).text('收起功能');
        } else {
            $(this).text('展开功能');
        }
    });

    /**
     * 全选
     */
    $("input[name='keeperUserGroup-checkable']").on("change", function () {
        if ($(this).is(':checked')) {
            $(":checkbox", '#datatable-buttons').prop("checked", $(this).prop("checked"));
            $('#datatable-buttons tbody tr').addClass('success');
        } else {
            $(":checkbox", '#datatable-buttons').prop("checked", false);
            $('#datatable-buttons tbody tr').removeClass('success');
        }
    });
    $('#datatable-buttons').on("change", ":checkbox", function () {
        $(this).parents('tr').toggleClass('success');
    });

    //导出
    function exportWeightedPrice() {
        var param = $("#form1").serialize();
        $custom.alert.warning("确定导出？",function(){
            var url = serverAddr+"/weightedPrice/exportWeightedPrice.asyn?"+param;
            $downLoad.downLoadByUrl(url);
        });
    }
</script>