/**
 * 用户组管理测试2016-09-12
 */
package com.sys.service.impl;


//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.annotation.Resource;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.ReadOnlyProcessDefinition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ExecutorConfigurationSupport;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sys.utils.Utils;

@Service("externalFormServiceImpl")
public class ExternalFormServiceImpl {
	private Logger logger = Logger.getLogger(ExternalFormServiceImpl.class);
	@Resource
	public IdentityService  identityService;
	@Resource
	public ProcessEngine processEngine;
	@Resource
	public TaskService taskService;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private FormService formService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private HistoryService historyService;
	public void readResource(){
		
	}
	/**
	 * 获取上个任务
	 * tom 2016年12月1日
	 * @param currActivity
	 */
	public void getPreTask(ActivityImpl currActivity){
		
		List<PvmTransition> inPvmTransitions = currActivity.getIncomingTransitions();
		for (PvmTransition inPvmTransition : inPvmTransitions) {
			PvmActivity pvmAcvitity = inPvmTransition.getSource();
			System.out.println("迁移节点："+pvmAcvitity.getId()+"|"+pvmAcvitity.isExclusive()+"|");
		}
	}
	
	/**
	 * 驳回到上个任务节点
	 */
	public void activityReject(){
		String executionId = "257565";
		ExecutionEntity executionEntity = (ExecutionEntity) runtimeService.createExecutionQuery().executionId(executionId).singleResult();
		List<String> activeActivityIds = runtimeService.getActiveActivityIds(executionEntity.getId());
		
		RepositoryServiceImpl repositoryServiceImpl = (RepositoryServiceImpl) repositoryService;
		ReadOnlyProcessDefinition processDefinition = repositoryServiceImpl.getDeployedProcessDefinition(executionEntity.getProcessDefinitionId());
		List<ActivityImpl> activityImplList = (List<ActivityImpl>) processDefinition.getActivities();
		for (ActivityImpl activityImpl : activityImplList) {
			logger.debug("activityImpl.getId():"+activityImpl.getId());
			ActivityImpl currActivity = null;
			if(activeActivityIds.contains(activityImpl.getId())){
				currActivity = activityImpl;
				logger.debug("当前节点："+currActivity.getId());
				this.getPreTask(currActivity);
			}
		}
	}
	
