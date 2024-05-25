package io.cc.config.server.cluster;

import io.cc.config.server.service.DistributedLock;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author nhsoft.lsd
 */
@Component
@Slf4j
public class ClusterManager {

    private static final String LEADER_KEY = "leader_key";

    private static final int LOCK_TIME = 5;

    @Resource
    private DistributedLock distributedLock;

    private AtomicBoolean leader = new AtomicBoolean(false);

    private ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1);;

    @PostConstruct
    public void init() {
        executor.scheduleWithFixedDelay(() -> {
            boolean locked = distributedLock.tryLock(LEADER_KEY, LOCK_TIME);
            if (!locked) {
                log.info("failed to get lock");
                leader.set(false);
                return;
            }
            if (leader.get()) {
                log.info("reenter this lock");
            } else {
                log.info("get a lock");
            }
            leader.set(true);
        }, 1, LOCK_TIME, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void destroy() {
        distributedLock.unlock(LEADER_KEY);
        log.info("close a lock");
    }

    public boolean isLeader() {
        return leader.get();
    }
}
