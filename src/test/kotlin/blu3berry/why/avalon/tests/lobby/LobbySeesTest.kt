package blu3berry.why.avalon.tests.lobby

import blu3berry.why.avalon.mockdata.builders.LobbyBuilder
import blu3berry.why.avalon.model.db.UserRoleMap
import blu3berry.why.avalon.model.db.lobby.extensions.sees
import blu3berry.why.avalon.model.enums.ROLE
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class LobbySeesTest {

    @Test
    fun `sees should return all evil users except Mordred for Merlin role`() {
        // Arrange
        val lobby = LobbyBuilder("lobby_sees_test")
            .apply {

                this.userRoles
                    .addAll(
                        mutableListOf(
                            UserRoleMap("Player_Merlin", ROLE.MERLIN),
                            UserRoleMap("Player_Mordred", ROLE.MORDRED),
                            UserRoleMap("Player_Assassin", ROLE.ASSASSIN),
                            UserRoleMap("Player_Morgana", ROLE.MORGANA),
                            UserRoleMap("Player_Oberon", ROLE.OBERON),
                            UserRoleMap("Player_Minion_of_Mordred", ROLE.MINION_OF_MORDRED),
                            UserRoleMap("Player_Servant_of_Arthur", ROLE.SERVANT_OF_ARTHUR),
                            UserRoleMap("Player_Percival", ROLE.PERCIVAL),
                            UserRoleMap("Player_Arnold", ROLE.ARNOLD),

                            )
                    )
            }
            .build()

        // Act
        val result = lobby.sees(ROLE.MERLIN, "Player1")

        // Assert
        assertEquals(listOf("Player_Assassin", "Player_Morgana", "Player_Oberon", "Player_Minion_of_Mordred"), result)
    }

    @Test
    fun `sees should return all users looking like Merlin for Percival role`() {
        // Arrange
        val lobby = LobbyBuilder("lobby_sees_test")
            .apply {


                userRoles.addAll(
                    mutableListOf(
                        UserRoleMap("Player_Merlin", ROLE.MERLIN),
                        UserRoleMap("Player_Mordred", ROLE.MORDRED),
                        UserRoleMap("Player_Assassin", ROLE.ASSASSIN),
                        UserRoleMap("Player_Morgana", ROLE.MORGANA),
                        UserRoleMap("Player_Oberon", ROLE.OBERON),
                        UserRoleMap("Player_Minion_of_Mordred", ROLE.MINION_OF_MORDRED),
                        UserRoleMap("Player_Servant_of_Arthur", ROLE.SERVANT_OF_ARTHUR),
                        UserRoleMap("Player_Percival", ROLE.PERCIVAL),
                        UserRoleMap("Player_Arnold", ROLE.ARNOLD),
                    )
                )

            }
            .build()

        // Act
        val result = lobby.sees(ROLE.PERCIVAL, "Player_Percival")

        // Assert
        assertTrue(result.contains("Player_Morgana"))
        assertTrue(result.contains("Player_Merlin"))
    }

    @Test
    fun `sees should return an empty list for Servant of Arthur role`() {
        // Arrange
        val lobby = LobbyBuilder("lobby_sees_test")
            .apply {
                userRoles.addAll(
                    mutableListOf(
                        UserRoleMap("Player_Merlin", ROLE.MERLIN),
                        UserRoleMap("Player_Mordred", ROLE.MORDRED),
                        UserRoleMap("Player_Assassin", ROLE.ASSASSIN),
                        UserRoleMap("Player_Morgana", ROLE.MORGANA),
                        UserRoleMap("Player_Oberon", ROLE.OBERON),
                        UserRoleMap("Player_Minion_of_Mordred", ROLE.MINION_OF_MORDRED),
                        UserRoleMap("Player_Servant_of_Arthur", ROLE.SERVANT_OF_ARTHUR),
                        UserRoleMap("Player_Percival", ROLE.PERCIVAL),
                        UserRoleMap("Player_Arnold", ROLE.ARNOLD)
                    )
                )
            }
            .build()

        // Act
        val result = lobby.sees(ROLE.SERVANT_OF_ARTHUR, "Player_Servant_of_Arthur")

        // Assert
        assertTrue(result.isEmpty())
    }

}