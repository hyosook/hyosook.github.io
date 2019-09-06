package kr.co.apexsoft.jpaboot._support;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Class Description
 * DB data 'Y/N' 과 객체 boolean 컨버터
 * 글로벌 설정
 *
 * @author 김혜연
 * @since 2019-07-08
 */
@Converter(autoApply = true)
public class BooleanToYNConverter implements AttributeConverter<Boolean, Character> {
    @Override
    public Character convertToDatabaseColumn(Boolean attribute) {
        return (attribute != null && attribute) ? 'Y' : 'N';
    }

    @Override
    public Boolean convertToEntityAttribute(Character dbData) {
        return dbData == 'Y';
    }
}
