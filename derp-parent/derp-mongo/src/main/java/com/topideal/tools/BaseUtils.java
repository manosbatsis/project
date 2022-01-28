package com.topideal.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;

/**
 * Created by weixiaolei on 2018/1/9.
 */
public class BaseUtils {

    /**
     * map 转  Criteria
     * @param params
     * @return
     */
    public static Criteria parseCriteria(Map<String,Object> params){
       Criteria criteria=null;
        //注入过滤条件
        for (Map.Entry<String, Object> entry : params.entrySet()) {
        	if(criteria == null){
        		criteria=Criteria.where(entry.getKey()).is(entry.getValue());
        	}else{
        		criteria=criteria.and(entry.getKey()).is(entry.getValue());
        	}
        }
       return criteria;
    }
    /**
     * map 转  Criteria
     * @param params
     * @return
     */
    public static Criteria parseCriteriaAndDate(Map<String,Object> params){
        Criteria criteria=null;
        //注入过滤条件
        for (Map.Entry<String, Object> entry : params.entrySet()) {
        	if("differenceTime".equals(entry.getKey())){
        		if(criteria == null){
	                //小于0.5秒
	                if("1".equals(entry.getValue())){
	                    criteria=Criteria.where(entry.getKey()).lt(0.5);
	                }
	                //0.5秒-1秒内
	                else if("2".equals(entry.getValue())){
	                    criteria=Criteria.where(entry.getKey()).gte(0.5).lte(1.0);
	                }
	                //大于1秒
	                else{
	                	criteria=Criteria.where(entry.getKey()).gt(1.0);
	                }
	            } else {
	            	//小于0.5秒
	                if("1".equals(entry.getValue())){
	                    criteria=criteria.and(entry.getKey()).lt(0.5);
	                }
	                //0.5秒-1秒内
	                else if("2".equals(entry.getValue())){
	                    criteria=criteria.and(entry.getKey()).gte(0.5).lte(1.0);
	                }
	                //大于1秒
	                else{
	                	criteria=criteria.and(entry.getKey()).gt(1.0);
	                }
	            }
        	} else if("ltEndDate".equals(entry.getKey())){
        		if(criteria == null){
	                criteria=Criteria.where("endDate").lt(entry.getValue());
	            } else {
	                criteria=criteria.and("endDate").lt(entry.getValue());
	            }
        	} else if("ltcreateDate".equals(entry.getKey())){
        		if(criteria == null){
	                criteria=Criteria.where("createDate").lt(entry.getValue());
	            } else {
	                criteria=criteria.and("createDate").lt(entry.getValue());
	            }
        	}else{
        		if(criteria == null){
	                //时间类型
	                if(entry.getValue() instanceof List){
	                    List dates=(List) entry.getValue();
	                    criteria=Criteria.where(entry.getKey()).gte(dates.get(0)).lt(dates.get(1));
	                    continue;
	                }
	                criteria=Criteria.where(entry.getKey()).is(entry.getValue());
	            } else {
	                //时间类型
	                if(entry.getValue() instanceof List){
	                    List dates=(List) entry.getValue();
	                    criteria=criteria.and(entry.getKey()).gte(dates.get(0)).lt(dates.get(1));
	                    continue;
	                }
	                criteria=criteria.and(entry.getKey()).is(entry.getValue());
	            }
        	}
        }
        return criteria;
    }

