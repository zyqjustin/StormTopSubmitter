package org.stream.common.bolt;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.stream.common.annotation.component.Bolt;
import org.stream.common.annotation.grouping.ShuffleGrouping;
import org.stream.common.proxy.impl.ProxyBaseBasicBolt;
import org.stream.common.proxy.impl.ProxyBaseRichBolt;

import backtype.storm.Config;
import backtype.storm.Constants;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

@Bolt(topologyName = "Test", value = "sentenceCounterBolt")
@ShuffleGrouping(value = "spout")
public class SentenceCounterBolt extends ProxyBaseRichBolt {
	
	private static final Logger _logger = LoggerFactory.getLogger(SentenceCounterBolt.class);

	// decide the frequency of emit a tick
	private static final int EMIT_FREQUENCY = 15;
	private OutputCollector collector;
	private int counter;
	
	@Override
	public void execute(Tuple input) {
		try {
			if (isTickTuple(input)) {
				collector.emit(new Values("Total sentence", counter));
				counter = 0;
			} else {
				String sentence = input.getStringByField("sentence");
				if (sentence.length() > 0) {
					counter++;
				}
			}
		} catch (Exception e) {
			_logger.warn("Tack failed, ", e);
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("tag", "count"));
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector, ApplicationContext appContext) {
		// init 
		this.counter = 0;
		this.collector = collector;
	}
	
//	@Override
//	public void execute(Tuple input, BasicOutputCollector collector) {
//
//		try {
//			if (isTickTuple(input)) {
//				collector.emit(new Values("Total sentence", counter));
//				counter = 0;
//			} else {
//				String sentence = input.getStringByField("sentence");
//				if (sentence.length() > 0) {
//					counter++;
//				}
//			}
//		} catch (Exception e) {
//			_logger.warn("Tack failed, ", e);
//		}
//	}
//
//	@Override
//	public void declareOutputFields(OutputFieldsDeclarer declarer) {
//		declarer.declare(new Fields("tag", "count"));
//	}
//
//	@Override
//	public void prepare(Map stormConf, TopologyContext context, ApplicationContext appContext) {
//		// init 
//		counter = 0;
//	}
	
	protected static boolean isTickTuple(Tuple tuple) {
		return tuple.getSourceComponent().equals(Constants.SYSTEM_COMPONENT_ID)
				&& tuple.getSourceStreamId().equals(Constants.SYSTEM_TICK_STREAM_ID);
	}
	
	@Override
	public Map<String, Object> getComponentConfiguration() {
		Map<String, Object> config = super.getComponentConfiguration();
		if (null == config) {
			config = new Config();
		}
		config.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, EMIT_FREQUENCY);
		return config;
	}

}
