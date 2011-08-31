package jp.ac.osaka_u.ist.sdl.scorpio.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.CloneSetInfo;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.CodeCloneController;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.CodeCloneInfo;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.ElementInfo;

public class BellonReader {

	public static void read(final String filename, final String id) {

		try {

			final BufferedReader reader = new BufferedReader(new FileReader(
					filename));
			while (reader.ready()) {
				final String line = reader.readLine();
				final StringTokenizer tokenizer = new StringTokenizer(line,
						"\t");

				final String filename1 = tokenizer.nextToken();
				final int fromLine1 = Integer.parseInt(tokenizer.nextToken());
				final int toLine1 = Integer.parseInt(tokenizer.nextToken());
				final String filename2 = tokenizer.nextToken();
				final int fromLine2 = Integer.parseInt(tokenizer.nextToken());
				final int toLine2 = Integer.parseInt(tokenizer.nextToken());
				// final int typeNumber =
				// Integer.parseInt(tokenizer.nextToken());

				final ElementInfo element1 = new ElementInfo(filename1
						.hashCode(), 0, fromLine1, 0, toLine1, 0);
				final ElementInfo element2 = new ElementInfo(filename2
						.hashCode(), 0, fromLine2, 0, toLine2, 0);
				final CodeCloneInfo codeclone1 = new CodeCloneInfo();
				codeclone1.add(element1);
				final CodeCloneInfo codeclone2 = new CodeCloneInfo();
				codeclone1.add(element2);
				final CloneSetInfo cloneset = new CloneSetInfo();
				cloneset.add(codeclone1);
				cloneset.add(codeclone2);
				CodeCloneController.getInstance(id).add(cloneset);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
