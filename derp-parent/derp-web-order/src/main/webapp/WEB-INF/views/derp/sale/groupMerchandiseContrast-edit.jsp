<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common.jsp" />
<style>
    .edit_span {
        text-align: right;
        line-height: 30px;
    }

    .box_input {
        width: 38px;
        height: 30px;
        border: 1px solid #dadada;
        text-indent: 5px;
        margin: 0 10px 0 10px;
    }


</style>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt60">
        <!-- Start row -->
            <div class="row">
                <div class="card-box margin w100">
                    <div class="col-12 row">
                        <div>
                            <ol class="breadcrumb">
                                <li><i class="block"></i> 当前位置：</li>
                                <li class="breadcrumb-item"><a href="#">爬虫配置</a></li>
                                <li class="breadcrumb-item "><a
                                        href="javascript:$load.a('/groupMerchandiseContrast/toPage.html');">商品对照表</a>
                                </li>
                                <li class="breadcrumb-item "><a
                                        href="javascript:$load.a('/groupMerchandiseContrast/toAddPage.html');">商品对照编辑</a>
                                </li>
                            </ol>
                        </div>
                    </div>



                    <form id="add-form" >
                        <div class="col-12 ml10">
                            <div class="title_text">基础资料</div>
                            <div class="info_box" >
                                <div class="edit_box ">
                                    <div class="edit_item col-5">
                                        <label class="edit_label"><i class="red">*</i> 店铺：&nbsp;&nbsp;</label>
                                        <input type="hidden" value="${detail.shopName}" name="shopName" id="shopName"/>
                                        <input type="hidden" value="${detail.id}" name="id" />
                                        <select class="selectpicker show-tick form-control" data-live-search="true" name="shopId" id="shopId" disabled>
                                        </select>
                                    </div>
                                    <div class="edit_item col-5">
                                        <label class="edit_label"><i class="red">*</i> 公司：&nbsp;&nbsp;</label>
                                        <input type="hidden" value="${detail.merchantId}" name="merchantId" id="merchantId"/>
                                        <input type="text" class="edit_input" name = "merchantName" id ="merchantName"
                                               value="${detail.merchantName}" disabled>
                                    </div>
                                    <div class="edit_item">

                                    </div>
                                </div>

                                <div class="edit_box ">
                                    <div class="edit_item col-5">
                                        <label class="edit_label"><i class="red">*</i> 商品ID：</label>
                                        <input type="text"  class="edit_input" name="skuId"
                                               id="skuId" value="${detail.skuId}">
                                    </div>
                                    <div class="edit_item col-5">
                                        <label class="edit_label"><i class="red">*</i>商品名称：</label>
                                        <input type="text" required="" class="edit_input" id="groupGoodsName" name="groupGoodsName"
                                               value="${detail.groupGoodsName}">
                                    </div>
                                    <div class="edit_item">

                                    </div>
                                </div>

                                <div class="edit_box ">
                                    <div class="edit_item col-5">
                                        <label class="edit_label"> 备注：</label>
                                        <input type="text"  class="edit_input" name="remark"
                                               id="remark" value="${detail.remark}">
                                    </div>
                                    <div class="edit_item col-5">
                                        <label class="edit_label"><i class="red">*</i>状态：</label>
                                        <select class="edit_input" name="status" id="status">
                                        </select>
                                        <input type="hidden" id="statusCode" value="${detail.status}">
                                    </div>
                                    <div class="edit_item">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>





                    <div class="col-12 ml10">
                        <div>
                            <ul class="nav nav-tabs">
                                <li class="nav-item">
                                    <a href="javascript:void(0);" onclick="changeTable('0');" data-toggle="tab"
                                       class="nav-link active">组合品信息</a>
                                </li>
                                <li class="nav-item">
                                    <a href="javascript:void(0);" onclick="changeTable('1');" data-toggle="tab"
                                       class="nav-link">变更记录</a>
                                </li>
                            </ul>
                            <div>
                                <div class="tab-pane fade show active" id="itemList">
                                    <div id="goodsInfoTab">
                                        <div class="col-1 mt10" style="padding-bottom: 10px; padding-left: 0px;">
                                            <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick="chooseGoods();">选择商品</button>
                                        </div>
                                        <table id="table-list" class="table table-striped" cellspacing="0" width="100%">
                                            <thead>
                                            <tr>
                                                <th>商品货号</th>
                                                <th>商品名称</th>
                                                <th>条形码</th>
                                                <th>商品品牌</th>
                                                <th>数量</th>
                                                <th>销售标准单价</th>
                                                <th>操作</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:forEach items="${detail.detailList}" var="item">
                                                <tr>
                                                    <input type="hidden" name="goodsId" value="${item.goodsId}"/>
                                                    <td>${item.goodsNo}<input type="hidden" name="goodsNo" value="${item.goodsNo}"/></td>
                                                    <td>${item.goodsName}<input type="hidden" name="goodsName" value="${item.goodsName}"/></td>
                                                    <td>${item.barcode}<input type="hidden" name="barcode" value="${item.barcode}"/></td>
                                                    <td>${item.brand == null ? null : item.brand}<input type="hidden" name="brand" value="${item.brand}"/></td>
                                                    <td><input type='text' name='num' value='${item.num }'></td>
                                                    <td><input type='text' name='price' value="${item.price}"></td>
                                                    <td><p class='nowrap'><a href='javascript:void(0)' onclick='remove(this,"${item.goodsId}")'>删除</a></p></td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div id="historyInfoTab" style="display: none;">
                                        <table class="table table-striped" cellspacing="0" width="100%">
                                            <thead>
                                            <tr>
                                                <th style="white-space:nowrap;">商品货号</th>
                                                <th style="white-space:nowrap;">商品名称</th>
                                                <th style="white-space:nowrap;">条形码</th>
                                                <th style="white-space:nowrap;">数量</th>
                                                <th style="white-space:nowrap;">销售标准单价</th>
                                                <th style="white-space:nowrap;">修改人</th>
                                                <th style="white-space:nowrap;">修改时间</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:forEach items="${detail.historyList}" var="item">
                                                <tr>
                                                    <td>${item.goodsNo}</td>
                                                    <td>${item.goodsName}</td>
                                                    <td>${item.barcode}</td>
                                                    <td>${item.num}</td>
                                                    <td>${item.price}</td>
                                                    <td>${item.editor}</td>
                                                    <td><fmt:formatDate value="${item.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                                <div class="tab-pane fade" id="batchDefault">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-row m-t-50">
                        <div class="col-12">
                            <div class="row">
                                <div class="col-6">
                                    <input type="button" class="btn btn-info waves-effect waves-light fr btn_default" id="save-buttons" value="保 存"/>
                                </div>
                                <div class="col-6">
                                    <button type="button" class="btn btn-light waves-effect waves-light btn_default" id="cancel-buttons">取 消</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- end row -->
        <jsp:include page="/WEB-INF/views/modals/1503.jsp"/>
    </div>
    <!-- container -->
