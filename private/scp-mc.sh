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

    scp -P 3843 Werewolf.jar akira@183.181.16.20:~/minecraft_server/plugins

    cd ${WWDIR}
    rm -r tmp
else
#    WWDIR=~/workspace/Werewolf
    WWALIDIR=~/workspace/WerewolfAliases
#    mkdir ${WWALIDIR}/tmp
#    mkdir ${WWALIDIR}/tmp/WerewolfAliases
#    mkdir ${WWALIDIR}/tmp/ProtocolLib

#    cd ${WWALIDIR}/tmp/WerewolfAliases
#    jar xf $WWALIDIR/target/WerewolfAliases-0.0.1.jar
#    cd ${WWALIDIR}/tmp/ProtocolLib
#    jar xf $WWDIR/ProtocolLib-3.4.0.jar
#    cd ${WWALIDIR}/tmp/
#    mv ProtocolLib/com/ WerewolfAliases/
#    jar cf WerewolfAliases.jar -C WerewolfAliases .

#    scp -P 3843 WerewolfAliases.jar akira@183.181.16.20:~/minecraft_server/plugins
    
#    cd ${WWALIDIR}
#    rm -r tmp

    cd ${WWALIDIR}/target
    cp WerewolfAliases-0.0.1.jar WerewolfAliases.jar
    scp -P 3843 WerewolfAliases.jar akira@183.181.16.20:~/minecraft_server/plugins
    rm WerewolfAliases.jar
fi