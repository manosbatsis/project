/**
 * 自定义插件工具
 */
var $custom={
	alert:{
		/**
		 * 成功提示
		 * @param context  提示内容
		 */
		success:function(content){
			swal(
					{
						title: content,
						type: 'success',
						confirmButtonColor: '#4fa7f3'
					}
			)
		},
		/**
		 * 错误提示
		 * @param  content  提示内容
		 */
		error:function(content){
			swal(
					{
						title: content,
						type: 'error',
						confirmButtonColor: '#d57171'
					}
			)
		},
		/**
		 * 确认回调函数
		 * @param content
		 * @param eventOK  回调方法
		 */
		warning:function(content,eventOK){
			swal({
				title: content,
				type: 'warning',
				showCancelButton: true,
				confirmButtonColor: '#4fa7f3',
				cancelButtonColor: '#d57171',
			}).then(eventOK)
		},
		/**
		 * 警示语  无回调方法
		 * @param context
		 */
		warningText:function(content){
			swal(
					{
						title: content,
						type: 'warning',
						confirmButtonColor: '#4fa7f3'
					}
			)
		},
		/**
		 * 提示语  有回调
		 * @param context
		 */
		prompt:function(content,eventOK){
			swal(
					{
						title: content,
						type: 'warning',
						confirmButtonColor: '#4fa7f3'
					}
			).then(eventOK)
		},
		/**
		 * 输入文本框
		 */
		input:function(title, context, url, id, key, newUrl, defaultValue){
			swal({
				title: title,
				input: 'text',
				inputValue:defaultValue,
				showCancelButton: true,
				confirmButtonText: '保存',
				cancelButtonText: '关闭',
				showLoaderOnConfirm: true,
				confirmButtonClass: 'btn btn-success',
				cancelButtonClass: 'btn btn-danger m-l-10',
				preConfirm: function (text) {
					return new Promise(function (resolve, reject) {
						resolve();
					});
				},
				allowOutsideClick: false
			}).then(function (text) {
				var dataJson = {};
				dataJson[key] = text;
				dataJson["id"] = id;
				$custom.request.ajax(url, dataJson, function(result){
					if(result.state == '200'){
						$custom.alert.success("修改成功！");
						setTimeout(function () {
							$load.a(newUrl);
						}, 1000);
					}else{
						$custom.alert.error("修改失败！");
					}
				});
			});
		},
		/**
		 * 确认回调函数 指定按钮名称
		 * @param content
		 * @param confirmButtonText
		 * @param cancelButtonText
		 * @param eventOK  回调方法
		 */
		warningBtnText:function(content,confirmButtonText,cancelButtonText,eventOK){
			if (!confirmButtonText) {
				confirmButtonText = "OK";
			}
			if (!cancelButtonText) {
				cancelButtonText = "Cancel";
			}
			swal({
				title: content,
				type: 'warning',
				showCancelButton: true,
				confirmButtonColor: '#4fa7f3',
				cancelButtonColor: '#d57171',
				confirmButtonText: confirmButtonText,
				cancelButtonText: cancelButtonText,
			}).then(eventOK)
		},
		
	},
	cancelReqrCheck:function(content,eventOK){
		var reqrs = $('*[require]') ;
		//是否校验成功
		var flagLength = 0  ;
		$(reqrs).each(function(index , reqr){
			var val = $(reqr).val();
			
			if(val){
				flagLength++ ;
			}
		});
		
		//检查必填项是否全部已输入，若是全部已输入，提示
		if(flagLength == $(reqrs).length){
			$custom.alert.warning(content,eventOK) ;
		}else{
			eventOK() ;
		}
	}
};

/**
 * 请求操作
 */
