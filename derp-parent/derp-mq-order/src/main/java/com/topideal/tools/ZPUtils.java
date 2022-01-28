/*
package com.topideal.tools;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.entity.dto.common.ZPJsonGoodsDTO;
import com.topideal.entity.dto.common.ZPOrderItemDTO;
import com.topideal.entity.dto.common.ZPTempItemDTO;
import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.stream.Collectors;

*/
/**
 * 美赞臣赠品分摊工具类
 **//*

public class ZPUtils {

  */
/**@param zpJsonGoods 接口报文商品
   * @param zpOrderItemList 单据表体商品(常规品、赠品、sample)
   * return allItemList 返回分摊实体
   * *//*

  public static List<ZPTempItemDTO> splitOrderItemNum(ZPJsonGoodsDTO zpJsonGoods, List<ZPOrderItemDTO> zpOrderItemList){
      List<ZPTempItemDTO> allTempItemList = new ArrayList<ZPTempItemDTO>();//临时存储分摊好的实体明细
      */
/**1.对单据的库位商品按库位货号升序排序*//*

      zpOrderItemList = zpOrderItemList.stream()
              .sorted(Comparator.comparing(ZPOrderItemDTO::getGoodsNo))
              .collect(Collectors.toList());

      */
/**2.单据库位商品分类存储*//*

      List<ZPOrderItemDTO> cgOrderItemList = new ArrayList<>();//常规品
      List<ZPOrderItemDTO> zpinOrderItemList =  new ArrayList<>();//赠品
      List<ZPOrderItemDTO> sampleOrderItemList =  new ArrayList<>();//sample样品
      for(ZPOrderItemDTO OrderItem : zpOrderItemList){
          */
/**库位类型：1、常规品 2、赠送品 3、sample*//*

          if (DERP_SYS.INVEN_LOCAITON_MAPPING_TYPE_1.equals(OrderItem.getType())) {
              cgOrderItemList.add(OrderItem);
          }
          if (DERP_SYS.INVEN_LOCAITON_MAPPING_TYPE_2.equals(OrderItem.getType())) {
              zpinOrderItemList.add(OrderItem);
          }
          if (DERP_SYS.INVEN_LOCAITON_MAPPING_TYPE_3.equals(OrderItem.getType())) {
              sampleOrderItemList.add(OrderItem);
          }
      }

      */
/**3.分摊
           先分摊好品数量》过期品分摊到单据好品数量上》坏品数量，若分完单据坏品数量后报文坏品数量还有则分到单据好品数量上；*//*

      */
/**3.1分摊好品====================================================================好品数量start
       * 3.1.1分摊好品数量到常规品*//*

      String type = "1";//1-好品 2-过期品 3-坏品
      Integer jsonNum = zpJsonGoods.getNormalNum();
      if(jsonNum.intValue()>0 && cgOrderItemList.size()>0){
          */
/**循环分摊报文商品数量到单据常规品上*//*

          for(ZPOrderItemDTO orderItem : cgOrderItemList) {
              jsonNum = splitNum(jsonNum, type, zpJsonGoods, orderItem, allTempItemList);
          }
      }
      */
/**3.1.2.未分完则分摊好品数量到赠品上*//*

      jsonNum = zpJsonGoods.getNormalNum();
      if(jsonNum.intValue()>0 && zpinOrderItemList.size()>0){
          */
/**循环分摊报文商品数量到单据常规品上*//*

          for(ZPOrderItemDTO orderItem : zpinOrderItemList){
              jsonNum = splitNum(jsonNum,type,zpJsonGoods,orderItem,allTempItemList);
          }
      }
      */
/**3.1.3.分摊好品数量到样品上*//*

      jsonNum = zpJsonGoods.getNormalNum();
      if(jsonNum.intValue()>0 && sampleOrderItemList.size()>0){
          */
/**循环分摊报文商品数量到单据常规品上*//*

          for(ZPOrderItemDTO orderItem : sampleOrderItemList) {
              jsonNum = splitNum(jsonNum, type, zpJsonGoods, orderItem, allTempItemList);
          }
      }
      */
