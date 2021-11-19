package com.graphhopper.routing.ev;

import com.graphhopper.reader.ReaderWay;
import com.graphhopper.routing.util.CarFlagEncoder;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.storage.IntsRef;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DecimalEncodedValueTest {

    @Test
    public void testInit() {
        DecimalEncodedValue prop = new SignedDecimalEncodedValue("test", 10, 2, false);
        prop.init(new EncodedValue.InitializerConfig());
        IntsRef ref = new IntsRef(1);
        prop.setDecimal(false, ref, 10d);
        assertEquals(10d, prop.getDecimal(false, ref), 0.1);
    }

    @Test
    public void testMaxValue() {
        CarFlagEncoder carEncoder = new CarFlagEncoder(10, 0.5, 0);
        EncodingManager em = EncodingManager.create(carEncoder);
        DecimalEncodedValue carAverageSpeedEnc = em.getDecimalEncodedValue(EncodingManager.getKey(carEncoder, "average_speed"));

        ReaderWay way = new ReaderWay(1);
        way.setTag("highway", "motorway_link");
        way.setTag("maxspeed", "70 mph");
        IntsRef flags = carEncoder.handleWayTags(em.createEdgeFlags(), way, carEncoder.getAccess(way));
        assertEquals(101.5, carAverageSpeedEnc.getDecimal(true, flags), 1e-1);

        DecimalEncodedValue instance1 = new SignedDecimalEncodedValue("test1", 8, 0.5, false);
        instance1.init(new EncodedValue.InitializerConfig());
        flags = em.createEdgeFlags();
        instance1.setDecimal(false, flags, 100d);
        assertEquals(100, instance1.getDecimal(false, flags), 1e-1);
    }

    @Test
    public void testNegativeBounds() {
        DecimalEncodedValue prop = new SignedDecimalEncodedValue("test", 10, 5, false);
        prop.init(new EncodedValue.InitializerConfig());
        try {
            prop.setDecimal(false, new IntsRef(1), -1);
            assertTrue(false);
        } catch (Exception ex) {
        }
    }
}