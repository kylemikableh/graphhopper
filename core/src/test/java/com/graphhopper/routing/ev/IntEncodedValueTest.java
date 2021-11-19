package com.graphhopper.routing.ev;

import com.graphhopper.storage.IntsRef;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IntEncodedValueTest {

    @Test
    public void testInvalidReverseAccess() {
        IntEncodedValue prop = new SignedIntEncodedValue("test", 10, 0, false);
        prop.init(new EncodedValue.InitializerConfig());
        try {
            prop.setInt(true, new IntsRef(1), -1);
            fail();
        } catch (Exception ex) {
        }
    }

    @Test
    public void testDirectedValue() {
        IntEncodedValue prop = new SignedIntEncodedValue("test", 10, 0, true);
        prop.init(new EncodedValue.InitializerConfig());
        IntsRef ref = new IntsRef(1);
        prop.setInt(false, ref, 10);
        prop.setInt(true, ref, 20);
        assertEquals(10, prop.getInt(false, ref));
        assertEquals(20, prop.getInt(true, ref));
    }

    @Test
    public void multiIntsUsage() {
        IntEncodedValue prop = new SignedIntEncodedValue("test", 31, 0, true);
        prop.init(new EncodedValue.InitializerConfig());
        IntsRef ref = new IntsRef(2);
        prop.setInt(false, ref, 10);
        prop.setInt(true, ref, 20);
        assertEquals(10, prop.getInt(false, ref));
        assertEquals(20, prop.getInt(true, ref));
    }

    @Test
    public void padding() {
        IntEncodedValue prop = new SignedIntEncodedValue("test", 30, 0, true);
        prop.init(new EncodedValue.InitializerConfig());
        IntsRef ref = new IntsRef(2);
        prop.setInt(false, ref, 10);
        prop.setInt(true, ref, 20);
        assertEquals(10, prop.getInt(false, ref));
        assertEquals(20, prop.getInt(true, ref));
    }

    @Test
    public void testSignedInt() {
        IntEncodedValue prop = new SignedIntEncodedValue("test", 31, -5, false);
        EncodedValue.InitializerConfig config = new EncodedValue.InitializerConfig();
        prop.init(config);

        IntsRef ref = new IntsRef(1);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            prop.setInt(false, ref, Integer.MAX_VALUE);
        });
        assertTrue(exception.getMessage().contains("test value too large for encoding"), exception.getMessage());

        prop.setInt(false, ref, -5);
        assertEquals(-5, prop.getInt(false, ref));
    }

    @Test
    public void testSignedInt2() {
        IntEncodedValue prop = new SignedIntEncodedValue("test", 31, 0, false);
        EncodedValue.InitializerConfig config = new EncodedValue.InitializerConfig();
        prop.init(config);

        IntsRef ref = new IntsRef(1);
        prop.setInt(false, ref, Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, prop.getInt(false, ref));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            prop.setInt(false, ref, -5);
        });
        assertTrue(exception.getMessage().contains("test value too small for encoding"), exception.getMessage());
    }
}