/*
 * Copyright (C) 2011 Alexey Matveev <mvaleksej@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.matveev.pomodoro4nb.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 
 * @author Alexey Matvey
 */
public class Properties {
    
    private final Map<Property<?>, Object> holder;
    private final List<Properties> elements;
    private final List<PropertyListener> listeners;
    
    public Properties() {
        holder = new HashMap<Property<?>, Object>();
        elements = new CopyOnWriteArrayList<Properties>();
        listeners = new CopyOnWriteArrayList<PropertyListener>();
    }
    
    public Set<Property<?>> getProperties() {
        return holder.keySet();
    }
    
    public <T> T getProperty(Property<T> property) {
        return property.getType().cast(holder.get(property));
    }
    
    public <T> T getProperty(Property<T> property, T fallback) {
        T value = (T) holder.get(property);
        return value != null ? property.getType().cast(value) : fallback;
    }
    
    public <T, V> void setProperty(Property<T> property, V value) {
        V oldValue = (V) holder.get(property);
        if (value != oldValue) {
            holder.put(property, value);
            fire(property, oldValue, value);
        }
    }
    
    public void addElement(Properties e) {
        elements.add(e);
    }
    
    public void removeElement(Properties e) {
        elements.remove(e);
    }
    
    public List<Properties> getElements() {
        return Collections.unmodifiableList(elements);
    }
    
    protected void fire(Property<?> property, Object oldValue, Object newValue) {
        for (PropertyListener l : listeners) {
            l.propertyChange(property, oldValue, newValue);
        }
    }
    
    public void addPropertyListener(PropertyListener listener) {
        listeners.add(listener);
    }
    
    public void removePropertyListener(PropertyListener listener) {
        listeners.remove(listener);
    }
}