#!/bin/sh

if [ ! -f ./conf ]; then
    echo "[ERR] Cannot find ./conf"
    exit
fi
source ./conf

GIT_TMP=$TMP_DIR/Historage.tmp
ELSE_DIR=$TMP_DIR/Historage.else
GINDEX=$ELSE_DIR/index

HERE=$(pwd)
MASU_DIR=$HERE/extractor
PERL_DIR=$HERE/perl
MASU_PATH=$MASU_DIR/bin/:$MASU_DIR/lib/antlr.jar:$MASU_DIR/lib/asm-all-3.3.jar:$MASU_DIR/lib/commons-cli-1.1.jar


rm -rf $GIT_TMP $ELSE_DIR $HISTORAGE
mkdir $ELSE_DIR

echo '[1] cloning to tmporal repository...'
git clone file://$TARGET $GIT_TMP

echo '[2] reconstructing...'
cd $GIT_TMP
git rev-list \
    --reverse --topo-order --default HEAD \
    --simplify-merges $rev_args \
    > $ELSE_DIR/coms

git filter-branch \
    --env-filter "GIT_INDEX_FILE=$GINDEX; export GIT_INDEX_FILE" \
    --index-filter "$HERE"'/filter/filter_index.sh'" $ELSE_DIR $GIT_TMP $MASU_DIR $PERL_DIR $MASU_PATH" \
    --prune-empty \
    --tag-name-filter cat
cd -

echo '[3] finishing...'
git clone file://$GIT_TMP $HISTORAGE
rm -rf $GIT_TMP $TMP_DIR

exit 0
