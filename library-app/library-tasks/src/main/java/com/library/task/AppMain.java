package com.library.task;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AppMain {
	@SuppressWarnings({ "unused", "resource" })
	public static void main(final String args[]) {
		final AbstractApplicationContext context = new ClassPathXmlApplicationContext("quartz-context.xml");
			
	}

}