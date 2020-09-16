package com.heraizen.zuulapigatewayserver;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

@Component
@Primary
public class SwaggerApiDocsResourceProvider implements SwaggerResourcesProvider {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	DiscoveryClient discoveryClient;

	@Override
	public List<SwaggerResource> get() {
		
		log.info("Initializing of the swagger resources {}", discoveryClient.getServices());
		
		List<SwaggerResource> resources = new ArrayList<>();
		
		discoveryClient.getServices().stream().sorted().forEach(serviceName -> {
			log.info("Initializing the swagger for the service : {}", serviceName);
			
			resources.add(swaggerResource(serviceName, String.format("/%s/v2/api-docs", serviceName), "2.0"));
		
		});

		return resources;
	}

	private SwaggerResource swaggerResource(String name, String url, String version) {
		SwaggerResource swaggerResource = new SwaggerResource();
		swaggerResource.setName(name);
		swaggerResource.setUrl(url);
		swaggerResource.setSwaggerVersion(version);
		return swaggerResource;
	}
}
