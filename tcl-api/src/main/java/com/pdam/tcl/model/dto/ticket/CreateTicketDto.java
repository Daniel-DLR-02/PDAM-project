package com.pdam.tcl.model.dto.ticket;


import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CreateTicketDto {
    public UUID sessionUuid;
    private int row;
    private int column;
}