/**3.1.4.好品数量分摊到常规品、赠品、样品后未分完的全部分到第一个常规品上》赠品》样品*//*

      jsonNum = zpJsonGoods.getNormalNum();
      if(jsonNum.intValue()>0){
          if(cgOrderItemList.size()>0) {
              allInNum(jsonNum, type, zpJsonGoods, cgOrderItemList.get(0), allTempItemList);
          }else if(zpinOrderItemList.size()>0){
              //无常规品则分到赠品上
              allInNum(jsonNum, type, zpJsonGoods, zpinOrderItemList.get(0), allTempItemList);
          }else if(sampleOrderItemList.size()>0){
              //无常规品、赠品则分到样品上
              allInNum(jsonNum, type, zpJsonGoods, sampleOrderItemList.get(0), allTempItemList);
          }
      }
      */
/*分摊好品=================================================================好品数量end*//*


      */
/**分摊过期品==============================================================过期数量start
       * 4.1.1分摊过期品数量到常规品*//*

      type = "2";//1-好品 2-过期品 3-坏品
      jsonNum = zpJsonGoods.getExpireNum();
      if(jsonNum.intValue()>0 && cgOrderItemList.size()>0){
          */
/**循环分摊报文商品数量到单据常规品上*//*

          for(ZPOrderItemDTO orderItem : cgOrderItemList) {
              jsonNum = splitNum(jsonNum, type, zpJsonGoods, orderItem, allTempItemList);
          }
      }
       */
/* 4.1.2分摊过期品数量到赠品上*//*

      jsonNum = zpJsonGoods.getExpireNum();
      if(jsonNum.intValue()>0 && zpinOrderItemList.size()>0){
          */
/**循环分摊报文商品数量到单据常规品上*//*

          for(ZPOrderItemDTO orderItem : zpinOrderItemList){
              jsonNum = splitNum(jsonNum,type,zpJsonGoods,orderItem,allTempItemList);
          }
      }
      */
/* 4.1.3分摊过期品数量到样品上*//*

      jsonNum = zpJsonGoods.getExpireNum();
      if(jsonNum.intValue()>0 && sampleOrderItemList.size()>0){
          */
/**循环分摊报文商品数量到单据常规品上*//*

          for(ZPOrderItemDTO orderItem : sampleOrderItemList) {
              jsonNum = splitNum(jsonNum, type, zpJsonGoods, orderItem, allTempItemList);
          }
      }
      */
/* 4.1.4过期数量分摊到常规品、赠品、样品后未分完的全部分到第一个常规品上》赠品》样品**//*

      jsonNum = zpJsonGoods.getExpireNum();
      if(jsonNum.intValue()>0){
          if(cgOrderItemList.size()>0) {
              allInNum(jsonNum, type, zpJsonGoods, cgOrderItemList.get(0), allTempItemList);
          }else if(zpinOrderItemList.size()>0){
              //无常规品则分到赠品上
              allInNum(jsonNum, type, zpJsonGoods, zpinOrderItemList.get(0), allTempItemList);
          }else if(sampleOrderItemList.size()>0){
              //无常规品、赠品则分到样品上
              allInNum(jsonNum, type, zpJsonGoods, sampleOrderItemList.get(0), allTempItemList);
          }
      }
      */
/**分摊过期品=============================================================过期数量end*//*


      */
/**分摊坏品===============================================================坏品数量start
       * 5.1.1分摊坏品数量到常规品坏品数量*//*

      type = "3";//1-好品 2-过期品 3-坏品
      jsonNum = zpJsonGoods.getWornNum();
      if(jsonNum.intValue()>0 && cgOrderItemList.size()>0){
          */
/**循环分摊报文商品数量到单据常规品上*//*

          for(ZPOrderItemDTO orderItem : cgOrderItemList) {
              jsonNum = splitNum(jsonNum, type, zpJsonGoods, orderItem, allTempItemList);
          }
      }
      */
