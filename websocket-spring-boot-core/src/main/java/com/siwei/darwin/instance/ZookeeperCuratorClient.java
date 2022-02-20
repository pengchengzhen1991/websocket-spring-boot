package com.siwei.darwin.instance;

import com.alibaba.fastjson.JSON;
import com.siwei.darwin.common.exception.ZookeeperException;
import com.siwei.darwin.common.util.ExecutorTool;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.*;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.util.StringUtils;

import java.io.Closeable;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Data
public class ZookeeperCuratorClient<T> implements Closeable {
    private CuratorFramework curatorFramework;
    private AtomicReference<CuratorListener> listener;
    private AtomicReference<Boolean> connectSuccess;

    public ZookeeperCuratorClient(CuratorFramework curatorFramework) {
        this.curatorFramework = curatorFramework;
        this.listener = new AtomicReference<>(null);
        this.connectSuccess = new AtomicReference<>(false);
    }

    public boolean connect() {
        try {
            this.curatorFramework.start();
            log.info("1    state: {}", this.curatorFramework.getState());
            if (this.curatorFramework.getState() == CuratorFrameworkState.STARTED) {
                log.info("2    state: {}", this.curatorFramework.getState());
                this.connectSuccess.set(true);
            }
        } catch (Exception e) {
            log.info("curatorFramework connect exception", e);
            this.connectSuccess.set(false);
            this.timingConnection();
            return false;
        }
        return true;
    }

    private void checkConnect() {
        if (connectSuccess.get()) {
            return;
        }
        this.connect();
        if (!connectSuccess.get()) {
            throw new ZookeeperException("zookeeper connect fail");
        }
    }

