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
                    <h5 class="modal-title" id="myLargeModalLabel">本次销售商品数量</h5>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" id="invoice-from">
                        <!-- 自定义参数  -->
                        <div class="form-group">
                            <div class="col-12">
                                <label >客户 : </label>
                                <select  class="edit_input" disabled name="customerId2" id="customerId2"style="width:200px;" parsley-trigger="change" required>
                                    <option value="">请选择</option>
                                </select>
                                <label >PO号 : </label>
                                <input type="text" class="edit_input" name="poNo2" id="poNo2" style="width: 200px;" readonly>
                                <input class="form-control" id="id" name="id"  type="hidden" >
                                <label >预售单号 : </label>
                                <input readonly type="text" class="input_msg" id="code2" name="code2"style="width: 400px;" >
                            </div>
                        </div>
                    </form>
                    <div class="form-row mt20">
                        <table id="datatable-detail" class="table table-striped" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>
                                    <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                        <input type="checkbox" name="keeperMerchantGroup-checkable" class="group-checkable" />
                                    </label>
                                </th>
	                     	   <th>商品货号</th>
	                           <th>商品名称</th>
	                           <th>预售总量</th>
	                           <th>预售单价</th>
	                           <th>待销量</th>
	                           <th>本次销售量</th>
                            </tr>
                            </thead>
                            <tbody id="data-tbody">
                            </tbody>
                        </table>
                    </div>
                    <div class="form-row mt20">
                        <div class="form-inline m0auto">
                            <div class=form-group>
                                <button type="button" class="btn btn-info waves-effect waves-light fr" onclick='$m9061.save();'>生成销售订单</button>
                                <button type="button" class="btn btn-light waves-effect waves-light ml15"  onclick='$m9061.hide();'>取消</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">
    var $m9061={
        init:function(data,infoData,ids){
            $m9061.params.customerId = infoData.customerId ;
            $m9061.params.poNo = infoData.poNo ;
            $m9061.params.preSaleOrderCode = infoData.preSaleOrderCode ;
            $m9061.params.businessModel = infoData.businessModel ;
            $m9061.params.outDepotId = infoData.outDepotId ;
            $m9061.params.buId = infoData.buId ;
            $m9061.params.merchantId = infoData.merchantId ;
            $m9061.params.merchantName = infoData.merchantName ;
            $m9061.params.ids = ids ;// 关联的预售单

            //加载客户的下拉数据
            $module.customer.loadSelectData.call($('select[name="customerId2"]')[0],infoData.customerId);
            $("#poNo2").val(infoData.poNo);
            $("#code2").val(infoData.preSaleOrderCode);
            $m9061.show(data);
        },
        show:function(data){
            $("#data-tbody").empty();
            $("input[name='keeperMerchantGroup-checkable']").prop("checked", false);
                var html = "" ;
                for (var i = 0; i < data.length; i++) {
                    var m = data[i];
                    var goodsId = m.goodsId;
                    var goodsName = m.goodsName;
                    var goodsNo = m.goodsNo;
                    var preNum = m.preNum;
                    var price = m.price;
                //    var saleNum = m.saleNum;
                    var barcode = m.barcode;
                    var commbarcode = m.commbarcode;
                    var brandName = m.brandName;
                    var staySaleNum = m.staySaleNum;
                    var goodsCode = m.goodsCode;
                    html += '<tr><td class=" td-checkbox"><input type="checkbox"  name="item-checkbox"  value="' + goodsId + '" class="iCheck" ' + '></td>'
                        + '<td><input type="hidden" name="goodsNo2" value="'+$m9061.strFormat(goodsNo)+'">' + $m9061.strFormat(goodsNo) + '</td>'
                        + '<td><input type="hidden" name="goodsName2" value="'+$m9061.strFormat(goodsName)+'">' + $m9061.strFormat(goodsName) + '</td>'
                        + '<td><input type="hidden" name="preNum2" value="'+preNum+'">' + $m9061.strFormat(preNum) + '</td>'
                        + '<td><input type="hidden" name="price2" value="'+$m9061.strFormat(price)+'">' + $m9061.strFormat(price) + '</td>'
                        + '<td><input type="hidden" name="staySaleNum2" value="'+staySaleNum+'">' + $m9061.strFormat(staySaleNum) + '</td>'
                        + '<td><input type="text" name="num2"></td>'
                        + '<td><input type="hidden" name="barcode2" value="'+$m9061.strFormat(barcode)+'"></td>'
                        + '<td><input type="hidden" name="" value="'+$m9061.strFormat(commbarcode)+'"></td>'
                        + '<td><input type="hidden" name="brandName2" value="'+$m9061.strFormat(brandName)+'"></td>'
                        + '<td><input type="hidden" name="goodsCode2" value="'+$m9061.strFormat(goodsCode)+'"></td>'
                        + '</tr>';
                }
                $("#datatable-detail").append(html);
            $($m9061.params.modalId).modal("show");
        },
        hide:function(){
            $($m9061.params.modalId).modal("hide");
        },
        save:function(){
            var checked = $(".td-checkbox").find("input[type=checkbox]:checked") ;
            if (checked.length == 0) {
                $custom.alert.error("请选择商品！");
                return;
            }
             var isSameGoods = true;
             var compareNum=true;//比较数量
             var str = '';
             //遍历表体，判断销售量是否有一个有值
            var existGoodsArr = [];
             $("#data-tbody").find("tr").each(function(){
                 var staySaleNum = $(this).find('td').eq(5).find("input").val();// 待销售量
                 var saleNum = $(this).find('td').eq(6).find("input").val();// 销售量
                 var goodsId = $(this).find('td').eq(0).find("input").val(); //商品id

                 if (saleNum!=null && saleNum!=''&& saleNum!=undefined && saleNum!=0) {
                     str += saleNum;
                     if(parseInt(staySaleNum)<parseInt(saleNum)){
                         compareNum = false;
                     }
                     var goodsId = $(this).find('td').eq(0).find("input").val(); //商品id
                     if (existGoodsArr.indexOf(goodsId) != -1) {
                         isSameGoods=false;
                     }
                     existGoodsArr.push(goodsId);
                 }
             });

                if (str == ''){
                    $custom.alert.error("至少得选择一个商品进行分配销售量");
                    return;
                }
                if (!compareNum) {
                    $custom.alert.error("本次销售量不能大于待销量");
                    return;
                }
                if (!isSameGoods) {
                    $custom.alert.error("不能选择不同单价的相同商品");
                    return;
                }
                 var goodsJson = [];
                 var saveJson = {};
                 $("#data-tbody").find("tr").each(function(){

                     var num = $(this).find('td').eq(6).find("input").val();
                     if(num!=null && num!='' && num!=undefined && num!=0){
                     var goodsId = $(this).find('td').eq(0).find("input").val(); //商品id
                         /*  var goodsNo = $(this).find('td').eq(1).find("input").val();
                         var goodsName = $(this).find('td').eq(2).find("input").val();*/
                     var preNum = $(this).find('td').eq(3).find("input").val();
                     var price = $(this).find('td').eq(4).find("input").val();
                     var staySaleNum = $(this).find('td').eq(5).find("input").val();
                     var num = $(this).find('td').eq(6).find("input").val();
                   /*  var barcode = $(this).find('td').eq(7).find("input").val();
                     var commbarcode = $(this).find('td').eq(8).find("input").val();
                     var goodsCode = $(this).find('td').eq(10).find("input").val();*/
                     var brandName = $(this).find('td').eq(9).find("input").val();

                     if (!buId) { return true;}
                     var json = {};
                     json.id = goodsId;
                   /*  json.goodsNo = goodsNo;
                     json.goodsName = goodsName;*/
                     json.preNum = preNum;
                     json.price = price;
                     json.staySaleNum = staySaleNum;
                     json.num = num;
                    /* json.barcode = barcode;
                     json.commbarcode = commbarcode;
                     json.goodsCode = goodsCode;*/
                     json.brandName = brandName;

                     goodsJson.push(json)
                      }
                 });
                 saveJson.customerId = $m9061.params.customerId;
                 saveJson.businessModel = $m9061.params.businessModel;
                 saveJson.outDepotId = $m9061.params.outDepotId;
                 saveJson.buId = $m9061.params.buId;
                 saveJson.preSaleOrderCode = $m9061.params.preSaleOrderCode;
                 saveJson.poNo = $m9061.params.poNo;
                 saveJson.merchantId=$m9061.params.merchantId;
                 saveJson.merchantName=$m9061.params.merchantName;
                 saveJson.ids=$m9061.params.ids;//关联的预售单
                 saveJson.type="1";
                 saveJson.itemList = goodsJson;
                 var jsonStr = encodeURIComponent(JSON.stringify(saveJson));
                 $('.modal-backdrop').remove();//去掉遮罩层
                 $("#signup-modal").modal('hide');
                 $module.load.pageOrder("act=40020&data="+jsonStr);

        },
        strFormat:function (str) {
            if (!str) {
                return "";
            }
            return str;
        },
        params:{
            poNo:"" ,
            customerId:"" ,
            preSaleOrderCode:"" ,
            businessModel:"",
            outDepotId:"",
            buId:"",
            merchantId:"",
            merchantName:"",
            ids:"",
            modalId:'#signup-modal'
        }
    };

    $("input[name='keeperMerchantGroup-checkable']").on("change", function(){
        if($(this).is(':checked')){
            $(":checkbox", '#datatable-detail').prop("checked",$(this).prop("checked"));
            $('#datatable-detail tbody tr').addClass('success');
            $("#data-tbody").find("tr").each(function(){
                var val = $(this).find('td').eq(5).find('input').val();
                $(this).find('td').eq(6).find('input').val(val);
            });
        }else{
            $(":checkbox", '#datatable-detail').prop("checked",false);
            $('#datatable-detail tbody tr').removeClass('success');
            $("#data-tbody").find("tr").each(function(){
                $(this).find('td').eq(6).find('input').val("");
            });
        }
    });
    $('#data-tbody').on("change", ":checkbox", function() {
        $(this).parents('tr').toggleClass('success');
        if($(this).is(':checked')){
            var stayNumText = $(this).parent().parent().find('td').eq(5).find('input').val();
            $(this).parent().next().next().next().next().next().next().html('<input type=\"text\" name=\"num2\" value="'+stayNumText+'">');
        }else{
            $(this).parent().next().next().next().next().next().next().html('<input type=\"text\" name=\"num2\" value="">');
        }
    })

</script>