package jp.ac.osaka_u.ist.sel.metricstool.main.util;


/**
 * 言語情報を表すEnum．
 * 
 * 現在はJAVAのみ(2006/11/16)
 * 
 * @author kou-tngt
 * 
 */
public enum LANGUAGE {
    /**
     * Java言語 バージョン1.5以降用
     */
    JAVA15 {
        @Override
        public boolean isObjectOrientedLanguage() {
            return true;
        }

        @Override
        public String getExtension() {
            return ".java";
        }

        @Override
        public String getName() {
            return "Java";
        }

        @Override
        public String getIdentifierName() {
            return "java15";
        }

        @Override
        public String getNamespaceDelimiter() {
            return ".";
        }
    },

    /**
     * Java言語 バージョン1.4用
     */
    JAVA14 {
        @Override
        public boolean isObjectOrientedLanguage() {
            return true;
        }

        @Override
        public String getExtension() {
            return ".java";
        }

        @Override
        public String getName() {
            return "Java";
        }

        @Override
        public String getIdentifierName() {
            return "java14";
        }

        @Override
        public String getNamespaceDelimiter() {
            return ".";
        }
    },

    /**
     * Java言語 バージョン1.3用
     */
    JAVA13 {
        @Override
        public boolean isObjectOrientedLanguage() {
            return true;
        }

        @Override
        public String getExtension() {
            return ".java";
        }

        @Override
        public String getName() {
            return "Java";
        }

        @Override
        public String getIdentifierName() {
            return "java13";
        }

        @Override
        public String getNamespaceDelimiter() {
            return ".";
        }
    },
    
    /**
     * Java言語 バージョン1.3用
     */
    CSHARP {
        @Override
        public boolean isObjectOrientedLanguage() {
            return true;
        }

        @Override
        public String getExtension() {
            return ".cs";
        }

        @Override
        public String getName() {
            return "CSharp";
        }

        @Override
        public String getIdentifierName() {
            return "CSharp";
        }

        @Override
        public String getNamespaceDelimiter() {
            return ".";
        }
    },

    //    C_PLUS_PLUS {
    //        @Override
    //        public boolean isObjectOrientedLanguage() {
    //            return true;
    //        }
    //    },

    //    C_SHARP {
    //        @Override
    //        public boolean isObjectOrientedLanguage() {
    //            return true;
    //        }
    //    },

    ;

    /**
     * この言語がオブジェクト指向言語かどうかを返すメソッド
     * 
     * @return オブジェクト指向言語であればtrue
     */
    public boolean isObjectOrientedLanguage() {
        return false;
    }

    /**
     * この言語が構造化言語かどうかを返すメソッド
     * 
     * @return 構造化言語であればtrue
     */
    public boolean isStructuralLanguage() {
        return false;
    }

    /**
     * この言語が関数型言語かどうかを返すメソッド
     * 
     * @return 関数型言語であればtrue
     */
    public boolean isFunctionalLanguage() {
        return false;
    }

    /**
     * この言語がスクリプト言語かどうかを返すメソッド
     * 
     * @return スクリプト言語であればtrue
     */
    public boolean isScriptLanguage() {
        return false;
    }

    /**
     * この言語の拡張子を返す
     * 
     * @return この言語の拡張子
     */
    public abstract String getExtension();

    /**
     * この言語の名前を返す
     * 
     * @return この言語の名前
     * 
     */
    public abstract String getName();

    /**
     * この言語の識別子名を返す．
     * 識別子名は -l オプションに用いられる.
     * 
     * @return この言語の説明文
     */
    public abstract String getIdentifierName();

    /**
     * この言語の名前空間を区切る文字（列）をを返す．
     * 
     * @return この言語の名前空間を区切る文字（列）
     */
    public abstract String getNamespaceDelimiter();
}
