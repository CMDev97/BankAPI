package it.dcm.bank.config.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@Aspect
public class AOPLogController {
    @Autowired
    private ObjectMapper mapper;


    @Pointcut("execution(* it.dcm.bank.generated.api.BankingApi.*(..))")
    public void server() { }


    @Before("server()")
    public void beforeMyMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RequestMapping mapping = signature.getMethod().getAnnotation(RequestMapping.class);
        log.info("------------------ START API --------------------");
        log.info("{} - path(s): {}, method(s): {}", joinPoint.getSignature().getName(), mapping.path(), mapping.method());
    }

    @AfterReturning(pointcut = "server()", returning = "entity")
    public void afterMyMethod(JoinPoint joinPoint, ResponseEntity<?> entity){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        try {
            log.info("{}", signature.getName());
            if (entity != null){
                log.info("response : {}", mapper.writeValueAsString(entity));
            }

        } catch (JsonProcessingException e) {
            log.error("Error while converting", e);
        }
        log.info("------------------ END API --------------------");
    }


    private Map<String, Object> getParameters(JoinPoint joinPoint) {
        CodeSignature signature = (CodeSignature) joinPoint.getSignature();

        HashMap<String, Object> map = new HashMap<>();

        String[] parameterNames = signature.getParameterNames();

        for (int i = 0; i < parameterNames.length; i++) {
            map.put(parameterNames[i], joinPoint.getArgs()[i]);
        }

        return map;
    }
}
