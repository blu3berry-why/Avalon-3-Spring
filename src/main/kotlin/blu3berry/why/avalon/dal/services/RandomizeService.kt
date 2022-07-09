package blu3berry.why.avalon.dal.services

import blu3berry.why.avalon.model.helpers.Constants
import org.springframework.stereotype.Service
import kotlin.random.Random


@Service
class RandomizeService() {
    fun sixCharStr(): String{
        val characters= "abcdefghijklmnopqrstuvwxyz0123456789"
        var result = ""
        for(i in 0 until 6){
            result += characters[Random.nextInt(0,36)]
        }
        return result
    }


}