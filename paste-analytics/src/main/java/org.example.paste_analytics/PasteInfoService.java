package org.example.paste_analytics;

import lombok.RequiredArgsConstructor;
import org.example.validator.ObjectValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PasteInfoService {
    private final PasteInfoRepository pasteInfoRepository;
    private final ObjectValidator<PasteInfoRequest> objectValidator;

    public Integer savePasteInfo(PasteInfoRequest pasteInfoRequest){
        objectValidator.validate(pasteInfoRequest);
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
        increaseVisitedCounter(pasteShortLink);
        PasteInfo pasteInfo = pasteInfoRepository.findByPasteShortLink(pasteShortLink);
        return PasteInfoDTOFactory.convertToDTO(pasteInfo);
    }

    public List<String> loadAllPasteInfoShortLinks() {
        return pasteInfoRepository.getAllPasteShortLinks();
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

    public void deletePasteInfos(List<String> pasteShortLinks){
        pasteInfoRepository.deleteAllInBatchByPasteShortLinks(pasteShortLinks);
    }

    private void checkPasteInfoExists(String pasteShortLink) {
        if(!pasteInfoRepository.existsByPasteShortLink(pasteShortLink))
            throw new PasteInfoNotFoundException(pasteShortLink);
    }
}
