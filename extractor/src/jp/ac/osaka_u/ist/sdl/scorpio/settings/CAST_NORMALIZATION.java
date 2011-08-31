package jp.ac.osaka_u.ist.sdl.scorpio.settings;


public enum CAST_NORMALIZATION {

    /**
     * キャストをそのまま用いる 
     */
    NO {

        @Override
        public String getText() {
            return "no";
        }
    },

    /**
     * キャストをその型の正規化する
     */
    TYPE {

        @Override
        public String getText() {
            return "type";
        }
    },

    /**
     * 全てのキャストを同一の字句に正規化する
     */
    ALL {

        @Override
        public String getText() {
            return "all";
        }
    };

    public abstract String getText();
}
