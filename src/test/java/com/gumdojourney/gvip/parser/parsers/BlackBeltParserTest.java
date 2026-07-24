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
public class BlackBeltParserTest {

    public BlackBeltParserTest() {

    }

    @Test
    public void testParseBlackBelt1() {
        BlackBeltParser parser = new BlackBeltParser();
        String filename = "Black belt 1st Dan 2nd self-defense form [유단자 1단 두번째 격검] commentary - Haidong Gumdo (2026-04-05 14.15).mp4";
        Optional<Metadata> mdOpt = parser.parse(null, filename);
        assertTrue(mdOpt.isPresent());

        Metadata md = mdOpt.get();
        assertEquals(filename, md.getSourceFilename());
        assertEquals("1st Dan 2nd Self-Defense (2026-04) Commentary - Haidong Gumdo", md.getTitle());
        assertEquals("Black belt 1st Dan 2nd self-defense form [유단자 1단 두번째 격검] commentary - Haidong Gumdo (2026-04-05 14.15)", md.getDescription());
        assertEquals("2026-04-05", md.getRecordingDate().toString());
        assertEquals(2, md.getPlaylists().size());
        assertEquals("1st Dan 2nd Self-Defense", md.getPlaylists().get(0));
        assertEquals("Commentary", md.getPlaylists().get(1));   
        assertEquals(false, md.isMadeForKids());
        assertEquals(18, md.getTags().size());
    }

    @Test
    public void testParseBlackBelt2() {
        BlackBeltParser parser = new BlackBeltParser();
        String filename = "Black belt 1st Dan 2nd self-defense form [유단자 1단 두번째 격검] - Haidong Gumdo (2026-04-05 14.15).mp4";
        Optional<Metadata> mdOpt = parser.parse(null, filename);
        assertTrue(mdOpt.isPresent());

        Metadata md = mdOpt.get();
        assertEquals(filename, md.getSourceFilename());
        assertEquals("1st Dan 2nd Self-Defense (2026-04) - Haidong Gumdo", md.getTitle());
        assertEquals("Black belt 1st Dan 2nd self-defense form [유단자 1단 두번째 격검] - Haidong Gumdo (2026-04-05 14.15)", md.getDescription());
        assertEquals("2026-04-05", md.getRecordingDate().toString());
        assertEquals(1, md.getPlaylists().size());
        assertEquals("1st Dan 2nd Self-Defense", md.getPlaylists().get(0));
        assertEquals(false, md.isMadeForKids());
        assertEquals(18, md.getTags().size());
    }

    @Test
    public void testParseBlackBelt3() {
        BlackBeltParser parser = new BlackBeltParser();
        String filename = "Black belt 1st Dan 1st self-defense form [유단자 1단 두번째 격검] - Haidong Gumdo (2026-04-05 14.15).mp4";
        Optional<Metadata> mdOpt = parser.parse(null, filename);
        assertTrue(mdOpt.isPresent());

        Metadata md = mdOpt.get();
        assertEquals(filename, md.getSourceFilename());
        assertEquals("1st Dan 1st Self-Defense (2026-04) - Haidong Gumdo", md.getTitle());
        assertEquals("Black belt 1st Dan 1st self-defense form [유단자 1단 두번째 격검] - Haidong Gumdo (2026-04-05 14.15)", md.getDescription());
        assertEquals("2026-04-05", md.getRecordingDate().toString());
        assertEquals(1, md.getPlaylists().size());
        assertEquals("1st Dan 1st Self-Defense", md.getPlaylists().get(0));
        assertEquals(false, md.isMadeForKids());
        assertEquals(18, md.getTags().size());
    }

    @Test
    public void testParseBlackBelt4() {
        BlackBeltParser parser = new BlackBeltParser();
        String filename = "Black belt 2nd Dan 3rd self-defense form [유단자 1단 두번째 격검] - Haidong Gumdo (2026-04-05 14.15).mp4";
        Optional<Metadata> mdOpt = parser.parse(null, filename);
        assertTrue(mdOpt.isPresent());

        Metadata md = mdOpt.get();
        assertEquals(filename, md.getSourceFilename());
        assertEquals("2nd Dan 3rd Self-Defense (2026-04) - Haidong Gumdo", md.getTitle());
        assertEquals("Black belt 2nd Dan 3rd self-defense form [유단자 1단 두번째 격검] - Haidong Gumdo (2026-04-05 14.15)", md.getDescription());
        assertEquals("2026-04-05", md.getRecordingDate().toString());
        assertEquals(1, md.getPlaylists().size());
        assertEquals("2nd Dan 3rd Self-Defense", md.getPlaylists().get(0));
        assertEquals(false, md.isMadeForKids());
        assertEquals(18, md.getTags().size());
    }


}