    private void timingConnection() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!connect()) {
                    log.info("zookeeper connect fail");
                    timingConnection();
                }
            }
        }, 1000, 30 * 1000);
    }

    public String create(String path) {
        return this.create(path, null);
    }

    public String create(String path, String payload) {
        return this.create(path, payload, CreateMode.PERSISTENT);
    }

    public String create(String path, String payload, CreateMode mode) {
        return this.create(path, payload, mode, true);
    }

    public String create(String path, String payload, CreateMode mode, boolean createParent) {
        CreateBuilder builder = this.curatorFramework.create();
        if (createParent) {
            return this.create(builder.creatingParentContainersIfNeeded(), path, payload, mode);
        }
        return this.create(builder, path, payload, mode);
    }

    public String create(ProtectACLCreateModeStatPathAndBytesable<String> sable, String path, String payload, CreateMode mode) {
        return this.create(sable.withMode(mode), path, payload);
    }

    public String create(CreateBuilder builder, String path, String payload, CreateMode mode) {
        return this.create(builder.withMode(mode), path, payload);
    }

    public String create(ACLBackgroundPathAndBytesable<String> sable, String path, String payload) {
        this.checkConnect();
        try {
            if (StringUtils.hasText(payload)) {
                return sable.forPath(path, payload.getBytes());
            }
            return sable.forPath(path);
        } catch (Exception e) {
            throw new ZookeeperException(e.getMessage());
        }
    }

    public boolean exist(String path) {
        return this.exist(path, false);
    }

    public boolean exist(String path, boolean watch) {
        this.checkConnect();
        try {
            return watch ? this.curatorFramework.checkExists().watched().forPath(path) != null
                    : this.curatorFramework.checkExists().forPath(path) != null;
        } catch (Exception e) {
            throw new ZookeeperException(e.getMessage());
        }
    }

    public List<String> getChildren(String path, boolean watch) {
        this.checkConnect();
        try {
            return watch ? this.curatorFramework.getChildren().watched().forPath(path)
                    : this.curatorFramework.getChildren().forPath(path);
        } catch (Exception e) {
            throw new ZookeeperException(e.getMessage());
        }
    }

    public List<String> getChildrenData(String path, boolean watch) {
        List<String> childrenData = new LinkedList<>();
        try {
            for (String child : this.getChildren(path, watch)) {
                String childPath = path.endsWith("/") ? path + child : path + "/" + child;
                String data = this.readStringData(childPath, watch);
                if (StringUtils.hasText(data)) {
                    childrenData.add(data);
                }
            }
        } catch (Exception e) {
            throw new ZookeeperException(e.getMessage());
        }
        return childrenData;
    }

    public T readObject(String path, boolean watch, Class<T> t) {
        String readStringData = this.readStringData(path, watch);
        return StringUtils.hasText(readStringData) ? JSON.parseObject(readStringData, t) : null;
    }

    public String readStringData(String path, boolean watch) {
        return this.readStringData(path, null, watch);
    }

    public String readStringData(String path, Stat stat, boolean watch) {
        return new String(this.readData(path, stat, watch));
    }

    public byte[] readData(String path, boolean watch) {
        return this.readData(path, null, watch);
    }

    public byte[] readData(String path, Stat stat, boolean watch) {
        this.checkConnect();
        try {
            if (stat != null) {
                return watch ? this.curatorFramework.getData().storingStatIn(stat).watched().forPath(path)
                        : this.curatorFramework.getData().storingStatIn(stat).forPath(path);
            } else {
                return watch ? this.curatorFramework.getData().watched().forPath(path)
                        : this.curatorFramework.getData().forPath(path);
            }
        } catch (Exception e) {
            throw new ZookeeperException(e.getMessage());
        }
    }

    public void writeData(String path, byte[] data, int expectedVersion) {
        this.writeDataReturnStat(path, data, expectedVersion);
    }

    public void writeDataReturnStat(String path, byte[] data, int expectedVersion) {
        this.checkConnect();
        try {
            this.curatorFramework.setData().withVersion(expectedVersion).forPath(path, data);
        } catch (Exception e) {
            throw new ZookeeperException(e.getMessage());
        }
    }

    public void delete(String path) {
        this.checkConnect();
        try {
            this.curatorFramework.delete().forPath(path);
        } catch (Exception e) {
            throw new ZookeeperException(e.getMessage());
        }
    }

    public ZooKeeper.States getZookeeperState() {
        this.checkConnect();
        try {
            return this.curatorFramework.getZookeeperClient().getZooKeeper().getState();
        } catch (Exception e) {
            throw new ZookeeperException(e.getMessage());
        }
    }

    public Long getCreateTime(String path) {
        this.checkConnect();
        try {
            Stat stat = this.curatorFramework.checkExists().forPath(path);
            return stat != null ? stat.getCtime() : 0;
        } catch (Exception e) {
            throw new ZookeeperException(e.getMessage());
        }
    }

    public String getServers() {
        this.checkConnect();
        return this.curatorFramework.getZookeeperClient().getCurrentConnectionString();
    }

    public void nodeCache(String path, NodeCacheListener nodeCacheListener) {
        this.checkConnect();
        final NodeCache nodeCache = new NodeCache(this.curatorFramework, path, false);
        try {
            nodeCache.start(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        nodeCache.getListenable().addListener(nodeCacheListener, ExecutorTool.Instance());
    }

    public void pathChildrenCache(String path, PathChildrenCacheListener pathChildrenCacheListener) {
        this.checkConnect();
        final PathChildrenCache childrenCache = new PathChildrenCache(this.curatorFramework, path, true);
        try {
            childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        childrenCache.getListenable().addListener(pathChildrenCacheListener, ExecutorTool.Instance());
    }

    /**
     * @param watcher
     * @param url
     */
    public void callback(Watcher watcher, String url) {
        if (this.connectSuccess.get() && watcher != null) {
            CuratorListener localListener = (curatorFramework, curatorEvent) -> {
                if (curatorEvent.getWatchedEvent() != null) {
                    watcher.process(curatorEvent.getWatchedEvent());
                }
            };
            this.listener.set(localListener);
            this.curatorFramework.getCuratorListenable().addListener(localListener);

            try {
                BackgroundCallback callback = (curatorFramework, curatorEvent) -> {
                    Watcher.Event.KeeperState keeperState = curatorFramework.getZookeeperClient().isConnected()
                            ? Watcher.Event.KeeperState.SyncConnected : Watcher.Event.KeeperState.Disconnected;
                    WatchedEvent fakeEvent = new WatchedEvent(Watcher.Event.EventType.None, keeperState, null);
                    watcher.process(fakeEvent);
                };
                this.curatorFramework.checkExists().inBackground(callback).forPath(url);
            } catch (Exception e) {
                throw new ZookeeperException("");
            }
        }
    }

    @Override
    public void close() {
        CuratorListener localListener = listener.getAndSet(null);
        if (localListener != null) {
            this.curatorFramework.getCuratorListenable().removeListener(localListener);
        }
        if (this.curatorFramework != null) {
            this.curatorFramework.close();
            this.curatorFramework = null;
        }
    }

}
