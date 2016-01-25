package cn.howardliu.demo.storm.trident;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storm.trident.operation.TridentCollector;
import storm.trident.spout.ITridentSpout;
import storm.trident.topology.TransactionAttempt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <br/>create at 16-1-25
 *
 * @author liuxh
 * @since 1.0.0
 */
public class DiagnosisEventEmitter implements ITridentSpout.Emitter<Long>, Serializable {
    private static final Logger logger = LoggerFactory.getLogger(DiagnosisEventEmitter.class);
    AtomicInteger successfulTransactions = new AtomicInteger(0);

    @Override
    public void emitBatch(TransactionAttempt tx, Long coordinatorMeta, TridentCollector collector) {
        for (int i = 0; i < 10000; i++) {
            List<Object> events = new ArrayList<>();
            double lat = -30 + (int) (Math.random() * 75);
            double lng = -120 + (int) (Math.random() * 70);
            long time = System.currentTimeMillis();
            String diag = 320 + (int) (Math.random() * 7) + "";
            DiagnosisEvent event = new DiagnosisEvent(lat, lng, time, diag);
            events.add(event);
            collector.emit(events);
        }
    }

    @Override
    public void success(TransactionAttempt tx) {
        successfulTransactions.incrementAndGet();
    }

    @Override
    public void close() {
    }
}
