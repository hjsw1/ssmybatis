package com.crazy.service;

import java.util.List;
import java.util.Map;

import com.crazy.bean.TUser;

public interface TUserService {
	int deleteByPrimaryKey(Integer uid);

    int insert(TUser record);

    int insertSelective(TUser record);

    TUser selectByPrimaryKey(Integer uid);

    int updateByPrimaryKeySelective(TUser record);

    int updateByPrimaryKey(TUser record);
    
    List<TUser> queryAll(Map<String, Object> map);
    
    Long getTotal(Map<String, Object> map);
}
