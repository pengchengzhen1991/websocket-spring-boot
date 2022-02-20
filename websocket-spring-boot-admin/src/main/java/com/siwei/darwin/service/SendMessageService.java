package com.siwei.darwin.service;

import com.siwei.darwin.repository.message.SendMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SendMessageService {

    private final SendMessageRepository sendMessageRepository;



}
