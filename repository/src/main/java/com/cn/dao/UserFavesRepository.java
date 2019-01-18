package com.cn.dao;

import com.cn.entity.UserFaves;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFavesRepository extends JpaRepository<UserFaves,String> {

    void deleteUserFavesByUserIdAndAndEssayIdAndFaveType(String userId,String essayId,byte type);
}
