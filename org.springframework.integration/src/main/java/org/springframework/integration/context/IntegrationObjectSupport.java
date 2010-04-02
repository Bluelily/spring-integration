/*
 * Copyright 2002-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.integration.context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.integration.channel.BeanFactoryChannelResolver;
import org.springframework.integration.channel.ChannelResolver;
import org.springframework.integration.support.ComponentMetadata;
import org.springframework.integration.support.ComponentMetadataProvider;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.util.Assert;

/**
 * A base class that provides convenient access to the bean factory as
 * well as {@link ChannelResolver} and {@link TaskScheduler} instances.
 * 
 * <p>This is intended to be used as a base class for internal framework
 * components whereas code built upon the integration framework should not
 * require tight coupling with the context but rather rely on standard
 * dependency injection.
 * 
 * @author Mark Fisher
 */
public abstract class IntegrationObjectSupport implements ComponentMetadataProvider, BeanNameAware, BeanFactoryAware, InitializingBean {

	/** Logger that is available to subclasses */
	protected final Log logger = LogFactory.getLog(getClass());

	private volatile String beanName;

	private volatile BeanFactory beanFactory;

	private volatile ChannelResolver channelResolver;

	private volatile TaskScheduler taskScheduler;

	private volatile ConversionService conversionService;

	private final ComponentMetadata componentMetadata = new ComponentMetadata();


	public final void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	protected String getBeanName() {
		return this.beanName;
	}

	public ComponentMetadata getComponentMetadata() {
		return this.componentMetadata;
	}

	public final void setBeanFactory(BeanFactory beanFactory) {
		Assert.notNull(beanFactory, "beanFactory must not be null");
		this.beanFactory = beanFactory;
	}

	public final void afterPropertiesSet() {
		this.componentMetadata.setComponentName(this.beanName);
		this.populateComponentMetadata(this.componentMetadata);
		try {
			this.onInit();
		}
		catch (Exception e) {
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			}
			throw new BeanInitializationException("failed to initialize", e);
		}
	}

	/**
	 * Subclasses may implement this for initialization logic.
	 */
	protected void onInit() throws Exception {
	}

	protected final BeanFactory getBeanFactory() {
		return this.beanFactory;
	}

	protected ChannelResolver getChannelResolver() {
		if (this.channelResolver == null && this.beanFactory != null) {
			this.channelResolver = new BeanFactoryChannelResolver(this.beanFactory);
		}
		return this.channelResolver;
	}

	protected void setChannelResolver(ChannelResolver channelResolver) {
		Assert.notNull(channelResolver, "channelResolver must not be null");
		this.channelResolver = channelResolver;
	}

	protected TaskScheduler getTaskScheduler() {
		if (this.taskScheduler == null && this.beanFactory != null) {
			this.taskScheduler = IntegrationContextUtils.getTaskScheduler(this.beanFactory);
		}
		return this.taskScheduler;
	}

	protected void setTaskScheduler(TaskScheduler taskScheduler) {
		Assert.notNull(taskScheduler, "taskScheduler must not be null");
		this.taskScheduler = taskScheduler;
	}

	protected final ConversionService getConversionService() {
		if (this.conversionService == null && this.beanFactory != null) {
			this.conversionService = IntegrationContextUtils.getConversionService(this.beanFactory);
			if (this.conversionService == null && logger.isDebugEnabled()) {
				logger.debug("Unable to attempt conversion of Message payload types. Component '" +
						this.getBeanName() + "' has no explicit ConversionService reference, " +
						"and there is no 'integrationConversionService' bean within the context.");
			}
		}
		return this.conversionService;
	}

	protected void setConversionService(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	/**
	 * Subclasses may override this to add attributes to the {@link ComponentMetadata}.
	 */
	protected void populateComponentMetadata(ComponentMetadata metadata) {
	}

	@Override
	public String toString() {
		return (this.beanName != null) ? this.beanName : super.toString();
	}

}
