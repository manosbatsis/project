/**定义表格对象*/
var $myTree={};
/**自定义  表格 插件*/
$.fn.extend({
    myTree:function(setting,checkbox){
        $myTree.fn.init.call(this,setting,checkbox);
    }
});

//配置参数
$myTree.params={
    zTreeObj:null,
    chkboxType:{ "Y" : "ps", "N" : "s" }
};

$myTree.fn={
        /**
         * 初始化
         * @param setting
         */
        init:function(setting,checkbox){
            $.fn.zTree.init($(this), setting);
            //获取tree对象
            $myTree.params.zTreeObj=$.fn.zTree.getZTreeObj($(this).attr("id"));
            if(checkbox){
                $myTree.params.zTreeObj.setting.check.chkboxType=$myTree.params.chkboxType;
            }
        },
        /**
         * 获取选中ID
         */
        getCheckedIds:function(){
            var ids=[];
            var nodes= $myTree.params.zTreeObj.getCheckedNodes(true);
            for(var i=0;i<nodes.length;i++){
                ids.push(nodes[i].id);
            }
            return ids.join(",");
        },
        /**
         * 取消所有选中
         */
        cancelAllChecked:function(){
            $myTree.params.zTreeObj.checkAllNodes(false);
        },
        /**
         * 选中指定的ID
         */
        checkById:function(ids){
            for(var i=0;i<ids.length;i++){
                var nodes=$myTree.params.zTreeObj.getNodeByParam("id",ids[i]);
                if(nodes) {
                    $myTree.params.zTreeObj.checkNode(nodes,true);
                }

            }
        },
        /**
         * 展开所有的节点
         */
        expandAll:function(){
           $myTree.params.zTreeObj.expandAll(true);
        },
        refresh:function(){
            $myTree.params.zTreeObj.reAsyncChildNodes(null, "refresh");
        }
};


