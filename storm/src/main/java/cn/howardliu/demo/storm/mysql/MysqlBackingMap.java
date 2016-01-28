package cn.howardliu.demo.storm.mysql;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import storm.trident.state.OpaqueValue;
import storm.trident.state.StateType;
import storm.trident.state.TransactionalValue;
import storm.trident.state.map.IBackingMap;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <br/>create at 16-1-26
 *
 * @author liuxh
 * @since 1.0.0
 */
public class MysqlBackingMap<T> implements IBackingMap<T> {
    private static final Logger logger = Logger.getLogger(MysqlBackingMap.class);

    private Connection connection;
    private MysqlStateConfig config;

    public MysqlBackingMap(final MysqlStateConfig config) {
        this.config = config;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword());
        } catch (final SQLException | ClassNotFoundException ex) {
            logger.error("Failed to establish DB connection", ex);
            throw new RuntimeException("Failed to establish DB connection", ex);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> multiGet(List<List<Object>> keys) {
        final List<List<List<Object>>> partitionedKeysList = Lists.partition(keys, config.getBatchSize());
        final List<T> result = new ArrayList<>();
        for (List<List<Object>> pKeys : partitionedKeysList) {
            // build a query using
            // select key1, keys2, ..., val1, val2, ..., [txid], [prev_val1], ...
            // FROM table
            // WHERE (key1 = ? AND keys = ? ...) OR ...
            // noinspection StringBufferReplaceableByString
            final StringBuilder queryBuilder = new StringBuilder()
                    .append("SELECT ")
                    .append(buildColumns())
                    .append(" FROM ")
                    .append(config.getTable())
                    .append(" WHERE ")
                    .append(buildKeyQuery(pKeys.size()));
            final Map<List<Object>, List<Object>> queryResults = query(queryBuilder.toString(), pKeys);
            result.addAll(Lists.transform(pKeys, new Function<List<Object>, T>() {
                @Override
                public T apply(final List<Object> key) {
                    final List<Object> values = queryResults.get(key);
                    if (values == null) {
                        return null;
                    } else {
                        switch (config.getType()) {
                            case OPAQUE: {
                                // partition the values list into 3 values [current], [txid], [prev]
                                return (T) new OpaqueValue(
                                        (Long) values.get(config.getValueColumns().length),// txid
                                        values.subList(0, config.getValueColumns().length),// curr values
                                        values.subList(config.getValueColumns().length, values.size())// prev values
                                );
                            }
                            case TRANSACTIONAL: {
                                return (T) new TransactionalValue(
                                        (Long) values.get(config.getValueColumns().length),// txid
                                        values.subList(0, config.getValueColumns().length)// curr values
                                );
                            }
                            default: {
                                return (T) values;
                            }
                        }
                    }
                }
            }));
        }
        return result;
    }

    @Override
    public void multiPut(List<List<Object>> keys, List<T> values) {
        // partitions the keys and the values and run it over every one
        final Iterator<List<List<Object>>> pKeysList = Lists.partition(keys, config.getBatchSize()).iterator();
        final Iterator<List<T>> partitionedValues = Lists.partition(values, config.getBatchSize()).iterator();
        while (pKeysList.hasNext() && partitionedValues.hasNext()) {
            final List<List<Object>> pKeys = pKeysList.next();
            final List<T> pValues = partitionedValues.next();
            // build a query
            // insert into table(key1, key2, ..., value1, value2, ... , [txid], [prev_val1], ...)
            // values (?,?,...), ...
            // ON DUPLICATE KEY
            // UPDATE value1 = VALUES(value1), ...
            // how many params per row of data
            // opaque => keys + 2 * vals + 1
            // transactional => keys + vals + 1
            // non-transactional => keys + vals
            int paramCount = 0;
            switch (config.getType()) {
                case OPAQUE:
                    paramCount += config.getValueColumns().length;
                case TRANSACTIONAL:
                    paramCount += 1;
                default:
                    paramCount += (config.getKeyColumns().length + config.getValueColumns().length);
            }
            final StringBuilder queryBuilder = new StringBuilder()
                    .append("INSERT INTO ")
                    .append(config.getTable())
                    .append("(")
                    .append(buildColumns())
                    .append(") VALUES ")
                    .append(Joiner.on(",")
                            .join(repeat("(" + Joiner.on(",").join(repeat("?", paramCount)) + ")", pKeys.size())))
                    .append(" ON DUPLICATE KEY UPDATE ")
                    .append(Joiner.on(",").join(Lists.transform(getValueColumns(), new Function<String, String>() {
                        @Override
                        public String apply(final String col) {
                            return col + " = VALUES(" + col + ")";
                        }
                    }))); // for every value column, constructs "valcol = VALUE(valcol)", joined by commas
            // run the update
            final List<Object> params = flattenPutParams(pKeys, pValues);
            PreparedStatement ps = null;
            int i = 0;
            try {
                ps = connection.prepareStatement(queryBuilder.toString());
                for (final Object param : params) {
                    ps.setObject(++i, param);
                }
                ps.execute();
            } catch (final SQLException ex) {
                logger.error("multi put update failed", ex);
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException ignore) {
                    }
                }
            }
            logger.debug(String.format("%1$d keys flushed", pKeys.size()));
        }
    }

    /**
     * run the multi get query, passing in the list of keys and returning key tuples mapped to value tuples
     *
     * @param sql  查询sql
     * @param keys 查询值
     * @return 结果
     */
    private Map<List<Object>, List<Object>> query(final String sql, final List<List<Object>> keys) {
        final Map<List<Object>, List<Object>> result = new HashMap<>();
        PreparedStatement ps = null;
        int i = 0;
        try {
            ps = connection.prepareStatement(sql);
            for (final List<Object> key : keys) {
                for (final Object keyPart : key) {
                    ps.setObject(++i, keyPart);
                }
            }
            final ResultSet rs = ps.executeQuery();
            final Function<String, Object> rsReader = new Function<String, Object>() {
                @Override
                public Object apply(final String column) {
                    try {
                        return rs.getObject(column);
                    } catch (final SQLException sqlex) {
                        return null;
                    }
                }
            };
            final List<String> keyColumns = Arrays.asList(config.getKeyColumns());
            final List<String> valueColumns = getValueColumns();
            while (rs.next()) {
                result.put(Lists.transform(keyColumns, rsReader), Lists.transform(valueColumns, rsReader));
            }
            rs.close();
        } catch (final SQLException e) {
            logger.error("multi get query failed", e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ignored) {
                }
            }
        }
        return result;
    }

    private String buildColumns() {
        // the columns for the composite unique key
        final List<String> cols = Lists.newArrayList(config.getKeyColumns());
        cols.addAll(getValueColumns());
        return Joiner.on(",").join(cols);
    }

    private String buildKeyQuery(int n) {
        final String single = "(" + Joiner.on(" AND ")
                .join(Lists.transform(Arrays.asList(config.getKeyColumns()), new Function<String, String>() {
                    @Override
                    public String apply(final String field) {
                        return field + " = ?";
                    }
                })) + ")";
        return Joiner.on(" OR ").join(repeat(single, n));
    }

    private List<String> getValueColumns() {
        // the columns storing the values
        List<String> cols = Lists.newArrayList(config.getValueColumns());
        if (StateType.OPAQUE.equals(config.getType()) || StateType.TRANSACTIONAL.equals(config.getType())) {
            cols.add("txid");
        }
        if (StateType.OPAQUE.equals(config.getType())) {
            cols.addAll(Lists.transform(Arrays.asList(config.getValueColumns()), new Function<String, String>() {
                @Override
                public String apply(final String field) {
                    return "prev_" + field;
                }
            })); // the prev_* columns
        }
        return cols;
    }

    private <U> List<U> repeat(final U val, final int count) {
        final List<U> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(val);
        }
        return list;
    }

    private List<Object> flattenPutParams(final List<List<Object>> keys, final List<T> values) {
        final List<Object> flattenedRows = new ArrayList<>();
        for (int i = 0; i < keys.size(); i++) {
            flattenedRows.addAll(keys.get(i));
            switch (config.getType()) {
                case OPAQUE: {
                    flattenedRows.addAll(valueToParams(((OpaqueValue) values.get(i)).getCurr()));
                    flattenedRows.add(((OpaqueValue) values.get(i)).getCurrTxid());
                    flattenedRows.addAll(valueToParams(((OpaqueValue) values.get(i)).getPrev()));
                    break;
                }
                case TRANSACTIONAL: {
                    flattenedRows.addAll(valueToParams(((TransactionalValue) values.get(i)).getVal()));
                    flattenedRows.add(((TransactionalValue) values.get(i)).getTxid());
                    break;
                }
                default: {
                    flattenedRows.addAll(valueToParams(values.get(i)));
                }
            }
        }
        return flattenedRows;
    }

    @SuppressWarnings("unchecked")
    private List<Object> valueToParams(final Object value) {
        if (!(value instanceof List)) {
            return repeat(value, config.getValueColumns().length);
        } else {
            return (List<Object>) value;
        }
    }
}
