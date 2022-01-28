package com.topideal.service;

/**
 * 库存扣减失败重推
 */
public interface LowerFailService {

    //库存扣减失败重推
    boolean resendLowerFailRecode() throws Exception;

}
