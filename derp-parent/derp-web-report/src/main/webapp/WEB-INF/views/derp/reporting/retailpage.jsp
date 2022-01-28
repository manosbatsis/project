<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<!-- ============================================================== -->
<jsp:include page="/WEB-INF/views/common.jsp" />
<!-- Start right Content here -->
<link rel="stylesheet"
	href='<c:url value="${reportWebhost}/resources/css/reset.css"/>'>
<link rel="stylesheet"
	href='<c:url value="${reportWebhost}/resources/css/retail.css"/>'>
<script
	src="<c:url value="${reportWebhost}/resources/echarts/echarts.common.js"/>"></script>
<script src='/resources/plugins/datatables/jquery.dataTables.min.js'></script>
<script src='/resources/plugins/datatables/dataTables.bootstrap4.min.js'></script>


<!-- ============================================================== -->
<!-- Start content -->
<div class="htmleaf-container">
	<section class="x_container flex">
		<!-- 左边内容区域start -->
		<div class="x_content flex1">
			<!-- 事业群销售达成start -->
			<div class="x_itembox" id="point1">
				<div class="x_titlebox flex_align x_pd_lr x_pd_tb">
					<div class="flex1">
						<span class="x_title">事业群销售达成</span> <span class="x_time">统计月份：<i
							id="TimeVal0"></i></span>
					</div>
					<div class="x_righttitle flex_align">
						<span><i>币种：人民币</i></span>
						<div class="layui-inline flex_align">
							<i>统计月份：</i>
							<div class="layui-input" id="histogramTime" placeholder="请选择统计时间"></div>
							<em class="x_date flex_center"></em>
						</div>
					</div>
				</div>
				<div class="x_tab_content flex">
					<ul class="x_tab flex_align">
						<li class="on">事业群汇总</li>
						<c:forEach items="${buList}" var="buModel">
							<li>${buModel.name}<input type="hidden" name="buId"
								value="${buModel.id}"></li>
						</c:forEach>
					</ul>
					<ul class="x_right_tab flex_align">
						<li>销售汇总</li>
						<li class="on">细分对比</li>
					</ul>
				</div>
				<!-- 柱形图start -->
				<div id="histogram" class="x_chart"
					style="width: 78%; height: 320px"></div>
				<!-- 柱形图end -->
			</div>
			<!-- 事业群销售达成end -->
			<!-- 品牌销量TOP8分析start -->
			<div class="x_itembox" id="point2">
				<div class="x_titlebox flex_align x_pd_lr x_pd_tb">
					<div class="flex1">
						<span class="x_title">品牌销量TOP8分析</span> <span class="x_time">统计时间段：<i
							id="TimeVal1"></i></span>
					</div>
					<div class="x_righttitle flex_align">
						<span><i>币种：人民币</i></span>
						<div class="layui-inline flex_align">
							<i>统计时间：</i>
							<div class="layui-input" id="brandTime" placeholder="请选择统计时间"></div>
							<em class="x_date flex_center"></em>
						</div>
					</div>
				</div>
				<div class="x_tab_content flex">
					<ul class="x_tab flex_align">
						<li class="on">事业群汇总</li>
						<c:forEach items="${buList}" var="buModel">
							<li>${buModel.name}<input type="hidden" name="buId"
								value="${buModel.id}"></li>
						</c:forEach>
					</ul>
				</div>
				<!-- 折线图start -->
				<div id="brand_broken" class="x_chart"
					style="width: 48%; height: 280px; display: inline-block;"></div>
				<!-- 折线图end -->
				<!-- 饼图start -->
				<div id="brand_pie" class="x_chart"
					style="width: 46%; height: 280px; display: inline-block;"></div>
				<!-- 饼图end -->
			</div>
			<!-- 品牌销量TOP8分析end -->
			<!-- 客户销量TOP8分析start -->
			<div class="x_itembox" id="point3">
				<div class="x_titlebox flex_align x_pd_lr x_pd_tb">
					<div class="flex1">
						<span class="x_title">客户销量TOP8分析</span> <span class="x_time">统计时间段：<i
							id="TimeVal2"></i></span>
					</div>
					<div class="x_righttitle flex_align">
						<span><i>币种：人民币</i></span>
						<div class="layui-inline flex_align">
							<i>统计时间：</i>
							<!-- <input type="text" class="layui-input" placeholder="请选择统计时间" readonly="readonly"
                            id="salesTime"> -->
							<div class="layui-input" id="salesTime" placeholder="请选择统计时间"></div>
							<em class="x_date flex_center"></em>
						</div>
					</div>
				</div>
				<div class="x_tab_content flex">
					<ul class="x_tab flex_align">
						<li class="on">事业群汇总</li>
						<c:forEach items="${buList}" var="buModel">
							<li>${buModel.name}<input type="hidden" name="buId"
								value="${buModel.id}"></li>
						</c:forEach>
					</ul>
				</div>
				<div class="x_checktab x_pd_lr flex_align" id="saleCheck">
					<span>渠道类型</span> <span><em class="x_checkicon on"
						onclick="onChange(this,'sales')"></em>统计全部</span> <span><em
						class="x_checkicon" onclick="onChange(this,'sales')"></em>只看ToB渠道</span>
					<span><em class="x_checkicon"
						onclick="onChange(this,'sales')"></em>只看ToC平台</span>
				</div>
				<!-- 折线图start -->
				<div id="sales_broken" class="x_chart"
					style="width: 48%; height: 280px; display: inline-block;"></div>
				<!-- 折线图end -->
				<!-- 饼图start -->
				<div id="sales_pie" class="x_chart"
					style="width: 46%; height: 280px; display: inline-block;"></div>
				<!-- 饼图end -->
			</div>
			<!-- 客户销量TOP8分析分析end -->
			<!-- 库存量分析 start -->
			<div class="x_itembox" id="point4">
				<div class="x_titlebox flex_align x_pd_lr x_pd_tb">
					<div class="flex1">
						<span class="x_title">库存量分析</span> <span class="x_time">统计月份：<i
							id="TimeVal3"></i></span>
					</div>
					<div class="x_righttitle flex_align">
						<span><i>币种：人民币</i></span>
						<div class="layui-inline flex_align">
							<i>统计月份：</i>
							<!-- <input type="text" class="layui-input" id="stockTime" placeholder="请选择统计时间"
                            readonly="readonly"> -->
							<div class="layui-input" id="stockTime" placeholder="请选择统计月份"></div>
							<em class="x_date flex_center"></em>
						</div>
					</div>
				</div>
				<div class="x_tab_content flex">
					<ul class="x_tab flex_align">
						<li class="on">事业群汇总</li>
						<c:forEach items="${buList}" var="buModel">
							<li>${buModel.name}<input type="hidden" name="buId"
								value="${buModel.id}"></li>
						</c:forEach>
					</ul>
				</div>
				<div class="x_checktab x_pd_lr flex_align" , id="stockCheck">
					<span>渠道类型</span> <span><em class="x_checkicon on"
						onclick="onChange(this,'stock')"></em>按品牌</span> <span><em
						class="x_checkicon" onclick="onChange(this,'stock')"></em>按仓库</span> <span><em
						class="x_checkicon" onclick="onChange(this,'stock')"></em>按公司</span>
				</div>
				<!-- 折线图start -->
				<div id="stock_broken" class="x_chart"
					style="width: 54%; height: 280px; display: inline-block;"></div>
				<!-- 折线图end -->
				<!-- 饼图start -->
				<div id="stock_pie" class="x_chart"
					style="width: 40%; height: 280px; display: inline-block;"></div>
				<!-- 饼图end -->
			</div>
			<!-- 库存量分析 end -->
			<!-- 商品在库天数分析 start -->
			<div class="x_itembox" id="point5">
				<div class="x_titlebox flex_align x_pd_lr x_pd_tb">
					<div class="flex1">
						<span class="x_title">商品在库天数分析</span> <span class="x_time">统计月份：<i
							id="TimeVal4"></i></span>
					</div>
					<div class="x_righttitle flex_align">
						<span><i>币种：人民币</i></span>
						<div class="layui-inline flex_align">
							<i>统计月份：</i>
							<div class="layui-input" id="waresTime" placeholder="请选择统计月份"></div>
							<em class="x_date flex_center"></em>
						</div>
					</div>
				</div>
				<div class="x_tab_content flex">
					<ul class="x_tab flex_align">
						<li class="on">事业群汇总</li>
						<c:forEach items="${buList}" var="buModel">
							<li>${buModel.name}<input type="hidden" name="buId"
								value="${buModel.id}"></li>
						</c:forEach>
					</ul>
				</div>
				<!-- 折线图start -->
				<div id="wares_broken" class="x_chart"
					style="width: 48%; height: 280px; display: inline-block;"></div>
				<!-- 折线图end -->
				<!-- 饼图start -->
				<div id="wares_pie" class="x_chart"
					style="width: 46%; height: 280px; display: inline-block;"></div>
				<!-- 饼图end -->
			</div>
			<!-- 商品在库天数分析 end -->
		</div>
		<!-- 左边内容区域end -->
		<!-- 导航start -->
		<div class="x_box">
			<ul class="x_right_menu">
				<li><em class="icon"></em>返回顶部</li>
				<li class="on">事业群销售达成</li>
				<li>品牌销量TOP8分析</li>
				<li>平台/渠道销量TOP8</li>
				<li>库存量分析</li>
				<li>商品在库天数分析</li>
			</ul>
		</div>
		<!-- 导航end -->
	</section>
