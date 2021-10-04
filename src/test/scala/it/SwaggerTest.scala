package it

import io.swagger.v3.core.util.Yaml
import io.swagger.v3.jaxrs2.Reader
import io.swagger.v3.oas.integration.ClasspathOpenApiConfigurationLoader
import io.swagger.v3.oas.models.OpenAPI
import org.scalatest.DoNotDiscover

import java.io.File
import scala.jdk.CollectionConverters._

@DoNotDiscover
class SwaggerTest extends BaseIntegrationSpec {

  private val unwantedDefinitions = List(
    "PartialFunctionRequestIOIOResponseIO",
    "ListFormatReqData"
  )

  private val openApiConfResource = "/openapi-configuration.yaml"
  private lazy val reader = new Reader(new ClasspathOpenApiConfigurationLoader().load(openApiConfResource))

  it should "generate swagger api-docs" in {
    //given
    val apiClasses = apiRoutes.map(_.getClass).toSet[Class[_]]

    //when & then
    val openAPI = filteredSwagger(apiClasses)
    Yaml.pretty.writeValue(getOutputFile, openAPI)
  }

  private def getOutputFile: File = {
    var projectDir = new File(getClass.getResource(openApiConfResource).getFile)
    while (projectDir.getName != "opening-hours") {
      projectDir = projectDir.getParentFile
    }

    new File(projectDir, "./src/main/resources/openapi.yaml")
  }

  // Copied from:
  //   https://github.com/swagger-akka-http/swagger-akka-http/blob/master/src/main/scala/com/github/swagger/akka/SwaggerHttpService.scala
  private def filteredSwagger(apiClasses: Set[Class[_]]): OpenAPI = {
    val swagger = reader.read(apiClasses.asJava)
    if (unwantedDefinitions.nonEmpty) {
      val filteredSchemas = swagger.getComponents.getSchemas.asScala.view
        .filterKeys { definitionName =>
          !unwantedDefinitions.contains(definitionName)
        }
        .toMap
        .asJava
      swagger.getComponents.setSchemas(filteredSchemas)
    }
    swagger
  }
}
