package blu3berry.why.avalon.model.db.lobby.extensions

import blu3berry.why.avalon.api.errorhandling.exceptions.ConflictException
import blu3berry.why.avalon.model.db.lobby.Lobby
import blu3berry.why.avalon.model.helpers.Constants

internal fun Lobby.select_impl(chosen: List<String>) {
    if (chosen.size != Constants.getAdventreimit(playerSize, info.currentRound))
        ConflictException.Throw(
            "This is not the required amount of people! Required: ${
                Constants.getAdventreimit(
                    playerSize,
                    info.currentRound
                )
            }, but found : ${chosen.size}!"
        )

    if (chosen.isNotEmpty())
        ConflictException.Throw("The king has already chosen")

    currentChosen.addAll(chosen)

    this.info.selectedForAdventure = currentChosen

}