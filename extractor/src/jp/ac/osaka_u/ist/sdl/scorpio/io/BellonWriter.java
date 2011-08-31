package jp.ac.osaka_u.ist.sdl.scorpio.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sdl.scorpio.data.CloneSetInfo;
import jp.ac.osaka_u.ist.sdl.scorpio.data.CodeCloneInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

public class BellonWriter {

	public BellonWriter(final String outputFile,
			final SortedSet<FileInfo> files, final Set<CloneSetInfo> cloneSets) {

		this.files = files;
		this.cloneSets = cloneSets;

		try {
			this.writer = new BufferedWriter(new FileWriter(outputFile));
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public void write() {

		try {

			final SortedSet<String> lines = new TreeSet<String>();

			// このforループは出力がペア形式であることが前提
			for (final CloneSetInfo cloneset : this.cloneSets) {

				assert cloneset.getCodeClones().size() == 2 : "format must be \"papr\"!";

				final CodeCloneInfo codecloneA = cloneset.getCodeClones()
						.first();
				final CodeCloneInfo codecloneB = cloneset.getCodeClones()
						.last();

				final SortedSet<Integer> linesA = new TreeSet<Integer>();
				for (final PDGNode<?> node : codecloneA.getRealElements()) {
					final ExecutableElementInfo element = node.getCore();
					for (int i = element.getFromLine(); i <= element
							.getToLine(); i++) {
						linesA.add(i);
					}
				}

				final SortedSet<Integer> linesB = new TreeSet<Integer>();
				for (final PDGNode<?> node : codecloneB.getRealElements()) {
					final ExecutableElementInfo element = node.getCore();
					for (int i = element.getFromLine(); i <= element
							.getToLine(); i++) {
						linesB.add(i);
					}
				}

				final ExecutableElementInfo elementA = codecloneA
						.getRealElements().first().getCore();
				final String filenameA = ((TargetClassInfo) elementA
						.getOwnerMethod().getOwnerClass()).getOwnerFile()
						.getName();
				final ExecutableElementInfo elementB = codecloneB
						.getRealElements().first().getCore();
				final String filenameB = ((TargetClassInfo) elementB
						.getOwnerMethod().getOwnerClass()).getOwnerFile()
						.getName();

				final StringBuilder line = new StringBuilder();
				line.append(filenameA);
				line.append("\t");
				line.append(linesA.first().toString());
				line.append("\t");
				line.append(linesA.last().toString());
				line.append("\t");
				line.append(filenameB);
				line.append("\t");
				line.append(linesB.first().toString());
				line.append("\t");
				line.append(linesB.last().toString());
				lines.add(line.toString());
			}

			for (final String line : lines) {
				this.writer.write(line);
				this.writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			this.writer.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	BufferedWriter writer;

	final Set<CloneSetInfo> cloneSets;

	final SortedSet<FileInfo> files;
}
