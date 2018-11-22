package com.atw.bpmsystem.Repositories;

import com.atw.bpmsystem.Entities.DetailInStorage;
import com.atw.bpmsystem.Entities.InStorage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InStorageRepository extends JpaRepository<InStorage,Integer> {
}
