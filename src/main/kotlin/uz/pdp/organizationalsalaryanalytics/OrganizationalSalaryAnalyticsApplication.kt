package uz.pdp.organizationalsalaryanalytics

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication(scanBasePackages = ["uz.pdp"])
@EnableJpaAuditing
class OrganizationalSalaryAnalyticsApplication

fun main(args: Array<String>) {
    runApplication<OrganizationalSalaryAnalyticsApplication>(*args)
}
