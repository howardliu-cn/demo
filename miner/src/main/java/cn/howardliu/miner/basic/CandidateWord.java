package cn.howardliu.miner.basic;

import java.util.HashMap;
import java.util.Map;

public class CandidateWord implements Comparable<CandidateWord> {
    public String word;
    public Long frequency = 0L;
    public Map<String, Long> preMap = new HashMap<>();
    public Map<String, Long> postMap = new HashMap<>();
    public long preSum;
    public long postSum;
    public double preEntropy;
    public double postEntropy;

    @Override
    public int compareTo(CandidateWord o) {
        if (o == null) {
            return -1;
        }
        // 先比价频数
        if (this.frequency < o.frequency) {
            return 1;
        } else if (this.frequency > o.frequency) {
            return -1;
        }
        // 再比较左邻字信息熵
        if (this.preEntropy < o.preEntropy) {
            return 1;
        } else if (this.preEntropy > o.preEntropy) {
            return -1;
        }
        // 再比较右邻字信息熵
        if (this.postEntropy < o.postEntropy) {
            return 1;
        } else if (this.postEntropy > o.postEntropy) {
            return -1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "CandidateWord{" +
                "word='" + word + '\'' +
                ", frequency=" + frequency +
                ", preMap=" + preMap +
                ", postMap=" + postMap +
                ", preSum=" + preSum +
                ", postSum=" + postSum +
                ", preEntropy=" + preEntropy +
                ", postEntropy=" + postEntropy +
                '}';
    }
}