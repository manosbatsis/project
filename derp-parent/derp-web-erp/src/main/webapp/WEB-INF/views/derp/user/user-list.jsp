<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!-- Start content -->

<div class="content">
    <div class="container-fluid mt80">
 
        <div class="row">
                     <!--  title   start  -->
            <div class="col-12">
                <div class="card-box table-responsive">
                <div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                       <li><i class="block"></i> 当前位置：</li>
                        <li class="breadcrumb-item"><a href="#">系统管理</a></li>
                        <li class="breadcrumb-item active"><a href="javascript:$load.a('/user/toPage.html');">用户管理</a></li>
                    </ol>
                    </div>
            </div>
                    <!--  title   end  -->
                   <!--  过滤条件查询  start  -->
                    <!--  ================================过滤条件 start  ===============================================   -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                    <span class="msg_title">员工姓名 :</span>
                                    <input class="input_msg" type="text" name="name" required="" >
                                    <span class="msg_title">用户名 :</span>
                                    <input class="input_msg" type="text" name="username" required="" >
                                    <span class="msg_title">电话号码 :</span>
                                    <input class="input_msg" type="text" name="tel" required="" >
                                    <div class="fr">
                                        <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">状态 :</span>
                                    <select class="input_msg" name="disable"></select>

                                </div>
                            </div>
                            <a href="javascript:" class="unfold">展开功能</a>
                        </div>
                    </form>
                    <!--  ================================过滤条件  end===============================================   -->
                    <!-- 过滤条件查询  end  -->
                    <div class="row col-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="user_add">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick='$m1011.init();'>新增</button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                    <!--  ================================表格 ===============================================   -->
                    <div class="row mt10">
                    <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th>工号</th>
                            <th>登陆帐号</th>
                            <th>员工姓名</th>
                            <th>性别</th>
                            <th>状态</th>
                            <th>账号类型</th>
                            <th>角色</th>
                            <th>创建时间</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
                    <!--  ================================表格  end ===============================================   -->
            </div>
            </div>
                <!--======================Modal框===========================  -->
        <jsp:include page="/WEB-INF/views/modals/1011.jsp" />
        </div>
        <!-- End row -->
        <!-- end row -->
    </div> <!-- container -->
</div> <!-- content -->

<script type="text/javascript">
    $module.constant.getConstantListByNameURL.call($('select[name="disable"]')[0],"userInfo_disableList",null);
    $(document).ready(function() {
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:'/user/list.asyn?r='+Math.random(),
            columns:[
                {data:'code'},{data:'username', width: "100px"},{data:'name'},{data:'sex'},{data:'disableLabel', width: "80px"},{data:'accountTypeLabel'},{data:'roles'},{data:'createDate'},
                { //操作
                    orderable: false,
                    width: "160px",
                    data: null,
                    render: function (data, type, row, meta) {
                        var resetPwdURL='\'/user/resetPwd.asyn\'';
                        var caoZuoHtml = '<shiro:hasPermission name="user_toEditPage"><a href="javascript:edit('+row.id+');">编辑</a>&nbsp;&nbsp;</shiro:hasPermission>';
                        if (row.disable == '0') {
                            caoZuoHtml += '<shiro:hasPermission name="user_delUser"><a   href="javascript:;"   onclick="enableUser('+row.id+', 1)" class="red">停用</a>&nbsp;&nbsp;</shiro:hasPermission>';
                        } else {
                            caoZuoHtml += '<shiro:hasPermission name="user_delUser"><a   href="javascript:;"   onclick="enableUser('+row.id+', 0)" class="red">启用</a>&nbsp;&nbsp;</shiro:hasPermission>';
                        }
                        caoZuoHtml += '<shiro:hasPermission name="user_resetPwd"><a  href="javascript:;"   onclick="$custom.request.resetPwd('+row.id+','+resetPwdURL+')">重置密码</a></shiro:hasPermission>';
                        return caoZuoHtml;
                    }
                },
            ],
            formid:'#form1'
        };
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
            if (aData.sex == 'm'){
                $('td:eq(3)', nRow).html('男');
            }else{
                $('td:eq(3)', nRow).html('女');
            }
            
            if(aData.roles!=null){
	           	 var itemList = new Array();
	           	 if(aData.roles.indexOf(",")!=-1){
	           		 itemList.push(aData.roles.split(","));
	            	 for (var i = 0; i < itemList.length; i++) {
	            		// 如果只有一个PO号
	            		if(itemList[i].length==1){
	   	        			 $('td:eq(6)', nRow).html(aData.roles);
	   	        		}else{	// 如果有多个角色
	   	        			// 显示poNo
	   	        			$('td:eq(6)', nRow).html("<div name='partRoles'>"+aData.roles.split(",")[0]+"</div><div name = 'showdiv' style='display:none;'>"+aData.roles+"</div><a href='###' onclick='showHideCode(this)''>查看更多</a>");
	   	        		}
					}
	           	 }
         	}
        };
        //生成列表信息s
        $('#datatable-buttons').mydatatables();

    } );
    //编辑
    function edit(id){
    	
    	$module.revertList.serializeListForm() ;
    	$module.revertList.isMainList = true ; 
    	
    	$load.a("/user/toEditPage.html?id="+id);
    }
   	//删除
   	function deleteUser(id,url){
   		$custom.alert.warning("是否确认删除？",function(){
   			$custom.request.ajax("/user/delUser.asyn", {"id":id}, function(result){
   				if(result.state == '200'){
					$custom.alert.success("删除成功！");
					setTimeout(function () {
						//列表菜单缓存参数清空
						$module.revertList.serializeListForm() ;
				    	$module.revertList.isMainList = true ; 
						
						$load.a('/user/toPage.html');
					}, 1000);
				}else{
					$custom.alert.error(result.message);
				}
   			});
   		});
   	}

   	//启用/停用
    //删除
    function enableUser(id,type){
   	    var typeLabel = "启用";
   	    if (type == '1') {
   	        typeLabel = "停用";
        }
        $custom.alert.warning("是否确认"+typeLabel+"？",function(){
            $custom.request.ajax("/user/enableUser.asyn", {"id":id, "type": type}, function(result){
                if(result.state == '200'){
                    $custom.alert.success(typeLabel+"成功！");
                    setTimeout(function () {
                        //列表菜单缓存参数清空
                        $module.revertList.serializeListForm() ;
                        $module.revertList.isMainList = true ;

                        $load.a('/user/toPage.html');
                    }, 1000);
                }else{
                    $custom.alert.error(result.message);
                }
            });
        });
    }

    // 点击展开功能
    $(".unfold").click(function () {
        $(".form_con").toggleClass('hauto');
        if($(this).text() == '展开功能'){
            $(this).text('收起功能');
        }else{
            $(this).text('展开功能');
        }
    })
    //查看角色
    function showHideCode(obj){
   		$(obj).prev().toggle();
   		//被隐藏了
    	if($(obj).prev().is(":hidden")){
    		$(obj).prev().prev().show();
   		}else{
   			$(obj).prev().prev().hide();
   		} 
       }
</script>