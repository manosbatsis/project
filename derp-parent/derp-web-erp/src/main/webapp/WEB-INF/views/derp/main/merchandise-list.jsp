<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- end row -->
        <div class="row">
            <div class="col-12">
                <div class="card-box">
                    <div class="col-12 row">
                        <div>
                            <ol class="breadcrumb">
                                <li><i class="block"></i> 当前位置：</li>
                                <li class="breadcrumb-item"><a href="#">商品档案</a></li>
                                <li class="breadcrumb-item "><a href="javascript:$load.a('/merchandise/toPage.html');">商品列表</a>
                                </li>
                            </ol>
                        </div>
                    </div>
                    <!--  过滤条件查询  start  -->
                    <form id="form1">
                        <div class="form_box">
                            <div class="form_con">
                                <%-- <div class="form-row ml15 h35">
                                     <div class="form-group col-md-3">
                                         <div class="row">
                                             <label class="col-4 col-form-label " style="white-space:nowrap;"><div class="fr">商品编码<span>：</span></div></label>
                                             <div class="col-7 ml10">
                                                 <input class="form-control" type="text" required=""  name="goodsCode">
                                             </div>
                                         </div>
                                     </div>
                                     <div class="form-group col-md-3">
                                         <div class="row">
                                             <label class="col-4 col-form-label" style="white-space:nowrap;"><div class="fr">商品名称<span>：</span></div></label>
                                             <div class="col-7 ml10">
                                                 <input class="form-control" type="text" name="name" required="" >
                                             </div>
                                         </div>
                                     </div>
                                     <div class="form-group col-md-3">
                                         <div class="row">
                                             <label class="col-4 col-form-label" style="white-space:nowrap;"><div class="fr">商品货号<span>：</span></div></label>
                                             <div class="col-7 ml10">
                                                 <input class="form-control" type="text" name="goodsNo" required="" >
                                             </div>
                                         </div>
                                     </div>
                                     <div class="form-group col-md-2">
                                         <div class="row">
                                             <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick='searchData();' >查询</button>
                                             <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                         </div>
                                     </div>
                                 </div>
                                 <div class="form-row ml15 h35">
                                     <div class="form-group col-md-3">
                                         <div class="row">
                                             <label class="col-4 col-form-label" style="white-space:nowrap;"><div class="fr">商品条码<span>：</span></div></label>
                                             <div class="col-7 ml10">
                                                 <input class="form-control" type="text" name="barcode" required="" >
                                             </div>
                                         </div>
                                     </div>
                                     <div class="col-md-3">
                                         <div class="row">
                                             <label class="col-4 col-form-label" style="white-space:nowrap;"><div class="fr">工厂源码<span>：</span></div></label>
                                             <div class="col-7 ml10">
                                                 <input class="form-control" type="text" name="factoryNo" required="" >
                                             </div>
                                         </div>
                                     </div>
                                     <div class="form-group col-md-3">
                                         <div class="row">
                                             <label class="col-4 col-form-label" style="white-space:nowrap;"><div class="fr">数据来源<span>：</span></div></label>
                                             <div class="col-7 ml10">
                                                 <select class="form-control" name="source" required="" >
                                                     <option value="">请选择</option>
                                                     <option value="1">主数据</option>
                                                     <option value="0">录入/导入</option>
                                                 </select>
                                             </div>
                                         </div>
                                     </div>
                                 </div>
                                 <div class="form-row ml15 h35">
                                     <div class="col-md-3">
                                         <div class="row">
                                             <label class="col-4 col-form-label" style="white-space:nowrap;"><div class="fr">商品品牌<span>：</span></div></label>
                                             <div class="col-7 ml10">
                                                 <select name="brandId" class="form-control goods_select2">
                                                     <option value="">请选择</option>
                                                     <c:forEach items="${brandBean }" var="brand">
                                                         <option value="${brand.value }">${brand.label }</option>
                                                     </c:forEach>
                                                 </select>
                                             </div>
                                         </div>
                                     </div>
                                     <div class="col-md-3">
                                         <div class="row">
                                             <label class="col-4 col-form-label" style="white-space:nowrap;"><div class="fr">商品分类<span>：</span></div></label>
                                             <div class="col-7 ml10">
                                                 <select name="productTypeId" class="form-control goods_select2">
                                                     <option value="">请选择</option>
                                                     <c:forEach items="${productTypeBean }" var="productType">
                                                         <option value="${productType.value }">${productType.label }</option>
                                                     </c:forEach>
                                                 </select>
                                             </div>
                                         </div>
                                     </div>
                                     <div class="col-md-3">
                                         <div class="row">
                                             <label class="col-4 col-form-label" style="white-space:nowrap;"><div class="fr">是否备案<span>：</span></div></label>
                                             <div class="col-7 ml10">
                                                 <select class="form-control" name="isRecord" required="" >
                                                     <option value="">请选择</option>
                                                     <option value="1">是</option>
                                                     <option value="0">否</option>
                                                 </select>
                                             </div>
                                         </div>
                                     </div>
                                 </div>--%>
                                <div class="form_item h35">
                                    <span class="msg_title">商品编码 :</span>
                                    <input class="input_msg" type="text" required="" name="goodsCode">
                                    <span class="msg_title">商品名称 :</span>
                                    <input class="input_msg" type="text" name="name" required="">
                                    <span class="msg_title">商品货号 :</span>
                                    <input class="input_msg" type="text" name="goodsNo" required="">
                                    <div class="fr">
                                        <button type="button" class="btn btn-find waves-effect waves-light btn_default"
                                                onclick='searchData();'>查询
                                        </button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">
                                           	 重置
                                        </button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">商品条码 :</span>
                                    <input class="input_msg" type="text" name="barcode" required="">
                                    <span class="msg_title">工厂源码 :</span>
                                    <input class="input_msg" type="text" name="factoryNo" required="">
                                    <span class="msg_title">数据来源 :</span>
                                    <select class="input_msg" name="source" required="">
                                    </select>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">商品品牌 :</span>
                                    <select name="brandId" class="input_msg">
                                        <option value="">请选择</option>
                                        <c:forEach items="${brandBean }" var="brand">
                                            <option value="${brand.value }">${brand.label }</option>
                                        </c:forEach>
                                    </select>
                                    <span class="msg_title">二级类目 :</span>
                                    <select name="productTypeId" class="input_msg">
                                        <option value="">请选择</option>
                                        <c:forEach items="${productTypeBean }" var="productType">
                                            <option value="${productType.value }">${productType.label }</option>
                                        </c:forEach>
                                    </select>
                                    <span class="msg_title">是否备案 :</span>
                                    <select class="input_msg" name="isRecord" required="">
                                    </select>
                                </div>
                                    <div class="form_item h35">
                                        <span class="msg_title">外仓统一码 :</span>
                                        <select class="input_msg" name="outDepotFlag" required="">
                                        </select>
                                    </div>
                            </div>
                            <a href="javascript:" class="unfold">展开功能</a>
                        </div>
                    </form>
                    <!-- 过滤条件查询  end  -->

                    <div class="row col-md-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="merchandise_toAddPage">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="add-buttons">
                               	     新增
                                </button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="merchandise_delMerchandise">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm"
                                        id="delete-buttons">删除
                                </button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="merchandise_toImportPage">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm"
                                        id="import-buttons">批量导入
                                </button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="merchandise_toImportImage">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm"
                                        id="import-buttons-image">导入图片
                                </button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="merchandise_toExportPage">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm"
                                        id="export-buttons">导出
                                </button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                    <div class="mt10 table-responsive" style="width: 100%">
                        <table id="datatable-buttons" class="table table-striped twoRows" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>
                                    <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                        <input type="checkbox" name="keeperUserGroup-checkable"
                                               class="group-checkable"/>
                                        <span></span>
                                    </label>
                                </th>
                                <th style="min-width: 100px">商品编码</th>
                                <th>商品货号</th>
                                <th style="min-width: 220px">商品名称</th>
                                <th>计量单位</th>
                                <th>单价</th>
                                <th>所属公司</th>
                                <th>商品条码</th>
                                <th style="min-width: 100px">数据来源</th>
                                <th>是否备案</th>
                                <th>状态</th>
                                <th>外仓统一码</th>
                                <th style="min-width: 100px;">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
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
</div>
<!-- content -->
      

