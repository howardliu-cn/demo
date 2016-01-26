package cn.howardliu.demo.storm.mysql;

import storm.trident.state.StateType;

import java.io.Serializable;

/**
 * <br/>create at 16-1-26
 *
 * @author liuxh
 * @since 1.0.0
 */
public class MysqlStateConfig implements Serializable {
    public static final int DEFAULT_CACHE_SIZE = 5000;
    public static final int DEFAULT_BATCH_SIZE = 5000;

    private String url;
    private String username;
    private String password;
    private String table;
    private StateType type = StateType.OPAQUE;
    private String[] keyColumns;
    private String[] valueColumns;
    private int batchSize = DEFAULT_BATCH_SIZE;
    private int cacheSize = DEFAULT_CACHE_SIZE;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public StateType getType() {
        return type;
    }

    public void setType(StateType type) {
        this.type = type;
    }

    public String[] getKeyColumns() {
        return keyColumns;
    }

    public void setKeyColumns(String[] keyColumns) {
        this.keyColumns = keyColumns;
    }

    public String[] getValueColumns() {
        return valueColumns;
    }

    public void setValueColumns(String[] valueColumns) {
        this.valueColumns = valueColumns;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public int getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }
}
