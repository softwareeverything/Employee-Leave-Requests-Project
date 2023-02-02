package com.ydursun.demo.repository;

import com.ydursun.demo.model.entity.LeaveRequestEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequestEntity, Long> {
    @Query(value = "SELECT SUM(lr.days_off) FROM leave_request lr WHERE lr.requester_id = :user_id " +
            " AND lr.status = 'APPROVED'" +
            " AND lr.start_date > DATE(NOW()-INTERVAL 1 YEAR)"
            , nativeQuery = true)
    Optional<Integer> sumApprovedDaysOffSinceLastYearByRequesterId(@Param("user_id") Long userId);

    @Query(value = "SELECT * FROM leave_request lr WHERE lr.requester_id = (SELECT id FROM user WHERE email = :email)" +
            " AND lr.status = 'WAITING'" +
            " AND lr.end_date >= DATE(NOW())" +
            " ORDER BY lr.created_at DESC"
            , nativeQuery = true)
    List<LeaveRequestEntity> findWaitingLeaveRequestEntityListByRequesterEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM leave_request lr WHERE lr.requester_id = (SELECT id FROM user WHERE email = :email)" +
            " ORDER BY lr.end_date DESC"
            , countQuery = "SELECT COUNT(*) FROM leave_request lr WHERE lr.requester_id = (SELECT id FROM user WHERE email = :email)"
            , nativeQuery = true)
    Page<LeaveRequestEntity> findAllLeaveRequestEntityListByRequesterEmail(@Param("email") String email, Pageable pageable);

    @Query(value = "SELECT * FROM leave_request lr ORDER BY lr.created_at DESC"
            , countQuery = "SELECT COUNT(*) FROM leave_request lr"
            , nativeQuery = true)
    Page<LeaveRequestEntity> findAllLeaveRequestEntityList(Pageable pageable);

}
