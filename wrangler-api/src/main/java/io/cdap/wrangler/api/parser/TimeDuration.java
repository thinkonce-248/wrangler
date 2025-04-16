
package io.cdap.wrangler.api.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.cdap.wrangler.api.parser.Token;
import io.cdap.wrangler.api.parser.TokenType;

public class TimeDuration implements Token {
    private final double value;
    private final String unit;
    private final String raw;

    public TimeDuration(String raw) {
        this.raw = raw.trim().toUpperCase();
        String numPart = this.raw.replaceAll("[^0-9.]", "");
        this.value = Double.parseDouble(numPart);
        this.unit = this.raw.replaceAll("[0-9.]", "");
    }

    public long getMilliseconds() {
        switch (unit) {
            case "MS": return (long) value;
            case "S": return (long) (value * 1000);
            case "M": return (long) (value * 60 * 1000);
            case "H": return (long) (value * 60 * 60 * 1000);
            case "D": return (long) (value * 24 * 60 * 60 * 1000);
            default: throw new IllegalArgumentException("Unknown unit: " + unit);
        }
    }

    @Override
    public String value() {
        return this.value + this.unit;
    }

    @Override
    public TokenType type() {
        return TokenType.TIMEDURATION;
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(this.value() + this.unit);
    }

    @Override
    public String getRaw() {
        return this.raw;
    }
}
