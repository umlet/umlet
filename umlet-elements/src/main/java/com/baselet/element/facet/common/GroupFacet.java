package com.baselet.element.facet.common;

import com.baselet.diagram.draw.helper.StyleException;
import com.baselet.element.facet.FirstRunKeyValueFacet;
import com.baselet.element.facet.PropertiesParserState;
import java.util.concurrent.atomic.AtomicInteger;

public class GroupFacet extends FirstRunKeyValueFacet {

   public static final GroupFacet INSTANCE = new GroupFacet();

    private GroupFacet() {}

    public static final String KEY = "group";
    public static final String VALUE_PREFIX = "group";
    private static final int KEY_START_VAL = 0;
    public static final AtomicInteger LAST_KNOWN_VAl = new AtomicInteger(KEY_START_VAL);

    public static final String nextDefaultKey() {
        return String.format("%s-%d", VALUE_PREFIX, LAST_KNOWN_VAl.getAndIncrement());
    }

    @Override
    public KeyValue getKeyValue() {
        return new KeyValue(KEY, false, String.format("%s-%d", VALUE_PREFIX, KEY_START_VAL), "grouped elements are selected at once");
    }

    /**
     * trimming the group value, returns null when val is not valid
     */
    public static String getElementGroupValSafe(String arg_group) {
        if (arg_group == null || arg_group.length()==0) {
            return null;
        }
        String _res = arg_group.trim();
        if (_res.length() == 0) {
            return null;
        }
        return _res;
    }

    @Override
    public void handleValue(String value, PropertiesParserState state) {
        String safeValue = getElementGroupValSafe(value);
        if(safeValue==null || !value.equals(safeValue)){
            if(value.length()>0){
                throw new StyleException("Group value cannot have lead-tail whitespaces");
            }else{
                throw new StyleException("Group value cannot be empty");
            }
        }
        state.setFacetResponse(GroupFacet.class, value);
    }
}
