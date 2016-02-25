package org.stream.common.annotation.component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface KafkaSpoutConf {

	String value();
	
	String topologyName();
	
	int tasks() default 1;
	
	int parallelism() default 1;
	
	int maxSpoutPending() default -1;
}
