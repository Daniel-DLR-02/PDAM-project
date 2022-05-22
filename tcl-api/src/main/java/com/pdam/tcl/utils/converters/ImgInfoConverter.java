package com.pdam.tcl.utils.converters;

import com.pdam.tcl.model.img.ImgurImageInfo;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ImgInfoConverter implements AttributeConverter<ImgurImageInfo,String> {

    private static final String SEPARATOR = ", ";

    @Override
    public String convertToDatabaseColumn(ImgurImageInfo attribute) {
        if (attribute == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        if (attribute.getLink() != null && !attribute.getLink()
                .isEmpty()) {
            sb.append(attribute.getLink());
            sb.append(SEPARATOR);
        }

        if (attribute.getDeletehash() != null
                && !attribute.getDeletehash().isEmpty()) {
            sb.append(attribute.getDeletehash());
        }

        return sb.toString();
    }

    @Override
    public ImgurImageInfo convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }

        String[] pieces = dbData.split(SEPARATOR);

        if (pieces == null || pieces.length == 0) {
            return null;
        }

        ImgurImageInfo imgInfo = new ImgurImageInfo();
        String firstPiece = !pieces[0].isEmpty() ? pieces[0] : null;
        if (dbData.contains(SEPARATOR)) {
            imgInfo.setLink(firstPiece);

            if (pieces.length >= 2 && pieces[1] != null
                    && !pieces[1].isEmpty()) {
                imgInfo.setDeletehash(pieces[1]);
            }
        } else {
            imgInfo.setDeletehash(firstPiece);
        }

        return imgInfo;
    }
}
