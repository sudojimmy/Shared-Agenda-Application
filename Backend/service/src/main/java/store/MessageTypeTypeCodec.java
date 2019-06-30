package store;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import types.MessageType;

public class MessageTypeTypeCodec implements Codec<MessageType> {
    @Override
    public void encode(final BsonWriter writer, final MessageType value, final EncoderContext encoderContext) {
        writer.writeString(value.value());
    }

    @Override
    public MessageType decode(final BsonReader reader, final DecoderContext decoderContext) {
        return MessageType.fromValue(reader.readString());
    }

    @Override
    public Class<MessageType> getEncoderClass() {
        return MessageType.class;
    }
}
