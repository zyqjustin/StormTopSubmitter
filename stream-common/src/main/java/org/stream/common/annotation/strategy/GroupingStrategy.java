package org.stream.common.annotation.strategy;

import org.stream.common.proxy.Proxy;

import backtype.storm.topology.InputDeclarer;

public interface GroupingStrategy {

	void grouping(InputDeclarer declarer, Proxy proxy);
}
