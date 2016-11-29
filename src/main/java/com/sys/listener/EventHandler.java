package com.sys.listener;

import org.activiti.engine.delegate.event.ActivitiEvent;

/**
 * @author tom
 *
 */
public interface EventHandler {
	public void handle(ActivitiEvent event);
}
