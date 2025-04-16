package io.cdap.wrangler.api.parser;


import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.cdap.wrangler.api.parser.Token;
import io.cdap.wrangler.api.parser.TokenType;

public class Bytesize implements Token {
    private final double value;
    private final String unit;
    private final String raw;

    public Bytesize(String raw) {
        this.raw = raw.trim().toUpperCase();
        String numPart = this.raw.replaceAll("[^0-9.]", "");
        this.value = Double.parseDouble(numPart);
        this.unit = this.raw.replaceAll("[0-9.]", "");
    }

    public long getBytes() {
        switch (unit) {
            case "B": return (long) value;
            case "KB": return (long) (value * 1024);
            case "MB": return (long) (value * 1024 * 1024);
            case "GB": return (long) (value * 1024 * 1024 * 1024);
            case "TB": return (long) (value * 1024L * 1024 * 1024 * 1024);
            default: throw new IllegalArgumentException("Unknown unit: " + unit);
        }
    }

    @Override
    public String value() {
        return this.value + this.unit;
    }

    @Override
    public TokenType type() {
        return TokenType.BYTESIZE;
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
