package com.sys.listener.impl;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.springframework.stereotype.Service;

import com.sys.listener.EventHandler;

/**
 * @author tom
 *
 */
@Service
public class TaskAssignedListener implements EventHandler {

	@Override
	public void handle(ActivitiEvent event) {
		// TODO Auto-generated method stub

	}

}