$custom.request={
	del:function(id,url){
		$custom.alert.warning("你确认要删除吗？",function(){
			$custom.request.ajax(url,{"id":id},function(data){
				if(data.state==200){
					$custom.alert.success("删除成功");
					//删除成功，重新加载页面
					$mytable.fn.reload();
				}else{
					$custom.alert.error(data.message);
				}
			});
		});
	},
	/**
	 * 列表  删除多选框选中的对象
	 */
	delChecked:function(url){
		//获取选中的id信息
		var ids=$mytable.fn.getCheckedRow();
		if(ids==null||ids==''||ids==undefined){
			$custom.alert.warningText("至少选择一条记录！");
			return ;
		}
		$custom.alert.warning("确定删除选中对象？",function(){
			$custom.request.ajax(url,{"ids":ids},function(data){
				if(data.state==200){
					$custom.alert.success("删除成功");
					//删除成功，重新加载页面
					$mytable.fn.reload();
				}else{
					if(data.expMessage != null){
						$custom.alert.error(data.expMessage);
					}else{
						$custom.alert.error(data.message);
					}
				}
			});
		});
	},
	/**
	 * 重置密码
	 * @param id
	 * @param url
	 */
	resetPwd:function(id,url){
		$custom.alert.warning("你确认要重置密码吗？",function(){
			$custom.request.ajax(url,{"id":id},function(data){
				if(data.state==200){
					$custom.alert.success("重置成功！");
				}else{
					$custom.alert.error(data.message);
				}
			});
		});
	},
	/**
	 * 发送ajax 请求
	 * @param url
	 * @param dataJson
	 * @param funObj
	 */
	ajax:function(url,dataJson,successFun,errorFun){
        var b=$load.invalidLogin();
        if(b){
            $custom.alert.prompt("登陆失效,请重新登陆",function(){
                //location.reload();
				window.location.href='/logout';
            });
        }
		//正则添加token 校验
		var reg= /^.*\.asyn\?.*$/;
		if(reg.test(url)){
			url+='&token='+token;
		}else{
			url+='?token='+token;
		}
		
		//ajax 发送请求
		$.ajax({
			type:'post',
			url:url,
			data:dataJson,
			dataType:'json',
			xhrFields: {
				withCredentials: true
			},
			success:successFun,
			error:errorFun
		});
	},
	/**
	 * ajax 同步请求
	 * @param url
	 * @param dataJson
	 * @param successFun
	 * @param errorFun
     */
	ajaxSync:function(url,dataJson,successFun,errorFun){
		//正则添加token 校验
		var reg= /^.*\.asyn\?.*$/;
		if(reg.test(url)){
			url+='&token='+token;
		}else{
			url+='?token='+token;
		}
		
		//ajax 发送请求
		$.ajax({
			type:'post',
			url:url,
			data:dataJson,
			dataType:'json',
			async: false,
			xhrFields: {
				withCredentials: true
			},
			success:successFun,
			error:errorFun
		});
	},
	/**
	 * 上传发送ajax 请求
	 * @param url
	 * @param dataJson
	 * @param funObj
	 */
	ajaxUpload:function(url,dataJson,successFun){
        var b=$load.invalidLogin();
        if(b){
            $custom.alert.prompt("登陆失效,请重新登陆",function(){
                //location.reload();
				window.location.href='/logout';
            });
        }
		//正则添加token 校验
		var reg= /^.*\.asyn\?.*$/;
		if(reg.test(url)){
			url+='&token='+token;
		}else{
			url+='?token='+token;
		}
		//ajax 发送请求
		$.ajax({
			type:'post',
			url:url,
			dataType: "json",
			data:dataJson,
			xhrFields: {
				withCredentials: true
			},
			contentType: false,
			processData: false,
			success:successFun,

		});
	},
	/**
	 * ajax 提交前必填校验
	 * @param url
	 * @param dataJson
	 * @param successFun
	 * @param errorFun
     */
	ajaxReqrCheck:function(url,dataJson,successFun,errorFun,logicFun){
		//正则添加token 校验
		var reg= /^.*\.asyn\?.*$/;
		if(reg.test(url)){
			url+='&token='+token;
		}else{
			url+='?token='+token;
		}
		
		var reqrs = $('*[require]') ;
		//是否校验成功
		var flag = true ;
		$(reqrs).each(function(index , reqr){
			 
			var val = $(reqr).val() ;
			//若不存在 ，显示提示语 
			if(!val){
				var msg = $(reqr).attr("reqrMsg") ; 
				
				if(!msg) {
					msg = "必填项不能为空" ; 
				}
				
				$custom.alert.error(msg) ;
				
				flag = false ; 
				
				return false ; 
			}
			
		});
		
		/*
		 *规定如果返回false,跳出方法体 
		 */
		if(!flag || logicFun() == false){
			return ;
		}
		
		//ajax 发送请求
		$.ajax({
			type:'post',
			url:url,
			data:dataJson,
			dataType:'json',
			async: false,
			xhrFields: {
				withCredentials: true
			},
			success:successFun,
			error:errorFun
		});
	},
	ajaxJson:function(url,dataJson,successFun,errorFun){
        var b=$load.invalidLogin();
        if(b){
            $custom.alert.prompt("登陆失效,请重新登陆",function(){
                //location.reload();
				window.location.href='/logout';
            });
        }
		//正则添加token 校验
		var reg= /^.*\.asyn\?.*$/;
		if(reg.test(url)){
			url+='&token='+token;
		}else{
			url+='?token='+token;
		}
		//ajax 发送请求
		$.ajax({
			type:'post',
			url:url,
			data:dataJson,
			dataType:'json',
			contentType:'application/json;charset=UTF-8',
			xhrFields: {
				withCredentials: true
			},
			success:successFun,
			error:errorFun
		});
	},

};

