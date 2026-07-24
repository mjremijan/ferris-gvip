package com.gumdojourney.gvip.parser.parsers;

import com.gumdojourney.gvip.model.Metadata;
import com.gumdojourney.gvip.parser.FilenameParser;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BeltTestParser implements FilenameParser {
    private static final Pattern PAT = Pattern.compile(".*Haidong Gumdo (.+?) belt test .*\\((\\d{4}-\\d{2}-\\d{2}) .*\\)\\.mp4", Pattern.CASE_INSENSITIVE);
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public Optional<Metadata> parse(Path filePath, String filename) {
        Matcher m = PAT.matcher(filename);
        if (!m.find()) return Optional.empty();
        String belt = m.group(1).trim();
        String date = m.group(2);

        Metadata md = new Metadata();
        md.setSourceFilename(filename);
        md.setTitle(String.format("%s Belt Test (%s) - Haidong Gumdo", belt, date.substring(0,7)));
        md.setDescription(filename);
        md.setRecordingDate(LocalDate.parse(date, DATE));
        md.setPlaylists(Arrays.asList("Belt Tests"));
        md.setTags(Arrays.asList());
        md.setMadeForKids(false);
        return Optional.of(md);
    }
}
