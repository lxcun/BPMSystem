package com.atw.bpmsystem.Repositories;

import com.atw.bpmsystem.Entities.DetailPurchase;
import com.atw.bpmsystem.Entities.Storage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetailPurchaseRepository extends JpaRepository<DetailPurchase,Integer> {
  //  List<DetailPurchase> findByStorage(Storage storage);
}
