package com.jtspringproject.JtSpringProject;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Required for war packaging. Without this, the app can still run via
 * `java -jar`, but JSP views under src/main/webapp/views are not reliably
 * resolved by the embedded container - war packaging + this initializer is
 * what makes JSP rendering work, both for java -jar and for deployment to an
 * external servlet container.
 */
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(JtSpringProjectApplication.class);
	}

}
