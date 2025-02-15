package mr.master.edu_click.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String access_token;
//    private String password;
}

