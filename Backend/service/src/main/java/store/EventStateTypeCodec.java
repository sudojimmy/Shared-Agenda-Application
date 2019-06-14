package store;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import types.EventState;

public class EventStateTypeCodec implements Codec<EventState> {
    @Override
    public void encode(final BsonWriter writer, final EventState value, final EncoderContext encoderContext) {
        writer.writeString(value.value());
    }

    @Override
    public EventState decode(final BsonReader reader, final DecoderContext decoderContext) {
        return EventState.fromValue(reader.readString());
    }

    @Override
    public Class<EventState> getEncoderClass() {
        return EventState.class;
    }
}