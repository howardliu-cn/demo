package cn.howardliu.demo.storm.alert;

import storm.trident.tuple.TridentTuple;

import java.io.Serializable;

/**
 * <br/>create at 16-1-25
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface MessageMapper extends Serializable {
    String toMessageBody(TridentTuple tuple);
}
