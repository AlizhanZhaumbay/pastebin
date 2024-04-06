package org.example.paste;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v1/pastebin")
@RequiredArgsConstructor
public class PasteController {

    private final PasteService pasteService;

    @PostMapping
    public ResponseEntity<String> createPaste(@RequestBody PasteRequest pasteRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(pasteService.savePaste(pasteRequest));
    }

    @GetMapping("/{shortLink}")
    public ResponseEntity<String> getPaste(@PathVariable("shortLink") String hash){
        return ResponseEntity.ok(pasteService.loadPaste(hash));
    }

    @GetMapping("/exists/{shortLink}")
    public ResponseEntity<String> exists(@PathVariable("shortLink") String hash){
        return ResponseEntity.ok(pasteService.pasteExists(hash));
    }
}
