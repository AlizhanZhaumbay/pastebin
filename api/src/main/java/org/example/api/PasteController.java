package org.example.api;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;

@RestController
@RequestMapping("/api/v1/pastebin")
@RequiredArgsConstructor
public class PasteController {

    private final PasteService pasteService;


    @PostMapping
    public ResponseEntity<String> createPaste(@RequestBody PasteDto pasteRequest){
        return ResponseEntity.ok(pasteService.savePaste(pasteRequest));
    }

    @GetMapping("/{hash}")
    public String getPaste(@PathVariable("hash") String hash){
        return pasteService.getPasteByHash(hash);
    }
}
