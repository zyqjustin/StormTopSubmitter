package org.stream.common.service;

import java.util.Map;
import java.util.Map.Entry;

import org.stream.common.build.AutoBuild;
import org.stream.common.build.Build;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;

public class TopologyLocalTestSubmitter implements TopologysRunner {
	
	@Override
	public void submit(String topologyName, Config conf, TopologyBuilder builder) {
		conf.setMaxTaskParallelism(3);
		LocalCluster cluster = new LocalCluster();
		System.out.println("topologyName: " + topologyName);
		cluster.submitTopology(topologyName, conf, builder.createTopology());
	
		try {
			Thread.sleep(35000);
		} catch (InterruptedException e) {
			System.out.println(e);
		} finally {
			cluster.shutdown();
		}
	}
	
	@Override
	public Build build() throws Exception {
		AutoBuild build = new AutoBuild();
		build.build();
		
		return build;
	}

	@Override
	public void run() {
		Build build;
		try {
			build = build();
		} catch (Exception e) {
			throw new RuntimeException("Build topologys failed, ", e);
		}
		
		Map<String, TopologyBuilder> builders = build.getBuilders();
		for (Entry<String, TopologyBuilder> entry : builders.entrySet()) {
			submit(entry.getKey(), new Config(), entry.getValue());
		}
	}

}
