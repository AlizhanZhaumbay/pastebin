package org.example.paste_analytics;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/paste-info")
@RequiredArgsConstructor
public class PasteInfoController {

    private final PasteInfoService pasteInfoService;

    @GetMapping("/{pasteShortLink}")
    public ResponseEntity<PasteInfoDTO> getPasteInfo(@PathVariable("pasteShortLink") String pasteShortLink){
        return ResponseEntity.ok(pasteInfoService.loadPasteInfo(pasteShortLink));
    }

    @GetMapping
    public ResponseEntity<List<String>> getAllPasteInfoShortLinks(){
        return ResponseEntity.ok(pasteInfoService.loadAllPasteInfoShortLinks());
    }

    @PostMapping("/{pasteShortLink}/visit")
    public ResponseEntity<Integer> visitPasteInfo(@PathVariable("pasteShortLink") String pasteShortLink){
        pasteInfoService.increaseVisitedCounter(pasteShortLink);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Integer> createPasteInfo(@RequestBody PasteInfoRequest pasteInfoRequest){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pasteInfoService.savePasteInfo(pasteInfoRequest));
    }

    @DeleteMapping("/{pasteShortLink}")
    public ResponseEntity<Integer> deletePasteInfo(@PathVariable("pasteShortLink") String pasteShortLink){
        pasteInfoService.deletePasteInfo(pasteShortLink);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
