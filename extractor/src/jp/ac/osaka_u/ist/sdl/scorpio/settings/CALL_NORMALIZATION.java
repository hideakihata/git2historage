package jp.ac.osaka_u.ist.sdl.scorpio.settings;

public enum CALL_NORMALIZATION {

	/**
	 * メソッドが定義されているクラスの完全限定名も用いる
	 */
	FQN {

		@Override
		public String getText() {
			return "fqn";
		}
	},

	/**
	 * 呼び出し名はそのまま．引数情報も用いる．
	 */
	NO {

		@Override
		public String getText() {
			return "no";
		}
	},

	/**
	 * 呼び出し名を返り値の型名に変換する．引数情報も用いる．
	 */
	TYPE_WITH_ARG {

		@Override
		public String getText() {
			return "type with arg";
		}
	},

	/**
	 *　呼び出し名を返り値の型名に変換する．引数情報は用いない．
	 */
	TYPE_WITHOUT_ARG {

		@Override
		public String getText() {
			return "type without arg";
		}
	},

	/**
	 * 全ての呼び出しを同一字句に正規化する．引数情報は用いない．
	 */
	ALL {

		@Override
		public String getText() {
			return "all";
		}
	};

	public abstract String getText();
}
