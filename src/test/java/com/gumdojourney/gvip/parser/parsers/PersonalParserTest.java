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
public class PersonalParserTest {

    public PersonalParserTest() {

    }

    @Test
    public void test1() {
        PersonalParser parser = new PersonalParser();
        String filename = "Personal 1st self-defense form [개인 첫번째 격검] commentary - Haidong Gumdo (2026-07-09 05.27).mp4";
        Optional<Metadata> mdOpt = parser.parse(null, filename);
        assertTrue(mdOpt.isPresent());

        Metadata md = mdOpt.get();
        assertEquals(filename, md.getSourceFilename());
        assertEquals("Personal 1st Self-Defense (2026-07) Commentary - Haidong Gumdo", md.getTitle());
        assertEquals("Personal 1st self-defense form [개인 첫번째 격검] commentary - Haidong Gumdo (2026-07-09 05.27)", md.getDescription());
        assertEquals("2026-07-09", md.getRecordingDate().toString());
        assertEquals(2, md.getPlaylists().size());
        assertEquals("Personal 1st Self-Defense", md.getPlaylists().get(0));
        assertEquals("Commentary", md.getPlaylists().get(1));   
        assertEquals(false, md.isMadeForKids());
        assertEquals(18, md.getTags().size());
    }

    @Test
    public void test2() {
        PersonalParser parser = new PersonalParser();
        String filename = "Personal 1st self-defense form [개인 첫번째 격검] - Haidong Gumdo (2026-07-09 05.27).mp4";
        Optional<Metadata> mdOpt = parser.parse(null, filename);
        assertTrue(mdOpt.isPresent());

        Metadata md = mdOpt.get();
        assertEquals(filename, md.getSourceFilename());
        assertEquals("Personal 1st Self-Defense (2026-07) - Haidong Gumdo", md.getTitle());
        assertEquals("Personal 1st self-defense form [개인 첫번째 격검] - Haidong Gumdo (2026-07-09 05.27)", md.getDescription());
        assertEquals("2026-07-09", md.getRecordingDate().toString());
        assertEquals(1, md.getPlaylists().size());
        assertEquals("Personal 1st Self-Defense", md.getPlaylists().get(0));
        assertEquals(false, md.isMadeForKids());
        assertEquals(18, md.getTags().size());
    }


    @Test
    public void test3() {
        PersonalParser parser = new PersonalParser();
        String filename = "Personal 3rd self-defense form [개인 첫번째 격검] commentary - Haidong Gumdo (2026-07-09 05.27).mp4";
        Optional<Metadata> mdOpt = parser.parse(null, filename);
        assertTrue(mdOpt.isPresent());

        Metadata md = mdOpt.get();
        assertEquals(filename, md.getSourceFilename());
        assertEquals("Personal 3rd Self-Defense (2026-07) Commentary - Haidong Gumdo", md.getTitle());
        assertEquals("Personal 3rd self-defense form [개인 첫번째 격검] commentary - Haidong Gumdo (2026-07-09 05.27)", md.getDescription());
        assertEquals("2026-07-09", md.getRecordingDate().toString());
        assertEquals(2, md.getPlaylists().size());
        assertEquals("Personal 3rd Self-Defense", md.getPlaylists().get(0));
        assertEquals("Commentary", md.getPlaylists().get(1));
        assertEquals(false, md.isMadeForKids());
        assertEquals(18, md.getTags().size());
    }


}