package com.topideal.report.webapi.reporting;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.entity.dto.BuLocationSummaryDTO;
import com.topideal.report.service.reporting.BuLocationSummaryService;
import com.topideal.report.service.reporting.FileTaskService;
import com.topideal.report.shiro.ShiroUtils;
import com.topideal.report.webapi.form.BuLocationSummaryForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Wilson Lau
 * @Date: 2022/1/18 16:16
 * @Description:
 */
@RestController
@RequestMapping("/webapi/report/buLocationSummary")
@Api(tags = "库位进销存汇总")
public class APIBuLocationSummaryController {

    private static final Logger LOG = Logger.getLogger(APIBuLocationSummaryController.class);

    @Autowired
    private BuLocationSummaryService service;
    @Autowired
    private FileTaskService fileTaskService;

    @ApiOperation(value = "获取库位进销存汇总分页数据")
    @PostMapping(value = "/listByPage.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean listByPage(BuLocationSummaryForm form) {
        try {
            boolean isEmpty = new EmptyCheckUtils()
                    .addObject(form.getToken())
                    .empty();

            if(isEmpty){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }

            User user = ShiroUtils.getUserByToken(form.getToken());
            BuLocationSummaryDTO dto = new BuLocationSummaryDTO();
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());
            dto = service.listByPage(user,dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

}
