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
public class SpringMybatisControler{
	private Logger logger = Logger.getLogger(SpringMybatisControler.class);
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
	
	@RequestMapping(value="/process_sign.ctrl")
	public void process_sign(){
		logger.debug("process_sign");
//		RepositoryService repositoryService = processEngine.getRepositoryService();
//		List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().list();
//		System.out.println("已发布流程数："+processDefinitionList.size());
//		ModelAndView mav = new ModelAndView("processList");
//		mav.addObject("processDefinitionList",processDefinitionList);
//		return mav;
	}
	
	/*public Map<String,Object> packageSingleActiviInfo(ActivityImpl activity,ExecutionEntity execution,boolean currentActiviti){
		TaskService taskService = processEngine.getTaskService();
		IdentityService identityService = processEngine.getIdentityService();
		Map<String,Object> vars = new HashMap<String, Object>();
		Map<String,Object> activityInfo = new HashMap<String, Object>();
		activityInfo.put("currentActiviti", currentActiviti);
		activityInfo.put("width", activity.getWidth());
		activityInfo.put("height", activity.getHeight());
		activityInfo.put("x", activity.getX());
		activityInfo.put("y", activity.getY());
		Map<String,Object> properties = new HashMap<String, Object>();
		//vars.put("任务类型", Activit)
		if(currentActiviti){
			Task currentTask = taskService.createTaskQuery().executionId(execution.getId()).taskDefinitionKey(execution.getActivityId()).singleResult();
			if(currentTask == null){
				System.out.println("当前任务为空");
				return null;
			}
			User assigneeUser = identityService.createUserQuery().userId("200810405233").singleResult();
			String userInfo = assigneeUser.getFirstName()+","+assigneeUser.getLastName();
			vars.put("当前处理人", userInfo);
		}
		activityInfo.put("vars", vars);
		return activityInfo;
	}*/
	
