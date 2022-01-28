package com.topideal.common.tools;

import java.math.BigDecimal;

/**
 * @Author: Wilson Lau
 * @Date: 2021/10/18 16:59
 * @Description: 数字工具
 */
public class NumberUtil {

    /**
     * 用于转化BigDecimal， 例如10000 -> 1(万)
     * @param amount 分子
     * @param unitDecimal 分母
     * @return
     */
    public static BigDecimal convertNumber(BigDecimal amount, BigDecimal unitDecimal, int scale) {
        if(amount == null) {
            return null;
        }

        if(unitDecimal == null) {
            unitDecimal = BigDecimal.ONE;
        }

        return amount.divide(unitDecimal, scale, BigDecimal.ROUND_HALF_UP);
    }
}
