package com.sys.listener.impl;

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
public class ActivityCompletedListener implements EventHandler {
	Logger logger = Logger.getLogger(ActivityCompletedListener.class);
	@Override
	public void handle(ActivitiEvent event) {
		logger.debug("节点执行完成");
		ActivitiActivityEvent activityImpl = (ActivitiActivityEvent) event;
		logger.debug("ActivityCompletedListener:"+activityImpl.getActivityId()+"|"+activityImpl.getActivityName()+"|"+activityImpl.getActivityType());
	}
}
