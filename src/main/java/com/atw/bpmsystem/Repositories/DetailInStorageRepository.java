package com.atw.bpmsystem.Repositories;

import com.atw.bpmsystem.Entities.DetailInStorage;
import com.atw.bpmsystem.Entities.InStorage;
import com.atw.bpmsystem.Entities.Storage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetailInStorageRepository extends JpaRepository<DetailInStorage,Integer> {
//    List<DetailInStorage> findByStorage(Storage storage);
//    DetailInStorage findByBatchNumberAndStorage(String batchNumber,Storage storage);
   // List<DetailInStorage> findByNeedManAndInStorage(String needMan, InStorage inStorage);
    //List<DetailInStorage> findByNeedMan(String needMan);
}
