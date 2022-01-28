/**定义表格对象*/
var $mydate={};
/**自定义  表格 插件*/
$.fn.extend({
    mydate:function(params){
        if(params!=null&&params!=undefined&&params!=''){
            $mydate.params=params;
        }
        //表格初始化
        $mydate.fn.loading.call(this);
    }
});

//参数
$mydate={
    params:{
    	autoUpdateInput: false,
        locale:{
            "format": 'YYYY/MM/DD',
            "separator": " - ",//
            "applyLabel": "确定",
            "cancelLabel": "取消",
            "fromLabel": "起始时间",
            "toLabel": "结束时间'",
            "customRangeLabel": "自定义",
            "weekLabel": "W",
            "daysOfWeek": ["日", "一", "二", "三", "四", "五", "六"],
            "monthNames": ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
            "firstDay": 1
        }
    }

};
//方法
$mydate.fn={
    loading:function(){
        /*
         *  2019-7-23 设置不显示默认值 ， 自定义控件确定，取消事件
         */
        $(this).daterangepicker($mydate.params) 
        .on('cancel.daterangepicker', function(ev, picker) {
                $(this).val("请选择日期范围");
            }).on('apply.daterangepicker', function(ev, picker) {
                $(this).val(picker.startDate.format('YYYY/MM/DD')+" - "+picker.endDate.format('YYYY/MM/DD'));
            });
        
    }
};