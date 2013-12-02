package ch.javaee.basicMvc.domain;


import ch.javaee.basicMvc.utility.TypeActivationEnum;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Date;

@Entity(name="security_code")
public class
        SecurityCode {

    @Id@GeneratedValue
    private Long id;
    @OneToOne
    private User user;
    private String code;
    private Date timeRequest;
    private TypeActivationEnum typeActivationEnum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String secureString) {
        this.code = secureString;
    }

    public Date getTimeRequest() {
        return timeRequest;
    }

    public void setTimeRequest(Date timeRequest) {
        this.timeRequest = timeRequest;
    }

    public TypeActivationEnum getTypeActivationEnum() {
        return typeActivationEnum;
    }

    public void setTypeActivationEnum(TypeActivationEnum typeActivationEnum) {
        this.typeActivationEnum = typeActivationEnum;
    }
}
