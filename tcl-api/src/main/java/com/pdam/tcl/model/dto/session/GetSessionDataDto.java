package com.pdam.tcl.model.dto.session;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class GetSessionDataDto {
    private UUID sessionId;
    private String filmTitle;
    private LocalDateTime sessionDate;
    private String hallName;
}