dependencies {
    implementation(project(":movie-business"))

    implementation("org.springframework.boot:spring-boot-starter-web")
}

tasks.withType<Jar> {
    enabled = true
}