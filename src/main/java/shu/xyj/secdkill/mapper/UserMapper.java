package shu.xyj.secdkill.mapper;

import org.springframework.stereotype.Repository;
import shu.xyj.secdkill.pojo.User;

@Repository
public interface UserMapper{
    User selectById(String id);
}
