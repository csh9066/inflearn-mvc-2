package hello.typeconverter.converter;

import hello.typeconverter.type.IpPort;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConverterTest {

    @Test
    void stringToInteger() {
        StringToIntegerConverter converter = new StringToIntegerConverter();
        Integer result = converter.convert("10");
        assertThat(result).isEqualTo(10);
    }

    @Test
    void integerToString() {
        IntegerToStringConverter converter = new IntegerToStringConverter();
        String result = converter.convert(10);
        assertThat(result).isEqualTo("10");
    }

    @Test
    public void StringToIpPort() throws Exception {
        //given
        String ip = "127.0.0.1";
        String port = "8080";
        String stringIpPort = ip + ":" + port;
        //when
        StringToIpPortConverter stringToIpPortConverter = new StringToIpPortConverter();
        IpPort ipPort = stringToIpPortConverter.convert(stringIpPort);
        //then
        assertThat(ipPort.getIp()).isEqualTo(ip);
        assertThat(ipPort.getPort()).isEqualTo(port);
    }

    @Test
    public void IpPortToString() throws Exception {
        //given
        String ip = "127.0.0.1";
        String port = "8080";
        IpPort ipPort = new IpPort(ip, port);
        //when
        IpPortToStringConverter ipPortToStringConverter = new IpPortToStringConverter();
        String result = ipPortToStringConverter.convert(ipPort);
        //then
        assertThat(result).isEqualTo(ip + ":" + port);
    }


}