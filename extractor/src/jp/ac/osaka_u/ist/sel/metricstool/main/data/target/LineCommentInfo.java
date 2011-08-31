package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


@SuppressWarnings("serial")
public final class LineCommentInfo extends CommentInfo {

    public LineCommentInfo(final String content, final int fromLine, int fromColumn, final int toColumn) {
        super(content, fromLine, fromColumn, fromLine, toColumn);
    }
}