	/**
	 * 会签
	 */
	public void reportBackLeaderToCountersign(){
		logger.debug("reportBackLeaderToCountersign");
	}
/**
 * 流程节点跳转
 * tom 2016年11月30日
 */
	public void activityJump(){
		String executionId = "267505";
		ExecutionEntity executionEntity = (ExecutionEntity) runtimeService.createExecutionQuery().executionId(executionId).singleResult();
		RepositoryServiceImpl repositoryServiceImpl = (RepositoryServiceImpl) repositoryService;
		ReadOnlyProcessDefinition deployedProcessDefinition = repositoryServiceImpl.getDeployedProcessDefinition(executionEntity.getProcessDefinitionId());
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) deployedProcessDefinition;
		List<ActivityImpl> activityImplList = processDefinitionEntity.getActivities();
		List<String> activiActivityIds = runtimeService.getActiveActivityIds(executionId);
		for (String activiActivityId : activiActivityIds) {
			System.out.println("activiActivityId:"+activiActivityId);
		}
		
		
		Task currTask = taskService.createTaskQuery().executionId(executionEntity.getId()).taskDefinitionKey(executionEntity.getActivityId()).singleResult();
		TaskEntity taskEntity = (TaskEntity) currTask;
//		taskEntity.fireEvent("complete");
		//节点提交
//		Map<String, Object> variable = new HashMap<String, Object>();
//		variable.put("deptLeaderApproved", "true");
//		taskService.complete(currTask.getId(),variable);
//		taskService.deleteTask("200024");
		//默认任务改派deptLeaderAudit
		ActivityImpl targetActivityImpl = null;
		for (ActivityImpl activityImpl : activityImplList) {
			if("deptLeaderAudit".equals(activityImpl.getId())){
				targetActivityImpl = activityImpl;
			}
		}
		for (ActivityImpl activityImpl : activityImplList) {
			System.out.println(activityImpl.getId());
			if(activiActivityIds.contains(activityImpl.getId())){
				System.out.println("当前节点");
				List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
				List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
				for (PvmTransition pvmTransition : pvmTransitionList) {
					oriPvmTransitionList.add(pvmTransition);
				}
				pvmTransitionList.clear();
				
				TransitionImpl newTransitionImpl = activityImpl.createOutgoingTransition();
				newTransitionImpl.setDestination(targetActivityImpl);
				taskService.complete(currTask.getId());
				
				targetActivityImpl.getIncomingTransitions().remove(newTransitionImpl);
				activityImpl.getOutgoingTransitions().remove(newTransitionImpl);
				for (PvmTransition pvmTransition : oriPvmTransitionList) {
					pvmTransitionList.add(pvmTransition);
				}
			}
		}
		
		
	}
	
	/**读取流程信息(获取各个节点信息)
	 * tom 2016年11月28日
	 */
	public List<Map<String,Object>> readFlow(){
		String executionId = "252505";
		RepositoryServiceImpl repositoryServiceImpl = (RepositoryServiceImpl) repositoryService;
		ExecutionEntity executionEntity = (ExecutionEntity) runtimeService.createExecutionQuery().executionId(executionId).singleResult();
		/*System.out.println("executionEntity.getId():"+executionEntity.getId());
		System.out.println("executionEntity.getActivityId():"+executionEntity.getActivityId());
		System.out.println("executionEntity.getProcessDefinitionId():"+executionEntity.getProcessDefinitionId());*/
	
		ReadOnlyProcessDefinition deployedProcessDefinition = repositoryServiceImpl.getDeployedProcessDefinition(executionEntity.getProcessDefinitionId());
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) deployedProcessDefinition;
		List<ActivityImpl> activityImplList = processDefinitionEntity.getActivities();
		List<Map<String,Object>> activityInfos = new ArrayList<Map<String,Object>>();
		List<String> activeActivityIds = runtimeService.getActiveActivityIds(executionId);
		System.out.println("JSONObject.toJSONString(activeActivityIds):"+JSONObject.toJSONString(activeActivityIds));
		
		for (ActivityImpl activityImpl : activityImplList) {
			ActivityBehavior activityBehaver = activityImpl.getActivityBehavior();
			boolean currentActiviti = false;
			if(activeActivityIds.contains(activityImpl.getId())){
				System.out.println("当前节点："+activityImpl.getId());
				currentActiviti = true;	
				
				List<PvmTransition> outTrans = activityImpl.getOutgoingTransitions();
				activityImpl.findOutgoingTransition("");
				for (PvmTransition outTran : outTrans) {
					System.out.println("outTran:"+outTran.getSource().getId()+"|"+outTran.getDestination().getId());
					PvmActivity activity = outTran.getDestination();
					System.out.println("activity.getId()："+activity.getId());
					List<PvmTransition> inTrans = activity.getIncomingTransitions();
					List<PvmTransition> outTrans2 = activity.getOutgoingTransitions();
					for (PvmTransition inTran : inTrans) {
						System.out.println("inTran:"+inTran.getSource().getId()+"|"+inTran.getDestination().getId());
					}
					for (int i = 0; i < outTrans2.size(); i++) {
						PvmTransition outTran2 = outTrans2.get(i);
						System.out.println(i+",outTran2:"+outTran2.getSource().getId()+"|"+outTran2.getDestination().getId());
					}
				} 
			}
			
			activityImpl.getOutgoingTransitions();
			Task task = taskService.createTaskQuery().executionId(executionEntity.getId()).taskDefinitionKey(executionEntity.getActivityId()).singleResult();//当前任务	
			logger.debug(task.getAssignee()+"|"+task.getId()+"|"+task.getName()+"|"+activityBehaver+"|");
			List<Task> taskList2 = taskService.createTaskQuery().processInstanceId(executionId).list();
			for (Task task2 : taskList2) {
//				System.out.println("task2.getId():"+task2.getId());
			}
			Map<String,Object> activityInfo = new HashMap<String,Object>();
			activityInfo.put("currentActiviti", currentActiviti);
			activityInfo.put("width", activityImpl.getWidth());
			activityInfo.put("height", activityImpl.getHeight());
			activityInfo.put("x", activityImpl.getX());
			activityInfo.put("y", activityImpl.getY());
			activityInfo.put("id", activityImpl.getId());
			activityInfo.put("activityBehavior", activityImpl.getActivityBehavior());
			//执行历史
			List<HistoricTaskInstance> hisTaskList = historyService.createHistoricTaskInstanceQuery().executionId(executionId).list();
			for (HistoricTaskInstance historicTaskInstance : hisTaskList) {
				String startTime = "";
				String endTime = "";
				if(historicTaskInstance.getStartTime() != null)
					startTime = Utils.formateDate2String(historicTaskInstance.getStartTime(), "yyyy-MM-dd hh:mm:ss");
				if(historicTaskInstance.getEndTime() != null)
					endTime = Utils.formateDate2String(historicTaskInstance.getEndTime(), "yyyy-MM-dd hh:mm:ss");
//				System.out.println("historicTaskInstance历史任务信息:"+historicTaskInstance.getId()+"|"+historicTaskInstance.getAssignee()+"|"+startTime+"|"+endTime+"|"+historicTaskInstance.getTaskDefinitionKey());
				if(historicTaskInstance.getTaskDefinitionKey().equals(activityImpl.getId())){
					activityInfo.put("assignee", historicTaskInstance.getAssignee());
					if(historicTaskInstance.getStartTime() != null)
						activityInfo.put("startTime", startTime);
					if(historicTaskInstance.getEndTime() != null)
						activityInfo.put("endTime", endTime);
					activityInfo.put("duration", historicTaskInstance.getDurationInMillis());
				}
			}
			activityInfos.add(activityInfo);
		}
