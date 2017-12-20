#!/bin/bash
# install xdotool if it's not already done
command -v xdotool >/dev/null || sudo apt-get install xdotool 

xdotool key Ctrl+F5
