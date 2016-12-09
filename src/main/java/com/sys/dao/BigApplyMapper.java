package com.sys.dao;

import java.util.HashMap;
import java.util.List;

import com.sys.domain.BigApply;
import com.sys.domain.BigApplyKey;

public interface BigApplyMapper {
    int deleteByPrimaryKey(BigApplyKey key);

	int insert(BigApply record);

	int insertSelective(BigApply record);

	BigApply selectByPrimaryKey(BigApplyKey key);

	int updateByPrimaryKeySelective(BigApply record);

	int updateByPrimaryKey(BigApply record);

	
    int insertBatch(List<HashMap<String,Object>> bigApply); 
}