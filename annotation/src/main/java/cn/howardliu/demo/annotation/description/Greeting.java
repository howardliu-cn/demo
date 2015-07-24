package cn.howardliu.demo.annotation.description;

/**
 * <br/>create at 15-7-24
 *
 * @author liuxh
 * @since 1.0.0
 */
public @interface Greeting {
    public enum FontColor {BLUE, RED, GREEN}

    String name();

    FontColor fontColor() default FontColor.GREEN;
}