<script type="text/javascript">

$module.constant.getConstantListByNameURL.call($('select[name="source"]')[0],"merchandiseInfo_sourceList",null);
$module.constant.getConstantListByNameURL.call($('select[name="isRecord"]')[0],"merchandiseInfo_isRecordList",null);
$module.constant.getConstantListByNameURL.call($('select[name="outDepotFlag"]')[0],"merchandiseInfo_outDepotFlagList",null);
    $(document).ready(function () {

        //重置按钮
        $("button[type='reset']").click(function () {
            $(".goods_select2").each(function () {
                $(this).val("");
            });
            //重新加载select2
            $(".goods_select2").select2({
                language: "zh-CN",
                placeholder: '请选择',
                allowClear: true
            });
        });

        //重新加载select2
        $(".goods_select2").select2({
            language: "zh-CN",
            placeholder: '请选择',
            allowClear: true
        });

        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params = {
            url: '/merchandise/listMerchandise.asyn?r=' + Math.random(),
            columns: [{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
            {data: 'goodsCode'}, {data: 'goodsNo', width: "100px"}, {data: 'name',width:'167px'}, {data: 'unitName'}, {data: 'filingPrice'},
            {data: 'merchantName'}, {data: 'barcode',width:'91px'}, {data: 'sourceLabel',width:'90px'},
            {data: 'isRecordLabel'},{data: 'statusLabel'},{data:'outDepotFlagLabel'},
                { //操作
                    orderable: false,
                    width: "100px",
                    data: null,
                    render: function (data, type, row, meta) {
                    	var status = "";
                    	if(row.status == '1'){
                    		status = '<shiro:hasPermission name="merchandise_auditMerchandies"><a href="javascript:audit('+row.id+',0)">禁用</a></shiro:hasPermission>';
                    	}else{
                    		status = '<shiro:hasPermission name="merchandise_auditMerchandies"><a href="javascript:audit('+row.id+',1)">启用</a></shiro:hasPermission>';
                    	}
                        var edit = "";
                    	edit = '<shiro:hasPermission name="merchandise_toEditPage"><a href="javascript:edit(' + row.id + ')">编辑</a></shiro:hasPermission>'
                        return edit +' '+ status+ '  <shiro:hasPermission name="merchandise_toDetailsPage"><a href="javascript:details(' + row.id + ')">详情</a></shiro:hasPermission>';
                    }
                },
            ],
            formid: '#form1'
        };
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback = function (nRow, aData, iDisplayIndex) {

        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();

    });

    function searchData() {
        $mytable.fn.reload();
    }

    //新增
    $("#add-buttons").click(function () {
        $load.a("/merchandise/toAddPage.html");
    });

    //详情
    function details(id) {
    	
    	$module.revertList.serializeListForm() ;
    	$module.revertList.isMainList = true ; 
    	
        $load.a("/merchandise/toDetailsPage.html?id=" + id);
    }

    //主数据编辑
    function mainEdit(id) {
    	$module.revertList.serializeListForm() ;
    	$module.revertList.isMainList = true ; 
    	
        $load.a("/merchandise/toMainEditPage.html?id=" + id);
    }

    //编辑
    function edit(id) {
    	
    	$module.revertList.serializeListForm() ;
    	$module.revertList.isMainList = true ; 
    	
        $load.a("/merchandise/toEditPage.html?id=" + id);
    }

    //导入
    $("#import-buttons").click(function () {
        $load.a("/merchandise/toImportPage.html");
    });
    //导入图片
    $("#import-buttons-image").click(function () {
        $load.a("/merchandise/toImportImagePage.html");
    });
    
    
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
       	var url = "/merchandise/exportMerchandise.asyn";
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

    /**
     * 全选
     */
    $("input[name='keeperUserGroup-checkable']").on("change", function(){
        if($(this).is(':checked')){
            $(":checkbox", '#datatable-buttons').prop("checked",$(this).prop("checked"));
            $('#datatable-buttons tbody tr').addClass('success');
        }else{
            $(":checkbox", '#datatable-buttons').prop("checked",false);
            $('#datatable-buttons tbody tr').removeClass('success');
        }
    })
    $('#datatable-buttons').on("change", ":checkbox", function() {
        $(this).parents('tr').toggleClass('success');
    })
    // 点击展开功能
    $(".unfold").click(function () {
        $(".form_con").toggleClass('hauto');
        if($(this).text() == '展开功能'){
            $(this).text('收起功能');
        }else{
            $(this).text('展开功能');
        }
    });
    $(function () {
        $("#datatable-buttons_wrapper").removeClass('container-fluid');
    });
    
    $("#delete-buttons").click(function(){
    	//获取选中的id信息
		var ids=$mytable.fn.getCheckedRow();
		if(ids==null||ids==''||ids==undefined){
			$custom.alert.warningText("至少选择一条记录！");
			return ;
		}
		$custom.alert.warning("确定删除选中对象？",function(){
			$(".popupbg").show();
			//检测是否在订单中存在
			$custom.request.ajax($module.params.serverAddrByOrder+"/checkGoods/checkGoods.asyn",{"ids":ids},function(data){
				if(data.state==200){
					//检测是否在库存中存在
					$custom.request.ajax($module.params.serverAddrByInventory+"/checkGoodsByInventory/checkGoods.asyn",{"ids":ids},function(data){
						if(data.state==200){
							//检测是否在仓储中存在
							$custom.request.ajax($module.params.serverAddrByStorage+"/checkGoodsByStorage/checkGoods.asyn",{"ids":ids},function(data){
								$(".popupbg").hide();
								if(data.state==200){
									$custom.request.ajax("/merchandise/delMerchandise.asyn",{"ids":ids},function(result){
										if(result.state==200){
											$custom.alert.success("删除成功");
											//删除成功，重新加载页面
											$mytable.fn.reload();
										}else{
											if(result.expMessage != null){
												$custom.alert.error(result.expMessage);
											}else{
												$custom.alert.error(result.message);
											}
										}
									});
								}else{
									$(".popupbg").hide();
									if(data.expMessage != null){
										$custom.alert.error(data.expMessage);
									}else{
										$custom.alert.error(data.message);
									}
								}
							});
						}else{
							$(".popupbg").hide();
							if(data.expMessage != null){
								$custom.alert.error(data.expMessage);
							}else{
								$custom.alert.error(data.message);
							}
						}
					});
				}else{
					$(".popupbg").hide();
					if(data.expMessage != null){
						$custom.alert.error(data.expMessage);
					}else{
						$custom.alert.error(data.message);
					}
				}
			});
		});
    });
    
    //禁用/启用
    function audit(id, status){
    	$custom.alert.warning("你确认要禁用/启用吗？",function(){
	    	var url = "/merchandise/auditMerchandies.asyn";
	    	var form = {"id":id, "status":status};
	    	var state = "";
	    	if(status == '1'){
	    		state = "启用";
	    	}else{
	    		state = "禁用";
	    	}
			$custom.request.ajax(url, form, function(result){
				if(result.state == '200'){
					$custom.alert.success(state + "成功！");
					setTimeout(function () {
						
						$module.revertList.serializeListForm() ;
				    	$module.revertList.isMainList = true ; 
						
						$load.a("/merchandise/toPage.html");
					}, 1000);
				}else{
					$custom.alert.error(state + result.message);
				}
			});
    	});
    }
    
</script>