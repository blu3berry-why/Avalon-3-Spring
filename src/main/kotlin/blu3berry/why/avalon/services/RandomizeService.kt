package blu3berry.why.avalon.services

import blu3berry.why.avalon.helpers.Constants
import org.springframework.stereotype.Service
import kotlin.random.Random


@Service
class RandomizeService(var constants: Constants) {
    fun sixCharStr(): String{
        val characters= "abcdefghijklmnopqrstuvwxyz0123456789"
        var result = ""
        for(i in 0 until 6){
            result += characters[Random.nextInt(0,36)]
        }
        return result
    }


}