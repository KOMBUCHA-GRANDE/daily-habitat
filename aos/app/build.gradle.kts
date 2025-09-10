import com.google.devtools.ksp.gradle.KspExtension
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    id("kotlin-parcelize")
}

val localProps = Properties()
localProps.load(rootProject.file("local.properties").inputStream())

val kakaoKey: String = localProps.getProperty("KAKAO_NATIVE_APP_KEY")
val naverClientId: String = localProps.getProperty("NAVER_CLIENT_ID")
val naverClientSecret: String = localProps.getProperty("NAVER_CLIENT_SECRET")
val naverClientName: String = localProps.getProperty("NAVER_CLIENT_NAME")

android {
    namespace = "com.kombucha.dailyhabitat"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.kombucha.dailyhabitat"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        manifestPlaceholders["KAKAO_NATIVE_APP_KEY"] = kakaoKey
        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", kakaoKey)
        buildConfigField("String", "NAVER_CLIENT_ID", naverClientId)
        buildConfigField("String", "NAVER_CLIENT_SECRET", naverClientSecret)
        buildConfigField("String", "NAVER_CLIENT_NAME", naverClientName)
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    extensions.configure<KspExtension> {
        arg("circuit.codegen.mode", "hilt")
    }
}

dependencies {
    implementation(project(":oauth"))

    implementation("com.kakao.sdk:v2-user:2.21.7")
    implementation("com.navercorp.nid:oauth:5.9.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //circuit
    implementation(libs.bundles.circuit)
    ksp(libs.circuit.codegen.ksp)

    //hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
}