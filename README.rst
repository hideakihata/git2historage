toHistorage
===========

What's Historage?
-----------------
Fine-grained version control system (only for Java now) on Git.

You can trace the history of fine-grained entities in Java, such as fields, constrictors, methods, and classes.

What's toHistorage?
-------------------
This tool converts an ordinary Git repository to a fine-grained version control repository, Historage.

How to use
----------
1. Set a "conf" file

::

  TARGET=/path/to/original/git/repository
  TMP_DIR=/path/to/tmp/directory
  HISTORAGE=/path/to/Historage

2. Run

::

  cd toHistorage
  ./Convert.sh

Contact
-------
Hideaki Hata: hdrky[at]gmail.com

Acknowledgments
---------------
This tool makes use of MASU_, which is a platform for measuring software metrics from source code. We can use it as a static program analysis tool.
The author would like to thank the MASU_ developers for providing the tool and giving me valuable advice.

.. _MASU: http://sourceforge.net/projects/masu/

License
-------
Eclipse Public License
