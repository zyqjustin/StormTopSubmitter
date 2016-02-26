package org.stream.common.proxy.impl;

import java.util.Map;

import org.stream.common.proxy.Proxy;
import org.stream.common.utils.BeanUtil;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

public class ProxyRichBolt implements IRichBolt, Proxy {
	private final IRichBolt bolt;
	private boolean isAutowired;
	

	public ProxyRichBolt(IRichBolt bolt) {
		this(bolt, true);
	}

	public ProxyRichBolt(IRichBolt bolt, boolean isAutowired) {
		this.bolt = bolt;
		this.isAutowired = isAutowired;
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		if (isAutowired) {
			BeanUtil.autowire(this);
		}
		bolt.prepare(stormConf, context, collector);
	}

	@Override
	public void execute(Tuple input) {
		bolt.execute(input);
	}

	@Override
	public void cleanup() {
		bolt.cleanup();
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		bolt.declareOutputFields(declarer);
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return bolt.getComponentConfiguration();
	}

	@Override
	public Object getInner() {
		return bolt;
	}

}
