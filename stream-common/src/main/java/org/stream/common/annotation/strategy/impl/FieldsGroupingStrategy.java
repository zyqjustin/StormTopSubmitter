package org.stream.common.annotation.strategy.impl;

import org.stream.common.annotation.grouping.FieldsGrouping;
import org.stream.common.annotation.groups.FieldsGroups;
import org.stream.common.annotation.strategy.GroupingStrategy;
import org.stream.common.proxy.Proxy;

import backtype.storm.topology.InputDeclarer;
import backtype.storm.tuple.Fields;

public class FieldsGroupingStrategy implements GroupingStrategy {

	@Override
	public void grouping(InputDeclarer declarer, Proxy proxy) {
		Class<?> clazz = proxy.getInner().getClass();
		FieldsGrouping grouping = clazz.getAnnotation(FieldsGrouping.class);
		grouping(declarer, grouping);
		FieldsGroups groups = clazz.getAnnotation(FieldsGroups.class);
		if (groups != null) {
			FieldsGrouping[] fieldsGroupings = groups.value();
			for (FieldsGrouping fieldsGrouping : fieldsGroupings) {
				grouping(declarer, fieldsGrouping);
			}
		}
	}

	private void grouping(InputDeclarer declarer, FieldsGrouping grouping) {
		if (grouping == null) {
			return ;
		}
		
		if ("".equals(grouping.stream())) {
			declarer.fieldsGrouping(grouping.value(), new Fields(grouping.fields()));
		} else {
			declarer.fieldsGrouping(grouping.value(), grouping.stream(), new Fields(grouping.fields()));
		}
	}
	
}
