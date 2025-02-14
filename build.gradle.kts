import groovy.util.Node
import groovy.util.NodeList
import groovy.xml.XmlParser
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
    application
    id("io.gitlab.arturbosch.detekt") version "1.18.0-RC3"
    id("org.jetbrains.dokka") version "1.4.32"
    id("org.jetbrains.kotlinx.kover") version "0.6.1"
}

group = "edu.udo.cs.sopra"
version = "1.0"

repositories {
    mavenCentral()
}

application {
    mainClass.set("MainKt")
}

dependencies {
    testImplementation(kotlin("test-junit5"))
    implementation(group = "tools.aqua", name = "bgw-gui", version = "0.9")
}

tasks.distZip {
    archiveFileName.set("distribution.zip")
    destinationDirectory.set(layout.projectDirectory.dir("public"))
}

tasks.test {
    useJUnitPlatform()
    reports.html.outputLocation.set(layout.projectDirectory.dir("public/test"))
    finalizedBy(tasks.koverReport) // report is always generated after tests run
    ignoreFailures = true
}

tasks.clean {
    delete.add("public")
}

kover {
    filters {
        classes {
            excludes += listOf("gui.*", "entity.*", "*MainKt*", "service.bot.*")
        }
    }
    xmlReport {
        reportFile.set(file("public/coverage/report.xml"))
    }
    htmlReport {
        reportDir.set(layout.projectDirectory.dir("public/coverage"))
    }
}

detekt {
    // Version of Detekt that will be used. When unspecified the latest detekt
    // version found will be used. Override to stay on the same version.
    toolVersion = "1.18.0-RC3"

    //source.setFrom()
    config = files("detektConfig.yml")

    reports {
        // Enable/Disable HTML report (default: true)
        html {
            enabled = true
            reportsDir = file("public/detekt")
        }

        sarif {
            enabled = false
        }
    }
}

tasks.dokkaHtml.configure {
    outputDirectory.set(projectDir.resolve("public/dokka"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks.register("verify") {
    dependsOn(tasks.test)
    doLast {
        val testResultsDir = layout.buildDirectory.dir("test-results/test")
        val xmlFiles = testResultsDir.get().asFile.listFiles { file -> file.extension == "xml" }
        val parser = XmlParser()
        xmlFiles?.forEach { xmlFile ->
            println("Processing ${xmlFile.name}")
            val parsedXml = parser.parse(xmlFile)
            val suit = parsedXml.parseTestSuite()
            val failedCases = suit?.cases?.filter { it.failure != null }
            val failed = failedCases?.any { it.failure?.type != "org.opentest4j.AssertionFailedError" } ?: true
            println(failedCases)
            if (failed) {
                throw GradleException()
            }
        }
    }
}

fun Node.parseTestSuite(): TestSuite? {
    val name = this.name().toString()
    if (name == "testsuite") {
        val values = this.value() as? NodeList ?: return null
        val testCases = values.filterIsInstance<Node>().mapNotNull { it.parseTestCase() }
        return TestSuite(cases = testCases)
    }
    return null
}

fun Node.parseTestCase(): TestCase? {
    val name = this.name().toString()
    if (name == "testcase") {
        val values = this.value() as? NodeList ?: return null
        val failure = values.filterIsInstance<Node>().mapNotNull { it.parseFailure() }.firstOrNull()
        return TestCase(failure = failure)
    }
    return null
}

fun Node.parseFailure(): Failure? {
    val name = this.name().toString()
    if (name == "failure") {
        val type = this.attributes()["type"]?.toString() ?: return null
        return Failure(type = type)
    }
    return null
}

data class TestSuite(
    val cases: List<TestCase>,
)

data class TestCase(
    val failure: Failure?
)

data class Failure(
    val type: String,
)
