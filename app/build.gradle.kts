plugins {
    id(Plugin.application)
    id(Plugin.androidKotlin)
    id(Plugin.kotlinParcelize)
    id(Plugin.kotlinKapt)
    id(Plugin.hilt)
    id(Plugin.navArgs)
}

android {

    compileSdk = BuildType.Default.compileSdkVersion
    buildToolsVersion = BuildType.Default.buildToolsVersion

    defaultConfig {
        applicationId = Build.applicationId
        versionCode = BuildType.Default.versionCode
        versionName = BuildType.Default.versionName

        minSdk = BuildType.Default.minSdkVersion
        targetSdk = BuildType.Default.targetSdkVersion

        testInstrumentationRunner = Config.testInstrumentationRunner
        buildConfigField(Build.stringType, AppProperties.APPLICATION_KEY_NAME, AppProperties.APPLICATION_KEY)
        buildConfigField(Build.stringType, AppProperties.APPLICATION_KEY_VALUE_NAME, AppProperties.APPLICATION_KEY_VALUE)
        buildConfigField(Build.stringType, AppProperties.ACCEPT_KEY, AppProperties.ACCEPT_KEY_VALUE)
        buildConfigField(Build.stringType, AppProperties.ACCEPT_VALUE, AppProperties.ACCEPT_VALUE_VALUE)
        buildConfigField(Build.stringType, AppProperties.COOKIE, AppProperties.COOKIE_VALUE)
    }

    flavorDimensions(BuildType.flavorDimen)

    productFlavors {
        create(Flavors.develop) {
            dimension = BuildType.flavorDimen
            applicationIdSuffix = BuildType.Staging.suffixId
            versionNameSuffix = BuildType.Staging.suffixName


            targetSdk = BuildType.Staging.targetSdkVersion
            resValue(Build.stringResType, BuildType.Staging.appNameKey, BuildType.Develop.appName)
            resValue(Build.stringResType, BuildType.Staging.resBaseUrlName, BuildType.Develop.resBaseUrl)
            buildConfigField(Build.stringType, BuildType.Staging.baseUrlName, BuildType.Develop.baseUrl)

        }

        create(Flavors.staging) {
            dimension = BuildType.flavorDimen
            applicationIdSuffix = BuildType.Staging.suffixId
            versionNameSuffix = BuildType.Staging.suffixName

            targetSdk = BuildType.Staging.targetSdkVersion
            resValue(Build.stringResType, BuildType.Staging.appNameKey, BuildType.Staging.appName)
            resValue(Build.stringResType, BuildType.Staging.resBaseUrlName, BuildType.Staging.resBaseUrl)
            buildConfigField(Build.stringType, BuildType.Staging.baseUrlName, BuildType.Staging.baseUrl)
        }

        create(Flavors.production) {
            dimension = BuildType.flavorDimen

            resValue(Build.stringResType, BuildType.Production.appNameKey, BuildType.Production.appName)
            buildConfigField(Build.stringType, BuildType.Production.baseUrlName, BuildType.Production.baseUrl)
        }
    }

    buildFeatures {
        viewBinding =  true
    }

    buildTypes {
        getByName(BuildType.release) {
            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile(ProGuard.optimizeRule),
                ProGuard.optimizeFile
            )
        }

        getByName(BuildType.debug) {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_11)
        targetCompatibility(JavaVersion.VERSION_11)
    }

    kotlinOptions {
        jvmTarget = Build.jvmTarget
    }

    applicationVariants.all {
        outputs.all {
            val project = mergedFlavor.resValues[Build.resAppName]
            val buildType = buildType.name
            val version = versionName

            val outputName = "$project-$buildType-$version${Build.apkExtension}"

            file(File(outputName))
        }
    }

}

dependencies {

    implementation(Dependencies.playCoreKtx)
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.Kotlin.kotlin)

    Dependencies.Kotlin.Coroutines.coroutinesDependencies.forEach { dependency ->
        implementation(dependency)
    }

    Dependencies.UI.uiDependencies.forEach { dependency ->
        implementation(dependency)
    }

    Dependencies.Jetpack.jetpackComponentDependencies.forEach { dependency ->
        implementation(dependency)
    }

    Dependencies.Network.networkDependencies.forEach { dependency ->
        implementation(dependency)
    }

    Dependencies.Facebook.facebookDependencies.forEach { dependency ->
        implementation(dependency)
    }

    Dependencies.Injection.injectionDependencies.forEach { dependency ->
        implementation(dependency)
    }

    Dependencies.Media.mediaDependencies.forEach { dependency ->
        implementation(dependency)
    }
    implementation(Dependencies.Support.easyPermission)

    implementation(Dependencies.Logging.timber)
    implementation(Dependencies.Media.glide)
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    Dependencies.kaptImp.forEach { kaptImp ->
        kapt(kaptImp)
    }

    testImplementation(Dependencies.Testing.jUnit)
    androidTestImplementation(Dependencies.Testing.jUnitTest)
    androidTestImplementation(Dependencies.Testing.espresso)

    implementation(kotlin("reflect"))

}