package gf.app

import gf.Config
import io.restassured.RestAssured
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner

@RunWith(classOf[SpringRunner])
@ContextConfiguration(classes = Array(classOf[Config]))
@SpringBootTest(webEnvironment = RANDOM_PORT)
abstract class IntegrationTest {
  @LocalServerPort var port: Int = _

  @Before def before(): Unit = {
    RestAssured.reset()
    RestAssured.port = port
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
  }
}
