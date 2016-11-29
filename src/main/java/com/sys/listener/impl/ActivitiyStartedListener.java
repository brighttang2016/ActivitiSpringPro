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
public class ActivitiyStartedListener implements EventHandler {
	Logger logger = Logger.getLogger("ActivitiyStartedListener.class");
	@Override
	public void handle(ActivitiEvent event) {
		// TODO Auto-generated method stub
		logger.debug("节点开始执行");
	}

}
