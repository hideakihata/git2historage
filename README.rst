git2historage (Legacy)
=============
This repository has been deprecated.

- Please use this software: `niyaton/kenja https://github.com/niyaton/kenja/`_.

- `Kataribe http://sdlab.naist.jp/kataribe/`_ is a hosting service of Historage repositories.

What's Historage?
-----------------
Historega is a fine-grained version control system based on Git.
With Historage you can trace the history of fine-grained entities in Java, such as fields, constructors, methods, and classes.

At present, Historage supports only source code written in Java.

What's git2historage?
---------------------
A tool to convert any ordinary Git repository to a Historage repository.

How to use git2istorage
-----------------------
1. Set a "conf" file

::

  TMP_DIR=/path/to/tmp/directory
  GIT_REPOSITORY=/path/to/original/git/repository
  HISTORAGE_REPOSITORY=/path/to/historage/repository

2. Run

::

  cd git2historage
  ./git2historage.sh

Note
----
- Prepare a case-sensitive file system for Historage.
- Locate $TMP_DIR on RAM disk for improving converting speed.

Contact
-------
Hideaki Hata: hdrky[at]gmail.com

Acknowledgments
---------------
This tool uses MASU_ for static program analysis.
The author would like to thank the MASU_'s developer team for providing the tool and giving him valuable advice.

.. _MASU: http://sourceforge.net/projects/masu/

License
-------
Eclipse Public License - v 1.0
http://www.eclipse.org/legal/epl-v10.html
