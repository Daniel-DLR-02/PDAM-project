package com.pdam.tcl.model.dto.session;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CreateSessionDto {

    private UUID filmUuid;
    private LocalDateTime sessionDate;
    private UUID hallUuid;
    private boolean active;
    private boolean[][] availableSeats;
}

