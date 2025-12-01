package uz.pdp.organizationalsalaryanalytics.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableJpaRepositories(basePackages = ["uz.pdp.organizationalsalaryanalytics.repo"])
@EnableTransactionManagement
class JpaConfig