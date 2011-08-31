#!/bin/sh

TDIR=$1
GTMP=$2
EXTR=$3
PERL=$4
JPTH=$5

COMT=$(head -1 "$TDIR/coms")

if [ -f "$TDIR/pre_com" ]; then
    PCOMT=$(cat "$TDIR/pre_com")
else
    PCOMT=
fi

if [ -f "$TDIR/tree" ]; then
    git read-tree "$(cat $TDIR/tree)"
    git update-index --refresh
else
    git ls-files -z --cached | xargs -0 git rm --cached -q
fi

if [ ! -d "$GTMP/.git-rewrite/t/resource" ]; then
    cp -R "$EXTR/resource" "$GTMP/.git-rewrite/t"
fi

printf ' '
$PERL/evolveTrees.pl "$GTMP/.git-rewrite/t" $JPTH "$TDIR/tree-state" $COMT $PCOMT
NUM=$?

if test $NUM -gt 0; then
    git update-index --add --replace --remove --stdin \
	< "$TDIR/tree-state"
    git write-tree > "$TDIR/tree"
fi

echo $COMT > "$TDIR/pre_com"
sed -e "1,1d" "$TDIR/coms" > "$TDIR/coms_tmp"
mv "$TDIR/coms_tmp" "$TDIR/coms"
rm -f "$TDIR/tree-state"

exit 0