/* 5.1.2分摊坏品数量到赠品的坏品数量上*//*

      jsonNum = zpJsonGoods.getWornNum();
      if(jsonNum.intValue()>0 && zpinOrderItemList.size()>0){
          */
/**循环分摊报文商品数量到单据常规品上*//*

          for(ZPOrderItemDTO orderItem : zpinOrderItemList){
              jsonNum = splitNum(jsonNum,type,zpJsonGoods,orderItem,allTempItemList);
          }
      }
      */
/* 5.1.3分摊坏品数量到样品的坏品数量上*//*

      jsonNum = zpJsonGoods.getWornNum();
      if(jsonNum.intValue()>0 && sampleOrderItemList.size()>0){
          */
/**循环分摊报文商品数量到单据常规品上*//*

          for(ZPOrderItemDTO orderItem : sampleOrderItemList) {
              jsonNum = splitNum(jsonNum, type, zpJsonGoods, orderItem, allTempItemList);
          }
      }
      */
/* 5.2坏品数量--好品数量上----------------------------start
         坏品数量分摊到常规品、赠品、样品的坏品数量上后未分完的，再分到常规品、赠品、样品的好品数量上**//*

      */
/* 5.2.1坏品数量分到常规品的好品数量上**//*

      type = "4";//1-好品 2-过期品 3-坏品 4-坏品-好品(报文坏品数量分摊到单据好品数量上)
      jsonNum = zpJsonGoods.getWornNum();
      if(jsonNum.intValue()>0 && cgOrderItemList.size()>0){
          */
/**循环分摊报文商品数量到单据常规品上*//*

          for(ZPOrderItemDTO orderItem : cgOrderItemList) {
              jsonNum = splitNum(jsonNum, type, zpJsonGoods, orderItem, allTempItemList);
          }
      }
      */
/* 5.2.2分摊坏品数量到赠品的好品数量上*//*

      jsonNum = zpJsonGoods.getWornNum();
      if(jsonNum.intValue()>0 && zpinOrderItemList.size()>0){
          */
/**循环分摊报文商品数量到单据常规品上*//*

          for(ZPOrderItemDTO orderItem : zpinOrderItemList){
              jsonNum = splitNum(jsonNum,type,zpJsonGoods,orderItem,allTempItemList);
          }
      }
      */
/* 5.2.3分摊坏品数量到样品的好品数量上*//*

      jsonNum = zpJsonGoods.getWornNum();
      if(jsonNum.intValue()>0 && sampleOrderItemList.size()>0){
          */
/**循环分摊报文商品数量到单据常规品上*//*

          for(ZPOrderItemDTO orderItem : sampleOrderItemList) {
              jsonNum = splitNum(jsonNum, type, zpJsonGoods, orderItem, allTempItemList);
          }
      }
      */
/* 5.2.4分摊坏品数量到常规品、赠品、样品的好品数量上后，还未分完的全部分到第一个常规品》赠品》样品*//*

      jsonNum = zpJsonGoods.getWornNum();
      if(jsonNum.intValue()>0){
          if(cgOrderItemList.size()>0) {
              allInNum(jsonNum, type, zpJsonGoods, cgOrderItemList.get(0), allTempItemList);
          }else if(zpinOrderItemList.size()>0){
              //无常规品则分到赠品上
              allInNum(jsonNum, type, zpJsonGoods, zpinOrderItemList.get(0), allTempItemList);
          }else if(sampleOrderItemList.size()>0){
              //无常规品、赠品则分到样品上
              allInNum(jsonNum, type, zpJsonGoods, sampleOrderItemList.get(0), allTempItemList);
          }
      }
      */
/**分摊坏品--------------------------------------------------------------坏品数量end*//*

      return allTempItemList;
  }
    */
