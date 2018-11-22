package com.atw.bpmsystem.Repositories;

import com.atw.bpmsystem.Entities.DetailOutStorage;
import com.atw.bpmsystem.Entities.Storage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetailOutStorageRepository extends JpaRepository<DetailOutStorage,Integer> {
  //  List<DetailOutStorage> findByStorage(Storage storage);
}
