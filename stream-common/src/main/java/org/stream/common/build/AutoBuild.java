package org.stream.common.build;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.stream.common.annotation.component.Bolt;
import org.stream.common.annotation.component.Spout;
import org.stream.common.annotation.component.KafkaSpoutConf;
import org.stream.common.annotation.strategy.GroupingStrategy;
import org.stream.common.annotation.strategy.Strategies;
import org.stream.common.proxy.AbstractKafkaSpoutConfig;
import org.stream.common.proxy.impl.ProxyRichBolt;
import org.stream.common.proxy.impl.ProxyRichSpout;
import org.stream.common.utils.PackageUtil;
import org.stream.common.utils.Topologies;

import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.BoltDeclarer;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.SpoutDeclarer;
import backtype.storm.topology.TopologyBuilder;

public class AutoBuild implements Build {
	
	private final static String SCAN_PATH = "org.stream.topology";
	
	private List<GroupingStrategy> strategies = new ArrayList<GroupingStrategy>();
	private Map<String, TopologyBuilder> builders;
	private Topologies topologies;
	
	public AutoBuild() {
		this(new HashMap<String, TopologyBuilder>());
	}

	public AutoBuild(Map<String, TopologyBuilder> builders) {
		this.builders = builders;
		this.topologies = new Topologies();
		Strategies[] values = Strategies.values();
		for (Strategies value : values) {
			strategies.add(value.getStrategy());
		}
	}
	
	public void registryStrategy(GroupingStrategy strategy) {
		strategies.add(strategy);
	}
	
	public void build() throws Exception {
		Set<Class<?>> classes = PackageUtil.getClasses(SCAN_PATH);
		for (Class<?> clazz : classes) {
			// assemble normal spout
			Spout spout = clazz.getAnnotation(Spout.class);
			if (spout != null) {
				ProxyRichSpout proxyRichSpout = new ProxyRichSpout((IRichSpout) clazz.newInstance());
				TopologyBuilder builder = getBuilder(spout.topologyName());
				SpoutDeclarer spoutDeclarer = builder.setSpout(spout.value(), proxyRichSpout, spout.parallelism()).setNumTasks(spout.tasks());
				if (spout.maxSpoutPending() > 0) {
					spoutDeclarer.setMaxSpoutPending(spout.maxSpoutPending());
				}
			}
		
			//TODO monkey patch, assemble kafka spout
			KafkaSpoutConf kafkaSpoutConf = clazz.getAnnotation(KafkaSpoutConf.class);
			if (kafkaSpoutConf != null) {
				SpoutConfig spoutConfig = ((AbstractKafkaSpoutConfig)clazz.newInstance()).getSpoutConfig();
				ProxyRichSpout proxyRichSpout = new ProxyRichSpout((IRichSpout) new KafkaSpout(spoutConfig));
				TopologyBuilder builder = getBuilder(kafkaSpoutConf.topologyName());
				SpoutDeclarer spoutDeclarer = builder.setSpout(kafkaSpoutConf.value(), proxyRichSpout, kafkaSpoutConf.parallelism()).setNumTasks(kafkaSpoutConf.tasks());
				if (kafkaSpoutConf.maxSpoutPending() > 0) {
					spoutDeclarer.setMaxSpoutPending(kafkaSpoutConf.maxSpoutPending());
				}
			}
			
			// assemble bolt
			Bolt bolt = clazz.getAnnotation(Bolt.class);
			if (bolt != null) {
				ProxyRichBolt proxyRichBolt = new ProxyRichBolt((IRichBolt) clazz.newInstance());
				TopologyBuilder builder = getBuilder(bolt.topologyName());
				BoltDeclarer boltDeclarer = builder.setBolt(bolt.value(), proxyRichBolt, bolt.parallelism()).setNumTasks(bolt.tasks());
				invokeGroupingStrategy(boltDeclarer, proxyRichBolt);
			}
		}
	
	}
	
	private TopologyBuilder getBuilder(String topologyName) {
		if (builders.containsKey(topologyName)) {
			return builders.get(topologyName);
		} else {
			TopologyBuilder builder = new TopologyBuilder();
			builders.put(topologyName, builder);
			topologies.registryTopology(topologyName);
			return builder;
		}
	}

	private void invokeGroupingStrategy(BoltDeclarer declarer, ProxyRichBolt proxyBolt) {
		for (GroupingStrategy strategy : strategies) {
			strategy.grouping(declarer, proxyBolt);
		}
	}
	
	public Map<String, TopologyBuilder> getBuilders() {
		return builders;
	}

	public Topologies getTopologies() {
		return topologies;
	}

	public static void main(String[] args) throws Exception {
		AutoBuild autoBuild = new AutoBuild(new HashMap<String, TopologyBuilder>());
		autoBuild.build();
		Map<String, TopologyBuilder> builders = autoBuild.getBuilders();
		for (Entry<String, TopologyBuilder> entry : builders.entrySet()) {
			StormSubmitter.submitTopologyWithProgressBar(entry.getKey(), new Config(), entry.getValue().createTopology());
		}
	}

}
