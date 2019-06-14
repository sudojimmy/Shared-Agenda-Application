package store;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import types.EventRepeat;

public class EventRepeatTypeCodec implements Codec<EventRepeat> {
    @Override
    public void encode(final BsonWriter writer, final EventRepeat value, final EncoderContext encoderContext) {
        writer.writeString(value.value());
    }

    @Override
    public EventRepeat decode(final BsonReader reader, final DecoderContext decoderContext) {
        return EventRepeat.fromValue(reader.readString());
    }

    @Override
    public Class<EventRepeat> getEncoderClass() {
        return EventRepeat.class;
    }
}
