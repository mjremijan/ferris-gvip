package com.gumdojourney.gvip.parser.parsers;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gumdojourney.gvip.model.Metadata;
import com.gumdojourney.gvip.parser.FilenameParser;

public class TwoHandedParser implements FilenameParser {
    private static final Pattern PAT = Pattern.compile("Two-handed .*#(\\d+) .*\\((\\d{4}-\\d{2}-\\d{2}) .*\\)\\.mp4", Pattern.CASE_INSENSITIVE);
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public Optional<Metadata> parse(Path filePath, String filename) {
        Matcher m = PAT.matcher(filename);
        if (!m.find()) return Optional.empty();
        String num = m.group(1);
        String date = m.group(2);

        Metadata md = new Metadata();
        md.setSourceFilename(filename);
        md.setTitle(String.format("Two-Handed #%s (%s) - Haidong Gumdo", num, date.substring(0,7)));
        md.setDescription(filename);
        md.setRecordingDate(LocalDate.parse(date, DATE));

        LinkedList<String> playlists = new LinkedList<>();
        playlists.add(String.format("Two-Handed #%s Sword Form", num));
        if (filename.toLowerCase().contains("commentary")) {
            playlists.add("Commentary");
        }
        md.setPlaylists(playlists);

        md.setTags(Arrays.asList());
        md.setMadeForKids(false);
        return Optional.of(md);
    }
}
