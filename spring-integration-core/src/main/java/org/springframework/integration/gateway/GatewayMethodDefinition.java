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

package org.springframework.integration.gateway;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the definition of Gateway methods, when using multiple methods per Gateway interface.
 * &lt;si:method name="echo" request-channel="inputA" reply-timeout="2" request-timeout="200"/&gt;
 * 
 * @author Oleg Zhurakousky
 * @since 2.0
 */
public class GatewayMethodDefinition {

	private volatile String payloadExpression;

	private volatile String requestChannelName;

	private volatile String replyChannelName;

	private volatile String requestTimeout;

	private volatile String replyTimeout;

	private volatile Map<String, Object> staticHeaders = new HashMap<String, Object>();


	public String getPayloadExpression() {
		return payloadExpression;
	}

	public void setPayloadExpression(String payloadExpression) {
		this.payloadExpression = payloadExpression;
	}

	public Map<String, Object> getStaticHeaders() {
		return staticHeaders;
	}

	public void setStaticHeaders(Map<String, Object> staticHeaders) {
		this.staticHeaders = staticHeaders;
	}

	public String getRequestChannelName() {
		return requestChannelName;
	}

	public void setRequestChannelName(String requestChannelName) {
		this.requestChannelName = requestChannelName;
	}

	public String getReplyChannelName() {
		return replyChannelName;
	}

	public void setReplyChannelName(String replyChannelName) {
		this.replyChannelName = replyChannelName;
	}

	public String getRequestTimeout() {
		return requestTimeout;
	}

	public void setRequestTimeout(String requestTimeout) {
		this.requestTimeout = requestTimeout;
	}

	public String getReplyTimeout() {
		return replyTimeout;
	}

	public void setReplyTimeout(String replyTimeout) {
		this.replyTimeout = replyTimeout;
	}

}
