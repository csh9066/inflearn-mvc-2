package hello.typeconverter.type;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class IpPort {
    private String ip;
    private String port;
}
