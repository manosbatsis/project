package com.topideal.canalclient;


import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.Message;
import com.topideal.api.wx.WXUtils;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.ApolloUtilDB;
import com.topideal.common.tools.TimeUtils;
import com.topideal.service.CanalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
public class ReportCanalClient implements CommandLineRunner {

    /* 打印日志 */
    private static final Logger logger = LoggerFactory.getLogger(ReportCanalClient.class);
    private int BATCH_SIZE = 1000;//每次获取数量
    private int canalport = 11111;//端口
    private String destination = "example";
    private String canalUserName = "";
    private String canalPassword = "";

    @Autowired
    CanalService canalService;

    /**项目启动后即刻执行本此方法
     * */
    @Override
    public void run(String... args) {

        // 接接zookeeper地址都可以
        CanalConnector connector = CanalConnectors.newClusterConnector(ApolloUtilDB.canalHost, destination, canalUserName, canalPassword);
        try {
            //打开连接
            connector.connect();
            //允许所有库表 .*\\..*
            //监听指定库 derp-inventory.*,derp-order.*,derp-storage.*,derp-system.*
            connector.subscribe(ApolloUtilDB.canalSubscribe);
            //回滚到未进行ack的地方，下次fetch的时候，可以从最后一个没有ack的地方开始拿
            connector.rollback();
            //一直循环获取消息
            while (true) {
                // 获取指定数量的数据
                Message message = connector.getWithoutAck(BATCH_SIZE);
                //获取批量ID
                long batchId = message.getId();
                //获取批量的数量
                int size = message.getEntries().size();
                //如果没有数据
                if(batchId == -1 || size == 0) {
                    //线程休眠2秒
                    Thread.sleep(2000);
                } else {
                    //有数据,则处理数据
                    canalService.synsparseBinlogEntry(message.getEntries());
                }
                //进行 batch id 的确认。确认之后，小于等于此 batchId 的 Message 都会被确认。
                connector.ack(batchId);
            }
        } catch (Exception e) {
            //发送预警
            String content = "【canal管道】"+ ApolloUtil.jxRemark+":紧急提醒！"+e.getMessage()+ TimeUtils.formatFullTime();
            logger.info(content);
            WXUtils.sendPush(content);//蓝精灵推送
            e.printStackTrace();
        } finally {
            //断开链接
            connector.disconnect();
        }
    }




}
