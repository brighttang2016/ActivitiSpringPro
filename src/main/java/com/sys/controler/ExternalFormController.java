package com.sys.controler;

import java.util.HashMap;

import javax.annotation.Resource;

import org.activiti.engine.FormService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
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
