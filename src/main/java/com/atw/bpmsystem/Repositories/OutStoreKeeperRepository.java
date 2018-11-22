package com.atw.bpmsystem.Repositories;

import com.atw.bpmsystem.Entities.OutStorage;
import com.atw.bpmsystem.Entities.OutStoreKeeper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutStoreKeeperRepository extends JpaRepository<OutStoreKeeper,Integer> {
    /**通过outStorage查询OutStoreKeeper
     * @param outStorage
     * @return
     */
    List<OutStoreKeeper>findByOutStorage(OutStorage outStorage);
}
