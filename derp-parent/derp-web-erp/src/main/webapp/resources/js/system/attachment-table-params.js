/**定义附件表格对象*/
var $attachTable={};
/**自定义  表格 插件*/
$.fn.extend({
    createAttachTables:function(code){
    	
    	$attachTable.fn.init(code);
    	
    	var tableHtml = '<table id="datatableAttachment" class="table table-striped" cellspacing="0" width="100%">' +
                	'<thead>'+
                           '<tr>'+
                               '<th>'+
                                  '<label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">'+
                                      '<input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />' +
                                      '<span></span>'+
                                  '</label>'+
                               '</th>'+
                               '<th>文件名称</th>' +
                               '<th>文件类型</th>' +
                               '<th>文件大小</th>' +
                               '<th>上传时间</th>' +
                               '<th>上传人员</th>' +
                               '<th style="width:200px">操作</th>' +
                           '</tr>' +
                     '</thead>' +
                     '<tbody></tbody>'+
                    '</table>' ;
        
        $(this).append(tableHtml) ;
        
        if($(this).attr("multiple")){
        	
        	var btn_html = '<div class="button-list mt10">' +
                       	'<input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="multi_down" value="批量下载"/>' + 
                       	'<input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="multi_del" value="批量删除"/>' +
                    	'</div>' ;
        	
        	$(this).before(btn_html) ;
        	
        	$("#multi_down").click(function(){
        		var rows = $attachTable.fn.getCheckedRow() ;
        		
        		for(var i = 0 ; i < rows.length ; i ++){
        			var row = rows[i] ;
        			
        			$attachTable.fn.download(row.attachmentUrl , row.attachmentName) ;
        		}
        		
        	}) ; 
        	
        	$("#multi_del").click(function(){
        		var rows = $attachTable.fn.getCheckedRow() ;
        		
        		var codes = "" ;
        		for(var i = 0 ; i < rows.length ; i ++){
        			var row = rows[i] ;
        			
        			codes += row.attachmentCode ;
        			
        			if( i != rows.length -1 ){
        				codes += "," ;
        			}
        			
        		}
        		
        		$attachTable.fn.delAttachment(codes) ;
        		
        	}) ; 
        	
        }
    	
        //表格初始化
        $attachTable.fn.loading.call($("#datatableAttachment"));
        
        $attachTable.fn.initCheckBox() ;
    }
});

/**
 * 表格参数配置
 */
$attachTable={
    /**
     * 基本表格参数设置
     */
    params:{},
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
        }
    },
    //table api
    dataTableOjb:null,
    // 原生对象
    tableObj:null

};


/**
 * 自定义重写的回调函数
 */
$attachTable.rewriter={
    /**
     * 回调修改表格参数
     * @param nRow
     * @param aData
     * @param iDisplayIndex
     */
    fnRowCallback:function(nRow, aData, iDisplayIndex){
		if (Math.floor(aData.attachmentSize / (1024 * 1024) ) > 0 ){
			var size = aData.attachmentSize / (1024 * 1024) ;
			size = size.toString() ; 
			
			if(size.indexOf(".") > -1){
				size = size.substring(0 , size.indexOf(".") + 2) ;
			}
			
	        $('td:eq(3)', nRow).html( size + 'MB');
        }else if (Math.floor(aData.attachmentSize / 1024 ) > 0){
        	
        	var size = aData.attachmentSize / 1024 ;
        	size = size.toString() ; 
        	
        	if(size.indexOf(".") > -1){
				size = size.substring(0 , size.indexOf(".") + 2) ;
			}
        	
            $('td:eq(3)', nRow).html( size + 'KB');
        }else {
            $('td:eq(3)', nRow).html(aData.attachmentSize + 'B');
        }
    }
};



/**
 * 自定义表格  方法
 */
