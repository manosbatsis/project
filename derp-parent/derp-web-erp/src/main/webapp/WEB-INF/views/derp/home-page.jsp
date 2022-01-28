<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!-- ============================================================== -->
<!-- Start right Content here -->
<!-- ============================================================== -->
<!-- Start content -->

<link href='<c:url value="/resources/assets/css/homepage.css" />' rel="stylesheet" type="text/css"/>
<link href='<c:url value="/resources/assets/scss/icons/order-detail/order-detail.css" />' rel="stylesheet"
      type="text/css"/>
<link href='<c:url value="/resources/plugins/datetimepicker/bootstrap-datetimepicker.min.css" />' rel="stylesheet" type="text/css"/>
<link href='<c:url value="/resources/assets/css/bootstrap.min.css" />' rel="stylesheet" type="text/css" />
<%--<link href='<c:url value="/resources/assets/css/style.css" />' rel="stylesheet" type="text/css" />--%>
<%--<link href='<c:url value="https://cdn.bootcss.com/bootstrap-datetimepicker/4.17.47/css/bootstrap-datetimepicker.min.css" />'--%>
        <%--rel="stylesheet" type="text/css"/>--%>
<script href='<c:url value="/resources/assets/js/jquery.core.js" />' rel="stylesheet" type="text/css"></script>
<script href='<c:url value="/resources/assets/js/bootstrap.min.js" />' rel="stylesheet" type="text/css"></script>
<script src='<c:url value="/resources/plugins/datetimepicker/bootstrap-datetimepicker.js"/>'></script>
<script src='<c:url value="/resources/plugins/datetimepicker/bootstrap-datetimepicker.zh-CN.js"/>'></script>
<script src='<c:url value="/resources/plugins/datetimepicker/bootstrap-datetimepicker.fr.js"/>'></script>

<style>
    .user_operate {
        color:#3488CA;
    }
    .user_operate_a {
        color:#3488CA;
    }
    .remove_li {
        pointer-events: none;
    }
</style>
<div class="htmleaf-container">
    <div class="container-top">
        <div class="contain-left">
            <div class="order">
                <div class="order-head">
                	<div class="order-tab-box">
                		<span class="order-tab" style="margin-bottom:2px;">快捷入口</span>
                		/
                		<span>事业部库存</span>

                	</div>
                    <shiro:hasPermission name="index_bu_inventory">
                        <div class="inventory-div" style="display: none;">
                            <a class="store-detail" style="display: block;" href="javascript:void(0);" onclick="toBuInventoryDetail();">
                                详情
                            </a>
                        </div>
                    </shiro:hasPermission>
                </div>
               	<div class="order-detail-box order-detail-show">
                    <div class="order-detail">
                        <shiro:hasPermission name="purchase_add">
                            <a class="order-buy order-content" href="javascript:void(0);"  onclick="addPurchaseOrder();">
                                <span class="order-title"><span class="radius"><i class="icon iconfont icon-cart"></i></span>入口</span>
                                <span>新增采购订单</span>
                                <span class="order-num">01</span>
                            </a>
                        </shiro:hasPermission>
                        <shiro:lacksPermission name="purchase_add">
                            <a class="order-buy order-content" href="javascript:void(0);">
                                <span class="order-title"><span class="radius"><i class="icon iconfont icon-cart"></i></span>入口</span>
                                <span class="order-num">01</span>
                            </a>
                        </shiro:lacksPermission>
                        <shiro:hasPermission name="sale_add">
                            <a class="order-sale order-content" href="javascript:void(0);" onclick="addSaleOrder();">
	                            <span class="order-title"><span class="radius"><i
                                        class="icon iconfont icon-orderform"></i></span>入口</span>
                                <span>新增销售订单</span>
                                <span class="order-num">02</span>
                            </a>
                        </shiro:hasPermission>
                        <shiro:lacksPermission name="sale_add">
                            <a class="order-sale order-content" href="javascript:void(0);">
	                            <span class="order-title"><span class="radius"><i
                                        class="icon iconfont icon-orderform"></i></span>入口</span>
                                <span class="order-num">02</span>
                            </a>
                        </shiro:lacksPermission>
                        <shiro:hasPermission name="transfer_add">
                            <a class="order-adjust order-content" href="javascript:void(0);" onclick="addTransferOrder();">
                                <span class="order-title"><span class="radius"><i class="icon iconfont icon-diaobo"></i></span>入口</span>
                                <span>新增调拨订单</span>
                                <span class="order-num">03</span>
                            </a>
                        </shiro:hasPermission>
                        <shiro:lacksPermission name="transfer_add">
                            <a class="order-adjust order-content" href="javascript:void(0);">
                                <span class="order-title"><span class="radius"><i class="icon iconfont icon-diaobo"></i></span>入口</span>
                                <span class="order-num">03</span>
                            </a>
                        </shiro:lacksPermission>
                    </div>
               	</div>
               	<div class="order-detail-box">
                    <div class="stock-echart-l"></div>
                    <div class="stcok-echart-box">
                        <div style="font-size: 18px; font-weight: bolder; font-family: FangSong; padding-left: 20%; padding-top: 10px; color: #888 ">事业部仓库库存分布图</div>
                        <a href="javascript:;" disabled="disabled" style="cursor: pointer;" class="prev">&lt;</a>
                        <div class="stock-echart-r">

                        </div>
                        <a href="javascript:;" disabled="disabled" style="cursor: pointer;" class="next">&gt;</a>
                    </div>
	        	</div>
            </div>
            <div class="todo">
                <div class="todo-head tab-head">
                    <p style="width: 100px;">待办事项</p>
                    <ul class="todo-list" style="width: 650px;">
                        <li class="order-active" onclick="getPendingRecordOrders()">待录入时间 <span class="tip recordTip">*</span></li>
                        <li value="data1" onclick="getPendingShelfSaleOrders()">待上架 <span class="tip shelfTip">*</span></li>
                        <li value="data2" onclick="getPendingConfirmOrders()">待确认 <span class="tip confirmTip">*</span></li>
                        <li value="data3" onclick="getPendingCheckOrders()">待审核 <span class="tip checkTip">*</span></li>
                        <li value="data4" onclick="getPendingCarryForward()">待结转 <span class="tip carryTip">*</span></li>
                        <li value="data5" onclick="getSkuPriceWarns()">SKU预警 <span class="tip warnTip">*</span></li>
                    </ul>
                </div>

                <div class="todo-content">
                    <table style="table-layout: fixed;width:100%;border-collapse:collapse; border-spacing:0;">
                        <thead>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                    <div id="titleTips"></div>
                </div>
            </div>
        </div>
        <div class="store">
            <div class="store-head">
                <p>效期预警</p>
                <shiro:hasPermission name="index_inventoryWarning">
                    <a class="store-detail" href="javascript:void(0);" onclick="toInventoryWarningList();">
                        详情
                    </a>
                </shiro:hasPermission>
            </div>

            <div class="store-option">
                <span>统计仓库:</span>
                <select class="options" name="depotId" id="depotId">

                </select>
            </div>
            <div class="store-content">
                <div class="store-echart" style="width: 370px;height:422px;"></div>
            </div>
        </div>
    </div>
    <div class="container-bottom">
        <div class="brand-head tab-head">
            <p>品牌销售 Top 10</p>
            <ul class="brand-list">
                <li class="brand-active" saleType="1" orderType="1">购销</li>
                <li saleType="2" orderType="1">代销</li>                
                <li saleType="3" orderType="1">采销执行</li>
                <li saleType="002" orderType="2">一件代发</li>
                <li saleType="001" orderType="2">POP</li>
            </ul>
            <div class="input-append date" id="datetimepicker" >
                <div class="date">
                    <span>选择年月：</span>
                    <input type="text" class="input_msg form_datetime date-input" id="deliverDate"
                           style="font-size: 14px;" onchange="top10Data();">
                </div>

            </div>
        </div>
        <div class="brand-detail">
            <div class="shop-echart" style="width: 50%;height:356px;"></div>
            <div class="brand-echart" style="width: 50%;height:356px;"></div>
        </div>

    </div>
