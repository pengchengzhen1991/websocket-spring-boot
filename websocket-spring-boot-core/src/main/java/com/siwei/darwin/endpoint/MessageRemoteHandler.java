package com.siwei.darwin.endpoint;

import com.siwei.darwin.model.MessageSendResult;
import com.siwei.darwin.model.SendMessage;
import com.siwei.darwin.instance.RegistryInstance;

public interface MessageRemoteHandler {

    MessageSendResult sendMessage(RegistryInstance instance, SendMessage sendMessage);

}
