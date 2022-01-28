<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!-- Start content -->

<div class="content">
    <div class="container-fluid mt80">
        <!-- end row -->
        <div class="row">
            <div class="col-12">
                <div class="card-box table-responsive">
                    <div class="col-12 row">
                        <div>
                            <ol class="breadcrumb">
                                <li><i class="block"></i> 当前位置：</li>
                                <li class="breadcrumb-item"><a href="#">品牌管理</a></li>
                                <li class="breadcrumb-item "><a href="javascript:$load.a('/brandParent/toPage.html');">标准品牌列表</a></li>
                            </ol>
                        </div>
                    </div>
                    <ul class="nav nav-tabs">
                        <shiro:hasPermission name="brandParentList">
                            <li class="nav-item">
                                <a href="#" data-toggle="tab" class="nav-link  active"  onclick="javascript:$load.a('/brandParent/toPage.html');">标准品牌</a>
                            </li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="brandSuperiorList">
                            <li class="nav-item">
                                <a href="#" data-toggle="tab" class="nav-link"  onclick="javascript:$load.a('/brandSuperior/toPage.html');">母品牌</a>
                            </li>
                        </shiro:hasPermission>
                    </ul>
                    <!--  过滤条件查询  start  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                    <span class="msg_title">标准品牌：</span>
                                    <input class="input_msg" type="text" name="name" >
                                    <span class="msg_title">上级母品牌：</span>
                                    <select class="input_msg" name="superiorParentBrandId" id="superiorParentBrandId"></select>

 									<span class="msg_title">授权方：</span>
                                    <select class="input_msg" name="authorizer">
                                        <option value="">请选择</option>
                                        <option value="1">品牌方</option>
                                        <option value="2">经销商</option>
                                        <option value="3">无授权</option>
                                    </select>

                                    <div class="fr">
                                        <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick='searchData();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default ">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    
                                    <span class="msg_title">分类：</span>
                                    <select class="input_msg" name="type">
                                        <option value="">请选择</option>
                                        <option value="1">跨境电商</option>
                                        <option value="2">内贸</option>
                                    </select>

                                    
                                </div>
                                

                                <a href="javascript:" class="unfold">展开功能</a>
                            </div>
                        </div>
                    </form>
                    <!-- 过滤条件查询  end  -->

                    <div class="row col-md-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="brandParent_add">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick='$m2011.init();'>新增</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="brandParent_import">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="import-buttons" onclick="$load.a('/list/menu.asyn?act=16021');">导入</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="brandParent_export">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="export-buttons" >导出</button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                    <div class="row mt20">
                        <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>标准品牌</th>
                                <th>英文名称</th>
                                <th>上级母品牌</th>
                                <th>修改时间</th>
                                <th>授权方</th>
                                <th>分类</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <jsp:include page="/WEB-INF/views/modals/2011.jsp" />
        </div>
        <div class="popupbg" style="display: none">
		    <div class="popup_box">
		        <img src="/resources/assets/images/uploading.gif" alt="">
		        <p>正在校验是否被使用中...</p>
		    </div>
		</div>
        <!-- End row -->
        <!-- end row -->
    </div> <!-- container -->
</div> <!-- content -->

<script type="text/javascript">
// $module.constant.getConstantListByNameURL.call($('select[name="superiorParentBrandCode"]')[0],"brandParent_superiorParentBrandCodeList",null);
    $(document).ready(function() {
        getSuperiorList($('select[name="superiorParentBrandId"]'));
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:'/brandParent/list.asyn?r='+Math.random(),
            columns:[
                {data:'name'},{data:'englishName'},{data:'superiorParentBrand'},
                {data:'modifyDate'},{data:'authorizerLabel'},{data:'typeLabel'},
                { //操作
                    orderable: false,
                    width: "130px",
                    data: null,
                    render: function (data, type, row, meta) {
                        return '<shiro:hasPermission name="brandParent_modify"><a href="#" onclick="$m2011.init('+row.id+');">编辑</a></shiro:hasPermission> '+
                        '<shiro:hasPermission name="brandParent_delete"><a href="#" onclick="$m2011.del('+row.id+');">删除</a></shiro:hasPermission>';
                    }
                }
            ],
            formid:'#form1'
        };
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();
        
      //点击展开功能
    	$(".unfold").click(function () {
    	    $(".form_con").toggleClass('hauto');
    	    if($(this).text() == '展开功能'){
    	        $(this).text('收起功能');
    	    }else{
    	        $(this).text('展开功能');
    	    }
    	});
        
    });
    
    function searchData(){
        $mytable.fn.reload();
    }

    function getSuperiorList(obj){
        var selectObj=$(obj);
        var jsonData=$module.constant.ajax('/brandSuperior/getSelectBean.asyn');
        selectObj.empty();
        selectObj.append("<option value=''>请选择</option>");
        jsonData.forEach(function(o,i){
            selectObj.append("<option value='"+ o.value+"'>"+o.label+"</option>");
        });
    }
    
	//导出
    $("#export-buttons").on("click",function(){
    	//根据筛选条件导出
    	var jsonData=null;
    	var jsonArray=$("#form1").serializeArray();
        $.each(jsonArray, function(i, field){
            if(field.value!=null&&field.value!=''&&field.value!=undefined){
                jsonData=$json.setJson(jsonData,field.name,field.value);
            }
        });
        var form = JSON.parse(jsonData);
   		//导出数据
       	var url = "/brandParent/export.asyn";
   		var i =0;
       	if(!!form){
      		    for(var key in form){
      		    	if(i==0){
          	    		url +="?";
          	    	}else{
          	    		url +="&";
          	    	}
      		    	url +=key+"="+form[key];
      		    	i++;
      		    }
       	}
       	$downLoad.downLoadByUrl(url);
    });
    
</script>