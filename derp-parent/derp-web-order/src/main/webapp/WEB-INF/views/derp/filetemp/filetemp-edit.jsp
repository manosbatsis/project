<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="/WEB-INF/views/common.jsp" />
<!-- Start content -->
<div class="content">
	<div class="container-fluid mt80">
		<!-- Start row -->
		<div class="row">
			<div class="col-12">
				<div class="card-box">
					<!--  title   start  -->
					<div class="col-12">
						<div>
							<div class="col-12 row">
								<div>
									<ol class="breadcrumb">
										<li><i class="block"></i> 当前位置：</li>
										<li class="breadcrumb-item"><a href="#">模版管理</a></li>
										<li class="breadcrumb-item"><a
											href="#">导出模版管理</a></li>
										<li class="breadcrumb-item"><a
											href="#">模版编辑</a></li>
									</ol>
								</div>
							</div>
							<form id="add-form">
								<!--  title   end  -->
								<div class="title_text">编辑模版</div>
								<input type="hidden" id="id" name="id">
								<div class="info_box">
									<div class="form-row">
										<div class="col-12">
											<div class="row col-10">
												<label class="col-form-label "
													style="white-space: nowrap;"><div >
														<i class="red">*</i>模版类型<span>：</span>
													</div></label>
												<div class="col-8 ml10">
													<select id="type" name="type" class="form-control">
													</select>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="form-row">
									<div class="col-12">
										<div class="row col-10">
											<label class="col-form-label "
												style="white-space: nowrap;"><div >
													<i class="red">*</i>模版标题<span>：</span>
												</div></label>
											<div class="col-8 ml10">
												<input type="text" class="form-control" id="title"
													name="title">
											</div>
										</div>
									</div>
								</div>
								<div class="form-row" id="codeDiv" disabled>
									<div class="col-12">
										<div class="row col-10">
											<label class="col-form-label "
												   style="white-space: nowrap;"><div >
												<i class="red">*</i>模板编码<span>：</span>
											</div></label>
											<div class="col-8 ml10">
												<input type="text" class="form-control" id="code"
													   name="code" readonly>
											</div>
										</div>
									</div>
								</div>
								<div class="form-row">
									<div class="col-12">
										<div class="row col-10">
											<label class="col-form-label "
												style="white-space: nowrap;"><div >
													<i class="red">*</i>适用类型<span>：</span>
												</div></label>
											<div class="col-8 ml10">
												<input type="radio" value="1" name="cusType">客户
												<input type="radio" value="2" name="cusType">供应商
											</div>
										</div>
									</div>
								</div>
								<div class="form-row">
									<div class="col-12">
										<div class="row col-10">
											<label class="col-form-label "
												   style="white-space: nowrap;"><div >跳转页面地址<span>：</span>
											</div></label>
											<div class="col-8 ml10">
												<input type="text" class="form-control" id="toUrl"
													   name="toUrl">
											</div>
										</div>
									</div>
								</div>
								<div class="form-row">
								    <textarea id="contentBody" name="contentBody" style="margin-top:15px;"></textarea>
								</div>
							</form>

							<div class="form-row m-t-50">
								<div class="col-12">
									<div class="row">
										<div class="col-6">
											<input type="button"
												class="btn btn-info waves-effect waves-light fr btn_default"
												id="save-buttons" value="保 存" />
										</div>
										<div class="col-6">
											<input type="button"
												class="btn btn-light waves-effect waves-light btn_default"
												id="cancel-buttons" value="取消">
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- end row -->
				<!--======================Modal框===========================  -->
			</div>
			<!-- container -->
		</div>

	</div>
	<!-- content -->
	<script src='/resources/js/system/base.js' type="text/javascript"></script>
	
	<!-- 配置UE 读取资源文件路径 -->
	<script type="text/javascript">
	    window.UEDITOR_HOME_URL = "/resources/plugins/ueditor/" ;
	</script>
	
	<!-- UE配置文件 -->
	<script type="text/javascript"
		src="/resources/plugins/ueditor/ueditor.config.js"></script>
	<!-- UE编辑器源码文件 -->
	<script type="text/javascript"
		src="/resources/plugins/ueditor/ueditor.all.js"></script>
		
		
	<script type="text/javascript">
    $module.constant.getConstantListByNameURL.call($('select[name="type"]')[0],"temp_typelist",null);

    //实例化编辑器
    var ue = UE.getEditor('contentBody', {
    	serverUrl : window.UEDITOR_HOME_URL + "jsp/config.json",
        initialFrameHeight : 350,
        zIndex: 99,
        toolbars : [ [ 'source' ,
        'anchor', //锚点
        'undo', //撤销
        'redo', //重做
        'bold', //加粗
        'indent', //首行缩进
        'italic', //斜体
        'underline', //下划线
        'time', //时间
        'date', //日期
        'inserttitle', //插入标题
        'cleardoc', //清空文档
        'insertparagraphbeforetable', //"表格前插入行"
        'fontfamily', //字体
        'fontsize', //字号
        'paragraph', //段落格式
        'justifyleft', //居左对齐
        'justifyright', //居右对齐
        'justifycenter', //居中对齐
        'justifyjustify', //两端对齐
        'forecolor', //字体颜色
        'backcolor', //背景色
        'fullscreen', //全屏
        'imagenone', //默认
        'imagecenter', //居中
        'lineheight', //行间距
        ] ] ,
        allHtmlEnabled:false,
        xssFilterRules: false,
		//input xss过滤
		inputXssFilter: false,
		//output xss过滤
		outputXssFilter: false
    });
  
    /**
    * 修改文件上传路径
    **/
    UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
    UE.Editor.prototype.getActionUrl = function(action) {
        if (action == 'uploadimage' || action == 'uploadscrawl' || action == 'uploadimage') {
            return '';
        } else {
            return this._bkGetActionUrl.call(this, action);
        }
    }
    
    /**详情值*/
    var id = '${detail.id}';
    var type = '${detail.type}';
    var title = '${detail.title}' ;
    var code = '${detail.code}' ;
    var contentBody = '${detail.contentBody}' ; 
    var cusType = '${detail.cusType}' ;
    var toUrl = '${detail.toUrl}' ;
    initDetail(id , type, title, code,cusType, contentBody,toUrl) ;
    
    

	$(function(){
		//取消按钮
		$("#cancel-buttons").click(function(){
			$module.load.pageOrder("act=16001");
		});
		
		//保存按钮
		$("#save-buttons").click(function(){
			var type = $("#type").val() ;
			if(!type){
				$custom.alert.warningText("请选择类型");
		        return ;
			}
			
		    var title = $("#title").val() ;
            if(!title){
                $custom.alert.warningText("请填写标题");
                return ;
            }

			var cusType = "";
			$("input[name='cusType']").each(function(){
				if($(this).prop("checked")){
					cusType = $(this).val();
				}
			});
            if(!cusType){
                $custom.alert.warningText("请选择适用类型");
                return ;
            }

			var url = serverAddr + "/fileTemp/saveOrModify.asyn?r="+Math.random();
            var data = $("#add-form").serializeArray();
            $custom.request.ajax(url, data, function(result){
                if(result.state == '200'){
                	$custom.alert.success("编辑成功！");
                    setTimeout(function () {
                    	$module.load.pageOrder("act=16001");
                    }, 1000);
                }else{
                	$custom.alert.error(result.expMessage);
                }
            });
		});		
	});

	/**
	* 初始化方法
	*/
    function initDetail(id , type, title, code, cusType, contentBody,toUrl) {
    	if(id){
    		$("#id").val(id) ;
    	}
    	
    	if(type){
    		$("#type").val(type) ;
    	}
    	
    	if(title){
    		$("#title").val(title) ;
    	}
    	
    	if(code){
    		$("#code").val(code) ;
    	}

    	if (cusType) {
			$("input[name='cusType'][value=" + cusType + "]").attr("checked",true);
		}

    	if (toUrl) {
			$("#toUrl").val(toUrl) ;
		}
    	
    	if(contentBody){
    		 ue.addListener("ready", function () {
    		        //赋值
    		        ue.setContent(contentBody);
    		});
    	}
    }
</script>