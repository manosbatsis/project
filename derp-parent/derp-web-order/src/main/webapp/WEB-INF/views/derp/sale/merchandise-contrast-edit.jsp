<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!-- App favicon -->

<jsp:include page="/WEB-INF/views/common.jsp" />
<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
          <form id="add-form" action="" name="form1">
          <div class="row">
           <div class="col-12">
              <div class="card-box" >
                                         <!--  title   start  -->
              <div class="col-12">
                <div>
                   <div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                           <li><i class="block"></i> 当前位置：</li>
	                           <li class="breadcrumb-item"><a href="javascript:$module.load.pageOrder('act=11001');">爬虫商品对照表</a></li>
	                           <li class="breadcrumb-item">编辑</li>
                       </ol>
                    </div>
            </div>
            <!-- 基本信息开始 -->
            <div class="title_text">基本信息</div>
                    <div class="info_box">
                     <input type="hidden" required="" class="edit_input" name="id" id="id" value="${detail.id}">
                    <div class="edit_box">
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i>平台名称：</label>
                             <input type="text" required="" parsley-type="text" class="edit_input" name="platformName" id="platformName" value="${detail.platformName}">
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i>平台用户名：</label>
                            <input type="text" required="" parsley-type="text" class="edit_input" name="platformUsername" id="platformUsername" value="${detail.platformUsername}">
                        </div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i>公司：</label>
                            <select class="edit_input" id="merchant" name="merchantId" onchange="getGoods()">
                            </select>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i>事业部：</label>
                            <select class="edit_input" id="buId" name="buId" >
                            </select>
                        </div>
                    </div>
                     <div class="edit_box">
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i>爬虫商品货号：</label>
                            <input type="text" required="" parsley-type="text" class="edit_input" name="crawlerGoodsNo" id="crawlerGoodsNo" value="${detail.crawlerGoodsNo}">
                        </div>
                         <div class="edit_item">
                             <label class="edit_label"><i class="red">*</i>爬虫商品名称：</label>
                             <input type="text" required="" parsley-type="text" class="edit_input" name="crawlerGoodsName" id="crawlerGoodsName" value="${detail.crawlerGoodsName}">
                         </div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i>状态：</label>
                            <select class="edit_input" id="status" name="status">
                            </select>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i>平台类型：</label>
                            <select class="edit_input" id="type" name="type">
                            </select>
                        </div>
                    </div>
                  </div>

                    <!--   商品信息  start -->
                    <div class="col-12 ml10">
                        <div class="row">
                            <div class="col-11">
                                <div class="title_text">商品信息</div>
                            </div>
                            <div class="col-1 mt10">
                                <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick="$erpPopup.orderGoods.init(6, null, null);">选择商品</button>
                            </div>
                        </div>
                    </div>
                    <div class="form-row mt20 ml15 table-responsive">
                        <table id="table-list" class="table table-striped">
                            <thead>
                            <tr>
                                <th>商品货号</th>
                                <th>商品名称</th>
                                <th>条形码</th>
                                <th><i class="red">*</i>数量</th>
                                <th><i class="red">*</i>销售标准单价</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody id="tbody">
                            <c:forEach items="${item}" var="item">
                                <tr>
                                    <input type="hidden" name="goodsId" value="${item.goodsId}"/>
                                    <td>${item.goodsNo}<input type="hidden" name="goodsNo" class='goodsNo' value="${item.goodsNo}"/></td>
                                    <td>${item.goodsName}<input type="hidden" name="goodsName" value="${item.goodsName}"/></td>
                                    <td>${item.barcode}<input type="hidden" name="barcode" value="${item.barcode}"/></td>
                                    <td><input type='text' name='num' style='width:70px;text-align:center;' class='goods-num' value="${item.num}"  onkeyup="value=value.replace(/[^\d]/g,'')"></td>
                                    <td><input type='text' name='price' class='goods-price' style='width:100px;text-align:center;' value='<fmt:formatNumber value="${item.price}" pattern="#.#####" minFractionDigits="5" />'  ></td>
                                    <td><p class='nowrap'><a href='javascript:void(0)' onclick='remove(this,"${item.goodsId}")'>删除</a></p></td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <!-- 基本信息结束 -->
                 </div>
                </div>
                  <div class="form-row m-t-50">
                        <div class="col-12">
                            <div class="row">
                                <div class="col-6">
                                    <input type="button" class="btn btn-info waves-effect waves-light fr btn_default" id="save-buttons" value="保 存"/>
                                </div>
                                <div class="col-6">
                                    <input type="button" class="btn btn-light waves-effect waves-light btn_default" id="cancel-buttons" value="取 消"/>
                                </div>
                            </div>
                        </div>
                    </div>
              </div>

             </div>

           </div>
          </form>
            <!-- 弹窗开始 -->
            <jsp:include page="/WEB-INF/views/modals/1504.jsp" />
            <!-- 弹窗结束 -->
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->
<script type="text/javascript">

$(document).ready(function () {
    //加载公司
    var merchantId = "${detail.merchantId}";
    var status = "${detail.status}";
    var buId = "${detail.buId}";
    $module.merchant.loadSelectData.call($('select[name="merchantId"]')[0], merchantId);
    $module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"merchandiseContrast_statusList",status);
    $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0], buId, merchantId, null, null);
    $module.constant.getConstantListByNameURL.call($('select[name="type"]')[0],"merchandiseContrast_typeList",'${detail.type}');
    //重新加载select2
    $(".goods_select2").select2({
        language: "zh-CN",
        placeholder: '请选择',
        allowClear: true
    });

});


