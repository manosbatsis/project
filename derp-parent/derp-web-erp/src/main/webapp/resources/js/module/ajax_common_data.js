//公共异步数据加载平台
var $module={
    load:{
    	/*-------------------2019-07-22web页面列表跳转规则修改内容开始-------------------------*/
        /**
         * 加载子页面  订单模块
         * @param params  "act=4001"
         */
        pageOrder:function(params){
        	if(params.indexOf("id") > -1 && params.indexOf("act") > -1){
        		$module.revertList.serializeListForm() ;
        	}
        	
        	$module.revertList.isMainList = true ; 
        	
            $load.a($module.params.loadOrderPageURL+params);
        },
        pageOrderNew: function(url) {
            $module.revertList.isMainList = true ; 
        	var hostname = window.location.hostname
            var array = hostname.split('.') || []
            var flag = true
            for (var i = 0; i < array.length; i++) {
                if (isNaN(array[i])) {
                    flag = false
                    break
                }
            }
            if (flag) { // ip
                console.log('测试环境')
                var ip = '10.10.102.208:89'
                $load.a('http://'+ ip + url)
            } else { // 域名 正式环境
                console.log('正式环境')
                $load.a('http://depx.topideal.mobi' + url)
            }
        },
        /**
         * 加载子页面   库存
         * @param params
         */
        pageInventory:function(params){
        	
        	if(params.indexOf("id") > -1 && ( params.indexOf("bls") > -1 || params.indexOf("act") > -1) ){
        		$module.revertList.serializeListForm() ;
        	}
        	
        	$module.revertList.isMainList = true ; 
        	
            $load.a($module.params.loadInventoryPageURL+params);
        },
        /**
         * 加载子页面   仓储
         * @param params
         */
        pageStorage:function(params){
        	
        	if(params.indexOf("id") > -1 && params.indexOf("act") > -1){
        		$module.revertList.serializeListForm() ;
        	}
        	
        	$module.revertList.isMainList = true ; 
        	
            $load.a($module.params.loadStoragePageURL+params);
        },
        /**
         * 加载子页面  报表
         * @param params
         */
        pageReport:function(params){

        	if((params.indexOf("id") > -1 || params.indexOf("month") > -1) && params.indexOf("act") > -1){
        		$module.revertList.serializeListForm() ;
        	}
        	
        	$module.revertList.isMainList = true ; 
        	
            $load.a($module.params.loadReportPageURL+params);
        },
        /**
         * 加载子页面,传json参数
         * @param params
         * @param json
         */
        page:function(params,json){
        	
        	if(params.indexOf("id") > -1 && params.indexOf("act") > -1){
        		$module.revertList.serializeListForm() ;
        	}
        	
        	$module.revertList.isMainList = true ; 
        	
            $load.a($module.params.loadModulePageURL+params,json);
        }
        /*-------------------2019-07-22web页面列表跳转规则修改内容结束-------------------------*/
    },
    depot:{
    	/**
         * 根据代销仓加载--已废弃
         */
        loadSelectByTopBooks:function(isTopBooks,id){
        	var selectObj=$(this);
        	var typeArys=isTopBooks.split(",");
        	typeArys.forEach(function(isTopBooks,i){
        		var jsonData=$module.depot.ajax($module.params.getAllDepotURL,{isTopBooks:isTopBooks});
        		jsonData.forEach(function(o,i){
        			selectObj.append("<option value='"+ o.value+"' isTopBooks='"+isTopBooks+"'>"+o.label+"</option>");
        		});
        	});
        	if(id!=null&&id!=""&&id!=undefined){
        		selectObj.find("option[value = '"+id+"']").attr("selected","selected");
        	}
        },

        /**
         * 根据页面传入商家id、仓库类型（以逗号隔开）、是否代销仓、进出接口指令、统计存货跌价、选品限制、已入定出、已出定入获取此商家下仓库的下拉框
         * @param id
         * @param json
         */
        getSelectBeanByMerchantRel:function(id,json){
            var selectObj=$(this);
            var jsonData=$module.depot.ajax($module.params.getSelectBeanByMerchantRelURL, json);
            selectObj.empty();
            selectObj.append("<option value=''>请选择</option>");
            jsonData.forEach(function(o,i){
                selectObj.append("<option value='"+ o.value+"'>"+o.label+"</option>");
            });
            if(id!=null&&id!=""&&id!=undefined){
                selectObj.find("option[value = '"+id+"']").attr("selected","selected");
            }
        },
        /**
         * 根据过滤条件加载
         * @param id
         * @param json
         */
        loadSelectData:function(id,json){
            var selectObj=$(this);
            var jsonData=$module.depot.ajax($module.params.getAllDepotURL,json);

            jsonData.forEach(function(o,i){
                selectObj.append("<option value='"+ o.value+"'>"+o.label+"</option>");
            });
            if(id!=null&&id!=""&&id!=undefined){
                selectObj.find("option[value = '"+id+"']").attr("selected","selected");
            }
        },
        /**
         * 根据类型加载
         */
        loadSelectByType:function(types,id){
            var selectObj=$(this);
            var typeArys=types.split(",");
            typeArys.forEach(function(type,i){
                var jsonData=$module.depot.ajax($module.params.getAllDepotURL,{type:type});
                jsonData.forEach(function(o,i){
                    selectObj.append("<option value='"+ o.value+"' type='"+type+"'>"+o.label+"</option>");
                });
            });
            if(id!=null&&id!=""&&id!=undefined){
                selectObj.find("option[value = '"+id+"']").attr("selected","selected");
            }
        },
        /**
         * 根据是否同关区--已废弃
         */
        loadSelectByArea:function(isSameArea,customsNo,id){
            var selectObj=$(this);
            var jsonData=$module.depot.ajax($module.params.getDepotByAreaURL,{isSameArea:isSameArea,customsNo:customsNo});
            selectObj.empty();
            selectObj.append("<option value=''>请选择</option>");
            jsonData.forEach(function(o,i){
                selectObj.append("<option value='"+ o.value+"'>"+o.label+"</option>");
            });
            if(id!=null&&id!=""&&id!=undefined){
                selectObj.find("option[value = '"+id+"']").attr("selected","selected");
            }
        },
        /**
         * 根据id获取仓库信息
         */
        getDepotById:function(id){
            var jsonData=$module.depot.ajax($module.params.getDepotByIdURL,{id:id});
            return jsonData;
        },
        ajax:function(url,json){
            var jsonData=null;
            $custom.request.ajaxSync(url,json,function(data){
                jsonData=data.data;
            });
            return jsonData;
        },
         /**
         * 根据仓库id获取仓库关联关区 ，如果有配置关区，默认带出一个
         */
        getSelectBeanByDepotCustomsRel:function(depotId,id){
            var selectObj=$(this);
            var jsonData=$module.depot.ajax($module.params.getSelectBeanByDepotCustomsRelURL, {"depotId":depotId});
            selectObj.empty();
            selectObj.append("<option value=''>请选择</option>");
            if(jsonData){
	            jsonData.forEach(function(o,i){
	                selectObj.append("<option value='"+ o.value+"'>"+o.label+"</option>");
	            });
	            if(id!=null&&id!=undefined){
	                selectObj.find("option[value = '"+id+"']").attr("selected","selected");
	            }else{
	            	selectObj.find('option:eq(1)').attr('selected','selected');}
            }
           
        },

        /**
         * 根据页面传入商家id、事业部id、仓库类别、是否代客管理仓库、是否是代销仓获取此商家事业部下仓库的下拉框
         * @param id
         * @param json
         */
        getSelectBeanByMerchantBuRel:function(id,json){
            var selectObj=$(this);
            var jsonData=$module.depot.ajax($module.params.getSelectBeanByMerchantRelBuURL, json);
            selectObj.empty();
            selectObj.append("<option value=''>请选择</option>");
            jsonData.forEach(function(o,i){
                selectObj.append("<option value='"+ o.value+"'>"+o.label+"</option>");
            });
            if(id!=null&&id!=""&&id!=undefined){
                selectObj.find("option[value = '"+id+"']").attr("selected","selected");
            }
        },
    },
    depotSchedule:{
    	/**
    	 * 根据仓库id获取 仓库附表仓库地址下拉框
    	 */
    	loadSelectByDepotId:function(depotId,depotScheduleId){
    		var selectObj=$(this);    		
            var jsonData=$module.depotSchedule.ajax($module.params.getAllDepotScheduleURL,{depotId:depotId});
            selectObj.empty();
            selectObj.append("<option value=''>请选择</option>");           
			  for (var int = 0; int < jsonData.length; int++) {
				var o = jsonData[int];
				 selectObj.append("<option value='"+ o.value+"'>"+o.label+"</option>");
			  }	
			  if(depotScheduleId){
	             selectObj.find("option[value = '"+depotScheduleId+"']").attr("selected","selected");
	          }
            
            
    	},
    	ajax:function(url,json){
            var jsonData=null;
            $custom.request.ajaxSync(url,json,function(data){
                jsonData=data.data;
            });
            return jsonData;
        },
    },
    constant:{
    	/**
         * 根据名称获取常量集合
         * listName 常量集合名称
         * selectValue 选中的值
         */
    	getConstantListByNameURL:function(listName,selectValue){
        	var selectObj=$(this);
    		var jsonData=$module.constant.ajax($module.params.getConstantListByNameURL,{listName:listName});
    		selectObj.empty();
            selectObj.append("<option value=''>请选择</option>");    
    		jsonData.forEach(function(o,i){
    			selectObj.append("<option value='"+ o.key+"'>"+o.value+"</option>");
    		});
    		if(selectValue){
	             selectObj.find("option[value = '"+selectValue+"']").attr("selected","selected");
	          }
        },
        /**
         * 根据名称获取常量集合
         * listName 常量集合名称
         */
    	getConstantListByName:function(listName){
    		return $module.constant.ajax($module.params.getConstantListByNameURL,{listName:listName});
        },
        ajax:function(url,json){
            var jsonData=null;
            $custom.request.ajaxSync(url,json,function(data){
                jsonData=data.data;
            });
            return jsonData;
        },
    },
    order:{
        getAdjustmentStatus:function(sourceCode,currentDate){
        	var json={"sourceCode": sourceCode, "currentDate": currentDate};
            return $module.order.ajax($module.params.getAdjustmentStatusURL, json);
        },
        ajax:function(url,json){
            var jsonData=null;
            $custom.request.ajaxSync(url,json,function(data){
                jsonData=data.data;
            });
            return jsonData;
        }
    },
    inventory:{
    	 queryAvailability:function(depotId,goodsId,depotCode,unit,type,isExpire,batchNo){
             var json={"depotId": depotId, "goodsId": goodsId, "depotCode": depotCode,"unit":unit,"type":type,"isExpire":isExpire,"batchNo":batchNo};
             return $module.inventory.ajax($module.params.queryAvailabilityURL,json);
         },
        ajax:function(url,json){
            var num=null;
            $custom.request.ajaxSync(url,json,function(data){
                if(data.status=="1"){
                    num=data.num;
                }else{
                    num=-1;
                }
            });
            return num;
        },
    },
    supplier:{
        loadSelectData:function(id){
            var selectObj=$(this);
            selectObj.html("<option value=''>请选择</option>") ;
            
            var width = "100%" ;
            if($(selectObj).hasClass("input_msg")){
            	width = "136px" ;
            	
            	$(selectObj).removeClass("input_msg") ;
            } else if($(selectObj).hasClass("edit_input")){
            	width = "70%" ;
            	
            	$(selectObj).removeClass("edit_input") ;
            }
            
            $(selectObj).wrap('<div style="width:'+ width +'; display: inline-block;"></div>') ;
            
            $(selectObj).addClass("selectpicker").addClass("show-tick") ;
            $(selectObj).attr("data-live-search", "true") ;
            
            var jsonData=$module.depot.ajax($module.params.getSupplierByMerchantURL);

            jsonData.forEach(function(o,i){
                selectObj.append("<option value='"+ o.value+"'>"+o.label+"</option>");
            });
            
            $(selectObj).selectpicker({width: width});
			$(selectObj).selectpicker('refresh');
			
			$(".selectpicker").prev().prev().css({"height": "30px","margin-bottom": "5px","border": "1px solid #dadada","background": "white"}) ;
		    $(".selectpicker").prev().css({"z-index":"99"});
		    $(".selectpicker").prev().find(".dropdown-menu.inner").css({"height":"220px"}) ;
            
            if(id!=null&&id!=""&&id!=undefined){
                //selectObj.find("option[value = '"+id+"']").attr("selected","selected");
            	
            	$(selectObj).selectpicker('val', id);
            }
            
        },
        loadAllSelectData:function(id){
            var selectObj=$(this);
            selectObj.html("<option value=''>请选择</option>") ;
            
            var width = "100%" ;
            if($(selectObj).hasClass("input_msg")){
            	width = "136px" ;
            	
            	$(selectObj).removeClass("input_msg") ;
            } else if($(selectObj).hasClass("edit_input")){
            	width = "70%" ;
            	
            	$(selectObj).removeClass("edit_input") ;
            }
            
            $(selectObj).wrap('<div style="width:'+ width +'; display: inline-block;"></div>') ;
            
            $(selectObj).addClass("selectpicker").addClass("show-tick") ;
            $(selectObj).attr("data-live-search", "true") ;
            
            var jsonData=$module.depot.ajax($module.params.getAllSupplierURL);

            jsonData.forEach(function(o,i){
                selectObj.append("<option value='"+ o.value+"'>"+o.label+"</option>");
            });
            
            $(selectObj).selectpicker({width: width});
			$(selectObj).selectpicker('refresh');
			
			$(".selectpicker").prev().prev().css({"height": "30px","margin-bottom": "5px","border": "1px solid #dadada","background": "white"}) ;
		    $(".selectpicker").prev().css({"z-index":"99"});
		    $(".selectpicker").prev().find(".dropdown-menu.inner").css({"height":"220px"}) ;
            
            if(id!=null&&id!=""&&id!=undefined){
                //selectObj.find("option[value = '"+id+"']").attr("selected","selected");
            	
            	$(selectObj).selectpicker('val', id);
            }
            
        },
        loadSelectDataByMerchantId:function(id, merchantId){
            var selectObj=$(this);
            selectObj.html("<option value=''>请选择</option>") ;
            
            var width = "100%" ;
            if($(selectObj).hasClass("input_msg")){
            	width = "136px" ;
            	
            	$(selectObj).removeClass("input_msg") ;
            } else if($(selectObj).hasClass("edit_input")){
            	width = "70%" ;
            	
            	$(selectObj).removeClass("edit_input") ;
            }
            
            $(selectObj).wrap('<div style="width:'+ width +'; display: inline-block;"></div>') ;
            
            $(selectObj).addClass("selectpicker").addClass("show-tick") ;
            $(selectObj).attr("data-live-search", "true") ;
            
            var jsonData=$module.depot.ajax($module.params.getSupplierByMerchantIdURL, {"merchantId": merchantId});

            jsonData.forEach(function(o,i){
                selectObj.append("<option value='"+ o.value+"'>"+o.label+"</option>");
            });
            
            $(selectObj).selectpicker({width: width});
			$(selectObj).selectpicker('refresh');
			
			$(".selectpicker").prev().prev().css({"height": "30px","margin-bottom": "5px","border": "1px solid #dadada","background": "white"}) ;
		    $(".selectpicker").prev().css({"z-index":"99"});
		    $(".selectpicker").prev().find(".dropdown-menu.inner").css({"height":"220px"}) ;
            
            if(id!=null&&id!=""&&id!=undefined){
                //selectObj.find("option[value = '"+id+"']").attr("selected","selected");
            	
            	$(selectObj).selectpicker('val', id);
            }
            
        },
        ajax:function(url,json){
            var jsonData=null;
            $custom.request.ajaxSync(url,json,function(data){
                jsonData=data.data;
            });
            return jsonData;
        },
        loadSelectDataByMainId:function(id,mainId){
            var selectObj=$(this);
            var json={"mainId":mainId};
            var jsonData=$module.supplier.ajax($module.params.getSupplierMerchantRelByMainIdURL,json);
            selectObj.empty();
            selectObj.append("<option value=''>请选择</option>");
            jsonData.forEach(function(o,i){
                selectObj.append("<option value='"+ o.merchantId+"'>"+o.merchantName+"</option>");
            });
            if(id!=null&&id!=""&&id!=undefined){
                selectObj.find("option[value = '"+id+"']").attr("selected","selected");
            }
        },
        /**
         * 根据主数据id和商家获取供应商信息
         */
        getCustomerByMainMerchantId:function(mainId,merchantId){
            var jsonData=$module.customer.ajax($module.params.getCustomerByMainMerchantIdURL,{mainId:mainId,merchantId:merchantId});
            return jsonData;
        },
    },
    customer:{
        loadSelectData:function(id,type){
            var selectObj=$(this);
            var json={"type": type};
            var jsonData=$module.customer.ajax($module.params.getCustomerByMerchantURL,json);
            selectObj.empty();
            selectObj.append("<option value=''>请选择</option>");
            
            var width = "100%" ;
            if($(selectObj).hasClass("input_msg")){
            	width = "136px" ;
            	
            	$(selectObj).removeClass("input_msg") ;
            } else if($(selectObj).hasClass("edit_input")){
            	width = "70%" ;
            	
            	$(selectObj).removeClass("edit_input") ;
            }
            
            $(selectObj).wrap('<div style="width:'+ width +'; display: inline-block;"></div>') ;
            
            $(selectObj).addClass("selectpicker").addClass("show-tick") ;
            $(selectObj).attr("data-live-search", "true") ;
            
            jsonData.forEach(function(o,i){
                selectObj.append("<option value='"+ o.value+"'>"+o.label+"</option>");
            });
            
            $(selectObj).selectpicker({width: width});
			$(selectObj).selectpicker('refresh');
			
			$(".selectpicker").prev().prev().css({"height": "30px","margin-bottom": "5px","border": "1px solid #dadada","background": "white"}) ;
		    $(".selectpicker").prev().css({"z-index":"99"});
		    $(".selectpicker").prev().find(".dropdown-menu.inner").css({"height":"220px"}) ;
            
            if(id!=null&&id!=""&&id!=undefined){
                //selectObj.find("option[value = '"+id+"']").attr("selected","selected");
            	
            	$(selectObj).selectpicker('val', id);
            }
            
        },
        /*多客户回显*/
        loadSelectDataIds:function(ids,type){        
            var selectObj=$(this);
            var json={"type": type};
            var jsonData=$module.customer.ajax($module.params.getCustomerByMerchantURL,json);
            selectObj.empty();
            selectObj.append("<option value=''>请选择</option>");
            
            var width = "100%" ;
            if($(selectObj).hasClass("input_msg")){
            	width = "136px" ;
            	
            	$(selectObj).removeClass("input_msg") ;
            } else if($(selectObj).hasClass("edit_input")){
            	width = "70%" ;
            	
            	$(selectObj).removeClass("edit_input") ;
            }
            
            $(selectObj).wrap('<div style="width:'+ width +'; display: inline-block;"></div>') ;
            
            $(selectObj).addClass("selectpicker").addClass("show-tick") ;
            $(selectObj).attr("data-live-search", "true") ;
            
            jsonData.forEach(function(o,i){
                selectObj.append("<option value='"+ o.value+"'>"+o.label+"</option>");
            });
            
            $(selectObj).selectpicker({width: width});
			$(selectObj).selectpicker('refresh');
			
			$(".selectpicker").prev().prev().css({"height": "30px","margin-bottom": "5px","border": "1px solid #dadada","background": "white"}) ;
		    $(".selectpicker").prev().css({"z-index":"99"});
		    $(".selectpicker").prev().find(".dropdown-menu.inner").css({"height":"220px"}) ;
            
            if(ids!=null&&ids!=""&&ids!=undefined){
                //selectObj.find("option[value = '"+id+"']").attr("selected","selected");
            	var idsArr=ids.split(',');
                $(selectObj).selectpicker('val', idsArr);
            	//$(selectObj).selectpicker('val', id);
            }
            
        },
        /*获取客户或者供应商 下拉框  ids不为空回显 id为ids的客户 多个id用逗号隔开
		 * 1.查询全部客户(包含启用禁用客户 客户列表用)
		 * 2.查询全部供应商 (包含启用禁用供应商列表用)
		 * 3.查询启用客户(全部)
		 * 4.查询启用供应商(全部)
		 * 5.查询当前商家的启用客户
		 * 6.查询当前商家启用供应商 
         * */
        getCusOrSurpSelectBean:function(typeFlag,ids){  
        	debugger;
            var selectObj=$(this);
            var json={"typeFlag": typeFlag};
            var jsonData=$module.customer.ajax($module.params.getCusOrSurpSelectBeanURL,json);
            selectObj.empty();
            selectObj.append("<option value=''>请选择</option>");            
            var width = "100%" ;
            if($(selectObj).hasClass("input_msg")){
            	width = "136px" ;          	
            	$(selectObj).removeClass("input_msg") ;
            } else if($(selectObj).hasClass("edit_input")){
            	width = "70%" ;            	
            	$(selectObj).removeClass("edit_input") ;
            }
            
            $(selectObj).wrap('<div style="width:'+ width +'; display: inline-block;"></div>') ;
            
            $(selectObj).addClass("selectpicker").addClass("show-tick") ;
            $(selectObj).attr("data-live-search", "true") ;
            
            jsonData.forEach(function(o,i){
                selectObj.append("<option value='"+ o.value+"'>"+o.label+"</option>");
            });
            
            $(selectObj).selectpicker({width: width});
			
			
			$(".selectpicker").prev().prev().css({"height": "30px","margin-bottom": "5px","border": "1px solid #dadada","background": "white"}) ;
		    $(".selectpicker").prev().css({"z-index":"99"});
		    $(".selectpicker").prev().find(".dropdown-menu.inner").css({"height":"220px"}) ;
            
            if(ids!=null&&ids!=""&&ids!=undefined){
                //selectObj.find("option[value = '"+id+"']").attr("selected","selected");
            	var idsArr=ids.split(',');
                $(selectObj).selectpicker('val', idsArr);
            	//$(selectObj).selectpicker('val', id);
            }
            $(selectObj).selectpicker('refresh');
            $(selectObj).selectpicker('render');
            
            
        },
        ajax:function(url,json){
            var jsonData=null;
            $custom.request.ajaxSync(url,json,function(data){
                jsonData=data.data;
            });
            return jsonData;
        },
        loadSelectDataByDto:function(id,cusType,name){
            var selectObj=$(this);
            var json={"cusType": cusType,"name": name};
            //var json={"name": name};
            var jsonData=$module.customer.ajax($module.params.getCustomerByDTOURL,json);
            selectObj.empty();
            selectObj.append("<option value=''>请选择</option>");
            jsonData.forEach(function(o,i){
                selectObj.append("<option value='"+ o.id+"'>"+o.name+"</option>");
            });
            if(id!=null&&id!=""&&id!=undefined){
                selectObj.find("option[value = '"+id+"']").attr("selected","selected");
            }

        },
        loadSelectDataByMerchantId:function(id,cusType,merchantId,mainId){
            var selectObj=$(this);
            var json={"merchantId": merchantId,"cusType": cusType,"mainId":mainId};
            var jsonData=$module.customer.ajax($module.params.getCustomerByMerchantId,json);
            selectObj.empty();
            selectObj.append("<option value=''>请选择</option>");
            jsonData.forEach(function(o,i){
                selectObj.append("<option value='"+ o.id+"'>"+o.name+"</option>");
            });
            if(id!=null&&id!=""&&id!=undefined){
                selectObj.find("option[value = '"+id+"']").attr("selected","selected");
            }
        },
        /**
         * 根据id获取客户信息
         */
        getCustomerById:function(id){
            var jsonData=$module.customer.ajax($module.params.getCustomerByIdURL,{id:id});
            return jsonData;
        },
    },
    customsAreaConfig:{       
        /*多关区回显*/
        loadSelectDataIds:function(ids,type){        
            var selectObj=$(this);
            var json={"type": type};
            var jsonData=$module.customer.ajax($module.params.getCustomsSelectBean,json);
            selectObj.empty();
            selectObj.append("<option value=''>请选择</option>");
            
            var width = "100%" ;
            if($(selectObj).hasClass("input_msg")){
            	width = "136px" ;
            	
            	$(selectObj).removeClass("input_msg") ;
            } else if($(selectObj).hasClass("edit_input")){
            	width = "70%" ;
            	
            	$(selectObj).removeClass("edit_input") ;
            }
            
            $(selectObj).wrap('<div style="width:'+ width +'; display: inline-block;"></div>') ;
            
            $(selectObj).addClass("selectpicker").addClass("show-tick") ;
            $(selectObj).attr("data-live-search", "true") ;
            
            jsonData.forEach(function(o,i){
                selectObj.append("<option value='"+ o.value+"'>"+o.label+"</option>");
            });
            
            $(selectObj).selectpicker({width: width});
			$(selectObj).selectpicker('refresh');
			
			$(".selectpicker").prev().prev().css({"height": "30px","margin-bottom": "5px","border": "1px solid #dadada","background": "white"}) ;
		    $(".selectpicker").prev().css({"z-index":"99"});
		    $(".selectpicker").prev().find(".dropdown-menu.inner").css({"height":"220px"}) ;
            
            if(ids!=null&&ids!=""&&ids!=undefined){
                //selectObj.find("option[value = '"+id+"']").attr("selected","selected");
            	var idsArr=ids.split(',');
                $(selectObj).selectpicker('val', idsArr);
            	//$(selectObj).selectpicker('val', id);
            }
            
        },
        ajax:function(url,json){
            var jsonData=null;
            $custom.request.ajaxSync(url,json,function(data){
                jsonData=data.data;
            });
            return jsonData;
        },       
    },
    
    merchant:{
        loadSelectData:function(id){
            var selectObj=$(this);
            var jsonData=$module.merchant.ajax($module.params.getMerchantRelURL);

            jsonData.forEach(function(o,i){
                selectObj.append("<option value='"+ o.value+"'>"+o.label+"</option>");
            });
            if(id!=null&&id!=""&&id!=undefined){
                selectObj.find("option[value = '"+id+"']").attr("selected","selected");
            }
            
        },
        getUserMerchantSelectBeanURL:function(ids){// 获取商家下拉框  传ids 多选 回显
        	debugger;
            var selectObj=$(this);
            var jsonData=$module.merchant.ajax($module.params.getUserMerchantSelectBeanURL);
            $(selectObj).addClass("selectpicker").addClass("show-tick") ;
            $(selectObj).attr("data-live-search", "true") 
            jsonData.forEach(function(o,i){
                selectObj.append("<option value='"+ o.value+"'>"+o.label+"</option>");
            });
            if(ids!=null&&ids!=""&&ids!=undefined){
            	var idsArr=ids.split(',');
                $(selectObj).selectpicker('val', idsArr);
            }
            $(selectObj).selectpicker('refresh');
            $(selectObj).selectpicker('render');
        },
        
        ajax:function(url){
            var jsonData=null;
            $custom.request.ajaxSync(url,null,function(data){
                jsonData=data.data;
            });
            return jsonData;
        },
    },
    brand:{
    	/**加载品牌下拉框*/
        loadSelectData:function(id){
            var selectObj=$(this);
            var jsonData=$module.brand.ajax($module.params.getBrandURL);

            jsonData.forEach(function(o,i){
                selectObj.append("<option value='"+ o.value+"'>"+o.label+"</option>");
            });
            if(id!=null&&id!=""&&id!=undefined){
                selectObj.find("option[value = '"+id+"']").attr("selected","selected");
            }
            
        },
        /**加载父品牌下拉框*/
        loadParentBrandSelect:function(ids){
            var selectObj=$(this);
            var jsonData=$module.brand.ajax($module.params.getBrandParentURL);
            selectObj.empty();
            selectObj.append("<option value=''>请选择</option>");
            
            var width = "100%" ;
            if($(selectObj).hasClass("input_msg")){
            	width = "136px" ;
            	
            	$(selectObj).removeClass("input_msg") ;
            } else if($(selectObj).hasClass("edit_input")){
            	width = "70%" ;
            	
            	$(selectObj).removeClass("edit_input") ;
            }
            
            $(selectObj).wrap('<div style="width:'+ width +'; display: inline-block;"></div>') ;
            
            $(selectObj).addClass("selectpicker").addClass("show-tick") ;
            $(selectObj).attr("data-live-search", "true") ;
            
            jsonData.forEach(function(o,i){
                selectObj.append("<option value='"+ o.value+"'>"+o.label+"</option>");
            });
            
            $(selectObj).selectpicker({width: width});
			$(selectObj).selectpicker('refresh');
			
			$(".selectpicker").prev().prev().css({"height": "30px","margin-bottom": "5px","border": "1px solid #dadada","background": "white"}) ;
		    $(".selectpicker").prev().css({"z-index":"99"});
		    $(".selectpicker").prev().find(".dropdown-menu.inner").css({"height":"220px"}) ;
            
            if(ids!=null&&ids!=""&&ids!=undefined){
            	var idsArr=ids.split(',');
                $(selectObj).selectpicker('val', idsArr);
            }
        },
        ajax:function(url){
            var jsonData=null;
            $custom.request.ajaxSync(url,null,function(data){
                jsonData=data.data;
            });
            return jsonData;
        },
    },
    relMerchantIds:{
        /**
         * 根据过滤条件加载:根据当前商家获取所有代理商家
         * @param id
         * @param json
         */
        loadRelMerchantIds:function(merchantId){
        	var json={"merchantId": merchantId};
            var jsonData=$module.relMerchantIds.ajax($module.params.getRelMerchantIdsURL,json);
            return  jsonData;
        },
        ajax:function(url,json){
            var jsonData=null;
            $custom.request.ajaxSync(url,json,function(data){
                jsonData=data.data;
            });
            return jsonData;
        },
    },
    batchValidation:{
    	/**
    	 * 根据仓库ID  查询仓库库存余量不为0的所有商品是否都有批次效期
    	 */
    	queryBatchValidation:function(depotId){
            var json={"depotId": depotId};
            return $module.batchValidation.ajax($module.params.queryBatchValidationURL,json);
        },
       ajax:function(url,json){
    	   var reslut;
           $custom.request.ajaxSync(url,json,function(data){
        	   reslut=data;
           });
           return reslut;
       },
   },
   merchandiseCat:{
	   /**获取商品分类下拉列表
	    * */
	   getMerchandiseCat:function(level,id){
		   var selectObj=$(this);
           var json={"level": level};
           var jsonData=$module.merchandiseCat.ajax($module.params.getMerchandiseCatURL,json);
           jsonData.forEach(function(o,i){
               selectObj.append("<option value='"+ o.value+"'>"+o.label+"</option>");
           });
           if(id!=null&&id!=""&&id!=undefined){
               selectObj.find("option[value = '"+id+"']").attr("selected","selected");
           }
      },
      ajax:function(url,json){
   	      var reslut;
          $custom.request.ajaxSync(url,json,function(data){
       	   reslut=data.data;
          });
          return reslut;
      },
  },
  currency : {
		/**加载币种下拉框*/
		loadSelectData:function(customerId){
		    var selectObj=$(this);
		    var jsonData=$module.currency.ajax($module.params.getCurrencyURL , {'customerId' : customerId});
		
		    if(customerId!=null&&customerId!=""&&customerId!=undefined){
		        selectObj.find("option[value = '"+jsonData+"']").prop("selected","selected");
		    	return ;
		    }
		    
		    jsonData.forEach(function(o,i){
		        selectObj.append("<option value='"+ o.selectValue+"'>"+o.selectLable+"</option>");
		    });
		    
		    
		},
		ajax:function(url,json){
		  var reslut;
		  $custom.request.ajaxSync(url,json,function(data){
		   reslut=data.data;
		  });
		  return reslut;
       }
  }, 
  shopCode : {
		/**加载客户编码下拉框*/
		loadSelectData:function(id){
		    var selectObj=$(this);
		    var jsonData=$module.shopCode.ajax($module.params.getShopCodeURL);
		
		    jsonData.forEach(function(o,i){
		        selectObj.append("<option value='"+ o.selectValue+"'>"+o.selectLable+"</option>");
		    });
            if (id != null && id != "" && id != undefined) {
                selectObj.find("option[value = '" + id + "']").attr("selected", "selected");
            }
  
		},
		ajax:function(url,json){
		  var reslut;
		  $custom.request.ajaxSync(url,json,function(data){
		   reslut=data.data;
		  });
		  return reslut;
	     }
	},
    merchantAll: {
        loadSelectData: function (id) {
            var selectObj = $(this);
            var jsonData = $module.merchant.ajax($module.params.getMerchantUrl);
            selectObj.empty();
            selectObj.append("<option value=''>请选择</option>");
            jsonData.forEach(function (o, i) {
                selectObj.append("<option value='" + o.value + "'>" + o.label + "</option>");
            });
            if (id != null && id != "" && id != undefined) {
                selectObj.find("option[value = '" + id + "']").attr("selected", "selected");
            }

        },
        listData: function () {
           return $module.merchant.ajax($module.params.getMerchantUrl);
        },
        ajax: function (url) {
            var jsonData = null;
            $custom.request.ajaxSync(url, null, function (data) {
                jsonData = data.data;
            });
            return jsonData;
        }
    },
    shopMerchantRel:{
        loadSelectData:function(id){
            var selectObj=$(this);
            var jsonData=$module.merchant.ajax($module.params.getShopMerchantRelUrl);

            jsonData.forEach(function(o,i){
                selectObj.append("<option value='"+ o.id+"'>"+o.shopName+"</option>");
            });
            if(id!=null&&id!=""&&id!=undefined){
                selectObj.find("option[value = '"+id+"']").attr("selected","selected");
            }

        },
        ajax:function(url){
            var jsonData=null;
            $custom.request.ajaxSync(url,null,function(data){
                jsonData=data.data;
            });
            return jsonData;
        },
    },
    shopPlatformRel:{
        loadSelectData:function(code, platformCode){
            var selectObj=$(this);
            var jsonData=$module.shopPlatformRel.ajax($module.params.getShopByPlatformCodeUrl, {"storePlatformCode":platformCode});

            jsonData.forEach(function(o,i){
                selectObj.append("<option value='"+ o.shopCode+"'" + "shopType='" + o.shopTypeName +"'>"+o.shopName+"</option>");
            });
            if(code!=null&&code!=""&&code!=undefined){
                selectObj.find("option[value = '"+code+"']").attr("selected","selected");
            }

        },
        ajax:function(url, json){
            var jsonData=null;
            $custom.request.ajaxSync(url,json,function(data){
                jsonData=data.data;
            });
            return jsonData;
        },
    },
    businessUnit:{
        /**
         * 根据页面传入仓库1，仓库2查询此商家下关联仓库的事业部下拉框，为空时则获取商家关联的所有事业部
         * @param
         * @param json
         */
        getSelectBeanByMerchantDepotRel:function(id,merchantId,depotId1,depotId2){
        	
            var selectObj=$(this);
            var json={"merchantId":merchantId,"depotId1":depotId1,"depotId2":depotId2};
            var jsonData=$module.businessUnit.ajax($module.params.getBUSelectBeanByMerchantDepotRel,json);
            selectObj.empty();
            selectObj.append("<option value=''>请选择</option>");
            jsonData.forEach(function(o,i){
                selectObj.append("<option value='"+ o.value+"'>"+o.label+"</option>");
            });
            if(id!=null&&id!=""&&id!=undefined){
                selectObj.find("option[value = '"+id+"']").attr("selected","selected");
            }
        },
        getSelectBeanByManyDepot:function(id,merchantId,depotIdList){
            var selectObj=$(this);
            var json={"merchantId":merchantId,"depotIdList":depotIdList};
            var jsonData=$module.businessUnit.ajax($module.params.getBUSelectBeanByManyDepot,json);
            selectObj.empty();
            selectObj.append("<option value=''>请选择</option>");
            jsonData.forEach(function(o,i){
                selectObj.append("<option value='"+ o.value+"'>"+o.label+"</option>");
            });
            if(id!=null&&id!=""&&id!=undefined){
                selectObj.find("option[value = '"+id+"']").attr("selected","selected");
            }
        },
        getListByDepot:function(merchantId,depotId1,depotId2){
        	
        	var json={"merchantId":merchantId,"depotId1":depotId1,"depotId2":depotId2};
            return $module.businessUnit.ajax($module.params.getBUSelectBeanByMerchantDepotRel,json);
        },
        getAllSelectBean:function(id){
            var selectObj=$(this);
            var json={};
            var jsonData=$module.businessUnit.ajax($module.params.getAllBuSelectBean,json);
            selectObj.empty();
            selectObj.append("<option value=''>请选择</option>");
            jsonData.forEach(function(o,i){
                selectObj.append("<option value='"+ o.value+"'>"+o.label+"</option>");
            });
            if(id!=null&&id!=""&&id!=undefined){
                selectObj.find("option[value = '"+id+"']").attr("selected","selected");
            }
        },
        ajax:function(url,json){
            var jsonData=null;
            $custom.request.ajaxSync(url,json,function(data){
                jsonData=data.data;
            });
            return jsonData;
        },
    },
    settlementConfig:{
        getSettlementConfig: function (level, customerId, type, moduleType) {
            var selectObj = $(this);
            var jsonData = $module.settlementConfig.ajax($module.params.getSettlementConfigUrl,
                {"level":level, "customerId" :customerId, "type":type, "moduleType":moduleType});
            selectObj.empty();
            selectObj.append("<option value=''>请选择</option>");
            jsonData.forEach(function (o, i) {
                selectObj.append("<option value='" + o.value + "'>" + o.label + "</option>");
            });
        },
        ajax:function(url,json){
            var jsonData=null;
            $custom.request.ajaxSync(url,json,function(data){
                jsonData=data.data;
            });
            return jsonData;
        },
    },
    inventoryLocationMapping: {
    	getOriginalGoodsId: function(goodsId, goodsNo){
    		var json = {"goodsNo": goodsNo, "goodsId": goodsId, "type": "originalGoodsId" } ;
    		var url = $module.params.inventoryLocationMappingURL ;
    		
    		var originalGoodsId = $module.inventoryLocationMapping.ajax(url, json);

    		
    		return originalGoodsId ;
    		
    	},
    	getOriginalGoodsNo: function(goodsId, goodsNo){
    		var json = {"goodsNo": goodsNo, "goodsId": goodsId, "type": "originalGoodsNo" } ;
    		var url = $module.params.inventoryLocationMappingURL ;
    		
    		var originalGoodsNo = $module.inventoryLocationMapping.ajax(url, json);

    		
    		return originalGoodsNo ;
    		
    	},
    	ajax:function(url,json){
            var jsonData=null;
            $custom.request.ajaxSync(url,json,function(data){
                if(data.state == '200'){
                	if(data.data.originalGoodsId && json['type'] == 'originalGoodsId'){
                		jsonData = data.data.originalGoodsId ;
                	}else if(data.data.originalGoodsNo && json['type'] == 'originalGoodsNo'){
                		jsonData = data.data.originalGoodsNo ;
                	}
                }else{
                	if(data.expMessage != null){
						$custom.alert.error(data.expMessage);
					}else{
						$custom.alert.error(data.message);
					}
                }
            });
            return jsonData;
        },
    },
  /*------------------------2019-07-22web页面列表跳转缓存对象开始-------------------------------*/
  //编辑，详细页返回列表列表
  revertList : {
  	//是否主列表页面
  	isMainList : true ,
  	//列表查询表单参数
  	params : [] ,
  	//序列化列表参数
  	serializeListForm : function(formId){
  		this.params = [] ;
  		
  		if(!formId){
  			formId = 'form1' ;
  		}
  		
  		var listPageInfo = $mytable.fn.getCurrentPageInfo() ;
  		this.params = $("#" + formId).serializeArray() ; 
  		
  		for (var i = 0 ; i < listPageInfo.length ; i ++){
  			this.params.push(listPageInfo[i]) ; 
  		} 
  	} 
  	
  },
  /*------------------------2019-07-22web页面列表跳转缓存对象结束-------------------------------*/
    params:{
        // 根据仓库id获取仓库附表URL
        getAllDepotScheduleURL:'/depotSchedule/loadSelectByDepotId.asyn',
        //通过ID获取仓库信息URL
        getAllDepotURL:'/depot/getSelectBean.asyn',
        //通过当前商家ID获取所有代理商家id
        getRelMerchantIdsURL:'/merchant/getRelMerchantIds.asyn',
        //获取所有币种信息
        getCurrencyURL:'/customer/getCurrency.asyn',
        //获取所有店铺编码信息
        getShopCodeURL:'/customer/getShopCode.asyn',
        //根据id获取客户信息
        getCustomerByIdURL:'/customer/getCustomerById.asyn',
        // 根据主数据id和商家获取供应商信息
        getCustomerByMainMerchantIdURL:'/customer/getCustomerByMainMerchantId.asyn',
        // 根据商家/类型获取客户下拉框
        getCustomerByMerchantId:'/customer/getCustomerByMerchantId.asyn',
        //根据是否同关区获取仓库信息
        getDepotByAreaURL:'/depot/getSelectBeanByArea.asyn',
        //根据id获取仓库信息
        getDepotByIdURL:'/depot/getDepotById.asyn',
        //根据页面传入商家id、进出接口指令、统计存货跌价、选品限制、已入定出、已出定入获取此商家下仓库的下拉框
        getSelectBeanByMerchantRelURL:'/depot/getSelectBeanByMerchantRel.asyn',
        //根据名称获取常量集合
        getConstantListByNameURL:'/getConstantListByName.asyn',
        //查询可用库存
        queryAvailabilityURL:inventoryWebhost+'/queryAvailability/getAvailableNum.asyn',
        //获取库存调整单信息
        getAdjustmentStatusURL:storageWebhost+'/adjustmentInventory/getAdjustmentStatus.asyn',
        //通过id获取当前商家的所有供应商的url
        getSupplierByMerchantURL:'/supplier/getSelectBean.asyn',
        //根据商家ID获取供应商的url
        getSupplierByMerchantIdURL:'/supplier/getSupplierByMerchantId.asyn',
        //获取所有供应商的url
        getAllSupplierURL:'/supplier/getAllSelectBean.asyn',
        //通过主数据id
        getSupplierMerchantRelByMainIdURL:'/supplier/getSupplierMerchantRelByMainIdURL.asyn',
        //通过id获取当前商家的所有供应商的url
        getCustomerByMerchantURL:'/customer/getSelectBean.asyn',
        //获取客户或供应商的公共方法
        getCusOrSurpSelectBeanURL:'/customer/getCusOrSurpSelectBean.asyn',
        //获取当前商家的所有代理商家及自己
        getMerchantRelURL:'/merchant/getMerchantSelectBean.asyn',
        //获取当前商家的所有代理商家及自己
        getUserMerchantSelectBeanURL:'/merchant/getUserMerchantSelectBean.asyn',
        //获取商品分类
        getMerchandiseCatURL:'/merchandise/getMerchandiseCatList.asyn',
        //获取品牌信息URL
        getBrandURL:'/brand/getSelectBean.asyn',
        //获取父品牌信息URL
        getBrandParentURL:'/brandParent/getSelectBean.asyn',
        //加载业务子模块URL   订单模块
        loadOrderPageURL:'/module/loadPage.asyn?url='+orderWebhost+'/load/page.asyn?',
        //加载子模块URL      库存模块
        loadInventoryPageURL:'/module/loadPage.asyn?url='+inventoryWebhost+'/load/page.asyn?',
        //加载子模块URL      仓储模块
        loadStoragePageURL:'/module/loadPage.asyn?url='+storageWebhost+'/load/page.asyn?',
        //加载子模块URL      报表
        loadReportPageURL:'/module/loadPage.asyn?url='+reportWebhost+'/load/page.asyn?',
        //根据仓库ID  查询仓库库存余量不为0的所有商品是否都有批次效期
        queryBatchValidationURL:inventoryWebhost+'/inventoryBatch/getBatchValidation.asyn',
        //获取所有商家
        getMerchantUrl:'/merchant/getSelectBean.asyn',
        //获取店铺
        getShopMerchantRelUrl:'/merchantShop/getSelectBean.asyn',
        //根据平台获取对应店铺
        getShopByPlatformCodeUrl:'/merchantShop/getShopByPlatformCodeUrl.asyn',
        //根据页面传入仓库1，仓库2查询此商家下关联仓库的事业部下拉框，为空时则获取商家关联的所有事业部
        getBUSelectBeanByMerchantDepotRel:'/businessUnit/getBUSelectBeanByMerchantDepotRel.asyn',
        //根据页面传入的多个仓库查询此商家下关联仓库的事业部下拉框，为空时则获取商家关联的所有事业部
        getBUSelectBeanByManyDepot:'/businessUnit/getBUSelectBeanByManyDepot.asyn',
        //获取所有事业部
        getAllBuSelectBean:'/businessUnit/getAllSelectBean.asyn',
        getSettlementConfigUrl:orderWebhost+'/settlementConfig/getSelectBean.asyn',
        //通过查询条件获取当前商家的客户信息的url
        getCustomerByDTOURL:'/customer/getSelectBeanByDto.asyn',
        //获取发票模板信息url
        listFileTempInfoURL:orderWebhost+'/fileTemp/listFileTempInfo.asyn',
        inventoryLocationMappingURL:'/inventoryLocationMapping/getOriginalGoodsId.asyn',
        getBrandParentOrderURL:'/brandParent/getBrandParent.asyn',
        //获取仓库信息URL
        getSelectBeanByDTOURL:'/depot/getSelectBeanByDTO.asyn',
        //获取仓库关联关区URL
        getSelectBeanByDepotCustomsRelURL:'/webapi/system/customsArea/getSelectBean.asyn',
        // 获取关区下拉框数据
        getCustomsSelectBean:'/webapi/system/customsArea/getCustomsSelectBean.asyn',
        //根据页面传入商家id、事业部id、仓库类别、是否代客管理仓库、是否是代销仓获取此商家事业部下仓库的下拉框
        getSelectBeanByMerchantRelBuURL:'/depot/getSelectBeanByMerchantBuRel.asyn',
        //公共配置
        //跳转的ip和端口（order） 3202  3201  3203
        serverAddrByOrder:orderWebhost,
        serverAddrByInventory:inventoryWebhost,
        serverAddrByStorage:storageWebhost,
        serverAddrByReport:reportWebhost,
    }

};