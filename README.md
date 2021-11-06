# Text search

This program reads the files in a directory, and creates an in-memory index of
words in them. It expects a single argument, the directory to read. In order to
run it, you need to have SBT installed.

```bash
 $ sbt
 > runMain test.SimpleSearch <directory>
```

After indexing the files, you get a prompt to enter words for searching. It
prints the files the word can be found in, and a score for each result. To exit
the prompt, use the `:quit` command, or press CTRL+D.


## Ranking

There are multiple ranking algorithms you can choose from. To select a ranking
algorithm, type `!` followed by the name of the algorithm, e.g. `!linear`. To
see the possible ranking algorithms, see
[Rank.scala](src/main/scala/test/Rank.scala).
