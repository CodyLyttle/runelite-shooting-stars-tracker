package com.shootingstarstracker.ui.elements;

import java.util.function.BiConsumer;

public interface IFilterElement<T>
{
    T getValue();
    
    void setValue(T value);
    
    void setLabelTooltip(String tooltip);

    void subscribe(BiConsumer<Object, Object> callback);
}
