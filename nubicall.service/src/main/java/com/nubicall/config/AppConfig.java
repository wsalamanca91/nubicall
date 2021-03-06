package com.nubicall.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.hibernate.ejb.HibernatePersistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.annotation.Resource;
import javax.sql.DataSource;


@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.nubicall")
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
@EnableJpaRepositories("com.nubicall.repositories")
public class AppConfig extends WebMvcConfigurerAdapter{
		
	private static final String PACKAGE_ENTITIES = "com.nubicall.model";
	private static final String PROPERTY_NAME_DATABASE_DRIVER = "db.driver";
    private static final String PROPERTY_NAME_DATABASE_PASSWORD = "db.password";
    private static final String PROPERTY_NAME_DATABASE_URL = "db.url";
    private static final String PROPERTY_NAME_DATABASE_USERNAME = "db.username";
    private static final String PROPERTY_NAME_HIBERNATE_DIALECT = "hibernate.dialect";
    private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    private static final String PROPERTY_NAME_HIBERNATE_DDL_AUTO = "hibernate.hbm2ddl.auto";
	
	@Bean
    public ViewResolver contentNegotiatingViewResolver(
            ContentNegotiationManager manager) {

        List<ViewResolver> resolvers = new ArrayList<ViewResolver>();

        InternalResourceViewResolver pagesResolver = new InternalResourceViewResolver();
        pagesResolver.setPrefix("/WEB-INF/");
        pagesResolver.setSuffix(".jsp");
        pagesResolver.setViewClass(JstlView.class);
        resolvers.add(pagesResolver);

        JsonViewResolver jsonResolver = new JsonViewResolver();
        resolvers.add(jsonResolver);

        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
        resolver.setViewResolvers(resolvers);
        resolver.setContentNegotiationManager(manager);
        return resolver;

    }

    /**
     * View resolver for returning JSON in a view-based system. Always returns a
     * {@link MappingJacksonJsonView}.
     */
    public class JsonViewResolver implements ViewResolver{
    	 
        @Override
        public View resolveViewName(String viewName, Locale locale) throws Exception {
            MappingJackson2JsonView view = new MappingJackson2JsonView();
            view.setPrettyPrint(true);       
            return view;
          }
    }
    
    @Resource
	private Environment env;
    
    @Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		
		dataSource.setDriverClassName(env.getRequiredProperty(PROPERTY_NAME_DATABASE_DRIVER));
		dataSource.setUrl(env.getRequiredProperty(PROPERTY_NAME_DATABASE_URL));
		dataSource.setUsername(env.getRequiredProperty(PROPERTY_NAME_DATABASE_USERNAME));
		dataSource.setPassword(env.getRequiredProperty(PROPERTY_NAME_DATABASE_PASSWORD));
		
		return dataSource;
	}
    
    @Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setDataSource(dataSource());
		entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistence.class);
		entityManagerFactoryBean.setPackagesToScan(PACKAGE_ENTITIES);
				
		entityManagerFactoryBean.setJpaProperties(hibProperties());
		
		return entityManagerFactoryBean;
	}
    
    private Properties hibProperties() {
		Properties properties = new Properties();
		properties.put(PROPERTY_NAME_HIBERNATE_DIALECT, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_DIALECT));
		properties.put(PROPERTY_NAME_HIBERNATE_SHOW_SQL, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_SHOW_SQL));
		properties.put(PROPERTY_NAME_HIBERNATE_DDL_AUTO, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_DDL_AUTO));
		return properties;	
	}
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
    	registry.addMapping("/**").allowedOrigins("*").
    	allowedMethods("GET", "POST", "PUT","DELETE","HEAD");
    }
    
    @Bean
	public JpaTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
		return transactionManager;
	}
}