$derpTimer ={
		init: function(elems, format, mindate, maxdate){
			
			$(elems).each(function(i, elem){
				$(elem).attr("readonly", "true") ;
				
				var type = "date" ;
				
				if(format.indexOf("HH:mm:ss") > -1){
					type = 'datetime' ;
				}
				
				var config = {
			            elem: elem, //指定元素
			            type: type,
			            format: format,
			            ready: function () {
			                $('.laydate-btns-time').remove();
			            },
			            trigger: 'click'
			        } ;
				
				if(mindate){
					config['min'] = mindate;
				}
				
				if(maxdate){
					config['max'] = maxdate;
				}
				
				laydate.render(config);
			});
			
		}, 
		lessThanNowDateTimer: function(elem, format){
			this.init(elem, format, null, Date.now()) ;
		}
};


/**
 * load  加载方法   越连接    form表单提交
 * @type {{form: $.load.form, a: $.load.a, a: $.load.a}}
 */
var $load={
	/**
	 * 越连接
	 * @param url
	 */
	a:function(url){
		var b=$load.invalidLogin();
		if(b){
			$custom.alert.prompt("登陆失效,请重新登陆",function(){
				window.location.href='/logout';
				//location.reload();

			});
		}else{
			$("#loadingDiv").show();// loading 显示 
			$('#rightContent').load(url, data, function(response,status,xhr){

				if(status != 'success'){
					$custom.alert.error("系统异常");
				}
				
				$("#loadingDiv").hide();// loading 隐藏
		    });
		}


	},
	/**
	 * 带参数的越连接
	 * @param url
	 * @param json
	 */
	a:function(url,json){
		$('#myrame-bx').hide()
		$('#rightContent').show()
		var b=$load.invalidLogin();
		if(b){
			$custom.alert.prompt("登陆失效,请重新登陆",function(){
				window.location.href='/logout';
				//location.reload();
			});
		} else if (url.indexOf('from=oldsystem') > 0) {
			$('#rightContent').hide()
			$('#myrame-bx').show()
			const company = $('#company-name').attr('data-name')
			const src = url + '&token=' + token +'&company=' + company
			$('#myrame').attr('src', encodeURI(src))
		} else{
			$("#loadingDiv").show();// loading 显示 
			$('#rightContent').load(url,json, function(response,status,xhr){

				if(status != 'success'){
					$custom.alert.error("系统异常");
				}
				
				$("#loadingDiv").hide();// loading 隐藏
		    });
		}


	},
	/**
	 * 校验登陆失效
	 */
	invalidLogin:function(){
		//true 失效
		var b=false;
		$custom.request.ajaxSync('/login/validate.asyn',null,function(data){
			if(data.state==801||data.code=='99998'){
				b=true;
			}else if(data.state==200){
				//更新token值
				token=data.data;
			}
		});
		return b;
	}

};

/**
 * downLoad  下载文件
 * 
 */
var $downLoad={
	/**
	 * @param url
	 */
	downLoadByUrl:function(url){
		//正则添加token 校验
		var reg= /^.*\.asyn\?.*$/;
		if(reg.test(url)){
			url+='&token='+token;
		}else{
			url+='?token='+token;
		}
		/*$('#downloadDiv').html("");
		var html="<iframe id=\"downLoadIframe\" style=\"display:none\" src=\""+url+"\"></iframe>";
		$('#downloadDiv').html(html);*/
		
		/*let link = document.createElement('a');
		link.href=url;
		link.click();*/
		window.open(url);
	}
};
/**
 * 开发js公用方法监听save-button单击后禁用五秒
 */
var wait = 5;
$("#save-buttons").click(function(){
    time(this);
});
$("#confirm-buttons").click(function(){
    time(this);
});
$("#save-temp-buttons").click(function(){
    time(this);
});
$("#flashReport").click(function(){
    time(this);
});
$(".delayedBtn").click(function(){
    time(this);
});
function time(o) {
    if (wait == 0) {
        o.removeAttribute("disabled"); 
        wait = 5;
    } else {
        o.setAttribute("disabled", true);
        wait--;
        setTimeout(function() {
            time(o)
        }, 1000)
    }};
    

/**列表清空表单时，将客户下拉框值清空*/
if($("#form1")[0]){
	$("#form1")[0].addEventListener("reset", function(){
		$(".selectpicker").selectpicker('val',['noneSelectedText']);
		$(".selectpicker").selectpicker('refresh');
	});
}

