package com.topideal.service.main.impl;

import com.topideal.dao.main.ReminderEmailRelDao;
import com.topideal.entity.dto.main.ReminderEmailRelDTO;
import com.topideal.service.main.ReminderEmailRelService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReminderEmailRelServiceImpl  implements ReminderEmailRelService {
    @Autowired
    ReminderEmailRelDao reminderEmailRelDao;

    @Override
    public List<ReminderEmailRelDTO> getConfigById(Long configId) {
        List<ReminderEmailRelDTO> list =reminderEmailRelDao.getConfigById(configId);
        Map<String,ReminderEmailRelDTO> map=new HashMap<>();
        List<ReminderEmailRelDTO> listEmail=new ArrayList<>();
        if(list.size()>0){
            for(ReminderEmailRelDTO item:list){
                String str=item.getConfigId()+"_"+item.getType()+"_"+item.getReminderType();
                if(item.getUserId()!=null){
                    if(map.containsKey(str)) {
                        ReminderEmailRelDTO model=map.get(str);
                        if(StringUtils.isNotBlank(model.getUserIdStr())){
                            model.setUserIdStr(model.getUserIdStr()+","+item.getUserId());
                        }else{
                            model.setUserIdStr(item.getUserId().toString());
                        }
                        map.put(str,model);
                    }else{
                        map.put(str,item);
                        item.setUserIdStr(item.getUserId().toString());
                    }
                }else{
                    map.put(str,item);
                }
            }
            for(String key:map.keySet()){
                ReminderEmailRelDTO dto=map.get(key);
                listEmail.add(dto);
            }
        }
        return listEmail;
    }
}
