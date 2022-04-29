package shu.xyj.secdkill.pojo;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1l;

    private Long id;

    private String nickname;

    private String  password;

    private String salt;

    private String head;

    private Date registerDate;

    private Date lastLoginDate;

    private Integer loginCount;
}
