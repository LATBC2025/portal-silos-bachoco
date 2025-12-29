package com.bachoco.app_boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.filter.CharacterEncodingFilter;

@SpringBootApplication(scanBasePackages = {
		"com.bachoco",
		"com.bachoco.infrastructure"
})
@EnableJpaRepositories({
	"com.bachoco.infrastructure.persistence.repository",
	"com.bachoco.persistence.repository"
})
@EntityScan({
	"com.bachoco.infrastructure.persistence.entity",
	"com.bachoco.persistence.entity"
})
@EnableScheduling
public class AppBootApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(AppBootApplication.class, args);
	}
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AppBootApplication.class);
    }
	
    @Bean
    public FilterRegistrationBean<CharacterEncodingFilter> characterEncodingFilterRegistration() {
        FilterRegistrationBean<CharacterEncodingFilter> registration = new FilterRegistrationBean<>();
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        return registration;
    }

}
