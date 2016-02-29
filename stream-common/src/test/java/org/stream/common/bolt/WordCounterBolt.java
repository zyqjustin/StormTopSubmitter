package org.stream.common.bolt;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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

@Bolt(topologyName = "Test", value = "wordCounterBolt")
@ShuffleGrouping(value = "splitBolt")
public class WordCounterBolt extends ProxyBaseRichBolt {
	
	private static final Logger _logger = LoggerFactory.getLogger(WordCounterBolt.class);

	// decide the frequency of emit a tick
	private static final int EMIT_FREQUENCY = 10;
	private OutputCollector collector;
	private Map<String, Integer> counter;
	
	@Override
	public void execute(Tuple input) {
		try {
			if (isTickTuple(input)) {
				for (Entry<String, Integer> entry : counter.entrySet()) {
					collector.emit(new Values(entry.getKey(), entry.getValue()));
				}
				counter.clear();
			} else {
				String word = input.getStringByField("word");
				Integer count = counter.get(word);
				if (count != null && count > 0) {
					counter.put(word, count + 1);
				} else {
					counter.put(word, 1);
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
		this.counter = new HashMap<String, Integer>();
		this.collector = collector;
	}
	
//	@Override
//	public void execute(Tuple input, BasicOutputCollector collector) {
//
//		try {
//			if (isTickTuple(input)) {
//				for (Entry<String, Integer> entry : counter.entrySet()) {
//					collector.emit(new Values(entry.getKey(), entry.getValue()));
//				}
//				counter.clear();
//			} else {
//				String word = input.getStringByField("word");
//				Integer count = counter.get(word);
//				if (count != null && count > 0) {
//					counter.put(word, count + 1);
//				} else {
//					counter.put(word, 1);
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
//		counter = new HashMap<String, Integer>();
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
