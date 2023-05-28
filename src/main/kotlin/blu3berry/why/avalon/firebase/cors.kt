package blu3berry.why.avalon.firebase

import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Bean
fun cors(): WebMvcConfigurer {
   return object : WebMvcConfigurer{
       override fun addCorsMappings(registry: CorsRegistry) {
           // Allow our client (on localhost:4200) to send requests anywhere in our backend
           registry.addMapping("/**").allowedOrigins("http://localhost:4200")
       }
   }
}