/**3.根据类型分摊数量
     * jsonNum:报文商品剩余未分摊数量
     * type:1好品 2-过期品 3-坏品
     * zpJsonGoodsDTO:接口报文商品
     * zpOrderItemList :单据库存商品
     * allTempItemList:存储分摊好的实体
     *//*

    public static int splitNum(int jsonNum,String type,ZPJsonGoodsDTO zpJsonGoods, ZPOrderItemDTO orderItem,
                         List<ZPTempItemDTO> allTempItemList){
            int benNum = 0;//本次分摊数量
            int ordernum = 0;//单据商品剩余未分摊量
            if(type.equals("1")){//好品
                ordernum = orderItem.getNormalNum();
            }else if(type.equals("2")){//过期品
                ordernum = orderItem.getNormalNum();//过期品分摊单据好品数量
            } else if(type.equals("3")){//坏品
                ordernum = orderItem.getWornNum();
            }else if(type.equals("4")){//坏品-好品(报文坏品数量分摊到单据的好品数量上)
                ordernum = orderItem.getNormalNum();
            }

            if(ordernum<=0||jsonNum<=0) return jsonNum;//单据可分摊数量为0或报文剩余可分摊数量为0，结束

            */
/**3.1.1报文商品数量<=单据商品数量 本次分配数量为报文商品数量*//*

            if(jsonNum<=ordernum) {
                benNum = jsonNum;
            }else if(jsonNum>ordernum){
            */
/**3.1.2报文商品数量>单据商品数量 本次分配数量为单据商品数量*//*

                benNum = ordernum;
            }
            */
/*3.1.3 减去报文商品剩余未分配数量，减去单据商品剩余量，拼装分配实体*//*

            jsonNum = jsonNum-benNum;
            if(type.equals("1")){//好品-好品
                zpJsonGoods.setNormalNum(jsonNum);
                orderItem.setNormalNum(ordernum-benNum);
            }else if(type.equals("2")){//过期品-好品
                zpJsonGoods.setExpireNum(jsonNum);
                orderItem.setNormalNum(ordernum-benNum);//过期量分摊在好品上
            }else if(type.equals("3")){//坏品-坏品
                zpJsonGoods.setWornNum(jsonNum);
                orderItem.setWornNum(ordernum-benNum);
            }else if(type.equals("4")){//坏品-好品(报文坏品数量分摊到单据商品的好品数量上)
                zpJsonGoods.setWornNum(jsonNum);
                orderItem.setNormalNum(ordernum-benNum);
            }
            ZPTempItemDTO tempItem = getEntity(type,benNum,zpJsonGoods,orderItem);//拼装分配实体
            allTempItemList.add(tempItem);//添加到分摊实体集合
        return jsonNum;
    }
    */
/**剩余数量全部分到指定商品
     * jsonNum:报文商品剩余未分摊数量
     * type:1好品 2-过期品 3-坏品
     * zpJsonGoodsDTO:接口报文商品
     * orderItem :单据库存商品
     * allTempItemList:存储分摊好的实体
     *//*

    public static void allInNum(int jsonNum,String type,ZPJsonGoodsDTO zpJsonGoods, ZPOrderItemDTO orderItem,
                         List<ZPTempItemDTO> allTempItemList){

        int ordernum = orderItem.getNormalNum();;//单据商品剩余未分摊量
        */
/*3.1.3 减去报文商品剩余未分配数量，减去单据商品剩余量，拼装分配实体*//*

        orderItem.setNormalNum(ordernum-jsonNum);
        if(type.equals("1")){//好品-好品
            zpJsonGoods.setNormalNum(0);
        }else if(type.equals("2")){//过期品-好品
            zpJsonGoods.setExpireNum(0);
        }else if(type.equals("4")){//坏品-好品(报文坏品数量分摊到单据商品的好品数量上)
            zpJsonGoods.setWornNum(0);
        }
        ZPTempItemDTO tempItem = getEntity(type,jsonNum,zpJsonGoods,orderItem);//拼装分配实体
        allTempItemList.add(tempItem);//添加到分摊实体集合
    }
    */
