#!/usr/bin/python3

import os
import pprint
import subprocess
from os.path import join, dirname
import dotenv


def env():
    #print("dotenv should be shown here: ", dir(dotenv))

    APP_ROOT = os.path.join(os.path.dirname(__file__), '')
    dotenv_path = os.path.join(APP_ROOT, '.env')
    dotenv.load(dotenv_path)
    #os.environ.update(dotenv)
