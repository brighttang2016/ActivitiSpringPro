package com.sys.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sys.domain.BigData;

public interface BigDataMapper {
    int deleteByPrimaryKey(String id);

	int insert(BigData record);

	int insertSelective(BigData record);

	BigData selectByPrimaryKey(String id);

	int updateByPrimaryKeySelective(BigData record);

	int updateByPrimaryKey(BigData record);

	int myInsert(BigData record);
	
//	int insertBatch(List<BigData> bigData); 
	int insertBatch(List<HashMap<String,Object>> bigData); 
}