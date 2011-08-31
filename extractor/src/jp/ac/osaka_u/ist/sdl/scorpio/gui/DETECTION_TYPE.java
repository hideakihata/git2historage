package jp.ac.osaka_u.ist.sdl.scorpio.gui;

public enum DETECTION_TYPE {

	intra {

	},

	inter {

	};

	public static DETECTION_TYPE create(final String type) {
		if (type.equalsIgnoreCase("intra")) {
			return DETECTION_TYPE.intra;
		} else if (type.equalsIgnoreCase("inter")) {
			return DETECTION_TYPE.inter;
		} else {
			return null;
		}
	}
}
