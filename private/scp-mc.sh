#!/bin/bash

if test $# -eq 0; then
    WWDIR=~/workspace/Werewolf
    mkdir ${WWDIR}/tmp
    mkdir ${WWDIR}/tmp/Werewolf
    mkdir ${WWDIR}/tmp/BarAPI

    cd ${WWDIR}/tmp/Werewolf
    jar xf $WWDIR/target/Werewolf-0.0.1.jar
    cd ${WWDIR}/tmp/BarAPI
    jar xf $WWDIR/BarAPI.jar
    cd ${WWDIR}/tmp/
    mv BarAPI/me/ Werewolf/
    jar cf Werewolf.jar -C Werewolf .

    scp -P ??? Werewolf.jar ???@???:~/minecraft_server/plugins

    cd ${WWDIR}
    rm -r tmp
else
    WWALIDIR=~/workspace/WerewolfAliases
    cd ${WWALIDIR}/target
    cp WerewolfAliases-0.0.1.jar WerewolfAliases.jar
    scp -P ??? WerewolfAliases.jar ???@???:~/minecraft_server/plugins
    rm WerewolfAliases.jar
fi
