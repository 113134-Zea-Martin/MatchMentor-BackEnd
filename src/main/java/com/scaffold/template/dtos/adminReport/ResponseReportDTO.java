package com.scaffold.template.dtos.adminReport;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseReportDTO {
    private Long quantity;
    private String name;
}
