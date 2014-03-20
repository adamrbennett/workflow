package com.lwolf.wf.admin.servlets;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.lwolf.scud.locators.DataSourceLocator;
import com.lwolf.scud.locators.IdLocator;
import com.lwolf.wf.admin.locators.TomeeResourceLocator;
import com.lwolf.wf.dto.UserDto;
import com.lwolf.wf.exceptions.RepositoryException;
import com.lwolf.wf.locators.UniqueIdLocator;
import com.lwolf.wf.persistence.DataFactory;
import com.lwolf.wf.repository.UserRepository;
import com.lwolf.wf.repository.impl.UserRepositoryImpl;

/**
 * Servlet Filter implementation class AuthenticationFilter
 */
@WebFilter("/index.html")
public class AuthenticationFilter implements Filter {
	
	private final DataSourceLocator dataSourceLocator = TomeeResourceLocator.locator();
	private final IdLocator idLocator = UniqueIdLocator.load(new UniqueIdLocator(TomeeResourceLocator.locator()));
	private final DataFactory dataFactory = DataFactory.getDataFactory(DataFactory.FactoryType.JDBC, dataSourceLocator, idLocator);
	private final UserRepository userRepository = new UserRepositoryImpl(dataFactory.userDao());

    /**
     * Default constructor. 
     */
    public AuthenticationFilter() {
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpSession session = httpRequest.getSession();
		
		UserDto user = (UserDto) session.getAttribute("UserContext");
		String userName = httpRequest.getRemoteUser();
		
		try {
			if (user == null && userName != null && !userName.equals("")) {
				user = new UserDto();
				user.userName = userName;
				userRepository.get(user);
				session.setAttribute("UserContext", user);
			}
		} catch (RepositoryException ex) {
			throw new ServletException(ex);
		}
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
