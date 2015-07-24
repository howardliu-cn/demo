package cn.howardliu.demo.annotation.handler;

import cn.howardliu.demo.annotation.custom.FruitColor;
import cn.howardliu.demo.annotation.custom.FruitName;
import cn.howardliu.demo.annotation.custom.FruitProvider;

/**
 * <br/>create at 15-7-24
 *
 * @author liuxh
 * @since 1.0.0
 */
public class Apple {
    @FruitName("Apple")
    private String appleName;

    @FruitColor(fruitColor = FruitColor.Color.RED)
    private String appleColor;

    @FruitProvider(id = 1, name = "红富士集团", address = "红富士大厦")
    private String appleProvider;

    public void setAppleColor(String appleColor) {
        this.appleColor = appleColor;
    }

    public String getAppleColor() {
        return appleColor;
    }

    public void setAppleName(String appleName) {
        this.appleName = appleName;
    }

    public String getAppleName() {
        return appleName;
    }

    public void setAppleProvider(String appleProvider) {
        this.appleProvider = appleProvider;
    }

    public String getAppleProvider() {
        return appleProvider;
    }

    public void displayName() {
        System.out.println("水果的名字是：苹果");
    }
}
