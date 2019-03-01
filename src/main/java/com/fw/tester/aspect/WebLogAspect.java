package com.fw.tester.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.util.Arrays;

 

/**
 * Aspect for http log
 */

@Aspect
@Order(5)
@Component
@Slf4j
public class WebLogAspect {
	
	 @Autowired
	 private KafkaTemplate kafkaTemplate;
	 
	 @Value("${spring.kafka.enable}")
	 private boolean kafkaEnable;

    ThreadLocal<Long> startTime = new ThreadLocal<>();
    
    @Pointcut("execution(public * com.fw.tester.controller..*.*(..))")
    public void webLog(){}

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        startTime.set(System.currentTimeMillis());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        LogRequest logRequest = new LogRequest(System.currentTimeMillis(), request.getRequestURL().toString(), request.getMethod(),
        		joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName(), joinPoint.getArgs());
        log.info(JSON.toJSONString(logRequest));
        
        if(kafkaEnable) {
        	try {
            	kafkaTemplate.send("httplog",  JSON.toJSONString(logRequest));
                log.info("Send message to kafka successfully");
            } catch (Exception e) {
            	log.error("Send message to kafka unsuccessfully", e);
            	e.printStackTrace();
            }
        }
    }

 

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
    	HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    	LogResponse logResponse = new LogResponse(startTime.get(), response.getStatus(), ret, System.currentTimeMillis() - startTime.get());
    	log.info(JSON.toJSONString(logResponse));
    	if(kafkaEnable) {
         	try {
             	kafkaTemplate.send("httplog",  JSON.toJSONString(logResponse));
                 log.info("Send message to kafka successfully");
             } catch (Exception e) {
             	log.error("Send message to kafka unsuccessfully", e);
             	e.printStackTrace();
             }
         }
    }
    
    @AfterThrowing(throwing="ex", pointcut = "webLog()")
    public void doThrowing(Throwable ex){
    	LogResponse logResponse = new LogResponse(startTime.get(), HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), System.currentTimeMillis() - startTime.get());
    	log.info(JSON.toJSONString(logResponse));
    	if(kafkaEnable) {
         	try {
             	kafkaTemplate.send("httplog",  JSON.toJSONString(logResponse));
                 log.info("Send message to kafka successfully");
             } catch (Exception e) {
             	log.error("Send message to kafka unsuccessfully", e);
             	e.printStackTrace();
             }
         }
    }
    
    
    @Data
    @AllArgsConstructor
    class LogRequest {
    	private long traceId;
    	private String url;
    	private String httpMethod;
    	private String classMethod;
    	private Object[] args;
    }
    
    @Data
    @AllArgsConstructor
    class LogResponse {
    	private long traceId;
    	private int status;
    	private Object response;
    	private long spendTime;
    }
    
}

 
