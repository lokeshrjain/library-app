package com.library.task.scheduling;

import org.springframework.stereotype.Component;

@Component("anotherBean")
public class AnotherBean {

	public void printAnotherMessage(final String name) {
		System.out.println("I am called by Quartz jobBean using CronTriggerFactoryBean" + name);
	}

}