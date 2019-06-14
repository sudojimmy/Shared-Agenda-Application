package store;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import types.EventType;

public class EventTypeTypeCodec implements Codec<EventType> {
    @Override
    public void encode(final BsonWriter writer, final EventType value, final EncoderContext encoderContext) {
        writer.writeString(value.value());
    }

    @Override
    public EventType decode(final BsonReader reader, final DecoderContext decoderContext) {
        return EventType.fromValue(reader.readString());
    }

    @Override
    public Class<EventType> getEncoderClass() {
        return EventType.class;
    }
}