	//读取任务流程图片文档
	@RequestMapping(value="/read_taskimage.ctrl")
	public void read_taskimage(@RequestParam("pdid") String processDefinitionId,@RequestParam("executionId") String executionId
		,HttpServletResponse response){
		System.out.println("Class5_2->read_taskimage,processDefinitionId:"+processDefinitionId+",executionId:"+executionId);
		RepositoryService repositoryService= processEngine.getRepositoryService();
		ProcessDefinition processDefinition  = repositoryService.getProcessDefinition(processDefinitionId);
		ProcessDefinitionImpl processDefinitionImpl = (ProcessDefinitionImpl) processDefinition;
		
		/*List<ActivityImpl> activitiList = processDefinitionImpl.getActivities();
		List<Map<String,Object>> activityInfos = new ArrayList<Map<String,Object>>();
		
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ExecutionEntity executionEntity = (ExecutionEntity) runtimeService.createExecutionQuery().executionId(executionId).singleResult();
		
		String activeId = executionEntity.getActivityId();
		
		for (ActivityImpl activity:activitiList) {
			ActivityBehavior activityBefavior = activity.getActivityBehavior();
			boolean currentActivit = false;
			if(activity.getId().equals(activeId)){
				currentActivit = true;
			}
			Map<String,Object> activityImageInfo = packageSingleActiviInfo(activity,executionEntity,currentActivit);
			activityInfos.add(activityImageInfo);
		}*/
		
		
		/*
		System.out.println("流程标识processDefinitionImpl："+processDefinitionImpl);
		String pdfId = processDefinitionImpl.getId();
		System.out.println("pdfId:"+pdfId);
		
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ExecutionEntity executionEntity = (ExecutionEntity) runtimeService.createExecutionQuery().executionId(executionId).singleResult();
		
		String activeId = executionEntity.getActivityId();
		System.out.println("当前任务activeId:"+activeId);
		ProcessDefinitionEntity pdfEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(pdfId); 
		List<ActivityImpl> actImplList = pdfEntity.getActivities();
		ActivityImpl actImpl = null;
		System.out.println("当前任务所有id");
		for(ActivityImpl actImplTemp:actImplList){
			String id = actImplTemp.getId();
			System.out.println("id:"+id);
			if(id.equals(activeId)){
				actImpl = actImplTemp;
			}
		}*/
		RuntimeService runtimeService = processEngine.getRuntimeService();

		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(executionId).singleResult();
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
		List<String> activiActivityIds = runtimeService.getActiveActivityIds(executionId);
//		InputStream is = ProcessDiagramGenerator.generateDiagram(bpmnModel, "png", activiActivityIds);
		
		
		//显示流程图，无定位当前任务节点功能
		/*String resourceName = processDefinition.getDiagramResourceName();
		String deploymentId = processDefinition.getDeploymentId();
		InputStream is = repositoryService.getResourceAsStream(deploymentId, resourceName);*/
		
		InputStream is = new DefaultProcessDiagramGenerator().generateDiagram(
				bpmnModel, "png",
				activiActivityIds, new ArrayList<String>(), 
				processEngine.getProcessEngineConfiguration().getActivityFontName(), 
				processEngine.getProcessEngineConfiguration().getLabelFontName(), 
				null, 1.0);
		byte[] buf = new byte[1024];
		int length = -1;
		try {
			while(( length = is.read(buf)) != -1){
				OutputStream os = response.getOutputStream();
				os.write(buf, 0, length);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//查询执行历史
	@RequestMapping(value="/node_his.ctrl")
	public void  node_his(){
		TaskService taskService = processEngine.getTaskService();	
		FormService formService = processEngine.getFormService();
		//获取执行历史
		HistoryService historyService = processEngine.getHistoryService();
		long count = historyService.createHistoricProcessInstanceQuery().finished().count();
		System.out.println("执行历史任务数："+count);
		List<HistoricDetail> hdList = historyService.createHistoricDetailQuery().processInstanceId("50005").list();
		for (HistoricDetail historicDetail : hdList) {
			System.out.println(historicDetail);
			if(historicDetail instanceof HistoricFormProperty){
				HistoricFormProperty hfp = (HistoricFormProperty) historicDetail;
				System.out.println("HistoricFormProperty|"+hfp.getTaskId()+"|"+hfp.getPropertyId()+"|"+hfp.getPropertyValue()+"|"+hfp.getTime());
			}else if(historicDetail instanceof HistoricVariableUpdate){
				HistoricVariableInstanceEntity hvie = (HistoricVariableInstanceEntity) historicDetail;
				System.out.println("HistoricVariableUpdate|"+hvie.getTaskId()+"|"+hvie.getName()+"|"+hvie.getValue()+"|"+hvie.getTime());
			}
		}
//		assertEquals(1, count);
	}
	
	
	//查询节点表单
	@RequestMapping(value="/node_form.ctrl")
	public void  node_form(HttpServletRequest request){
		System.out.println("node_form::::"+request.getSession().getServletContext().getRealPath(""));
		TaskService taskService = processEngine.getTaskService();	
		FormService formService = processEngine.getFormService();
		TaskQuery taskQuery = taskService.createTaskQuery().processInstanceId("35005").orderByProcessInstanceId().desc();
		List<Task> task = taskQuery.list();
		for (Task task2 : task) {
			System.out.println("task2.getId():"+task2.getId());
			Object obj = formService.getRenderedTaskForm(task2.getId());
//			System.out.println(obj.toString());
		}
//		System.out.println(task.getId());
	}
	
	//任务签收
//	@ResponseBody
	@RequestMapping(value="/task_claim.ctrl")
	public void  task_claim(){
		TaskService taskService = processEngine.getTaskService();
		RuntimeService runTimeservice;
		
		Task task = taskService.createTaskQuery().taskCandidateGroup("deptLeader").singleResult();
		String taskId = task.getId();
		System.out.println("taskId:"+taskId);
		taskService.claim(task.getId(), "partLeaderUserId");
		Map<String, String> variable = new HashMap<String, String>();
		variable.put("approval", "true");
		variable.put("userName", "唐亮");
		FormService formService = processEngine.getFormService();
		formService.submitTaskFormData(taskId, variable);
		
//		taskService.complete(taskId);
//		taskService.complete(taskId, variable);
		
		
		
	}
	
	//查询已部署流程
//	@ResponseBody
	@RequestMapping(value="/task_list.ctrl")
	public ModelAndView task_list(){
		RepositoryService repositoryService = processEngine.getRepositoryService();
		List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().list();
		logger.debug("已部署流程数："+processDefinitionList.size());
		ModelAndView mav = new ModelAndView("taskList");
		List<Task> taskList = class5_2.taskQuery();
		mav.addObject("taskList",taskList);
		return mav;
	}
	
	//读取流程xml文档
	@RequestMapping(value="/read_resource.ctrl")
	public void read_resource(@RequestParam("pdid") String processDefinitionId,@RequestParam("resourceName") String resourceName
		,HttpServletResponse response){
		System.out.println("Class5_2->read_resource processDefinitionId:"+processDefinitionId+",resourceName:"+resourceName);
		RepositoryService repositoryService = processEngine.getRepositoryService();
		ProcessDefinition processDefnition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
		String deploymentId = processDefnition.getDeploymentId();
		logger.debug("deploymentId:"+deploymentId);
		InputStream inputStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
		
		byte[] buf = new byte[1024];
		int length = -1;
		try {
			while(( length = inputStream.read(buf)) != -1){
				OutputStream os = response.getOutputStream();
				os.write(buf, 0, length);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//已发布流程列表查询
//	@ResponseBody
	@RequestMapping(value="/process_list.ctrl")
	public ModelAndView process_list(){
		RepositoryService repositoryService = processEngine.getRepositoryService();
		List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().list();
		System.out.println("已发布流程数："+processDefinitionList.size());
		ModelAndView mav = new ModelAndView("processList");
		mav.addObject("processDefinitionList",processDefinitionList);
		return mav;
	}
	
	//task 查询
	@ResponseBody
	@RequestMapping(value="/task_query.ctrl")
	public JSONArray task_query(){
		class5_2.taskQuery();
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("retmsg", "任务查询成功");
		JSONArray ja = new JSONArray();
		ja.add(hm);
		return ja;
	}
	
	//流程启动
	@ResponseBody
	@RequestMapping(value="/process_start.ctrl")
	public JSONArray process_start(){
		class5_2.process_start();
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("retmsg", "请假流程启动成功");
		JSONArray ja = new JSONArray();
		ja.add(hm);
		return ja;
	}
	
	//流程发布
	@ResponseBody
	@RequestMapping(value="/process_delploy.ctrl")
	public JSONArray process_delploy(){
		class5_2.process_delploy();
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("retmsg", "请假流程发布成功");
		JSONArray ja = new JSONArray();
		ja.add(hm);
		return ja;
	}
	
	@ResponseBody
	@RequestMapping(value="/class5_2.ctrl")
	public JSONArray class5_2(){
		class5_2.testUser();
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("retmsg", "代码清单5-2返回");
		JSONArray ja = new JSONArray();
		ja.add(hm);
		return ja;
	}
	
	@ResponseBody
	@RequestMapping(value="/class5_1.ctrl")
	public JSONArray class5_1(){
		class5_1.testUser();
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("retmsg", "代码清单5-1返回");
		JSONArray ja = new JSONArray();
		ja.add(hm);
		return ja;
	}
	
	@ResponseBody
	@RequestMapping(value="/userQuery.ctrl")
	public JSONArray userQuery(@RequestParam(value="",required=false) String name){
		System.out.println("name:"+name);
		userService.getUserById("12345");
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("name", "唐亮");
		JSONArray ja = new JSONArray();
		ja.add(hm);
		return ja;
	}
	
	//查询已部署流程
	@RequestMapping(value="/processList.ctrl")
	public ModelAndView processList(){
		System.out.println("processList");
		 // 对应WEB-INF/views/chapter5/process-list.jsp
        ModelAndView mav = new ModelAndView("chapter5/process-list");
        ProcessEngine pe = ProcessEngines.getDefaultProcessEngine();
        System.out.println("pe:"+pe.equals(processEngine));
//        System.out.println("repositoryService:"+repositoryService);
//        RepositoryService repositoryService = processEngine.getRepositoryService();
//        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().list();
//        mav.addObject("processDefinitionList", processDefinitionList);
        return mav;
	}
	
}
