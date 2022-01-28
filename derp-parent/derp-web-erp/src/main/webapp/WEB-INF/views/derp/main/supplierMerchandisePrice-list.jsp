<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!-- Start content -->

<div class="content">
    <div class="container-fluid mt80">
        <!-- end row -->
        <div class="row">
            <div class="col-12">
                <div class="card-box">
                    <div class="col-12 row">
                        <div>
                            <ol class="breadcrumb">
                                <li><i class="block"></i> 当前位置：</li>
                                <li class="breadcrumb-item"><a href="#">供应商档案</a></li>
                                <li class="breadcrumb-item "><a
                                        href="javascript:$load.a('/supplierMerchandisePrice/toPage.html');">采购价格管理</a>
                                </li>
                            </ol>
                        </div>
                    </div>
                    <!--  过滤条件查询  start  -->
                    <form id="form1">
                        <div class="form_box">
                            <div >
                                <div class="form_item h35">
                                    <span class="msg_title">供应商 :</span>
                                    <select name="customerIds" class="input_msg" id="customerIds" multiple="multiple" title="请选择" >
                                        <option value="">请选择</option>
                                       <%-- <c:forEach items="${supplierBean }" var="supplier">
                                            <option value="${supplier.value }">${supplier.label }</option>
                                        </c:forEach>--%>
                                    </select>
                                    <input type="hidden" name="customerIdsStr" id="customerIdsStr"/>
                                    <span class="msg_title">创建人 :</span>
                                    <input class="input_msg" type="text" name="createName" id="createName">

                                    <span class="msg_title">标准条码 :</span>
                                    <input class="input_msg" type="text" name="commbarcodes" required="">

                                    <span class="msg_title">事业部 :</span>
                                    <select class="input_msg" name="buId" id="buId">
                                        <option value="">请选择</option>
                                    </select>
                                    <input type="hidden" name="state" id="state" value="002">
                                    <div class="fr">
                                        <button type="button" class="btn btn-find waves-effect waves-light btn_default"
                                                onclick='searchData();'>查询
                                        </button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">
                                            重置
                                        </button>
                                    </div>
                                </div>
                                <%--<div class="form_item h35">
                                    <span class="msg_title">品牌 :</span>
                                    <select name="brandId" class="input_msg">
                                        <option value="">请选择</option>
                                        <c:forEach items="${brandBean }" var="brand">
                                            <option value="${brand.value }">${brand.label }</option>
                                        </c:forEach>
                                    </select>
                                    <span class="msg_title">商品货号 :</span>
                                    <input class="input_msg" type="text" name="goodsNo" required="" >
                                    <span class="msg_title">商品名称:</span>
                                    <input class="input_msg" type="text" name="goodsName" required="" >
                                </div>--%>
                            </div>
                        </div>
                    </form>
                    <!-- 过滤条件查询  end  -->

                    <div class="row col-md-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="supplierMerchandisePrice_delSMPrice">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm"
                                        onclick="$custom.request.delChecked('/supplierMerchandisePrice/delSMPrice.asyn');">
                                    删除
                                </button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="supplierMerchandisePrice_toImportPage">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm"
                                        id="import-buttons">批量导入
                                </button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="supplierMerchandisePrice_submit">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm"
                                        id="submit-buttons">提交
                                </button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="supplierMerchandisePrice_audit">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm"
                                        id="audit-buttons">审核
                                </button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="supplierMerchandisePrice_export">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm"
                                        id="export-buttons">导出
                                </button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                    <div class="mt20 table-responsive">
                        <ul class="nav nav-tabs">
                            <li class="nav-item">
                                <a href="javascript:void(0)" data-toggle="tab"
                                   class="nav-link active" onclick="loadPriceData('002');">待提交<span id="submitSpan"></span></a>
                            </li>
                            <li class="nav-item">
                                <a href="javascript:void(0)" data-toggle="tab" class="nav-link" onclick="loadPriceData('001');">待审核<span id="auditSpan"></span></a>
                            </li>
                            <li class="nav-item">
                                <a href="javascript:void(0)" data-toggle="tab" class="nav-link" onclick="loadPriceData('003');">已审核<span id="beAuditSpan"></span></a>
                            </li>
                            <li class="nav-item">
                                <a href="javascript:void(0)" data-toggle="tab" class="nav-link" onclick="loadPriceData('004');">已驳回<span id="rejectSpan"></span></a>
                            </li>
                            <li class="nav-item">
                                <a href="javascript:void(0)" data-toggle="tab" class="nav-link" onclick="loadPriceData('');">全部<span id="totalSpan"></span></a>
                            </li>
                        </ul>
                        <div class="tab-content">
                            <table id="datatable-buttons" class="table table-striped" cellspacing="0" width=2300>
                                <thead>
                                <tr>
                                    <th>
                                        <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                            <input type="checkbox" name="keeperUserGroup-checkable"
                                                   class="group-checkable"/>
                                            <span></span>
                                        </label>
                                    </th>
                                    <th>所属公司</th>
                                    <th>事业部</th>
                                    <th>供应商</th>
                                    <th>标准条码</th>
                                    <th>商品名称</th>
                                    <th>品牌</th>
                                    <th>规格型号</th>
                                    <th>采购价</th>
                                    <th>状态<a title="若同一时间存在多条已生效的记录，则以最新审核的记录为准" href="#" class="dripicons-information"
                                             style="color: #00a0e9;padding-left: 5px;"></a></th>
                                    <th>报价生效时间</th>
                                    <th>报价失效时间</th>
                                    <th>创建人</th>
                                    <th>审核人</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- End row -->
        <!-- end row -->
        <jsp:include page="/WEB-INF/views/modals/18001.jsp" />
        <jsp:include page="/WEB-INF/views/modals/18002.jsp" />
    </div> <!-- container -->
