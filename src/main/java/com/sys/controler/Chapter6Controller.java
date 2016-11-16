package com.sys.controler;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.RepositoryServiceImpl;
//import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;
import org.activiti.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sys.service.impl.Class5_1;
import com.sys.service.impl.Class5_2;
import com.sys.service.impl.UserServiceImpl;

//@RequestMapping(value="/ActivitiSpring2")
@Controller
public class Chapter6Controller{
	private Logger logger = Logger.getLogger(Chapter6Controller.class);
	@Resource
	public UserServiceImpl userService;
	@Resource 
	public Class5_1 class5_1;
	@Resource 
	public Class5_2 class5_2;
//	@Resource
//	public RepositoryService repositoryService;
	@Autowired
	public ProcessEngine processEngine;


	
	//任务签收
	@ResponseBody
	@RequestMapping(value="/leave_release.ctrl")
	public String  leave_release(){
		TaskService taskService = processEngine.getTaskService();
		Task task = taskService.createTaskQuery().taskCandidateGroup("deptLeader").singleResult();
		String taskId = task.getId();
		System.out.println("taskId:"+taskId);
		taskService.claim(task.getId(), "partLeaderUserId");
		Map<String, String> variable = new HashMap<String, String>();
		variable.put("approval", "true");
		FormService formService = processEngine.getFormService();
		formService.submitTaskFormData(taskId, variable);
		return "发布成功";
	}
}
