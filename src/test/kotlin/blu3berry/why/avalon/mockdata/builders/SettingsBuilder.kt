package blu3berry.why.avalon.mockdata.builders

import blu3berry.why.avalon.model.network.Settings

class SettingsBuilder (
        var assassin: Boolean = false,
        var mordred: Boolean = false,
        var morgana: Boolean = false,
        var oberon: Boolean = false,
        var percival: Boolean = false,
        var arnold: Boolean = false,
        ) {
     fun build():Settings {
         return Settings(
                 assassin,
                 mordred,
                 morgana,
                 oberon,
                 percival,
                 arnold
         )
     }
 }