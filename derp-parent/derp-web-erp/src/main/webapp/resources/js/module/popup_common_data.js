/**
 *  JS
 * @author zhanghx
 */

/**
 * 商品分页弹窗
 */
var unNeedIds = [];// 储存选择过的商品id
var initType="";
var $erpPopup = {
   /* goods:{
        //type  1采购 2销售 3调拨 4基础数据 5组合品对照表 6销售退 7.协议单价
        init:function(type, obj, thisDepotId) {
            //重置表单
            //$($erpPopup.goods.params.formId)[0].reset();
            if(type==1){
                // 先选择仓库
                var depotId = $("#depotId").val();
                if (depotId == null || depotId == "") {
                    $custom.alert.error("请先选择仓库！");
                } else {
                    // 初始化重选按钮
                    $(".group-checkable").prop("checked", false);
                    var unIds = $("#unNeedIds").val();
                    if (unIds == null || unIds == "") {
                        unNeedIds = [];
                    } else {
                        unNeedIds = unIds.split(",");
                    }
                    var obj_ids = document.getElementsByName("goodsId");
                    for(var i = 0; i < obj_ids.length; i++){
                        unNeedIds.push(obj_ids[i].value);
                    }
                    $("#popupPurchaseDepotId").val(depotId);
                    var unNeedIds_temp = "";
                    if (unNeedIds != []) {
                        for (var i = 0; i < unNeedIds.length; i++) {
                            if (unNeedIds_temp == "") {
                                unNeedIds_temp = unNeedIds[i];
                            } else {
                                unNeedIds_temp = unNeedIds_temp + "," + unNeedIds[i];
                            }
                        }
                    }
                    $("#unNeedIds").val(unNeedIds_temp);
                }
            }else if(type==2){
                var outDepotId = $("#outDepotId").val();
                var merchantId = $('#merchantId').val();
                //初始化重选按钮
                $(".group-checkable").prop("checked",false);
                var unIds = $("#unNeedIds").val();
                if(unIds==null || unIds==""){
                    unNeedIds = [];
                }else{
                    unNeedIds = unIds.split(",");
                }
                $("#id2").val(merchantId);
                $("#id").val(outDepotId);
                var unNeedIds2 = [];
                var unNeedIds_temp = "";
                if(unNeedIds!=[]){
                    for (var i = 0; i < unNeedIds.length; i++) {
                        if(unNeedIds2.indexOf(unNeedIds[i])==-1){
                            unNeedIds2.push(unNeedIds[i]);
                        }
                    }
                    for (var i = 0; i < unNeedIds2.length; i++) {
                        if(unNeedIds_temp == ""){
                            unNeedIds_temp = unNeedIds2[i];
                        }else{
                            unNeedIds_temp = unNeedIds_temp + "," + unNeedIds2[i];
                        }
                    }
                }
                $("#unNeedIds").val(unNeedIds_temp);
            }else if(type==3){
                thisObj=obj;
                outOrInDepotId=thisDepotId;
                //先选择仓库
                var depotId = $("#"+thisDepotId).val();
                if(depotId==null || depotId==""){
                    $custom.alert.error("请先选择仓库！");
                    return false;
                }else{
                    //获取已选中的商品id
                    var unNeedIds ="";//已经选中的商品id
                    $("#datatable-buttons tbody tr").each(function (index, item) {
                        var outGoodsId = $(this).find("td").eq(1).find("input").val();//调出商品id
                        var inGoodsId = $(this).find("td").eq(5).find("input").val();//调入商品id
                        if(outGoodsId!=''&&outGoodsId!=undefined){
                            unNeedIds = unNeedIds+","+outGoodsId;
                        }
                        if(inGoodsId!=''&&inGoodsId!=undefined){
                            unNeedIds = unNeedIds+","+inGoodsId;
                        }

                    });
                    unNeedIds = unNeedIds.substr(1);
                    $("#unNeedIds").val(unNeedIds);
                    //初始化重选按钮
                    $(".group-checkable").prop("checked",false);
                    $("#id").val(depotId);
                }
            }else if(type == 4) {
                $(".group-checkable").prop("checked", false);
                var unIds = $("#unNeedIds").val();
                if (unIds == null || unIds == "") {
                    unNeedIds = [];
                } else {
                    unNeedIds = unIds.split(",");
                }
                var obj_ids = document.getElementsByName("goodsId");
                for(var i = 0; i < obj_ids.length; i++){
                    unNeedIds.push(obj_ids[i].value);
                }
                var unNeedIds_temp = "";
                if (unNeedIds != []) {
                    for (var i = 0; i < unNeedIds.length; i++) {
                        if (unNeedIds_temp == "") {
                            unNeedIds_temp = unNeedIds[i];
                        } else {
                            unNeedIds_temp = unNeedIds_temp + "," + unNeedIds[i];
                        }
                    }
                }
                $("#unNeedIds").val(unNeedIds_temp);
            } else if(type == 5) {
                thisObj=obj;
                var merchantId = $("#merchantId").val();
                if(merchantId==null || merchantId==""){
                    $custom.alert.error("请先选择商家！");
                    return false;
                }else{
                    // 初始化重选按钮
                    $("#existMerchantId").val(merchantId);
                    $(".group-checkable").prop("checked", false);
                    var unIds = $("#unNeedIds").val();
                    if (unIds == null || unIds == "") {
                        unNeedIds = [];
                    } else {
                        unNeedIds = unIds.split(",");
                    }
                    var obj_ids = document.getElementsByName("goodsId");
                    for(var i = 0; i < obj_ids.length; i++){
                        unNeedIds.push(obj_ids[i].value);
                    }
                    var unNeedIds_temp = "";
                    if (unNeedIds != []) {
                        for (var i = 0; i < unNeedIds.length; i++) {
                            if (unNeedIds_temp == "") {
                                unNeedIds_temp = unNeedIds[i];
                            } else {
                                unNeedIds_temp = unNeedIds_temp + "," + unNeedIds[i];
                            }
                        }
                    }
                    $("#unNeedIds").val(unNeedIds_temp);
                }
            }else if(type==6){
                thisObj=obj;
                outOrInDepotId=thisDepotId;
                //先选择仓库
                var depotId = $("#"+thisDepotId).val();
                //var outDepotId = $("#outDepotId").val();
                var merchantId = $('#merchantId').val();
                //初始化重选按钮
                $(".group-checkable").prop("checked",false);
                var unIds = $("#unNeedIds").val();
                if(unIds==null || unIds==""){
                    unNeedIds = [];
                }else{
                    unNeedIds = unIds.split(",");
                }
                $("#id2").val(merchantId);
                $("#id").val(depotId);
                var unNeedIds2 = [];
                var unNeedIds_temp = "";
                if(unNeedIds!=[]){
                    for (var i = 0; i < unNeedIds.length; i++) {
                        if(unNeedIds2.indexOf(unNeedIds[i])==-1){
                            unNeedIds2.push(unNeedIds[i]);
                        }
                    }
                    for (var i = 0; i < unNeedIds2.length; i++) {
                        if(unNeedIds_temp == ""){
                            unNeedIds_temp = unNeedIds2[i];
                        }else{
                            unNeedIds_temp = unNeedIds_temp + "," + unNeedIds2[i];
                        }
                    }
                }
                $("#unNeedIds").val(unNeedIds_temp);
            } else if(type == 7) {
                thisObj=obj;
                var merchantId = $("#merchantId").val();
                // 初始化重选按钮
                $("#existMerchantId").val(merchantId);
                $(".group-checkable").prop("checked", false);
                var unIds = $("#unNeedIds").val();
                if (unIds == null || unIds == "") {
                    unNeedIds = [];
                } else {
                    unNeedIds = unIds.split(",");
                }
                var obj_ids = document.getElementsByName("goodsId");
                for(var i = 0; i < obj_ids.length; i++){
                    unNeedIds.push(obj_ids[i].value);
                }
                var unNeedIds_temp = "";
                if (unNeedIds != []) {
                    for (var i = 0; i < unNeedIds.length; i++) {
                        if (unNeedIds_temp == "") {
                            unNeedIds_temp = unNeedIds[i];
                        } else {
                            unNeedIds_temp = unNeedIds_temp + "," + unNeedIds[i];
                        }
                    }
                }
                $("#unNeedIds").val(unNeedIds_temp);
            }else if(type == 8) {
                thisObj=obj;
                $(".group-checkable").prop("checked", false);
                var unIds = $("#unNeedIds").val();
                if (unIds == null || unIds == "") {
                    unNeedIds = [];
                } else {
                    unNeedIds = unIds.split(",");
                }
                var obj_ids = document.getElementsByName("goodsId");
                for(var i = 0; i < obj_ids.length; i++){
                    unNeedIds.push(obj_ids[i].value);
                }
                var unNeedIds_temp = "";
                if (unNeedIds != []) {
                    for (var i = 0; i < unNeedIds.length; i++) {
                        if (unNeedIds_temp == "") {
                            unNeedIds_temp = unNeedIds[i];
                        } else {
                            unNeedIds_temp = unNeedIds_temp + "," + unNeedIds[i];
                        }
                    }
                }
                $("#unNeedIds").val(unNeedIds_temp);
            }
            $erpPopup.goods.show();
        },
        list:function() {
            // 重新加载select2
            $(".goods_select2").select2({
                language:"zh-CN",
                placeholder:'请选择',
                allowClear:true
            });
            $erpPopup.goods.loadTable(1, 10);
        },
        // 加载表格
        loadTable:function(pageNo, pageSize) {
            $($erpPopup.goods.params.tableId + ' tr:gt(0)').remove();
            var form = $($erpPopup.goods.params.formId).serializeArray();
            // 当前页，每页显示条数
            var data_pageNo = {
                name:"begin",
                value:(pageNo-1)*pageSize
            };
            var data_pageSize = {
                name:"pageSize",
                value:pageSize
            };
            form.push(data_pageNo);
            form.push(data_pageSize);
            var resultJson = $erpPopup.merchandise.loadMerchandisesByPage(form);
            var $tr = "";
            //渲染表格
            $.each(resultJson.list,function(i,e){
                $tr += "<tr>"+
                    "<td><input type='checkbox' class='iCheck' name='checkId' value="+(e.id==null?'':e.id)+"></td>"+
                    "<td>"+(e.merchantName==null?'':e.merchantName)+"</td>"+
                    "<td>"+(e.name==null?'':e.name)+"</td>"+
                    "<td>"+(e.goodsNo==null?'':e.goodsNo)+"</td>"+
                    "<td>"+(e.brandName==null?'':e.brandName)+"</td>"+
                    "<td>"+(e.barcode==null?'':e.barcode)+"</td>"+
                    "<td>"+(e.factoryNo==null?'':e.factoryNo)+"</td>"+
                    "<td>"+(e.unitName==null?'':e.unitName)+"</td>"+
                    "<td>"+(e.outDepotFlag=='0' ? '是':'否')+"</td>"+
                    "</tr>";
            });
            $($erpPopup.goods.params.tableId).append($tr);
            var begin = resultJson.list.length > 0 ? resultJson.begin:0;
            var end = resultJson.list.length > 0 ? resultJson.end:0;
            var total = resultJson.list.length > 0 ? resultJson.total:0;
            $('.page_txt').html("当前显示第 " + (begin + 1) + " 至 " + end + " 项，共 " + total + " 项");
            $erpPopup.goods.loadPageUtils(resultJson);
        },
        // 加载分页插件
        loadPageUtils:function(resultJson) {
            $('.pageUtils').pagination({
                totalData:resultJson.list.length > 0 ? resultJson.total:1, // 数据总条数
                showData:resultJson.list.length > 0 ? resultJson.pageSize:1, // 每页显示的条数
                current:resultJson.list.length > 0 ? resultJson.pageNo:1, // 当前第几页
                coping:true, // 开启首页和尾页
                homePage:'首页', // 首页节点内容
                endPage:'末页', // 末页节点内容
                prevContent:'上页', // 上一页内容
                nextContent:'下页', // 下一页内容
                jump:false, // 关闭跳转到指定页数
                callback:function(api) { // 回调方法
                    $erpPopup.goods.loadTable(api.getCurrent(), resultJson.pageSize); // 重新加载页面表格数据
                }
            });
        },
        reloadTable:function(){
            $erpPopup.goods.loadTable(1,10);
        },
        show:function() {
            $erpPopup.goods.list();
            $($erpPopup.goods.params.modalId).modal("show");
        },
        hide:function() {
            $($erpPopup.goods.params.modalId).modal("hide");
            //将自定义选商品的链接还原，否则会影响其他页面的商品选择
            $erpPopup.goods.params.getMerchandisesByPageURL = "/merchandise/getPopupList.asyn";
        },
        params:{
            formId:'#popup-goods-form',
            modalId:'#popup-signup-modal',
            tableId:'#popup-datatable-buttons',
            getMerchandisesByPageURL:'/merchandise/getPopupList.asyn',//获取所有的商品信息(用于弹窗分页显示)
            getMerchandisesByIdsURL:'/merchandise/getListByIds.asyn',//根据商品ids获取所有的商品信息
            getMerchandisesByIdsAndTypeAndInOutDepotURL:'/merchandise/getListByIdsAndTypeAndInOutDepot.asyn',//根据商品ids获取所有的商品信息
        }
    },*/
	merchandise:{
        /**
         * 显示商品弹窗
         * @param form
         */
        /*loadMerchandisesByPage:function(form){
            var jsonData=$erpPopup.merchandise.ajax($erpPopup.goods.params.getMerchandisesByPageURL,form);
            return  jsonData;
        },*/
        /**
         * 根据ids获取商品信息
         * @param form
         */
        loadMerchandisesByIds:function(ids){
        	var ids = {"ids":ids};
            var jsonData=$erpPopup.merchandise.ajax($erpPopup.orderGoods.params.getMerchandisesByIdsURL,ids);
            return  jsonData;
        },
        ajax:function(url,json){
            var jsonData=null;
            $custom.request.ajaxSync(url,json,function(data){
                jsonData=data.data;
            });
            return jsonData;
        },
        /**
		 * 根据ids、单据类型、调入仓库id、调出仓库id获取商品信息
         */
        getListByIdsAndTypeAndInOutDepot:function(ids, type, depotId){
            var json = {"ids":ids, "type":type, "depotId":depotId};
            console.log(JSON.stringify(json))
            var jsonData=$erpPopup.merchandise.ajax($erpPopup.orderGoods.params.getMerchandisesByIdsAndTypeAndInOutDepotURL,json);
            return  jsonData;
        },
        /**
         * 显示单据商品弹窗
         * @param form
         */
        loadOrderMerchandisesByPage:function(form){
            var jsonData=$erpPopup.merchandise.ajax($erpPopup.orderGoods.params.getMerchandisesByPageURL,form);
            return  jsonData;
        },
    },
	//单据选择商品弹窗
	orderGoods:{
        params:{
            formId:'#popup-goods-form',
            modalId:'#popup-signup-modal',
            tableId:'#popup-datatable-buttons',
            getMerchandisesByPageURL:'/merchandise/getOrderPopupList.asyn',//获取所有的商品信息(用于弹窗分页显示)
            getMerchandisesByIdsURL:'/merchandise/getListByIds.asyn',//根据商品ids获取所有的商品信息
            getMerchandisesByIdsAndTypeAndInOutDepotURL:'/merchandise/getListByIdsAndTypeAndInOutDepot.asyn',//根据商品ids获取所有的商品信息
        },
        //type: 1:采购  2:销售 3: 销售退 4: 调拨 5: 盘点 6：爬虫商品对照表 7：无选品限制 8：不过滤已选择商品
        init:function(type, obj, thisDepotId) {
        	initType=type;
            if(type == 1){
                // 先选择仓库
                var depotId = $("#depotId").val();
                if (depotId == null || depotId == "") {
                    $custom.alert.error("请先选择仓库！");
                } else {
                    // 初始化重选按钮
                    $(".group-checkable").prop("checked", false);
                    var unIds = $("#unNeedIds").val();
                    if (unIds == null || unIds == "") {
                        unNeedIds = [];
                    } else {
                        unNeedIds = unIds.split(",");
                    }
                    var obj_ids = document.getElementsByName("goodsId");
                    for(var i = 0; i < obj_ids.length; i++){
                        unNeedIds.push(obj_ids[i].value);
                    }
                    $("#popupPurchaseDepotId").val(depotId);
                    var unNeedIds_temp = "";
                    if (unNeedIds != []) {
                        for (var i = 0; i < unNeedIds.length; i++) {
                            if (unNeedIds_temp == "") {
                                unNeedIds_temp = unNeedIds[i];
                            } else {
                                unNeedIds_temp = unNeedIds_temp + "," + unNeedIds[i];
                            }
                        }
                    }
                    $("#unNeedIds").val(unNeedIds_temp);
                }
            }else if(type == 2){
                var outDepotId = $("#outDepotId").val();
                var merchantId = $('#merchantId').val();
                if (outDepotId == null || outDepotId == "") {
                    $custom.alert.error("请先选择出仓仓库！");
                    return;
                }
                //初始化重选按钮
                $(".group-checkable").prop("checked",false);
                var unIds = $("#unNeedIds").val();
                if(unIds==null || unIds==""){
                    unNeedIds = [];
                }else{
                    unNeedIds = unIds.split(",");
                }
                var obj_ids = document.getElementsByName("goodsId");
                for(var i = 0; i < obj_ids.length; i++){
                    unNeedIds.push(obj_ids[i].value);
                }
                $("#id2").val(merchantId);
                $("#id").val(outDepotId);
                var unNeedIds2 = [];
                var unNeedIds_temp = "";
                if(unNeedIds){
                    for (var i = 0; i < unNeedIds.length; i++) {
                        if(unNeedIds2.indexOf(unNeedIds[i])==-1){
                            unNeedIds2.push(unNeedIds[i]);
                        }
                    }
                    for (var i = 0; i < unNeedIds2.length; i++) {
                        if(unNeedIds_temp == ""){
                            unNeedIds_temp = unNeedIds2[i];
                        }else{
                            unNeedIds_temp = unNeedIds_temp + "," + unNeedIds2[i];
                        }
                    }
                }
                $("#unNeedIds").val(unNeedIds_temp);
            } else if(type == 3){
                thisObj=obj;
                outOrInDepotId=thisDepotId;
                $("#goodsId").val("");
                //先选择仓库
                var depotId = $("#"+thisDepotId).val();
                var merchantId = $('#merchantId').val();
                var unNeedIds ="";//已经选中的商品id

                $("#datatable-buttons tbody tr").each(function (index, item) {
                    var inGoodsId = $(this).find("td").eq(3).find("input").val();//退入商品id
                    var outGoodsId = $(this).find("td").eq(4).find("input").val();//退出商品id
                    if (thisDepotId == 'inDepotId') {
                        if(inGoodsId!=''&&inGoodsId!=undefined){
                            unNeedIds = unNeedIds+","+inGoodsId;
                        }
                    }
                });
                if (thisDepotId == 'outDepotId') {
                    var inGoodsId = $(obj).parents("tr").children("td").eq(3).find("input").val();//退入商品id
                    var outGoodsId = $(obj).parents("tr").children("td").eq(4).find("input").val();//退出商品id
                    $("#goodsId").val(inGoodsId);
                    unNeedIds = unNeedIds+","+outGoodsId
                }
                unNeedIds = unNeedIds.substr(1);
                $("#unNeedIds").val(unNeedIds);
                //初始化重选按钮
                $(".group-checkable").prop("checked",false);
                $("#id2").val(merchantId);
                $("#id").val(depotId);
            } else if(type == 4){
                thisObj=obj;
                outOrInDepotId=thisDepotId;
                //先选择仓库
                var depotId = $("#"+thisDepotId).val();
                if(depotId==null || depotId==""){
                    $custom.alert.error("请先选择仓库！");
                    return false;
                }else{
                    //获取已选中的商品id
                    var unNeedIds ="";//已经选中的商品id
                    $("#goodsId").val("");
                    $("#datatable-buttons tbody tr").each(function (index, item) {
                        var outGoodsId = $(this).find("td").eq(2).find("input").val();//调出商品id
                        if (thisDepotId == 'outDepotId') {
                            if(outGoodsId!=''&&outGoodsId!=undefined){
                                unNeedIds = unNeedIds+","+outGoodsId;
                            }
                        }
                    });
                    if (thisDepotId == 'inDepotId') {
                        var outGoodsId = $(obj).parents("tr").children("td").eq(2).find("input").val();//调出商品id
                        var inGoodsId = $(obj).parents("tr").children("td").eq(7).find("input").val();//调出商品id
                        $("#goodsId").val(outGoodsId);
                        if (inGoodsId) {
                            unNeedIds = unNeedIds+","+inGoodsId;
                        }
                    }
                    unNeedIds = unNeedIds.substr(1);
                    $("#unNeedIds").val(unNeedIds);
                    //初始化重选按钮
                    $(".group-checkable").prop("checked",false);
                    $("#id").val(depotId);

                }
            } else if(type == 5) {
                thisObj=obj;
                var depotId = $("#depotId").val();
                if(depotId==null || depotId==""){
                    $custom.alert.error("请先选择盘点仓库！");
                    return false;
                } else{
                    // 初始化重选按钮
                    $(".group-checkable").prop("checked", false);
                    var unNeedIds = "";//已经选中的商品id
                    $("#datatable-buttons tbody tr").each(function (index, item) {
                        var goodsId = $(this).find("td").eq(1).find("input").val();//调出商品id
                        if(goodsId!=''&&goodsId!=undefined){
                            unNeedIds = unNeedIds+","+goodsId;
                        }
                    });
                    unNeedIds = unNeedIds.substr(1);
                    $("#unNeedIds").val(unNeedIds);
                    $("#id").val(depotId);
                }
            } else if(type == 6) {
                thisObj=obj;
                var merchantId = $("#merchant").val();
                if(!merchantId){
                    $custom.alert.error("请先选择公司！");
                    return false;
                } else{
                    // 初始化重选按钮
                    $(".group-checkable").prop("checked", false);
                    var unNeedIdArr = [];//已经选中的商品id
                    $("#table-list tbody tr").each(function (index, item) {
                        debugger
                        var goodsId = $(this).find("input").eq(0).val();
                        if(goodsId){
                            unNeedIdArr.push(goodsId);
                        }
                    });
                    var unNeedIds = unNeedIdArr.join(",");
                    $("#unNeedIds").val(unNeedIds);
                    $("#id2").val(merchantId);
                }
            } else if (type == 7) {
                thisObj=obj;
                var merchantId = $("#merchantId").val();
                // 初始化重选按钮
                $("#existMerchantId").val(merchantId);
                $(".group-checkable").prop("checked", false);
                var unNeedIdArr = [];//已经选中的商品id
                var obj_ids = document.getElementsByName("goodsId");
                for(var i = 0; i < obj_ids.length; i++){
                    unNeedIdArr.push(obj_ids[i].value);
                }
                var unNeedIds = unNeedIdArr.join(",");
                $("#unNeedIds").val(unNeedIds);
            } else if (type == 8) {
                thisObj=obj;
                // 初始化重选按钮
                $(".group-checkable").prop("checked", false);
            }
            $erpPopup.orderGoods.show();
        },
        list:function() {
            // 重新加载select2
            $(".goods_select2").select2({
                language:"zh-CN",
                placeholder:'请选择',
                allowClear:true
            });
            $erpPopup.orderGoods.loadTable(1, 10);
        },
        // 加载表格
        loadTable:function(pageNo, pageSize) {
            $($erpPopup.orderGoods.params.tableId + ' tr:gt(0)').remove();
            var form = $($erpPopup.orderGoods.params.formId).serializeArray();
            // 当前页，每页显示条数
            var data_pageNo = {
                name:"begin",
                value:(pageNo-1)*pageSize
            };
            var data_pageSize = {
                name:"pageSize",
                value:pageSize
            };
            form.push(data_pageNo);
            form.push(data_pageSize);
            var resultJson = $erpPopup.merchandise.loadOrderMerchandisesByPage(form);
            var $tr = "";
            //渲染表格
            $.each(resultJson.list,function(i,e){
            	if (initType=='7') {
            		$tr += "<tr>"+
                    "<td><input type='checkbox' class='iCheck' name='checkId' value="+(e.id==null?'':e.id)+"></td>"+
                    "<td>"+(e.merchantName==null?'':e.merchantName)+"</td>"+
                    "<td>"+(e.name==null?'':e.name)+"</td>"+
                    "<td>"+(e.goodsNo==null?'':e.goodsNo)+"</td>"+
                    "<td>"+(e.brandName==null?'':e.brandName)+"</td>"+
                    "<td>"+(e.barcode==null?'':e.barcode)+"</td>"+
                    "<td>"+(e.factoryNo==null?'':e.factoryNo)+"</td>"+
                    "<td>"+(e.unitName==null?'':e.unitName)+"</td>"+
                    "<td>"+(e.customsAreaNames==null?'':e.customsAreaNames)+"</td>"+
                    "<td>"+(e.outDepotFlag=='0'?'是':'否')+"</td>"+
                    "</tr>";	
				}else{
					$tr += "<tr>"+
                    "<td><input type='checkbox' class='iCheck' name='checkId' value="+(e.id==null?'':e.id)+"></td>"+
                    "<td>"+(e.merchantName==null?'':e.merchantName)+"</td>"+
                    "<td>"+(e.name==null?'':e.name)+"</td>"+
                    "<td>"+(e.goodsNo==null?'':e.goodsNo)+"</td>"+
                    "<td>"+(e.brandName==null?'':e.brandName)+"</td>"+
                    "<td>"+(e.barcode==null?'':e.barcode)+"</td>"+
                    "<td>"+(e.factoryNo==null?'':e.factoryNo)+"</td>"+
                    "<td>"+(e.unitName==null?'':e.unitName)+"</td>"+
                    "<td>"+(e.customsAreaNames==null?'':e.customsAreaNames)+"</td>"+
                    "</tr>";
				}
                
            });
            $($erpPopup.orderGoods.params.tableId).append($tr);
            var begin = resultJson.list.length > 0 ? resultJson.begin:0;
            var end = resultJson.list.length > 0 ? resultJson.end:0;
            var total = resultJson.list.length > 0 ? resultJson.total:0;
            $('.page_txt').html("当前显示第 " + (begin + 1) + " 至 " + end + " 项，共 " + total + " 项");
            $erpPopup.orderGoods.loadPageUtils(resultJson);
        },
        // 加载分页插件
        loadPageUtils:function(resultJson) {
            $('.pageUtils').pagination({
                totalData:resultJson.list.length > 0 ? resultJson.total:1, // 数据总条数
                showData:resultJson.list.length > 0 ? resultJson.pageSize:1, // 每页显示的条数
                current:resultJson.list.length > 0 ? resultJson.pageNo:1, // 当前第几页
                coping:true, // 开启首页和尾页
                homePage:'首页', // 首页节点内容
                endPage:'末页', // 末页节点内容
                prevContent:'上页', // 上一页内容
                nextContent:'下页', // 下一页内容
                jump:false, // 关闭跳转到指定页数
                callback:function(api) { // 回调方法
                    $erpPopup.orderGoods.loadTable(api.getCurrent(), resultJson.pageSize); // 重新加载页面表格数据
                }
            });
        },
        reloadTable:function(){
            $erpPopup.orderGoods.loadTable(1,10);
        },
        show:function() {
            $erpPopup.orderGoods.list();
            $($erpPopup.orderGoods.params.modalId).modal("show");
        },
        hide:function() {
            $($erpPopup.orderGoods.params.modalId).modal("hide");
            //将自定义选商品的链接还原，否则会影响其他页面的商品选择
            $erpPopup.orderGoods.params.getMerchandisesByPageURL = "/merchandise/getOrderPopupList.asyn";
        },
	}
};
