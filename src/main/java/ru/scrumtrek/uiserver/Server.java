package ru.scrumtrek.uiserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.scrumtrek.uiserver.domain.Line;
import ru.scrumtrek.uiserver.time.LocalTimeGetter;
import ru.scrumtrek.uiserver.time.TimeGetter;
import ru.scrumtrek.uiserver.time.TimeGetterException;
import ru.scrumtrek.uiserver.time.TimeType;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class Server {
    private AtomicInteger idInt = new AtomicInteger(0);
    private List<Line> lines = new LinkedList<>();
    @Autowired private TimeGetter timeGetter;
    private LocalTimeGetter localTimeGetter = new LocalTimeGetter();

    @Value("${timetype}")
    private String timeType;

    @GetMapping(path = {"/lines"})
    public List<Line> getLines() {
        return lines;
    }

    @PostMapping(path = {"/lines"})
    public ResponseEntity<Void> setLines(@RequestBody String line) {
        if (lines.stream().anyMatch(l -> l.getLineChars().equals(line)))
            return ResponseEntity.badRequest().build();
        String time;
        try {
            time = timeGetter.getTime(TimeType.ofRepresentation(timeType));
        } catch (TimeGetterException e) {
            try {
                time = localTimeGetter.getTime(TimeType.ofRepresentation(timeType));
            } catch (TimeGetterException e1) {
                return ResponseEntity.badRequest().build();
            }
        }
        lines.add(new Line(idInt.incrementAndGet(), line, time));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = {"/lines"})
    public void removeAll() { lines.clear(); }
}
