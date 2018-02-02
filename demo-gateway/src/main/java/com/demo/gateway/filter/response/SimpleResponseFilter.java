package com.demo.gateway.filter.response;

import com.netflix.zuul.ZuulFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import com.netflix.zuul.context.RequestContext;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;
import static org.springframework.util.ReflectionUtils.rethrowRuntimeException;

@Component
public class SimpleResponseFilter extends ZuulFilter {

	@Override
	public Object run() {
		try {
			RequestContext context = getCurrentContext();
			InputStream stream = context.getResponseDataStream();
			String body = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
			context.setResponseBody("Modified via simple response filter: "+body);
		}
		catch (IOException e) {
			rethrowRuntimeException(e);
		}
		return null;
	}

	@Override
	public boolean shouldFilter() {
		RequestContext context = getCurrentContext();
		return context.getRequest().getRequestURI().contains("api");
	}

	@Override
	public String filterType() {
		return "post";
	}

	@Override
	public int filterOrder() {
		return 999;
	}

}
