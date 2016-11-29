package com.sys.listener.impl;

import java.util.Map;

import org.activiti.engine.delegate.event.ActivitiActivityEvent;
import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.sys.listener.EventHandler;

/**
 * @author tom
 *
 */
@Service
public class TaskCreatedListener implements EventHandler{
	Logger logger = Logger.getLogger("TaskCreateListener.class");
	@Override
	public void handle(ActivitiEvent event) {
		logger.debug("任务创建");
		ActivitiEntityEvent eventImpl = (ActivitiEntityEvent) event;
		TaskEntity taskEntity = (TaskEntity) eventImpl.getEntity();
		logger.debug(taskEntity.getId()+"|"+taskEntity.getName()+"|"+taskEntity.getProcessDefinitionId()+"|"+taskEntity.getTaskDefinitionKey());
		String applyUserId = (String) taskEntity.getVariable("applyUserId");//获取运行时变量池中指定名称变量
		Map<String,Object> varaMap = taskEntity.getVariables();//获取运行时变量池中所有变量
		logger.debug("varaMap:"+varaMap);
	}
}
