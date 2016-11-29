package com.sys.listener.impl;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoader;

import com.sys.listener.EventHandler;

/**
 * @author tom
 *
 */
public class GlobalEventListener implements ActivitiEventListener {
	Logger logger = Logger.getLogger("GlobalEventListener.class");
	private Map<String,String> handler = new HashMap<String,String>();
	
	public Map<String, String> getHandler() {
		return handler;
	}

	public void setHandler(Map<String, String> handler) {
		this.handler = handler;
	}

	@Override
	public void onEvent(ActivitiEvent event) {
		logger.debug("GlobalEventListener->onEvent:"+event.getType().name());
		String eventName = event.getType().name();
		String listenerName = handler.get(eventName);
		if(listenerName != null){
			EventHandler eventHandler = (EventHandler) ContextLoader.getCurrentWebApplicationContext().getBean(listenerName);
			eventHandler.handle(event);
			logger.debug("eventHandler:"+eventHandler);
		}
	}

	@Override
	public boolean isFailOnException() {
		logger.debug("GlobalEventListener->isFailOnException");
		return false;
	}
}
