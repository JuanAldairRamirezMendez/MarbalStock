package controlador;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SecurityUtilTest {

    @Test
    void sha256Produces64HexChars() {
        String h = SecurityUtil.sha256Hex("abc");
        assertNotNull(h);
        assertEquals(64, h.length());
        // Known SHA-256 for "abc"
        assertEquals("ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad", h);
    }
}
