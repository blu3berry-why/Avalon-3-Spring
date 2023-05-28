package blu3berry.why.avalon.firebase

import com.google.api.client.util.Value
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.io.IOException
import javax.annotation.PostConstruct


@Service
class FirebaseInitializer {
    /*@Value("\${app.Firebase-config-file}")  ??*/
     var FirebaseConfigPath: String = "firebase-config/avalon-blu3berry-firebase-adminsdk.json"

    var logger: Logger = LoggerFactory.getLogger(FirebaseInitializer::class.java)

    @PostConstruct
    fun initialize(){
        // Get our credentials to authorize this Spring Boot application.
        try {
            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(ClassPathResource(FirebaseConfigPath).inputStream)).build()
            // If our app Firebase application was not initialized, do so.
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options)
                logger.info("Firebase application has been initialized")
            }
        } catch (e: IOException) {
            logger.error(e.message)
        }

    }
}
