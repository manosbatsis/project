<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- Custom Modals -->
<style>
    .choose {
        color: red;
    }
</style>
<div>
    <!-- Signup modal content -->
    <div id="signup-modal1" class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog"
         aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;" data-backdrop="static">
        <div class="modal-dialog  modal-lg">
            <div class="modal-content" style="width: 920px;">
                <div class="modal-header">
                    <h5 class="modal-title" id="myLargeModalLabel">编辑采购价格</h5>
                </div>
                <div class="modal-body">
                    <!-- 加载中图片 -->
                    <img id="loadingDiv" src="/resources/assets/images/loading.gif" width="180" height="180"
                         style="display: none;position:absolute; z-index:2; filter:alpha(opacity=60);opacity:0.3;top: 25%;left: 50%"
                         alt="加载中...">

                    <form id="order_form">
                        <div class="form-row">
                            <div class="col-4">
                                <div class="row">
                                    <label class="col-5 col-form-label">
                                        <div class="fr"><i class="red">*</i>公司<span>：</span></div>
                                    </label>
                                    <div class="col-7 ml10">
                                        <input type="text" class="form-control" id="editMerName" readonly>
                                    </div>
                                </div>
                            </div>
                            <div class="col-4">
                                <div class="row">
                                    <label class="col-5 col-form-label " style="white-space:nowrap;">
                                        <div class="fr"><i class="red">*</i>事业部<span>：</span></div>
                                    </label>
                                    <div class="col-7 ml10">
                                        <input type="text" class="form-control" id="editBuName" readonly>
                                    </div>
                                </div>
                            </div>
                            <div class="col-4">
                                <div class="row">
                                    <label class="col-5 col-form-label " style="white-space:nowrap;">
                                        <div class="fr"><i class="red">*</i>供应商编码<span>：</span></div>
                                    </label>
                                    <div class="col-7 ml10">
                                        <input type="text" class="form-control" id="editCusCode" readonly>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="col-4">
                                <div class="row">
                                    <label class="col-5 col-form-label">
                                        <div class="fr"><i class="red">*</i>标准条码<span>：</span></div>
                                    </label>
                                    <div class="col-7 ml10">
                                        <input type="text" class="form-control" id="editCommbarcode" readonly>
                                    </div>
                                </div>
                            </div>
                            <div class="col-4">
                                <div class="row">
                                    <label class="col-5 col-form-label " style="white-space:nowrap;">
                                        <div class="fr"><i class="red">*</i>供应商名称<span>：</span></div>
                                    </label>
                                    <div class="col-7 ml10">
                                        <input type="text" class="form-control" id="editCusName" readonly>
                                    </div>
                                </div>
                            </div>
                            <div class="col-4">
                                <div class="row">
                                    <label class="col-5 col-form-label " style="white-space:nowrap;">
                                        <div class="fr"><i class="red">*</i>商品名称<span>：</span></div>
                                    </label>
                                    <div class="col-7 ml10">
                                        <input type="text" class="form-control" id="goodsName" readonly>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="col-4">
                                <div class="row">
                                    <label class="col-5 col-form-label">
                                        <div class="fr"><i class="red">*</i>品牌<span>：</span></div>
                                    </label>
                                    <div class="col-7 ml10">
                                        <input type="text" class="form-control" id="brandName" readonly>
                                    </div>
                                </div>
                            </div>
                            <div class="col-4">
                                <div class="row">
                                    <label class="col-5 col-form-label " style="white-space:nowrap;">
                                        <div class="fr"><i class="red">*</i>规格型号<span>：</span></div>
                                    </label>
                                    <div class="col-7 ml10">
                                        <input type="text" class="form-control" id="spec" readonly>
                                    </div>
                                </div>
                            </div>
                            <div class="col-4">
                                <div class="row">
                                    <label class="col-5 col-form-label " style="white-space:nowrap;">
                                        <div class="fr"><i class="red">*</i>币种<span>：</span></div>
                                    </label>
                                    <div class="col-7 ml10">
                                        <select class="input_msg" name="currency" id="currency"></select>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="col-4">
                                <div class="row">
                                    <label class="col-5 col-form-label">
                                        <div class="fr"><i class="red">*</i>采购价<span>：</span></div>
                                    </label>
                                    <div class="col-7 ml10">
                                        <input type="text" class="form-control" name="supplyPrice" id="supplyPrice">
                                    </div>
                                </div>
                            </div>
                            <div class="col-4">
                                <div class="row">
                                    <label class="col-5 col-form-label " style="white-space:nowrap;">
                                        <div class="fr"><i class="red">*</i>报价生效时间<span>：</span></div>
                                    </label>
                                    <div class="col-7 ml10">
                                        <input type="text" class="form-control date-input" name="effectiveDateStr" id="effectiveDateStr">
                                    </div>
                                </div>
                            </div>
                            <div class="col-4">
                                <div class="row">
                                    <label class="col-5 col-form-label " style="white-space:nowrap;">
                                        <div class="fr"><i class="red">*</i>报价失效时间<span>：</span></div>
                                    </label>
                                    <div class="col-7 ml10">
                                        <input type="text" class="form-control date-input" name="expiryDateStr" id="expiryDateStr">
                                    </div>
                                </div>
                            </div>
                        </div>

                    </form>
                    <div class="form-row mt20">
                        <div class="form-inline m0auto">
                            <div class=form-group>
                                <button type="button" id="billSaveBtn" class="btn btn-info waves-effect waves-light fr"
                                        onclick='$m18002.save();'>保存
                                </button>
                                <button type="button" class="btn btn-light waves-effect waves-light ml15"
                                        onclick='$m18002.hide();'>取消
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
        <%--        <jsp:include page="/WEB-INF/views/modals/13006.jsp" />--%>
    </div><!-- /.modal -->
