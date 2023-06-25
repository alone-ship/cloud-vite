package entitys.auth;

import lombok.Data;

@Data
public class AccountInfo {
    public String nickname;
    public String username;
    public String password;
    public String role;
}
