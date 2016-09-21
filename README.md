# CompBase

Basic classes for working with parsed sentences, comparison information, NLP tools for basic processing and other useful classes.


## Basic packages

- `de.uni_stuttgart.ims.nlpbase.io`:
   Reading and writing files with dependency parse trees in CoNLL format.

- `de.uni_stuttgart.ims.nlpbase.nlp`:
   Basic stuff to represent a word, a sentence, part-of-speech tags, predicate-argument information.

- `de.uni_stuttgart.ims.nlpbase.tools`:
   Sentence splitting, tokenization, part-of-speech tagging. Actually only wrapper around the tools provided by OpenNLP and Stanford CoreNLP.

- `de.uni_stuttgart.ims.util`:
   Assorted useful stuff, e.g., for reading/writing files, handling HashMaps, mapping stuff to parse trees, etc.


## Stuff from other people

Needed for the classes in `de.uni_stuttgart.ims.nlpbase.tools`, i.e., sentence splitter, tokenizer, POS tagger. You can always opt to not use these classes or use only one of them.
The code assumes the model files are located in `models/` and are called exactly what is written below.

- [Stanford CoreNLP](http://stanfordnlp.github.io/CoreNLP/) aka `stanford-corenlp-3.2.0.jar`
   You will need the model file
   `english-left3words-distsim.tagger` for `POSTaggerStanford`.
- [OpenNLP Tools](https://opennlp.apache.org), aka `opennlp-tools-1.5.2-incubating.jar`.
   You will need the model files
   `en-sent.bin` for `SentenceSplitterOpenNLP` and
   `en-token.bin` for `TokenizerOpenNLP`
   

## Usage

These are only helper classes, there is no main method. But you will need this for other projects.
You will probably not need all the classes, so you can only just compile those that you need at that time.

Compile all classes (this assumes you have the two needed jar files in the folder `lib` and want to have the class files in `bin`):

    mkdir bin
    javac -cp bin -d bin src/de/uni_stuttgart/ims/nlpbase/nlp/*.java
    javac -cp bin -d bin src/de/uni_stuttgart/ims/nlpbase/io/*.java
    javac -cp bin:lib/stanford-corenlp-3.2.0.jar:lib/opennlp-tools-1.5.2-incubating.jar -d bin src/de/uni_stuttgart/ims/nlpbase/tools/*.java
    javac -cp bin -d bin src/de/uni_stuttgart/ims/util/*.java


## Licence

(c) Wiltrud Kessler

This code is distributed under a Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported license
[http://creativecommons.org/licenses/by-nc-sa/3.0/](http://creativecommons.org/licenses/by-nc-sa/3.0/)
