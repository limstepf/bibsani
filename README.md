# Bib(TeX) Sani(tizer)

`bibsani` is a command line tool to specifically sanitize BibTeX files served by [ScienceDirect](http://www.sciencedirect.com/) (and ocassionally [ACM Digital Library](http://dl.acm.org/)), since they tend to crash the [JBibTeX](https://github.com/jbibtex/jbibtex) parser. 

For the curious, line 328 in `bibtex.jj`:

```jj
< #UNICODE_LETTER : ["\u0100"-"\u1fff", "\u20ac", "\u2122"] >
```

is causing the problem, since we're facing lots of unicode chars in the range of `"\u0000"-"\u0100"`. [ScienceDirect](http://www.sciencedirect.com/) puts those into the BibTeX entry keys, and the [JBibTeX](https://github.com/jbibtex/jbibtex) parser isn't happy about it.

`bibsani` thus sanitizes all BibTeX entry starting lines (containing the BibTex entry key), by

1. Removing special accents/diacritical marks, and
2. Removing any remaining illegal characters.

## Usage

```
$ java -jar bibsani-1.0.0-SNAPSHOT.jar <options>

-f, --file <file>
    The BibTeX file to process.

-d, --directory <file>
    The output directory where to put the sanitized BibTeX file. (OPTIONAL)

-u, --usage
    Print the usage of this program.
```

## Example

```bash
#!/bin/bash
app=./bibsani.git/target/bibsani-1.0.0-SNAPSHOT.jar
file=./database.bib
log=./database-bibsani.log

java -jar ${app} --file ${file} | tee ${log}
```

### Example Output

```
input file: .\science.bib
output file: .\science-bibsani.bib

line      356: @article{Ibañez201773,
 sanitized to: @article{Ibanez201773,
line      592: @article{Sáez20112047,
 sanitized to: @article{Saez20112047,
line     1290: @article{Prudêncio2012277,
 sanitized to: @article{Prudencio2012277,
line     1605: @article{Mart??20011287,
 sanitized to: @article{Mart20011287,
line     1976: @article{Rößling20091,
 sanitized to: @article{Rossling20091,
line     2002: @article{Rößling20071,
 sanitized to: @article{Rossling20071,
line     2188: @article{GonzálezTorres2013486,
 sanitized to: @article{GonzalezTorres2013486,
line     2271: @article{Gîrba200557,
 sanitized to: @article{Girba200557,
line     2692: @article{UquillasGómez2015376,
 sanitized to: @article{UquillasGomez2015376,
line     2816: @article{BarbeitoAndrés201277,
 sanitized to: @article{BarbeitoAndres201277,
line     3061: @article{MossadeghiBjörklund201691,
 sanitized to: @article{MossadeghiBjorklund201691,
line     3079: @article{FernándezLozano2016509,
 sanitized to: @article{FernandezLozano2016509,
line     3311: @article{Rößling20093,
 sanitized to: @article{Rossling20093,
line     3350: @article{Lagravère2015302,
 sanitized to: @article{Lagravere2015302,
line     3498: @article{BarbeitoAndrés201559,
 sanitized to: @article{BarbeitoAndres201559,
line     3755: @article{Mat?cha2006S616,
 sanitized to: @article{Matecha2006S616,
line     4284: @article{Rößling200769,
 sanitized to: @article{Rossling200769,
line     4354: @article{Ellegård20111920,
 sanitized to: @article{Ellegard20111920,
line     5055: @article{Vrána2015277,
 sanitized to: @article{Vrana2015277,
line     5167: @article{Jørgensen20108168,
 sanitized to: @article{Jrgensen20108168,
line     5395: @article{GonzálezTorres201655,
 sanitized to: @article{GonzalezTorres201655,
line     6500: @article{Carrière19971257,
 sanitized to: @article{Carriere19971257,
line     7054: @article{Go9aevaPopstojanova2001179,
 sanitized to: @article{GosevaPopstojanova2001179,
line     7100: @article{Dallüge200369,
 sanitized to: @article{Dalluge200369,

kthxbai.
```

Granted, the same probably could have been accomplished way easier just using some good old unix tool like `sed`. But what do I know, I'm only 'nix wizerd level one and a half. Maybe.

A second objection might be that one would better fork/fix/do the pull request dance with [JBibTeX](https://github.com/jbibtex/jbibtex)... but ehh (I'm fine with a strict but correct parser to write BibTeX files, but one used to read could be a little more forgiving).


## Related Projects

* [csvtobib](https://github.com/limstepf/csvtobib): Converts (IEEE) CSV to BibTeX
* [pdfdbscrap](https://github.com/limstepf/pdfdbscrap): PDF Database Scrap(er)
* [pdfhiscore](https://github.com/limstepf/pdfhiscore): PDF Hi(stogram) Score