/**创建分摊实体
     * type:1好品 2-过期品 3-坏品
     * benNum 分摊数量
     * *//*

    public static ZPTempItemDTO getEntity(String type,int benNum,ZPJsonGoodsDTO zpJsonGoods,ZPOrderItemDTO orderItem){
        ZPTempItemDTO tempItem = new ZPTempItemDTO();
        tempItem.setGoodsId(orderItem.getGoodsId());
        tempItem.setGoodsNo(orderItem.getGoodsNo());
        tempItem.setOriginalGoodsId(orderItem.getOriginalGoodsId());
        tempItem.setOriginalGoodsNo(orderItem.getOriginalGoodsNo());
        tempItem.setBatchNo(zpJsonGoods.getBatchNo());
        tempItem.setProductionDate(zpJsonGoods.getProductionDate());
        tempItem.setOverdueDate(zpJsonGoods.getOverdueDate());
        tempItem.setTallyingUnit(zpJsonGoods.getTallyingUnit());
        tempItem.setPoNo(orderItem.getPoNo());
        tempItem.setOrderItemId(orderItem.getOrderItemId());
        if(type.equals("1")){//好品
            tempItem.setNormalNum(benNum);
        }else if(type.equals("2")){//过期品
            tempItem.setExpireNum(benNum);
        }else if(type.equals("3")){//坏品
            tempItem.setWornNum(benNum);
        }else if(type.equals("4")){//坏品-好品(坏品分摊到单据的好品数量上)
            tempItem.setWornNum(benNum);
        }
        return tempItem;
    }

  */
/**合并相同：库位商品id+原货号id+批次号+生产日期+失效日期+理货单位+采购单表体id 的正常数量、过期数量、坏品数量
   * goodsId+originalGoodsId+bathNo+productionDate+overdueDate+tallyingUnit
   * *//*

  public static List<ZPTempItemDTO> mergItem(List<ZPTempItemDTO> allTempItemList){
        Map<String,ZPTempItemDTO> tempItemMap = new LinkedHashMap<>();
        for(ZPTempItemDTO temp : allTempItemList){
            String key = temp.getGoodsId().toString()+temp.getOriginalGoodsId().toString()+temp.getBatchNo()
                         +temp.getProductionDate()+temp.getOverdueDate()+temp.getTallyingUnit()+temp.getPoNo()+temp.getOrderItemId();
            ZPTempItemDTO item = tempItemMap.get(key);
            if(item!=null){
                //合并正常数量、过期数量、坏品数量
                item.setNormalNum(item.getNormalNum()+temp.getNormalNum());
                item.setExpireNum(item.getExpireNum()+temp.getExpireNum());
                item.setWornNum(item.getWornNum()+temp.getWornNum());
            }else{
                item = temp;
            }
            tempItemMap.put(key,item);
        }
      List<ZPTempItemDTO> allItemList = new ArrayList<>();
        for(ZPTempItemDTO dto:tempItemMap.values()){
            allItemList.add(dto);
       }
      return allItemList;
  }
    */
