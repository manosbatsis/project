<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div>
    <!-- Signup modal content -->
    <div id="signup-modal" class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel"
         aria-hidden="true" style="display: none; overflow-y: auto;">
        <div class="modal-dialog  modal-lg" style="max-width:1200px  !important;">
            <div class="modal-content" style="max-width:1200px;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h5 class="modal-title" id="myLargeModalLabel">分配事业部</h5>
                </div>
                <div class="modal-body">
                    <div class="form-row">
                        <button type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick="$m11012.assign()">分配事业部</button>
                    </div>
                    <div class="form-row mt20">
                        <table id="datatable-detail" class="table table-striped" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>
                                    <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                        <input type="checkbox" name="keeperMerchantGroup-checkable" class="group-checkable" />
                                    </label>
                                </th>
                                <th>PO单号</th>
                                <th>商品货号</th>
                                <th>商品名称</th>
                                <th>商品条形码</th>
                                <th>事业部</th>
                                <th>结算单价</th>
                                <th>调整类型</th>
                                <th>原始批次号</th>
                                <th>总调整数量</th>
                                <th>是否坏品</th>
                                <th>海外理货单位</th>
                                <th>生产日期</th>
                                <th>失效日期</th>
                            </tr>
                            </thead>
                            <tbody id="data-tbody">
                            </tbody>
                        </table>
                    </div>
                    <div class="form-row mt20">
                        <div class="form-inline m0auto">
                            <div class=form-group>
                                <button type="button" class="btn btn-info waves-effect waves-light fr" onclick='$m11012.save();'>确定</button>
                                <button type="button" class="btn btn-light waves-effect waves-light ml15"  onclick='$m11012.hide();'>取消</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<jsp:include page="/WEB-INF/views/modals/11011.jsp" />
