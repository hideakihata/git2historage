package jp.ac.osaka_u.ist.sdl.scorpio.settings;


public enum LITERAL_NORMALIZATION {

    /**
     * リテラルをそのまま用いる 
     */
    NO {

        @Override
        public String getText() {
            return "no";
        }
    },

    /**
     * リテラルをその型の正規化する
     */
    TYPE {

        @Override
        public String getText() {
            return "type";
        }
    },

    /**
     * 全てのリテラルを同一の字句に正規化する
     */
    ALL {

        @Override
        public String getText() {
            return "all";
        }
    };

    public abstract String getText();
}
