package com.topideal.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import net.sf.json.JSONObject;
import org.jasig.cas.client.Protocol;
import org.jasig.cas.client.authentication.*;
import org.jasig.cas.client.configuration.ConfigurationKeys;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.util.ReflectUtils;
import org.jasig.cas.client.validation.Assertion;
/**
 * 自定义的过滤器,目的是为了解决session过期或失效点击页面上的接口报异常的问题
 * 如果是location.href的页面跳转则没有问题，如果是ajax请求则会报接口异常跨域，是因为cas无法识别ajax请求
 **/
public class MyCasAuthenticationFilter extends AbstractCasFilter {
    private String casServerLoginUrl;
    private boolean renew;
    private boolean gateway;
    private GatewayResolver gatewayStorage;
    private AuthenticationRedirectStrategy authenticationRedirectStrategy;
    private UrlPatternMatcherStrategy ignoreUrlPatternMatcherStrategyClass;
    private static final Map<String, Class<? extends UrlPatternMatcherStrategy>> PATTERN_MATCHER_TYPES = new HashMap();

    public MyCasAuthenticationFilter() {
        this(Protocol.CAS2);
    }

    protected MyCasAuthenticationFilter(Protocol protocol) {
        super(protocol);
        this.renew = false;
        this.gateway = false;
        this.gatewayStorage = new DefaultGatewayResolverImpl();
        this.authenticationRedirectStrategy = new DefaultAuthenticationRedirectStrategy();
        this.ignoreUrlPatternMatcherStrategyClass = null;
    }

    protected void initInternal(FilterConfig filterConfig) throws ServletException {
        if (!this.isIgnoreInitConfiguration()) {
            super.initInternal(filterConfig);
            String loginUrl = this.getString(ConfigurationKeys.CAS_SERVER_LOGIN_URL);
            if (loginUrl != null) {
                this.setCasServerLoginUrl(loginUrl);
            } else {
                this.setCasServerUrlPrefix(this.getString(ConfigurationKeys.CAS_SERVER_URL_PREFIX));
            }

            this.setRenew(this.getBoolean(ConfigurationKeys.RENEW));
            this.setGateway(this.getBoolean(ConfigurationKeys.GATEWAY));
            String ignorePattern = this.getString(ConfigurationKeys.IGNORE_PATTERN);
            String ignoreUrlPatternType = this.getString(ConfigurationKeys.IGNORE_URL_PATTERN_TYPE);
            Class gatewayStorageClass;
            if (ignorePattern != null) {
                gatewayStorageClass = (Class)PATTERN_MATCHER_TYPES.get(ignoreUrlPatternType);
                if (gatewayStorageClass != null) {
                    this.ignoreUrlPatternMatcherStrategyClass = (UrlPatternMatcherStrategy)ReflectUtils.newInstance(gatewayStorageClass.getName(), new Object[0]);
                } else {
                    try {
                        this.logger.trace("Assuming {} is a qualified class name...", ignoreUrlPatternType);
                        this.ignoreUrlPatternMatcherStrategyClass = (UrlPatternMatcherStrategy)ReflectUtils.newInstance(ignoreUrlPatternType, new Object[0]);
                    } catch (IllegalArgumentException var7) {
                        this.logger.error("Could not instantiate class [{}]", ignoreUrlPatternType, var7);
                    }
                }

                if (this.ignoreUrlPatternMatcherStrategyClass != null) {
                    this.ignoreUrlPatternMatcherStrategyClass.setPattern(ignorePattern);
                }
            }

            gatewayStorageClass = this.getClass(ConfigurationKeys.GATEWAY_STORAGE_CLASS);
            if (gatewayStorageClass != null) {
                this.setGatewayStorage((GatewayResolver)ReflectUtils.newInstance(gatewayStorageClass, new Object[0]));
            }

            Class<? extends AuthenticationRedirectStrategy> authenticationRedirectStrategyClass = this.getClass(ConfigurationKeys.AUTHENTICATION_REDIRECT_STRATEGY_CLASS);
            if (authenticationRedirectStrategyClass != null) {
                this.authenticationRedirectStrategy = (AuthenticationRedirectStrategy)ReflectUtils.newInstance(authenticationRedirectStrategyClass, new Object[0]);
            }
        }

    }

