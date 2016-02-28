package org.stream.common.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stream.common.build.AutoBuild;
import org.stream.common.build.Build;
import org.stream.common.utils.Topologies;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;

/**
 * 
 * @author justwin
 * @date 2016年2月16日
 * @version 1.0
 * @description 自动提交Topology, 装配config目录下的topology环境配置，AutoBuild装配
 */
public class TopologyClusterAutoSubmitter implements TopologysRunner {
	
	private static Logger _logger = LoggerFactory.getLogger(TopologyClusterAutoSubmitter.class);
	
	private final String CONFIG_ROOT_PATH = "config/";
	private Map<String, Config> confMap;
	
	public Build build() throws Exception {
		AutoBuild build = new AutoBuild();
		build.build();
		
		return build;
	}
	
	private void loadTopologyConfigs(Topologies topologies) {
		Set<String> topologyList = topologies.getTopologyList();
		for (String topology : topologyList) {
			loadProperties(topology);
		}
	}
	
	private void loadProperties(String topologyName) {
		String propertiesPath = getPropertiesPath(topologyName);
		InputStream in = getClass().getClassLoader().getResourceAsStream(propertiesPath);
		
		if (in == null) {
			_logger.info("Not found properties file, file: " + propertiesPath);
			return ;
		}
		
		Properties properties = new Properties();
		try {
			properties.load(in);
		} catch (IOException e) {
			_logger.info("Properties loading failed, file: " + propertiesPath, e);
			return ;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				_logger.info("Properties close failed, file: " + propertiesPath);
			}
		}
		
		readAndPutConfig(topologyName, properties);
	}

	private void readAndPutConfig(String topologyName, Properties properties) {
		Config conf = new Config();
		
		for (Entry<Object, Object> entry : properties.entrySet()) {
			conf.put(String.valueOf(entry.getKey()), entry.getValue());
		}
		
		confMap.put(topologyName, conf);
	}

	private String getPropertiesPath(String topologyName) {
		return CONFIG_ROOT_PATH + topologyName + ".properties";
	}

	@Override
	public void submit(String topologyName, Config conf, TopologyBuilder builder) {
		try {
			StormSubmitter.submitTopologyWithProgressBar(topologyName, conf, builder.createTopology());
		} catch (Exception e) {
			_logger.error("Submit topology failed, topology name is " + topologyName, e);
		}
	}

	@Override
	public void run() {
		Build build;
		try {
			build = build();
		} catch (Exception e) {
			throw new RuntimeException("Build topologys failed, ", e);
		}
		
		loadTopologyConfigs(build.getTopologies());
		Map<String, TopologyBuilder> builders = build.getBuilders();
		for (Entry<String, TopologyBuilder> entry : builders.entrySet()) {
			submit(entry.getKey(), confMap.get(entry.getKey()), entry.getValue());
		}
	}

}
