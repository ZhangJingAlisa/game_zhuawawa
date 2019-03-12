package com.zhua.game.intereptor;

import java.lang.annotation.Annotation;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.izhaowo.code.base.exception.GeneralBusinessException;
import com.izhaowo.code.base.utils.AssertUtil;
import com.izhaowo.code.spring.plus.base.web.BaseController;
import com.izhaowo.code.spring.plus.interceptor.account.LoginRequired;
import com.zhua.game.base.GameBaseController;
import com.zhua.game.service.user.service.UserService;
import com.zhua.game.service.user.vo.UserVO;

public class AccountInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private UserService userService;
	
	private static final String KEY = "token";
	 
	
	/**
	 * 进入 mapping 之前
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		//过滤 登陆验证页面
		boolean flag = true;// 标记是否登录
		System.out.println(request.getContextPath());
		if (handler instanceof HandlerMethod) {
			HandlerMethod hm = (HandlerMethod) handler;
			LoginRequired loginRequired = findAnnotation((HandlerMethod) handler, LoginRequired.class);
			if(loginRequired != null && !loginRequired.request()){ //当有登陆注解 并且 登陆需求是false 
				return super.preHandle(request, response, handler); //直接
			}else{
				String token = keyLoadingFromParameter(request);
				if(token == null){
					token = keyLoadingFromHeader(request);
					if(token == null){
						token = keyLoadingFromCookie(request);
					}
				}
				if(AssertUtil.isNull(token)){
					throw new GeneralBusinessException("800000","未登陆");
				}
				UserVO vo = userService.getUserVOByToken(token);
				//AbstractUserAccount account = accountRequest.getAbstractUserAccount(token);
				if(AssertUtil.isNull(vo) || AssertUtil.isNull(vo.getId())){
					throw new GeneralBusinessException("800000","未登陆");
				}
				injectUser(hm, vo);
			}
			return flag; 
		}	
		return super.preHandle(request, response, handler);
	}
	
//	if(ACCOUNT_TOKEN_KEY.equals(cookie.getName())){
//		AccessBean accessBean = redisCacheService.get(cookie.getValue(),AccessBean.class);
//		if(accessBean != null){
//			injectUser(hm, accessBean);
//			flag = true;
//			break;
//		}
//	}
	
	
	private String keyLoadingFromCookie(HttpServletRequest request){
		String token = null;
		Cookie[] cookies = request.getCookies();
		if(cookies != null){
			for(Cookie cookie : cookies){
				if(KEY.equals(cookie.getName())){
					token = cookie.getValue();
					break;
				}
			}
		}
		return token;
	}
	
	private String keyLoadingFromHeader(HttpServletRequest request){
		String token = request.getHeader(KEY);
		return token;
	}
	
	private String keyLoadingFromParameter(HttpServletRequest request){
		String token = request.getParameter(KEY);
		return token;
	}
	
	/**
	 * 注入AccessBean
	 */
	private void injectUser(HandlerMethod handlerMethod, UserVO vo) {
		//Object obj = new Object();
		//handlerMethod.getBean()
		if(handlerMethod.getBean() instanceof GameBaseController){
			GameBaseController bc = (GameBaseController) handlerMethod.getBean();
			//bc.setUserAccount(account);
			bc.setUserVO(vo);
		}
	}
	
	private <T extends Annotation> T findAnnotation(HandlerMethod handler,Class<T> annotationType) {
		T annotation = handler.getBeanType().getAnnotation(annotationType);
        if (annotation != null){
        	 return annotation;
        }
        return handler.getMethodAnnotation(annotationType);
    }
	
	/**
	 * 清理
	 */
	@Override  
    public void afterCompletion(HttpServletRequest request,  
            HttpServletResponse response, Object handler, Exception ex)  
         throws Exception {  
		if (handler instanceof HandlerMethod) {
			 HandlerMethod hm = (HandlerMethod) handler;
			 if(hm.getBean() instanceof BaseController){
				 GameBaseController bc = (GameBaseController) hm.getBean();
				 bc.clean(); //清除资源
			 }
			
		}
          
    } 
	
}
