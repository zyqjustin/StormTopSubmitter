package org.stream.common.build;

import java.util.Map;

import org.stream.common.utils.Topologies;

import backtype.storm.topology.TopologyBuilder;

/**
 * 
 * @author justwin
 * @date 2016年2月24日
 * @version 1.0
 * @description Build all topologies
 */
public interface Build {

	public void build() throws Exception;
	
	public Map<String, TopologyBuilder> getBuilders();
	
	public Topologies getTopologies();
}
