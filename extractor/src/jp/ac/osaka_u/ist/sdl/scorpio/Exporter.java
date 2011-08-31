package jp.ac.osaka_u.ist.sdl.scorpio;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.CloneSetInfo;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.CodeCloneController;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.CodeCloneInfo;
import jp.ac.osaka_u.ist.sdl.scorpio.io.XMLReader;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class Exporter {

	public static final String ID = "EXPORTER";

	public static void main(String[] args) {

		try {

			// コマンドライン引数を処理
			final Options options = new Options();

			final Option i = new Option("i", "input", true, "input file");
			i.setArgName("input");
			i.setArgs(1);
			i.setRequired(true);
			options.addOption(i);

			final Option o = new Option("o", "output file", true, "output file");
			o.setArgName("output");
			o.setArgs(1);
			o.setRequired(true);
			options.addOption(o);

			final CommandLineParser parser = new PosixParser();
			final CommandLine cmd = parser.parse(options, args);

			XMLReader.read(cmd.getOptionValue("i"), Exporter.ID);

			exportCodeClone(cmd.getOptionValue("o"));
			exportCloneSet();

		} catch (ParseException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	private static void exportCodeClone(final String output) {

		final SortedSet<CodeCloneInfo> codeclones = new TreeSet<CodeCloneInfo>();
		for (final CloneSetInfo cloneset : CodeCloneController.getInstance(
				Exporter.ID).getCloneSets()) {
			codeclones.addAll(cloneset.getCodeclones());
		}

		try {

			final BufferedWriter writer = new BufferedWriter(new FileWriter(
					output));

			for (final CodeCloneInfo codeclone : codeclones) {
				final int size = codeclone.getLength();
				final int method = codeclone.getNumberOfMethods();
				writer.write(Integer.toString(size));
				writer.write(",");
				writer.write(Integer.toString(method));
				writer.newLine();
			}

			writer.close();

		} catch (IOException e) {

		}
	}

	private static void exportCloneSet() {

		int type1 = 0;
		int type2 = 0;
		int type3 = 0;

		CLONESET: for (final CloneSetInfo cloneset : CodeCloneController
				.getInstance(Exporter.ID).getCloneSets()) {

			final List<Integer> list = new LinkedList<Integer>();
			for (final CodeCloneInfo codeclone : cloneset.getCodeclones()) {
				final int method = codeclone.getNumberOfMethods();
				list.add(method);
			}

			int value = list.get(0).intValue();
			for (final Integer integer : list) {
				if (value != integer.intValue()) {
					type3++;
					continue CLONESET;
				}
			}

			if (value == 1) {
				type1++;
			} else {
				type2++;
			}
		}

		System.out.println("type1: " + type1);
		System.out.println("type2: " + type2);
		System.out.println("type3: " + type3);
	}
}
