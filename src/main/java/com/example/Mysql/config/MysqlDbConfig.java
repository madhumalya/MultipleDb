package com.example.Mysql.config;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
	entityManagerFactoryRef = "entityMangerFactoryBean",
	basePackages = {"com.example.Mysql.repo"},
	transactionManagerRef = "transactionManager"
)
public class MysqlDbConfig {
	
	@Autowired
	private Environment environment;
	
//	datasource 
	
	@Bean(name="datasource")
	@Primary
	public DataSource dataSource() {
		
		DriverManagerDataSource dataSource=new DriverManagerDataSource();
		dataSource.setUrl(environment.getProperty("spring.datasource.url"));
		dataSource.setDriverClassName(environment.getProperty("spring.datasource.driver-class-name"));
		dataSource.setUsername(environment.getProperty("spring.datasource.username"));
		dataSource.setPassword(environment.getProperty("spring.datasource.password"));
		return dataSource;
		
	}
	 
	
	
//	entityMangerFactory
	@Bean(name="entityMangerFactoryBean")
	@Primary
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
		LocalContainerEntityManagerFactoryBean bean=new LocalContainerEntityManagerFactoryBean();
		bean.setDataSource(dataSource());
		
		JpaVendorAdapter adapter=new HibernateJpaVendorAdapter();
		bean.setJpaVendorAdapter(adapter);
		Map<String, String> props=new HashMap<>();
		props.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
		props.put("hibernate.show_sql","true");
		props.put("hibernate.hbm2ddl.auto", "update");
		bean.setJpaPropertyMap(props);
		bean.setPackagesToScan("com.example.Mysql.Entities");
		return bean;
	}
	
//	platfromTransationManager
	
	@Primary
	@Bean(name="transactionManager")
	public PlatformTransactionManager transactionManager() {
		
		JpaTransactionManager manager=new JpaTransactionManager();
		
		manager.setEntityManagerFactory(entityManagerFactoryBean().getObject());
		
		
		return manager;
		
	}

	
	
	
	

}
