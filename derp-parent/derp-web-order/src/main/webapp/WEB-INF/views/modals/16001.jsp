<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- Custom Modals -->
<div class="row">
    <div class="col-12">
        <div>
            <!-- Signup modal content -->
            <div id="invoice-modal" class="modal fade" tabindex="-1" role="dialog"
                 aria-labelledby="custom-width-modalLabel" aria-hidden="true"
                 style="display: none;position:fixed;top:130px;bottom:auto;height:100%;">
                <div class="modal-dialog">
                    <div class="modal-content" style="width: 600px;text-align: center;">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                            <h5 class="modal-title" id="myLargeModalLabel">选择发票模板</h5>
                        </div>
                        <div class="modal-body">
                            <form id="popup-goods-form">
                                <div class="form-row mt10">
                                    <div class="col-6">
                                        <div class="row">
                                            <label class="col-4 col-form-label">
                                                <div class="fr">发票名称<span>：</span></div>
                                            </label>
                                            <div class="col-8"><input name="title" id="title" type="text" class="form-control"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-2">
                                        <button type="button" class="btn btn-info waves-effect waves-light fr btn-sm"
                                                onclick="$m16001.searchData();">查询
                                        </button>
                                    </div>
                                    <div class="col-4">

                                    </div>
                                </div>
                            </form>
                            <div class="form-row mt20">
                                <table id="fileTemp-detail" class="table table-striped" cellspacing="0" width="100%">
                                    <thead>
                                    <tr>
                                        <th style="text-align: center;">
                                            <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                                <input type="checkbox" name="keeperMerchantGroup-checkable"
                                                       class="group-checkable"/>
                                            </label>
                                        </th>
                                        <th style="text-align: center;">发票模板名称</th>
                                        <th style="text-align: center;">发票编码</th>
                                        <th style="text-align: center;">适用客户</th>
                                    </tr>
                                    </thead>
                                    <tbody id="model-tbody"></tbody>
                                </table>
                            </div>
                        </div>

                        <div class="form-row ml20" style="padding-bottom: 20px;">
                            <div class="col-12">
                                <div class="row">
                                    <div class="col-4"></div>
                                    <div class="col-2">
                                        <button type="button" class="btn btn-info waves-effect waves-light fr btn-sm"
                                                onclick='$m16001.save()'>确认
                                        </button>
                                    </div>
                                    <div class="col-2">
                                        <button type="button"
                                                class="btn btn-light waves-effect waves-light btn_default btn-sm"
                                                onclick='$m16001.hide();'>取消
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div><!-- /.modal -->
    </div>
</div>
<!-- end col -->
</div>
<script type="text/javascript">


    var $m16001 = {
        init: function (url, customerId, type) {
            $m16001.params.url = url;
            $m16001.params.customerId = customerId;
            $("#model-tbody").empty();
            
            var data = {"customerIds": $m16001.params.customerId, "type": type} ;
            
            if($m16001.params.url.substring(0, $m16001.params.url.indexOf("ids")).indexOf("300") > -1){
            	data["type"] = "1" ;
            }
            $custom.request.ajax(serverAddr + '/fileTemp/listFileTempInfo.asyn', data, function (result) {
                if (result.state == 200) {
                    var list = result.data;
                    if (list) {

                        if(list.length == 0){

                            //如果包含采购跳转码，直接跳转
                            if($m16001.params.url.indexOf("300") > -1){
                                $load.a($m16001.params.url);

                                return ;
                            }

                            $custom.alert.error("该订单客户没有关联的发票模板");
                            return ;
                        }

                        if(list.length == 1){
                            var id = $(list).get(0).id;
                            $load.a($m16001.params.url + '&tempId=' + id);

                            return ;
                        }

                        $($m16001.params.modalId).modal({backdrop: 'static', keyboard: false});
                        var html = "";
                        $(list).each(function (i, m) {
                            var id = m.id;
                            var title = m.title;
                            var code = m.code;
                            var customers = m.customers;
                            html += '<tr>'
                                + '<td class="temp-checkbox"><input type="checkbox" value="' + id + '" class="iCheck" ' + '></td>'
                                + '<td>' + title + '</td>'
                                + '<td>' + code + '</td>'
                                + '<td>' + customers + '</td>'
                                + '</tr>';

                        });
                        $("#fileTemp-detail").append(html);
                    }
                } else {
                    var message = result.data.message;
                    if (message != null && message != '' && message != undefined) {
                        $custom.alert.error(message);
                    } else {
                        $custom.alert.error("没有关联模板！");
                    }
                }
            });
        },
        hide: function () {
            $($m16001.params.modalId).modal("hide");
        },
        save: function () {
            var checked = $(".temp-checkbox").find("input[type=checkbox]:checked");
            if (checked.length == 0) {
                $custom.alert.warningText("请选择模板！");
                return;
            }
            if (checked.length > 1) {
                $custom.alert.warningText("只能选择一个模板！");
                return;
            }
            var id = $(".temp-checkbox").find("input[type=checkbox]:checked").val();
            $($m16001.params.modalId).modal("hide");
            $('.modal-backdrop').remove();//去掉遮罩层
            $load.a($m16001.params.url + '&tempId=' + id);
        },
        searchData: function () {
            $("#model-tbody").empty();
            var title = $("#title").val();
            $custom.request.ajax(serverAddr + '/fileTemp/listFileTempInfo.asyn', {
                "title": title,
                "customerIds": $m16001.params.customerId
            }, function (result) {
                if (result.state == 200) {
                    var list = result.data;
                    if (list) {
                        var html = "";
                        $(list).each(function (i, m) {
                            var id = m.id;
                            var title = m.title;
                            var code = m.code;
                            var customers = m.customers;
                            html += '<tr>'
                                + '<td class="temp-checkbox"><input type="checkbox" value="' + id + '" class="iCheck" ' + '></td>'
                                + '<td>' + title + '</td>'
                                + '<td>' + code + '</td>'
                                + '<td>' + customers + '</td>'
                                + '</tr>';

                        });
                        $("#fileTemp-detail").append(html);
                    }
                }
            });

        },
        params: {
            modalId: '#invoice-modal',
            url: null,
            customerId: null
        }
    };

</script>