</div>
<jsp:include page="/WEB-INF/views/modals/13001.jsp" />
<jsp:include page="/WEB-INF/views/modals/13002.jsp" />
<jsp:include page="/WEB-INF/views/modals/13003.jsp" />
<script src='<c:url value="/resources/plugins/echarts/echarts.min.js" />'></script>
<script src='<c:url value="/resources/plugins/echarts/echarts-gl.min.js" />'></script>
<script src='<c:url value="/resources/plugins/jquery-starfield/jquery-starfield.js" />'></script>
<script src='<c:url value="/resources/assets/js/jquery.slimscroll.js" />'></script>

<script>

    var userType = '${type}';
    var storeIndex = 0;
    $(document).ready(function () {

        var initYear;
        laydate.render({
            elem: '#deliverDate', //指定元素
            type: 'month',
            showBottom: false,
            ready: function(date){ // 控件在打开时触发，回调返回一个参数：初始的日期时间对象
                initYear = date.year;
            },
            change: function(value, date, endDate){ // 年月日时间被切换时都会触发。回调返回三个参数，分别代表：生成的值、日期时间对象、结束的日期时间对象
                var selectYear = date.year;
                var differ = selectYear-initYear;
                if (differ == 0) {
                    if($(".layui-laydate").length){
                        $("#deliverDate").val(value);
                        top10Data();
                        $(".layui-laydate").remove();
                    }
                }
                initYear = selectYear;
            }
        });
        $("#deliverDate").val(getPreMonth());
        //加载仓库下拉
        if (userType == '1') {
            $module.depot.loadSelectByType.call($("select[name='depotId']")[0], "a,c", 11);
        } else {
            $module.depot.getSelectBeanByMerchantRel.call($("select[name='depotId']")[0],11, {"type":"a,c","isInvertoryFallingPrice":"1"});
        }

        //效期预警饼图
        var depotId = $('#depotId option:selected').val();
        drawInventoryWarning(depotId);

        //品牌销售Top10(购销)
        top10Data('1', '1');

        // 初始化Todolist列表数据
        countPendingOrdersNum();
        getPendingRecordOrders();
        // 事业部库存初始化
        /* drawStockPie() */
        // drawBuInventory()
    });
    //事业部库存/快捷入口切换
    $(".order-tab-box").on('click', 'span', function (e) {
    	// 切换tab
    	$(this).addClass('order-tab');
    	$(this).siblings().removeClass('order-tab');
    	// 切换box
    	$('.order-detail-box').eq($(this).index()).addClass('order-detail-show');
    	$('.order-detail-box').eq($(this).index()).siblings().removeClass('order-detail-show');
    	if($(this).index() == '1') {
            $(".inventory-div").css("display","inline-block");
            if (storeIndex == 0) {
                drawBuInventory()
            }
            storeIndex++;
        } else {
            $(".inventory-div").css("display","none");
        }
    })
    
    var stockList = []; // 事业部库存数据
    var stockData = []; // 默认前4个数据
    var preIdx = 0;
    var nextIdx = 3;
    var len = stockList.length;
    var preSelect = null; //事业部库存饼图选中事业部名称
    var preSelectId = null; //事业部库存饼图选中事业部id
    $(".prev").attr("disabled",true).css("cursor","not-allowed");
    $('.stcok-echart-box').on('click','a',function(e){
    	if(this.className =='prev'){
    		if(preIdx == 0) {
    			return
    		}
    		$(".next").attr("disabled",true).css("cursor","pointer");
    		stockData = stockList.slice(--preIdx,--nextIdx + 1)
    		drawStockPie(stockData)
    		if(preIdx == 0) {
    			$(".prev").attr("disabled",true).css("cursor","not-allowed");
    		}
    	}
    	if(this.className =='next'){
    		if(nextIdx >= len -1) {
                $(".next").attr("disabled",true).css("cursor","not-allowed");
    			return
    		}
            $(".prev").attr("disabled",true).css("cursor","pointer");
    		stockData = stockList.slice(++preIdx,++nextIdx + 1)
        	drawStockPie(stockData)
    	}
    })

    
    //加载库存仪表盘
    var stockEchart;
    function drawStockPie(stockData) {
        let dseries = [];
        for (var i=0; i<stockData.length; i++) {
            let pos = (15+23*i)+"%"
            let dData = {
                type: 'gauge',
                radius: '70%',
                center: [pos, '60%'],
                splitNumber: 0, //刻度数量
                startAngle: 225,
                endAngle: -45,
                axisLine: {
                    show: true,
                    lineStyle: {
                        width: 15,
                        color: [
                            [stockData[i].ratio, stockData[i].color],
                            [1, "#ececec"]
                        ],
                        width: 8
                    },
                },
                //分隔线样式。
                splitLine: {
                    show: false,
                },
                axisLabel: {
                    show: false
                },
                axisTick: {
                    show: false
                },
                pointer: {
                    show: false,
                },
                title: {
                    show: true,
                    offsetCenter: [0, '100%'], // x, y，单位px
                    textStyle: {
                        color: '#7C8B92',
                        fontSize: 13
                    }
                },
                //仪表盘详情，用于显示数据。
                detail: {
                    show: true,
                    offsetCenter: [0, '0%'],
                    color: stockData[i].color,
                    size: 12,
                    textStyle: {
                        fontSize: 18
                    }
                },
                data: stockData[i].data
            }
            dseries.push(dData)
        }
        $('.stock-echart-r').removeAttr("_echarts_instance_");
        stockEchart = echarts.init(document.querySelector('.stock-echart-r'))
		var value = 50
		var colorRegionRate = (value / 100).toFixed(2) // 颜色占用比例
		option = {
		    backgroundColor: '#fff',
		    series: dseries
		};
        stockEchart.setOption(option);

    }
    window.addEventListener('resize', function () {
    	var width = $('.stock-echart').width()
        stockEchart.resize({
             width:width,
             height:256
        });
    });
    //待办事项点击切换
    $('.todo-list').on('click', 'li', function (e) {
        $(this).siblings().removeClass('order-active')
        $(this).addClass('order-active')
    });

    //效期预警 仓库选择
    $("#depotId").change(function () {
        //要触发的事件
        var selectVal = $('#depotId option:selected').val();
        drawInventoryWarning(selectVal);
    });

    //品牌销售Top10 点击切换
    $('.brand-list').on('click', 'li', function (e) {
        $(this).siblings().removeClass('brand-active')
        $(this).addClass('brand-active')
        var orderType = $(this).attr("orderType");
        var saleType = $(this).attr("saleType");
        top10Data(saleType, orderType);
    });

    // 滚动条插件
    $(".todo-content tbody").slimScroll({
        /* width: '100%', //可滚动区域宽度 */
        height: '176px', //可滚动区域高度
        size: '10px', //滚动条宽度，即组件宽度
        color: '#333', //滚动条颜色
        position: 'right', //组件位置：left/right
        distance: '0px', //组件与侧边之间的距离
        start: 'top', //默认滚动位置：top/bottom
        opacity: .2, //滚动条透明度
        alwaysVisible: false, //是否 始终显示组件
        disableFadeOut: true, //是否 鼠标经过可滚动区域时显示组件，离开时隐藏组件
        railVisible: true, //是否 显示轨道
        railColor: '#333', //轨道颜色 
        railOpacity: .2, //轨道透明度
        railDraggable: true, //是否 滚动条可拖动
        railClass: 'slimScrollRail', //轨道div类名 
        barClass: 'slimScrollBar', //滚动条div类名
        wrapperClass: 'slimScrollDiv', //外包div类名
        allowPageScroll: true, //是否 使用滚轮到达顶端/底端时，滚动窗口
        wheelStep: 10, //滚轮滚动量
        touchScrollStep: 200, //滚动量当用户使用手势
        borderRadius: '7px', //滚动条圆角
        railBorderRadius: '7px' //轨道圆角
    })

    // 显示todo里面省略的内容
    $("tbody").on("mouseover", 'td', function (e) {
        //获取选中元素的私有属性值
        let _this = e.target;
        var val = $(_this).text();
        //获取元素左边距到窗口左边缘的距离  
        var xAxis = $(_this).offset().left;
        //获取元素上边距到窗口顶端的距离
        var yAxis = $(_this).offset().top;
        //获取当前元素的宽度与高度  
        var domWidth = $(_this).width();
        var domHeight = $(_this).height();
        //计算要显示字符的字母个数（显示的框框要根据字符数自动设置宽度）  
        var fontNumber = val.length;
        //设置每个字符所占据的像素长度  
        var widthForSingleAlpha = 10;
        //鼠标移入的时候显示提示框。
        if (this.offsetWidth < this.scrollWidth) { //设置文本框的样式以及坐标  
            $("#titleTips").css({
                "position": "absolute",
                "top": "4px",
                "width": fontNumber * widthForSingleAlpha + "px", /*自适应设置弹框宽度*/
                "height": "30px",
                "background": "#F9F9F9",
                "border": "1px solid grey",
                "line-height": "30px",
                "text-align": "center",
                "border-radius": "5px",
                "font-family": "microsoft yahei",
                "font-size": "15px",
                "font-weight": "normal",
                "z-index": "100",
                "color": "#2D353E"
            });

            $("#titleTips").css("top", (yAxis + domHeight) + "px");
            /*设置到顶端的距离*/
            var smallTipsWidth = $("#titleTips").width();
            /*获取弹框的宽度*/
            $("#titleTips").css("left", xAxis + domWidth / 2 - smallTipsWidth / 2);
            /*根据弹框的宽度设置其到左端的距离*/
            $("#titleTips").text(val);
            /*设置显示的文字内容*/
            $("#titleTips").show();
        }


    })
    $("tbody").on("mouseout", 'td', function (e) {
        if (this.offsetWidth < this.scrollWidth) {
            $("#titleTips").hide();
        }
    })
    /**快捷入口**/
    //新增采购订单——跳转到采购新增页面
    function addPurchaseOrder() {
        // $module.load.pageOrder("act=30011&pageSource=1");
        $module.load.pageOrderNew("/purchase/purchaseOrderedit/add?from=oldsystem"); // 新跳转
    }

    //新增销售订单——跳转到销售订单新增自制页面
    function addSaleOrder() {
        $module.load.pageOrderNew("/sales/salesorderadd?from=oldsystem"); // 新跳转
        // $module.load.pageOrder('act=40011&pageSource=1');
    }

    //新增调拨订单——跳转到调拨订单新增页面
    function addTransferOrder() {
        $module.load.pageOrderNew("/transfer/orderEdit/add?from=oldsystem"); // 新跳转
        // $module.load.pageOrder('act=80014&pageSource=1');
    }

    /**待办事项**/
    //统计待办事项数量
    function countPendingOrdersNum() {
        $custom.request.ajax($module.params.serverAddrByOrder + "/external/countPendingOrderNum.asyn", {}, function (data) {
            if (data.state == 200) {
                if (data.data.pendingRecordNum == 0) {
                    $('.recordTip').remove();
                    $('.todo-list li').eq(0).attr("style", "pointer-events: none");
                }
                if (data.data.pendingShelfNum == 0) {
                    $('.shelfTip').remove();
                    $('.todo-list li').eq(1).attr("style", "pointer-events: none");
                }
                if (data.data.pendingConfirmNum == 0) {
                    $('.confirmTip').remove();
                    $('.todo-list li').eq(2).attr("style", "pointer-events: none");
                }
                if (data.data.pendingCheckNum == 0) {
                    $('.checkTip').remove();
                    $('.todo-list li').eq(3).attr("style", "pointer-events: none");
                }
            } else {
                if (!data.expMessage) {
                    $custom.alert.error(data.expMessage);
                } else {
                    $custom.alert.error(data.message);
                }
            }

        });

        $custom.request.ajax($module.params.serverAddrByInventory + "/external/getPendingCarryForward.asyn", {}, function (data) {
            if (data.state == 200) {
                if (data.data.length == 0) {
                    $('.carryTip').remove();
                    $('.todo-list li').eq(4).attr("style", "pointer-events: none");
                }
            } else {
                if (!data.expMessage) {
                    $custom.alert.error(data.expMessage);
                } else {
                    $custom.alert.error(data.message);
                }
            }
        });

        $custom.request.ajax($module.params.serverAddrByReport + "/external/countSkuPriceWarns.asyn", {}, function (data) {
            if (data.state == 200) {
                if (data.data.count == 0) {
                    $('.warnTip').remove();
                    $('.todo-list li').eq(5).attr("style", "pointer-events: none");
                }
            } else {
                if (!data.expMessage) {
                    $custom.alert.error(data.expMessage);
                } else {
                    $custom.alert.error(data.message);
                }
            }
        });
        removeOperate();
    }

    //待录入时间列表
    function getPendingRecordOrders() {
        $custom.request.ajax($module.params.serverAddrByOrder + "/external/getPendingRecordOrders.asyn", {}, function (data) {
            if (data.state == 200) {
                $('.todo-content thead').html('')
                $('.todo-content thead').append(`<tr>
	            <th style="padding-right: 5%;">订单编号</th>
	            <th>入仓仓库</th>
	            <th>PO单号</th>
	            <th>事业部</th>
<!--	            <th class="user_operate">操作</th>-->
	            </tr>`)
                $('.todo-content tbody').html('')
                let len = data.data.length
                for (let i = 0; i < len; i++) {
                    var operate = '<shiro:hasPermission name="purchase_receiveInvoice"><a href="javascript:void(0);" onclick="receiveInvoice(' + data.data[i].id + ')">录入发票时间</a></shiro:hasPermission>';
                    if (data.data[i].billStatus == '1') {
                        operate = '<shiro:hasPermission name="purchase_alreadyPay"><a href="javascript:void(0);" onclick="alreadyPay(' + data.data[i].id + ')">录入付款时间</a></shiro:hasPermission>';
                    }
                    var buName = data.data[i].buName;
                    if(!buName) {
                        buName = '';
                    }
                    $('.todo-content tbody').append(`<tr>
<!--                        <td style="padding-right: 5%;"><a class="user_operate_a" href="javascript:void(0);" onclick="toPurchaseOrderDetail(\${data.data[i].id})">\${data.data[i].code}</a></td>-->
                        <td>\${data.data[i].code}</td>
                        <td>\${data.data[i].depotName}</td>
                        <td>\${isEmpty(data.data[i].poNo)}</td>
                        <td>\${buName}</td>
<!--                        <td class="user_operate">\${operate}</td>-->
                        </tr>`)
                }
            } else {
                if (!data.expMessage) {
                    $custom.alert.error(data.expMessage);
                } else {
                    $custom.alert.error(data.message);
                }
            }
            removeOperate();
        });

    }

    //待录上架列表
    function getPendingShelfSaleOrders() {
        $custom.request.ajax($module.params.serverAddrByOrder + "/external/getPendingShelfOrders.asyn", {}, function (data) {
            if (data.state == 200) {
                $('.todo-content thead').html('')
                $('.todo-content thead').append(`<tr>
	            <th>订单编号</th>
	            <th>出仓仓库</th>
	            <th>事业部</th>
	            <th>客户</th>
	            <th>销售类型</th>
<!--	            <th class="user_operate">操作</th>-->
	            </tr>`)
                $('.todo-content tbody').html('')
                let len = data.data.length
                for (let i = 0; i < len; i++) {
                    var saleType = '';
                    var operate = '';
                    if (data.data[i].saleType == '1') {
                        saleType = '购销-整批结算';
                    } else {
                        saleType = '购销-实销实结';
                    }

                    if (data.data[i].status == '018') {
                        operate = '<shiro:hasPermission name="sale_receive"><a href="javascript:void(0);" onclick="receive(' + data.data[i].id + ',' + data.data[i].saleType + ',' + data.data[i].poNo + ')">签收</a></shiro:hasPermission>';
                    } else {
                        operate = '<shiro:hasPermission name="sale_shelfIsEnd"><a href="javascript:void(0);" onclick="saleShelf(' + data.data[i].id  + ')">上架</a></shiro:hasPermission>';
                    }
                    var buName = data.data[i].buName;
                    if(!buName) {
                        buName = '';
                    }
                    $('.todo-content tbody').append(`<tr>
<!--                        <td><a class="user_operate_a" href="javascript:void(0);" onclick="toShelfOrderDetail(\${data.data[i].id}, \${data.data[i].orderType})">\${data.data[i].code}</a></td>-->
                        <td>\${data.data[i].code}</td>
                        <td>\${data.data[i].depotName}</td>
                        <td>\${buName}</td>
                        <td>\${isEmpty(data.data[i].customerName)}</td>
                        <td>\${saleType}</td>
<!--                        <td class="user_operate">\${operate}</td>-->
                        </tr>`)
                }
            } else {
                if (!data.expMessage) {
                    $custom.alert.error(data.expMessage);
                } else {
                    $custom.alert.error(data.message);
                }
            }
            removeOperate();
        });
    }

    //待确认列表
    function getPendingConfirmOrders() {
        $custom.request.ajax($module.params.serverAddrByOrder + "/external/getPendingConfirmOrders.asyn", {}, function (data) {
            if (data.state == 200) {
                $('.todo-content thead').html('')
                $('.todo-content thead').append(`<tr>
	            <th>理货单号</th>
	            <th>仓库</th>
	            <th>事业部</th>
	            <th>理货时间</th>
	            <th>订单类型</th>
<!--	            <th class="user_operate">操作</th>-->
	            </tr>`)
                $('.todo-content tbody').html('')
                let len = data.data.length
                for (let i = 0; i < len; i++) {
                    var operate = '';
                    var orderType = '';
                    if (data.data[i].orderType == '1') {
                        orderType = '采购理货';
                        operate = '<shiro:hasPermission name="tallying_confirm"><a href="javascript:void(0);" onclick="confirm(' + data.data[i].id + ',' + data.data[i].orderType + ')">确认</a></shiro:hasPermission>';
                    } else if (data.data[i].orderType == '2') {
                        orderType = '调拨理货';
                        operate = '<shiro:hasPermission name="transferTallying_confirm"><a href="javascript:void(0);" onclick="confirm(' + data.data[i].id + ',' + data.data[i].orderType + ')">确认</a></shiro:hasPermission>';
                    } else {
                        orderType = '销售退理货';
                        operate = '<shiro:hasPermission name="saleReturnTallyin_confirm"><a href="javascript:void(0);" onclick="confirm(' + data.data[i].id + ',' + data.data[i].orderType + ')">确认</a></shiro:hasPermission>';
                    }
                    var buName = data.data[i].buName;
                    if(!buName) {
                        buName = '';
                    }
                    $('.todo-content tbody').append(`<tr>
<!--                        <td><a class="user_operate_a" href="javascript:void(0);" onclick="toConfirmOrderDetail(\${data.data[i].id}, \${data.data[i].orderType})">\${data.data[i].code}</a></td>-->
                        <td>\${data.data[i].code}</td>
                        <td>\${data.data[i].depotName}</td>
                        <td>\${buName}</td>
                        <td>\${formatDate(data.data[i].tallyingDate,'yyyy-MM-dd')}</td>
                        <td>\${orderType}</td>
<!--                        <td class="user_operate">\${operate}</td>-->
                        </tr>`)
                }
            } else {
                if (!data.expMessage) {
                    $custom.alert.error(data.expMessage);
                } else {
                    $custom.alert.error(data.message);
                }
            }
            removeOperate();
        });
    }

    //待审核列表
    function getPendingCheckOrders() {
        $custom.request.ajax($module.params.serverAddrByOrder + "/external/getPendingCheckOrders.asyn", {}, function (data) {
            if (data.state == 200) {
                $('.todo-content thead').html('')
                $('.todo-content thead').append(`<tr>
	            <th>订单编号</th>
	            <th>仓库</th>
	            <th>事业部</th>
	            <th>创建时间</th>
	            <th>订单类型</th>
<!--	            <th class="user_operate">操作</th>-->
	            </tr>`)
                $('.todo-content tbody').html('')
                let len = data.data.length
                for (let i = 0; i < len; i++) {
                    var operate = '';
                    var orderType = '';
                    if (data.data[i].orderType == '1') {
                        orderType = '采购订单';
                        operate = '<shiro:hasPermission name="purchase_savePurchaseOrder"><a href="javascript:void(0);" onclick="check(' + data.data[i].id + ',' + data.data[i].orderType + ')">审核</a></shiro:hasPermission>';
                    } else if (data.data[i].orderType == '2') {
                        orderType = '销售订单';
                        operate = '<shiro:hasPermission name="sale_addSaleOrder"><a href="javascript:void(0);" onclick="check(' + data.data[i].id + ',' + data.data[i].orderType + ')">审核</a></shiro:hasPermission>';
                    } else if (data.data[i].orderType == '3') {
                        orderType = '调拨订单';
                        operate = '<shiro:hasPermission name="transfer_saveTransferOrder"><a href="javascript:void(0);" onclick="check(' + data.data[i].id + ',' + data.data[i].orderType + ')">审核</a></shiro:hasPermission>';
                    } else {
                        orderType = '销售退订单';
                        operate = '<shiro:hasPermission name="saleReturn_auditSaleReturnOrder"><a href="javascript:void(0);" onclick="check(' + data.data[i].id + ',' + data.data[i].orderType + ')">审核</a></shiro:hasPermission>';
                    }
                    var buName = data.data[i].buName;
                    if(!buName) {
                        buName = '';
                    }
                    $('.todo-content tbody').append(`<tr>
<!--                        <td><a class="user_operate_a" href="javascript:void(0);" onclick="toCheckOrderDetail(\${data.data[i].id}, \${data.data[i].orderType})">\${data.data[i].code}</a></td>-->
                        <td>\${data.data[i].code}</td>
                        <td>\${data.data[i].depotName}</td>
                        <td>\${buName}</td>
                        <td>\${formatDate(data.data[i].createDate,'yyyy-MM-dd')}</td>
                        <td>\${orderType}</td>
<!--                        <td class="user_operate"><a href="index.html">\${operate}</a></td>-->
                        </tr>`)
                }
            } else {
                if (!data.expMessage) {
                    $custom.alert.error(data.expMessage);
                } else {
                    $custom.alert.error(data.message);
                }
            }
            removeOperate();
        });

    }

    //待结转列表
    function getPendingCarryForward() {
        $custom.request.ajax($module.params.serverAddrByInventory + "/external/getPendingCarryForward.asyn", {}, function (data) {
            if (data.state == 200) {
                $('.todo-content thead').html('')
                $('.todo-content thead').append(`<tr>
	            <th>仓库名称</th>
	            <th>结转月份</th>
	            <th>期初时间</th>
	            <th>期末时间</th>
<!--	            <th class="user_operate">操作</th>-->
	            </tr>`)
                $('.todo-content tbody').html('')

                let len = data.data.length
                for (let i = 0; i < len; i++) {
                    var operate = '<shiro:hasPermission name="carryForward_enter"><a href="javascript:void(0);" onclick="settlement(' + data.data[i].depotId + ',\'' + data.data[i].settlementMonth + '\')">进入结转</a></shiro:hasPermission>';
                    $('.todo-content tbody').append(`<tr>
                        <td>\${data.data[i].depotName}</td>
                        <td>\${data.data[i].settlementMonth}</td>
                        <td>\${formatDate(data.data[i].firstDate,'yyyy-MM-dd hh:mm:ss')}</td>
                        <td>\${formatDate(data.data[i].endDate,'yyyy-MM-dd hh:mm:ss')}</td>
<!--                        <td class="user_operate"><a href="index.html">\${operate}</a></td>-->
                        </tr>`)
                }
            } else {
                if (!data.expMessage) {
                    $custom.alert.error(data.expMessage);
                } else {
                    $custom.alert.error(data.message);
                }
            }
            removeOperate();
        });
    }

    //sku预警列表
    function getSkuPriceWarns() {
        $custom.request.ajax($module.params.serverAddrByReport + "/external/getSkuPriceWarns.asyn", {}, function (data) {
            if (data.state == 200) {
                $('.todo-content thead').html('')
                $('.todo-content thead').append(`<tr>
	            <th>事业部</th>
	            <th>商品条码</th>
	            <th>采购单价</th>
	            <th>折算汇率价</th>
	            <th>本位币价</th>
	            <th>浮动率</th>
<!--	            <th class="user_operate">操作</th>-->
	            </tr>`)
                $('.todo-content tbody').html('')

                let len = data.data.length
                for (let i = 0; i < len; i++) {
                    var afterPrice = data.data[i].afterPrice;
                    if (afterPrice) {
                        afterPrice = data.data[i].afterCurrency + data.data[i].afterPrice
                    } else {
                        afterPrice = '';
                    }
                    var settlementPrice = data.data[i].settlementPrice;
                    if (settlementPrice) {
                        settlementPrice = data.data[i].settlementCurrency + data.data[i].settlementPrice
                    } else {
                        settlementPrice = '';
                    }
                    var waveRate = data.data[i].waveRate;
                    if (!waveRate) {
                        waveRate = '';
                    } else {
                        waveRate = waveRate + "%";
                    }
                    if(!afterPrice && !settlementPrice && !waveRate) {
                        settlementPrice = "未维护";
                    }
                    var operate = '<shiro:hasPermission name="settlement_enter"><a href="javascript:void(0);" onclick="toSettlementPage(' + data.data[i].buId + ',\'' + data.data[i].sku + '\')">进入维护</a></shiro:hasPermission>';
                    $('.todo-content tbody').append(`<tr>
                        <td>\${data.data[i].buName}</td>
                        <td>\${data.data[i].sku}</td>
                        <td>\${data.data[i].purchaseCurrency+data.data[i].purchasePrice}</td>
                        <td>\${afterPrice}</td>
                        <td>\${settlementPrice}</td>
                        <td>\${waveRate}</td>
<!--                        <td class="user_operate"><a href="index.html">\${operate}</a></td>-->
                        </tr>`)
                }
            } else {
                if (!data.expMessage) {
                    $custom.alert.error(data.expMessage);
                } else {
                    $custom.alert.error(data.message);
                }
            }
            removeOperate();
        });
    }

    //待录入——跳转到采购订单详情页面
    function toPurchaseOrderDetail(id) {
        $load.a($module.params.loadOrderPageURL+"act=30013&id="+id+"&pageSource=1");
    }

    //待上架——跳转到订单详情页面
    function toShelfOrderDetail(id, orderType) {
        //订单类型 1-销售订单 2-销售出库单
        if(orderType == '1') {
            $load.a($module.params.loadOrderPageURL+"act=40013&id="+id+"&pageSource=1");
        } else {
            $load.a($module.params.loadOrderPageURL+"act=40031&id="+id+"&pageSource=1");
        }
    }

    //待确认——跳转到订单详情页面
    function toConfirmOrderDetail(id, orderType) {
        //订单类型 1-采购 2-调拨 3-销售退
        if(orderType == '1') {
            $load.a($module.params.loadOrderPageURL+"act=30031&id="+id+"&pageSource=1");
        } else if(orderType == '2') {
            $load.a($module.params.loadOrderPageURL+"act=80021&id="+id+"&pageSource=1");
        } else {
            $load.a($module.params.loadOrderPageURL+"act=50041&id="+id+"&pageSource=1");
        }
    }

    //待审核——跳转到订单详情页面
    function toCheckOrderDetail(id, orderType) {
        //订单类型 1-采购 2-销售 3-调拨 4-销售退
        if(orderType == '1') {
            $load.a($module.params.loadOrderPageURL+"act=30013&id="+id+"&pageSource=1");
        } else if(orderType == '2') {
            $load.a($module.params.loadOrderPageURL+"act=40013&id="+id+"&pageSource=1");
        } else if(orderType == '3') {
            $load.a($module.params.loadOrderPageURL+"act=80011&id="+id+"&pageSource=1");
        } else {
            $load.a($module.params.loadOrderPageURL+"act=50011&id="+id+"&pageSource=1");
        }
    }

    //SKU预警——跳转到成本单价页面
    function toSettlementPage(buId, sku) {
        $load.a($module.params.loadReportPageURL + 'act=13001&buId=' + buId + '&barcode=' + sku);
    }


    //录入发票时间
    function receiveInvoice(id)
    {

        var url = $module.params.serverAddrByOrder+"/purchase/getPurchaseItem.asyn" ;
        //获取采购订单商品
        $custom.request.ajax(url, {"id" : id }, function(result){
            if(result.state == '200'){

                $("#itemTbody").empty() ;
                var items = result.data ;

                var html = "" ;
                $(items).each(function( index , item){

                    var amount = item.amount ;
                    amount = amount.toFixed(3) ;
                    var price = item.price ;
                    price = price.toFixed(8) ;

                    html += '<tr>' +
                        '<td>'+item.goodsNo+'</td>'+
                        '<td>'+item.goodsName+'</td>' +
                        '<td>'+item.num+'</td>' +
                        '<td>'+price+'</td>' +
                        '<td><input type="text" name="amount" value="'+amount+'" onblur="changePrice(this)"></td>' +
                        '</tr>' ;

                }) ;

                $("#itemTbody").append(html) ;

            }else{
                $custom.alert.error(result.message);
            }
        });

        $('#id').val(id);
        $('#invoice-modal').modal('show');

    }

    //录入付款
    function alreadyPay(id){
        $('#id').val(id);
        $('#pay-modal').modal('show');
    }

    //上架
    function saleShelf(id){
        $custom.request.ajax($module.params.serverAddrByOrder+"/sale/shelfIsEnd.asyn", {"id":id}, function(data){
            if(data.state==200){
                if(data.data){
                    $custom.alert.error("待上架数量为0，无需进行上架操作，请查看详情。");
                    return;
                }
                $load.a($module.params.loadOrderPageURL+"act=40016&id="+id);
            }else{
                if(!!data.expMessage){
                    $custom.alert.error(data.expMessage);

                }else{
                    $custom.alert.error(data.message);

                }
            }
        });
    }

    //签收
    function receive(id, saleType, poNo){
        $('#id').val(id);
        $("#businessModel2").val(saleType);
        if(poNo=="null"){
            poNo="";
        }
        $('#poNoReceive').val(poNo);
        $('#receive-modal').modal('show');
    }

    //确认
    function confirm(id, type) {
        $custom.alert.warning("确定确认选中对象？",function(){
            if (type == '1') {
                $custom.request.ajax($module.params.serverAddrByOrder+"/tallying/modifyOrderState.asyn",{"ids":id,"state":'010'},function(data){
                    if(data.state==200){
                        if(data.data=='状态必须为待确认'){
                            $custom.alert.error(data.data);
                        }else{
                            $custom.alert.success("确认成功");
                            getPendingConfirmOrders();
                        }
                    }else{
                        $custom.alert.error(data.message);
                    }
                });
            } else if (type == '2') {
                var url=$module.params.serverAddrByOrder+"/transferTallying/tallyConfirmTransfer.asyn";
                $custom.request.ajax(url,{"ids":id,"state":"010"},function(data){
                    if(data.state==200){
                        if(data.data.failCode!=''&&data.data.failCode!=undefined){
                            $custom.alert.success('完成\n<font size="1">理货失败单号：\n'+data.data.failCode+'</font>');
                        }else{
                            $custom.alert.success('完成');
                        }
                        getPendingConfirmOrders();
                    }else{
                        $custom.alert.error(data.message);
                    }
                });
            } else if (type == '3') {
                var url=$module.params.serverAddrByOrder+"/saleReturnTallyin/tallyConfirmTransfer.asyn";
                $custom.request.ajax(url,{"ids":id,"state":"010"},function(data){
                    if(data.state==200){
                        if(data.data.failCode!=''&&data.data.failCode!=undefined){
                            $custom.alert.success('完成\n<font size="1">理货失败单号：\n'+data.data.failCode+'</font>');
                        }else{
                            $custom.alert.success('完成');
                        }
                        getPendingConfirmOrders();
                    }else{
                        $custom.alert.error(data.message);
                    }
                });
            }

        });
    }

    //审核
    function check(id, type) {
        if (type == '1') {
            $load.a($module.params.loadOrderPageURL+"act=30012&id="+id+"&pageSource=1");
        } else if (type == '2') {
            $load.a($module.params.loadOrderPageURL+"act=40012&id="+id+"&pageSource=1");
        } else if (type == '3') {
            $load.a($module.params.loadOrderPageURL+"act=80012&id="+id+"&pageSource=1");
        } else {
            var url = $module.params.serverAddrByOrder+"/saleReturn/auditSaleReturnOrder.asyn";
            $custom.alert.warning("确定审核选中的数据？",function(){
                $custom.request.ajax(url,{"ids":id},function(data){
                    if(data.state==200){
                        $custom.alert.success("审核成功");
                        getPendingCheckOrders();
                    }else{
                        if(!!data.expMessage){
                            $custom.alert.error(data.expMessage);
                        }else{
                            $custom.alert.error(data.message);
                        }
                    }
                });
            });
        }
    }

    //跳转到月结页面
    function settlement(depotId, settlementMonth) {
        $load.a($module.params.loadInventoryPageURL + 'bls=6005&depotId=' + depotId + '&settlementMonth=' + settlementMonth);
    }

    //如果用户是admin， 删除操作按钮
    function removeOperate() {
        if (userType == '1') {
            $('.user_operate').remove();
            $('.user_operate_a').removeAttr('href');
            $('.user_operate_a').removeAttr('onclick');
        }
    }

    //判断是否为空
    function isEmpty(value) {
        if (value) {
            return value
        } else {
            return '';
        }
    }

    //时间格式化
    function formatDate(date, fmt){
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
    }

    /**效期预警**/
    // 仓库数据饼状图
    function drawInventoryWarning(depotId) {
        // 初始化仓库数据
        let storeData = [];
        $custom.request.ajax($module.params.serverAddrByReport + "/external/countInventoryWarning.asyn", {"depotId": depotId}, function (result) {
            $.each(result.data, function (i, e) {
                var temp = {"value": e.surplusNum, "name": e.effectiveIntervalLabel, "selected": true};
                storeData.push(temp);
            });
            drawStorePie(storeData);
        });
    }

    // 仓库数据饼状图函数
    function drawStorePie(data) {
        if (data.length == 0) {
            $('.store-echart').removeAttr("_echarts_instance_");
            $('.store-echart').html('<div style="text-align: center;height: 400px;line-height: 400px; color: #7E8B96;">该仓库下暂无数据</div>');
            return;
        }
        let storeEchart = echarts.init(document.querySelector('.store-echart'));
        let legendData = [
            {
                value: '2/3以上',
                name: '2/3以上效期',
                selected: true
            },
            {
                value: '1/2<x≤2/3',
                name: '2/3效期',
                selected: true
            },
            {
                value: '1/3<x≤1/2',
                name: '1/2效期',
                selected: true
            },
            {
                value: '1/5<x≤1/3',
                name: '1/3效期',
                selected: true
            }, {
                value: '1/10<x≤1/5',
                name: '1/5效期',
                selected: true
            }, {
                value: '0<x≤1/10',
                name: '1/10效期',
                selected: true
            },
            {
                value: '过期品',
                name: '过期品',
                selected: true
            },
            {
                value: '残次品',
                name: '残次品',
                selected: true
            }
        ]
        let option = {
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b}: {c} ({d}%)"
            },
            color: ['#FCD63D', '#DAA3FF', '#75B1FF', '#FF94C1', '#FFCC99', '#FF9900', '#6666CC', '#FF00CC'],
            legend: {
                itemWidth: 14,
                itemHeight: 14,
                width: 300,
                itemGap: 26,
                orient: 'horizontal',
                bottom: 10,
                left: '10%',
                align: 'auto',
                selectedMode: false,
                formatter: function (name) {
                    let target;
                    for (let i = 0; i < legendData.length; i++) {
                        if (legendData[i].name === name) {
                            target = legendData[i].value
                        }
                    }
                    if (target) {
                        let arr = ["{b|" + target + "}"]
                        return arr.join("\n")
                    }
                },
                icon: "rectangle",
                textStyle: {
                    rich: {
                        a: {
                            fontSize: 14,
                            color: "#EA5504",
                            padding: 12
                        },
                        b: {
                            fontSize: 12,
                            height: 12,
                            lineHeight: 12,
                            width: 60,
                            color: "#333"
                        }
                    }
                }

            },
            series: [{
                name: '仓库数据',
                type: 'pie',
                radius: '50%',
                center: ['55%', '40%'],
                data
            }]
        };
        storeEchart.setOption(option)
    }

    //效期预警详情
    function toInventoryWarningList() {
        var depotId = $('#depotId option:selected').val();
        $load.a($module.params.loadInventoryPageURL + 'bls=6006&depotId=' + depotId);
    }

    /**品牌销售 Top 10**/
    //订单类型：1-销售出库单(销售类型 1-购销 2-代销 3-采购执行) 2-电商订单(001-一件代发 002-POP)
    //品牌销售Top10
    function top10Data(type, orderType) {
        if(!type || !orderType) {
            type = $(".brand-active").attr("saleType");
            orderType = $(".brand-active").attr("orderType");
        }
        var deliverDate = $("#deliverDate").val();
        if(orderType == '1') {
            if (type == '2' && deliverDate >= '2020-04') {
                getBillOutDepotSaleNum();
                getBillOutOrderBrandTop10();
            } else {
                if (type == '1') {
                    type = "1,4";
                }
                getSaleOutOrderSaleNum(type);
                getSaleOutOrderBrandTop10(type);
            }
        } else if(orderType == '2'){
            getOrderSaleNum(type);
            getOrderBrandTop10(type);
        }
    }

    //购销/采购执行对应公司主体下所有客户订单的销售总量
    function getSaleOutOrderSaleNum(type) {
        var deliverDate = $("#deliverDate").val();
        let shopNumData = [];
        $custom.request.ajax($module.params.serverAddrByReport + "/external/countSaleOutOrderSaleNum.asyn",
            {"type":type,"deliverDate" : deliverDate}, function (result) {
            $.each(result.data, function (i, e) {
                var temp = {value: e.saleNum, name: e.name, "selected": true};
                shopNumData.push(temp);
            });
            drawShopPie('平台数据', shopNumData);
        });
    }

    //对应公司主体下所有代销客户的销售总量(账单出库单)
    function getBillOutDepotSaleNum() {
        var deliverDate = $("#deliverDate").val();
        let shopNumData = [];
        $custom.request.ajax($module.params.serverAddrByReport + "/external/countBillOutDepotOrderNum.asyn",
            {"deliverDate" : deliverDate}, function (result) {
                $.each(result.data, function (i, e) {
                    var temp = {value: e.saleNum, name: e.name, "selected": true};
                    shopNumData.push(temp);
                });
                drawShopPie('平台数据', shopNumData);
            });
    }

    //购销/采购执行统计所有客户购销模式下销量top 10 的品牌
    function getSaleOutOrderBrandTop10(type) {
        var deliverDate = $("#deliverDate").val();
        let brandNameArr= [];
        let brandNumArr = [];
        $custom.request.ajax($module.params.serverAddrByReport + "/external/getTop10SaleOutOrderBrand.asyn",
            {"type":type,"deliverDate" : deliverDate}, function (result) {
                $.each(result.data, function (i, e) {
                    brandNameArr.push(e.name);
                    brandNumArr.push(e.saleNum);
                });
                drawBrandLine(brandNameArr, brandNumArr);
            });
    }

    //代销统计所有客户购销模式下销量top 10 的品牌
    function getBillOutOrderBrandTop10() {
        var deliverDate = $("#deliverDate").val();
        let brandNameArr= [];
        let brandNumArr = [];
        $custom.request.ajax($module.params.serverAddrByReport + "/external/getBillOutDepotTop10ByBrand.asyn",
            {"deliverDate" : deliverDate}, function (result) {
                $.each(result.data, function (i, e) {
                    brandNameArr.push(e.name);
                    brandNumArr.push(e.saleNum);
                });
                drawBrandLine(brandNameArr, brandNumArr);
            });
    }

    function getOrderSaleNum(type) {
        var deliverDate = $("#deliverDate").val();
        let shopNumData = [];
        $custom.request.ajax($module.params.serverAddrByReport + "/external/countOrderSaleNum.asyn",
            {"type":type,"deliverDate" : deliverDate}, function (result) {
                $.each(result.data, function (i, e) {
                    var temp = {value: e.saleNum, name: e.name, "selected": true};
                    shopNumData.push(temp);
                });
                drawShopPie('店铺销量', shopNumData);
            });
    }

    function getOrderBrandTop10(type) {
        var deliverDate = $("#deliverDate").val();
        let brandNameArr= [];
        let brandNumArr = [];
        $custom.request.ajax($module.params.serverAddrByReport + "/external/getTop10OrderBrand.asyn",
            {"type":type,"deliverDate" : deliverDate}, function (result) {
                $.each(result.data, function (i, e) {
                    brandNameArr.push(e.name);
                    brandNumArr.push(e.saleNum);
                });
                drawBrandLine(brandNameArr, brandNumArr);
            });
    }

    //加载品牌销售饼形图
    function drawShopPie(name, seriesData) {
        if (seriesData.length == 0) {
            $('.shop-echart').removeAttr("_echarts_instance_");
            $('.shop-echart').html('<div style="text-align: center;height: 400px;line-height: 400px; color: #7E8B96;">该日期下暂无数据</div>');
            return;
        }
        let shopEchart = echarts.init(document.querySelector('.shop-echart'))
        let option = {
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            color: ['#946EC6', '#EF5665', '#F4A869', '#FFCCCC', '#FFCC99', '#0099FF', '#9966FF', '#33CC66', '#CC9933', '#FF0099' ],
            stillShowZeroSum: false,
            series: [
                {
                    name: name,
                    type: 'pie',
                    radius: [0, '50%'],
                    center: ['50%', '50%'],
                    data: seriesData,
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(128, 128, 128, 0.5)'
                        }
                    }
                }
            ]
        };
        shopEchart.setOption(option)
    }

    //加载品牌销售柱状图
    function drawBrandLine(brandNames, brandNums) {
        if (brandNames.length == 0) {
            $('.brand-echart').removeAttr("_echarts_instance_");
            $('.brand-echart').html('<div style="text-align: center;height: 400px;line-height: 400px; color: #7E8B96;">该日期下暂无数据</div>');
            return;
        }
        let brandEchart = echarts.init(document.querySelector('.brand-echart'));
        let option = {
            color: ['#3398DB'],
            tooltip: {
                trigger: 'axis',
                axisPointer: { // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: [{
                type: 'category',
                data: brandNames,
                axisTick: {
                    show: false
                },
                axisLabel : {
                    interval:0,
                    rotate:45,
                    formatter: function (value) {
                        var maxlength = 6;
                        if (value.length>maxlength) {
                            return value.substring(0, maxlength-1) + '...';
                        } else{
                            return value;
                        };
                    }
                }
                
            }],
            grid: { // 控制图的大小，调整下面这些值就可以，
                x: 70,
                x2: 100,
                y2: 60,// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
            },
            yAxis: [{
                type: 'value',
                axisTick: {
                    show: false
                },
                name: '销量',
                nameLocation: 'end',
                splitLine: { // gird 区域中的分割线
                    show: true, // 是否显示
                    lineStyle: {
                        color: '#F3F6FA',
                        width: 2,
                    }
                },
            },],
            series: [{
                name: '品牌销量',
                type: 'bar',
                barWidth: '18',
                barGap: '60',
                barBorderRadius: [10, 10],
                data: brandNums,
                itemStyle: {
                    normal: {
                        barBorderRadius: [18, 18, 0, 0],
                        color: new echarts.graphic.LinearGradient(0, 1, 0, 0, [{
                            offset: 0,
                            color: "#428AF5" // 0% 处的颜色
                        }, {
                            offset: 1,
                            color: "#60C5FF" // 100% 处的颜色
                        }], false)
                    }
                },
            }]
        };
        brandEchart.setOption(option);
    }

    function getPreMonth() {
        var date = new Date;
        var year = date.getFullYear();
        var month = date.getMonth();
        if(month == 0){
            year = year -1;
            month = 12;
        }
        if (month < 10) {
            month = "0" + month;
        }
        return year + "-" + month;
    }

    function changePrice(org){
        var tr = $(org).parent().parent() ;

        var amount = tr.find("input[name='amount']").val() ;

        if(!amount || ! /^[0-9]+(.[0-9]+)?$/.test(amount)){
            $custom.alert.error("请输入数值");
            return ;
        }

        amount = parseFloat(amount) ;

        if(amount < 0) {
            $custom.alert.error("请输入大于0的数值");
            return ;
        }

        var num = parseFloat(tr.find("td").eq(2).text()) ;
        var price = parseFloat(tr.find("td").eq(3).text()) ;

        price = amount / num ;
        price = price.toFixed(8) ;

        tr.find("td").eq(3).text(price) ;
        tr.find("input[name='amount']").val(amount.toFixed(3)) ;
    }

    //事业部库存
    // 事业部饼图
    function drawBuInventory() {
        // 初始化仓库数据
        let inventoryData = [];
        let colorList = ['#FFCC99','#946EC6','#FFCC66','#ee9ca7','#91c7ae','#61a0a8','#FCD63D', '#DAA3FF', '#75B1FF', '#FF94C1', '#FF9900'];
        colorList.sort(function(){return Math.random()>0.5?-1:1;});
        $custom.request.ajax($module.params.serverAddrByInventory + "/external/countBuInventory.asyn", {}, function (result) {
            $.each(result.data, function (i, e) {
                let ind = i%10;
                var index = e.buName.indexOf("事业部");
                var buName = e.buName;
                if(index != -1) {
                    buName = buName.substr(0,index);
                }
                var temp = null;
                if (i == 0) {
                    preSelect = buName;
                    preSelectId = e.buId;
                    temp = {"value": e.surplusNum, "name": buName, "id": e.buId, "itemStyle" : { "color": colorList[ind]}, "selected":true};
                } else {
                    temp = {"value": e.surplusNum, "name": buName, "id": e.buId, "itemStyle" : { "color": colorList[ind]}};
                }

                inventoryData.push(temp);
            });
            drawStockPie_1(inventoryData);
        });
    }

    function drawStockPie_1(data){
        if (data.length == 0) {
            $(".inventory-div").css("display","none");
            $('.stock-echart-l').removeAttr("_echarts_instance_");
            $('.order-detail-show').html('<div style="text-align: center;height: 186px;line-height: 186px; color: #7E8B96;">暂无数据</div>');
            return;
        }
        let stockEchart_1 = echarts.init(document.querySelector('.stock-echart-l'));
        let option = {
            backgroundColor:'#fff',
            tooltip: {
                trigger: 'item',
                position: function (point, params, dom, rect, size) {
                    // 鼠标坐标和提示框位置的参考坐标系是：以外层div的左上角那一点为原点，x轴向右，y轴向下
                    // 提示框位置
                    var x = 0; // x坐标位置
                    var y = 0; // y坐标位置

                    // 当前鼠标位置
                    var pointX = point[0];
                    var pointY = point[1];

                    // 提示框大小
                    var boxWidth = size.contentSize[0];
                    var boxHeight = size.contentSize[1];

                    // boxWidth > pointX 说明鼠标左边放不下提示框
                    if (boxWidth > pointX) {
                        x = 5;
                    } else { // 左边放的下
                        x = pointX - boxWidth;
                    }

                    // boxHeight > pointY 说明鼠标上边放不下提示框
                    if (boxHeight > pointY) {
                        y = 40;
                    } else { // 上边放得下
                        /* y = pointY - boxHeight; */
                        y = pointY - boxHeight -20;
                    }

                    return [x, y];
                },

                formatter: "事业部库存总量:{c} <br/> 占比比例: {d}%"
            },
            series: [{
                name: '仓库数据',
                type: 'pie',
                radius: '80%',
                center: ['53%', '50%'],
                label: {
                    position: 'inside'
                },
                itemStyle: {
                    borderColor: '#fff'
                },
                data:data
            }]
        };
        stockEchart_1.setOption(option)
        var selectId = option.series[0].data[0].id;// 根据饼图第一个data的id获取数据
        var selectColor = option.series[0].data[0].itemStyle.color;// 根据饼图第一个data的id获取数据
        drawBuDepotRate(selectId, selectColor);
        stockEchart_1.on('click',function (params) { // 根据饼图点击选中的数据获取右侧仓库数据
            if (preSelect) {
                stockEchart_1.dispatchAction({
                    type: "pieUnSelect",
                    name: preSelect
                });
            }

            var index = params.data.name;
            preSelect = index;
            preSelectId = params.data.id;
            stockEchart_1.dispatchAction({
                type: "pieSelect",
                name: index
            });
            drawBuDepotRate(params.data.id, params.data.itemStyle.color);
            $(".next").attr("disabled",true).css("cursor","pointer");
        })

    }

    //仪表盘
    function drawBuDepotRate(buId, color) {
        // 初始化仓库数据
        stockList = []
        $custom.request.ajax($module.params.serverAddrByInventory + "/external/countBuInventoryRate.asyn", {buId: buId}, function (result) {
            $.each(result.data, function (i, e) {
                var depotName = e.name;
                if (depotName.length > 8) {
                    depotName = depotName.substr(0,8) + "..."
                }
                var tempData = [{"name": depotName, "value": e.num}];
                var temp = {"ratio": e.rate, "data": tempData, "color": color};
                stockList.push(temp);
            });
            len = stockList.length;
            if (len <= 4) {
                stockData = stockList.slice(0,len);
            } else {
                stockData = stockList.slice(0,4);
            }
            drawStockPie(stockData)
            $(".next").css("color",color);
            $(".prev").css("color",color);
        });
    }

    //事业部库存详情
    function toBuInventoryDetail() {
        $load.a($module.params.loadInventoryPageURL + 'bls=6019&buId=' + preSelectId);
    }
</script>