package org.stream.common.proxy.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.stream.common.proxy.Proxy;
import org.stream.common.utils.BeanUtil;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;

/**
 * 
 * @author justwin
 * @date 2016年2月24日
 * @version 1.0
 * @description TODO delete
 */
public abstract class ProxyKafkaSpout extends KafkaSpout {

	@Autowired
	@Qualifier("dynamicCdnSpoutConfig")
	private SpoutConfig spoutConfig;
	
	public ProxyKafkaSpout(SpoutConfig spoutConf) {
		super(spoutConf);
	}

	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		super.open(conf, context, collector);
		
		BeanUtil.autowire(this);
		open(conf, context, collector, BeanUtil.getContext());
	}
	
	public abstract void open(Map conf, TopologyContext context, SpoutOutputCollector collector, ApplicationContext appContext);

	public SpoutConfig getSpoutConfig() {
		return spoutConfig;
	}

	public void setSpoutConfig(SpoutConfig spoutConfig) {
		this.spoutConfig = spoutConfig;
	}
	
}
