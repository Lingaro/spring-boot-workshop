#!/bin/bash
out=SRC.md
echo -n > $out
for f in `find src -type f ` pom.xml requests.http spring.service Dockerfile; do
    echo $f
    ext="${f##*.}" 
    echo "##### `echo $f | sed 's/src.main.java.//'`"  >> $out
    echo '```'$ext >> $out
    cat $f |sed -e '$a\\' | grep -v '^import'| grep -v '^package' | grep . | sed 's/    /  /g' >> $out
    echo '```' >> $out
done

