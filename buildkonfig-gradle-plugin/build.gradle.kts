plugins {
    @Suppress("DSL_SCOPE_VIOLATION") // See also, https://github.com/gradle/gradle/issues/22797#issuecomment-1517046458
    run {
        alias(libs.plugins.kotlin.jvm)
        alias(libs.plugins.mavenPublish)
        alias(libs.plugins.pluginPublish)
        id("java-gradle-plugin")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

val POM_URL: String by project
val POM_DESCRIPTION: String by project
val POM_NAME: String by project

gradlePlugin {
    website.set(POM_URL)
    vcsUrl.set("https://github.com/yshrsmz/BuildKonfig.git")
    plugins {
        create("buildKonfig") {
            id = "com.codingfeline.buildkonfig"
            implementationClass = "com.codingfeline.buildkonfig.gradle.BuildKonfigPlugin"
            displayName = POM_NAME
            description = POM_DESCRIPTION
            tags.set(listOf("BuildConfig", "Kotlin", "Kotlin Multiplatform"))
        }
    }
}

val fixtureClasspath by configurations.creating

// Append any extra dependencies to the test fixtures via a custom configuration classpath. This
// allows us to apply additional plugins in a fixture while still leveraging dependency resolution
// and de-duplication semantics.
tasks.pluginUnderTestMetadata {
    pluginClasspath.from(fixtureClasspath)
}

dependencies {
    implementation(projects.buildkonfigCompiler)
//    implementation(kotlin("stdlib-jdk8", Versions.kotlin))

    compileOnly(gradleApi())
    implementation(libs.kotlin.plugin)

    testImplementation(libs.junit)
    testImplementation(libs.truth)

    fixtureClasspath(libs.kotlin.plugin)
    fixtureClasspath(libs.android.plugin)
}

tasks.compileKotlin {
    kotlinOptions.jvmTarget = libs.versions.jvmTarget.get()
}
tasks.compileTestKotlin {
    kotlinOptions.jvmTarget = libs.versions.jvmTarget.get()
}

apply(from = "$rootDir/gradle/maven-publish.gradle")
