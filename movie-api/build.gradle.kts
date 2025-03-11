import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(project(":movie-application"))

    implementation("org.springframework.boot:spring-boot-starter-web")
}

tasks.withType<BootJar> {
    enabled = true
}