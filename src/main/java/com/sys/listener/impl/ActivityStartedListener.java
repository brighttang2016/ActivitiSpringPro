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
public class ActivityStartedListener implements EventHandler {
	Logger logger = Logger.getLogger("ActivitiyStartedListener.class");
	@Override
	public void handle(ActivitiEvent event) {
		logger.debug("节点开始执行");
		ActivitiActivityEvent eventImpl = (ActivitiActivityEvent) event;
		logger.debug("ActivityStartedListener:"+eventImpl.getActivityId()+"|"+eventImpl.getActivityName()+"|"+eventImpl.getActivityType());
	}
}
