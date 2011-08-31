package jp.ac.osaka_u.ist.sdl.scorpio.settings;

public enum MERGE {

	TRUE {

		@Override
		public boolean isMerge() {
			return true;
		}
	},

	FALSE {

		@Override
		public boolean isMerge() {
			return false;
		}
	};

	public abstract boolean isMerge();
}
