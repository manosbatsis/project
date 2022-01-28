/**定义表格对象*/
var $mytable={};
/**自定义  表格 插件*/
$.fn.extend({
    mydatatables:function(){
        //表格初始化
        $mytable.fn.loading.call(this);
    }
});

/**
 * 表格参数配置
 */
$mytable={
    /**
     * 基本表格参数设置
     */
    params:{
        url:'',
        columns:[],
        formid:'',
    },
    /**
     * 国际化  中文
     */
    lang:{
        //国际化
        "sProcessing": "处理中...",
        "sLengthMenu": "每页 _MENU_ 项",
        "sZeroRecords": "没有匹配结果",
        "sInfo": "当前显示第 _START_ 至 _END_ 项，共 _TOTAL_ 项。",
        "sInfoEmpty": "当前显示第 0 至 0 项，共 0 项",
        "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
        "sInfoPostFix": "",
        "sSearch": "搜索:",
        "sUrl": "",
        "sEmptyTable": "表中数据为空",
        "sLoadingRecords": "载入中...",
        "sInfoThousands": ",",
        "oPaginate": {
            "sFirst": "首页",
            "sPrevious": "上页",
            "sNext": "下页",
            "sLast": "末页",
            "sJump": "跳转"
        },
        "oAria": {
            "sSortAscending": ": 以升序排列此列",
            "sSortDescending": ": 以降序排列此列"
        },
    },
    //table api
    dataTableOjb:null,
    // 原生对象
    tableObj:null,

};


/**
 * 自定义重写的回调函数
 */
$mytable.rewriter={
    /**
     * 回调修改表格参数
     * @param nRow
     * @param aData
     * @param iDisplayIndex
     */
    fnRowCallback:function(nRow, aData, iDisplayIndex){

    }
};



/**
 * 自定义表格  方法
 */
