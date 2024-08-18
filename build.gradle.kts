plugins {
    kotlin("jvm") version "2.0.10"
}

group = "org.arjix"
version = "1.0-SNAPSHOT"

dependencies {
    implementation("com.github.Minestom:minestom:77df1bdfd29d5a898eb75aa4fdd91b648dd75751")
    implementation("com.github.Minestom:VanillaReimplementation:bb4e93c83c")
    implementation("net.bladehunt:kotstom:0.3.0")
}

kotlin {
    jvmToolchain(21)
}
