package org.stream.common.utils;

import java.lang.reflect.Field;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanUtil {

	private static ApplicationContext context;
	
	static {
		context = new ClassPathXmlApplicationContext("classpath:application-context.xml");
	}
	
	public static void autowire(Object obj) {
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			Resource resource = field.getAnnotation(Resource.class);
			Autowired autowired = field.getAnnotation(Autowired.class);
			if (resource != null) {
				Object bean;
				if (resource.name() != null && !resource.name().equals("")) {
					bean = context.getBean(resource.name());
				} else if (context.containsBean(field.getName())) {
					bean = context.getBean(field.getName());
				} else {
					bean = context.getBean(field.getType());
				}
				
				field.setAccessible(true);
				try {
					field.set(obj, bean);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
			
			if (autowired != null) {
				Object bean;
				Qualifier qualifier = field.getAnnotation(Qualifier.class);
				if (qualifier != null && qualifier.value() != null && !qualifier.value().equals("")) {
					bean = context.getBean(qualifier.value());
				} else {
					bean = context.getBean(field.getType());
				}
				
				field.setAccessible(true);
				try {
					field.set(obj, bean);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
		
		}
	
	}
	
	public static <T> T getBean(String name, Class<T> clazz) {
		return context.getBean(name, clazz);
	}
	
	public static Object getBean(String name) {
		return context.getBean(name);
	}
	
	public static <T> T getBean(Class<T> clazz) {
		return context.getBean(clazz);
	}
	
	public static ApplicationContext getContext() {
		return context;
	}
}
