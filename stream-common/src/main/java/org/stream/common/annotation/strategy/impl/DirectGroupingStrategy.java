package org.stream.common.annotation.strategy.impl;

import org.stream.common.annotation.grouping.DirectGrouping;
import org.stream.common.annotation.groups.DirectGroups;
import org.stream.common.annotation.strategy.GroupingStrategy;
import org.stream.common.proxy.Proxy;

import backtype.storm.topology.InputDeclarer;

public class DirectGroupingStrategy implements GroupingStrategy {

	@Override
	public void grouping(InputDeclarer declarer, Proxy proxy) {
		Class<?> clazz = proxy.getInner().getClass();
		DirectGrouping grouping = clazz.getAnnotation(DirectGrouping.class);
		grouping(declarer, grouping);
		DirectGroups groups = clazz.getAnnotation(DirectGroups.class);
		if (groups != null) {
			DirectGrouping[] directGroupings = groups.value();
			for (DirectGrouping directGrouping : directGroupings) {
				grouping(declarer, directGrouping);
			}
		}
	}

	private void grouping(InputDeclarer declarer, DirectGrouping grouping) {
		if (grouping == null) {
			return ;
		}
		
		if ("".equals(grouping.stream())) {
			declarer.directGrouping(grouping.value());
		} else {
			declarer.directGrouping(grouping.value(), grouping.stream());
		}
	}

}
