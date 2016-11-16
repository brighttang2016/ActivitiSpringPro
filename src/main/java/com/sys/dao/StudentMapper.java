package com.sys.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.sys.domain.StuPoint;
import com.sys.domain.Student;

public interface StudentMapper {
	public List<StuPoint> getPoint(Map<String,Object> map);
	public void stuAdd(Student student);
	public void stuAddBat(List<Student> list);
}
