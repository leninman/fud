/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bod.securefactorbod.fud.daos;

import com.bod.securefactorbod.fud.models.Customermachine;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author lmanrique
 */
public interface IMachineDao extends JpaRepository<Customermachine,Long> {
    
    Customermachine findMachineByFingerprint(String fingerprint);
    
    Customermachine findMachineByFingerprintAndCuscunuser(String fingerprint, String cuscunuser);
    
    List<Customermachine> findMachineBycuscunuser(String cuscunuser);
    
    @Query(countQuery="select count(*) from CUSTOMER_MACHINE u where u.cuscunuser=?1",nativeQuery=true)
     Page<Customermachine> findNumberMachinesBycuscunuser(String cuscunuser,Pageable pageable);
     
//     @Query(value="select * from CUSTOMER_MACHINE u where u.cuscunuser=?1",nativeQuery=true,
//             dateQuery=)
}
