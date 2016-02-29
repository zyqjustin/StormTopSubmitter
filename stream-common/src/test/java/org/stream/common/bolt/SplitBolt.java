package org.stream.common.bolt;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.stream.common.annotation.component.Bolt;
import org.stream.common.annotation.grouping.ShuffleGrouping;
import org.stream.common.annotation.groups.ShuffleGroups;
import org.stream.common.proxy.impl.ProxyBaseBasicBolt;
import org.stream.common.proxy.impl.ProxyBaseRichBolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

@Bolt(topologyName = "Test", value = "splitBolt")
@ShuffleGrouping(value = "spout")
public class SplitBolt extends ProxyBaseRichBolt {
	private static final Logger _logger = LoggerFactory.getLogger(SplitBolt.class);

	private OutputCollector collector;
	
	@Override
	public void execute(Tuple input) {
		String sentence = input.getStringByField("sentence");
		String[] words = sentence.split("\\s+");
		for (String word : words) {
			collector.emit(new Values(word));
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector, ApplicationContext appContext) {
		_logger.info("Split bolt, id: " + context.getThisTaskId());
		
		this.collector = collector;
	}
	
//	@Override
//	public void execute(Tuple input, BasicOutputCollector collector) {
//		String sentence = input.getStringByField("sentence");
//		String[] words = sentence.split("//s+");
//		for (String word : words) {
//			collector.emit(new Values(word));
//		}
//	}
//
//	@Override
//	public void declareOutputFields(OutputFieldsDeclarer declarer) {
//		declarer.declare(new Fields("word"));
//	}
//
//	@Override
//	public void prepare(Map stormConf, TopologyContext context, ApplicationContext appContext) {
//		_logger.info("Split bolt, id: " + context.getThisTaskId());
//	}

}
