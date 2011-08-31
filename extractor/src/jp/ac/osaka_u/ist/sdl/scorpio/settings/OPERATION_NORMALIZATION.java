package jp.ac.osaka_u.ist.sdl.scorpio.settings;

public enum OPERATION_NORMALIZATION {

    /**
     * ‰‰Z‚ğ‚»‚Ì‚Ü‚Ü—p‚¢‚é 
     */
    NO {

        @Override
        public String getText() {
            return "no";
        }
    },

    /**
     * ‰‰Z‚ğ‚»‚ÌŒ^‚Ì³‹K‰»‚·‚é
     */
    TYPE {

        @Override
        public String getText() {
            return "type";
        }
    },

    /**
     *@‰‰Z‚ÌƒLƒƒƒXƒg‚ğ“¯ˆê‚Ìš‹å‚É³‹K‰»‚·‚é
     */
    ALL {

        @Override
        public String getText() {
            return "all";
        }
    };

    public abstract String getText();
}
