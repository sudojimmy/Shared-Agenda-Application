#!/bin/sh

set -e

# remove old version
rm -rf Frontend/ShareAgenda/app/src/main/java/types

# build new version
cd Backend/schema/ && ../gradlew clean build && cd ../..

# copy new version to frontend
cp Backend/schema/build/generated-sources/js2p/types/ Frontend/ShareAgenda/app/src/main/java -r 
