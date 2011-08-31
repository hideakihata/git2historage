package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


@SuppressWarnings("serial")
public final class BlockCommentInfo extends CommentInfo {

    public BlockCommentInfo(final String content, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {
        super(content, fromLine, fromColumn, toLine, toColumn);
    }
}
