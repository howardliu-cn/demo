package cn.howardliu.demo.guava;

import com.google.common.util.concurrent.RateLimiter;

/**
 * <br>created at 17-4-27
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class SmoothBurstyDemo {
    public static void main(String[] args) {
        RateLimiter limiter = RateLimiter.create(1);
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire(10));
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
    }
}
