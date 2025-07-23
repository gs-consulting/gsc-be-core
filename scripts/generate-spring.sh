#!/bin/sh

set -ex

# check the redocly cli with the following command
# redocly --version
# if not installed, install it via npm
if [ ! command -v redocly >/dev/null 2>&1 ]; then
    npm install -g @redocly/cli
else
    echo "Redocly CLI is installed."
fi

redocly bundle specs/openapi.yaml --output specs/build/index.yaml

cd specs

./gradlew openApiValidate

./gradlew openApiGenerate -PgeneratorName=spring
exit 0
