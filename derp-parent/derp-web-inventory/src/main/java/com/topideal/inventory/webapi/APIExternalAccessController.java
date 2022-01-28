package com.topideal.inventory.webapi;


import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.entity.dto.BuInventoryDTO;
import com.topideal.entity.vo.MonthlyAccountModel;
import com.topideal.inventory.service.BuInventoryService;
import com.topideal.inventory.service.InventoryGoodsSnapshotService;
import com.topideal.inventory.service.MonthlyAccountService;
import com.topideal.inventory.shiro.ShiroUtils;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/webapi/inventory/external")
@Api(tags = "首页 提供给外部访问的类")
public class APIExternalAccessController {

    @Autowired
    private MonthlyAccountService monthlyAccountService;
    @Autowired
    private InventoryGoodsSnapshotService inventoryGoodsSnapshotService;
    @Autowired
    private BuInventoryService buInventoryService;
    @Autowired
    private UserBuRelMongoDao userBuRelMongoDao;


    @PostMapping("/countPendingCarryForwardNum.asyn")
    @ApiOperation(value = "统计月库存结转中结转状态为未结转的仓库月结记录数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
    })
    private ResponseBean<Map<String, Integer>> countPendingCarryForwardNum(@RequestParam(value = "token", required = true) String token) {
        try {
            MonthlyAccountModel monthlyAccountModel = new MonthlyAccountModel();
            User user = ShiroUtils.getUserByToken(token);
            if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
                monthlyAccountModel.setMerchantId(user.getMerchantId());
            }
            monthlyAccountModel.setState(DERP_INVENTORY.MONTHLYACCOUNT_STATE_1);

            int num = monthlyAccountService.countByMonthlyAccount(monthlyAccountModel);

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, num) ;
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
        }

    }


    /**
     * 月库存结转中结转状态为未结转的仓库月结记录
     * @param token
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "月库存结转中结转状态为未结转的仓库月结记录")
    @PostMapping(value="/getPendingCarryForward.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean getPendingCarryForward(String token)throws Exception {
        List<MonthlyAccountModel> list = null;
        try {
            MonthlyAccountModel monthlyAccountModel = new MonthlyAccountModel();
            User user = ShiroUtils.getUserByToken(token);
            if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
                monthlyAccountModel.setMerchantId(user.getMerchantId());
            }
            monthlyAccountModel.setState(DERP_INVENTORY.MONTHLYACCOUNT_STATE_1);
            list = monthlyAccountService.listByMonthlyAccount(monthlyAccountModel);
        }catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,list);
    }


    /**
     * 事业部库存—汇总该公司主体下各事业部库存总量占所有仓库库存总量的比例
     * @param token
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "事业部库存-汇总该公司主体下各事业部库存总量占所有仓库库存总量的比例")
    @PostMapping(value="/countBuInventory.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean countBuInventory(String token)throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            BuInventoryDTO dto = new BuInventoryDTO();
            User user = ShiroUtils.getUserByToken(token);
            if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
                Long merchantId = user.getMerchantId();
                dto.setMerchantId(merchantId);

                List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
                if(!buList.isEmpty()) {
                    dto.setBuList(buList);
                }
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            String month = sdf.format(new Date());
            dto.setMonth(month);
            result = buInventoryService.countByMonthAndMerchant(dto);
        }catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);
    }


    /**
     * 事业部库存仪表盘---汇总该公司主体下各事业部库存总量占各仓库库存总量的比例
     * @param token
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "事业部库存仪表盘-汇总")
    @PostMapping(value="/countBuInventoryRate.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean countBuInventoryRate(String token,Long buId)throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            BuInventoryDTO dto = new BuInventoryDTO();
            User user = ShiroUtils.getUserByToken(token);
            if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
                Long merchantId = user.getMerchantId();
                dto.setMerchantId(merchantId);

                if(buId == null){
                    List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
                    if(!buList.isEmpty()) {
                        dto.setBuList(buList);
                    }
                }

            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            String month = sdf.format(new Date());
            dto.setMonth(month);
            dto.setBuId(buId);
            result = buInventoryService.countBuInventoryRate(dto);
        }catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);
    }
}
