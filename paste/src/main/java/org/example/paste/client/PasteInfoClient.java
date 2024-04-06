package org.example.paste.client;

import org.example.paste.paste_info.PasteInfoRequest;
import org.example.paste.paste_info.PasteInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "pasteInfo", url = "localhost:8085/api/v1/paste-info")
public interface PasteInfoClient {

    @GetMapping("/{shortLink}")
    PasteInfoResponse loadInfo(@PathVariable("shortLink") String shortLink);

    @PostMapping("/{shortLink}/visit")
    void visitInfo(@PathVariable("shortLink") String shortLink);

    @PostMapping
    Integer createInfo(PasteInfoRequest pasteRequest);

    @DeleteMapping("/{shortLink}")
    void deleteInfo(@PathVariable("shortLink") String shortLink);
}
