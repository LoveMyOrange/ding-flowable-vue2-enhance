package springfox.documentation.spring.web;

import static java.util.Optional.ofNullable;

import com.fasterxml.classmate.ResolvedType;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.AbstractRequestCondition;
import org.springframework.web.servlet.mvc.condition.ConsumesRequestCondition;
import org.springframework.web.servlet.mvc.condition.HeadersRequestCondition;
import org.springframework.web.servlet.mvc.condition.ParamsRequestCondition;
import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import springfox.documentation.RequestHandler;
import springfox.documentation.RequestHandlerKey;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spring.web.readers.operation.HandlerMethodResolver;
import springfox.documentation.spring.wrapper.NameValueExpression;
import springfox.documentation.spring.wrapper.PatternsRequestCondition;

/**
 * SpringBoot 2.6 Swagger无法使用
 * 方法1：重写此类， 不影响SpringBoot匹配方式
 * 方法2：改匹配方式为ANT
 * spring:
 *   mvc:
 *     pathmatch:
 *       matching-strategy: ANT_PATH_MATCHER
 */
public class WebMvcRequestHandler implements RequestHandler {
    private final String contextPath;
    private final HandlerMethodResolver methodResolver;
    private final RequestMappingInfo requestMapping;
    private final HandlerMethod handlerMethod;



    private static final org.springframework.web.servlet.mvc.condition.PatternsRequestCondition EMPTY_PATTERNS = new org.springframework.web.servlet.mvc.condition.PatternsRequestCondition();

    private static final ParamsRequestCondition EMPTY_PARAMS = new ParamsRequestCondition();

    private static final HeadersRequestCondition EMPTY_HEADERS = new HeadersRequestCondition();

    private static final ConsumesRequestCondition EMPTY_CONSUMES = new ConsumesRequestCondition();

    private static final ProducesRequestCondition EMPTY_PRODUCES = new ProducesRequestCondition();

    public WebMvcRequestHandler(
            String contextPath,
            HandlerMethodResolver methodResolver,
            RequestMappingInfo requestMapping,
            HandlerMethod handlerMethod) {
        this.contextPath = contextPath;
        this.methodResolver = methodResolver;
        this.requestMapping = requestMapping;
        this.handlerMethod = handlerMethod;
    }

    @Override
    public HandlerMethod getHandlerMethod() {
        return handlerMethod;
    }

    @Override
    public RequestHandler combine(RequestHandler other) {
        return this;
    }

    @Override
    public Class<?> declaringClass() {
        return handlerMethod.getBeanType();
    }

    @Override
    public boolean isAnnotatedWith(Class<? extends Annotation> annotation) {
        return null != AnnotationUtils.findAnnotation(handlerMethod.getMethod(), annotation);
    }

    @Override
    public PatternsRequestCondition getPatternsCondition() {
        AbstractRequestCondition contition = requestMapping.getPatternsCondition();
        if(contition == null){
            contition = new org.springframework.web.servlet.mvc.condition.PatternsRequestCondition(requestMapping.getPathPatternsCondition().getPatterns().stream().map(p->p.getPatternString()).collect(Collectors.toList()).toArray(new String[]{}));
        }
        return new WebMvcPatternsRequestConditionWrapper(
                contextPath,
                (org.springframework.web.servlet.mvc.condition.PatternsRequestCondition) contition);
    }

    @Override
    public String groupName() {
        return ControllerNamingUtils.controllerNameAsGroup(handlerMethod);
    }

    @Override
    public String getName() {
        return handlerMethod.getMethod().getName();
    }

    @Override
    public Set<RequestMethod> supportedMethods() {
        return requestMapping.getMethodsCondition().getMethods();
    }

    @Override
    public Set<MediaType> produces() {
        return Optional.ofNullable(requestMapping.getProducesCondition()).orElseGet(()->EMPTY_PRODUCES).getProducibleMediaTypes();
    }

    @Override
    public Set<MediaType> consumes() {
        return Optional.ofNullable(requestMapping.getConsumesCondition()).orElseGet(()->EMPTY_CONSUMES).getConsumableMediaTypes();
    }

    @Override
    public Set<NameValueExpression<String>> headers() {
        return WebMvcNameValueExpressionWrapper.from(Optional.ofNullable(requestMapping.getHeadersCondition()).orElseGet(()->EMPTY_HEADERS).getExpressions());
    }

    @Override
    public Set<NameValueExpression<String>> params() {
        return WebMvcNameValueExpressionWrapper.from(Optional.ofNullable(requestMapping.getParamsCondition()).orElseGet(()->EMPTY_PARAMS).getExpressions());
    }

    @Override
    public <T extends Annotation> Optional<T> findAnnotation(Class<T> annotation) {
        return ofNullable(AnnotationUtils.findAnnotation(handlerMethod.getMethod(), annotation));
    }

    @Override
    public RequestHandlerKey key() {
        return new RequestHandlerKey(
                Optional.ofNullable(requestMapping.getPatternsCondition()).orElseGet(()->EMPTY_PATTERNS).getPatterns(),
                requestMapping.getMethodsCondition().getMethods(),
                Optional.ofNullable(requestMapping.getConsumesCondition()).orElseGet(()->EMPTY_CONSUMES).getConsumableMediaTypes(),
                Optional.ofNullable(requestMapping.getProducesCondition()).orElseGet(()->EMPTY_PRODUCES).getProducibleMediaTypes());
    }

    @Override
    public springfox.documentation.spring.wrapper.RequestMappingInfo<?> getRequestMapping() {
        return new WebMvcRequestMappingInfoWrapper(requestMapping);
    }

    @Override
    public List<ResolvedMethodParameter> getParameters() {
        return methodResolver.methodParameters(handlerMethod);
    }

    @Override
    public ResolvedType getReturnType() {
        return methodResolver.methodReturnType(handlerMethod);
    }

    @Override
    public <T extends Annotation> Optional<T> findControllerAnnotation(Class<T> annotation) {
        return ofNullable(AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), annotation));
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", WebMvcRequestHandler.class.getSimpleName() + "{", "}")
                .add("requestMapping=" + requestMapping)
                .add("handlerMethod=" + handlerMethod)
                .add("key=" + key())
                .toString();
    }
}
