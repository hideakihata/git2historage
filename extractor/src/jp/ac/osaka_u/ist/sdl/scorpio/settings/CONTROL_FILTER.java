package jp.ac.osaka_u.ist.sdl.scorpio.settings;


public enum CONTROL_FILTER {

    USE {
        @Override
        public boolean useControlFilter() {
            return true;
        }
        
        @Override
        public String getText(){
            return "yes";
        }
    },

    NO_USE {
        @Override
        public boolean useControlFilter() {
            return false;
        }
        
        @Override
        public String getText(){
            return "no";
        }
    };

    public abstract boolean useControlFilter();
    
    public abstract String getText();
}
