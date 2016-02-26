package org.stream.common.proxy.impl;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.stream.common.utils.BeanUtil;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.base.BaseRichSpout;

public abstract class ProxyBaseRichSpout extends BaseRichSpout {

	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		BeanUtil.autowire(this);
		open(conf, context, collector, BeanUtil.getContext());
	}

	public abstract void open(Map conf, TopologyContext context, SpoutOutputCollector collector, ApplicationContext appContext);

}
