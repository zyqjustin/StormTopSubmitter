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

@Spout(topologyName = "Test", value = "spout")
public class LoopWordSpout extends ProxyBaseRichSpout {
	
	private final String[] _sentences = new String[]{
		"My Mother is a kind and gentle woman",
		"She is always very gentle",
		"We all love her and she loves us also",
		"She is also a thrifty and industrious woman",
		"She looks older than she really is",
		"What piece of good advice this is",
		"We must worth it well and always keep it in our mind",
		"She gets up very early and sleeps very late every day",
		"yet without complaining",
		"her hair becomes silver white",
		"She saves every cent that she can and keeps everything in order",
		"The people say you are carrying their cock away"
	};
	
	private SpoutOutputCollector collector;
	private Random rander;

	@Override
	public void nextTuple() {
		Utils.sleep(10);
		
		String sentence = _sentences[rander.nextInt(_sentences.length)];
		collector.emit(new Values(sentence));
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("sentence"));
	}

	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector, ApplicationContext appContext) {
		this.rander = new Random();
		this.collector = collector;
	}

}
