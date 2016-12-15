package com.sys.controler;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricFormProperty;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.RepositoryServiceImpl;
//import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;
import org.activiti.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricVariableInstanceEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskInfoQueryWrapper;
import org.activiti.engine.task.TaskQuery;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.research.ws.wadl.Request;
import com.sys.service.impl.Class5_1;
import com.sys.service.impl.Class5_2;
import com.sys.service.impl.PairsAppImpl;
import com.sys.service.impl.UserServiceImpl;

//@RequestMapping(value="/ActivitiSpring2")
@Controller
public class SparkController{
	private Logger logger = Logger.getLogger(SparkController.class);
	@Autowired
	public PairsAppImpl pairsAppImpl;

	@RequestMapping(value="/logTest.ctrl",method=RequestMethod.GET)
	public String logTest(){
		System.out.println("输出到控制台");
		logger.debug("SparkController->worldCount debug信息");
		logger.info("SparkController->worldCount info信息");
		logger.error("SparkController->worldCount error信息");
		return "logTest测试";
	}
	
	@RequestMapping(value="/worldCount.ctrl",method=RequestMethod.GET)
	public String worldCount(){
		logger.debug("SparkController->worldCount");
		System.out.println("SparkController->worldCount");
		SparkConf sparkConf = new SparkConf().setAppName("app名称：pairApp测试").setMaster("spark://192.168.137.16:7077");
//		  SparkConf sparkConf = new SparkConf().setAppName("PairsApp测试").setMaster("local");
		  JavaSparkContext jsc = new JavaSparkContext(sparkConf);
//		sparkConf.setAppName("app名称：pairApp测试");
//		sparkConf.setMaster("spark://192.168.137.16:7077");
//		JavaSparkContext jsc = new JavaSparkContext(sparkConf);
//		JavaSparkContext jsc = new JavaSparkContext("spark://192.168.137.16:7077", "tang name");
//		JavaSparkContext jsc = new JavaSparkContext("spark://192.168.137.16:7077", "tang name", "/usr/local/spark-2.0.0-bin-hadoop2.7", new String[0]);
//		pairsAppImpl.wordCount(jsc);
		
		 /*SparkConf conf = new SparkConf().setAppName("PairsApp测试").setMaster("spark://1270.0.0.1:7077");
		   JavaSparkContext jsc = new JavaSparkContext(conf);*/
		pairsAppImpl.pairsStart(jsc);
		return "pairsAppTest测试";
	}
}