$attachTable.fn={
    /**
     * 初始化
     */
    init:function(code){
    	$attachTable.params = { 
    			url: serverAddr + '/attachment/listAttachment.asyn?r='+Math.random() + '&code=' + code ,
    	        columns:[{ //复选框单元格
    	                className: "td-checkbox",
    	                orderable: false,
    	                width: "30px",
    	                data: null,
    	                render: function (data, type, row, meta) {
    	                    return '<input type="checkbox" class="iCheck">';
    	                }
    	            },
    	                {data:'attachmentName'},{data:'attachmentType'},{data:'attachmentSize'},{data:'createDate'},{data:'creatorName'},
    	                { //操作
    	                    orderable: false,
    	                    width: "120px",
    	                    data: null,
    	                    render: function (data, type, row, meta) {
    	                    	var opreateHtml = '<a href="javascript:$attachTable.fn.delAttachment(\''+row.attachmentCode+'\')">删除</a> ';
    	                    	opreateHtml += '<a href="javascript:$attachTable.fn.download(\''+row.attachmentUrl+'\',\''+row.attachmentName+'\')">下载</a> ';
    	                    	
    	                    	var imgExt = ['jpg' , 'png' , 'bmp' , 'jpeg' ,'JPG' , 'PNG' , 'BMP' , 'JPEG' , 'Jpg' , 'Png' , 'Bmp' , 'Jpeg'] ;
    	                    	if( imgExt.indexOf(row.attachmentType) > -1 ) {
    	                    		opreateHtml += '<a href="javascript:$attachTable.fn.preview(\''+row.attachmentUrl+'\')">预览</a> ';
    	                    	}
    	                    	
    	                        return opreateHtml;
    	                    }
    	                }
    	            ]
    		};
    },
    /**
     * 重新加载数据
     */
    reload:function(){
        $attachTable.dataTableOjb.ajax.reload();
        //$mytable.dataTableOjb.draw();
    },
    /**
     * 获取选中的行
     */
    getCheckedRow:function(){
        var rows = new Array ();
        var nTrs = $attachTable.tableObj.fnGetNodes();//fnGetNodes获取表格所有行，nTrs[i]表示第i行tr
        for(var i = 0; i < nTrs.length; i++){
            if($(nTrs[i]).find("input[type=checkbox]").prop('checked')){
                var row = $attachTable.tableObj.fnGetData(nTrs[i]);
                rows.push(row);
            }
        }
        return rows;
    },
    /**
     * 向服务器发送请求
     * @param data
     * @param callback
     * @param settings
     */
    ajax:function(data, callback, settings){
        $custom.request.ajaxSync($attachTable.params.url,{},function(result){
            //异常判断与处理
            if (result.errorCode) {
                $attachTable.alert.error("查询失败");
                return;
            }
            
            //在订单详细页面同时附件数为0，隐藏表格
            if( $("#datatableAttachment").parents(".attachDetail").length == 0 
            	&& result.data.total <= 0) {
            	
            	if($("#datatableAttachment").parents(".order").length > 0){
            		$("#datatableAttachment").parents(".order").remove() ;
            		
            		return ;
            	}
            	
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
        });
    },
    /**
     * 加载jquery datatables 表格
     */
    loading:function(){
    	var begin = 0 ; 
        var pageSize = 50 ;

        $attachTable.tableObj=$(this).dataTable({
            //自定义
            language: $attachTable.lang,
            //是否允许用户改变表格每页显示的记录数
            // lengthChange: false,
            //启用服务器端分页
            serverSide: false,
            //禁用原生搜索
            searching: false,
            //允许多次初始化同一表格
            retrieve: true,
            //禁用排序
            "ordering": false, // 禁止排序
            'paging':false,
            "bInfo": false,
            //显示字段数据
            columns:$attachTable.params.columns,
            //异步获取数据
            ajax:$attachTable.fn.ajax,
            iDisplayStart :begin,  //用于指定从哪一条数据开始显示到表格中去 
    		iDisplayLength :pageSize,
            //每创建一行都回调
            fnRowCallback : $attachTable.rewriter.fnRowCallback,
            "scrollX": true,
            "dom": '<"top"rt><"bottom"lip><"clear">'
        });
        $attachTable.dataTableOjb=$attachTable.tableObj.api();
        
    } ,
    //文件下载
    download:function(url , filename){
    	
    	url = encodeURI(url) ;
    	filename = encodeURI(filename) ;
    	var requestUrl = serverAddr + "/attachment/downloadFile.asyn?token="+token;
    	
    	requestUrl += "&url=" + url + "&fileName=" + filename ;
    	
    	 var iframe = document.createElement("iframe");
        iframe.src = requestUrl;
        iframe.style.display = "none";
        document.body.appendChild(iframe);
    },
    //图片预览
    preview : function (url) {
    	
    	var a = document.createElement('a') ;
    	a.href = url ;
    	a.target = '_blank' ;
    	a.click();
    },
    delAttachment : function(attachmentCodes){
    	
    	if(attachmentCodes.length <= 0){
    		return ;
    	}
    	
    	$custom.alert.warning("确定删除" , function(){
    	
    		var delUrl = serverAddr + '/attachment/delAttachment.asyn?token='+token ;
    	
	    	$custom.request.ajax(delUrl,{'attachmentCodes' : attachmentCodes},function(result){
	        	
	    		if(result.state == 200){
	    			$custom.alert.success(result.message);
	    			debugger ;
	    			$attachTable.fn.reload();
	    		}
	        
	        },function(){
	        	$custom.alert.error("删除失败");
	        });
    		
    	}) ;
    	
    },
    initCheckBox : function(){
    	/**
	     * 全选
	     */
	    $("input[name='keeperUserGroup-checkable']").on("change", function(){
	        if($(this).is(':checked')){
	            $(":checkbox", '#datatableAttachment').prop("checked",$(this).prop("checked"));
	            $('#datatableAttachment tbody tr').addClass('success');
	        }else{
	            $(":checkbox", '#datatableAttachment').prop("checked",false);
	            $('#datatable-buttons tbody tr').removeClass('success');
	        }
	    })
	    $('#datatableAttachment').on("change", ":checkbox", function() {
	        $(this).parents('tr').toggleClass('success');
	    })
    }
    
};






