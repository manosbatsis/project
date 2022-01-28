package com.topideal.api.wx;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.api.wx.w001.QWXRequestTextModel;
import com.topideal.api.wx.w001.TextModel;
import com.topideal.api.wx.w002.MarkdownModel;
import com.topideal.api.wx.w002.QWXRequestMarkdownModel;
import com.topideal.api.wx.w003.ImageModel;
import com.topideal.api.wx.w003.QWXRequestImageModel;
import com.topideal.api.wx.w004.Article;
import com.topideal.api.wx.w004.NewsModel;
import com.topideal.api.wx.w004.QWXRequestNewsModel;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.MD5Utils;
import com.topideal.enums.SmurfsAPICodeEnum;
import com.topideal.enums.SmurfsAPIEnum;
import com.topideal.http.HttpClientUtil;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WXUtils {
    /**
     * 打印日志
     */
    private static final Logger logger= LoggerFactory.getLogger(WXUtils.class);
    /**-----------------------旧start-----------------------------------*/
    /**
     * 蓝精灵推送日志
     * @param content
     * @return
     */
    public static boolean sendPush(String content){
        if(content.length()>5000) content = content.substring(0,5000);
        JSONObject jsonObject = new JSONObject();//推送内容
        jsonObject.put("code", SmurfsAPICodeEnum.WORK_WEIXN_W001.getCode());//模板编码
        JSONObject paramJson=new JSONObject();
        paramJson.put("content", content);//文本模板中的变量，模板后台配置
        jsonObject.put("paramJson",paramJson);
        jsonObject.put("mentionedMobileList","@all");//手机号列表，提醒手机号对应的群成员(@某个成员)，@all表示提醒所有人。

        //调用外部接口发送邮件
        String resultMsg= SmurfsUtils.send(jsonObject, SmurfsAPIEnum.SMURFS_WEIXN);
        System.out.println(resultMsg);
        logger.info("-----------------蓝精灵推送日志-企业微信机器人发送消息----------------------");
        return true;
    }
    /**
     * 企业微信机器人
     * 经销精灵
     * @return
     */
    public static String send(String content){
        TextModel text = new TextModel();
        text.setContent(content);
        text.setMentioned_mobile_list(new String[]{"@all"});
        QWXRequestTextModel questModel = new QWXRequestTextModel();
        questModel.setText(text);

        // 实体类转json
        JSONObject jSONObject = JSONObject.fromObject(questModel);
        String json = jSONObject.toString();
        logger.info("调用企业微信机器人URL:"+ApolloUtil.jxWebhookUrl);
        logger.info("请求:"+json);
        String doPost = HttpClientUtil.doPost(ApolloUtil.jxWebhookUrl, json, "utf-8");
        logger.info("机器人响应:"+doPost);
        return doPost;
    }
    /**-----------------------旧end-----------------------------------*/
    /**----------------------新start-----------------------------------*/
    /**
     * 文本
     * @param hookUrl 机器人url
     * @param content 消息内容
     * @param mobileList 手机号多个用,号隔开 全部-@all
     */
    public static String sendText(String hookUrl,String content,String mobileList){
        TextModel text = new TextModel();
        text.setContent(content);
        if(StringUtils.isNotBlank(mobileList)){
            text.setMentioned_mobile_list(mobileList.split(","));
        }
        QWXRequestTextModel questModel = new QWXRequestTextModel();
        questModel.setText(text);

        // 实体类转json
        JSONObject jSONObject = JSONObject.fromObject(questModel);
        String json = jSONObject.toString();
        logger.info("机器人URL:"+hookUrl);
        logger.info("请求:"+json);
        String doPost = HttpClientUtil.doPost(hookUrl, json, "utf-8");
        logger.info("机器人响应:"+doPost);
        return doPost;
    }
    /**
     * markdown类型
     * @param hookUrl 机器人url
     * @param content 消息内容
     */
    public static String sendMarkdown(String hookUrl,String content){
        MarkdownModel markdown = new MarkdownModel();
        markdown.setContent(content);
        QWXRequestMarkdownModel questModel = new QWXRequestMarkdownModel();
        questModel.setMarkdown(markdown);

        // 实体类转json
        JSONObject jSONObject = JSONObject.fromObject(questModel);
        String json = jSONObject.toString();
        logger.info("机器人URL:"+hookUrl);
        logger.info("请求:"+json);
        String doPost = HttpClientUtil.doPost(hookUrl, json, "utf-8");
        logger.info("机器人响应:"+doPost);
        return doPost;
    }
    /**
     * 图片
     * @param hookUrl 机器人url
     * @param base64 图片内容的base64编码
     * @param md5 图片内容（base64编码前）的md5值
     */
    public static String sendImage(String hookUrl,String base64,String md5){
        ImageModel image = new ImageModel();
        image.setBase64(base64);
        image.setMd5(md5);
        QWXRequestImageModel questModel = new QWXRequestImageModel();
        questModel.setImage(image);

        // 实体类转json
        JSONObject jSONObject = JSONObject.fromObject(questModel);
        String json = jSONObject.toString();
        logger.info("机器人URL:"+hookUrl);
        logger.info("请求:"+json);
        String doPost = HttpClientUtil.doPost(hookUrl, json, "utf-8");
        logger.info("机器人响应:"+doPost);
        return doPost;
    }
    /**
     * 图文消息
     * @param hookUrl 机器人url
     * @param articles 图文实体
     */
    public static String sendnews(String hookUrl, List<Article> articles){
        NewsModel newsModel = new NewsModel();
        newsModel.setArticles(articles);
        QWXRequestNewsModel questModel = new QWXRequestNewsModel();
        questModel.setNews(newsModel);

        // 实体类转json
        JSONObject jSONObject = JSONObject.fromObject(questModel);
        String json = jSONObject.toString();
        logger.info("机器人URL:"+hookUrl);
        logger.info("请求:"+json);
        String doPost = HttpClientUtil.doPost(hookUrl, json, "utf-8");
        logger.info("机器人响应:"+doPost);
        return doPost;
    }
    /**
     *
     * 图片转化成base64字符串
     * @param imgPath
     * @return
     */
    public static String ImageBase64(String imgPath) {
        String imgFile = imgPath;// 待处理的图片
        InputStream in = null;
        byte[] data = null;
        String encode = null; // 返回Base64编码过的字节数组字符串
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        try {
            // 读取图片字节数组
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            encode = encoder.encode(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return encode;
    }
    /**-----------------------新end-----------------------------------*/
    public static void main(String[] args) throws Exception {
        String url = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=4311abae-1aef-46b2-84fb-cd1b01504601";

        //String text="测试内容";
        //sendText(url,text,"");

        String Markdown = "实时新增用户反馈<font color=\"warning\">132例</font>，请注意。\n" +
                        ">类型:<font color=\"comment\">用户反馈</font>\n" +
                        ">普通用户反馈:<font color=\"comment\">117例</font>\n" +
                        ">VIP用户反馈:<font color=\"comment\">15例</font>";
        //sendMarkdown(url,Markdown);

        String imagepath = "D:/1234.jpg";
        String base64 = ImageBase64(imagepath);
        base64 = base64.replaceAll("\\r|\\n|\\r\n","");//去除换行符否则MD5会校验不过
        String md5 =DigestUtils.md5Hex(new FileInputStream(imagepath));;
        //sendImage(url,base64,md5);

        Article ae1= new Article();
        ae1.setTitle("超级玛丽");
        ae1.setDescription("测试描述");
        ae1.setUrl("https://wwww.baidu.com");
        ae1.setPicurl("https://article-fd.zol-img.com.cn/t_s640x360c5/g5/M00/0A/02/ChMkJltpVgqICIMzAAO-ELA0a2sAAqi_wFCHtUAA74o596.jpg");
        List<Article> list = new ArrayList<>();
        list.add(ae1);
        sendnews(url,list);
    }




}
