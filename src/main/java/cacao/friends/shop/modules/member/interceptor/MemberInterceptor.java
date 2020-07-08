package cacao.friends.shop.modules.member.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public abstract class MemberInterceptor implements HandlerInterceptor {
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if(!isMemberRequestPage(request, modelAndView)) return;
		postHandleProcess(modelAndView);
	}
	
	abstract protected void postHandleProcess(ModelAndView modelAndView);
	
	private boolean isMemberRequestPage(HttpServletRequest request, ModelAndView modelAndView)  {
		if(modelAndView == null)
			return false;
		if(MediaType.APPLICATION_JSON_VALUE.equals(request.getContentType()))
			return false;
		if(modelAndView.getViewName().startsWith("redirect:") || modelAndView.getView() instanceof RedirectView)
			return false;
		if(modelAndView.getViewName().startsWith("manager"))
			return false;
		return true;
	}
	
}
