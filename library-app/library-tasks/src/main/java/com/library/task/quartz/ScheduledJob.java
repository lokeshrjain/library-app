package com.library.task.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.library.task.scheduling.AnotherBean;

public class ScheduledJob extends QuartzJobBean {

	private AnotherBean anotherBean;
	private String name;

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	protected void executeInternal(final JobExecutionContext arg0)
			throws JobExecutionException {
		anotherBean.printAnotherMessage(name);
	}

	public void setAnotherBean(final AnotherBean anotherBean) {
		this.anotherBean = anotherBean;
	}
}