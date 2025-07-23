#!/bin/bash

set -eux
env=$1
script_dir=`(cd $(dirname $0); pwd)`
image_tag="asia-northeast1-docker.pkg.dev/${PROJECT_ID}/gs-consulting/gsc-be-core:${GITHUB_SHA}"

cd ${script_dir}/../overlays/${env}
kustomize edit set image gsc-be-core=$image_tag
kustomize build  | kubectl apply -f -
