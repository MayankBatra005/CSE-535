#!/bin/bash

docker run -it --rm -v $PWD/html:/var/www/html -p 8080:80 assignment2 bash