    public void init() {
        super.init();
        String message = String.format("one of %s and %s must not be null.", ConfigurationKeys.CAS_SERVER_LOGIN_URL.getName(), ConfigurationKeys.CAS_SERVER_URL_PREFIX.getName());
        CommonUtils.assertNotNull(this.casServerLoginUrl, message);
    }

    public final void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        String requestURL = request.getRequestURI().toString();

        if (this.isRequestUrlExcluded(request)) {
            this.logger.debug("Request is ignored.");
            filterChain.doFilter(request, response);
        } else {
            HttpSession session = request.getSession(false);
            Assertion assertion = session != null ? (Assertion)session.getAttribute("_const_cas_assertion_") : null;
            if (assertion != null) {
                filterChain.doFilter(request, response);
            } else {
                String serviceUrl = this.constructServiceUrl(request, response);
                String ticket = this.retrieveTicketFromRequest(request);
                boolean wasGatewayed = this.gateway && this.gatewayStorage.hasGatewayedAlready(request, serviceUrl);
                if (!CommonUtils.isNotBlank(ticket) && !wasGatewayed) {
                    this.logger.debug("no ticket and no assertion found");
                    String modifiedServiceUrl;
                    if (this.gateway) {
                        this.logger.debug("setting gateway attribute in session");
                        modifiedServiceUrl = this.gatewayStorage.storeGatewayInformation(request, serviceUrl);
                    } else {
                        modifiedServiceUrl = serviceUrl;
                    }

                    this.logger.debug("Constructed service url: {}", modifiedServiceUrl);
                    String urlToRedirectTo = CommonUtils.constructRedirectUrl(this.casServerLoginUrl, this.getProtocol().getServiceParameterName(), modifiedServiceUrl, this.renew, this.gateway);
                    //判断请求方式是否为ajax请求
                    String header = request.getHeader("X-Requested-With");
                    if(header != null && "XMLHttpRequest".equals(header)){
                        //登录已经超时或者认证未通过
                        ajaxHttpToLogin(request,response);
                        return;
                    }else{
                        this.logger.debug("redirecting to \"{}\"", urlToRedirectTo);
                        this.authenticationRedirectStrategy.redirect(request, response, urlToRedirectTo);
                    }
                } else {
                    filterChain.doFilter(request, response);
                }
            }
        }
    }

    /**
     * ajax请求标记
     * @param request
     * @param response
     */
    private void ajaxHttpToLogin(HttpServletRequest request, HttpServletResponse response) {
        try {
            ResponseBean responseBean = WebResponseFactory.responseBuild(MessageEnum.LOGINTIMEOUT_99998);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(JSONObject.fromObject(responseBean).toString());
            writer.flush();
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public final void setRenew(boolean renew) {
        this.renew = renew;
    }

    public final void setGateway(boolean gateway) {
        this.gateway = gateway;
    }

    public final void setCasServerUrlPrefix(String casServerUrlPrefix) {
        this.setCasServerLoginUrl(CommonUtils.addTrailingSlash(casServerUrlPrefix) + "login");
    }

    public final void setCasServerLoginUrl(String casServerLoginUrl) {
        this.casServerLoginUrl = casServerLoginUrl;
    }

    public final void setGatewayStorage(GatewayResolver gatewayStorage) {
        this.gatewayStorage = gatewayStorage;
    }

    private boolean isRequestUrlExcluded(HttpServletRequest request) {
        if (this.ignoreUrlPatternMatcherStrategyClass == null) {
            return false;
        } else {
            StringBuffer urlBuffer = request.getRequestURL();
            if (request.getQueryString() != null) {
                urlBuffer.append("?").append(request.getQueryString());
            }

            String requestUri = urlBuffer.toString();
            return this.ignoreUrlPatternMatcherStrategyClass.matches(requestUri);
        }
    }

    public final void setIgnoreUrlPatternMatcherStrategyClass(UrlPatternMatcherStrategy ignoreUrlPatternMatcherStrategyClass) {
        this.ignoreUrlPatternMatcherStrategyClass = ignoreUrlPatternMatcherStrategyClass;
    }

    static {
        PATTERN_MATCHER_TYPES.put("CONTAINS", ContainsPatternUrlPatternMatcherStrategy.class);
        PATTERN_MATCHER_TYPES.put("REGEX", RegexUrlPatternMatcherStrategy.class);
        PATTERN_MATCHER_TYPES.put("EXACT", ExactUrlPatternMatcherStrategy.class);
    }
}

