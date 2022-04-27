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
                        select new com.pdam.tcl.model.dto.session.GetSessionDto(
                            s.uuid,f.title,s.sessionDate,h.name,s.active,s.availableSeats
                        ) 
                        from Session s join s.film f
                            join s.hall h 
                        where f.uuid = filmUuid
                    """)
    Page<GetSessionDto> getSessionsByFilmId(UUID filmUuid, Pageable pageable);
}
