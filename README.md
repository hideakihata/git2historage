git2historage (Legacy)
=============
This repository has been deprecated.

- Please use [sh5i/git-stein](https://github.com/sh5i/git-stein>).

What's Historage?
-----------------
Historega is a fine-grained version control system based on Git.
With Historage you can trace the history of fine-grained entities in Java, such as fields, constructors, methods, and classes.

```Tex
@inproceedings{historage-hata-2011,
 author = {Hata, Hideaki and Mizuno, Osamu and Kikuno, Tohru},
 title = {Historage: Fine-Grained Version Control System for Java},
 booktitle = {Proceedings of the 12th International Workshop on Principles of Software Evolution and the 7th Annual ERCIM Workshop on Software Evolution},
 series = {IWPSE-EVOL ’11}
 pages = {96–100},
 doi = {10.1145/2024445.2024463},
 year = {2011},
}
```

What's git2historage?
---------------------
A tool to convert any ordinary Git repository to a Historage repository.


How to use git2istorage
-----------------------
1. Set a "conf" file

```
TMP_DIR=/path/to/tmp/directory
GIT_REPOSITORY=/path/to/original/git/repository
HISTORAGE_REPOSITORY=/path/to/historage/repository
```

2. Run

```
cd git2historage
./git2historage.sh
```

Note
----
- Prepare a case-sensitive file system for Historage.
- Locate $TMP_DIR on RAM disk for improving converting speed.

Contact
-------
Hideaki Hata: hata[at]is.naist.jp


Acknowledgments
---------------
This tool uses [MASU](http://sourceforge.net/projects/masu/) for static program analysis.
The author would like to thank the MASU developer team for providing the tool and giving him valuable advice.


License
-------
Eclipse Public License - v 1.0
http://www.eclipse.org/legal/epl-v10.html
