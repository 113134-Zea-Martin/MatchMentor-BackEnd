package com.scaffold.template.dtos;

import lombok.Data;

@Data
public class MercadoPagoNotificationDTO {
    private String action;
    private String api_version;
    private DataDTO data;
    private String date_created;
    private Long id;
    private boolean live_mode;
    private String type;
    private String user_id;
    private String resource;  // Añadido campo resource


    @Data
    public static class DataDTO {
        private String id; // Este es el payment_id
    }
}
