package org.stream.common.annotation.strategy;

import org.stream.common.annotation.strategy.impl.AllGroupingStrategy;
import org.stream.common.annotation.strategy.impl.DirectGroupingStrategy;
import org.stream.common.annotation.strategy.impl.FieldsGroupingStrategy;
import org.stream.common.annotation.strategy.impl.GlobalGroupingStrategy;
import org.stream.common.annotation.strategy.impl.LocalOrShuffleGroupingStrategy;
import org.stream.common.annotation.strategy.impl.NoneGroupingStrategy;
import org.stream.common.annotation.strategy.impl.ShuffleGroupingStrategy;

public enum Strategies {

	ALL(new AllGroupingStrategy()),
	
	NONE(new NoneGroupingStrategy()),

	DIRECT(new DirectGroupingStrategy()),
	
	FIELDS(new FieldsGroupingStrategy()),
	
	GLOBAL(new GlobalGroupingStrategy()),
	
	SHUFFLE(new ShuffleGroupingStrategy()),
	
	LOCAL_OR_SHUFFLE(new LocalOrShuffleGroupingStrategy());
	
	private GroupingStrategy strategy;
	
	Strategies(GroupingStrategy strategy) {
		this.strategy = strategy;
	}
	
	public GroupingStrategy getStrategy() {
		return strategy;
	}
	
}
