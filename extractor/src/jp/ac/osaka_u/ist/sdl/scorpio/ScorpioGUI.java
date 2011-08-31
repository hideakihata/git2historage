package jp.ac.osaka_u.ist.sdl.scorpio;

import java.awt.Dimension;
import java.awt.Toolkit;

import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.CodeCloneController;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.inter.InterCloneViewPanel;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.intra.IntraCloneViewPanel;
import jp.ac.osaka_u.ist.sdl.scorpio.io.XMLReader;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class ScorpioGUI {

	public static void main(String[] args) {

		try {

			// 　コマンドライン引数を処理
			final Options options = new Options();

			final Option i = new Option("i", "input", true, "input file");
			i.setArgName("language");
			i.setArgs(1);
			i.setRequired(true);
			options.addOption(i);

			final CommandLineParser parser = new PosixParser();
			final CommandLine cmd = parser.parse(options, args);

			XMLReader.read(cmd.getOptionValue("i"), ScorpioGUI.ID);

			switch (CodeCloneController.getInstance(ScorpioGUI.ID)
					.getDetectionType()) {
			case intra: {
				final IntraCloneViewPanel mainWindow = new IntraCloneViewPanel();
				Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
				mainWindow.setSize(new Dimension(d.width - 5, d.height - 27));
				mainWindow.setVisible(true);
				break;
			}

			case inter: {
				final InterCloneViewPanel mainWindow = new InterCloneViewPanel();
				Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
				mainWindow.setSize(new Dimension(d.width - 5, d.height - 27));
				mainWindow.setVisible(true);
				break;
			}

			default: {
				System.out.println("invalid input file.");
			}
			}

		} catch (ParseException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static final String ID = "SCVISUALIZER";
}
