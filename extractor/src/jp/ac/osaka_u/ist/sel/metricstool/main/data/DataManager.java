package jp.ac.osaka_u.ist.sel.metricstool.main.data;


import java.lang.reflect.Field;
import java.util.Map;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.ClassMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.FieldMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.FileMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MethodMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFileManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.PluginManager;


/**
 * 全てのマネージャーを管理するマネージャー
 * 
 * @author higo
 *
 */
public class DataManager {

    /**
     * データマネージャーのインスタンスを取得する
     * 
     * @return　データマネージャーのインスタンス
     */
    public static DataManager getInstance() {

        if (null == SINGLETON) {
            SINGLETON = new DataManager();
        }

        return SINGLETON;

    }

    /**
     * データマネージャーに登録されている情報をクリアする
     */
    public static void clear() {
        SINGLETON = null;

        try {

            {
                final Class<?> settings = Settings.class;
                final Field instance = settings.getDeclaredField("INSTANCE");
                instance.setAccessible(true);
                instance.set(null, null);
            }

            {
                final Class<?> fieldUsageInfo = FieldUsageInfo.class;
                final Field FIELD_USAGE_MAP = fieldUsageInfo.getDeclaredField("USAGE_MAP");
                FIELD_USAGE_MAP.setAccessible(true);
                final Map<?, ?> fieldMap = (Map<?, ?>) FIELD_USAGE_MAP.get(null);
                fieldMap.clear();
            }

            {
                final Class<?> parameterUsageInfo = ParameterUsageInfo.class;
                final Field PARAMETER_USAGE_MAP = parameterUsageInfo.getDeclaredField("USAGE_MAP");
                PARAMETER_USAGE_MAP.setAccessible(true);
                final Map<?, ?> parameterMap = (Map<?, ?>) PARAMETER_USAGE_MAP.get(null);
                parameterMap.clear();
            }

            {
                final Class<?> localVariableUsageInfo = LocalVariableUsageInfo.class;
                final Field LOCALVARIABLE_USAGE_MAP = localVariableUsageInfo
                        .getDeclaredField("USAGE_MAP");
                LOCALVARIABLE_USAGE_MAP.setAccessible(true);
                final Map<?, ?> localVariableMap = (Map<?, ?>) LOCALVARIABLE_USAGE_MAP.get(null);
                localVariableMap.clear();
            }

            {
                final Class<?> arrayTypeInfo = ArrayTypeInfo.class;
                final Field ARRAY_TYPE_MAP = arrayTypeInfo.getDeclaredField("ARRAY_TYPE_MAP");
                ARRAY_TYPE_MAP.setAccessible(true);
                final Map<?, ?> arrayTypeMap = (Map<?, ?>) ARRAY_TYPE_MAP.get(null);
                arrayTypeMap.clear();
            }

            {
                final Class<?> unresolvedArrayTypeInfo = UnresolvedArrayTypeInfo.class;
                final Field ARRAY_TYPE_MAP = unresolvedArrayTypeInfo
                        .getDeclaredField("ARRAY_TYPE_MAP");
                ARRAY_TYPE_MAP.setAccessible(true);
                final Map<?, ?> arrayTypeMap = (Map<?, ?>) ARRAY_TYPE_MAP.get(null);
                arrayTypeMap.clear();
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * TargetFileManager　を返す
     * 
     * @return TargetFileManager
     */
    public TargetFileManager getTargetFileManager() {
        return this.targetFileManager;
    }

    public void clearTargetFileManager() {
        this.targetFileManager = new TargetFileManager();
    }

    /**
     * ClassMetricsInfoManager　を返す
     * 
     * @return ClassMetricsInfoManager
     */
    public ClassMetricsInfoManager getClassMetricsInfoManager() {
        return this.classMetricsInfoManager;
    }

    /**
     * FieldMetricsInfoManager　を返す
     * 
     * @return FieldMetricsInfoManager
     */
    public FieldMetricsInfoManager getFieldMetricsInfoManager() {
        return this.fieldMetricsInfoManager;
    }

    /**
     * FileMetricsInfoManager　を返す
     * 
     * @return FileMetricsInfoManager
     */
    public FileMetricsInfoManager getFileMetricsInfoManager() {
        return this.fileMetricsInfoManager;
    }

    /**
     * MethodMetricsInfoManager　を返す
     * 
     * @return MethodMetricsInfoManager
     */
    public MethodMetricsInfoManager getMethodMetricsInfoManager() {
        return this.methodMetricsInfoManager;
    }

    /**
     * UnresolvedClassInfoManager　を返す
     * 
     * @return UnresolvedClassInfoManager
     */
    public UnresolvedClassInfoManager getUnresolvedClassInfoManager() {
        return this.unresolvedClassInfoManager;
    }

    public void clearUnresolvedClassInfoManager() {
        this.unresolvedClassInfoManager = new UnresolvedClassInfoManager();
    }

    /**
     * ClassInfoManager　を返す
     * 
     * @return ClassInfoManager
     */
    public ClassInfoManager getClassInfoManager() {
        return this.classInfoManager;
    }

    /**
     * FieldInfoManager　を返す
     * 
     * @return FieldInfoManager
     */
    public FieldInfoManager getFieldInfoManager() {
        return this.fieldInfoManager;
    }

    /**
     * FileInfoManager　を返す
     * 
     * @return FileInfoManager
     */
    public FileInfoManager getFileInfoManager() {
        return this.fileInfoManager;
    }

    public void clearFileInfoManager() {
        this.fileInfoManager.clear();
    }

    /**
     * MethodInfoManager　を返す
     * 
     * @return MethodInfoManager
     */
    public MethodInfoManager getMethodInfoManager() {
        return this.methodInfoManager;
    }

    /**
     * PluginManager　を返す
     * 
     * @return PluginManager
     */
    public PluginManager getPluginManager() {
        return this.pluginManager;
    }

    private DataManager() {
        this.targetFileManager = new TargetFileManager();
        this.unresolvedClassInfoManager = new UnresolvedClassInfoManager();

        this.classMetricsInfoManager = new ClassMetricsInfoManager();
        this.fieldMetricsInfoManager = new FieldMetricsInfoManager();
        this.fileMetricsInfoManager = new FileMetricsInfoManager();
        this.methodMetricsInfoManager = new MethodMetricsInfoManager();

        this.classInfoManager = new ClassInfoManager();
        this.fieldInfoManager = new FieldInfoManager();
        this.fileInfoManager = new FileInfoManager();
        this.methodInfoManager = new MethodInfoManager();

        this.pluginManager = new PluginManager();
    }

    private static DataManager SINGLETON;

    private TargetFileManager targetFileManager;

    final private ClassMetricsInfoManager classMetricsInfoManager;

    final private FieldMetricsInfoManager fieldMetricsInfoManager;

    final private FileMetricsInfoManager fileMetricsInfoManager;

    final private MethodMetricsInfoManager methodMetricsInfoManager;

    private UnresolvedClassInfoManager unresolvedClassInfoManager;

    final private ClassInfoManager classInfoManager;

    final private FieldInfoManager fieldInfoManager;

    final private FileInfoManager fileInfoManager;

    final private MethodInfoManager methodInfoManager;

    final private PluginManager pluginManager;
}
