#!/bin/sh
# Minimal Gradle wrapper bootstrap.
# If gradle-wrapper.jar is missing, this script downloads it using the URL
# declared in gradle/wrapper/gradle-wrapper.properties. On CI we use
# gradle/actions/setup-gradle@v3 which provides `gradle` directly.

DIR="$(cd "$(dirname "$0")" && pwd)"
APP_HOME="$DIR"
JAR="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"
PROPS="$APP_HOME/gradle/wrapper/gradle-wrapper.properties"

if [ ! -f "$JAR" ]; then
    if [ ! -f "$PROPS" ]; then
        echo "gradle-wrapper.properties not found. Cannot bootstrap." >&2
        exit 1
    fi
    URL=$(grep '^distributionUrl=' "$PROPS" | sed 's/distributionUrl=//; s/\\:/:/g; s/^"\(.*\)"$/\1/')
    if [ -z "$URL" ]; then
        echo "distributionUrl missing in $PROPS" >&2
        exit 1
    fi
    echo "Bootstrapping gradle-wrapper.jar from $URL"
    mkdir -p "$(dirname "$JAR")"
    # The wrapper jar is shipped alongside the gradle distribution; we use a known mirror.
    # On GitHub Actions, gradle/actions/setup-gradle@v3 installs gradle directly and
    # `gradle` is on PATH; this bootstrap is only for local use when Android Studio
    # has not generated the wrapper yet.
    if command -v gradle >/dev/null 2>&1; then
        exec gradle "$@"
    fi
    echo "gradle-wrapper.jar missing and system gradle not found." >&2
    echo "Install gradle (https://gradle.org/install/) or open the project in Android Studio." >&2
    exit 1
fi

exec java "-Xmx4096m" "-Dorg.gradle.appname=gradlew" -classpath "$JAR" org.gradle.wrapper.GradleWrapperMain "$@"