    /**
     * map  转  Update
     * @return
     */
    public static Update parseUpdate(Map<String,Object> data){
        Update update=new Update();
        //注入过滤条件
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            update = update.set(entry.getKey(), entry.getValue());
        }
        return update;
    }


    /**
     * map 转  Criteria(支持大于、小于、不等于、不存在等条件)
     * @param params -- Operator(操作类型),map(key:属性名   vaule:值)
     * @return
     */
    public static Criteria customParseCriteria(Map<Operator,Map<String,Object>> params){
       Criteria criteria=null;
        //注入过滤条件
        for (Map.Entry<Operator, Map<String,Object>> entry : params.entrySet()) {
        	Operator operator = entry.getKey();
        	if(Operator.eq.equals(operator)){//等于
        		for (Map.Entry<String,Object> entry1 : entry.getValue().entrySet()) {
	        		if(criteria == null){
	            		criteria=Criteria.where(entry1.getKey()).is(entry1.getValue());
	            	}else{
	            		criteria=criteria.and(entry1.getKey()).is(entry1.getValue());
	            	}
        		}
        	}else if(Operator.ne.equals(operator)){//不等于
        		for (Map.Entry<String,Object> entry1 : entry.getValue().entrySet()) {
	        		if(criteria == null){
	            		criteria=Criteria.where(entry1.getKey()).ne(entry1.getValue());
	            	}else{
	            		criteria=criteria.and(entry1.getKey()).ne(entry1.getValue());
	            	}
        		}
        	}else if(Operator.gt.equals(operator)){//大于
        		for (Map.Entry<String,Object> entry1 : entry.getValue().entrySet()) {
	        		if(criteria == null){
	            		criteria=Criteria.where(entry1.getKey()).gt(entry1.getValue());
	            	}else{
	            		criteria=criteria.and(entry1.getKey()).gt(entry1.getValue());
	            	}
        		}
        	}else if(Operator.lt.equals(operator)){//小于
        		for (Map.Entry<String,Object> entry1 : entry.getValue().entrySet()) {
	        		if(criteria == null){
	            		criteria=Criteria.where(entry1.getKey()).lt(entry1.getValue());
	            	}else{
	            		criteria=criteria.and(entry1.getKey()).lt(entry1.getValue());
	            	}
        		}
        	}else if(Operator.ge.equals(operator)){//大于等于
        		for (Map.Entry<String,Object> entry1 : entry.getValue().entrySet()) {
	        		if(criteria == null){
	            		criteria=Criteria.where(entry1.getKey()).gte(entry1.getValue());
	            	}else{
	            		criteria=criteria.and(entry1.getKey()).gte(entry1.getValue());
	            	}
        		}
        	}else if(Operator.le.equals(operator)){//小于等于
        		for (Map.Entry<String,Object> entry1 : entry.getValue().entrySet()) {
	        		if(criteria == null){
	            		criteria=Criteria.where(entry1.getKey()).lte(entry1.getValue());
	            	}else{
	            		criteria=criteria.and(entry1.getKey()).lte(entry1.getValue());
	            	}
        		}
        	}else if(Operator.like.equals(operator)){//模糊匹配
        		for (Map.Entry<String,Object> entry1 : entry.getValue().entrySet()) {
	        		if(criteria == null){
	            		criteria=Criteria.where(entry1.getKey()).regex(".*" + entry1.getValue() + ".*");
	            	}else{
	            		criteria=criteria.and(entry1.getKey()).regex(".*" + entry1.getValue() + ".*");
	            	}
        		}
        	}else if(Operator.llike.equals(operator)){//左模糊匹配
        		for (Map.Entry<String,Object> entry1 : entry.getValue().entrySet()) {
	        		if(criteria == null){
	            		criteria=Criteria.where(entry1.getKey()).regex(".*" + entry1.getValue());
	            	}else{
	            		criteria=criteria.and(entry1.getKey()).regex(".*" + entry1.getValue());
	            	}
        		}
        	}else if(Operator.rlike.equals(operator)){//右模糊匹配
        		for (Map.Entry<String,Object> entry1 : entry.getValue().entrySet()) {
	        		if(criteria == null){
	            		criteria=Criteria.where(entry1.getKey()).regex(entry1.getValue() + ".*");
	            	}else{
	            		criteria=criteria.and(entry1.getKey()).regex(entry1.getValue() + ".*");
	            	}
        		}
        	}else if(Operator.in.equals(operator)){//在列表中
        		for (Map.Entry<String,Object> entry1 : entry.getValue().entrySet()) {
	        		if(criteria == null){
	            		criteria=Criteria.where(entry1.getKey()).in(toArr(entry1.getValue()));
	            	}else{
	            		criteria=criteria.and(entry1.getKey()).in(toArr(entry1.getValue()));
	            	}
        		}
        	}else if(Operator.ni.equals(operator)){//不在列表中
        		for (Map.Entry<String,Object> entry1 : entry.getValue().entrySet()) {
	        		if(criteria == null){
	            		criteria=Criteria.where(entry1.getKey()).nin(toArr(entry1.getValue()));
	            	}else{
	            		criteria=criteria.and(entry1.getKey()).nin(toArr(entry1.getValue()));
	            	}
        		}
        	}else if(Operator.isNotNull.equals(operator)){//属性存在，且值不为空
        		for (Map.Entry<String,Object> entry1 : entry.getValue().entrySet()) {
	        		if(criteria == null){
	            		criteria=Criteria.where(entry1.getKey()).ne(null).exists(true);
	            	}else{
	            		criteria=criteria.and(entry1.getKey()).ne(null).exists(true);
	            	}
        		}
        	}else if(Operator.isExists.equals(operator)){//属性是否存在 值为boolean
        		for (Map.Entry<String,Object> entry1 : entry.getValue().entrySet()) {
	        		if(criteria == null){
	            		criteria=Criteria.where(entry1.getKey()).exists((boolean) entry1.getValue());
	            	}else{
	            		criteria=criteria.and(entry1.getKey()).exists((boolean) entry1.getValue());
	            	}
        		}
        	}
        	
        }
       return criteria;
    }

    private static List toArr(Object obj) {
        List list = new ArrayList<>();

        if (obj instanceof Object[]) {
            Object[] objs = (Object[]) obj;
            for (Object o : objs) {
                list.add(o);
            }
        } else if (obj instanceof List) {
            list = (List) obj;
        }

        return list;
    }
    
    /**
     * 用于判断查询类型的枚举
     *
     */
    public enum Operator {
		/** == */
		eq("=="),
		/** != */
		ne("!="),
		/** &gt; */
		gt(">"),
		/** &lt; */
		lt("<"),
		/** &gt;= */
		ge(">="),
		/** &lt;= */
		le("<="),
		/** like %*/
		like("like %"),
		/** like %?*/
		llike("like %?"),
		/** like ?% */
        rlike("like ?%"),
		/** in (?) vaule 为：数组 */
		in("in (?)"),
		/** not in (?) vaule 为：数组  */
		ni("not in (?)"),
		/**is not null  属性存在， 且值不为空*/
		isNotNull("is not null"),
		/**is Exists 值为boolean*/
		isExists("is Exists");

		private String desc;

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		Operator(String desc) {
			this.desc = desc;
		}

		public static Operator fromString(String value) {
			return valueOf(value.toLowerCase());
		}
	}
}
