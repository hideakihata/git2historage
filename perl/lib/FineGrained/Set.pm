package FineGrained::Set;

use 5.008;
use strict;

our (@ISA, @EXPORT);

require Exporter;

@ISA = qw (Exporter);

our @EXPORT = qw(set_java__get_line set_java__get_line_regular set_mth);

use Carp qw(carp croak);

use File::Path qw(mkpath);


sub set_java__get_line {
  my ($obj, $java) = @_;
  my $line_num = 1;
  my @lines = ();

  open my $git_cat, "-|", "git cat-file -p $obj | nkf -Lu"
    or die "Couldn't open git cat-file: $!";
  open my $file, ">>", $java
    or die "Couldn't open $java: $!";

  while (my $line = <$git_cat>) {
    print $file $line;
    $lines[$line_num] = $line;
    $line_num++;
  }

  close $git_cat
    or warn $! ? "Syserr closing pipe: $!" : "Wait status ". $? . " from git cat-file";
  close $file or die "Couldn't close $java: $!";

  return \@lines;
}

sub set_java__get_line_regular {
  my ($obj, $java) = @_;
  my $lines = '';

  open my $git_cat, "-|", "git cat-file -p $obj | nkf -Lu | mcpp -P -W 0"
    or die "Couldn't open git cat-file: $!";
  while (my $line = <$git_cat>) {
    chomp $line;
    $line =~ s/;$/;>@@@</g;
    $line =~ s/{$/{>@@@</g;
    $line =~ s/}$/}>@@@</g;

    $lines .= $line;
  }
  close $git_cat
    or warn $! ? "Syserr closing pipe: $!" : "Wait status ". $? . " from git cat-file";

  $lines =~ s/\s+/ /g;
  my @rlines = split(/>@@@</, $lines);

  my $line_num = 0;
  my @lines = ();

  open my $file, ">>", $java
    or die "Couldn't open $java: $!";
  for my $rline (@rlines) {
    $line_num++;

    $rline .= "\n";
    print $file $rline;
    $lines[$line_num] = $rline;
  }
  close $file or die "Couldn't close $java: $!";

  return \@lines;
}

sub set_mth {
  my ($javas, $ref_lines, $ext_cls_path, $ref_tree_state) = @_;
  my %comments = ();
  my %classes = ();
  my $made_classes = 0;

  open my $extract, "-|", "java -classpath $ext_cls_path jp.ac.osaka_u.ist.sel.metricstool.main.Extractor $javas"
    or die "Couldn't run Extractor.java: $!";
  while (my $line = <$extract>) {
    if ($line =~ /^\[MASU\]/) {
      print $line;
      next;
    }

    chomp $line;
    my @line_info = split(/@@/, $line);
    my $type = $line_info[0];
    my $beg_line = $line_info[1];
    my $end_line = $line_info[2];
    next if ($beg_line == 0);

    my $target_file = $line_info[3];
    $target_file =~ s/\.java$//;

    if ($type eq 'CL' or $type eq 'IN') {
      my $class = $line_info[4];
      my $modifier = $line_info[5];

      $classes{$target_file}{$class}{type} = $type;
      $classes{$target_file}{$class}{beg} = $beg_line;
      $classes{$target_file}{$class}{end} = $end_line;
      $classes{$target_file}{$class}{access} = $modifier;

      if ($line_info[6]) {
	my @inners = split(/@/, $line_info[6]);
	for my $inner (@inners) {
	  $classes{$target_file}{$inner}{outer} = $class;
	}
      }
      next;
    }
    elsif ($type eq 'CM') {
      $comments{$target_file}{$end_line} = $beg_line;
      next;
    }

    unless ($made_classes) {
      set_class(\%classes, $ref_lines, $ref_tree_state);
      $made_classes += 1;
    }

    if ($type eq 'FE') {
      my $class = $line_info[4];
      my $modifier = $line_info[5];
      my $fie_name = $line_info[6];
      #my $fie_type = $line_info[7];

      my $path = $classes{$target_file}{$class}{location};
      my $write_file .= $path . '/FE';
      mkdir $write_file or die "Couldn't mkdir $write_file: $!" unless (-d $write_file);

      #$write_file = set_access($write_file, $modifier);
      mkdir $write_file or die "Couldn't mkdir $write_file: $!" unless (-d $write_file);

      #$write_file .= '/'.$fie_type.'@'.$fie_name;
      $write_file .= '/'.$fie_name;
      out_element($beg_line, $end_line, $ref_lines->{$target_file}, $write_file, $ref_tree_state);
    }
    elsif ($type eq 'CN') {
      my $class = $line_info[4];
      my $modifier = $line_info[5];
      my $cst_sig = $line_info[6];

      my $path = $classes{$target_file}{$class}{location};
      my $write_file .= $path . '/CN';
      mkdir $write_file or die "Couldn't mkdir $write_file: $!" unless (-d $write_file);

      #$write_file = set_access($write_file, $modifier);
      mkdir $write_file or die "Couldn't mkdir $write_file: $!" unless (-d $write_file);

      $cst_sig =~ s/\w+\.//g;
      $cst_sig =~ s/ /@/g;
      $cst_sig = name_hash($cst_sig) if (length $cst_sig > 255);

      $write_file .= '/'.$cst_sig;
      out_element($beg_line, $end_line, $ref_lines->{$target_file}, $write_file, $ref_tree_state);
    }
    elsif ($type eq 'MT') { # 4,[5]:cls_num, 6:access_modifier, 7:mth_name()ret_type
      my $class = $line_info[4];
      my $modifier = $line_info[5];
      my $mth_sig = $line_info[6];
      #my $rtn = $line_info[7];

      my $path = $classes{$target_file}{$class}{location};
      my $write_file = $path . '/MT';
      my $write_file_cm = $path . '/CM';
      mkdir $write_file or die "Couldn't mkdir $write_file: $!" unless (-d $write_file);
      mkdir $write_file_cm or die "Couldn't mkdir $write_file_cm: $!" unless (-d $write_file_cm);

      #$write_file = set_access($write_file, $modifier);
      mkdir $write_file or die "Couldn't mkdir $write_file: $!" unless (-d $write_file);
      #$write_file_cm = set_access($write_file_cm, $modifier);
      mkdir $write_file_cm or die "Couldn't mkdir $write_file_cm: $!" unless (-d $write_file_cm);

      $mth_sig =~ s/\w+\.//g;
      $mth_sig =~ s/ /@/g;
      $mth_sig = name_hash($mth_sig) if (length $mth_sig > 255);

      $write_file .= '/'.$mth_sig;
      $write_file_cm .= '/'.$mth_sig;
      out_element($beg_line, $end_line, $ref_lines->{$target_file}, $write_file, $ref_tree_state);
      out_comment($comments{$target_file}, $ref_lines->{$target_file},
      		  $beg_line, $end_line, $write_file_cm, $ref_tree_state);
    }
    else {
      print "\n!!!ELSE:\n";
      print "$line\n";
      exit;
    }
  }
  close $extract
    or warn $! ? "Syserr closing pipe: $!" : "Wait status ". $? . " from Extractor.java";

  return 0;
}

