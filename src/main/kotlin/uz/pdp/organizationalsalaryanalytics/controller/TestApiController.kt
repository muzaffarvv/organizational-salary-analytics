package uz.pdp.organizationalsalaryanalytics.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestApiController {

    @GetMapping("/hello")
    fun hello(): String {
        return "Assalomu alaykum! API is working )))))"
    }
}