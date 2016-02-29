package org.stream.common.bolt;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.stream.common.annotation.component.Bolt;
import org.stream.common.annotation.grouping.ShuffleGrouping;
import org.stream.common.proxy.impl.ProxyBaseRichBolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

@Bolt(topologyName = "MutilTest", value = "accumulateBolt")
@ShuffleGrouping(value = "spout")
public class AccumulateBolt extends ProxyBaseRichBolt {

	private OutputCollector collector; 
	private long total;
	
	@Override
	public void execute(Tuple input) {
		Integer num = input.getIntegerByField("num");
		total += num;
		System.out.println("total: " + total);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// emit nothing
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector, ApplicationContext appContext) {
		this.collector = collector;
		this.total = 0L;
	}

}