</div>
<!-- content -->

<script type="text/javascript">
    $module.customer.getCusOrSurpSelectBean.call($('select[name="customerIds"]')[0],'6',null);

    var chooseState = '002';
    $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0], null, null, null, null);

    $(document).ready(function () {
        //showCustomer();
        //重置按钮
        $("button[type='reset']").click(function () {
           /* $(".selectpicker").selectpicker('val', ['noneSelectedText']);
            $(".selectpicker").selectpicker('refresh');*/
            $(".goods_select2").each(function () {
                $(this).val("");
            });
            $("#customerIds").selectpicker('refresh');

            //重新加载select2
            $(".goods_select2").select2({
                language: "zh-CN",
                placeholder: '请选择',
                allowClear: true
            });
        });

        //重新加载select2
        $(".goods_select2").select2({
            language: "zh-CN",
            placeholder: '请选择',
            allowClear: true
        });


        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params = {
            url: '/supplierMerchandisePrice/listSMPrice.asyn?r=' + Math.random(),
            columns: [{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" name="item-checkbox" class="iCheck" value="' + data.id + '">';
                }
            },
                {data: 'merchantName'}, {data: 'buName'},
                {
                    orderable: false,
                    data: null,
                    width: '80px',
                    render: function (data, type, row, meta) {
                        return row.customerCode + '<br>' + row.customerName;
                    }
                },
                {data: 'commbarcode'}, {data: 'goodsName'},
                {data: 'brandName'}, {data: 'spec'},
                {
                    orderable: false,
                    data: null,
                    width: '80px',
                    render: function (data, type, row, meta) {
                        return row.currencyLabel + '&nbsp;' + row.supplyPrice;
                    }
                },
                {data: 'stateLabel'},
                {data: 'effectiveDate'}, {data: 'expiryDate'},
                {
                    orderable: false,
                    data: null,
                    width: '80px',
                    render: function (data, type, row, meta) {
                        var createName = '';
                        var createDate = '';
                        if (row.createName) {
                            createName = row.createName;
                        }

                        if (row.createDate) {
                            createDate = row.createDate;
                        }
                        return createName + '<br>' + createDate;
                    }
                },
                {
                    orderable: false,
                    data: null,
                    width: '80px',
                    render: function (data, type, row, meta) {
                        var auditName = '';
                        var auditDate = '';
                        if (row.auditName) {
                            auditName = row.auditName;
                        }

                        if (row.auditDate) {
                            auditDate = row.auditDate;
                        }
                        return auditName + '<br>' + auditDate;
                    }
                },
                {
                    orderable: false,
                    data: null,
                    width: '80px',
                    render: function (data, type, row, meta) {
                        var html = '';
                        if (row.state == '002' || row.state == '004') {
                            html += '<shiro:hasPermission name="supplierMerchandisePrice_edit"><a href="javascript:edit('+row.id+')">编辑</a></shiro:hasPermission>';
                        }
                        return html;
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

       /* var customerIds=$('input[name="customerIdsStr"]').val();//客户ids
        var customerIdsArr = customerIds.split(",");
        $('select[name="customerIds"]').selectpicker('val', customerIdsArr);
*/
        statisticsStateNum();
    });

    function searchData() {
        var customerIdArr = $("#customerIds").val();
        debugger
        var customerIdStr='';
        if(customerIdArr!='' && customerIdArr!=null){
            customerIdStr=customerIdArr.toString();
        }
        $("#customerIdsStr").val(customerIdArr);
        statisticsStateNum();
        $("#state").val(chooseState);
        $mytable.fn.reload();
    }

    // 审核
    $("#audit-buttons").click(function () {
        var checkeds = $.find("input[name='item-checkbox']:checked");
        if (checkeds.length == 0) {
            $custom.alert.warningText("请选择需要审核的数据！");
            return;
        }
        var check = true;
        var ids = [];
        var customerIds = [];
        var buIds = [];
        var states = [];

        var nTrs = $mytable.tableObj.fnGetNodes();//fnGetNodes获取表格所有行，nTrs[i]表示第i行tr
        for(var i = 0; i < nTrs.length; i++){
            if($(nTrs[i]).find("input[type=checkbox]").prop('checked')){
                var row = $mytable.tableObj.fnGetData(nTrs[i]);
                ids.push(row.id);
                customerIds.push(row.customerId);
                buIds.push(row.buId);
                states.push(row.state);
            }
        }
        if(ids.length == 0){
            $custom.alert.error("请至少选择一单");
            return;
        }

        var customerId0 = null;
        customerIds.forEach(function (val, index) {
            if (index == 0) {
                customerId0 = val;
            } else if (customerId0 != val) {
                check = false;
                $custom.alert.warningText("请选择相同“客户“");
                return ;
            }
        });

        if (check) {
            var buIds0 = null;
            buIds.forEach(function (val, index) {
                if (index == 0) {
                    buIds0 = val;
                } else if (buIds0 != val) {
                    check = false;
                    $custom.alert.warningText("请选择相同“事业部“");
                    return ;
                }
            });
        }

        if (check) {
            states.forEach(function (val, index) {
                if (val != '001') {
                    check = false;
                    $custom.alert.warningText("存在不是待审核数据");
                    return ;
                }
            });
        }

        if (check) {
            $m18001.init(checkeds);
        }

    });

    //批量导入
    $("#import-buttons").click(function () {
        $load.a("/supplierMerchandisePrice/toImportPage.html");
    });
    //导出
    $("#export-buttons").on("click", function () {
        //根据筛选条件导出
        var jsonData = null;
        var jsonArray = $("#form1").serializeArray();
        $.each(jsonArray, function (i, field) {
            if (field.value != null && field.value != '' && field.value != undefined) {
                jsonData = $json.setJson(jsonData, field.name, field.value);
            }
        });
        var form = JSON.parse(jsonData);

        $custom.request.ajax("/supplierMerchandisePrice/getOrderCount.asyn", form, function (data) {//判断导出的数量是否超出1W
            if (data.state == 200) {
                var total = data.data.total;//总数
                if (total > 10000) {
                    $custom.alert.error("导出的数量过大，请填写筛选条件再导出");
                    return;
                }
                //导出数据
                var url = "/supplierMerchandisePrice/exportSMPrice.asyn";
                var i = 0;
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
            } else {
                if (!!data.expMessage) {
                    $custom.alert.error(data.expMessage);

                } else {
                    $custom.alert.error(data.message);

                }
            }
        });
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
    })
    $('#datatable-buttons').on("change", ":checkbox", function () {
        $(this).parents('tr').toggleClass('success');
    })
    $(function () {
        $("#datatable-buttons_wrapper").removeClass('container-fluid');
    })

    function loadPriceData(state) {
        chooseState = state;
        searchData();
    }

    function statisticsStateNum() {
        var url = "/supplierMerchandisePrice/statisticsStateNum.asyn";
        $custom.request.ajax(url, $("#form1").serializeArray(), function (result) {
            if (result.state == '200') {
                $("#submitSpan").text("(" + result.data.submitNum + ")")
                $("#auditSpan").text("(" + result.data.auditNum + ")")
                $("#beAuditSpan").text("(" + result.data.beAuditNum + ")")
                $("#rejectSpan").text("(" + result.data.rejectNum + ")")
                $("#totalSpan").text("(" + result.data.allNum + ")")
            }
        });
    }

    //提交
    $("#submit-buttons").click(function () {
        var check = true;
        var ids = [];
        var customerIds = [];
        var buIds = [];
        var states = [];

        var nTrs = $mytable.tableObj.fnGetNodes();//fnGetNodes获取表格所有行，nTrs[i]表示第i行tr
        for(var i = 0; i < nTrs.length; i++){
            if($(nTrs[i]).find("input[type=checkbox]").prop('checked')){
                var row = $mytable.tableObj.fnGetData(nTrs[i]);
                ids.push(row.id);
                customerIds.push(row.customerId);
                buIds.push(row.buId);
                states.push(row.state);
            }
        }
        if(ids.length == 0){
            $custom.alert.error("请至少选择一单");
            return;
        }

        var customerId0 = null;
        customerIds.forEach(function (val, index) {
            if (index == 0) {
                customerId0 = val;
            } else if (customerId0 != val) {
                check = false;
                $custom.alert.warningText("请选择相同“客户“");
                return ;
            }
        });

        if (check) {
            states.forEach(function (val, index) {
                if (val != '002') {
                    check = false;
                    $custom.alert.warningText("存在不是待提交数据");
                    return ;
                }
            });
        }

        if (check) {
            var buIds0 = null;
            buIds.forEach(function (val, index) {
                if (index == 0) {
                    buIds0 = val;
                } else if (buIds0 != val) {
                    check = false;
                    $custom.alert.warningText("请选择相同“事业部“");
                    return ;
                }
            });
        }

        if (check) {
            $custom.alert.warning("确认提交选中数据？", function () {
                $custom.request.ajax("/supplierMerchandisePrice/submitSMPrice.asyn", {"ids": ids.join(",")}, function (data) {
                    if (data.state == 200) {
                        $custom.alert.success("提交成功");
                        //重新加载页面
                        $mytable.fn.reload();
                    } else {
                        if (!!data.expMessage) {
                            $custom.alert.error(data.expMessage);
                        } else {
                            $custom.alert.error(data.message);
                        }
                    }
                });
            });
        }
    });

    function edit(id) {
        $m18002.init(id);
    }

    function showCustomer(){
        var selectObj=$('select[name="customerIds"]');
        var json={"type": ""}
        var jsonData=$module.constant.ajax($module.params.getAllSupplierURL,json);

        selectObj.empty();

        width = "136px" ;
        $(selectObj).removeClass("input_msg") ;

        $(selectObj).wrap('<div style="width:'+ width +'; display: inline-block;"></div>') ;

        $(selectObj).addClass("selectpicker").addClass("show-tick") ;
        $(selectObj).attr("data-live-search", "true") ;
        $(selectObj).attr("multiple","multiple") ;

        jsonData.forEach(function(o,i){
            selectObj.append("<option value='"+ o.value+"'>"+o.label+"</option>");
        });

        $(selectObj).selectpicker({width: width});
        $(selectObj).selectpicker({noneSelectedText : '请选择'});
        $(selectObj).selectpicker('refresh');

        $(".selectpicker").prev().prev().css({"height": "30px","margin-bottom": "5px","border": "1px solid #dadada","background": "white"}) ;
        $(".selectpicker").prev().css({"z-index":"99"});
        $(".selectpicker").prev().find(".dropdown-menu.inner").css({"height":"220px"}) ;

    }

</script>