package cn.howardliu.demo;

/**
 * <br/>create at 15-8-3
 *
 * @author liuxh
 * @since 1.0.0
 */
public enum Color {
    RED {
        @Override
        public String value() {
            return null;
        }
    };
    public abstract String value();
}
