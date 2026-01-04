package uz.pdp.organizationalsalaryanalytics.config

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource

@Configuration
class MessageSourceConfig {

    @Bean
    fun messageSource(): MessageSource {
        val messageSource = ResourceBundleMessageSource()
        
        messageSource.setBasename("messages")
        messageSource.setDefaultEncoding("UTF-8")
        messageSource.setUseCodeAsDefaultMessage(true)
        
        return messageSource
    }
}
