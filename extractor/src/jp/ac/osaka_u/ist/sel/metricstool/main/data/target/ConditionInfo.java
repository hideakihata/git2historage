package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * if•¶‚âfor•¶‚ÌğŒ‚ğ•\‚·ƒNƒ‰ƒX
 * 
 * @author higo
 *
 */
public interface ConditionInfo extends ExecutableElementInfo {

    ConditionalBlockInfo getOwnerConditionalBlock();

    void setOwnerConditionalBlock(ConditionalBlockInfo ownerConditionalBlock);
}
