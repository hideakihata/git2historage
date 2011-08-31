package jp.ac.osaka_u.ist.sel.metricstool.main.parse.asm;


import java.util.ArrayList;
import java.util.List;


public class JavaByteCodeUtility {

    /**
     * 引数で与えられたFull Qualified Nameを表す文字列を分割して，を配列として返す
     * 型引数がついていても，取り除く等の処理はしない
     * 
     * @param name
     * @return
     */
    public static String[] separateName(final String name) {

        final List<String> names = new ArrayList<String>();

        int startIndex = 0;
        int nestLevel = 0;

        for (int index = 0; index < name.length(); index++) {

            if ('<' == name.charAt(index)) {
                nestLevel++;
            }

            else if ('>' == name.charAt(index)) {
                nestLevel--;
            }

            else if ((0 == nestLevel)
                    && (('/' == name.charAt(index)) || ('$' == name.charAt(index)) || ('.' == name
                            .charAt(index)))) {
                names.add(name.substring(startIndex, index));
                startIndex = index + 1;
            }
        }
        names.add(name.substring(startIndex, name.length()));

        return names.toArray(new String[0]);
    }

    /**
     * 与えられた型（名前）から型引数を取り除いたものを返す
     * 型パラメータがない場合はそのまま返す
     * 
     * @param type
     * @return
     */
    public static String removeTypeArguments(final String type) {
        final int index = type.indexOf('<');
        return -1 == index ? type : type.substring(0, index);
    }

    /**
     * 与えられた型（名前）から型引数部分を抽出して返す
     * 型パラメータがない場合はnullを返す
     * 
     * @param type
     * @return
     */
    public static String extractTypeArguments(final String type) {
        final int openIndex = type.indexOf('<');
        final int closeIndex = type.lastIndexOf('>');
        return (-1 == openIndex) || (-1 == closeIndex) ? null : type.substring(openIndex + 1,
                closeIndex);
    }

    /**
     * 引数として与えられた型の文字列から，各型を切り出して配列として返す
     * 
     * @param text
     * @return
     */
    public static String[] separateTypes(final String text) {

        if (null == text) {
            throw new IllegalArgumentException();
        }

        final List<String> types = new ArrayList<String>();

        for (int index = 0, nestLevel = 0, dimension = 0; index < text.length(); index++) {

            if ('<' == text.charAt(index)) {
                nestLevel++;
            }

            else if ('>' == text.charAt(index)) {
                nestLevel--;
            }

            // スローされる例外のための分岐，特に処理はない
            else if ('^' == text.charAt(index)) {

            }

            //一文字の型のとき
            else if ((0 == nestLevel) && isSingleCharacterType(text.charAt(index))) {
                final String type = String.valueOf(text.charAt(index));
                final StringBuilder sb = new StringBuilder();
                for (int i = 1; i <= dimension; i++) { //配列を考慮
                    sb.append('[');
                }
                sb.append(type);
                types.add(sb.toString());
                dimension = 0;
            }

            // 複数文字の型のとき
            else if ((0 == nestLevel) && isMultipleCharactersType(text.charAt(index))) {
                final String type = extractMultipleCharactersType(text.substring(index));
                final StringBuilder sb = new StringBuilder();
                for (int i = 1; i <= dimension; i++) { //配列を考慮
                    sb.append('[');
                }
                sb.append(type);
                types.add(sb.toString());
                index += type.length() - 1;
                dimension = 0;
            }

            else if ((0 == nestLevel) && ('[' == text.charAt(index))) {
                dimension++;
            }

            // それ以外のときは状態異常
            else
                throw new IllegalStateException();
        }

        return types.toArray(new String[0]);
    }

    /**
     * 引数で与えられた文字が一文字型を表す場合はtrue,そうでない場合はfalseを返す
     * 
     * @param c
     * @return
     */
    private static boolean isSingleCharacterType(final char c) {

        switch (c) {
        case 'Z':
        case 'C':
        case 'B':
        case 'S':
        case 'I':
        case 'F':
        case 'J':
        case 'D':
        case 'V':
        case '*':
            return true;
        default:
            return false;
        }
    }

    /**
     * 引数で与えられた文字が複数文字型を表す場合はtrue,そうでない場合はfalseを返す
     * 
     * @param c
     * @return
     */
    private static boolean isMultipleCharactersType(final char c) {

        switch (c) {
        case 'L':
        case 'T':
        case '+':
        case '-':
            return true;
        default:
            return false;
        }
    }

    /**
     * 引数として与えられた文字列の先頭に現れる複数型を切り出して返す
     * 
     * @param text
     * @return
     */
    private static String extractMultipleCharactersType(final String text) {

        for (int index = 0, nestLevel = 0; index < text.length(); index++) {

            if ('<' == text.charAt(index)) {
                nestLevel++;
            }

            else if ('>' == text.charAt(index)) {
                nestLevel--;
            }

            else if ((';' == text.charAt(index)) && (0 == nestLevel)) {
                return text.substring(0, index + 1);
            }
        }

        assert false : "Here shouldn't be reached!";
        return null;
    }
}
