package jp.ac.osaka_u.ist.sdl.scorpio.settings;


public enum PDG_TYPE {

    INTRA {

        @Override
        public boolean useIntraProceduralPDG() {
            return true;
        }

        @Override
        public boolean useInterProceduralPDG() {
            return false;
        }

        @Override
        public String getText() {
            return "intra";
        }
    },

    INTER {

        @Override
        public boolean useIntraProceduralPDG() {
            return false;
        }

        @Override
        public boolean useInterProceduralPDG() {
            return true;
        }

        @Override
        public String getText() {
            return "inter";
        }
    };

    public abstract boolean useIntraProceduralPDG();

    public abstract boolean useInterProceduralPDG();

    public abstract String getText();
}
