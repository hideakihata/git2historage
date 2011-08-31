#!/usr/bin/perl

use strict;
use warnings;

use File::Basename qw(dirname basename);

use FindBin qw($Bin);
use lib "$Bin/lib";
use FineGrained qw(:add_del_normal);

my $git_work = $ARGV[0];
my $ext_cls_path = $ARGV[1];
my $out_file = $ARGV[2];
my $now =$ARGV[3];
my $pre= $ARGV[4];


my $git_raw_option = '--pretty="@@%n" --raw --no-renames';
my $exist_files = 0;
my %change_info = ();
my %touched = ();
my $git_raw;
my $out4git;

if ($pre) {
  open $git_raw, "-|", "git diff $git_raw_option $pre $now"
    or die "Couldn't open git diff: $!";
}
else {
  open $git_raw, "-|", "git diff-index $git_raw_option -R $now"
    or die "Couldn't open git show: $!";
}

while (my $line = <$git_raw>) {
  next if ($line =~ /^\n/);
  next if ($line =~ /^@@/);
  next if ($line !~ /\.java$/);
  $exist_files++;

  my $sha1;
  my $com_status;
  my $path;
  if ($line =~ /:\d+? \d+? \w+\.* (\w+)\.* (\w)\t(.*?)\n$/) {
    $sha1 = $1;
    $com_status = $2; # A, M, D
    $path = $3;
  }
  elsif ($line =~ /(:+)([^\t]+)\t(.*?)\n$/) {
    my $colons = $1;
    my $vals = $2;
    $path = $3;

    my $count = $colons =~ s/://g;
    my @data = split(/ /, $vals);
    $sha1 = $data[2*$count+1];
    $sha1 =~ s/\.+//;
    $com_status = 'M';
  }
  else {
    print "\n(?show)$line";
    exit;
  }

  print $com_status;

  my $com_dir = dirname($path);
  my $com_file = basename($path, '.java');

  if ($com_status ne 'A') { # D or M
    $change_info{del}{$com_dir.$com_file}{status} = $com_status;
    $change_info{del}{$com_dir.$com_file}{dir} = $git_work.'/'.$com_dir.'/'.$com_file;
  }

  if ($com_status ne 'D') { # A or M
    $change_info{add}{$com_dir.$com_file}{status} = $com_status;
    $change_info{add}{$com_dir.$com_file}{base} = $git_work.'/'.$com_dir;
    $change_info{add}{$com_dir.$com_file}{dir} = $git_work.'/'.$com_dir.'/'.$com_file;
    $change_info{add}{$com_dir.$com_file}{sha1} = $sha1;
  }

  $touched{$git_work.'/'.$com_dir} = 1;
}
close $git_raw
  or warn $! ? "Syserr closing pipe: $!" : "Wait status ". $? . " from git command";

if ($exist_files) {
  my $ref_tree_state = {};

  if (exists $change_info{del}) {
    del_procedure($change_info{del}, $ref_tree_state);
  }
  if (exists $change_info{add}) {
    add_procedure($change_info{add}, $ext_cls_path, $ref_tree_state);
  }
  count_files_for_packages(\%touched, $ref_tree_state);

  open $out4git, ">", $out_file
    or die "Couldn't open $out_file: $!";
  for my $element (keys %$ref_tree_state) {
    print $out4git $element . "\n";
  }
  close $out4git
    or die "Couldn't close $out_file: $!";

  print ' ';
}

print "$exist_files\n";
exit $exist_files;
