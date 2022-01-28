/**
 * json  基本操作
 * 添加  查询    删除   拼接
 * @type {{fn: {setJson: $json.fn.setJson, deleteJson: $json.fn.deleteJson, getJson: $json.fn.getJson, concat: $json.fn.concat}}}
 */
var $json={
        /**
         * 添加
         * 返回  json字符串
         * @param jsonStr  字符串
         * @param name     key值
         * @param value    value值
         */
        setJson:function(jsonStr,name,value){
            if(!jsonStr)jsonStr="{}";
            var jsonObj = JSON.parse(jsonStr);
            jsonObj[name] = value;
            return JSON.stringify(jsonObj)
        },
        /**
         *删除
         * @param jsonStr     json字符串
         * @param name        key值
         * @returns {null}    json字符串
         */
        deleteJson:function(jsonStr,name){
            if(!jsonStr)return null;
            var jsonObj = JSON.parse(jsonStr);
            delete jsonObj[name];
            return JSON.stringify(jsonObj);
        },
        /**
         *查询
         * @param jsonStr
         * @param name
         * @returns {*}
         */
        getJson:function(jsonStr,name){
            if(!jsonStr)return null;
            var jsonObj=null;
            if((typeof jsonStr)=='string'){
                jsonObj = JSON.parse(jsonStr);
            }
            return jsonObj[name];
        },
        concat:function(jsonStr1,jsonStr2){
            if(!jsonStr1)jsonStr1=JSON.parse("{}");
            for(var key in jsonStr2){
                jsonStr1[key]=jsonStr2[key];
            }
            return jsonStr1;
        }

};

/**
 * 基本数据计算工具类
 * 1、浮点类型   加  减  乘  除
 * @type {{accAdd: $match.accAdd, accSub: $match.accSub, accDiv: $match.accDiv, accMul: $match.accMul}}
 */
var $match={
        /**
         * 两数相加
         * @param num1
         * @param num2
         * @returns {number}
         */
        accAdd:function(num1,num2){
            var r1,r2,m;
            try{
                r1 = num1.toString().split('.')[1].length;
            }catch(e){
                r1 = 0;
            }
            try{
                r2=num2.toString().split(".")[1].length;
            }catch(e){
                r2=0;
            }
            m=Math.pow(10,Math.max(r1,r2));
            return Math.round(num1*m+num2*m)/m;
        },
        /**
         * 乘
         * @param num1
         * @param num2
         * @returns {number}
         */
        accMul:function(num1,num2){
            var m=0,s1=num1.toString(),s2=num2.toString();
            try {
                m += s1.split(".")[1].length
            } catch (e) {
            }
            try {
                m += s2.split(".")[1].length
            } catch (e) {
            }
            return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
        },

        /**
         * 两数相减
         * @param num1
         * @param num2
         * @returns {string}
         */
        accSub:function(num1,num2){
            var r1,r2,m;
            try{
                r1 = num1.toString().split('.')[1].length;
            }catch(e){
                r1 = 0;
            }
            try{
                r2=num2.toString().split(".")[1].length;
            }catch(e){
                r2=0;
            }
            m=Math.pow(10,Math.max(r1,r2));
            n=(r1>=r2)?r1:r2;
            return (Math.round(num1*m-num2*m)/m).toFixed(n);
        },
        /**
         * 除
         * @param num1
         * @param num2
         * @returns {number}
         */
        accDiv:function(num1,num2){
            var t1,t2,r1,r2;
            try{
                t1 = num1.toString().split('.')[1].length;
            }catch(e){
                t1 = 0;
            }
            try{
                t2=num2.toString().split(".")[1].length;
            }catch(e){
                t2=0;
            }
            r1=Number(num1.toString().replace(".",""));
            r2=Number(num2.toString().replace(".",""));
            return (r1/r2)*Math.pow(10,t2-t1);
        }


    };

/**
 * 页面校验器
 * 
 */
var $checker={
		/* 表单必填项校验 例子：<input type="text" required reqMsg = "车次不能为空" parsley-type="text" class="edit_input" name="trainNumber" id="trainNumber">
		/ 	若表单元素 需必填项校验 只需添加属性 required 、 reqMsg， reqMsg为错误提示信息
		/ 	必填通过返回 true 否则返回false */ 
		verificationRequired: function(){
			var reqrs = $('*[required]') ;
			//是否校验成功
			var flagLength = 0  ;
			var content = "" ;
			$(reqrs).each(function(index , reqr){
				
				$(reqr).css("border-color", "") ;
				
				var val = $(reqr).val();
				
				if(val){
					flagLength++ ;
				}else {
					content = $(reqr).attr("reqMsg") ;
					
					if(!content){
						content = "请检查必填项" ;
					}
					
					$(reqr).css("border-color", "red") ;
					
					var top = $(reqr).offset() ; 
					// 滑动
					$("html,body").animate({scrollTop:top.top - "150" + "px"}, 500);
					
					// 震动
					$(reqr).shake(8, 10, 1500); ;
					
					return false ;
				}
			});
			
			//检查必填项是否全部已输入，否则提示
			if(flagLength != $(reqrs).length){
				$custom.alert.error(content);
				
				return false ;
			}
			
			return true ;
		}
	};

	// 页面文本框抖动
	jQuery.fn.shake = function (intShakes /*Amount of shakes*/, intDistance /*Shake distance*/, intDuration /*Time duration*/) {
	    this.each(function () {
	        var jqNode = $(this);
	        jqNode.css({ position: 'relative' });
	        for (var x = 1; x <= intShakes; x++) {
	            jqNode.animate({ left: (intDistance * -1) }, (((intDuration / intShakes) / 4)))
	            .animate({ left: intDistance }, ((intDuration / intShakes) / 2))
	            .animate({ left: 0 }, (((intDuration / intShakes) / 4)));
	        }
	    });
	    return this;
	}
