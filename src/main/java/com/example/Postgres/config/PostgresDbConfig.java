package com.example.Postgres.config;



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
	entityManagerFactoryRef = "secondEntityMangerFactoryBean",
	basePackages = {"com.example.Postgres.repo"},
	transactionManagerRef = "secondtransactionManager"
)
public class PostgresDbConfig {
	
	@Autowired
	private Environment environment;
	
//	datasource 
	
	@Bean(name="secondDataSource")
	@Primary
	public DataSource secondDataSource() {
		
		DriverManagerDataSource dataSource=new DriverManagerDataSource();
		dataSource.setUrl(environment.getProperty("spring.second-datasource.url"));
		dataSource.setDriverClassName(environment.getProperty("spring.second-datasource.driver-class-name"));
		dataSource.setUsername(environment.getProperty("spring.second-datasource.username"));
		dataSource.setPassword(environment.getProperty("spring.second-datasource.password"));
		return dataSource;
	}
	 
	
	
//	entityMangerFactory
	@Bean(name="secondEntityMangerFactoryBean")
	@Primary
	public LocalContainerEntityManagerFactoryBean secondEntityMangerFactoryBean() {
		LocalContainerEntityManagerFactoryBean bean=new LocalContainerEntityManagerFactoryBean();
		bean.setDataSource(secondDataSource());
		
		JpaVendorAdapter adapter=new HibernateJpaVendorAdapter();
		bean.setJpaVendorAdapter(adapter);
		Map<String, String> props=new HashMap<>();
		props.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
		props.put("hibernate.show_sql","true");
		props.put("hibernate.hbm2ddl.auto", "update");
		bean.setJpaPropertyMap(props);
		bean.setPackagesToScan("com.example.Postgres.Entitie");
		return bean;
	}
	
//	platfromTransationManager
	
	@Primary
	@Bean(name="secondtransactionManager")
	public PlatformTransactionManager transactionManager() {
		
		JpaTransactionManager manager=new JpaTransactionManager();
		
		manager.setEntityManagerFactory(secondEntityMangerFactoryBean().getObject());
		
		
		return manager;
		
	}
}