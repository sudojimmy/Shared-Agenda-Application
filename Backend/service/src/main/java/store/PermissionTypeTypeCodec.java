package store;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import types.Permission;
import types.PermissionType;

public class PermissionTypeTypeCodec implements Codec<PermissionType> {
    @Override
    public void encode(final BsonWriter writer, final PermissionType value, final EncoderContext encoderContext) {
        writer.writeString(value.value());
    }

    @Override
    public PermissionType decode(final BsonReader reader, final DecoderContext decoderContext) {
        return PermissionType.fromValue(reader.readString());
    }

    @Override
    public Class<PermissionType> getEncoderClass() {
        return PermissionType.class;
    }
}
