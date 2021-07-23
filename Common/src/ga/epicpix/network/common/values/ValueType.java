package ga.epicpix.network.common.values;

import com.google.gson.JsonObject;

/**
 * Types
 *
 * 0 - String
 * 1 - Boolean
 * 2 - Byte
 * 3 - Short
 * 4 - Integer
 * 5 - Long
 * 6 - Float
 * 7 - Double
 */
public final class ValueType {

    private final Object value;
    private final int type;

    public ValueType(Object value) {
        this.value = value;
        type = getTypeFromObject(value);
    }

    public static int getTypeFromObject(Object value) {
        if(value instanceof String) return 0;
        else if(value instanceof Boolean) return 1;
        else if(value instanceof Byte) return 2;
        else if(value instanceof Short) return 3;
        else if(value instanceof Integer) return 4;
        else if(value instanceof Long) return 5;
        else if(value instanceof Float) return 6;
        else if(value instanceof Double) return 7;
        return -1;
    }

    public static ValueType getValueTypeFromJson(JsonObject obj) {
        int type = obj.get("type").getAsInt();
        Object o = null;
        if(type==0) o = obj.get("value").getAsString();
        else if(type==1) o = obj.get("value").getAsBoolean();
        else if(type==2) o = obj.get("value").getAsByte();
        else if(type==3) o = obj.get("value").getAsShort();
        else if(type==4) o = obj.get("value").getAsInt();
        else if(type==5) o = obj.get("value").getAsLong();
        else if(type==6) o = obj.get("value").getAsFloat();
        else if(type==7) o = obj.get("value").getAsDouble();
        return new ValueType(o);
    }

    public static JsonObject getJsonFromValueType(ValueType vt) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", vt.getType());
        int type = vt.getType();
        if(type==0) obj.addProperty("value", vt.getAsString());
        else if(type==1) obj.addProperty("value", vt.getAsBoolean());
        else if(type==2) obj.addProperty("value", vt.getAsByte());
        else if(type==3) obj.addProperty("value", vt.getAsShort());
        else if(type==4) obj.addProperty("value", vt.getAsInt());
        else if(type==5) obj.addProperty("value", vt.getAsLong());
        else if(type==6) obj.addProperty("value", vt.getAsFloat());
        else if(type==7) obj.addProperty("value", vt.getAsDouble());
        return obj;
    }

    public int getType() {
        return type;
    }

    public Boolean getAsBoolean() {
        return (Boolean) value;
    }

    public Byte getAsByte() {
        return (Byte) value;
    }

    public Short getAsShort() {
        return (Short) value;
    }

    public Integer getAsInt() {
        return (Integer) value;
    }

    public Long getAsLong() {
        return (Long) value;
    }

    public Float getAsFloat() {
        return (Float) value;
    }

    public Double getAsDouble() {
        return (Double) value;
    }

    public String getAsString() {
        return (String) value;
    }

    public String toString() {
        return value==null?"null":value.toString();
    }
}