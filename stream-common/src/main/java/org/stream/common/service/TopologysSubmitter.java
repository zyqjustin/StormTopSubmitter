package org.stream.common.service;

import backtype.storm.Config;
import backtype.storm.topology.TopologyBuilder;

public interface TopologysSubmitter {

	public void submit(String topologyName, Config conf, TopologyBuilder builder);
}
