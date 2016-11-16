package com.sys.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import com.sys.dao.StudentMapper;
import com.sys.domain.StuPoint;
import com.sys.domain.User;
import com.sys.service.IUserService;

@Service("userService")
public class UserServiceImpl implements IUserService {

	@Resource
	public SqlSessionFactory sqlSessionFactory;
	public List<StuPoint> pointQueryList(SqlSession session,String statement){
		System.out.println("UserServiceImpl->pointQueryList");
//		public List<StuPoint> pointQueryList(SqlSession session){
			
			/* PageHelper.startPage(1,2,true);*/
			
			Map<String,Object> paramMap = new HashMap<String, Object>();
			StuPoint paramStr = new StuPoint();
	        paramStr.setId("12");
	        
	        String [] idArray = {"7","12","13","14","15"};
	        //paramMap.put("name", "姓名");
	        paramMap.put("idArray", idArray);
	      //方法一：不使用接口的方式
//	        List al = session.selectList(statement, paramMap);
	      //方法二：使用接口的方式
	        StudentMapper stuMapper = session.getMapper(StudentMapper.class);
	        List al = stuMapper.getPoint(paramMap);
	        
//			logger.info("学生得分查询 al:" + al + "|" + al.size());
			for (int i = 0; i < al.size(); i++) {
				StuPoint stuPoint = (StuPoint) al.get(i);
				System.out.println("stuPoint:" + stuPoint + "|" + stuPoint.getId() + "|"+ stuPoint.getName()+"|"+stuPoint.getWd()+"|"+stuPoint.getZb()+"|"+stuPoint.getPointZz()+"|"+stuPoint.getPointSx());
//				logger.info("stuPoint:" + stuPoint + "|" + stuPoint.getId() + "|"+ stuPoint.getName()+"|"+stuPoint.getWd()+"|"+stuPoint.getZb()+"|"+stuPoint.getPointZz()+"|"+stuPoint.getPointSx());
			}
			return al;
		}
	
	@Override
	public User getUserById(String userId) {
		// TODO Auto-generated method stub
		System.out.println("UserServiceImpl->getUserById");
		SqlSession session = sqlSessionFactory.openSession();
//		String stmtPoint = "com.sys.dao.StudentMapper"+".getStudentList";
		String stmtPoint = "";
		List<StuPoint> stuList = this.pointQueryList(session,stmtPoint);
		return null;
	}

}
