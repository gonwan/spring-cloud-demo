//package com.gonwan.springcloud.zuul.filter;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
//import org.springframework.stereotype.Component;
//
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
//
//import brave.Tracer;
//
//@Component
//public class ResponseFilter extends ZuulFilter {
//
//    private static final String CORRELATION_ID = "sc-correlation-id";
//
//    @Autowired
//    private Tracer tracer;
//
//    @Override
//    public String filterType() {
//        return FilterConstants.PRE_TYPE;
//    }
//
//    @Override
//    public int filterOrder() {
//        return FilterConstants.SEND_RESPONSE_FILTER_ORDER - 1;
//    }
//
//    @Override
//    public boolean shouldFilter() {
//        return true;
//    }
//
//    @Override
//    public Object run() {
//        RequestContext ctx = RequestContext.getCurrentContext();
//        ctx.getResponse().addHeader(CORRELATION_ID, tracer.currentSpan().context().traceIdString());
//        return null;
//    }
//
//}
