package com.pdam.tcl.model.dto.ticket;


import com.pdam.tcl.validation.multiple.anotations.UniqueTicketForSession;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Validated
public class CreateTicketDto {
    private UUID sessionUuid;
    private int row;
    private int column;
}
