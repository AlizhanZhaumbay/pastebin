package org.example.paste_analytics;

public class PasteInfoDTOFactory {

    public static PasteInfoDTO convertToDTO(PasteInfo pasteInfo){
        return new PasteInfoDTO(pasteInfo.getCategory(), pasteInfo.getVisited(), pasteInfo.getPasteSize());
    }
}
