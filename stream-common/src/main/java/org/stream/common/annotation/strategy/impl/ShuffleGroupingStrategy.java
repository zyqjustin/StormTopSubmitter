package org.stream.common.annotation.strategy.impl;

import org.stream.common.annotation.grouping.ShuffleGrouping;
import org.stream.common.annotation.groups.ShuffleGroups;
import org.stream.common.annotation.strategy.GroupingStrategy;
import org.stream.common.proxy.Proxy;

import backtype.storm.topology.InputDeclarer;

public class ShuffleGroupingStrategy implements GroupingStrategy {

	@Override
	public void grouping(InputDeclarer declarer, Proxy proxy) {
		Class<?> clazz = proxy.getInner().getClass();
		ShuffleGrouping grouping = clazz.getAnnotation(ShuffleGrouping.class);
		grouping(declarer, grouping);
		ShuffleGroups groups = clazz.getAnnotation(ShuffleGroups.class);
		if (groups != null) {
			ShuffleGrouping[] shuffleGroupings = groups.value();
			for (ShuffleGrouping shuffleGrouping : shuffleGroupings) {
				grouping(declarer, shuffleGrouping);
			}
		}
	}

	private void grouping(InputDeclarer declarer, ShuffleGrouping grouping) {
		if (grouping == null) {
			return ;
		}
		
		if ("".equals(grouping.stream())) {
			declarer.shuffleGrouping(grouping.value());
		} else {
			declarer.shuffleGrouping(grouping.value(), grouping.stream());
		}
	}

}
