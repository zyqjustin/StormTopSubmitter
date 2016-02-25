package org.stream.common.annotation.strategy.impl;

import org.stream.common.annotation.grouping.LocalOrShuffleGrouping;
import org.stream.common.annotation.groups.LocalOrShuffleGroups;
import org.stream.common.annotation.strategy.GroupingStrategy;
import org.stream.common.proxy.Proxy;

import backtype.storm.topology.InputDeclarer;

public class LocalOrShuffleGroupingStrategy implements GroupingStrategy {

	@Override
	public void grouping(InputDeclarer declarer, Proxy proxy) {
		Class<?> clazz = proxy.getInner().getClass();
		LocalOrShuffleGrouping grouping = clazz.getAnnotation(LocalOrShuffleGrouping.class);
		grouping(declarer, grouping);
		LocalOrShuffleGroups groups = clazz.getAnnotation(LocalOrShuffleGroups.class);
		if (groups != null) {
			LocalOrShuffleGrouping[] localOrShuffleGroupings = groups.value();
			for (LocalOrShuffleGrouping localOrShuffleGrouping : localOrShuffleGroupings) {
				grouping(declarer, localOrShuffleGrouping);
			}
		}
	}

	private void grouping(InputDeclarer declarer, LocalOrShuffleGrouping grouping) {
		if (grouping == null) {
			return ;
		}
		
		if ("".equals(grouping.stream())) {
			declarer.localOrShuffleGrouping(grouping.value());
		} else {
			declarer.localOrShuffleGrouping(grouping.value(), grouping.stream());
		}
	}

	
}
