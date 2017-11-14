#!/bin/bash
#v1

ps ax | grep androfleet | grep -v grep | awk '{print $1}' |xargs kill

arg1="$(ps ax | grep entrypoint | grep -v grep | awk '{print $7}')"
arg2="$(ps ax | grep entrypoint | grep -v grep | awk '{print $8}')"
arg3="$(ps ax | grep entrypoint | grep -v grep | awk '{print $9}')"
arg4="$(ps ax | grep entrypoint | grep -v grep | awk '{print $10}')"
arg5="$(ps ax | grep entrypoint | grep -v grep | awk '{print $11}')"
arg6="$(ps ax | grep entrypoint | grep -v grep | awk '{print $12}')"
arg7="$(ps ax | grep entrypoint | grep -v grep | awk '{print $13}')"
/build/parse.sh $arg1 $arg2 $arg3 $arg4 $arg5 $arg6 $arg7 &
