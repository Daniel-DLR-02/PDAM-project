package com.pdam.tcl.repository;

import com.pdam.tcl.model.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    @Query(value="select t from Ticket t where t.user.id = :idUser")
    Page<Ticket> findAllByUserId(UUID idUser, Pageable pageable);

    @Query("select t from Ticket t where t.session.uuid = :idSession")
    List<Ticket> getAllTicketsFromASession(UUID idSession);
}
