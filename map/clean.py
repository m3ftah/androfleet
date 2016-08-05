#!/usr/bin/python3

import shutil
import os

if os.path.exists('node_modules'):
    shutil.rmtree('node_modules')
