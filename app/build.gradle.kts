plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  kotlin("kapt")
  id("com.google.gms.google-services")
}

android {
  namespace = "com.mapcok"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.mapcok"
    minSdk = 24
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }

  buildFeatures {
    viewBinding = true
    dataBinding = true
  }


}

dependencies {

  //androidx
  implementation("androidx.core:core-ktx:1.13.1")
  implementation("androidx.appcompat:appcompat:1.6.1")
  implementation("com.google.android.material:material:1.12.0")
  implementation("androidx.constraintlayout:constraintlayout:2.1.4")
  implementation("androidx.fragment:fragment-ktx:1.3.6")
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")



  //test
  testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.5")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

  // navigation
  implementation("androidx.navigation:navigation-fragment-ktx:2.7.5")
  implementation("androidx.navigation:navigation-ui-ktx:2.7.5")


  // retrofit,
  implementation("com.squareup.retrofit2:retrofit:2.9.0")
  implementation("com.squareup.retrofit2:converter-gson:2.9.0")

  //okHttpClient
  implementation("com.squareup.okhttp3:okhttp:4.9.0")
  implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

  // gilde
  implementation("com.github.bumptech.glide:glide:4.12.0")
  annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

  // timber
  implementation("com.jakewharton.timber:timber:4.7.1")

  // tedPermission
  implementation("io.github.ParkSangGwon:tedpermission-normal:3.3.0")
  implementation("io.github.ParkSangGwon:tedpermission-coroutine:3.3.0")

  implementation ("io.github.ParkSangGwon:tedclustering-naver:1.0.2")

  implementation("com.naver.maps:map-sdk:3.18.0")
  implementation("com.google.android.gms:play-services-location:21.0.1")


  //google
  implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
  implementation("com.google.firebase:firebase-auth-ktx")
  implementation("com.google.android.gms:play-services-auth:20.7.0")

  implementation ("de.hdodenhof:circleimageview:3.1.0")
}