package jp.ac.osaka_u.ist.sdl.scorpio.settings;


public enum DEPENDENCY_TYPE {

    DATA {
        @Override
        public String getText() {
            return "data";
        }
    },

    CONTROL {
        @Override
        public String getText() {
            return "control";
        }
    },

    EXECUTION {
        @Override
        public String getText() {
            return "execution";
        }
    };

    public abstract String getText();
}