sub set_class {
  my ($ref_classes, $refl, $ref_tree_state) = @_;

  for my $file (keys %$ref_classes) {
    for my $class (keys %{ $ref_classes->{$file} }) {
      my $ctype = $ref_classes->{$file}{$class}{type};
      my $cmodi = $ref_classes->{$file}{$class}{access};
      my $beg = $ref_classes->{$file}{$class}{beg};
      my $end = $ref_classes->{$file}{$class}{end};

      my $location = '';
      #$ctype = set_access($ctype, $cmodi);

      if (exists $ref_classes->{$file}{$class}{outer}) {
	my $outer = $ref_classes->{$file}{$class}{outer} ;
	my $otype = $ref_classes->{$file}{$outer}{type};
	my $omodi = $ref_classes->{$file}{$outer}{access};

	#$otype = set_access($otype, $omodi);
	$location = $otype . '/' . $outer . '.c/' . $ctype . '/' . $class;
	$location = search_outer($location, $ref_classes->{$file}, $outer);
      }
      else {
	$location = $ctype . '/' . $class;
      }

      my $content = $file . '.f/' . $location;
      my $path = $content . '.c';

      if (not -d $path) {
	eval {
	  mkpath($path);
	};
	if ($@) {
	  die "Couldn't mkpath $path: $!";
	}
      }

      out_element($beg, $end, $refl->{$file}, $content, $ref_tree_state);

      $ref_classes->{$file}{$class}{location} = $path;
    }
  }

  return 0;
}

sub search_outer {
  my ($loca, $reff, $clas) = @_;

  if (exists $reff->{$clas}{outer}) {
    my $outer = $reff->{$clas}{outer} . '.c';
    my $otype = $reff->{$clas}{type};
    my $omodi = $reff->{$clas}{access};

    #$otype = set_access($otype, $omodi);
    $loca = $otype . '/' . $outer . '/' . $loca;
    $loca = search_outer($loca, $reff, $outer);
  }

  return $loca;
}

sub set_access {
  my $path = shift;
  if ($_[0]) {
    my $modif = shift;

    if ($modif eq 'public') {
      $path .= '/1';
    }
    elsif ($modif eq 'protected') {
      $path .= '/2';
    }
    elsif ($modif eq 'private') {
      $path .= '/4';
    }
    else {
      die "unknown modifier: $modif"
    }
  }
  else {
    $path .= '/3';
  }

  return $path;
}

sub name_hash {
  my $name = shift;

  open my $git_hsho, "-|", "echo -n '$name' | git hash-object --stdin"
    or die "Couldn't open git hash-object: $!";
  while (my $line = <$git_hsho>) {
    chomp $line;
    $name = $line;
  }
  close $git_hsho
    or warn $! ? "Syserr closing pipe: $!" : "Wait status ". $? . " from git hash-object";

  print "\n($name)";
  return $name;
}

sub out_element {
  my ($beg, $end, $ref, $write, $ref_tree_state) = @_;
  my $lines = '';

  for (my $line_num = $beg; $line_num <= $end; $line_num++) {
    $lines .= $ref->[$line_num];
  }

  open my $file, ">", $write
    or die "Couldn't open $write: $!";
  print $file $lines;
  close $file
    or die "Couldn't close $write: $!";

  $ref_tree_state->{$write} = 1;

  return 0;
}

sub out_comment {
  my ($cm, $ref, $mb, $me, $write, $ref_tree_state) = @_;
  my $bef = '';
  my $aft = '';

  for my $cend (sort {$a <=> $b} keys %$cm) {
    if ($cend == $mb - 1) { # just before the method
      $bef = $cm->{$cend};
      $aft = $cend;
    }
  }
  if ($bef) {
    my $lines = '';
    for (my $line_num = $bef; $line_num <= $aft; $line_num++) {
      $lines .= $ref->[$line_num]; 
    }

    open my $file2, ">", $write
      or die "Couldn't open $write: $!";
    print $file2 $lines;
    close $file2
      or die "Couldn't close $write: $!";

    $ref_tree_state->{$write} = 1;
  }

  return 0;
}


1;