/**
     * 拆分报文商品：好品、过期品、坏品 按类型排序
     * 按好品》过期品》坏品顺序分摊
     * *//*

    public static List<ZPJsonGoodsDTO> splitByTypeAndSort(List<ZPJsonGoodsDTO> zpJsonGoodsList){
        List<ZPJsonGoodsDTO> allJsonGoodsList = new ArrayList<>();//排序后
        List<ZPJsonGoodsDTO> normalJsonGoodsList = new ArrayList<>();//好品
        List<ZPJsonGoodsDTO> expireJsonGoodsList = new ArrayList<>();//过期品
        List<ZPJsonGoodsDTO> wornJsonGoodsList = new ArrayList<>();//坏品
        for(ZPJsonGoodsDTO zpdto : zpJsonGoodsList){
            //好品
            if(zpdto.getNormalNum()!=null&&zpdto.getNormalNum()>0) {
                ZPJsonGoodsDTO  zpJsonGoods = new ZPJsonGoodsDTO();
                BeanUtils.copyProperties(zpdto,zpJsonGoods);//复制对象
                zpJsonGoods.setExpireNum(0);//保留好品数量，把过期数量和坏品数量去掉
                zpJsonGoods.setWornNum(0);
                normalJsonGoodsList.add(zpJsonGoods);
            }
            //过期品
            if(zpdto.getExpireNum()!=null&&zpdto.getExpireNum()>0) {
                ZPJsonGoodsDTO  zpJsonGoods = new ZPJsonGoodsDTO();
                BeanUtils.copyProperties(zpdto,zpJsonGoods);//复制对象
                zpJsonGoods.setNormalNum(0);//保留过期数量，把好品数量和坏品数量去掉
                zpJsonGoods.setWornNum(0);
                expireJsonGoodsList.add(zpJsonGoods);
            }
            //坏品
            if(zpdto.getWornNum()!=null&&zpdto.getWornNum()>0) {
                ZPJsonGoodsDTO  zpJsonGoods = new ZPJsonGoodsDTO();
                BeanUtils.copyProperties(zpdto,zpJsonGoods);//复制对象
                zpJsonGoods.setNormalNum(0);//保留坏品数量，把好品数量和过期数量去掉
                zpJsonGoods.setExpireNum(0);
                wornJsonGoodsList.add(zpJsonGoods);
            }
        }
        //顺序添加进去后分摊
        allJsonGoodsList.addAll(normalJsonGoodsList);
        allJsonGoodsList.addAll(expireJsonGoodsList);
        allJsonGoodsList.addAll(wornJsonGoodsList);
        return allJsonGoodsList;
    }

    public static void main(String[] args) {

        */
/*ZPJsonGoodsDTO zpJsonGoods = new ZPJsonGoodsDTO();
        zpJsonGoods.setGoodsId(100001L);
        zpJsonGoods.setGoodsNo("A001");
        zpJsonGoods.setNormalNum(25);//正常数量
        zpJsonGoods.setWornNum(5);//坏品数量
        zpJsonGoods.setExpireNum(5);//过期数量

        List<ZPOrderItemDTO> orderItemList = new ArrayList<>();
        ZPOrderItemDTO cgOrderItem = new ZPOrderItemDTO();//常规品
        cgOrderItem.setGoodsId(201L);//库存商品id
        cgOrderItem.setGoodsNo("k01");
        cgOrderItem.setOriginalGoodsId(100001L);
        cgOrderItem.setOriginalGoodsNo("A001");
        cgOrderItem.setType("1");//常规品
        cgOrderItem.setNormalNum(2);//正常数量
        cgOrderItem.setWornNum(2);

        ZPOrderItemDTO zpOrderItem = new ZPOrderItemDTO();//赠品
        zpOrderItem.setGoodsId(202L);//库存商品id
        zpOrderItem.setGoodsNo("k02");
        zpOrderItem.setOriginalGoodsId(100001L);
        zpOrderItem.setOriginalGoodsNo("A001");
        zpOrderItem.setType("2");//常规品
        zpOrderItem.setNormalNum(4);//正常数量
        zpOrderItem.setWornNum(1);

        ZPOrderItemDTO ypOrderItem = new ZPOrderItemDTO();//样品
        ypOrderItem.setGoodsId(203L);//库存商品id
        ypOrderItem.setGoodsNo("k03");
        ypOrderItem.setOriginalGoodsId(100001L);
        ypOrderItem.setOriginalGoodsNo("A001");
        ypOrderItem.setType("3");//常规品
        ypOrderItem.setNormalNum(4);//正常数量
        ypOrderItem.setWornNum(1);
        orderItemList.add(cgOrderItem);
        orderItemList.add(zpOrderItem);
        orderItemList.add(ypOrderItem);

        List<ZPTempItemDTO> allTempItemList = splitOrderItemNum(zpJsonGoods,orderItemList);
        for(ZPTempItemDTO tempItem :allTempItemList){
            System.out.println("goodsNo:"+tempItem.getGoodsNo()+",正常品:"+tempItem.getNormalNum()
                    +",过期:"+tempItem.getExpireNum()+",坏品:"+tempItem.getWornNum());
        }*//*

    }
}
*/
