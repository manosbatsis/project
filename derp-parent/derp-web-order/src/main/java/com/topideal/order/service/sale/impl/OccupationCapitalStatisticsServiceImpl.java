package com.topideal.order.service.sale.impl;

import com.topideal.common.system.auth.User;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.common.OccupationRateConfigDao;
import com.topideal.dao.sale.SaleCreditBillOrderDao;
import com.topideal.entity.dto.common.OccupationRateConfigDTO;
import com.topideal.entity.dto.sale.OccupationCapitalStatisticsDTO;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.order.service.sale.OccupationCapitalStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OccupationCapitalStatisticsServiceImpl implements OccupationCapitalStatisticsService {
    @Autowired
    private UserBuRelMongoDao userBuRelMongoDao;
    @Autowired
    private SaleCreditBillOrderDao saleCreditBillOrderDao;
    @Autowired
    private OccupationRateConfigDao occupationRateConfigDao;

    //查询列表信息 分页
    @Override
    public OccupationCapitalStatisticsDTO listDTOByPage(OccupationCapitalStatisticsDTO dto, User user) throws Exception {
        if (dto.getBuId() == null) {
            List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
            if (buList.isEmpty()) {
                dto.setList(new ArrayList<>());
                dto.setTotal(0);
                return dto;
            }
            dto.setBuList(buList);
        }
        dto = saleCreditBillOrderDao.listOccupationCapitalDTOByPage(dto);
        List<OccupationCapitalStatisticsDTO> list = dto.getList();
        if(list != null && list.size() > 0){
            calAmount(list);
            dto.setList(list);
        }

        return dto;
    }

    // 获取导出数量
    @Override
    public Integer getOrderCount(OccupationCapitalStatisticsDTO dto, User user) throws Exception {
        if (dto.getBuId() == null) {
            List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
            if (buList.isEmpty()) {
               return 0;
            }
            dto.setBuList(buList);
        }
        return saleCreditBillOrderDao.getOccupationCapitalCount(dto);
    }

    //获取单据信息
    @Override
    public  List<OccupationCapitalStatisticsDTO> listDTO(OccupationCapitalStatisticsDTO dto, User user) throws Exception {
        if (dto.getBuId() == null) {
            List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
            if (buList.isEmpty()) {
                return new ArrayList<>();
            }
            dto.setBuList(buList);
        }
        List<OccupationCapitalStatisticsDTO> list = saleCreditBillOrderDao.listOccupationCapitalDTO(dto);
        if(list != null && list.size() > 0){
            calAmount(list);
        }
        return list;
    }

    private List<OccupationCapitalStatisticsDTO> calAmount(List<OccupationCapitalStatisticsDTO> list){

        for(OccupationCapitalStatisticsDTO dto : list){
            //垫付金额=支付金额-实收保证金
            BigDecimal advancedAmount = dto.getCreditAmount().subtract(dto.getActualMarginAmount()).setScale(2, BigDecimal.ROUND_HALF_UP);
            dto.setAdvancedAmount(advancedAmount);//垫付金额
            //垫付余款=支付金额-已收保证金-累计回款本金之和
            BigDecimal advancedRestAmount = dto.getCreditAmount().subtract(dto.getActualMarginAmount())
                    .subtract(dto.getReceivePrincipalAmount()== null ? BigDecimal.ZERO:dto.getReceivePrincipalAmount());
            dto.setAdvancedRestAmount(advancedRestAmount.setScale(2, BigDecimal.ROUND_HALF_UP));//垫付余款
            //逾期金额 = 滞纳金-滞纳金减免金额
            BigDecimal overdueAmount = dto.getDelayAmount().subtract((dto.getDiscountDelayAmount() == null ? BigDecimal.ZERO:dto.getDiscountDelayAmount()));
            dto.setOverdueAmount(overdueAmount.setScale(2, BigDecimal.ROUND_HALF_UP));//逾期金额

            dto.setOverdueDays(0);//逾期日期
            if(dto.getExpireDate() != null && dto.getReceiveDate() != null){
                //逾期日期 ==> (1)当回款日期-到期日>0，则逾期天数=回款日期-到期日 ; (2)当回款日期-到期日不大于0，则逾期天数=0
                Integer overdueDays = TimeUtils.daysBetween(dto.getExpireDate(), dto.getReceiveDate());
                if(overdueDays.intValue() > 0){
                    dto.setOverdueDays(overdueDays);//逾期日期
                }
            }

            // 资金占用天数 ==> 回款日期-起息日+1
            Integer occupationDays = TimeUtils.daysBetween(dto.getValueDate(), dto.getReceiveDate()) + 1;
            dto.setOccupationDays(occupationDays);//资金占用天数

            dto.setOccupationRate(BigDecimal.ZERO);//资金占用费率
            dto.setOccupationAmount(BigDecimal.ZERO);//资金占用费
            //取回款时间在资金占用费配置失效时间范围内资金占用费配置
            OccupationRateConfigDTO configDTO = new OccupationRateConfigDTO();
            configDTO.setOrderDate(dto.getReceiveDate());
            configDTO.setBuId(dto.getBuId());
            configDTO.setMerchantId(dto.getMerchantId());
            configDTO = occupationRateConfigDao.searchDTO(configDTO);

            BigDecimal occuptionAmount = BigDecimal.ZERO;//资金占用费
            if(configDTO != null){
                dto.setOccupationRate(configDTO.getOccupationRate());//资金占用费率
                //资金占用费=【(回款本金*资金占用费率)/360】*资金占用天数
                BigDecimal rate = configDTO.getOccupationRate().multiply(new BigDecimal(0.01));
                occuptionAmount = dto.getPrincipalAmount().multiply(rate).divide(new BigDecimal(360), 2,BigDecimal.ROUND_HALF_UP)
                        .multiply(new BigDecimal(occupationDays)).setScale(2, BigDecimal.ROUND_HALF_UP);

                dto.setOccupationAmount(occuptionAmount);//资金占用费
            }
            //毛利=利息+逾期费用-资金占用费
            BigDecimal grossProfit = dto.getInterest().add(dto.getOverdueAmount()).subtract(occuptionAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
            dto.setGrossProfit(grossProfit);//毛利
        }

        return list;
    }
}
