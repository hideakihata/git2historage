package FineGrained;

use 5.008;
use strict;

our (@ISA, @EXPORT, %EXPORT_TAGS);

require Exporter;

@ISA = qw (Exporter);

our @EXPORT = qw(del_procedure count_files_for_packages);
our @EXPORT_OK = qw(add_procedure add_procedure_szz get_package_name);

%EXPORT_TAGS = (
		 add_del_normal  => [qw(del_procedure add_procedure     count_files_for_packages)],
		 add_del_szz     => [qw(del_procedure add_procedure_szz count_files_for_packages)],
		 add_del_display => [qw(del_procedure add_procedure     count_files_for_packages get_package_name)],
		);

use Carp qw(carp croak);

use File::Find;
use File::Path qw(mkpath);

use FineGrained::Set;


sub del_procedure {
  my ($ref_del, $ref_tree_state) = @_;

  local *check_files = sub {
    if (-f) {
      $ref_tree_state->{$File::Find::name} = 1;
      unlink $File::Find::name;
    }
  };

  for my $key (keys %$ref_del) {
    my $status = $ref_del->{$key}{status};
    my $dir = $ref_del->{$key}{dir};
    my $file = $dir . '.java';
    my $fdir = $dir . '.f';

    print 'd';

    if ( -f $file) {
      unlink $file or die "Couldn't delete $file: $!";
    }
    $ref_tree_state->{$file} = 1;

    if ( -d $fdir) {
      find(\&check_files, $fdir);
    }
  }

  return 0;
}

sub add_procedure {
  my ($ref_add, $path, $ref_tree_state, $choice) = @_;
  my %lines = ();

  local *rm_files = sub {
    if (-f) {
      $ref_tree_state->{$File::Find::name} = 1;
      unlink $File::Find::name;
    }
  };

  for my $key (keys %$ref_add) {
    my $status = $ref_add->{$key}{status};
    my $base = $ref_add->{$key}{base};
    my $dir = $ref_add->{$key}{dir};
    my $sha = $ref_add->{$key}{sha1};
    my $file = $dir . '.java';
    my $fdir = $dir . '.f';

    print 'a';

    if (not -d $base) {
      eval {
	mkpath($base);
      };
      if ($@) {
	die "Couldn't mkpath $base: $!";
      }
    }

    if ( -f $file) {
      unlink $file or die "Couldn't delete $file: $!";
    }
    $ref_tree_state->{$file} = 1;

    if ($choice eq 'szz') {
      $lines{$dir} = set_java__get_line_regular($sha, $file);
    }
    else {
      $lines{$dir} = set_java__get_line($sha, $file);
    }

    if (-d $fdir) {
      find(\&rm_files, $fdir);
    }

    set_mth("'$file'", \%lines, $path, $ref_tree_state);
    %lines = ();
  }

  return 0;
}

sub add_procedure_szz {
  add_procedure(@_, 'szz');

  return 0;
}

sub count_files_for_packages {
  my ($ref_touched, $ref_tree_state) = @_;

  for my $bdir (keys %$ref_touched) {
    opendir(DIR, $bdir) or die "Couldn't opendir $bdir: $!";

    my $num_files = 0;
    my $list_file = $bdir . '/list';
    open my $list, ">", $list_file
      or die "Couldn't open $list_file: $!";
    while ( defined( my $file = readdir(DIR) ) ) {
      next if $file =~ /^\.\.?$/;
      next unless $file =~/\.java$/;
      $num_files++;
      print $list "$file\n";
    }
    close $list or die "Couldn't close $list_file: $!";
    unlink $list_file unless ($num_files);

    $ref_tree_state->{$list_file} = 1;

    closedir(DIR);
  }

  return 0;
}

sub get_package_name {
  my $obj = shift;
  my $pck = 'default';

  open my $git_cat, "-|", "git cat-file -p $obj | nkf -Lu | mcpp -P -W 0"
    or die "Couldn't open git cat-file: $!";
  while (my $line = <$git_cat>) {
    chomp $line;
    next unless ($line =~ /^\s*package\s+([\w\.]+);$/);
    $pck = $1;
    $pck =~ s|/|\.|g;
  }
  close $git_cat
    or warn $! ? "Syserr closing pipe: $!" : "Wait status ". $? . " from git cat-file";

  return $pck;
}


1;