$mytable.fn={
    /**
     * 初始化
     */
    init:function(){
        $mytable.rewriter={};
    },
    /**
     * 重新加载数据
     */
    reload:function(){
        $mytable.dataTableOjb.ajax.reload();
        //$mytable.dataTableOjb.draw();
    },
    /**
     * 获取选中的行
     */
    getCheckedRow:function(){
        var ids=[];
        var nTrs = $mytable.tableObj.fnGetNodes();//fnGetNodes获取表格所有行，nTrs[i]表示第i行tr
        for(var i = 0; i < nTrs.length; i++){
            if($(nTrs[i]).find("input[type=checkbox]").prop('checked')){
                var row = $mytable.tableObj.fnGetData(nTrs[i]);
                ids.push(row.id);
            }
        }
        return ids.join(",");
    },
    /**
     * 向服务器发送请求
     * @param data
     * @param callback
     * @param settings
     */
    ajax:function(data, callback, settings){
        $custom.request.ajax($mytable.params.url,$mytable.fn.getFormParams(data),function(result){
            //异常判断与处理
            if (result.errorCode) {
                $mytable.alert.error("查询失败");
                return;
            }
            //封装返回数据
            var returnData = {};
            returnData.draw = data.draw;//这里直接自行返回了draw计数器,应该由后台返回
            returnData.recordsTotal = result.data.total;//总记录数
            returnData.recordsFiltered = result.data.total;//后台不实现过滤功能，每次查询均视作全部结果
            returnData.data = result.data.list;
            //调用DataTables提供的callback方法，代表数据已封装完成并传回DataTables进行渲染
            //此时的数据需确保正确无误，异常判断应在执行此回调前自行处理完毕
            callback(returnData);
        },function(){
            //$mytable.alert.error("查询失败");
        });
    },
    /**
     * 获取请求参数
     * @param data
     */
    getFormParams:function(data){
        var jsonData=null;
        
        /*	2019-07-22,判断公共异步数据加载平台$module.revertList.params是否有值
         *	 如存在，获取原跳转列表查询参数
         */
        if($module.revertList.isMainList && $module.revertList.params.length > 0){
        	var jsonArray=$module.revertList.params;
            $.each(jsonArray, function(i, field){
                if(field.value!=null&&field.value!=''&&field.value!=undefined){
                    jsonData=$json.setJson(jsonData,field.name,field.value);
                }
            });
        }else{
	        //设置开始页和每页最大记录数
	        jsonData=$json.setJson(jsonData,"begin",data.start);
	        jsonData=$json.setJson(jsonData,"pageSize",data.length);
	        //设置过滤条件参数
	        var formid=$mytable.params.formid;
	        if(formid!=null&&formid!=''&&formid!=undefined){
	            var jsonArray=$(formid).serializeArray();
	            $.each(jsonArray, function(i, field){
	                if(field.value!=null&&field.value!=''&&field.value!=undefined){
	                    jsonData=$json.setJson(jsonData,field.name,field.value);
	                }
	            });
	        }
        }
        
        return JSON.parse(jsonData);
    },
    /**
     * 加载jquery datatables 表格
     */
    loading:function(){
    	//2019-7-22如果页面公共参数存在，说明从详情页跳转，展示原列表
    	var tableId = $(this).attr("id") ; 
    	
    	var begin = 0 ; 
        var pageSize = 10 ;
        
        if ( tableId != "datatable-buttons" ){
        	$module.revertList.isMainList = false ;
        }else{
        	$module.revertList.isMainList = true ;
        }
        
    	if($module.revertList.isMainList && $module.revertList.params.length > 0) {
        	
        	var jsonArray=$module.revertList.params;
            $.each(jsonArray, function(i, field){
                if(field.value!=null&&field.value!=''&&field.value!=undefined){
                	
                	if(field.name.indexOf("begin") > -1){
                			begin = field.value ; 
                		}else if (field.name.indexOf("pageSize") > -1) {
                			pageSize = field.value ;
                		}
                }
            });
        }
    	
        $mytable.tableObj=$(this).dataTable({
            //自定义
            language: $mytable.lang,
            //是否允许用户改变表格每页显示的记录数
            // lengthChange: false,
            //启用服务器端分页
            serverSide: true,
            //禁用原生搜索
            searching: false,
            //允许多次初始化同一表格
            retrieve: true,
            //禁用排序
            "ordering": false, // 禁止排序
            //显示数字的翻页样式
            sPaginationType: "full_numbers",
            //显示字段数据
            columns:$mytable.params.columns,
            //异步获取数据
            ajax:$mytable.fn.ajax,
            iDisplayStart :begin,  //用于指定从哪一条数据开始显示到表格中去 
    		iDisplayLength :pageSize,
            //每创建一行都回调
            fnRowCallback : $mytable.rewriter.fnRowCallback,
            "scrollX": true,
            "dom": '<"top"rt><"bottom"lip><"clear">',
            // fixedHeader: true
            // 'pagingType': 'input'
        });
        $mytable.dataTableOjb=$mytable.tableObj.api();
        
        /*
         * 2019-07-23 渲染查询表单
         */
        //setTimeout('$mytable.fn.renderFromDetail()' , 1000) ;
        $mytable.fn.renderFromDetail() ;
    } ,
    //获取当前分页页面信息
    getCurrentPageInfo : function (){
    	
    	var oSettings = $("#datatable-buttons").dataTable().fnSettings();
    	
    	var pageInfo = [
    		{"name":"begin" , "value" : oSettings._iDisplayStart},
    		{"name":"pageSize" , "value" : oSettings._iDisplayLength }
    	] ;
    	
    	return pageInfo ;
    		
    } ,  
    //详细页面返回后渲染列表
    renderFromDetail : function () {
    	
    	//判断$module.revertList.params是否有值，有值说明由详情页面跳转
        if($module.revertList.isMainList && $module.revertList.params.length > 0) {
        	var jsonArray=$module.revertList.params;
            $.each(jsonArray, function(i, field){
                if(field.value!=null&&field.value!=''&&field.value!=undefined){
                	if(field.name.indexOf("begin") > -1 ||
                		field.name.indexOf("pageSize") > -1){
                			//continue
                			return true ; 
                		}
                	
                		//多选组件特殊处理赋值
                	if($("*[name='"+ field.name +"']").hasClass("goods_select2")){
                		var arr = field.value.split(",");
                		$("*[name='"+ field.name +"']").val(arr).trigger('change');
                	}else{
                		$("*[name='"+ field.name +"']").val(field.value) ; 
                	}
                	
                }
            });
            
	    	$module.revertList.params = [] ;
        }
    	
    }
    
};






