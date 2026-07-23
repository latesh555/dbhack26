package com.regintel.ai.common.dto;

import com.regintel.ai.common.dto.StepDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StepDetailsRepository extends JpaRepository<StepDetails, Long> {

    List<StepDetails> findByReqId(Long reqId);

}