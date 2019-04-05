#!/bin/bash

sbt universal:packageZipTarball
rm -rf scala-times1
tar zxf target/universal/scala-times1.tgz
hokusai build
