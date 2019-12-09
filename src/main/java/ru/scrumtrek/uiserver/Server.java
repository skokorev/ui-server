package ru.scrumtrek.uiserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.scrumtrek.uiserver.domain.Line;
import ru.scrumtrek.uiserver.time.*;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class Server {
    private AtomicInteger idInt = new AtomicInteger(0);
    private List<Line> lines = new LinkedList<>();
    @Autowired private SafeTimeGetter safeTimeGetter;

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
        String time = safeTimeGetter.getTime(TimeType.ofRepresentation(timeType));
        lines.add(new Line(idInt.incrementAndGet(), line, time));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = {"/lines"})
    public void removeAll() { lines.clear(); }
}
