#!/bin/bash
cd src
javac Person.java Graph.java
java Graph ../input.tsv
mv drp-family-tree.dot ../
cd ../
dot -Tpng drp-family-tree.dot -o drp-family-tree.png

