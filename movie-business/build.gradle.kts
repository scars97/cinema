dependencies {
    implementation(project(":movie-infrastructure"))

    implementation("org.springframework.boot:spring-boot-starter-web")
}

tasks.withType<Jar> {
    enabled = true
}