<script type="text/javascript">

    var $m11012={
        init:function(id, depotId){
            $m11012.params.inventoryId = id ;
            $m11012.show(id);
            $m11012.params.depotId = depotId;
            //分配事业部弹窗——事业部下拉框加载
            $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("#modalBuId")[0],null, null,depotId,null);
        },
        show:function(id){
            $("#data-tbody").empty();
            $("input[name='keeperMerchantGroup-checkable']").prop("checked", false);
            $custom.request.ajax($m11012.params.detailUrl, {"id":id}, function(result){
                if(result.state == 200) {
                    var list = result.data.itemList;
                    if(list){
                        var html = "" ;
                        $(list).each(function(i, m) {
                            var poNo = m.poNo;
                            var goodsNo = m.goodsNo;
                            var goodsName = m.goodsName;
                            var barcode = m.barcode;
                            var buId = m.buId;
                            var buName = m.buName;
                            var settlementPrice = m.settlementPrice;
                            var typeLabel = m.typeLabel;
                            var oldBatchNo = m.oldBatchNo;
                            var adjustTotal = m.adjustTotal;
                            var isDamageLabel = m.isDamageLabel;
                            var tallyingUnitLabel = m.tallyingUnitLabel;
                            var productionDate = m.productionDate;
                            var overdueDate = m.overdueDate;
                            var itemId = m.id;
                            html += '<tr><td class=" td-checkbox"><input type="checkbox" value="' + itemId + '" class="iCheck" ' + '></td>'
                                + '<td>' + $m11012.strFormat(poNo) + '</td>'
                                + '<td>' + $m11012.strFormat(goodsNo) + '</td>'
                                + '<td>' + $m11012.strFormat(goodsName) + '</td>'
                                + '<td>' + $m11012.strFormat(barcode) + '</td>'
                                + '<td>' + $m11012.strFormat(buName) + '<input type="hidden" value="' + $m11012.strFormat(buId) + '"></td>'
                                + '<td>' + $m11012.strFormat(settlementPrice) + '</td>'
                                + '<td>' + $m11012.strFormat(typeLabel) + '</td>'
                                + '<td>' + $m11012.strFormat(oldBatchNo) + '</td>'
                                + '<td>' + $m11012.strFormat(adjustTotal) + '</td>'
                                + '<td>' + $m11012.strFormat(isDamageLabel) + '</td>'
                                + '<td>' + $m11012.strFormat(tallyingUnitLabel) + '</td>'
                                + '<td>' + $m11012.formatDate(productionDate, "yyyy-MM-dd") + '</td>'
                                + '<td>' + $m11012.formatDate(overdueDate, "yyyy-MM-dd") + '</td>'
                                + '</tr>';

                        });
                        $("#datatable-detail").append(html);
                    }
                }
            });
            $($m11012.params.modalId).modal({backdrop: 'static', keyboard: false});
        },
        hide:function(){
            $($m11012.params.modalId).modal("hide");
        },
        assign:function(){
            var checked = $(".td-checkbox").find("input[type=checkbox]:checked") ;
            if (checked.length == 0) {
                $custom.alert.warningText("请选择商品！");
                return;
            }
            var rows = new Array ();
            $(checked).each(function(i,m){
                var id = $(m).val() ;
                rows.push(id);
            });
            rows = rows.join(",") ;
            $('#modelDepotId').val($m11012.params.depotId);
            //分配事业部弹窗
            $('#confirm-modal').modal({backdrop: 'static', keyboard: false});
        },
        save:function () {
            var check = true; //是否全部事业部选择完整，若是，校验可用量并推送库存
            var buIsNull = true; //
            //遍历表体，判断事业部是否选择完整
            $("#data-tbody").find("tr").each(function(){
                var buId = $(this).children().eq(5).find("input").val();
                if (!buId) {
                    check = false;
                } else {
                    buIsNull = false;
                }
            });
            if (buIsNull) {
                $custom.alert.warning("至少有一个商品分配事业部");
                return;
            }
            //校验可用量
            if (check) {
                //获取商品数量 检查可用库存量
                var getUrl = serverAddr+"/adjustmentInventory/getAdjustmentSum.asyn";
                $custom.request.ajaxSync(getUrl,{"ids":$m11012.params.inventoryId},function(result) {
                    if (result.state == '200') {
                        var mergeList = result.data.mergeList;
                        if (mergeList) {
                            for (var k = 0; k < mergeList.length; k++) {
                                var item = mergeList[k];
                                var depotId = item.depot_id;
                                var goodsId = item.goods_id;
                                var goodsNo = item.goods_no;
                                var depotcode = item.depot_code;
                                var type = item.is_damage;//是否坏品 0-好品 1-坏品
                                var isExpire = item.isExpire;//是否过期0-是 1-否
                                var batchNo = item.old_batch_no;//是否过期0-是 1-否
                                var adjustTotal = item.adjust_total;//调整量

                                //查询可用量
                                var tallyingUnit = null;
                                if (item.depot_type == "c") {
                                    tallyingUnit = item.tallying_unit;
                                }
                                var availableNum = $module.inventory.queryAvailability(depotId, goodsId, depotcode, tallyingUnit, type, isExpire, batchNo);
                                if (availableNum == -1) {
                                    check = false;
                                    $custom.alert.warningText("货号:" + goodsNo + "未查到库存余量");
                                    break;
                                } else if (adjustTotal > availableNum) {
                                    check = false;
                                    if (batchNo != null) {
                                        $custom.alert.warningText("库存不足货号:" + goodsNo + "批次:" + batchNo + ",余量：" + availableNum);
                                    } else {
                                        $custom.alert.warningText("库存不足货号：" + goodsNo + ",余量：" + availableNum);
                                    }
                                    break;
                                }
                            }
                        }
                    } else {
                        $custom.alert.warningText("确认入库失败！");
                        return;
                    }
                    //保存并推送库存
                    if (check) {
                        $m11012.saveData();
                    }
                });
            } else { //保存数据
                $m11012.saveData();
            }
        },
        saveData: function () {
            var goodsJson = [];
            var saveJson = {};
            $("#data-tbody").find("tr").each(function(){
                var itemId = $(this).children().eq(0).find("input").val(); //表体id
                var buId = $(this).children().eq(5).find("input").val(); //事业部id
                if (!buId) { return true;}
                var json = {};
                json.id = itemId;
                json.buId = buId;
                goodsJson.push(json)
            });
            saveJson.id = $m11012.params.inventoryId;
            saveJson.itemList = goodsJson;
            var jsonStr = JSON.stringify(saveJson);
            if (goodsJson.length == 0) {
                $custom.alert.error("商品没有分配事业部");
                return;
            }
            $custom.request.ajax($m11012.params.saveUrl,{"json":jsonStr},function(result){
                if (result.state == 200) {
                    if (result.data.code == '00') {
                        $custom.alert.success("保存成功！");
                        $m11012.hide();
                        //重新加载页面
    					setTimeout(function () {
    						$load.a(pageUrl+"10002");
    					}, 1000);
                    } else if (result.data.code == "01") {
                        var message = result.data.message;
                        if (message) {
                            $custom.alert.error(message);
                        } else {
                            $custom.alert.error("保存失败！");
                        }
                    }
                } else {
                    $custom.alert.error(result.expMessage);
                }
            });
        },
        strFormat:function (str) {
            if (!str) {
                return "";
            }
            return str;
        },
        formatDate:function (date, fmt) {
            date=date || new Date();
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
        },
        params:{
            detailUrl:serverAddr + '/adjustmentInventory/getDetails.asyn?r='+Math.random(),
            saveUrl:serverAddr + '/adjustmentInventory/saveBuDetails.asyn?r='+Math.random(),
            inventoryId:"" ,
            depotId:"",
            modalId:'#signup-modal'
        }
    };

    $("input[name='keeperMerchantGroup-checkable']").on("change", function(){
        if($(this).is(':checked')){
            $(":checkbox", '#datatable-detail').prop("checked",$(this).prop("checked"));
            $('#datatable-detail tbody tr').addClass('success');
        }else{
            $(":checkbox", '#datatable-detail').prop("checked",false);
            $('#datatable-detail tbody tr').removeClass('success');
        }
    })
    $('#datatable-detail').on("change", ":checkbox", function() {
        $(this).parents('tr').toggleClass('success');
    })

</script>