//		System.out.println("JSONObject.toJSONString(activityInfos):"+JSONObject.toJSONString(activityInfos));
		return activityInfos;
//		return JSONObject.toJSONString(activityInfos);
	}
	/**
	 * 查询业务编码为：200810405234的已完成的流程，200810405234对应对应申请单的主键
	 * tom 2016年11月17日
	 */
	public void finish_task(){
		List<HistoricProcessInstance> finishList = historyService.createHistoricProcessInstanceQuery().
				processInstanceBusinessKey("200810405234").finished().list();//200810405234:businesskey
		System.out.println("finishList.size():"+finishList.size());
	}
	//已发布流程查询
	public void process_list(){
		
	}
	
	//hr待办任务
	public List<Task> hrTasklist(){
		taskService = processEngine.getTaskService();
		TaskQuery taskQuery = taskService.createTaskQuery();
		List<Task> taskOfDepLeaderList = taskQuery.taskCandidateGroup("hrLeader").list();
		logger.debug("taskOfDepLeaderList:"+taskOfDepLeaderList);
		return taskOfDepLeaderList;
	}
	
	//活动任务
	public ProcessInstance activeTasklist(){
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey("200810405234").active().singleResult();
		return processInstance;
	}	
	
	public List<Task> reportBackTasklist(){
		List<Task> taskList = new ArrayList<Task>();
		taskList = taskService.createTaskQuery().taskCandidateGroup("reportBackLeader").list();
		return taskList;
	}
	//modify待办任务
	public List<Task> modifyTasklist(){
		taskService = processEngine.getTaskService();
		TaskQuery taskQuery = taskService.createTaskQuery();
		List<Task> taskOfDepLeaderList = taskQuery.taskCandidateGroup("modifyLeader").list();
		logger.debug("taskOfDepLeaderList:"+taskOfDepLeaderList);
		return taskOfDepLeaderList;
	}
	
	//查询用户任务
	public List<Task> taskQuery(){
		TaskQuery taskQuery = taskService.createTaskQuery();
		List<Task> taskOfDepLeaderList = taskQuery.taskCandidateGroup("user").list();
		return taskOfDepLeaderList;
	}
	
	//启动流程
	public void process_start(){
		//启动流程并返回流程实例
		String createUserid = "20081040";
		identityService.setAuthenticatedUserId(createUserid);
		RuntimeService runtimeService = processEngine.getRuntimeService();
		Map<String,String> variables = new HashMap<String,String>();
		Calendar cl =Calendar.getInstance();
		variables.put("startDate", "2015-01-01");
		variables.put("endDate", "2015-01-05");
		variables.put("reason", "公休");
		variables.put("applyFormName", "");
//		ProcessInstance processInstance = runtimeService
//				.startProcessInstanceByKey("myProcess");
//		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess",variables);
//		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leave", "200810405234", variables);
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("leaveExternalForm").singleResult();
		ProcessInstance processInstance = formService.submitStartFormData(processDefinition.getId(), "200810405234", variables);
//		ProcessInstance processInstance = runtimeService.startProcessInstanceById("myProcess");
//		assertNotNull(processInstance);
		System.out.println("pid=" + processInstance.getId() + ",pdid="
				+ processInstance.getProcessDefinitionId());
		
	}
	//发布流程
	public void process_delploy(){
		//fail("Not yet implemented");
				System.out.println("流程引擎测试开始");
				//创建流程引擎，使用内存数据库
//				ProcessEngine processEngine = ProcessEngineConfiguration
//						.createStandaloneInMemProcessEngineConfiguration()
//						.buildProcessEngine();
				//部署流程定义文件
				RepositoryService repositoryService = processEngine.getRepositoryService();
				
				//发布流程xml图片（中文乱码）
//				repositoryService.createDeployment().addClasspathResource("chapter5/leave.bpmn").deploy();
//				repositoryService.createDeployment().addClasspathResource("chapter6/workFlowLeave_externalForm.bpmn").deploy();
				repositoryService.createDeployment().addClasspathResource("chapter6/workFlowLeave_counterSign.bpmn").deploy();//会签测试流程图
				
				//发布流程图片（单独图片无法发布成功）
//				repositoryService.createDeployment().addClasspathResource("chapter5/candidateUserInUserTask.png").deploy();
				
				//发布bar文件（中文正常）
				/*
				InputStream inputStream = getClass().getClassLoader().getResourceAsStream("chapter5/leave.zip");
				repositoryService.createDeployment().addZipInputStream(new ZipInputStream(inputStream)).deploy();
				*/
				/*InputStream inputStream = getClass().getClassLoader().getResourceAsStream("chapter6/workFlowLeave_externalForm.zip");//外置表单测试流程图
				repositoryService.createDeployment().addZipInputStream(new ZipInputStream(inputStream)).deploy();*/
				
				//流发布
				/*try {String path = getClass().getClassLoader().getResource("").getPath();
					String pathDecode = URLDecoder.decode(path, "UTF-8");
					System.out.println("path:"+path);
					System.out.println(path+"chapter5\\leave.bpmn");
//					FileInputStream fis = new FileInputStream("D:\\Program Files\\Apache Software Foundation\\apache-tomcat-6.0.45_luna\\webapps\\ActivitiSpring2\\WEB-INF\\classes\\chapter5\\leave.bpmn");//可以
					//xml文件流
					FileInputStream fis = new FileInputStream(pathDecode+"\\chapter5\\leave.bpmn");//可以
					repositoryService.createDeployment().addInputStream("leave.bpmn", fis).deploy();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				
				/*String pngPath = "chapter5/leave.png";
				DeploymentBuilder db = repositoryService.createDeployment();
				db.addClasspathResource(pngPath);
				db.deploy();
				*/
				
				System.out.println("部署流程定义文件完成");
				//验证已部署流程定义
				List<ProcessDefinition> pdfList = repositoryService
						.createProcessDefinitionQuery().list();
				System.out.println("流程已发布,对应流程id：");
				for (ProcessDefinition pdf:pdfList) {
					System.out.println(pdf.getKey());
				}
				
//				assertEquals("myProcess", processDefinition.getKey());
	}
	
	public void testUser(){
		
		List<Group> groupList2 = identityService.createGroupQuery().list();
		for (Group group : groupList2) {
			System.out.println(group.getName());
			
		}
		identityService.deleteMembership("200810405233", "user");
		identityService.deleteMembership("200810405234", "hrLeader");
		identityService.deleteMembership("200810405233", "deptLeader");
		identityService.deleteMembership("200810405235", "modifyLeader");
		identityService.deleteMembership("200810405236", "reportBackLeader");
		
		
		//创建用户
		
		User user0 = identityService.newUser("200810405233");
		user0.setFirstName("唐");
		user0.setLastName("亮");
		user0.setPassword("200810405233");
		identityService.deleteUser("200810405233");
		identityService.saveUser(user0);
		
		User user = identityService.newUser("200810405234");
//		user.setId("2008");
		user.setFirstName("唐");
		user.setLastName("亮");
		user.setPassword("200810405234");
		identityService.deleteUser("200810405234");
		identityService.saveUser(user);
		
		User user2 = identityService.newUser("");
		user2.setId("200810405233");
		user2.setFirstName("唐");
		user2.setLastName("亮");
		user2.setPassword("200810405233");
		identityService.deleteUser("200810405233");
		identityService.saveUser(user2);
		
		User user3 = identityService.newUser("");
		user3.setId("200810405235");
		user3.setFirstName("唐");
		user3.setLastName("亮");
		user3.setPassword("200810405235");
		identityService.deleteUser("200810405235");
		identityService.saveUser(user3);
		
		User user4 = identityService.newUser("");
		user4.setId("200810405236");
		user4.setFirstName("唐");
		user4.setLastName("亮");
		user4.setPassword("200810405236");
		identityService.deleteUser("200810405236");
		identityService.saveUser(user4);
		
		//创建用户组
		String[] groups = new String[]{"user","deptLeader","hrLeader","modifyLeader","reportBackLeader"};
		System.out.println("class5_2->testUser");
		for(int i = 0;i < groups.length;i++){
			Group group  = identityService.newGroup(groups[i]);
//					group.setId("31001");
			group.setName("总经理办公会");
			group.setType("assignment");
			identityService.deleteGroup(groups[i]);
			identityService.saveGroup(group);
			List<Group> groupList = identityService.createGroupQuery().groupId(groups[i]).list();
			System.out.println("用户组数："+groupList.size());
		}
		
		//用户设置用户组
		identityService.createMembership("200810405233", "user");
		identityService.createMembership("200810405234", "hrLeader");
		identityService.createMembership("200810405233", "deptLeader");
		identityService.createMembership("200810405235", "modifyLeader");
		identityService.createMembership("200810405236", "reportBackLeader");
		List<User> userList = identityService.createUserQuery().memberOfGroup("deptLeader").list();
		System.out.println("用户组 deptLeader中的用户：");
		for(User userTemp:userList){
			System.out.println("userTemp:"+userTemp.getId());
		}
	}
}
