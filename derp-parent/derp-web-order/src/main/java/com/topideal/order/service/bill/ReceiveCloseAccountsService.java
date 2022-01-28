package com.topideal.order.service.bill;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.entity.dto.bill.ReceiveCloseAccountsDTO;
import com.topideal.order.webapi.bill.form.ReceiveCloseAccountVerifyMonthForm;

import java.util.Map;

/**
 * @Author: Wilson Lau
 * @Date: 2021/11/17 10:43
 * @Description: 应收关账
 */
public interface ReceiveCloseAccountsService {

    /**
     * （1）若手工输入回款日期，且回款日期归属月份的应收关帐状态为“未关帐”时，则默认核销月份等于回款月份；
     * 若回款日期归属月份的应收关帐状态为“已关帐”时，则默认取回款月份最近一个应收关帐状态为“未关帐”的月份，可修改重选；
     *
     * （2）若为勾稽预收款单时，且回款日期归属月份的应收关帐状态为“未关帐”时，则默认核销月份等于回款月份；
     * 若回款日期归属月份的应收关帐状态为“已关帐”时，则默认取回款月份最近一个应收关帐状态为“未关帐”的月份，可修改重选；
     * @param user
     * @param form
     * @return
     */
    ResponseBean getVerifyMonth(User user, ReceiveCloseAccountVerifyMonthForm form) throws Exception;

    /**
     * 分页获取
     * @param user
     * @param dto
     * @return
     */
    ReceiveCloseAccountsDTO listDTOByPage(User user, ReceiveCloseAccountsDTO dto);

    /**
     * 关账或反关账
     * @param user
     * @param dto
     * @param operationType
     */
    ResponseBean updateAccountStatus(User user, ReceiveCloseAccountsDTO dto, String operationType) throws Exception;

    /**
     * 判断该月结月份是否全部关账
     * @param user
     * @param month
     * @return
     * @throws Exception
     */
    boolean validateIsClose(User user, String month) throws Exception;

    /**
     * 刷新
     * @param month
     * @return
     */
    Map<String, Object> refresh(User user, String month) throws Exception;
}
