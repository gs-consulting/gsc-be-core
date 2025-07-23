#!/bin/bash

script_dir=`(cd $(dirname $0); pwd)`

set -eux
cd ${script_dir}/../specs/
redocly bundle openapi.yaml --output build/index.yaml
