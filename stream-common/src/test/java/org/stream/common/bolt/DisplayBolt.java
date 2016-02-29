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

import scala.collection.parallel.ParIterableLike.Collect;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

@Bolt(topologyName = "Test", value = "displayBolt")
@ShuffleGroups(value = { 
	@ShuffleGrouping(value = "wordCounterBolt"),
	@ShuffleGrouping(value = "sentenceCounterBolt")
})
public class DisplayBolt extends ProxyBaseRichBolt {

	private static final Logger _logger = LoggerFactory.getLogger(DisplayBolt.class);
	private OutputCollector collector;
	
	@Override
	public void execute(Tuple input) {
		String tag = input.getStringByField("tag");
		Integer count = input.getIntegerByField("count");
	
		System.out.println(tag + ": " + count);
//		_logger.info(tag + ": " + count);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// declare nothing
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector, ApplicationContext appContext) {
		this.collector = collector;
	}
	
//	@Override
//	public void execute(Tuple input, BasicOutputCollector collector) {
//		String tag = input.getStringByField("tag");
//		Integer count = input.getIntegerByField("count");
//	
//		_logger.info(tag + ": " + count);
//	}
//
//	@Override
//	public void declareOutputFields(OutputFieldsDeclarer declarer) {
//		// declare nothing
//	}
//
//	@Override
//	public void prepare(Map stormConf, TopologyContext context, ApplicationContext appContext) {
//		
//	}

}
