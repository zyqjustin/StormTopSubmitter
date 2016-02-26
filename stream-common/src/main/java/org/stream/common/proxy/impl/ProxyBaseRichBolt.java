package org.stream.common.proxy.impl;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.stream.common.utils.BeanUtil;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.base.BaseRichBolt;


public abstract class ProxyBaseRichBolt extends BaseRichBolt {
	
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		BeanUtil.autowire(this);
		prepare(stormConf, context, collector, BeanUtil.getContext());
	}
	
	public abstract void prepare(Map stormConf, TopologyContext context, OutputCollector collector, ApplicationContext appContext);

}
