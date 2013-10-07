package org.moskito.control.core.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Muter Test. Time dependent test.
 *
 * @author vkazhdan
 * @since 07.10.2013
 */
public class MuterTest {

    @Test
    public void testMute() throws InterruptedException{
        Muter muter = new Muter();
        long delay = 100;

        // Verify before muting
        assertFalse(muter.isMuted());
        assertEquals(0, muter.getRemainedTime());

        muter.mute(delay);

        // Verify after muting
        assertTrue(muter.isMuted());
        assertTrue(muter.getRemainedTime() > 0);

        // Sleep and verify after delay
        Thread.sleep(delay + 100);
        assertFalse(muter.isMuted());
        assertEquals(0, muter.getRemainedTime());
    }

    @Test
    public void testDoubleMute() throws InterruptedException{
        Muter muter = new Muter();
        long delay1 = 100;
        long delay2 = 200;

        muter.mute(delay1);
        assertTrue(muter.isMuted());
        assertTrue(muter.getRemainedTime() <= delay1);

        muter.mute(delay2);
        assertTrue(muter.isMuted());
        assertTrue(muter.getRemainedTime() > delay1);

        // Sleep and verify after delay
        Thread.sleep(delay2 + 100);
        assertFalse(muter.isMuted());
        assertEquals(0, muter.getRemainedTime());
    }

    @Test
    public void testUnmute() throws InterruptedException{
        Muter muter = new Muter();
        long delay = 100;

        muter.mute(delay);
        muter.unmute();

        // Verify
        assertFalse(muter.isMuted());
        assertEquals(0, muter.getRemainedTime());
    }
}
