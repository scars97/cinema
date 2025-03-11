dependencies {
    implementation(project(":movie-application"))

    implementation("org.springframework.boot:spring-boot-starter-web")
}

tasks.withType<Jar> {
    enabled = true
}