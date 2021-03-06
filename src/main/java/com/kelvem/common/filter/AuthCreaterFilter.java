package com.kelvem.common.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;

import com.kelvem.common.utils.AuthUtil;
import com.kelvem.common.utils.WebUtil;
import com.kelvem.sample.system.model.SysAuthorityModel;
import com.kelvem.sample.system.service.SysAuthorityService;

public class AuthCreaterFilter implements Filter {
	
	private static Log log = LogFactory.getLog(AuthCreaterFilter.class);

	@Autowired SysAuthorityService sysAuthorityService;

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		try {
			
//			log.info("===================  doFilter start ======================");
			String url = request.getServletPath();
//			String projectName = request.getContextPath();
//			log.info(ObjectUtil.getObjectInfo(servletRequest));

			sysAuthorityService = WebUtil.getBean(SysAuthorityService.class);
			log.info("AuthCreaterFilter : " + url);
			List<SysAuthorityModel> list = sysAuthorityService.querySysAuthority(null, null, url);
			
			if (list != null && list.size() <= 0) {
				SysAuthorityModel model = AuthUtil.createSysAuthorityModel(url);
				sysAuthorityService.saveSysAuthority(model);
			}
			
//			log.info("===================  doFilter  end   ======================");
		} catch (BeansException e) {
			log.error(e.getMessage(), e);
		}
		
		chain.doFilter(request, response);

	}

	public void init(FilterConfig arg0) throws ServletException {
		
	}
	
	public void destroy() {
		
	}

	
}
