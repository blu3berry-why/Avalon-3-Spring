package blu3berry.why.avalon.model.db.lobby.extensions

import blu3berry.why.avalon.api.errorhandling.exceptions.ConflictException
import blu3berry.why.avalon.model.db.lobby.Lobby
import blu3berry.why.avalon.model.helpers.Constants

internal fun Lobby.select_impl(chosen: List<String>, username:String) {
    if(this.info.king != username)
        ConflictException.Throw("You are not the king!")

    if(chosen.toSet().size != chosen.size)
        ConflictException.Throw("Repeated usernames!")

    if (chosen.size != Constants.getAdventreimit(playerSize, info.currentRound))
        ConflictException.Throw(
            "This is not the required amount of people! Required: ${
                Constants.getAdventreimit(
                    playerSize,
                    info.currentRound
                )
            }, but found: ${chosen.size}!"
        )

    if (currentChosen.isNotEmpty())
        ConflictException.Throw("The king has already chosen")

    currentChosen.addAll(chosen)

    this.info.selectedForAdventure = currentChosen
    //this.votes[this.info.currentRound-1].chosen.addAll(currentChosen)

}