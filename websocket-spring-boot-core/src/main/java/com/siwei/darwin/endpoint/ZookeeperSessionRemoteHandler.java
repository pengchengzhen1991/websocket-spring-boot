package com.siwei.darwin.endpoint;

import com.siwei.darwin.common.constant.CommonConstants;
import com.siwei.darwin.common.exception.WebsocketException;
import com.siwei.darwin.config.properties.InstanceConfigBean;
import com.siwei.darwin.config.properties.SessionConfigBean;
import com.siwei.darwin.model.UserSessionInfo;
import com.siwei.darwin.instance.ZookeeperCuratorClient;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketSession;

import java.security.Principal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


@Slf4j
@Data
@RequiredArgsConstructor
public class ZookeeperSessionRemoteHandler implements SessionRemoteHandler {
    private final ZookeeperCuratorClient curatorClient;
    private final SessionConfigBean sessionConfigBean;
    private final InstanceConfigBean instanceConfigBean;


    @Override
    public void registry(WebSocketSession session) {
        try {
            String sessionUrl = this.getSessionPath(session);
            if (!this.curatorClient.exist(sessionUrl)) {
                this.curatorClient.create(sessionUrl, null, CreateMode.EPHEMERAL, true);
            }
        } catch (Exception e) {
            log.info("WebSocketSession registry", e);
        }
    }

    @Override
    public List<String> getInstances(String uri, String userId) {
        String urlPrefix = this.sessionConfigBean.getUrlPrefix();
        if (urlPrefix.endsWith(CommonConstants.VIRGULE)) {
            urlPrefix = urlPrefix.substring(0, urlPrefix.length() - 1);
        }

        List<String> instances = new ArrayList<>();
        if (!uri.startsWith(CommonConstants.VIRGULE)) {
            uri = CommonConstants.VIRGULE + uri;
        }

        if (StringUtils.hasText(uri)) {
            String userIdPath = urlPrefix + uri + CommonConstants.VIRGULE + userId.replaceAll(CommonConstants.VIRGULE, "-");
            instances = this.curatorClient.getChildren(userIdPath, false);
        } else {
            List<String> uries = this.curatorClient.getChildren(urlPrefix, false);
            for (String uriItem : uries) {
                String userIdPath = urlPrefix + CommonConstants.VIRGULE + uriItem +
                        CommonConstants.VIRGULE + userId.replaceAll(CommonConstants.VIRGULE, "-");
                List<String> tempInstances = this.curatorClient.getChildren(userIdPath, false);
                instances.addAll(tempInstances);
            }
        }
        return instances;
    }

    @Override
    public List<UserSessionInfo> getUserInstanceInfoes() {
        List<UserSessionInfo> userSessionInfoes = new LinkedList<>();
        String urlPrefix = this.sessionConfigBean.getUrlPrefix();
        if (urlPrefix.endsWith(CommonConstants.VIRGULE)) {
            urlPrefix = urlPrefix.substring(0, urlPrefix.length() - 1);
        }

        List<String> uries = this.curatorClient.getChildren(urlPrefix, false);
        for (String uri : uries) {
            String uriPath = urlPrefix + CommonConstants.VIRGULE + uri;
            List<String> userIdes = this.curatorClient.getChildren(uriPath, false);
            for (String userId : userIdes) {
                String userNamePath = uriPath + CommonConstants.VIRGULE + userId.replaceAll(CommonConstants.VIRGULE, "-");
                List<String> instances = this.curatorClient.getChildren(userNamePath, false);
                userSessionInfoes.add(new UserSessionInfo(uri, userId, instances));
            }
        }
        return userSessionInfoes;
    }

    @Override
    public void cancel(WebSocketSession session) {
        try {
            String sessionUrl = this.getSessionPath(session);
            if (this.curatorClient.exist(sessionUrl)) {
                this.curatorClient.delete(sessionUrl);
            }
        } catch (Exception e) {
            log.info("WebSocketSession cancel", e);
        }
    }

    private String getSessionPath(WebSocketSession session) {
        String sessionPath = this.sessionConfigBean.getUrlPrefix();
        if (sessionPath.endsWith(CommonConstants.VIRGULE)) {
            sessionPath = sessionPath.substring(0, sessionPath.length() - 1);
        }

        String uri = session.getUri().getPath();
        if (!uri.startsWith(CommonConstants.VIRGULE)) {
            uri = CommonConstants.VIRGULE + uri;
        }
        return sessionPath + uri + CommonConstants.VIRGULE + this.getUserName(session).replaceAll(CommonConstants.VIRGULE, "-")
                + CommonConstants.VIRGULE + this.instanceConfigBean.getCurrentInstanceId().replaceAll(CommonConstants.VIRGULE, "-");
    }

    private String getUserName(WebSocketSession session) {
        Principal principal = session.getPrincipal();
        if (Objects.isNull(principal)) {
            throw new WebsocketException("no user principal");
        }
        return principal.getName();
    }

}