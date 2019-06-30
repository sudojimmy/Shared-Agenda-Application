package store;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import types.ReplyStatus;

public class ReplyStatusTypeCodec implements Codec<ReplyStatus> {
    @Override
    public void encode(final BsonWriter writer, final ReplyStatus value, final EncoderContext encoderContext) {
        writer.writeString(value.value());
    }

    @Override
    public ReplyStatus decode(final BsonReader reader, final DecoderContext decoderContext) {
        return ReplyStatus.fromValue(reader.readString());
    }

    @Override
    public Class<ReplyStatus> getEncoderClass() {
        return ReplyStatus.class;
    }
}
