package com.sys.listener.impl;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.sys.listener.EventHandler;

/**
 * @author tom
 *
 */
@Service
public class TaskCompletedListener implements EventHandler {
	Logger logger = Logger.getLogger("TaskCompletedListener.class");
	@Override
	public void handle(ActivitiEvent event) {
		logger.debug("任务完成");
	}

}
