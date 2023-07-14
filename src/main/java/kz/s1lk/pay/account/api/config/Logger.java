package kz.s1lk.pay.account.api.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Aspect
@Component
public class Logger {
    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());


    @Pointcut("within (@org.springframework.web.bind.annotation.RestController *)")
    public void controllerListenerAspect() {
    }

    @Before("controllerListenerAspect()")
    public void logForController(JoinPoint joinPoint) throws JsonProcessingException {
        List<String> body = new ArrayList<>();
        List<String> params = new ArrayList<>();
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.
                getRequestAttributes())).getRequest();
        ObjectMapper objectMapper = new ObjectMapper();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Annotation[][] annotationMatrix = methodSignature.getMethod().getParameterAnnotations();
        int index = -1;
        int indexSecond = -1;
        log.info("---------------------------------------");
        log.info("Received {} in {} ", request.getMethod(), request.getServletPath());
        for (Annotation[] annotations : annotationMatrix) {
            index++;
            for (Annotation annotation : annotations) {
                if (!(annotation instanceof RequestBody))
                    continue;
                Object requestBody = joinPoint.getArgs()[index];
                body.add(objectMapper.writeValueAsString(requestBody));
            }
        }
        for (Annotation[] annotations : annotationMatrix) {
            indexSecond++;
            for (Annotation annotation : annotations) {
                if (!(annotation instanceof RequestParam))
                    continue;
                try {
                    Object requestBody = joinPoint.getArgs()[indexSecond];
                    params.add(objectMapper.writeValueAsString(requestBody));
                } catch (Exception exception) {
                    log.info(exception.getMessage());
                }
            }
        }
        log.info("Body: {}", body);
        log.info("Params: {}", params);
        log.info("---------------------------------------");


    }
}
