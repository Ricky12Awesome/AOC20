import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.4.20"
  id("com.github.johnrengelman.shadow") version "6.1.0"
  application
}

group = "com.github.ricky12awesome"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  implementation(kotlin("reflect"))
  shadow(kotlin("reflect"))
  implementation("com.xenomachina:kotlin-argparser:2.0.7")
  shadow("com.xenomachina:kotlin-argparser:2.0.7")
  testImplementation(kotlin("test-junit"))
}

tasks {
  test {
    useJUnit()
  }

  withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
    kotlinOptions.freeCompilerArgs += listOf(
      "-Xopt-in=kotlin.RequiresOptIn",
      "-Xopt-in=kotlin.io.path.ExperimentalPathApi"
    )
  }
}

application {
  mainClassName = "MainKt"
}