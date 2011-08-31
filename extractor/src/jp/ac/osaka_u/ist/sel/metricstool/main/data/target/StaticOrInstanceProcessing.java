package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * Member インターフェースを利用した処理を行うクラス．
 * 
 * @author higo
 * 
 */
public class StaticOrInstanceProcessing {

    /**
     * 引数で与えられたメンバーのうち，スタティックなものだけを抽出して返す．
     * 
     * @param <T> メンバーの型
     * @param members メンバーの List
     * @return スタティックなメンバーの List
     */
    public static final <T extends StaticOrInstance> List<T> getInstanceMembers(final List<T> members) {

        final List<T> instanceMembers = new ArrayList<T>();
        for (T member : members) {
            if (member.isInstanceMember()) {
                instanceMembers.add(member);
            }
        }

        return Collections.unmodifiableList(instanceMembers);
    }

    /**
     * 引数で与えられたメンバーのうち，インスタンスなものだけを抽出して返す．
     * 
     * @param <T> メンバーの型
     * @param members メンバーの List
     * @return インスタンスメンバーの List
     */
    public static final <T extends StaticOrInstance> List<T> getStaticMembers(final List<T> members) {

        final List<T> staticMembers = new ArrayList<T>();
        for (T member : members) {
            if (member.isStaticMember()) {
                staticMembers.add(member);
            }
        }

        return Collections.unmodifiableList(staticMembers);
    }

    /**
     * 引数で与えられたメンバーのうち，スタティックなものだけを抽出して返す．
     * 
     * @param <T> メンバーの型
     * @param members メンバーの SortedSet
     * @return スタティックなメンバーの SortedSet
     */
    public static final <T extends StaticOrInstance> SortedSet<T> getInstanceMembers(
            final SortedSet<T> members) {

        final SortedSet<T> instanceMembers = new TreeSet<T>();
        for (T member : members) {
            if (member.isInstanceMember()) {
                instanceMembers.add(member);
            }
        }

        return Collections.unmodifiableSortedSet(instanceMembers);
    }

    /**
     * 引数で与えられたメンバーのうち，インスタンスなものだけを抽出して返す．
     * 
     * @param <T> メンバーの型
     * @param members メンバーの SortedSet
     * @return インスタンスメンバーの SortedSet
     */
    public static final <T extends StaticOrInstance> SortedSet<T> getStaticMembers(final SortedSet<T> members) {

        final SortedSet<T> staticMembers = new TreeSet<T>();
        for (T member : members) {
            if (member.isStaticMember()) {
                staticMembers.add(member);
            }
        }

        return Collections.unmodifiableSortedSet(staticMembers);
    }
}
