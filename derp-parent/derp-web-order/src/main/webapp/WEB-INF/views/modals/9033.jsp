<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div class="row">
    <div class="col-12">
        <div>
            <!-- Signup modal content -->
            <div id="stockOut-modal"  class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;position:fixed;top:130px;bottom:auto;height:100%;">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-body">
                            <form class="form-horizontal" id="receive-from">
                                <!-- 自定义参数  -->                              
                        <div class="form-group">
	                      <div class="col-12">
	                      	  <label style="margin-left: 48px;"><i class="red">*</i>出库时间: </label>           
	                      	  <input type="text" class="input_msg form_datetime date-input" name="outDepotDate" id="outDepotDate">
	                      	  <input type="hidden" id="stockOutIds"/>
	                    </div>
	                 	</div>
	                  <div class="form-group">
	                      <div class="col-12">
	                      	  <label style="margin-left: 48px;">对选中的单据进行中转仓出库确认！</label>                                	
	                      </div>
	                 	</div>
                                <div class="form-row mt20">
                      		<div class="form-inline m0auto">
                          		<div class=form-group>
                          			<button type="button" class="btn btn-info waves-effect waves-light fr delayedBtn" onclick='stockOutSave()'>确定</button>
                          			<button type="button" class="btn btn-light waves-effect waves-light ml15"  onclick='stockOutCancel()'>取消</button>
                          		</div>
                          	</div>
                      </div>
                            </form>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->
        </div>
    </div><!-- end col -->
</div>
<script type="text/javascript">
	$(document).ready(function() {
	});
   
   $derpTimer.lessThanNowDateTimer("#outDepotDate", "yyyy-MM-dd HH:mm:ss") ;
   
	// 中转仓出库
	function stockOutSave(){
		//必填项校验
		var outDepotDate = $("#outDepotDate").val();
		var ids = $("#stockOutIds").val();	
		if(outDepotDate==""){
			$custom.alert.error("请选择出库时间");
			return ;
		}	
		var url = serverAddr+"/sale/saleOrderStockOut.asyn";
        //获取选中订单的所有商品和对应数量（相同商品数量叠加）
        var checkInventoryGoods = [];
        $custom.request.ajax(serverAddr+"/sale/getOrderGoodsInfo.asyn",{"ids":ids,"type":"2"},function(data){
            var flag = true;
            var map = data.data;
            for(var key in map){
                var keys = key.split(',');
                var outDepotId = keys[1];
                var goodsId = keys[0];
                var outDepotCode = keys[2];
                var goodsNo = keys[3];
                var unit=keys[4];
                var goodsNum = map[key];
                var tallyingUnit='';
                if ('HK01'==outDepotCode) {
                    if ('00'==unit) {
                        tallyingUnit='0';
                    }else if ('01'==unit) {
                        tallyingUnit='1';
                    }else if ('02'==unit) {
                        tallyingUnit='2';
                    }
                }
                checkInventoryGoods.push({"goodsId":goodsId, "goodsNo":goodsNo, "goodsNum":goodsNum,'tallyingUnit':tallyingUnit,"outDepotId":outDepotId});
            }
        });

        var mergeJson = {};
        var flag = true;
        //合并相同项
        $.each(checkInventoryGoods, function (index, value) {
            var originalGoodsId = $module.inventoryLocationMapping.getOriginalGoodsId(value.goodsId, value.goodsNo) ;
            if (originalGoodsId) {
                if (!mergeJson[originalGoodsId]) {
                    mergeJson[originalGoodsId] = value;
                } else {
                    var num = mergeJson[originalGoodsId].transferNum;
                    value.goodsNum = parseInt(num) + parseInt(value.transferNum);
                    mergeJson[originalGoodsId] = value;
                }
            } else {
                mergeJson[value.goodsId] = value;
            }
        });

        //查询可用量
        $.each(mergeJson, function (index, value) {
            var DepotData = $module.depot.getDepotById(value.outDepotId);
            var depotType = DepotData.type;
            var searchUnit = null;
            if (depotType == 'c') {
                if(value.tallyingUnit == "00"){//转换为库存的理货单位
                    searchUnit = "0";//托盘
                }else if(value.tallyingUnit == "01"){
                    searchUnit = "1";//箱
                }else if(value.tallyingUnit == "02"){
                    searchUnit = "2";//件
                }
            }

            var availableNum =$module.inventory.queryAvailability(value.outDepotId,index,DepotData.code,searchUnit,null,null,null);
            var tempGoodsNo = $module.inventoryLocationMapping.getOriginalGoodsNo(index, value.goodsNo) ;
            if(availableNum ==-1){
                flag = false;
                if(tempGoodsNo){
                    $custom.alert.warningText("原货号：" + tempGoodsNo + " 商品货号："+value.goodsNo+",未查到库存可用量");
                }else{
                    $custom.alert.warningText("未查到库存余量货号："+value.goodsNo+",仓库:"+DepotData.name);
                }
                return;
            }else if(value.goodsNum>availableNum){
                flag = false;
                if(tempGoodsNo){
                    $custom.alert.warningText("库存不足，"+"原货号：" + tempGoodsNo + +" 商品货号："+value.goodsNo+",余量："+availableNum);
                }else{
                    $custom.alert.warningText("库存不足货号："+value.goodsNo+",仓库:"+DepotData.name+",余量："+availableNum);
                }
                return;
            }
        });
            if(!flag){
                return;
            }
            //执行出库
            $custom.request.ajax(url,{"ids":ids,"outDepotDateStr":outDepotDate},function(data){
                if(data.state==200){
                    $custom.alert.success("出库成功");
                    //重新加载页面
                    setTimeout(function () {
                        $load.a(pageUrl+"4001");
                    }, 1000);
                }else{
                    if(!!data.expMessage){
                        $custom.alert.error(data.expMessage);
                    }else{
                        $custom.alert.error(data.message);
                    }
                }
            });

			$('#stockOut-modal').modal('hide');
			$("#id").val("");
			$("#outDepotDate").val("");	
	};
	function stockOutCancel(){
		$('#stockOut-modal').modal('hide');
		$("#id").val("");
		$("#outDepotDate").val("");	
	}

</script>