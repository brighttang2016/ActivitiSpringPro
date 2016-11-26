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
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

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
		taskService = processEngine.getTaskService();
		TaskQuery taskQuery = taskService.createTaskQuery();
		List<Task> activeTasklist = taskQuery.taskCandidateGroup("modifyLeader").active().list();
		logger.debug("activeTasklist:"+activeTasklist);
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
//		List<Task> taskList = new ArrayList<Task>();
//		String taskId = "";
		taskService = processEngine.getTaskService();
		TaskQuery taskQuery = taskService.createTaskQuery();
		//用户200810405234当前任务
		/*List<Task> taskList1 = taskQuery.taskCandidateUser("200810405233").list();
		List<Task> taskList2 = taskQuery.taskCandidateUser("200810405234").list();
		
		logger.debug("用户200810405233当前任务数:"+taskList1.size());
		//说明200810405234和200810405234在同一个用户组：deptLeader
		for (Task task:taskList1) {
			if(task != null){
				logger.debug("task.getExecutionId():"+task.getExecutionId());
			}
		}
		logger.debug("taskList2:"+taskList2);
		*/
		List<Task> taskOfDepLeaderList = taskQuery.taskCandidateGroup("deptLeader").list();
		logger.debug("taskOfDepLeaderList:"+taskOfDepLeaderList);
		return taskOfDepLeaderList;
		
		/*System.out.println("用户200810405234当前任务数:"+taskList2.size());
		for (Task task:taskList2) {
			taskId = task.getId();
			System.out.println("task.getId():"+task.getId());
		}*/
		
		/*
		//如果用户200810405234签收了任务，那么200810405233就不会出现该条代办任务
		taskService.claim(taskId, "200810405234");
		taskList2 = taskQuery.taskCandidateUser("200810405234").list();
		System.out.println("200810405234代办，taskList1.size():"+taskList1.size());
		for (Task task:taskList2) {
			System.out.println("200810405234代办，task.getId():"+task.getId());
		}
		//再次查询200810405233代办任务
		taskList1 = taskQuery.taskCandidateUser("200810405233").list();
		System.out.println("200810405234签收后，200810405233代办，taskList1.size():"+taskList1.size());
		*/
		
		/*
		//获取task
				TaskService taskService = processEngine.getTaskService();
				Task taskOfDepLeader = taskService.createTaskQuery().taskCandidateGroup("deptLeader").singleResult();
//				assertNotNull(taskOfDepLeader);
				System.out.println("领导审批任务节点name："+taskOfDepLeader.getName());
//				assertEquals("领导审批", taskOfDepLeader.getName());
				//指定执行task责任人(签收，任务taskOfDepLeader.getId()归leaderUser所有)
				System.out.println("领导审批节点id："+taskOfDepLeader.getId());
				taskService.claim(taskOfDepLeader.getId(), "leaderUser");//api文档：void claim(String taskId, String userId)
				variables.put("approved", true);//代表领导审批通过
				taskService.complete(taskOfDepLeader.getId(), variables);//完成任务的同时以流程变量的形式设置审批结果
				
				//再次查询组任务
				taskOfDepLeader = taskService.createTaskQuery().taskCandidateGroup("deptLeader").singleResult();
//				assertNull(taskOfDepLeader);//此时用户组：deptLeader未处理任务为空
				//获取执行历史
				HistoryService historyService = processEngine.getHistoryService();
				long count = historyService.createHistoricProcessInstanceQuery().finished().count();
				System.out.println("执行历史任务数："+count);
//				assertEquals(1, count);
*/	}
	
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
//		ProcessInstance processInstance = runtimeService
//				.startProcessInstanceByKey("myProcess");
//		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess",variables);
//		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leave", "200810405234", variables);
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("leave").singleResult();
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
				repositoryService.createDeployment().addClasspathResource("chapter6/workFlowLeave_externalForm.bpmn").deploy();
				
				//发布流程图片（单独图片无法发布成功）
//				repositoryService.createDeployment().addClasspathResource("chapter5/candidateUserInUserTask.png").deploy();
				
				//发布bar文件（中文正常）
//				InputStream inputStream = getClass().getClassLoader().getResourceAsStream("chapter5/leave.zip");
//				repositoryService.createDeployment().addZipInputStream(new ZipInputStream(inputStream)).deploy();
				
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
