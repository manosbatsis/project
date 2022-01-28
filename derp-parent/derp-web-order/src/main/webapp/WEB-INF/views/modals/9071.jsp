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
                                <select  class="edit_input" disabled name="customerId2" id="customerId2"style="width:200px;" parsley-trigger="change">
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
                                <button type="button" class="btn btn-info waves-effect waves-light fr" onclick='$m9071.save();'>确定</button>
                                <button type="button" class="btn btn-light waves-effect waves-light ml15"  onclick='$m9071.hide();'>取消</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">
    var $m9071={
        init:function(data,infoData){
            $m9071.params.customerId = infoData.customerId ;
            $m9071.params.poNo = infoData.poNo ;
            $m9071.params.preSaleOrderCode = infoData.preSaleOrderCode ;
            $m9071.params.businessModel = infoData.businessModel ;
            $m9071.params.outDepotId = infoData.outDepotId ;
            $m9071.params.buId = infoData.buId ;
            $m9071.params.merchantId = infoData.merchantId ;
            $m9071.params.merchantName = infoData.merchantName ;

            //加载客户的下拉数据
            $module.customer.loadSelectData.call($('select[name="customerId2"]')[0],infoData.customerId);
            $("#poNo2").val(infoData.poNo);
            $("#code2").val(infoData.preSaleOrderCode);
            $m9071.show(data);
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
                    var grossWeight = m.grossWeight;
                    var netWeight = m.netWeight;
                    html += '<tr><td class=" td-checkbox"><input type="checkbox"  name="item-checkbox"  value="' + goodsId + '" class="iCheck" ' + '></td>'
                        + '<td><input type="hidden" name="goodsNo2" value="'+$m9071.strFormat(goodsNo)+'">' + $m9071.strFormat(goodsNo) + '</td>'
                        + '<td><input type="hidden" name="goodsName2" value="'+$m9071.strFormat(goodsName)+'">' + $m9071.strFormat(goodsName) + '</td>'
                        + '<td><input type="hidden" name="preNum2" value="'+$m9071.strFormat(preNum)+'">' + $m9071.strFormat(preNum) + '</td>'
                        + '<td><input type="hidden" name="price2" value="'+$m9071.strFormat(price)+'">' + $m9071.strFormat(price) + '</td>'
                        + '<td><input type="hidden" name="staySaleNum2" value="'+$m9071.strFormat(staySaleNum)+'">' + $m9071.strFormat(staySaleNum) + '</td>'
                        + '<td><input type="text" name="num2"></td>'
                        + '<td><input type="hidden" name="barcode2" value="'+$m9071.strFormat(barcode)+'"></td>'
                        + '<td><input type="hidden" name="commbarcode2" value="'+$m9071.strFormat(commbarcode)+'"></td>'
                        + '<td><input type="hidden" name="brandName2" value="'+$m9071.strFormat(brandName)+'"></td>'
                        + '<td><input type="hidden" name="goodsCode2" value="'+$m9071.strFormat(goodsCode)+'"></td>'
                        + '<td><input type="hidden" name="grossWeight" value="'+$m9071.strFormat(grossWeight)+'"></td>'
                        + '<td><input type="hidden" name="netWeight" value="'+$m9071.strFormat(netWeight)+'"></td>'
                        + '</tr>';
                }
                $("#datatable-detail").append(html);
                goods_change3();
            $($m9071.params.modalId).modal("show");
        },
        hide:function(){
            $($m9071.params.modalId).modal("hide");
        },
        save:function(){
           /* var checked = $(".td-checkbox").find("input[type=checkbox]:checked") ;
            if (checked.length == 0) {
                $custom.alert.error("请选择商品！");
                return;
            }*/
        	 var check = true;
        	 var saleNumIsNull = true;
             var isSameGoods = true;
             var compareNum=true;//比较数量
             //遍历表体，判断销售量是否有一个有值
             var existGoodsArr = [];
             var str = '';
             $("#data-tbody").find("tr").each(function(){
                 var staySaleNum = $(this).find('td').eq(5).find("input").val();// 待销售量
                 var saleNum = $(this).find('td').eq(6).find("input").val();// 销售量
                 var goodsId = $(this).find('td').eq(0).find("input").val(); //商品id

                 if (saleNum!=null && saleNum!='' && saleNum!=undefined && saleNum!=0) {
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
                 // 清空已有商品
                 $("#table-list").find("tr").each(function(){
                     $("#goods-tbody").empty();
                 });
                   var i = 1;
                   $("#data-tbody").find("tr").each(function(){
                           var goodsId = $(this).find('td').eq(0).find("input").val(); //商品id
                           var goodsNo = $(this).find('td').eq(1).find("input").val();
                           var goodsName = $(this).find('td').eq(2).find("input").val();
                           var preNum = $(this).find('td').eq(3).find("input").val();
                           var price = $(this).find('td').eq(4).find("input").val();
                           var staySaleNum = $(this).find('td').eq(5).find("input").val();
                           var num = $(this).find('td').eq(6).find("input").val();
                           var barcode = $(this).find('td').eq(7).find("input").val();
                           var commbarcode = $(this).find('td').eq(8).find("input").val();
                           var brandName = $(this).find('td').eq(9).find("input").val();
                           var goodsCode = $(this).find('td').eq(10).find("input").val();
                           var grossWeight = $(this).find('td').eq(11).find("input").val();
                           var netWeight = $(this).find('td').eq(12).find("input").val();
                           var seq = i;
                           i++;
                           var grossWeightSum = num*grossWeight;
                           var netWeightSum = num*netWeight;

                           var amount=Number((price*num).toFixed(4));

                           var $tr = "";
                           if(num!=null && num!='' && num!=undefined && num!=0){
                               $tr+="<tr><td><input type='checkbox' name='item-checkbox'><input type='hidden' name='goodsId' value='"+goodsId+"'/></td>"+
                                   "<td><input type='text' name='seq' style='width:70px;text-align:center;' value='"+(seq==null?'':seq)+"'/></td>"+
                                   "<td>"+(goodsCode==null?'':goodsCode)+"<input type='hidden' name='goodsCode' value='"+(goodsCode==null?'':goodsCode)+"'/></td>"+
                                   "<td>"+(goodsName==null?'':goodsName)+"<input type='hidden' name='goodsName' value='"+(goodsName==null?'':goodsName)+"'/></td>"+
                                   "<td>"+(goodsNo==null?'':goodsNo)+"<input type='hidden' name='goodsNo' value='"+(goodsNo==null?'':goodsNo)+"'/></td>"+
                                   "<td>"+(barcode==null?'':barcode)+"<input type='hidden' name='barcode' value='"+(barcode==null?'':barcode)+"'/></td>"+
                                   "<td><input type='text' disabled name='num' style='width:70px;text-align:center;' class='goods-num' value='"+(num==null?0:num)+"'></td>"+
                                   "<td><input type='text'  name='price' class='goods-price' style='width:100px;text-align:center;' value='"+(price==null?0:price)+"'></td>"+
                                   "<td><input type='text'  name='declarePrice' class='goods-price' style='width:100px;text-align:center;' value='"+(price==null?0:price)+"'></td>"+
                                   "<td><input type='text'  name='amount' class='goods-amount' style='width:100px;text-align:center;' value='"+(amount==null?0:amount)+"' onkeyup='numFormat(this,2)'></td>"+
                                   "<td>"+(brandName==null?'':brandName)+"<input type='hidden' name='brandName' value='"+(brandName==null?'':brandName)+"'/></td>"+
                                   "<td><input type='text'  onkeyup='numFormat(this,2)' onchange='calculateWeight2()' id='goods-rough' name='grossWeight' value='"+(grossWeightSum==null?'':parseFloat(grossWeightSum).toFixed(2))+"'/>" +
                                   "<input type='hidden' name='grossWeight-hidden' value='"+(grossWeight==null?'':grossWeight)+"'/></td>"+
                                   "<td><input type='text' onkeyup='numFormat(this,5)'  onchange='suttleWeight2()' name='netWeight' id='goods-suttle' value='"+(netWeightSum==null?'':parseFloat(netWeightSum).toFixed(5))+"'/>" +
                                   "<input type='hidden'  name='netWeight-hidden' value='"+(netWeight==null?'':netWeight)+"'></td>"+
                                   "<td><input type='text' name='boxNo' value=''/></td>"+
                                   "<td><input type='text' name='itemContractNo' value=''/></td>"+
                                   "<td><input type='text' name='itemRemark' value=''/></td>"+
                                   "</tr>";
                               $("#table-list").append($tr);

                           }
                       // 汇总数量
                       goods_change3();
                   });
                 $('.modal-backdrop').remove();//去掉遮罩层
                 $("#signup-modal").modal('hide');
                 //$($m9071.params.modalId).modal("hide");
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
    //计算毛重之和
    function calculateWeight2() {
        var totalGrossWeight = 0;
        $("#table-list tbody tr").each(function (index, item) {
            var grossWeight = $(this).find("td").eq(11).children('input[name="grossWeight"]').val();//毛重
            if (!grossWeight) {
                grossWeight = 0;
            }
            totalGrossWeight += parseFloat(grossWeight);
        });
        $("#billWeight").val(totalGrossWeight.toFixed(2));
        $("#totalRough").text(totalGrossWeight.toFixed(2))
    }

    //计算净重之和
    function suttleWeight2() {
        var totalGrossWeight = 0;
        $("#table-list tbody tr").each(function (index, item) {
            var grossWeight = $(this).find("td").eq(12).children('input[name="netWeight"]').val();//毛重
            if (!grossWeight) {
                grossWeight = 0;
            }
            totalGrossWeight += parseFloat(grossWeight);
        });
        $("#totalSuttle").text(totalGrossWeight.toFixed(5))
    }

    $("#table-list").on("blur",'#goods-rough',function(){
        goods_change3();
    })

    $("#table-list").on("blur",'#goods-suttle',function(){
        goods_change3();
    })
</script>