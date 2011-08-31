package jp.ac.osaka_u.ist.sdl.scorpio.settings;


public enum SLICE_TYPE {

    BACKWARD {

        @Override
        public boolean useBackwardSlice() {
            return true;
        }

        @Override
        public boolean useForwardSlice() {
            return false;
        }

        @Override
        public String getText() {
            return "backward";
        }
    },

    FORWARD {

        @Override
        public boolean useBackwardSlice() {
            return false;
        }

        @Override
        public boolean useForwardSlice() {
            return true;
        }

        @Override
        public String getText() {
            return "forward";
        }
    };

    public abstract boolean useBackwardSlice();

    public abstract boolean useForwardSlice();

    public abstract String getText();
}
