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
public class BeltTestParserTest {

    public BeltTestParserTest() {

    }

    @Test
    public void test1() {
        BeltTestParser parser = new BeltTestParser();
        String filename = "Sword test montage - Haidong Gumdo Red-Blue belt test - Master Lim's Martial Arts, Fairview Heights, IL - Michael (2023-07-12 19.46).mp4";
        Optional<Metadata> mdOpt = parser.parse(null, filename);
        assertTrue(mdOpt.isPresent());

        Metadata md = mdOpt.get();
        assertEquals(filename, md.getSourceFilename());
        assertEquals("Red-Blue Belt Test (2023-07) - Haidong Gumdo", md.getTitle());
        assertEquals("Sword test montage - Haidong Gumdo Red-Blue belt test - Master Lim's Martial Arts, Fairview Heights, IL - Michael (2023-07-12 19.46)", md.getDescription());
        assertEquals("2023-07-12", md.getRecordingDate().toString());
        assertEquals(1, md.getPlaylists().size());
        assertEquals("Belt Tests", md.getPlaylists().get(0));
        assertEquals(false, md.isMadeForKids());
        assertEquals(18, md.getTags().size());
    }

    @Test
    public void test2() {
        BeltTestParser parser = new BeltTestParser();
        String filename = "Sword test montage with kicking, sparing, cucumber - Haidong Gumdo Red-Blue belt test - Master Lim's Martial Arts, Fairview Heights, IL - Michael (2023-07-12 19.46).mp4";
        Optional<Metadata> mdOpt = parser.parse(null, filename);
        assertTrue(mdOpt.isPresent());

        Metadata md = mdOpt.get();
        assertEquals(filename, md.getSourceFilename());
        assertEquals("Red-Blue Belt Test (2023-07) - Haidong Gumdo", md.getTitle());
        assertEquals("Sword test montage with kicking, sparing, cucumber - Haidong Gumdo Red-Blue belt test - Master Lim's Martial Arts, Fairview Heights, IL - Michael (2023-07-12 19.46)", md.getDescription());
        assertEquals("2023-07-12", md.getRecordingDate().toString());
        assertEquals(1, md.getPlaylists().size());
        assertEquals("Belt Tests", md.getPlaylists().get(0));
        assertEquals(false, md.isMadeForKids());
        assertEquals(18, md.getTags().size());
    }
    
}