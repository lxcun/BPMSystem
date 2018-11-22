package com.atw.bpmsystem.Repositories;

import com.atw.bpmsystem.Entities.ProgramManage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramManageRepository extends JpaRepository<ProgramManage,Integer>{
    ProgramManage findByProgramNameAndAndProgramNumber(String programName,String programNumber);
}
