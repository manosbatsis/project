<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<jsp:include page="/WEB-INF/views/common.jsp"/>
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
                                <li class="breadcrumb-item"><a href="#">预售勾稽明细列表</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  title   end  -->
                    <form id="form1">
                        <div class="form_box">
                            <div class="">
                                <div class="form_item h35">
                                    <span class="msg_title">预售单号:</span>
                                    <input class="input_msg" name="preSaleOrderCode"/>
                                    <span class="msg_title">客户 :</span>
                                    <select name="customerId" class="input_msg">
                                        <option value="">请选择</option>
                                    </select>
                                    <span class="msg_title">事业部 :</span>
                                    <select class="input_msg" name="buId"></select>
                                    <div class="fr">
                                        <button type="button"
                                                class="btn ml15 btn-find waves-effect waves-light btn_default"
                                                onclick='searchData();'>查询
                                        </button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">
                                            重置
                                        </button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">出仓仓库 :</span>
                                    <select name="outDepotId" class="input_msg"></select>
                                    <span class="msg_title">商品货号 :</span>
                                    <input class="input_msg" name="goodsNo"/>
                                    <span class="msg_title">出库时间 :</span>
                                    <input type="text" name="outDepotStartDate" id="outDepotStartDate" class="startDate input_msg">
                                    -
                                    <input type="text" name="outDepotEndDate" id="outDepotEndDate" class="endDate input_msg">
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">商品条码 :</span>
                                    <input class="input_msg" name="barcode"/>
                                    <span class="msg_title">审核时间 :</span>
                                    <input type="text" name="auditStartDate" id="auditStartDate" class="startDate input_msg">
                                    -
                                    <input type="text" name="auditEndDate" id="auditEndDate" class="endDate input_msg">
                                </div>
                            </div>
                            <!-- <a href="javascript:" class="unfold">展开功能</a>  -->
                        </div>
                    </form>
                    <div class="row col-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="preSaleOrderCorrelation_export">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm"
                                        id="exportOrder">导出
                                </button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                    <!--  ================================表格 ===============================================   -->
                    <div class="row mt10 table-responsive">
                        <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>
                                    <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                        <input type="checkbox" name="keeperUserGroup-checkable"
                                               class="group-checkable"/>
                                        <span></span>
                                    </label>
                                </th>
                                <th>预售单号</th>
                                <th>PO号</th>
                                <th>商品货号</th>
                                <th>商品条码</th>
                                <th>商品名称</th>
                                <th>客户</th>
                                <th>事业部</th>
                                <th>出仓仓库</th>
                                <th>预售数量</th>
                                <th>销售数量</th>
                                <th>出库数量</th>
                                <th>预售余量</th>
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
                <!-- end row -->
            </div>
        </div>
        <!-- container -->
    </div>

</div>
<!-- content -->
<script type="text/javascript">
    $module.businessUnit.getSelectBeanByMerchantDepotRel.call($('select[name="buId"]')[0], null, null, null, null);//事业部下拉列表
    $module.depot.getSelectBeanByMerchantRel.call($("select[name='outDepotId']")[0], "", {}); //仓库下拉列表
    $module.customer.loadSelectData.call($('select[name="customerId"]')[0]); //客户下拉列表
    // 时间插件
    $(".startDate").each(function () {
        laydate.render({
            elem: this, //指定元素
            type: 'datetime',
            format: 'yyyy-MM-dd HH:mm:ss',
            ready: function () {
                $('.laydate-btns-time').remove();
            }
        });
    });
    $(".endDate").each(function () {
        laydate.render({
            elem: this, //指定元素
            type: 'datetime',
            format: 'yyyy-MM-dd HH:mm:ss',
            ready: function () {
                $('.laydate-btns-time').remove();
            },
            done: function (value) {
                this.dateTime.hours = 23;
                this.dateTime.minutes = 59;
                this.dateTime.seconds = 59;
            }
        });
    });

    $(document).ready(function () {
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params = {
            url: serverAddr + '/preSaleOrderCorrelation/listPreSaleOrderCorrelation.asyn?r=' + Math.random(),
            columns: [
                { //复选框单元格
                    orderable: false,
                    width: "30px",
                    data: null,
                    render: function (data, type, row, meta) {
                        return '<input type="checkbox" class="iCheck">';
                    }
                },
                {data: 'preSaleOrderCode'}, {data: 'poNo'}, {data: 'goodsNo'}, {data: 'barcode'}, {data: 'goodsName'},
                {data: 'customerName'}, {data: 'buName'}, {data: 'outDepotName'}, {data: 'preNum'},
                {data: 'saleNum'}, {data: 'outNum'},
                { //预售余量 = 预售数量  - 出库数量
                    orderable: false,
                    width: 70,
                    data: null,
                    render: function (data, type, row, meta) {
                        return row.preNum - row.outNum;
                    }
                },
                { //操作
                    orderable: false,
                    width: 70,
                    data: null,
                    render: function (data, type, row, meta) {
                        return '<shiro:hasPermission name="preSaleOrderCorrelation_detail"><a href="javascript:details(\'' + row.preSaleOrderCode + '\',\'' + row.goodsNo + '\')">详情</a></shiro:hasPermission>';
                    }
                },
            ],
            formid: '#form1'
        };
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback = function (nRow, aData, iDisplayIndex) {
        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();

    });

    function searchData() {
        $mytable.fn.reload();
    }

    //详情
    function details(code, goodsNo) {
        $module.revertList.serializeListForm() ;
        $module.revertList.isMainList = true ;
        $load.a($module.params.loadOrderPageURL+"act=70011&code=" + code + "&goodsNo=" + goodsNo);
    }


    // 点击展开功能
    $(".unfold").click(function () {
        $(".form_con").toggleClass('hauto');
        if ($(this).text() == '展开功能') {
            $(this).text('收起功能');
        } else {
            $(this).text('展开功能');
        }
    });

    //导出
    $("#exportOrder").on("click", function () {
        var jsonData = null;
      	//获取选中的id信息
        var ids=$mytable.fn.getCheckedRow();
    	if(ids){
    		jsonData=$json.setJson(jsonData,"ids",ids);
		}else{
			//根据筛选条件导出
	        var jsonArray = $("#form1").serializeArray();
	        $.each(jsonArray, function (i, field) {
	            if (field.value != null && field.value != '' && field.value != undefined) {
	                jsonData = $json.setJson(jsonData, field.name, field.value);
	            }
	        });
		}
        //导出数据
        var url = serverAddr + "/preSaleOrderCorrelation/exportPreSaleOrderCorrelation.asyn";
        var i = 0;
        var form = JSON.parse(jsonData);
        if (!!form) {
            for (var key in form) {
                if (i == 0) {
                    url += "?";
                } else {
                    url += "&";
                }
                url += key + "=" + form[key];
                i++;
            }
        }
        $downLoad.downLoadByUrl(url);
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

</script>
