package com.sys.controler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.FormService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sys.domain.Student;
import com.sys.service.impl.Class5_1;
import com.sys.service.impl.ExternalFormServiceImpl;
import com.sys.service.impl.UserServiceImpl;

/**
 * @author tom
 *
 */
@Controller
public class ExternalFormController {
	private Logger logger = Logger.getLogger(SpringMybatisController.class);
	@Resource
	public UserServiceImpl userService;
	@Resource 
	public Class5_1 class5_1;
	@Resource 
	public ExternalFormServiceImpl class5;
//	@Resource
//	public RepositoryService repositoryService;
	@Autowired
	public ProcessEngine processEngine;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private FormService formService;
	@Autowired
	private RepositoryService repositoryService;
	
	/**
	 * 节点跳转
	 */
	@ResponseBody
	@RequestMapping(value="/activityJump.ctrl")
	public void activityJump(){
		class5.activityJump();
	}
	
	/**
	 * 读取流程信息
	 * tom 2016年11月28日
	 */
	@ResponseBody
	@RequestMapping(value="/readFlow.ctrl")
	public String readFlow(){
		return JSONObject.toJSONString(class5.readFlow());
	}
	//读取申请人任务
	@ResponseBody
	@RequestMapping(value="/external_task_list.ctrl")
	public ModelAndView external_task_list(){
		List<Task> taskList = class5.taskQuery();
		ModelAndView mav = new ModelAndView("taskList");
		mav.addObject("taskList",taskList);
		return mav;
	}
	
	//读取外置表单
	@RequestMapping(value="/readExternalForm.ctrl")
	public void readExternalForm(){
		Task task = taskService.createTaskQuery().taskCandidateGroup("user").singleResult();
		System.out.println(task.getId());
		
		runtimeService.setVariable(task.getExecutionId(), "applyFormName", "start");
		runtimeService.setVariable(task.getExecutionId(), "applyFormName2", "start2");
		Object renderedApplyForm = formService.getRenderedTaskForm(task.getId());
		System.out.println("renderedApplyForm:"+renderedApplyForm);
	}
	//当前活动任务列表
	@RequestMapping(value="/external_active_tasklist.ctrl")
	public void external_active_tasklist(HttpServletResponse response){
		RepositoryService repositoryService = processEngine.getRepositoryService();
		List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().list();
		logger.debug("已部署流程数："+processDefinitionList.size());
		ModelAndView mav = new ModelAndView("taskList");
		ProcessInstance processInstance = class5.activeTasklist();

//			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(executionId).singleResult();
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
//			List<String> activiActivityIds = runtimeService.getActiveActivityIds(executionId);
		List<String> activiActivityIds = runtimeService.getActiveActivityIds(processInstance.getProcessInstanceId());
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
	
	//流程启动
	@ResponseBody
	@RequestMapping(value="/external_process_start.ctrl")
	public JSONArray external_process_start(){
		class5.process_start();
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("retmsg", "请假流程启动成功");
		JSONArray ja = new JSONArray();
		ja.add(hm);
		return ja;
	}
	
	//流程发布
	@ResponseBody
	@RequestMapping(value="/external_process_delploy.ctrl")
	public JSONArray process_delploy(){
		class5.process_delploy();
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("retmsg", "请假流程发布成功");
		JSONArray ja = new JSONArray();
		ja.add(hm);
		return ja;
	}

}
