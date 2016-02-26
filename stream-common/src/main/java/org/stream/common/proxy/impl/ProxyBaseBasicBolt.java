package org.stream.common.proxy.impl;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.stream.common.utils.BeanUtil;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.base.BaseBasicBolt;

/**
 * 
 * @author justwin
 * @date 2016年2月22日
 * @version 1.0
 * @description unsupport
 */
public abstract class ProxyBaseBasicBolt extends BaseBasicBolt {
	
	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		super.prepare(stormConf, context);
		
		BeanUtil.autowire(this);
		prepare(stormConf, context, BeanUtil.getContext());
	}

	public abstract void prepare(Map stormConf, TopologyContext context, ApplicationContext appContext);
}
