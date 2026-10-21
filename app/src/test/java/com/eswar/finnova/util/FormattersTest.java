package com.eswar.finnova.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FormattersTest {

    @Test public void formatPhone() {
        assertEquals("(469) 555-0142", Formatters.formatPhone("4695550142"));
        assertEquals("123", Formatters.formatPhone("123"));
    }

    @Test public void maskPhone() {
        assertEquals("(•••) •••-0142", Formatters.maskPhone("4695550142"));
    }

    @Test public void maskEmail() {
        assertEquals("a••••••••••n@example.com", Formatters.maskEmail("alex.johnson@example.com"));
        assertEquals("a•@x.com", Formatters.maskEmail("ab@x.com"));
    }

    @Test public void money() {
        assertEquals("$4,823.15", Formatters.formatCents(482_315));
        assertEquals("$95,000", Formatters.formatDollars(95_000));
    }
}
