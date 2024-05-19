package io.cc.config.client.value;

import java.lang.reflect.Field;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * spring value
 */
@Data
@AllArgsConstructor
public class SpringValue {
    private Object bean;
    private String beanName;
    private String key;
    private String placeholder;
    private Field  field;
}
