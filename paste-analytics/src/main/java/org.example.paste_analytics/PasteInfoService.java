package org.example.paste_analytics;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PasteInfoService {
    private final PasteInfoRepository pasteInfoRepository;

    public Integer savePasteInfo(PasteInfoRequest pasteInfoRequest){
        PasteInfo savedPasteInfo = pasteInfoRepository.save(
                PasteInfo.builder()
                        .pasteShortLink(pasteInfoRequest.pasteShortLink())
                        .category(pasteInfoRequest.category())
                        .pasteSize(pasteInfoRequest.pasteSize())
                        .build()
        );

        return savedPasteInfo.getId();
    }

    public PasteInfoDTO loadPasteInfo(String pasteShortLink){
        checkPasteInfoExists(pasteShortLink);
        PasteInfo pasteInfo = pasteInfoRepository.findByPasteShortLink(pasteShortLink);
        return new PasteInfoDTO(pasteInfo.getCategory(), pasteInfo.getVisited(), pasteInfo.getPasteSize());
    }

    public void increaseVisitedCounter(String pasteShortLink){
        checkPasteInfoExists(pasteShortLink);
        Integer id = pasteInfoRepository.getIdByPasteShortLink(pasteShortLink);

        pasteInfoRepository.increaseVisitedSequence(id);
    }

    public void deletePasteInfo(String pasteShortLink){
        checkPasteInfoExists(pasteShortLink);
        pasteInfoRepository.deleteByPasteShortLink(pasteShortLink);
    }

    private void checkPasteInfoExists(String pasteShortLink) {
        if(!pasteInfoRepository.existsByPasteShortLink(pasteShortLink))
            throw new PasteInfoNotFound();
    }
}