</div>
<div id="ShowImage_Form" class="modal hide">
    <div class="modal-header">
        <button data-dismiss="modal" class="close" type="button"></button>
    </div>
    <div class="modal-body">
        <div id="img_show">
        </div>
    </div>
</div>
</div> <!-- content -->
<script src='/resources/js/system/base.js' type="text/javascript"></script>
<script type="text/javascript"  src='/resources/js/system/base.js' ></script>
<script type="text/javascript">
    $module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"groupMerchandiseContrast_statusList",$("#statusCode").val());
    $(document).ready(function() {
        
    	getShopCodeSelect('${detail.shopId }') ;
    	
        //根据店铺选择带出公司
        $("#shopId").change(function(){
            var val = $(this).val() ;
            var shopName = $("#shopId").find("option:selected").text();
            $("#shopName").val(shopName);
            $custom.request.ajax("/merchantShop/getShopDetails.asyn", {"id":val}, function(result){
                var res = result.data;
                $("#merchantId").val(res.merchantId);
                $("#merchantName").val(res.merchantName);
            });

        }) ;


        //重新加载select2
        $(".goods_select2").select2({
            language:"zh-CN",
            placeholder: '请选择',
            allowClear:true
        });
    });
    //渲染 下拉框
    function selLoad(data, id) {
        $("#" + id).empty();
        var ops = "<option value=''>请选择</option>";
        $.each(data, function (index, event) {
            if (event.value != null) {
                ops += "<option value='" + event.value + "'>" + event.label + "</option>";
            }
        });
        $("#" + id).append(ops);
    }

    //删除选中行
    function remove(obj, goodsId) {
        $(obj).parent("p").parent("td").parent("tr").remove();
        var unNeedIds = $("#unNeedIds").val();
        var temp = unNeedIds.split(",");
        var newUnNeedIds = delArrElement(goodsId,temp);
        $("#unNeedIds").val(newUnNeedIds);
    }
    function delArrElement(goodsId,temp){
        for (var j = 0; j < temp.length; j++) {
            if(goodsId == temp[j]){
                temp.splice(j,1);
                delArrElement(goodsId,temp);
            }
        }
        return temp;
    }

    //保存按钮
    $("#save-buttons").click(function(){
        //必填项校验
        if($("#shopId").val()==""){
            $custom.alert.error("店铺不能为空！");
            return ;
        }
        if($("#merchantId").val()==""){
            $custom.alert.error("公司不能为空！");
            return ;
        }
        if($("#skuId").val()==""){
            $custom.alert.error("商品ID不能为空！");
            return ;
        }
        if($("#groupGoodsName").val()==""){
            $custom.alert.error("商品名称不能为空！");
            return ;
        }
        if($("#stauts").val()==""){
            $custom.alert.error("状态不能为空！");
            return ;
        }
        var itemList = [];
        var flag = true;


        $('#table-list tbody tr').each(function(i){// 遍历 tr
            var item ={};
            var goodsId = $(this).find('input[name="goodsId"]').val();
            item['goodsId']=goodsId;
            var goodsName = $(this).find('input[name="goodsName"]').val();
            item['goodsName']=goodsName;
            var goodsNo = $(this).find('input[name="goodsNo"]').val();
            item['goodsNo']=goodsNo;
            var barcode = $(this).find('input[name="barcode"]').val();
            item['barcode']=barcode;
            var brandName = $(this).find('input[name="brandName"]').val();
            item['brandName']=brandName;
            var num = $(this).find('input[name="num"]').val();
            item['num']=num;
            var price = $(this).find('input[name="price"]').val();
            item['price']=price;

            if(!num || num <= 0){
                $custom.alert.error("商品数量不能为空或0");
                flag = false;
                return;
            }
            var reg = /^\d+$/;
            if (!reg.test(num)) {
                $custom.alert.error("数量只能输入正整数");
                flag = false;
                return;
            }
            if(!price || price < 0){
                $custom.alert.error("商品单价不能为空或小于0");
                flag = false;
                return;
            }
            var reg1 = /^\d+(\.\d+)?$/;
            if (!reg1.test(price)) {
                $custom.alert.error("单价只能输入数字");
                flag = false;
                return;
            }
            if(price.length>11){
                $custom.alert.error("单价长度只能输入11位（包含小数点）");
                flag = false;
                return;
            }
            itemList.push(item);
            console.log(JSON.stringify(itemList))
        });
        if(!flag){
            return;
        }
        if(!itemList || itemList.length==0){
            $custom.alert.error("至少选择一个商品");
            return;
        }
        $('#shopId').attr("disabled",false);
        var skuId = $("#skuId").val();
        var merchantId = $("#merchantId").val();
        var merchantShopId = $("#shopId").val();
        var id = ${detail.id};
        //校验同一店铺编码、同一公司、同一组合品ID仅能维护存在一条数据
        $custom.request.ajaxSync(serverAddr + "/groupMerchandiseContrast/getGroupMerchantContrast.asyn", {"skuId":skuId,"shopId":merchantShopId, "merchantId":merchantId, "id":id}, function(result){
            if(result.state != 200){
                $custom.alert.error("存在重复的公司店铺组合品ID信息!");
                return ;
            }else{
                var form = $("#add-form").serializeArray();
                var goodsItem = {name:"goodsItem", value:JSON.stringify(itemList)}
                form.push(goodsItem);
                var url = serverAddr + "/groupMerchandiseContrast/modifyGroupMerchandiseContrast.asyn";
                $custom.request.ajax(url, form, function(result){
                    if(result.state == '200'){
                        $custom.alert.success("编辑成功！");
                        setTimeout(function () {
                            $module.load.pageOrder("act=11003");
                        }, 1000);
                    }else {
                        if (result.expMessage != null) {
                            $custom.alert.error(result.expMessage);
                        } else {
                            $custom.alert.error("编辑失败！");
                        }
                    }
                });
            }
        });

    });

    $("#cancel-buttons").click(function () {
        $module.load.pageOrder("act=11003");
    });
    
    /**
     *   下拉搜索
     */
     $("#shopId").prev().find(".bs-searchbox").find("input").keyup(
         function(){
             var shopName = $("#form1 .open input").val() ;
             getShopCodeSelect(null, shopName) ;
         }
     );
     
     /**
     * 获取标准品牌下拉
     */
     function getShopCodeSelect(val, shopName){
         var url = serverAddr + "/groupMerchandiseContrast/getShop.asyn" ;
         $custom.request.ajax(url,{"shopName":shopName},function(data) {
             if (data.state == 200) {
                 
                 var html = "" ;
                 
                 var shopList = data.data ;
                 
                 if(!shopName){
                     html += '<option value="">请选择</option>' ;
                 }
                 
                 $(shopList).each(function(index , shop){
                     html += '<option value="'+shop.shopId+'">'+shop.shopName+'</option>' ;
                 
                 }) ;
                 
                 $("#shopId").html(html) ;
                 $('#shopId').selectpicker({width: '68.7%'});
                 if(val){
                	 $('#shopId').val(val);
                 }
                 $('#shopId').selectpicker('refresh');
                 
                 $("#shopId").prev().prev().css({"height": "30px","margin-bottom": "5px","border": "1px solid #dadada","background": "white","font-size": "12px"}) ;
                 $("#shopId").prev().css({"z-index":"99"}) ;
                 $("#shopId").prev().find(".dropdown-menu.inner").css({"height":"220px"}) ;
                 
             } else {
                 $custom.alert.error(data.message);
             }
         });
     }

    function changeTable(type) {
        if (type == '0') {
            $("#goodsInfoTab").show();
            $("#historyInfoTab").hide();
        }else  if (type == '1') {
            $("#historyInfoTab").show();
            $("#goodsInfoTab").hide();
        }
    }

    function chooseGoods() {
        var merchantId = $("#merchantId").val();
        if(!merchantId){
            $custom.alert.error("请先选择公司！");
            return false;
        }
        $erpPopup.orderGoods.init(7, null, null);
    }
</script>
