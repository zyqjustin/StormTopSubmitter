package org.stream.common.utils;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author justwin
 * @date 2016年2月16日
 * @version 1.0
 * @description 注册需提交给JStorm的topology
 */
public class Topologies {
	private static Logger _logger = LoggerFactory.getLogger(Topologies.class);
	
	private Set<String> topologyList;
	
	public Topologies() {
		this(new HashSet<String>());
	}

	public Topologies(Set<String> topologyList) {
		this.topologyList = topologyList;
	}

	public void registryTopology(String name) {
		topologyList.add(name);
	}
	
	public void showTopologies() {
		_logger.info("Total submit topology: " + topologyList.size());
		for (String topology : topologyList) {
			_logger.info("Submit topology " + topology);
		}
	}

	public Set<String> getTopologyList() {
		return topologyList;
	}
	
}
