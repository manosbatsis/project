<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<!-- Start content -->
<jsp:include page="/WEB-INF/views/common.jsp"/>
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

                                <li class="breadcrumb-item"><a href="#">盘点指令列表</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  title   end  -->
                    <div class="form_box">
                        <form id="form1">
                            <div class="form_con">
                           
                                <div class="form_item h35">
                                    <span class="msg_title">盘点指令单号 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="code">
                                    <span class="msg_title">仓库 :</span>
                                    <select class="input_msg" id="depotId" name="depotId">
                                        <option value="">请选择</option>
                                    </select>
                                    <span class="msg_title">公司名称 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="merchantName">
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='searchData();'>查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">服务类型 :</span>
                                    <select class="input_msg" name="serverType">
                                    </select>
                                    <span class="msg_title">单据状态 :</span>
                                    <select class="input_msg" >
                                        <option value="013">待提交</option>
                                        <option value="014">已提交</option>
                                    </select>
                                </div>
                                <a href="javascript:" class="unfold">展开功能</a>
                            </div>
                        </form>
                    </div>

                    <div class="mt20">
                        <shiro:hasPermission name="takesstock_add">
                            <button type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick="toAdd();">新增
                            </button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="takesstock_delTakesStockBatch">
                            <button type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick="del();">删除
                            </button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="takesstock_sendtakesStock">
                            <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="send-buttons">提交
                            </button>
                        </shiro:hasPermission>
                    </div>
                    <div class="mt10">
                        <table id="datatable-buttons" class="table table-striped" cellspacing="0"
                               width="100%">
                            <thead>
                            <tr>
                                <th style="white-space:nowrap;" class="tc">序号</th>
                                <th style="white-space:nowrap;" class="tc">盘点指令单号</th>
                                <th style="white-space:nowrap;" class="tc">公司</th>
                                <th style="white-space:nowrap;" class="tc">服务类型</th>
                                <th style="white-space:nowrap;" class="tc">仓库</th>
                                <th style="white-space:nowrap;" class="tc">创建日期</th>
                                <th style="white-space:nowrap;" class="tc">创建人</th>
                                <th style="white-space:nowrap;" class="tc">单据状态</th>
                                <th style="white-space:nowrap;" class="tc">操作</th>
                            </tr>
                            </thead>
                            <tbody>

                            </tbody>
                        </table>
                    </div>
                </div>

            </div>
            <!--======================Modal框===========================  -->
            <!-- end row -->

        </div>
        <!-- container -->
    </div>
</div>
<!-- content -->
</div>
<script type="text/javascript">
    $module.constant.getConstantListByNameURL.call($('select[name="serverType"]')[0],"takesStock_serverTypeList",null);
    //加载仓库
    $module.depot.getSelectBeanByMerchantRel.call($("select[name='depotId']")[0],null, {});
    $(document).ready(function () {

        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params = {
            url: serverAddr + '/takesstock/listTakesStock.asyn?r=' + Math.random(),
            columns: [{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },//	创建日期	创建人	单据状态
                {data: 'code'}, {data: 'merchantName'}, {data: 'serverTypeLabel'}, {data: 'depotName'}, {data: 'createTime'}, {data: 'createUsername'}, {data: 'statusLabel'},
                { //操作
                    orderable: false,
                    width: "80px",
                    data: null,
                    render: function (data, type, row, meta) {
                        var caoZuoHtml = '<shiro:hasPermission name="takesstock_detail"><a href="javascript:detail(' + row.id + ')">详情</a></shiro:hasPermission>';
                        if (row.status == '013') {
                            caoZuoHtml += '<shiro:hasPermission name="takesstock_edit">&nbsp;&nbsp;<a href="javascript:edit(' + row.id + ')">编辑</a></shiro:hasPermission>';
                        }
                        return caoZuoHtml;
                    }
                },
            ],
            formid: '#form1'
        }
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback = function (nRow, aData, iDisplayIndex) {
            /*if (aData.serverType == '10') {
                $('td:eq(3)', nRow).html('个性盘点');
            } else if (aData.serverType == '20') {
                $('td:eq(3)', nRow).html('自主盘点');
            }*/

            /*if (aData.status == '006') {
                $('td:eq(7)', nRow).html('已删除');
                $('td:eq(0)', nRow).html('<input type="checkbox" disabled="true">');
            } else if (aData.status == '014') {
                $('td:eq(7)', nRow).html('已提交');
                $('td:eq(0)', nRow).html('<input type="checkbox" disabled="true">');
            } else {
                $('td:eq(7)', nRow).html('待提交');
            }*/
            if (aData.status == '006' || aData.status == '014') {
                $('td:eq(0)', nRow).html('<input type="checkbox" disabled="true">');
            }

        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();

        //重新加载select2
        $(".goods_select2").select2({
            language: "zh-CN",
            placeholder: '请选择',
            allowClear: true
        });

        // 点击展开功能
        $(".unfold").click(function () {
            $(".form_con").toggleClass('hauto');
            if($(this).text() == '展开功能'){
                $(this).text('收起功能');
            }else{
                $(this).text('展开功能');
            }
        })
    });


    //查询
    function searchData() {
        $mytable.fn.reload();
    }

    //新增
    function toAdd() {
        $module.load.pageStorage("act=90011");
    }

    //编辑
    function edit(id) {
        $module.load.pageStorage("act=90012&id=" + id);
    }

    //详情
    function detail(id) {
        $module.load.pageStorage("act=90013&id=" + id);
    }

    //删除
    function del() {
        $custom.request.delChecked(serverAddr + '/takesstock/delTakesStockBatch.asyn');
    }


    $("#send-buttons").click(function () {
        //获取选中的id信息
        var ids = $mytable.fn.getCheckedRow();
        if (ids == null || ids == '' || ids == undefined) {
            $custom.alert.warningText("至少选择一条记录！");
            return;
        }
        $custom.alert.warning("确定提交选中对象？", function () {
            var url = serverAddr + "/takesstock/sendtakesStock.asyn";
            $custom.request.ajax(url, {"ids": ids}, function (data) {
                if (data.state == 200) {
                    if (data.data.failCode != '' && data.data.failCode != undefined) {
                        $custom.alert.success('完成\n<font size="1">提交失败单号：\n' + data.data.failCode + '</font>');
                    } else {
                        $custom.alert.success('完成');
                    }
                    //删除成功，重新加载页面
                    $mytable.fn.reload();
                } else {
                    $custom.alert.error(data.message);
                }
            });
        });
    });

    //重置
    $("button[type='reset']").click(function () {
        $(".goods_select2").each(function () {
            $(this).val("");
        });
        //重新加载
        $(".goods_select2").select2({
            language: "zh-CN",
            placeholder: '请选择',
            allowClear: true
        });
    });

    $(function () {
        $("#datatable-buttons_wrapper").removeClass('container-fluid');
    })
</script>