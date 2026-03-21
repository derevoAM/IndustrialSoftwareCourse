package library.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reader {

    private int id;
    private String fullName;
    private String email;
    private String phone;
    private LocalDateTime registeredAt;
}
