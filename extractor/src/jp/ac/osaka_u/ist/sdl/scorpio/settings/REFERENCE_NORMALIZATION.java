package jp.ac.osaka_u.ist.sdl.scorpio.settings;


public enum REFERENCE_NORMALIZATION {

    /**
     * 演算をそのまま用いる 
     */
    NO {

        @Override
        public String getText() {
            return "no";
        }
    },

    /**
     *　演算のキャストを同一の字句に正規化する
     */
    ALL {

        @Override
        public String getText() {
            return "all";
        }
    };

    public abstract String getText();
}