</div>

<script type="text/javascript">
    var $m18002 = {
        init: function (id) {
            $module.constant.getConstantListByNameURL.call($('select[name="currency"]')[0], "currencyCodeList", '');
            $(":input").val("");
            $custom.request.ajax("/supplierMerchandisePrice/getDetailsById.asyn", {"id": id}, function (data) {
                if (data.state == 200) {
                    var data = data.data;
                    $m18002.params.id = data.id;
                    $("#editMerName").val(data.merchantName);
                    $("#editBuName").val(data.buName);
                    $("#editCusCode").val(data.customerCode);
                    $("#editCusName").val(data.customerName);
                    $("#editCommbarcode").val(data.commbarcode);
                    $("#goodsName").val(data.goodsName);
                    $("#brandName").val(data.brandName);
                    $("#spec").val(data.spec);
                    $("#currency").val(data.currency);
                    $("#supplyPrice").val(data.supplyPrice);
                    $("#effectiveDateStr").val(formatDate(data.effectiveDate, "yyyy-MM-dd"));
                    $("#expiryDateStr").val(formatDate(data.expiryDate, "yyyy-MM-dd"));
                } else {
                    if (!!data.expMessage) {
                        $custom.alert.error(data.expMessage);
                    } else {
                        $custom.alert.error(data.message);
                    }
                }
            });
            $($m18002.params.modalId).modal("show")
        },
        hide: function () {
            $($m18002.params.modalId).modal("hide");
        },
        save: function () {
            var currency = $("#currency").val();
            var effectiveDateStr = $("#effectiveDateStr").val();
            var expiryDateStr = $("#expiryDateStr").val();
            var supplyPrice = $("#supplyPrice").val();
            $custom.request.ajax("/supplierMerchandisePrice/modifySMPrice.asyn", {"id":$m18002.params.id, "currency": currency,"effectiveDateStr":effectiveDateStr,
            "expiryDateStr":expiryDateStr, "supplyPrice":supplyPrice}, function (data) {
                if (data.state == 200) {
                    $custom.alert.success("编辑成功");
                    $m18002.hide();
                    // $('.nav-tabs li:eq(0) a').tab('show');
                    //重新加载页面
                    searchData();
                } else {
                    if (!!data.expMessage) {
                        $custom.alert.error(data.expMessage);
                    } else {
                        $custom.alert.error(data.message);
                    }
                }
            });

        },
        params: {
            formId: '#order_form',
            modalId: '#signup-modal1',
            id: null
        }
    }

    laydate.render({
        elem: '#effectiveDateStr', //指定元素
    });

    laydate.render({
        elem: '#expiryDateStr', //指定元素
    });

    function formatDate(date, fmt) {
        if (!date) {
            return '';
        }
        date = date;
        fmt=fmt || 'yyyy-MM-dd hh:mm:ss';
        if(Object.prototype.toString.call(date).slice(8,-1)!=='Date'){
            date=new Date(date)
        }
        if (/(y+)/.test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
        }
        let o = {
            'M+': date.getMonth() + 1,
            'd+': date.getDate(),
            'h+': date.getHours(),
            'm+': date.getMinutes(),
            's+': date.getSeconds()
        }
        for (let k in o) {
            if (new RegExp("(" + k + ")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
        return fmt
    }

</script>