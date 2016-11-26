import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;

/**
 * 请假流程leave.bpmn
 * @author Administrator
 *
 */
public class TestLeave {
	@Before
	public void beforeTest(){
		System.out.println("开始测试之前");
	}

	@Test
	public void test() {
		//fail("Not yet implemented");
		System.out.println("流程引擎测试开始");
		//创建流程引擎，使用内存数据库
		ProcessEngine processEngine = ProcessEngineConfiguration
				.createStandaloneInMemProcessEngineConfiguration()
				.buildProcessEngine();
		//部署流程定义文件
		RepositoryService repositoryService = processEngine.getRepositoryService();
		repositoryService.createDeployment().addClasspathResource("chapter5/leave.bpmn").deploy();
		repositoryService.createDeployment().addClasspathResource("chapter6/workFlowLeave.bpmn").deploy();
		System.out.println("部署流程定义文件完成");
		//验证已部署流程定义
		/*ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
		System.out.println(processDefinition.getKey());
		assertEquals("myProcess", processDefinition.getKey());*/
		//启动流程并返回流程实例
		RuntimeService runtimeService = processEngine.getRuntimeService();
		Map<String,Object> variables = new HashMap<String,Object>();
		variables.put("applyUser", "brighttang");
		variables.put("days", "5");
//		ProcessInstance processInstance = runtimeService
//				.startProcessInstanceByKey("myProcess");
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey("leave",variables);
//		ProcessInstance processInstance = runtimeService.startProcessInstanceById("myProcess");
		assertNotNull(processInstance);
		System.out.println("pid=" + processInstance.getId() + ",pdid="
				+ processInstance.getProcessDefinitionId());
		
		//获取task
		TaskService taskService = processEngine.getTaskService();
		Task taskOfDepLeader = taskService.createTaskQuery().taskCandidateGroup("deptLeader").singleResult();
		List<Task> taskOfDepLeaderList = taskService.createTaskQuery().taskCandidateGroup("deptLeader").list();
		System.out.println("taskOfDepLeaderList:"+taskOfDepLeaderList);
		assertNotNull(taskOfDepLeader);
		System.out.println("领导审批任务节点name："+taskOfDepLeader.getName());
		assertEquals("领导审批", taskOfDepLeader.getName());
		//指定执行task责任人(签收，任务taskOfDepLeader.getId()归leaderUser所有)
		System.out.println("领导审批节点id："+taskOfDepLeader.getId());
		taskService.claim(taskOfDepLeader.getId(), "leaderUser");//api文档：void claim(String taskId, String userId)
		variables.put("approved", true);//代表领导审批通过
		taskService.complete(taskOfDepLeader.getId(), variables);//完成任务的同时以流程变量的形式设置审批结果
		
		//再次查询组任务
		taskOfDepLeader = taskService.createTaskQuery().taskCandidateGroup("deptLeader").singleResult();
		assertNull(taskOfDepLeader);//此时用户组：deptLeader未处理任务为空
		//获取执行历史
		HistoryService historyService = processEngine.getHistoryService();
		long count = historyService.createHistoricProcessInstanceQuery().finished().count();
		System.out.println("执行历史任务数："+count);
		assertEquals(1, count);
		
	}
}
