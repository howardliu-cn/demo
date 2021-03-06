package cn.howardliu.demo.heart;

/**
 * <br>created at 17-3-29
 *
 * @author liuxh
 * @since 1.0.0
 */
public enum IdleState {
    /**
     * No data was received for a while.
     */
    READER_IDLE,
    /**
     * No data was sent for a while.
     */
    WRITER_IDLE,
    /**
     * No data was either received or sent for a while.
     */
    ALL_IDLE
}