</div>
<script type="text/javascript">
    /**公用变量 start**/
    var fontSize = 14
    var fontSizeMiddle = 16
    var fontSizeLarge = 18

    // 柱形图大小
    var barWidth = 18
    var barWidthMiddle = 35
    var barWidthLarge = 40

    // 事业群销售对比（销售汇总图表）
    var barGap = '70%' //柱形边距

    // 折线图线宽
    var lineWidth = 2.5

    // 圆的间距
    var borderWidth = 1
    var borderColor = '#fff'

    // 标记的图形
    var symbolSize = 4
    var symbol1 = 'image://'+serverAddr+'/resources/imgs/symbol1.png'//'emptyCircle' 空心 'circle'圆点
    var symbol2 = 'image://'+serverAddr+'/resources/imgs/symbol2.png'//'emptyCircle' 空心 'circle'圆点

    // 图例标签tab
    var itemWidth = 37
    var itemHeight = 11
    var itemGap = 76

    // 饼图大小
    var radius = ['28%', '50%']

    // 柱形图圆角
    var barBorderRadius = [2, 2, 0, 0]
    /**公用变量 end**/
 	
    //事业部销售达成 图表初始化
    var histogram = {}; 
    
 	// 品牌销售 折线图
    var brandBroken = {};
    // 品牌销售 饼状图
    var brandPie = {};
    
 	 //客户销量 折线图
    var salesBroken = {};
    // 客户销量 饼图
    var salesPie = {};

    //库存量分析 柱状折线图
    var stockBroken = {};
    //库存量分析 饼图
    var stockPie = {};

    // 商品在库天数分析 柱状折线图
    var waresLineBroken = {};
    // 商品在库天数分析 饼状图
    var waresPie = {};
    
    $(document).ready(function (){
        $("#TimeVal0").html(getNowMonth());
        $("#TimeVal1").html(getBeforeDate(60) + " ~ " +getNowDate());
        $("#TimeVal2").html(getBeforeDate(60) + " ~ " +getNowDate());
        $("#TimeVal3").html(getNowMonth());
        $("#TimeVal4").html(getNowMonth());

        setTimeout(function(){
        	//事业部销售达成
        	histogram = echarts.init(document.getElementById('histogram'));
        	
        	// 品牌销售 折线图
			 brandBroken = echarts.init(document.getElementById('brand_broken'));			 
		    // 品牌销售 饼状图
			 brandPie = echarts.init(document.getElementById('brand_pie'));
		    
			 //客户销量 折线图
		    salesBroken = echarts.init(document.getElementById('sales_broken'));
		    //客户销量 饼图
		    salesPie = echarts.init(document.getElementById('sales_pie'));

		    //库存量分析 柱状折线图
            stockBroken = echarts.init(document.getElementById('stock_broken'));
            //库存量分析 饼图
            stockPie = echarts.init(document.getElementById('stock_pie'));
		    
		    // 商品在库天数分析 柱状折线图
		    waresLineBroken = echarts.init(document.getElementById('wares_broken'));
		    // 商品在库天数分析 饼状图
		    waresPie = echarts.init(document.getElementById('wares_pie'));
		    
		    //事业部销售达成 汇总
	        initBuStatistic();
	        //事业部销售达成 细分
	        iniBuAchieve();
        	//品牌销量Top
       		 initBrandSaleTop();
	        //客户销量Top
	        initCusSaleTop();
            //库存量分析
            initInventoryAnalysis();
	        //商品在库天数分析
	        initWarehouse();
	        
		},200);


    });

    // 点击菜单导航滚动到固定位置
    $('.x_right_menu li').click(function () {
        $(this).addClass('on').siblings().removeClass('on')
        // 获取索引
        let index = $(this).index()
        // 如果是点击第一个导航则滚动到顶部
        if ($(this).index() == 0) {
            $('body,html').animate({ scrollTop: 0 }, 1000)
        } else {
            // 滚动到某个锚点
            var _scrolltop = $('#point' + index).offset().top;
            $('html, body').animate({
                scrollTop: _scrolltop
            }, 1000);
        }
    })

    // 滚动到固定位置相应菜单栏激活
    $(window).scroll(function () {
        var dom = $('.x_right_menu li')
        for (let index = 1; index < dom.length; index++) {
            // 固定位置
            var offSet = $('#point' + index).offset().top - 200;
            // 当滚动超过了固定点则激活相应的菜单
            if ($(window).scrollTop() >= offSet) {
                $('.x_right_menu li').eq(index).addClass('on').siblings().removeClass('on')
            }
        }
    })

    //同时绑定多个
    lay('.layui-input').each(function (index) {
        laydate.render({
            elem: this,
            trigger: 'click',//可以解决多个实例闪退问题
            // isInitValue: false,//初始值填充（需配合value使用）
            // value:$('#TimeVal'+index).text(),
            range: index > 0 && index < 3 ? '~' : false, //或 range: '~' 来自定义分割字符
            theme: '#0069E0',//主题
            type:(index === 0 || index === 3 || index === 4) ? 'month' : 'date',
            done: function (value, date) {
                $('#TimeVal' + index).text(value);
                onChangeTime(this.elem[0].id,value);
            }
        });
    });

    lay('.x_date').each(function (index) {
        laydate.render({
            elem: this,
            trigger: 'click',//可以解决多个实例闪退问题
            range: index > 0 && index < 3 ? '~' : false, //或 range: '~' 来自定义分割字符
            theme: '#0069E0',//主题
            type: (index === 0 || index === 3 || index === 4) ? 'month' : 'date',
            done: function (value, date) {
                setTimeout(() => {
                    $('.x_date').eq(index).text('')
                }, 0);
                $('.x_date').eq(index).siblings('.layui-input').text(value)
                $('#TimeVal' + index).text(value);
                var id = $('.x_date').eq(index).siblings('.layui-input').attr('id');
                onChangeTime(id,value);
            }
        });
    });

    function onChangeTime(id,dateTime){
        //事业部销售达成 时间控件
        if(id == "histogramTime"){
            var buId = $('#point1 .x_tab .on').find("input[name='buId']").val();
            if(buId == "" || buId == undefined){
                buId = null;
            }
            if(dateTime == "" || dateTime == null || dateTime == undefined) dateTime = getNowMonth();
            busAchieveData(buId,dateTime);
        }
        //品牌销售TOP8 时间控件
        if(id == "brandTime"){
            var buId = $('#point2 .x_tab .on').find("input[name='buId']").val();
            if(buId == "" || buId == undefined){
                buId = null;
            }
            if(dateTime == "" || dateTime == null || dateTime == undefined) dateTime = getNowMonth();
            brandSaleData(buId,dateTime);
        }

        //客户销售TOP8 时间控件
        if(id == "salesTime"){
            var buId = $('#point3 .x_tab .on').find("input[name='buId']").val();
            if(buId == "" || buId == undefined){
                buId = null;
            }
            if(dateTime == "" || dateTime == null || dateTime == undefined) dateTime = getNowMonth();
            var channelType = null;
            var i = $("#saleCheck span .on").parent('.x_checktab span').index();
            if(i == 2){
                channelType = "To B";
            }else if(i == 3){
                channelType = "To C";
            }
            cusSaleTopData(buId, dateTime, channelType);
        }
        //库存量分析 时间控件
        if(id == "stockTime"){
            var buId = $('#point4 .x_tab .on').find("input[name='buId']").val();
            if(buId == "" || buId == undefined){
                buId = null;
            }
            if(dateTime == "" || dateTime == null || dateTime == undefined) dateTime = getNowMonth();
            var i = $("#stockCheck span .on").parent('.x_checktab span').index();
            inventoryAnalysisData(buId,dateTime,i);
        }
        //商品在库天数分析 时间控件
        if(id == "waresTime"){
            var buId = $('#point5 .x_tab .on').find("input[name='buId']").val();
            if(buId == "" || buId == undefined){
                buId = null;
            }
            if(dateTime == "" || dateTime == null || dateTime == undefined) dateTime = getNowMonth();
            inWarehouseData(buId,dateTime);
        }
    }

    // echarts 图表跟随屏幕自适应大小
    window.onresize = function () {
        histogram.resize();
        // histogram1.resize()
        brandBroken.resize();
        brandPie.resize();
        salesBroken.resize()
        salesPie.resize()
        stockBroken.resize()
        stockPie.resize()
        waresLineBroken.resize()
        waresPie.resize()
    }

    //获取当前月份
    function getNowMonth(){
        var date=new Date();
        var year=date.getFullYear();
        var month=date.getMonth()+1;
        month = month > 9 ? month : ("0" + month);
        return year + "-" + month;
    }
    // 获取之前日期
    function getBeforeDate(day){
        var nDate=new Date();
        var lw = new Date(nDate - 1000 * 60 * 60 * 24 * day);//最后一个数字可改，若输入30，则30天的意思
        var lastY = lw.getFullYear();
        var lastM = lw.getMonth()+1;
        var lastD = lw.getDate();
        return lastY+"-"+(lastM<10 ? "0" + lastM : lastM)+"-"+(lastD<10 ? "0"+ lastD : lastD);//之前日期
    }
    //获取当前日期
    function getNowDate(){
        var nDate = new Date();
        var nowY = nDate.getFullYear();
        var nowM = nDate.getMonth()+1;
        var nowD = nDate.getDate();
        return nowY+"-"+(nowM<10 ? "0" + nowM : nowM)+"-"+(nowD<10 ? "0"+ nowD : nowD);//当前日期
    }
    /** 事业群销售达成 start **/   
    var option1 = { };
    //事业部销售达成 汇总
    var option11 = { };
    // 相关点击事件交互
    $('.x_tab li , #point1 .x_right_tab li').click(function () {
        $(this).addClass('on').siblings().removeClass('on');
    })
    // 初始化事业部达成柱状图  细分
    function iniBuAchieve(){
        // 事业销售数据
        // 细分对比
        var histogramLegendData = [{ name: '月度达成', icon: 'image://'+serverAddr+'/resources/imgs/histogram1.png' }, { name: '月度目标', icon: 'image://'+serverAddr+'/resources/imgs/histogram2.png' }, { name: '年度达成', icon: 'image://'+serverAddr+'/resources/imgs/histogram3.png' }, { name: '年度目标', icon: 'image://'+serverAddr+'/resources/imgs/histogram4.png' }];
        var histogramXAxisData = ['购销A', '购销B', '代销', '代发', 'POP'];
        var histogramSeriesData1 = [0, 0, 0, 0,0];
        var histogramSeriesData2 = [0, 0, 0, 0,0];
        var histogramSeriesData3 = [0, 0, 0, 0,0];
        var histogramSeriesData4 = [0, 0, 0, 0,0];
        option1 = {
            tooltip: {//提示框组件。
                trigger: 'axis',//坐标轴触发，主要在柱状图，折线图等会使用类目轴的图表中使用
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow',        // 默认为直线，可选为：'line' | 'shadow'
                    shadowStyle: {//axisPointer.type 为 'shadow' 时有效
                        color: 'rgba(1, 18, 62, .2)'
                    }
                },
                backgroundColor: 'rgba(1, 18, 62, .8)',//提示框浮层的背景颜色。
                textStyle: {
                    color: '#fff',
                },
                borderWidth: 0,//提示器边框颜色
                formatter: function (param) {
                    // 数组排序
                    function createComprisonFunction(propertyName) {
                        return function (object1, object2) {
                            var value1 = object1[propertyName];
                            var value2 = object2[propertyName];
                            if (value1 < value2) {
                                return -1;
                            } else if (value1 > value2) {
                                return 1;
                            } else {
                                return 0;
                            }
                        }
                    }

                    // 小数转百分数
                    function toPercent(point) {
                        var str = Number(point * 100).toFixed(1);
                        str += "%";
                        return str;
                    }
                    let arr2 = param.sort(createComprisonFunction("seriesName")).reverse();
                    var str = ''
                    let val1 = 0;
                    let val2 = 0;
                    if(arr2[1].value <= 0 || arr2[1].value == null || arr2[1].value == undefined){
                        val1 = "-";
                    }else{
                        val1 = toPercent(arr2[0].value / arr2[1].value)
                    }
                    if(arr2[3].value <= 0 || arr2[3].value == null || arr2[3].value == undefined){
                        val2 = "-";
                    }else{
                        val2 = toPercent(arr2[2].value / arr2[3].value);
                    }
                    arr2.forEach((item, index) => {
                        if(item.marker == "" || item.marker == null || item.marker == undefined ){
                            let color= item.color.colorStops[0].color;
                            item.marker = '<span style="display:inline-block;margin-right:4px;border-radius:10px;width:10px;height:10px;background-color:'+color+';"></span>';
                        }
                        str += '<div>' + item.marker + item.seriesName + ':' + item.value + '<div>'
                        if (index === 1) {
                            str += '<div style="color:#AFD6FF;padding:5px 0;">达成率：' + val1 + '</div>'
                            str += '<div style="background:#fff;width:100%;height:1px;margin:6px 0 8px 0;"></div>'
                        }
                        if (index === 3) {
                            str += '<div style="color:#AFD6FF;padding:5px 0 0 0;">达成率：' + val2 + '</div>'
                        }
                    })
                    return str;
                }
            },//提示框组件
            grid: {
                left: '0.5%',
                right: '0',
                bottom: '3%',
                top: '30%',
                containLabel: true//grid 区域是否包含坐标轴的刻度标签。
            },
            // 图表上面tab标签栏
            legend: {
                data: histogramLegendData,
                    // fontSize: fontSize,
                    color: 'rgba(124, 139, 146, 1)'
                ,
                selectedMode: false,//图例选择的模式，控制是否可以通过点击图例改变系列的显示状态
                itemWidth: itemWidth,
                itemHeight: itemHeight,
                left: 0,
                orient: 'horizontal',
                itemGap: itemGap,//图例每项之间的间隔。横向布局时为水平间隔，纵向布局时为纵向间隔。
            },
            xAxis: [
                {
                    type: 'category',
                    axisTick: { //y轴刻度线
                        show: false
                    },
                    axisLine: {//坐标轴轴线相关设置。
                        lineStyle: {//坐标轴线线的颜色。
                            color: '#F3F6FA'
                        }
                    },
                    axisLabel: {//坐标轴名称的文字样式
                        color: '#757B80',
                        // fontSize: fontSize,
                    },
                    data: histogramXAxisData,// x轴标题
                },
                {
                    type: 'category',
                    data: histogramXAxisData,// x轴标题
                    axisLine: {
                        show: false
                    },
                    axisTick: {
                        show: false
                    },
                    axisLabel: {
                        show: false
                    },
                    splitArea: {
                        show: false
                    },
                    splitLine: {
                        show: false
                    },
                }
            ],
            yAxis: {
                name: '单位：万',//坐标轴名称
                nameTextStyle: {//坐标轴名称的文字样式
                    color: 'rgba(117, 123, 128, 1)',
                    // fontSize: fontSize
                },
                axisLine: {
                    show: true,
                    lineStyle: {//坐标轴刻度标签的相关设置。
                        color: '#F3F6FA'
                    }
                },
                splitArea: { show: false },
                splitLine: {//坐标轴在 grid 区域中的分隔线。
                    show: false //是否显示分隔线。默认数值轴显示，类目轴不显示。
                },
                axisLabel: {//坐标轴刻度标签的相关设置。
                    color: '#757B80',
                    // fontSize: fontSize,
                },
            },
            series: [
                {
                    data: histogramSeriesData1,
                    name: histogramLegendData[0].name,
                    type: "bar",
                    // stack: '1',//数据堆叠，同个类目轴上系列配置相同的stack值可以堆叠放置。
                    itemStyle: {
                        show: true,
                        barBorderRadius: [6, 6, 0, 0], //（顺时针左上，右上，右下，左下） 柱条圆角
                        // color: 'rgba(31, 142, 255, 1)' //柱条颜色
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                            offset: 0,
                            color: 'rgba(57, 155, 255, 1)'
                        }, {
                            offset: 1,
                            color: 'rgba(0, 105, 224, 1)'
                        }]),
                    },
                    barWidth: barWidthMiddle,
                    z: 3,
                    xAxisIndex: 1,
                    emphasis: {
                        itemStyle: {
                            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                offset: 0,
                                color: 'rgba(57, 155, 255, 1)'
                            }, {
                                offset: 1,
                                color: 'rgba(0, 105, 224, 1)'
                            }]),
                        }
                    }
                },
                {
                    data: histogramSeriesData2,
                    name: histogramLegendData[1].name,
                    type: "bar",
                    // stack: '1',//数据堆叠，同个类目轴上系列配置相同的stack值可以堆叠放置。
                    itemStyle: {
                        show: true,
                        barBorderRadius: [6, 6, 0, 0], //（顺时针左上，右上，右下，左下） 柱条圆角
                        color: 'rgba(224, 241, 251, 1)' //柱条颜色
                    },
                    barWidth: barWidthMiddle,
                    emphasis: {
                        itemStyle: {
                            color: 'rgba(224, 241, 251, 1)'
                        }
                    },
                },
                {
                    data: histogramSeriesData3,
                    name: histogramLegendData[2].name,
                    type: "bar",
                    // stack: '2',//数据堆叠，同个类目轴上系列配置相同的stack值可以堆叠放置。
                    itemStyle: {
                        show: true,
                        barBorderRadius: [6, 6, 0, 0], //（顺时针左上，右上，右下，左下） 柱条圆角
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{//柱条颜色
                            offset: 0,
                            color: 'rgba(255, 177, 11, 1)'
                        }, {
                            offset: 1,
                            color: 'rgba(255, 114, 11, 1)'
                        }]),
                    },
                    barWidth: barWidthMiddle,
                    xAxisIndex: 1,
                    z: 3,
                    emphasis: {
                        itemStyle: {
                            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{//柱条颜色
                                offset: 0,
                                color: 'rgba(255, 177, 11, 1)'
                            }, {
                                offset: 1,
                                color: 'rgba(255, 114, 11, 1)'
                            }]),
                        }
                    }
                },
                {
                    data: histogramSeriesData4,
                    name: histogramLegendData[3].name,
                    type: "bar",
                    // stack: '2',//数据堆叠，同个类目轴上系列配置相同的stack值可以堆叠放置。
                    itemStyle: {
                        show: true,
                        barBorderRadius: [6, 6, 0, 0], //（顺时针左上，右上，右下，左下） 柱条圆角
                        color: 'rgba(240, 240, 240, 1)' //柱条颜色
                    },
                    barWidth: barWidthMiddle,
                    emphasis: {
                        itemStyle: {
                            color: 'rgba(240, 240, 240, 1)'
                        }
                    },
                },

            ]

        };
        busAchieveData(null,getNowMonth());
    }
    // 获取事业部销售达成数据
    function busAchieveData(buId,month){
        $custom.request.ajax($module.params.serverAddrByReport + "/retailAdmin/getTargetAndAchieve.asyn", {"buId":buId,"month":month},function(result){
            if(result.state == 200){
                if(result.data.monthAchieve != null){
                    var monthAchieve = result.data.monthAchieve;
                    option1.series[0].data = [parseFloat(monthAchieve.saleAmountA/10000).toFixed(2),parseFloat(monthAchieve.saleAmountB/10000).toFixed(2),parseFloat(monthAchieve.agencySaleAmount/10000).toFixed(2),parseFloat(monthAchieve.agencyDeliverAmount/10000).toFixed(2),parseFloat(monthAchieve.popAmount/10000).toFixed(2)];
                    option11.series[0].data = [parseFloat((monthAchieve.saleAmountA + monthAchieve.saleAmountB + monthAchieve.popAmount + monthAchieve.agencySaleAmount + monthAchieve.agencyDeliverAmount)/10000).toFixed(2)];
                }
                if(result.data.monthTarget != null){
                    var monthTarget = result.data.monthTarget;
                    option1.series[1].data = [parseFloat(monthTarget.saleAmountA/10000).toFixed(2),parseFloat(monthTarget.saleAmountB/10000).toFixed(2),parseFloat(monthTarget.agencySaleAmount/10000).toFixed(2),parseFloat(monthTarget.agencyDeliverAmount/10000).toFixed(2),parseFloat(monthTarget.popAmount/10000).toFixed(2)];
                    option11.series[1].data = [parseFloat((monthTarget.saleAmountA + monthTarget.saleAmountB + monthTarget.popAmount + monthTarget.agencySaleAmount + monthTarget.agencyDeliverAmount)/10000).toFixed(2)];
                }
                if(result.data.yearAchieve != null){
                    var yearAchieve = result.data.yearAchieve;
                    option1.series[2].data = [parseFloat(yearAchieve.saleAmountA/10000).toFixed(2),parseFloat(yearAchieve.saleAmountB/10000).toFixed(2),parseFloat(yearAchieve.agencySaleAmount/10000).toFixed(2),parseFloat(yearAchieve.agencyDeliverAmount/10000).toFixed(2),parseFloat(yearAchieve.popAmount/10000).toFixed(2)];
                    option11.series[2].data = [parseFloat((yearAchieve.saleAmountA + yearAchieve.saleAmountB + yearAchieve.popAmount + yearAchieve.agencySaleAmount + yearAchieve.agencyDeliverAmount)/10000).toFixed(2)];
                }
                if(result.data.yearTarget != null){
                    var yearTarget = result.data.yearTarget;
                    option1.series[3].data = [parseFloat(yearTarget.saleAmountA/10000).toFixed(2),parseFloat(yearTarget.saleAmountB/10000).toFixed(2),parseFloat(yearTarget.agencySaleAmount/10000).toFixed(2),parseFloat(yearTarget.agencyDeliverAmount/10000).toFixed(2),parseFloat(yearTarget.popAmount/10000).toFixed(2)];
                    option11.series[3].data = [parseFloat((yearTarget.saleAmountA + yearTarget.saleAmountB + yearTarget.popAmount + yearTarget.agencySaleAmount + yearTarget.agencyDeliverAmount)/10000).toFixed(2)];
                }
                if($('.x_right_tab .on').index() == 0 ){
                    histogram.setOption(option11, true);
                }else {
                    histogram.setOption(option1, true);
                }
            }else{
                if (!result.expMessage) {
                    $custom.alert.error(result.expMessage);
                } else {
                    $custom.alert.error(result.message);
                }
            }
        });

    }
    // 改变图表数据
    // 事业销售达成图表数据变更 事业部
    $('#point1 .x_tab li').click(function () {
    	histogram.clear();//清空当前实例
    	var buId = $(this).find("input[name='buId']").val();
        if(buId == "" || buId == undefined || buId == null){
            buId = null;
        }
        var month = $("#histogramTime").text();
        if(month == "" ||month == null || month == undefined){
            month = getNowMonth();
        }
        busAchieveData(buId,month);
    });
    // 销售汇总、细分对比
    $('#point1 .x_right_tab li').click(function () {
    	histogram.clear();//清空当前实例
    	var buId = $('#point1 .x_tab .on').find("input[name='buId']").val();
        if(buId == "" || buId == undefined || buId == null){
            buId = null;
        }
        var month = $("#histogramTime").text();
        if(month == "" ||month == null || month == undefined){
            month = getNowMonth();
        }
        busAchieveData(buId,month);
    });
    
    //初始化事业部达成柱状图 汇总
    function initBuStatistic(){
        var histogramLegendData = [{ name: '月度达成', icon: 'image://'+serverAddr+'/resources/imgs/histogram1.png' }, { name: '月度目标', icon: 'image://'+serverAddr+'/resources/imgs/histogram2.png' }, { name: '年度达成', icon: 'image://'+serverAddr+'/resources/imgs/histogram3.png' }, { name: '年度目标', icon: 'image://'+serverAddr+'/resources/imgs/histogram4.png' }];
        // 销售总额
        var histogramTotalData = [[0], [0], [0], [0]];

        option11 = {
            // 图表上面tab标签栏
            legend: {
                data: histogramLegendData,
                textStyle: {//图例的公用文本样式。(标签栏)
                    // fontSize: fontSize,
                    color: 'rgba(124, 139, 146, 1)'
                },
                selectedMode: false,//图例选择的模式，控制是否可以通过点击图例改变系列的显示状态
                itemWidth: itemWidth,
                itemHeight: itemHeight,
                left: 0,
                orient: 'horizontal',
                itemGap: itemGap,//图例每项之间的间隔。横向布局时为水平间隔，纵向布局时为纵向间隔。
            },
            grid: {
                left: '0',
                right: '15%',
                bottom: '3%',
                top: '14%',
                containLabel: true//grid 区域是否包含坐标轴的刻度标签。
            },
            xAxis: {
                type: 'value',
                name: '单位/万',
                nameTextStyle: {
                    color: 'rgba(117, 123, 128, 1)'
                },
                axisTick: { //x轴刻度线
                    show: false
                },
                axisLine: {//坐标轴轴线相关设置。
                    show: true,
                    lineStyle: {//坐标轴线线的颜色。
                        color: 'rgba(243, 246, 250, 1)'
                    }
                },
                axisLabel: {//坐标轴名称的文字样式
                    color: 'rgba(117, 123, 128, 1)',
                    // fontSize: fontSize,
                    formatter: '{value}',
                    interval: 0
                },
                splitLine: {//坐标轴在 grid 区域中的分隔线。
                    show: false
                },
            },
            yAxis: [
                {
                    type: 'category',
                    axisTick: { //y轴刻度线
                        show: false
                    },
                    axisLine: {//坐标轴轴线相关设置。
                        lineStyle: {//坐标轴线线的颜色。
                            color: '#F3F6FA'
                        }
                    },
                    axisLabel: {//坐标轴名称的文字样式
                        color: '#757B80',
                        // fontSize: fontSize,
                    },
                    data: ['']// x轴标题
                },
                {
                    type: 'category',
                    data: [''],// x轴标题
                    axisLine: {
                        show: false
                    },
                    axisTick: {
                        show: false
                    },
                    axisLabel: {
                        show: false
                    },
                    splitArea: {
                        show: false
                    },
                    splitLine: {
                        show: false
                    },
                }
            ],
            series: [
                {
                    name: histogramLegendData[0].name,
                    type: 'bar',
                    data: histogramTotalData[0],
                    barWidth: barWidthLarge,
                    itemStyle: {
                        show: true,
                        barBorderRadius: [0, 6, 6, 0], //（顺时针左上，右上，右下，左下） 柱条圆角
                        // color: 'rgba(31, 142, 255, 1)' //柱条颜色（左，下，右，上）
                        color: new echarts.graphic.LinearGradient(1, 0, 0, 0, [{
                            offset: 0,
                            color: 'rgba(57, 155, 255, 1)'
                        }, {
                            offset: 1,
                            color: 'rgba(0, 105, 224, 1)'
                        }]),
                    },
                    z: 3,
                    yAxisIndex: 1,
                    barGap: barGap,
                    label: {
                        show: true,
                        position: 'insideRight',
                        formatter: '{c}万',
                        color: '#fff',
                        // 相对的百分比
                        // position: ['85%', '40%']
                    },
                    emphasis: {
                        itemStyle: {
                            color: new echarts.graphic.LinearGradient(1, 0, 0, 0, [{
                                offset: 0,
                                color: 'rgba(57, 155, 255, 1)'
                            }, {
                                offset: 1,
                                color: 'rgba(0, 105, 224, 1)'
                            }]),
                        }
                    }
                },
                {
                    name: histogramLegendData[1].name,
                    type: 'bar',
                    data: histogramTotalData[1],
                    barWidth: barWidthLarge,
                    itemStyle: {
                        show: true,
                        barBorderRadius: [0, 6, 6, 0], //（顺时针左上，右上，右下，左下） 柱条圆角
                        color: 'rgba(224, 241, 251, 1)' //柱条颜色
                    },
                    barGap: barGap,
                    label: {
                        show: true,
                        color: '#757B80',
                        position: 'right',
                        formatter: '{c}万'
                    },
                    emphasis: {
                        itemStyle: {
                            color: 'rgba(224, 241, 251, 1)'
                        }
                    }
                },
                {
                    name: histogramLegendData[2].name,
                    type: 'bar',
                    data: histogramTotalData[2],
                    barWidth: barWidthLarge,
                    itemStyle: {
                        show: true,
                        barBorderRadius: [0, 6, 6, 0], //（顺时针左上，右上，右下，左下） 柱条圆角
                        color: new echarts.graphic.LinearGradient(1, 0, 0, 0, [{//柱条颜色
                            offset: 0,
                            color: 'rgba(255, 177, 11, 1)'
                        }, {
                            offset: 1,
                            color: 'rgba(255, 114, 11, 1)'
                        }]),
                    },
                    yAxisIndex: 1,
                    z: 3,
                    barGap: barGap,
                    label: {
                        show: true,
                        color: '#fff',
                        position: 'insideRight',
                        formatter: '{c}万'
                    },
                    emphasis: {
                        itemStyle: {
                            color: new echarts.graphic.LinearGradient(1, 0, 0, 0, [{//柱条颜色
                                offset: 0,
                                color: 'rgba(255, 177, 11, 1)'
                            }, {
                                offset: 1,
                                color: 'rgba(255, 114, 11, 1)'
                            }]),
                        }
                    }
                },
                {
                    name: histogramLegendData[3].name,
                    type: 'bar',
                    data: histogramTotalData[3],
                    barWidth: barWidthLarge,
                    itemStyle: {
                        show: true,
                        barBorderRadius: [0, 6, 6, 0], //（顺时针左上，右上，右下，左下） 柱条圆角
                        color: 'rgba(240, 240, 240, 1)' //柱条颜色
                    },
                    barGap: barGap,
                    label: {
                        show: true,
                        color: '#757B80',
                        position: 'right',
                        formatter: '{c}万'
                    },
                    emphasis: {
                        itemStyle: {
                            color: 'rgba(240, 240, 240, 1)'
                        }
                    }
                }
            ]
        };
    }

    /* $('#point1 .x_right_tab li').click(function () {
        $(this).addClass('on').siblings().removeClass('on')
        // 1显示细分对比 0销售汇总
        if ($(this).index() == 0) {
            histogram.setOption(option11, true);
        } else {
            histogram.setOption(option1, true);
        }
    }) */
    /** 事业群销售达成 end **/


    /** 品牌销量TOP8 start **/
    // 品牌销售 折线图
    var option2 = {};
    // 品牌销售 饼状图
    var option3 = {};
    //初始化品牌销售
    function initBrandSaleTop(){
        //折线图
        option2 = {
            tooltip: {
                trigger: 'axis',//坐标轴触发，主要在柱状图，折线图等会使用类目轴的图表中使用
                backgroundColor: 'rgba(1, 18, 62, .8)',//提示框浮层的背景颜色。
                textStyle: {
                    color: '#fff',
                },
                borderWidth: 0,//提示器边框颜色
                formatter: function (param) {
                    var str = ''
                    param.forEach((item, index) => {
                        str += '<div style="color:#AFD6FF;">' + item.name + '<div>'
                        str += '<div style="color:#fff;">' + '销量:' + parseFloat(item.value).toFixed(1) + '万<div>'
                    })
                    return str;
                }
            },
            grid: {
                left: '0',
                right: '0',
                bottom: '3%',
                top: '12%',
                containLabel: true,
            },
            xAxis: [{
                type: 'category',
                boundaryGap: true,
                axisLine: {
                    show: true,
                    lineStyle: {//坐标轴刻度标签的相关设置。
                        color: '#F3F6FA'
                    }
                },
                axisLabel: {//坐标轴刻度标签的相关设置。
                    color: '#757B80',
                    // fontSize: fontSize,
                    interval: 0,//坐标轴刻度标签的显示间隔，在类目轴中有效 0 强制显示所有标签。
                    rotate: 30 //刻度标签旋转的角度
                },
                splitLine: {//坐标轴在 grid 区域中的分隔线。(竖线)
                    show: true,
                    lineStyle: {
                        color: 'rgba(243, 246, 250, 1)'
                    }
                },
                data: [],
            }],
            yAxis: [{
                name: '单位：万',//坐标轴名称
                nameTextStyle: {//坐标轴名称的文字样式
                    color: 'rgba(117, 123, 128, 1)',
                    // fontSize: fontSize
                },
                type: 'value',
                // nameGap:20,//坐标轴名称与轴线之间的距离。
                splitNumber: 7,
                splitLine: {////坐标轴在 grid 区域中的分隔线 (横线)。
                    show: true,
                    lineStyle: {
                        color: 'rgba(243, 246, 250, 1)'
                    }
                },
                axisLine: {//坐标轴轴线相关设置（竖起来的那根先）
                    show: true,
                    lineStyle: {
                        color: '#F3F6FA'
                    }
                },
                axisLabel: {//坐标轴刻度标签的相关设置。
                    color: '#757B80'
                }
            }],
            series: [
                {
                    // name: '品牌销量TOP8分析',
                    type: 'line',
                    smooth: true, //是否平滑曲线显示
                    symbol: symbol1,//标记的图形
                    symbolSize: symbolSize,//圆点大小
                    lineStyle: {
                        color: "#247BFF", // 线条颜色
                        width: lineWidth//线宽。
                    },
                    showSymbol: false,//是否显示 symbol, 如果 false 则只有在 tooltip hover 的时候显示。
                    itemStyle: {//折线拐点标志的样式。

                            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                offset: 0,
                                color: 'rgba(0, 105, 224, 1)'
                            },
                                {
                                    offset: 1,
                                    color: 'rgba(57, 155, 255, 1)'
                                }
                            ], false),
                            barBorderRadius: 10

                    },
                    tooltip: {//是否显示提示框组件
                        show: true
                    },
                    areaStyle: { //区域填充样式

                            //线性渐变，前4个参数分别是x0,y0,x2,y2(范围0~1);相当于图形包围盒中的百分比。如果最后一个参数是‘true’，则该四个值是绝对像素位置。
                            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                offset: 0,
                                color: 'rgba(0, 105, 224, 1)'
                            },
                                {
                                    offset: 0.6,
                                    color: 'rgba(57, 155, 255, 0)'
                                }
                            ], false),
                            shadowOffsetY: 20,
                            shadowColor: 'rgba(36, 123, 255, .21)', //阴影颜色
                            shadowBlur: 20, //shadowBlur设图形阴影的模糊大小。配合shadowColor,shadowOffsetX/Y, 设置图形的阴影效果。
                            opacity: 0.21

                    },
                    data: []
                }
            ]
        };

        //饼状图
        var brandPieLegendData = [];
        var brandPieValue = [];
        option3 = {
            title: {
                text: '品牌销量占比',
                textStyle: {
                    color: '#2D353E',
                    fontSize: fontSizeMiddle
                },
                left: 'center',
            },
            tooltip: {//提示框组件
                trigger: 'item',//触发类型
                formatter: function(data){
                    return data.name+ " : " + data.value + " ("+data.percent.toFixed(1)+"%)";
                },
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow',        // 默认为直线，可选为：'line' | 'shadow'
                    shadowStyle: {//axisPointer.type 为 'shadow' 时有效
                        color: 'rgba(1, 18, 62, .2)'
                    }
                },
                backgroundColor: 'rgba(1, 18, 62, .8)',//提示框浮层的背景颜色。
                textStyle: {
                    color: '#fff',
                },
                borderWidth: 0,//提示器边框颜色
            },
            // legend: {
            //     bottom: 0,
            //     // orient: 'vertical',//图例列表的布局朝向。
            //     data: brandPieLegendData,
            //     textStyle: {//图例的公用文本样式。(标签栏)
            //         fontSize: fontSize,
            //         color: '#7C8B92'
            //     },
            //     selectedMode: false,//图例选择的模式，控制是否可以通过点击图例改变系列的显示状态
            //     itemWidth:32,//图例标记的图形宽度。
            //     itemHeight:8//图例标记的图形高度。
            // },
            series: [
                {
                    type: 'pie',
                    center: ['50%', '50%'],
                    radius: radius,
                    roseType: 'radius',
                    label: {//饼图图形上的文本标签
                        // fontSize: fontSize,
                        // formatter: '{b}: {d}%'
                        color: '#7C8B92',  // 改变标示文字的颜色
                        formatter: function(data){
                            return data.name+ " : "+data.percent.toFixed(1)+"% ";
                        }

                    },
                    labelLine: {//标签的视觉引导线样式
                        smooth: true,
                        // length:8,
                        length2: 8
                    },
                    itemStyle: {
                        borderWidth: borderWidth,
                        borderColor: borderColor,
                    },
                    data: [
                        {
                            value: brandPieValue[0],
                            name: brandPieLegendData[0],
                            itemStyle: {
                                color: {
                                    type: 'linear',
                                    x: 0,
                                    y: 0,
                                    x2: 0,
                                    y2: 1,
                                    colorStops: [{
                                        offset: 0, color: '#FF720B' // 0% 处的颜色
                                    }, {
                                        offset: 1, color: '#FFB10B' // 100% 处的颜色
                                    }],
                                    global: false // 缺省为 false
                                }
                            },
                            label: {
                                rich: {
                                    hr: {
                                        borderRadius: 3,
                                        width: 3,
                                        height: 3,
                                        borderWidth: 1,
                                        borderColor: '#FF720B',
                                        padding: [3, 3, 0, -12]
                                    },
                                    a: {
                                        padding: [-13, 15, -20, 5]
                                    }
                                }
                            }
                        },
                        {
                            value: brandPieValue[1],
                            name: brandPieLegendData[1],
                            itemStyle: {
                                color: '#1B82EB'
                            },
                            label: {
                                rich: {
                                    hr: {
                                        borderRadius: 3,
                                        width: 3,
                                        height: 3,
                                        borderWidth: 1,
                                        borderColor: '#1B82EB',
                                        padding: [3, 3, 0, -12]
                                    },
                                    a: {
                                        padding: [-13, 15, -20, 5]
                                    }
                                }
                            }
                        },
                        {
                            value: brandPieValue[2],
                            name: brandPieLegendData[2],
                            itemStyle: {
                                color: '#55E2E6'
                            },
                            label: {
                                rich: {
                                    hr: {
                                        borderRadius: 3,
                                        width: 3,
                                        height: 3,
                                        borderWidth: 1,
                                        borderColor: '#55E2E6',
                                        padding: [3, 3, 0, -12]
                                    },
                                    a: {
                                        padding: [-13, 15, -20, 5]
                                    }
                                }
                            }
                        },
                        {
                            value: brandPieValue[3],
                            name: brandPieLegendData[3],
                            itemStyle: {
                                color: '#5DADFF'
                            },
                            label: {
                                rich: {
                                    hr: {
                                        borderRadius: 3,
                                        width: 3,
                                        height: 3,
                                        borderWidth: 1,
                                        borderColor: '#5DADFF',
                                        padding: [3, 3, 0, -12]
                                    },
                                    a: {
                                        padding: [-13, 15, -20, 5]
                                    }
                                }
                            }
                        },
                        {
                            value: brandPieValue[4],
                            name: brandPieLegendData[4],
                            itemStyle: {
                                color: '#B39FFF'
                            },
                            label: {
                                rich: {
                                    hr: {
                                        borderRadius: 3,
                                        width: 3,
                                        height: 3,
                                        borderWidth: 1,
                                        borderColor: '#B39FFF',
                                        padding: [3, 3, 0, -12]
                                    },
                                    a: {
                                        padding: [-13, 15, -20, 5]
                                    }
                                }
                            }
                        },
                        {
                            value: brandPieValue[5],
                            name: brandPieLegendData[5],
                            itemStyle: {
                                color: '#D5CAFF'
                            },
                            label: {
                                rich: {
                                    hr: {
                                        borderRadius: 3,
                                        width: 3,
                                        height: 3,
                                        borderWidth: 1,
                                        borderColor: '#D5CAFF',
                                        padding: [3, 3, 0, -12]
                                    },
                                    a: {
                                        padding: [-13, 15, -20, 5]
                                    }
                                }
                            }
                        },
                        {
                            value: brandPieValue[6],
                            name: brandPieLegendData[6],
                            itemStyle: {
                                color: '#CCD5D9'
                            },
                            label: {
                                rich: {
                                    hr: {
                                        borderRadius: 3,
                                        width: 3,
                                        height: 3,
                                        borderWidth: 1,
                                        borderColor: '#CCD5D9',
                                        padding: [3, 3, 0, -12]
                                    },
                                    a: {
                                        padding: [-13, 15, -20, 5]
                                    }
                                }
                            }
                        },
                        {
                            value: brandPieValue[7],
                            name: brandPieLegendData[7],
                            itemStyle: {
                                color: '#79F7FB'
                            },
                            label: {
                                rich: {
                                    hr: {
                                        borderRadius: 3,
                                        width: 3,
                                        height: 3,
                                        borderWidth: 1,
                                        borderColor: '#79F7FB',
                                        padding: [3, 3, 0, -12]
                                    },
                                    a: {
                                        padding: [-13, 15, -20, 5]
                                    }
                                }
                            }
                        },
                        {
                            value: brandPieValue[8],
                            name: brandPieLegendData[8],
                            itemStyle: {
                                color: '#79F7FB'
                            },
                            label: {
                                rich: {
                                    hr: {
                                        borderRadius: 3,
                                        width: 3,
                                        height: 3,
                                        borderWidth: 1,
                                        borderColor: '#79F7FB',
                                        padding: [3, 3, 0, -12]
                                    },
                                    a: {
                                        padding: [-13, 15, -20, 5]
                                    }
                                }
                            }
                        }
                    ]
                }
            ]
        };

        var brandTime = $("#brandTime").text();
        if(brandTime == "" || brandTime == null || brandTime == undefined){
            brandTime = getBeforeDate(60) + " ~ " + getNowDate();
        }
        brandSaleData(null ,brandTime);
    }
    //品牌销量 数据获取
    function brandSaleData(buId,month){
        $custom.request.ajax($module.params.serverAddrByReport + "/retailAdmin/getBrandSaleTop.asyn", {"buId":buId,"month":month},function(result){
            if(result.state == 200){
                var xAxisData = [];
                var seriesData = [];

                // 清空option的值
                var dataSize = option3.series[0].data.length;
                for(var j=0;j < dataSize;j++){
                    option3.series[0].data[j].name = null;
                    option3.series[0].data[j].value = null;
                }
                if(result.data != null){
                    for(var i=0; i < result.data.length;i++){
                        xAxisData.push(result.data[i].brandParent);
                        seriesData.push(parseFloat(result.data[i].cnyAmount/10000).toFixed(1));

                        option3.series[0].data[i].name = result.data[i].brandParent;
                        option3.series[0].data[i].value = parseFloat(result.data[i].cnyAmount/10000).toFixed(1);
                    }
                }
                option2.xAxis[0].data = xAxisData;
                option2.series[0].data = seriesData;

                brandBroken.setOption(option2, true);
                brandPie.setOption(option3,true);
            }
        });
    }

    // 品牌销量数据图表数据变更
    $('#point2 .x_tab li').click(function () {
        // 折线图
        brandBroken.clear();//清空当前实例
        // 饼图
        brandPie.clear();//清空当前实例

        var buId = $(this).find("input[name='buId']").val();
        if(buId == "" || buId == undefined || buId == null){
            buId = null;
        }
        var brandTime = $("#TimeVal1").text();
        if(brandTime == "" || brandTime == null || brandTime == undefined){
            branTime = getBeforeDate(60) + " ~ " + getNowDate();
        }
        brandSaleData(buId ,brandTime);
    });
    /** 品牌销量TOP8 end **/


    /** 客户销量TOP8 start **/
    //折线图渠道类型
    var option4 = { };
    // 饼图
    var option5 = { };
    function initCusSaleTop(){
        // 折线图
        var salesBrokenXAxisData = [];
        var salesBrokenSeriesData = [];
        //饼图
        var salesPieLegendData = [];
        var salesPieValue = [];

        option4 = {
            tooltip: {
                trigger: 'axis',//坐标轴触发，主要在柱状图，折线图等会使用类目轴的图表中使用
                backgroundColor: 'rgba(1, 18, 62, .8)',//提示框浮层的背景颜色。
                textStyle: {
                    color: '#fff',
                },
                borderWidth: 0,//提示器边框颜色
                formatter: function (param) {
                    var str = ''
                    param.forEach((item, index) => {
                        str += '<div style="color:#AFD6FF;">' + item.name + '<div>'
                        str += '<div style="color:#fff;">' + '销量:' + item.value + '万<div>'
                    })
                    return str;
                }
            },
            grid: {
                left: '0',
                right: '0',
                bottom: '3%',
                top: '12%',
                containLabel: true,
            },
            xAxis: [{
                type: 'category',
                boundaryGap: true,
                axisLine: {
                    show: true,
                    lineStyle: {//坐标轴刻度标签的相关设置。
                        color: '#F3F6FA'
                    }
                },
                axisLabel: {//坐标轴刻度标签的相关设置。
                    color: '#757B80',
                    // fontSize: fontSize,
                    interval: 0,//坐标轴刻度标签的显示间隔，在类目轴中有效 0 强制显示所有标签。
                    rotate: 30 //刻度标签旋转的角度
                },
                splitLine: {//坐标轴在 grid 区域中的分隔线。(竖线)
                    show: true,
                    lineStyle: {
                        color: 'rgba(243, 246, 250, 1)'
                    }
                },
                data: salesBrokenXAxisData
            }],
            yAxis: [{
                name: '单位：万',//坐标轴名称
                nameTextStyle: {//坐标轴名称的文字样式
                    color: 'rgba(117, 123, 128, 1)',
                    // fontSize: fontSize
                },
                type: 'value',
                min: 0,
                // max: 140,
                splitNumber: 7,
                splitLine: {////坐标轴在 grid 区域中的分隔线 (横线)。
                    show: true,
                    lineStyle: {
                        color: 'rgba(243, 246, 250, 1)'
                    }
                },
                axisLine: {//坐标轴轴线相关设置（竖起来的那根先）
                    show: true,
                    lineStyle: {
                        color: '#F3F6FA'
                    }
                },
                axisLabel: {//坐标轴刻度标签的相关设置。
                    color: '#757B80',
                    // fontSize: fontSize,
                },
                // axisPointer: {//坐标轴指示器配置项。
                //     show: true,
                //     label: {
                //         color: '#fff',
                //         backgroundColor: 'rgba(0,0,0,0.6)'
                //     },
                //     snap: true//坐标轴指示器是否自动吸附到点上
                // },
            }],
            series: [
                {
                    // name: '品牌销量TOP8分析',
                    type: 'line',
                    smooth: true, //是否平滑曲线显示
                    symbol: symbol2,
                    symbolSize: symbolSize,//圆点大小
                    lineStyle: {
                        color: "#B39FFF", // 线条颜色
                        width: lineWidth,//线宽。

                    },
                    // label: {//图形上的文本标签，可用于说明图形的一些数据信息，比如值，名称等
                    //     show: true,
                    //     position: 'top',
                    //     textStyle: {
                    //         color: '#fff',
                    //     }
                    // },
                    showSymbol: false,//是否显示 symbol, 如果 false 则只有在 tooltip hover 的时候显示。
                    itemStyle: {//折线拐点标志的样式。
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                            offset: 0,
                            color: 'rgba(146, 93, 255, 1)'
                        },
                            {
                                offset: 1,
                                color: 'rgba(179, 159, 255, 1)'
                            }
                        ], false),

                    },
                    tooltip: {//是否显示提示框组件
                        show: true
                    },
                    areaStyle: { //区域填充样式
                        //线性渐变，前4个参数分别是x0,y0,x2,y2(范围0~1);相当于图形包围盒中的百分比。如果最后一个参数是‘true’，则该四个值是绝对像素位置。
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                            offset: 0,
                            color: 'rgba(146, 93, 255, 1)'
                        },
                            {
                                offset: 0.6,
                                color: 'rgba(179, 159, 255, 0)'
                            }
                        ], false),
                        shadowOffsetY: 20,
                        shadowColor: 'rgba(36, 123, 255, .21)', //阴影颜色
                        shadowBlur: 20, //shadowBlur设图形阴影的模糊大小。配合shadowColor,shadowOffsetX/Y, 设置图形的阴影效果。
                        opacity: 0.21
                    },
                    data: salesBrokenSeriesData
                }
            ]
        };

        option5 = {
            title: {
                text: '渠道销量占比',
                textStyle: {
                    color: '#2D353E',
                    fontSize: fontSizeMiddle
                },
                left: 'center'
            },
            tooltip: {//提示框组件
                trigger: 'item',//触发类型
                formatter: function(data){
                    return data.name+ " : " + data.value + " ("+data.percent.toFixed(1)+"%)";
                },
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow',        // 默认为直线，可选为：'line' | 'shadow'
                    shadowStyle: {//axisPointer.type 为 'shadow' 时有效
                        color: 'rgba(1, 18, 62, .2)'
                    }
                },
                backgroundColor: 'rgba(1, 18, 62, .8)',//提示框浮层的背景颜色。
                textStyle: {
                    color: '#fff',
                },
                borderWidth: 0,//提示器边框颜色
            },
            // legend: {
            //     bottom: 0,
            //     data: salesPieLegendData,
            //     textStyle: {//图例的公用文本样式。(标签栏)
            //         // fontSize: fontSize,
            //         color: '#7C8B92'
            //     },
            //     selectedMode: false,//图例选择的模式，控制是否可以通过点击图例改变系列的显示状态
            //     itemWidth:32,//图例标记的图形宽度。
            //     itemHeight:8//图例标记的图形高度。
            // },
            series: [
                {
                    type: 'pie',
                    center: ['50%', '50%'],
                    radius: radius,
                    roseType: 'radius',
                    label: {//饼图图形上的文本标签
                        // fontSize: fontSize,
                        color: '#7C8B92',
                        // formatter: '{b}: {d}%',
                        // formatter: '{a|{b}：{d}%}\n{hr|}',
                        formatter: function(data){
                            return data.name+ " : "+data.percent.toFixed(1)+"% ";
                        },
                    },
                    labelLine: {//标签的视觉引导线样式
                        smooth: true,
                        length2: 8
                    },
                    itemStyle: {
                        borderWidth: borderWidth,
                        borderColor: borderColor,
                    },
                    data: [
                        {
                            value: salesPieValue[0],
                            name: salesPieLegendData[0],
                            itemStyle: {
                                color: {
                                    type: 'linear',
                                    x: 0,
                                    y: 0,
                                    x2: 0,
                                    y2: 1,
                                    colorStops: [{
                                        offset: 0, color: '#FF720B' // 0% 处的颜色
                                    }, {
                                        offset: 1, color: '#FFB10B' // 100% 处的颜色
                                    }],
                                    global: false // 缺省为 false
                                }
                            },
                            label: {
                                rich: {
                                    hr: {
                                        borderRadius: 3,
                                        width: 3,
                                        height: 3,
                                        borderWidth: 1,
                                        borderColor: '#FF720B',
                                        padding: [3, 3, 0, -12]
                                    },
                                    a: {
                                        padding: [-13, 15, -20, 5]
                                    }
                                }
                            }
                        },
                        {
                            value: salesPieValue[1],
                            name: salesPieLegendData[1],
                            itemStyle: {
                                color: '#1B82EB'
                            },
                            label: {
                                rich: {
                                    hr: {
                                        borderRadius: 3,
                                        width: 3,
                                        height: 3,
                                        borderWidth: 1,
                                        borderColor: '#1B82EB',
                                        padding: [3, 3, 0, -12]
                                    },
                                    a: {
                                        padding: [-13, 15, -20, 5]
                                    }
                                }
                            }
                        },
                        {
                            value: salesPieValue[2],
                            name: salesPieLegendData[2],
                            itemStyle: {
                                color: '#55E2E6'
                            },
                            label: {
                                rich: {
                                    hr: {
                                        borderRadius: 3,
                                        width: 3,
                                        height: 3,
                                        borderWidth: 1,
                                        borderColor: '#55E2E6',
                                        padding: [3, 3, 0, -12]
                                    },
                                    a: {
                                        padding: [-13, 15, -20, 5]
                                    }
                                }
                            }
                        },
                        {
                            value: salesPieValue[3],
                            name: salesPieLegendData[3],
                            itemStyle: {
                                color: '#5DADFF'
                            },
                            label: {
                                rich: {
                                    hr: {
                                        borderRadius: 3,
                                        width: 3,
                                        height: 3,
                                        borderWidth: 1,
                                        borderColor: '#5DADFF',
                                        padding: [3, 3, 0, -12]
                                    },
                                    a: {
                                        padding: [-13, 15, -20, 5]
                                    }
                                }
                            }
                        },
                        {
                            value: salesPieValue[4],
                            name: salesPieLegendData[4],
                            itemStyle: {
                                color: '#B39FFF'
                            },
                            label: {
                                rich: {
                                    hr: {
                                        borderRadius: 3,
                                        width: 3,
                                        height: 3,
                                        borderWidth: 1,
                                        borderColor: '#B39FFF',
                                        padding: [3, 3, 0, -12]
                                    },
                                    a: {
                                        padding: [-13, 15, -20, 5]
                                    }
                                }
                            }
                        },
                        {
                            value: salesPieValue[5],
                            name: salesPieLegendData[5],
                            itemStyle: {
                                color: '#D5CAFF'
                            },
                            label: {
                                rich: {
                                    hr: {
                                        borderRadius: 3,
                                        width: 3,
                                        height: 3,
                                        borderWidth: 1,
                                        borderColor: '#D5CAFF',
                                        padding: [3, 3, 0, -12]
                                    },
                                    a: {
                                        padding: [-13, 15, -20, 5]
                                    }
                                }
                            }
                        },
                        {
                            value: salesPieValue[6],
                            name: salesPieLegendData[6],
                            itemStyle: {
                                color: '#CCD5D9'
                            },
                            label: {
                                rich: {
                                    hr: {
                                        borderRadius: 3,
                                        width: 3,
                                        height: 3,
                                        borderWidth: 1,
                                        borderColor: '#CCD5D9',
                                        padding: [3, 3, 0, -12]
                                    },
                                    a: {
                                        padding: [-13, 15, -20, 5]
                                    }
                                }
                            }
                        },
                        {
                            value: salesPieValue[7],
                            name: salesPieLegendData[7],
                            itemStyle: {
                                color: '#79F7FB'
                            },
                            label: {
                                rich: {
                                    hr: {
                                        borderRadius: 3,
                                        width: 3,
                                        height: 3,
                                        borderWidth: 1,
                                        borderColor: '#79F7FB',
                                        padding: [3, 3, 0, -12]
                                    },
                                    a: {
                                        padding: [-13, 15, -20, 5]
                                    }
                                }
                            }
                        },
                        {
                            value: salesPieValue[8],
                            name: salesPieLegendData[8],
                            itemStyle: {
                                color: '#79F7FB'
                            },
                            label: {
                                rich: {
                                    hr: {
                                        borderRadius: 3,
                                        width: 3,
                                        height: 3,
                                        borderWidth: 1,
                                        borderColor: '#79F7FB',
                                        padding: [3, 3, 0, -12]
                                    },
                                    a: {
                                        padding: [-13, 15, -20, 5]
                                    }
                                }
                            }
                        }
                    ]
                }
            ]
        };

        var salesTime = $("#salesTime").text();
        if(salesTime == "" || salesTime == null || salesTime == undefined){
            salesTime = getBeforeDate(60) + " ~ " + getNowDate();
        }
        cusSaleTopData(null, salesTime, null);
    }

    function cusSaleTopData(buId, month, channelType){
        $custom.request.ajax($module.params.serverAddrByReport + "/retailAdmin/getCusSaleTop.asyn", {"buId":buId,"month":month,"channelType":channelType},function(result){
            if(result.state == 200){
                var xAxisData = [];
                var seriesData = [];
                // 清空option的值
                var dataSize = option5.series[0].data.length;
                for(var j=0;j < dataSize;j++){
                    option5.series[0].data[j].name = null;
                    option5.series[0].data[j].value = null;
                }
                if(result.data != null){
                    for(var i=0; i < result.data.length;i++){
						var name = result.data[i].customerName == "" ? result.data[i].customerId : result.data[i].customerName;
						xAxisData.push(name);
                        seriesData.push(parseFloat(result.data[i].cnyAmount/10000).toFixed(1));

                        option5.series[0].data[i].name = name;
                        option5.series[0].data[i].value = parseFloat(result.data[i].cnyAmount/10000).toFixed(1);
                    }
                }
                option4.xAxis[0].data = xAxisData;
                option4.series[0].data = seriesData;

                salesBroken.setOption(option4, true);
                salesPie.setOption(option5,true);
            }
        });
    }

    // 客户销量TOP8分析数据图表数据变更
    $('#point3 .x_tab li').click(function () {
        // 折线图
        salesBroken.clear();//清空当前实例
        // 饼图
        salesPie.clear();//清空当前实例

        var buId = $(this).find("input[name='buId']").val();
        if(buId == "" || buId == undefined || buId == null){
            buId = null;
        }
        var salesTime = $("#TimeVal2").text();
        if(salesTime == "" || salesTime == null || salesTime == undefined){
            salesTime = getBeforeDate(60) + " ~ " + getNowDate();
        }
        var channelType = null;
        var i = $("#saleCheck span .on").parent('.x_checktab span').index();
        if(i == 2){
            channelType = "To B";
        }else if(i == 3){
            channelType = "To C";
        }

        cusSaleTopData(buId, salesTime, channelType);
    })

    // 选择事件改变数据
    function onChange(item, type) {
        // type='sales' 则为客户销量, "stock"则为库存量分析
        if (type === 'sales') {
            $(item).addClass('on').parent('.x_checktab span').siblings().children().removeClass('on')
            // 折线图
            salesBroken.clear();//清空当前实例
            // 饼图
            salesPie.clear();//清空当前实例
            var buId = $("#point3 .x_tab_content .x_tab .on").find("input[name='buId']").val();
            if(buId == "" || buId == undefined || buId == null){
                buId = null;
            }
            var salesTime = $("#TimeVal2").text();
            if(salesTime == "" || salesTime == null || salesTime == undefined){
                salesTime = getBeforeDate(60) + " ~ " + getNowDate();
            }
            var channelType = null;
            var i = $("#saleCheck span .on").parent('.x_checktab span').index();
            if(i == 2){
                channelType = "To B";
            }else if(i == 3){
                channelType = "To C";
            }
            cusSaleTopData(buId, salesTime, channelType);
        }

        if (type === 'stock') {
            // 折线图
            stockBroken.clear();//清空当前实例
            // 饼图
            stockPie.clear();//清空当前实例
            $(item).addClass('on').parent('.x_checktab span').siblings().children().removeClass('on');
            var buId = $("#point4 .x_tab_content .x_tab .on").find("input[name='buId']").val();
            if(buId == "" || buId == undefined || buId == null){
                buId = null;
            }
            var stockTime = $("#TimeVal3").text();
            if(stockTime == "" || stockTime == null || stockTime == undefined){
                stockTime = getNowMonth();
            }
            var channelType = $("#stockCheck span .on").parent('.x_checktab span').index();
            inventoryAnalysisData(buId, stockTime, channelType);
        }

    }
    /** 客户销量TOP8 end **/

    /**库存量分析 start**/
    var stockBrokenLegendData = [{ name: '库存量', icon: 'image://'+serverAddr+'/resources/imgs/stock1.png' }, { name: '库存金额', icon: 'image://'+serverAddr+'/resources/imgs/stock2.png' }, { name: '预计清空天数', icon: 'image://'+serverAddr+'/resources/imgs/stock3.png' }]
    //折线图
    var stockBrokenLineOption = {};
    // 饼图
    var stockBrokenPieOption = {};

    function initInventoryAnalysis() {
        var stockBrokenNameData = []; //折线图、饼图
        var stockBrokenseriesData1 = []//库存量
        var stockBrokenseriesData2 = []//库存金额
        var stockBrokenseriesData3 = []//预计清空天数

        //折线图
        stockBrokenLineOption = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow',//指示器类型。
                    shadowStyle: {
                        color: 'rgba(1, 18, 62, .2)'
                    }
                },
                backgroundColor: 'rgba(1, 18, 62, .8)',//提示框浮层的背景颜色。
                textStyle: {
                    color: '#fff',
                },
                borderWidth: 0,//提示器边框颜色
                formatter: function (param) {
                    var str = ''
                    param.forEach((item, index) => {
                        str += '<div>' + item.marker + item.seriesName + ':' + item.value + '<div>'
                    })
                    return str;
                }
            },
            grid: {
                left: '4%',
                right: '18%',
                bottom: '3%',
                top: '30%',
                containLabel: true,
            },
            legend: {
                data: stockBrokenLegendData,
                textStyle: {//图例的公用文本样式。(标签栏)
                    // fontSize: fontSize,
                    color: '#757B80'
                },
                selectedMode: false,//图例选择的模式，控制是否可以通过点击图例改变系列的显示状态
                itemWidth: itemWidth,
                itemHeight: itemHeight,
                left: 0,
                orient: 'horizontal',
                itemGap: 60,//图例间距
            },
            xAxis: [
                {
                    type: 'category',
                    data: stockBrokenNameData,
                    axisLine: {//坐标轴轴线相关设置
                        lineStyle: {
                            color: '#F3F6FA'
                        }
                    },
                    axisTick: {//坐标轴刻度相关设置。
                        show: false,
                    },
                    axisLabel: {//坐标轴刻度标签的相关设置。
                        color: '#757B80',
                        // fontSize: fontSize,
                        interval: 0,
                        rotate: 30 //刻度标签旋转的角度
                    },
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    name: stockBrokenLegendData[0].name + '(万件)',
                    nameTextStyle: {
                        // fontSize: fontSize,
                    },
                    position: 'right',
                    axisLine: {
                        show: true,
                        lineStyle: {
                            color: '#1F8EFF'
                        }
                    },
                    axisLabel: {
                        formatter: '{value}',
                        interval: 0
                    },
                    axisTick: {//坐标轴刻度相关设置。
                        show: true
                    },
                    splitLine: {
                        show: false
                    }
                },
                {
                    type: 'value',
                    name: stockBrokenLegendData[1].name + '(万)',
                    label: {//饼图图形上的文本标签
                        // fontSize: fontSize,
                        color: '#7C8B92',
                        formatter: '{b}: \n{d}%',
                    },
                    nameTextStyle: {
                        // fontSize: fontSize
                    },
                    position: 'right',
                    offset: 80,
                    axisLine: {
                        show: true,
                        lineStyle: {
                            color: '#55E2E6'
                        }
                    },
                    axisLabel: {
                        formatter: '{value}',
                        interval: 0
                    },
                    axisTick: {//坐标轴刻度相关设置。
                        show: true
                    },
                    splitLine: {
                        show: false
                    }
                },
                {
                    type: 'value',
                    name: stockBrokenLegendData[2].name,
                    nameTextStyle: {
                        color: '#757B80',
                        // fontSize: fontSize
                    },
                    position: 'left',
                    axisLine: {
                        show: true,
                        lineStyle: {
                            color: '#F3F6FA'
                        }
                    },
                    axisLabel: {
                        color: '#757B80',
                        // fontSize: fontSize,
                        interval: 0
                    },
                    splitLine: {
                        show: true,
                        lineStyle: {
                            color: '#F3F6FA'
                        }
                    }
                }
            ],
            series: [
                {
                    name: stockBrokenLegendData[0].name,
                    type: 'bar',
                    data: stockBrokenseriesData1,
                    color: {
                        type: 'linear',
                        x: 0,
                        y: 0,
                        x2: 0,
                        y2: 1,
                        colorStops: [{
                            offset: 0, color: '#399BFF' // 0% 处的颜色
                        }, {
                            offset: 1, color: '#0069E0' // 100% 处的颜色
                        }],
                        global: false // 缺省为 false
                    },
                    barWidth: barWidth,
                    itemStyle: {
                        barBorderRadius: barBorderRadius
                    }
                },
                {
                    name: stockBrokenLegendData[1].name,
                    type: 'bar',
                    yAxisIndex: 1,
                    data: stockBrokenseriesData2,
                    color: {
                        type: 'linear',
                        x: 0,
                        y: 0,
                        x2: 0,
                        y2: 1,
                        colorStops: [{
                            offset: 0, color: '#66ECF0' // 0% 处的颜色
                        }, {
                            offset: 1, color: '#00C2C7' // 100% 处的颜色
                        }],
                        global: false // 缺省为 false
                    },
                    barWidth: barWidth,
                    itemStyle: {
                        barBorderRadius: barBorderRadius
                    }
                },
                {
                    name: stockBrokenLegendData[2].name,
                    type: 'line',
                    smooth: true,//是否为平滑线
                    symbol: 'none',//标记图形
                    yAxisIndex: 2,
                    data: stockBrokenseriesData3,
                    color: {
                        type: 'linear',
                        x: 0,
                        y: 0,
                        x2: 0,
                        y2: 1,
                        colorStops: [{
                            offset: 0, color: '#FFA00B' // 0% 处的颜色
                        }, {
                            offset: 1, color: '#FF7D0B' // 100% 处的颜色
                        }],
                        global: false // 缺省为 false
                    },
                    lineStyle: {
                        normal: {
                            width: lineWidth,//线宽。
                        },
                    },
                }
            ]
        };

        // 饼图
        stockBrokenPieOption = {
            title: {
                text: '库存金额占比',
                textStyle: {
                    color: '#2D353E',
                    fontSize: fontSizeMiddle
                },
                left: 'center'
            },
            tooltip: {//提示框组件
                trigger: 'item',//触发类型
                formatter: '{b} : {c} ({d}%)',
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow',        // 默认为直线，可选为：'line' | 'shadow'
                    shadowStyle: {//axisPointer.type 为 'shadow' 时有效
                        color: 'rgba(1, 18, 62, .2)'
                    }
                },
                backgroundColor: 'rgba(1, 18, 62, .8)',//提示框浮层的背景颜色。
                textStyle: {
                    color: '#fff',
                },
                borderWidth: 0,//提示器边框颜色
            },
            series: [
                {
                    type: 'pie',
                    center: ['50%', '50%'],
                    radius: radius,
                    roseType: 'radius',
                    label: {//饼图图形上的文本标签
                        // fontSize: fontSize,
                        color: '#7C8B92',
                        // formatter: '{b}: {d}%',
                        position: 'outside',
                        formatter: '{a|{b}：{d}%}\n{hr|}',
                    },
                    labelLine: {//标签的视觉引导线样式
                        smooth: true,
                        // length: 1,
                        length2: 8
                    },
                    itemStyle: {
                        borderWidth: borderWidth,
                        borderColor: borderColor,
                    },
                    data: []
                }
            ]
        };

        inventoryAnalysisData(null, getNowMonth(), "1");
    }

    function inventoryAnalysisData(buId,month,type){
        $custom.request.ajax($module.params.serverAddrByReport + "/retailAdmin/getInventoryAnalysisData.asyn", {"buId":buId,"month":month,"type":type},function(result){
            if(result.state == 200){
                var xAxisData = [];
                var seriesData1 = [];
                var seriesData2 = [];
                var seriesData3 = [];
                var stockPieValue = [];
                var stockPieLegendData = [];
                //饼图
                var pieOption = [];
                if(result.data != null){
                    for(var i=0; i < result.data.length;i++){
                        xAxisData.push(result.data[i].name);
                        stockPieLegendData.push(result.data[i].name);
                        seriesData1.push(result.data[i].surplusNum);
                        seriesData2.push(result.data[i].amount);
                        seriesData3.push(result.data[i].clearDays);
                        stockPieValue.push(result.data[i].amount);
                    }
                }
                stockBrokenLineOption.xAxis[0].data = xAxisData;
                stockBrokenLineOption.series[0].data = seriesData1;
                stockBrokenLineOption.series[1].data = seriesData2;
                stockBrokenLineOption.series[2].data = seriesData3;

                var colorArr = ['#FFB10B', '#CCD5D9', '#5DADFF', '#55E2E6', '#1B82EB', '#B39FFF'];
                var count = 0;
                for (var i=0; i<stockPieValue.length; i++){
                    if (stockPieValue[i] == 0) {
                        continue;
                    }
                    if (count == 0) {
                        count++;
                        pieOption.push({
                            value: stockPieValue[0],
                            name: stockPieLegendData[0],
                            itemStyle: {
                                color: {
                                    type: 'linear',
                                    x: 0,
                                    y: 0,
                                    x2: 0,
                                    y2: 1,
                                    colorStops: [{
                                        offset: 0, color: '#FF720B' // 0% 处的颜色
                                    }, {
                                        offset: 1, color: '#FFB10B' // 100% 处的颜色
                                    }],
                                    global: false // 缺省为 false
                                }
                            },
                            label: {
                                rich: {
                                    hr: {
                                        borderRadius: 3,
                                        width: 3,
                                        height: 3,
                                        borderWidth: 1,
                                        borderColor: '#FFB10B',
                                        padding: [3, 3, 0, -12],
                                    },
                                    a: {
                                        padding: [-13, 15, -20, 5]
                                    }
                                }
                            }
                        })
                    } else {
                        pieOption.push({
                            value: stockPieValue[i],
                            name: stockPieLegendData[i],
                            itemStyle: {
                                color: colorArr[i]
                            },
                            label: {
                                rich: {
                                    hr: {
                                        borderRadius: 3,
                                        width: 3,
                                        height: 3,
                                        borderWidth: 1,
                                        borderColor: colorArr[i],
                                        padding: [3, 3, 0, -12]
                                    },
                                    a: {
                                        padding: [-13, 15, -20, 5]
                                    }
                                }
                            }
                        });
                    }

                }
                stockBrokenPieOption.series[0].data = pieOption;

            }

            stockBroken.setOption(stockBrokenLineOption, true);
            stockPie.clear();
            stockPie.setOption(stockBrokenPieOption,true);
        });
    }

    // 库存量分析图表数据变更
    $('#point4 .x_tab li').click(function () {
        // 折线图
        stockBroken.clear();//清空当前实例

        // 饼图
        stockPie.clear();//清空当前实例
        var buId = $(this).find("input[name='buId']").val();
        if(buId == "" || buId == undefined || buId == null){
            buId = null;
        }
        var selectTime = $("#TimeVal3").text();
        if(selectTime == "" || selectTime == null || selectTime == undefined){
            selectTime = getNowMonth();
        }
        var channelType = $("#stockCheck span .on").parent('.x_checktab span').index();
        inventoryAnalysisData(buId ,selectTime, channelType);
    });
    /**库存量分析 end**/

    /**商品在库天数分析 start**/
    // 商品在库天数分析 折线图
    var waresBrokenLegendData = [{ name: '库存量', icon: 'image://'+serverAddr+'/resources/imgs/wases1.png' }, { name: '库存金额', icon: 'image://'+serverAddr+'/resources/imgs/wases2.png' }]
    var waresLineOption = {};
    var waresPieOption = {};

    function initWarehouse() {
        // 折线图
        var waresBrokenXAxisData = [];
        var waresBrokenSeriesData1 = [];
        var waresBrokenSeriesData2 = [];

        waresLineOption = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow',//指示器类型。
                    shadowStyle: {
                        color: 'rgba(1, 18, 62, .2)'
                    }
                },
                backgroundColor: 'rgba(1, 18, 62, .8)',//提示框浮层的背景颜色。
                textStyle: {
                    color: '#fff',
                },
                borderWidth: 0,//提示器边框颜色
                formatter: function (param) {
                    var str = ''
                    param.forEach((item, index) => {
                        str += '<div>' + item.marker + item.seriesName + ':' + item.value + '<div>'
                    })
                    return str;
                }
            },
            grid: {
                left: '9%',
                right: '18%',
                bottom: '3%',
                top: '30%',
                containLabel: true,
            },
            legend: {
                data: waresBrokenLegendData,
                textStyle: {//图例的公用文本样式。(标签栏)
                    // fontSize: fontSize,
                    color: '#757B80'
                },
                selectedMode: false,//图例选择的模式，控制是否可以通过点击图例改变系列的显示状态
                itemWidth: itemWidth,
                itemHeight: itemHeight,
                left: 0,
                orient: 'horizontal',
                itemGap: 25,//图例间距
            },
            xAxis: [
                {
                    type: 'category',
                    data: waresBrokenXAxisData,
                    axisLine: {//坐标轴轴线相关设置
                        lineStyle: {
                            color: '#F3F6FA'
                        }
                    },
                    axisTick: {//坐标轴刻度相关设置。
                        show: false,
                    },
                    axisLabel: {//坐标轴刻度标签的相关设置。
                        color: '#757B80',
                        // fontSize: fontSize,
                        interval: 0,
                        rotate: 30 //刻度标签旋转的角度
                    },
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    name: waresBrokenLegendData[0].name + '（单位：件）',
                    nameTextStyle: {
                        color: '#757B80',
                        // fontSize: fontSize
                    },
                    position: 'left',
                    axisLine: {
                        show: true,
                        lineStyle: {
                            color: '#F3F6FA'
                        }
                    },
                    axisLabel: {
                        color: '#757B80',
                        // fontSize: fontSize
                    },
                    splitLine: {
                        show: true,
                        lineStyle: {
                            color: '#F3F6FA'
                        }
                    }
                },
                {
                    type: 'value',
                    name: waresBrokenLegendData[1].name + '（单位：万）',
                    label: {//饼图图形上的文本标签
                        // fontSize: fontSize,
                        color: '#7C8B92',
                        formatter: '{b}: \n{d}%',
                    },
                    nameTextStyle: {
                        // fontSize: fontSize
                    },
                    position: 'right',
                    axisLine: {
                        show: true,
                        lineStyle: {
                            color: '#55E2E6'
                        }
                    },
                    axisLabel: {
                        formatter: '{value}'
                    },
                    axisTick: {//坐标轴刻度相关设置。
                        show: true
                    },
                    splitLine: {
                        show: false
                    }
                },
            ],
            series: [
                {
                    name: waresBrokenLegendData[0].name,
                    type: 'bar',
                    yAxisIndex: 0,
                    data: waresBrokenSeriesData1,
                    color: {
                        type: 'linear',
                        x: 0,
                        y: 0,
                        x2: 0,
                        y2: 1,
                        colorStops: [{
                            offset: 0, color: '#399BFF' // 0% 处的颜色
                        }, {
                            offset: 1, color: '#0069E0' // 100% 处的颜色
                        }],
                        global: false // 缺省为 false
                    },
                    barWidth: barWidth,
                    itemStyle: {
                        barBorderRadius: barBorderRadius
                    }
                },
                {
                    name: waresBrokenLegendData[1].name,
                    type: 'line',
                    smooth: true,//是否为平滑线
                    symbol: 'none',//标记图形
                    yAxisIndex: 1,
                    data: waresBrokenSeriesData2,
                    color: {
                        type: 'linear',
                        x: 0,
                        y: 0,
                        x2: 0,
                        y2: 1,
                        colorStops: [{
                            offset: 0, color: '#66ECF0' // 0% 处的颜色
                        }, {
                            offset: 1, color: '#00C2C7' // 100% 处的颜色
                        }],
                        global: false // 缺省为 false
                    },
                    lineStyle: {
                        normal: {
                            width: lineWidth,//线宽。
                        },
                    },
                }
            ]
        };

        waresPieOption = {
            title: {
                text: '库存金额占比',
                textStyle: {
                    color: '#2D353E',
                    fontSize: fontSizeMiddle
                },
                left: 'center'
            },
            tooltip: {//提示框组件
                trigger: 'item',//触发类型
                formatter: '{b} : {c} ({d}%)',
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow',        // 默认为直线，可选为：'line' | 'shadow'
                    shadowStyle: {//axisPointer.type 为 'shadow' 时有效
                        color: 'rgba(1, 18, 62, .2)'
                    }
                },
                backgroundColor: 'rgba(1, 18, 62, .8)',//提示框浮层的背景颜色。
                textStyle: {
                    color: '#fff',
                },
                borderWidth: 0,//提示器边框颜色
            },
            series: [
                {
                    type: 'pie',
                    center: ['50%', '50%'],
                    radius: '45%',
                    // roseType: 'radius',
                    label: {//饼图图形上的文本标签
                        // fontSize: fontSize,
                        color: '#7C8B92',
                        // formatter: '{b}: {d}%',
                        position: 'outside',
                        formatter: '{a|{b}：{d}%}\n{hr|}',
                    },
                    labelLine: {//标签的视觉引导线样式
                        smooth: true,
                        // length:1,
                        length2: 8
                    },
                    data: []
                }]
        };

        inWarehouseData(null, getNowMonth());
    }
    function inWarehouseData(buId,month){
        $custom.request.ajax($module.params.serverAddrByReport + "/retailAdmin/getInWarehouseData.asyn", {"buId":buId,"month":month},function(result){
            if(result.state == 200){
                var xAxisData = [];
                var seriesData1 = [];
                var seriesData2 = [];
                var waresPieValue = [];
                var waresPieLegendData = [];
                //饼图
                var pieOption = [];
                if(result.data != null){
                    for(var i=0; i < result.data.length;i++){
                        xAxisData.push(result.data[i].inWarehouseRange);
                        waresPieLegendData.push(result.data[i].inWarehouseRange);
                        seriesData1.push(result.data[i].totalNum);
                        seriesData2.push(result.data[i].totalAmount);
                        waresPieValue.push(result.data[i].totalAmount);
                    }
                }
                waresLineOption.xAxis[0].data = xAxisData;
                waresLineOption.series[0].data = seriesData1;
                waresLineOption.series[1].data = seriesData2;

                var colorArr = ['#CCD5D9', '#5DADFF', '#55E2E6', '#1B82EB', '#B39FFF', '#D79AEB','#ccd5d9'];
                var count = 0;
                for (var i=0; i<waresPieValue.length; i++){
                    if (waresPieValue[i] == 0) {
                        break;
                    }
                    if (count == 0) {
                        count++;
                        pieOption.push({
                            value: waresPieValue[i],
                            name: waresPieLegendData[i],
                            itemStyle: {
                                color: {
                                    type: 'linear',
                                    x: 0,
                                    y: 0,
                                    x2: 0,
                                    y2: 1,
                                    colorStops: [{
                                        offset: 0, color: '#FF720B' // 0% 处的颜色
                                    }, {
                                        offset: 1, color: '#FFB10B' // 100% 处的颜色
                                    }],
                                    global: false // 缺省为 false
                                }
                            },
                            label: {
                                rich: {
                                    hr: {
                                        borderRadius: 3,
                                        width: 3,
                                        height: 3,
                                        borderWidth: 1,
                                        borderColor: '#FFB10B',
                                        padding: [3, 5, 0, -12]
                                    },
                                    a: {
                                        padding: [-13, 15, -20, 5]
                                    }
                                }
                            }
                        })
                    } else {
                        pieOption.push({
                            value: waresPieValue[i],
                            name: waresPieLegendData[i],
                            itemStyle: {
                                color: colorArr[i]
                            },
                            label: {
                                rich: {
                                    hr: {
                                        borderRadius: 3,
                                        width: 3,
                                        height: 3,
                                        borderWidth: 1,
                                        borderColor: colorArr[i],
                                        padding: [3, 3, 0, -12]
                                    },
                                    a: {
                                        padding: [-13, 15, -20, 5]
                                    }
                                }
                            }
                        });
                    }

                }
                waresPieOption.series[0].data = pieOption;

            }
            waresLineBroken.setOption(waresLineOption, true);
            waresPie.clear();
            waresPie.setOption(waresPieOption,true);
        });
    }
    // 商品在库天数分析数据变更
    $('#point5 .x_tab li').click(function () {
        // 折线图
        waresLineBroken.clear();//清空当前实例
        // 饼图
        waresPie.clear();//清空当前实例

        var buId = $(this).find("input[name='buId']").val();
        if(buId == "" || buId == undefined || buId == null){
            buId = null;
        }
        var selectTime = $("#TimeVal4").text();
        if(selectTime == "" || selectTime == null || selectTime == undefined){
            selectTime = getNowMonth();
        }
        inWarehouseData(buId ,selectTime);
    });
    /**商品在库天数分析 end**/
</script>
