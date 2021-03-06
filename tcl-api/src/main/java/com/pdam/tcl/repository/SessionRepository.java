package com.pdam.tcl.repository;

import com.pdam.tcl.model.Session;
import com.pdam.tcl.model.dto.session.GetSessionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session, UUID> {


    @Query(value = """ 
                        SELECT new com.pdam.tcl.model.dto.session.GetSessionDto(
                            s.uuid,f.uuid,f.title,s.sessionDate,h.uuid,h.name,s.active,s.availableSeats
                        ) 
                        FROM Session s JOIN Film f ON s.film.uuid = f.uuid
                        JOIN Hall h ON s.hall.uuid = h.uuid
                        WHERE s.film.uuid = :filmUuid
                        ORDER BY s.sessionDate ASC
                    """)
    Page<GetSessionDto> getSessionsByFilmId(UUID filmUuid, Pageable pageable);

    @Query(value = """ 
                        SELECT s
                        FROM Session s JOIN Film f ON s.film.uuid = f.uuid
                        JOIN Hall h ON s.hall.uuid = h.uuid
                        WHERE s.film.uuid = :filmUuid
                        ORDER BY s.sessionDate ASC
                    """)
    List<Session> getSessionsByFilmIdList(UUID filmUuid);


    @Query(value = """ 
                        SELECT new com.pdam.tcl.model.dto.session.GetSessionDto(
                            s.uuid,f.uuid,f.title,s.sessionDate,h.uuid,h.name,s.active,s.availableSeats
                        ) 
                        FROM Session s JOIN Film f ON s.film.uuid = f.uuid
                        JOIN Hall h ON s.hall.uuid = h.uuid
                        ORDER BY s.sessionDate ASC
                    """)
    Page<GetSessionDto> findAllSessions(Pageable pageable);

}

