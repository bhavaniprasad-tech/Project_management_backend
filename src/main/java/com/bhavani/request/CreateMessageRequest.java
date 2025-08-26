package com.bhavani.request;

import lombok.Data;

@Data
public class CreateMessageRequest {

    private Long senderId;

    private String Content;

    private Long projectId;
}
