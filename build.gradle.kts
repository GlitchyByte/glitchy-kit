// Copyright 2020 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

//import java.util.Locale
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

tasks.named<Wrapper>("wrapper") {
    // Automatically set distribution type all when updating gradlew.
    distributionType = Wrapper.DistributionType.ALL
}

plugins {
    `java-library`
    `maven-publish`
    id("com.google.cloud.artifactregistry.gradle-plugin") version "2.1.1"
//    id("com.glitchybyte.gradle.plugin.buildinfo") version "1.0.0"
}

repositories {
    maven(url = uri("artifactregistry://us-west1-maven.pkg.dev/glitchy-maven/repo"))
//    mavenLocal()
    mavenCentral()
}

//tasks {
//    saveBuildInfo {
//        destinations = setOf(
//            "src/main/resources/com/glitchybyte/lib"
//        )
//    }
//}

java {
    // Java version for the library.
    sourceCompatibility = JavaVersion.VERSION_15
    targetCompatibility = JavaVersion.VERSION_15
    // Create javadoc and sources publishing artifacts.
    withJavadocJar()
    withSourcesJar()
}

tasks.withType<Test> {
    // Use JUnit 5 (Jupiter) with 8 parallel test execution and full logging.
    useJUnitPlatform()
    maxParallelForks = 8
    testLogging.exceptionFormat = TestExceptionFormat.FULL
}

dependencies {
    // Main dependencies.
    implementation("com.google.code.gson:gson:2.8.6")
    // Test dependencies.
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")
}

// Setup build info.
group = "com.glitchybyte"
version = "1.0.0"

// Publish to Google Cloud Platform Artifact Registry.
publishing {
    repositories {
        maven(url = uri("artifactregistry://us-west1-maven.pkg.dev/glitchy-maven/repo"))
    }
}

//publishing {
//    publications {
//        create<MavenPublication>("library") {
//            groupId = project.group as String
//            artifactId = project.name.toLowerCase(Locale.US)
//            version = project.version as String
//            from(components["java"])
//            pom {
//                name.set("Example Library")
//                description.set("Reusable generic utilities to speed up development.")
////                url.set("https://github.com/wyvx/base-java-lib-gradle")
//                licenses {
//                    license {
//                        name.set("Apache License 2.0")
//                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
//                    }
//                }
//            }
//        }
//    }
//}

// Expose javadoc for GitHub.
tasks.register<Copy>("exposeJavadoc") {
    dependsOn("javadoc")
    from("$buildDir/docs/javadoc")
    into("docs")
}

tasks.named("javadoc") {
    finalizedBy("exposeJavadoc")
}
