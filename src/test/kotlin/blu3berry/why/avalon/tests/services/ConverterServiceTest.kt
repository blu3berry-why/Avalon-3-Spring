package blu3berry.why.avalon.tests.services

import blu3berry.why.avalon.dal.services.ConverterService
import blu3berry.why.avalon.model.db.User
import blu3berry.why.avalon.model.network.LoginInfo
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ConverterServiceTest {
    private val converterService = ConverterService()

    @Test
    fun `test toSingleLoginInfo converts User to SingleLoginInfo`() {
        // Arrange
        val user = User(ObjectId(),"john", "password","john@example.com",  "salt", mutableListOf())

        // Act
        val loginInfo = converterService.toSingleLoginInfo(user)

        // Assert
        assertEquals(user.username, loginInfo.username)
        assertEquals(user.email, loginInfo.email)


    }

    @Test
    fun `test toSingleLoginInfo converts User to SingleLoginInfo not converting password and friends`() {
        // Arrange
        val user = User(ObjectId(),"john", "password","john@example.com",  "salt", mutableListOf())

        // Act
        val loginInfo = converterService.toSingleLoginInfo(user)

        // Assert


        //Can not give out information
        assertNull(loginInfo.password)
        assertNull(loginInfo.friends)
    }

    @Test
    fun `test toLoginInfo converts User to LoginInfo`() {
        // Arrange
        val friend1 = User(ObjectId(),"friend1",  "password1","friend1@example.com", "salt", mutableListOf())
        val friend2 = User(ObjectId(),"friend2", "password2","friend2@example.com",  "salt",mutableListOf())
        val user = User(ObjectId(),"john", "password","john@example.com",  "salt",mutableListOf(friend1, friend2))

        // Act
        val loginInfo = converterService.toLoginInfo(user)

        // Assert
        assertNotNull(loginInfo)
        assertEquals(user.username, loginInfo?.username)
        assertEquals(user.email, loginInfo?.email)
        assertNull(loginInfo?.password)

        val expectedFriends = listOf(
            LoginInfo("friend1", null,"friend1@example.com",  null),
            LoginInfo("friend2", null,"friend2@example.com",  null)
        )
        assertEquals(expectedFriends, loginInfo?.friends)
    }

    @Test
    fun `test toLoginInfo returns null when user is null`() {
        // Arrange
        val user: User? = null

        // Act
        val loginInfo = converterService.toLoginInfo(user)

        // Assert
        assertNull(loginInfo)
    }
}