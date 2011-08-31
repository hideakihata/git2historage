package jp.ac.osaka_u.ist.sdl.scorpio.settings;

public enum VARIABLE_NORMALIZATION {

    /**
     * •Ï”‚ğ‚»‚Ì‚Ü‚Ü—p‚¢‚é 
     */
    NO {

        @Override
        public String getText() {
            return "no";
        }
    },

    /**
     * •Ï”‚ğ‚»‚ÌŒ^‚Ì³‹K‰»‚·‚é
     */
    TYPE {

        @Override
        public String getText() {
            return "type";
        }
    },

    /**
     * ‘S‚Ä‚Ì•Ï”‚ğ“¯ˆê‚Ìš‹å‚É³‹K‰»‚·‚é
     */
    ALL {

        @Override
        public String getText() {
            return "all";
        }
    };

    public abstract String getText();
}
