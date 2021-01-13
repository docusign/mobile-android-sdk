#!/bin/sh

./gradlew clean build
BUILD_TOOLS_DIR=$(ls -td $ANDROID_HOME/build-tools/* | head -1)
echo $BUILD_TOOLS_DIR
$BUILD_TOOLS_DIR/zipalign -p 4 app/build/outputs/apk/release/app-release-unsigned.apk app/build/outputs/apk/release/app-release_unaligned.apk
java -jar $BUILD_TOOLS_DIR/lib/apksigner.jar sign --ks docusign.keystore --ks-key-alias docusign app/build/outputs/apk/release/app-release_unaligned.apk
rm app-release.apk
cp app/build/outputs/apk/release/app-release_unaligned.apk app-release.apk
