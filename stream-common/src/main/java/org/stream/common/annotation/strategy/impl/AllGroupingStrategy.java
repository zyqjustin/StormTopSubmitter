package org.stream.common.annotation.strategy.impl;

import org.stream.common.annotation.grouping.AllGrouping;
import org.stream.common.annotation.groups.AllGroups;
import org.stream.common.annotation.strategy.GroupingStrategy;
import org.stream.common.proxy.Proxy;

import backtype.storm.topology.InputDeclarer;

public class AllGroupingStrategy implements GroupingStrategy {

	@Override
	public void grouping(InputDeclarer declarer, Proxy proxy) {
		AllGrouping grouping = proxy.getInner().getClass().getAnnotation(AllGrouping.class);
		grouping(declarer, grouping);
		AllGroups groups = proxy.getInner().getClass().getAnnotation(AllGroups.class);
	
		if (groups != null) {
			AllGrouping[] allGroupings = groups.value();
			for (AllGrouping allGrouping : allGroupings) {
				grouping(declarer, allGrouping);
			}
		}
	}

	private void grouping(InputDeclarer declarer, AllGrouping grouping) {
		if (grouping == null) {
			return ;
		}
		
		if ("".equals(grouping.stream())) {
			declarer.allGrouping(grouping.value());
		} else {
			declarer.allGrouping(grouping.value(), grouping.stream());
		}
	}
}
