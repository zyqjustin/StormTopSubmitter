package org.stream.common.annotation.strategy.impl;

import org.stream.common.annotation.grouping.NoneGrouping;
import org.stream.common.annotation.groups.NoneGroups;
import org.stream.common.annotation.strategy.GroupingStrategy;
import org.stream.common.proxy.Proxy;

import backtype.storm.topology.InputDeclarer;

public class NoneGroupingStrategy implements GroupingStrategy {

	@Override
	public void grouping(InputDeclarer declarer, Proxy proxy) {
		Class<?> clazz = proxy.getInner().getClass();
		NoneGrouping grouping = clazz.getAnnotation(NoneGrouping.class);
		grouping(declarer, grouping);
		NoneGroups groups = clazz.getAnnotation(NoneGroups.class);
		if (groups != null) {
			NoneGrouping[] noneGroupings = groups.value();
			for (NoneGrouping noneGrouping : noneGroupings) {
				grouping(declarer, noneGrouping);
			}
		}
	}

	private void grouping(InputDeclarer declarer, NoneGrouping grouping) {
		if (grouping == null) {
			return ;
		}
		
		if ("".equals(grouping.stream())) {
			declarer.noneGrouping(grouping.value());
		} else {
			declarer.noneGrouping(grouping.value(), grouping.stream());
		}
	}

}
