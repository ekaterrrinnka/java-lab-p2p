package lab.p2p.controller;

import lab.p2p.service.DataImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ImportController {

    private final DataImportService dataImportService;

    public ImportController(DataImportService dataImportService) {
        this.dataImportService = dataImportService;
    }

    @PostMapping("/import")
    public ResponseEntity<Map<String, Integer>> runImport() {
        int imported = dataImportService.importFromExternal();
        return ResponseEntity.ok(Map.of("imported", imported));
    }
}
