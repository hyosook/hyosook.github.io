package kr.co.apexsoft.jpaboot._support;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class Description
 *
 * @author ±èÇý¿¬
 * @since 2019-07-12
 */
@Component
public class MapperUtils {
    static ModelMapper modelMapper;

    public MapperUtils(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);
    }

    public static <D> D convert(Object source, Class<D> classLiteral) {
        return modelMapper.map(source, classLiteral);
    }

    public static <D, E> List<D> convert(Collection<E> sourceList, Class<D> classLiteral) {
        return sourceList.stream()
                .map(source -> modelMapper.map(source, classLiteral))
                .collect(Collectors.toList());
    }

    public static <D, E> Page<D> convert(Page<E> sourceList, Class<D> classLiteral, Pageable pageable) {
        List<D> list = sourceList.getContent().stream()
                .map(source -> modelMapper.map(source, classLiteral))
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, sourceList.getTotalElements());
    }
}
