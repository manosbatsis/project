package com.topideal.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * 事务配置类
 * @author gy
 *
 */
@Aspect
@Configuration
public class TransactionAdviceConfig {
	/**
     * 定义切点路径
     */
    private static final String AOP_POINTCUT_EXPRESSION = "execution (* com.topideal.service..impl.*.*(..))";

    @Autowired
    private PlatformTransactionManager transactionManager;

	/**
     * @description 事务管理配置
     */
    @Bean
    public TransactionInterceptor TxAdvice() {
        // 事务管理规则，承载需要进行事务管理的方法名（模糊匹配）及设置的事务管理属性
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
		
		// 设置第一个事务管理的模式（适用于“增删改”）
		RuleBasedTransactionAttribute txAttr_REQUIRED = new RuleBasedTransactionAttribute();
		// 当抛出设置的对应异常后，进行事务回滚（此处设置为“Exception”级别）
		txAttr_REQUIRED.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
		// 设置隔离级别（存在事务则加入其中，不存在则新建事务）
		txAttr_REQUIRED.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		// 设置传播行为（读已提交的数据）
		txAttr_REQUIRED.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);

		// 设置第二个事务管理的模式（适用于“查”）
		RuleBasedTransactionAttribute txAttr_REQUIRED_READONLY = new RuleBasedTransactionAttribute();
		// 当抛出设置的对应异常后，进行事务回滚（此处设置为“Exception”级别）
		txAttr_REQUIRED_READONLY.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
		// 设置隔离级别（存在事务则挂起该事务，执行当前逻辑，结束后再恢复上下文事务）
		txAttr_REQUIRED_READONLY.setPropagationBehavior(TransactionDefinition.PROPAGATION_SUPPORTS);
		// 设置传播行为（读已提交的数据）
		txAttr_REQUIRED_READONLY.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
		// 设置事务是否“只读”（非必需，只是声明该事务中不会进行修改数据库的操作，可减轻由事务造成的数据库压力，属于性能优化的推荐配置）
		txAttr_REQUIRED_READONLY.setReadOnly(true);
		
		// 建立一个map，用来储存要需要进行事务管理的方法名（模糊匹配）
		Map<String, TransactionAttribute> txMap = new HashMap<>();
        txMap.put("insert*", txAttr_REQUIRED );
        txMap.put("save*", txAttr_REQUIRED );
        txMap.put("add*", txAttr_REQUIRED );
        txMap.put("del*", txAttr_REQUIRED );
        txMap.put("update*", txAttr_REQUIRED );
        txMap.put("modify*", txAttr_REQUIRED );
        txMap.put("edit*", txAttr_REQUIRED );
        txMap.put("syns*", txAttr_REQUIRED );
        txMap.put("batch*", txAttr_REQUIRED );
        txMap.put("import*", txAttr_REQUIRED );
        txMap.put("submit*", txAttr_REQUIRED );
        txMap.put("get*", txAttr_REQUIRED_READONLY);
        txMap.put("find*", txAttr_REQUIRED_READONLY);
        txMap.put("list*", txAttr_REQUIRED_READONLY);

		// 注入设置好的map
		source.setNameMap(txMap);
		// 实例化事务拦截器
        TransactionInterceptor txAdvice = new TransactionInterceptor(transactionManager, source);
        return txAdvice;
    }

	/**
     * @description 利用AspectJExpressionPointcut设置切面
     */
	@Bean
    public Advisor txAdviceAdvisor() {
        // 声明切点要切入的面
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        // 设置需要被拦截的路径
        pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
        // 设置切面和配置好的事务管理
        return new DefaultPointcutAdvisor(pointcut, TxAdvice());
    }
}
