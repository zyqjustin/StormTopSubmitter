package org.stream.common.annotation.strategy.impl;

import org.stream.common.annotation.grouping.GlobalGrouping;
import org.stream.common.annotation.groups.GlobalGroups;
import org.stream.common.annotation.strategy.GroupingStrategy;
import org.stream.common.proxy.Proxy;

import backtype.storm.topology.InputDeclarer;

public class GlobalGroupingStrategy implements GroupingStrategy {

	@Override
	public void grouping(InputDeclarer declarer, Proxy proxy) {
		Class<?> clazz = proxy.getInner().getClass();
		GlobalGrouping grouping = clazz.getAnnotation(GlobalGrouping.class);
		grouping(declarer, grouping);
		GlobalGroups groups = clazz.getAnnotation(GlobalGroups.class);
		if (groups != null) {
			GlobalGrouping[] globalGroupings = groups.value();
			for (GlobalGrouping globalGrouping : globalGroupings) {
				grouping(declarer, globalGrouping);
			}
		}
	}

	private void grouping(InputDeclarer declarer, GlobalGrouping grouping) {
		if (grouping == null) {
			return ;
		}
		
		if ("".equals(grouping.stream())) {
			declarer.globalGrouping(grouping.value());
		} else {
			declarer.globalGrouping(grouping.value(), grouping.stream());
		}
		
	}

}
