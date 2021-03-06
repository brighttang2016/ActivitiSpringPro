package com.sys.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.sys.enumeration.EIntervalMode;
public class Utils {
	private Logger logger = Logger.getLogger(Utils.class);
	
	
	
	/**格式化对象中的Double对象
	 * tom 2016年11月23日
	 * @param obj 待转换Double成员属性对象
	 * @param scale 转换后Double成员属性小数位数
	 * @return
	 */
	public static Object formateDoubleOfObject(Object obj,int scale){
		Class objClass = obj.getClass();
		Field[] fields = objClass.getDeclaredFields();
		Method[] methods = objClass.getMethods();
		List<Field> fieldList = Utils.getFieldList(objClass);
		for (Field field : fieldList) {
			if(field.getType().getName().equals("double") || field.getType().getName().equals("java.lang.Double")){//目前仅支持转double、Double对象数据
				try {
//					System.out.println(field.getName());
					String getMethodStr = Utils.field2GetMethod(field.getName());
					String setMethodStr = Utils.field2SetMethod(field.getName());
					Method getMethod = objClass.getMethod(getMethodStr);
					Method setMethod = null;
					try {
						setMethod = objClass.getMethod(setMethodStr,Double.class);
					} catch (Exception e) {
						setMethod = objClass.getMethod(setMethodStr,double.class);
					}
					Double score = (Double) getMethod.invoke(obj, null);
					if(score != null)
						setMethod.invoke(obj,Utils.formateDouble2Double(score, scale));
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
		return obj;
	}
	
	
	/**
	 * 获取时间间隔
	 * tom 2016年11月8日
	 * @param beginDate 开始日期
	 * @param endDate 截止日期
	 * @param intervalMode 间隔模式
	 * @return 时间间隔
	 */
	public static long getTimeInterval(Date beginDate,Date endDate,EIntervalMode intervalMode){
		long interval = 0;
		Calendar beginCl = Calendar.getInstance();
		Calendar endCl = Calendar.getInstance();
		beginCl.setTime(beginDate);
		endCl.setTime(endDate);
		switch(intervalMode.name()){
		case "YEARS":
			interval = endCl.get(Calendar.YEAR) - beginCl.get(Calendar.YEAR);
			break;
		case "MONTHS":
			interval = (endCl.get(Calendar.YEAR) - beginCl.get(Calendar.YEAR)) * 12 + endCl.get(Calendar.MONTH) - beginCl.get(Calendar.MONTH);
			break;
		case "DAYS":
			System.out.println(endCl.getTimeInMillis());
			System.out.println(beginCl.getTimeInMillis());
			interval = (endCl.getTimeInMillis() - beginCl.getTimeInMillis())/(24 * 60 * 60 * 1000);
			break;
		case "HOURS":
			interval = (endCl.getTimeInMillis() - beginCl.getTimeInMillis())/(60 * 60 * 1000);
			break;
		case "MINUTES":
			interval = (endCl.getTimeInMillis() - beginCl.getTimeInMillis())/(60 * 1000);
			break;
		case "SECONDS":
			interval = (endCl.getTimeInMillis() - beginCl.getTimeInMillis())/(1000);
		case "MIllISECCONDS":
			interval = endCl.getTimeInMillis() - beginCl.getTimeInMillis();
			break;
		}
		return interval;
	}
	
	/**
	 * 字符串转日期
	 * tom 2016年11月7日
	 * @param date
	 * @param formateStr
	 * @return
	 */
	public static Date formateString2Date(String date,String formateStr){
		SimpleDateFormat formate = new SimpleDateFormat(formateStr);
		Date dateRet = null;
		try {
			dateRet = formate.parse(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateRet;
	}
	
	/**
	 * 日期转字符串
	 * tom 2016年11月7日
	 * @param date
	 * @param formateStr
	 * @return
	 */
	public static String formateDate2String(Date date,String formateStr){
		SimpleDateFormat formate = new SimpleDateFormat(formateStr);
		String dateRet = "";
		try {
			dateRet = formate.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateRet;
	}
	
	/**
	 * 日期格式化
	 * tom 2016年11月7日
	 * @param date
	 * @param formateStr
	 * @return
	 */
	public static Date formateDate(Date date,String formateStr){
		SimpleDateFormat formate = new SimpleDateFormat(formateStr);
		Date dateRet = null;
		try {
			dateRet = formate.parse(formate.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateRet;
	}
	
	/**
	 * 递归所有父类field
	 * @param obj 当前递归对象
	 * @param fieldList 所有field列表
	 */
	public static void getField(Class obj,List<Field> fieldList){
		Field[] fields = obj.getDeclaredFields();
		if(!obj.getName().equals("java.lang.Object")){
			for (Field field : fields) {
				field.setAccessible(true);
				fieldList.add(field);
			}
			Utils.getField(obj.getSuperclass(), fieldList);
		}
	}
	/**
	 * 获取对象所有field
	 * @param obj
	 * @return
	 */
	public static List<Field> getFieldList(Class obj){
		List<Field> fieldList = new LinkedList<Field>();
		Utils.getField(obj, fieldList);
		return fieldList;
	}
	
	
	/**
	 * 双精度浮点数转指定格式双进度浮点数
	 * tom 2016年11月2日
	 * @param number 数据源
	 * @param scale 小数位数
	 * @return 格式化后双精度浮点数（输入：number=123.1 scale=3 输出：123.1）
	 */
	public static Double formateDouble2Double(BigDecimal bigDecimal,int scale){
		System.out.println("formateDouble2Double");
		return Utils.formateDouble2Double(bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_EVEN).doubleValue(), scale);
	}
	
	/**
	 * 双精度浮点数转制定格式双进度浮点数
	 * tom 2016年11月2日
	 * @param number 数据源
	 * @param scale 小数位数
	 * @return 格式化后双精度浮点数（输入：number=123.1 scale=3 输出：123.1）
	 */
	public static Double formateDouble2Double(double number,int scale){
		System.out.println("formateDouble2Double2");
		Double formateDouble = null;
		BigDecimal formater = new BigDecimal(number);
//		方法一：
//		formateDouble = formater.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
//		方法二：
		formateDouble = Math.round(number * Math.pow(10, new Double(scale)))/Math.pow(10, new Double(scale));
		System.out.println(formateDouble);
		return formateDouble;
	}
	
	/**
	 * 双精度浮点数转制定格式字符串
	 * tom 2016年11月2日
	 * @param number 数据源
	 * @param scale 小数位数
	 * @return 格式化后双精度浮点数（输入：number=123.1 scale=3 输出："123.100"）
	 */
	public static String formateDouble2String(double number,int scale){
		String formateDouble = "";
		BigDecimal formater = new BigDecimal(number);
//		new Double("");
		formateDouble = formater.setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
		return formateDouble;
	}
	
	
	/**
	 * 格式化双精度浮点数
	 * tom 2016年11月2日
	 * @param number 数据源
	 * @param place 小数位数
	 * @return 格式化后双精度浮点数
	 */
	public static double formateDouble(double number,int place){
//		System.out.println(number);
		double formateDouble = 0;
//		formateDouble = Math.round(number * Math.pow(10, place))/Math.pow(10, place);
//		DecimalFormat formater = new DecimalFormat("####0.##");
//		formater.setRoundingMode(RoundingMode.HALF_UP);
//		String numberStr = formater.format(number);
//		System.out.println(numberStr);
		/*try {
			number = formater.parse(numberStr).doubleValue();
			System.out.println(number);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
//		formateDouble = Double.parseDouble(formater.format(number));
		BigDecimal formater = new BigDecimal(number);
		formateDouble = formater.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		String formateStr = formater.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
//		System.out.println("double转字符串："+formateStr);
		
		BigDecimal d1 = new BigDecimal(900);
		BigDecimal d2 = new BigDecimal(100);
//		System.out.println(d1.add(d2));
		return formateDouble;
	}
	
	/**
	 * 获取首期还款日期
	 * tom 2016年11月2日
	 * @param loadDate
	 * @return
	 */
	public static Date getFirstRepayDate(Date valueDate){
		Date firstRepayDate = null;
		Calendar firstRepayCl = Calendar.getInstance();
		firstRepayCl.setTime(valueDate);
		firstRepayCl.add(Calendar.MONTH, 1);
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
		return firstRepayCl.getTime();
	}
	
	/**
	 * 生成还款日期
	 * tom 2016年11月2日
	 * @param loadDate
	 * @return
	 */
	public static Date generateRepayDate(Date loadDate,int period){
		Date firstRepayDate = null;
		Calendar firstRepayCl = Calendar.getInstance();
		for (int i = 1; i < period; i++) {
			firstRepayCl.setTime(loadDate);
			firstRepayCl.add(Calendar.MONTH, i);
			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
			System.out.println((i+1)+"期还款日："+formater.format(firstRepayCl.getTime()));
		}
		return firstRepayCl.getTime();
	}
	
	/**
	 * 获取某年总天数
	 * tom 2016年11月2日
	 * @param year 年份
	 * @return 总天数
	 */
	public static int getDayCountOfYear(int year){
		SimpleDateFormat formater = new SimpleDateFormat("yyyy");
		Calendar cl = Calendar.getInstance();
		try {
			cl.setTime(formater.parse(year+""));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		boolean isLeapYear = new GregorianCalendar().isLeapYear(cl.get(Calendar.YEAR));
		System.out.println("年份："+year+"|是否闰年："+isLeapYear);
		return isLeapYear?366:365;
	}
	
	public static String field2GetMethod(String fieldName){
		StringBuffer buffer = new StringBuffer();
		buffer.append("get");
		buffer.append(fieldName.substring(0, 1).toUpperCase());
		buffer.append(fieldName.substring(1, fieldName.length()));
		return buffer.toString();
	}
	public static String field2SetMethod(String fieldName){
		StringBuffer buffer = new StringBuffer();
		buffer.append("set");
		buffer.append(fieldName.substring(0, 1).toUpperCase());
		buffer.append(fieldName.substring(1, fieldName.length()));
		return buffer.toString();
	}
	
	/**
	 * 数据库列名转javabean属性名
	 * tom 2016年11月1日
	 * 输入：user_name_test 输出：userNameTest
	 */
	public static String col2Field(String colName){
		StringBuffer fieldNameBuf = new StringBuffer();
		String[] colNames = colName.split("_");
		for (int i = 0; i < colNames.length; i++) {
			String temp = colNames[i];
			if(i==0)
				fieldNameBuf.append(temp);
			else{
				fieldNameBuf.append(temp.substring(0, 1).toUpperCase());
				fieldNameBuf.append(temp.substring(1,temp.length()));
			}
		}
		return fieldNameBuf.toString();
	}
	
	/**
	 * 对象属性转换 
	 * @param propName  输入格式："myUserName"
	 * @return 返回格式：：MY_USER_NAME
	 */
	public static String field2Col(String propName){
		System.out.println("对象属性转换前："+propName);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < propName.length(); i++) {
			char c = propName.charAt(i);
			if(Character.isUpperCase(c)){
				sb.append("_"+Character.toLowerCase(c));
			}else{
				sb.append(c);
			}
		}
		return sb.toString().toUpperCase();
	}
}
