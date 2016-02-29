package org.stream.common.spout;

import java.util.Map;
import java.util.Random;

import org.springframework.context.ApplicationContext;
import org.stream.common.annotation.component.Spout;
import org.stream.common.proxy.impl.ProxyBaseRichSpout;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

@Spout(topologyName = "MutilTest", value = "spout")
public class CronEmitSpout extends ProxyBaseRichSpout {

	private SpoutOutputCollector collector;
	private Random rand;
	
	@Override
	public void nextTuple() {
		Utils.sleep(1000);
		
		collector.emit(new Values(rand.nextInt(20)));
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("num"));
	}

	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector, ApplicationContext appContext) {
		this.collector = collector;
		this.rand = new Random();
	}

}