//获取商品
function getGoods(){
    var merchantId = $("#merchant").val();
    var goodsNo = $("#goodsNo").val();
    if(merchantId==null||merchantId==''||merchantId==undefined||goodsNo==null||goodsNo==''||goodsNo==undefined){
      return false;
    }
    
    var url = serverAddr+"/merchandise/getByMerchantAndGoodsNo.asyn";
    $custom.request.ajax(url, {"merchantId":merchantId,"goodsNo":goodsNo}, function(result){
       if(result.data.code=='00'){
            $("#goodsName").val(result.data.merchandise.name);
       }else{
           $custom.alert.error(result.data.message);
       }
   })

    //加载事业部
    $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null, merchantId, null, null);
}
   

//保存按钮
$("#save-buttons").click(function(){
    var url = serverAddr+"/contrast/saveContrast.asyn";
    var form = {};
    var json = {};

    //必填项校验
    var id = $("#id").val();
    var platformName = $("#platformName").val();
    var crawlerGoodsNo = $("#crawlerGoodsNo").val();
    var platformUsername = $("#platformUsername").val();
    var crawlerGoodsName = $("#crawlerGoodsName").val();
    var merchantId = $("#merchant").find("option:selected").val();
    var merchantName = $("#merchant").find("option:selected").text();
    var buId = $("#buId").find("option:selected").val();
    var buName = $("#buId").find("option:selected").text();
    var status = $("#status").find("option:selected").val();
    var type = $("#type").find("option:selected").val();

    if(platformName==null||platformName==''||platformName==undefined){
       $custom.alert.error("请输入平台名称！");
       return false;
    }
    if(platformUsername==null||platformUsername==''||platformUsername==undefined){
        $custom.alert.error("请输入平台用户名！");
        return false;
    }
	    if(crawlerGoodsNo==null||crawlerGoodsNo==''||crawlerGoodsNo==undefined){
	       $custom.alert.error("请输入爬虫商品货号！");
	       return false;
	    }
	    if(crawlerGoodsName==null||crawlerGoodsName==''||crawlerGoodsName==undefined){
	       $custom.alert.error("请输入爬虫商品名称！");
	       return false;
	    }
        if(merchantId==null||merchantId==''||merchantId==undefined){
	       $custom.alert.error("请选择公司！");
	       return false;
	    }
        if(status==null||status==''||status==undefined){
            $custom.alert.error("请选择状态！");
            return false;
        }
        if(!buId) {
            $custom.alert.error("请选择事业部！");
            return false;
        }

    var itemList = [];
    var result = true;
    var flag = false;
    $('#table-list tbody tr').each(function(i) {// 遍历 tr

        var item ={};
        var goodsId = $(this).find('input[name="goodsId"]').val();
        item['goodsId']=goodsId;
        var goodsName = $(this).find('input[name="goodsName"]').val();
        item['goodsName']=goodsName;
        var goodsNo = $(this).find('input[name="goodsNo"]').val();
        item['goodsNo']=goodsNo;
        var barcode = $(this).find('input[name="barcode"]').val();
        item['barcode']=barcode;
        var num = $(this).find('input[name="num"]').val();
        item['num']=num;
        var price = $(this).find('input[name="price"]').val();
        item['price']=price;

        if(num==null||num==''||num==undefined){
            $custom.alert.error("数量必填！");
            flag = true;
            result = false;
            return false;
        }

        // debugger;
        // if (+num >= 1 && +num <= 99){
        //     $custom.alert.error('数量的范围是1-99的整数');
        //     return false;
        // }

        // if (!/^([1-9]){1,2}$/g.test(num)) {
        //     $custom.alert.error('请输入1-99之间的数字');
        //     return false;
        // }

        if(price==null||price==''||price==undefined){
            $custom.alert.error("销售标准单价必填！");
            flag = true;
            result = false;
            return false;
        }

        itemList.push(item);

    });
    if (!result) {
        return false;
    }
    if (!flag){
        if(!itemList || itemList.length==0){
            $custom.alert.error("至少选择一个商品");
            return;
        }
    }

    json['id']=id;
    json['platformName']=platformName;
    json['platformUsername']=platformUsername;
    json['crawlerGoodsNo']=crawlerGoodsNo;
    json['crawlerGoodsName']=crawlerGoodsName;
    json['merchantId']=merchantId;
    json['merchantName']=merchantName;
    json['buId']=buId;
    json['buName']=buName;
    json['status']=status;
    json['type']=type;
    json['itemList']=itemList;
    var jsonStr= JSON.stringify(json);
    form['json']=jsonStr;
        
		$custom.request.ajax(url, form, function(result){
			if(result.data.code == '00'){
				$custom.alert.success("保存成功！");
				setTimeout(function () {
					$module.load.pageOrder("act=11001");
				}, 1000);
			}else{
				$custom.alert.error(result.data.message);
			}
		});

	});

//返回
$("#cancel-buttons").click(function(){
	$module.load.pageOrder("act=11001");
});

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


</script>
