package org.stream.common.proxy;

import org.stream.common.utils.BeanUtil;

import storm.kafka.SpoutConfig;

public abstract class AbstractKafkaSpoutConfig {

	public AbstractKafkaSpoutConfig() {
		BeanUtil.autowire(this);
	}

	public abstract SpoutConfig getSpoutConfig();
	
}
