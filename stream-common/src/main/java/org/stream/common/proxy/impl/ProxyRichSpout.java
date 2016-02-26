package org.stream.common.proxy.impl;

import java.util.Map;

import org.stream.common.proxy.Proxy;
import org.stream.common.utils.BeanUtil;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;

public class ProxyRichSpout implements IRichSpout, Proxy {
	private final IRichSpout spout;
	private boolean isAutowired;
	
	
	public ProxyRichSpout(IRichSpout spout) {
		this(spout, true);
	}

	public ProxyRichSpout(IRichSpout spout, boolean isAutowired) {
		this.spout = spout;
		this.isAutowired = isAutowired;
	}

	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		if (isAutowired) {
			BeanUtil.autowire(this);
		}
		spout.open(conf, context, collector);
	}

	@Override
	public void close() {
		spout.close();
	}

	@Override
	public void activate() {
		spout.activate();
	}

	@Override
	public void deactivate() {
		spout.deactivate();
	}

	@Override
	public void nextTuple() {
		spout.nextTuple();
	}

	@Override
	public void ack(Object msgId) {
		spout.ack(msgId);
	}

	@Override
	public void fail(Object msgId) {
		spout.fail(msgId);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		spout.declareOutputFields(declarer);
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return spout.getComponentConfiguration();
	}

	@Override
	public Object getInner() {
		return spout;
	}

}
