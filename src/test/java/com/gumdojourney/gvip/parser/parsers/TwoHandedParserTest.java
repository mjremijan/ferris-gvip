package com.gumdojourney.gvip.parser.parsers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.gumdojourney.gvip.model.Metadata;

/**
 *
 * @author Michael
 */
public class TwoHandedParserTest {

    public TwoHandedParserTest() {

    }

    @Test
    public void test1() {
        TwoHandedParser parser = new TwoHandedParser();
        String filename = "Two-handed sword form #11 [쌍수검법 11번] commentary - Haidong Gumdo (2025-11-25 05.18).mp4";
        Optional<Metadata> mdOpt = parser.parse(null, filename);
        assertTrue(mdOpt.isPresent());

        Metadata md = mdOpt.get();
        assertEquals(filename, md.getSourceFilename());
        assertEquals("Two-Handed #11 Sword Form (2025-11) Commentary - Haidong Gumdo", md.getTitle());
        assertEquals("Two-handed sword form #11 [쌍수검법 11번] commentary - Haidong Gumdo (2025-11-25 05.18)", md.getDescription());
        assertEquals("2025-11-25", md.getRecordingDate().toString());
        assertEquals(2, md.getPlaylists().size());
        assertEquals("Two-Handed #11 Sword Form", md.getPlaylists().get(0));
        assertEquals("Commentary", md.getPlaylists().get(1));   
        assertEquals(false, md.isMadeForKids());
        assertEquals(18, md.getTags().size());
    }

    @Test
    public void test2() {
        TwoHandedParser parser = new TwoHandedParser();
        String filename = "Two-handed sword form #11 [쌍수검법 11번] - Haidong Gumdo (2025-11-25 05.18).mp4";
        Optional<Metadata> mdOpt = parser.parse(null, filename);
        assertTrue(mdOpt.isPresent());

        Metadata md = mdOpt.get();
        assertEquals(filename, md.getSourceFilename());
        assertEquals("Two-Handed #11 Sword Form (2025-11) - Haidong Gumdo", md.getTitle());
        assertEquals("Two-handed sword form #11 [쌍수검법 11번] - Haidong Gumdo (2025-11-25 05.18)", md.getDescription());
        assertEquals("2025-11-25", md.getRecordingDate().toString());
        assertEquals(1, md.getPlaylists().size());
        assertEquals("Two-Handed #11 Sword Form", md.getPlaylists().get(0));
        assertEquals(false, md.isMadeForKids());
        assertEquals(18, md.getTags().size());
    }

    @Test
    public void test3() {
        TwoHandedParser parser = new TwoHandedParser();
        String filename = "Two-handed sword form #6 [쌍수검법 6번] commentary - Haidong Gumdo (2025-11-25 05.18).mp4";
        Optional<Metadata> mdOpt = parser.parse(null, filename);
        assertTrue(mdOpt.isPresent());

        Metadata md = mdOpt.get();
        assertEquals(filename, md.getSourceFilename());
        assertEquals("Two-Handed #6 Sword Form (2025-11) Commentary - Haidong Gumdo", md.getTitle());
        assertEquals("Two-handed sword form #6 [쌍수검법 6번] commentary - Haidong Gumdo (2025-11-25 05.18)", md.getDescription());
        assertEquals("2025-11-25", md.getRecordingDate().toString());
        assertEquals(2, md.getPlaylists().size());
        assertEquals("Two-Handed #6 Sword Form", md.getPlaylists().get(0));
        assertEquals("Commentary", md.getPlaylists().get(1));   
        assertEquals(false, md.isMadeForKids());
        assertEquals(18, md.getTags().size());
    }
}