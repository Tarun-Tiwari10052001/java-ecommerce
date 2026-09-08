package com.jtspringproject.JtSpringProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Makes ${appVersion} available in every JSP without every controller having
 * to add it to its own model. BuildProperties is only populated when the app
 * was built with `mvn package` (the build-info Maven goal writes
 * META-INF/build-info.properties) - it's null in some test slices, so this
 * falls back to "dev" rather than failing.
 */
@ControllerAdvice
public class GlobalModelAttributes {

	@Autowired(required = false)
	@Nullable
	private BuildProperties buildProperties;

	@ModelAttribute("appVersion")
	public String appVersion() {
		return buildProperties != null ? buildProperties.getVersion() : "dev";
	}
}
